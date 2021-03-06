package me.kafeitu.demo.activiti.rediscache;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class RedisSessionContext {
	private static final String WEB_USER = "demo-activiti_";
	// session分钟过期时间
	@Value("${web.session.minTimeout}")
	private int webSessionMinTimeOut = 10;
	@Autowired
	private JedisConnection redis;
	private long getWebExprieTime(){
		return webSessionMinTimeOut * 60;
	}
	private String getWebKey(String key){
		return WEB_USER+key;
	}
	
	public void removeWebUser(HttpServletRequest request){
		String key = request.getSession().getId();
		redis.del(getWebKey(key));
	}
	public void setWebUser(HttpServletRequest request,User user){
		String key = request.getSession().getId();
		redis.setObject(getWebKey(key),user,getWebExprieTime());
	}
	public void setCookieUser(HttpServletResponse response, Object cookieName){
	    Cookie cookie = new Cookie(CookieConst.USER_COOKIE, UUID.randomUUID().toString());
        cookie.setPath("/");
        cookie.setMaxAge(3600*1000);
        response.addCookie(cookie);
        redis.setObject(cookie.getValue(),(User)cookieName);
	}
	public Object getCookie(HttpServletRequest request){
	    Cookie[] cookies = request.getCookies();
	    for(Cookie cookie : cookies){
	        Object redisKey = (Object) redis.getObject(cookie.getValue());
	        if(null!=redisKey){
	            return redisKey;
	        }
	    }
	    return null;
	}
	public void removeCookie(String userName){
	    redis.del(userName);
	}
	public void setUser(HttpServletRequest request,String string){
	    String key = request.getSession().getId();
	    redis.setObject(getWebKey(key),string);
	}
	
	public User getWebUser(HttpServletRequest request){
		String key = request.getSession().getId();
		Object o = redis.getObject(getWebKey(key));
		if(null==o){
			return null;
		}
		//SessionHolder.setSessionId(key);
		webHeartbeat(key);
		return (User) o;
	}
	
	public void webHeartbeat(String key) {
		redis.expire(getWebKey(key),getWebExprieTime());
	}
}
