package com.lino.secondkill.controller;


import com.alibaba.druid.util.StringUtils;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.service.LoginService;
import com.lino.secondkill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/GoodsController")
public class GoodsController {
    private static Logger logger = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisService redisService;
    @RequestMapping(value = "/to_list")
    public String toLogin(HttpServletResponse response,Model model,
//                          @CookieValue(value = LoginService.COOKIE_NAME_TOKEN,required = false)String cookieToken,
//                          @RequestParam(value = LoginService.COOKIE_NAME_TOKEN,required = false)String paramToken,
                          SecondkillUser secondkillUser
    ){
        if(secondkillUser == null){
            return "login";
        }


        model.addAttribute("user",secondkillUser);
        return "good_list";
    }




}
