package com.dahuaboke.mpda.tools;

import com.dahuaboke.mpda.tools.ToolResult;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommandTool {

    @Tool(description = "在本地路径执行命令" )
    public ToolResult runCommand(@ToolParam(description = "指令内容") String command,
                                 @ToolParam(description = "本地文件路径,默认当前路径") String path) {
        try {
            File workingDir = new File(path);
            if (!workingDir.exists()) {
                workingDir.mkdirs();
            }

            Process process = Runtime.getRuntime().exec(command, null, workingDir);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            Map<String, Object> data = new HashMap<>();
            data.put("command", command);
            data.put("output", output.toString());
            data.put("error", exitCode != 0 ? errorOutput.toString() : null);

            return new ToolResult(
                    exitCode == 0 ? "success" : "error",
                    exitCode == 0 ? "命令执行成功" : "命令执行失败",
                    data
            );
        } catch (Exception e) {
            return new ToolResult("error", "命令执行异常: " + e.getMessage(), null);
        }
    }
}
