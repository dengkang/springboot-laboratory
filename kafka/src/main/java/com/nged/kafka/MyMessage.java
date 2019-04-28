package com.nged.kafka;

import java.util.Date;

/**
 * @author: dengk
 * @Date: 2019/4/26 15:33
 * @Description:
 */
public class MyMessage {
    private Long id;

    private String msg;

    private Date sendTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
