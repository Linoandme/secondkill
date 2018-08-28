package com.lino.secondkill.dao;

import com.lino.secondkill.domain.SecondkillGoods;
import com.lino.secondkill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Mapper
public interface GoodsDao {
    @Select("select * from secondkill_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();
    @Select("select * from secondkill_goods mg left join goods g on mg.goods_id = g.id where goods_id=#{goods_id}")
    public GoodsVo getGoodsByGoodsId(@Param("goods_id") long goods_id);
    @Select("update secondkill_goods set stock_count = stock_count-1 where goods_id = #{goods_id}")
    public void reduceStock(SecondkillGoods secondkillGoods);
}
