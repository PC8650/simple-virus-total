package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "有关文件分发者的信息")
public record KnownDistributors(
        @SerializedName("data_sources")
        @Schema(name = "data_sources", description = "信息摄取的数据源")
        List<String> dataSources,
        @Schema(description = "分发该文件的公司")
        List<String> distributors,
        @Schema(description = "文件分发时使用的名称")
        List<String> filenames,
        @Schema(description = "获取有关该文件更多信息的 URL")
        List<String> links,
        @Schema(description = "此文件所属的产品")
        List<String> products
) {
}
