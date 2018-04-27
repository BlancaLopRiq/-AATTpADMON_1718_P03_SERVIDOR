<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

    <form action="http://localhost:8080/dni/IntroduceDatos" class="login-form" method=post>
      Usuario: <input type="text" name=nombre />
     Clave(DNI): <input type="text" name=nif />
      
      <br>
      <input type=submit value=Acceder>
    </form>

</body>
</html>