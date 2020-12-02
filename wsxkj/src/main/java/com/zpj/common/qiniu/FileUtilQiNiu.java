package com.zpj.common.qiniu;

import com.zpj.common.FileUtil;
import com.zpj.common.SpringContext;
import com.zpj.sys.service.UploadfileService;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Properties;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 *@ClassName FileUtilQiNiu
 *@Description (TODO  区别于服务器本地备份功能)
 *@Author zpj
 *@Version 1.0
 *@Date  2020/11/25 9:27
**/
public class FileUtilQiNiu {
    //文件备份操作延时
    private final int OPERATE_DELAY_TIME = 1000;

    //文件备份操作线程池
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

    public UploadfileService uploadfileService=null;

    private FileUtilQiNiu() {
    }
    private static FileUtilQiNiu fu=new FileUtilQiNiu();

    public static FileUtilQiNiu getInstance(){
        if(fu==null){
            fu=new FileUtilQiNiu();
        }
        return fu;
    }

    /**
     * 拷贝文件
     * @Title copyFile
     * @param src
     * @param target
     * @throws IOException
     * @author zpj
     * @time 2019年12月17日 下午1:20:36
     */
    private void copyFile(File src, File target) throws IOException {
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
     /**
      *@MethodName downLoadFile
      *@Description (TODO)
      *@Params [url, destFilePath]
      *@Return void 
      *@Author zpj
      *@Date  2020/11/25 10:23
      **/
    public void downLoadFile(String url,File destFilePath){
        RequestConfig requestConfig= RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
        CloseableHttpClient client= HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        CloseableHttpResponse response=null;
        HttpEntity entity=null;
        try {
            response= client.execute(new HttpGet(new URL(url).toURI()));
            entity=response.getEntity();
            entity.writeTo(new FileOutputStream(destFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }finally {
            try {
                if(response!=null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) throws IOException {
//        File a=new File("D://test/jflyfox/bbs/article_file/20180912_143719_248315.txt");
//        File b=new File("D://test/fa/1.txt");
////		copyFile(a,b);
//    }


    /**
     * 备份附件
     * @Title backup
     * @param sourcePath 项目路径到ueditor/userid
     * @param fileName  文件名称
     * @return
     * @author zpj
     * @time 2019年12月17日 下午3:09:08
     */
    private boolean backup(String key) {
        Long start=System.currentTimeMillis();
        System.out.println("备份文件开始");
        Properties props=System.getProperties();
        String osname = props.getProperty("os.name");
        StringBuffer backupPath = new StringBuffer(50); // 备份路径
        String fileName=key.substring(key.lastIndexOf("/"),key.length());
        if(osname.indexOf("Window")>-1){
            backupPath.append("D://test/ueditor/"+key.substring(0,key.lastIndexOf("/")));
        }else{
            backupPath.append("/opt/backup/ueditor/"+key.substring(0,key.lastIndexOf("/")));
        }
        File existBackupPath=new File(backupPath.toString());
        File destFile = new File(backupPath+"/"+fileName);
        //判断备份文件夹是否存在，不存在就创建
        if(!existBackupPath.exists()){
            existBackupPath.mkdirs();
        }
        downLoadFile(QiNiu.getInstance().getDownloadUrl(key),destFile);
        Long end=System.currentTimeMillis();
        System.out.println("备份文件结束,耗时："+(end-start)/1000+"毫秒");
        return true;
    }

    public static void main(String[] args) {
//    	String name="USER_19127195302259/20201127201331339.jpg";
//    	String name="/ueditor/20201127201331339.jpg";
    	String name="/20201127201331339.jpg";
    	if(name.startsWith("/ueditor/")){
        	//老数据存的是/ueditor/
    		name=name.substring(9);
        }else if(name.startsWith("/")){
        	//处理路径中可能存在的/问题
        	name=name.substring(1);
        }
    	System.out.println(name);
	}
    
    /**
     * 还原备份的文件
     * @Title restore
     * @param restorePathAndName   sys_uploadfile表中filrUrl 带路径和项目名
     * @return
     * @author zpj
     * @time 2019年12月17日 下午3:12:29
     */
    public String restore(String restorePathAndName) {
        Long start=System.currentTimeMillis();
        System.out.println("构造访问文件路径开始");
        if(restorePathAndName.startsWith("/ueditor/")){
        	//老数据存的是/ueditor/
        	restorePathAndName=restorePathAndName.substring(9);
        }else if(restorePathAndName.startsWith("/")){
        	//处理路径中可能存在的/问题
        	restorePathAndName=restorePathAndName.substring(1);
        }
        String url=QiNiu.getInstance().getDownloadUrl(restorePathAndName);
        Long end=System.currentTimeMillis();
        System.out.println(url);
        System.out.println("构造访问文件路径结束,耗时："+(end-start)/1000+"毫秒");
        return url;
    }
    public void backupFile(String file_id,String key) {
        executor.schedule(this.backupFile1(file_id,key), OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }
    private TimerTask backupFile1(final String file_id,final String key) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    if(null==uploadfileService){
                        uploadfileService=(UploadfileService) SpringContext.getBean("uploadfileService");
                    }
                    boolean flag=backup(key);
                    uploadfileService.updateBackupField(file_id, flag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
