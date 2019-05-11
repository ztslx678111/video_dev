package cn.hncu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hncu.service.BgmService;
import cn.hncu.utils.hncuJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="背景音乐业务得接口", tags= {"背景音乐业务得controller"})
@RequestMapping("/bgm")
public class BgmController {
    
	@Autowired
	private BgmService bgmService;
	
	@ApiOperation(value="获取背景音乐列表",notes="获取背景音乐列表的接口")
	@PostMapping("/list")
	public hncuJSONResult list() {
		return hncuJSONResult.ok(bgmService.queryBgmList());
		
	}
}
