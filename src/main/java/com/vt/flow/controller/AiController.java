package com.vt.flow.controller;

import com.vt.enums.MsgEnum;
import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.utils.FlowSseUtil;
import com.vt.flow.vo.FlowResp;
import com.vt.utils.MessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Tag(name = "AI 分析流", description = "驱动 VirusTotal 完整分析链路的 AI 专家接口")
@RestController
@RequestMapping("/vt")
@RequiredArgsConstructor
public class AiController {

    private final ChatClient vtClient;

    @Operation(summary = "发起流式分析任务", description = "提交文件/域名/IP/URL，并以 SSE 流的形式实时推送分析节点的详细专家报告。")
    @PostMapping(path = "/flow", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> vtFlow(@ModelAttribute InputContent inputContent) {
        // 流程限制最长20分钟
        SseEmitter emitter = new SseEmitter(1200000L);

        // 初始化参数
        // chainContent 会被浅拷贝再传入流程，因此String这种不可变又需要被外部感知的对象需要单独处理
        AtomicReference<String> current = new AtomicReference<>();
        Map<String, Object> chainContent = ChainKey.initVtFlowMap(emitter, inputContent, new FlowResp(), current);

        // 使用虚拟线程执行后续所有的分析流程，释放 Tomcat 的主请求线程
        Thread.startVirtualThread(() -> {
            String lang = inputContent.getLanguage();
            try {
                // 1. 发起流式分析请求（添加占位符 User 消息，防止 Google API 校验失败）
                String userPrompt;
                if (inputContent.getDescription() != null && !inputContent.getDescription().isBlank()) {
                    userPrompt = MessageUtils.getMessage(lang, MsgEnum.PROMPT_INITIAL,
                            inputContent.getDescription());
                } else {
                    userPrompt = MessageUtils.getMessage(lang, MsgEnum.PROMPT_INITIAL_EMPTY);
                }

                Flux<String> flux = vtClient.prompt(userPrompt)
                        .advisors((a) -> a.params(chainContent))
                        .stream()
                        .content();

                // 2. 订阅并推送结果
                flux.subscribe(
                        chunk -> FlowSseUtil.send(chainContent, current.get(), chunk),
                        error -> {
                            FlowSseUtil.sendFinish(chainContent, current.get(),
                                    MessageUtils.getMessage(lang, MsgEnum.SSE_TASK_ERROR, error.getMessage()));
                            emitter.completeWithError(error);
                        },
                        () -> {
                            FlowSseUtil.sendFinishNotMainText(chainContent, current.get(),
                                    MessageUtils.getMessage(lang, MsgEnum.SSE_TASK_FINISH));
                            emitter.complete();
                        });
            } catch (Exception e) {
                FlowSseUtil.sendFinish(chainContent, current.get(),
                        MessageUtils.getMessage(lang, MsgEnum.SSE_TASK_INTERNAL_ERROR, e.getMessage()));
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(emitter);
    }

}
