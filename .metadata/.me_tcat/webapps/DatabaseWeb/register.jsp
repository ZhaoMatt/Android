<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<base href="<%=basePath%>">

		<title>用户注册</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
		<style type="text/css">
body {
	margin: auto;
	text-align: center;
}
</style>
		<script type="text/javascript">
	$(document).ready(function() {
		var width = document.body.clientWidth;
		var height = document.body.clientHeight;
		$('#head').css("margin-top", (height - 230) / 2 - 100);
		$('#head').css("margin-left", (width - 400) / 2);
		$('#login').css("margin-left", (width - 400) / 2);
	});
</script>
	</head>

	<body>
		<div id="head"
			style="width: 400; height: 30; background: url('img/loginhead.jpg'); padding-top: 5">
			基于JavaWeb的数据库系统实践
		</div>
		<div id="login"
			style="width: 400; height: 220; background-color: #8AB800;padding-top:10">
			<h2 style="text-align: center;">用户注册</h2>
			<form action="actions/registerAction.jsp" method="post">
				用&nbsp;户&nbsp;名&nbsp;: <input type="text" name="username">
				<br>
				密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码&nbsp;: <input type="password" name="password">
				<br>
				重复密码: <input type="password" name="password2">
				<br>
				<input type="submit" name='submit' value="提交">
			</form>
		</div>
	</body>
</html>
