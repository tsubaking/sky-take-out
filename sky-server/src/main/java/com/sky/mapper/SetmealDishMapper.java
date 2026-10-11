package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询菜品套餐
     * */
    List<Long> getSetmealIdsByDishIds(List<Long> ids);

    /**
     * 插入多条套餐菜品关系数据
     *
     * */
    void insertList(List<SetmealDish> setmealDishes);
}
