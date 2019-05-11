package cn.hncu.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.hncu.pojo.Videos;
import cn.hncu.pojo.vo.VideosVo;
import cn.hncu.utils.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos> {
	
	/**
	 * 查询所有短视频
	 * @param videoDesc
	 * @return
	 */
	public List<VideosVo> queryAllVideos(@Param("videoDesc") String videoDesc, @Param("userId") String userId);
	
	
	/**
	 * @Description:对视频喜欢的数量进行累加
	 */
	public void addVideoLikeCount(String videoId);
	
	/**
	 * @Description:对视频喜欢的数量进行累减
	 */
	public void reduceVideoLikeCount(String videoId);

   
	/**
	 *  @Description:查询点赞视频
	 */
	public List<VideosVo> queryMyLikeVideos(String userId);

	/**
	 *  @Description:查询关注的视频
	 */
	public List<VideosVo> queryMyFollowVideos(String userId);
	
	
	
}