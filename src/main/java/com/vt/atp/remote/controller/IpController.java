package com.vt.atp.remote.controller;

import com.vt.atp.remote.api.IpApi;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.BaseScanResp;
import com.vt.atp.remote.dto.vt.ip.IpReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ip")
@RequiredArgsConstructor
public class IpController {

    private final IpApi ipApi;

    @PostMapping("/scan/{ip}")
    public VtResult<BaseScanResp> scan(@PathVariable String ip) {
        return ipApi.scanIp(ip);
    }

    @GetMapping("/get/report/{ip}")
    public VtResult<IpReportResp> getReport(@PathVariable String ip) {
        return ipApi.getIpReport(ip);
    }

}

//103.235.46.115