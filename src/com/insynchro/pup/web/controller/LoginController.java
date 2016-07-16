package com.insynchro.pup.web.controller;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.insynchro.pup.utils.ConfigProps;
import com.primavera.integration.client.RMIURL;
import com.primavera.integration.client.Session;
import com.primavera.integration.common.DatabaseInstance;

/**
 * Servlet implementation class LoginController
 */
public class LoginController extends HttpServlet {
	Logger logger = Logger.getLogger(LoginController.class);
	private static final long serialVersionUID = 1L;
	private static ConfigProps confProperty = ConfigProps.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		String error=null;
		logger.info("Inside logincontroller user name : "+userName);
		Session p6Session=null;
		try {
			System.setProperty("primavera.bootstrap.home", confProperty.getProperty("primavera.bootstrap.home"));
			DatabaseInstance[] dbInstances = Session.getDatabaseInstances(RMIURL.getRmiUrl(RMIURL.LOCAL_SERVICE));
			p6Session = Session.login(RMIURL.getRmiUrl(RMIURL.LOCAL_SERVICE), dbInstances[0].getDatabaseId(), userName,
					password);
			logger.info("-----------p6Session2222222222 "+p6Session);
			HttpSession session=null;
			if(p6Session!=null) {
				logger.info("-----------p6Session "+p6Session);
				//Valid p6 user
				session=request.getSession(true);
				//session.setAttribute("p6Session", p6Session);
				p6Session.logout();
				
				session.setAttribute("p6UserName", userName);
				session.setAttribute("p6Password", password);
				
				request.getRequestDispatcher("/DashboardController").forward(request, response);
				
				
			} else {
				System.out.println("------------wrong user name pass -- ");
				response.sendRedirect("login.jsp?error=yes");
				//request.getRequestDispatcher("login.jsp?error=yes").forward(request, response);
				//error="P6 user name or password is not correct. ";
			}
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("error in LoginController");
			response.sendRedirect("login.jsp?error=yes");
		}
		
		
		
		//doGet(request, response);
	}

}
