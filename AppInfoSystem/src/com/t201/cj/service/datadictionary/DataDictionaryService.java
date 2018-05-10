package com.t201.cj.service.datadictionary;

import java.util.List;

import com.t201.cj.pojo.DataDictionary;

public interface DataDictionaryService {
	/**
	 * 根据类型编码查询字典表
	 * @param typeCode
	 * @return
	 */
	public List<DataDictionary> getDataDictionaries(String typeCode);
}
