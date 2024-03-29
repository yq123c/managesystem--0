package com.yeqiu.sys.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author 陆昌
 * @time 创建于2019年1月18日上午9:07:57
 * 说明： md5生成工具
 */
public class MD5Util {
	private final static Log log = LogFactory.getLog(MD5Util.class);
	static MessageDigest md = null;
	/**
	 * 静态加载md5实例
	 */
	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ne) {
			log.error("NoSuchAlgorithmException: md5", ne);
		}
	}
	/**
	 * 获取文件的md5值
	 * @param file 文件
	 * @return 该文件的md5值
	 */
	public static String getMD5Value(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}

			return new String(Hex.encodeHex(md.digest()));
		} catch (FileNotFoundException e) {
			log.error("md5 file " + file.getAbsolutePath() + " failed:" + e.getMessage());
			return null;
		} catch (IOException e) {
			log.error("md5 file " + file.getAbsolutePath() + " failed:" + e.getMessage());
			return null;
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取字符串的md5值
	 * @param msg 字符串
	 * @return 该字符串的md5值
	 */
	public static String getMD5Value(String msg) {
		return DigestUtils.md5Hex(msg);
	}	
}
