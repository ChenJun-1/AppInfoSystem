package com.t201.cj.service.appcategory;

import java.util.List;

import com.t201.cj.pojo.AppCategory;

public interface AppCategoryService {
	/**
	 * 根据父类id查询分类名称
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> getAppCategories(Integer parentId);
}
