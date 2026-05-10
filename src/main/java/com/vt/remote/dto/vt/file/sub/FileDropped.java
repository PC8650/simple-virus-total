package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "执行过程中投放（释放）的文件记录")
public record FileDropped(

        @Schema(description = "文件释放路径")
        String path,

        @Schema(description = "文件 SHA256 哈希")
        String sha256,

        @Schema(description = "文件类型（如 CAB、PE32 等）")
        String type,

        @SerializedName("download_url")
        @Schema(name = "download_url", description = "文件下载来源 URL")
        String downloadUrl
) {
}
