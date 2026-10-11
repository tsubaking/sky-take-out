package com.sky.service;

import com.sky.dto.SetmealDTO;

public interface SetmealService {

    /**
     * 新增套餐（套餐数据内含菜品信息）
     * */
    void addSetmealWithDish(SetmealDTO setmealDTO);
}
