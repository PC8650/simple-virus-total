package com.vt.flow.scan.interfaces;

import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import org.springframework.ai.chat.client.ChatClientRequest;

import java.util.function.Supplier;

public interface Scanner {

    /**
     * 扫描指定目标
     */
    VtResult<? extends UploadScanResp> scan(InputContent input);

    /**
     * 报告id
     */
    String getReportId(ChatClientRequest chatClientRequest);

    /**
     * 重新分析
     */
    VtResult<? extends UploadScanResp> reAnalyze(String target);

    /**
     * 获取报告
     */
    VtResult<?> getReport(String id);

    /**
     * 获取行为报告
     */
    default VtResult<?> getBehaviourReport(String id) {
        return new VtResult<>();
    }

    /**
     * api 调用
     */
    default <T> VtResult<T> apiRemote(Supplier<VtResult<T>> remote) {
        VtResult<T> result = new VtResult<>();
        try {
            result =  remote.get();
        } catch (Exception e) {
            result.setError(e.getMessage());
        }
        return result;
    }

    /**
     * 重新分析
     */
    default VtResult<? extends UploadScanResp> reAnalyze(ChatClientRequest chatClientRequest) {
        String reportId = getReportId(chatClientRequest);
        return reAnalyze(reportId);
    }

    /**
     * 类型
     */
    TypeEnum type();

}
