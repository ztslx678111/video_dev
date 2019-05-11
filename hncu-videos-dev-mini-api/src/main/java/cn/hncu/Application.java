package cn.hncu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages= {"cn.hncu"})
@MapperScan(basePackages= {"cn.hncu.mapper"})
@ComponentScan(basePackages= {"cn.hncu"})
public class Application {  
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
