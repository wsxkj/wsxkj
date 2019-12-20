package com.zpj.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Properties;

import org.apache.commons.io.FileUtils;


public class FileUtil {
	/**
	 * 拷贝文件
	 * @Title copyFile
	 * @param src
	 * @param target
	 * @throws IOException
	 * @author zpj
	 * @time 2019年12月17日 下午1:20:36
	 */
	public static void copyFile(File src, File target) throws IOException {
		FileChannel in = null;
		FileChannel out = null;
		try {
			in = new FileInputStream(src).getChannel();
			out = new FileOutputStream(target).getChannel();
			out.transferFrom(in, 0, src.length());
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		File a=new File("D://test/jflyfox/bbs/article_file/20180912_143719_248315.txt");
		File b=new File("D://test/fa/1.txt");
		copyFile(a,b);
	}
	
	
	/**
	 * 备份附件
	 * @Title backup
	 * @param sourcePath 项目路径到ueditor/userid
	 * @param fileName  文件名称
	 * @return
	 * @author zpj
	 * @time 2019年12月17日 下午3:09:08
	 */
	public static boolean backup(String sourcePath,String fileName) {
		Long start=System.currentTimeMillis();
		System.out.println("备份文件开始");
		String filePath=sourcePath.substring(sourcePath.lastIndexOf("/"));
		Properties props=System.getProperties();
		String osname = props.getProperty("os.name");
		StringBuffer backupPath = new StringBuffer(50); // 备份路径
	    if(osname.indexOf("Window")>-1){
	    	backupPath.append("D://test/ueditor/"+filePath); 
	    }else{
	    	backupPath.append("/opt/backup/ueditor/"+filePath); 
	    }
		File existBackupPath=new File(backupPath.toString());
		File srcFile = new File(sourcePath+"/"+fileName);
		File destFile = new File(backupPath+"/"+fileName);
		try {
			//判断备份文件夹是否存在，不存在就创建
			if(!existBackupPath.exists()){
				existBackupPath.mkdirs();
			}
			if (destFile.exists()) {
				destFile.delete();
			}
			copyFile(srcFile, destFile);
			Long end=System.currentTimeMillis();
			System.out.println("备份文件结束,耗时："+(end-start)+"毫秒");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 还原备份的文件
	 * @Title restore
	 * @param projectPath 项目路径不带ueditor
	 * @param restorePathAndName   sys_uploadfile表中filrUrl 带路径和项目名
	 * @return
	 * @author zpj
	 * @time 2019年12月17日 下午3:12:29
	 */
	public static boolean restore(String projectPath,String restorePathAndName) {
		Long start=System.currentTimeMillis();
		System.out.println("还原文件开始");
		if ("".equalsIgnoreCase(projectPath)) {
			return false;
		}
		File destFile = new File(projectPath+restorePathAndName);
		// 判断文件是否存在，已经存在不用恢复
		if (destFile.exists()) {
			return true;
		}
		//判断文件夹是否存在
		String fullPath=projectPath+restorePathAndName.substring(0, restorePathAndName.lastIndexOf("/"));
		File fullPathFile=new File(fullPath);
		if(!fullPathFile.exists()){
			fullPathFile.mkdirs();
		}
			
		Properties props=System.getProperties();
		String osname = props.getProperty("os.name");
		StringBuffer backupPath = new StringBuffer(50); // 备份路径
	    if(osname.indexOf("Window")>-1){
	    	backupPath.append("D://test"+restorePathAndName); 
	    }else{
	    	backupPath.append("/opt/backup"+restorePathAndName); 
	    }
		File srcFile = new File(backupPath.toString());
		// 备份文件都不存在，还恢复个P啊
		if (!srcFile.exists()) {
			System.out.println("备份都没有还原个p");
			return false;
		}
		try {
			// 复制恢复
			copyFile(srcFile, destFile);
			Long end=System.currentTimeMillis();
			System.out.println("还原文件结束,耗时："+(end-start)+"毫秒");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
