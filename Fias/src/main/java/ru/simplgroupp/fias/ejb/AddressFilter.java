package ru.simplgroupp.fias.ejb;

import java.io.Serializable;

import ru.simplgroupp.fias.persistence.Address;

public class AddressFilter extends Address implements Serializable 
{

	public String getHouseTo() {
		return houseTo;
	}
	public void setHouseTo(String houseTo) {
		this.houseTo = houseTo;
	}
	public String getLiterTo() {
		return literTo;
	}
	public void setLiterTo(String literTo) {
		this.literTo = literTo;
	}
	public String getCorpusTo() {
		return corpusTo;
	}
	public void setCorpusTo(String corpusTo) {
		this.corpusTo = corpusTo;
	}
	public String getBuildingTo() {
		return buildingTo;
	}
	public void setBuildingTo(String buildingTo) {
		this.buildingTo = buildingTo;
	}

	public String getFlatTo() {
		return flatTo;
	}
	public void setFlatTo(String flatTo) {
		this.flatTo = flatTo;
	}

	private String houseTo;
	private String literTo;
	private String corpusTo;
	private String buildingTo;	
	private String flatTo;	
}
