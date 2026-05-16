进入 `JAVA_HOME/bin` 目录，或确保该目录已加入系统环境变量（PATH），在终端执行以下打包脚本：

```bash
# 生成绿色版软件目录 (app-image)
./jpackage --type app-image \
  --name {app_name} \
  --input {jar_dir} \
  --main-jar {jar_name} \
  --main-class org.springframework.boot.loader.launch.JarLauncher \
  --dest {output_path} \
  --verbose
```

### 示例
```bash
./jpackage --type app-image --name svt --input D:\test --main-jar simple-virus-total-0.0.1-SNAPSHOT.jar --main-class org.springframework.boot.loader.launch.JarLauncher --dest D:\test --win-console --verbose
```

### 核心参数说明：

1. **`--name {app_name}`**：
   - 生成的可执行文件名称及安装目录名称。建议使用简单的英文或项目名。

2. **`--input {jar_dir}`**：
   - 包含待打包 JAR 文件的目录。建议将生成的 fat JAR 单独放入一个新目录（如 `dist`），并将此参数指向该目录。如果指向 `target`，则会将 target 下所有无关的中间文件也打包进去，导致体积臃肿。

3. **`--main-jar {jar_name}`**：
   - 位于 `{jar_dir}` 目录下的主可执行 JAR 文件全名（例如：`example.jar`）。

4. **`--main-class org.springframework.boot.loader.launch.JarLauncher`**：
   - 指定启动类。对于 Spring Boot 项目，使用 `JarLauncher` 以确保正确加载内部依赖。

5. **`--dest {output_path}`**：
   - 打包结果的输出目标目录（绝对路径）。

---

### 平台特定的可选参数：

> [!IMPORTANT]
> **注意**：带有“快捷方式”、“菜单”或“目录选择”字样的参数仅在 `--type` 为安装包格式（如 `msi`, `exe`, `pkg`, `deb`, `rpm`）时生效。若使用 `app-image` 类型，这些安装相关的参数将被忽略。

#### 1. Windows 专用选项：
- **`--win-console`**：
  - **适用类型**：`app-image` 及所有安装包。
  - **功能**：运行程序时弹出控制台窗口，方便调试或查看实时日志。
- **`--win-menu`**：仅限安装包。将应用添加到开始菜单。
- **`--win-shortcut`**：仅限安装包。在桌面创建快捷方式。
- **`--win-dir-chooser`**：仅限安装包。允许用户自定义安装路径。

#### 2. macOS 专用选项：
- **`--mac-package-identifier {id}`**：
  - **适用类型**：`app-image` 及安装包。指定唯一 ID（如 `com.example.app`）。
- **`--mac-sign`**：对生成的 App Bundle 进行签名。
- **`--mac-package-name {name}`**：菜单栏显示的名称。

#### 3. Linux 专用选项：
- **`--linux-shortcut`**：仅限安装包。创建桌面快捷方式。
- **`--linux-app-category {category}`**：指定分类（如 `Network`, `Utility`）。
- **`--linux-package-deps`**：仅限安装包。指定系统级依赖。

---

### 打包提示与注意事项：

1. **不支持交叉编译**：必须在 Windows 上打包 Windows 版，在 macOS 上打包 macOS 版。
2. **体积优化**：务必保持 `--input` 目录的整洁。通常一个打包好的绿色版软件大小由 `JRE (运行时)` + `你的 JAR` 组成。
3. **管理权限**：在某些系统中，向 `C:\` 或 `/usr/local` 等目录输出结果可能需要管理员权限，建议 `--dest` 指向用户目录下的路径。

---

### 外部配置与参数说明 (重要)

打包完成后，为了方便用户配置 API Key 或修改端口，而无需重新打包，建议利用 Spring Boot 的外部配置加载机制。

#### 1. 创建配置文件
在生成的可执行文件（如 `simple-virus-total.exe`）同级目录下，手动创建 `config` 文件夹，并在其中创建 `application.yml` 文件。

**目录结构示例：**
```
simple-virus-total/
├── simple-virus-total.exe  (或相应系统的可执行文件)
├── config/
│   └── application.yml  <-- 手动创建此文件
└── app/
    └── ...
```

#### 2. 全量参数模板
将以下内容复制到 `config/application.yml` 中，并根据实际情况修改：

```yaml
# 外部配置文件模板, 使用默认值时可取消该配置项
params:
  # 基础设置
  port: 8080               # 程序运行端口 (默认: 8080)
  
  # API 密钥 (必填)
  v-key: VirusTotal API Key
  
  # Google AI 设置
  google:
    key: Google GenAI API Key                # 
    model: gemma-4-120b-it   # 指定使用的 AI 模型名称, 默认 gemma-4-31b-i
    search: false          # 是否开启 Google Search 联网增强 (默认: false)
    thinking-level: high   # 推理深度/等级，默认 high (可选: minimal, low, medium, high)
```

#### 3. 为什么这样做？
- **安全性**：避免将私有的 API Key 硬编码在 JAR 包中。
- **灵活性**：用户可以直接通过文本编辑器修改配置，重启程序即可生效。
- **优先级**：Spring Boot 会优先读取 `config/application.yml` 中的值，从而覆盖打包时内置的默认配置。