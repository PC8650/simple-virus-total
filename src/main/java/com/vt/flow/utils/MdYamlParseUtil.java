package com.vt.flow.utils;

import com.vt.flow.dto.ExternalSkillTag;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown 文件的 YAML Frontmatter 解析工具
 */
@Slf4j
public class MdYamlParseUtil {

    private MdYamlParseUtil() {};

    private static final Pattern FRONTMATTER_PATTERN = Pattern.compile("^---\\s*\\n(.*?)\\n---\\s*\\n", Pattern.DOTALL);

    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("description:\\s*(.*)");

    private static final Pattern TAGS_PATTERN = Pattern.compile("tags:\\s*\\n(?:[ \\t]*-\\s*(.*)\\n?)+");

    private static final Pattern ITEM_PATTERN = Pattern.compile("[ \\t]*-\\s*(.+)");

    /**
     * 解析 Markdown 文件的 YAML Frontmatter
     */
    public static ExternalSkillTag.ExternalSkillInfo parseMdFile(File file) {
        try {
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            // 匹配以 --- 开头和结尾的 YAML 块
            Matcher matcher = FRONTMATTER_PATTERN.matcher(content);

            // 没有标准的 YAML 头部，直接跳过
            if (!matcher.find()) return null;

            String yamlBlock = matcher.group(1);

            // 提取 description
            String description = null;
            Matcher descMatcher = DESCRIPTION_PATTERN.matcher(yamlBlock);
            if (descMatcher.find()) {
                description = cleanQuotes(descMatcher.group(1).trim());
            }

            // 提取 tags (支持带缩进的列表格式)
            Set<String> tags = new HashSet<>();
            Matcher tagsMatcher = TAGS_PATTERN.matcher(yamlBlock + "\n");

            if (tagsMatcher.find()) {
                String tagsBlock = tagsMatcher.group();
                Matcher itemMatcher = ITEM_PATTERN.matcher(tagsBlock);
                while (itemMatcher.find()) {
                    String tag = cleanQuotes(itemMatcher.group(1).trim()).toLowerCase();
                    tags.add(tag);
                }
            }

            // 没有 tag，跳过
            if (tags.isEmpty()) return null;

            return ExternalSkillTag.ExternalSkillInfo.init(file.getAbsolutePath(), description, tags);

        } catch (Exception e) {
            log.error(">>> failed to parse md file: {}", file.getAbsolutePath(), e);
            return null;
        }
    }

    /**
     * 去除字符串首尾可能存在的单/双引号
     */
    private static String cleanQuotes(String str) {
        if (str == null || str.length() < 2) return str;
        if ((str.startsWith("\"") && str.endsWith("\"")) || (str.startsWith("'") && str.endsWith("'"))) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

}
