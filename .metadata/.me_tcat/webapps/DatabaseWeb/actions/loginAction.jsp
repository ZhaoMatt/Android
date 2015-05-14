<%@page import="com.jacob.Tools"%>
<%@page import="com.jacob.BaseDao"%>
<%@page import="java.sql.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setCharacterEncoding("utf-8");
	response.setContentType("text/html;charset=utf-8");
	response.setCharacterEncoding("utf-8");
	String username = request.getParameter("username");
	String password = request.getParameter("password");

	String url = "jdbc:sqlserver://localhost:1433;databaseName=DBPrac";
	String dbUserName = "sa";
	String dbPassword = "newxykang";
	String pass = null;
	String lastVisitedTime = null;
	try {

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection conn = DriverManager.getConnection(url, dbUserName,
				dbPassword);
		String sql = "select * from user1 where username='" + username
				+ "'";

		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			pass = rs.getString("password");
			lastVisitedTime = rs.getString("lastVisited");
		}

		rs.close();
		pst.close();
		conn.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	if (password.equals(pass)) {
		//login success
		session.setAttribute("isLogin", "true");
		session.setAttribute("username", username);
		session.setAttribute("lastVisited", lastVisitedTime);
		String sql = "update  user1 set lastVisited='"
				+ Tools.getNowTime() + "' where username='" + username
				+ "'";
		BaseDao.insert(sql);
		response.sendRedirect("../frame.jsp");
	}
	else{
		session.setAttribute("errorInfo","对不起,登录出错,请返回重试!");
		response.sendRedirect("../error.jsp");
	}
	//out.println("username:" + username);
	//out.println("password:" + password);
	//out.println("lastVisitedTime:" + lastVisitedTime);
%>

