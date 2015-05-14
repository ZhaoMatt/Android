<%@page import="com.jacob.BaseDao"%>
<%@page import="com.jacob.Tools"%>
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
	/*
	 String url = "jdbc:sqlserver://localhost:1433;databaseName=DBPrac";
	 String dbUserName = "sa";
	 String dbPassword = "newxykang";
	 String sql = "select * from test ";
	 try {

	 Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	 Connection conn = DriverManager.getConnection(url, dbUserName,
	 dbPassword);
	 PreparedStatement pst = conn.prepareStatement(sql);
	 ResultSet rs = pst.executeQuery();
	 while (rs.next()) {
	 System.out.println("rs.getString(1)" + rs.getString(1));
	 System.out.println("rs.getString(2)" + rs.getString(2));
	 }

	 rs.close();
	 pst.close();
	 conn.close();
	 } catch (Exception e) {
	 e.printStackTrace();
	 }*/
	//get username
	String sqlQuery = "select * from user1 where username='" + username
			+ "'";
	boolean exist = false;
	ResultSet rs = BaseDao.query(sqlQuery);
	while (rs.next()) {
		exist = true;
	}
	if (!exist) {
		String sql = String
				.format("insert into user1(username,password,lastvisited) values('%s','%s','%s')",
						username, password, Tools.getNowTime());
		BaseDao.insert(sql);
		response.sendRedirect("../login.jsp");
	} else {
		session.setAttribute("errorInfo", "注册失败,该用户名已经存在");
		response.sendRedirect("../error.jsp");
	}
%>

