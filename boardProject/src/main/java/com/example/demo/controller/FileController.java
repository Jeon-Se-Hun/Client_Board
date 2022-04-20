package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class FileController {

	@Resource
	FileService fs;
	
	// ck 에디터에서 파일 업로드
	@RequestMapping("/ckeditor/file_upload.do")
	@ResponseBody
	public String communityImageUpload(HttpServletRequest request, HttpServletResponse response,
			@RequestParam MultipartFile upload) {

		OutputStream out = null;
		PrintWriter printWriter = null;
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String callback = request.getParameter("CKEditorFuncNum");
		String fileName = "";
		try {

			fileName = upload.getOriginalFilename();
			byte[] bytes = upload.getBytes();

			String drv = request.getRealPath("");

			drv = drv.substring(0, drv.length()) + "resources" + request.getContextPath() + "/img/ckUpload/";
			
			File desti = new File(drv);
			if (!desti.exists()) {
				desti.mkdirs();
			}

			String inDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
			String inTime = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());

			fileName = inDate + inTime + fileName;

			out = new FileOutputStream(new File(drv + fileName));
			out.write(bytes);

			if (log.isDebugEnabled()) {
				log.debug(" Request drv \t: " + drv);
				log.debug(" Request filename \t: " + fileName);
				log.debug(" Request callback \t: " + callback);
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
		}
		return "{\"uploaded\":1, \"url\":\"" + "http://localhost:8088/resources" + request.getContextPath()
				+ "/img/ckUpload/" + fileName + "\"}";
	}

	@RequestMapping("/client/board/FileUpload")
	@ResponseBody
	public boolean FileUpload(@RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFile,
			@RequestParam(value = "deleteFiles", required = false) List<String> deleteFiles,
			@RequestParam(value = "borKeyId", required = false) String bor_key_id, HttpServletRequest request) throws Exception {
		String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
		boolean flag = false;
		
		try {
			// 파일이 있을때 탄다.
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (multipartFile != null) {
				map.put("bor_key_id", bor_key_id);
				map.put("contextRoot", contextRoot);
				flag = fs.UpFile(map, multipartFile);
			}
			
			if(deleteFiles.size() > 0) {
				flag = fs.DeleteFile(deleteFiles);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	@RequestMapping("/fileDownload")
	public void download(HttpServletResponse response
			, @RequestParam(value = "originalName", required = false) String originalName
			, @RequestParam(value = "saveName", required = false) String saveName) throws IOException {

	    String path = "D:\\study\\project\\boardProject\\src\\main\\webapp\\resources\\file/" + saveName;
	    
	    byte[] fileByte = FileUtils.readFileToByteArray(new File(path));

	    response.setContentType("application/octet-stream");
	    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalName, "UTF-8")+"\";");
	    response.setHeader("Content-Transfer-Encoding", "binary");

	    response.getOutputStream().write(fileByte);
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	  }
}
