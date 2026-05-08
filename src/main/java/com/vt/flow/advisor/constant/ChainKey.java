package com.vt.flow.advisor.constant;

import com.vt.flow.dto.InputContent;
import com.vt.flow.dto.ReportContent;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;

import java.util.Map;

@AllArgsConstructor
public final class ChainKey<T> {

    public static final ChainKey<InputContent> INPUT = new ChainKey<>("input", "原始输入", InputContent.class);
    public static final ChainKey<Scanner> SCANNER = new ChainKey<>("scanner", "类型策略扫描器", Scanner.class);
    public static final ChainKey<UploadScanResp> SCAN_RESP = new ChainKey<>("scan_result", "扫描结果", UploadScanResp.class);
    public static final ChainKey<ReportContent> REPORT_SUMMARY = new ChainKey<>("report_summary", "报告汇总", ReportContent.class);

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

}
