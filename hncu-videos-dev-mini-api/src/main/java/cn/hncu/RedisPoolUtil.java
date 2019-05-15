package cn.hncu;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolUtil {
	 private static JedisPool jedisPool = null;
	 
	    
	    private static ThreadLocal<Jedis> local=new ThreadLocal<Jedis>();
	    
	   
	    private RedisPoolUtil() {
	    }
	 
	    /**
	     * 初始化Redis连接池
	     */
	    public static void initialPool() {
	        try {
	           
	           
	            JedisPoolConfig config = new JedisPoolConfig();
	           
	            config.setMaxTotal(100);
	            config.setMaxIdle(50);
	            config.setMaxWaitMillis(1500);
	            config.setTestOnBorrow(true);
	            config.setTestOnReturn(true);
	           
	            jedisPool = new JedisPool(config, "129.204.220.11",6379,3000,"hncu");
	            System.out.println("线程池被成功初始化");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * 获得连接
	     * @return Jedis
	     */
	    public static Jedis getConn() {
	        //Redis对象
	        Jedis jedis =local.get();
	        if(jedis==null){
	            if (jedisPool == null) {    
	                initialPool();  
	            }
	            jedis = jedisPool.getResource();
	            local.set(jedis);
	        }
	        return jedis;  
	    }
	    
	    //新版本用close归还连接
	    public static void closeConn(){
	        //从本地线程中获取
	        Jedis jedis =local.get();
	        if(jedis!=null){
	            jedis.close();
	        }
	        local.set(null);
	    }
	    
	    //关闭池
	    public static void closePool(){
	        if(jedisPool!=null){
	            jedisPool.close();
	        }
	    }
	    
       public static void main(String[] args) {
    	   RedisPoolUtil.initialPool();
	}
	    
	    
	}
 
