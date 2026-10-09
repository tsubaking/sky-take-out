package com.sky.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "分类DTO")
public class CategoryDTO implements Serializable {

    //主键
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "1.菜品分类 2.套餐分类",allowableValues = {"1","2"})
    //类型 1 菜品分类 2 套餐分类
    private Integer type;

    @Schema(description = "分类名称")
    //分类名称
    private String name;

    @Schema(description = "排序序号，按照升序排序")
    //排序
    private Integer sort;

}
