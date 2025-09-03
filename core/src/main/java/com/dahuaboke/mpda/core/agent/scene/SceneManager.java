package com.dahuaboke.mpda.core.agent.scene;


import com.dahuaboke.mpda.core.agent.chain.DefaultChain;
import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.agent.exception.MpdaIllegalConfigException;
import com.dahuaboke.mpda.core.trace.TraceManager;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 14:41
 */
@Component
public class SceneManager implements BeanPostProcessor {

    private SceneWrapper rootWrapper;
    private TraceManager traceManager;
    private List<Scene> scenes = new ArrayList<>();
    private Map<String, SceneWrapper> sceneWrappers = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Scene scene) {
            if (scene.parent() == null) {
                if (rootWrapper != null) {
                    throw new MpdaIllegalConfigException("Root scene only one");
                }
                rootWrapper = buildWrapper(scene);
                sceneWrappers.put(scene.getClass().getSimpleName(), rootWrapper);
            } else {
                scenes.add(scene);
                sceneWrappers.put(scene.getClass().getSimpleName(), buildWrapper(scene));
            }
        }
        if (bean instanceof TraceManager traceManager) {
            this.traceManager = traceManager;
        }
        return bean;
    }

    @PostConstruct
    public void init() {
        scenes.stream().forEach(scene -> {
            Class<? extends Scene> parent = scene.parent();
            Scene parentScene = SpringUtil.getBean(parent);
            SceneWrapper parentSceneWrapper = sceneWrappers.get(parentScene.getClass().getSimpleName());
            SceneWrapper childSceneWrapper = sceneWrappers.get(scene.getClass().getSimpleName());
            parentSceneWrapper.addChildWrapper(childSceneWrapper);
        });
    }

    private SceneWrapper buildWrapper(Scene scene) {
        DefaultChain chain = DefaultChain.builder()
                .graph(scene.graph())
                .prompt(scene.prompt())
                .traceManager(traceManager)
                .build();
        SceneWrapper wrapper = SceneWrapper.builder()
                .chain(chain)
                .traceManager(traceManager)
                .prompt(scene.prompt())
                .description(scene.description())
                .build();
        try {
            wrapper.init();
        } catch (MpdaGraphException e) {
            e.printStackTrace(); //TODO
        }
        return wrapper;
    }

    public Flux<String> apply(String conversationId, String query) throws MpdaException {
        SceneWrapper runtimeWrapper = rootWrapper;
        while (!runtimeWrapper.isEnd()) {
            runtimeWrapper = rootWrapper.next(conversationId, query);
        }
        return runtimeWrapper.apply(conversationId, query);
    }
}
