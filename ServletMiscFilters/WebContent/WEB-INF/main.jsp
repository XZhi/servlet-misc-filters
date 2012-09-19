<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PRINCIPAL</title>
</head>
<body>
PAGINA PRINCIPAAAAL!!!
<%-- ID DE SESION: <%= request.getSession(false).getId() %> --%>
REQUESTED FROM URL: <%= request.isRequestedSessionIdFromURL() %>

<form action=<%= response.encodeURL("Test") %> method='POST'>
		<input type="hidden" name="pagina" value="inicio">
		<input type="submit" name="submit" value="Volver">
	</form>

</body>
</html>