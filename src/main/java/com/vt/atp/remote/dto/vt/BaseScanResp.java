package com.vt.atp.remote.dto.vt;

import com.vt.atp.remote.dto.vt.abs.UploadScanResp;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;

@Schema(description = "基础扫描响应，适用 ip/url/domain")
public class BaseScanResp extends UploadScanResp {

    @Override
    public String getIdSha256() {
        String id = super.getId();
        if (!StringUtils.hasText(id)) return "";

        int index = id.indexOf("-");
        int last = id.lastIndexOf("-");
        return id.substring(index + 1, last);
    }
}
