package org.java.MD5;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.hw.code.InputUtil;

public class MD5 {

	public static void main(String[] args) {
		System.out.println("Please Input data");
		String string = InputUtil.readString();
		byte source[] = new byte[1024];
		try {
			source = string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("result: " + testMD5(source));
		
		System.out.println(Rsync.calcCheckSum("123.txt"));

	}

	public static String testMD5(byte source[]) {
		char hexDigist[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		String s = null;
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte datas[] = md.digest();

			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte b = datas[i];
				str[k++] = hexDigist[b >> 4 & 0xf];
				str[k++] = hexDigist[b & 0xf];
			}
			s = new String(str);
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;

	}

}
