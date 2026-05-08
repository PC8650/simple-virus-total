package com.vt.remote.dto;

import com.google.api.client.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

public record FileUploadParse(
        MultipartFile file,
        Integer size,
        String filename,
        String pwd
) {
    public MultipartContent uploadForm() {
        MultipartContent multipartContent = new MultipartContent()
                .setMediaType(new HttpMediaType("multipart/form-data")
                        .setParameter("boundary", "__END_OF_PART__"));

        if (StringUtils.hasText(pwd)) {
            MultipartContent.Part passwordPart = new MultipartContent.Part(
                    new ByteArrayContent("text/plain", pwd.getBytes(StandardCharsets.UTF_8))
            );
            passwordPart.setHeaders(new HttpHeaders().set(
                    "Content-Disposition", "form-data; name=\"password\""
            ));
            multipartContent.addPart(passwordPart);
        }

        try {
            byte[] fileBytes = file.getBytes();
            HttpContent fileContent = new ByteArrayContent(file.getContentType(), fileBytes);
            MultipartContent.Part filePart = new MultipartContent.Part(fileContent);
            filePart.setHeaders(new HttpHeaders().set(
                    "Content-Disposition",
                    String.format("form-data; name=\"file\"; filename=\"%s\"", filename)
            ));
            multipartContent.addPart(filePart);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return multipartContent;
    }

}
