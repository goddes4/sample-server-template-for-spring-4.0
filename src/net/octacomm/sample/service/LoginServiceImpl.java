package net.octacomm.sample.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.octacomm.sample.db.mapper.UserMapper;
import net.octacomm.sample.netty.msg.login.LoginResult;
import net.octacomm.sample.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserMapper userMapper;
	
	private ConcurrentMap<String, Boolean> loginMap = new ConcurrentHashMap<String, Boolean>();
	
	@Override
	public LoginResult login(String id, String password) {
		if (isExistUser(id)) {
			// 비밀번호 인증을 위해서 MySQL에 질의 한다.
			if (userMapper.getUserForAuth(id, password) != null) {
				if (isLogin(id)) {
					return LoginResult.DUPLICATED_LOGIN;
				}
				updateLogin(id);
				
				return LoginResult.SUCCESS;
			} else {
				return LoginResult.INVALID_PASSWORD;
			}
		} else {
			return LoginResult.NOT_EXIST_USER;
		}
	}

	private boolean isLogin(String id) {
		Boolean ret = loginMap.get(id);
		if (ret != null) return ret;
		else return false;
	}

	private Boolean updateLogin(String id) {
		return loginMap.put(id, true);
	}

	private boolean isExistUser(String id) {
		return userMapper.getUser(id) != null;
	}

	@Override
	public void logout(String id) {
		loginMap.remove(id);
	}

}
