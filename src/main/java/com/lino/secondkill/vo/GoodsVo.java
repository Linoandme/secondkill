package com.lino.secondkill.vo;

import com.lino.secondkill.domain.Goods;

import java.util.Date;

public class GoodsVo extends Goods {
    private Long goods_id;
    private Integer stock_count;
    private Date start_date;
    private Date end_date;
    private Double goods_price;
    private Double secondkill_price;


    public Double getSecondkill_price() {
        return secondkill_price;
    }

    public void setSecondkill_price(Double secondkill_price) {
        this.secondkill_price = secondkill_price;
    }

    public Long getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Long goods_id) {
        this.goods_id = goods_id;
    }

    public Integer getStock_count() {
        return stock_count;
    }

    public void setStock_count(Integer stock_count) {
        this.stock_count = stock_count;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    @Override
    public Double getGoods_price() {
        return goods_price;
    }

    @Override
    public void setGoods_price(Double goods_price) {
        this.goods_price = goods_price;
    }
}
