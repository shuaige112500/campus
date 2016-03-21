package com.ibm.gswt.campus.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionUtil {

	public static boolean isAdminUser(HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (session.getAttribute("admin") == null) {
			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
			return false;
		}
		return true;
	}
}
