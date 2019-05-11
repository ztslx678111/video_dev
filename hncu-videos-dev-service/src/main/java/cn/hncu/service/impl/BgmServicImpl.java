package cn.hncu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hncu.mapper.BgmMapper;
import cn.hncu.pojo.Bgm;
import cn.hncu.service.BgmService;

@Service
public class BgmServicImpl implements BgmService {
  
	@Autowired
	private BgmMapper bgmMapper;
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public List<Bgm> queryBgmList() {
		return bgmMapper.selectAll();
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Bgm queryBgmById(String bgmId) {
		return bgmMapper.selectByPrimaryKey(bgmId);
	}
  
	
	

	
}
