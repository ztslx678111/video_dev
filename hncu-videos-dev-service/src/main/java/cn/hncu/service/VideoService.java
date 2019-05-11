package cn.hncu.service;

import java.util.List;

import cn.hncu.pojo.Comments;
import cn.hncu.pojo.Videos;
import cn.hncu.utils.PagedResult;
/**
 * 上传视频的接口
 * @author zhang
 *
 */
public interface VideoService {
  
    /**
     * @Description保存视频
     * @param video
     * @return
     */
	public String  saveVideo(Videos video);
	
	
	/**
	 * @Description修改视频的封面
	 * @param videoId
	 * @param coverPath
	 * @return
	 */
	public void updateVideo(String videoId,String coverPath);
	
	/**
	 * @Description分页查询列表
	 * @param page
	 * @param pageSize
	 * @return
	 */
    public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);
    
    /**
     * @Description:获取热搜词列表
     * @return
     */
    public List<String> getHotwords();
    
    /**
     * @Description:用户喜欢视频
     */
    public void userLikeVideo(String userId,String videoId,String videoCreaterId);
    
    /**
     * @Description:用户取消点赞
     */
    public void userUnLikeVideo(String userId,String videoId,String videoCreaterId);

    /**
	 * @Description: 查询我关注的人的视频列表
	 */
	public PagedResult queryMyFollowVideos(String userId, Integer page, int pageSize);

	/**
	 * @Description: 查询我喜欢的视频列表
	 */
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);
	
	/**
	 * @Description: 保存留言/评论
	 */
	public void saveComment(Comments comment);

	/**
	 * @Description: 留言分页
	 */
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
	
	
}
