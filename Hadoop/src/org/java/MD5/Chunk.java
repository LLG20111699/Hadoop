package org.java.MD5;
/*
 * 文件分块的定义
 */
public class Chunk {
	 private int weakCheckSum ;//弱校验和
	 private String strongCheckSum ;//强校验和
	 private int index;//分块编号
	 private int size;//分块的大小
	public int getWeakCheckSum() {
		return weakCheckSum;
	}
	public void setWeakCheckSum(int weakCheckSum) {
		this.weakCheckSum = weakCheckSum;
	}
	public String getStrongCheckSum() {
		return strongCheckSum;
	}
	public void setStrongCheckSum(String strongCheckSum) {
		this.strongCheckSum = strongCheckSum;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	 
	 
	 

}
