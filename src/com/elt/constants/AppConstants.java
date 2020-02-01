package com.elt.constants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class AppConstants {

	private AppConstants() {

	}

	public static final String DATABASE_CONFIG_PATH = "elt-app-configurations.properties";
	public static final String CREATE_MYSQL_TABLES = "resources" + File.separator + "database" + File.separator
			+ "employee_leave_manager_mysql_tables.sql";

	public static final String CREATE_PSQL_TABLES = "resources" + File.separator + "database" + File.separator
			+ "employee_leave_manager_psql_tables.sql";

	public enum DATABASE_TYPE {
		MYSQL, PSQL
	}

	public static final String APP_FOLDER_NAME = "employee_leave_tracker";
	public static final String ADMIN_EMAIL = "adminEmail";
	public static final String ADMIN_PASSWORD = "adminPassword";

	public static final String EMAIL = "admin@gmail.com";
	public static final String PASSWORD = "admin";

	// Jump to pages
//	public static final String ADMIN_ADD_HOLIDAY = "admin-add-holidays.html";
//	public static final String ADMIN_ADD_MANAGER = "admin-add-manager.html";
//	public static final String ADMIN_APPROVE_MANAGER_LEAVES = "admin-approve-manager-leaves.html";
//	public static final String ADMIN_LOGIN = "admin-login.html";
//	public static final String EMPLOYEE_LEAVE_PAGES = "employee-leave-page.html";
//	public static final String EMPLOYEE_LOGIN = "employee-login.html";

	public static final String VALID_USER = "VALID_USER";
	public static final String INVALID_USER = "INVALID_USER";
	public static final String MANAGER = "MANAGER";
	public static final String SUCCESS_MESSAGE = "SUCCESS";
	public static final String FAILURE_MESSAGE = "FAIL";

	public static final String TOTAL_LEAVES_HEADER = "totalLeaves";
	public static final String CURRENT_YEAR_HEADER = "currentYear";
	public static final Long CURRENT_YEAR_RECORD_TABLE_ID = 1000L;
	public static final int CURRENT_YEAR = LocalDate.now().getYear();

	public static final String fromDate = "fromDate";
	public static final String toDate = "toDate";
	public static final String reason = "reason";
	public static final String leaveStatus = "leaveStatus";

	public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STANDARD_DATE_FORMAT);
	public static final String INVALID_DATE_RANGE = "INVALID_DATE_RANGE";
	public static final String INVALID_LEAVE_COUNT = "INVALID_LEAVE_COUNT";
	
	public static final String EMP_EMAIL_ID = "employeeEmailId";

}
