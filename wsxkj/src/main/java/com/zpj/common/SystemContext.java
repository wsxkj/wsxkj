package com.zpj.common;

/** 
 * 用来传递列表对象的ThreadLocal数据 
 * 这里注意getWebClassesPath这个方法的注释！！！！ 
 * @author Administrator 
 * 
 */  
public class SystemContext {  
    /** 
     * 分页大小 
     */  
    private static ThreadLocal<Integer> pageSize = new ThreadLocal<Integer>();  
    /** 
     * 分页的起始页 
     */  
    private static ThreadLocal<Integer> pageOffset = new ThreadLocal<Integer>();  
    /** 
     * 列表的排序字段 
     */  
    private static ThreadLocal<String> sort = new ThreadLocal<String>();  
    /** 
     * 列表的排序方式 
     */  
    private static ThreadLocal<String> order = new ThreadLocal<String>();  
      
    public static String getWebClassesPath() {  
        //获取path的另一种方式，不过均要检验web项目是否到web-inf/class下面。  
        //可能直接到达target下的class，这里网上提出  
        //项目---》property---》java build path---》source---》 outputfile  
        //但是maven项目是否没有作用。这里不知是要在哪里设置？？？？？？  
        //return getClass().getProtectionDomain().getCodeSource().getLocation().getPath();  
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();   
            
        }  
      
    private String getWebInfPath() throws IllegalAccessException{  
           String path = getWebClassesPath();  
           if (path.indexOf("WEB-INF") > 0) {  
            path = path.substring(0, path.indexOf("WEB-INF")+8);  
           } else {  
            throw new IllegalAccessException("路径获取错误");  
           }  
           return path;  
        }  
  
        private String getWebRoot() throws IllegalAccessException{  
           String path = getWebClassesPath();  
           if (path.indexOf("WEB-INF") > 0) {  
            path = path.substring(0, path.indexOf("WEB-INF/classes"));  
           } else {  
            throw new IllegalAccessException("路径获取错误");  
           }  
           return path;  
        }  
      
    public static Integer getPageSize() {  
        return pageSize.get();  
    }  
    public static void setPageSize(Integer _pageSize) {  
        pageSize.set(_pageSize);  
    }  
    public static Integer getPageOffset() {  
        return pageOffset.get();  
    }  
    public static void setPageOffset(Integer _pageOffset) {  
        pageOffset.set(_pageOffset);  
    }  
    public static String getSort() {  
        return sort.get();  
    }  
    public static void setSort(String _sort) {  
        SystemContext.sort.set(_sort);  
    }  
    public static String getOrder() {  
        return order.get();  
    }  
    public static void setOrder(String _order) {  
        SystemContext.order.set(_order);  
    }  
      
    public static void removePageSize() {  
        pageSize.remove();  
    }  
      
    public static void removePageOffset() {  
        pageOffset.remove();  
    }  
      
    public static void removeSort() {  
        sort.remove();  
    }  
      
    public static void removeOrder() {  
        order.remove();  
    }  
      
}  