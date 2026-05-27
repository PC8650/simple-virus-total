package com.vt.flow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Schema(description = "外部skill tag信息")
public record ExternalSkillTag(
        @Schema(description = "tag")
        String tag,
        @Schema(description = "tag的正则匹配")
        Pattern pattern,
        @Schema(description = "tag下的skill列表")
        List<ExternalSkillInfo> skills
) {

    public static ExternalSkillTag init(String tag) {
        // 使用词边界正则 \b，防止假阳性 (例如 tag="ip" 匹配到 "zip"), Pattern.quote 防止 tag 中包含特殊正则符号
        return new  ExternalSkillTag(tag, Pattern.compile("\\b" + Pattern.quote(tag) + "\\b"), new ArrayList<>());
    }

    public boolean hasSkills() {
        return !CollectionUtils.isEmpty(skills);
    }

    public boolean matched(String content) {
        return pattern.matcher(content).find();
    }

    public void addSkill(ExternalSkillInfo skill) {
        skills.add(skill);
    }

    @Schema(description = "外部skill信息")
    public record ExternalSkillInfo(
            @Schema(description = "文件绝对路径")
            String path,
            @Schema(description = "md yaml头中的描述，如果存在 `description` key")
            String description,
            @Schema(description = "md yaml头中的tag列表，如果不存在将跳过扫描该文件")
            Set<String> tags,
            @Schema(description = "关联的参考文件路径列表")
            List<String> references
    ) {

        public static ExternalSkillInfo init(String path, String description, Set<String> tags) {
            return new ExternalSkillInfo(path, description, tags, new ArrayList<>());
        }

        public void addReference(String reference) {
            references.add(reference);
        }

        public boolean hasReference() {
            return !CollectionUtils.isEmpty(references);
        }

    }

}
