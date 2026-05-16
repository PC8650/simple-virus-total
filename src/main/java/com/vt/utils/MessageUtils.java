package com.vt.utils;

import com.vt.enums.MsgEnum;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 获取国际化信息，基于默认 Locale 或 LocaleContextHolder
     */
    public static String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * 指定 Locale 获取国际化信息
     */
    public static String getMessage(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

    /**
     * 根据语言标识符获取国际化信息
     */
    public static String getMessage(String lang, MsgEnum key, Object... args) {
        return getMessage(key.getKey(), parseLocale(lang), args);
    }

    /**
     * 获取国际化信息 (默认降级 Locale)
     */
    public static String getMessage(MsgEnum key, Object... args) {
        return getMessage(key.getKey(), Locale.getDefault(), args);
    }

    public static String getMessage(MsgEnum key) {
        return getMessage(key.getKey(), Locale.getDefault());
    }

    /**
     * 根据字符串解析 Locale
     */
    private static Locale parseLocale(String lang) {
        if (StringUtils.hasText(lang)) {
            return Locale.forLanguageTag(lang);
        }
        return Locale.getDefault();
    }

}
