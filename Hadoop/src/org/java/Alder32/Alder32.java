package org.java.Alder32;

/*
 * 
 * 新的A = 原来的A - D（1） + D（n + 1）
 * 新的B = 新的A + 原来的B - n*D(1) - 1 
 */
import java.io.UnsupportedEncodingException;

import com.hw.code.*;

public class Alder32 {
	public static void main(String[] args)
	{
		byte bData[] = new byte[1024];
		int iResult;
		String string;
		
				
		System.out.println("Please Input Data");
		string = InputUtil.readString();
		
		try {
			bData = string.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		iResult = ApplyAlder32.alder32(bData);//set(bData);
		System.out.println("Result " + iResult);
		
		
		System.out.println("请输入后面的字段");
		String string2 = InputUtil.readString();
		byte bData2[] = new byte[1024];
		
		try {
			bData2 = string2.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		reset(iResult,bData,bData2);
	}
	
	public static int set(byte[] data)
	{
		int A = 1;
		int B = 0;
		int Mod = 65521;
		for(int i = 0;i < data.length;i++ )
		{
			A = A + data[i];
			B = B + A;
		}
		 A = A % Mod;
		 B = B % Mod;
		 B = B * 65536 + A; 
		 return B;
	}
	
	public static int reset(int index,byte before[], byte next[])
	{	
		/*
		 * 
		 * 新的A = 原来的A - D（1） + D（n + 1）
		 * 新的B = 新的A + 原来的B - n*D(1) - 1 
		 */
		int A = 0 ;
		int B = 0;
		int iA = 0;
		int iB = 0;
		int j = 0;
		A = index & 0xffff;
		B = index >> 16;
		for(int i = 0;i < next.length;i++)
		{
			if(j < before.length) {
				int result = (iB%65521)<<16|(iA%65521);
			   iA = A + before[j] + next[i];
			   iB =iA + B - before.length * before[j] - 1;
			   System.out.println("新的A " + iA + " 新的B " + iB +" 新的Adler32值 " + result );
			   A = iA;
			   B = iB;
			   j++;
			}
			else
			{
				return -1;
			}

		}
		
		
		
		
		
		return 0 ;
	}
}
