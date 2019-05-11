package cn.hncu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.hncu.pojo.Users;
import cn.hncu.pojo.UsersReport;
import cn.hncu.pojo.vo.PublisherVideo;
import cn.hncu.pojo.vo.UsersVo;
import cn.hncu.service.UserService;
import cn.hncu.utils.hncuJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户相关业务的接口",tags= {"用户相关业务的controller"} )
@RequestMapping("/user")
public class UserController extends BasicController{
	
	@Autowired
    private UserService userService;
	
	@ApiOperation(value="用户上传头像",notes="用户上传头像的接口")
	@ApiImplicitParam(name="userId",value="用户id",required=true,
	                 dataType="String",paramType="query")
	@PostMapping("/uploadFace")
	public hncuJSONResult uploadFace(String userId,@RequestParam("file") MultipartFile[] files ) throws Exception {
		
		if(StringUtils.isBlank(userId)) {
			return hncuJSONResult.errorMsg("用户id不能为空...");
		}
		
		
		//文件保存得命名空间
		String fileSpace = "D:/hncu_videos_dev";
		//保存到数据库中得相对路径
		String uploadPathDB ="/" +userId +"/face";
		FileOutputStream fout= null;
		InputStream in = null;
		try {
			if(files != null && files.length > 0 ) {
				String fileName = files[0].getOriginalFilename();
				if(StringUtils.isNotEmpty(fileName)) {
					//文件上传得最终保存路径 绝对路径
					String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
					//设置数据库保存得路径
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalFacePath);
					if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					fout= new FileOutputStream(outFile);
					in = files[0].getInputStream();
					IOUtils.copy(in, fout);
				}
				
			} else {
				 return hncuJSONResult.errorMsg("上传出错，请小主重新试试哦~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fout != null ) {
				fout.flush();
				fout.close();
			}
		}
		
		
		//此时文件已经上传成功
		//以下操作更新到数据库
		Users user = new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);
		
		
		return hncuJSONResult.ok(uploadPathDB);
	}
	
	
	@ApiOperation(value="查询用户信息",notes="查询用户信息的接口")
	@ApiImplicitParam(name="userId",value="用户id",required=true,
	                 dataType="String",paramType="query")
	@PostMapping("/query")
	public hncuJSONResult query(String userId,String fanId) throws Exception {
		System.out.println(userId);
		if(StringUtils.isBlank(userId)) {
			return hncuJSONResult.errorMsg("用户id不能为空");
		}
		
		Users userInfo = userService.queryUserInfo(userId);
		UsersVo userVo = new UsersVo();
		userVo.setFollow(userService.queryIfFollow(userId, fanId));
		
		BeanUtils.copyProperties(userInfo, userVo);
		
		
		return hncuJSONResult.ok(userVo);
	}
	
	
	/**
	 * @Description:查询视频发布者信息
	 * @param loginUserId
	 * @param videoId
	 * @param publishUserId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/queryPublisher")
	public hncuJSONResult queryPublisher(String loginUserId, String videoId, 
			String publishUserId) throws Exception {
		
		if (StringUtils.isBlank(publishUserId)) {
			return hncuJSONResult.errorMsg("");
		}
		
		// 1. 查询视频发布者的信息
		Users userInfo = userService.queryUserInfo(publishUserId);
		UsersVo publisher = new UsersVo();
		BeanUtils.copyProperties(userInfo, publisher);
		
		// 2. 查询当前登录者和视频的点赞关系
		boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);
		
		PublisherVideo bean = new PublisherVideo();
		bean.setPublisher(publisher);
		bean.setUserLikeVideo(userLikeVideo);
		
		return hncuJSONResult.ok(bean);
	}
  
	
	@PostMapping("/beyourfans")
	public hncuJSONResult beyourfans(String userId, String fanId) throws Exception {
		
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return hncuJSONResult.errorMsg("");
		}
		
		userService.saveUserFanRelation(userId, fanId);
		
		return hncuJSONResult.ok("关注成功...");
	}
	
	@PostMapping("/dontbeyourfans")
	public hncuJSONResult dontbeyourfans(String userId, String fanId) throws Exception {
		
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return hncuJSONResult.errorMsg("");
		}
		
		userService.deleteUserFanRelation(userId, fanId);
		
		return hncuJSONResult.ok("取消关注成功...");
	}
	
	@PostMapping("/reportUser")
	public hncuJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {
		
		// 保存举报信息
		userService.reportUser(usersReport);
		
		return hncuJSONResult.errorMsg("举报成功...有你平台变得更美好...");
	}
}
