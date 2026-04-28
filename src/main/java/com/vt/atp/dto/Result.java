package com.vt.atp.dto;

import com.vt.atp.dto.vt.Link;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Map;

@Data
public class Result<T> {

    private T data;

    private Link links;

    private Map<String, Object> meta;

    private String error;

    public static Result<?> error(String error) {
        Result<?> result = new Result<>();
        result.error = error;
        return result;
    }

    public boolean isSuccess() {
        return !StringUtils.hasText(error);
    }

}
