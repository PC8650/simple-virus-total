package com.vt.atp.dto.vt.file;

import com.google.gson.annotations.SerializedName;
import com.vt.atp.dto.vt.Link;
import com.vt.atp.dto.vt.file.sub.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "文件行为报告响应")
public record FileBehaviourReportResp(

        @Schema(description = "<sha256>_<sandboxName>")
        String id,

        @Schema(description = "报告类型")
        String type,

        @Schema(description = "结果链接")
        Link links,

        @Schema(description = "文件对象属性")
        Attributes attributes
) {

    public record Attributes(

            @SerializedName("sandbox_name")
            @Schema(name = "sandbox_name", description = "沙箱名称")
            String sandboxName,

            @SerializedName("analysis_date")
            @Schema(name = "analysis_date", description = "Unix时代UTC时间（秒）")
            Long analysisDate,

            @Schema(description = "用于寻找类似的行为分析")
            String behash,

            @SerializedName("calls_highlighted")
            @Schema(name = "calls_highlighted", description = "值得重点强调的API调用/系统调用")
            List<String> callsHighlighted,

            @SerializedName("command_executions")
            @Schema(name = "command_executions", description = "在分析给定文件时观察到的shell命令执行情况")
            List<String> commandExecutions,

            @SerializedName("files_opened")
            @Schema(name = "files_opened", description = "执行过程中打开的文件")
            List<String> filesOpened,

            @SerializedName("files_written")
            @Schema(name = "files_written", description = "执行过程中写入的文件")
            List<String> filesWritten,

            @SerializedName("files_deleted")
            @Schema(name = "files_deleted", description = "执行过程中删除的文件")
            List<String> filesDeleted,

            @SerializedName("files_attribute_changed")
            @Schema(name = "files_attribute_changed", description = "文件的完整路径，这些文件可进行某种主动属性修改")
            List<String> filesAttributeChanged,

            @SerializedName("has_html_report")
            @Schema(name = "has_html_report", description = "是否有用于此行为分析的HTML报告。更多信息访问 /file_behaviours/{sandbox_id}/html")
            Boolean hasHtmlReport,

            @SerializedName("has_evtx")
            @Schema(name = "has_evtx", description = "是否有用于此行为分析的 EVTX 文件。更多信息请访问 /file_behaviours/{sandbox_id}/evtx")
            Boolean hasEvtx,

            @SerializedName("has_memdump")
            @Schema(name = "has_memdump", description = "是否有用于此行为分析的memdump文件。查看/file_behaviours/{sandbox_id}/memdump")
            Boolean hasMemdump,

            @SerializedName("has_pcap")
            @Schema(name = "has_pcap", description = "是否存在用于此行为分析的PCAP网络捕获。更多信息请访问 /file_behaviours/{sandbox_id}/pcap")
            Boolean hasPcap,

            @SerializedName("memory_dumps")
            @Schema(name = "memory_dumps", description = "内存转储信息。如果存在")
            List<MemoryDump> memoryDumps,

            @SerializedName("processes_tree")
            @Schema(name = "processes_tree", description = "进程树")
            List<ProcessTreeContent> processTree,

            @Schema(description = "恶意行为目录")
            List<MalwareBehaviorCatalog> mbc,

            @SerializedName("dns_lookups")
            @Schema(name = "dns_lookups", description = "dns查询列表")
            List<DnsLookup> dnsLookups,

            @SerializedName("hosts_file")
            @Schema(name = "hosts_file", description = "存储本地 hostname-ip 映射 hosts 文件的内容，当且仅当该文件被修改时，否则该字段未被填充")
            String hostsFile,

            @SerializedName("ids_alerts")
            @Schema(name = "ids_alerts", description = "IDS警报列表，按时间戳排序")
            List<IdsAlert> idsAlerts,

            @SerializedName("processes_created")
            @Schema(name = "processes_created", description = "在执行给定文件时新建进程的名称")
            List<String> processesCreate,

            @SerializedName("processes_terminated")
            @Schema(name = "processes_terminated", description = "在执行给定文件时终止进程的名称")
            List<String> processesTerminated,

            @SerializedName("processes_killed")
            @Schema(name = "processes_killed", description = "在执行给定文件时被终止的进程名称")
            List<String> processesKilled,

            @SerializedName("processes_injected")
            @Schema(name = "processes_injected", description = "在执行给定文件时被某种代码注入的进程名称")
            List<String> processesInjected,

            @SerializedName("services_opened")
            @Schema(name = "services_opened", description = "在分析给定文件时获得句柄的服务名称")
            List<String> servicesOpened,

            @SerializedName("services_created")
            @Schema(name = "services_created", description = "新建服务")
            List<String> servicesCreated,

            @SerializedName("services_started")
            @Schema(name = "services_started", description = "新服务启动")
            List<String> servicesStarted,

            @SerializedName("services_stopped")
            @Schema(name = "services_stopped", description = "服务在执行给定文件时停止")
            List<String> servicesStopped,

            @SerializedName("services_deleted")
            @Schema(name = "services_deleted", description = "在执行给定文件时被删除的服务")
            List<String> servicesDeleted,

            @SerializedName("services_bound")
            @Schema(name = "services_bound", description = "服务绑定，主要在 Android 中，参见：https://developer.android.com/guide/components/bound-services.html")
            List<String> servicesBound,

            @SerializedName("windows_searched")
            @Schema(name = "windows_searched", description = "被搜索的窗口名称")
            List<String> windowsSearched,

            @SerializedName("windows_hidden")
            @Schema(name = "windows_hidden", description = "设置为不可见的窗口名称")
            List<String> windowsHidden,

            @SerializedName("mutexes_opened")
            @Schema(name = "mutexes_opened", description = "文件获得句柄的互斥组名称")
            List<String> mutexesOpened,

            @SerializedName("mutexes_created")
            @Schema(name = "mutexes_created", description = "新建互斥组")
            List<String> mutexesCreated,

            @SerializedName("signals_observed")
            @Schema(name = "signals_observed", description = "操作系统信号和广播事件，请注意 Android 广播也在此分类")
            List<String> signalsObserved,

            @Schema(description = "通过反射或某种运行时实例调用的方法/功能。最好的例子是 Java 反射调用，在这种情况下会将结构化为字符串")
            List<String> invokes,

            @SerializedName("crypto_algorithms_observed")
            @Schema(name = "crypto_algorithms_observed", description = "观察到的加密算法。示例：RSA")
            List<String> cryptoAlgorithmsObserved,

            @SerializedName("crypto_keys")
            @Schema(name = "crypto_keys", description = "加密密钥")
            List<String> cryptoKeys,

            @SerializedName("crypto_plain_text")
            @Schema(name = "crypto_plain_text", description = "在观察到的时间段内被加密或解码的字符串，只记录明文")
            List<String> cryptoPlainText,

            @SerializedName("text_decoded")
            @Schema(name = "text_decoded", description = "明文，解码操作的结果")
            List<String> textDecoded,

            @SerializedName("text_highlighted")
            @Schema(name = "text_highlighted", description = "窗口对话框、标题等中出现的有趣文本")
            List<String> textHighlighted,

            @SerializedName("verdict_confidence")
            @Schema(name = "verdict_confidence", description = "结论信心百分比")
            Integer verdictConfidence,

            @SerializedName("ja3_digests")
            @Schema(name = "ja3_digests", description = "TLS客户端连接的JA3指纹识别")
            List<String> ja3Digests,

            @Schema(description = "联系域名/IP证书")
            List<Tls> tls,

            @Schema(description = "沙箱行为裁决列表")
            List<String> verdicts,

            @SerializedName("sigma_analysis_results")
            @Schema(name = "sigma_analysis_results", description = "汇总Sigma分析所有沙盒生成的EVTX文件结果")
            List<SigmaAnalysisResult> sigmaAnalysisResults,

            @SerializedName("signature_matches")
            @Schema(name = "signature_matches", description = "匹配签名的汇总列表")
            List<SignatureMatches> signatureMatches,

            @SerializedName("mitre_attack_techniques")
            @Schema(name = "mitre_attack_techniques", description = "堆叠攻击技术汇总列表")
            List<MitreAttackTechnique> mitreAttackTechniques,

            //Android专用字段

            @SerializedName("activities_started")
            @Schema(name = "activities_started", description = "由研究应用启动的Android活动")
            List<String> activitiesStarted,

            @SerializedName("content_model_observers")
            @Schema(name = "content_model_observers", description = "Android应用会注册逻辑以告知其任何变更")
            List<String> contentModelObservers,

            @SerializedName("content_model_sets")
            @Schema(name = "content_model_sets", description = "由Android应用执行的内容模型条目")
            List<Map<String, Object>> contentModelSets,

            @SerializedName("databases_deleted")
            @Schema(name = "databases_deleted", description = "删除的数据库，例如，已删除Android SQLite DB")
            List<String> databasesDeleted,

            @SerializedName("databases_opened")
            @Schema(name = "databases_opened", description = "与数据库的交互，例如当Android应用打开SQLite数据库时")
            List<String> databasesOpened,

            @SerializedName("permissions_requested")
            @Schema(name = "permissions_requested", description = "应用在运行时请求的Android权限。在Windows中，它还应记录进程令牌权限的修改，如SE_LOAD_DRIVER_PRIVILEGE")
            List<String> permissionsRequested,

            @SerializedName("shared_preferences_lookups")
            @Schema(name = "shared_preferences_lookups", description = "在Android共享偏好设置中被勾选的条目（https://developer.android.com/reference/android/content/SharedPreferences.html）")
            List<String> sharedPreferencesLookups,

            @SerializedName("shared_preferences_sets")
            @Schema(name = "shared_preferences_sets", description = """
                在 Android 共享偏好设置中写入的条目
                key： 偏好名称
                value： 集合值
                """)
            List<Map<String, String>> sharedPreferencesSets,

            @SerializedName("signals_hooked")
            @Schema(name = "signals_hooked", description = "在 Android 中注册接收器被视为广播钩子。在 Windows 中，该字段将包含活动等内容，如 SetWindowsHookExA")
            List<String> signalsHooked,

            @SerializedName("system_property_lookups")
            @Schema(name = "system_property_lookups", description = "与Android系统属性数据集（getInt、getString、putInt、putString等）的交互都被简单地翻译成字符串。如 android.os.SystemProperties")
            List<String> systemPropertyLookups,

            @SerializedName("system_property_sets")
            @Schema(name = "system_property_sets", description = "Android系统属性数据集中的键和值设置")
            List<Map<String, Object>> systemPropertySets,

            //Windows专用字段

            @SerializedName("modules_loaded")
            @Schema(name = "modules_loaded", description = "与库、共享对象和组件动态加载相关的操作")
            List<String> modulesLoaded,

            @SerializedName("registry_keys_opened")
            @Schema(name = "registry_keys_opened", description = "获取句柄的Windows注册表键")
            List<String> registryKeysOpened,

            @SerializedName("registry_keys_set")
            @Schema(name = "registry_keys_set", description = """
                键和注册表键的值
                key： 修改过的注册表键。
                value： 的值设置为注册表键。
                """)
           List<Map<String, String>> registryKeysSet,

            @SerializedName("registry_keys_deleted")
            @Schema(name = "registry_keys_deleted", description = "被删除的Windows注册表键名称")
            List<String> registryKeysDeleted
    ){}
}
