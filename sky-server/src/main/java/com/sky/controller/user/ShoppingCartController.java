package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "购物车相关接口")
@Slf4j
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService service;

    /**
     * 添加购物车商品
     * @param shoppingCartDTO
     * @return
     */
    @Operation(description = "添加购物车")
    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车：{}",shoppingCartDTO);

        service.add(shoppingCartDTO);
        return Result.success();
    }


    @GetMapping("/list")
    @Operation(description = "查询购物车内容")
    public Result<List<ShoppingCart>> list() {
        log.info("查询购物车内容");
        return Result.success(service.list());
    }
}
