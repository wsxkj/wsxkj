package com.zpj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.Customer;
import com.zpj.sys.entity.Level;
import com.zpj.sys.service.LevelService;
@Service
public class LevelServiceImpl implements LevelService {
	@Autowired
    private BaseDao<Level> levelDao;
    private String tablename="sys_level";
    
	public MyPage findPageData(Map canshu, Integer page, Integer limit) {
        Map param=new HashMap();
        Map px=new HashMap();
        px.put("level", "asc");
        return levelDao.findPageDateSqlT(tablename,"", param,px , page, limit, Level.class);
    }

    public void saveInfo(Level level){
	    if(null!=levelDao.get(level.getId(),Level.class)){
            levelDao.merge(level,level.getId());
        }else{
            levelDao.add(level);
        }
    }
    
    public Level findInfoById(Integer level){
    	List<Level> list=levelDao.findBySqlT("select * from "+tablename+" where level="+level, Level.class);
    	if(null!=list&&list.size()>0){
    		return list.get(0);
    	}else{
    		return null;
    	}
    }
}
