package com.nged.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {
    @Autowired
    private StringRedisTemplate redisTemplate;
    String key ="msg1";
    @Test
    public void contextLoads() {



        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(true){
                    String value = "message--"+i++;
                    //②通过发布订阅方式
                    redisTemplate.convertAndSend(key,value);
                    System.out.println("push 完成"+value+"    "+System.currentTimeMillis());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        try {
            Thread.sleep(60*60*2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListPop(){

        //一般不推荐这种利用list 来实现消息队列
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(true){
                    String value = "message--"+i++;
                    //往list插入数据
                     redisTemplate.opsForList().rightPush(key,value);

                    System.out.println("push 完成"+value+"    "+System.currentTimeMillis());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

           new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(true){
                   if(redisTemplate.hasKey(key)&& redisTemplate.opsForList().size(key)>0){
                       String message= (String) redisTemplate.opsForList().leftPop(key);

                       System.out.println("接收到"+message +"    "+System.currentTimeMillis());
                   } else {
                       try {
                           Thread.sleep(500);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }


                }


            }
        }).start();

        try {
            Thread.sleep(60*60*2*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
