package com.vt.atp.remote.dto.vt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "社区投票总数的未加权值，分为“无害”和“恶意”两类")
public record Vote(

        @Schema(description = "无害")
        Integer harmless,

        @Schema(description = "恶意")
        Integer malicious
) {
}
