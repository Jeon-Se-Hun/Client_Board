package com.example.demo.model;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDTO {
	String com_key_id, bor_key_id, reply_key_id, content, pid, del_YN, orderType;
	Integer good_cnt, bad_cnt;
	Date reg_date, up_date;
}
