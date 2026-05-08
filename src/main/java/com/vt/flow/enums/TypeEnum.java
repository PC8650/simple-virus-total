package com.vt.flow.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TypeEnum {

    FILE("/file/"),
    URL("/url/"),
    IP("/ip-address/"),
    DOMAIN("/domain/");

    private static final String GUI_PREFIX = "https://www.virustotal.com/gui";

    private final String guiUri;

    public String guiUrl(String reportId) {
        return GUI_PREFIX + guiUri + reportId;
    }

}
