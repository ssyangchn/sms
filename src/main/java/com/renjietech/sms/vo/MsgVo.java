package com.renjietech.sms.vo;

import lombok.Data;

@Data
public class MsgVo {
    private String status;
    private String msg;

    private MsgVo(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static MsgVo success(String msg){
        return new MsgVo("success", msg);
    }
    public static MsgVo info(String msg){
        return new MsgVo("info", msg);
    }
    public static MsgVo warning(String msg){
        return new MsgVo("warning", msg);
    }
    public static MsgVo danger(String msg){
        return new MsgVo("danger", msg);
    }
}
