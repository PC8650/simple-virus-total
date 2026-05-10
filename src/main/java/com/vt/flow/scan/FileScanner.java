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
    public void valid(InputContent input) {
        if (input.getFile() == null) {
            throw new IllegalArgumentException("Must choose a file");
        }
    }

    @Override
    public VtResult<? extends UploadScanResp> scan(InputContent input) {
        FileUpload upload = new FileUpload();
        upload.setFile(input.getFile());
        upload.setPwd(input.getPwd());
        return apiRemote(() -> api.uploadFile(upload.parse()), "File scan failed: ");
    }

    @Override
    public String getReportId(ChatClientRequest chatClientRequest) {
        UploadScanResp scanResp = ChainKey.SCAN_RESP.get(chatClientRequest);
        return scanResp.getIdSha256();
    }

    @Override
    public VtResult<? extends UploadScanResp> reAnalyze(String target) {
        return api.reAnalyze(target);
    }

    @Override
    public VtResult<?> getReport(String id) {
        return apiRemote(()-> api.getFileReport(id), "File report fetching failed: " );
    }

    @Override
    public VtResult<List<FileBehaviourReportResp>> getBehaviourReport(String id) {
        return apiRemote(()-> api.getBehaviourReport(id), "File behaviour fetching failed: " );
    }

    @Override
    public VtResult<?> getBehaviourMitre(String id) {
        return apiRemote(() -> api.getMitreTrees(id), "File behaviour mitre fetching failed: ");
    }

    @Override
    public TypeEnum type() {
        return type;
    }

}
