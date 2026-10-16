package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getById(Long id);

    void cancelOrder(Long id);

    void repetition(Long id);

    PageResult adminPageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO getStatistic();

    void acceptOrder(Long id);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);
}
