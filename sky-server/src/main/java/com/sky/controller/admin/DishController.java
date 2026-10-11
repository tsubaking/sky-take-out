package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "菜品相关接口")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {


    @Resource
    DishService dishService;

    @PostMapping
    @Operation(description = "新增菜品")
    public Result save(
            @RequestBody
            DishDTO dishDTO) {
        log.info("新增菜品：{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(description = "菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);//后绪步骤定义
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @Operation(description = "菜品批量删除")
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("菜品批量删除：{}", ids);
        dishService.deleteBatch(ids);//后绪步骤实现
        return Result.success();
    }

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(description = "根据菜品id查询菜品")
    public Result<DishVO> getById(@PathVariable("id") Long id) {
        log.info("根据菜品id查询菜品：{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);//后绪步骤实现
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @Operation(description = "修改菜品信息")
    public Result update(DishDTO dishDTO) {

        log.info("修改菜品：{}",dishDTO);

        dishService.updateWithFlavor(dishDTO);

        return Result.success();
    }
}
