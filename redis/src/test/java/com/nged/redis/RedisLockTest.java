package com.nged.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockTest {

    @Autowired
    private volatile RedisLock redisLock;

    public void doWork(String value){
        String name="redisLock";


        try {
            while (!redisLock.lock(name,value)){
                System.out.println(Thread.currentThread().getName()+"加锁失败");
                Thread.sleep(1000);
            }
            System.out.println(Thread.currentThread().getName()+"加锁成功");
            // do something
            Thread.sleep(3000);
            if(redisLock.unlock(name,value)){
                System.out.println(Thread.currentThread().getName()+"释放锁");
            }else{
                System.out.println(Thread.currentThread().getName()+"释放锁异常");
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void lock() {
       /* String name="redisLock";
        String value="redisLock";
        redisLock.lock(name,value);
        redisLock.unlock(name,value);*/
        for(int i=0;i<2;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doWork(Thread.currentThread().getName());
                }
            }).start();
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}