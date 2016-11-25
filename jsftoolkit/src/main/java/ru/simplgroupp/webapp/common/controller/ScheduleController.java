package ru.simplgroupp.webapp.common.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.ScheduleExpression;
import javax.faces.model.SelectItem;

import ru.simplgroupp.toolkit.common.IntegerRange;

public class ScheduleController  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -702610158627921305L;

	private ScheduleExpression schedule;
	
	private String yearType;
	private Integer year;
	private String yearList;
	private IntegerRange yearRange = new IntegerRange(null, null);
	
	private String monthType;
	private Integer month;
	private String monthList;
	private IntegerRange monthRange = new IntegerRange(null, null);
	
	private String dayType;
	private Integer day;
	private String dayList;
	private IntegerRange dayRange = new IntegerRange(null, null);
	
	private String dayOfWeekType;
	private Integer dayOfWeek;
	private String dayOfWeekList;
	private IntegerRange dayOfWeekRange = new IntegerRange(null, null);	
	
	private String hourType;
	private Integer hour;
	private String hourList;
	private IntegerRange hourRange = new IntegerRange(null, null);	
	
	private String minuteType;
	private Integer minute;
	private String minuteList;
	private IntegerRange minuteRange = new IntegerRange(null, null);	
	
	private String secondType;
	private Integer second;
	private String secondList;
	private IntegerRange secondRange = new IntegerRange(null, null);	
	
	private ArrayList<SelectItem> valueTypes = new ArrayList<SelectItem>(5);
	
	public void init() {
		valueTypes.add(new SelectItem("*", "любой"));
		valueTypes.add(new SelectItem("S", "указанный"));
		valueTypes.add(new SelectItem("L", "список"));
		valueTypes.add(new SelectItem("R", "интервал"));	
		valueTypes.add(new SelectItem("I", "каждый, начиная с"));		
	}

	public ScheduleExpression getSchedule() {
		return schedule;
	}

	public void setSchedule(ScheduleExpression schedule) {
		this.schedule = schedule;
	}
	
	public List<SelectItem> getValueTypes() {
		return valueTypes;
	}

	public String getYearType() {
		return yearType;
	}

	public void setYearType(String yearType) {
		this.yearType = yearType;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getMonthType() {
		return monthType;
	}

	public void setMonthType(String monthType) {
		this.monthType = monthType;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public IntegerRange getYearRange() {
		return yearRange;
	}

	public String getYearList() {
		return yearList;
	}

	public void setYearList(String yearList) {
		this.yearList = yearList;
	}

	public String getMonthList() {
		return monthList;
	}

	public void setMonthList(String monthList) {
		this.monthList = monthList;
	}

	public String getDayType() {
		return dayType;
	}

	public void setDayType(String dayType) {
		this.dayType = dayType;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public String getDayList() {
		return dayList;
	}

	public void setDayList(String dayList) {
		this.dayList = dayList;
	}

	public String getDayOfWeekType() {
		return dayOfWeekType;
	}

	public void setDayOfWeekType(String dayOfWeekType) {
		this.dayOfWeekType = dayOfWeekType;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getDayOfWeekList() {
		return dayOfWeekList;
	}

	public void setDayOfWeekList(String dayOfWeekList) {
		this.dayOfWeekList = dayOfWeekList;
	}

	public String getHourType() {
		return hourType;
	}

	public void setHourType(String hourType) {
		this.hourType = hourType;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public String getHourList() {
		return hourList;
	}

	public void setHourList(String hourList) {
		this.hourList = hourList;
	}

	public String getMinuteType() {
		return minuteType;
	}

	public void setMinuteType(String minuteType) {
		this.minuteType = minuteType;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public String getMinuteList() {
		return minuteList;
	}

	public void setMinuteList(String minuteList) {
		this.minuteList = minuteList;
	}

	public String getSecondType() {
		return secondType;
	}

	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}

	public Integer getSecond() {
		return second;
	}

	public void setSecond(Integer second) {
		this.second = second;
	}

	public String getSecondList() {
		return secondList;
	}

	public void setSecondList(String secondList) {
		this.secondList = secondList;
	}

	public IntegerRange getMonthRange() {
		return monthRange;
	}

	public IntegerRange getDayRange() {
		return dayRange;
	}

	public IntegerRange getDayOfWeekRange() {
		return dayOfWeekRange;
	}

	public IntegerRange getHourRange() {
		return hourRange;
	}

	public IntegerRange getMinuteRange() {
		return minuteRange;
	}

	public IntegerRange getSecondRange() {
		return secondRange;
	}

	public void setYearRange(IntegerRange yearRange) {
		this.yearRange = yearRange;
	}

	public void setValueTypes(ArrayList<SelectItem> valueTypes) {
		this.valueTypes = valueTypes;
	}
}
