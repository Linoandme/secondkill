package com.lino.secondkill.service;

import com.alibaba.fastjson.JSON;
import com.lino.secondkill.dao.LoginDao;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.util.MD5Util;
import com.lino.secondkill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class LoginService {
    private static Logger logger = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private LoginDao loginDao;
    public CodeMsg login(LoginVo loginVo) {
        SecondkillUser user =loginDao.getUserById(Long.parseLong(loginVo.getMobile()));
        logger.info("数据库秒杀用户："+ JSON.toJSONString(user));
        if(user==null){
            return CodeMsg.MOBILE_NOT_EXIST;
        }
        String dbPass=user.getPassword();
        String saltDB=user.getSalt();
        String calcPass= MD5Util.formPassToDbPass(loginVo.getPassword(),saltDB);
        if(!calcPass.equals(dbPass)){
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCCESS;
    }
}
