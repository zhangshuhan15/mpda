package com.dahuaboke.mpda.client.entity.resp;

import java.util.List;

/**
 * @Desc: 通用数据批量写入-C014001 返回体
 * @Author：zhh
 * @Date：2025/9/1 10:10
 */
public class C014001Resp extends BaseResp{

    /**
     * 成功清单,返回生成的id
     */
    private List<String> successIdList;

    /**
     * 失败插入清单
     */
    private List<String> failedInsertList;

    /**
     * 个数
     */
    private int rowCount;


    public List<String> getSuccessIdList() {
        return successIdList;
    }

    public void setSuccessIdList(List<String> successIdList) {
        this.successIdList = successIdList;
    }

    public List<String> getFailedInsertList() {
        return failedInsertList;
    }

    public void setFailedInsertList(List<String> failedInsertList) {
        this.failedInsertList = failedInsertList;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

}
