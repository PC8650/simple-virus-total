package com.vt.atp.controller;

import com.vt.atp.api.DomainApi;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.BaseScanResp;
import com.vt.atp.dto.vt.domain.DomainReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/domain")
@RequiredArgsConstructor
public class DomainController {

    private final DomainApi domainApi;

    @PostMapping("/scan/{domain}")
    public Result<BaseScanResp> scan(@PathVariable String domain) {
        return domainApi.scanDomain(domain);
    }

    @GetMapping("/get/report/{domain}")
    public Result<DomainReportResp> getReport(@PathVariable String domain) {
        return domainApi.getDomainReport(domain);
    }

}