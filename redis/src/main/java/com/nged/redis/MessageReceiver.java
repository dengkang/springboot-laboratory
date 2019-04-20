package com.nged.redis;

import org.springframework.stereotype.Component;

/**
 * @author: Administrator
 * @Date: 2019/4/17 20:28
 * @Description:
 */
@Component
public class MessageReceiver {

    public void receiveMsg(String message){
        System.out.println(message);
    }

}
