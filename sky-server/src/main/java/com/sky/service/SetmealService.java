package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.vo.SetmealVO;

public interface SetmealService {

    /**
     * 新增套餐（套餐数据内含菜品信息）
     * */
    void addSetmealWithDish(SetmealDTO setmealDTO);

    /**
     * 根据套餐id查询套餐，以及对应的菜品信息
     * */
    SetmealVO getById(Long id);
}
