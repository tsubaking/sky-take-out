package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @Operation(description = "订单分页查询")
    public Result<PageResult> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("管理员订单分页查询：{}",ordersPageQueryDTO);
        PageResult pageResult = orderService.adminPageQuery(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @Operation(description = "订单数据统计")
    public Result<OrderStatisticsVO> orderStatistics() {
        log.info("管理员申请查询订单数据统计");
        OrderStatisticsVO vo = orderService.getStatistic();
        return Result.success(vo);
    }


    @GetMapping("/details/{id}")
    @Operation(description = "查询订单详情")
    public Result<OrderVO> getById(@PathVariable("id") Long id) {
        log.info("查询订单详情，订单id：{}",id);
        OrderVO byId = orderService.getById(id);
        return Result.success(byId);
    }

    @PutMapping("/confirm")
    @Operation(description = "接单")
    public Result accept(@RequestBody Long id) {
        log.info("接单，订单id：{}",id);
        orderService.acceptOrder(id);
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }
}
