package com.dahuaboke.mpda.agent.scene;

import com.dahuaboke.mpda.agent.graph.Graph;
import com.dahuaboke.mpda.agent.prompt.Prompt;

/**
 * auth: dahua
 * time: 2025/08/21 8:50
 */
public interface Scene {

    String description();

    Graph graph();

    Prompt prompt();
}
