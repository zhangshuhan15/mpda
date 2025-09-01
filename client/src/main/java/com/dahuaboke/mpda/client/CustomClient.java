package com.dahuaboke.mpda.client;


import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.CommonResp;
import com.dahuaboke.mpda.client.entity.TxBodyReq;
import com.dahuaboke.mpda.client.entity.TxHeaderReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.ai.util.JacksonUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * auth: dahua
 * time: 2025/9/1 13:35
 */
public class CustomClient<T> {

    /**
     * ！！！一切的前提
     * 工具调用使用同步
     * 人工返回使用流式输出
     */

    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .addModules(JacksonUtils.instantiateAvailableModules())
            .build()
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    private static final Predicate<String> SSE_DONE_PREDICATE = "[DONE]"::equals;
    private final RestClient restClient;
    private final WebClient webClient;

    public CustomClient(RestClient restClient, WebClient webClient) {
        this.restClient = restClient;
        this.webClient = webClient;
    }

    public T execute() {
        CommonReq bodyReq = new CommonReq();
        TxBodyReq<Object> embeddingReq = new TxBodyReq<>();
        embeddingReq.setTxEntity(Map.of("text", ""));
        TxHeaderReq txHeaderReq = new TxHeaderReq();
        txHeaderReq.setStartSysOrCmptNo("99370000000");
        txHeaderReq.setSendSysOrCmptNo("99370000000");
        txHeaderReq.setTargetSysOrCmptNo("99370000000");
        txHeaderReq.setServNo("rag_v1_C014007");
        bodyReq.setTxBody(embeddingReq);
        bodyReq.setTxHeader(txHeaderReq);
        String url = "http://20.200.175.85:32011/online-service/rag/v1/C011001";
        MultiValueMap<String, String> customHeaders = new LinkedMultiValueMap<>();
        ParameterizedTypeReference<CommonResp<T>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<CommonResp<T>> responseEntity = restClient.post()
                .uri(url)
                .headers(headers -> headers.addAll(customHeaders))
                .body(bodyReq)
                .retrieve()
                .toEntity(typeRef);
        return responseEntity.getBody().getTxBody();
    }

    public Flux<T> executeStream() {
        CommonReq bodyReq = new CommonReq();
        TxBodyReq<Object> embeddingReq = new TxBodyReq<>();
        embeddingReq.setTxEntity(Map.of("text", ""));
        TxHeaderReq txHeaderReq = new TxHeaderReq();
        txHeaderReq.setStartSysOrCmptNo("99370000000");
        txHeaderReq.setSendSysOrCmptNo("99370000000");
        txHeaderReq.setTargetSysOrCmptNo("99370000000");
        txHeaderReq.setServNo("rag_v1_C014007");
        bodyReq.setTxBody(embeddingReq);
        bodyReq.setTxHeader(txHeaderReq);
        String url = "http://20.200.175.85:32011/online-service/rag/v1/C011001";
        MultiValueMap<String, String> customHeaders = new LinkedMultiValueMap<>();
        TypeReference<CommonResp<T>> typeRef = new TypeReference<>() {
        };
        return this.webClient.post()
                .uri(url)
                .headers(headers -> headers.addAll(customHeaders))
                .body(Mono.just(bodyReq), CommonReq.class)
                .retrieve()
                .bodyToFlux(String.class)
                // cancels the flux stream after the "[DONE]" is received.
                .takeUntil(SSE_DONE_PREDICATE)
                // filters out the "[DONE]" message.
                .filter(SSE_DONE_PREDICATE.negate())
                .map(content ->
                {
                    try {
                        return OBJECT_MAPPER.readValue(content, typeRef);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                // Flux<ChatCompletionChunk> -> Flux<Flux<ChatCompletionChunk>>
                // Merging the window chunks into a single chunk.
                // Reduce the inner Flux<ChatCompletionChunk> window into a single
                // Mono<ChatCompletionChunk>,
                // Flux<Flux<ChatCompletionChunk>> -> Flux<Mono<ChatCompletionChunk>>
                .concatMapIterable(window -> {
                    Mono<T> monoChunk = Mono.just(window.getTxBody());
                    return List.of(monoChunk);
                })
                // Flux<Mono<ChatCompletionChunk>> -> Flux<ChatCompletionChunk>
                .flatMap(mono -> mono);
    }
}
