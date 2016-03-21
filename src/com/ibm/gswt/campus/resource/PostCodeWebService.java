package com.ibm.gswt.campus.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.util.EmailSMS;


@Path("/PostCode")
public class PostCodeWebService {

	private static Logger logger = Logger.getLogger(PostCodeWebService.class);
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public void PsotCode(@QueryParam("tel") String tel,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		logger.info("---PsotCode---");
		logger.info("---tel---" + tel);
		
		String num = EmailSMS.emailSMS(tel);
		
		HttpSession numSession = request.getSession();
		numSession.setAttribute("num", num);
		
	}
	
}
