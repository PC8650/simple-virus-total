package com.vt.remote.controller;

import com.vt.remote.api.UrlApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.BaseScanResp;
import com.vt.remote.dto.vt.url.UrlReportResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "URL 情报管理", description = "提供 URL 的扫描提交、重新分析与信誉报告查询能力")
@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlApi urlApi;

    @Operation(summary = "提交 URL 扫描", description = "将指定 URL 提交至 VirusTotal 进行分析。")
    @PostMapping("/scan")
    public VtResult<BaseScanResp> scan(@Parameter(description = "待扫描的完整 URL") @RequestParam String url) {
        return urlApi.scanUrl(url);
    }

    @Operation(summary = "触发重新分析", description = "对已扫描过的 URL ID 强制触发一次新的分析。")
    @PostMapping("/re/analyse/{id}")
    public VtResult<BaseScanResp> reAnalyse(@Parameter(description = "URL 唯一 ID") @PathVariable String id) {
        return urlApi.reAnalyze(id);
    }

    @Operation(summary = "获取 URL 报告", description = "查询 URL 的全量信誉报告，包含跳转链、分类、页面标题等。")
    @GetMapping("/get/report/{id}")
    public VtResult<UrlReportResp> getReport(@Parameter(description = "URL 唯一 ID") @PathVariable String id) {
        return urlApi.getUrlReport(id);
    }

}
