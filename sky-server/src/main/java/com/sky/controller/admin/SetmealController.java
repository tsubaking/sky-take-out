package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
//@Tag(name = "套餐管理",description = "套餐管理相关接口")
@Api(tags = "套餐管理接口")
public class SetmealController {

    @Resource
    private SetmealService setmealService;

    /**
     * 新增套餐（套餐数据内含有菜品信息）
     * @Param SetmealDTO
     * @return
     * */
    @PostMapping
    @Operation(description = "新增套餐")
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}",setmealDTO);
        setmealService.addSetmealWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 根据主键id查询套餐信息，需要返回套餐以及套餐菜品关系
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @Operation(description = "查询套餐")
    public Result<SetmealVO> getById(@PathVariable @Parameter(description = "套餐id",example = "33") Long id) {
        log.info("根据套餐id{}查询套餐",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }


    /**
     * 根据主键id批量删除套餐
     *
     * @param ids
     * @return
     */
    @Operation(description = "根据id批量删除套餐")
    @DeleteMapping
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("根据id:{}删除套餐",ids);
        setmealService.deleteBatch(ids);

        return Result.success();
    }

    /**
     * 套餐起售停售状态修改，1标识起售，0标识停售
     * @param status
     * @return
     */
    @Operation(description = "修改套餐状态，1起售，0停售")
    @PostMapping("/status/{status}")
    public Result changeStatus(@PathVariable("status") int status,@RequestParam long id) {

        log.info("{}套餐起售停售状态修改：{}",id,status);

        setmealService.changeStatus(id,status);
        return Result.success();
    }

    /**
     * 分页查询套餐信息
     * @param setmealPageQueryDTO
     * @return
     */
    @Operation(description = "分页查询套餐信息")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐：{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 修改套餐（套餐数据内含有菜品信息）
     * @Param SetmealDTO
     * @return
     * */
    @PutMapping
    @Operation(description = "修改套餐")
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}",setmealDTO);
        setmealService.updateSetmealWithDish(setmealDTO);
        return Result.success();
    }
}
