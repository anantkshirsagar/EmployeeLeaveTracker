package com.elt.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.objectify.Objectify;
import com.elt.constants.AppConstants;
import com.elt.constants.ApproveStatus;
import com.elt.model.CurrentYearRecord;
import com.elt.model.Employee;
import com.elt.model.LeaveRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AppUtils {

	private static final Logger LOG = LoggerFactory.getLogger(AppUtils.class.getName());

	private AppUtils() {
	}

	public static Object mapToClass(HttpServletRequest request, Class classType) throws IOException {
		String jsonObj = request.getReader().lines().collect(Collectors.joining());
		LOG.debug("Json {}", jsonObj);
		Gson gson = getGsonInstance();
		return gson.fromJson(jsonObj, classType);
	}

	public static <T> List<T> mapToList(HttpServletRequest request, Type jsonType) throws IOException {
		String jsonObj = request.getReader().lines().collect(Collectors.joining());
		LOG.debug("Json {}", jsonObj);
		Gson gson = getGsonInstance();
		return gson.fromJson(jsonObj, jsonType);
	}

	public static Gson getGsonInstance() {
		GsonBuilder builder = new GsonBuilder();
		return builder.create();
	}

	public static boolean isStringEmpty(String string) {
		return string == null || string.isEmpty();
	}

	public static boolean isStringNotEmpty(String string) {
		return string != null && !string.isEmpty();
	}

	public static byte[] getObject(List<String> keywordsList) throws SQLException, IOException {
		return Objectify.serialize(keywordsList);
	}

	public static boolean validateAdminLogin(String email, String password) {
		return email.equals(AppConstants.EMAIL) && password.equals(AppConstants.PASSWORD);
	}

	public static CurrentYearRecord getCurrentYearRecord(int totalLeaves) {
		CurrentYearRecord currentYearRecord = new CurrentYearRecord();
		currentYearRecord.setId(AppConstants.CURRENT_YEAR_RECORD_TABLE_ID);
		currentYearRecord.setCurrentYear(AppConstants.CURRENT_YEAR);
		currentYearRecord.setTotalLeaves(totalLeaves);
		return currentYearRecord;
	}

	public static Date getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		Calendar calendar = Calendar.getInstance();
		LOG.debug("{} ,{}, {}", currentDate.getYear(), currentDate.getMonth().ordinal(), currentDate.getDayOfMonth());
		calendar.set(currentDate.getYear(), currentDate.getMonth().ordinal(), currentDate.getDayOfMonth(), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static boolean validateDateRange(Date fromDate, Date toDate) {
		Date currentDate = getCurrentDate();
		if (currentDate.compareTo(fromDate) == 0 || currentDate.compareTo(toDate) == 0) {
			return false;
		}
		return (fromDate.compareTo(toDate) < 0 || fromDate.compareTo(toDate) == 0)
				&& !(fromDate.compareTo(currentDate) < 0);
	}

	public static Date getDateFromString(String dateStr) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.STANDARD_DATE_FORMAT);
		return simpleDateFormat.parse(dateStr);
	}

	public static int daysBetween(Date fromDate, Date toDate) {
		if (fromDate.compareTo(toDate) == 0) {
			return 1;
		}
		long difference = toDate.getTime() - fromDate.getTime();
		return (int) (difference / (1000 * 60 * 60 * 24)) + 1;
	}

	public static LeaveRequest getLeaveRequest(Date fromDate, Date toDate, String reason, int days,
			ApproveStatus approveStatus, Long employeeId) {
		LeaveRequest leaveRequest = new LeaveRequest();
		leaveRequest.setFromDate(fromDate);
		leaveRequest.setToDate(toDate);
		leaveRequest.setLeaveReason(reason);
		leaveRequest.setDays(days);
		leaveRequest.setApproveStatus(approveStatus);
		leaveRequest.setEmployeeId(employeeId);
		return leaveRequest;
	}

	public static ApproveStatus getApproveStatusFromString(String approveStatus) {
		return ApproveStatus.valueOf(approveStatus);
	}

	public static int getPendingAndApprovedLeavesAddition(Employee employee) {
		return employee.getApprovedLeavesCount() + employee.getPendingLeavesCount();
	}
}
