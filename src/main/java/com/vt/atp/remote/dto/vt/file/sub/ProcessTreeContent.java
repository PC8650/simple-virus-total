package com.vt.atp.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "进程树内容")
public record ProcessTreeContent(
        @SerializedName("process_id")
        @Schema(name = "process_id", description = "进程id")
        String processId,
        @Schema(description = "名称")
        String name,
        @SerializedName("files_opened")
        @Schema(name = "files_opened", description = "打卡文件列表")
        List<String> filesOpened
) {
}
