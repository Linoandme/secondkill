package com.lino.secondkill.dao;

import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillOrder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface OrderDao {
    @Select("select * from secondkill_order where goods_id=#{goods_id} and user_id=#{user_id} ")
    public SecondkillOrder getSecondkillOrderByUseridGoodsid(@Param("user_id") Long id,@Param("goods_id") long goods_id) ;


    @Insert("insert into order_info (goods_id,user_id,delivery_addr_id,goods_name,goods_count,goods_price,order_channel,status,create_date) values (#{goods_id},#{user_id},#{delivery_addr_id},#{goods_name},#{goods_count},#{goods_price},#{order_channel},#{status},#{create_date})")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select LAST_INSERT_ID()")
    public long insert(OrderInfo orderInfo);



    @Insert("insert into secondkill_order (goods_id,order_id,user_id) values(#{goods_id},#{order_id},#{user_id})")
    public int insertSecondkillOrder(SecondkillOrder secondkillOrder);
}
