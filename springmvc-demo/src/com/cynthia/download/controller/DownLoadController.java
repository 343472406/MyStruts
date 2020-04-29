package com.cynthia.download.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DownLoadController {
	
	/**
	 * Spring对文件下载没有进行封装
	 * 需要用原生Servlet的ServletOutputStream进行下载
	 * 
	 * 1. 下载资格判断
	 * 2. 根据文件名找到磁盘对应文件
	 * 3. ServletOutputStream将文件响应给浏览器（下载）
	 * @param fileName
	 * @throws IOException 
	 */
	@RequestMapping("/download")
	public void download(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		/* 1. 下载资格判断 */
		
		/* 2. 根据文件名找到磁盘对应文件 */
		FileInputStream inputStream = new FileInputStream("d:/Develop/test/upload/" + fileName);
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		/*
		 * 历史问题，浏览器2大阵营
		 * IE	微软
		 * 下载文件以UTF-8编码（文件名）
		 * 
		 * W3C	万维网联盟（互联网标准组织）
		 * 下载文件以ISO-8859-1编码（文件名）
		 * 
		 */

		/* 获取浏览器信息 */
		String agent = request.getHeader("User-Agent");
		/* 当不是IE时，设置为ISO-8859-1，则不会乱码 */
		if(!agent.contains("MSIE")) {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		
		
		/* 
		 * 设置相应头，因为默认html
		 * 下载应该是文件类型
		 * 
		 */
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		//使用commons-io-1.4.jar 中 IOUtils工具类，直接可以将输入数据转换到输出流中
		IOUtils.copy(inputStream, outputStream);
		
	}
	
}
