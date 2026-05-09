package com.vt.flow.utils;

import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.vo.FlowResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * SSE 流程推送工具类
 */
@Slf4j
public class FlowSseUtil {

    private FlowSseUtil() {
    }

    /**
     * 推送过程节点信息
     */
    public static void send(Map<String, Object> chainContent, String advisorName, String content) {
        send(chainContent, advisorName, content, true, FlowResp::addAttributes);
    }

    /**
     * 推送最后一条信息并标记流程结束
     */
    public static void sendFinish(Map<String, Object> chainContent, String advisorName, String content) {
        send(chainContent, advisorName, content, true, FlowResp::addLastAttributes);
    }

    /**
     * 推送过程节点信息
     * 非正文
     */
    public static void sendNotMainText(ChatClientRequest request, String advisorName, String content) {
        send(request.context(), advisorName, content, false, FlowResp::addAttributes);
    }

    /**
     * 推送最后一条信息并标记流程结束
     * 非正文
     */
    public static void sendFinishNotMainText(Map<String, Object> chainContent, String advisorName, String content) {
        send(chainContent, advisorName, content, false, FlowResp::addLastAttributes);
    }

    /**
     * 直接通过 Emitter 推送信息
     */
    private static void send(Map<String, Object> chainContent, String advisorName, String content, boolean mainText,
            BiConsumer<FlowResp, FlowResp.Attributes> addConsumer) {
        try {
            SseEmitter emitter = ChainKey.SEE.get(chainContent);
            FlowResp flowResp = ChainKey.RESP_RECORD.get(chainContent);
            FlowResp.Attributes attributes = new FlowResp.Attributes(advisorName, content, mainText);
            addConsumer.accept(flowResp, attributes);
            emitter.send(flowResp);
        } catch (Exception e) {
            log.error("Emitter Exception", e);
        }
    }
}
