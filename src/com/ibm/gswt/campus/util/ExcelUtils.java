package com.ibm.gswt.campus.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class ExcelUtils {

	//	private static short XLS_ENCODING = HSSFWorkbook.SHEET_STATE_HIDDEN;

	private static String DATE_FORMAT = "m/d/yyyy hh:mm:ss"; //  "m/d/yy h:mm"

	private static String NUMBER_FORMAT = "#,##0.00";

	private String xlsFileName;

	private HSSFWorkbook workbook;

	/**
	 * 
	 * @param fileName
	 */
	public ExcelUtils(String fileName) {
		this.xlsFileName = fileName;
		this.workbook = new HSSFWorkbook();
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void exportXLS() throws IOException {
		FileOutputStream fOut = new FileOutputStream(xlsFileName);
		workbook.write(fOut);
		fOut.flush();
		fOut.close();

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public HSSFSheet createSheet(String name) {
		HSSFSheet sheet = workbook.createSheet(name);
		return sheet;
	}

	/**
	 * 
	 * @param index
	 */
	public HSSFRow createRow(HSSFSheet sheet, int index) {
		HSSFRow row = sheet.createRow(index);
		return row;
	}

	/**
	 * 
	 * @param index
	 * @param value
	 */
	public void setCell(HSSFRow row, int index, String value, HSSFCellStyle cellStyle) {
		HSSFCell cell = row.createCell(index);
		cell.setCellStyle(cellStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(value));
	}

	/**
	 * 
	 * @param index
	 * @param value
	 */
	public void setCell(HSSFRow row, int index, String value, String rul, HSSFCellStyle cellStyle) {
		HSSFCell cell = row.createCell(index);
		cell.setCellStyle(cellStyle);
		if (StringUtils.isNotEmpty(rul)) {
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula("HYPERLINK(\"" + rul + "\")");
		}
		cell.setCellValue(new HSSFRichTextString(value));
	}

	/**
	 * 
	 * @param index
	 * @param value
	 */
	public void setCell(HSSFRow row, int index, Date value, HSSFCellStyle cellStyle) {
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 
	 * @param index
	 * @param value
	 */
	public void setCell(HSSFRow row, int index, int value, HSSFCellStyle cellStyle) {
		HSSFCell cell = row.createCell(index);
		cell.setCellStyle(cellStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}

	/**
	 * 
	 * @param index
	 * @param value
	 */
	public void setCell(HSSFRow row, int index, double value) {
		HSSFCell cell = row.createCell(index);
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat format = workbook.createDataFormat();
		cellStyle.setDataFormat(format.getFormat(NUMBER_FORMAT));
		cell.setCellStyle(cellStyle);
	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, HSSFCellStyle> getCellStyle() {
		Map<String, HSSFCellStyle> map = new HashMap<String, HSSFCellStyle>();
		map.put("middle", getCellStyle(Constant.ALIGNMIDDLE, Constant.VERTICALALIGNMIDDLE, false));
		map.put("header", getCellStyle(Constant.ALIGNMIDDLE, Constant.VERTICALALIGNMIDDLE, true));
		map.put("left", getCellStyle(Constant.ALIGNLEFT, Constant.VERTICALALIGNMIDDLE, false));
		map.put("Date", getDateCellStyle(Constant.ALIGNMIDDLE, Constant.VERTICALALIGNMIDDLE));
		map.put("link", getLinkCellStyle(Constant.ALIGNLEFT, Constant.VERTICALALIGNMIDDLE));
		return map;
	}

	/**
	 * 
	 * @param align
	 * @param verticalAlign
	 * @param isHeader
	 * @return
	 */
	public HSSFCellStyle getCellStyle(short align, short verticalAlign, boolean isHeader) {
		HSSFCellStyle cellStyle = this.workbook.createCellStyle();
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(align);
		cellStyle.setVerticalAlignment(verticalAlign);
		if (isHeader) {
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}

		//font
		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		cellStyle.setFont(font);

		// border
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		return cellStyle;
	}

	/**
	 * 
	 * @param align
	 * @param verticalAlign
	 * @return
	 */
	public HSSFCellStyle getDateCellStyle(short align, short verticalAlign) {
		HSSFCellStyle cellStyle = this.workbook.createCellStyle();
		cellStyle.setDataFormat(this.workbook.createDataFormat().getFormat(DATE_FORMAT));
		cellStyle.setWrapText(true);
		cellStyle.setAlignment(align);
		cellStyle.setVerticalAlignment(verticalAlign);

		//font
		HSSFFont font = this.workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		cellStyle.setFont(font);

		// border
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		return cellStyle;
	}

	public HSSFCellStyle getLinkCellStyle(short align, short verticalAlign) {
		HSSFCellStyle cellStyle = this.workbook.createCellStyle();

		cellStyle.setWrapText(true);
		cellStyle.setAlignment(align);
		cellStyle.setVerticalAlignment(verticalAlign);

		//font
		HSSFFont font = this.workbook.createFont();
		font.setUnderline((byte) 1);
		font.setColor(HSSFColor.BLUE.index);
		font.setFontHeightInPoints((short) 10);
		cellStyle.setFont(font);

		// border
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		return cellStyle;
	}
	
	public static void main(String[] args) throws IOException {
		
		OutputStream os = new FileOutputStream("C:\\Users\\IBM_ADMIN\\Desktop\\aaa.xls");
		
		HSSFWorkbook book = new HSSFWorkbook();
		
		HSSFSheet sheet = book.createSheet();
		
		HSSFRow row = sheet.createRow(1);
		
		HSSFCell cell = row.createCell(1);
		
		cell.setCellValue("aaa");
		
		book.write(os);
	}
}
