package com.zpj.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @Description: poi操作excel工具类
 * @ClassName: ExcelUtils
 *
 */
public class ExcelHelper {
	/**
	 * 采用单利模式实例化
	 */
	private static ExcelHelper excelUtils = new ExcelHelper();

	private ExcelHelper() {
	}

	public static ExcelHelper getInstance() {
		return excelUtils;
	}

	/**
	 * @Description 读取excel
	 * @Title readExcel
	 * @param inputStream
	 * @return List
	 */
	public List readExcel(InputStream inputStream) {
		return new ReadExcel().readExcel(inputStream);
	}

	/**
	 * @Description 生成excel
	 * @Title writeExcel
	 * @param excelPath
	 * @return boolean
	 */
	public boolean writeExcel(String excelPath) {
		return true;
	}

	/**
	 * @Description: 读取excel
	 * @ClassName: ReadExcel
	 *
	 */
	private class ReadExcel {
		/**
		 * 读取excel操作对象
		 */
		private POIFSFileSystem fs = null;
		private HSSFWorkbook wb = null;
		private HSSFSheet sheet = null;
		private HSSFRow row = null;

		public List readExcel(InputStream inputStream) {
			List<Map<String, String>> jsonList = new ArrayList<Map<String, String>>();
			/**
			 * 读取表头
			 */
			jsonList.add(this.readExcelTitle(inputStream));

			/**
			 * 读取内容
			 */
			jsonList.add(this.readExcelContent(inputStream));

			return jsonList;
		}
		
		/**
		 * @Description 读取表头
		 * @Title readExcelTitle
		 * @param inputStream
		 * @return Map<String,String>
		 */
		private Map<String, String> readExcelTitle(InputStream inputStream) {
			Map<String, String> titleMap = new HashMap<String, String>();
			try {
				fs = new POIFSFileSystem(inputStream);
				wb = new HSSFWorkbook(fs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogManager.getLogger(ReadExcel.class).log(Level.FATAL,
						"file stream is error.", e);
			}
			sheet = wb.getSheetAt(0);
			row = sheet.getRow(0);
			// 标题总列数
			int colNum = row.getPhysicalNumberOfCells();
			LogManager.getLogger(ReadExcel.class).log(Level.INFO, "" + colNum);
			
			for (int i = 0; i < colNum; i++) {
				titleMap.put("" + i, this.getCellFormatValue(row.getCell(i)));
			}

			return titleMap;
		}

		/**
		 * @Description 读取Excel数据内容
		 * @Title readExcelContent
		 * @param inputStream
		 * @return Map<String,String>
		 */
		private Map<String, String> readExcelContent(InputStream inputStream) {
			Map<String, String> content = new HashMap<String, String>();
			sheet = wb.getSheetAt(0);
			// 得到总行数
			int rowNum = sheet.getLastRowNum();
			row = sheet.getRow(0);
			int colNum = row.getPhysicalNumberOfCells();
			String [] str = new String[colNum];
			// 正文内容应该从第二行开始,第一行为表头的标题
			for (int i = 1; i <= rowNum; i++) {
				row = sheet.getRow(i);
				int j = 0;
				while (j < colNum) {
					str[j] = getCellFormatValue(row.getCell(j)).trim();
					j++;
				}
				content.put("" + i, Arrays.toString(str));
			}
			return content;
		}

		/**
		 * @Description 格式化单元格数据
		 * @Title getCellFormatValue
		 * @param cell
		 * @return String
		 */
		private String getCellFormatValue(HSSFCell cell) {
			String cellvalue = "";
			if (cell != null) {
				// 判断当前Cell的Type
				switch (cell.getCellType()) {
				// 如果当前Cell的Type为NUMERIC
				case HSSFCell.CELL_TYPE_NUMERIC:{}
				case HSSFCell.CELL_TYPE_FORMULA: {
					// 判断当前的cell是否为Date
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// 如果是Date类型则，转化为Data格式
						// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
						// cellvalue = cell.getDateCellValue().toLocaleString();
						// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
						Date date = cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						cellvalue = sdf.format(date);
					}
					// 如果是纯数字
					else {
						// 取得当前Cell的数值
						cellvalue = String.valueOf((int)cell.getNumericCellValue());
					}
					break;
				}
				// 如果当前Cell的Type为STRIN
				case HSSFCell.CELL_TYPE_STRING:
					// 取得当前的Cell字符串,去换行
					cellvalue = cell.getRichStringCellValue().getString().replaceAll("\n", "").replaceAll(",","，");
					break;
				// 默认的Cell值
				default:
					cellvalue = " ";
				}
			} else {
				cellvalue = "";
			}
			return cellvalue;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("E:\\测试文档05.xls");
		InputStream inputStream =new FileInputStream(file);
		List list = ExcelHelper.getInstance().readExcel(inputStream);
		System.out.println(list);
	}
}