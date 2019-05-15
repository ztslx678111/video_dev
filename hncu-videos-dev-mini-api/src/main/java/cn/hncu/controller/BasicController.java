package cn.hncu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cn.hncu.utils.RedisOperator;

@RestController
public class BasicController {

	@Autowired
	public RedisOperator redis;
	
	//无状态用户session
	public static final String USER_REDIS_SESSION="USER_REDIS_SESSION";
	//文件路径
	public static final String FILE_SPACE="C:/hncu_videos_dev";
	//ffmpeg路径
	public static final String FFMPEGEXE="C:\\ffmpeg\\bin\\ffmpeg.exe";
	
	//每页分页的记录数
	public static final Integer PAGE_SIZE = 5;
}


