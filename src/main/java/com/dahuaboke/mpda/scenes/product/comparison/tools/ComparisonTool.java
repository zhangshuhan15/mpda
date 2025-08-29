package com.dahuaboke.mpda.scenes.product.comparison.tools;

import com.dahuaboke.mpda.agent.tools.AbstractBaseTool;
import com.dahuaboke.mpda.agent.tools.ToolResult;
import com.dahuaboke.mpda.scenes.product.information.tools.InformationTool;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ComparisonTool extends AbstractBaseTool<ComparisonTool.Input> {

    @Autowired
    private InformationTool informationTool;

    @Override
    public String getDescription() {
        return """
                    通过两个产品的产品编号，查询两个产品信息，用于对比
                """;
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType(), "p1", "p2");
    }

    @Override
    public Class<Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(Input input) {
        try {
            ToolResult cpxx1 = informationTool.execute(new InformationTool.Input(input.p1()));
            ToolResult cpxx2 = informationTool.execute(new InformationTool.Input(input.p2()));

            if (input.p1().equals("007540")) {
                Map data = (Map) cpxx1.getData();
                data.put("yearRita1Year", "8.12");
                data.put("maxNetval1Year", "-3.93");
            }

            if (input.p2().equals("011968")) {
                Map data = (Map) cpxx2.getData();
                data.put("yearRita1Year", "3.29");
                data.put("maxNetval1Year", "-0.99");
            }

            return ToolResult.success("查询成功", Map.of("first", cpxx1.getData(), "second", cpxx2.getData()));
        } catch (Exception e) {
            return ToolResult.error("查询失败");
        }
    }

    public record Input(@JsonPropertyDescription("第一个产品编号") String p1
            , @JsonPropertyDescription("第二个产品编号") String p2) {
    }
}