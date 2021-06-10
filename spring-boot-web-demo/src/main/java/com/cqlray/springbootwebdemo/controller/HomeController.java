package com.cqlray.springbootwebdemo.controller;

import com.cqlray.springbootwebdemo.common.ApiException;
import com.cqlray.springbootwebdemo.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Slf4j
@RestController
@RequestMapping("home")
@Api(value = "home", tags = "1.0")
public class HomeController {

    @Valid
    @GetMapping("hello")
    @ApiOperation(value = "测试请求字段必填", notes = "测试请求字段必填")
    public String Hello(@NotEmpty @ApiParam(value = "姓名") @RequestParam(value = "name") String name){
        return "Hello " + name;
    }

    @GetMapping("error")
    @ApiOperation(value = "测试错误码", notes = "测试错误码")
    public void getError(@ApiParam(value = "错误码") @RequestParam(value = "code") String code){
        if(StringUtils.isEmpty(code)){
            code = HttpStatus.BAD_REQUEST.value() + "";
        }
        throw ApiException.create(StringUtils.isEmpty(code) ? HttpStatus.BAD_REQUEST.value() + "" : code, "请求错误码为" + code);
    }

    @PostMapping("/user")
    @ApiOperation(value = "测试请求消息体校验", notes = "测试请求消息体校验")
    public UserDto insertUser(@Valid @RequestBody UserDto userDto){
        return userDto;
    }

}
