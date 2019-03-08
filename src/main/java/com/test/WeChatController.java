package com.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
/**
 * 处理微信请求
 * @author yinyunqi
 *
 */
@Controller
public class WeChatController {
	private static final Logger log = LoggerFactory.getLogger(WeChatController.class);

	// 测试号平台 https://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo?action=showinfo&t=sandbox/index
	// 公众号的唯一标识【必需】
	public static final String APPID = "wx3cc7cac0b41e9111";
	// 授权后重定向的回调链接地址， 请使用 urlEncode 对链接进行处理【必需】
	public static final String REDIRECT_URI = "https://damionew.top/wx/weChat/callBack";
	// 返回类型，请填写code【必需】
	public static final String RESPONSE_TYPE = "code";
	// 应用授权作用域【必需】
	// snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid）
	// snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
	public static final String SCOPE = "snsapi_userinfo";
	// 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节【非必需】
	public static final String STATE = "123";
	// 无论直接打开还是做页面302重定向时候，必须带此参数【必需】
	public static final String wechat_redirect = "";
	public static final String APPSECRET = "b51067db03de9fa9d785e99db70b937e"; 
	
	/**
	 * 初始页面
	 * @return
	 */
	@RequestMapping("/weChat")
	public String weChatTest() {
		log.info("weChat");
		return "weChat";
	}
	
	/**
	 * 点击页面超链请求此处
	 * @param req
	 * @param res
	 */
	@ResponseBody
	@RequestMapping("/weChat/auth")
	public void weChatAuth(HttpServletRequest req,HttpServletResponse res) {
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APPID
				+ "&redirect_uri="+REDIRECT_URI
				+ "&response_type="+RESPONSE_TYPE
				+ "&scope="+SCOPE
				+ "&state="+STATE
				+ "#wechat_redirect";
		log.info("url"+url);
		try {
			res.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上方 REDIRECT_URI 变量中带有的回调请求，即用户点击确定授权后所做的操作
	 * @param req
	 * @param res
	 */
	@ResponseBody
	@RequestMapping("weChat/callBack")
	public void weChatCallBack(HttpServletRequest req,HttpServletResponse res){
		String code = req.getParameter("code");
		log.info("code为："+code);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+APPID
				+ "&secret="+APPSECRET
				+ "&code="+code
				+ "&grant_type=authorization_code";
		JSONObject jsonObject = WeChatUtil.weChatTest(url);
		String openId = jsonObject.getString("openid");
		String access_token = jsonObject.getString("access_token");
		log.info("openid为"+openId);
		// 拉取用户信息
		String url2 = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token
				+ "&openid="+openId
				+ "&lang=zh_CN";
		JSONObject userInfo = WeChatUtil.weChatTest(url2);
		log.info("唯一标识为：" + userInfo.getString("openid"));
		log.info("昵称为：" + userInfo.getString("nickname"));
		log.info("性别为：" + userInfo.getString("sex"));
		log.info("省为：" + userInfo.getString("province"));
		log.info("市为：" + userInfo.getString("city"));
		log.info("头像地址为：" + userInfo.getString("headimgurl"));
	}
}
