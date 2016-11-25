package ru.simplgroupp.rules.watched;

public class FieldInfo {
	private String name;
	private boolean watchCorrectedSymbols;
	private int numCorrectedSymbols;	
	
	public FieldInfo() {
		super();
	}
	
	public FieldInfo(String aname) {
		super();
		name = aname;
		watchCorrectedSymbols = false;
		numCorrectedSymbols = 0;
	}
	
	public boolean isWatchCorrectedSymbols() {
		return watchCorrectedSymbols;
	}
	public void setWatchCorrectedSymbols(boolean watchCorrectedSymbols) {
		this.watchCorrectedSymbols = watchCorrectedSymbols;
	}
	public int getNumCorrectedSymbols() {
		return numCorrectedSymbols;
	}
	public void setNumCorrectedSymbols(int numCorrectedSymbols) {
		this.numCorrectedSymbols = numCorrectedSymbols;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
