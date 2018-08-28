package com.lino.secondkill.service;

import com.lino.secondkill.dao.GoodsDao;
import com.lino.secondkill.domain.SecondkillGoods;
import com.lino.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsByGoodsId(long goods_id) {
        return goodsDao.getGoodsByGoodsId(goods_id);
    }

    public void reduceStock(GoodsVo goods) {
        SecondkillGoods secondkillGoods = new SecondkillGoods();
        secondkillGoods.setId(goods.getId());
        goodsDao.reduceStock(secondkillGoods);
    }
}
