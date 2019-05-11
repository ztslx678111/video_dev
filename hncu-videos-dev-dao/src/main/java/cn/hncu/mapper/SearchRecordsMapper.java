package cn.hncu.mapper;

import java.util.List;

import cn.hncu.pojo.SearchRecords;
import cn.hncu.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotwords();
	
	
}