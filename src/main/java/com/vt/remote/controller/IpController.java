package com.vt.remote.controller;

import com.vt.remote.api.IpApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.BaseScanResp;
import com.vt.remote.dto.vt.ip.IpReportResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "IP 情报管理", description = "提供 IP 地址的扫描提交与信誉报告查询能力")
@RestController
@RequestMapping("/ip")
@RequiredArgsConstructor
public class IpController {

    private final IpApi ipApi;

    @Operation(summary = "提交 IP 扫描", description = "将指定 IP 地址提交至 VirusTotal 进行实时分析（通常用于刷新其地理位置或分类数据）")
    @PostMapping("/scan/{ip}")
    public VtResult<BaseScanResp> scan(@Parameter(description = "待扫描的 IP 地址") @PathVariable String ip) {
        return ipApi.scanIp(ip);
    }

    @Operation(summary = "获取 IP 报告", description = "查询 IP 的全量信誉报告，包含地理位置、ASN、SSL 证书及关联恶意软件等信息")
    @GetMapping("/get/report/{ip}")
    public VtResult<IpReportResp> getReport(@Parameter(description = "目标 IP 地址") @PathVariable String ip) {
        return ipApi.getIpReport(ip);
    }

}

//103.235.46.115