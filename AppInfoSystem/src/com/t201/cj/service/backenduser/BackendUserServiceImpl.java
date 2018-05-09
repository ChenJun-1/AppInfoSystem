package com.t201.cj.service.backenduser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.t201.cj.dao.backenduser.BackendUserMapper;
import com.t201.cj.pojo.BackendUser;

@Service
public class BackendUserServiceImpl implements BackendUserService {
	
	@Resource
	private BackendUserMapper backendUserMapper;
	
	@Override
	public BackendUser getLogin(String userCode) {
		return backendUserMapper.getLogin(userCode);
	}
	
}
