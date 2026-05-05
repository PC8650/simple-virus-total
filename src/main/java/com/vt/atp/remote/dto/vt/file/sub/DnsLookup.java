package com.vt.atp.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DNS 查询信息")
public record DnsLookup(
        @Schema(description = "主机名")
        String hostname,
        @SerializedName("resolved_ips")
        @Schema(name = "resolved_ips", description = "解析ip")
        List<String> resolvedIps
) {
}
