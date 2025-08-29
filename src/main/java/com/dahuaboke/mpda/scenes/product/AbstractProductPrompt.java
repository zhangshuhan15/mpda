package com.dahuaboke.mpda.scenes.product;


import com.dahuaboke.mpda.agent.prompt.Prompt;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:32
 */
public abstract class AbstractProductPrompt implements Prompt {

    protected Map<String, String> promptMap;
    protected String description;

    public void changePrompt(String key) {
        this.description = promptMap.get(key);
    }

    @Override
    public String description() {
        return this.description;
    }
}
