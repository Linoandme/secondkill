package com.lino.secondkill.vo;

import com.lino.secondkill.domain.SecondkillGoods;
import com.lino.secondkill.domain.SecondkillUser;

public class SecondkillMessage {
    private SecondkillUser secondkillUser;
    private Long goodsId;

    public SecondkillUser getSecondkillUser() {
        return secondkillUser;
    }

    public void setSecondkillUser(SecondkillUser secondkillUser) {
        this.secondkillUser = secondkillUser;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}
