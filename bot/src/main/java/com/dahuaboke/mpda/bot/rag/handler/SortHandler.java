package com.dahuaboke.mpda.bot.rag.handler;


import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * auth: dahua
 * time: 2025/9/1 10:26
 */
@Component
public class SortHandler {

    public List<Document> handler(List<Document> topDocs) {
        Map<String, List<Document>> groupByFileName =
                topDocs.stream().collect(Collectors.groupingBy(doc -> (String) doc.getMetadata().get("file_name")));
        HashMap<String, List<Document>> sortedGroups = new HashMap<>();
        groupByFileName.forEach((filename, docs) -> {
            List<Document> sortedDocs = docs.stream().sorted(Comparator.comparingDouble(doc -> (Double) doc.getMetadata().get("page_number")))
                    .collect(Collectors.toList());
            sortedGroups.put(filename, sortedDocs);
        });
        return sortedGroups.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
