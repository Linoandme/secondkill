package com.lino.secondkill.controller;


import com.alibaba.druid.util.StringUtils;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.redis.GoodsKey;
import com.lino.secondkill.redis.RedisService;

import com.lino.secondkill.service.GoodsService;

import com.lino.secondkill.vo.GoodsVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "/GoodsController")
public class GoodsController {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);
   @Autowired
   private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private ApplicationContext applicationContext;

   /*
   * 压测数据：476qps
   * 5000*10
   *
   * */
    @RequestMapping(value = "/to_list")
    @ResponseBody
    public String tolist(Model model,
                            HttpServletResponse response,
                           HttpServletRequest request,
                          SecondkillUser secondkillUser
    ){
        logger.info("========================GoodsController-tolist=================");
        if(secondkillUser == null){
            return "login";
        }
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsVoList",goodsVoList);
      //  return "good_list";


        /*
        *
        * 页面缓存处理
        * */
        //1.取缓存
        String html = redisService.get(GoodsKey.goodsList,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        //2.手动渲染
        WebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("good_list", ctx);

        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.goodsList,"",html);
        }
        return html;
    }
    @RequestMapping(value = "/to_detail/{goods_id}")
    @ResponseBody
    public String toDetail(Model model, SecondkillUser secondkillUser, @PathVariable("goods_id") long goods_id,HttpServletRequest request,HttpServletResponse response){
        logger.info("========================GoodsController-toDetail=================");
        if(secondkillUser == null){
            return "login";
        }
        //页面缓存
        String html = redisService.get(GoodsKey.goodsDetail,String.valueOf(goods_id),String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }




        GoodsVo goodsVo=  goodsService.getGoodsByGoodsId(goods_id);
        long start = goodsVo.getStart_date().getTime();
        long end = goodsVo.getEnd_date().getTime();
        long now = System.currentTimeMillis();

        int secondkillStatus = 0;//0未开始，1进行中，2已经结束
        int remainSeconds=0;
        if(start>now){
            secondkillStatus=0;
            remainSeconds = (int) (start-now)/1000;
        }else if (now >end){
            secondkillStatus=2;
        }else{
            secondkillStatus=1;

        }
        model.addAttribute("secondkillStatus",secondkillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("user",secondkillUser);


        //2.手动渲染
        WebContext ctx = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);

        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.goodsList,String.valueOf(goods_id),html);
        }
        return html;
    }



}
