package com.elt.model;

public class CurrentYearRecord {

	private Long id;
	private int currentYear;
	private int totalLeaves;

	public CurrentYearRecord() {
		
	}
	
	public CurrentYearRecord(Long id, int currentYear, int totalLeaves) {
		this.id = id;
		this.currentYear = currentYear;
		this.totalLeaves = totalLeaves;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(int currentYear) {
		this.currentYear = currentYear;
	}

	public int getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(int totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurrentYearRecord [id=");
		builder.append(id);
		builder.append(", currentYear=");
		builder.append(currentYear);
		builder.append(", totalLeaves=");
		builder.append(totalLeaves);
		builder.append("]");
		return builder.toString();
	}
}
