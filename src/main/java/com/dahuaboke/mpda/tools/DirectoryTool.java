package com.dahuaboke.mpda.tools;

import org.springframework.ai.tool.annotation.Tool;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DirectoryTool {

    @Tool(description = "获取指定目录结构")
    public ToolResult getDirectoryStructure(String path) {
        try {
            Path absPath = Paths.get(path).toAbsolutePath();
            Map<String, Object> data = new HashMap<>();
            data.put("path", absPath.toString());
            data.put("structure", scanDirectory(absPath.toFile(), 3, 0));
            data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

            return new ToolResult("success", "目录结构获取成功", data);
        } catch (Exception e) {
            return new ToolResult("error", "目录结构获取失败: " + e.getMessage(), null);
        }
    }

    private Map<String, Object> scanDirectory(File directory, int maxDepth, int currentDepth) {
        Map<String, Object> structure = new HashMap<>();
        structure.put("name", directory.getName());
        structure.put("type", "directory");
        structure.put("path", directory.getAbsolutePath());

        if (currentDepth >= maxDepth) {
            structure.put("note", "目录深度超过" + maxDepth + "层已折叠");
            return structure;
        }

        List<Map<String, Object>> items = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    items.add(scanDirectory(file, maxDepth, currentDepth + 1));
                } else {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("name", file.getName());
                    fileInfo.put("type", "file");
                    fileInfo.put("path", file.getAbsolutePath());
                    fileInfo.put("size", file.length());
                    int dotIndex = file.getName().lastIndexOf('.');
                    fileInfo.put("extension", dotIndex > 0 ? file.getName().substring(dotIndex).toLowerCase() : "");
                    items.add(fileInfo);
                }
            }
        }

        structure.put("items", items);
        return structure;
    }
}
