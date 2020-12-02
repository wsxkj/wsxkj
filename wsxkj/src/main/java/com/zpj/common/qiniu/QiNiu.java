package com.zpj.common.qiniu;

import com.google.gson.Gson;
import com.qiniu.cdn.CdnManager;
import com.qiniu.cdn.CdnResult;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.zpj.common.PropertyHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class QiNiu {
    private Logger log = LoggerFactory.getLogger(QiNiu.class);
    private static String accessKey ="";
    private static String secretKey ="";
    private static String bucket ="";
    private static Auth auth;
    private static Configuration cfg;
    private static String domainName="";
    private QiNiu(){
        Map params= PropertyHelper.getPhonePropertiesValue();
        accessKey=(String)params.get("accessKey");
        secretKey=(String)params.get("secretKey");
        bucket=(String)params.get("bucket");
        domainName=(String)params.get("domainName");
        auth = Auth.create(accessKey, secretKey);
        //构造一个带指定 Region 对象的配置类
        cfg = new Configuration(Region.region0());//华东
        cfg.useHttpsDomains = false;
    }

    public static void main(String[] args) {
//        System.out.println(QiNiu.getInstance().getToken());
        //EZlzAk3AQWJwTKdgpVEYdmSZQFR9y0HgqRUcJfri:oTVfQvM-SeZtHbW1biQoXIizMgY=:eyJzY29wZSI6IndzeGtqIiwiZGVhZGxpbmUiOjE2MDYyNzI4NjZ9
//        long deadline = System.currentTimeMillis() / 1000 + 3600;
//        1606273043
        String token=QiNiu.getInstance().getDownloadUrl("my-java.png");
        System.out.println(token);
    }


    /**
     *@MethodName uploadPathFile
     *@Description (TODO 上传本地文件，通过filepath路径读取)
     *@Params [filePath, key]
     *@Return java.util.Map<java.lang.String,java.lang.Object>
     *@Author zpj
     *@Date  2020/11/24 9:53
     **/
    public Map<String, Object> uploadPathFile(String filePath, String key){
        Map<String, Object> result = new HashMap<String, Object>();

        //...生成上传凭证，然后准备上传
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String upToken = getToken(key);
        try {
            Response response = getUploadManager().put(filePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            result.put("hash", putRet.hash);
            result.put("key", putRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("uploadPathFile Exception:" + ex.getMessage());
            result.put("hash", "");
            result.put("key", "");
        }
        return result;
    }
    public Map<String, Object> uploadFile(File file, String key){
        Map<String, Object> result = new HashMap<String, Object>();

        String upToken = getToken(key);
        try {
            Response response = getUploadManager().put(file,key,upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);

            result.put("hash", putRet.hash);
            result.put("key", putRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("uploadPathFile Exception:" + ex.getMessage());
            result.put("hash", "");
            result.put("key", "");
        }
        return result;
    }
    /**
     *@MethodName uploadByteFile
     *@Description (TODO 上传字节数组对象)
     *@Params [uploadBytes, key]
     *@Return java.util.Map<java.lang.String,java.lang.Object>
     *@Author zpj
     *@Date  2020/11/24 9:55
     **/
    public Map<String, Object> uploadByteFile(byte[] uploadBytes, String key){
        Map<String, Object> result = new HashMap<String, Object>();
        String upToken = getToken(key);
        try {
            Response response = getUploadManager().put(uploadBytes, key, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            result.put("hash", putRet.hash);
            result.put("key", putRet.key);
        } catch (QiniuException e) {
            e.printStackTrace();
            log.error("uploadByteFile Exception:" + e.getMessage());
            result.put("hash", "");
            result.put("key", "");
        }
        return result;
    }
    /**
     *@MethodName asyncUploadByteFile
     *@Description (TODO 异步字节数组上传  暂时不用)
     *@Params [uploadBytes, key]
     *@Return java.util.Map<java.lang.String,java.lang.Object>
     *@Author zpj
     *@Date  2020/11/24 11:43
     **/
    public Map<String, Object> asyncUploadByteFile(byte[] uploadBytes, String key){
        Map<String, Object> result = new HashMap<String, Object>();
//        String upToken = getToken(key);
        StringMap putPolicy = new StringMap();
        //回调的地址
        putPolicy.put("callbackUrl", "http://域名/common/qiniu/upload/callback");
        putPolicy.put("callbackBody", "filename=$(fname)&filesize=$(fsize)&mimeType=$(mimeType)");
        putPolicy.put("callbackBodyType", "application/json");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
        try {
            Response response = getUploadManager().put(uploadBytes,key,upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            result.put("hash", putRet.hash);
            result.put("key", putRet.key);
        } catch (QiniuException e) {
            e.printStackTrace();
            log.error("uploadByteFile Exception:" + e.getMessage());
            result.put("hash", "");
            result.put("key", "");
        }
        return result;
    }
    /**
     *@MethodName uploadInputStreamFile
     *@Description (TODO 上传字节流数据对象)
     *@Params [fileInputStream, key]
     *@Return java.util.Map<java.lang.String,java.lang.Object>
     *@Author zpj
     *@Date  2020/11/24 10:00
     **/
    public Map<String, Object> uploadInputStreamFile(InputStream fileInputStream, String key){
        Map<String, Object> result = new HashMap<String, Object>();
        String upToken = getToken(key);
        try {
            Response response = getUploadManager().put(fileInputStream, key, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            result.put("hash", putRet.hash);
            result.put("key", putRet.key);
        } catch (QiniuException e) {
            log.error("uploadInputStramFile Exception:" + e.getMessage());
            e.printStackTrace();
            result.put("hash", "");
            result.put("key", "");
        }
        return  result;
    }


    /**
     *@MethodName getDownloadUrl
     *@Description (TODO 获取下载路径地址（文件远程访问地址）)
     *@Params [key]
     *@Return java.lang.String
     *@Author zpj
     *@Date  2020/11/24 10:26
     **/
    public String getDownloadUrl(String key) {
        String finalUrl="";
        String downloadRUL = String.format("%s/%s", domainName, key);
		long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
		finalUrl = auth.privateDownloadUrl(downloadRUL, expireInSeconds);
        return finalUrl;
    }
    /**
     *@MethodName getSimpleDownloadUrl
     *@Description (TODO 获取单纯的域名加key的路径)
     *@Params [key]
     *@Return java.lang.String
     *@Author zpj
     *@Date  2020/11/25 11:41
     **/
    public String getSimpleDownloadUrl(String key){
        String finalUrl="";
        try {
            String encodeKey = URLEncoder.encode(key, "utf-8");
            finalUrl = String.format("%s/%s", domainName, encodeKey);
        } catch (UnsupportedEncodingException e) {
            log.error("getDownloadUrl exception:" + e.getMessage());
            e.printStackTrace();
        }
        return finalUrl;
    }
    /**
     *@MethodName getDownloadUrlSelf
     *@Description (TODO 根据私有资源规则 自己生成查看链接)
     *@Params [key]
     *@Return java.lang.String
     *@Author zpj
     *@Date  2020/11/25 9:43
     **/
//    public String generateDownloadUrlBySelf(String key,long expires) {
//        String finalUrl="";
//        try {
//            String encodeKey = URLEncoder.encode(key, "utf-8");
//            String downloadRUL = String.format("%s/%s", YUMING, encodeKey);
//            long deadline = System.currentTimeMillis() / 1000 + expires;
//            finalUrl = downloadRUL+"/"+key+"?e="+deadline+"&token="+getToken();
//        } catch (UnsupportedEncodingException e) {
//            log.error("getDownloadUrl exception:" + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return finalUrl;
//    }
    /**
     *@MethodName getFileInfo
     *@Description (TODO 获取文件信息)
     *@Params [key]
     *@Return java.util.Map<java.lang.String,java.lang.Object>
     *@Author zpj
     *@Date  2020/11/24 10:27
     **/
    public Map<String, Object> getFileInfo(String key){
        Map<String, Object> result = new HashMap<String, Object>();
        try{
            FileInfo fileInfo = getBucketManager().stat(bucket, key);
            result.put("hash",fileInfo.hash);
            result.put("fsize",fileInfo.fsize);
            result.put("mimeType",fileInfo.mimeType);
            result.put("putTime",fileInfo.putTime);
        }catch (QiniuException e){
            log.error("getFileInfo exception:" + e.getMessage());
            e.printStackTrace();
            result.put("hash","");
            result.put("fsize","");
            result.put("mimeType","");
            result.put("putTime","");
        }
        return result;
    }
    /**
     *@MethodName deleteFile
     *@Description (TODO  根据key值删除文件信息)
     *@Params [key]
     *@Return boolean
     *@Author zpj
     *@Date  2020/11/24 10:48
     **/
    public boolean deleteFile(String key) {
        try{
            Response response=getBucketManager().delete(bucket, key);
            if (null == response || 200 != response.statusCode) {
                return false;
            }
            return true;
        }catch (QiniuException e){
            e.printStackTrace();
            log.error("deleteFile exception:" + e.getMessage());
            return false;
        }
    }
    /**
     *@MethodName getDomains
     *@Description (TODO 获取空间)
     *@Params []
     *@Return java.lang.String[]
     *@Author zpj
     *@Date  2020/11/24 10:47
     **/
    public String[] getDomains() {
        try {
            String[] domainLists = getBucketManager().domainList(bucket);

            if (null == domainLists || 0 == domainLists.length) {
                return null;
            }
            return domainLists;
        } catch (Exception e) {
            log.error("getDomains Exception:" + e.getMessage());
            return null;
        }
    }
    /**
     *@MethodName fileRefresh
     *@Description (TODO 覆盖后需要调用刷新文件，否则看到的还是原来的 ,刷新的链接不能超过100个)
     *@Params [keys]
     *@Return boolean
     *@Author zpj
     *@Date  2020/11/24 11:06
     **/
    public boolean fileRefresh(String keys)  {
        String[] filesUrls=keys.split(",");
        for (int i=0;i<filesUrls.length;i++){
            filesUrls[i]=domainName+filesUrls[i];
        }
        try{
            CdnResult.RefreshResult result = getCdnManager().refreshUrls(filesUrls);
            if (null == result || 200 != result.code) {
                return false;
            }
            return true;
        }catch (QiniuException e){
            e.printStackTrace();
            log.error("fileRefresh Exception:" + e.getMessage());
            return false;
        }

    }

    /**
     *@MethodName getToken
     *@Description (TODO 获取token信息，key如果不传，则是添加，传有则覆盖)
     *@Params [key]
     *@Return java.lang.String
     *@Author zpj
     *@Date  2020/11/24 10:39
     **/
    public String getToken(String key){
        if(StringUtils.isEmpty(key)){
            return auth.uploadToken(bucket);
        }
        return auth.uploadToken(bucket, key);
    }
    public String getToken(){
       return auth.uploadToken(bucket);
    }

    private UploadManager getUploadManager(){
        UploadManager uploadManager= new UploadManager(cfg);
        return uploadManager;
    }

    private BucketManager getBucketManager() {
        BucketManager bucketManager = new BucketManager(auth, cfg);
        return bucketManager;
    }

    private CdnManager getCdnManager() {
        CdnManager cdnManager = new CdnManager(auth);
        return cdnManager;
    }

    /**
     * 封装单例的静态内部类
     */
    private static class Singleton {
        private static QiNiu instance;

        static {
            instance = new QiNiu();
        }

        public static QiNiu getInstance() {
            return instance;
        }

    }

    public static QiNiu getInstance() {
        return QiNiu.Singleton.getInstance();
    }

}
