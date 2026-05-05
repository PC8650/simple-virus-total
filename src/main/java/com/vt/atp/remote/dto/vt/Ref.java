package com.vt.atp.remote.dto.vt;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "引用信息")
public record Ref(

        @Schema(description = "引用类型")
        String ref,

        @Schema(description = "引用值")
        String value
){
}
