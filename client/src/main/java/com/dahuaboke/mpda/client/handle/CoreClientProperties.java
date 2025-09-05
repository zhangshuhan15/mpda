package com.dahuaboke.mpda.client.handle;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Desc: 新核心客户端配置类
 * @Author：zhh
 * @Date：2025/9/5 10:37
 */
@Component
@ConfigurationProperties(prefix = "core.client")
public class CoreClientProperties {

    // 发送方系统号
    private String sendSysNo;

    // 接收方系统号
    private String targetSysNo;

    public String getSendSysNo() {
        return sendSysNo;
    }

    public void setSendSysNo(String sendSysNo) {
        this.sendSysNo = sendSysNo;
    }

    public String getTargetSysNo() {
        return targetSysNo;
    }

    public void setTargetSysNo(String targetSysNo) {
        this.targetSysNo = targetSysNo;
    }
}
