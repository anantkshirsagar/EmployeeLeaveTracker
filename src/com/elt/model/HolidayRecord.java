package com.elt.model;

import java.util.Date;

public class HolidayRecord {
	
	private Long id;
	private String holidayName;
	private Date holidayDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HolidayRecord [id=");
		builder.append(id);
		builder.append(", holidayName=");
		builder.append(holidayName);
		builder.append(", holidayDate=");
		builder.append(holidayDate);
		builder.append("]");
		return builder.toString();
	}
}
