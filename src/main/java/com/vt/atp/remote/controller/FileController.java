package com.vt.atp.remote.controller;

import com.vt.atp.remote.api.FileApi;
import com.vt.atp.remote.dto.FileUpload;
import com.vt.atp.remote.dto.FileUploadParse;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.file.FileBehaviourReportResp;
import com.vt.atp.remote.dto.vt.file.FileReportResp;
import com.vt.atp.remote.dto.vt.file.FileScanResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileApi fileApi;

    @PostMapping("/upload")
    public VtResult<FileScanResp> upload(@ModelAttribute FileUpload fileUpload) {
        FileUploadParse uploadParse = fileUpload.parse();
        return fileApi.uploadFile(uploadParse);
    }

    @PostMapping("/re/analyse/{id}")
    public VtResult<FileScanResp> reAnalyse(@PathVariable String id) {
        return fileApi.reAnalyze(id);
    }

    @GetMapping("/get/report/{id}")
    public VtResult<FileReportResp> getReport(@PathVariable String id) {
        return fileApi.getFileReport(id);
    }

    @GetMapping("/get/behaviour/report/{id}")
    public VtResult<List<FileBehaviourReportResp>> getBehaviourReport(@PathVariable String id) {
        return fileApi.getBehaviourReport(id);
    }

}
