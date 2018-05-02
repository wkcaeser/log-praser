package com.wkutil.log.parse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args){
		String path = Main.class.getResource("/").getPath();
		System.out.println(path);
		path = path.substring(0, path.length()-8) + "resources/";
		//读取日志文件
		List<LogElement> logs = LogFileParse.readLogFile(new File("C:\\Users\\wkgui\\Desktop\\服务器巡检\\merge-file-1524569845630.txt"));
		logs = logs.stream()
				.filter(logElement -> "ERROR".equals(logElement.getLogLevel()))
				.sorted(((o1, o2) -> {
					if (Objects.equals(o1.getCmd(), o2.getCmd())){
						return o1.getDateTime().compareTo(o2.getDateTime());
					}else {
						return o1.getCmd().compareTo(o2.getCmd());
					}
				}))
				.collect(Collectors.toList());
//		logs.forEach(System.out::println);
		System.out.println("ERROR 数量： " + logs.size());
		Workbook out;
		try {
			out = new XSSFWorkbook(OPCPackage.open(new File(path + "日志ERROR统计表.xlsx")));
		} catch (IOException | InvalidFormatException e) {
			System.err.println("读取模板出错");
			return;
		}
		//写入excel
		String outPath = "C:/Users/wkgui/Desktop/服务器巡检/xiantao-04-24.xlsx";
		out = LogFileParse.writeWorkbook(logs, out, 0);
		LogFileParse.writeExcelFile(out, outPath);
	}
}
