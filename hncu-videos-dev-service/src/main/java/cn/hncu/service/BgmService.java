package cn.hncu.service;

import java.util.List;

import cn.hncu.pojo.Bgm;

public interface BgmService {
  
	/**
	 * 查询背景音乐列表
	 * @return list
	 */
	public List<Bgm> queryBgmList();
	
    /**
     * 根据id查询bgm信息
     * @param bgmId
     * @return
     */
	public Bgm queryBgmById(String bgmId);
	
}
