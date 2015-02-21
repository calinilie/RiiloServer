<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<html>
<head>
	<title>Home</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<sf:form method="POST" action="/web/posts/upload/" enctype="multipart/form-data">
<input type="file" name="image"/>
<input name="userId" type="text" value="1f93dsa8s20s1"/>
<input name="latitude" type="text" value="122.8392"/>
<input name="longitude" type="text" value="-40.32321"/>
<input name="Message" type="text" value=" să mă piș pe ăla de țânțar !"/>
<input name="isUserAtLocation" type="checkbox" checked="checked"/>
<input name="repliesToPostId" type="text" value="0" />
<input name="conversationId" type="text" value="0" />
<input name="postedDate" type="text" value="13.09.2013" />
<input type="submit" value="submit!" />
</sf:form>
</body>
</html>
