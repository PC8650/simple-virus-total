package com.vt.remote.controller;

import com.vt.remote.api.FileApi;
import com.vt.remote.dto.FileUpload;
import com.vt.remote.dto.FileUploadParse;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.file.FileBehaviourReportResp;
import com.vt.remote.dto.vt.file.FileMitreResp;
import com.vt.remote.dto.vt.file.FileReportResp;
import com.vt.remote.dto.vt.file.FileScanResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "文件情报管理", description = "提供文件的上传扫描、静态报告查询、沙箱行为分析及 MITRE 矩阵映射等核心能力")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileApi fileApi;

    @Operation(summary = "提交文件扫描", description = "上传本地文件至 VirusTotal 进行全量引擎扫描。")
    @PostMapping("/upload")
    public VtResult<FileScanResp> upload(@ModelAttribute FileUpload fileUpload) {
        FileUploadParse uploadParse = fileUpload.parse();
        return fileApi.uploadFile(uploadParse);
    }

    @Operation(summary = "触发重新分析", description = "针对已存在的样本 ID (Hash)，强制触发一次新的扫描任务。")
    @PostMapping("/re/analyse/{id}")
    public VtResult<FileScanResp> reAnalyse(@Parameter(description = "文件哈希 (SHA256/MD5)") @PathVariable String id) {
        return fileApi.reAnalyze(id);
    }

    @Operation(summary = "获取静态报告", description = "获取文件的静态分析报告，包含引擎检出汇总、文件属性、签名信息等。")
    @GetMapping("/get/report/{id}")
    public VtResult<FileReportResp> getReport(@Parameter(description = "文件哈希") @PathVariable String id) {
        return fileApi.getFileReport(id);
    }

    @Operation(summary = "获取行为报告列表", description = "获取该文件在不同沙箱环境（Windows, Linux, Android 等）下的动态行为报告列表。")
    @GetMapping("/get/behaviour/report/{id}")
    public VtResult<List<FileBehaviourReportResp>> getBehaviourReport(@Parameter(description = "文件哈希") @PathVariable String id) {
        return fileApi.getBehaviourReport(id);
    }

    @Operation(summary = "获取活跃行为摘要", description = "提取样本在动态执行过程中的关键 API 调用、网络连接及文件 IO 摘要。")
    @GetMapping("/get/behaviour/active/{id}")
    public VtResult<?> getActive(@Parameter(description = "文件哈希") @PathVariable String id) {
        return fileApi.getActiveSummary(id);
    }

    @Operation(summary = "获取 MITRE 攻击矩阵", description = "将样本的行为特征映射至 MITRE ATT&CK 知识库，并以树状结构返回命中的战术与技术细节。")
    @GetMapping("/get/behaviour/mitre/{id}")
    public VtResult<Map<String, FileMitreResp>> getMitre(@Parameter(description = "文件哈希") @PathVariable String id) {
        return fileApi.getMitreTrees(id);
    }

}
