package com.example.demo.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.idgen.service.impl.IdgenServiceImpl;

@Configuration
public class ContextIdgen {
	@Bean(name = "usersIdgen")
	public IdgenServiceImpl userIdgen() {
		IdgenServiceImpl idgenServiceImpl = new IdgenServiceImpl();
		idgenServiceImpl.setCipers(3);
		idgenServiceImpl.setFillChar('0');
		idgenServiceImpl.setPrefix("USER_");
		idgenServiceImpl.setTableName("USERS");
		return idgenServiceImpl;
	}
	
	@Bean(name = "boardIdgen")
	public IdgenServiceImpl boardIdgen() {
		IdgenServiceImpl idgenServiceImpl = new IdgenServiceImpl();
		idgenServiceImpl.setCipers(3);
		idgenServiceImpl.setFillChar('0');
		idgenServiceImpl.setPrefix("BOR_");
		idgenServiceImpl.setTableName("BOARD");
		return idgenServiceImpl;
	}
	
	@Bean(name = "fileIdgen")
	public IdgenServiceImpl fileIdgen() {
		IdgenServiceImpl idgenServiceImpl = new IdgenServiceImpl();
		idgenServiceImpl.setCipers(3);
		idgenServiceImpl.setFillChar('0');
		idgenServiceImpl.setPrefix("FILE_");
		idgenServiceImpl.setTableName("UPFILE");
		return idgenServiceImpl;
	}
	
	@Bean(name = "commentIdgen")
	public IdgenServiceImpl commentIdgen() {
		IdgenServiceImpl idgenServiceImpl = new IdgenServiceImpl();
		idgenServiceImpl.setCipers(3);
		idgenServiceImpl.setFillChar('0');
		idgenServiceImpl.setPrefix("COM_");
		idgenServiceImpl.setTableName("COMMENT");
		return idgenServiceImpl;
	}
	
	@Bean(name = "likeIdgen")
	public IdgenServiceImpl likeIdgen() {
		IdgenServiceImpl idgenServiceImpl = new IdgenServiceImpl();
		idgenServiceImpl.setCipers(3);
		idgenServiceImpl.setFillChar('0');
		idgenServiceImpl.setPrefix("LIKE_");
		idgenServiceImpl.setTableName("LIKE");
		return idgenServiceImpl;
	}
	
	@Bean(name = "replyIdgen")
	public IdgenServiceImpl replyIdgen() {
		IdgenServiceImpl idgenServiceImpl = new IdgenServiceImpl();
		idgenServiceImpl.setCipers(3);
		idgenServiceImpl.setFillChar('0');
		idgenServiceImpl.setPrefix("REPLY_");
		idgenServiceImpl.setTableName("REPLY");
		return idgenServiceImpl;
	}
}
