package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("adminShopController")
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/admin/shop")
public class ShopController {

    private static final String KEY = "SHOP_STATUS";

    @Resource
    private RedisTemplate redisTemplate;

    @PutMapping("{status}")
    @Operation(description = "修改店铺状态")
    public Result putStatus(@PathVariable("status") @Parameter(description = "1表示营业，0表示打烊") int status) {
        log.info("修改店铺状态为：{}",status == 1 ? "营业" : "打烊");

        redisTemplate.opsForValue().set(KEY,status);

        return Result.success();
    }

    @GetMapping("status")
    @Operation(description = "获取店铺状态")
    public Result<Integer> getStatus() {
        log.info("获取店铺状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }
}
