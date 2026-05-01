package com.vt.atp.controller;

import com.vt.atp.api.IpApi;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.BaseScanResp;
import com.vt.atp.dto.vt.ip.IpReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ip")
@RequiredArgsConstructor
public class IpController {

    private final IpApi ipApi;

    @PostMapping("/scan/{ip}")
    public Result<BaseScanResp> scan(@PathVariable String ip) {
        return ipApi.scanIp(ip);
    }

    @GetMapping("/get/report/{ip}")
    public Result<IpReportResp> getReport(@PathVariable String ip) {
        return ipApi.getIpReport(ip);
    }

}

//103.235.46.115