package com.dahuaboke.mpda.ai_code.scenes.codeGenerate.tools;


import com.dahuaboke.mpda.core.agent.tools.AbstractBaseTool;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * auth: dahua
 * time: 2025/9/10 16:45
 */
@Component
public class CodeGenerateTool extends AbstractBaseTool<CodeGenerateTool.Input> {

    @Override
    public String getDescription() {
        return """
                    代码生成工具
                """;
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType(), "path", "content");
    }

    @Override
    public Class<Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(Input input) {
        System.out.println(input);
        String filePath = input.path();
        String content = input.content();
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filePath, true))) {
            printWriter.print(content);
            return ToolResult.success("文件生成成功");
        } catch (IOException e) {
            return ToolResult.error("文件生成失败");
        }
    }

    public record Input(@JsonPropertyDescription("文件路径（包含文件名称，使用系统绝对路径）") String path
            , @JsonPropertyDescription("文件内容") String content) {
    }
}
