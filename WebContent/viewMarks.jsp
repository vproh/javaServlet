<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Student marks</title>
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
		.mark {
			text-align: center;
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
		<h3>Marks:</h3>
		<%
			dataBase.DataBase db = new dataBase.DataBase();
			java.util.List<String> sbjList = db.getColumns("marks");
		%>
		<table>
			<tr>
				<c:forEach items="<%=sbjList%>" var="sbj">
					<td>${sbj}</td> 
				</c:forEach>
			</tr>
			<tr>
				<c:forEach items="${groupList}" var="mark">
					<td class="mark">${mark}</td>
				</c:forEach>
			</tr>
		</table>
		<a href="StudentHandler?action=view"><button class="btn">Back</button></a>
		<a href="StudentHandler?action=updateMarks"><button class="btn">Update marks</button></a>
	</div>
</body>
</html>