package cn.hncu.controller;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.hncu.pojo.Users;
import cn.hncu.pojo.vo.UsersVo;
import cn.hncu.service.UserService;
import cn.hncu.utils.MD5Utils;
import cn.hncu.utils.hncuJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户注册的接口",tags= {"注册和登陆的controller"} )
public class RegistLoginController extends BasicController{
	
	@Autowired
    private UserService userService;
	
	@ApiOperation(value="用户注册",notes="用户注册的接口")
	@PostMapping("/regist")
	public hncuJSONResult regist(@RequestBody Users user) {
		
		//卫条件
		//1.判断用户名和密码必须不为空，
		if(  StringUtils.isBlank( user.getUsername() ) || StringUtils.isBlank(user.getPassword())  ){
			return hncuJSONResult.errorMsg("用户名和密码不能为空");
		}
		
		//2.判断用户名是否存在
		boolean boo = userService.queryUserNameisExist( user.getUsername() );
		
		if( !boo ) {
			//3.保存用户，注册信息
			
			try {
				user.setNickname(user.getUsername());
				user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
				user.setFansCounts(0);
				user.setFollowCounts(0);
				user.setReceiveLikeCounts(0);
				user.setFaceImage(null);
				userService.savaUser(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else {
			return  hncuJSONResult.errorMsg("用户名已存在");
		}
		
		user.setPassword("");
		
		UsersVo userVo = setUserRedisSessionToken(user);
		
		return hncuJSONResult.ok(userVo);
	}
	
	/**
	 * 设置用户session Token
	 * @param userModel
	 * @return
	 */
	public UsersVo setUserRedisSessionToken(Users userModel) {
		String uniqueToken = UUID.randomUUID().toString();
		//System.out.println("RLCuniqueToken:" + uniqueToken);
		redis.set(USER_REDIS_SESSION + " : " + userModel.getId(), uniqueToken);
		UsersVo userVo=new UsersVo();
		BeanUtils.copyProperties(userModel, userVo);
		userVo.setUserToken(uniqueToken);
		//System.out.println("userVo.getUserToken:" + userVo.getUserToken());
		return userVo;
	}
	
	
	@ApiModelProperty(value = "用户登录",notes = "用户登录的接口")
	@PostMapping(value="/login")
	public hncuJSONResult login(@RequestBody Users user) throws Exception {
		String username = user.getUsername();
		String password = user.getPassword();
		
		// 1. 判断用户名和密码必须不为空
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return hncuJSONResult.ok("用户名和密码不能为空..");
		}
		
		// 2. 判断用户是否存在
		Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str( user.getPassword( ) ));
		
		
		// 3.返回
		if(userResult != null) {
			userResult.setPassword("");
			UsersVo userVo = setUserRedisSessionToken(userResult);
			return hncuJSONResult.ok(userVo);
		} else {
			return hncuJSONResult.errorMsg("用户名或密码不正确, 请重试...");
		}
		
	}
	
	@ApiModelProperty(value = "用户注销",notes = "用户注销的接口")
	@ApiImplicitParam(name = "userId",value = "用户Id", required = true , dataType = "String" ,paramType= "query" )
	@PostMapping(value="/logout")
	public hncuJSONResult logout(String userId) throws Exception {
		    redis.del(USER_REDIS_SESSION + " : " + userId);
			return hncuJSONResult.ok("成功注销~");
			
	}
}
