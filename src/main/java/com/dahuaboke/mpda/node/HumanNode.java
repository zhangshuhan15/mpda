package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

import java.util.Map;

public class HumanNode implements NodeAction {

    @Override
    public Map<String, Object> apply(OverAllState state) {
        return Map.of();
    }
}
