package com.dahuaboke.mpda.bot.scenes;


import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionAgentPrompt;
import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.agent.scene.SceneWrapper;
import com.dahuaboke.mpda.core.trace.TraceManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

import static com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene.BEAN_NAME;

/**
 * auth: dahua
 * time: 2025/8/21 14:41
 */
@Component
public class SceneManager implements BeanPostProcessor {

    private final Map<String, String> sceneDependencies = new HashMap<>();
    private final Map<String, SceneWrapper> sceneWrappers = new HashMap<>();
    private TraceManager traceManager;
    private Scene preScene;
    private SceneWrapper preSceneWrapper;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Scene scene) {
            if (beanName.equals(BEAN_NAME)) {
                preScene = scene;
            } else {
                SceneWrapper sceneWrapper = buildWrapper(scene);
                String sceneId = sceneWrapper.getSceneId();
                sceneWrappers.put(sceneId, buildWrapper(scene));
                sceneDependencies.put(sceneId, scene.description());
            }
        }
        if (bean instanceof TraceManager traceManager) {
            this.traceManager = traceManager;
        }
        return bean;
    }

    private SceneWrapper buildWrapper(Scene scene) {
        DefaultChain chain = DefaultChain.builder()
                .graph(scene.graph())
                .prompt(scene.prompt())
                .traceManager(traceManager)
                .build();
        SceneWrapper wrapper = SceneWrapper.builder().chain(chain).traceManager(traceManager).build();
        try {
            wrapper.init();
        } catch (MpdaGraphException e) {
            e.printStackTrace(); //TODO
        }
        return wrapper;
    }

    public Flux<String> apply(String conversationId, String query) throws MpdaException {
        if (preSceneWrapper == null) {
            ResolutionAgentPrompt prompt = (ResolutionAgentPrompt) preScene.prompt();
            prompt.buildPrompt(sceneDependencies);
            preSceneWrapper = buildWrapper(preScene);
        }
        String apply = preSceneWrapper.execute(conversationId, query);
        if (apply.startsWith("<think>")) {
            apply = apply.replaceFirst("(?s)<think>.*?</think>", "");
        }
        SceneWrapper sceneWrapper = sceneWrappers.get(apply.trim());
        if (sceneWrapper == null) {
            throw new MpdaException(apply); //TODO
        }
        return sceneWrapper.executeAsync(conversationId, query);
    }
}
