package com.mooc.house.biz.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

@Service
public class FileService {
	
	@Value("${file.path}")
	private String filePath;
	
	public List<String> getImgPath(List<MultipartFile> files){
		List<String> paths = Lists.newArrayList();
		files.forEach(file->{
			File localFile = null;
			//判断要上传的文件是否为空
			if(!file.isEmpty()) {
				try {
				localFile = saveToLocal(file,filePath);
				//将绝对路径转为相对路径
				String path = StringUtils.substringAfterLast(localFile.getAbsolutePath(), filePath);
				//保存相对路径
				paths.add(path);
			}catch(Exception e) {
				throw new IllegalArgumentException(e); 
			}
			}
		});
		return paths;
	}
	//将上传文件保存到本地
	private File saveToLocal(MultipartFile file, String filePath2) throws IOException {
		//Instant.now().getEpochSecond() 获取以秒为单位的时间戳
		File newFile = new File(filePath+"/"+Instant.now().getEpochSecond()+"/"+file.getOriginalFilename());
		//文件不存在创建上级目录
		if(!newFile.exists()) {
			newFile.getParentFile().mkdirs();
			newFile.createNewFile();
		}
		//将要上传的文件写到
		Files.write(file.getBytes(), newFile);;
	return null;
	}
}
