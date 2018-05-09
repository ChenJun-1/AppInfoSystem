package com.t201.cj.service.devuser;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.t201.cj.dao.devuser.DevUserMapper;
import com.t201.cj.pojo.DevUser;

@Service
public class DevUserServiceImpl implements DevUserService {
	
	@Resource
	private DevUserMapper devUserMapper;
	
	@Override
	public DevUser getLogin(String devCode) {
		return devUserMapper.getLogin(devCode);
	}
	
}
