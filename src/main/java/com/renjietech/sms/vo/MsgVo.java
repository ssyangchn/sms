package com.renjietech.sms.vo;

import lombok.Data;

@Data
public class MsgVo {
    private boolean flag;
    private String msg;

    public MsgVo(boolean flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }
}
