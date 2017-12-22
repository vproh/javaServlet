<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Groups</title>
	<style type="text/css">
		table {
			font-size: 18px;
			border: 1px solid black;
			width: 50%;
			border-collapse: collapse;
		}
		th {
			border: 1px solid black;
		}
		td {
			border: 1px solid black;
		}
		input {
			margin-top: 10px;	
		}
		a {
			text-decoration: none;
			color: black;
		}
		#view:hover {
			color: blue;
		}

		#del:hover {
			color: red;
		}
		#download:hover {
			color: green;
		}
		.btn {
			padding: 10px;
			font-size: 16px;
			border-radius: 5%;
			margin-top: 15px;
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
		<h3>List of groups :</h3>
		<table>
			<tr>
				<td>Group name</td>
				<td>Group number</td>
				<td>Curator</td>
			</tr>
			<c:forEach items="${groupList}" var="group">
				<tr>
					<td><a id="view" href="StudentHandler?action=view&groupNumber=<c:out value="${group.groupNumber}"/>">${group.groupName}</a></td>
					<td>${group.groupNumber}</td>
					<td>${group.curatorName}</td>
					<td ><a id="del" href="GroupHandler?action=delete&groupNumber=<c:out value="${group.groupNumber}"/>">Delete</a></td>
					<td ><a id="download" href="GroupHandler?action=download&groupNumber=<c:out value="${group.groupNumber}"/>">Download</a></td>
				</tr>
			</c:forEach>
		</table>
		<form name="groupAction" method="POST" action="GroupHandler" enctype="multipart/form-data">
			<input class="btn" type="file" name="file" accept="text/plain, text/json, text/xml">
			<input class="btn" type="submit" name="action" value="Add"><br>
		</form>
	</div>
</body>
</html>