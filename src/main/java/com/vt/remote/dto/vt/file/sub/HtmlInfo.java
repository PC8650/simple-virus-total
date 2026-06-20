package com.vt.remote.dto.vt.file.sub;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(description = "关于 HTML 文件的信息")
public record HtmlInfo(
        @Schema(description = "所有 tag 中的 超文本引用")
        List<String> hrefs,
        @Schema(description = "frame列表")
        List<Iframe> iframes,
        @Schema(description = "DictionariesMeta标签列表")
        List<Meta> meta,
        @Schema(description = "脚本列表")
        List<Script> scripts,
        @Schema(description = "网站标题")
        String title,
        @Schema(description = "追踪器列表")
        List<Tracker> trackers
) {
    @Schema(description = "frame")
    public record Iframe(
            @Schema(description = "标签属性")
            Map<String, Object> attributes
    ){}
    @Schema(description = "DictionariesMeta标签")
    public record Meta(
            @Schema(description = "所有带有该名称标签的内容")
            List<String> content,
            @Schema(description = "tag名称")
            String name
    ){}
    @Schema(description = "脚本")
    public record Script(
            @Schema(description = "标签属性")
            Map<String, Object> attributes,
            @Schema(description = "如果脚本嵌入在脚本中，该属性包含其 SHA256 校验和")
            String sha256
    ){}
    @Schema(description = "追踪器信息")
    public record Tracker(
            @Schema(description = "追踪器名称")
            String name,
            @Schema(description = "发现的追踪器")
            List<info> trackers
    ){
        @Schema(description = "信息")
        public record info(
                @SerializedName("tracker_id")
                @Schema(name = "tracker_id", description = "追踪活动/客户ID")
                String trackerId,
                @Schema(description = "脚本 URL")
                String url
        ){}
    }
}
