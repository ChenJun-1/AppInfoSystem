package com.t201.cj.service.appversion;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.t201.cj.dao.appversion.AppVersionMapper;
import com.t201.cj.pojo.AppVersion;

@Service
public class AppVersionServiceImpl implements AppVersionService {

	@Resource
	private AppVersionMapper appVersionMapper;
	
	@Override
	public List<AppVersion> getAppVersionList(Integer aid) {
		return appVersionMapper.getAppVersionList(aid);
	}

	@Override
	public boolean addAppVersion(AppVersion appVersion) {
		return appVersionMapper.addAppVersion(appVersion) == 1 ? true : false;
	}

	@Override
	public int getNewId() {
		return appVersionMapper.getNewId();
	}

	@Override
	public AppVersion getAppVersionById(Integer vid) {
		return appVersionMapper.getAppVersionById(vid);
	}

	@Override
	public boolean deleteApkFile(Integer vid) {
		return appVersionMapper.deleteApkFile(vid) == 1 ? true : false;
	}

	@Override
	public boolean modify(AppVersion appVersion) {
		return appVersionMapper.modify(appVersion) == 1 ? true : false;
	}

	@Override
	public boolean delAppVersion(Integer aid) {
		return appVersionMapper.delAppVersion(aid) == 1 ? true : false;
	}
	
}
