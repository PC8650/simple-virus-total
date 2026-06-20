package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "关于 Java jar 文件的信息")
public record JarInfo(
        @Schema(description = "包含的文件名称")
        List<String> filenames,
        @SerializedName("files_by_type")
        @Schema(name = "files_by_type", description = "每种文件类型的类型和数量。键是文件类型，值是每种文件类型的数量")
        Map<String, Integer> filesByType,
        @Schema(description = "Jar manifest 文件内容")
        String manifest,
        @SerializedName("max_date")
        @Schema(name = "max_date", description = "包含文件的最大日期。%Y-%m-%d %H:%M:%S")
        String maxDate,
        @SerializedName("max_depth")
        @Schema(name = "max_depth", description = "包的最大目录深度")
        Integer maxDepth,
        @SerializedName("min_date")
        @Schema(name = "min_date", description = "包含文件的最小日期。%Y-%m-%d %H:%M:%S")
        String minDate,
        @Schema(description = "猜测包文件中使用的包")
        List<String> packages,
        @Schema(description = "在包文件中找到有趣的字符串")
        List<String> strings,
        @SerializedName("total_dirs")
        @Schema(name = "total_dirs", description = "包中的目录数量")
        Integer totalDirs,
        @SerializedName("total_files")
        @Schema(name = "total_files", description = "包中的文件数量")
        Integer totalFiles
) {
}
