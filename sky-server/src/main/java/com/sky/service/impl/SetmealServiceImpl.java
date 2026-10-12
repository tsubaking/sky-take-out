package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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

    @Resource
    private CategoryMapper categoryMapper;
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

    /**
     * 根据套餐id查询套餐，并查询出套餐对应的套餐菜品关系
     * */
    @Override
    public SetmealVO getById(Long id) {

        Setmeal setmeal = setmealMapper.selectById(id);
        //再查询套餐菜品关系表，获取套餐菜品关系信息
        List<SetmealDish> list = setmealDishMapper.selectBySetmealId(id);

        //转换为VO对象，返回给controller，交给前端系统
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(list);

        //还得根据分类id查询出分类名称
        String nameById = categoryMapper.getNameById(setmealVO.getCategoryId());
        setmealVO.setCategoryName(nameById);

        return setmealVO;
    }

    /**
     * 根据id批量删除套餐
     * 删除套餐后，对应的套餐菜品关联信息也得删除
     * @param ids
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        setmealMapper.deleteBatch(ids);

        setmealDishMapper.deleteBatchBySetmealId(ids);
    }

    /**
     * 根据套餐id修改套餐状态1起售，0停售
     * @param id
     * @param status
     */
    @Override
    public void changeStatus(long id, int status) {

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }


}
