package com.t201.cj.dao.backenduser;

import org.apache.ibatis.annotations.Param;

import com.t201.cj.pojo.BackendUser;

public interface BackendUserMapper {
	/**
	 * 根据用户编码查询用户信息
	 * @param userCode
	 * @return
	 */
	public BackendUser getLogin(@Param("userCode")String userCode);
}
