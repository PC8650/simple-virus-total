package com.vt.remote.api.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApiEnum {

    UPLOAD_LT_32("/files", "", false, "post", "文件上传扫描"),
    GET_UPLOAD_GT_32_URL("/files/upload_url", "", false, "post", "获取大文件上传url"),
    GET_ANALYSE("/analyses/", "", true, "get", "分析结果，/analyses/{analyses_id}"),
    RE_ANALYSE_FILE("/files/", "/analyse", true, "post", "重新分析文件"),
    GET_FILE_REPORT("/files/", "", true, "get", "文件报告"),
    GET_BEHAVIOUR_REPORT("/files/", "/behaviours", true, "get", "文件行为报告"),
    GET_MITRE_TREE("/files/", "/behaviour_mitre_trees", true, "get", "文件行为战术/技术汇总"),
    GET_ACTIVE_SUMMARY("/files/", "/behaviour_summary", true, "get", "文件活动汇总"),
    SCAN_URL("/urls", "", false, "post", "扫描url"),
    RE_ANALYSE_URL("/urls/", "/analyse", true, "post", "重新分析url"),
    GET_URL_REPORT("/urls/", "", true, "get", "url 报告"),
    SCAN_DOMAIN("/domains/", "/analyse", true, "post", "分析域名， /domains/{domain}/analyse"),
    GET_DOMAIN_REPORT("/domains/", "", true, "get", "域名报告，/domains/{domain}"),
    SCAN_IP("/ip_addresses/", "/analyse", true, "post", "分析ip，/ip_addresses/{ip}/analyse"),
    GET_IP_REPORT("/ip_addresses/", "", true, "get", "ip报告，/ip_addresses/{ip}");

    private final String prefix;

    private final String suffix;

    private final boolean concat;

    private final String method;

    private final String remark;

    private static final String API = "https://www.virustotal.com/api/v3";

    public String getApiUrl(String target) {
        if (concat) {
            return API + prefix + target + suffix;
        }

        return API + prefix;
    }
}
