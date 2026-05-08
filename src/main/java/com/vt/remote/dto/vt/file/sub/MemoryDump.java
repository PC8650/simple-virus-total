package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "内存转储")
public record MemoryDump(
        @SerializedName("file_name")
        @Schema(name = "file_name", description = "转储文件名称")
        String file_name,
        @Schema(description = "进程")
        String process,
        @Schema(description = "大小")
        String size,
        @SerializedName("base_address")
        @Schema(name = "base_address", description = "地址")
        String base_address,
        @SerializedName("process_id")
        @Schema(name = "process_id", description = "进程id")
        String processId,
        @Schema(description = "阶段")
        String stage
) {
}
