<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Apache MQ Demo</title>
<script src="https://code.jquery.com/jquery-1.10.2.js"
	type="text/javascript"></script>
	
</head>
<body>
	Distributed Message: <input type="text" name="distributedMsg" id="distributedMsg">
	<button id="btnSend" type="button" onclick="send()">Send</button><br/><br/>
	Simple Message: <input type="text" name="simpleMsg" id="simpleMsg">
	<div id="ajaxGetUserServletResponse"></div>
	<button id="btnSend" type="button" onclick="send1()">Send</button>
</body>
<script>
function send1() {
	alert(1112);
}
function send() {
	var msg = $("#distributedMsg").val();
	$.ajax({
	    url: "activeMQStartUpServlet",
	    type: 'POST',
	    dataType:'text',
		data:{"distributedMsg":msg},
	    success: function (data) {
	    	console.log(1111);
	    },
	    error:function(data,status,er) {
	        console.log("error: "+data+" status: "+status+" er:"+er);
	    }
	});
}
</script>
</html>