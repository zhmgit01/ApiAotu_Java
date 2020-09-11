package com.api.test.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.test.model.HttpClientResult;
import com.api.test.utils.HttpClientUtil;
import com.api.test.utils.RsaKeyUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class AuthorizationGet {

    private String url;
    private ResourceBundle bundle;
    private HttpClientResult result;

    //用来存储token
    private String token;

    public String getPublicKey()  {
        //获取配置文件中的test.url
        bundle = ResourceBundle.getBundle("baseconfig", Locale.CHINA);
        url = bundle.getString("test.url");
        //获取配置文件中的变量，拼接成为获取token地址
        String uri = bundle.getString("getkey.uri");
        String getKeyUrl = this.url + uri;
        //发送请求
        try {
            result = HttpClientUtil.doGet(getKeyUrl);
        } catch (Exception e) {
            System.out.println("发送的地址：" + getKeyUrl + "错误！" );
        }

        //处理响应结果
        JSONObject resultBody = JSON.parseObject(result.getContent());
        String key = resultBody.getString("data");
        String publicKey = JSONObject.toJSONString(key);
        return publicKey;
    }

    //通过登陆获取token 令牌
    public String getUserToken() throws Exception {
        //获取配置文件中的test.url
        bundle = ResourceBundle.getBundle("baseconfig", Locale.CHINA);
        url = bundle.getString("test.url");
        //获取配置文件中的变量，拼接成为获取token地址
        String uri = bundle.getString("token.uri");
        String testUrl = this.url + uri;

        //请求头信息
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type","application/json;charset=UTF-8");

        //用户名密码
        String username = bundle.getString("test.username");
        String pwd = bundle.getString("test.password");

//        //密码密文传输
//        String pwd = "qwe1234!";
//        String midPwd = null;
//        //生成公钥和私钥
//        String publicKey = RsaKeyUtil.generateBase64PublicKey();
//        String privateKey = RsaKeyUtil.generateBase64PrivateKey();
//        //使用公钥将密码明文加密
//        String newPwd = RsaKeyUtil.decryptBase64(midPwd);

        //使用获取到的公钥对输入的密码进行加密
        String password = RsaKeyUtil.encrypt(pwd,getPublicKey());

        //请求参数信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username",username);
        params.put("password",password);
        params.put("code","999999");

        //发送请求
        result = HttpClientUtil.doPost(testUrl,headers,params);

        //处理返回内容
        JSONObject resultBody = JSON.parseObject(result.getContent());
        JSONObject data = resultBody.getJSONObject("data");
        token = data.getString("token");
        return token;
    }

}
