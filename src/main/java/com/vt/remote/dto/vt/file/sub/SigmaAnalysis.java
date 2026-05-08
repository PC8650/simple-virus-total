package com.vt.remote.dto.vt.file.sub;

public record SigmaAnalysis(
        Integer high,
        Integer medium,
        Integer critical,
        Integer low
) {
}
