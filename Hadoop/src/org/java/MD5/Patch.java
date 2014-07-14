package org.java.MD5;

import java.util.ArrayList;

/*
 * ≤π∂°
 */
public class Patch {
	private ArrayList<PatchPart> patchParts = new ArrayList<PatchPart>();
	//œÚpatchParts÷–ÃÌº”patchPart
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
