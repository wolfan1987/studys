<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

		<title>My JSP 'didplay.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		<table id="ds" border="1" cellpadding="2" cellspacing="1" width="100%"
			style="boder: 1px">
			<tr>
				<td align="center" style="cursor: hand"></td>
				<td align="center" style="cursor: hand"></td>
			</tr>
			<c:forEach var="rows" items="${info}" varStatus="status">
				<c:if test="${status.count%2==0 }">
					<tr bgcolor="white">
				</c:if>
				<c:if test="${status.count%2==1}">
					<tr bgcolor="green">
				</c:if>
				<td>
					<c:out value="${rows.rid}" />
				</td>
				<td>
					<c:out value="${rows.rname}" />
				</td>
			</c:forEach>


		</table>
	</body>
</html>
