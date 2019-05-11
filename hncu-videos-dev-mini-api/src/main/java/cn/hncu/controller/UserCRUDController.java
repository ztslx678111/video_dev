package cn.hncu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hncu.pojo.Users;
import cn.hncu.service.crud.UserCRUDService;
import cn.hncu.utils.hncuJSONResult;

@RestController
@RequestMapping("/crud")
public class UserCRUDController {
	
	@Autowired
	private UserCRUDService userCRUDService;
	
	@RequestMapping("/save")
	public hncuJSONResult save() {
		
		Users user = new Users();
		userCRUDService.saveUser(user);
		
		return hncuJSONResult.ok();
	}
	
	@RequestMapping("/update")
	public hncuJSONResult update() {
		
		Users user = new Users();
		userCRUDService.updateUser(user);
		
		return hncuJSONResult.ok();
	}
	
	@RequestMapping("/update2")
	public hncuJSONResult update2() {
		
		Users user = new Users();
		userCRUDService.updateUser(user);
		
		return hncuJSONResult.ok();
	}
	
	@RequestMapping("/delUser")
	public hncuJSONResult delUser() {
		
		userCRUDService.delete();
		
		return hncuJSONResult.ok();
	}
}
