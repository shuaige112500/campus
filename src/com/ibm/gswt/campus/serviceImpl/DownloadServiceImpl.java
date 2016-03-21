package com.ibm.gswt.campus.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.ibm.gswt.campus.bean.Comment;
import com.ibm.gswt.campus.bean.CommentList;
import com.ibm.gswt.campus.bean.LotteryReport;
import com.ibm.gswt.campus.bean.Topic;
import com.ibm.gswt.campus.bean.User;
import com.ibm.gswt.campus.dao.CommentDao;
import com.ibm.gswt.campus.dao.TopicDao;
import com.ibm.gswt.campus.dao.UserDao;
import com.ibm.gswt.campus.dao.UserLogDao;
import com.ibm.gswt.campus.daoImpl.CommentDaoImpl;
import com.ibm.gswt.campus.daoImpl.TopicDaoImpl;
import com.ibm.gswt.campus.daoImpl.UserDaoImpl;
import com.ibm.gswt.campus.daoImpl.UserLogDaoImpl;
import com.ibm.gswt.campus.service.DownloadService;
import com.ibm.gswt.campus.util.ExcelUtils;

public class DownloadServiceImpl implements DownloadService {
	
	private Logger logger = Logger.getLogger(DownloadServiceImpl.class);
	private UserDao userDao = new UserDaoImpl();
	private UserLogDao userLogDao = new UserLogDaoImpl();
	private CommentDao commentDao = new CommentDaoImpl();
	private TopicDao topicDao = new TopicDaoImpl();
	private String pattern = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sFormat = new SimpleDateFormat(pattern);

	@Override
	public String downloadExcel(String fileRootPath,String eventId) {
		
		logger.info("begin to create excel");
//		String path = fileRootPath + System.getProperty("file.separator") + new Date().getTime() + ".xls";
		String path = fileRootPath + System.getProperty("file.separator") + "reports.xls";
		
		try {
//			String dateStr = anmDaoImpl.getAnmByLocation(eventId).getDate();
//			logger.info("date=====" + dateStr);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = sdf.parse(dateStr);
//			Calendar c = Calendar.getInstance();
//			c.setTime(date);
//			c.set(Calendar.HOUR_OF_DAY, 0);
//			c.set(Calendar.MINUTE, 0);
//			c.set(Calendar.SECOND, 0);
//			c.set(Calendar.MILLISECOND,0);
//			Date startDate = c.getTime();
//			c.add(Calendar.DATE, 1);
//			Date endDate = c.getTime();
			
			
			
//			List<User> registUserList = userDao.getRegistUserList(location, startDate, endDate);
//			List<User> checkedUserList = userDao.getCheckInUserList(location, startDate, endDate);
//			List<Topic> topicList = topicDao.getTopicAndCommentCount(location);
//			List<CommentList> commentsList = commentListDao.getCommentListByLocation(location);
//			List<LotteryReport> lotteryList = topicDao.getLotteryByLocation(location);
			
			List<User> registerUsers = userLogDao.getRegisterUsers(eventId);
			List<User> checkedUsers = userLogDao.getCheckedUsers(eventId);
			List<Topic> topicList = topicDao.getTopicAndCommentCount(eventId);
			List<CommentList> comments = commentDao.getCommentListByEventId(eventId);
			List<LotteryReport> lotteryList = topicDao.getLotteryByLocation(eventId);
			
			List<User> users = userDao.getLuckyUsers(eventId);
			
			ExcelUtils excel = new ExcelUtils(path);
			Map<String, HSSFCellStyle> map = excel.getCellStyle();
			
			//register sheet
			writeFirstSheet(excel, map, registerUsers);
			//sign-in sheet
			writeSecondSheet(excel, map, checkedUsers);
			//topic sheet
			writeThirdSheet(excel, map, topicList);
			//comments sheet
			writeForthSheet(excel, map, comments);
			//shake sheet
			writeFifthSheet(excel,map, lotteryList);
			
			
			excel.exportXLS();
			logger.info("end to create excel");
		} catch (IOException e) {
			logger.info(e);
			e.printStackTrace();
		} catch (ParseException e) {
			logger.info(e);
			e.printStackTrace();
		}
		return path;
	}


	private void writeFirstSheet(ExcelUtils excel,
			Map<String, HSSFCellStyle> map, List<User> users) throws ParseException {
		
		final String SHEETNAME1 = "Registration";
		final String[] columnName1 = { "姓名", "学校", "专业", "年级", "邮箱", "手机", "注册时间", "是否签到" };
		final int[] rowWidthSheet1 = { 20, 20, 20, 20, 20, 20, 20, 20};
		
		final short rowHeight = 250;
		
		HSSFSheet sheet = excel.createSheet(SHEETNAME1);
		HSSFRow row1 = excel.createRow(sheet, 0);
		row1.setHeight(rowHeight);
		for (int i = 0; i < columnName1.length; i++) {
			excel.setCell(row1, i, columnName1[i], map.get("header"));
			sheet.setColumnWidth(i, rowWidthSheet1[i] * 256);
		}
		
		for (int i = 0; i < users.size(); i++) {
			HSSFRow row = excel.createRow(sheet, i + 1);
			User user = users.get(i);
//			excel.setCell(row, 0, user.getUID(), map.get("middle"));
			excel.setCell(row, 0, user.getUsername(), map.get("middle"));
			excel.setCell(row, 1, user.getSchool(), map.get("left"));
			excel.setCell(row, 2, user.getMajor(), map.get("left"));
			excel.setCell(row, 3, user.getGrade(), map.get("left"));
			excel.setCell(row, 4, user.getEmail(), map.get("left"));
			excel.setCell(row, 5, user.getPnum(),  map.get("left"));
			if(user.getCreatedTS()!=null){
				Date login = sFormat.parse(user.getCreatedTS());
				excel.setCell(row, 6, login, map.get("Date"));
			}else{
				excel.setCell(row, 6, "", map.get("left"));
			}
			if(user.getChecked()!=null && "1".equals(user.getChecked())){
				excel.setCell(row, 7, "是",  map.get("middle"));
			}else{
				excel.setCell(row, 7, "",map.get("middle"));
			}
			
			
		}
	}
	
	private void writeSecondSheet(ExcelUtils excel,
			Map<String, HSSFCellStyle> map, List<User> users) {
		
		final String SHEETNAME2 = "Sign-in";
		final String[] columnName2 = { "姓名", "手机" };
		final int[] rowWidthSheet2 = { 20, 20};
		
		final short rowHeight = 250;
		
		HSSFSheet sheet = excel.createSheet(SHEETNAME2);
		HSSFRow row1 = excel.createRow(sheet, 0);
		row1.setHeight(rowHeight);
		for (int i = 0; i < columnName2.length; i++) {
			excel.setCell(row1, i, columnName2[i], map.get("header"));
			sheet.setColumnWidth(i, rowWidthSheet2[i] * 256);
		}
		
		for (int i = 0; i < users.size(); i++) {
			HSSFRow row = excel.createRow(sheet, i + 1);
			User user = users.get(i);
			excel.setCell(row, 0, user.getUsername(), map.get("middle"));
			excel.setCell(row, 1, user.getPnum(),  map.get("left"));
		}
	}
	
	private void writeThirdSheet(ExcelUtils excel, Map<String, HSSFCellStyle> map, List<Topic> topics){
		
		final String SHEETNAME3 = "topic";
		final String[] columnName3 = {"主题","演讲者","点赞数","评论数"};
		final int[] rowWidthSheet3 = {20,20,20,20};
		final short rowHeight = 250;
		
		HSSFSheet sheet = excel.createSheet(SHEETNAME3);
		HSSFRow row1 = excel.createRow(sheet, 0);
		row1.setHeight(rowHeight);
		
		for(int i=0; i<columnName3.length; i++){
			excel.setCell(row1, i, columnName3[i],map.get("header"));
			sheet.setColumnWidth(i, rowWidthSheet3[i] * 256);
		}
		for(int i=0; i<topics.size(); i++){
			HSSFRow row = excel.createRow(sheet, i+1);
			Topic topic = topics.get(i);
			excel.setCell(row, 0, topic.getTopicName(), map.get("left"));
			excel.setCell(row, 1, topic.getSpeakerName(), map.get("left"));
			excel.setCell(row, 2, topic.getRating(), map.get("left"));
			excel.setCell(row, 3, topic.getCommentsCount(), map.get("left"));
		}
	}
	
	
	private void writeForthSheet(ExcelUtils excel, Map<String, HSSFCellStyle> map, List<CommentList> comments) throws ParseException{
		
		final String SHEETNAME4 = "comments";
		final String[] columnName4 = {"主题","演讲者","地点","留言","留言时间","留言者","电话","学校","邮箱","专业","年级","是否显示到大屏"};
		final int[] rowWidthSheet4 = { 20, 15, 20, 40, 20, 15, 15, 20, 20, 20, 10, 20};
		final short rowHeight = 250;
		
		HSSFSheet sheet = excel.createSheet(SHEETNAME4);
		HSSFRow row1 = excel.createRow(sheet, 0);
		row1.setHeight(rowHeight);
		for (int i = 0; i < columnName4.length; i++) {
			excel.setCell(row1, i, columnName4[i], map.get("header"));
			sheet.setColumnWidth(i, rowWidthSheet4[i] * 256);
		}
		for (int i = 0; i < comments.size(); i++) {
			HSSFRow row = excel.createRow(sheet, i + 1);
			CommentList comment = comments.get(i);
			excel.setCell(row, 0, comment.getTopicName(), map.get("left"));
			excel.setCell(row, 1, comment.getSpeakerName(),  map.get("left"));
			excel.setCell(row, 2, comment.getClocation(),  map.get("left"));
			excel.setCell(row, 3, comment.getComments(),  map.get("left"));
			if(comment.getCreateTs()!=null){
				Date createdDate = sFormat.parse(comment.getCreateTs());
				excel.setCell(row, 4, createdDate, map.get("Date"));
			}else{
				excel.setCell(row, 4, "", map.get("left"));
			}
			excel.setCell(row, 5, comment.getUsername(),  map.get("left"));
			excel.setCell(row, 6, comment.getPnum(),  map.get("left"));
			excel.setCell(row, 7, comment.getSchool(),  map.get("left"));
			excel.setCell(row, 8, comment.getEmail(),  map.get("left"));
			excel.setCell(row, 9, comment.getMajor(),  map.get("left"));
			excel.setCell(row, 10, comment.getGrade(),  map.get("left"));
			if(comment.getChoosen()!=null && "1".equals(comment.getChoosen())){
				excel.setCell(row, 11, "是",  map.get("middle"));
			}else{
				excel.setCell(row, 11, "",  map.get("middle"));
			}
			
		}
	}
	
	private void writeFifthSheet(ExcelUtils excel,
			Map<String, HSSFCellStyle> map, List<LotteryReport> lotteryList) throws ParseException {
		
		final String SHEETNAME4 = "lottery";
		final String[] columnName4 = {"地点", "开始时间","结束时间","参与人数","中奖者","电话号","邮箱","学校","专业","年级"};
		final int[] rowWidthSheet4 = { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
		final short rowHeight = 250;
		
		HSSFSheet sheet = excel.createSheet(SHEETNAME4);
		HSSFRow row1 = excel.createRow(sheet, 0);
		row1.setHeight(rowHeight);
		for (int i = 0; i < columnName4.length; i++) {
			excel.setCell(row1, i, columnName4[i], map.get("header"));
			sheet.setColumnWidth(i, rowWidthSheet4[i] * 256);
		}
		for (int i = 0; i < lotteryList.size(); i++) {
			HSSFRow row = excel.createRow(sheet, i + 1);
			LotteryReport lottery = lotteryList.get(i);
			excel.setCell(row, 0, lottery.getLocation(), map.get("left"));
			if(lottery.getStartTs()!=null){
				Date startDate = sFormat.parse(lottery.getStartTs());
				excel.setCell(row, 1, startDate, map.get("Date"));
			}else{
				excel.setCell(row, 1, "", map.get("left"));
			}
			if(lottery.getEndTs()!=null){
				Date endDate = sFormat.parse(lottery.getEndTs());
				excel.setCell(row, 2, endDate,  map.get("Date"));
			}else{
				excel.setCell(row, 2, "",  map.get("left"));
			}
			excel.setCell(row, 3, lottery.getNumber(),  map.get("left"));
			excel.setCell(row, 4, lottery.getUsername(),  map.get("left"));
			excel.setCell(row, 5, lottery.getPnum(),  map.get("left"));
			excel.setCell(row, 6, lottery.getEmail(),  map.get("left"));
			excel.setCell(row, 7, lottery.getSchool(),  map.get("left"));
			excel.setCell(row, 8, lottery.getMajor(),  map.get("left"));
			excel.setCell(row, 9, lottery.getGrade(),  map.get("left"));
		}
	}
	
	/**
	 * delete file
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFile(String filePath){
		File file = new File(filePath);
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (File fileChild : files) {
				if (fileChild.isFile()) {
					fileChild.delete();
				} else {
					boolean isTrue = deleteFile(fileChild.getPath());
					if (isTrue) {
						fileChild.delete();
					} else {
						logger.info("There some files can not be deleted!");
					}
				}
			}
		}

		File isDelAll = new File(file.getPath());
		if (isDelAll.exists()) {
			return true;
		} else {
			File[] isFiles = isDelAll.listFiles();
			if (null != isFiles && isFiles.length > 0) {
				return false;
			}
			return true;
		}
	}

	@Override
	public String downloadComment(String fileRootPath) {
		
		logger.info("begin to create comment excel");
		String path = fileRootPath + System.getProperty("file.separator") + new Date().getTime() + ".xls";
		
		try {
			
//			List<Comment> commentList = topicDao.getAllComments();
			List<Comment> commentList = null;
			
			ExcelUtils excel = new ExcelUtils(path);
			Map<String, HSSFCellStyle> map = excel.getCellStyle();
			
			writeCommentFirstSheet(excel, map, commentList);
			
			excel.exportXLS();
			logger.info("end to create comment excel");
		} catch (IOException e) {
			logger.info(e);
			e.printStackTrace();
		} catch (ParseException e) {
			logger.info(e);
			e.printStackTrace();
		}
		return path;
	}
	
	private void writeCommentFirstSheet(ExcelUtils excel,
			Map<String, HSSFCellStyle> map, List<Comment> comments) throws ParseException {
		
		final String SHEETNAME3 = "Comments";
		final String[] columnName3 = { "Comment", "user", "topic", "create time" };
		final int[] rowWidthSheet3 = { 100, 20, 20, 20};
		
		final short rowHeight = 250;
		
		HSSFSheet sheet = excel.createSheet(SHEETNAME3);
		HSSFRow row1 = excel.createRow(sheet, 0);
		row1.setHeight(rowHeight);
		for (int i = 0; i < columnName3.length; i++) {
			excel.setCell(row1, i, columnName3[i], map.get("header"));
			sheet.setColumnWidth(i, rowWidthSheet3[i] * 256);
		}
		
		for (int i = 0; i < comments.size(); i++) {
			HSSFRow row = excel.createRow(sheet, i + 1);
			Comment comment = comments.get(i);
			excel.setCell(row, 0, comment.getComments(), map.get("middle"));
			excel.setCell(row, 1, comment.getUserName(), map.get("left"));
//			excel.setCell(row, 2, comment.getTopicName(), map.get("left"));
			if(comment.getCreatedTS()!=null){
				Date createTime = sFormat.parse(comment.getCreatedTS());
				excel.setCell(row, 3, createTime, map.get("Date"));
			}else{
				excel.setCell(row, 3, "", map.get("left"));
			}
		}
	}
}