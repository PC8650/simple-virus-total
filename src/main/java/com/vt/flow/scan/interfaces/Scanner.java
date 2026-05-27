package com.vt.flow.scan.interfaces;

import com.vt.enums.MsgEnum;
import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import com.vt.utils.MessageUtils;
import org.springframework.ai.chat.client.ChatClientRequest;

import java.util.function.Supplier;

public interface Scanner {

    /**
     * 参数校验
     */
    void valid(InputContent input);

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
     * 获取行为报告中的ATT&CK汇总信息
     */
    default VtResult<?> getBehaviourMitre(String id) {
        return new VtResult<>();
    }

    /**
     * api 调用
     */
    default <T> VtResult<T> apiRemote(Supplier<VtResult<T>> remote, String remark) {
        VtResult<T> result;
        result =  remote.get();
        //不成功就抛出异常，终止流程
        if (!result.isSuccess()) {
            throw new RuntimeException(MessageUtils.getMessage(MsgEnum.SCAN_ERR_API_FAILED, remark, result.getError()));
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
