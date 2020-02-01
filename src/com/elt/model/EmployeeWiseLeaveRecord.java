package com.elt.model;

import java.util.List;

public class EmployeeWiseLeaveRecord {
	
	private Employee employee;
	private List<LeaveRequest> leaveRequests;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}
}
