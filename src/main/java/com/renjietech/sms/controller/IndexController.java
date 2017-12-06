package com.renjietech.sms.controller;

import com.renjietech.sms.vo.MsgVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

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
    @RequestMapping("sms/send")
    public String sendSms(String templateId, String[] vars, String phones, ModelMap modelMap){
        System.out.println(templateId);
        System.out.println(Arrays.asList(vars));
        System.out.println(phones);

        //modelMap.addAttribute("success","发送成功！");
        modelMap.addAttribute("msg", MsgVo.warning("操作失败"));
        return "index";
    }
}
