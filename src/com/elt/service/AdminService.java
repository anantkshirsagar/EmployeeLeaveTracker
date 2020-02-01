package com.elt.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elt.constants.AppConstants;
import com.elt.model.CurrentYearRecord;
import com.elt.model.HolidayRecord;

public class AdminService extends AbstractDBService {

	private static final Logger LOG = LoggerFactory.getLogger(AdminService.class);

	public void addHolidayRecord(HolidayRecord holidayRecord) throws ClassNotFoundException, SQLException {
		if (holidayRecord.getId() != null && isHolidayRecordExists(holidayRecord)) {
			updateHolidayRecord(holidayRecord);
			return;
		}
		connectionSettings.build();
		String query = "insert into holiday_record(holiday_name, holiday_date) values(?, ?)";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, holidayRecord.getHolidayName());
		java.sql.Timestamp sqlDateFormat = new java.sql.Timestamp(holidayRecord.getHolidayDate().getTime());
		prepareStatement.setTimestamp(2, sqlDateFormat);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Entry to holiday_record {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void addHolidayRecords(List<HolidayRecord> holidayRecordList) throws ClassNotFoundException, SQLException {
		if (CollectionUtils.isNotEmpty(holidayRecordList)) {
			deleteAllHolidayRecord();
			for (HolidayRecord holidayRecord : holidayRecordList) {
				addHolidayRecord(holidayRecord);
			}
		}
	}

	public void deleteHolidayRecord(long id) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "delete from holiday_record where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, id);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Record deleted from holiday_record {} with id {}", prepareStatement.executeUpdate(), id);
		connectionSettings.closeConnection();
	}

	public void deleteAllHolidayRecord() throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "delete from holiday_record";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Delete from holiday_record {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void updateHolidayRecord(HolidayRecord holidayRecord) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update holiday_record set holiday_name = ?, holiday_date = ? where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setString(1, holidayRecord.getHolidayName());
		java.sql.Timestamp sqlDateFormat = new java.sql.Timestamp(holidayRecord.getHolidayDate().getTime());
		prepareStatement.setTimestamp(2, sqlDateFormat);
		prepareStatement.setLong(3, holidayRecord.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Delete from current_year_record {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public List<HolidayRecord> getHolidayRecordGridInfo() throws ClassNotFoundException, SQLException {
		List<HolidayRecord> holidayRecordList = new ArrayList<HolidayRecord>();
		connectionSettings.build();
		String query = "select * from holiday_record";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		ResultSet resultSet = prepareStatement.executeQuery();
		while (resultSet.next()) {
			HolidayRecord holidayRecord = new HolidayRecord();
			holidayRecord.setId(resultSet.getLong("id"));
			holidayRecord.setHolidayName(resultSet.getString("holiday_name"));
			Timestamp timestamp = resultSet.getTimestamp("holiday_date");
			holidayRecord.setHolidayDate(new Date(timestamp.getTime()));
			holidayRecordList.add(holidayRecord);
		}
		connectionSettings.closeConnection();
		return holidayRecordList;
	}

	public void addCurrentYearRecord(CurrentYearRecord currentYearRecord) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "insert into current_year_record(id, current_year, total_leaves) values(?, ?, ?)";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, currentYearRecord.getId());
		prepareStatement.setInt(2, currentYearRecord.getCurrentYear());
		prepareStatement.setInt(3, currentYearRecord.getTotalLeaves());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Entry to current_year_record {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void deleteCurrentYearRecord(int id) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "delete from current_year_record where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, id);
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Delete from current_year_record {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();
	}

	public void editCurrentYearRecord(CurrentYearRecord currentYearRecord) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "update current_year_record set current_year = ?, total_leaves = ? where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, currentYearRecord.getCurrentYear());
		prepareStatement.setInt(2, currentYearRecord.getTotalLeaves());
		prepareStatement.setLong(3, currentYearRecord.getId());
		LOG.debug("Query {}", prepareStatement);
		LOG.debug("Update from current_year_record {}", prepareStatement.executeUpdate());
		connectionSettings.closeConnection();

	}

	public CurrentYearRecord getCurrentYearRecord() throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from current_year_record where current_year = ?";
		CurrentYearRecord currentYearRecord = new CurrentYearRecord();
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, AppConstants.CURRENT_YEAR);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			currentYearRecord.setId(resultSet.getLong("id"));
			currentYearRecord.setCurrentYear(resultSet.getInt("current_year"));
			currentYearRecord.setTotalLeaves(resultSet.getInt("total_leaves"));
		}
		connectionSettings.closeConnection();
		return currentYearRecord;
	}

	public boolean isCurrentYearRecordPresent() throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from current_year_record where current_year = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setInt(1, AppConstants.CURRENT_YEAR);
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			return true;
		}
		connectionSettings.closeConnection();
		return false;
	}

	public boolean checkIfHolidayRecordPresentForCurrentYear(int year) throws ClassNotFoundException, SQLException {
		connectionSettings.build();

		connectionSettings.closeConnection();
		return false;
	}

	public boolean isHolidayRecordExists(HolidayRecord holidayRecord) throws ClassNotFoundException, SQLException {
		connectionSettings.build();
		String query = "select * from holiday_record where id = ?";
		PreparedStatement prepareStatement = connectionSettings.getConnection().prepareStatement(query);
		prepareStatement.setLong(1, holidayRecord.getId());
		LOG.debug("Query {}", prepareStatement);
		ResultSet resultSet = prepareStatement.executeQuery();
		if (resultSet.next()) {
			LOG.debug("Holiday record exists with id {}", holidayRecord.getId());
			return true;
		}
		connectionSettings.closeConnection();
		return false;
	}

	public void saveCurrentYearDetails() {

	}
}
