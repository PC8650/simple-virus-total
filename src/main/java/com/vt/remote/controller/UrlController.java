package com.vt.remote.controller;

import com.vt.remote.api.UrlApi;
import com.vt.remote.dto.VtResult;
import com.vt.remote.dto.vt.BaseScanResp;
import com.vt.remote.dto.vt.url.UrlReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlApi urlApi;

    @PostMapping("/scan")
    public VtResult<BaseScanResp> scan(@RequestParam String url) {
        return urlApi.scanUrl(url);
    }

    @PostMapping("/re/analyse/{id}")
    public VtResult<BaseScanResp> reAnalyse(@PathVariable String id) {
        return urlApi.reAnalyze(id);
    }

    @GetMapping("/get/report/{id}")
    public VtResult<UrlReportResp> getReport(@PathVariable String id) {
        return urlApi.getUrlReport(id);
    }

}
