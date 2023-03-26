package club.beenest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class RedisDemo {
    public static void main(String[] args) {
        // 基本使用
//        baseUse();
        // 连接池的使用
        poolUser();
    }

    /**
     * 连接池的使用
     */
    private static void poolUser() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大连接
        jedisPoolConfig.setMaxTotal(10);
        // 设置最大空闲连接
        jedisPoolConfig.setMaxIdle(10);
        // 设置最小空闲连接
        jedisPoolConfig.setMinIdle(0);
        // 设置最长等待时间
        Duration duration = Duration.ofMillis(200);
        jedisPoolConfig.setMaxWait(duration);

        // 获取连接池
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,
                "localhost.beenest.club",
                6379, 1000, "123456");
        // 使用连接池获取连接
        Jedis jedis = jedisPool.getResource();
        String res = jedis.get("test11");
        System.out.println(res);
    }

    private static void baseUse() {
        // 建立连接
        Jedis jedis = new Jedis("localhost.beenest.club", 6379);
        // 设置密码
        jedis.auth("123456");
        // 选择对应库
        jedis.select(0);
        // 获取对应数据
        String res = jedis.get("test11");
        System.out.println(res);
    }
}
