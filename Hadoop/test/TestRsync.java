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
		//算出校验和
		Map<Integer,List<Chunk>> checkSums = Rsync.calcCheckSum("G:\\RsyncTest\\remote.txt");
		
		//2.
		//生成补丁
		File file = new File("G:\\RsyncTest\\local.txt");
		Patch patch = Rsync.createPatch(checkSums, "G:\\RsyncTest\\local.txt",0,file.length());//生成补丁
			
		//3.
		//生成文件
		Rsync.createNewFile(patch,"G:\\RsyncTest\\remote.txt");
		
		
		long end = System.currentTimeMillis();
		System.out.println("运行时间： " + (end-start));
	
	}
	
		
	

}
