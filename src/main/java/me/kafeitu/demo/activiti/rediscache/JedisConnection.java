package me.kafeitu.demo.activiti.rediscache;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

public class JedisConnection {
		public  RedisTemplate<Serializable, Serializable> template;
		
		private static int selectDb = 0;//当前选择db
		
		public static long ONE_MINUTES = 60;//保存一分钟
		
		public static long ONE_WEEKS = 60*60*24*7;//保存一周
		
		public static long ONE_MONTH = 60*60*24*30;//保存30天
		
		public static long ONE_DAY = 60*60*24;//保存一天
		
		//优先级
		public static double SOCRE_1 = 1;
		public static double SOCRE_2 = 2;
		public static double SOCRE_3 = 3;
		public static double SOCRE_4 = 4;
		public static double SOCRE_5 = 5;
		public static double SOCRE_6 = 6;
		public static double SOCRE_7 = 7;
		public static double SOCRE_8 = 8;
		public static double SOCRE_9 = 9;
		public static double SOCRE_10 = 10;
		
		/**
		 * 存储string
		 * @param key
		 * @param value
		 */
		public void setString(final String key, final String value) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection){
	            	connection.select(selectDb);
	            	connection.set(key.getBytes(), value.getBytes());
	                return true;  
	            }  
	        });  
	    }
		/**
		 * 有时效存储
		 * @param key
		 * @param value
		 * @param time
		 */
		public void setString(final String key, final String value, final long time) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection){
	            	connection.select(selectDb);
	            	
	            	connection.setEx(key.getBytes(), time, value.getBytes());
	                return true;  
	            }  
	        });  
	    }
		/**
		 * 将序列化对象存储
		 * @param key
		 * @param value
		 */
		public void setObject(final String key, final Serializable value) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection){
	            	connection.select(selectDb);
	            	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	            	byte[] serializeValue = serializer.serialize(value);
	            	connection.set(key.getBytes(), serializeValue);
	                return true;  
	            }  
	        });  
	    }
		/**
		 * 有时效存储
		 * @param key
		 * @param value
		 * @param time
		 */
		public void setObject(final String key, final Serializable value, final long time) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection){
	            	connection.select(selectDb);
	            	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	            	byte[] serializeValue = serializer.serialize(value);
	            	connection.setEx(key.getBytes(), time, serializeValue);
	                return true;  
	            }  
	        });  
	    }
//		public void setToken(final String key,)
		/**
		 * 添加一个元素到list中 String
		 * @param key
		 * @param value
		 */
		public  void lpush(final String key, final String value) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection) {
	            	connection.select(selectDb);
	            	connection.lPush(key.getBytes(),value.getBytes());
	                return true;  
	            }  
	        });  
	    }
		
		/**
		 * 对象插入  从头插入 Object
		 * @param key
		 * @param value
		 */
		public  void lpush(final String key, final Object value) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection) {
	            	connection.select(selectDb);
	            	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	            	connection.lPush(key.getBytes(),serializer.serialize(value));
	                return true;  
	            }  
	        });  
	    }
		/**
		 * 对象插入  从尾插入
		 * @param key
		 * @param value
		 */
		public  void rpush(final String key, final Object value) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
//	            	System.out.println(value);
	            	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	            	connection.lPush(key.getBytes(),serializer.serialize(value));
	                return true;  
	            }  
	        });  
	    }
		/**
		 * 对象插入  从尾插入
		 * @param key
		 * @param value
		 */
		public  void rpush(final String key, final String value) {  
	        template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	            	connection.rPush(key.getBytes(),value.getBytes());
	                return true;  
	            }  
	        });  
	    }
		/**
		 * rpop   删除list中的最后一个元素
		 * @param key
		 */
		public void rpop(final String key){
			template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	connection.rPop(key.getBytes());
	            	return true;
	            }  
	        });
		}
		
		/**
		 * lpop   删除list中的最一个元素 并返回
		 * @param key
		 */
		public  String lpop(final String key){
			return template.execute(new RedisCallback<String>() {  
	            @Override  
	            public String doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	 byte[] bytes = connection.lPop(key.getBytes());
	                if(bytes!=null)
	                	return new String(bytes);
	                else return null;
	            }  
	        });
		}
		/**
		 * 将list放入redis中，放入过程清除原有list   一直从尾插入
		 * @param key
		 * @param list
		 */
		public void pushList(final String key, final ArrayList<Serializable> list){
			template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	connection.del(key.getBytes());
	            	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	            	for(Serializable o: list){
	                	byte[] serializeValue = serializer.serialize(o);
	            		connection.rPush(key.getBytes(),serializeValue);
	            	}
	                return true;  
	            }  
	        });  
		}
		
		public void pushStringList(final String key, final ArrayList<String> list){
			template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	connection.del(key.getBytes());
	            	for(String s: list){
	            		connection.lPush(key.getBytes(),s.getBytes());
	            	}
	                return true;  
	            }  
	        });  
		}

		/**
		 * 获取list长度
		 * @param key
		 * @return
		 */
		public  Long getArrayListSize(final String key){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	return connection.lLen(key.getBytes());
	            }  
	        }); 
		}
		
		/**
		 * 获取list数据
		 * @param key
		 * @param start
		 * @param size
		 * @return
		 */
		public ArrayList<Serializable> getArrayList(final String key, final int start,final int size){
			return template.execute(new RedisCallback<ArrayList<Serializable>>() {  
	            @Override  
	            public ArrayList<Serializable> doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	List<byte[]> list = connection.lRange(key.getBytes(), start, size);
	            	if(CollectionUtils.isNotEmpty(list)){
	            		ArrayList<Serializable> resultList = new ArrayList<Serializable>();
	                	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	                	for(byte[] b:list){
	                		resultList.add((Serializable)serializer.deserialize(b));
	                	}
	                	
	                	return resultList;
	            	}
	            	return null;
	            }  
	        }); 
		}
		
		public ArrayList<String> getStringList(final String key, final int start,final int size){
			return template.execute(new RedisCallback<ArrayList<String>>() {  
	            @Override  
	            public ArrayList<String> doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	List<byte[]> list = connection.lRange(key.getBytes(), start, size);
	            	if(CollectionUtils.isNotEmpty(list)){
	            		ArrayList<String> resultList = new ArrayList<String>();
	                	for(byte[] b:list){
	                		resultList.add(new String(b));
	                	}
	                	return resultList;
	            	}
	            	return null;
	            }  
	        }); 
		}
		
		/**
		 * 获取string
		 * @param key
		 * @return
		 */
		public String getString(final String key){
			return template.execute(new RedisCallback<String>() {  
	            @Override  
	            public String doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	                byte[] bytes = connection.get(key.getBytes());
//	                connection.getva
	                if(bytes!=null)
						try {
							return new String(bytes,"utf8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return null;
						}
					else return null;
	            }  
	        }); 
		}
		
		/**
		 * 获取序列化对象
		 * @param key
		 * @return
		 */
		public Serializable getObject(final String key){
			return template.execute(new RedisCallback<Serializable>() {  
	            @Override  
	            public Serializable doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	                byte[] bytes = connection.get(key.getBytes());
	                JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	                return (Serializable) serializer.deserialize(bytes);  
	            }  
	        }); 
		}
		
		/**
		 * 获取数据库大小
		 * @return
		 */
		public Long getDbSize(){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	                return connection.dbSize();  
	            }  
	        }); 
		}
		
		/**
		 * 删除单条数据
		 * @param key
		 * @return
		 */
		public Long del(final String key){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	return connection.del(key.getBytes());
	            }  
	        });
		}
		
		/**
		 * 给key设置时效
		 * @param key
		 * @param time
		 * @return
		 */
		public Boolean expire(final String key, final long time){
			return template.execute(new RedisCallback<Boolean>() {  
	            @Override  
	            public Boolean doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	return connection.expire(key.getBytes(), time);
	            }  
	        });
		}
		/**
		 * 获取剩余时长
		 * @param key
		 * @return
		 */
		public Long ttl(final String key){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	return connection.ttl(key.getBytes());
	            }  
	        });
		}
		/**
		 * 往sortedSet中zadd数据   
		 * @param key
		 * @param score
		 * @param value
		 */
		public void zadd(final String key , final double score, final String value){
			template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	connection.zAdd(key.getBytes(),	score, value.getBytes());
	            	return true;  
	            }  
	        });  
		}
		/**
		 * 删除sortSet中的某一个元素
		 * @param key
		 * @param score
		 * @param value
		 */
		public Boolean zrem(final String key , final String value){
			return template.execute(new RedisCallback<Boolean>() {  
	            @Override  
	            public Boolean doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	return connection.zRem(key.getBytes(),	value.getBytes());
	            }  
	        });  
		}
		/**
		 * 获取排行榜
		 * @param key
		 * @param start
		 * @param size
		 * @return
		 */
		public ArrayList<String> zrevrange(final String key, final int start, final int size){
			return template.execute(new RedisCallback<ArrayList<String>>() {  
	            @Override  
	            public ArrayList<String> doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	//Set<byte[]> set = connection.zRevRange(key.getBytes(), start, size);
	            	Set<Tuple> set = connection.zRevRangeWithScores(key.getBytes(), start, size);
	            	if(CollectionUtils.isNotEmpty(set)){
	            		ArrayList<String> resultList = new ArrayList<String>();
	            		for(Tuple b : set){
	            			resultList.add(new String(b.getValue()));
	                	}
	            		return resultList;
	            	}
	            	return null;
	            }  
	        });  
		}
		/**
		 * zrevrank 返回排名 
		 * 如果返回0  没有排名
		 * @return
		 */
		public Long zrevrank(final String key, final String value){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	//验证是否有排名
	            	if(null != connection.zScore(key.getBytes(),  value.getBytes())){
	            		//有排名则返回排名
	            		return connection.zRevRank(key.getBytes(),value.getBytes()) + 1;
	            	}else{
	            		connection.zAdd(key.getBytes(),0,value.getBytes());
	            		return connection.zCard(key.getBytes());
	            	}
	            }  
	        });
		}
		/**
		 * zscore 返回score
		 * @return
		 */
		public Double zscore(final String key, final String value){
			return template.execute(new RedisCallback<Double>() {  
	            @Override  
	            public Double doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	return connection.zScore(key.getBytes(),value.getBytes());
	            }  
	        });
		}
		/**
		 * zcard 返回sortedSet的大小
		 * @return
		 */
		public Long zcard(final String key){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection)  { 
	            	connection.select(selectDb);
	            	return connection.zCard(key.getBytes());
	            }  
	        });
		}
		/**
		 * 往hashs中添值  map
		 * @param key
		 * @param field
		 * @param list
		 */
		public void hset(final String key, final String field,final ArrayList<Serializable> list){
			template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	            	connection.hSet(key.getBytes(),	field.getBytes(), serializer.serialize(list));
	            	return true;  
	            }  
	        });  
		}
		/**
		 * 获取hashs中的field   map
		 * @param key
		 * @param field
		 * @return
		 */
		public Serializable hget(final String key, final String field){
			return template.execute(new RedisCallback<Serializable>() {  
	            @Override  
	            public Serializable doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	                JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
	                byte[] bytes = connection.hGet(key.getBytes(), field.getBytes());
	               return  (Serializable)serializer.deserialize(bytes);
	            }  
	        });  
		}
		/**
		 * 获取map中的某一String 元素
		 * @param key
		 * @param field
		 * @return
		 */
		public String hgetString(final String key, final String field){
			return template.execute(new RedisCallback<String>() {  
	            @Override  
	            public String doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	byte[] bytes  = connection.hGet(key.getBytes(), field.getBytes());
	                if(bytes!=null)
	                	return new String(bytes);
	                else return null;
	            }  
	        });  
		}
		/**
		 * hset  String
		 * @param key
		 * @param field
		 * @param value
		 */
		public void hsetString(final String key, final String field, final String value){
			template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	connection.hSet(key.getBytes(), field.getBytes(), value.getBytes());
	            	return true;
	            }  
	        });  
		}
		/**
		 * 删除map中的某一元素
		 * @param key
		 * @param field
		 */
		public void hDel(final String key, final String field){
			template.execute(new RedisCallback<Object>() {  
	            @Override  
	            public Object doInRedis(RedisConnection connection) throws DataAccessException {
	            	connection.select(selectDb);
	            	connection.hDel(key.getBytes(),	field.getBytes());
	            	return true;
	            }  
	        });  
		}
		/**
		 * 获取map长度
		 * @param key
		 * @return
		 */
		public Long hLen(final String key){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection) throws DataAccessException {
	            	connection.select(selectDb);
	            	 return connection.hLen(key.getBytes());
	            }  
	        });  
		}
		public  Long incre(final String key){
			return template.execute(new RedisCallback<Long>() {  
	            @Override  
	            public Long doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	Long id = connection.incr(key.getBytes());
	            	return id;
	            }  
	        });  
		}
		
		/**
		 * 往set里添加元素，可用于排重
		 * @param key
		 * @param field
		 * @return true 插入成功，false失败
		 */
		public Boolean sadd(final String key,final String field){
			return template.execute(new RedisCallback<Boolean>() {  
	            @Override  
	            public Boolean doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	return connection.sAdd(key.getBytes(), field.getBytes());
	            }  
	        });  
		}
		
		/**
		 * 判断元素是否存在set里
		 * @param key
		 * @param field
		 * @return 
		 */
		public Boolean sIsMember(final String key,final String field){
			return template.execute(new RedisCallback<Boolean>() {  
	            @Override  
	            public Boolean doInRedis(RedisConnection connection)  {
	            	connection.select(selectDb);
	            	return connection.sIsMember(key.getBytes(), field.getBytes());
	            }  
	        });  
		}
		
		/**
		 * 监听key
		 * @param key
		 */
		public void watch(final String key){
			template.watch(key);
		}
		
		/**
		 * 加锁
		 */
		public void multi(){
			template.multi();
		}
		
		/**
		 * 解锁
		 */
		public void exec(){
			template.exec();
		}
		
		/**
		 * 回滚
		 * @return
		 */
		public void discard(){
			template.discard();
		}
		
		public int getSelectDb() {
			return selectDb;
		}
		public void setSelectDb(int selectDb) {
			this.selectDb = 0;
		}
		public  RedisTemplate<Serializable, Serializable> getTemplate() {
			return template;
		}
		public  void setTemplate(
				RedisTemplate<Serializable, Serializable> template) {
			this.template = template;
		}
		
		

}
