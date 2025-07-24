package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.dahuaboke.mpda.context.StateGraphContext;

public class NodeDispatcher implements EdgeAction {

    private StateGraphContext stateGraphContext;

    public NodeDispatcher(StateGraphContext stateGraphContext) {
        this.stateGraphContext = stateGraphContext;
    }

    @Override
    public String apply(OverAllState state) {
        return "go_human";
    }
}
