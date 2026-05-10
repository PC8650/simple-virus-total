package com.vt.flow.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 缓存数据
 */
@Data
public class CacheDto {

    public CacheDto() {}

    public CacheDto(String key) {
        this.key = key;
    }
    
    private String key;
    
    private String analyseId;

    private String reportId;

    public static CacheDto init(String key) {
        return new CacheDto(key);
    }

    public boolean hasAnalyseId() {
        return StringUtils.hasText(analyseId);
    }

    public boolean hasReportId() {
        return StringUtils.hasText(reportId);
    }

    public boolean isEmpty() {
        return !hasAnalyseId() && !hasReportId();
    }

}
