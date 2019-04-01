package com.zpj.common;
import java.util.List;  

/** 
 * 分页对象 
 * @author Administrator 
 * 
 * @param <T> 
 */  
public class Pager<T> {  
    /** 
     * page
     */  
    private int page;  
    /** 
     * 分页的起始页 
     */  
    private int start;  
    /**
     * pagesize
     */
    private int limit;
    /** 
     * 总记录数 
     */  
    private long total;  
    /** 
     * 分页的数据 
     */  
    private List<T> d;  
      
   
    public long getTotal() {  
        return total;  
    }  
    public void setTotal(long total) {  
        this.total = total;  
    }  
    public List<T> getDatas() {  
        return d;  
    }  
    public void setDatas(List<T> datas) {  
        this.d = datas;  
    }
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}  
}  