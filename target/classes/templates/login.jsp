<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<form action="/loginRequest">
	Enter your login : <input name="suppliedLogin"/>
	<input type="submit" value="submit"/>
</form>

</body>
</html>
