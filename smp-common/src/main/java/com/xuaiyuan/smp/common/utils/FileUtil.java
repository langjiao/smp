package com.xuaiyuan.smp.common.utils;

import com.google.common.base.Preconditions;
import com.xuaiyuan.smp.common.constant.SmpConstant;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件相关操作
 * @author lj
 * @date 2019.12.18
 */
public class FileUtil {
	private static final int BUFFER = 1024 * 8;
	/**
	 * 获取项目根路径
	 * @return
	 */
	public static String getProjectPath() {
		String classPath = getClassPath();
		return new File(classPath).getParentFile().getParentFile().getAbsolutePath();
	}
	/**
	 * 获取类路径
	 * @return
	 */
	public static String getClassPath() {
		String classPath = FileUtil.class.getClassLoader().getResource("").getPath();
		return classPath;
	}
	/**
	 * 文件下载
	 *
	 * @param filePath 待下载文件路径
	 * @param fileName 下载文件名称
	 * @param delete   下载后是否删除源文件
	 * @param response HttpServletResponse
	 * @throws Exception Exception
	 */
	public static void download(String filePath, String fileName, Boolean delete, HttpServletResponse response) throws Exception {
		File file = new File(filePath);
		if (!file.exists())
			throw new Exception("文件未找到");

		String fileType = getFileType(file);
		if (!fileTypeIsValid(fileType)) {
			throw new Exception("暂不支持该类型文件下载");
		}
		response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "utf-8"));
		response.setContentType("multipart/form-data");
		response.setCharacterEncoding("utf-8");
		try (InputStream inputStream = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} finally {
			if (delete)
				delete(filePath);
		}
	}
	/**
	 * 读取txt文件的内容
	 * @param file 想要读取的文件路径
	 * @return 返回文件内容
	 */
	public static String readFile(String file){
		return readFile(new File(file));
	}
	
    /**
     * 读取txt文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String readFile(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){
            	//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
	/**
	 * 压缩文件或目录
	 *
	 * @param fromPath 待压缩文件或路径
	 * @param toPath   压缩文件，如 xx.zip
	 */
	public static void compress(String fromPath, String toPath) throws IOException {
		File fromFile = new File(fromPath);
		File toFile = new File(toPath);
		if (!fromFile.exists()) {
			throw new FileNotFoundException(fromPath + "不存在！");
		}
		try (
				FileOutputStream outputStream = new FileOutputStream(toFile);
				CheckedOutputStream checkedOutputStream = new CheckedOutputStream(outputStream, new CRC32());
				ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream)
		) {
			String baseDir = "";
			compress(fromFile, zipOutputStream, baseDir);
		}
	}
	/**
	 * 递归删除文件或目录
	 *
	 * @param filePath 文件或目录
	 */
	public static void delete(String filePath) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) Arrays.stream(files).forEach(f -> delete(f.getPath()));
		}
		file.delete();
	}
	/**
	 * 递归删除文件
	 * @param file
	 */
	public static void deleteFile(File file) {
		// 判断是否是一个目录, 不是的话跳过, 直接删除; 如果是一个目录, 先将其内容清空.
		if(file.isDirectory()) {
			// 获取子文件/目录
			File[] subFiles = file.listFiles();
			// 遍历该目录
			for (File subFile : subFiles) {
				// 递归调用删除该文件: 如果这是一个空目录或文件, 一次递归就可删除.
				// 如果这是一个非空目录, 多次递归清空其内容后再删除
				deleteFile(subFile);
			}
		}
		// 删除空目录或文件
		file.delete();
	}
	/**
	 * 获取文件类型
	 *
	 * @param file 文件
	 * @return 文件类型
	 * @throws Exception Exception
	 */
	private static String getFileType(File file) throws Exception {
		Preconditions.checkNotNull(file);
		if (file.isDirectory()) {
			throw new Exception("file不是文件");
		}
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	/**
	 * 校验文件类型是否是允许下载的类型
	 * @param fileType fileType
	 * @return Boolean
	 */
	private static Boolean fileTypeIsValid(String fileType) {
		Preconditions.checkNotNull(fileType);
		fileType = StringUtils.lowerCase(fileType);
		return ArrayUtils.contains(SmpConstant.VALID_FILE_TYPE, fileType);
	}
	private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
		if (file.isDirectory()) {
			compressDirectory(file, zipOut, baseDir);
		} else {
			compressFile(file, zipOut, baseDir);
		}
	}
	private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
		File[] files = dir.listFiles();
		if (files != null && ArrayUtils.isNotEmpty(files)) {
			for (File file : files) {
				compress(file, zipOut, baseDir + dir.getName() + "/");
			}
		}
	}
	private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
		if (!file.exists()) {
			return;
		}
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
			ZipEntry entry = new ZipEntry(baseDir + file.getName());
			zipOut.putNextEntry(entry);
			int count;
			byte[] data = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				zipOut.write(data, 0, count);
			}
		}
	}
}
