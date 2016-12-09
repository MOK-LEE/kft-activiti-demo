package me.kafeitu.demo.activiti.rediscache;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalValue {
	
	@Autowired
	private static JedisConnection redis;
	
	private static int globalValueMinTimeout;
	private static final String GLOBAL_VALUE = "demo_activiti:";
	
	
	@Autowired
	public  void setRedis(JedisConnection redis) {
		GlobalValue.redis = redis;
	}
	
	@Value("${global.value.minTimeout}")
	public void setGlobalValueMinTimeout(int minTimeout){
		GlobalValue.globalValueMinTimeout = minTimeout;
	}

	private static  long getExprieTime(){
		return globalValueMinTimeout * 60;
	}
	
	private static String getKey(String key){
		return GLOBAL_VALUE+key;
	}
	public static void put(String key, Serializable o) {
		redis.setObject(getKey(key), o,getExprieTime());
	}

	public static void put(String key, Serializable o,long expire){
		redis.setObject(getKey(key), o,expire);
	}
	public static Object get(String key) {
		return redis.getObject(getKey(key));
	}

	public static Object remove(String key) {
		return redis.del(getKey(key));
	}

}
