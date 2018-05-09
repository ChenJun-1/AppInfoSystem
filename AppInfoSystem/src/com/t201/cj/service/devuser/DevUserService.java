package com.t201.cj.service.devuser;

import com.t201.cj.pojo.DevUser;

public interface DevUserService {
	/**
	 * 根据开发者帐号查询开发者信息
	 * @param userCode
	 * @return
	 */
	public DevUser getLogin(String devCode);
}
