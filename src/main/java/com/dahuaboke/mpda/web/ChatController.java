package com.dahuaboke.mpda.web;

import com.dahuaboke.mpda.agent.exception.MpdaException;
import com.dahuaboke.mpda.web.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/stream")
    public Flux<ServerSentEvent<String>> chat(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException {
        Flux<String> response = chatService.chat(conversationId, q);
        return response.map(res -> {
            Map<String, Object> delta = Map.of("role", "assistant", "content", res);
            Map<String, Object> choice = Map.of("index", 0, "delta", delta, "finish_reason", "");
            List<Map<String, Object>> choices = List.of(choice);
            try {
                String jsonData = objectMapper.writeValueAsString(Map.of("choices", choices));
                return ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event("message")
                        .data(jsonData)
                        .build();
            } catch (JsonProcessingException e) {
                return ServerSentEvent.<String>builder().build();
            }
        });
    }
}
