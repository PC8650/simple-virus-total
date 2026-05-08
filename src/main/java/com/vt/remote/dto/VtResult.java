package com.vt.remote.dto;

import com.vt.remote.dto.vt.Link;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Map;

@Data
public class VtResult<T> {

    @Schema(description = "数据")
    private T data;

    @Schema(description = "链接")
    private Link links;

    @Schema(description = "元数据")
    private Map<String, Object> meta;

    @Schema(description = "错误信息")
    private String error;

    @Schema(description = "操作状态。非返回值，需手动处理")
    private String status;

    public static VtResult<?> error(String error) {
        VtResult<?> vtResult = new VtResult<>();
        vtResult.error = error;
        return vtResult;
    }

    public boolean isSuccess() {
        return !StringUtils.hasText(error);
    }

}
