package com.zpj.materials.service;

import java.util.List;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.Supplier;

public interface SupplierService {
	MyPage findPageData(String username, Integer page, Integer limit);
	
	void saveInfo(Supplier info);

	void delete(String deleteID);
	
	public Supplier findById(int id);
	
	
	public List findSupplier();
}
