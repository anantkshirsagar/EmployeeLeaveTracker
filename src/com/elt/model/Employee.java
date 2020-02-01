package com.elt.model;

public class Employee {

	private Long id;
	private String name;
	private String city;
	private String email;
	private String password;
	private String contactNo;
	private boolean isManager;
	private Long leaveRequestId;
	private int approvedLeavesCount;
	private int pendingLeavesCount;
	private int remainingLeavesCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public boolean isManager() {
		return isManager;
	}

	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}

	public Long getLeaveRequestId() {
		return leaveRequestId;
	}

	public void setLeaveRequestId(Long leaveRequestId) {
		this.leaveRequestId = leaveRequestId;
	}

	public int getApprovedLeavesCount() {
		return approvedLeavesCount;
	}

	public void setApprovedLeavesCount(int approvedLeavesCount) {
		this.approvedLeavesCount = approvedLeavesCount;
	}

	public int getPendingLeavesCount() {
		return pendingLeavesCount;
	}

	public void setPendingLeavesCount(int pendingLeavesCount) {
		this.pendingLeavesCount = pendingLeavesCount;
	}

	public int getRemainingLeavesCount() {
		return remainingLeavesCount;
	}

	public void setRemainingLeavesCount(int remainingLeavesCount) {
		this.remainingLeavesCount = remainingLeavesCount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Employee [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", city=");
		builder.append(city);
		builder.append(", email=");
		builder.append(email);
		builder.append(", password=");
		builder.append(password);
		builder.append(", contactNo=");
		builder.append(contactNo);
		builder.append(", isManager=");
		builder.append(isManager);
		builder.append(", leaveRequestId=");
		builder.append(leaveRequestId);
		builder.append(", approvedLeavesCount=");
		builder.append(approvedLeavesCount);
		builder.append(", pendingLeavesCount=");
		builder.append(pendingLeavesCount);
		builder.append(", remainingLeavesCount=");
		builder.append(remainingLeavesCount);
		builder.append("]");
		return builder.toString();
	}
}
