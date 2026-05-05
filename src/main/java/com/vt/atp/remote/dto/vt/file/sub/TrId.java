package com.vt.atp.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "TrID是一款旨在根据文件二进制签名识别文件类型的实用程序。它可能会给出多个检测结果，并按文件格式识别概率从高到低排序（以百分比表示）")
public record TrId(
        @SerializedName("file_type")
        @Schema(name = "file_type", description = "文件格式")
        String fileType,
        @Schema(description = "识别概率")
        Double probability
) {}
