package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import com.vt.remote.dto.vt.SslCertificate;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "显示有关使用 Androguard 工具提取的 Android APK、DEX 和 AXML 文件的信息.")
public record Androguard(
        @SerializedName("Activities")
        @Schema(name = "Activities", description = "包含应用程序的活动名称")
        List<String> activities,
        @SerializedName("AndroguardVersion")
        @Schema(name = "AndroguardVersion", description = "Androguard 版本")
        String androguardVersion,
        @SerializedName("AndroidApplication")
        @Schema(name = "AndroidApplication", description = "Android 文件类型，整数格式")
        Integer androidApplication,
        @SerializedName("AndroidApplicationError")
        @Schema(name = "AndroidApplicationError", description = "表示应用程序处理过程中是否出现错误")
        Boolean androidApplicationError,
        @SerializedName("AndroidApplicationInfo")
        @Schema(name = "AndroidApplicationInfo", description = "Android 文件类型（可读形式 APK, DEX, AXML)")
        String androidApplicationInfo,
        @SerializedName("AndroidVersionCode")
        @Schema(name = "AndroidVersionCode", description = "Android 版本代码")
        String androidVersionCode,
        @SerializedName("AndroidVersionName")
        @Schema(name = "AndroidVersionName", description = "Android 版本名称")
        String AndroidVersionName,
        @SerializedName("Libraries")
        @Schema(name = "Libraries", description = "包含应用程序所用库名称的字符串列表")
        List<String> libraries,
        @SerializedName("main_activity")
        @Schema(name = "main_activity", description = "主活动名称")
        String mainActivity,
        @SerializedName("MinSdkVersion")
        @Schema(name = "MinSdkVersion", description = "支持的最低 SDK 版本")
        String minSdkVersion,
        @SerializedName("Package")
        @Schema(name = "Package", description = "包名")
        String packages,
        @SerializedName("Providers")
        @Schema(name = "Providers", description = "包含应用程序使用的提供商")
        List<String> providers,
        @SerializedName("Receivers")
        @Schema(name = "Receivers", description = "包含应用程序使用的接收器")
        List<String> receivers,
        @SerializedName("RiskIndicator")
        @Schema(name = "RiskIndicator", description = "包风险指标")
        RiskIndicator riskIndicator,
        @SerializedName("Services")
        @Schema(name = "Services", description = "包含应用程序使用的服务")
        List<String> services,
        @SerializedName("StringsInformation")
        @Schema(name = "StringsInformation", description = "包含应用程序中发现的有趣字符串")
        List<String> stringsInformation,
        @SerializedName("TargetSdkVersion")
        @Schema(name = "TargetSdkVersion", description = "该应用程序已经过测试的 Android 版本")
        String targetSdkVersion,
        @SerializedName("VTAndroidInfo")
        @Schema(name = "VTAndroidInfo", description = "VT使用的Androguard工具的内部版本")
        String vTAndroidInfo,
        @Schema(description = "应用程序证书详细信息")
        SslCertificate certificate,
        @SerializedName("intent_filters")
        @Schema(name = "intent_filters", description = "图过滤器")
        IntentFilters intentFilters,
        @SerializedName("permission_details")
        @Schema(name = "permission_details", description = "权限的详细信息。key是权限名称, value是权限详情")
        Map<String, PermissionDetail> permissionDetails
) {
    @Schema(description = "包含两个key：APK（结构）和PERM（权限）风险指标")
    public record RiskIndicator(
            @SerializedName("APK")
            @Schema(name = "APK", description = "使用的组件名称以及数量（例如 可执行文件：3）。键是字符串，值是整数")
            Map<String, Integer> apk,
            @SerializedName("PERM")
            @Schema(name = "PERM", description = "权限类型名称及其数量（例如 DANGEROUS：11）。键是字符串，值是整数")
            Map<String, Integer> perm
    ){}
    @Schema(description = "意图过滤器")
    public record IntentFilters(
            @SerializedName("Activities")
            @Schema(name = "Activities", description = "活动的意图过滤器")
            Filter activities,
            @SerializedName("Receivers")
            @Schema(name = "Receivers", description = "接收器的意图过滤器")
            Filter receivers,
            @SerializedName("Services")
            @Schema(name = "Services", description = "服务的意图过滤器")
            Filter services
    ){
        public record Filter(
                @Schema(description = "行动")
                List<String> action,
                @Schema(description = "类别")
                List<String> category
        ){}
    }
    @Schema(description = "权限详细信息")
    public record PermissionDetail(
            @SerializedName("full_description")
            @Schema(name = "full_description", description = "更详细的权限描述")
            String fullDescription,
            @SerializedName("permission_type")
            @Schema(name = "permission_type", description = "描述权限类型（例如normal, dangerous等）")
            String permissionType,
            @SerializedName("short_description")
            @Schema(name = "short_description", description = "描述权限的简短摘要")
            String shortDescription
    ){}
}
