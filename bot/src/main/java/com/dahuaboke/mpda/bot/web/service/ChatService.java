package com.dahuaboke.mpda.bot.web.service;

import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import com.dahuaboke.mpda.core.agent.scene.SceneManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 14:34
 */
@Service
public class ChatService {

    @Autowired
    private SceneManager sceneManager;

    public Flux<String> chat(String conversationId, String query) throws MpdaException {
        return sceneManager.apply(conversationId, query);
    }
}
