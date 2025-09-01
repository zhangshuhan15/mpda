package com.dahuaboke.mpda.bot.scenes.product.marketRanking;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingAgentPrompt extends AbstractProductAgentPrompt {

    public MarketRankingAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户的对话内容，必须调用以下工具：
                                查询产品市场排名
                        """
                , "tool", """
                            输出结尾需要添加以下内容：
                                对话框内为您展示前十名产品详情，全市场排名情况您可以在Excel文件中查看。如需其他指标，您可以直接告诉邮小金。
                                Excel链接：https://www.psbc.com/excel
                            用md格式返回。
                        """);
        this.description = promptMap.get("guide");
    }
}
