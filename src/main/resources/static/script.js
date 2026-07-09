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
const backToTopBtn = document.getElementById('backToTop');

// --- Incremental rendering state ---
const advisorState = new Map(); // advisorName -> {count, lastType, lastElement}
let isFirstEvent = true;

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

// --- Helper functions ---
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function shouldAutoScroll() {
    const threshold = 30;
    return logContainer.scrollHeight - logContainer.scrollTop - logContainer.clientHeight < threshold;
}

function toggleCollapse(element) {
    element.classList.toggle('collapsed');
}

function normalizeContent(text) {
    // 确保 \n 被正确处理为换行
    return text.replace(/\\n/g, '\n');
}

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
function appendAttribute(section, attr, lastType, lastElement) {
    const { content, type, fold } = attr;

    // Merge decision logic
    let shouldMerge = false;
    let prefix = '';

    if (type === 'NOTICE') {
        // NOTICE 每条独立换行，从不合并
        shouldMerge = false;
        prefix = '> ';
    } else if (type === 'ERROR') {
        shouldMerge = (lastType === 'ERROR');
        prefix = '! ';
    } else if (type === 'PROMPT' || type === 'THOUGHT') {
        shouldMerge = (lastType === type);
    } else {
        // MAIN_TEXT 及其他
        shouldMerge = (lastType === type);
    }

    // --- Merge into existing element ---
    if (shouldMerge && lastElement) {
        if (fold && lastElement.classList.contains('collapsible')) {
            const foldContent = lastElement.querySelector('.fold-content');
            if (foldContent) {
                foldContent.textContent += normalizeContent(content);
            } else {
                lastElement.textContent += normalizeContent(content);
            }
        } else {
            lastElement.textContent += normalizeContent(content);
        }
        return { type, element: lastElement };
    }

    // --- Create new element ---
    const entry = document.createElement('div');
    entry.className = 'log-entry';

    // Type-based styling
    if (type === 'NOTICE' || type === 'PROMPT' || type === 'THOUGHT') {
        entry.classList.add('non-main');
    } else if (type === 'MAIN_TEXT') {
        entry.classList.add('main');
    } else if (type === 'ERROR') {
        entry.classList.add('main', 'error');
    }

    // Fold/collapsible wrapping
    if (fold) {
        entry.classList.add('collapsible', 'collapsed');
        const chevron = document.createElement('span');
        chevron.className = 'chevron';
        chevron.textContent = '▶';
        entry.appendChild(chevron);

        // 只点击 chevron 切换折叠，允许用户选中内容文本
        chevron.addEventListener('click', (e) => {
            e.stopPropagation();
            toggleCollapse(entry);
        });

        const foldContent = document.createElement('span');
        foldContent.className = 'fold-content';
        foldContent.textContent = normalizeContent(content);
        entry.appendChild(foldContent);
    } else {
        entry.textContent = (prefix || '') + normalizeContent(content);
    }

    section.appendChild(entry);
    return { type, element: entry };
}

function updateUI(flowResp) {
    // 首次 SSE 事件：清除 SYSTEM 占位内容
    if (isFirstEvent) {
        logContainer.innerHTML = '';
        advisorState.clear();
        isFirstEvent = false;
    }

    const entries = Object.entries(flowResp.flowAttributes); // 保持 LinkedHashMap 插入顺序

    for (const [advisor, attrs] of entries) {
        const state = advisorState.get(advisor);
        const prevCount = state ? state.renderedCount : 0;

        if (attrs.length <= prevCount) continue;

        const newAttrs = attrs.slice(prevCount);

        // 查找或创建顾问节点区块
        let section = logContainer.querySelector(`.node-section[data-advisor="${CSS.escape(advisor)}"]`);
        if (!section) {
            section = document.createElement('div');
            section.className = 'node-section animate-in';
            section.dataset.advisor = advisor;
            section.innerHTML = `<div class="node-header">${escapeHtml(advisor)}</div>`;
            logContainer.appendChild(section);
        }

        let lastType = state ? state.lastType : null;
        let lastElement = state ? state.lastElement : null;

        for (const attr of newAttrs) {
            const result = appendAttribute(section, attr, lastType, lastElement);
            lastType = result.type;
            lastElement = result.element;
        }

        advisorState.set(advisor, {
            renderedCount: attrs.length,
            lastType: lastType,
            lastElement: lastElement
        });
    }

    // 条件自动滚动
    if (shouldAutoScroll()) {
        logContainer.scrollTop = logContainer.scrollHeight;
    }
}

// --- Back to top ---
logContainer.addEventListener('scroll', () => {
    backToTopBtn.classList.toggle('visible', logContainer.scrollTop > 500);
});

backToTopBtn.addEventListener('click', () => {
    logContainer.scrollTo({ top: 0, behavior: 'smooth' });
});

// 3. 表单提交与 SSE 处理
scanForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    // 重置渲染状态
    advisorState.clear();
    isFirstEvent = true;

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
        const errHeader = document.createElement('div');
        errHeader.className = 'node-header';
        errHeader.textContent = 'ERROR';
        errSection.appendChild(errHeader);
        const errEntry = document.createElement('div');
        errEntry.className = 'log-entry main error';
        errEntry.textContent = '! ' + err.message;
        errSection.appendChild(errEntry);
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
