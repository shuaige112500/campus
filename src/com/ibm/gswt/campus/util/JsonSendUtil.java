package com.ibm.gswt.campus.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import com.ibm.jjson.JsonMap;

public class JsonSendUtil {
	public static void sendJsonData(String jsonKey,Object object,
			ServletRequest request, ServletResponse response) throws IOException {
		JsonMap<String, Object> modelMap = new JsonMap<String, Object>();
		modelMap.put(jsonKey, object);
		PrintWriter out = response.getWriter();
		out.println(request.getParameter("callback") + "("+modelMap+")");
		out.flush();
		out.close();
	}
}