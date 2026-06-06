package com.vt.component;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * 临时文件清理器
 *
 * 程序关闭时清理相关临时文件夹
 * 只在进行相关配置时进行清理，默认配置时不会越权删除外部文件夹
 */
@Slf4j
@Component
public class TempCleaner {

    @Value("${server.tomcat.basedir:}")
    private String tomcat;

    @Value("${spring.servlet.multipart.location:}")
    private String multipart;

    @PreDestroy
    private void clean() {
        clean(multipart);
        clean(tomcat);
    }

    private void clean(String dir) {
        if (!StringUtils.hasText(dir)) return;

        File file = new File(dir);
        if (!file.exists() || !file.isDirectory()) return;

        boolean delete = FileSystemUtils.deleteRecursively(file);
        log.info("delete temp dir {} - {} ", dir, delete);
    }

}
