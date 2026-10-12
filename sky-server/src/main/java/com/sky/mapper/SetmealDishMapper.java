package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据套餐id查询出所有的套餐菜品关系
     * */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> selectBySetmealId(Long id);

    /**
     * 根据套餐id批量删除套餐菜品关系数据
     * @param ids
     */
    void deleteBatchBySetmealId(List<Long> ids);
}
