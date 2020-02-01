package com.elt.manager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.util.SqlFileRunner;
import com.elt.constants.AppConstants;

public class SQLTableManager {

	private static final Logger LOG = LoggerFactory.getLogger(SQLTableManager.class);
	private final AppConstants.DATABASE_TYPE type;

	public SQLTableManager(AppConstants.DATABASE_TYPE type) {
		this.type = type;
	}

	public void createTables() throws ClassNotFoundException, IOException, SQLException {
		Map<String, String> configurationMap = ConfigurationReader
				.getConfigurationProperties(AppConstants.DATABASE_CONFIG_PATH);
		String propertiesPath = ConfigurationReader.getDBPropertiesPath(configurationMap);
		LOG.debug("Loading tables. Database type {}, propertiesPath {}, configurationMap {}", type, propertiesPath,
				configurationMap);

		SqlFileRunner sqlRunner = null;
		if (AppConstants.DATABASE_TYPE.MYSQL == type) {
			sqlRunner = new SqlFileRunner(propertiesPath, AppConstants.CREATE_MYSQL_TABLES);
			sqlRunner.runScript();
		} else if (AppConstants.DATABASE_TYPE.PSQL == type) {
			sqlRunner = new SqlFileRunner(propertiesPath, AppConstants.CREATE_PSQL_TABLES);
			sqlRunner.runScript();
		}
		LOG.debug("Tables created successfully");
	}
}
