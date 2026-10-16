package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
/**
 * 处理定时任务
 */
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理支付超时订单
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理支付超时订单：{}", new Date());
        //从数据库中查找下单时间order_time距离现在的时间大于15分钟的，并且没有付款的
        LocalDateTime now = LocalDateTime.now();
        //15分钟前
        LocalDateTime time = now.plusMinutes(-15);
        List<Orders> list = orderMapper.listBeforeTime(time,Orders.PENDING_PAYMENT);
        //然后进行修改
        if(list != null && list.size() > 0) {
            list.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("支付超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });
        }
    }

    /**
     * 处理“派送中”状态的订单
     * 有些订单已经送达了，但是可能忘记点击完成了
     * 如果已经过了订单时间2小时，并且还是配送中，就需要处理
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void processDeliveryOrder(){
        log.info("处理派送中订单：{}", new Date());
        LocalDateTime time = LocalDateTime.now().plusHours(-2);
        //正在配送中，且已经超过订单时间2小时
        List<Orders> orders = orderMapper.listBeforeTime(time, Orders.DELIVERY_IN_PROGRESS);
        if(orders != null && orders.size() > 0) {
            orders.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            });
        }

    }

}
