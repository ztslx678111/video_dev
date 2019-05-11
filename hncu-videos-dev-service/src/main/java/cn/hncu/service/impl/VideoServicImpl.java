package cn.hncu.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.hncu.mapper.CommentsMapper;
import cn.hncu.mapper.CommentsMapperCustom;
import cn.hncu.mapper.SearchRecordsMapper;
import cn.hncu.mapper.UsersLikeVideosMapper;
import cn.hncu.mapper.UsersMapper;
import cn.hncu.mapper.VideosMapper;
import cn.hncu.mapper.VideosMapperCustom;
import cn.hncu.pojo.Comments;
import cn.hncu.pojo.SearchRecords;
import cn.hncu.pojo.UsersLikeVideos;
import cn.hncu.pojo.Videos;
import cn.hncu.pojo.vo.CommentsVO;
import cn.hncu.pojo.vo.VideosVo;
import cn.hncu.service.VideoService;
import cn.hncu.utils.PagedResult;
import cn.hncu.utils.TimeAgoUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 上传视频的接口实现类
 * @author zhang
 *
 */
@Service
public class VideoServicImpl implements VideoService {
  
	@Autowired
	private VideosMapper videosMapper;
	
	@Autowired
	private UsersMapper usersMapper;
	
	@Autowired
	private VideosMapperCustom videosMapperCustom;
	
	@Autowired
	private SearchRecordsMapper searchRecordsMapper;
 
	@Autowired
	private CommentsMapper commentsMapper;
	
	@Autowired
	private  CommentsMapperCustom commentsMapperCustom;
	
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	
	//保存视频
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String saveVideo(Videos video) {
		
		String id=UUID.randomUUID().toString().replace("-", "");
		video.setId(id);
		videosMapper.insertSelective(video);
		return id;
		
	}
   
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateVideo(String videoId, String coverPath) {
		
		Videos video = new Videos();
		video.setId(videoId);
		video.setCoverPath(coverPath);
		videosMapper.updateByPrimaryKeySelective(video);
		
	}
  
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {
		
		//保存热搜词
		String desc = video.getVideoDesc();
		String userId = video.getUserId();
		if(isSaveRecord != null && isSaveRecord == 1) {
			
			SearchRecords record = new SearchRecords();
		    String recordId = UUID.randomUUID().toString().replace("-", "");
			record.setId(recordId);
			record.setContent(desc);
			
			searchRecordsMapper.insert(record);
		}
		
		PageHelper.startPage(page,pageSize);//分页
		
		//查出出来的数据 返回一个list
		List<VideosVo> list =  videosMapperCustom.queryAllVideos(desc,userId); 
		
		//封装一个对象，填充一些相关属性
		PageInfo<VideosVo> pageList = new PageInfo<>(list);
		PagedResult pagedResult = new PagedResult();
		pagedResult.setPage(page);//当前页数
		pagedResult.setRecords(pageList.getTotal());//总记录数
		pagedResult.setRows(list);//总行数
	    pagedResult.setTotal((int) pageList.getPages());//每行显示的内容
		return pagedResult;
	}

	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<String> getHotwords() {
		return searchRecordsMapper.getHotwords();
	}
 
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
		//1. 保存用户和视频的喜欢点赞关联关系表
		String likeId = UUID.randomUUID().toString().replace("-", "");
		
		UsersLikeVideos ulv = new UsersLikeVideos();
		ulv.setId(likeId);
		ulv.setUserId(userId);
		ulv.setVideoId(videoId);
		
		usersLikeVideosMapper.insert(ulv);
		
		//2.视频喜欢数量累加
		videosMapperCustom.addVideoLikeCount(videoId);
		//3.用户受喜欢数量的累加
		usersMapper.addReceiveLikeCount(userId);
		
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
		//1. 删除用户和视频的喜欢点赞关联关系表
				
		Example example = new Example(UsersLikeVideos.class);	
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId",userId);
		criteria.andEqualTo("videoId",videoId);
		
		usersLikeVideosMapper.deleteByExample(example);
				
		//2.视频喜欢数量累加
		videosMapperCustom.reduceVideoLikeCount(videoId);;
		//3.用户受喜欢数量的累加
		usersMapper.reduceReceiveLikeCount(userId);
		
	}
     
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyFollowVideos(String userId, Integer page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVo> list = videosMapperCustom.queryMyFollowVideos(userId);
				
		PageInfo<VideosVo> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
    
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
		PageHelper.startPage(page, pageSize);
		List<VideosVo> list = videosMapperCustom.queryMyLikeVideos(userId);
				
		PageInfo<VideosVo> pageList = new PageInfo<>(list);
		
		PagedResult pagedResult = new PagedResult();
		pagedResult.setTotal(pageList.getPages());
		pagedResult.setRows(list);
		pagedResult.setPage(page);
		pagedResult.setRecords(pageList.getTotal());
		
		return pagedResult;
	}
    
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveComment(Comments comment) {
		String uuid =UUID.randomUUID().toString().replaceAll("-", "");
		comment.setId(uuid);
		comment.setCreateTime(new Date());
		commentsMapper.insert(comment);	
	}
    
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
		PageHelper.startPage(page,pageSize);
		
		List<CommentsVO> list = commentsMapperCustom.queryComments(videoId);
		
		for(CommentsVO c : list) {
			String timeAgo = TimeAgoUtils.format(c.getCreateTime());
			c.setTimeAgoStr(timeAgo);
		}
		
		PageInfo<CommentsVO> pageList = new PageInfo<>(list);
		
		PagedResult grid = new PagedResult();
		grid.setTotal(pageList.getPages());
		grid.setRows(list);
		grid.setPage(page);
		grid.setRecords(pageList.getTotal());
		
		return grid;
	}

	
	
	
	

	
}
