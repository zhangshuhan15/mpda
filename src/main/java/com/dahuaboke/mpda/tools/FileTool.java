package com.dahuaboke.mpda.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileTool {

    @Tool(description = "创建文件并写入内容")
    public ToolResult createFile(@ToolParam(description = "本地文件路径,默认当前路径") String filePath,
                                 @ToolParam(description = "文件内容") String content) {

        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path, content.getBytes());

            Map<String, Object> data = new HashMap<>();
            data.put("file_path", path.toAbsolutePath().toString());

            return new ToolResult("success", "文件创建成功: " + filePath, data);
        } catch (IOException e) {
            return new ToolResult("error", "文件创建失败: " + e.getMessage(), null);
        }
    }

    public ToolResult uploadAndAnalyzeFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return new ToolResult("error", "文件不存在", null);
            }

            String fileName = path.getFileName().toString();
            long fileSize = Files.size(path);
            String fileExt = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

            String content;
            switch (fileExt) {
                case ".txt":
                    content = new String(Files.readAllBytes(path));
                    break;
                case ".pdf":
                    content = extractPdfContent(path);
                    break;
                case ".xlsx":
                case ".xls":
                    content = extractExcelContent(path);
                    break;
                case ".docx":
                    content = extractWordContent(path);
                    break;
                default:
                    return new ToolResult("error", "不支持的文件类型: " + fileExt, null);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("file_name", fileName);
            data.put("file_size", fileSize);
            data.put("content", content);

            return new ToolResult("success", "文件分析成功", data);
        } catch (Exception e) {
            return new ToolResult("error", "文件分析失败: " + e.getMessage(), null);
        }
    }

    private String extractPdfContent(Path path) throws IOException {
        // 实现PDF内容提取
        return "PDF content extraction not implemented";
    }

    private String extractExcelContent(Path path) throws IOException {
        // 实现Excel内容提取
        return "Excel content extraction not implemented";
    }

    private String extractWordContent(Path path) throws IOException {
        // 实现Word内容提取
        return "Word content extraction not implemented";
    }
}
