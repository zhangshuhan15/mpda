package com.dahuaboke.mpda.core.agent.chain;


import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.context.CoreContext;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 08:55
 */
public interface Chain {

    void init() throws MpdaGraphException;

    String slide(CoreContext context) throws MpdaException;

    Flux<String> slideAsync(CoreContext context) throws MpdaException;
}
