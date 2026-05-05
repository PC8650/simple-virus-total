package com.vt.atp.remote.controller;

import com.vt.atp.remote.api.DomainApi;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.BaseScanResp;
import com.vt.atp.remote.dto.vt.domain.DomainReportResp;
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