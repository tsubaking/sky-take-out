package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐管理")
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
}
