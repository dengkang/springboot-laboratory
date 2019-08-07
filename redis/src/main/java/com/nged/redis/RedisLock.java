package com.nged.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.script.ScriptExecutor;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 锁
 *
 */
@Component
public class RedisLock {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private StringRedisSerializer argsStringSerializer = new StringRedisSerializer();
    private StringRedisSerializer resultStringSerializer = new StringRedisSerializer();

    public boolean lock(String name ,String value){
        //BoundValueOperations<String, String> valueOperations = redisTemplate.boundValueOps(name);
        //判断key是否存在，设置value ，设置有效时长 原子操作
        String luaString ="if redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2]) then " +
                "  return '1' " +
                "else " +
                "  return '0' " +
                "end";
        try{
          // return valueOperations.setIfAbsent(value,5, TimeUnit.SECONDS);
            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(luaString);
            redisScript.setResultType(String.class);
            //锁的有效时间太短，释放锁的时候锁已经不存在了 会出现释放锁异常的情况 px的单位是毫秒
           String result = redisTemplate.execute(redisScript,argsStringSerializer,resultStringSerializer, Collections.singletonList(name),value,"5000");
           System.out.println( result);

            return "1".equals(result);

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean unlock(String name,String value){
        //查询key 的value等于传入的value del key
        String luaString ="if redis.call('get',KEYS[1])==ARGV[1] then return tostring(redis.call('del',KEYS[1])) else return '0' end";

        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(luaString);
        redisScript.setResultType(String.class);//该类型需要与lua脚本中返回值类型一致

        String result =redisTemplate.execute(redisScript,argsStringSerializer,resultStringSerializer, Collections.singletonList(name),value);
        System.out.println(result);
        try{
           return "1".equals(result);
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;

    }
}
