package com.lino.secondkill.access;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.redis.AccessKey;
import com.lino.secondkill.redis.RedisService;
import com.lino.secondkill.result.CodeMsg;
import com.lino.secondkill.result.Result;
import com.lino.secondkill.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisService redisService;
    //方法进入之前处理
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            SecondkillUser user = getUser(response, request);
            //通过Threadlocal设置user
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod)handler;
            //获取方法上面的注解
            AccessLimit methodAnnotation = hm.getMethodAnnotation(AccessLimit.class);
            if(methodAnnotation==null){
                return true;
            }
            int maxCount = methodAnnotation.maxCount();
            int second = methodAnnotation.second();
            boolean needLogin = methodAnnotation.needLogin();
            String key = request.getRequestURI();
            if(needLogin){
                if(user==null){
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key+="_"+user.getId();

            }else{
                //do nothing
            }

            AccessKey accessKey = AccessKey.with(second);
            Integer count = redisService.get(accessKey, key, Integer.class);
            if(count==null){
                redisService.set(accessKey,key,1);
            }else if(count<maxCount){
                redisService.incr(accessKey,key);
            }else {
                render(response,CodeMsg.REQUEST_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out=response.getOutputStream();
        String str = JSON.toJSONString(Result.error(sessionError));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();

    }

    private SecondkillUser getUser(HttpServletResponse response,HttpServletRequest request){
        String paramToken = request.getParameter(LoginService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,LoginService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return loginService.getByToken(response,token);
    }
    private String getCookieValue(HttpServletRequest request, String cookieNameToken) {
        Cookie[] cookies = request.getCookies();
        if(cookies==null||cookies.length<=0) return null;
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieNameToken)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
