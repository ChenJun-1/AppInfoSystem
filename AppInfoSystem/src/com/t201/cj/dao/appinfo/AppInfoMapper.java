package com.t201.cj.dao.appinfo;

import java.util.List;



import org.apache.ibatis.annotations.Param;

import com.t201.cj.pojo.AppInfo;

public interface AppInfoMapper {
	/**
	 * 根据id 修改APP的状态
	 * @param id
	 * @param sta
	 * @return
	 */
	public int updStatus(@Param("id")Integer id,@Param("sta")Integer sta);
	
	/**
	 * 根据id 删除APP信息
	 * @param id
	 * @return
	 */
	public int delAppInfo(@Param("id")Integer id);
	
	/**
	 * 修改APP基本信息的最新版本信息
	 * @return
	 */
	public int updVersionIdById(@Param("vid")Integer vid,@Param("aid")Integer aid);
	
	/**
	 * 修该APP基础信息
	 * @param appInfo
	 * @return
	 */
	public int modify(AppInfo appInfo);
	
	/**
	 * 根据id删除APPLOGO
	 * @param id
	 * @return
	 */
	public int delAppLOGO(@Param("id")Integer id);
	
	/**
	 * 根据条件查询APP信息
	 * @param id
	 * @param APKName
	 * @return
	 */
	public AppInfo getAppInfo(@Param("id")Integer id , @Param("APKName")String APKName);
	
	/**
	 * 添加APP信息
	 * @param appInfo
	 * @return
	 */
	public int addAppInfo(AppInfo appInfo);
	
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
	 */
	public int getAppInfoCount(
			   @Param(value="softwareName")String querySoftwareName,
			   @Param(value="status")Integer queryStatus,
			   @Param(value="flatformId")Integer queryFlatformId,
			   @Param(value="categoryLevel1")Integer queryCategoryLevel1,
			   @Param(value="categoryLevel2")Integer queryCategoryLevel2,
			   @Param(value="categoryLevel3")Integer queryCategoryLevel3);
	
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
	 */
	public List<AppInfo> getAppInfoList(
			@Param(value="softwareName")String querySoftwareName,
			@Param(value="status")Integer queryStatus,
			@Param(value="flatformId")Integer queryFlatformId,
			@Param(value="categoryLevel1")Integer queryCategoryLevel1,
			@Param(value="categoryLevel2")Integer queryCategoryLevel2,
			@Param(value="categoryLevel3")Integer queryCategoryLevel3,
			@Param(value="devId")Integer devId,
			@Param(value="currentPageNo")Integer currentPageNo,
			@Param(value="pageSize")Integer pageSize);
}
