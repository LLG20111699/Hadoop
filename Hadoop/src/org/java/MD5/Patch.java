package org.java.MD5;

import java.util.ArrayList;

/*
 * ����
 */
public class Patch {
	private ArrayList<PatchPart> patchParts = new ArrayList<PatchPart>();
	//��patchParts�����patchPart
	public void addPatchPart(PatchPart patchPart)
	{
		this.patchParts.add(patchPart);
	}

	public ArrayList<PatchPart> getPatchParts() {
		return patchParts;
	}

	public void setPatchParts(ArrayList<PatchPart> patchParts) {
		this.patchParts = patchParts;
	}
	

}
