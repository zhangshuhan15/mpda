package com.dahuaboke.mpda.client.entity.resp;

import java.util.List;

/**
 * @Desc: 通用数据普通查询-C014005 返回体
 * @Author：zhh
 * @Date：2025/9/9 10:10
 */
public class C014005Resp<R> extends BaseResp {

    /**
     * 返回对象集合
     */
    private List<R> resultMap;

    public List<R> getResultMap() {
        return resultMap;
    }

    public void setResultMap(List<R> resultMap) {
        this.resultMap = resultMap;
    }

}
