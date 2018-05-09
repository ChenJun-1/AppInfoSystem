package com.t201.cj.dao.devuser;

import org.apache.ibatis.annotations.Param;

import com.t201.cj.pojo.DevUser;

public interface DevUserMapper {
	/**
	 * 根据开发者帐号查询开发者信息
	 * @param userCode
	 * @return
	 */
	public DevUser getLogin(@Param("devCode")String devCode);
}
