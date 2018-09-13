package com.lino.secondkill.service;

import com.lino.secondkill.dao.SecondkillDao;
import com.lino.secondkill.domain.Goods;
import com.lino.secondkill.domain.OrderInfo;
import com.lino.secondkill.domain.SecondkillOrder;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.redis.GoodsKey;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class SecondkillService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    public OrderInfo secondkill(SecondkillUser secondkillUser, GoodsVo goods) {
        //减库存，下订单，写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if(success){
            return orderService.createOrder(secondkillUser,goods);
        }else {
            //做一个标记，表示东西已经卖完了
            setGoodsOver(goods.getGoods_id());
            return  null;
        }
    }

    private void setGoodsOver(Long goods_id) {
        redisService.set(GoodsKey.goodsOver,""+goods_id,true);
    }



    public long getSecondkillResult(Long id, long goods_id) {
        SecondkillOrder secondkillOrderByUseridGoodsid = orderService.getSecondkillOrderByUseridGoodsid(id, goods_id);
        if(secondkillOrderByUseridGoodsid!=null){
            return secondkillOrderByUseridGoodsid.getOrder_id();
        }else{
            if(!getGoodsOver(goods_id)){
                return -1;
            }else{
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goods_id) {
        return redisService.exists(GoodsKey.goodsStock,String.valueOf(goods_id));
    }



    public BufferedImage createVerifyCode(SecondkillUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(GoodsKey.getSecondkilVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerfiCode(SecondkillUser secondkillUser, long goods_id, int verfiCode) {
        if(secondkillUser ==null|| goods_id<=0 ){
            return false;
        }
        Integer redisCode = redisService.get(GoodsKey.getSecondkilVerifyCode, secondkillUser.getId()+","+goods_id, Integer.class);
        if(redisCode==null||redisCode-verfiCode==0){
            return false;
        }

        redisService.delete(GoodsKey.getSecondkilVerifyCode, secondkillUser.getId()+","+goods_id);

        return true;
    }
}
