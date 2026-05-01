package com.vt.atp.controller;

import com.vt.atp.api.FileApi;
import com.vt.atp.dto.FileUpload;
import com.vt.atp.dto.FileUploadParse;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.file.FileBehaviourReportResp;
import com.vt.atp.dto.vt.file.FileReportResp;
import com.vt.atp.dto.vt.file.FileScanResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileApi fileApi;

    @PostMapping("/upload")
    public Result<FileScanResp> upload(@ModelAttribute FileUpload fileUpload) {
        FileUploadParse uploadParse = fileUpload.parse();
        return fileApi.uploadFile(uploadParse);
    }

    @PostMapping("/re/analyse/{id}")
    public Result<FileScanResp> reAnalyse(@PathVariable String id) {
        return fileApi.reAnalyze(id);
    }

    @GetMapping("/get/report/{id}")
    public Result<FileReportResp> getReport(@PathVariable String id) {
        return fileApi.getFileReport(id);
    }

    @GetMapping("/get/behaviour/report/{id}")
    public Result<List<FileBehaviourReportResp>> getBehaviourReport(@PathVariable String id) {
        return fileApi.getBehaviourReport(id);
    }

}
