package com.dahuaboke.mpda.bot.scenes.product.comparison;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 09:13
 */
@Component
public class ComparisonAgentPrompt extends AbstractProductAgentPrompt {

    public ComparisonAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                            1.分析用户的对话内容，必须调用以下工具：
                                通过两个产品的产品编号，查询两个产品信息，用于对比
                            2.如果不满足工具调用条件，需要引导用户补充。
                        """
                , "tool", translate() + """
                            根据工具执行结果，返回以下信息的对比结果：
                                产品名称
                                产品代码
                                基金公司
                                产品类型
                                基金经理
                                基金规模（提取其中的数字信息）
                                近3月收益率
                                近3月最大回撤
                            需要完全引用字段的实际内容不要推测。
                            最后给出投资建议严格如下，不要自己发挥：
                                这两支产品的基金管理人和基金经理均相同，但是东方臻宝纯债主要投资长期债，收益和稳定性表现更突出，不涉及股票投资，专注于债券资产，回撤较小，适合风险偏好低、追求长期稳健增值的保守型投资者，其申购成本也略低，利于长期持有控制成本。东方臻裕债券A侧重投资短期债，策略灵活性更强，可参与衍生品投资，风险和收益波动略高于前者，适合能承受小幅波动、希望通过灵活策略捕捉市场机会的投资者，两支基金管理费率相同。
                            如果字段内容不是md格式需要用md格式美化下输出结果。
                        """);
        this.description = promptMap.get("guide");
    }
}
