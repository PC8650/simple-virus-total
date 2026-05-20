# AI 总结分析请求数据结构 (ReportContent JSON 规范)

本页面记录了 `SummaryAdvisor` 组件在将 VirusTotal 扫描结果发送给 Google GenAI 大模型做“安全科普式总结”时所组装的 **`ReportContent`** 数据负载（即 `gson.toJson(reportContent)` 的产物）。

根据项目的 4 个分析维度（`FILE`、`URL`、`IP`、`DOMAIN`），以下分别展示其完整的 Mock JSON 数据。JSON 中包含内联的注释说明，用以详细解释每个关键字段的含义及其在数据库/系统中的安全背景。

---

## 1. FILE (文件) 维度

文件维度的 JSON 数据最为庞杂，它不仅包含基础的文件元数据与多引擎扫描结果（`report`），还包含了沙箱动态行为追踪报告列表（`behaviour`）以及行为中提取出来的 MITRE ATT&CK 战术与技术树汇总（`mitre`）。

```json5
{
  "type": "FILE", // 分析维度类型，对应 TypeEnum.FILE
  "url": "https://www.virustotal.com/gui/file/4a86bba41a6b0c25a0734c56bbd011152a0734c56bbd011152a0734c56bbd0111", // 该文件在 VirusTotal 上的官方 Web 报告访问地址
  "report": { // 核心报告数据，对应 FileReportResp 类
    "id": "4a86bba41a6b0c25a0734c56bbd011152a0734c56bbd011152a0734c56bbd0111", // 文件的 SHA-256 唯一标识符
    "type": "file", // VT 内部对象类型
    "links": {
      "self": "https://www.virustotal.com/api/v3/files/4a86bba41a6b0c25a0734c56bbd011152a0734c56bbd011152a0734c56bbd0111" // 自查询 API 端点
    },
    "attributes": { // 对应 FileReportResp.Attributes 类
      "md5": "e99a18c428cb38d5f260853678922e03", // 文件的 MD5 哈希
      "sha1": "c12a38c428cb38d5f260853678922e0378922e03", // 文件的 SHA-1 哈希
      "sha256": "4a86bba41a6b0c25a0734c56bbd011152a0734c56bbd011152a0734c56bbd0111", // 文件的 SHA-256 哈希
      "meaningful_name": "malicious_payload.exe", // 系统推荐的“最具代表性”文件名
      "names": [ // 该文件在历史上传中被使用过的所有文件名列表
        "malicious_payload.exe",
        "update_patch.exe",
        "invoice_9821.exe"
      ],
      "size": 1048576, // 文件大小，单位为字节 (Bytes)
      "ssdeep": "24576:gD1k32k...:gD1k32k...", // 模糊哈希 (Fuzzy Hash)，用于识别结构相似的文件
      "tlsh": "T13A15...8D9", // Trend Micro Locality Sensitive Hash，用于恶意软件聚类
      "vhash": "012056655d1215z201z", // VirusTotal 内部相似度聚类哈希
      "first_submission_date": 1715848200, // 首次被提交到 VT 的时间戳 (UTC 秒)
      "last_submission_date": 1715849800, // 最近一次提交到 VT 的时间戳 (UTC 秒)
      "last_modification_date": 1715850000, // 对象在 VT 数据库中最后的修改时间戳
      "last_analysis_date": 1715849900, // 最近一次引擎扫描时间戳
      "times_submitted": 42, // 该文件总共被用户上传提交的次数
      "unique_sources": 15, // 该文件来自于多少个不同的上传源 (IP 或 API 账户)
      "magic": "PE32 executable (GUI) Intel 80386, for MS Windows", // Unix magic 工具猜测的文件底层格式
      "type_description": "Win32 Executable", // 文件类型直观描述
      "type_tag": "peexe", // 过滤用的文件类型标签
      "type_extension": "exe", // 推荐的文件后缀名
      "type_tags": [ // 更宽泛的属性标签集合，利于大模型提取运行环境
        "executable",
        "windows",
        "win32",
        "pe"
      ],
      "tags": [ // 文件的代表性特征标签，如加壳、包含签名等
        "packed",
        "peauth",
        "signed"
      ],
      "reputation": -85, // VT 社区信誉积分（负数表示倾向于恶意，正数表示良性）
      "total_votes": { // 社区未加权的投票结果 (对应 Vote 类)
        "harmless": 2, // 投无害票的数量
        "malicious": 85 // 投恶意票的数量
      },
      "last_analysis_stats": { // 最新扫描结果统计 (对应 AnalyseStats 类)
        "malicious": 58, // 判定为恶意的防病毒引擎数量
        "suspicious": 2, // 判定为可疑的防病毒引擎数量
        "harmless": 10, // 判定为安全的引擎数量
        "undetected": 5, // 未发现异常的引擎数量
        "timeout": 0, // 超时的引擎数
        "confirmed-timeout": 0,
        "failure": 0,
        "type-unsupported": 0
      },
      "last_analysis_results": { // 各扫描引擎的明细映射表 (Map<String, AnalyseResult>)
        "Kaspersky": {
          "method": "blacklist", // 检出方法（如黑名单、启发式等）
          "engine_name": "Kaspersky", // 引擎名称
          "engine_version": "21.3.10.391", // 引擎版本
          "engine_update": "20260520", // 引擎特征库更新日期
          "category": "malicious", // 判定类型 (malicious / suspicious / harmless / undetected)
          "result": "Trojan.Win32.Generic" // 引擎给出的具体恶意家族或类别名称
        },
        "CrowdStrike": {
          "method": "heuristic",
          "engine_name": "CrowdStrike",
          "engine_version": "1.0",
          "engine_update": "20260520",
          "category": "malicious",
          "result": "win/malicious.generic"
        }
      },
      "trid": [ // 二进制文件特征概率识别结果 (对应 TrId 类)
        {
          "file_type": "Win32 Executable MS Visual C++ (generic)", // 识别出来的文件类型
          "probability": 78.4 // 该识别结果的可信度百分比
        }
      ],
      "detectiteasy": { // Detect It Easy (DIE) 的静态分析输出
        "file_format": "PE", // 文件格式
        "compiler": "Microsoft Visual C++(2015-2022)", // 使用的编译器
        "packer": "UPX (3.96)[Modified]" // 识别到的壳/保护工具（这常被恶意程序用于逃避静态查杀）
      },
      "authentihash": "93a61f22e86b...", // 微软 AppLocker 用于防篡改验证的哈希值
      "creation_date": 1715817600, // 从文件头部或 PE 元数据中提取的编译时间戳（可被攻击者伪造）
      "signature_info": { // 数字签名详情 (对应 SignatureInfo 类)
        "authority": "GlobalSign", // 签名授权颁发机构
        "description": "Valid signature", // 签名状态描述
        "verified": "Unsigned" // 实际校验状态（若签名无效或伪造，此处可辅助分析）
      },
      "downloadable": true, // 高级 API 账号是否可以从 VT 下载此样本二进制流
      "permhash": "9cfc6d2d48...", // 文件的永久哈希值
      "sandbox_verdicts": { // 多沙箱自动化运行的裁决结果汇总 (Map<String, SandboxVerdict>)
        "VirusTotal Juxta": {
          "sandbox_name": "VirusTotal Juxta",
          "malware_classification": [ "TROJAN" ], // 恶意行为类别划分
          "confidence": 98, // 判定信心指数 (0-100)
          "severity": "HIGH" // 严重性等级
        }
      },
      "capabilities_tags": [ // 文件具备的行为能力标签列表
        "modifies-registry",
        "contacts-remote-host",
        "injects-code"
      ],
      "sigma_analysis_summary": { // 触发的 Sigma 防御检测规则汇总统计 (Map<String, SigmaAnalysis>)
        "Security-Alerts": {
          "critical": 1, // 触发的高危规则数
          "high": 3,
          "medium": 5,
          "low": 0
        }
      },
      "sigma_analysis_stats": { // 触发 Sigma 规则的严重性细节统计
        "critical": 1,
        "high": 3,
        "medium": 5,
        "low": 0
      },
      "crowdsourced_ai_results": [ // 众包人工智能对样本的分析概要 (对应 CrowdsourcedAiResult 类)
        {
          "title": "AI Behavior Insight", // 标题
          "source": "Google Security AI", // AI 提供方
          "details": "This file is associated with registry manipulation and potential credential dumping." // AI 总结的详细行为描述
        }
      ],
      "threat_verdict": "VERDICT_MALICIOUS" // VT 对实体的最终安全定性 (MALICIOUS/SUSPICIOUS/UNDETECTED/UNKNOWN)
    }
  },
  "behaviour": [ // 文件的动态行为分析报告列表（List<FileBehaviourReportResp>）
    {
      "id": "4a86bba41a6b0c25a0734c56bbd011152a0734c56bbd011152a0734c56bbd0111_VirusTotal Zenbox", // 行为报告唯一ID (SHA256_沙箱名)
      "type": "file_behaviour", // 对象类型
      "attributes": { // 沙箱行为核心属性
        "sandbox_name": "VirusTotal Zenbox", // 执行该样本的沙箱环境名称
        "analysis_date": 1715849920, // 沙箱执行的时间戳 (UTC)
        "verdict_confidence": 95, // 动态裁决的置信度 (百分比)
        "verdicts": [ // 沙箱运行结论
          "malicious"
        ],
        "calls_highlighted": [ // 提取出的敏感高危 API 监控记录
          "RegCreateKeyExW",
          "VirtualAllocEx",
          "WriteProcessMemory"
        ],
        "command_executions": [ // 沙箱中观察到的 shell 命令行调用
          "cmd.exe /c powershell.exe -ExecutionPolicy Bypass -File C:\\Users\\Public\\temp.ps1"
        ],
        "files_opened": [ // 样本运行期间尝试打开的文件路径
          "C:\\Windows\\System32\\ntdll.dll",
          "C:\\Users\\Administrator\\AppData\\Local\\Temp\\payload.dat"
        ],
        "files_written": [ // 样本写入（Drop）到本地磁盘的文件路径
          "C:\\Users\\Public\\temp.ps1"
        ],
        "files_deleted": [ // 样本在退出前尝试删除的文件以销毁证据
          "C:\\Users\\Public\\temp.ps1"
        ],
        "files_attribute_changed": [], // 发生属性变更（如修改为只读、隐藏）的文件
        "processes_created": [ // 样本衍生（Spawn）的新子进程
          "powershell.exe"
        ],
        "processes_terminated": [ // 样本主动杀死或被终止的进程
          "cmd.exe"
        ],
        "processes_injected": [ // 被样本进行远程内存注入的宿主目标进程（用于隐藏行为）
          "explorer.exe"
        ],
        "registry_keys_opened": [ // 读取的注册表分支
          "HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run"
        ],
        "registry_keys_set": [ // 修改或新增的注册表键值（多用于实现开机持久化自启）
          {
            "key": "HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run\\Updater",
            "value": "C:\\Users\\Public\\temp.ps1"
          }
        ],
        "registry_keys_deleted": [], // 样本删除的注册表键值
        "mutexes_created": [ // 创建的防多开互斥体（Mutext），是排查木马特征的重要依据
          "Global\\MalwareSessionMutex_1102"
        ],
        "crypto_algorithms_observed": [ // 监控到的加密算法（常见于勒索软件对文件加密，或网络远控流的加密）
          "AES",
          "RSA"
        ],
        "crypto_keys": [], // 捕获到的密钥明文
        "crypto_plain_text": [ // 捕获到的加密前明文
          "client_hello_message"
        ],
        "dns_lookups": [ // 沙箱中发起的所有域名 DNS 查询行为记录 (对应 DnsLookup 类)
          {
            "hostname": "attacker-c2.com", // 查询的目标域名
            "type": "A", // 查询记录类型
            "resolved_ips": [ "192.168.99.100" ] // 域名解析出的 IP
          }
        ],
        "ip_traffic": [ // 网络流量记录，包含目标IP和端口 (对应 IpTraffic 类)
          {
            "destination_ip": "192.168.99.100", // 目标远控 IP
            "destination_port": 4444, // 远控回连端口 (如 Metasploit 默认的 4444)
            "transport_protocol": "TCP" // 协议
          }
        ],
        "ja3_digests": [ // TLS 握手特征指纹，用于识别远控通信协议工具
          "771aa10344d9f67a216f0cf7e2f5b61a"
        ],
        "mitre_attack_techniques": [ // 沙箱中触发的 MITRE 技术清单 (对应 MitreAttackTechnique 类)
          {
            "id": "T1059.001", // 技术 ID
            "severity": "HIGH", // 严重级别
            "signature_description": "Runs a PowerShell script from a command-line interface", // 触发该技术条目的签名描述
            "refs": [ // 参考资料
              {
                "ref": "mitre_attack",
                "value": "https://attack.mitre.org/techniques/T1059/001"
              }
            ]
          }
        ],
        "files_dropped": [ // 沙箱运行期间释放的落盘文件特征 (对应 FileDropped 类)
          {
            "path": "C:\\Users\\Public\\temp.ps1", // 路径
            "sha256": "81f1c2d3a4b...", // SHA-256
            "size": 512, // 大小 (字节)
            "type_description": "ASCII text"
          }
        ],
        "has_pcap": true, // 是否可向 VT 请求提供该沙箱运行的网络包 PCAP 下载
        "has_memdump": false, // 是否存在内存转储
        "has_html_report": true
      }
    }
  ],
  "mitre": { // MITRE 映射聚合字典，键为沙箱名称，值为该沙箱的战术树汇总 (Map<String, FileMitreResp>)
    "VirusTotal Zenbox": {
      "tactics": [ // 触发的 MITRE 战术大类列表
        {
          "id": "TA0002", // 战术编号
          "name": "Execution", // 战术名称（执行阶段）
          "link": "https://attack.mitre.org/tactics/TA0002/", // 战术官方参考文档链接
          "description": "The adversary is trying to run malicious code.", // 官方对该战术大类意图的描述
          "techniques": [ // 该战术目的下，具体被检出的底层技术手段
            {
              "id": "T1059", // 技术 ID
              "name": "Command and Scripting Interpreter", // 技术名称
              "link": "https://attack.mitre.org/techniques/T1059/", // 技术参考链接
              "description": "Adversaries may abuse command and scripting interpreters...", // 官方关于该技术的描述
              "signatures": [ // 最核心的底层沙箱触发证据
                {
                  "severity": "HIGH", // 签名风险等级
                  "description": "Powershell command line execution contains bypass policy flags", // 行为触发签名细节描述
                  "match_data": [ // 命中规则的实际运行时参数
                    "powershell.exe -ExecutionPolicy Bypass -File C:\\Users\\Public\\temp.ps1"
                  ]
                }
              ]
            }
          ]
        }
      ]
    }
  },
  "error": null // 错误信息，若获取成功则为 null
}
```

### 提示词中点名指出的字段属性 (File Skill Mentioned Fields)

基于 [file_skill_cn.md](file:///e:/Idea/Project/simple-vt/src/main/resources/skills/file_skill_cn.md)，以下字段在提示词中被明确指明需要重点分析与输出：
- **基础元数据 / report.attributes**：`id`, `type`, `md5`, `sha1`, `sha256`, `size`, `meaningful_name`, `names`, `first_submission_date`, `last_submission_date`, `times_submitted`, `unique_sources`, `reputation`, `tags`, `type_tags`
- **扫描与结论 / report.attributes**：`last_analysis_stats` (包含子项 `malicious`, `suspicious`, `undetected`, `harmless`, `failure`, `timeout`), `last_analysis_results` (包含子项 `category`, `engine_name`, `method`, `result`), `threat_verdict`, `crowdsourced_ai_results`
- **PE 结构深度指纹 / report.attributes.pe_info**：`imphash`, `entry_point`, `timestamp`, `sections` (包括 `entropy`, `flags`), `overlay` (包括其大小、熵、文件类型), `resource_details`, `import_list`, `exports`
- **签名与信誉 / report.attributes.signature_info**：`verified`, `status`, `signers`, `thumbprint`, `comments`, `copyright`, `product`
- **动态行为 / behaviour.attributes**：`registry_keys_set`, `services_created`, `services_started`, `command_executions`, `processes_injected`, `windows_hidden`, `windows_searched`, `mutexes_created`, `signals_observed`, `invokes`, `files_written`, `files_deleted`, `files_opened`, `registry_keys_opened`, `signals_hooked`, `calls_highlighted`, `crypto_algorithms_observed`, `crypto_keys`, `text_decoded`, `text_highlighted`, `permissions_requested`, `mitre_attack_techniques`, `sigma_analysis_results`, `ids_alerts`, `verdicts`
- **ATT&CK 意图汇总 / mitre.tactics**：`id`, `name`, `description`, `link`，以及 `techniques` 中的 `id`, `name`, `description`，进一步到最底层的 `signatures` 中的 `severity`, `description`

---

## 2. URL 维度

URL 维度的分析偏向于**网络应用层安全**，大模型可以通过分析重定向历史链、HTTP 响应头、仿冒的页面品牌、仿冒的 HTML Meta 标签以及包含的异常追踪器，来判定该 URL 是否是针对特定金融/科技品牌的**钓鱼（Phishing）网站**或**挂马/欺诈页面**。

```json5
{
  "type": "URL", // 分析维度类型，对应 TypeEnum.URL
  "url": "https://www.virustotal.com/gui/url/08fa5ef1d48c03507cba00e998822e0399881188337744aa5566778899aabbcc", // 该 URL 的 VT 平台 GUI 访问地址 (使用 URL 的 SHA256 拼接)
  "report": { // 核心报告数据，对应 UrlReportResp 类
    "id": "08fa5ef1d48c03507cba00e998822e0399881188337744aa5566778899aabbcc", // 扫描的 URL 的 SHA-256 哈希值
    "type": "url", // VT 内部对象类型
    "attributes": { // 属性数据，对应 UrlReportResp.Attributes
      "url": "http://paypal-security-update.com/login.php", // 被分析的原始目标恶意 URL 字符串
      "tld": "com", // 顶级域名后缀 (Top Level Domain)
      "title": "Log in to your PayPal account", // 页面标题。对于钓鱼分析而言，这里与域名不匹配是极强的欺诈信号！
      "reputation": -45, // VT 社区投票净得分 (负值说明是已知有害页面)
      "total_votes": { // 社区未加权投票 (对应 Vote 类)
        "harmless": 1,
        "malicious": 45
      },
      "first_submission_date": 1715848200,
      "last_submission_date": 1715849800,
      "last_modification_date": 1715850000,
      "last_analysis_date": 1715849900,
      "times_submitted": 12, // 该 URL 累计被检测的次数
      "last_analysis_stats": { // 各检测引擎定性统计 (对应 AnalyseStats 类)
        "malicious": 28, // 判定为恶意 (Malicious) 的安全引擎数量
        "suspicious": 1, // 判定可疑 (Suspicious) 引擎数
        "harmless": 35, // 判定安全/无害的引擎数
        "undetected": 10, // 未检出任何规则的引擎数
        "timeout": 0,
        "confirmed-timeout": 0,
        "failure": 0,
        "type-unsupported": 0
      },
      "last_analysis_results": { // 安全引擎分类判定细节 (Map<String, AnalyseResult>)
        "Sophos": {
          "method": "blacklist",
          "engine_name": "Sophos",
          "engine_version": "1.0",
          "engine_update": "20260520",
          "category": "malicious",
          "result": "phishing" // 判定为钓鱼攻击
        },
        "Fortinet": {
          "method": "blacklist",
          "engine_name": "Fortinet",
          "engine_version": "1.0",
          "engine_update": "20260520",
          "category": "malicious",
          "result": "Malicious Website"
        }
      },
      "categories": { // 厂商给出的业务分类 (Map<String, String>)
        "Forcepoint ThreatSeeker": "information-technology",
        "Sophos": "financial-services"
      },
      "tags": [ // 行为与属性关联标签
        "phishing",
        "social-engineering"
      ],
      "last_final_url": "http://paypal-security-update.com/login.php?error=timeout", // 经历重定向后最终达到的终端 URL 地址
      "redirection_chain": [ // 访问此页面发生的重定向链路（可逃避安全检测）
        "http://paypal-security-update.com/login.php"
      ],
      "last_http_response_code": 200, // 最新一次探测返回的 HTTP 响应码
      "last_http_response_content_length": 4512, // HTTP 响应的页面内容字节大小
      "last_http_response_content_sha256": "fa860d5b7a...", // 响应 HTML 文本的哈希，可用于特征匹配
      "has_content": true, // 页面是否有有效负载返回
      "last_http_response_headers": { // 最近一次抓取获取到的 HTTP 响应头
        "Server": "nginx/1.18.0",
        "Content-Type": "text/html; charset=UTF-8",
        "Connection": "keep-alive"
      },
      "last_http_response_cookies": { // 设置的客户端 Cookie。若伴随高危追踪参数，容易泄露隐私
        "PHPSESSID": "a0734c56bbd011152a0734c56bbd0111"
      },
      "html_meta": { // 从 HTML 页面中提取的 Meta 标签，大模型可凭此判断是否在仿冒知名机构 (Map<String, List<String>>)
        "viewport": [ "width=device-width, initial-scale=1" ],
        "description": [ "Log in to your PayPal account to complete your transaction." ]
      },
      "targeted_brand": { // 钓鱼引擎提取出来的钓鱼攻击目标品牌信息，极高危信号！ (Map<String, String>)
        "PayPal": "PayPal Inc." // 键是品牌，值是完整公司名
      },
      "outgoing_links": [ // 页面内部的外链，如果大量外链指向可疑域名，说明是挂马或SEO劫持
        "https://www.paypalobjects.com/webstatic/icon/favicon.ico"
      ],
      "trackers": { // 页面内置的广告/用户追踪脚本分析 (Map<String, List<Tracker>>)
        "Google Analytics": [
          {
            "id": "UA-998218-1", // 追踪器 ID
            "timestamp": 1715848200,
            "url": "https://www.google-analytics.com/analytics.js"
          }
        ]
      }
    }
  },
  "behaviour": null, // 仅文件维度支持该字段，此处固定为 null
  "mitre": null, // 仅文件维度支持该字段，此处固定为 null
  "error": null
}
```

### 提示词中点名指出的字段属性 (URL Skill Mentioned Fields)

基于 [url_skill_cn.md](file:///e:/Idea/Project/simple-vt/src/main/resources/skills/url_skill_cn.md)，以下字段在提示词中被明确指明需要重点分析与输出：
- **基础标识**：`id`, `type`
- **核心扫描与结论 / report.attributes**：`url` (原始 URL 字符串), `last_analysis_stats` (包含子项 `malicious`, `suspicious`, `harmless`, `undetected`), `last_analysis_results` (包含子项 `category`, `engine_name`, `method`, `result`)
- **URL 行为与内容 / report.attributes**：`last_final_url` (重定向目标), `redirection_chain` (重定向历史链), `last_http_response_code` (HTTP 响应码), `last_http_response_headers` (响应头), `last_http_response_content_length`, `last_http_response_content_sha256`, `last_http_response_cookies`, `html_meta` (HTML Meta 标签), `title` (页面标题), `has_content`
- **信誉与关联信息 / report.attributes**：`reputation` (信誉分), `total_votes` (社区投票, 包含子项 `harmless`, `malicious`), `categories` (厂商分类), `tags` (标签), `tld` (顶级后缀), `targeted_brand` (目标品牌), `trackers` (历史追踪器, 包含子项 `id`, `timestamp`, `url`), `outgoing_links` (页面外链)
- **流转时间戳 / report.attributes**：`first_submission_date`, `last_submission_date`, `last_analysis_date`, `last_modification_date`
```

---

## 3. IP 维度

IP 维度的分析核心偏向于**网络基础设施安全**、**地理位置判定**与**自治系统归属**。主要用于分析特定 IP 是否为恶意流量的出口、远控僵尸网络（C2）的控制节点、垃圾邮件的投递源头（Spamming）或是挂载扫描器的节点。

```json5
{
  "type": "IP", // 分析维度类型，对应 TypeEnum.IP
  "url": "https://www.virustotal.com/gui/ip-address/8.8.8.8", // 该 IP 地址在 VT 平台上的 GUI 访问地址
  "report": { // 核心报告数据，对应 IpReportResp 类
    "id": "8.8.8.8", // 被分析的 IPv4/IPv6 地址
    "type": "ip_address", // VT 内部对象类型
    "attributes": { // 属性数据，对应 IpReportResp.Attributes
      "asn": 15169, // 该 IP 所属的自治系统编号 (Autonomous System Number)，在划分流量来源归宿时非常关键
      "as_owner": "GOOGLE", // 自治系统所有者名称（如 Google LLC、Cloudflare、Tencent 等）
      "network": "8.8.8.0/24", // IP 归属的无类别域间路由选择 (CIDR) 子网网段
      "continent": "NA", // IP 地理坐标归属的大洲代码
      "country": "US", // IP 归属的国家/地区 ISO 代码 (大模型可用于评估地缘政治风险或流量合规性)
      "regional_internet_registry": "ARIN", // 负责分配该 IP 的区域互联网注册机构
      "jarm": "2ad2ad0002ad2ad0002ad2ad2ad2ad...", // 该 IP 绑定的 SSL 握手机会特征 JARM 指纹，可用来聚类特定的远控协议服务端
      "reputation": 99, // 社区声誉得分（正值表示健康，负值极低说明是恶意黑客攻击发起 IP）
      "total_votes": { // 社区未加权投票 (对应 Vote 类)
        "harmless": 2341,
        "malicious": 2
      },
      "first_submission_date": 1282662000,
      "last_submission_date": 1715849800,
      "last_modification_date": 1715850000,
      "last_analysis_date": 1715849900,
      "last_analysis_stats": { // 引擎判定统计 (对应 AnalyseStats 类)
        "malicious": 0, // 被标记为黑名单 IP 的安全引擎数
        "suspicious": 0,
        "harmless": 88, // 判定正常的厂商数
        "undetected": 2, // 未被标记的引擎数
        "timeout": 0,
        "confirmed-timeout": 0,
        "failure": 0,
        "type-unsupported": 0
      },
      "last_analysis_results": { // 引擎扫描明细 (Map<String, AnalyseResult>)
        "ADMINUSLabs": {
          "method": "blacklist",
          "engine_name": "ADMINUSLabs",
          "engine_version": "1.0",
          "engine_update": "20260520",
          "category": "harmless",
          "result": "clean"
        },
        "AlienVault": {
          "method": "blacklist",
          "engine_name": "AlienVault",
          "engine_version": "1.0",
          "engine_update": "20260520",
          "category": "harmless",
          "result": "clean"
        }
      },
      "tags": [], // 标签，如 "tor" (洋葱路由出口), "vpn" (代理), "botnet"
      "last_https_certificate": { // 上一次探测该 IP 时获取的 SSL 证书详情。这是进行威胁追踪和归因（Attribution）的重要证据。 (对应 SslCertificate 类)
        "serial_number": "0137abf60c239d1b", // 证书十六进制序列号
        "signature_algorithm": "sha256WithRSAEncryption", // 签名算法
        "size": 1780, // 大小
        "version": "V3", // 证书版本
        "thumbprint": "23a1c67d1b82ac3f...", // SHA1 指纹
        "thumbprint_sha256": "4a86bba41a6b...", // SHA256 指纹
        "first_seen_date": 1715800000,
        "validity": { // 有效时间范围 (对应 Validity 类)
          "not_before": "2026-01-01 00:00:00", // 生效时间
          "not_after": "2027-01-01 00:00:00" // 过期时间
        },
        "issuer": { // 证书发行者信息 (对应 GeneralInformation 类)
          "C": "US",
          "O": "Google Trust Services LLC",
          "CN": "GTS CA 1C3"
        },
        "subject": { // 证书持有者信息 (对应 GeneralInformation 类)
          "C": "US",
          "ST": "California",
          "L": "Mountain View",
          "O": "Google LLC",
          "CN": "dns.google" // 持有者通用名称（Common Name）
        },
        "extensions": { // X509 证书的常见扩展配置字段 (对应 Extension 类)
          "CA": false, // 该证书非根证书 CA
          "subject_alternative_name": [ // 该证书额外绑定的备用域名，大模型可以通过它挖掘出该 IP 的关联域名网络
            "dns.google",
            "dns.google.com",
            "8.8.8.8",
            "8.8.4.4"
          ]
        }
      },
      "last_https_certificate_date": 1715849000, // 证书获取的时间戳
      "whois": "NetRange:       8.8.8.0 - 8.8.8.255\nCIDR:           8.8.8.0/24\nNetName:        LVLT-GOGL-8-8-8\n...", // 原始 Whois 网络注册信息，大模型能够从中提取出 IP 的管辖权、注册人和地理注册地址等细节
      "whois_date": 1715850000 // WHOIS 最后更新日期戳
    }
  },
  "behaviour": null, // 仅文件维度支持，固定为 null
  "mitre": null, // 仅文件维度支持，固定为 null
  "error": null
}
```

### 提示词中点名指出的字段属性 (IP Skill Mentioned Fields)

基于 [ip_skill_cn.md](file:///e:/Idea/Project/simple-vt/src/main/resources/skills/ip_skill_cn.md)，以下字段在提示词中被明确指明需要重点分析与输出：
- **基础标识**：`id` (IP地址明文), `type`
- **核心扫描结果 / report.attributes**：`last_analysis_stats` (包含子项 `malicious`, `suspicious`, `harmless`, `undetected`), `last_analysis_results` (包含子项 `category`, `engine_name`, `method`, `result`)
- **地理与网络归属 / report.attributes**：`asn` (自治系统编号), `as_owner` (运营商/所有者), `network` (CIDR网段), `continent` (所属大洲), `country` (国家/地区), `regional_internet_registry` (注册机构)
- **SSL 证书信息 / report.attributes.last_https_certificate**：`subject.CN` (证书绑定域名), `issuer` (颁发机构), `validity.not_after` (有效期截止), `extensions.san` (SAN 关联域名), 以及证书获取时间戳 `last_https_certificate_date`
- **JARM 指纹 / report.attributes**：`jarm` (TLS 握手特征哈希)
- **信誉、标签与 Whois / report.attributes**：`reputation` (信誉分), `total_votes` (社区投票, 包含子项 `harmless`, `malicious`), `tags` (风险标签列表), `whois` (原始 Whois 文本), `whois_date` (更新时间戳)
```

---

## 4. DOMAIN (域名) 维度

域名维度的分析是 **域名系统（DNS）安全** 与 **防钓鱼检测** 的重心。大模型在此维度下，主要依据域名流行度排名（Popularity Ranks）、WHOIS 注册者、过期时间、近期的 DNS 解析指向记录、以及关联的 SSL 证书、众包威胁上下文来评估域名的信誉和欺诈意图。

```json5
{
  "type": "DOMAIN", // 分析维度类型，对应 TypeEnum.DOMAIN
  "url": "https://www.virustotal.com/gui/domain/google.com", // 域名在 VT 上的 GUI 页面链接
  "report": { // 核心报告数据，对应 DomainReportResp 类
    "id": "google.com", // 被分析的域名字符串
    "type": "domain", // VT 内部类型
    "attributes": { // 属性数据，对应 DomainReportResp.Attributes
      "tld": "com", // 顶级域名后缀
      "registrar": "MarkMonitor Inc.", // 域名注册代理商 (Registrar)
      "reputation": 100, // 社区评价信誉得分
      "total_votes": { // 社区未加权投票 (对应 Vote 类)
        "harmless": 4521,
        "malicious": 1
      },
      "creation_date": 874296000, // 域名创建时间戳 (UTC)。对于恶意网站，往往创建时间极短（如仅存活几天的新域名）
      "expiration_date": 1852786800, // 域名到期时间戳。短期域名（例如仅注册1年）多用于一次性欺诈攻击
      "last_update_date": 1715849800, // 域名 WHOIS 在注册商侧的最近更新时间戳
      "last_modification_date": 1715850000, // 对象在 VT 侧被修改的时间戳
      "last_analysis_date": 1715849900,
      "last_analysis_stats": { // 厂商黑名单扫描统计 (对应 AnalyseStats 类)
        "malicious": 0, // 被判定为恶意域名（Phishing / Spam / Malware / Suspicious）的引擎数量
        "suspicious": 0,
        "harmless": 90,
        "undetected": 1,
        "timeout": 0,
        "confirmed-timeout": 0,
        "failure": 0,
        "type-unsupported": 0
      },
      "last_analysis_results": { // 扫描结果映射明细表
        "Google Safe Browsing": {
          "method": "blacklist",
          "engine_name": "Google Safe Browsing",
          "engine_version": "1.0",
          "engine_update": "20260520",
          "category": "harmless",
          "result": "clean"
        }
      },
      "popularity_ranks": { // 域名在各大权威流行度排行里的排名，大模型可通过此过滤出知名高信誉网站 (Map<String, PopularityRank>)
        "Alexa": {
          "rank": 1, // Alexa 排名 (第 1 名)
          "timestamp": 1715848000 // 获取排行时间戳
        },
        "Cisco Umbrella": {
          "rank": 1,
          "timestamp": 1715848000
        }
      },
      "last_dns_records": [ // 域名最近一次解析获取到的 DNS 解析记录映射列表 (List<DnsRecord>)
        {
          "type": "A", // DNS 记录类型
          "value": "8.8.8.8", // 解析出的 IP
          "ttl": 300 // 在 DNS 解析缓存中的有效期（秒）
        },
        {
          "type": "MX", // 邮件服务器记录。攻击者常用特定的 MX 记录开展伪造发信攻击
          "value": "smtp.google.com",
          "priority": 10
        }
      ],
      "last_dns_records_date": 1715849800, // DNS 记录采集时间戳
      "last_https_certificate": { // 该域名在最近一次 SSL/TLS 握手时展现的证书。对于捕获钓鱼页面至关重要 (对应 SslCertificate 类)
        "serial_number": "00a12e34d56...",
        "signature_algorithm": "sha256WithRSAEncryption",
        "size": 2048,
        "version": "V3",
        "thumbprint": "a82bc3f...",
        "thumbprint_sha256": "4a86...",
        "validity": {
          "not_before": "2026-01-01 00:00:00",
          "not_after": "2027-01-01 00:00:00"
        },
        "issuer": {
          "O": "Google Trust Services",
          "CN": "GTS CA 1C3"
        },
        "subject": {
          "CN": "google.com"
        },
        "extensions": {
          "CA": false,
          "subject_alternative_name": [
            "*.google.com",
            "google.com"
          ]
        }
      },
      "last_https_certificate_date": 1715849000,
      "tags": [ // 标签
        "popular",
        "dns-service"
      ],
      "whois": "Domain Name: google.com\nRegistry Domain ID: 2138514_DOMAIN_COM-VRSN\nRegistrar WHOIS Server: whois.markmonitor.com\n...", // 原始 Whois 信息，大模型可由此评估域名的合法属主以及是否为“仿冒抢注”
      "whois_date": 1715850000,
      "crowdsourced_context": [ // 众包安全背景，常有安全社区直接提供的关键 IOC 分析证据说明 (List<CrowdsourcedContext>)
        {
          "timestamp": 1715840000, // 众包信息时间戳
          "severity": "info", // 严重级别
          "title": "Google DNS Service", // 标题
          "source": "VirusTotal Community", // 来源
          "details": "This domain is the primary host for Google search engine and dns lookup services." // 详细描述
        }
      ]
    }
  },
  "behaviour": null, // 仅文件维度支持，此处固定为 null
  "mitre": null, // 仅文件维度支持，此处固定为 null
  "error": null
}
```

### 提示词中点名指出的字段属性 (Domain Skill Mentioned Fields)

基于 [domain_skill_cn.md](file:///e:/Idea/Project/simple-vt/src/main/resources/skills/domain_skill_cn.md)，以下字段在提示词中被明确指明需要重点分析与输出：
- **基础标识**：`id` (域名明文), `type`
- **核心扫描结果 / report.attributes**：`last_analysis_stats` (包含子项 `malicious`, `suspicious`, `harmless`, `undetected`), `last_analysis_results` (包含子项 `category`, `engine_name`, `method`, `result`)
- **分类与标签 / report.attributes**：`categories` (厂商安全分类), `tags` (特征标签)
- **注册与时间信息 / report.attributes**：`tld` (顶级后缀), `creation_date` (注册时间), `expiration_date` (到期时间), `last_update_date` (更新时间), `registrar` (注册服务商), `whois` (完整 Whois 文本), `whois_date` (WHOIS 更新时间戳)
- **DNS 记录 / report.attributes**：`last_dns_records` (包括子字段 `type`, `value`, `ttl`), `last_dns_records_date` (DNS 记录更新时间)
- **SSL 证书 / report.attributes.last_https_certificate**：`subject.CN` (主域名), `issuer` (颁发机构), `validity` (有效期), `extensions.san` (SAN 关联域名), 以及获取时间戳 `last_https_certificate_date`
- **JARM 指纹 / report.attributes**：`jarm` (TLS 指纹哈希)
- **流行度与信誉 / report.attributes**：`popularity_ranks` (包含子项 `rank`, `timestamp`), `reputation` (社区得分), `total_votes` (包含子项 `harmless`, `malicious`), `crowdsourced_context` (众包背景, 包含子项 `timestamp`, `severity`, `title`, `source`, `details`)
- **Favicon 图标 / report.attributes.favicon**：`raw_md5`, `dhash` (差分哈希)
```
