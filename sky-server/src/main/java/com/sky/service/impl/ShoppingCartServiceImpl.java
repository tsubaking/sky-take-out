package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private DishMapper dishMapper;

    @Resource
    private SetmealMapper setmealMapper;
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //先判断商品在购物车中是否已存在，如果已存在则只需要数量+1
        //查询自己的购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> shoppingCarts = shoppingCartMapper.list(shoppingCart);

        //如果购物车中已经有了需要添加的商品
        if(shoppingCarts != null && shoppingCarts.size() == 1) {
            //更新数量
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            //更新
            shoppingCartMapper.updateNumber(shoppingCart);
        }
        //如果没有，就需要插入新一行数据
        else {
            String name;
            String image;
            BigDecimal price;

            //如果是菜品
            if(shoppingCart.getDishId() != null) {
                Dish dish = dishMapper.getById(shoppingCart.getDishId());
                name = dish.getName();
                image = dish.getImage();
                price = dish.getPrice();

            }
            //如果是套餐
            else {
                Setmeal setmeal = setmealMapper.selectById(shoppingCart.getSetmealId());
                name = setmeal.getName();
                price = setmeal.getPrice();
                image = setmeal.getImage();

            }
            shoppingCart.setAmount(price);
            shoppingCart.setImage(image);
            shoppingCart.setName(name);
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //插入
            shoppingCartMapper.insert(shoppingCart);
        }
    }
}
