package com.api.test.cases;

import com.api.test.config.AuthorizationGet;
import com.api.test.model.HttpClientResult;
import com.api.test.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class CentralBankReportSubmittedCase {

    //获取token
    private static String token;

    //请求头信息
    private static Map<String, String> headers = new HashMap<String, String>();

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(CentralBankReportSubmittedCase.class);

    //读取配置文件
    private ResourceBundle bundle;

    //测试主地址
    private static String testUrl;

    @BeforeTest
    public void beforeTest() throws Exception {
        //获取token
        token = new AuthorizationGet().getUserToken();
        //请求头信息
        headers.put("Content-Type","application/json");
        headers.put("Authorization",token);
        //访问
        bundle = ResourceBundle.getBundle("baseconfig", Locale.CHINA);
        testUrl = bundle.getString("test.url");
        String url = bundle.getString("test.url");
    }

    @Test
    public void getReportListTest() throws IOException {

        //获得配置文件中的请求内容
        String getReportListUrl = testUrl + bundle.getString("getReportList.uri");
        //请求参数
        Map<String,Object> params = new HashMap<String, Object>();
        //请求参数
        params.put("type","generate");
        params.put("supplyType","0");
        //获取响应结果
        HttpClientResult reslut = HttpClientUtil.doPost(getReportListUrl,headers,params);

    }
}
