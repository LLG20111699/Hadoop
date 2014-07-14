package org.java.Alder32;

public  class ApplyAlder32 {
	
	public static final int MOD = 65521;
	
	public static int alder32(byte[] datas)
	{
		int A = 1;
		int B = 0;
		for(byte b:datas)
		{
			A = A + b;
			B = B + A; 
		}
		A = A % MOD;
		B = B % MOD;
		
		return B<<16|A ;
	}
	
	public static int nextAdler32(int adler32,byte preByte,byte nextByte,int length)
	{
		int oldA = adler32&0xffff;
		int oldB = (adler32>>16)&0xffff;
		int A = oldA - preByte + nextByte;
		int B = A + oldB - length*preByte -1;
		
		return (B%MOD)<<16|(A%MOD);
	}
	
}
