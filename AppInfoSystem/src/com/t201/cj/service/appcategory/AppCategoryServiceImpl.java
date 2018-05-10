package com.t201.cj.service.appcategory;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.t201.cj.dao.appcategory.AppCategoryMapper;
import com.t201.cj.pojo.AppCategory;

@Service
public class AppCategoryServiceImpl implements AppCategoryService {
	
	@Resource
	private AppCategoryMapper appCategoryMapper;
	
	@Override
	public List<AppCategory> getAppCategories(Integer parentId) {
		return appCategoryMapper.getAppCategories(parentId);
	}
	
}
