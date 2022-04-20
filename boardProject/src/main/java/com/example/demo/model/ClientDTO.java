package com.example.demo.model;

import java.util.Date;

import lombok.Data;

@Data
public class ClientDTO {
	String pid, pName ,nickName ,email ,phone ,kind;
	Date regDate, up_Date;
}
