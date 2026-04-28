package com.vt.atp.controller;

import com.vt.atp.api.file.FileApi;
import com.vt.atp.dto.FileUpload;
import com.vt.atp.dto.FileUploadParse;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.file.FileAnalyseResp;
import com.vt.atp.dto.vt.file.FileBehaviourReportResp;
import com.vt.atp.dto.vt.file.FileReportResp;
import com.vt.atp.dto.vt.file.FileUploadResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileApi fileApi;

    @PostMapping("/upload")
    public Result<FileUploadResp> upload(@ModelAttribute FileUpload fileUpload) {
        FileUploadParse uploadParse = fileUpload.parse();
        return fileApi.uploadFile(uploadParse);
    }

    @GetMapping("/get/analyse/{id}")
    public Result<FileAnalyseResp> getAnalyse(@PathVariable String id) {
        return fileApi.getFileAnalyse(id);
    }

    @PostMapping("/re/analyse/{id}")
    public Result<FileUploadResp> reAnalyse(@PathVariable String id) {
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
