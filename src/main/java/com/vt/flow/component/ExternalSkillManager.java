package com.vt.flow.component;

import com.vt.enums.MsgEnum;
import com.vt.flow.config.ExternalSkillConfig;
import com.vt.flow.dto.ExternalSkillTag;
import com.vt.flow.utils.MdYamlParseUtil;
import com.vt.flow.utils.RecursionSearchUtils;
import com.vt.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 外部 skill 加载器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalSkillManager implements InitializingBean {

    private final ExternalSkillConfig externalSkillConfig;

    private static final Map<String, ExternalSkillTag> TAG_MAP = new HashMap<>();

    private static int skillCount = 0;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!externalSkillConfig.isEnable()) return;

        log.info(">>> external skill is enabled");
        String dir = externalSkillConfig.getDir();
        if (!StringUtils.hasText(dir)) {
            log.info(">>> external skill dir is empty, skip load");
            return;
        }

        log.info(">>> external skill dir: {}, md file mapping", dir);
        externalSkillMapping(dir);
    }

    /**
     * 匹配 Skill 文件内容，并组装成追加给 AI 的提示词
     *
     * @param content VT 汇总内容
     */
    public String externalGuidBuild(String lang, String content) {
        if (!externalSkillConfig.isEnable() || !externalSkillConfig.effectiveLimit() || TAG_MAP.isEmpty()) return "";

        List<ExternalSkillTag.ExternalSkillInfo> skills = matchTop(content, externalSkillConfig.getTopLimit());

        StringBuilder guid = new StringBuilder();
        guid.append(MessageUtils.getMessage(lang, MsgEnum.ENHANCEMENT_HEADER));

        for (ExternalSkillTag.ExternalSkillInfo skill : skills) {
            // 1. 加载主 Skill 文件
            boolean success = appendFileContent(lang, guid, skill.path(), true);

            if (!success) continue;

            // 2. 加载关联的参考文件 (references)
            if (skill.hasReference()) {
                for (String refPath : skill.references()) {
                    appendFileContent(lang, guid, refPath, false);
                }
            }
            guid.append("\n---\n\n");
            log.info("use external skill: {}", skill.path());
        }

        return guid.toString();
    }

    /**
     * tag 映射
     * @param file skill md
     * @param map tag 映射map
     * @return skill 是否创建并保存 tag 映射
     */
    private boolean tagMapping(File file, Map<String, ExternalSkillTag> map) {
        ExternalSkillTag.ExternalSkillInfo skillInfo = MdYamlParseUtil.parseMdFile(file);
        if (Objects.isNull(skillInfo) || CollectionUtils.isEmpty(skillInfo.tags())) {
            log.info(">>> skip invalid skill file (missing tags or yaml frontmatter): {}", file.getAbsolutePath());
            return false;
        }

        //满足条件，探测同级 references 目录
        File refsDir = new File(file.getParentFile(), "references");
        if (refsDir.exists() && refsDir.isDirectory()) {
            File[] refFiles = refsDir.listFiles((d, name) -> name.toLowerCase().endsWith(".md"));
            if (refFiles != null) {
                for (File ref : refFiles) skillInfo.addReference(ref.getAbsolutePath());
            }
        }

        //创建映射
        for (String tag : skillInfo.tags()) {
            map.computeIfAbsent(tag, ExternalSkillTag::init).addSkill(skillInfo);
        }
        return true;
    }

    /**
     * 外部 skill 映射
     */
    private void externalSkillMapping(String dirPath) {
        File rootDir = new File(dirPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            log.warn(">>> external skill dir does not exist or is not a directory: {}", dirPath);
            return;
        }

        AtomicInteger skipCount = new AtomicInteger(0);

        RecursionSearchUtils.startObjGetAllAnd(rootDir)
                // 设置递归属性：如果是目录，则返回其下的所有子文件/文件夹集合
                .addRecursionParam(f -> {
                    File[] files = f.listFiles();
                    return files != null ? Arrays.asList(files) : null;
                })
                //不为空
                .putJudgeValue(Function.identity(), Objects::nonNull)
                // 必须是 md文件
                .putJudgeValue(Function.identity(), f -> {
                    File file = (File) f; return file.isFile() && file.getName().toLowerCase().endsWith(".md");
                })
                // 对满足条件的文件立即进行映射操作，避免二次循环
                .getAll(TAG_MAP, (file, map) -> {
                    boolean mapping = tagMapping(file, map);
                    if (mapping) skillCount++;
                    else skipCount.incrementAndGet();
                }, f -> true);

        log.info(">>> external skill mapping complete. Success: {}, Skipped: {}, Unique Tags mapped: {}",
                skillCount, skipCount.get(), TAG_MAP.size());
    }


    /**
     * 根据 VT 的 JSON 内容，匹配并获取 Top N 的外部 Skill
     *
     * @param content VT 接口返回的 JSON 字符串
     * @param limit   最大返回数量
     */
    private List<ExternalSkillTag.ExternalSkillInfo> matchTop(String content, int limit) {
        String lowerContent = content.toLowerCase();
        Map<ExternalSkillTag.ExternalSkillInfo, Integer> scoreMap = new HashMap<>();

        for (ExternalSkillTag tag : TAG_MAP.values()) {
            if (!tag.matched(lowerContent)) continue;
            for (ExternalSkillTag.ExternalSkillInfo skill : tag.skills()) {
                scoreMap.put(skill, scoreMap.getOrDefault(skill, 0) + 1);
            }
        }

        // 按分数降序排序，取 Top N
        return scoreMap.entrySet().stream()
                .sorted(Map.Entry.<ExternalSkillTag.ExternalSkillInfo, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .toList();
    }


    /**
     *读取、清理并追加文件内容
     */
    private boolean appendFileContent(String lang, StringBuilder sb, String path, boolean isPrimary) {
        try {
            File file = new File(path);
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);

            // 统一剔除 YAML Frontmatter
            String body = content.replaceAll("(?s)^---\\s*\\n.*?\\n---\\s*\\n", "");

            if (isPrimary) {
                // 主文件标题：使用父目录名作为技能名，更具可读性
                String skillName = file.getParentFile().getName();
                sb.append(MessageUtils.getMessage(lang, MsgEnum.ENHANCEMENT_GUIDE_TITLE, skillName));
            } else {
                // 参考文件标题
                sb.append(MessageUtils.getMessage(lang, MsgEnum.ENHANCEMENT_REFERENCE_TITLE, file.getName()));
            }

            sb.append(body.trim()).append("\n\n");
            return true;
        } catch (Exception e) {
            log.error(">>> failed to read skill file: {}", path, e);
            return false;
        }
    }

}