package cn.hncu.mapper;

import java.util.List;

import cn.hncu.pojo.Comments;
import cn.hncu.pojo.vo.CommentsVO;
import cn.hncu.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
}