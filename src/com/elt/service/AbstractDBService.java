package com.elt.service;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbmanager.connection.setting.AbstractConnectionSettings;
import com.elt.constants.AppConstants;
import com.elt.manager.ConfigurationReader;
import com.elt.utils.DatabaseUtils;

public abstract class AbstractDBService {
	protected AbstractConnectionSettings connectionSettings;
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDBService.class);

	public AbstractDBService() {
		try {
			Map<String, String> configurationMap = ConfigurationReader
					.getConfigurationProperties(AppConstants.DATABASE_CONFIG_PATH);
			String propertiesPath = ConfigurationReader.getDBPropertiesPath(configurationMap);
			LOG.debug("propertiesPath {}, configurationPath {}", propertiesPath, configurationMap);
			connectionSettings = DatabaseUtils.getConnectionSettings(propertiesPath);
		} catch (IOException e) {
			LOG.error("Connection exception {}", e);
		}
	}
}
