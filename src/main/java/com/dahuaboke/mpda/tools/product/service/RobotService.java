package com.dahuaboke.mpda.tools.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.tools.product.dao.FundNetvalueMapper;
import com.dahuaboke.mpda.tools.product.dao.FundProductMapper;
import com.dahuaboke.mpda.tools.product.dao.YwjqrIntbankKindparaMapper;
import com.dahuaboke.mpda.tools.product.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.tools.product.dto.NetValReq;
import com.dahuaboke.mpda.tools.product.dto.ProdInfoDto;
import com.dahuaboke.mpda.tools.product.entity.YwjqrIntbankKindpara;
import com.dahuaboke.mpda.tools.product.entity.YwjqrNetvalue;
import com.dahuaboke.mpda.tools.product.entity.YwjqrProduct;
import com.dahuaboke.mpda.utils.DateUtil;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class RobotService {

    @Autowired
    private FundProductMapper fundProductMapper;

    @Autowired
    private FundNetvalueMapper fundNetvalueMapper;

    @Autowired
    private YwjqrIntbankKindparaMapper ywjqrIntbankKindparaMapper;

    public int insert(YwjqrProduct ywjqrProduct) {
        return fundProductMapper.insert(ywjqrProduct);
    }

    public int update(YwjqrProduct ywjqrProduct) {
        LambdaQueryWrapper<YwjqrProduct> queryWrapper = new LambdaQueryWrapper<YwjqrProduct>()
                .eq(YwjqrProduct::getFundCode, ywjqrProduct.getFundCode());
        int update = fundProductMapper.update(ywjqrProduct, queryWrapper);
        return update;
    }


    public List<YwjqrProduct> selectList(YwjqrProduct ywjqrProduct) {
        LambdaQueryWrapper<YwjqrProduct> queryWrapper = new LambdaQueryWrapper<YwjqrProduct>()
                .eq(YwjqrProduct::getFundCode, ywjqrProduct.getFundCode());
        List<YwjqrProduct> ywjqrProducts = fundProductMapper.selectList(queryWrapper);
        return ywjqrProducts;
    }

    public String maxNetval(NetValReq netValReq) {
        List<YwjqrNetvalue> ywjqrNetvalues = fundNetvalueMapper.selectList(new LambdaQueryWrapper<YwjqrNetvalue>()
                .eq(YwjqrNetvalue::getProdtCode, netValReq.getProdtCode())
                .between(YwjqrNetvalue::getNetValDate, netValReq.getBegDate(), netValReq.getEndDate())
                .orderByAsc(YwjqrNetvalue::getNetValDate));
        if (ywjqrNetvalues.size() == 0) {
            return "" + 0;
        }
        String maxNetVal = ywjqrNetvalues.get(0).getUnitNetVal();
        String ans = "0";
        for (YwjqrNetvalue fund : ywjqrNetvalues) {
            if (fund.getUnitNetVal().compareTo(maxNetVal) > 0) {
                maxNetVal = fund.getUnitNetVal();
            } else {
                BigDecimal bigDecimal1 = new BigDecimal(maxNetVal);
                BigDecimal bigDecimal2 = new BigDecimal(fund.getUnitNetVal());
                BigDecimal divide = bigDecimal1.subtract(bigDecimal2).divide(bigDecimal1, new MathContext(7, RoundingMode.HALF_UP));
                String tempAns = divide.multiply(new BigDecimal(100), new MathContext(5, RoundingMode.HALF_UP)).toString();
                ans = ans.compareTo(tempAns) > 0 ? ans : tempAns;
            }
        }
        return ans + "%";
    }

    public ProdInfoDto selectProdInfo(NetValReq netValReq) {
        ProdInfoDto prodInfoDto = new ProdInfoDto();
        LambdaQueryWrapper<YwjqrProduct> queryWrapper1 = new LambdaQueryWrapper<YwjqrProduct>()
                .eq(YwjqrProduct::getFundCode, netValReq.getProdtCode());
        List<YwjqrProduct> ywjqrProducts = fundProductMapper.selectList(queryWrapper1);
        YwjqrProduct product = new YwjqrProduct();
        if (ywjqrProducts.size() > 0) {
            product = ywjqrProducts.get(0);
        }
        LambdaQueryWrapper<YwjqrIntbankKindpara> queryWrapper2 = new LambdaQueryWrapper<YwjqrIntbankKindpara>()
                .eq(YwjqrIntbankKindpara::getFundProdtNo, netValReq.getProdtCode());
        List<YwjqrIntbankKindpara> ywjqrIntbankKindparas = ywjqrIntbankKindparaMapper.selectList(queryWrapper2);
        YwjqrIntbankKindpara ywjqrIntbankKindpara = new YwjqrIntbankKindpara();
        if (ywjqrIntbankKindparas.size() > 0) {
            ywjqrIntbankKindpara = ywjqrIntbankKindparas.get(0);
        }
        BeanUtils.copyProperties(product, prodInfoDto);
        BeanUtils.copyProperties(ywjqrIntbankKindpara, prodInfoDto);
        return prodInfoDto;
    }


    public Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        List<YwjqrIntbankKindpara> ywjqrIntbankKindparas = ywjqrIntbankKindparaMapper.selectList(new LambdaQueryWrapper<YwjqrIntbankKindpara>());
        for (YwjqrIntbankKindpara ywjqrIntbankKindpara : ywjqrIntbankKindparas) {
            map.put(ywjqrIntbankKindpara.getFundProdtNo(), ywjqrIntbankKindpara.getProdtName());
        }
        return map;
    }

    public List<ProdInfoDto> filterProdInfo(FilterProdInfoReq filterProdInfoReq) {
        List<ProdInfoDto> list = new ArrayList<>();
        List<YwjqrIntbankKindpara> ywjqrIntbankKindparas = ywjqrIntbankKindparaMapper.selectListFilter(filterProdInfoReq);
        for (YwjqrIntbankKindpara ywjqrIntbankKindpara : ywjqrIntbankKindparas) {
            String fundProdtNo = ywjqrIntbankKindpara.getFundProdtNo();
            String withDrawal = getWithDrawal(fundProdtNo);
            String yearRita = getYearRita(fundProdtNo);
            if (StringUtils.isEmpty(filterProdInfoReq.getWithDrawal()) && StringUtils.isEmpty(filterProdInfoReq.getYearRita())) {
                ProdInfoDto info = getInfo(ywjqrIntbankKindpara.getFundProdtNo());
                list.add(info);
                continue;
            }
            if (!StringUtils.isEmpty(filterProdInfoReq.getWithDrawal()) && !StringUtils.isEmpty(filterProdInfoReq.getYearRita())) {
                if (filterProdInfoReq.getWithDrawal().contains("%"))
                    filterProdInfoReq.setWithDrawal(filterProdInfoReq.getWithDrawal().replace("%", ""));
                if (filterProdInfoReq.getYearRita().contains("%"))
                    filterProdInfoReq.setYearRita(filterProdInfoReq.getYearRita().replace("%", ""));
                BigDecimal bigDecimal1 = new BigDecimal(filterProdInfoReq.getWithDrawal()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal2 = new BigDecimal(filterProdInfoReq.getYearRita()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal3 = new BigDecimal(withDrawal);
                BigDecimal bigDecimal4 = new BigDecimal(yearRita);
                //1<3返回负数
                if (bigDecimal1.compareTo(bigDecimal3) > 0 && bigDecimal2.compareTo(bigDecimal4) < 0) {
                    ProdInfoDto info = getInfo(ywjqrIntbankKindpara.getFundProdtNo());
                    list.add(info);

                }
                continue;
            }
            if (!StringUtils.isEmpty(filterProdInfoReq.getWithDrawal())) {
                if (filterProdInfoReq.getWithDrawal().contains("%"))
                    filterProdInfoReq.setWithDrawal(filterProdInfoReq.getWithDrawal().replace("%", ""));
                BigDecimal bigDecimal1 = new BigDecimal(filterProdInfoReq.getWithDrawal()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal3 = new BigDecimal(withDrawal);
                if (bigDecimal1.compareTo(bigDecimal3) > 0) {
                    ProdInfoDto info = getInfo(ywjqrIntbankKindpara.getFundProdtNo());
                    list.add(info);

                }
                continue;
            }
            if (!StringUtils.isEmpty(filterProdInfoReq.getYearRita())) {
                if (filterProdInfoReq.getYearRita().contains("%"))
                    filterProdInfoReq.setYearRita(filterProdInfoReq.getYearRita().replace("%", ""));
                BigDecimal bigDecimal2 = new BigDecimal(filterProdInfoReq.getYearRita()).divide(BigDecimal.valueOf(100), new MathContext(5, RoundingMode.HALF_UP));
                BigDecimal bigDecimal4 = new BigDecimal(yearRita);
                if (bigDecimal2.compareTo(bigDecimal4) < 0) {
                    ProdInfoDto info = getInfo(ywjqrIntbankKindpara.getFundProdtNo());
                    list.add(info);
                }
            }
        }
        return list;
    }

    public String yearRita(NetValReq netValReq) {
        if (StringUtils.isEmpty(netValReq.getEndDate())) {
            netValReq.setEndDate(DateUtil.getBusinessToday());
        }
        if (StringUtils.isEmpty(netValReq.getBegDate())) {
            String txDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -30);
            netValReq.setBegDate(txDate);
        }
        List<YwjqrNetvalue> ywjqrNetvalues = fundNetvalueMapper.selectList(new LambdaQueryWrapper<YwjqrNetvalue>()
                .eq(YwjqrNetvalue::getProdtCode, netValReq.getProdtCode())
                .between(YwjqrNetvalue::getNetValDate, netValReq.getBegDate(), netValReq.getEndDate())
                .orderByAsc(YwjqrNetvalue::getNetValDate));
        if (ywjqrNetvalues.size() == 0) {
            return "" + 0;
        }
        String one = ywjqrNetvalues.get(0).getUnitNetVal();
        String last = ywjqrNetvalues.get(ywjqrNetvalues.size() - 1).getUnitNetVal();
        BigDecimal bigDecimal1 = new BigDecimal(one);
        BigDecimal bigDecimal2 = new BigDecimal(last);
        BigDecimal divide = bigDecimal2.subtract(bigDecimal1).divide(bigDecimal1, new MathContext(7, RoundingMode.HALF_UP));
        String tempAns = divide.multiply(new BigDecimal(100), new MathContext(5, RoundingMode.HALF_UP)).toString();
        return tempAns + "%";
    }


    private String getWithDrawal(String fundProdtNo) {
        String endDate = DateUtil.getBusinessToday();

        String begDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -30);

        List<YwjqrNetvalue> ywjqrNetvalues = fundNetvalueMapper.selectList(new LambdaQueryWrapper<YwjqrNetvalue>()
                .eq(YwjqrNetvalue::getProdtCode, fundProdtNo)
                .between(YwjqrNetvalue::getNetValDate, begDate, endDate)
                .orderByAsc(YwjqrNetvalue::getNetValDate));
        if (ywjqrNetvalues.size() == 0) {
            return "" + 0;
        }
        String maxNetVal = ywjqrNetvalues.get(0).getUnitNetVal();
        String ans = "0";
        for (YwjqrNetvalue fund : ywjqrNetvalues) {
            if (fund.getUnitNetVal().compareTo(maxNetVal) > 0) {
                maxNetVal = fund.getUnitNetVal();
            } else {
                BigDecimal bigDecimal1 = new BigDecimal(maxNetVal);
                BigDecimal bigDecimal2 = new BigDecimal(fund.getUnitNetVal());
                BigDecimal divide = bigDecimal1.subtract(bigDecimal2).divide(bigDecimal1, new MathContext(5, RoundingMode.HALF_UP));
                ans = ans.compareTo(String.valueOf(divide)) > 0 ? ans : String.valueOf(divide);
            }
        }

        return ans;
    }

    private String getYearRita(String fundProdtNo) {
        String endDate = DateUtil.getBusinessToday();
        String begDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -30);
        List<YwjqrNetvalue> ywjqrNetvalues = fundNetvalueMapper.selectList(new LambdaQueryWrapper<YwjqrNetvalue>()
                .eq(YwjqrNetvalue::getProdtCode, fundProdtNo)
                .between(YwjqrNetvalue::getNetValDate, begDate, endDate)
                .orderByAsc(YwjqrNetvalue::getNetValDate));
        if (ywjqrNetvalues.size() == 0) {
            return "" + 0;
        }
        String one = ywjqrNetvalues.get(0).getUnitNetVal();
        String last = ywjqrNetvalues.get(ywjqrNetvalues.size() - 1).getUnitNetVal();
        BigDecimal bigDecimal1 = new BigDecimal(one);
        BigDecimal bigDecimal2 = new BigDecimal(last);
        BigDecimal divide = bigDecimal2.subtract(bigDecimal1).divide(bigDecimal1, new MathContext(5, RoundingMode.HALF_UP));
        return String.valueOf(divide);
    }

    public ProdInfoDto getInfo(String code) {
        ProdInfoDto prodInfoDto = new ProdInfoDto();
        LambdaQueryWrapper<YwjqrProduct> queryWrapper = new LambdaQueryWrapper<YwjqrProduct>()
                .eq(YwjqrProduct::getFundCode, code);
        List<YwjqrProduct> ywjqrProducts = fundProductMapper.selectList(queryWrapper);
        YwjqrProduct product = new YwjqrProduct();
        if (ywjqrProducts.size() > 0) {
            product = ywjqrProducts.get(0);
        }
        LambdaQueryWrapper<YwjqrIntbankKindpara> queryWrapper2 = new LambdaQueryWrapper<YwjqrIntbankKindpara>()
                .eq(YwjqrIntbankKindpara::getFundProdtNo, code);
        List<YwjqrIntbankKindpara> ywjqrIntbankKindparas = ywjqrIntbankKindparaMapper.selectList(queryWrapper2);
        YwjqrIntbankKindpara ywjqrIntbankKindpara = new YwjqrIntbankKindpara();
        if (ywjqrIntbankKindparas.size() > 0) {
            ywjqrIntbankKindpara = ywjqrIntbankKindparas.get(0);
        }
        BeanUtils.copyProperties(product, prodInfoDto);
        BeanUtils.copyProperties(ywjqrIntbankKindpara, prodInfoDto);
        return prodInfoDto;
    }

}
