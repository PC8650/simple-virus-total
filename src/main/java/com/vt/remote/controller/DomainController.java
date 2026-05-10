package com.vt.remote.controller;

import com.vt.remote.api.DomainApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.BaseScanResp;
import com.vt.remote.dto.vt.domain.DomainReportResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "域名情报管理", description = "提供域名的扫描提交与信誉报告查询能力")
@RestController
@RequestMapping("/domain")
@RequiredArgsConstructor
public class DomainController {

    private final DomainApi domainApi;

    @Operation(summary = "提交域名扫描", description = "将指定域名提交至 VirusTotal 进行实时分析，返回分析任务 ID")
    @PostMapping("/scan/{domain}")
    public VtResult<BaseScanResp> scan(@Parameter(description = "待扫描的目标域名") @PathVariable String domain) {
        return domainApi.scanDomain(domain);
    }

    @Operation(summary = "获取域名报告", description = "查询域名的全量信誉报告，包含 DNS 记录、Whois、分类等信息")
    @GetMapping("/get/report/{domain}")
    public VtResult<DomainReportResp> getReport(@Parameter(description = "目标域名") @PathVariable String domain) {
        return domainApi.getDomainReport(domain);
    }

}