package com.vt.atp.flow.dto;

import com.vt.atp.flow.enums.TypeEnum;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class InputContent {

    private TypeEnum type;

    private String payload;

    private MultipartFile file;

    private String pwd;

}
