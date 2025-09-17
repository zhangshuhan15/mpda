package com.dahuaboke.mpda.bot.web.service;

import com.dahuaboke.mpda.bot.model.common.CommonResponse;
import com.dahuaboke.mpda.bot.model.common.ResponseCode;
import com.dahuaboke.mpda.bot.model.request.ChatBotRequest;
import com.dahuaboke.mpda.bot.model.response.ChatBotResponse;
import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import com.dahuaboke.mpda.core.agent.scene.SceneManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 14:34
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    @Autowired
    private SceneManager sceneManager;

    public CommonResponse<ChatBotResponse> chat(ChatBotRequest chatBotRequest) throws MpdaException {
        try {
            if (chatBotRequest == null) {
                return CommonResponse.error(ResponseCode.MISSING_PARAM);
            }

            String conversationId = chatBotRequest.getConversationId();
            String query = chatBotRequest.getQuery();

            if (StringUtils.isEmpty(conversationId)) {
                return CommonResponse.error(ResponseCode.MISSING_PARAM, "会话id不能为空");
            }

            if (StringUtils.isEmpty(query)) {
                return CommonResponse.error(ResponseCode.MISSING_PARAM, "请求内容不能为空");
            }

            String result = sceneManager.apply(conversationId, query);
            ChatBotResponse chatBotResponse = new ChatBotResponse();
            chatBotResponse.setResult(result);
            // 返回成功响应
            return CommonResponse.success(chatBotResponse);

        } catch (IllegalArgumentException e) {
            return CommonResponse.error(ResponseCode.PARAM_FORMAT_ERROR, e.getMessage());
        } catch (Exception e) {
            logger.error("处理聊天请求时发生错误", e);
            return CommonResponse.error(ResponseCode.INTERNAL_ERROR);
        }
    }

    public Flux<String> chatStream(String conversationId, String query) throws MpdaException {
        return sceneManager.applyAsync(conversationId, query);
    }
}
