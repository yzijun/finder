package com.app.finder.common.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;


public class DownloadUtils {
    /**
	 * 下载文件,不需要把下载的文件写到服务器硬盘上。
	 * @param request
	 * @param response
	 * @param displayName
	 * @param bytes
	 */
	public static void download(HttpServletRequest request, 
				HttpServletResponse response, String displayName, byte[] bytes) {
		try {

	        if (bytes == null) {
	            response.setContentType("text/html;charset=utf-8");
	            response.setCharacterEncoding("utf-8");
	            response.getWriter().write("您下载的文件不存在！");
	            return;
	        }
	
	        response.reset();
	        response.setHeader("Pragma", "No-cache");
	        response.setHeader("Cache-Control", "must-revalidate, no-transform");
	        response.setDateHeader("Expires", 0L);
	
	        response.setContentType("application/x-download");
	        response.setContentLength(bytes.length);
	
	        String displayFilename = displayName.substring(displayName.lastIndexOf("_") + 1);
            displayFilename = new String(displayFilename.getBytes("gb2312"),"ISO_8859_1");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + displayFilename + "\"");

	        OutputStream os = response.getOutputStream();
	        BufferedInputStream is = new BufferedInputStream(new ByteArrayInputStream(bytes));
            FileCopyUtils.copy(is, os);
        } catch (Exception e) {
            throw new RuntimeException("下载文件时出现异常："+e);
        } 
    }
}
