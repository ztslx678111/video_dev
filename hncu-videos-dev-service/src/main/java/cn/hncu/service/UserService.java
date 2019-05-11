package cn.hncu.service;

import cn.hncu.pojo.Users;
import cn.hncu.pojo.UsersReport;

public interface UserService {
  
	/**
	 * 判断用户名是否存在
	 * @param username
	 * @return
	 */
	public boolean queryUserNameIsExist(String username);
	
	/**
	 * 保存用户 注册用户
	 * @param user
	 */
	public void saveUser(Users user);
	
	/**
	 * 用户登录，根据用户名和密码查询用户
	 * @return user
	 */
	public Users queryUserForLogin(String username,String password);
	
	/**
	 * 用户修改信息
	 * @param user
	 */
	public void updateUserInfo(Users user);
	
	/**
	 * 查询用户信息
	 * @param userId
	 * @return
	 */
	public Users queryUserInfo(String userId);
	
	/**
	 * 查询用户是否喜欢点赞视频
	 * @param userId
	 * @param videoId
	 * @return
	 */
	public boolean isUserLikeVideo(String userId, String videoId);
	
	/**
	 * 增加用户和粉丝的关系
	 * @param userId
	 * @param fanId
	 */
	public void saveUserFanRelation(String userId, String fanId);
	
	/**
	 * 删除用户和粉丝的关系
	 * @param userId
	 * @param fanId
	 */
	public void deleteUserFanRelation(String userId, String fanId);
	
	/**
	 * 查询用户是否关注
	 * @param userId
	 * @param fanId
	 * @return
	 */
	public boolean queryIfFollow(String userId, String fanId);
    
	/**
	 * @Description:举报用户
	 * @param usersReport
	 */
	public void reportUser(UsersReport usersReport);
	
	
}
