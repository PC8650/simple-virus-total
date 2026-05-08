package com.vt.flow.scan;

import com.vt.flow.advisor.constant.ChainKey;
import com.vt.flow.dto.InputContent;
import com.vt.flow.enums.TypeEnum;
import com.vt.flow.scan.interfaces.Scanner;
import com.vt.remote.api.FileApi;
import com.vt.remote.dto.FileUpload;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.abs.UploadScanResp;
import com.vt.remote.dto.vt.file.FileBehaviourReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件扫描器
 */
@Component
@RequiredArgsConstructor
public class FileScanner implements Scanner {

    private final FileApi api;

    private final TypeEnum type = TypeEnum.FILE;

    @Override
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        if (input.getFile() == null) {
            VtResult<? extends UploadScanResp> result = new VtResult<>();
            result.setError("Must choose a file");
            return result;
        }
        FileUpload upload = new FileUpload();
        upload.setFile(input.getFile());
        upload.setPwd(input.getPwd());
        return apiRemote(() -> api.uploadFile(upload.parse()));
    }

    @Override
    public String getReportId(ChatClientRequest chatClientRequest) {
        UploadScanResp scanResp = ChainKey.SCAN_RESP.get(chatClientRequest);
        return scanResp.getIdSha256();
    }

    @Override
    public VtResult<? extends UploadScanResp> reAnalyze(String target) {
        return apiRemote(() -> api.reAnalyze(target));
    }

    @Override
    public VtResult<?> getReport(String id) {
        VtResult<?> report = apiRemote(()-> api.getFileReport(id));
        if (!report.isSuccess()) {
            report.setError("File report fetching failed: " + report.getError());
        }
        return report;
    }

    @Override
    public VtResult<?> getBehaviourReport(String id) {
        VtResult<List<FileBehaviourReportResp>> behaviour = apiRemote(()-> api.getBehaviourReport(id));
        if (!behaviour.isSuccess()) {
            behaviour.setError("File behaviour fetching failed: " + behaviour.getError());
        }
        return behaviour;
    }

    @Override
    public TypeEnum type() {
        return type;
    }

}
