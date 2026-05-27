package com.vt.flow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "external-skill")
public class ExternalSkillConfig {

    private boolean enable;

    private String dir;

    private Integer topLimit;

    public boolean effectiveLimit() {
        return topLimit != null && topLimit > 0;
    }

}
