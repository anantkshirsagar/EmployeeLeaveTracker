package com.elt.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elt.model.CurrentYearRecord;
import com.elt.model.Employee;
import com.elt.model.EmployeeWiseLeaveRecord;
import com.elt.model.LeaveRequest;

public class EmployeeService extends AbstractDBService {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

	public void addEmployee(Employee employee) throws ClassNotFoundException, SQLException {
		if (employee.getId() != null && isEmployeeExists(employee)) {
			updateEmployee(employee);
			return;
		}
		AdminService adminService = new AdminService();
		CurrentYearRecord currentYearRecord = adminService.getCurrentYearRecord();
		connectionSettings.build();
		String query = "insert into employee(name, city, email, password, contact_no, approved_leaves_count, pending_leaves_count, remaining_leaves_count, is_manager) values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, employee.getName());
		prepareStatement.setString(2, employee.getCity());
		prepareStatement.setString(3, employee.getEmail());
		prepareStatement.setString(4, employee.getPassword());
		prepareStatement.setString(5, employee.getContactNo());
		prepareStatement.setInt(6, 0);
		prepareStatement.setInt(7, 0);
		prepareStatement.setInt(8, currentYearRecord.getTotalLeaves());
		prepareStatement.setBoolean(9, false);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Entry to holiday_record {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public boolean isEmployeeExists(Employee employee) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, employee.getId());
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			LOG.debug("Employee record exists with id {}", employee.getId());
			return true;
		}
		connectionSettings.closeConnection();
		return false;
	}

	public void updateEmployee(Employee employee) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update employee set name = ?, city = ?, email = ?, password = ?, contact_no = ?, is_manager = ?, approved_leaves_count = ?, pending_leaves_count = ?, remaining_leaves_count = ? where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, employee.getName());
		prepareStatement.setString(2, employee.getCity());
		prepareStatement.setString(3, employee.getEmail());
		prepareStatement.setString(4, employee.getPassword());
		prepareStatement.setString(5, employee.getContactNo());
		prepareStatement.setBoolean(6, employee.isManager());
		prepareStatement.setInt(7, employee.getApprovedLeavesCount());
		prepareStatement.setInt(8, employee.getPendingLeavesCount());
		prepareStatement.setInt(9, employee.getRemainingLeavesCount());
		prepareStatement.setLong(10, employee.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Update Employee {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void updateEmployeeLeaveRecords(Employee employee) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update employee set name = ?, city = ?, email = ?, contact_no = ?, is_manager = ?, approved_leaves_count = ?, pending_leaves_count = ?, remaining_leaves_count = ? where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, employee.getName());
		prepareStatement.setString(2, employee.getCity());
		prepareStatement.setString(3, employee.getEmail());
		prepareStatement.setString(4, employee.getContactNo());
		prepareStatement.setBoolean(5, employee.isManager());
		prepareStatement.setInt(6, employee.getApprovedLeavesCount());
		prepareStatement.setInt(7, employee.getPendingLeavesCount());
		prepareStatement.setInt(8, employee.getRemainingLeavesCount());
		prepareStatement.setLong(9, employee.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Update Employee {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void updateIsManager(Employee employee) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update employee set is_manager = ? where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setBoolean(1, employee.isManager());
		prepareStatement.setLong(2, employee.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Mark employee as mananger {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public boolean isValidEmployee(Employee employee) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee where email = ? and password = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, employee.getEmail());
		prepareStatement.setString(2, employee.getPassword());
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			return true;
		}
		connectionSettings.closeConnection();
		return false;
	}

	public boolean isManager(Employee employee) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee where email = ? and password = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, employee.getEmail());
		prepareStatement.setString(2, employee.getPassword());
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getBoolean("is_manager");
		}
		connectionSettings.closeConnection();
		return false;
	}

	public boolean isManager(String email, String password) throws ClassNotFoundException, SQLException {
		Employee employee = new Employee();
		employee.setEmail(email);
		employee.setPassword(password);
		return isManager(employee);
	}

	public void deleteEmployee(int id) {

	}

	public Employee getEmployeeRecordByEmail(String email) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee where email = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, email);
		ResultSet resultSet = prepareStatement.executeQuery();
		Employee employee = new Employee();
		if (resultSet.next()) {
			employee.setId(resultSet.getLong("id"));
			employee.setName(resultSet.getString("name"));
			employee.setCity(resultSet.getString("city"));
			employee.setEmail(resultSet.getString("email"));
			employee.setContactNo(resultSet.getString("contact_no"));
			employee.setManager(resultSet.getBoolean("is_manager"));
			employee.setPendingLeavesCount(resultSet.getInt("pending_leaves_count"));
			employee.setRemainingLeavesCount(resultSet.getInt("remaining_leaves_count"));
			employee.setApprovedLeavesCount(resultSet.getInt("approved_leaves_count"));
		}
		connectionSettings.closeConnection();
		return employee;
	}

	public Employee getEmployeeRecordById(Long id) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, id);
		ResultSet resultSet = prepareStatement.executeQuery();
		Employee employee = new Employee();
		if (resultSet.next()) {
			employee.setId(resultSet.getLong("id"));
			employee.setName(resultSet.getString("name"));
			employee.setCity(resultSet.getString("city"));
			employee.setEmail(resultSet.getString("email"));
			employee.setContactNo(resultSet.getString("contact_no"));
			employee.setManager(resultSet.getBoolean("is_manager"));
			employee.setPendingLeavesCount(resultSet.getInt("pending_leaves_count"));
			employee.setRemainingLeavesCount(resultSet.getInt("remaining_leaves_count"));
			employee.setApprovedLeavesCount(resultSet.getInt("approved_leaves_count"));
		}
		connectionSettings.closeConnection();
		return employee;
	}

	public List<Employee> getEmployees() throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		List<Employee> employeeList = new ArrayList<Employee>();
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			Employee employee = new Employee();
			employee.setId(resultSet.getLong("id"));
			employee.setName(resultSet.getString("name"));
			employee.setCity(resultSet.getString("city"));
			employee.setEmail(resultSet.getString("email"));
			employee.setContactNo(resultSet.getString("contact_no"));
			employee.setManager(resultSet.getBoolean("is_manager"));
			employee.setPendingLeavesCount(resultSet.getInt("pending_leaves_count"));
			employee.setRemainingLeavesCount(resultSet.getInt("remaining_leaves_count"));
			employee.setApprovedLeavesCount(resultSet.getInt("approved_leaves_count"));
			employeeList.add(employee);
		}
		connectionSettings.closeConnection();
		return employeeList;
	}

	public List<Employee> getAllEmployeesWithoutManager() throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee where is_manager = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setBoolean(1, false);
		List<Employee> employeeList = new ArrayList<Employee>();
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			Employee employee = new Employee();
			employee.setId(resultSet.getLong("id"));
			employee.setName(resultSet.getString("name"));
			employee.setCity(resultSet.getString("city"));
			employee.setEmail(resultSet.getString("email"));
			employee.setContactNo(resultSet.getString("contact_no"));
			employee.setManager(resultSet.getBoolean("is_manager"));
			employee.setPendingLeavesCount(resultSet.getInt("pending_leaves_count"));
			employee.setRemainingLeavesCount(resultSet.getInt("remaining_leaves_count"));
			employee.setApprovedLeavesCount(resultSet.getInt("approved_leaves_count"));
			employeeList.add(employee);
		}
		connectionSettings.closeConnection();
		return employeeList;
	}

	public List<EmployeeWiseLeaveRecord> getEmployeeWiseLeaveRequest() throws ClassNotFoundException, SQLException {
		List<EmployeeWiseLeaveRecord> employeeWiseLeaveRequest = new ArrayList<EmployeeWiseLeaveRecord>();
		List<Employee> employees = getAllEmployeesWithoutManager();
		if (CollectionUtils.isNotEmpty(employees)) {
			LeaveRequestService leaveRequestService = new LeaveRequestService();
			for (Employee employee : employees) {
				List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employee.getId());
				EmployeeWiseLeaveRecord empLeaveRecord = new EmployeeWiseLeaveRecord();
				empLeaveRecord.setEmployee(employee);
				empLeaveRecord.setLeaveRequests(leaveRequests);
				employeeWiseLeaveRequest.add(empLeaveRecord);
			}
		}
		return employeeWiseLeaveRequest;
	}
	
	public Employee getManagerRecord() throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from employee where is_manager = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setBoolean(1, true);
		ResultSet resultSet = prepareStatement.executeQuery();
		Employee employee = new Employee();
		if (resultSet.next()) {
			employee.setId(resultSet.getLong("id"));
			employee.setName(resultSet.getString("name"));
			employee.setCity(resultSet.getString("city"));
			employee.setEmail(resultSet.getString("email"));
			employee.setContactNo(resultSet.getString("contact_no"));
			employee.setManager(resultSet.getBoolean("is_manager"));
			employee.setPendingLeavesCount(resultSet.getInt("pending_leaves_count"));
			employee.setRemainingLeavesCount(resultSet.getInt("remaining_leaves_count"));
			employee.setApprovedLeavesCount(resultSet.getInt("approved_leaves_count"));
		}
		connectionSettings.closeConnection();
		return employee;
	}
}
