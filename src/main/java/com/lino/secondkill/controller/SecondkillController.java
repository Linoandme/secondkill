package com.lino.secondkill.controller;

import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillOrder;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.service.GoodsService;
import com.lino.secondkill.service.OrderService;
import com.lino.secondkill.service.SecondkillService;
import com.lino.secondkill.vo.GoodsVo;
import com.lino.secondkill.vo.OrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/SecondkillController")
public class SecondkillController {

    private static Logger logger = LoggerFactory.getLogger(SecondkillController.class);


    @Autowired
    private SecondkillService secondkillService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;


    /*
    * 压测数据：qps:66
    *
    * 2000*10
    * */

    @RequestMapping(value = "/secondkill" ,method = RequestMethod.POST)
    @ResponseBody
    public Result secondkill( SecondkillUser secondkillUser, @RequestParam("goods_id") long goods_id){
        logger.info("=======================SecondController--secondkill:");
        if(secondkillUser==null) return Result.error(CodeMsg.SESSION_ERROR);
        GoodsVo goods = goodsService.getGoodsByGoodsId(goods_id);
        //判断库存
        Integer stock = goods.getStock_count();
        if(stock<=0){

            return Result.error(CodeMsg.SECONDKILL_OVER);
        }
        //判断是否秒杀到，不能重复秒杀
        SecondkillOrder secondkillOrder = orderService.getSecondkillOrderByUseridGoodsid(secondkillUser.getId(),goods_id);
        if(secondkillOrder!=null){
            return Result.error(CodeMsg.SECONDKILL_REPEATE);
        }

        //减库存，下订单，写入秒杀订单
        OrderInfo orderInfo =  secondkillService.secondkill(secondkillUser,goods);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setGoodsVo(goods);
        orderDetail.setOrderInfo(orderInfo);
        return Result.success(orderDetail);

    }
}
