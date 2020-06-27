package com.zpj.common;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.Random;

public class MsgUtil {
    public static String REGIONID="cn-hangzhou";
    public static String ACCESSKEYID="";
    public static String SECRET="";


	public static void main(String[] args) {
        MsgUtil.sendMsg("8888","");
	}

	public static void sendMsg(String yzm,String phone){
        DefaultProfile profile = DefaultProfile.getProfile(REGIONID, ACCESSKEYID, SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "微商小会计");
        request.putQueryParameter("TemplateCode", "SMS_177242862");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+yzm+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    /*
     * @MethodName: generateYzm
     * @Description: TODO(随机生成4位验证码)
     * @params []
     * @return java.lang.String
     * @author zpj
     * @date 2019/4/17 15:08
    */
	public static String generateYzm(){

        String str="0123456789";
        StringBuilder uuid=new StringBuilder(5);
        for(int i=0;i<4;i++) {
            uuid.append(str.charAt(new Random().nextInt(str.length())));
        }
        return uuid.toString();
    }
	
	public static String generatePassword(int length){

        String str="0123456789";
        StringBuilder uuid=new StringBuilder(7);
        for(int i=0;i<length;i++) {
            uuid.append(str.charAt(new Random().nextInt(str.length())));
        }
        return uuid.toString();
    }
}
