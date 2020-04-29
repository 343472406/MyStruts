package com.cynthia.upload.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	/* 文件类型限制 */
	private List<String> fileTypes = Arrays.asList("image/jpg","image/jpeg","image/png");
			
	
	/**
	 * 1. 需要在方法的参数上使用MultipartFile 接收文件上传的表单参数 注意：参数名必须和表单的一致 2.
	 * 开发者需要手动配置文件上传解析器（Springmvc.xml）（因为不常用，所以不是默认有的）
	 * 2，开发者需要手动配置文件上传解析器
	 * 因为上传不是每个访问必备的功能，所以默认没有开启，需要自己配置。
	 * 
	 * @param headImg
	 * @param username
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/upload")
	public String upload(MultipartFile headImg, @RequestParam("username") String username, Model m) {
		System.out.println("username:" + username);// 姓名
		System.out.println("图片表单名称:" + headImg.getName());// 获取上传文件的表单名称
		System.out.println("文件类型" + headImg.getContentType());// MIME类型（计算机中所有文件都有文件类型，就是这个）
		System.out.println("文件大小" + headImg.getSize());// 文件大小
		System.out.println("文件原名" + headImg.getOriginalFilename());// 获取上传文件的完整名称
		// 获取上传文件对应的输入流
		// InputStream in = headImg.getInputStream();

		/* 判断文件类型 */
		if(!fileTypes.contains(headImg.getContentType())) {
			m.addAttribute("errorMsg","只能上传jpg和png格式的图片");
			return "forward:/upload.jsp";
		}
		
		// 获取一个磁盘目录用于保存文件
		/*
		 * 创建目录有两种：mkdir和mkdirs
		 * mkdir只能创建单层目录
		 * mkdirs能创建多层目录（及单层）
		 */
		File destFile = new File("d:/Develop/test/upload");
		if (!destFile.exists()) {
			destFile.mkdirs();
			System.out.println("文件夹是否存在:" + destFile.exists());
		}
		// 使用uuid作为文件随机名称
		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
		// 使用FileNameUtils获取上传文件名的后缀
		String extension = FilenameUtils.getExtension(headImg.getOriginalFilename());// jpg , png 等等
		// 创建新的文件名称
		String newFileName = fileName + "." + extension;

		// 创建要保存文件的File对象
		File file = new File(destFile, newFileName);
		// 保存文件到本地磁盘
		try {
			headImg.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

		m.addAttribute("successMsg","上传成功");
		return "forward:/upload.jsp";

	}
	
    /* 多文件上传 */
	@RequestMapping("/uploads")
	public String uploads(MultipartFile[] headImgs, String username, Model m) {
		
		// 获取一个磁盘目录用于保存文件
		/*
		 * 创建目录有两种：mkdir和mkdirs
		 * mkdir只能创建单层目录
		 * mkdirs能创建多层目录（及单层）
		 */
		File destFile = new File("d:/Develop/test/upload");
		if (!destFile.exists()) {
			destFile.mkdirs();
			System.out.println("文件夹是否存在:" + destFile.exists());
		}
		
		for(int i=0; i<headImgs.length; i++) {
			// 使用uuid作为文件随机名称
			String fileName = UUID.randomUUID().toString().replaceAll("-", "");
			// 使用FileNameUtils获取上传文件名的后缀
			String extension = FilenameUtils.getExtension(headImgs[i].getOriginalFilename());// jpg , png 等等
			// 创建新的文件名称
			String newFileName = fileName + "." + extension;
	
			// 创建要保存文件的File对象
			File file = new File(destFile, newFileName);
			// 保存文件到本地磁盘
			try {
				headImgs[i].transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		m.addAttribute("successMsg","上传成功");
		return "forward:/upload.jsp";

	}

}
