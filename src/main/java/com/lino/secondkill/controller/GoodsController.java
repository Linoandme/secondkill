package com.lino.secondkill.controller;


import com.alibaba.druid.util.StringUtils;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.service.GoodsService;
import com.lino.secondkill.service.LoginService;
import com.lino.secondkill.vo.GoodsVo;
import com.lino.secondkill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/GoodsController")
public class GoodsController {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);
   @Autowired
   private GoodsService goodsService;
    @RequestMapping(value = "/to_list")
    public String tolist(Model model,
//                          @CookieValue(value = LoginService.COOKIE_NAME_TOKEN,required = false)String cookieToken,
//                          @RequestParam(value = LoginService.COOKIE_NAME_TOKEN,required = false)String paramToken,
                          SecondkillUser secondkillUser
    ){
        logger.info("========================GoodsController-tolist=================");
        if(secondkillUser == null){
            return "login";
        }
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsVoList",goodsVoList);
        return "good_list";
    }
    @RequestMapping(value = "/to_detail/{goods_id}")
    public String toDetail(Model model, SecondkillUser secondkillUser, @PathVariable("goods_id") long goods_id){
        logger.info("========================GoodsController-toDetail=================");
        if(secondkillUser == null){
            return "login";
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
        return "goods_detail";
    }



}
