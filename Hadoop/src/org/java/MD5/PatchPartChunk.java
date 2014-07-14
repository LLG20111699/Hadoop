package org.java.MD5;
/*
 * 特殊的补丁快，里面存放的时与服务器一直的数据
 * 所以只需要存放编号
 */
public class PatchPartChunk extends PatchPart{
	private int index;//编号
	private int size;//大小
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


}
