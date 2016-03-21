package com.ibm.gswt.campus.util;

/**
 * 系统常量
 */
public class Config
{
	/**
	 * url前半部分
	 */
	public static final String BASE_URL = "https://api.qingmayun.com/20141029/accounts/";

	/**
	 * url中的accountSid。如果接口验证级别是主账户则传网站“个人中心”页面的“账户ID”，
	 * 如果验证级别是client则传clientNumber
	 * 。clientNumber由开发者调用“创建client接口”时平台生成并返回给开发者，或注册时平台自动生成的demo应用的clientId
	 */
	public static final String ACCOUNT_SID = "add7b485740a4a7d9ce41baf1ce0bfc8";

	/**
	 * 生成sig参数用到的key。 如果接口验证级别是主账户则传网站“个人中心”页面的“账户密钥”，
	 * 如果验证级别是client则传clientPwd。clientPwd由开发者调用
	 * “创建client接口”时平台生成并返回给开发者，或注册时平台自动生成的demo应用的clientKey
	 */
	public static final String KEY = "456a5a6626434cb281ed4e5631f829ce";
	
	/**
	 * 应用id
	 */
	public static final String APP_ID = "0efc8f60ae484f02874bc06701cc8f05";

	/**
	 * 请求的内容类型，可以是application/json或application/xml
	 */
	public static final String CONTENT_TYPE = "application/json";

	/**
	 * 期望服务器响应的内容类型，可以是application/json或application/xml
	 */
	public static final String ACCEPT = "application/json";
}