package com.dahuaboke.mpda.tools.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.tools.product.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.tools.product.entity.YwjqrIntbankKindpara;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Title: 基金产品 mapper
 * @Description:
 * @author: 吕远征
 * @date: 2022/4/6 15:32
 */
@Mapper
public interface YwjqrIntbankKindparaMapper extends BaseMapper<YwjqrIntbankKindpara> {

    void deleteAll();

    List<YwjqrIntbankKindpara> selectListFilter(FilterProdInfoReq filterProdInfoReq);

}