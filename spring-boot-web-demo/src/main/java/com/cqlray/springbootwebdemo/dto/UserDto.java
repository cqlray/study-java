package com.cqlray.springbootwebdemo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
@ApiModel("UserDto")
public class UserDto {

    @NotNull(message = "姓名必填")
    @ApiModelProperty(value = "姓名", example = "张三", required = true)
    private String name;

    @NotEmpty(message = "爱好至少一条数据")
    @ApiModelProperty(value = "爱好", example = "[]", required = true)
    private List<String> hobbies;

    @Min(message = "年龄不能小于0",value = 0)
    @Max(message = "年龄不会超过150", value = 150)
    @ApiModelProperty(value = "年龄", example = "6")
    private Integer age;
}
