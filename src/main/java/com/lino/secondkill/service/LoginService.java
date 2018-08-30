package com.lino.secondkill.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.lino.secondkill.dao.LoginDao;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.exception.GlobalException;
import com.lino.secondkill.redis.GoodsKey;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.redis.SecondkillUserKey;
import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.util.MD5Util;
import com.lino.secondkill.util.UUIDUtil;
import com.lino.secondkill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class LoginService {
    private static Logger logger = LoggerFactory.getLogger(LoginService.class);
    public static final String COOKIE_NAME_TOKEN="token";
    @Autowired
    private RedisService redisService;
    @Autowired
    private LoginDao loginDao;
    public String login(HttpServletResponse response,LoginVo loginVo) {
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        //判断是否有该用户
        SecondkillUser user =getUserById(Long.parseLong(loginVo.getMobile()));
        logger.info("数据库秒杀用户："+ JSON.toJSONString(user));
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass=user.getPassword();
        String saltDB=user.getSalt();
        String calcPass= MD5Util.formPassToDbPass(loginVo.getPassword(),saltDB);
        if(!calcPass.equals(dbPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }


        String token = UUIDUtil.uuid();
        addCookie(response,token,user);
        return token;
    }

    private SecondkillUser getUserById(long id) {
        //取缓存
        SecondkillUser secondkillUser =redisService.get(SecondkillUserKey.getById,""+id,SecondkillUser.class);
        if(secondkillUser!=null) return secondkillUser;

        //取数据库
        secondkillUser = loginDao.getUserById(id);
        //添加缓存
        if(secondkillUser!=null){
            redisService.set(SecondkillUserKey.getById,""+id,secondkillUser);
        }
        return secondkillUser;
    }

    //更新信息同时更新缓存
    public boolean updatePassword(String token ,long id,String passwordNew){
        SecondkillUser secondkillUser = loginDao.getUserById(id);
        //重新新建一个，提高效率
        SecondkillUser user = new SecondkillUser();
        user.setPassword( MD5Util.formPassToDbPass(passwordNew,secondkillUser.getSalt()));
        user.setId(id);
        loginDao.update(user);
        user.setSalt(secondkillUser.getSalt());

        //更新缓存信息
        redisService.set(SecondkillUserKey.token,token,user);
        redisService.delete(SecondkillUserKey.getById,""+id);
        return true;
    }

    private void addCookie(HttpServletResponse response,String token, SecondkillUser user) {
        //生成cookie


        redisService.set(SecondkillUserKey.token,token,user);

        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(SecondkillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public SecondkillUser getByToken(HttpServletResponse response,String token) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        //延长有效期
        SecondkillUser user =  redisService.get(SecondkillUserKey.token,token,SecondkillUser.class);
        if(user!=null){
            addCookie(response,token,user);
        }

        return  redisService.get(SecondkillUserKey.token,token,SecondkillUser.class);
    }


}
