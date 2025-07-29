package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;

public class NodeDispatcher implements EdgeAction {

    @Override
    public String apply(OverAllState state) {
        return "go_human";
    }
}
