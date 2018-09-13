package com.lino.secondkill.config;

import com.alibaba.druid.util.StringUtils;
import com.lino.secondkill.access.UserContext;
import com.lino.secondkill.domain.SecondkillUser;
import com.lino.secondkill.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private LoginService loginService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //判断是否有这个参数，有才做下面的处理，没有的话就不处理
        Class<?> clazz = methodParameter.getParameterType();
        return clazz== SecondkillUser.class;
    }
    //处理
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
       /* HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String paramToken = request.getParameter(LoginService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,LoginService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(paramToken)&&StringUtils.isEmpty(cookieToken)){
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return loginService.getByToken(response,token);*/
        //通过Threadlocal获取user
       return UserContext.getUser();
    }

/*
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
*/


}
