package com.lino.secondkill.controller;

import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillOrder;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.rabbitmq.MQSender;
import com.lino.secondkill.redis.GoodsKey;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.service.GoodsService;
import com.lino.secondkill.service.OrderService;
import com.lino.secondkill.service.SecondkillService;
import com.lino.secondkill.vo.GoodsVo;
import com.lino.secondkill.vo.OrderDetail;
import com.lino.secondkill.vo.SecondkillMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/SecondkillController")
public class SecondkillController implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(SecondkillController.class);


    @Autowired
    private SecondkillService secondkillService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender sender;
    //拿来做内存标记
    private Map<Long,Boolean> localOverMap =  new HashMap<Long,Boolean>();
    /*
    * 系统初始化，类加载后会调用该方法
    * */
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        for(GoodsVo goodsVo:goodsVos){
            redisService.set(GoodsKey.goodsStock,""+goodsVo.getGoods_id(),goodsVo.getStock_count());
            localOverMap.put(goodsVo.getGoods_id(),false);
        }
    }


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

        //判断是否已经秒杀过了
        SecondkillOrder secondkillOrder = orderService.getSecondkillOrderByUseridGoodsid(secondkillUser.getId(),goods_id);
        if(secondkillOrder!=null){
            return Result.error(CodeMsg.SECONDKILL_REPEATE);
        }


        //内存标记，减少redis访问
        Boolean flag = localOverMap.get(goods_id);
        if(flag){
            return Result.error(CodeMsg.SECONDKILL_OVER);
        }
        //预减库存
        Long decr = redisService.decr(GoodsKey.goodsStock, "" + goods_id);
        if(decr<0){
            localOverMap.put(goods_id,true);
            return Result.error(CodeMsg.SECONDKILL_OVER);
        }
        //入队
        SecondkillMessage message = new SecondkillMessage();
        message.setGoodsId(goods_id);
        message.setSecondkillUser(secondkillUser);
        sender.secondkillSend(message);


        return Result.success(0);


        /* GoodsVo goods = goodsService.getGoodsByGoodsId(goods_id);
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
        return Result.success(orderDetail);*/

    }







    /*
    * order_id:成功
    * -1：库存不足
    * 0：排队中
    *
    * */
    @RequestMapping(value = "/getSeondkillResult" ,method = RequestMethod.GET)
    @ResponseBody
    public Result getSeondkillResult( SecondkillUser secondkillUser, @RequestParam("goods_id") long goods_id) {
        logger.info("=======================SecondController--getResult:");
        if (secondkillUser == null) return Result.error(CodeMsg.SESSION_ERROR);


        long result = secondkillService.getSecondkillResult(secondkillUser.getId(), goods_id);
        logger.info("轮询返回的："+result);
        return Result.success(result);
    }

}
