package com.t201.cj.service.appinfo;

import java.util.List;

import com.t201.cj.pojo.AppInfo;

public interface AppInfoService {
	/**
	 * 根据条件查询APP信息列表数量
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @param devId
	 * @return
	 * @throws Exception
	 */
	public int getAppInfoCount(String querySoftwareName,Integer queryStatus,
			   Integer queryFlatformId,Integer queryCategoryLevel1,
			   Integer queryCategoryLevel2,Integer queryCategoryLevel3);
	
	/**
	 * 根据条件查询APP信息列表
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @param devId
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<AppInfo> getAppInfoList(String querySoftwareName,Integer queryStatus,
			Integer queryFlatformId,Integer queryCategoryLevel1,
			Integer queryCategoryLevel2,Integer queryCategoryLevel3,
			Integer devId,Integer currentPageNo,Integer pageSize);
}
