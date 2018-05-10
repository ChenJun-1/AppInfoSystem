package com.t201.cj.service.datadictionary;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.t201.cj.dao.datadictionary.DataDictionaryMapper;
import com.t201.cj.pojo.DataDictionary;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {
	
	@Resource
	private DataDictionaryMapper dataDictionaryMapper;
	
	@Override
	public List<DataDictionary> getDataDictionaries(String typeCode) {
		return dataDictionaryMapper.getDataDictionaries(typeCode);
	}

}
