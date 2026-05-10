package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "IP 流量记录")
public record IpTraffic(

        @SerializedName("destination_ip")
        @Schema(name = "destination_ip", description = "目标 IP 地址")
        String destinationIp,

        @SerializedName("destination_port")
        @Schema(name = "destination_port", description = "目标端口")
        Integer destinationPort,

        @SerializedName("transport_layer_protocol")
        @Schema(name = "transport_layer_protocol", description = "传输层协议（如 TCP、UDP）")
        String transportLayerProtocol
) {
}
