package com.vt.atp.controller;

import com.vt.atp.api.UrlApi;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.BaseScanResp;
import com.vt.atp.dto.vt.url.UrlReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlApi urlApi;

    @PostMapping("/scan")
    public Result<BaseScanResp> scan(@RequestParam String url) {
        return urlApi.scanUrl(url);
    }

    @PostMapping("/re/analyse/{id}")
    public Result<BaseScanResp> reAnalyse(@PathVariable String id) {
        return urlApi.reAnalyze(id);
    }

    @GetMapping("/get/report/{id}")
    public Result<UrlReportResp> getReport(@PathVariable String id) {
        return urlApi.getUrlReport(id);
    }

}
