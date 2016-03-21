package com.ibm.gswt.campus.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 邮件短信接口调用示例
 */
public class EmailSMS
{
	/**
	 * url中{function}/{operation}?部分
	 */
	private static String funAndOperate = "SMS/emailSMS?";

	// 参数详述请参考http://www.qingmayun.com/document.html
	private static String emailTemplateId = "1580019";
	//private static String to = "15998551479";
	//private static String param = "5622,1";

	public static String emailSMS(String tel)
	{
		
		String num = RandomStringUtils.random(4, false, true);
		
		System.out.println("---num---" + num);
		
		System.out.println("---emailSMS---");
		// 生成body
		String body = null;
		if (Config.CONTENT_TYPE.equals("application/json"))
		{
			body = "{\"emailSMS\": {\"appId\":\"" + Config.APP_ID + "\",\"emailTemplateId\":\"" + emailTemplateId
					+ "\",\"to\":\"" + tel + "\",\"param\":\"" + num + ",1" + "\"}}";
		} else if (Config.CONTENT_TYPE.equals("application/xml"))
		{
			body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><emailSMS><appId>" + Config.APP_ID
					+ "</appId><emailTemplateId>" + emailTemplateId + "</emailTemplateId><to>" + tel + "</to><param>"
					+ num + ",1" + "</param></emailSMS>";
		} else
		{
			System.out.println("不支持的Config.CONTENT_TYPE");
			//return;
		}

		// 提交请求
		String result = HttpUtil.post(funAndOperate, body);
		System.out.println("result:" + result);
		
		return num;
	}
}
