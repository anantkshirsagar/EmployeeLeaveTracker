package com.elt.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elt.constants.AppConstants;
import com.elt.constants.CallType;
import com.elt.model.CurrentYearRecord;
import com.elt.model.Employee;
import com.elt.model.HolidayRecord;
import com.elt.model.LeaveRequest;
import com.elt.service.AdminService;
import com.elt.service.EmployeeService;
import com.elt.service.LeaveRequestService;
import com.elt.utils.AppUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class AdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(AdminServlet.class);

	@Override
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
			AdminService adminService = new AdminService();
			EmployeeService employeeService = new EmployeeService();

			switch (callType) {
			case ADMIN_ADD_HOLIDAY_SERVICE:
				List<HolidayRecord> holidayRecordList = AppUtils.mapToList(request,
						new TypeToken<ArrayList<HolidayRecord>>() {
						}.getType());
				LOG.debug("Holiday record list {}", holidayRecordList);
				adminService.addHolidayRecords(holidayRecordList);
				out.print(AppConstants.SUCCESS_MESSAGE);
				break;
			case GET_HOLIDAY_RECORD_GRID_INFO:
				List<HolidayRecord> holidayRecordGridInfo = adminService.getHolidayRecordGridInfo();
				if (CollectionUtils.isEmpty(holidayRecordGridInfo)) {
					out.print(AppConstants.FAILURE_MESSAGE);
				} else {
					LOG.debug("Holiday record grid info {}", holidayRecordGridInfo);
					out.print(gson.toJson(holidayRecordGridInfo));
				}
				break;
			case DELETE_ALL_HOLIDAY_RECORD_SERVICE:
				adminService.deleteAllHolidayRecord();
				out.print(AppConstants.SUCCESS_MESSAGE);
				break;
			case SAVE_CURRENT_YEAR_RECORD_SERVICE:
				String totalLeaves = request.getHeader(AppConstants.TOTAL_LEAVES_HEADER);
				if (AppUtils.isStringEmpty(totalLeaves)) {
					out.print(AppConstants.FAILURE_MESSAGE);
				} else {
					CurrentYearRecord currentYearRecord = AppUtils.getCurrentYearRecord(Integer.parseInt(totalLeaves));
					adminService.editCurrentYearRecord(currentYearRecord);
					out.print(AppConstants.SUCCESS_MESSAGE);
				}
				break;
			case GET_CURRENT_YEAR_RECORD_SERVICE:
				CurrentYearRecord currentYearRecord = adminService.getCurrentYearRecord();
				response.setHeader(AppConstants.TOTAL_LEAVES_HEADER,
						String.valueOf(currentYearRecord.getTotalLeaves()));
				response.setHeader(AppConstants.CURRENT_YEAR_HEADER,
						String.valueOf(currentYearRecord.getCurrentYear()));
				break;
			case GET_ALL_EMPLOYEES_SERVICE:
				List<Employee> employees = employeeService.getEmployees();
				out.print(gson.toJson(employees));
				break;
			case MARK_OR_UNMARK_EMPLOYEE_AS_MANAGER:
				Employee updatedEmployee = (Employee) AppUtils.mapToClass(request, Employee.class);
				LOG.debug("Updated employee {}", updatedEmployee);
				employeeService.updateIsManager(updatedEmployee);
				break;
			case GET_MANAGER_RECORD:
				Employee managerRecord = employeeService.getManagerRecord();
				out.print(gson.toJson(managerRecord));
				break;
			case GET_MANAGER_LEAVE_GRID_INFO:
				Employee employeeRecordFromUI = (Employee) AppUtils.mapToClass(request, Employee.class);
				LOG.debug("employeeRecord {}", employeeRecordFromUI);
				LeaveRequestService leaveRequestService = new LeaveRequestService();
				List<LeaveRequest> leaveRequestsOfManager = leaveRequestService
						.getLeaveRequestsByEmployeeId(employeeRecordFromUI.getId());
				LOG.debug("leaveRequestsOfManager {}", leaveRequestsOfManager);
				out.print(gson.toJson(leaveRequestsOfManager));
				break;
			}
		} catch (Exception e) {
			out.print(AppConstants.FAILURE_MESSAGE);
			LOG.error("Servlet Exception {}", e);
		}
	}
}
