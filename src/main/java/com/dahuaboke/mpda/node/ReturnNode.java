package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;

import java.util.Map;

public class ReturnNode implements NodeAction {

    @Override
    public Map<String, Object> apply(OverAllState state) {
        String h = state.value("r", String.class).get();
        return Map.of("h", "回复是 》》》 " + h);
    }
}
