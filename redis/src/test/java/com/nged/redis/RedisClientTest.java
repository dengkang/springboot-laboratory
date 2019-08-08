package com.nged.redis;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisClientTest {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    private  static  final String GOOGS_NUM = "googsNum";
    @Before
    public void init(){
        //设置商品数量 及有效期
       redisTemplate.opsForValue().set(GOOGS_NUM,"10",160, TimeUnit.SECONDS);
    }


    @Test
    public void test(){


        List<Object> result =redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                //添加watch
                operations.watch(GOOGS_NUM);
                //开启事务
                operations.multi();
                operations.opsForValue().decrement(GOOGS_NUM);
                operations.opsForValue().set("userId",System.currentTimeMillis()+"");
                //提交事务
                return operations.exec();
            }
        });
        //watch + multi实现事务 这种虽然执行上保证事物 但是还需要对返回的命令结果进行判断才能知道是否执行成功
        //还是lua脚本比较好用
        if(null!=result){
            for(Object o:result){
                System.out.println(o);
            }
        }


    }

}
