package com.wkutil.log.parse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogFileParse {

	public static List<LogElement> readLogFile(File file){
		if(file == null){
			return null;
		}
		List<LogElement> logs = new ArrayList<>(256);
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			System.err.println("文件不存在");
			return null;
		}
		String logLine;
		try {
			while( (logLine = bufferedReader.readLine())!=null){
				LogElement logElement = generateLogElement(logLine);
				if(logElement != null){
					logs.add(logElement);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logs;
	}

	private static LogElement generateLogElement(String logLine){
		try {
			String[] logColumns = logLine.split("]");
			LogElement logElement = new LogElement();
			logElement.setCmd(logColumns[5].substring(6, logColumns[5].length()));
			logElement.setDateTime(logColumns[1].substring(1, logColumns[1].length()));
			logElement.setLogLevel(logColumns[0].substring(1, logColumns[0].length()));
			logElement.setVersion(logColumns[2].substring(10, logColumns[2].length()));
			logElement.setExceptionMsg(logColumns[15].substring(12, logColumns[15].length()));
			return logElement;
		} catch (Exception e) {
			System.err.println("无法分析日志内容：" + logLine);
			return null;
		}
	}

	public static Workbook writeWorkbook(List<LogElement> logs, Workbook workbook, int sheetIndex){
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		int rowId = 0;
		for (LogElement logElement : logs){
			rowId++;
			Row row = sheet.createRow(rowId);
			row.createCell(0).setCellValue(logElement.getCmd());
			row.createCell(1).setCellValue(logElement.getDateTime());
			row.createCell(2).setCellValue(logElement.getVersion());
			row.createCell(3).setCellValue(logElement.getLogLevel());
			row.createCell(4).setCellValue(logElement.getExceptionMsg());
		}
		return workbook;
	}
	public static void writeExcelFile(Workbook out, String outPath){
		try {
			out.write(new FileOutputStream(outPath));
		} catch (IOException e) {
			System.err.println("写入excel文件出错");
			e.printStackTrace();
		}
	}
}
