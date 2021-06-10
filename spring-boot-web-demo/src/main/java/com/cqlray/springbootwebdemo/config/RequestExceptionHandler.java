package com.cqlray.springbootwebdemo.config;

import com.cqlray.springbootwebdemo.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler {
    // 方法上单个普通类型（如：String、Long等）参数校验异常（校验注解直接写在参数前面的方式）
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ApiResult constraintViolationException(ConstraintViolationException e) {
        return ApiResult.errorOf("400", e.getConstraintViolations().iterator().next().getMessage());
    }

    // 对方法上@RequestBody的Bean参数校验的处理
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.ok(ApiResult.errorOf("400", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    // 对方法的Form提交参数绑定校验的处理
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.ok(ApiResult.errorOf("400", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.ok(ApiResult.errorOf(status.value() + "", ex.toString()));
    }
}
