package com.vt.atp.remote.api;

import com.google.api.client.http.EmptyContent;
import com.google.gson.reflect.TypeToken;
import com.vt.atp.remote.api.constant.ApiConstant;
import com.vt.atp.remote.component.VtRemoter;
import com.vt.atp.remote.dto.VtResult;
import com.vt.atp.remote.dto.vt.BaseScanResp;
import com.vt.atp.remote.dto.vt.ip.IpReportResp;
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
    public VtResult<BaseScanResp> scanIp(String ip) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.SCAN_IP, ip);
        return vtRemoter.post(url, new EmptyContent(), new TypeToken<VtResult<BaseScanResp>>(){});
    }

    /**
     * 获取ip报告
     * @param ip ip
     * @return ip报告
     */
    public VtResult<IpReportResp> getIpReport(String ip) {
        String url = String.format(ApiConstant.PREFIX + ApiConstant.GET_IP_REPORT, ip);
        return vtRemoter.get(url, new TypeToken<VtResult<IpReportResp>>(){});
    }

}
