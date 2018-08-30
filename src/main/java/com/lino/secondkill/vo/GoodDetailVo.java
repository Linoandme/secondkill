package com.lino.secondkill.vo;

import com.lino.secondkill.domain.SecondkillUser;

public class GoodDetailVo {
    private int secondkillStatus;
    private int remainSeconds;
    private GoodsVo goodsVo;
    private SecondkillUser secondkillUser;


    public SecondkillUser getSecondkillUser() {
        return secondkillUser;
    }

    public void setSecondkillUser(SecondkillUser secondkillUser) {
        this.secondkillUser = secondkillUser;
    }

    public int getSecondkillStatus() {
        return secondkillStatus;
    }

    public void setSecondkillStatus(int secondkillStatus) {
        this.secondkillStatus = secondkillStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }
}
