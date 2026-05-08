package com.vt.remote.controller;

import com.vt.remote.api.DomainApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.BaseScanResp;
import com.vt.remote.dto.vt.domain.DomainReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/domain")
@RequiredArgsConstructor
public class DomainController {

    private final DomainApi domainApi;

    @PostMapping("/scan/{domain}")
    public VtResult<BaseScanResp> scan(@PathVariable String domain) {
        return domainApi.scanDomain(domain);
    }

    @GetMapping("/get/report/{domain}")
    public VtResult<DomainReportResp> getReport(@PathVariable String domain) {
        return domainApi.getDomainReport(domain);
    }

}