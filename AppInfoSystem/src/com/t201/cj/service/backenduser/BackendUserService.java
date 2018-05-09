package com.t201.cj.service.backenduser;

import com.t201.cj.pojo.BackendUser;

public interface BackendUserService {
	/**
	 * 根据用户编码查询用户信息
	 * @param userCode
	 * @return
	 */
	public BackendUser getLogin(String userCode);
}
