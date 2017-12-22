<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Update marks</title>
	<style type="text/css">
		button {
			padding: 10px;
			font-size: 14px;
			border-radius: 5%;
			margin: 3px;
			background-color: transparent;
			cursor: pointer;
			border-color: gray;
		}
		button:hover{
			background-color: grey;
			color: white;
		}
	</style>
</head>
<body>
	<div align="center">
		<h3>Set new marks</h3>
		<form action="StudentHandler" method="GET">
		    <table>
		    	<%
		    		int markCount = 0;
		    	%>
		    	<c:forEach items="${groupList}" var="subject" varStatus="index">
		    		<tr>
		    			<td>${subject}</td>
		    			<td>
		    				<input type="text" name="<%="mark" + markCount%>">
		    			</td>
		    		</tr>
		    		<%
		    			markCount ++;
		    		%>
		    	</c:forEach>
		    </table>
		    <br>
		    <button type="submit" value="setNewMarks" name="action">Update</button>
		</form> 
		<a href="StudentHandler?action=view"><button>Cancel</button></a>
	</div>
</body>
</html>