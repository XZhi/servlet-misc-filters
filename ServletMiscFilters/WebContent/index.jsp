<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page session="true" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%-- ID DE SESION: <%= request.getSession().getId() %> --%>

	<form action="Test?test=rofl&wtf=lol" method='POST' accept-charset='UTF-8'>
		<input type="hidden" name="pagina" value="main">
		<input type="submit" name="submit" value="Entrar">
	</form>
	
<a href='Test'>pruebaca</a>
	
	<form action=<%= response.encodeURL("Test") %> method='POST'>
		<input type="hidden" name="pagina" value="main">
		<input type="submit" name="submit" value="Entrar URL REWRITING">
<%-- 		<input type="hidden" name="jsessionid" value=<%= request.getSession().getId() %>> --%>
	</form>
					
</body>
</html>