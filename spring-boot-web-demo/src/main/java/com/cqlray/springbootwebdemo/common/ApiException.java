package com.cqlray.springbootwebdemo.common;

import com.cqlray.springbootwebdemo.util.SpringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Locale;

@Data
public class ApiException extends RuntimeException implements Serializable {

    @Autowired
    private MessageSource messageSource;

    private String code;
    private String message;
    private Object[] args;

    public ApiException(String code, String message) {
        if (messageSource == null) {
            messageSource = SpringUtil.getBean(MessageSource.class);
        }
        this.code = code;
        this.message = !StringUtils.isEmpty(message) ? message : messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public static ApiException create(String code, String message) {
        return new ApiException(code, message);
    }

    public static ApiException create(String code) {
        return new ApiException(code, null);
    }

    public ApiException(String code, String message, Object... args) {
        this.code = code;
        this.message = !StringUtils.isEmpty(message) ? message : messageSource.getMessage(code, args, Locale.getDefault());//LocaleContextHolder.getLocale());
        this.args = args;
    }
}