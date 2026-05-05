package com.vt.atp.remote.dto.vt.file.sub;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "压缩文件信息")
public record BundleInfo(
        @Schema(name = "highest_datetime", description = "包含文件中的最新日期，格式为.%Y-%m-%d %H:%M:%S")
        String highestDatetime,
        @Schema(name = "lowest_datetime", description = "包含文件中最早的日期，格式为.%Y-%m-%d %H:%M:%S")
        String lowestDatetime,
        @Schema(name = "num_children", description = "捆绑包内的文件和目录数量")
        Integer numChildren,
        @Schema(description = "包含文件扩展名作为键，以及捆绑包中每种扩展名的数量作为值")
        Map<String, Integer> extensions,
        @Schema(name = "file_types", description = "包含文件类型作为键，以及捆绑包中每种文件的数量作为值")
        Map<String, Integer> file_types,
        @Schema(description = "捆绑包类型：ZIP, RAR, ZLIB, TAR, BZIP 和 GZIP")
        String type,
        @Schema(name = "uncompressed_size", description = "压缩文件中未压缩内容的大小(bytes)")
        Long uncompressedSize,
        @Schema(description = "某些文件格式（ZLIB 和 GZIP）的文件解压缩头部")
        String beginning,
        @Schema(description = "尝试解压缩捆绑包时出现的错误消息")
        String error
){}
