package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "关于 IOS App Store 包文件的信息")
public record IpaInfo(
        @Schema(description = "列表中的每个项目都包含应用内 Mach-O 可执行文件中的元数据信息")
        List<MachoInfo> apps,
        @Schema(description = """
                存储在iTunesMetadata.plist文件中的信息。该文件用于向iTunes提供关于iOS应用的信息。返回的字段可能会因文件而异
                https://learn.microsoft.com/en-us/previous-versions/xamarin/ios/deploy-test/app-distribution/itunesmetadata?tabs=macos
                """)
        Map<String, Object> itunes,
        @Schema(description = "包含存储在 Info.plist文件中的信息")
        Plist plist,
        @Schema(description = "Provision")
        Provision provision
) {
    @Schema(description = "关于苹果MachO文件的信息")
    public record MachoInfo(
            @Schema(description = "加载命令列表")
            List<Command> commands,
            @Schema(description = "该文件的一些基本文件属性")
            Info info,
            @Schema(description = "关于该文件的一些描述性元数据")
            Headers headers,
            @Schema(description = "文件所使用的库")
            List<String> libs,
            @Schema(description = "文件的片段列表")
            List<Segment> segments
    ){
        @Schema(description = "命令信息")
        public record Command(
                @Schema(description = "命令类型")
                String type
        ){}
        @Schema(description = "基本文件属性")
        public record Info(
                @Schema(description = "sha 256 hash")
                String sha256,
                @Schema(description = "文件名")
                String filename
        ){}
        @Schema(description = "描述性元数据")
        public record Headers(
                @SerializedName("cpu_subtype")
                @Schema(name = "cpu_subtype", description = "处理器子类型")
                String cpuSubtype,
                @SerializedName("cpu_type")
                @Schema(name = "cpu_type", description = "通用类型的处理器")
                String cpuType,
                @SerializedName("file_type")
                @Schema(name = "file_type", description = "文件类型")
                String fileType,
                @Schema(description = "文件标志")
                String flags,
                @Schema(description = "十六进制标识符")
                String magic,
                @SerializedName("num_cmds")
                @Schema(name = "num_cmds", description = "命令数量")
                Integer numCmds,
                @SerializedName("size_cmds")
                @Schema(name = "size_cmds", description = "命令大小")
                Integer sizeCmds
        ){}
        @Schema(description = "片段信息")
        public record Segment(
                @Schema(description = "十六进制格式的段物理地址")
                String fileoff,
                @Schema(description = "十六进制格式的段大小")
                String size,
                @Schema(description = "段名")
                String name,
                @Schema(description = "节信息列表")
                List<Section> sections,
                @Schema(description = "十六进制虚拟地址")
                String vmaddr,
                @Schema(description = "十六进制格式的虚拟地址大小")
                String vmsize
        ){
            @Schema(description = "节")
            public record Section(
                    @Schema(description = "标志")
                    List<String> flags,
                    @Schema(description = "名称")
                    String name,
                    @Schema(description = "类型")
                    String type
            ){}
        }
    }

    @Schema(description = """
                包含存储在 [Info.plist](https://developer.apple.com/documentation/bundleresources/information-property-list) 文件中的信息。
                每个应用和插件都使用一个文件来存储配置文件，系统可以轻松访问。
                OS X 和 iOS 使用文件来决定捆绑包显示哪个图标、支持哪些文档类型，以及许多其他影响捆绑包本身之外的行为。
                返回的字段可能会变化，因为并非所有应用的文件相同，这里映射vt官网给出的常见字段：https://docs.virustotal.com/reference/file-object-ipa-info
                """)
    public record Plist(
            @SerializedName("CBundleIdentifier")
            @Schema(name = "CBundleIdentifier", description = "唯一标识符")
            String cBundleIdentifier,
            @SerializedName("CFBundleSupportedPlatforms")
            @Schema(name = "CFBundleSupportedPlatforms", description = "支持平台列表")
            List<String> cFBundleSupportedPlatforms,
            @SerializedName("CFAppleHelpAnchor")
            @Schema(name = "CFAppleHelpAnchor", description = "捆绑包HTML帮助文件的名称")
            String cFAppleHelpAnchor,
            @SerializedName("CFBundleIcons")
            @Schema(name = "CFBundleIcons", description = "所用图标的信息")
            CFBundleIcons cfBundleIcons,
            @SerializedName("CFBundleInfoDictionaryVersion")
            @Schema(name = "CFBundleInfoDictionaryVersion", description = "捆绑包信息词典版本")
            String cFBundleInfoDictionaryVersion,
            @SerializedName("CFBundleShortVersionString")
            @Schema(name = "CFBundleShortVersionString", description = "版本号或捆绑包的版本号")
            String cFBundleShortVersionString,
            @SerializedName("CFBundleDisplayName")
            @Schema(name = "CFBundleDisplayName", description = "用户可见的捆绑包名称")
            String cFBundleDisplayName,
            @SerializedName("CFBundleName")
            @Schema(name = "CFBundleName", description = "用户可见的捆绑包简称")
            String cFBundleName,
            @SerializedName("CFBundlePackageType")
            @Schema(name = "CFBundlePackageType", description = "类型")
            String cFBundlePackageType,
            @SerializedName("CFBundleSignature")
            @Schema(name = "CFBundleSignature", description = "捆绑签名类似于经典 MacOS 中的“创建者代码”，仅存在于兼容经典 macOS 应用和文档。现代应用无需担心识别捆绑签名。在新的 Xcode 项目中，它初始化为“????”s")
            String cFBundleSignature,
            @SerializedName("CFBundleDevelopmentRegion")
            @Schema(name = "CFBundleDevelopmentRegion", description = "捆绑包的默认语言和地区，作为语言ID")
            String cFBundleDevelopmentRegion,
            @SerializedName("CFBundleExecutable")
            @Schema(name = "CFBundleExecutable", description = "捆绑包可执行文件的名称")
            String cFBundleExecutable,
            @SerializedName("MinimumOSVersion")
            @Schema(name = "MinimumOSVersion", description = "这是该应用在 iOS、tvOS 和 watchOS 上运行所需的最低操作系统版本")
            String minimumOSVersion
    ){
        @Schema(description = "图标的信息")
        public record CFBundleIcons(
                @SerializedName("CFBundleIconFiles")
                @Schema(name = "CFBundleIconFiles", description = "图标文件名")
                List<String> cFBundleIconFiles,
                @SerializedName("CFBundleIconName")
                @Schema(name = "CFBundleIconName", description = "代表应用图标的资产名称")
                String cFBundleIconName
        ){}
    }

    @Schema(description = """
            包含存储在文件中的信息。
            该文件嵌入应用中，是应用被苹果应用商店接受的必要条件。
            配置配置文件用于签署应用，安装和运行设备时必须使用。
            返回的字段可能会变化，因为并非所有应用的配置文件相同。
            这里映射vt官网给出的常见字段：https://docs.virustotal.com/reference/file-object-ipa-info
            """)
    public record Provision(
            @SerializedName("AppIDName")
            @Schema(name = "AppIDName", description = "应用ID名称")
            String appIDName,
            @SerializedName("ApplicationIdentifierPrefix")
            @Schema(name = "ApplicationIdentifierPrefix", description = "运行应用的代码签名标识符")
            List<String> applicationIdentifierPrefix,
            @SerializedName("CreationDate")
            @Schema(name = "CreationDate", description = "应用创建日期。%Y-%m-%d %H:%M:%S")
            String creationDate,
            @SerializedName("Entitlements")
            @Schema(name = "Entitlements", description = "允许特定功能或选择应用加入特定服务")
            Entitlements entitlements,
            @SerializedName("ExpirationDate")
            @Schema(name = "ExpirationDate", description = "应用的有效期。%Y-%m-%d %H:%M:%S")
            String expirationDate,
            @SerializedName("Name")
            @Schema(name = "Name", description = "应用名称")
            String name,
            @SerializedName("Platform")
            @Schema(name = "Platform", description = "支持的平台")
            List<String> platform,
            @SerializedName("TeamIdentifier")
            @Schema(name = "TeamIdentifier", description = "团队标识")
            List<String> teamIdentifier,
            @SerializedName("TeamName")
            @Schema(name = "TeamName", description = "团队名称")
            String teamName,
            @SerializedName("TimeToLive")
            @Schema(name = "TimeToLive", description = "应用有效天数")
            Integer timeToLive,
            @Schema(description = "唯一标识符")
            String UUID,
            @SerializedName("Version")
            @Schema(name = "Version", description = "应用版本")
            String version
    ){
        @Schema(description = "允许特定功能或选择应用加入特定服务")
        public record Entitlements(
                @SerializedName("application-identifier")
                @Schema(name = "application-identifier", description = "完整的应用程序标识符")
                String applicationIdentifier,
                @SerializedName("get-task-allow")
                @Schema(name = "get-task-allow", description = "get-task-allow")
                Boolean getTaskAllow,
                @SerializedName("keychain-access-groups")
                @Schema(name = "keychain-access-groups", description = "应用可能共享物品的钥匙串组标识符")
                List<String> keychainAccessGroups
        ){}
    }
}
