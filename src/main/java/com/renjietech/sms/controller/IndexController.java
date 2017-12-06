package com.renjietech.sms.controller;

import com.renjietech.sms.vo.MsgVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class IndexController {
    @Value("${sendUrl}")
    private String sendUrl;
    @Value("${appKey}")
    private String appKey;
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @ResponseBody
    @RequestMapping("sms/doSend")
    public ResponseEntity sendSms(String templateId, String[] vars, String phones){
        System.out.println(templateId);
        System.out.println(Arrays.asList(vars));
        System.out.println(phones);
        Assert.hasText(templateId,"请选择模板！");
        Assert.hasText(phones,"请输入手机号码！");
        String[] phone = phones.split("\r\n");
        Set<String> set = new HashSet();
        set.addAll(Arrays.asList(phone));

        SmsRequest req = new SmsRequest();
        List<Tel> tels = new ArrayList<>();
        req.setTel(tels);
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            tels.add(new Tel(iterator.next()));
            if (tels.size() / 200 ==0) {
                doSend();
                tels.clear();
            }
        }
        if (tels.size()>0) {
            doSend();
        }

        return ResponseEntity.ok(new MsgVo(true,"发送成功，本次发送100条"));
    }

    @RequestMapping("sms/send")
    public String send(String templateId, String[] vars, String phones, ModelMap modelMap){
        System.out.println(templateId);
        System.out.println(Arrays.asList(vars));
        System.out.println(phones);
        Set set = new HashSet();
        set.addAll(Arrays.asList(phones));


        return "index";
    }

    @ResponseBody
    @RequestMapping("/test")
    public ResponseEntity test(){
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("name", "ssyang");
        return ResponseEntity.ok(objectObjectHashMap);
    }

    public void doSend(){

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MsgVo> handle(Exception e){
        return ResponseEntity.ok(new MsgVo(false,e.getMessage()));
    }

    private static class Tel{
        String nationcode="86";
        String mobile;

        public Tel(String mobile) {
            this.mobile = mobile;
        }
    }
    @Data
    private static class SmsRequest{
        List<Tel> tel;
        String sign;
        String tpl_id;
        List<String> params;
        String sig;
        long time;
        String extend = "";
        String ext="";
    }
}
