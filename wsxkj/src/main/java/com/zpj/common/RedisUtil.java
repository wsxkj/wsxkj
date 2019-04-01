package com.zpj.common;/*
 * @Author zhupeijun
 * @Description //TODO $end$
 * @Date $time$ $date$
 * @Param $param$
 * @return $return$
 **/

import com.zpj.sys.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisUtil {
    //服务器IP地址
    private static String ADDR = "127.0.0.1";
//端口
    private static int PORT = 6379;
//密码
    private static String AUTH = "123456";
//连接实例的最大连接数
    private static int MAX_ACTIVE = 1024;

//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException
    private static int MAX_WAIT = 10000;

//连接超时的时间　　
    private static int TIMEOUT = 10000;

// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */

    static {

        try {

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * 获取Jedis实例
     */

    public synchronized static Jedis getJedis() {

        try {

            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
//                resource.select(1);
                return resource;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /***
     *
     * 释放资源
     */

    public static void returnResource(final Jedis jedis) {
        if(jedis != null) {
            jedis.close();
           // jedisPool.returnResource(jedis);
        }

    }

    public static void main(String[] args) {
        Jedis jedis=RedisUtil.getJedis();
        Jedis jedis1=RedisUtil.getJedis();
        Jedis jedis2=RedisUtil.getJedis();
        Jedis jedis3=RedisUtil.getJedis();
        Jedis jedis4=RedisUtil.getJedis();
        System.out.println("");RedisUtil.getJedis();
        jedis.set("zpj12","hahha");
        jedis4.lpush("","");
        System.out.println(jedis.get("zpj12"));
        System.out.println(jedis);
        RedisUtil.returnResource(jedis);
        System.out.println(jedis.get("zpj2"));
        System.out.println(jedis);

    }

    /**
     * 简单添加
     */
    @Test
    public void test1() {
        String name = "name";
        String value = "qq";
        Jedis jedis=RedisUtil.getJedis();
        jedis.set(name, value);
        System.out.println("追加前：" + jedis.get(name)); // 追加前：qq

        // 在原有值得基础上添加,如若之前没有该key，则导入该key
        jedis.append(name, "ww");
        System.out.println("追加后：" + jedis.get(name)); // 追加后：qqww

        jedis.append("id", "ee");
        System.out.println("没此key：" + jedis.get(name));
        System.out.println("get此key：" + jedis.get("id"));

    }

    /**
     * mset 是设置多个key-value值 参数（key1,value1,key2,value2,...,keyn,valuen） mget
     * 是获取多个key所对应的value值 参数（key1,key2,key3,...,keyn） 返回的是个list
     */
    @Test
    public void test2() {
        Jedis jedis=RedisUtil.getJedis();
        jedis.mset("name1", "aa", "name2", "bb", "name3", "cc");
        System.out.println(jedis.mget("name1", "name2", "name3"));
    }

    /**
     * map
     */
    @Test
    public void test3() {
        Jedis jedis=RedisUtil.getJedis();
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "fujianchao");
        map.put("password", "123");
        map.put("age", "12");
        // 存入一个map
        jedis.hmset("user", map);

        // map key的个数
        System.out.println("map的key的个数" + jedis.hlen("user"));

        // map key
        System.out.println("map的key" + jedis.hkeys("user"));

        // map value
        System.out.println("map的value" + jedis.hvals("user"));

        // (String key, String... fields)返回值是一个list
        List<String> list = jedis.hmget("user", "age", "name");
        System.out.println("redis中key的各个 fields值："
                + jedis.hmget("user", "age", "name") + list.size());

        // 删除map中的某一个键 的值 password
        // 当然 (key, fields) 也可以是多个fields
        jedis.hdel("user", "age");

        System.out.println("删除后map的key" + jedis.hkeys("user"));

    }

    /**
     * list
     */
    @Test
    public void test4() {
        Jedis jedis=RedisUtil.getJedis();
        jedis.lpush("list", "aa");
        jedis.lpush("list", "bb");
        jedis.lpush("list", "cc");
        System.out.println(jedis.lrange("list", 0, -1));
        System.out.println(jedis.lrange("list", 0, 1));
        System.out.println(jedis.lpop("list")); // 栈顶
        jedis.del("list");
    }

    /**
     * 自定义对象 User为例 id name
     *
     * RedisTemplate 中有 序列化和反序列化
     * 如：template.getStringSerializer().serialize("name")
     */
    @Test
    public void test5() {
        Jedis jedis=RedisUtil.getJedis();
        User user = new User();
        user.setId("123");
        user.setName("fighter");

        // 存入一个 user对象
        jedis.set("user".getBytes(), SerializationUtil.serialize(user));

        // 获取
        byte[] bs = jedis.get("user".getBytes());
        User desUser = (User) SerializationUtil.deserialize(bs);
        System.out.println(desUser.getId() + ":" + desUser.getName());

    }
}
