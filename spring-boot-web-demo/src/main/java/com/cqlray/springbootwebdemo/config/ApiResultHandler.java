package com.cqlray.springbootwebdemo.config;

import com.cqlray.springbootwebdemo.common.ApiException;
import com.cqlray.springbootwebdemo.common.ApiResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiResultHandler implements ResponseBodyAdvice {

    @Autowired
    private MessageSource messageSource;

    private ThreadLocal<ObjectMapper>  mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);

    private static final Class[] annos = {
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            DeleteMapping.class,
            PutMapping.class
    };

    private static final String[] swaggerPaths = {"/v2/api-docs", "/swagger-resources"};

    /**
     * 对所有RestController的接口方法进行拦截
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        AnnotatedElement element = returnType.getAnnotatedElement();
        return Arrays.stream(annos).anyMatch(anno -> anno.isAnnotation() && element.isAnnotationPresent(anno));
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Object out;
        ObjectMapper mapper = mapperThreadLocal.get();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (body == null) {
            out = ApiResult.valueOf("操作成功！");
        } else if (body instanceof ApiResult) {
            out = body;
        } else if (body instanceof String) {
            ApiResult result = ApiResult.valueOf(body);
            try {
                out = mapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                out = ApiResult.errorOf("10003", e.getMessage());
            }
        } else if (Arrays.stream(swaggerPaths).anyMatch(path -> request.getURI().getPath().contains(path))) {
            //排除对swagger的干扰
            out = body;
        } else {
            out = ApiResult.valueOf(body);
        }

        return out;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object runtimeExceptionHandler(RuntimeException ex) {
        return ApiResult.errorOf("10000", ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public Object iOExceptionHandler(IOException ex) {
        return ApiResult.errorOf("10002", ex.getMessage());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Object illegalArgumentExceptionHandler(IllegalArgumentException ex){
        return ApiResult.errorOf("10003", ex.getMessage());
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Object securityExceptionHandler(SecurityException ex){
        return ex;
    }

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object apiExceptionHandler(ApiException ex) {
        return ApiResult.errorOf(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object constraintViolationException(ConstraintViolationException e) {
        return ApiResult.errorOf("10003", e.getConstraintViolations().iterator().next().getMessage());
    }
}
