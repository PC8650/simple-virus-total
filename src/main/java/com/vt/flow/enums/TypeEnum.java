package com.vt.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TypeEnum {

    FILE("/file/", SkillEnum.FILE),
    URL("/url/", SkillEnum.URL),
    IP("/ip-address/", SkillEnum.IP),
    DOMAIN("/domain/", SkillEnum.DOMAIN);

    private static final String GUI_PREFIX = "https://www.virustotal.com/gui";

    private final String guiUri;

    @Getter
    private final SkillEnum skill;

    public String guiUrl(String reportId) {
        return GUI_PREFIX + guiUri + reportId;
    }

}
