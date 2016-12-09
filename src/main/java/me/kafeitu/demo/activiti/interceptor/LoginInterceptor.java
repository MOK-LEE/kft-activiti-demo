package me.kafeitu.demo.activiti.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import me.kafeitu.demo.activiti.rediscache.RedisSessionContext;


public class LoginInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware   {

    RedisSessionContext redisSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        Object o = redisSession.getWebUser(request);
        if(null==o){
//            redisSession.setWebUser(request, null);
//            String url = request.getRequestURL().toString();
            response.sendRedirect("/kft-activiti-demo/login?timeout=true");
            return false;
        }
        return true;
        
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        redisSession = applicationContext.getBean(RedisSessionContext.class);
    }
    

}
