package com.dahuaboke.mpda.tools.product;


import com.dahuaboke.mpda.tools.product.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.tools.product.dto.NetValReq;
import com.dahuaboke.mpda.tools.product.dto.ProdInfoDto;
import com.dahuaboke.mpda.tools.product.entity.YwjqrProduct;
import com.dahuaboke.mpda.tools.product.service.RobotService;
import com.dahuaboke.mpda.utils.DateUtil;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * auth: dahua
 * time: 2025/8/26 17:06
 */
public class ProductToolHandler {

    @Autowired
    private RobotService robotService;

    /**
     * 产品信息入库
     *
     * @param ywjqrProduct
     * @return
     */
    @RequestMapping(value = "/insert/fundProduct")
    public String fundProduct(YwjqrProduct ywjqrProduct) {
        int insert = robotService.insert(ywjqrProduct);
        return "" + insert;
    }

    /**
     * 根据基金代码更新产品信息
     *
     * @param ywjqrProduct
     * @return
     */
    @RequestMapping(value = "/update/fundProduct")
    public String updateFundProduct(YwjqrProduct ywjqrProduct) {
        int update = robotService.update(ywjqrProduct);
        return "" + update;
    }

    /**
     * 根据基金代码查询产品信息
     *
     * @param ywjqrProduct
     * @return
     */
    @RequestMapping(value = "/select/fundCode")
    public List<YwjqrProduct> selectByFundCode(YwjqrProduct ywjqrProduct) {
        List<YwjqrProduct> ywjqrProducts = robotService.selectList(ywjqrProduct);
        return ywjqrProducts;
    }

    /**
     * 最大回撤
     *
     * @param netValReq
     * @return
     */
    @RequestMapping("/select/maxNetval")
    public String maxNetval(NetValReq netValReq) {
        if (StringUtils.isEmpty(netValReq.getEndDate())) {
            netValReq.setEndDate(DateUtil.getBusinessToday());
        }
        if (StringUtils.isEmpty(netValReq.getBegDate())) {
            String txDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -90);
            netValReq.setBegDate(txDate);
        }
        return robotService.maxNetval(netValReq);
    }

    /**
     * 查产品信息
     *
     * @param netValReq
     * @return ProdInfoDto
     */
    @RequestMapping("/select/prodInfo")
    public ProdInfoDto selectProdInfo(NetValReq netValReq) {
        return robotService.selectProdInfo(netValReq);
    }

    /**
     * 映射产品代码与名称
     *
     * @return Map
     */
    @GetMapping("/select/prodNoName")
    public Map<String, String> getMap() {
        return robotService.getMap();
    }

    /**
     * 根据输入条件筛选产品
     *
     * @param filterProdInfoReq
     * @return
     */
    @RequestMapping("/select/filterProdInfo")
    public List<ProdInfoDto> filterProdInfo(FilterProdInfoReq filterProdInfoReq) {
        return robotService.filterProdInfo(filterProdInfoReq);
    }

    /**
     * 年收益率
     *
     * @param netValReq
     * @return
     */
    @RequestMapping(value = "/select/yearRita")
    public String yearRita(NetValReq netValReq) {
        if (StringUtils.isEmpty(netValReq.getEndDate())) {
            netValReq.setEndDate(DateUtil.getBusinessToday());
        }
        if (StringUtils.isEmpty(netValReq.getBegDate())) {
            String txDate = DateUtil.getDayBy(DateUtil.getBusinessToday(), 0, 0, -30);
            netValReq.setBegDate(txDate);
        }
        return robotService.yearRita(netValReq);
    }

}
