package com.zpj.common;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * @ClassName: UUIDGenerator
 * @Description: TODO(UUID的生成类)
 * @author Lee
 * @date 2014-2-10 下午3:06:16
 */
public final class UUIDGenerator {
 
	/**
	 * @Title generate36UUID
	 * @Description 生成36位的UUID
	 * @return String
	 * @author Lee
	 * @time 2014-2-10 下午3:06:52
	 */
	synchronized final public static String generate36UUID(){
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	/**
	 * @Title generate32UUID
	 * @Description 生成32位的UUID
	 * @return String
	 * @author Lee
	 * @time 2014-2-10 下午3:10:08
	 */
	synchronized final public static String generate32UUID(){
		return UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}
	synchronized final public static String generatePkold(String type){
		return type.toUpperCase()+"_"+UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}
	
	private static byte[] lock = new byte[0];
	 
	// 位数，默认是8位
	private final static long w = 100000;
 
	public static String createID() {
		long r = 0;
		synchronized (lock) {
			r = (long) ((Math.random() + 1) * w);
		}
 
		return System.currentTimeMillis() + String.valueOf(r).substring(1);
	}
	
	public static Lock mylock=new ReentrantLock();
	
	public static String generatePk(String prefix){
		  //if(prefix.length()>4) prefix=prefix.substring(0, 4);
		  String num="";
		  mylock.lock();
		  try{
			  long ctm=System.currentTimeMillis();
			  ThreadLocal<Long> tl=new ThreadLocal<Long>();
			  tl.set(System.currentTimeMillis());
			  int year=new Timestamp(ctm).getYear();
			  int month=(new Timestamp(ctm).getMonth()+1);
			  int day=new Timestamp(ctm).getDate();
			  int hours=new Timestamp(ctm).getHours();
			  int minte=new Timestamp(ctm).getMinutes();
			  long time=new Timestamp(ctm).getTime();
			  num=String.valueOf(year).substring(1)+String.valueOf(month)+String.valueOf(day)
			  +String.valueOf(hours)+String.valueOf(minte)+String.valueOf(time).substring(8, 13);
		  }catch (Exception e) {
			System.out.println("生成主键异常"+prefix);
			e.printStackTrace();
		  }finally{
			  mylock.unlock();
		  }
		  
		 
		
		
		  return prefix.toUpperCase()+"_"+num;
	}
	
	
	public static void main(String[] args) {
		System.out.println(generatePk("nihao"));
	}
}
