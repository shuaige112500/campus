package com.ibm.gswt.campus.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.ibm.gswt.campus.util.ApplicationProperties;
import com.ibm.gswt.campus.util.JsonSendUtil;

@Path("/FileUpload")
public class FileUploadWebService {
	
	private static Logger logger = Logger.getLogger(FileUploadWebService.class);	

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public void fileUpload(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@Context ServletContext servletContext) throws IOException {
		
        try {  
        	String path = ApplicationProperties.getProps().getProperty("user.file.path");
        	
        	logger.info(path);
        	
    		String currentTime = null;
    		String fileType = null;
    		// Get Header info
    		Enumeration<String>  enu = request.getHeaderNames();
    		while(enu.hasMoreElements()){
    			String headerName = enu.nextElement();
    			if ("Time-Stamp".equals(headerName)) {
    				currentTime = request.getHeader(headerName);
    			}
    			if ("File-Type".equals(headerName)) {
    				fileType = request.getHeader(headerName);
    			}
    		}
    		
    		DiskFileItemFactory factory = new DiskFileItemFactory();  
              
            ServletFileUpload upload = new ServletFileUpload(factory);
            
            List<FileItem> list = upload.parseRequest(request);  
            
            String filePath = null;
            
            for(FileItem item : list){  
                if(!item.isFormField()){
                	
                    String value = item.getName();
                    InputStream in = item.getInputStream();
                    
                    filePath = path + currentTime + "_" + fileType;
                    
                    File file = new File(filePath);
                    if (!file.exists()) {
                    	file.mkdir();
                    	logger.info("mkdir");
                    } else {
                    	File[] files = file.listFiles();
                    	for (File oldFile : files) {
                    		oldFile.delete();
                    		logger.info(oldFile.getName() + "delete file");
                    	}
                    }
                    
                    OutputStream os = new FileOutputStream(new File(filePath, value));
                    int i;
                    byte[] bytes = new byte[1024];
                    System.out.println(in.available());
                    while((i=in.read(bytes)) != -1) {
                    	os.write(bytes, 0, i);
                    }
                    os.flush();
                    os.close();
                    in.close();
                }
            }
            logger.info("upload successfully");
        } catch (Exception e) {  
        	JsonSendUtil.sendJsonData("Message", "Error", request, response);
            e.printStackTrace();  
        }  
	}
	
	@OPTIONS
	@Produces({MediaType.APPLICATION_JSON})
	public void get(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@Context ServletContext servletContext) throws IOException {
		
	}
}
