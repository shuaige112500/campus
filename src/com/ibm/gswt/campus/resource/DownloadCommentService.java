package com.ibm.gswt.campus.resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.ibm.gswt.campus.service.DownloadService;
import com.ibm.gswt.campus.serviceImpl.DownloadServiceImpl;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("DownloadComment")
public class DownloadCommentService {
	
	private static Logger logger = Logger.getLogger(DownloadCommentService.class);
	DownloadService downloadService = new DownloadServiceImpl();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public void download(@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@Context ServletContext servletContext) throws IOException {
		
//		HttpSession session = request.getSession();
//		if (session.getAttribute("admin") == null) {
//			JsonSendUtil.sendJsonData("IsAdmin", "False", request, response);
//			return;
//		}
		
		logger.info("start to download comments!");
		String fileRootPath = servletContext.getRealPath("files");
		String path = downloadService.downloadComment(fileRootPath);
		response.setContentType("excel/plain");
		response.setHeader("Location", "report");
		response.setHeader("Content-Disposition", "attachment; filename=" + "report.xls");
		OutputStream outputStream = response.getOutputStream();
		InputStream inputStream = new FileInputStream(path);
		byte[] buffer = new byte[1024];
		int i = -1;
		while ((i = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, i);
		}
		outputStream.flush();
		outputStream.close();
		inputStream.close();
		logger.info("end download excel");
		downloadService.deleteFile(path);
		
	}
}