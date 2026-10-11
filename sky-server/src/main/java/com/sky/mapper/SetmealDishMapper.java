package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询菜品套餐
     * */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);
}
