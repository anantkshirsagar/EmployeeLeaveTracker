package com.elt.tests;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import com.elt.model.EmployeeWiseLeaveRecord;
import com.elt.model.HolidayRecord;
import com.elt.service.EmployeeService;
import com.elt.utils.AppUtils;
import com.elt.utils.JsonType;
import com.google.gson.Gson;

public class JsonTest {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
//		getList(new JsonType<HolidayRecord>());
		getEmployeeWiseLeaveRequest();
	}
	
	public static <T> List<T> getList(JsonType<T> jsonMapper){
		Type type = jsonMapper.getType();
		return null;
	}
	
	public static void getEmployeeWiseLeaveRequest() throws ClassNotFoundException, SQLException {
		EmployeeService employeeService = new EmployeeService();
		List<EmployeeWiseLeaveRecord> employee = employeeService.getEmployeeWiseLeaveRequest();
		Gson gsonInstance = AppUtils.getGsonInstance();
		System.out.println(gsonInstance.toJson(employee));
	}
}
