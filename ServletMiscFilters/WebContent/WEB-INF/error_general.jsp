<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page session="false" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
  
    <title>Error</title>

</head>
<body>

		<%  String error = (String)request.getAttribute("errorMessage");
			String imagenError = "ERRRO GENERICO";
			String action = "index.jsp";
		
			//Dependiendo del tipo de error mostramos la imagen correspondiente
			if( error != null ){
				imagenError = error;
				
			}%>

			<%=imagenError%>

</body>
</html>