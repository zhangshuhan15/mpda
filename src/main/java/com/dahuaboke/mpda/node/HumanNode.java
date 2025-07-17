package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HumanNode implements NodeAction {

    @Override
    public Map<String, Object> apply(OverAllState state) {
        String h = state.value("l", String.class).get();
        return Map.of("h", h);
    }
}
