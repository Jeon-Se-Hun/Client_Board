package com.example.demo.service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.idgen.service.IdgenService;
import com.example.demo.mapper.FileMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService {

	@Resource
	FileMapper fm;
	
	@Resource(name = "fileIdgen")
	private IdgenService fileIdgen;
	
	
	public boolean UpFile(HashMap<String, Object> map, List<MultipartFile> multipartFile) throws Exception {
		boolean flag = false;
		
		for (MultipartFile file : multipartFile) {
			String fileRoot;
			String contextRoot = String.valueOf(map.get("contextRoot"));
			
			fileRoot = contextRoot + "resources/file/";
			String originalFileName = file.getOriginalFilename(); // 오리지날 파일명
			String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
			// 파일 확장자
			String savedFileName = UUID.randomUUID() + extension; // 저장될 파일 명
			File targetFile = new File(fileRoot + savedFileName);
			try {
				InputStream fileStream = file.getInputStream();
				FileUtils.copyInputStreamToFile(fileStream, targetFile); // 파일 저장
			} catch (Exception e) { // 파일삭제
				FileUtils.deleteQuietly(targetFile); // 저장된 현재 파일 삭제
				e.printStackTrace();
				break;
			}
			map.put("file_key_id", fileIdgen.getNextStringId());
			map.put("fileRoot", fileRoot);
			map.put("original_file_name", originalFileName);
			map.put("saved_file_name", savedFileName);
			map.put("file_size", file.getSize());
			fm.upFile(map);
			flag = true;
		}
		return flag;
	}
	
	public List<HashMap<String, Object>> FileDetail(HashMap<String, Object> map) throws Exception {
		List<HashMap<String, Object>> detail = fm.fileDetail(map);
		return detail;
	}
	
	
	public void DeleteFileAll(HashMap<String, Object> map) throws Exception {
		fm.deleteFileAll(map);
	}
	
	public boolean DeleteFile(List<String> deleteFiles) throws Exception {
		boolean flag = true;
		
		int cnt = fm.deleteFile(deleteFiles);
		
		if(cnt < 1) {
			flag = false;
		}
		
		return flag;
	}
	
	
}
