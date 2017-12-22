<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Students</title>
	<style type="text/css">
		table {
			font-size: 18px;
			border: 1px solid black;
			width: 75%;
			border-collapse: collapse;
		}
		th {
			border: 1px solid black;
		}
		td {
			border: 1px solid black;
		}
		a {
			text-decoration: none;
			color: black;
		}
		.del:hover {
			color: red;
		}
		#view:hover {
			color: blue;
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
		<h3>Students list of Group</h3>
		<table>
			<tr>
				<td>First name</td>
				<td>Last name</td>
				<td>Birthday</td>
				<td>Faculty</td>
				<td>Leader</td>
				<td>Missings</td>
				<td>Ship</td>
				<td>Email</td>
				<td>Phone number</td>
			</tr>
			<c:forEach items="${groupList}" var="student">
				<tr>
					<td><a id="view" href="StudentHandler?action=viewStudent&phoneNumber=<c:out value="${student.phoneNumber}"/>">${student.firstName}</a></td>
					<td>${student.lastName}</td>
					<td>${student.birthDay}</td>
					<td>${student.faculty}</td>
					<td>${student.leader}</td>
					<td>${student.missings}</td>
					<td>${student.ship}</td>
					<td>${student.email}</td>
					<td>${student.phoneNumber}</td>
					<td><a class="del" href="StudentHandler?action=delete&phoneNumber=<c:out value="${student.phoneNumber}"/>">Delete</a></td>
				</tr>
			</c:forEach>
		</table>
		<a href="StudentHandler?action=add"><button class="btn">Add student</button></a>
		<a href="GroupHandler?action=view"><button class="btn">Back</button></a>
	</div>
</body>
</html>