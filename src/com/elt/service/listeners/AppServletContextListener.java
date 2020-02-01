package com.elt.service.listeners;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elt.model.CurrentYearRecord;
import com.elt.service.AdminService;
import com.elt.utils.AppUtils;

public class AppServletContextListener implements ServletContextListener {

	private static final Logger LOG = LoggerFactory.getLogger(AppServletContextListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		LOG.debug("Context destroyed...");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		AdminService adminService = new AdminService();
		try {
			if (!adminService.isCurrentYearRecordPresent()) {
				CurrentYearRecord currentYearRecord = AppUtils.getCurrentYearRecord(0);
				LOG.debug("---> current year record {}", currentYearRecord);
				adminService.addCurrentYearRecord(AppUtils.getCurrentYearRecord(0));
			}
		} catch (ClassNotFoundException | SQLException e) {
			LOG.error("Current year record exception{}", e);
		}

	}
}
