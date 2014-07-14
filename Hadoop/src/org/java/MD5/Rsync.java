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
	 * ���ݲ������ļ����ڷ�����������һ���µ��ļ�
	 * @param patch����
	 * @param fileName�ļ���
	 * @throws Exception 
	 */
	
	public static void createNewFile(Patch patch,String fileName) throws Exception
	{
		File file = new File(fileName);
		RandomAccessFile src = new RandomAccessFile(fileName,"r");//ԭʼ�ļ�
		RandomAccessFile dst = new RandomAccessFile("G:\\RsyncTest\\target_" +file.getName() ,"rw");//��ò���֮�������ɵ��ļ�
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
	 * �����ļ���������������
	 * У���
	 * �ļ���
	 * ��ʼλ��
	 * ����λ��
	 * ,long end
	 */
	public static Patch createPatch(Map<Integer,List<Chunk>> checkSums,String fileName,long start,long end) throws Exception{
		Patch patch = new Patch();
		// �ļ���λ��
		File file = new File(fileName);
		long length = file.length();
		if (start >= length) {
			return null;
		}

		ArrayList<Byte> diffDatas = new ArrayList<Byte>();// ר�Ŵ治7�µ�����
		
		while (start < length) {
			byte[] buffer = readChunk(fileName, start);

			PatchPart patchPart = matchCeckSum(checkSums, buffer);// �Ƿ�ƥ����
			if (patchPart != null)// ƥ����
			{
				if (diffDatas.size() > 0)// �в�һ�µ�����
				{
					PatchPartData patchPartData = new PatchPartData();
					// diffDatas.toArray();
					byte[] temp = new byte[diffDatas.size()];
					for (int i = 0; i < diffDatas.size(); i++) {
						temp[i] = diffDatas.get(i);
					}
					patchPartData.setDatas(temp);
					patch.addPatchPart(patchPartData);// �ȼӲ�һ�µ�����
					diffDatas.clear();

				}
				patch.addPatchPart(patchPart);// ��ƥ���ϵ�
				start = start + buffer.length;
				if (start >= length) {
					return patch;
				}
				continue;

			} else {// δƥ����
				start = start + 1;//�����ƶ�һ���ֽ�
				if(start >= length)
				{
					PatchPartData patchPartData = new PatchPartData();
					byte[] temp = new byte[diffDatas.size() + buffer.length];
					//�ȰѲ�һ�µ����ݼ��뵽temp
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
			List<Chunk> strongCheckSums = checkSums.get(weakCheckSum);//���ǿУ���
			String strongCheckSum = calcStrongCheckSum(buffer, buffer.length);
			
			for(Chunk chunk:strongCheckSums)
			{
				if(strongCheckSum.equals(chunk.getStrongCheckSum()))
				{
					PatchPartChunk patchPartChunk = new PatchPartChunk();
					patchPartChunk.setIndex(chunk.getIndex());
					//Ŀǰ��δ����һֱ������
					return(patchPartChunk);//��patchPartChunk���뵽��������,ֻ����һ�������ƥ����
				}
			}
			
		}
		return null;
		
	}
	
	
	/*
	 * ���ļ��ֿ飬����ÿ�����У�����ǿУ���filename�ļ���return����У���Ϊkey,ǿУ�����ɵ�����Ϊvalue�����ݽṹ
	 */
	public static Map<Integer, List<Chunk>> calcCheckSum(String fileName) {

		Map<Integer, List<Chunk>> checkSums = new HashMap<Integer, List<Chunk>>();

		// �ļ��ֿ�
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
				chunk.setIndex(index);// ����chunk�ı�ţ���0��ʼ
				index++;
				chunk.setSize(read);// ����chunk�Ĵ�С
				
				int weakCheckSum = calcWeakCheckSum(buffer, read);//������У���
				String strongCheckSum = calcStrongCheckSum(buffer, read);//����ǿУ���
				
				chunk.setWeakCheckSum(weakCheckSum);//������У���
				chunk.setStrongCheckSum(strongCheckSum);//����ǿУ���
				
				//���checkSums�����д���У��͡���ô������ȡ�������µ�chunk��������
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
