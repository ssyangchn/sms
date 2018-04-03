package com.renjietech.sms.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renjietech.sms.util.Encrypt;
import com.renjietech.sms.util.RestUtil;
import com.renjietech.sms.vo.MsgVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.ArrayUtils;

import java.text.MessageFormat;
import java.util.*;

@PropertySource(value = {"classpath:sms.properties"}, encoding = "UTF-8")
@Controller
public class IndexController {
    @Value("${appKey}")
    private String appKey;
    @Value("${appId}")
    private String appId;
    @Value("${smsTpl}")
    private String smsTpl;
    private List<Map<String, String>> smstpl;
    @RequestMapping("/")
    public String index(ModelMap modelMap) {
        if (smstpl == null) {
            smstpl = new ArrayList<>();
            HashMap<String,String> map;
            String[] split = smsTpl.split(",");
            for (String s : split) {
                String[] op = s.split("-");
                map = new HashMap<>();
                map.put("id", op[0]);
                map.put("text", op[1]);
                smstpl.add(map);
            }
        }
        modelMap.addAttribute("smstpl", smstpl);

        return "index";
    }

    @ResponseBody
    @RequestMapping("sms/send")
    public ResponseEntity send(Long templateId, String[] vars, String phones) {
        Assert.notNull(templateId, "请选择模板！");
        Assert.hasText(phones, "请输入手机号码！");
        String[] phone = phones.split("\r\n");
        Set<String> set = new HashSet<>(Arrays.asList(phone));

        SmsRequest req = new SmsRequest();
        List<Tel> tels = new ArrayList<>();
        req.setTel(tels);
        req.setTpl_id(templateId);
        req.setParams(Arrays.asList(vars));
        Iterator<String> iterator = set.iterator();
        StringBuilder errorMsg = new StringBuilder();
        int num = 0;
        String var0 = "";
        if (vars.length > 0) {
            var0 = vars[0];
        }
        while (iterator.hasNext()) {
            String mobile = iterator.next().trim();
            String[] split = mobile.split("\t");
            req.getParams().set(0,var0+MessageFormat.format("{0}号",split[0]));
            tels.add(new Tel(split[1]));
                try {
                    num += doSend(req);
                } catch (Exception e) {
                    errorMsg.append(e.getMessage());
                }
                tels.clear();
        }
        if (errorMsg.length() > 0) {
            if (num > 0) {
                errorMsg.insert(0, "本次发送成功：" + num + "条;错误信息：");
            }
            return ResponseEntity.ok(new MsgVo(false, errorMsg.toString()));
        }
        return ResponseEntity.ok(new MsgVo(true, "发送成功，本次发送" + num + "条"));
    }

    private int doSend(SmsRequest req) {
        long time = System.currentTimeMillis() / 1000;
        req.setTime(time);
        StringBuilder sb = new StringBuilder("appkey=");
        sb.append(appKey);
        sb.append("&random=");
        String random = "7226249334";
        sb.append(random);
        sb.append("&time=");
        sb.append(time);
        sb.append("&mobile=");
        for (Tel tel : req.getTel()) {
            sb.append(tel.mobile);
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        req.setSig(Encrypt.SHA256(sb.toString()));

        String subfix = "sdkappid=" + appId + "&random=" + random;

        String url = "https://yun.tim.qq.com/v5/tlssmssvr/sendmultisms2?" + subfix;
        String s = RestUtil.postJson(url, req);
        JSONObject jsonObject = JSONObject.parseObject(s);
        if (jsonObject.getIntValue("result") == 0) {
            JSONArray detail = jsonObject.getJSONArray("detail");
            int num = 0;
            for (Object o : detail) {
                JSONObject jo = ((JSONObject) o);
                if (jo.getIntValue("result") == 0) {
                    num++;
                }
            }
            return num;
        } else {
            throw new RuntimeException(jsonObject.getString("errmsg"));
        }
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<MsgVo> handle(Exception e) {
        return ResponseEntity.ok(new MsgVo(false, e.getMessage()));
    }

    @Data
    private static class Tel {
        String nationcode = "86";
        String mobile;

        Tel(String mobile) {
            this.mobile = mobile;
        }
    }

    @Data
    private static class SmsRequest {
        List<Tel> tel;
        String sign;
        Long tpl_id;
        List<String> params;
        String sig;
        long time;
        String extend = "";
        String ext = "";
    }
}
