package ru.simplgroupp.persistence.antifraud;

import java.io.Serializable;
import java.util.Date;

import ru.simplgroupp.persistence.BaseEntity;
/**
 * параметры для правил антифрода
 *
 */
public class RefAntifraudRulesParamsEntity extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 3068973922394008985L;

	protected Integer txVersion = 0;

	/**
	 * правило, к которму относится
	 */
	private RefAntifraudRulesEntity rulesId;
	/**
	 * название поля (параметра)
	 */
    private String name;
    /**
	 * условие для запроса
	 */
    private String condition;
    /**
	 * описание
	 */
    private String description;
    /**
   	* описание
   	*/
    private String dataType;
    /**
	 * строковое значение
	 */
    private String valueString;
    /**
     * числовое значение
     */
    private Integer valueInteger;
    /**
     * вещественное значение
     */
    private Double valueFloat;
    /**
     * значение дата
     */
    private Date valueDate;
    /**
     * параметр для запроса (1), ответа (0)
     */
    private Integer forRequest;
	/**
	 * условие группировки
	 */
	private String groupCondition;
	/**
	 * индекс группировки
	 */
	private Integer groupIndex;
 
    public RefAntifraudRulesParamsEntity(){
    	
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RefAntifraudRulesEntity getRulesId() {
		return rulesId;
	}

	public void setRulesId(RefAntifraudRulesEntity rulesId) {
		this.rulesId = rulesId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public Integer getValueInteger() {
		return valueInteger;
	}

	public void setValueInteger(Integer valueInteger) {
		this.valueInteger = valueInteger;
	}

	public Double getValueFloat() {
		return valueFloat;
	}

	public void setValueFloat(Double valueFloat) {
		this.valueFloat = valueFloat;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public Integer getForRequest() {
		return forRequest;
	}

	public void setForRequest(Integer forRequest) {
		this.forRequest = forRequest;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Integer getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(Integer groupIndex) {
		this.groupIndex = groupIndex;
	}

	public String getGroupCondition() {
		return groupCondition;
	}

	public void setGroupCondition(String groupCondition) {
		this.groupCondition = groupCondition;
	}
}
