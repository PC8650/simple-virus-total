package com.vt.flow.enums;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
public enum SkillEnum {

    FILE("src/main/resources/skills/file_analysis_skill.md"),
    URL("src/main/resources/skills/url_analysis_skill.md"),
    IP("src/main/resources/skills/ip_analysis_skill.md"),
    DOMAIN("src/main/resources/skills/domain_analysis_skill.md");

    private final static Map<SkillEnum, String> SKILL_CACHE_MAP = new ConcurrentHashMap<>(4);

    private final String skillPath;

    private String loadSkillContent() {
        try {
            return Files.readString(Paths.get(skillPath));
        } catch (Exception e) {
            log.error("Failed to load skill content from path: " + skillPath, e);
            return "";
        }
    }

    public String getSkillContent() {
        String content = SKILL_CACHE_MAP.get(this);
        if (!StringUtils.hasText(content)) {
            content = loadSkillContent();
            if (StringUtils.hasText(content)) SKILL_CACHE_MAP.put(this, content);
        }
        return content;
    }

}
