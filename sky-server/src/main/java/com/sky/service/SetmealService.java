package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐（套餐数据内含菜品信息）
     * */
    void addSetmealWithDish(SetmealDTO setmealDTO);

    /**
     * 根据套餐id查询套餐，以及对应的菜品信息
     * */
    SetmealVO getById(Long id);

    /**
     * 根据id批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id修改套餐状态
     * @param id
     * @param status
     */
    void changeStatus(long id, int status);

    /**
     * 分页查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return
     */
    void updateSetmealWithDish(SetmealDTO setmealDTO);
}
