package com.zte.controller;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class uploadFileController {
 
@SuppressWarnings({ "unchecked", "unused" })
@RequestMapping(value = "/doUpload.do", method = RequestMethod.POST)
	public String uploadFile(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		 String filepath ="/usr/local/apache-tomcat-8.5.5/webapps/";
		//String filepath ="/Users/Arthur/Documents/apache-tomcat-9/webapps/";
		String backfPath="http://123.57.4.104:9998";
		String message="";
		String fileFname="";
		List fileList = null;
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		try {
			fileList= upload.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //迭代器,搜索前端发送过来的文件
        Iterator<FileItem> it = fileList.iterator();
        String name = "";
        String extName = "";
        String savePath = request.getSession().getServletContext().getRealPath("");
        int i = 0;
        while (it.hasNext()) {
        	System.out.println(i++);
        		FileItem item = it.next();
        		//判断该表单项是否是普通类型
                if (!item.isFormField()) {
	                	  name = item.getName();
	                    long size = item.getSize();
	                    String type = item.getContentType();
	                    if (name == null || name.trim().equals("")) {
	                        continue;
	                    }
	                    // 扩展名格式： extName就是文件的后缀,例如 .txt
	                    if (name.lastIndexOf(".") >= 0) {
	                       extName = name.substring(name.lastIndexOf("."));
	                    }
	                    fileFname=name.substring(name.lastIndexOf("/")+1,name.lastIndexOf(".") );
	                    savePath = savePath+"/upload/";
	                    File saveFile = new File(savePath + name);
	                    try {
							item.write(saveFile);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                }
        }
        File file = new File(savePath + name);
        FileOutputStream out  = null;
        InputStream is = null;
        try {
        		 is= new FileInputStream(file); 
			 out = new FileOutputStream(filepath+name);
			 int byteRead=0;
			 byte[] buffer = new byte[1024];
			  try {
				while ((byteRead = is.read(buffer, 0,1024)) != -1) {
						  try {
							out.write(buffer, 0, byteRead);
						} catch (IOException e) {
							e.printStackTrace();
						} 
				    }
				if (extName.equals(".zip")) {
					uploadFileController.unZip(savePath + name,filepath);
				}
				File filezip=new File(filepath+name);//将解压缩前的解压缩文件地址
    				filezip.delete();//删除上传的压缩包，只剩下解压文件
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				 try {
					 if(null!=out){
						 out.close();
					 }
					 if(null!=is){
						 is.close();
					 }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			message+="上传成功";
			model.addAttribute("path",backfPath+"/"+fileFname);
			model.addAttribute("message",message);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			message="上传失败";
			model.addAttribute("path","");
			model.addAttribute("message",message);
		}
        return "scjg";
	}
/**使用GBK编码可以避免压缩中文文件名乱码*/  
private static final String CHINESE_CHARSET = "utf-8";  
/**文件读取缓冲区大小*/  
private static final int CACHE_SIZE = 1024;  
/** 
 * 解压压缩包 
 * @param zipFilePath 压缩文件路径 
 * @param destDir 解压目录 
 */  
public static void unZip(String zipFilePath, String destDir) {  
    ZipFile zipFile = null;  
    try {  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        BufferedOutputStream bos = null;  
        zipFile = new ZipFile(zipFilePath, CHINESE_CHARSET);  
        Enumeration<ZipEntry> zipEntries = zipFile.getEntries();  
        File file, parentFile;  
        ZipEntry entry;  
        byte[] cache = new byte[CACHE_SIZE];  
        while (zipEntries.hasMoreElements()) {  
            entry = (ZipEntry) zipEntries.nextElement();  
            if (entry.isDirectory()) {  
                new File(destDir + entry.getName()).mkdirs();  
                continue;  
            }  
            bis = new BufferedInputStream(zipFile.getInputStream(entry));  
            file = new File(destDir + entry.getName());  
            parentFile = file.getParentFile();  
            if (parentFile != null && (!parentFile.exists())) {  
                parentFile.mkdirs();  
            }  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos, CACHE_SIZE);  
            int readIndex = 0;  
            while ((readIndex = bis.read(cache, 0, CACHE_SIZE)) != -1) {  
                fos.write(cache, 0, readIndex);  
            }  
            bos.flush();  
            bos.close();  
            fos.close();  
            bis.close();  
        }  
    } catch (IOException e) {  
        e.printStackTrace();  
    }finally{  
        try {  
            zipFile.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}  
}
