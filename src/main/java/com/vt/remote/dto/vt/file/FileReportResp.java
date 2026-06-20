package com.vt.remote.dto.vt.file;

import com.google.gson.annotations.SerializedName;
import com.vt.remote.dto.vt.AnalyseResult;
import com.vt.remote.dto.vt.AnalyseStats;
import com.vt.remote.dto.vt.Link;
import com.vt.remote.dto.vt.Vote;
import com.vt.remote.dto.vt.file.sub.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "文件报告响应")
public record FileReportResp(

        @Schema(description = "文件id，f-<sha256>-<timestamp>")
        String id,

        @Schema(description = "报告对象类型")
        String type,

        @Schema(description = "结果链接")
        Link links,

        @Schema(description = "文件对象属性")
        Attributes attributes
) {

    @Schema(name = "文件对象属性", description = "https://docs.virustotal.com/reference/files")
    public record Attributes(

            @Schema(description = "文件的 MD5 哈希值")
            String md5,

            @SerializedName("meaningful_name")
            @Schema(name = "meaningful_name", description = "所有文件名中最有趣的一个名称")
            String meaningfulName,

            @Schema(description = "文件大小(bytes)")
            Integer size,

            @Schema(description = "是一个用于计算上下文触发分段哈希的程序。也称为模糊哈希，它允许通过比较（通过编辑距离）哈希值来识别相似文件")
            String ssdeep,

           @SerializedName("first_submission_date")
           @Schema(name = "first_submission_date", description = "文件首次在VirusTotal中被发现的日期。UTC时间戳")
            Long firstSubmissionDate,

            @SerializedName("last_submission_date")
            @Schema(name = "last_submission_date", description = "文件最近一次上传到 VirusTotal 的日期。UTC 时间戳")
            Long lastSubmissionDate,

            @Schema(description = "根据 Unix 系统中常用的解析工具，猜测文件类型")
            String magic,

            @Schema(description = "文件得分，由 VirusTotal 社区所有投票计算得出。要了解更多关于信誉度计算方法的信息，请查看https://docs.virustotal.com/docs/community")
            Integer reputation,

            @SerializedName("last_analysis_date")
            @Schema(name = "last_analysis_date", description = "最近一次扫描日期。UTC 时间戳")
            Long lastAnalysisDate,

            @SerializedName("last_analysis_stats")
            @Schema(name = "last_analysis_stats", description = "最新扫描结果摘要")
            AnalyseStats lastAnalysisStats,

            @Schema(description = "基于简单结构特征哈希的内部相似性聚类算法值，可让您找到相似的文件")
            String vhash,

            @SerializedName("type_description")
            @Schema(name = "type_description", description = "描述文件类型")
            String typeDescription,

            @SerializedName("type_tag")
            @Schema(name = "type_tag", description = "标签，表示文件类型。可用于在VirusTotal智能搜索中按文件类型进行筛选")
            String typeTag,

            @Schema(description = "是一款旨在根据文件二进制签名识别文件类型的实用程序。它可能会给出多个检测结果，并按文件格式识别概率从高到低排序（以百分比表示）")
            List<TrId> trid,

            @Schema(description = "代表性属性列表")
            List<String> tags,

            @Schema(description = "图标的相关哈希值")
            FileCondis filecondis,

            @SerializedName("unique_sources")
            @Schema(name = "unique_sources", description = "该文件从多少个不同的来源发布")
            Integer uniqueSources,

            @Schema(description = "文件的 TLSH 哈希值")
            String tlsh,

            @Schema(description = "与该文件关联的所有文件名")
            List<String> names,

            @SerializedName("type_tags")
            @Schema(name = "type_tags", description = "包含与特定文件类型相关的更广泛的标签，例如，对于 DLL 文件，此列表将包含 executable、windows、win32、pe、pedll。可用于在VirusTotal 智能搜索中进行筛选，所有类型标签都会添加到 _type搜索修饰符中")
            List<String> typeTags,

            @SerializedName("last_modification_date")
            @Schema(name = "last_modification_date", description = "对象最后修改的日期。UTC 时间戳")
            Long lastModificationDate,

            @SerializedName("last_analysis_results")
            @Schema(name = "last_analysis_results", description = "最新扫描结果。引擎名称-结果")
            Map<String, AnalyseResult> lastAnalysisResults,

            @SerializedName("bundle_info")
            @Schema(name = "bundle_info", description = "提供有关压缩文件（ZIP、RAR、GZIP 等）的信息")
            BundleInfo bundleInfo,

            @SerializedName("total_votes")
            @Schema(name = "total_votes", description = "社区投票总数的未加权值，分为“无害”和“恶意”两类")
            Vote totalVotes,

            @Schema(description = "文件的 SHA256 哈希值")
            String sha256,

            @Schema(description = "文件的 SHA1 哈希值")
            String sha1,

            @SerializedName("type_extension")
            @Schema(name = "type_extension", description = "文件扩展名")
            String typeExtension,

            @SerializedName("times_submitted")
            @Schema(name = "times_submitted", description = "文件被发布到 VirusTotal 的次数")
            Integer timesSubmitted,

            @Schema(description = "Google Magika 模型识别的文件类型信息")
            String magika,

            @SerializedName("pe_info")
            @Schema(name = "pe_info", description = "Microsoft Windows 便携式可执行文件格式信息。为避免报告数量过多，我们将条目数量限制为 256 个")
            PeInfo peInfo,

            @Schema(description = "Detect It Easy（简称“DIE”）是一个用于确定文件类型的程序。该程序定义了MSDOS、PE、ELF、MACH和二进制文件类型")
            DetectItEasy detectiteasy,

            @Schema(description = "微软用于验证 PE 映像文件相关部分是否被篡改的SHA256 哈希值。微软 AppLocker 使用的就是这种哈希值")
            String authentihash,

            @SerializedName("creation_date")
            @Schema(name = "creation_date", description = "尽可能从文件元数据中提取。指示文件的构建或编译时间。恶意软件制造者也可以伪造此值。UTC 时间戳")
            Long creationDate,

            @SerializedName("signature_info")
            @Schema(name = "signature_info", description = "有关 Windows 可执行文件和 Mach-O 文件数字签名的信息")
            SignatureInfo signatureInfo,

            @SerializedName("sandbox_verdicts")
            @Schema(name = "sandbox_verdicts", description = "所有沙箱测试结果摘要。沙箱名称-摘要")
            Map<String, SandboxVerdict> sandboxVerdicts,

            @SerializedName("capabilities_tags")
            @Schema(name = "capabilities_tags",description = "与文件功能相关的代表性标签列表。仅限高级 API 用户使用")
            List<String> capabilitiesTags,

            @Schema(description = "如果文件可以下载则为 true，否则为 false。仅适用于高级 API 用户")
            Boolean downloadable,

            @Schema(description = "文件的永久哈希")
            String permhash,

            @SerializedName("sigma_analysis_summary")
            @Schema(name = "sigma_analysis_summary", description = "包含按严重程度分组的匹配 sigma 规则的数量，与按规则集分组的字典相同。字典的键是规则集名称，值是该特定规则集的统计信息")
            Map<String, SigmaAnalysis> sigmaAnalysisSummary,

            @SerializedName("sigma_analysis_stats")
            @Schema(name = "sigma_analysis_stats", description = "包含按严重程度分组的匹配 sigma 规则的统计信息")
            SigmaAnalysis sigmaAnalysisStats,

            @SerializedName("crowdsourced_ai_results")
            @Schema(name = "crowdsourced_ai_results", description = "所有众包人工智能结果的摘要")
            List<CrowdsourcedAiResult> crowdsourcedAiResults,

            @SerializedName("crowdsourced_ids_stats")
            @Schema(name = "crowdsourced_ids_stats", description = "ids 统计结果")
            CrowdsourcedIdsStats crowdsourcedIdsStats,

            @SerializedName("crowdsourced_ids_results")
            @Schema(name = "crowdsourced_ids_results", description = "文件的 IDS 匹配结果")
            List<CrowdsourcedIdsResult> crowdsourcedIdsResults,

            @SerializedName("crowdsourced_yara_results")
            @Schema(name = "crowdsourced_yara_results", description = "根据众包规则匹配的 YARA")
            List<CrowdsourcedYaraResult> crowdsourcedYaraResults,

            @SerializedName("threat_verdict")
            @Schema(name = "threat_verdict", description = """
                    威胁判决
                    VERDICT_UNKNOWN我们无法对该实体作出裁决。
                    VERDICT_UNDETECTED没有立即证据表明存在恶意意图。
                    VERDICT_SUSPICIOUS检测到疑似恶意活动，需要进一步调查。
                    VERDICT_MALICIOUS高度确信该实体构成威胁。
                    """)
            String threatVerdict,

            @SerializedName("threat_severity")
            @Schema(name = "threat_severity", description = "威胁验证程度。VT Enterprise users only")
            ThreatSeverity threatSeverity,

            @SerializedName("popular_threat_classification")
            @Schema(name = "popular_threat_classification", description = "威从反病毒软件判定和聚类哈希中提取的人类可读名称")
            PopularThreatClassification popularThreatClassification,

            @SerializedName("traffic_inspection")
            @Schema(name = "traffic_inspection", description = "从 PCAP 网络捕获中提取的流量概念")
            TrafficInspection trafficInspection,

            @Schema(description = "PCAP 网络捕获匹配的Suricata警报（新兴威胁 ETPro 规则集）列表。key - 规则名称，value - 包含警报详细信息的字典")
            Map<String, Suricata> suricata,

            @SerializedName("known_distributors")
            @Schema(name = "known_distributors", description = "有关文件分发者的信息")
            KnownDistributors knownDistributors,

            @SerializedName("link_info")
            @Schema(name = "link_info", description = "关于 Microsoft Windows LNK 文件的信息")
            LinkInfo linkInfo,

            @SerializedName("class_info")
            @Schema(name = "class_info", description = "关于 Java .class 字节码文件的信息")
            ClassInfo classInfo,

            @SerializedName("jar_info")
            @Schema(name = "jar_info", description = "关于 Java jar 文件的信息关")
            JarInfo jarInfo,

            @SerializedName("deb_info")
            @Schema(name = "deb_info", description = "关于Debian软件包的信息")
            DebInfo debInfo,

            @Schema(description = "有关 Android 文件的信息")
            Androguard androguard,

            @SerializedName("dmg_info")
            @Schema(name = "dmg_info", description = "关于可挂载macOS磁盘镜像的信息")
            DmgInfo dmgInfo,

            @SerializedName("ipa_info")
            @Schema(name = "ipa_info", description = "关于 IOS App Store 包文件的信息")
            IpaInfo ipaInfo,

            @SerializedName("html_info")
            @Schema(name = "html_info", description = "HTML文件中的信息")
            HtmlInfo htmlInfo,

            @SerializedName("elf_info")
            @Schema(name = "elf_info", description = "关于Unix ELF文件的信息")
            ElfInfo elfInfo,

            @SerializedName("dot_net_assembly")
            @Schema(name = "dot_net_assembly", description = "关于Microsoft .NET文件的信息")
            DotNetAssembly dotNetAssembly,

            @SerializedName("dot_net_guids")
            @Schema(name = "dot_net_guids", description = "Microsoft .NET 程序集的标识符")
            DotNetGuid dotNetGuids,

            @Schema(description = """
                    识别多种工具和反病毒软件在 Windows PE 文件上使用的加壳程序。
                    key工具名称，value是识别的加壳器，都是字符串
                    """)
            Map<String, String> packers,

            @SerializedName("java_scriptInfo")
            @Schema(name = "java_scriptInfo", description = "从 Javascript 文件中提取的信息")
            JavascriptInfo javascriptInfo,

            @SerializedName("pdf_info")
            @Schema(name = "pdf_info", description = "有关 Adobe PDF 文件的信息")
            PdfInfo pdfInfo,

            @SerializedName("vba_info")
            @Schema(name = "vba_info", description = "VBA宏信息")
            VbaInfo vbaInfo,

            @SerializedName("powershell_info")
            @Schema(name = "powershell_info", description = "Powershell 文件信息")
            PowershellInfo powershellInfo,

            @Schema(description = "Wireshark 在处理文件时生成的元数据")
            Wireshark wireshark

    ) {}

}
