package com.ibm.gswt.campus.service;

public interface DownloadService {
	
	public String downloadExcel(String fileRootPath,String location);
	
	public boolean deleteFile(String filePath);
	
	public String downloadComment(String fileRootPath);
	
}
