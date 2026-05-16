const scanForm = document.getElementById('scanForm');
const typeSelect = document.getElementById('type');
const payloadGroup = document.getElementById('payloadGroup');
const fileGroup = document.getElementById('fileGroup');
const pwdGroup = document.getElementById('pwdGroup');
const logContainer = document.getElementById('logContainer');
const submitBtn = document.getElementById('submitBtn');
const btnText = document.getElementById('btnText');
const loader = document.getElementById('loader');
const langSelect = document.getElementById('language');

// --- i18n logic ---
const i18n = {
    'zh-CN': {
        typeLabel: '分析类型',
        typeFile: '文件分析 (FILE)',
        typeUrl: '网址分析 (URL)',
        typeIp: 'IP 地址分析 (IP)',
        typeDomain: '域名分析 (DOMAIN)',
        payloadLabel: '载荷 (URL/IP/Domain)',
        payloadPlaceholder: '请输入分析目标...',
        fileLabel: '目标文件',
        pwdLabel: '压缩包密码 (可选)',
        pwdPlaceholder: '若为加密压缩包请填写...',
        descLabel: '补充说明',
        descPlaceholder: '例如：该文件来自可疑邮件，重点关注内嵌宏...',
        langLabel: '报告语言',
        btnStart: '立即启动分析',
        btnRunning: '分析引擎运行中...',
        selectFileBtn: '选择文件',
        noFileChosen: '未选择任何文件',
        sysReady: '系统准备就绪',
        sysWait: '等待输入指令以启动 AI 威胁分析流...',
        sysInit: '正在初始化分析流，建立 SSE 连接...'
    },
    'en-US': {
        typeLabel: 'Analysis Type',
        typeFile: 'File Analysis (FILE)',
        typeUrl: 'URL Analysis (URL)',
        typeIp: 'IP Analysis (IP)',
        typeDomain: 'Domain Analysis (DOMAIN)',
        payloadLabel: 'Payload (URL/IP/Domain)',
        payloadPlaceholder: 'Enter analysis target...',
        fileLabel: 'Target File',
        pwdLabel: 'Archive Password (Optional)',
        pwdPlaceholder: 'Fill if archive is encrypted...',
        descLabel: 'Additional Context',
        descPlaceholder: 'e.g., File is from a suspicious email, focus on embedded macros...',
        langLabel: 'Report Language',
        btnStart: 'Start Analysis',
        btnRunning: 'Engine Running...',
        selectFileBtn: 'Select File',
        noFileChosen: 'No file chosen',
        sysReady: 'System Ready',
        sysWait: 'Waiting for instructions to start AI threat analysis flow...',
        sysInit: 'Initializing analysis flow, establishing SSE connection...'
    }
};

function getCurrentDict() {
    return i18n[langSelect.value] || i18n['en-US'];
}

function updateI18n() {
    const dict = getCurrentDict();

    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (dict[key]) {
            el.textContent = dict[key];
        }
    });

    document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
        const key = el.getAttribute('data-i18n-placeholder');
        if (dict[key]) {
            el.placeholder = dict[key];
        }
    });
}

langSelect.addEventListener('change', updateI18n);

document.addEventListener('DOMContentLoaded', () => {
    const browserLang = navigator.language || navigator.userLanguage;
    if (browserLang.startsWith('zh')) {
        langSelect.value = 'zh-CN';
    } else {
        langSelect.value = 'en-US';
    }
    updateI18n();
});

// 1. 类型联动
const fileInput = document.getElementById('file');
const fileNameDisplay = document.getElementById('fileName');

fileInput.addEventListener('change', () => {
    if (fileInput.files.length > 0) {
        fileNameDisplay.textContent = fileInput.files[0].name;
        fileNameDisplay.removeAttribute('data-i18n'); // 选中文件后停止 i18n 覆盖
    } else {
        fileNameDisplay.setAttribute('data-i18n', 'noFileChosen');
        updateI18n();
    }
});

typeSelect.addEventListener('change', () => {
    const type = typeSelect.value;
    if (type === 'FILE') {
        payloadGroup.classList.add('hidden');
        fileGroup.classList.remove('hidden');
        pwdGroup.classList.remove('hidden');
        document.getElementById('payload').required = false;
        document.getElementById('file').required = true;
    } else {
        payloadGroup.classList.remove('hidden');
        fileGroup.classList.add('hidden');
        pwdGroup.classList.add('hidden');
        document.getElementById('payload').required = true;
        document.getElementById('file').required = false;
    }
});

// 2. 渲染函数
function updateUI(flowResp) {
    logContainer.innerHTML = '';
    let globalLastIsMain = false;

    // 按顺序遍历所有顾问节点
    const nodes = Object.keys(flowResp.flowAttributes);
    nodes.forEach(nodeName => {
        const section = document.createElement('div');
        section.className = 'node-section animate-in';
        section.innerHTML = `<div class="node-header">${nodeName}</div>`;
        
        const logs = flowResp.flowAttributes[nodeName];
        logs.forEach(attr => {
            if (!attr.mainText) {
                // 非正文：小号浅色，独立换行
                const entry = document.createElement('div');
                entry.className = 'log-entry non-main';
                entry.textContent = `> ${attr.content}`;
                section.appendChild(entry);
                globalLastIsMain = false;
            } else {
                // 正文：流式拼接
                if (!globalLastIsMain) {
                    // 上一条是非正文，开启新块
                    const entry = document.createElement('div');
                    entry.className = 'log-entry main';
                    entry.textContent = attr.content;
                    section.appendChild(entry);
                    globalLastIsMain = true;
                } else {
                    // 上一条是正文，追加内容
                    const mains = section.querySelectorAll('.log-entry.main');
                    const lastMain = mains[mains.length - 1];
                    if (lastMain) {
                        lastMain.textContent += attr.content;
                    }
                }
            }
        });
        logContainer.appendChild(section);
    });
    
    // 自动滚动到底部
    logContainer.scrollTop = logContainer.scrollHeight;
}

// 3. 表单提交与 SSE 处理
scanForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    // UI 状态
    submitBtn.disabled = true;
    const dict = getCurrentDict();
    btnText.textContent = dict.btnRunning;
    loader.classList.remove('hidden');
    logContainer.innerHTML = `<div class="node-section"><div class="node-header">SYSTEM</div><div class="log-entry non-main">${dict.sysInit}</div></div>`;

    const formData = new FormData(scanForm);
    
    try {
        const response = await fetch('/vt/flow', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error('网络响应异常');

        const reader = response.body.getReader();
        const decoder = new TextDecoder();
        let buffer = '';

        while (true) {
            const { value, done } = await reader.read();
            if (done) break;

            buffer += decoder.decode(value, { stream: true });
            
            // 处理 SSE 格式数据 (data: {...}\n\n)
            const lines = buffer.split('\n');
            buffer = lines.pop(); // 留下最后一行（可能不完整）

            for (const line of lines) {
                if (line.startsWith('data:')) {
                    const jsonStr = line.replace('data:', '').trim();
                    try {
                        const flowResp = JSON.parse(jsonStr);
                        updateUI(flowResp);

                        // 3. 通过 finish 标志断开
                        if (flowResp.finish) {
                            console.log('检测到完成标志，断开连接');
                            reader.cancel();
                            finalizeUI();
                            return;
                        }
                    } catch (e) {
                        console.error('解析 JSON 失败', e);
                    }
                }
            }
        }
    } catch (err) {
        const errSection = document.createElement('div');
        errSection.className = 'node-section';
        errSection.innerHTML = `<div class="node-header">ERROR</div><div class="log-entry error">${err.message}</div>`;
        logContainer.appendChild(errSection);
    } finally {
        finalizeUI();
    }
});

function finalizeUI() {
    submitBtn.disabled = false;
    btnText.textContent = getCurrentDict().btnStart;
    loader.classList.add('hidden');
}
