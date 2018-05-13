package com.t201.cj.service.appinfo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.t201.cj.dao.appinfo.AppInfoMapper;
import com.t201.cj.pojo.AppInfo;

@Service
public class AppInfoServiceImpl implements AppInfoService {
	
	@Resource
	private AppInfoMapper appInfoMapper;

	@Override
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryFlatformId, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3) {
		return appInfoMapper.getAppInfoCount(querySoftwareName, queryStatus,
				queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, 
				queryCategoryLevel3);
	}

	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryFlatformId,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer devId, Integer currentPageNo,
			Integer pageSize) {
		
		currentPageNo = (currentPageNo - 1) * pageSize;
		return appInfoMapper.getAppInfoList(querySoftwareName, queryStatus,
				queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, 
				queryCategoryLevel3, devId, currentPageNo, pageSize);
	}

	@Override
	public boolean addAppInfo(AppInfo appInfo) {
		return appInfoMapper.addAppInfo(appInfo) == 1 ? true : false;
	}

	@Override
	public AppInfo getAppInfo(Integer id, String APKName) {
		return appInfoMapper.getAppInfo(id, APKName);
	}

	@Override
	public boolean delAppLOGO(Integer id) {
		return appInfoMapper.delAppLOGO(id) == 1 ? true : false;
	}

	@Override
	public boolean modify(AppInfo appInfo) {
		return appInfoMapper.modify(appInfo) == 1 ? true : false;
	}

	@Override
	public boolean updVersionIdById(Integer vid, Integer aid) {
		return appInfoMapper.updVersionIdById(vid, aid) == 1 ? true : false;
	}

	@Override
	public boolean delAppInfo(Integer id) {
		return appInfoMapper.delAppInfo(id) == 1 ? true : false;
	}

	@Override
	public boolean updStatus(Integer id, Integer sta) {
		return appInfoMapper.updStatus(id, sta) == 1 ? true : false;
	}
	
}
