<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>Frame</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<style type="text/css">
.menu {
	text-decoration: none;
	color: #FF0000;
}

li {
	margin: 10 0 10 0
}

.menu:link {
	color: #FF0000;
}  /* 未访问的链接 */
.menu:visited {
	color: #FF0000;
	font-size: 150%;
}  /* 已访问的链接 */
.menu:hover {
	color: #FF00FF
}  /* 鼠标移动到链接上 */
.menu:active {
	color: #0000FF
} /* 选定的链接 */
</style>

		<script type="text/javascript" src="js/jquery-1.8.2.min.js"></script>
		<script type="text/javascript">
	function display1(e) {
		var ename = "#" + e;
		if ($(ename).css("display") == "none") {
			$(ename).css("display", "block");
		} else {
			$(ename).css("display", "none");
		}
	}
	$(document).ready(function() {
		var width = document.body.clientWidth;
		$('#left').css("margin-left", (width - 1040) / 2);
		$('#left').css("margin-right", 0);
		$('#prj1').css("display", "none");
	});
</script>

	</head>

	<body>
		<%
			if(!session.getAttribute("isLogin").equals("true")){
				response.sendRedirect("login.jsp");
				return;
			}
		%>
		<div id="head"
			style="background: url('img/logo.jpg'); height: 120; width: 1024; margin: auto">
		</div>
		<div id="left"
			style="background-color: #5781FF; height: 600; width: 300; float: left;">
			<ul>
				<li>
					<a class="menu" onclick="display1('userInfo')">用户信息</a>
					<div id="userInfo" style="display: none">
						您好:
						<span style="color: white;"><%=session.getAttribute("username")%></span>
						<br>
						您上次访问的时间是:
						<br>
						<span style="color: blue;"><%=session.getAttribute("lastVisited")%></span>
					</div>
				</li>
				<li>
					<a class="menu" onclick="display1('prj1')">项目1</a>
					<ul id="prj1" style="display: none">
						<li>
							<a href="yourcontent.jsp" target="right">这里是你的页面</a>
						</li>
					</ul>
				</li>
				<li>
					<a class="menu" href="#">项目2</a>
				</li>
				<li>
					<a class="menu" href="http://www.baidu.com" target="right">百度</a>
				</li>
				<li>
					<a class="menu" href="actions/logoutAction.jsp">退出</a>
				</li>
			</ul>
		</div>
		<iframe id="right" style="background-color: #F5B800" height="600"
			width="724"></iframe>
		<div id="bottom"
			style="background-color: #E0E0E0; height: 20; width: 1024; text-align: center; margin: 2 auto 10 auto">
			©2012 数据库系统
		</div>
	</body>
</html>
