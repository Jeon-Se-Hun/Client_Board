package com.example.demo.model;

import java.util.Date;

import lombok.Data;

@Data
public class FileDTO {
	String file_key_id, bor_key_id, original_file_name, stored_file_name, del_YN;
	Integer file_size;
	Date reg_date;
}
