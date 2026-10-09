package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "提交的员工数据")
public class EmployeeDTO implements Serializable {

    @Schema(description = "主键id",example = "2")
    private Long id;

    @Schema(description = "用户名（不可重复）" ,example = "thsubaking",required = true)
    private String username;

    @Schema(description = "姓名" ,example = "张三",required = true)
    private String name;

    @Schema(description = "电话（11位）" ,example = "13765592460",required = true)
    private String phone;

    @Schema(description = "性别（男：1，女：2）" ,allowableValues = {"1","2"},example = "1",required = true)
    private String sex;

    @Schema(description = "身份证号" ,required = true)
    private String idNumber;

}
