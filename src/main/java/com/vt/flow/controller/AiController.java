package com.vt.flow.controller;

import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.vo.FlowResp;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.MediaType;

import com.vt.flow.utils.FlowSseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

        //初始化参数
        AtomicReference<String> current = new AtomicReference<>();
        Map<String, Object> chainContent = ChainKey.initVtFlowMap(emitter, inputContent, new FlowResp(), current);

        // 使用虚拟线程执行后续所有的分析流程，释放 Tomcat 的主请求线程
        Thread.startVirtualThread(() -> {
            try {
                // 1. 发起流式分析请求（添加占位符 User 消息，防止 Google API 校验失败）
                Flux<String> flux = vtClient.prompt("Initial analysis request")
                        .advisors((a) -> a.params(chainContent))
                        .stream()
                        .content();

                // 2. 订阅并推送结果
                flux.subscribe(
                        chunk -> FlowSseUtil.send(chainContent, current.get(), chunk),
                        error -> {
                            FlowSseUtil.sendFinish(chainContent, current.get(),
                                    "任务执行异常: " + error.getMessage());
                            emitter.completeWithError(error);
                        },
                        () -> {
                            FlowSseUtil.sendFinishNotMainText(chainContent, current.get(), "任务执行完毕");
                            emitter.complete();
                        });
            } catch (Exception e) {
                FlowSseUtil.sendFinish(chainContent, current.get(), "服务内部异常: " + e.getMessage());
                emitter.completeWithError(e);
            }
        });

        return ResponseEntity.ok(emitter);
    }

}
