package org.java.MD5;
/*
 * ����Ĳ����죬�����ŵ�ʱ�������һֱ������
 * ����ֻ��Ҫ��ű��
 */
public class PatchPartChunk extends PatchPart{
	private int index;//���
	private int size;//��С
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
