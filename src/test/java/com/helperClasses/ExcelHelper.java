package com.helperClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class ExcelHelper {

	public HashMap<String, String> getXpathMap(String filePath) {
		HashMap<String, String> map = new HashMap<String, String>();

		try {
			File file = new File(System.getProperty("user.dir") + filePath);
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook repository = new HSSFWorkbook(fis);
			HSSFSheet workSheet = repository.getSheet("Sheet1");
			for (int i = 0; i <= workSheet.getLastRowNum(); i++) {
				Row row = workSheet.getRow(i);
				String key = row.getCell(0).getStringCellValue().trim();
				String value = (String) row.getCell(1).getStringCellValue().trim();
				map.put(key, value);
			}

		} catch (Exception ex) {
			System.out.println("<<Exception>>" + ex.getMessage());
		}
		return map;

	}

	public void exportResults(String filePath, Map<String, String> exportResult) {
		boolean result = false;
		try {
			String fileName = System.getProperty("user.dir") + filePath;
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			HSSFWorkbook repository = new HSSFWorkbook(fis);
			HSSFSheet workSheet = repository.getSheet("Sheet1");
			int rowNumber = workSheet.getLastRowNum();
			
			// enter fresh results
			
			Row row = workSheet.createRow(rowNumber + 1);
			int recordNumber=rowNumber+1;
			row.createCell(0).setCellValue(recordNumber);
			row.createCell(1).setCellValue(exportResult.get("Score").toString());
			row.createCell(2).setCellValue(exportResult.get("Rank").toString());
			row.createCell(3).setCellValue(exportResult.get("CutOff").toString());

			fis.close();
			FileOutputStream fos = new FileOutputStream(fileName);
			repository.write(fos);
			fos.close();
			result = true;

		} catch (Exception ex) {
			System.out.println("Exception while exporting results to excel >> " + ex.getMessage());
		}
	}

}
