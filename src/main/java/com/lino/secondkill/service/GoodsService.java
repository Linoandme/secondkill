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

    public boolean reduceStock(GoodsVo goods) {
        SecondkillGoods secondkillGoods = new SecondkillGoods();
        secondkillGoods.setGoods_id(goods.getGoods_id());
        int res =goodsDao.reduceStock(secondkillGoods);
       return res>0;
    }
}
