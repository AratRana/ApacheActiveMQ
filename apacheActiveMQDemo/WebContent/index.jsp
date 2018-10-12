<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Apache MQ Demo</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/activeMQStartUpServlet" method="post">
	Distributed Message: <input type="text" name="distributedMsg" id="distributedMsg">
	<button>Send</button> <br/><br/>
	Simple Message: <input type="text" name="simpleMsg" id="simpleMsg">
	<button>Send</button>
</form>	
</body>
</html>