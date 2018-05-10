package com.t201.cj.dao.datadictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.t201.cj.pojo.DataDictionary;

public interface DataDictionaryMapper {
	/**
	 * 根据类型编码查询字典表
	 * @param typeCode
	 * @return
	 */
	public List<DataDictionary> getDataDictionaries(@Param("typeCode")String typeCode);
}
