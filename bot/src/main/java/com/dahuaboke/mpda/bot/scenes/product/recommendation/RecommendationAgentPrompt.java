package com.dahuaboke.mpda.bot.scenes.product.recommendation;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:47
 */
@Component
public class RecommendationAgentPrompt extends AbstractProductAgentPrompt {

    public RecommendationAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析历史对话和用户问题，需要收集以下信息：
                                （1）用户的资金规模
                                （2）咨询产品的类型
                                （3）咨询产品的收益
                                （4）投资期限
                                （5）产品的债基分类
                                （6）产品的最大回撤
                                （7）货币基金配置比例
                                （8）债券基金配置比例
                            2.用户的对话是多轮的，你需要引导用户描述未收集到的信息
                            3.如果上述信息之前对话已经收集过的则复用历史信息
                            4.必须全部收集完成，然后调用以下工具，否则需要继续引导：
                                 根据年化利率/基金类型/债券基金类型/月最大回撤率来查询匹配的产品信息
                        """
                , "tool", """
                            如果字段内容不是md格式需要用md格式美化下输出结果。
                        """);
        this.description = promptMap.get("guide");
    }
}
