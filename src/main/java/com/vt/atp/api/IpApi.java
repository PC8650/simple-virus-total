package com.vt.atp.api;

import com.google.api.client.http.EmptyContent;
import com.google.gson.reflect.TypeToken;
import com.vt.atp.api.constant.ApiConstant;
import com.vt.atp.component.VtRemoter;
import com.vt.atp.dto.Result;
import com.vt.atp.dto.vt.BaseScanResp;
import com.vt.atp.dto.vt.ip.IpReportResp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IpApi {

    private final VtRemoter vtRemoter;

    /**
     * 扫描 ip
     * @param ip 要扫描的ip
     * @return 分析id和链接
     */
    public Result<BaseScanResp> scanIp(String ip) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.SCAN_IP, ip);
        return vtRemoter.post(url, new EmptyContent(), new TypeToken<Result<BaseScanResp>>(){});
    }

    /**
     * 获取ip报告
     * @param ip ip
     * @return ip报告
     */
    public Result<IpReportResp> getIpReport(String ip) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_IP_REPORT, ip);
        return vtRemoter.get(url, new TypeToken<Result<IpReportResp>>(){});
    }

}
