package com.dahuaboke.mpda.client;


import com.dahuaboke.mpda.client.entity.CommonResp;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.chat.observation.ChatModelObservationConvention;
import org.springframework.ai.chat.observation.ChatModelObservationDocumentation;
import org.springframework.ai.chat.observation.DefaultChatModelObservationConvention;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.common.OpenAiApiConstants;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * auth: dahua
 * time: 2025/9/1 11:32
 */
public class CustomChatModel implements ChatModel {

    private static final Logger logger = LoggerFactory.getLogger(CustomChatModel.class);
    private final ChatModelObservationConvention DEFAULT_OBSERVATION_CONVENTION = new DefaultChatModelObservationConvention();
    private final CustomClient customClient;
    private final CustomChatOptions customChatOptions;
    private final ObservationRegistry observationRegistry;
    private ChatModelObservationConvention observationConvention = DEFAULT_OBSERVATION_CONVENTION;

    public CustomChatModel(CustomChatOptions customChatOptions, ObservationRegistry observationRegistry) {
        this.customClient = new CustomClient(null, null);
        this.customChatOptions = customChatOptions;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        Prompt requestPrompt = buildRequestPrompt(prompt);
        return this.internalCall(requestPrompt, null);
    }

    public ChatResponse internalCall(Prompt prompt, ChatResponse previousChatResponse) {

        ChatModelObservationContext observationContext = ChatModelObservationContext.builder()
                .prompt(prompt)
                .provider(OpenAiApiConstants.PROVIDER_NAME)
                .build();

        ChatResponse response = ChatModelObservationDocumentation.CHAT_MODEL_OPERATION
                .observation(this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
                        this.observationRegistry)
                .observe(() -> {

                    CommonResp execute = customClient.execute("", null);

                    if (execute == null) {
                        logger.warn("No chat completion returned for prompt: {}", prompt);
                        return new ChatResponse(List.of());
                    }

                    List<Generation> generations = List.of(new Generation(new AssistantMessage(""))); // TODO 替换为wrapper包装类
                    ChatResponse chatResponse = new ChatResponse(generations);

                    observationContext.setResponse(chatResponse);

                    return chatResponse;

                });
        return response;
    }

    @Override
    public Flux<ChatResponse> stream(Prompt prompt) {
        Prompt requestPrompt = buildRequestPrompt(prompt);
        return internalStream(requestPrompt, null);
    }

    public Flux<ChatResponse> internalStream(Prompt prompt, ChatResponse previousChatResponse) {
        return Flux.deferContextual(contextView -> {

            Flux<CommonResp> executeFlux = customClient.executeStream("", null);

            // For chunked responses, only the first chunk contains the choice role.
            // The rest of the chunks with same ID share the same role.
            ConcurrentHashMap<String, String> roleMap = new ConcurrentHashMap<>();

            final ChatModelObservationContext observationContext = ChatModelObservationContext.builder()
                    .prompt(prompt)
                    .provider(OpenAiApiConstants.PROVIDER_NAME)
                    .build();

            Observation observation = ChatModelObservationDocumentation.CHAT_MODEL_OPERATION.observation(
                    this.observationConvention, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
                    this.observationRegistry);

            observation.parentObservation(contextView.getOrDefault(ObservationThreadLocalAccessor.KEY, null)).start();

            // Convert the ChatCompletionChunk into a ChatCompletion to be able to reuse
            // the function call handling logic.
            Flux<ChatResponse> chatResponse = executeFlux.switchMap(chatCompletion -> Mono.just(chatCompletion).map(chatCompletion2 -> {
                        try {
                            List<Generation> generations = List.of(new Generation(new AssistantMessage(""))); // TODO 替换为wrapper包装类
                            return new ChatResponse(generations);
                        } catch (Exception e) {
                            logger.error("Error processing chat completion", e);
                            return new ChatResponse(List.of());
                        }
                        // When in stream mode and enabled to include the usage, the OpenAI
                        // Chat completion response would have the usage set only in its
                        // final response. Hence, the following overlapping buffer is
                        // created to store both the current and the subsequent response
                        // to accumulate the usage from the subsequent response.
                    }))
                    .buffer(2, 1)
                    .map(bufferList -> {
                        ChatResponse firstResponse = bufferList.get(0);
//                        if (request.streamOptions() != null && request.streamOptions().includeUsage()) {
//                            if (bufferList.size() == 2) {
//                                ChatResponse secondResponse = bufferList.get(1);
//                                if (secondResponse != null && secondResponse.getMetadata() != null) {
//                                    // This is the usage from the final Chat response for a
//                                    // given Chat request.
//                                    Usage usage = secondResponse.getMetadata().getUsage();
//                                    if (!UsageCalculator.isEmpty(usage)) {
//                                        // Store the usage from the final response to the
//                                        // penultimate response for accumulation.
//                                        return new ChatResponse(firstResponse.getResults(),
//                                                from(firstResponse.getMetadata(), usage));
//                                    }
//                                }
//                            }
//                        }
                        return firstResponse;
                    });
            return new MessageAggregator().aggregate(chatResponse, observationContext::setResponse);
        });
    }


    private Prompt buildRequestPrompt(Prompt prompt) {
        CustomChatOptions runtimeOptions = null;
        if (prompt.getOptions() != null) {
            if (prompt.getOptions() instanceof ToolCallingChatOptions toolCallingChatOptions) {
                runtimeOptions = ModelOptionsUtils.copyToTarget(toolCallingChatOptions, ToolCallingChatOptions.class,
                        CustomChatOptions.class);
            } else {
                runtimeOptions = ModelOptionsUtils.copyToTarget(prompt.getOptions(), ChatOptions.class,
                        CustomChatOptions.class);
            }
        }
        OpenAiChatOptions requestOptions = ModelOptionsUtils.merge(runtimeOptions, this.customChatOptions,
                OpenAiChatOptions.class);
        if (runtimeOptions != null) {
            requestOptions.setHttpHeaders(
                    mergeHttpHeaders(runtimeOptions.getHttpHeaders(), this.customChatOptions.getHttpHeaders()));
            requestOptions.setInternalToolExecutionEnabled(
                    ModelOptionsUtils.mergeOption(runtimeOptions.getInternalToolExecutionEnabled(),
                            this.customChatOptions.getInternalToolExecutionEnabled()));
            requestOptions.setToolNames(ToolCallingChatOptions.mergeToolNames(runtimeOptions.getToolNames(),
                    this.customChatOptions.getToolNames()));
            requestOptions.setToolCallbacks(ToolCallingChatOptions.mergeToolCallbacks(runtimeOptions.getToolCallbacks(),
                    this.customChatOptions.getToolCallbacks()));
            requestOptions.setToolContext(ToolCallingChatOptions.mergeToolContext(runtimeOptions.getToolContext(),
                    this.customChatOptions.getToolContext()));
        } else {
            requestOptions.setInternalToolExecutionEnabled(this.customChatOptions.getInternalToolExecutionEnabled());
            requestOptions.setToolNames(this.customChatOptions.getToolNames());
            requestOptions.setToolCallbacks(this.customChatOptions.getToolCallbacks());
            requestOptions.setToolContext(this.customChatOptions.getToolContext());
        }
        ToolCallingChatOptions.validateToolCallbacks(requestOptions.getToolCallbacks());
        return new Prompt(prompt.getInstructions(), requestOptions);
    }

    private Map<String, String> mergeHttpHeaders(Map<String, String> runtimeHttpHeaders,
                                                 Map<String, String> defaultHttpHeaders) {
        var mergedHttpHeaders = new HashMap<>(defaultHttpHeaders);
        mergedHttpHeaders.putAll(runtimeHttpHeaders);
        return mergedHttpHeaders;
    }

    private Generation buildGeneration(OpenAiApi.ChatCompletion.Choice choice, Map<String, Object> metadata, OpenAiApi.ChatCompletionRequest request) {
        List<AssistantMessage.ToolCall> toolCalls = choice.message().toolCalls() == null ? List.of()
                : choice.message()
                .toolCalls()
                .stream()
                .map(toolCall -> new AssistantMessage.ToolCall(toolCall.id(), "function", // TODO 替换为wrapper包装类
                        toolCall.function().name(), toolCall.function().arguments()))
                .toList();

        String finishReason = (choice.finishReason() != null ? choice.finishReason().name() : "");
        var generationMetadataBuilder = ChatGenerationMetadata.builder().finishReason(finishReason);

        List<Media> media = new ArrayList<>();
        String textContent = choice.message().content();
        var audioOutput = choice.message().audioOutput();
        if (audioOutput != null) {
            String mimeType = String.format("audio/%s", request.audioParameters().format().name().toLowerCase());
            byte[] audioData = Base64.getDecoder().decode(audioOutput.data());
            Resource resource = new ByteArrayResource(audioData);
            Media.builder().mimeType(MimeTypeUtils.parseMimeType(mimeType)).data(resource).id(audioOutput.id()).build();
            media.add(Media.builder()
                    .mimeType(MimeTypeUtils.parseMimeType(mimeType))
                    .data(resource)
                    .id(audioOutput.id())
                    .build());
            if (!StringUtils.hasText(textContent)) {
                textContent = audioOutput.transcript();
            }
            generationMetadataBuilder.metadata("audioId", audioOutput.id());
            generationMetadataBuilder.metadata("audioExpiresAt", audioOutput.expiresAt());
        }

        if (Boolean.TRUE.equals(request.logprobs())) {
            generationMetadataBuilder.metadata("logprobs", choice.logprobs());
        }

        var assistantMessage = new AssistantMessage(textContent, metadata, toolCalls, media); // TODO 替换为wrapper包装类
        return new Generation(assistantMessage, generationMetadataBuilder.build());
    }
}
