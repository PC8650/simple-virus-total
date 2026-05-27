package com.vt.flow.enums;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
public enum SkillEnum {

    FILE("skills/file_skill.md"),
    URL("skills/url_skill.md"),
    IP("skills/ip_skill.md"),
    DOMAIN("skills/domain_skill.md");

    private final static Map<SkillEnum, String> SKILL_CACHE_MAP = new ConcurrentHashMap<>(4);

    private final String skillPath;

    private String loadSkillContent() {
        try {
            ClassPathResource resource = new ClassPathResource(this.skillPath);
            try (InputStream inputStream = resource.getInputStream()) {
                return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("Failed to load skill content from classpath: " + skillPath, e);
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
