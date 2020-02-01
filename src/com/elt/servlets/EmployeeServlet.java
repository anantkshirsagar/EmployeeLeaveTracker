package com.elt.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elt.constants.AppConstants;
import com.elt.constants.ApproveStatus;
import com.elt.constants.CallType;
import com.elt.model.Employee;
import com.elt.model.EmployeeWiseLeaveRecord;
import com.elt.model.HolidayRecord;
import com.elt.model.LeaveRequest;
import com.elt.service.AdminService;
import com.elt.service.EmployeeService;
import com.elt.service.LeaveRequestService;
import com.elt.utils.AppUtils;
import com.elt.utils.DatabaseUtils;
import com.google.gson.Gson;

public class EmployeeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = null;
		try {
			String callTypeStr = request.getHeader("callType");
			String userEmailId = request.getHeader("userEmailId");
			LOG.debug("Header params userEmailId {}, callType {}", userEmailId, callTypeStr);
			CallType callType = CallType.valueOf(callTypeStr);
			out = response.getWriter();
			Gson gson = AppUtils.getGsonInstance();
			EmployeeService employeeService = new EmployeeService();
			AdminService adminService = new AdminService();
			LeaveRequestService leaveRequestService = new LeaveRequestService();
			String leaveStatus = null;

			switch (callType) {
			case EMPLOYEE_REGISTER_SERVICE:
				Employee employee = (Employee) AppUtils.mapToClass(request, Employee.class);
				LOG.debug("Employee {}", employee);
				employeeService.addEmployee(employee);
				break;
			case VALIDATE_EMPLOYEE:
				Employee emp = (Employee) AppUtils.mapToClass(request, Employee.class);
				LOG.debug("Employee {}", emp);
				boolean validEmployee = employeeService.isValidEmployee(emp);
				boolean isManager = employeeService.isManager(emp);
				if (validEmployee && isManager) {
					out.print(AppConstants.MANAGER);
				} else if (validEmployee) {
					out.print(AppConstants.VALID_USER);
				} else {
					out.print(AppConstants.INVALID_USER);
				}
				break;
			case GET_ALL_LEAVE_COUNTS:
				Employee employeeLeaveCounts = employeeService.getEmployeeRecordByEmail(userEmailId);
				response.setHeader("approvedLeavesCount", String.valueOf(employeeLeaveCounts.getApprovedLeavesCount()));
				response.setHeader("pendingLeavesCount", String.valueOf(employeeLeaveCounts.getPendingLeavesCount()));
				response.setHeader("remainingLeavesCount",
						String.valueOf(employeeLeaveCounts.getRemainingLeavesCount()));
				break;
			case GET_HOLIDAY_RECORD_GRID_INFO:
				List<HolidayRecord> holidayRecordGridInfo = adminService.getHolidayRecordGridInfo();
				if (!CollectionUtils.isEmpty(holidayRecordGridInfo)) {
					LOG.debug("Holiday record grid info {}", holidayRecordGridInfo);
					out.print(gson.toJson(holidayRecordGridInfo));
				}
				break;
			case APPLY_LEAVE:
				String fromDateStr = request.getHeader(AppConstants.fromDate);
				String toDateStr = request.getHeader(AppConstants.toDate);
				String reason = request.getHeader(AppConstants.reason);
				LOG.debug("fromDate {}, toDate {}, reason {}", fromDateStr, toDateStr, reason);

				Date fromDate = AppUtils.getDateFromString(fromDateStr);
				Date toDate = AppUtils.getDateFromString(toDateStr);
				if (AppUtils.validateDateRange(fromDate, toDate)) {
					int days = AppUtils.daysBetween(fromDate, toDate);
					Employee employeeRecord = employeeService.getEmployeeRecordByEmail(userEmailId);
					if ((AppUtils.getPendingAndApprovedLeavesAddition(employeeRecord) + days) > employeeRecord
							.getRemainingLeavesCount()) {
						out.print(AppConstants.INVALID_LEAVE_COUNT);
						return;
					}
					LeaveRequest leaveRequest = AppUtils.getLeaveRequest(fromDate, toDate, reason, days,
							ApproveStatus.PENDING, employeeRecord.getId());
					leaveRequestService.addLeaveRequest(leaveRequest);

					int pendingLeaves = employeeRecord.getPendingLeavesCount() + leaveRequest.getDays();
					int remainingLeaves = employeeRecord.getRemainingLeavesCount() - leaveRequest.getDays();
					employeeRecord.setPendingLeavesCount(pendingLeaves);
					employeeRecord.setRemainingLeavesCount(remainingLeaves);
					employeeService.updateEmployeeLeaveRecords(employeeRecord);
					out.print(leaveRequest);
				} else {
					out.print(AppConstants.INVALID_DATE_RANGE);
				}
				break;

			case GET_EMPLOYEE_LEAVE_GRID_INFO:
				Employee employeeRecordByEmail = employeeService.getEmployeeRecordByEmail(userEmailId);
				List<LeaveRequest> leaveRequests = leaveRequestService
						.getLeaveRequestsByEmployeeId(employeeRecordByEmail.getId());
				LOG.debug("Employee leave requests {}", leaveRequests);
				out.print(gson.toJson(leaveRequests));
				break;
			case CANCEL_EMPLOYEE_LEAVE:
				LeaveRequest leaveRequestFromUI = (LeaveRequest) AppUtils.mapToClass(request, LeaveRequest.class);
				LOG.debug("Leave request object {}", leaveRequestFromUI);
				leaveRequestService.deleteLeaveRequest(leaveRequestFromUI);

				Employee currentEmployee = employeeService.getEmployeeRecordByEmail(userEmailId);
				currentEmployee.setRemainingLeavesCount(
						currentEmployee.getRemainingLeavesCount() + leaveRequestFromUI.getDays());
				currentEmployee
						.setPendingLeavesCount(currentEmployee.getPendingLeavesCount() - leaveRequestFromUI.getDays());
				employeeService.updateEmployeeLeaveRecords(currentEmployee);
				out.print(AppConstants.SUCCESS_MESSAGE);
				break;
			case GET_CURRENT_LOGIN_EMP_DETAILS:
				Employee empRecord = employeeService.getEmployeeRecordByEmail(userEmailId);
				out.print(gson.toJson(empRecord));
				break;
			case GET_EMPLOYEE_WISE_LEAVE_REQUEST:
				List<EmployeeWiseLeaveRecord> employeeWiseLeaveRequest = employeeService.getEmployeeWiseLeaveRequest();
				if (CollectionUtils.isNotEmpty(employeeWiseLeaveRequest)) {
					LOG.debug("Employee wise leave request count {}", employeeWiseLeaveRequest.size());
					out.print(gson.toJson(employeeWiseLeaveRequest));
				}
				break;
			case APPROVE_LEAVE_OF_EMPLOYEE:
				leaveStatus = request.getHeader(AppConstants.leaveStatus);
				LeaveRequest leaveReqApproveObj = (LeaveRequest) AppUtils.mapToClass(request, LeaveRequest.class);
				LOG.debug("Leave status {}, Leave request object {}", leaveStatus, leaveReqApproveObj);
				DatabaseUtils.approveLeaveRequest(employeeService, leaveRequestService, leaveReqApproveObj,
						leaveStatus);
				out.print(AppConstants.SUCCESS_MESSAGE);
				break;
			case CANCEL_LEAVE_OF_EMPLOYEE:
				leaveStatus = request.getHeader(AppConstants.leaveStatus);
				LeaveRequest leaveReqCancelObj = (LeaveRequest) AppUtils.mapToClass(request, LeaveRequest.class);
				LOG.debug("Leave status {}, Leave request object {}", leaveStatus, leaveReqCancelObj);
				DatabaseUtils.cancelLeaveRequest(employeeService, leaveRequestService, leaveReqCancelObj, leaveStatus);
				out.print(AppConstants.SUCCESS_MESSAGE);
				break;
			}
		} catch (Exception e) {
			out.print(AppConstants.FAILURE_MESSAGE);
			LOG.error("Servlet Exception {}", e);
		}
	}
}
