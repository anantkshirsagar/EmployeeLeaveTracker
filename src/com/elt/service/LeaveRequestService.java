package com.elt.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elt.constants.ApproveStatus;
import com.elt.model.LeaveRequest;

public class LeaveRequestService extends AbstractDBService {

	private static final Logger LOG = LoggerFactory.getLogger(LeaveRequestService.class);

	public void addLeaveRequest(LeaveRequest leaveRequest) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "insert into leave_request(from_date, to_date, days, employee_id, leave_reason, approve_status) values(?,?,?,?,?,?)";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setDate(1, new java.sql.Date(leaveRequest.getFromDate().getTime()));
		prepareStatement.setDate(2, new java.sql.Date(leaveRequest.getToDate().getTime()));
		prepareStatement.setInt(3, leaveRequest.getDays());
		prepareStatement.setLong(4, leaveRequest.getEmployeeId());
		prepareStatement.setString(5, leaveRequest.getLeaveReason());
		prepareStatement.setInt(6, leaveRequest.getApproveStatus().ordinal());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Leave request inserted {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void updateLeaveRequest(LeaveRequest leaveRequest) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update leave_request set from_date = ?, to_date = ?, days = ?, employee_id = ?, leave_reason = ?, approve_status = ? where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setDate(1, new java.sql.Date(leaveRequest.getFromDate().getTime()));
		prepareStatement.setDate(2, new java.sql.Date(leaveRequest.getToDate().getTime()));
		prepareStatement.setInt(3, leaveRequest.getDays());
		prepareStatement.setLong(4, leaveRequest.getEmployeeId());
		prepareStatement.setString(5, leaveRequest.getLeaveReason());
		prepareStatement.setInt(6, leaveRequest.getApproveStatus().ordinal());
		prepareStatement.setLong(7, leaveRequest.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Leave request cancelled {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public int getApproveStatusCount(ApproveStatus approveStatus, long employeeId)
			throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select sum(days) as approveDaysCount from leave_request where employee_id = ? and approve_status = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, employeeId);
		prepareStatement.setInt(2, approveStatus.ordinal());
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		int approveStatusCount = 0;
		if (resultSet.next()) {
			approveStatusCount = resultSet.getInt("approveDaysCount");
		}
		connectionSettings.closeConnection();
		return approveStatusCount;
	}

	public int getApproveStatusCountForDateRange(ApproveStatus approveStatus, long employeeId, Date fromDate,
			Date toDate) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select sum(days) as leaveCounts from leave_Request where employee_id = ? and from_date = ? and to_date = ? and approve_status = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, employeeId);
		prepareStatement.setDate(2, new java.sql.Date(fromDate.getTime()));
		prepareStatement.setDate(3, new java.sql.Date(toDate.getTime()));
		prepareStatement.setInt(4, approveStatus.ordinal());
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		int approveStatusCount = 0;
		if (resultSet.next()) {
			approveStatusCount = resultSet.getInt("leaveCounts");
		}
		connectionSettings.closeConnection();
		return approveStatusCount;
	}

	public void deleteLeaveRequest(LeaveRequest leaveRequest) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "delete from leave_request where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, leaveRequest.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Leave request cancelled {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	/**
	 * @param employeeId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public List<LeaveRequest> getLeaveRequestsByEmployeeId(Long employeeId) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		List<LeaveRequest> leaveRequestList = new ArrayList<LeaveRequest>();
		String query = "select * from leave_request where employee_id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, employeeId);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			LeaveRequest leaveRequest = new LeaveRequest();
			leaveRequest.setId(resultSet.getLong("id"));
			leaveRequest.setFromDate(resultSet.getDate("from_date"));
			leaveRequest.setToDate(resultSet.getDate("to_date"));
			leaveRequest.setDays(resultSet.getInt("days"));
			leaveRequest.setApproveStatus(ApproveStatus.values()[resultSet.getInt("approve_status")]);
			leaveRequest.setLeaveReason(resultSet.getString("leave_reason"));
			leaveRequest.setEmployeeId(resultSet.getLong("employee_id"));
			leaveRequestList.add(leaveRequest);
		}
		connectionSettings.closeConnection();
		return leaveRequestList;
	}

}
