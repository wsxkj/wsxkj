package com.zpj.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import javax.servlet.ServletContext;

@Controller
public class PropertyHelper{
	
	private static ResourceBundle rb ;
	
	public static Map getPhonePropertiesValue() {
		Properties prop = new Properties();
		InputStream fis = null;
		 Map params=new HashMap();
		try {
			fis = PropertyHelper.class.getResourceAsStream("/phone.properties");
			prop.load(fis);
			params.put("ios.latestversion",prop.get("ios.latestversion"));
			params.put("ios.downloadaddress",prop.get("ios.downloadaddress"));
			params.put("ios.forceupdate",prop.get("ios.forceupdate"));
			params.put("android.latestversion",prop.get("android.latestversion"));
			params.put("android.downloadaddress",prop.get("android.downloadaddress"));
			params.put("android.forceupdate",prop.get("android.forceupdate"));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e2) {
			}
		}
		return params;

	}
	
	public static String getPropertiesValue(String name, String key) {
		Properties prop = new Properties();
		String value = "";
		InputStream fis = null;
		try {
			fis = PropertyHelper.class.getResourceAsStream("/" + name + ".properties");
			prop.load(fis);
			value = (String) prop.get(key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e2) {
			}
		}
		return value;

	}
		
		public static void init(){
			try {
				System.out.println("----初始化版本配置信息----");
				rb = ResourceBundle.getBundle("phone");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**状态
		 * 根据key根据对应的value
		 * @Title getCodeValue
		 * @param prefix
		 * @return
		 */
		public static String getCodeValue(String prefix) {
			if(rb==null){
				rb = ResourceBundle.getBundle("phone");
			}
			return rb.getString(prefix);
		}

}
