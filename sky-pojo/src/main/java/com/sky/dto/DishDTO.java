package com.sky.dto;

import com.sky.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "菜品DTO")
public class DishDTO implements Serializable {

    private Long id;
    //菜品名称
    @Schema(description = "菜品名称")
    private String name;

    @Schema(description = "菜品分类ID")
    //菜品分类id
    private Long categoryId;
    @Schema(description = "菜品价格")
    //菜品价格
    private BigDecimal price;
    //图片
    @Schema(description = "菜品图片")
    private String image;
    //描述信息
    @Schema(description = "菜品描述")
    private String description;
    //0 停售 1 起售
    @Schema(description = "菜品状态：0停售，1起售")
    private Integer status;
    //口味
    @Schema(description = "菜品口味")
    private List<DishFlavor> flavors = new ArrayList<>();

}
