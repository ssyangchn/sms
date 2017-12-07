package com.renjietech.sms.util;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestUtil {
    public static String postJson(String url,Object body){
        RestTemplate restTemplate=new RestTemplate();
        /* 注意：必须 http、https……开头，不然报错，浏览器地址栏不加 http 之类不出错是因为浏览器自动帮你补全了 */
        HttpHeaders headers = new HttpHeaders();
        /* 这个对象有add()方法，可往请求头存入信息 */
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        /* 解决中文乱码的关键 , 还有更深层次的问题 关系到 StringHttpMessageConverter，先占位，以后补全*/
        HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(body), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        if (responseEntity.getStatusCode()== HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException(responseEntity.getStatusCode().getReasonPhrase());
        }
    }
}
