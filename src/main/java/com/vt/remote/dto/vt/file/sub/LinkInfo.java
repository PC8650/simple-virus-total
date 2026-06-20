package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "有关 Microsoft Windows LNK 文件的信息")
public record LinkInfo(
        @SerializedName("creation_date")
        @Schema(name = "creation_date", description = "ISO8601 格式的创建日期")
        String creationDate,
        @SerializedName("access_date")
        @Schema(name = "access_date", description = "ISO8601 格式的访问日期")
        String accessDate,
        @SerializedName("modification_date")
        @Schema(name = "modification_date", description = "ISO8601 格式的修改日期")
        String modificationDate,
        @SerializedName("link_flags")
        @Schema(name = "link_flags", description = "LNK 文件的基本属性列表")
        List<String> linkFlags,
        @SerializedName("target_path")
        @Schema(name = "target_path", description = "来自链接目标标识符字段的目标路径")
        String targetPath,
        @SerializedName("icon_location")
        @Schema(name = "icon_location", description = "图标位置的路径")
        String iconLocation,
        @SerializedName("mac_address")
        @Schema(name = "mac_address", description = "网络 MAC 地址")
        String macAddress,
        @SerializedName("mac_vendor_name")
        @Schema(name = "mac_vendor_name", description = "来自 MAC 地址的网络供应商名称")
        String macVendorName,
        @SerializedName("machine_id")
        @Schema(name = "machine_id", description = "计算机名称")
        String machineId,
        @SerializedName("working_directory")
        @Schema(name = "working_directory", description = "目标工作目录")
        String workingDirectory,
        @SerializedName("relative_path")
        @Schema(name = "relative_path", description = "目标文件的相对路径")
        String relativePath,
        @SerializedName("command_line_arguments")
        @Schema(name = "command_line_arguments", description = "命令行参数")
        String commandLineArguments,
        @SerializedName("volume_serial_number")
        @Schema(name = "volume_serial_number", description = "磁盘卷序列号")
        String volumeSerialNumber,
        @SerializedName("volume_label")
        @Schema(name = "volume_label", description = "磁盘卷标签")
        String volumeLabel,
        @SerializedName("local_path")
        @Schema(name = "local_path", description = "本地路径")
        String localPath,
        @SerializedName("common_path")
        @Schema(name = "common_path", description = "公共路径")
        String commonPath,
        @SerializedName("network_share_name")
        @Schema(name = "network_share_name", description = "网络共享名称")
        String networkShareName,
        @SerializedName("extra_data")
        @Schema(name = "extra_data", description = "LNK 文件的额外数据")
        ExtraData extraData,
        @SerializedName("link_target_id_list")
        @Schema(name = "link_target_id_list", description = "链接目标 ID 列表")
        List<LinkTargetId> linkTargetIdList,
        @Schema(description = "头部信息")
        Header header,
        @Schema(description = "文件的 vhash 值")
        String vhash
) {
    @Schema(description = "额外数据")
    public record ExtraData(
            @SerializedName("dlt_properties")
            @Schema(name = "dlt_properties", description = "LNK 文件的 DLT 属性")
            DltProperties dltProperties
    ) {
        @Schema(description = "DLT 属性")
        public record DltProperties(
                @SerializedName("birth_droid_file_id")
                @Schema(name = "birth_droid_file_id", description = "原始 Droid 文件 ID")
                String birthDroidFileId,
                @SerializedName("droid_file_id")
                @Schema(name = "droid_file_id", description = "Droid 文件 ID")
                String droidFileId,
                @SerializedName("birth_droid_volume_id")
                @Schema(name = "birth_droid_volume_id", description = "原始 Droid 卷 ID")
                String birthDroidVolumeId,
                @SerializedName("droid_volume_id")
                @Schema(name = "droid_volume_id", description = "Droid 卷 ID")
                String droidVolumeId
        ) {}
    }
    @Schema(description = "链接目标 ID 详情")
    public record LinkTargetId(
            @Schema(description = "类标识符")
            String clsid,
            @SerializedName("item_type")
            @Schema(name = "item_type", description = "项目类型")
            Integer itemType,
            @SerializedName("item_type_str")
            @Schema(name = "item_type_str", description = "项目类型字符串")
            String itemTypeStr
    ) {}
    @Schema(description = "头部信息详情")
    public record Header(
            @SerializedName("show_window")
            @Schema(name = "show_window", description = "显示窗口状态码")
            Integer showWindow,
            @SerializedName("show_window_str")
            @Schema(name = "show_window_str", description = "显示窗口状态字符串")
            String showWindowStr,
            @SerializedName("hot_key")
            @Schema(name = "hot_key", description = "热键")
            String hotKey,
            @SerializedName("file_size")
            @Schema(name = "file_size", description = "文件大小")
            String fileSize
    ) {}
}