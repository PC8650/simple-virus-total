package com.vt.flow.advisor.constant;

import com.vt.flow.dto.CacheDto;
import com.vt.flow.dto.InputContent;
import com.vt.flow.dto.ReportContent;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.flow.vo.FlowResp;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
public final class ChainKey<T> {

    // 流程中存放
    public static final ChainKey<Scanner> SCANNER = new ChainKey<>("scanner", "类型策略扫描器", Scanner.class);
    public static final ChainKey<CacheDto> CACHE = new ChainKey<>("cache", "缓存，包含key/分析id/报告id", CacheDto.class);
    public static final ChainKey<UploadScanResp> SCAN_RESP = new ChainKey<>("scan_result", "扫描结果", UploadScanResp.class);
    public static final ChainKey<ReportContent> REPORT_SUMMARY = new ChainKey<>("report_summary", "报告汇总", ReportContent.class);

    // 初始化存放
    public static final ChainKey<SseEmitter> SEE = new ChainKey<>("see", "SseEmitter 推送", SseEmitter.class);
    public static final ChainKey<InputContent> INPUT = new ChainKey<>("input", "原始输入", InputContent.class);
    public static final ChainKey<AtomicReference> CURRENT = new ChainKey<>("current", "当前节点", AtomicReference.class);
    public static final ChainKey<FlowResp> RESP_RECORD = new ChainKey<>("resp_record", "流程响应记录", FlowResp.class);

    private final String key;
    private final String description;
    private final Class<T> type;

    public T get(ChatClientRequest chatClientRequest) {
        return get(chatClientRequest.context());
    }

    public T get(Map<String, Object> map) {
        return type.cast(map.get(key));
    }

    public void put(ChatClientRequest chatClientRequest, T value) {
        put(chatClientRequest.context(), value);
    }

    public void put(Map<String, Object> map, T value) {
        map.put(key, value);
    }

    public static Map<String, Object> initVtFlowMap(SseEmitter sseEmitter, InputContent inputContent, FlowResp flowResp, AtomicReference<String> current) {
        Map<String, Object> map = new HashMap<>(8);
        map.put(SEE.key, sseEmitter);
        map.put(INPUT.key, inputContent);
        map.put(CURRENT.key, current);
        map.put(RESP_RECORD.key, flowResp);
        return map;
    }

}
