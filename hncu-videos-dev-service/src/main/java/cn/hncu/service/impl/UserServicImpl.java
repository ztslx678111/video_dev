package cn.hncu.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hncu.mapper.UsersFansMapper;
import cn.hncu.mapper.UsersLikeVideosMapper;
import cn.hncu.mapper.UsersMapper;
import cn.hncu.mapper.UsersReportMapper;
import cn.hncu.pojo.Users;
import cn.hncu.pojo.UsersFans;
import cn.hncu.pojo.UsersLikeVideos;
import cn.hncu.pojo.UsersReport;
import cn.hncu.service.UserService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServicImpl implements UserService {
  
	@Autowired
	private UsersMapper userMapper;
	
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	
	@Autowired
	private UsersFansMapper usersFansMapper;
	
	@Autowired
	private UsersReportMapper usersReportMapper;
	
	@Transactional(propagation = Propagation.SUPPORTS)//事务级别,查询
	@Override
	public boolean queryUserNameisExist(String username) {
		
		Users user=new Users();
		user.setUsername(username);
		//防护，查询是否存在当前用户
		Users reSult = userMapper.selectOne(user);
		
		
		return reSult == null ? false:true;
	}	

	@Transactional(propagation = Propagation.REQUIRED)//事务级别，插入操作
	@Override
	public void savaUser(Users user) {
		
        //补个UUid
		String uuid=UUID.randomUUID().toString().replaceAll("-", "");
		user.setId(uuid);
		
		//执行插入操作
		userMapper.insert(user);
		
		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String password) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("username",username);
		criteria.andEqualTo("password",password);
		Users result=userMapper.selectOneByExample(userExample);
		return result;
	}

	@Override
	public void updateUserInfo(Users user) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",user.getId());
		userMapper.updateByExampleSelective(user, userExample);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserInfo(String userId) {
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",userId);
		Users user = userMapper.selectOneByExample(userExample);
		return user;
	}
   
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeVideo(String userId, String videoId) {
	  
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)) {
			return false;
		}
		 
		
		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("videoId",videoId);
		List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
		
		if(list != null && list.size() > 0) 
		{
			return true;
		}
		return false;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveUserFanRelation(String userId, String fanId) {
		
		String uuid = UUID.randomUUID().toString().replace("-", "");
		
		UsersFans userFan = new UsersFans();
		userFan.setId(uuid);
		userFan.setUserId(userId);
		userFan.setFanId(fanId);
		
		usersFansMapper.insert(userFan);
		
		userMapper.addFansCount(userId);
		userMapper.addFollersCount(fanId);
		
	}
 
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {
		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("fanId",fanId);
		usersFansMapper.deleteByExample(example);
		
		userMapper.reduceFansCount(userId);
		userMapper.reduceFollersCount(fanId);
	}
   
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryIfFollow(String userId, String fanId) {
		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("fanId",fanId);
		List<UsersFans> list=usersFansMapper.selectByExample(example);
		if(list != null && !list.isEmpty() && list.size()>0) {
			return true;
		}
	   
		return false;
	}
    
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void reportUser(UsersReport usersReport) {
		 String uuid =UUID.randomUUID().toString().replace("-", "");
		 usersReport.setId(uuid);
		 usersReport.setCreateDate(new Date());
		 
		 usersReportMapper.insert(usersReport);
		
	}
	

}
