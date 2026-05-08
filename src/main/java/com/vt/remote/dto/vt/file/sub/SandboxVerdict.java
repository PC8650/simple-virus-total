package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "所有沙箱测试结果摘要")
public record SandboxVerdict(
        String category,
        Integer confidence,
        @SerializedName("malware_classification")
        @Schema(name = "malware_classification", description = "原始沙箱判决")
        List<String> malwareClassification,
        @SerializedName("malware_names")
        @Schema(name = "malware_names", description = "原始沙箱判决")
        List<String> malwareNames,
        @SerializedName("sandbox_name")
        @Schema(name = "sandbox_name", description = "提供判决结果的沙箱")
        String sandboxName
) {
}
