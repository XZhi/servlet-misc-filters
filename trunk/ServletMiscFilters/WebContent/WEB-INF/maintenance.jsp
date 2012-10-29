<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<body>
<form action="Test" method="post">
  <table>
    <tr><td>Maintenance Mode:</td>
      <td><select name="maintenancemode">
         <option value="false">NORMAL OPERATION</option>
         <option value="true">DOWN FOR MAINTENANCE</option>
      </select></td>
    </tr>
    <tr><td>Username: </td>
      <td><input name="username" type="password" /></td>
    </tr>
    <tr><td>Password: </td>
      <td><input name="password" type="password" /></td>
    </tr>    
    <tr>
      <td></td>
      <td>
      <input type="hidden" name="pagina" value="inicio">
      <input type="submit" value="Set Maintenance Mode"/> </td>
    </tr>

  </table>
</form>
</body>
</html>