package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AddressBookMapper addressBookMapper;

    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        /**
         * 用户订单的完整业务过程：
         *  判断地址，购物车，配送范围
         *  构造订单数据
         *  插入订单数据
         *  插入订单详细数据
         *  购物车清空
         *  构造VO
         */
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        //取出地址信息
        AddressBook address = addressBookMapper.getById(addressBookId);
        if(address == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //取出购物车信息
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = shoppingCartMapper.selectByUserId(currentId);
        if(shoppingCart == null) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        //配送范围这里暂时无法检验

        //构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,order);
        order.setPhone(address.getPhone());
        order.setAddress(address.getDetail());
        order.setConsignee(address.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(currentId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        //插入数据
        orderMapper.insert(order);

        //插入订单详细数据（利用回显的订单id和购物车）
        OrderDetail orderDetail = new OrderDetail();
        BeanUtils.copyProperties(shoppingCart,orderDetail);
        //但是id不能采纳
        orderDetail.setId(null);
        orderDetail.setOrderId(order.getId());

        //插入
        orderDetailMapper.insert(orderDetail);

        //清空购物车（其实放在付款结束后更合理）
        shoppingCartMapper.deleteCartByUserId(currentId);

        //构造VO
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(order.getId());
        orderSubmitVO.setOrderNumber(order.getNumber());
        orderSubmitVO.setOrderAmount(order.getAmount());
        orderSubmitVO.setOrderTime(LocalDateTime.now());

        return orderSubmitVO;
    }
}
