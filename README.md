## 1. 项目简介与目的

**[VirusTotal](https://www.virustotal.com/)** 是一个聚合多款反病毒引擎与安全扫描工具的情报平台，可对文件、URL、域名与 IP 进行检测。其返回的 JSON 体量大、字段专业（如 MITRE ATT&CK、沙箱行为、PE 结构等），对非安全从业人员阅读成本较高。

**项目初衷**：对接 VirusTotal API v3 拉取扫描与报告数据，再经大模型（默认 Google GenAI，亦可切换为 OpenAI 兼容接口）将结果整理为可读的分析报告。用户可获得**有害 / 可疑 / 安全**的参考性结论，并通过战术卡片等方式理解关键行为含义。本工具输出仅供**辅助参考**，不能替代专业安全研判或 VirusTotal 官方结论。

**核心分析链路**（`POST /vt/flow`，SSE 流式推送）：

1. **Scan**：按类型提交 VT 扫描（命中本地缓存则跳过重复提交）。
2. **Analyse**：轮询 `/analyses/{id}` 直至分析完成。
3. **Report**：拉取对象报告；**文件类型**额外拉取动态行为报告与 MITRE 行为树。
4. **Summary**：加载内置 Skill 提示词（可选叠加外部 Skill），流式生成专家报告。

>任务结束会推送本次消耗的token，包括: 输入/输出(含 思考/缓存/工具 等细分计算)/总计 <p>Summary 提示词组装说明，详见 ai_request_data.md</p>

---

## 2. API 对接与局限性声明

本项目基于 VirusTotal **Public API v3**，受免费额度、速率与权限约束。

**已对接能力（按对象类型）**：

| 类型 | 主要接口能力 |
| :--- | :--- |
| **File** | 上传扫描（≤32MB 直传，更大文件走 upload_url）、重分析、静态报告、动态行为报告（`behaviours`）、MITRE 行为树（`behaviour_mitre_trees`）、活动汇总（`behaviour_summary`） |
| **URL** | 提交扫描、重分析、获取报告 |
| **Domain / IP** | 提交分析、获取报告 |

**局限性**：

- **速率与配额**：受 VT 免费 API 的 QPS、日配额等限制，高并发或大批量分析可能触发限流。
- **数据映射折损**：VT 原始响应经 Gson 映射为项目 DTO，非核心或高阶字段可能被省略；部分沙箱/HTML/PCAP 等扩展能力未完整接入。
- **AI 生成内容**：报告由大模型基于已拉取数据归纳，存在遗漏、概括或误判可能；MITRE 等章节以 VT 返回数据为准，模型不得擅自删减已提供的战术/技术条目（详见内置 `file_skill` 约束）。
- **文件体积**：应用层上传上限 650MB（`application.yml`）；VT 侧对直传与分片上传另有 32MB 分界策略（见 `FileApi`）。

---

## 3. 开发环境与项目结构

### 核心环境

- **Java**：25（`pom.xml`）
- **框架**：Spring Boot 4.0.5、Spring AI 1.1.5（Google GenAI Starter）
- **其他**：虚拟线程（`spring.threads.virtual.enabled=true`）、Caffeine 流程缓存、springdoc OpenAPI

### 目录结构简析

```text
simple-virus-total
├── src/
│   └── main/
│       ├── java/com/vt/
│       │   ├── component/       # TempCleaner 临时文件清理器
│       │   ├── config/          # 全局配置（HTTP、Gson、i18n）
│       │   ├── enums/           # 系统消息枚举
│       │   ├── exception/       # 统一异常封装
│       │   ├── utils/           # 国际化等通用工具
│       │   ├── flow/            # AI 分析主链路（Advisor 编排 + SSE）
│       │   │   ├── advisor/     # Scan → Analyse → Report → Summary
│       │   │   │   └── constant/# 链路上下文键
│       │   │   ├── component/   # CacheManager、ExternalSkillManager
│       │   │   ├── config/      # ChatClient、AiProvider、ExternalSkill 绑定
│       │   │   ├── controller/  # /vt/flow 流式入口
│       │   │   ├── dto/         # 输入、缓存、报告聚合对象
│       │   │   ├── enums/       # 分析类型、内置 Skill 路径
│       │   │   ├── model/       # OpenAI 兼容 ChatModel 适配
│       │   │   ├── scan/        # 各类型 Scanner 与工厂
│       │   │   │   ├── factory/
│       │   │   │   └── interfaces/
│       │   │   ├── utils/       # SSE、轮询、Markdown/YAML 解析
│       │   │   └── vo/          # 流式响应结构
│       │   ├── remote/          # VT API v3 封装与本地代理
│       │   │   ├── api/         # File/Url/Ip/Domain/Analyse 调用
│       │   │   │   ├── constant/# 体积阈值等常量
│       │   │   │   └── enums/   # API 路径枚举
│       │   │   ├── component/   # VtRemoter（HTTP 客户端）
│       │   │   ├── controller/  # /file、/url、/ip、/domain 等直连代理
│       │   │   └── dto/         # 上传封装与 VT 响应 DTO
│       │   │       └── vt/      # 按对象拆分的报告子结构
│       │   └── VirusTotalApplication.java
│       └── resources/
│           ├── application.yml  # 内置默认配置（支持 params.* 外部覆盖）
│           ├── i18n/            # 界面与 SSE 文案（中/英）
│           ├── skills/          # 内置分析 Skill（含 *_cn.md 备稿；运行时默认加载英文版）
│           └── static/          # Web 前端（index.html / script.js / style.css）
├── external_skill_guide.md      # 外部 Skill 规范
├── package_command.md           # jpackage 打包与外部配置模板
└── pom.xml
```

**模块职责简述**：

- **`remote`**：封装 VT HTTP 调用，并暴露 REST 代理便于调试或二次集成。
- **`flow`**：面向用户的端到端分析编排；同一输入在 30 分钟内可走 Caffeine 缓存复用扫描上下文。

---

## 4. 核心配置参数说明 (`application.yml`)

运行时通过外部 `config/application.yml` 中的 **`params.*`** 覆盖占位符（Spring Boot 外部配置优先于 JAR 内默认值）。内置 `application.yml` 将 `params` 映射到 `server`、`spring.ai.google.genai`、`vt-key`、`external-skill`、`ai-provider` 等节点。

| 参数路径 | 默认值 / 示例 | 说明                                                                               |
| :--- | :--- |:---------------------------------------------------------------------------------|
| `params.port` | `8080` | HTTP 服务端口。                                                                       |
| `params.v-key` | 无（必填） | VirusTotal API Key，映射为 `vt-key`。                                                 |
| `params.google.key` | 空 | Google GenAI API Key；**当 `params.customer.ai=false` 时必填**。                       |
| `params.google.model` | `gemma-4-31b-it` | Google 模型名称。                                                                     |
| `params.google.search` | `false` | 是否启用 Google Search 检索增强。                                                         |
| `params.google.thinking-level` | `high` | 推理深度：`minimal` / `low` / `medium` / `high`。                                      |
| `params.customer.ai` | `false` | 为 `true` 时使用 `GenericChatModel`（OpenAI Chat Completions 协议），不再使用 Google Starter。 |
| `params.customer.base-url` | 空 | 自定义厂商 Base URL（如 `https://api.deepseek.com/v1` ）。                                |
| `params.customer.api-key` | 空 | 自定义厂商 API Key（`customer.ai=true` 时必填）。                                           |
| `params.customer.model` | 空 | 自定义模型名。                                                                          |
| `params.customer.temperature` | `0` | 采样温度；安全分析场景建议保持较低值。                                                              |
| `params.external.enable` | `false` | 是否启用外部 Skill 增强。                                                                 |
| `params.external.dir` | 空 | 外部 Skill 根目录（绝对路径）；`enable=true` 且目录有效时启动扫描。                                     |
| `params.external.top-limit` | `3` | 单次分析最多注入的外部 Skill 数量（须 > 0 且索引非空时生效）。                                            |

> 完整外部配置示例见 `package_command.md` 第 2 节模板。

---

## 5. 外部 Skill 说明

### 功能定位

在 **Summary** 阶段，系统将 VT 报告 JSON（含文件的行为与 MITRE 数据）与内置 Skill 合并；若启用外部 Skill，则由 `ExternalSkillManager` 在启动时建立 `tag → .md 文件` 索引，按命中得分注入补充提示词。

### 启用配置

```yaml
params:
  external:
    enable: true
    dir: "D:/my-security-skills"
    top-limit: 3
```

### 文件组织与最小格式

- 递归扫描 `dir` 下所有 `.md` 文件。
- 须含 YAML Frontmatter 且声明 `tags` 列表，否则跳过。
- 主文件同级若存在 `references/` 目录，会一并注入其中 `.md`。

示例：

```markdown
---
description: "PE Overlay 检测"
tags:
  - overlay
  - peexe
  - T1027
---

## 分析步骤
...
```

### 匹配与排序机制

- 对 VT JSON 全文做小写化后，以**词边界正则**（`\b`）匹配 tag，降低误命中（如 `ip` 不匹配 `zip`）。
- 每命中 1 个 tag 为该 Skill 计 1 分，按总分降序取前 `top-limit` 个注入。
- `top-limit` 过大易稀释模型对原始 JSON 的关注，建议不超过 3。

详细规范见 `external_skill_guide.md`。

---

## 6. 打包说明与外部参数配置

推荐使用 `jpackage` 生成 **app-image** 绿色版，命令与平台差异见 `package_command.md`。

**外部配置指引**：

1. 在可执行文件同级创建 `config/application.yml`（打包产物名称以 `jpackage --name` 为准，见 `package_command.md`）。
2. 写入第 4 节 `params` 配置；Spring Boot 启动时优先加载该文件，覆盖 JAR 内默认值。
3. 避免将 API Key 硬编码进构建产物。

---

## 7. 文件读写与系统隐私说明

本程序在设计上遵循安全与绿色的原则，关于文件读写与系统隐私的行为具体说明如下：

1. **禁用 JVM 临时性能日志写入**：若打包命令中未省略 `--java-options "-XX:-UsePerfData"` 参数，JVM 将禁用 Performance Data 性能数据收集。这会防止 JVM 退出时在系统临时目录或工作目录下生成 `hsperfdata_*` 等临时数据文件，从而避免这些临时文件因句柄被占用而导致退出清理失败。
2. **本地环境绿色化隔离**：程序运行期间仅在可执行文件（PE）同级生成 `./temp` 目录，该目录包含两部分内容：
    - **Tomcat 工作目录**（`./temp/tomcat`）：内嵌 Tomcat 的 `basedir` 被重定向至此，默认情况下 Tomcat 会在系统 `%TEMP%` 目录中创建缓存，此配置将其隔离到程序自身目录下，避免污染系统临时目录。
    - **文件上传缓存**（`./temp`）：文件分析时上传的文件暂存于此目录。
    - **清理说明**：由于 `jpackage` 打包后的可执行文件在 Windows 环境下关闭控制台窗口（点击 `X` 或 `Ctrl+C`）时，操作系统会强制终止进程，JVM 无法执行优雅退出的销毁回调，因此程序**不保证退出时自动清理** `./temp` 目录。用户可在程序退出后**手动删除** `./temp` 目录，不会影响下次正常启动。
3. **无额外外部文件读取**：
   - **业务逻辑层面**：除了用户显式配置的**外部技能库目录**（`external.dir`）下匹配到的 `.md` 文件，以及**用户在分析文件时主动上传的文件**外，本程序业务代码不会读取用户计算机上的任何其他外部文件。
   - **底层/系统层面**：除 JVM 加载自身运行所需的 JDK 系统类库、本程序 JAR 包内置的 Classpath 静态资源、以及显式挂载的外部配置文件（`config/application.yml`）外，Spring Boot 和 JVM 底层不存在任何越权扫描或未授权的文件读取行为，确保数据隐私安全。

---

## 8. 使用说明

1. 配置 **VirusTotal API Key**（`params.v-key`），并按所选 AI 路线配置 **Google Key** 或 **自定义厂商 Key**。
2. 启动应用：IDE 运行 `com.vt.VirusTotalApplication`，或执行打包后的可执行文件。
3. 访问 Web 界面：`http://localhost:8080/index.html` （端口以 `params.port` 为准）。
4. 选择 FILE / URL / IP / DOMAIN，提交后由 **`POST /vt/flow`** 以 SSE 推送各阶段进度与最终报告；可通过 `language` 参数切换界面与流式文案语言（`zh_CN` / 默认英文资源）。
5. （可选）通过 **`/swagger-ui.html`** 查看 OpenAPI；`/file`、`/url` 等 `remote` 代理接口可供直接调试 VT 原始能力。

**说明**：内置 Skill 当前由 `SkillEnum` 加载 `skills/*_skill.md`（英文版）；`skills/*_cn.md` 为同目录备稿，替换 classpath 资源或调整枚举路径后可切换为中文版提示词。
