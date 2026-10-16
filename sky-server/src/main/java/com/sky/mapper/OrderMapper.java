package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper {


    void insert(Orders order);

    /**
     * 根据条件查询订单，用于分页查询
     * 条件：userId,status
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> selectByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    @Select("select * from orders where id = #{id}")
    Orders selectById(Long id);

    void update(Orders order);

    /**
     * 根据传入的订单状态，统计数据
     * @param toBeConfirmed
     * @return
     */
    @Select("select count(*) from orders where status = #{toBeConfirmen}")
    Integer countStatus(Integer toBeConfirmed);
}
