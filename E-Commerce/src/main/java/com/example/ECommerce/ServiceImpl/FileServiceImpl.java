package com.example.ECommerce.ServiceImpl;

import com.example.ECommerce.Service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	
	
	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		String originalFileNae=file.getOriginalFilename();
		String randomId= UUID.randomUUID().toString();
		String fileName=randomId.concat(originalFileNae.substring(originalFileNae.lastIndexOf('.')));
		String filePath=path+File.separator+fileName;
		
		File folder=new File(path);
		
		if (!folder.exists()){
			folder.mkdir();
		}
		Files.copy(file.getInputStream(), Paths.get(filePath));
		return fileName;
	}
	
	
	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {

		String filePath=path+File.separator+fileName;
		InputStream inputStream=new FileInputStream(filePath);
		return inputStream;
	}
	

}
