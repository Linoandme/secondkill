package com.lino.secondkill.controller;

import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.service.GoodsService;
import com.lino.secondkill.service.OrderService;
import com.lino.secondkill.vo.GoodsVo;
import com.lino.secondkill.vo.OrderDetail;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/orderController")
public class OrderController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;



    @RequestMapping(value = "/getOrderByOrderid",method = RequestMethod.POST)
    @ResponseBody
    public Result getOrderByOrderid(SecondkillUser secondkillUser,long order_id){
        logger.info("orderController--getOrderByOrderid:order_id="+order_id);
        if(secondkillUser==null)
            return Result.error(CodeMsg.SESSION_ERROR);
        OrderInfo orderInfo = orderService.getOrderByOrderid(order_id);
        if(orderInfo==null)
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        long goods_id = orderInfo.getGoods_id();
        GoodsVo goodsvo = goodsService.getGoodsByGoodsId(goods_id);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderInfo(orderInfo);
        orderDetail.setGoodsVo(goodsvo);
        return Result.success(orderDetail);
    }



}
