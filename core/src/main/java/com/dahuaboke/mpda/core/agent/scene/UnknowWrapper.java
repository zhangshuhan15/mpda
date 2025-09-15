package com.dahuaboke.mpda.core.agent.scene;


import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/9/12 15:36
 */
public class UnknowWrapper extends SceneWrapper {

    private static final String reply = """
            您的问题我还不懂，等我变聪明些就可以回答了。
            """;

    public UnknowWrapper() {
        super(null, null, null, null);
    }

    @Override
    public Flux<String> applyAsync(String conversationId, String query) throws MpdaException {
        return Flux.just(reply);
    }
}
