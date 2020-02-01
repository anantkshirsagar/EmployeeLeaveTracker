package com.elt.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.elt.constants.AppConstants;
import com.elt.constants.CallType;
import com.elt.utils.AppUtils;
import com.google.gson.Gson;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);

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
			
			switch(callType) {
			case ADMIN_LOGIN_SERVICE:
				LOG.debug("Admin login service");
				String email = request.getHeader(AppConstants.ADMIN_EMAIL);
				String password = request.getHeader(AppConstants.ADMIN_PASSWORD);
				if(AppUtils.validateAdminLogin(email, password)) {
					out.print(AppConstants.VALID_USER);
				} else {
					out.print(AppConstants.INVALID_USER);
				}
				break;
			case EMPLOYEE_LOGIN_SERVICE:
				LOG.debug("Employee login service");
				break;
			}
		} catch (Exception e) {
			LOG.error("Servlet Exception {}", e);
		}
	}
}
