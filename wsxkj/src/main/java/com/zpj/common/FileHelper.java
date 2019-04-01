package com.zpj.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

public class FileHelper {
	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public String readTxt(String filePathAndName, String encoding)
			throws IOException {
		encoding = encoding.trim();
		StringBuffer str = new StringBuffer("");
		String st = "";
		try {
			FileInputStream fs = new FileInputStream(filePathAndName);
			InputStreamReader isr;
			if (encoding.equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			BufferedReader br = new BufferedReader(isr);
			try {
				String data = "";
				while ((data = br.readLine()) != null) {
					str.append(data + " ");
				}
			} catch (Exception e) {
				str.append(e.toString());
			}
			st = str.toString();
		} catch (IOException es) {
			st = "";
		}
		return st;
	}

	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            目录
	 * @return 返回目录创建后的路径
	 */
	public String createFolder(String folderPath) {
		String txt = folderPath;
		try {
			java.io.File myFilePath = new java.io.File(txt);
			txt = folderPath;
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return txt;
	}

	/**
	 * 多级目录创建
	 * 
	 * @param folderPath
	 *            准备要在本级目录下创建新目录的目录路径 例如 c:myf
	 * @param paths
	 *            无限级目录参数，各级目录以单数线区分 例如 a|b|c
	 * @return 返回创建文件后的路径 例如 c:myfac
	 */
	public String createFolders(String folderPath, String paths) {
		String txts = folderPath;
		try {
			String txt;
			txts = folderPath;
			StringTokenizer st = new StringTokenizer(paths, "|");
			for (int i = 0; st.hasMoreTokens(); i++) {
				txt = st.nextToken().trim();
				if (txts.lastIndexOf("/") != -1) {
					txts = createFolder(txts + txt);
				} else {
					txts = createFolder(txts + txt + "/");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return txts;
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @param fileContent
	 *            文本文件内容
	 * @return
	 */
	public void createFile(String path, String fileName, String fileContent) {
		String filePath = "";
		try {
			filePath = path + fileName;

			File myFilePath = new File(path, fileName);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			myFile.close();
			resultFile.close();
		} catch (Exception e) {
			System.out.println(filePath);
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            文本文件完整绝对路径及文件名
	 * @return Boolean 成功删除返回true遭遇异常返回false
	 */
	public static boolean delFile(String filePathAndName) {
		boolean bea = false;
		try {
			String filePath = filePathAndName;
			File myDelFile = new File(filePath);
			if (myDelFile.exists()) {
				myDelFile.delete();
				bea = true;
			} else {
				bea = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bea;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            文件夹完整绝对路径
	 * @return
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * @return
	 * @return
	 */
	public boolean delAllFile(String path) {
		boolean bea = false;
		File file = new File(path);
		if (!file.exists()) {
			return bea;
		}
		if (!file.isDirectory()) {
			return bea;
		}
		if(file != null){
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					temp.delete();
				}
				if (temp.isDirectory()) {
					delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
					delFolder(path + "/" + tempList[i]);// 再删除空文件夹
					bea = true;
				}
			}
		}
		return bea;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPathFile
	 *            准备复制的文件源
	 * @param newPathFile
	 *            拷贝到新绝对路径带文件名
	 * @return
	 */
	public int copyFile(String oldPathFile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件

				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					// System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}

				inStream.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public static int copyFile(File oldfile, String newPathFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (oldfile.exists()) { // 文件存在时
				Properties props=System.getProperties();
				 //System.out.println("操作系统的名称："+props.getProperty("os.name"));
			    // System.out.println("操作系统的构架："+props.getProperty("os.arch"));
			   //  System.out.println("操作系统的版本："+props.getProperty("os.version"));
			    // System.out.println("文件分隔符："+props.getProperty("file.separator"));   //在 unix 系统中是”／”
			   //  System.out.println("路径分隔符："+props.getProperty("path.separator"));   //在 unix 系统中是”:”
			  //   System.out.println("行分隔符："+props.getProperty("line.separator"));   //在 unix 系统中是”/n”
			     String osname = props.getProperty("os.name");
			     if(osname.indexOf("Window")>-1){
			    	 //window系统
			    	 newPathFile = newPathFile.replace("/", "\\");
						String s = newPathFile.substring(0, newPathFile.lastIndexOf("\\")+1);
						File fpath = new File(s);
						if(!fpath.exists()){
							fpath.mkdirs();
							//new FileHelper().createfiles(newPathFile);					
						}
						InputStream inStream = new FileInputStream(oldfile.getPath()); // 读入原文件
						FileOutputStream fs = new FileOutputStream(newPathFile);
						byte[] buffer = new byte[1444];
						while ((byteread = inStream.read(buffer)) != -1) {
							bytesum += byteread; // 字节数 文件大小
							// System.out.println(bytesum);
							fs.write(buffer, 0, byteread);
						}
						fs.close();
						inStream.close();
						
			     }else{
			    	 //Linux系统
			    	 newPathFile = newPathFile.replace("/", props.getProperty("file.separator"));
						String s = newPathFile.substring(0, newPathFile.lastIndexOf(props.getProperty("file.separator"))+1);
						File fpath = new File(s);
						if(!fpath.exists()){
							fpath.mkdirs();
							//new FileHelper().createfiles(newPathFile);					
						}
						InputStream inStream = new FileInputStream(oldfile.getPath()); // 读入原文件
						FileOutputStream fs = new FileOutputStream(newPathFile);
						byte[] buffer = new byte[1444];
						while ((byteread = inStream.read(buffer)) != -1) {
							bytesum += byteread; // 字节数 文件大小
							// System.out.println(bytesum);
							fs.write(buffer, 0, byteread);
						}
						fs.close();
						inStream.close();
			     }
			}
			return bytesum;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	/**
	 * @param oldfile
	 * @return 计算文件大小
	 * @throws IOException
	 */
	public static int getFileSize(File oldfile) throws IOException {
		int bytesum = 0;
		if (oldfile.exists()) {
			FileInputStream fis = new FileInputStream(oldfile);
			bytesum = fis.available();
		}
		/*
		 * int byteread = 0; try { if (oldfile.exists()) { // 文件存在时 InputStream
		 * inStream = new FileInputStream(oldfile.getPath()); // 读入原文件 byte[]
		 * buffer = new byte[1444]; while ((byteread = inStream.read(buffer)) !=
		 * -1) { bytesum += byteread; // 字节数 文件大小 //System.out.println(bytesum);
		 * } inStream.close();
		 * 
		 * } } catch (Exception e) { e.printStackTrace(); return 0; }
		 */
		return bytesum;
	}

	/**
	 * 复制整个文件夹的内容
	 * 
	 * @param oldPath
	 *            准备拷贝的目录
	 * @param newPath
	 *            指定绝对路径的新目录
	 * @return
	 */
	public void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			if(a != null){
				for (int i = 0; i < file.length; i++) {
					if (oldPath.endsWith(File.separator)) {
						temp = new File(oldPath + file[i]);
					} else {
						temp = new File(oldPath + File.separator + file[i]);
					}
					if (temp.isFile()) {
						FileInputStream input = new FileInputStream(temp);
						FileOutputStream output = new FileOutputStream(newPath
								+ "/" + (temp.getName()).toString());
						byte[] b = new byte[1024 * 5];
						int len;
						while ((len = input.read(b)) != -1) {
							output.write(b, 0, len);
						}
						output.flush();
						output.close();
						input.close();
					}
					if (temp.isDirectory()) {// 如果是子文件夹
						copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动目录
	 * 
	 * @param oldPath
	 * @param newPath
	 * @return
	 */
	public void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	public static void main(String[] args) {
		//System.out.println(fileHelper.getToFileName("afa.xls"));
		FileHelper.delFile("E:/eclipse/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/CatMvc/ueditor/jsp/upload/file/20160719/1468914401583019121.jpg");
        System.out.println("--------------------------");
	}

	/**
	 * 上传文件后需要重命名文件
	 * 
	 * @param upload_file_name
	 * @return
	 */
	public static String getToFileName(String upload_file_name) {
		int idx = upload_file_name.lastIndexOf(".");
		String to_file_name = upload_file_name.substring(0, idx)
				+ DateHelper.getToday("yyyyMMddHHmmss") + "."
				+ upload_file_name.substring(idx + 1);
		return to_file_name;
	}

	/**
	 * 下载
	 * 
	 * @param filePath
	 * @param filename
	 * @param response
	 * @return
	 */
	public static boolean downloadFile(String filePath, String filename,
			HttpServletResponse response) {
		InputStream fis = null;
		OutputStream toClient = null;
		// if (null != filePath && filePath.indexOf(".") != -1) {
		// filename = filename + filePath.substring(filePath.lastIndexOf("."),
		// filePath.length());
		// }
		File file = new File(filePath);
		if (file.exists()) {
			try {
				fis = new BufferedInputStream(new FileInputStream(filePath));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				response.reset();
				response.addHeader("Content-Disposition",
						"attachment;filename="
								+ new String(filename.getBytes(), "iso8859-1"));
				// 设置返回的文件类型
				response.addHeader("Content-Length", "" + file.length());
				toClient = new BufferedOutputStream(response.getOutputStream());
				//response.setContentType("application/vnd.ms-excel");
				
				// 输出数据
				toClient.write(buffer);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fis) {
						fis.close();
					}
					if (null != toClient) {
						toClient.flush();
						toClient.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			return false;
		}
		return false;
	}
	
	   public void zip(String inputFileName,String zipFileName) throws Exception {
	        System.out.println(zipFileName);
	        zip(zipFileName, new File(inputFileName));
	    }

	    private void zip(String zipFileName, File inputFile) throws Exception {
	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
	        zip(out, inputFile, "");
	        System.out.println("zip done");
	        out.close();
	    }

	    private void zip(ZipOutputStream out, File f, String base) throws Exception {
	        if (f.isDirectory()) {
	           File[] fl = f.listFiles();
	           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
	           base = base.length() == 0 ? "" : base + "/";
	           for (int i = 0; i < fl.length; i++) {
	           zip(out, fl[i], base + fl[i].getName());
	         }
	        }else {
	           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
	           FileInputStream in = new FileInputStream(f);
	           int b;
	           System.out.println(base);
	           while ( (b = in.read()) != -1) {
	            out.write(b);
	         }
	         in.close();
	       }
	    }
	    
	    public void createfiles(String   path){
	    	     StringTokenizer   st=new   StringTokenizer(path,"/");   
	    	     String   path1=st.nextToken()+"/";   
	    	     String   path2 =path1;   
	    	     while(st.hasMoreTokens())   
	    	     {   
	    	          path1=st.nextToken()+"/";   
	    	           path2+=path1;   
	    	           File inbox   =   new File(path2);   
	    	           if(!inbox.exists())   
	    	                inbox.mkdir();   
	    	     }  
	    }
	    
	    
	    
	    
	    public static String readInputStream(String filePathAndName, String encoding)
		throws IOException {
			encoding = encoding.trim();
			StringBuffer str = new StringBuffer("");
			String st = "";
			try {
				FileInputStream fs = new FileInputStream(filePathAndName);
				InputStreamReader isr;
				if (encoding.equals("")) {
					isr = new InputStreamReader(fs);
				} else {
					isr = new InputStreamReader(fs, encoding);
				}
				BufferedReader br = new BufferedReader(isr);
				try {
					String data = "";
					while ((data = br.readLine()) != null) {
						str.append(data + " ");
					}
				} catch (Exception e) {
					str.append(e.toString());
				}
				st = str.toString();
			} catch (IOException es) {
				st = "";
			}
			return st;
          }
	    
	    
	    
	    public static boolean downloadFile2(String filePath, String filename,
				HttpServletResponse response) {
			InputStream fis = null;
			OutputStream toClient = null;
			// if (null != filePath && filePath.indexOf(".") != -1) {
			// filename = filename + filePath.substring(filePath.lastIndexOf("."),
			// filePath.length());
			// }
			File file = new File(filePath);
			if (file.exists()) {
				try {
					fis = new BufferedInputStream(new FileInputStream(filePath));
					byte[] buffer = new byte[fis.available()];
					fis.read(buffer);
					response.reset();
					response.addHeader("Content-Disposition",
							"attachment;filename="
									+ new String(filename.getBytes(), "iso8859-1"));
					// 设置返回的文件类型
					response.addHeader("Content-Length", "" + file.length());
					toClient = new BufferedOutputStream(response.getOutputStream());
					//response.setContentType("application/vnd.ms-excel");
					response.setContentType("goldweb");
					
					// 输出数据
					toClient.write(buffer);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (null != fis) {
							fis.close();
						}
						if (null != toClient) {
							toClient.flush();
							toClient.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				return false;
			}
			return false;
		}
	    
	    public static String write(String context, String path) {  
	    	try {  
	    	    /* 创建写入对象 */
	    	    FileOutputStream fis=new FileOutputStream(path);
	    		OutputStreamWriter writer=new OutputStreamWriter(fis, "UTF-8");
	    		/* 写入字符串 */ 
	    		writer.write(context);
	    	    /* 关掉对象 */  
	    	    writer.close();  
	    	    return "1"; 
	    	   } catch (IOException e) {  
	    	    e.printStackTrace();
	    	    return "0";
	    	   }  
	    	}
	    
	    
	    
	    public static String writeMany(String context, String[] path) {  
	    	try {
	    		for(int i=0;i<path.length;i++){
	    	    /* 创建写入对象 */
	    	    FileOutputStream fis=new FileOutputStream(path[i]);
	    		OutputStreamWriter writer=new OutputStreamWriter(fis, "UTF-8");
	    		/* 写入字符串 */ 
	    		writer.write(context);
	    	    /* 关掉对象 */  
	    	    writer.close(); 
	    		}
	    	    return "1"; 
	    	   } catch (IOException e) {  
	    	    e.printStackTrace();
	    	    return "0";
	    	   }  
	    	}

		public static int copyFileIo(InputStream inStream, String newPathFile) {
			try {
				int bytesum = 0;
				int byteread = 0;
				//if (oldfile.exists()) { // 文件存在时
					Properties props=System.getProperties();
					 //System.out.println("操作系统的名称："+props.getProperty("os.name"));
				    // System.out.println("操作系统的构架："+props.getProperty("os.arch"));
				   //  System.out.println("操作系统的版本："+props.getProperty("os.version"));
				    // System.out.println("文件分隔符："+props.getProperty("file.separator"));   //在 unix 系统中是”／”
				   //  System.out.println("路径分隔符："+props.getProperty("path.separator"));   //在 unix 系统中是”:”
				  //   System.out.println("行分隔符："+props.getProperty("line.separator"));   //在 unix 系统中是”/n”
				     String osname = props.getProperty("os.name");
				     if(osname.indexOf("Window")>-1){
				    	 //window系统
				    	 newPathFile = newPathFile.replace("/", "\\");
							String s = newPathFile.substring(0, newPathFile.lastIndexOf("\\")+1);
							File fpath = new File(s);
							if(!fpath.exists()){
								fpath.mkdirs();
								//new FileHelper().createfiles(newPathFile);					
							}
							//InputStream inStream = new FileInputStream(oldfile.getPath()); // 读入原文件
							FileOutputStream fs = new FileOutputStream(newPathFile);
							byte[] buffer = new byte[1444];
							while ((byteread = inStream.read(buffer)) != -1) {
								bytesum += byteread; // 字节数 文件大小
								// System.out.println(bytesum);
								fs.write(buffer, 0, byteread);
							}
							fs.flush();
							fs.close();
							inStream.close();
							
				     }else{
				    	 //Linux系统
				    	 newPathFile = newPathFile.replace("/", props.getProperty("file.separator"));
							String s = newPathFile.substring(0, newPathFile.lastIndexOf(props.getProperty("file.separator"))+1);
							File fpath = new File(s);
							if(!fpath.exists()){
								fpath.mkdirs();
								//new FileHelper().createfiles(newPathFile);					
							}
							//InputStream inStream = new FileInputStream(oldfile.getPath()); // 读入原文件
							FileOutputStream fs = new FileOutputStream(newPathFile);
							byte[] buffer = new byte[1444];
							while ((byteread = inStream.read(buffer)) != -1) {
								bytesum += byteread; // 字节数 文件大小
								// System.out.println(bytesum);
								fs.write(buffer, 0, byteread);
							}
							fs.flush();
							fs.close();
							inStream.close();
				     }
				//}
				return bytesum;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		} 
}
