<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
	<h1>Ici toutes les dernières publications ! </h1>
	<div id="account-box">
		<span>Nom : ${user_lastname}</span>
		<span>Prénom : ${user_firstname}</span>
	</div>	

	<p><em>En fait pour l'instant il y a pas de publications sorry *ashamed*</em></p>
</body>
</html>
