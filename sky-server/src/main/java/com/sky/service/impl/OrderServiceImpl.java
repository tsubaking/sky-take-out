package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserId(currentId);
        if(shoppingCarts == null || shoppingCarts.size() == 0) {
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

        for (ShoppingCart shoppingCart : shoppingCarts) {
            //插入订单详细数据（利用回显的订单id和购物车）
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            //但是id不能采纳
            orderDetail.setId(null);
            orderDetail.setOrderId(order.getId());
            //插入
            orderDetailMapper.insert(orderDetail);

        }

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

    /**
     * 分页查询订单历史记录（每一条历史记录中需要包含订单detail）
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        int page = ordersPageQueryDTO.getPage();
        int pageSize = ordersPageQueryDTO.getPageSize();
        //设置并开启分页
        PageHelper.startPage(page,pageSize);
        //条件分页查询
        Long userId = BaseContext.getCurrentId();
        ordersPageQueryDTO.setUserId(userId);
        Page<Orders> orders = orderMapper.selectByCondition(ordersPageQueryDTO);
        //封装VO结果
        //OrderVO orderVo = new OrderVO();
        List<OrderVO> list = new ArrayList<>();
        for (Orders order : orders) {
            //查询订单对应的订单详细信息
            List<OrderDetail> details = orderDetailMapper.selectByOrderId(order.getId());

            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order,orderVO);
            orderVO.setOrderDetailList(details);
            list.add(orderVO);
        }
        return new PageResult(orders.getTotal(),list);
    }

    /**
     * 查询具体订单信息，封装成VO
     * @param id
     * @return
     */
    @Override
    public OrderVO getById(Long id) {
        Orders order = orderMapper.selectById(id);

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        //具体细节
        List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(id);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    /**
     * 通过订单id取消订单
     * @param id
     */
    @Override
    public void cancelOrder(Long id) {

        // 根据id查询订单
        Orders order = orderMapper.selectById(id);
        // 校验订单是否存在
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        /**
         *订单状态：1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
         * 取消订单需要考虑订单的状态：
         * 订单状态<3，可直接取消，否则不行（需要联系商家）
         *
         * 最后需要表格中更新订单信息
         */
        if(order.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //可取消订单，并退款（暂时无法实现）
        // 更新订单状态、取消原因、取消时间
        order.setStatus(Orders.CANCELLED);
        order.setCancelReason("用户取消");
        order.setCancelTime(LocalDateTime.now());
        orderMapper.update(order);
    }
}
