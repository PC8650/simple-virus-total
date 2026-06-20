package com.vt.remote.dto.vt.file.sub;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "从 Javascript 文件中提取的信息")
public record JavascriptInfo(
        @Schema(description = """
                代码中有趣的元素。可以是以下任意一个:
                - aes-encoded
                - Aes.Ctr.decrypt
                - charAt
                - charCodeAt
                - document.getElementById
                - document.write
                - eval
                - eval+unescape
                - fromCharCode
                - location
                - malformed
                - Math
                - obfuscated
                - ParseInt
                - replace
                - substr
                - unescape
                - write
                - write+unescape
                """)
        List<String> tags
) {
}
