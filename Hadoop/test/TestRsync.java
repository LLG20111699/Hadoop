import java.io.File;
import java.util.List;
import java.util.Map;

import org.java.MD5.Chunk;
import org.java.MD5.Patch;
import org.java.MD5.Rsync;


public class TestRsync {
	
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		//1.
		//���У���
		Map<Integer,List<Chunk>> checkSums = Rsync.calcCheckSum("G:\\RsyncTest\\remote.txt");
		
		//2.
		//���ɲ���
		File file = new File("G:\\RsyncTest\\local.txt");
		Patch patch = Rsync.createPatch(checkSums, "G:\\RsyncTest\\local.txt",0,file.length());//���ɲ���
			
		//3.
		//�����ļ�
		Rsync.createNewFile(patch,"G:\\RsyncTest\\remote.txt");
		
		
		long end = System.currentTimeMillis();
		System.out.println("����ʱ�䣺 " + (end-start));
	
	}
	
		
	

}
