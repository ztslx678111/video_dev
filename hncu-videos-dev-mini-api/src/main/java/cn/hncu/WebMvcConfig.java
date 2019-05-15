package cn.hncu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.hncu.controller.interceptor.MiniInterceptor;


@Configuration	
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override  
	public void addInterceptors(InterceptorRegistry registry) {

       
        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
	       .addPathPatterns("/video/upload", "/video/uploadCover",
	    		   			"/video/userLike", "/video/userUnLike",
	    		   			"/video/saveComment").addPathPatterns("/bgm/**").excludePathPatterns("/user/queryPublisher");  
		super.addInterceptors(registry);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
		.addResourceLocations("classpath:/META-INF/resources/")
		.addResourceLocations("file:C:/hncu_videos_dev/");
		//访问所有资源,虚拟目录
	}
   
	@Bean
	public MiniInterceptor miniInterceptor() {
		 return new MiniInterceptor();
	}
}
