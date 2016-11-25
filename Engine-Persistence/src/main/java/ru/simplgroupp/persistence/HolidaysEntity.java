package ru.simplgroupp.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


import ru.simplgroupp.toolkit.common.Convertor;
import ru.simplgroupp.toolkit.common.Identifiable;
import ru.simplgroupp.toolkit.common.Initializable;

public class HolidaysEntity extends BaseEntity implements Serializable, Identifiable, Initializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -501668161377135624L;
	public static final int WEEKEND = 1;
	public static final int HOLIDAY = 2;
	
	public static final int HOUR_PM=12;
	protected Integer txVersion = 0;
	
	/**
	 * название
	 */
	private String name;
	/**
	 * дата начала
	 */
    private Date databeg;
    /**
     * дата окончания
     */
    private Date dataend;
    /**
     * вид (выходной - 1, праздник - 2)
     */
    private Integer kind;
    
    public HolidaysEntity(){
    	
    }
  	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getKind() {
		return kind;
	}
	public void setKind(Integer kind) {
		this.kind=kind;
	}
	
	public Date getDatabeg() {
        return databeg;
    }

    public void setDatabeg(Date databeg) {
        this.databeg = databeg;
    }

    public Date getDataend() {
        return dataend;
    }

    public void setDataend(Date dataend) {
        this.dataend = dataend;
    }
    
    public String getDescription(){
    	return "дата начала "+Convertor.toWriteDate(databeg)+", дата окончания "+Convertor.toWriteDate(dataend);
    }
    
 	@Override
	public void init(Set options) {
		
	}	

}
