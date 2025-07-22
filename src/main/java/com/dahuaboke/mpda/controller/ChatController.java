package com.dahuaboke.mpda.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {

    @Autowired
    private ObjectProvider<StateGraph> prototypeBeanProvider;

    @RequestMapping("chat")
    public String chat(@RequestParam("q") String q) throws GraphRunnerException, GraphStateException {
        CompiledGraph stateGraph = prototypeBeanProvider.getObject().compile();
        return stateGraph.invoke(Map.of("q", q)).get().value("h", String.class).get();
    }
}
