package com.dahuaboke.mpda.agent.chain;


import com.dahuaboke.mpda.agent.exception.MpdaException;
import com.dahuaboke.mpda.agent.exception.MpdaGraphException;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 08:55
 */
public interface Chain {

    void init() throws MpdaGraphException;

    String slide(String query) throws MpdaException;

    Flux<String> slideAsync(String query) throws MpdaException;
}
