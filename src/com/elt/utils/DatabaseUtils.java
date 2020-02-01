package com.elt.utils;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.connection.setting.AbstractConnectionSettings;
import com.dbmanager.connection.setting.ConnectionSettings;
import com.dbmanager.property.util.PropertyReader;
import com.elt.constants.ApproveStatus;
import com.elt.model.Employee;
import com.elt.model.LeaveRequest;
import com.elt.service.EmployeeService;
import com.elt.service.LeaveRequestService;

public class DatabaseUtils {

	private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtils.class);

	private DatabaseUtils() {
	}

	public static AbstractConnectionSettings getConnectionSettings(String filePath) throws IOException {
		PropertyReader propertyReader = new PropertyReader(new File(filePath));
		return new ConnectionSettings(propertyReader.propertiesReader());
	}

	public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.close();
	}

	public static void cancelLeaveRequest(EmployeeService employeeService, LeaveRequestService leaveRequestService,
			LeaveRequest leaveRequest, String leaveStatus) throws ClassNotFoundException, SQLException {
		ApproveStatus approveStatus = ApproveStatus.valueOf(leaveStatus);
		Employee employeeRecord = employeeService.getEmployeeRecordById(leaveRequest.getEmployeeId());
		if (leaveRequest.getApproveStatus() == ApproveStatus.PENDING) {
			int pendingLeaves = leaveRequestService.getApproveStatusCountForDateRange(ApproveStatus.PENDING,
					employeeRecord.getId(), leaveRequest.getFromDate(), leaveRequest.getToDate());
			LOG.debug("pendingLeaves {}", pendingLeaves);
			employeeRecord.setRemainingLeavesCount(employeeRecord.getRemainingLeavesCount() + pendingLeaves);
			employeeRecord.setPendingLeavesCount(employeeRecord.getPendingLeavesCount() - pendingLeaves);
		} else if (leaveRequest.getApproveStatus() == ApproveStatus.APPROVE) {
			int approvedLeaves = leaveRequestService.getApproveStatusCountForDateRange(ApproveStatus.APPROVE,
					employeeRecord.getId(), leaveRequest.getFromDate(), leaveRequest.getToDate());
			LOG.debug("approvedLeaves {}", approvedLeaves);
			employeeRecord.setRemainingLeavesCount(employeeRecord.getRemainingLeavesCount() + approvedLeaves);
			employeeRecord.setApprovedLeavesCount(employeeRecord.getApprovedLeavesCount() - approvedLeaves);
		}
		employeeService.updateEmployeeLeaveRecords(employeeRecord);
		leaveRequest.setApproveStatus(approveStatus);
		leaveRequestService.updateLeaveRequest(leaveRequest);
	}

	public static void approveLeaveRequest(EmployeeService employeeService, LeaveRequestService leaveRequestService,
			LeaveRequest leaveRequest, String leaveStatus) throws ClassNotFoundException, SQLException {
		ApproveStatus approveStatus = ApproveStatus.valueOf(leaveStatus);
		Employee employeeRecord = employeeService.getEmployeeRecordById(leaveRequest.getEmployeeId());
		if (leaveRequest.getApproveStatus() == ApproveStatus.PENDING) {
			int pendingLeaves = leaveRequestService.getApproveStatusCountForDateRange(ApproveStatus.PENDING,
					employeeRecord.getId(), leaveRequest.getFromDate(), leaveRequest.getToDate());
			LOG.debug("pendingLeaves {}", pendingLeaves);
			employeeRecord.setApprovedLeavesCount(employeeRecord.getApprovedLeavesCount() + pendingLeaves);
			employeeRecord.setPendingLeavesCount(employeeRecord.getPendingLeavesCount() - pendingLeaves);
		} else if (leaveRequest.getApproveStatus() == ApproveStatus.CANCELLED) {
			int cancelledLeaves = leaveRequestService.getApproveStatusCountForDateRange(ApproveStatus.CANCELLED,
					employeeRecord.getId(), leaveRequest.getFromDate(), leaveRequest.getToDate());
			LOG.debug("cancelledLeaves {}", cancelledLeaves);
			employeeRecord.setApprovedLeavesCount(employeeRecord.getApprovedLeavesCount() + cancelledLeaves);
			employeeRecord.setRemainingLeavesCount(employeeRecord.getRemainingLeavesCount() - cancelledLeaves);
		}
		employeeService.updateEmployeeLeaveRecords(employeeRecord);
		leaveRequest.setApproveStatus(approveStatus);
		leaveRequestService.updateLeaveRequest(leaveRequest);
	}
}
