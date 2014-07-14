package org.java.MD5;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import java.util.HashMap;






import org.java.Alder32.ApplyAlder32;

/*
 * 
 */
public class Rsync {
	/**
	 * 根据补丁与文件名在服务器上生成一个新的文件
	 * @param patch补丁
	 * @param fileName文件名
	 * @throws Exception 
	 */
	
	public static void createNewFile(Patch patch,String fileName) throws Exception
	{
		File file = new File(fileName);
		RandomAccessFile src = new RandomAccessFile(fileName,"r");//原始文件
		RandomAccessFile dst = new RandomAccessFile("G:\\RsyncTest\\target_" +file.getName() ,"rw");//打好补丁之后新生成的文件
		for(PatchPart part:patch.getPatchParts())
		{
			if(part instanceof PatchPartData)
			{
				PatchPartData patchPartData = (PatchPartData)part;
				dst.write(patchPartData.getDatas());
			}else{
				PatchPartChunk patchPartChunk = (PatchPartChunk)part;
				src.seek(patchPartChunk.getIndex()*Constant.CHUNK_SIZE);		
				byte[] buffer = new byte[patchPartChunk.getSize()];
				src.read(buffer);
				dst.write(buffer);
			}
		}
		src.close();
		dst.close();
	}
	
	/*
	 * 根据文件名，创建补丁，
	 * 校验和
	 * 文件名
	 * 开始位置
	 * 结束位置
	 * ,long end
	 */
	public static Patch createPatch(Map<Integer,List<Chunk>> checkSums,String fileName,long start,long end) throws Exception{
		Patch patch = new Patch();
		// 文件的位置
		File file = new File(fileName);
		long length = file.length();
		if (start >= length) {
			return null;
		}

		ArrayList<Byte> diffDatas = new ArrayList<Byte>();// 专门存不7致的数据
		
		while (start < length) {
			byte[] buffer = readChunk(fileName, start);

			PatchPart patchPart = matchCeckSum(checkSums, buffer);// 是否匹配上
			if (patchPart != null)// 匹配上
			{
				if (diffDatas.size() > 0)// 有不一致的数据
				{
					PatchPartData patchPartData = new PatchPartData();
					// diffDatas.toArray();
					byte[] temp = new byte[diffDatas.size()];
					for (int i = 0; i < diffDatas.size(); i++) {
						temp[i] = diffDatas.get(i);
					}
					patchPartData.setDatas(temp);
					patch.addPatchPart(patchPartData);// 先加不一致的数据
					diffDatas.clear();

				}
				patch.addPatchPart(patchPart);// 再匹配上的
				start = start + buffer.length;
				if (start >= length) {
					return patch;
				}
				continue;

			} else {// 未匹配上
				start = start + 1;//向右移动一个字节
				if(start >= length)
				{
					PatchPartData patchPartData = new PatchPartData();
					byte[] temp = new byte[diffDatas.size() + buffer.length];
					//先把不一致的数据加入到temp
					for (int i = 0; i < diffDatas.size(); i++) {
						temp[i] = diffDatas.get(i);
					}
					System.arraycopy(buffer, 0, temp, diffDatas.size(), buffer.length);
					patchPartData.setDatas(temp);
					patch.addPatchPart(patchPartData);
					return patch;
					}
				diffDatas.add(buffer[0]);
				continue;
			}
		}
		return patch;
		
	}
	
	
	
	public static PatchPart matchCeckSum(Map<Integer,List<Chunk>> checkSums,byte[] buffer){
		
		int weakCheckSum = calcWeakCheckSum(buffer, buffer.length);
		if(checkSums.containsKey(weakCheckSum))
		{
			List<Chunk> strongCheckSums = checkSums.get(weakCheckSum);//提出强校验和
			String strongCheckSum = calcStrongCheckSum(buffer, buffer.length);
			
			for(Chunk chunk:strongCheckSums)
			{
				if(strongCheckSum.equals(chunk.getStrongCheckSum()))
				{
					PatchPartChunk patchPartChunk = new PatchPartChunk();
					patchPartChunk.setIndex(chunk.getIndex());
					//目前还未处理不一直的数据
					return(patchPartChunk);//将patchPartChunk加入到补丁快中,只有这一种情况会匹配上
				}
			}
			
		}
		return null;
		
	}
	
	
	/*
	 * 对文件分块，计算每块的弱校验和与强校验和filename文件名return以弱校验和为key,强校验和组成的链表为value的数据结构
	 */
	public static Map<Integer, List<Chunk>> calcCheckSum(String fileName) {

		Map<Integer, List<Chunk>> checkSums = new HashMap<Integer, List<Chunk>>();

		// 文件分块
		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//long length = file.length();
		byte[] buffer = new byte[Constant.CHUNK_SIZE];

		int read = 0;
		int index = 0;
		try {
			while ((read = fis.read(buffer)) != -1) {
				Chunk chunk = new Chunk();
				chunk.setIndex(index);// 设置chunk的编号，从0开始
				index++;
				chunk.setSize(read);// 设置chunk的大小
				
				int weakCheckSum = calcWeakCheckSum(buffer, read);//计算弱校验和
				String strongCheckSum = calcStrongCheckSum(buffer, read);//计算强校验和
				
				chunk.setWeakCheckSum(weakCheckSum);//设置若校验和
				chunk.setStrongCheckSum(strongCheckSum);//设置强校验和
				
				//如果checkSums包含有此若校验和。那么把链表取出，将新的chunk加入链表
				if (checkSums.containsKey(weakCheckSum)) {
					List<Chunk> chunks = checkSums.get(weakCheckSum);
					chunks.add(chunk);
				} else {
					List<Chunk> chunks = new ArrayList<Chunk>();
					chunks.add(chunk);
					checkSums.put(weakCheckSum, chunks);
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checkSums;
	}

	private static int calcWeakCheckSum(byte[] datas, int size) {
		/*byte[] temp = new byte[size];
		for (int i = 0; i < size; i++) {
			temp[i] = datas[i];

		}*/
		return ApplyAlder32.alder32(datas);
	}

	private static String calcStrongCheckSum(byte[] datas, int size) {
		/*byte[] temp = new byte[size];
		for (int i = 0; i < size; i++) {
			temp[i] = datas[i];

		}*/

		return MD5.testMD5(datas);
	}

	
	private static byte[] readChunk(String fileName,long start) throws Exception//,long end
	{
		RandomAccessFile raf = new RandomAccessFile(new File(fileName), "rw");
		raf.seek(start);		
		byte[] temp = new byte[Constant.CHUNK_SIZE];
		int read = raf.read(temp, 0, Constant.CHUNK_SIZE);
		byte[] buffer = new byte[read];
		System.arraycopy(temp, 0, buffer, 0, read);
		raf.close();
		return buffer;
	}
	
	
	
	
	
}
