package me.kafeitu.demo.activiti.interceptor;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.identity.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import me.kafeitu.demo.activiti.rediscache.CookieConst;
import me.kafeitu.demo.activiti.rediscache.RedisSessionContext;
import me.kafeitu.demo.activiti.util.UserUtil;


public class LoginInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware   {

    RedisSessionContext redisSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
//        Object o = redisSession.getWebUser(request);
//        String uri= request.getRequestURI();
//        if(null==o){
//            response.sendRedirect("/");
//            return false;
//        }
        User user = (User) redisSession.getCookie(request);
        if(null!=user){
            return true;
        }
        response.sendRedirect("/");
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        redisSession = applicationContext.getBean(RedisSessionContext.class);
    }
    

}
