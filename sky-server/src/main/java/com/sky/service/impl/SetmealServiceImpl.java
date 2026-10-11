package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Resource
    private SetmealMapper setmealMapper;

    @Resource
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增套餐（套餐数据内含菜品信息）
     * */
    @Transactional
    @Override
    public void addSetmealWithDish(SetmealDTO setmealDTO) {
        //转换为entity
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //插入新的套餐数据
        //插入完后，需要回馈id
        setmealMapper.insert(setmeal);

        //插入套餐内的菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0) {
            for (SetmealDish setmealDish : setmealDishes) {
                //设置菜品所属的套餐的id
                setmealDish.setSetmealId(setmeal.getId());
            }
            //插入所有数据
            setmealDishMapper.insertList(setmealDishes);
        }
    }
}
