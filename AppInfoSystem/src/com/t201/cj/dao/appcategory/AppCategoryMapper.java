package com.t201.cj.dao.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.t201.cj.pojo.AppCategory;

public interface AppCategoryMapper {
	/**
	 * 根据父类id查询分类名称
	 * @param parentId
	 * @return
	 */
	public List<AppCategory> getAppCategories(@Param("parentId")Integer parentId);
}
