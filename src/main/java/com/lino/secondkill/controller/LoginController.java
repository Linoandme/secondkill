package com.lino.secondkill.controller;


import com.alibaba.druid.util.StringUtils;
import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.service.LoginService;
import com.lino.secondkill.util.ValidatorUtil;
import com.lino.secondkill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/LoginController")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;
    @GetMapping(value = "/to_login")
    public String toLogin(){
        return "login";
    }

    @PostMapping(value = "do_login")
    @ResponseBody
    public Result doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        logger.info(loginVo.toString());
       String token = loginService.login(response,loginVo);
        return Result.success(token);
    }

}
