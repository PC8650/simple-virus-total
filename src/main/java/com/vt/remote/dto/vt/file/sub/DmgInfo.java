package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = """
        关于可挂载macOS磁盘镜像的信息
        dmg_info报告了关于[Apple .dmg file] (https://en.wikipedia.org/wiki/Apple_Disk_Image)结构的数据。大量数据来自内部元数据文件，这些文件可能出现在某些文件中，而有些则没有
        """)
public record DmgInfo(
        @Schema(description = "列表中的每个项目代表DMG图像中的一个BLKX块")
        List<Blkx> blkx,
        @SerializedName("data_fork_length")
        @Schema(name = "data_fork_length", description = "数据叉大小")
        String dataForkLength,
        @SerializedName("data_fork_offset")
        @Schema(name = "data_fork_offset", description = "数据分叉偏移量（通常为0）")
        String dataForkOffset,
        @SerializedName("dmg_version")
        @Schema(name = "dmg_version", description = "DMG文件版本")
        Integer dmgVersion,
        @Schema(description = "关于GPT头的信息")
        Gpt gpt,
        @Schema(description = "关于HFS元素的信息")
        Element hfs,
        @Schema(description = "关于ISO元素的信息")
        Element iso,
        @Schema(description = "包含应用程序的配置信息，如捆绑包ID、版本号和显示名称")
        List<Plst> plst,
        @SerializedName("plst_context")
        @Schema(name = "plst_context", description = "包含从列表中提取的有趣字符串，如SLA")
        List<String> plst_context,
        @SerializedName("plst_keys")
        @Schema(name = "plst_keys", description = "plst条目的密钥")
        List<String> plstKeys,
        @SerializedName("running_data_fork_offset")
        @Schema(name = "running_data_fork_offset", description = "表示运行数据叉的起始位置，通常为0")
        String runningDataForkOffset,
        @SerializedName("resourcefork_keys")
        @Schema(name = "resourcefork_keys", description = "在资源叉中找到的键")
        List<String> resourceforkKeys,
        @SerializedName("rsrc_fork_length")
        @Schema(name = "rsrc_fork_length", description = "资源分叉长度")
        String rsrcForkLength,
        @SerializedName("rsrc_fork_offset")
        @Schema(name = "rsrc_fork_offset", description = "资源分叉偏移量")
        String rsrcForkOffset,
        @SerializedName("xml_lenght")
        @Schema(name = "xml_lenght", description = "DMG中属性列表的长度")
        String xmlLength,
        @SerializedName("xml_offset")
        @Schema(name = "xml_offset", description = "DMG中属性列表的偏移")
        String xmlOffset
) {
    @Schema(description = "DMG图像中BLKX块信息")
    public record Blkx(
            @Schema(description = "十六进制格式")
            String attributes,
            @Schema(description = "块名")
            String name
    ){}

    @Schema(description = "关于GPT头的信息 https://wiki.osdev.org/GPT")
    public record Gpt(
            @SerializedName("alternate_lba")
            @Schema(name = "alternate_lba", description = "替代GPT头的LBA")
            Integer alternateLba,
            @SerializedName("disk_uuid")
            @Schema(name = "disk_uuid", description = "磁盘的GUID")
            String diskUuid,
            @SerializedName("entries_crc32")
            @Schema(name = "entries_crc32", description = "分区条目数组的CRC32")
            String entriesCrc32,
            @SerializedName("entries_lba")
            @Schema(name = "entries_lba", description = "GUID 部分条目数组的起始 LBA")
            Integer entriesLba,
            @SerializedName("first_usable_lba")
            @Schema(name = "first_usable_lba", description = "GPT条目中第一个可用块")
            Integer firstUsableLba,
            @SerializedName("header_crc32")
            @Schema(name = "header_crc32", description = "GPT头部的CRC32校验和")
            String headerCrc32,
            @SerializedName("last_usable_lba")
            @Schema(name = "last_usable_lba", description = "GPT条目中最后一个可用块")
            Integer lastUsableLba,
            @SerializedName("my_lba")
            @Schema(name = "my_lba", description = "包含该头部的LBA")
            Integer myLba,
            @SerializedName("number_of_entries")
            @Schema(name = "number_of_entries", description = "分区条目数量")
            Integer numberOfEntries,
            @Schema(description = "每个分区的详细信息")
            List<Partition> partitions,
            @Schema(description = "GPT 修订")
            String revision,
            @Schema(description = "签名，可以通过8字节 EFI PART 识别(45h 46h 49h 20h 50h 41h 52h 54h)")
            String signature,
            @Schema(description = "头部大小")
            Integer size,
            @SerializedName("size_of_entry")
            @Schema(name = "size_of_entry", description = "Paritions数组中每个条目的大小（字节单位）必须是 8 的倍数")
            Integer sizeOfEntry
    ){
        @Schema(description = "分区信息")
        public record Partition(
                @SerializedName("attrs_flags")
                @Schema(name = "attrs_flags", description = "属性")
                Integer attrsFlags,
                @SerializedName("ending_lba")
                @Schema(name = "ending_lba", description = "结束LBA")
                Integer endingLba,
                @Schema(description = "分区名称")
                String name,
                @SerializedName("starting_lba")
                @Schema(name = "starting_lba", description = "开始LBA")
                Integer startingLba,
                @SerializedName("type_guid")
                @Schema(name = "type_guid", description = "分区类型 GUID（0 表示未使用条目）")
                String typeGuid,
                @SerializedName("unique_guid")
                @Schema(name = "unique_guid", description = "唯一分区 GUID")
                String uniqueGuid
        ){}
    }

    @Schema(description = "HFS/ISO 元素信息。根据不同情况，不同字段可能会出现或不出现")
    public record  Element(
            @SerializedName("info_plist")
            @Schema(name = "info_plist", description = "块的plist（文件）内容。键和值是字符串")
            Map<String, String> infoPlist,
            @SerializedName("main_executable")
            @Schema(name = "main_executable", description = "块的主要可执行文件")
             MainExecutable mainExecutable,
            @SerializedName("num_files")
            @Schema(name = "num_files", description = "文件数量")
            Integer numFiles,
            @SerializedName("unreadable_files")
            @Schema(name = "unreadable_files", description = "无法读取的文件数量")
            Integer unreadableFiles,
            @Schema(description = "块处理是否超时")
            Boolean timeout,
            @SerializedName("volume_data")
            @Schema(name = "volume_data", description = "ISO卷数据")
            IsoImageInfo volumeData
    ){
        @Schema(description = "主要可执行文件")
        public record MainExecutable(
                @Schema(description = "id")
                String id,
                @Schema(description = "在包中的路径")
                String path,
                @Schema(description = "内容hash")
                String sha256,
                @Schema(description = "字节大小")
                String size
        ){}

        @Schema(description = "iso 图像文件信息")
        public record IsoImageInfo(
                @SerializedName("abstract_file_id")
                @Schema(name = "abstract_file_id", description = "根目录中包含该卷集抽象信息的文件名")
                String abstractFileId,
                @SerializedName("application_id")
                @Schema(name = "application_id", description = "用于创建文件的应用程序")
                String applicationId,
                @SerializedName("bibliographic_file_id")
                @Schema(name = "bibliographic_file_id", description = "根目录中包含该卷集书目信息的文件名")
                String bibliographicFileId,
                @Schema(description = "文件创建时间。%Y-%m-%d %H:%M:%S")
                String created,
                @SerializedName("data_preparer_id")
                @Schema(name = "data_preparer_id", description = "为本卷准备数据的人员标识符")
                String dataPreparerId,
                @Schema(description = "生效日期。%Y-%m-%d %H:%M:%S")
                String effective,
                @Schema(description = "有效期。%Y-%m-%d %H:%M:%S")
                String expires,
                @SerializedName("file_structure_version")
                @Schema(name = "file_structure_version", description = "文件结构版本")
                Integer fileStructureVersion,
                @SerializedName("max_date")
                @Schema(name = "max_date", description = "包含文件中最近的修改日期。%Y-%m-%d %H:%M%S")
                String maxDate,
                @SerializedName("min_date")
                @Schema(name = "min_date", description = "包含文件中最早的修改日期。%Y-%m-%d %H:%M%S")
                String minDate,
                @Schema(description = "最后修改日期。%Y-%m-%d %H:%M:%S")
                String modified,
                @SerializedName("num_files")
                @Schema(name = "num_files", description = "包含的文件数量")
                Integer numFiles,
                @SerializedName("publisher_id")
                @Schema(name = "publisher_id", description = "发行商")
                String publisherId,
                @SerializedName("unique_guid")
                @Schema(name = "unique_guid", description = "唯一分区 GUID")
                String uniqueGuid,
                @SerializedName("system_id")
                @Schema(name = "system_id", description = "能够作用于初始扇区的系统名称")
                String systemId,
                @SerializedName("total_size")
                @Schema(name = "total_size", description = "该逻辑卷集合的大小")
                String totalSize,
                @SerializedName("type_code")
                @Schema(name = "type_code", description = "格式类型code")
                String typeCode,
                @SerializedName("volume_id")
                @Schema(name = "volume_id", description = "卷标识")
                String volumeId,
                @SerializedName("volume_set_id")
                @Schema(name = "volume_set_id", description = "卷集标识符")
                String volumeSetId
        ){}
    }

    @Schema(description = "配置信息")
    public record Plst(
            @Schema(description = "十六进制格式")
            String attributes,
            @Schema(description = "属性名称")
            String name
    ){}
}
