package com.cqlray.springbootwebdemo.common;

import com.cqlray.springbootwebdemo.util.SpringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@Data
@NoArgsConstructor
@ApiModel("result")
public class ApiResult {
    @JsonIgnore
    private static final Object lock = new Object();

    @JsonIgnore
    @Autowired
    private static MessageSource messageSource;

    @ApiModelProperty("应用ID")
    private String APP_ID;
    @ApiModelProperty("相应消息码")
    private String RET_CODE;
    @ApiModelProperty("相应消息")
    private String RET_MSG;
    @ApiModelProperty("消息体")
    private Object DATA;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss")
//    private Date TIMESTAMP;

    public ApiResult(String code, String message, Object data) {
        this.RET_CODE = code;
        this.RET_MSG = message;
        this.DATA = data;
//        this.TIMESTAMP = new Date();
    }

    public static ApiResult valueOf(Object data) {
        return new ApiResult("0000","success",data);
    }

    public static ApiResult errorOf(String code, Object data) {
        initMessageSource();
        String message = null;

        message = getHttpStatusMessage(code);
        if(message == null) {
            message = messageSource.getMessage(code, null, Locale.getDefault());
        }
        return new ApiResult(code, message, data);
    }

    private static String getHttpStatusMessage(String code) {
        String message = null;
        HttpStatus status = HttpStatus.resolve(Integer.valueOf(code));
        if(status != null){
            message = status.getReasonPhrase();
        }
        return message;
    }

    private static void initMessageSource() {
        if(messageSource == null){
            synchronized(lock){
                if(messageSource == null){
                    messageSource = SpringUtil.getBean(MessageSource.class);
                }
            }
        }
    }
}

