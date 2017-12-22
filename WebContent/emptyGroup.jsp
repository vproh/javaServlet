<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Group is empty</title>
	<style type="text/css">
		.action {
			font-size: 20px;
		}
		.btn {
			padding: 10px;
			font-size: 16px;
			border-radius: 5%;
			margin: 3px;
			background-color: transparent;
			cursor: pointer;
			border-color: gray;
		}
		.btn:hover{
			background-color: grey;
			color: white;
		}
	</style>
</head>
<body>
	<div align="center">
		<h3>Oooops, looks like this group have NO students</h3>
		<p class="action">But You can:</p>
		<a href="StudentHandler?action=add&groupNumber=${requestScope.groupList}"><button class="btn" >Add new student</button></a>
		<a href="GroupHandler?action=delete&groupNumber=${requestScope.groupList}"><button class="btn" >Delete this group</button></a>
		<a href="GroupHandler?action=back"><button class="btn">Back</button></a>
	</div>
</body>
</html>