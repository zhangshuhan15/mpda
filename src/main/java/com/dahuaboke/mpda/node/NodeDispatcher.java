package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import org.springframework.stereotype.Component;

@Component
public class NodeDispatcher implements EdgeAction {

    private int a = 0;

    @Override
    public String apply(OverAllState state) throws Exception {
        System.out.println(state.toString());
        a++;
        if (a%2 == 0) {
            return "go_tool";
        }
        return "go_human";
    }
}
