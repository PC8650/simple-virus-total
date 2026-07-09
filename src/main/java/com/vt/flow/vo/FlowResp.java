package com.vt.flow.vo;

import com.vt.flow.enums.ContentEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Schema(description = "ai 流程响应")
public class FlowResp{

    @Schema(description = "整个流程的响应元素记录")
    private final HashMap<String, List<Attributes>> flowAttributes = new LinkedHashMap<>();

    @Schema(description = "流程是否结束")
    private boolean finish = false;

    @Schema(description = "流程响应元素")
    public record Attributes(
            @Schema(description = "顾问节点")
            String advisor,
            @Schema(description = "内容")
            String content,
            @Schema(description = "类型")
            String type,
            @Schema(description = "是否折叠")
            boolean fold
    ){
        public static Attributes init(String advisor, String content, ContentEnum contentEnum) {
            return new Attributes(advisor, content, contentEnum.name(), contentEnum.isFold());
        }
    }

    public void addAttributes(Attributes attributes){
        flowAttributes
                .computeIfAbsent(attributes.advisor, (k) -> new ArrayList<>())
                .add(attributes);
    }

    public void addLastAttributes(Attributes attributes){
        addAttributes(attributes);
        finish = true;
    }

}
