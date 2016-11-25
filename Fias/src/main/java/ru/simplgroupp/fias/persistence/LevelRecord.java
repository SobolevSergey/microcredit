package ru.simplgroupp.fias.persistence;

import ru.simplgroupp.fias.persistence.Level;

/**
 * Представляет один уровень адреса, например регион или город. Используется для представления адреса в виде уровней.
 * @author irina
 *
 */
public class LevelRecord {
	int nlevel;
	String ID;
	String AOGUID; // GUID адресного объекта
	Level aoLevel;
	String name;
	String code;
	String type;
	String typeName;
	
	public int getNlevel() {
		return nlevel;
	}
	public void setNlevel(int nlevel) {
		this.nlevel = nlevel;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAOGUID() {
		return AOGUID;
	}
	public void setAOGUID(String aOGUID) {
		AOGUID = aOGUID;
	}
	public Level getAoLevel() {
		return aoLevel;
	}
	public void setAoLevel(Level aoLevel) {
		this.aoLevel = aoLevel;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
