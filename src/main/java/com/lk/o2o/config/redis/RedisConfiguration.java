package com.lk.o2o.config.redis;

import com.lk.o2o.cache.JedisPoolWriper;
import com.lk.o2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * spring-redis.xml里的配置
 */
@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.maxActive}")
    private int maxActive;
    @Value("${redis.maxIdle}")
    private int maxIdle;
    @Value("${redis.maxWait}")
    private int maxWait;
    @Value("${redis.testOnBorrow}")
    private Boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisPoolWriper;
    @Autowired
    private JedisUtil jedisUtil;

    /*
     * 创建redis连接池到的设置
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool可分配多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxActive);
        //连接池中最多可空闲maxIdel个连接，这里取值为20
        //表示即使没有数据库连接时依然可以保持20空闲的连接
        //而不被清除，随时处于待命状态
        jedisPoolConfig.setMaxIdle(maxIdle);
        //最大等待时间，当没有可用连接时
        //连接池等待被归还的最大时间(以毫秒计算)，超出时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        //在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    /*
     * 创建Redis连接池，并做相关配置
     */
    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper createJedisPoolWriper(){
        //然后创建创建redis实例并返回
        return new JedisPoolWriper(jedisPoolConfig, hostname, port);
    }

    /*
     * 创建Redis工具类,封装好Redis的连接以进行相关的操作
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil(){
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    /*
     * Redis的key操作
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys(){
        return jedisUtil.new Keys();
    }

    /*
     * Redis的String操作
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings(){
        return jedisUtil.new Strings();
    }

    /*
     * Redis的Sets操作
     */
    @Bean(name = "jedisSets")
    public JedisUtil.Sets createJedisSets(){
        return jedisUtil.new Sets();
    }

    /*
     * Redis的Hash操作
     */
    @Bean(name = "jedisHash")
    public JedisUtil.Hash createHash(){
        return jedisUtil.new Hash();
    }

    /*
     * Redis的Lists操作
     */
    @Bean(name = "jedisLists")
    public JedisUtil.Lists createLists(){
        return jedisUtil.new Lists();
    }
}
