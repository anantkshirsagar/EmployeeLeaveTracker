package com.elt.model;

import java.util.Date;

import com.elt.constants.ApproveStatus;

public class LeaveRequest {
	
	private Long id;
	private Date fromDate;
	private Date toDate;
	private int days;
	private Long employeeId;
	private String leaveReason;
	private ApproveStatus approveStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public ApproveStatus getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(ApproveStatus approveStatus) {
		this.approveStatus = approveStatus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LeaveRequest [id=");
		builder.append(id);
		builder.append(", fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", days=");
		builder.append(days);
		builder.append(", employeeId=");
		builder.append(employeeId);
		builder.append(", leaveReason=");
		builder.append(leaveReason);
		builder.append(", approveStatus=");
		builder.append(approveStatus);
		builder.append("]");
		return builder.toString();
	}
}
