package com.vt.atp.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import com.vt.atp.dto.vt.Ref;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "攻击技术")
public record MitreAttackTechnique(
        @SerializedName("signature_description")
        @Schema(name = "signature_description", description = "描述")
        String signatureDescription,
        @Schema(description = "标识符")
        String id,
        @Schema(description = "严重程度")
        String severity,
        @Schema(description = "引用目录")
        List<Ref> refs
) {
}
