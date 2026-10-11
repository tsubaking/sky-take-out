package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
//@Tag(name = "菜品管理",description = "菜品管理相关接口")
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理接口")
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
    /*
    严格按 REST 设计时：POST 用于创建/非幂等处理，PUT 用于指定 URI 的完整创建或替换。
    PUT 幂等：因为它是“把指定 URI 的资源替换成请求体状态”，像赋值，重复执行最终状态不变。
    POST 不承诺幂等：因为它是“提交给服务器处理”，服务器可能创建新资源、追加记录或执行动作，重复执行可能产生多次副作用。
    * */
    @PutMapping
    @Operation(description = "修改菜品信息")
    public Result update(DishDTO dishDTO) {

        log.info("修改菜品：{}",dishDTO);

        dishService.updateWithFlavor(dishDTO);

        return Result.success();
    }

    /**
     * 菜品起售停售
     * */
    //起售停售是是“提交给服务器处理”，应该是post请求
    @PostMapping("/status/{status}")
    @Operation(description = "菜品起售停售")
    public Result status(@PathVariable("status")
                             @Parameter(description = "菜品状态,1起售，0停售",example = "1")
                             Integer status,
                         @RequestParam("id")
                         @Parameter(description = "菜品id",example = "1")
                         Long id
                         ) {

        log.info("{}号菜品起售停售修改：{}",id,status);
        dishService.modifyStatusById(id,status);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * */
    @GetMapping("/list")
    @Operation(description = "根据分类查询菜品")
    public Result<List<Dish>> getDishListByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类id{}查询菜品",categoryId);
        List<Dish> list = dishService.modifyStatusById(categoryId);
        return Result.success(list);
    }
}
