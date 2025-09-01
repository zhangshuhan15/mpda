package com.dahuaboke.mpda.bot.tools.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.bot.tools.dto.FilterProdInfoReq;
import com.dahuaboke.mpda.bot.tools.entity.YwjqrIntbankKindpara;
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