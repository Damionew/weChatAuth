package com.test;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;


/**
 * 微信处理工具类
 * @author yinyunqi
 *
 */
@Service
public class WeChatUtil {
	
	private static final Logger log = LoggerFactory.getLogger(WeChatUtil.class);
	
	public static JSONObject weChatTest(String url) {
		CloseableHttpClient httpCilent = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		String result = "";
		JSONObject jsonObject = new JSONObject();
		try {
			HttpResponse httpResponse = httpCilent.execute(httpGet);
			if(httpResponse.getStatusLine().getStatusCode() == 200){
				result = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
				log.info(result);
				jsonObject = JSONObject.parseObject(result);
            }
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
}
