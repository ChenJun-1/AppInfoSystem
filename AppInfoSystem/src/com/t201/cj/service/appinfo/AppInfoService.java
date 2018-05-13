package com.t201.cj.service.appinfo;

import java.util.List;

import com.t201.cj.pojo.AppInfo;

public interface AppInfoService {
	
	/**
	 * 根据id 修改APP的状态
	 * @param id
	 * @param sta
	 * @return
	 */
	public boolean updStatus(Integer id,Integer sta);
	
	/**
	 * 根据id 删除APP信息
	 * @param id
	 * @return
	 */
	public boolean delAppInfo(Integer id);
	
	/**
	 * 修改APP基本信息的最新版本信息
	 * @return
	 */
	public boolean updVersionIdById(Integer vid,Integer aid);
	
	/**
	 * 修该APP基础信息
	 * @param appInfo
	 * @return
	 */
	public boolean modify(AppInfo appInfo);
	
	/**
	 * 根据id删除APPLOGO
	 * @param id
	 * @return
	 */
	public boolean delAppLOGO(Integer id);
	
	/**
	 * 根据条件查询APP信息
	 * @param id
	 * @param APKName
	 * @return
	 */
	public AppInfo getAppInfo(Integer id , String APKName);
	
	/**
	 * 添加APP信息
	 * @param appInfo
	 * @return
	 */
	public boolean addAppInfo(AppInfo appInfo);
	
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
