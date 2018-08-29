package com.lino.secondkill.controller;

import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.result.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping(value = "UserController")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @RequestMapping(value = "userInfo")
    @ResponseBody
    public Result userInfo(Model model, SecondkillUser secondkillUser){
        logger.info("==============userInfo："+secondkillUser.getNickname());
        return Result.success(secondkillUser);
    }
}
