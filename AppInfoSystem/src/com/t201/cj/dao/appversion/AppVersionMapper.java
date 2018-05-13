package com.t201.cj.dao.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.t201.cj.pojo.AppVersion;

public interface AppVersionMapper {
	
	/**
	 * 根据AppId 删除版本信息
	 * @param aid
	 * @return
	 */
	public int delAppVersion(@Param("aid")Integer aid);
	
	/**
	 * 修改版本信息
	 * @param appVersion
	 * @return
	 */
	public int modify(AppVersion appVersion);
	
	/**
	 * 根据版本id删除 apk文件
	 * @param vid
	 * @return
	 */
	public int deleteApkFile(@Param("vid")Integer vid);
	
	/**
	 * 根据APP版本id，信息查询APP版本信息
	 * @param vid
	 * @return
	 */
	public AppVersion getAppVersionById(@Param("vid")Integer vid);
	
	/**
	 * 查询最新版本的自增ID,用于更新APP基础信息
	 * @return
	 */
	public int getNewId();
	
	/**
	 * 新增APP版本信息
	 * @param appVersion
	 * @return
	 */
	public int addAppVersion(AppVersion appVersion);
	
	/**
	 * 根据APPid查询-APP版本信息
	 * @param aid
	 * @return
	 */
	public List<AppVersion> getAppVersionList(@Param("aid")Integer aid);
	
}
