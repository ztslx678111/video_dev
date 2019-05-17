package cn.hncu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.hncu.enums.VideoStatusEnum;
import cn.hncu.pojo.Bgm;
import cn.hncu.pojo.Comments;
import cn.hncu.pojo.Videos;
import cn.hncu.service.BgmService;
import cn.hncu.service.VideoService;
import cn.hncu.utils.FetchVideoCover;
import cn.hncu.utils.MergeVideoMp3;
import cn.hncu.utils.PagedResult;
import cn.hncu.utils.hncuJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value="视频相关业务的接口", tags= {"视频相关业务得controller"})
@RequestMapping("/video")
public class VideoController extends BasicController{
     
	@Autowired
	private BgmService bgmService;
	@Autowired
	private VideoService videoService;
	
	/**
	 * 用户上传视频
	 * @param userId
	 * @param bgmId
	 * @param videoSeconds
	 * @param videoWidth
	 * @param videoHeight
	 * @param desc
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="用户上传视频",notes="用户上传视频的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户id",required=true,
                dataType="String",paramType="form"),
		@ApiImplicitParam(name="bgmId",value="背景音乐id",required=false,
        dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoSeconds",value="背景音乐播放长度",required=true,
        dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoWidth",value="视频宽度",required=true,
        dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoHeight",value="视频高度",required=true,
        dataType="String",paramType="form"),
		@ApiImplicitParam(name="desc",value="视频描述",required=false,
        dataType="String",paramType="form")
	})
	@PostMapping(value="/upload",headers="content-type=multipart/form-data")
	public hncuJSONResult upload(String userId, String bgmId,
			double videoSeconds, int videoWidth, int videoHeight, String desc,@ApiParam(value="短视频", required=true) MultipartFile file ) throws Exception {
		
		//判断用户id是否为空
		if(StringUtils.isBlank(userId)) {
			return hncuJSONResult.errorMsg("用户id不能为空...");
		}
		
		System.out.println(userId);
		
		//文件保存得命名空间
		//String fileSpace = "C:/hncu_videos_dev";
		//保存到数据库中得相对路径
		String uploadPathDB ="/" +userId +"/video";
		String coverPathDB="/" + userId + "/video" ;
		//用户上传视频的最终路径
		String finalVideoPath = "";
		FileOutputStream fout= null;
		InputStream in = null;
		
		try {
			if( file != null ) {
				//获取文件名
				String fileName = file.getOriginalFilename();
				//abc.mp4
				String fileNamePrefix = fileName.split("\\.")[0];
				
				
				//判断文件名是否为空
				if(StringUtils.isNotEmpty(fileName)) {
					//文件上传得最终保存路径 绝对路径
					finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					//设置数据库保存的路径
					uploadPathDB += ("/" + fileName);
					coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";
					//新建文件
					File outFile = new File(finalVideoPath);
					//判断文件的根目录是否为空以及是否是目录
					if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					//拷贝流
					fout= new FileOutputStream(outFile);
					in = file.getInputStream();
					IOUtils.copy(in, fout);
				}
				
			} else {
				 return hncuJSONResult.errorMsg("上传出错，请小主重新试试哦~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭流
			if(fout != null ) {
				fout.flush();
				fout.close();
			}
		}
		
		
		//判断bgmId是否为空，如果不为空
		//那就查询bgm的信息，并且合并视频，生成新的视频
		if(StringUtils.isNotBlank(bgmId)) {
			//调用service查找到bgmId
			Bgm bgm = bgmService.queryBgmById(bgmId);
			//获取bgm的路径 
			String mp3InputPath = FILE_SPACE + bgm.getPath();
			//调用MergeVideoMp3工具类 
			MergeVideoMp3 tool = new MergeVideoMp3(FFMPEGEXE);
			//获取用户上传视频的最终路径
			String videoInputPath = finalVideoPath;
			//获取最终合并视频的名字 取名规则
			String videoOutputName = UUID.randomUUID().toString()+".mp4";
			//设置保存到数据库中的相对路径
			uploadPathDB = "/" +userId +"/video" +  "/" + videoOutputName;
			//合成后的视频的最终路径
			finalVideoPath = FILE_SPACE + uploadPathDB;
			//调用合成方法 合成视频 并将 所生成的视频 放入 
			tool.convertor(mp3InputPath,videoInputPath, videoSeconds, finalVideoPath);
		}
		
		//System.out.println(uploadPathDB);
	    //System.out.println(finalVideoPath);
	    
	    
	    //对视频进行截图
		// 获取视频信息。
		FetchVideoCover videoInfo = new FetchVideoCover(FFMPEGEXE);
		videoInfo.getCover(finalVideoPath,FILE_SPACE+coverPathDB);
      
	    
	    //保存视频信息到数据库
	    Videos video = new Videos();
		video.setAudioId(bgmId);
		video.setUserId(userId);
		video.setVideoSeconds((float)videoSeconds);
		video.setVideoHeight(videoHeight);
		video.setVideoWidth(videoWidth);
		video.setVideoDesc(desc);
		video.setVideoPath(uploadPathDB);
		video.setCoverPath(coverPathDB);
		video.setStatus(VideoStatusEnum.SUCCESS.value);
		video.setCreateTime(new Date());
	  
		String videoId = videoService.saveVideo(video);
		return hncuJSONResult.ok(videoId);
	}
	
    /**
     * 用户上传封面
     * @param userId
     * @param videoId
     * @param file
     * @return
     * @throws Exception
     */
	@ApiOperation(value="上传封面",notes="上传封面的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="userId",value="用户的id",required=true,
		        dataType="String",paramType="form"),
		@ApiImplicitParam(name="videoId",value="视频主键id",required=true,
                dataType="String",paramType="form")
	})
	@PostMapping(value="/uploadCover",headers="content-type=multipart/form-data")
	public hncuJSONResult upload(String userId,String videoId, @ApiParam(value="视频封面",required=true) MultipartFile file ) throws Exception {
		
		//判断用户id是否为空
		if(StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
			return hncuJSONResult.errorMsg("视频主键id和用户id不能为空...");
		}
		
		System.out.println(videoId);
		
		
		//保存到数据库中得相对路径
		String uploadPathDB ="/" + userId +"/video";
		
		//用户上传视频的最终路径
		String finalCoverPath = "";
		FileOutputStream fout= null;
		InputStream in = null;
		
		try {
			if( file != null ) {
				//获取文件名
				String fileName = file.getOriginalFilename();
				
				
				//判断文件名是否为空
				if(StringUtils.isNotEmpty(fileName)) {
					//文件上传得最终保存路径 绝对路径
					finalCoverPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					//设置数据库保存得路径
					uploadPathDB += ("/" + fileName);
					
					//新建文件
					File outFile = new File(finalCoverPath);
					//判断文件的根目录是否为空以及是否是目录
					if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					//拷贝流
					fout= new FileOutputStream(outFile);
					in = file.getInputStream();
					IOUtils.copy(in, fout);
				}
				
			} else {
				 return hncuJSONResult.errorMsg("上传出错，请小主重新试试哦~~");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭流
			if(fout != null ) {
				fout.flush();
				fout.close();
			}
		}
		
		
		videoService.updateVideo(videoId, uploadPathDB);
		
	   
	 
		return hncuJSONResult.ok();
	}
	
	
	/**
	 * 
	 * @Description: 分页和搜索查询视频列表
	 * isSaveRecord： 1 - 需要保存
	 *               0 - 不需要保存，或者为空的时候
	 */
	@PostMapping(value="/showAll")
	public hncuJSONResult showAll(@RequestBody Videos video, Integer isSaveRecord, Integer page, Integer pageSize ) throws Exception {
		 
		if(page == null) {
			page = 1;
		}
		
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
	
		
		PagedResult result = videoService.getAllVideos(video,isSaveRecord ,page, pageSize);
		return hncuJSONResult.ok(result);
	}
	
	
	/**
	 * @Description: 我关注的人发的视频
	 */
	@PostMapping("/showMyFollow")
	public hncuJSONResult showMyFollow(String userId, Integer page) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return hncuJSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}

		int pageSize = 6;
		
		PagedResult videosList = videoService.queryMyFollowVideos(userId, page, pageSize);
		
		return hncuJSONResult.ok(videosList);
	}
	
	/**
	 * @Description: 我收藏(点赞)过的视频列表
	 */
	@PostMapping("/showMyLike")
	public hncuJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return hncuJSONResult.ok();
		}
		
		if (page == null) {
			page = 1;
		}

		if (pageSize == null) {
			pageSize = 6;
		}
		
		PagedResult videosList = videoService.queryMyLikeVideos(userId, page, pageSize);
		
		return hncuJSONResult.ok(videosList);
	}
	
	/**
	 * 热词搜索
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/hot")
	public hncuJSONResult hot() throws Exception {
		return hncuJSONResult.ok(videoService.getHotwords());
	}
	 
	/**
	 * 点赞视频
	 * @param userId
	 * @param videoId
	 * @param videoCreaterId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/userLike")
	public hncuJSONResult userLike(String userId,String videoId, String videoCreaterId) throws Exception {
		videoService.userLikeVideo(userId, videoId, videoCreaterId);
		return hncuJSONResult.ok();
	}
	
	/**
	 * 取消点赞视频
	 * @param userId
	 * @param videoId
	 * @param videoCreaterId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value="/userUnLike")
	public hncuJSONResult userUnLike(String userId,String videoId, String videoCreaterId) throws Exception {
		videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
		return hncuJSONResult.ok();
	}
	
	/**
	 * 保存评论
	 * @param comment
	 * @param fatherCommentId
	 * @param toUserId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/saveComment")
	public hncuJSONResult saveComment(@RequestBody Comments comment, String fatherCommentId, String toUserId) throws Exception {
		
		if(! StringUtils.isBlank(fatherCommentId) || StringUtils.isBlank(toUserId) ) {
		  comment.setFatherCommentId(fatherCommentId);
		  comment.setToUserId(toUserId);
		}
		
		videoService.saveComment(comment);
		return hncuJSONResult.ok();
	} 
	
	/**
	 * 获取评论
	 * @param videoId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/getVideoComments")
	public hncuJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {
		
		if(StringUtils.isBlank(videoId)) {
			return hncuJSONResult.ok();
		}
		
		//分页查询视频列表，时间顺序倒序排序
		if(page == null) {
			page = 1;
		}
		
		if(pageSize ==  null) {
			pageSize = 10;
		}
		
		PagedResult list = videoService.getAllComments(videoId, page, pageSize);
		
		return hncuJSONResult.ok(list);
		
	}
}
