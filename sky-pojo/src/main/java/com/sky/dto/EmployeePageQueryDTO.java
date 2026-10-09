package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "员工分页查询提交数据对象")
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    @Schema(description = "员工姓名",example = "张三",required = true)
    private String name;

    //页码
    @Schema(description = "页码",example = "1",required = true)
    private int page;

    //每页显示记录数
    @Schema(description = "每页显示条数",example = "10",required = true)
    private int pageSize;

}
