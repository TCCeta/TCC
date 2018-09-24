
<%

	String nivel = String.valueOf(session.getAttribute("nivel"));

	if (nivel.equals("2")) {
		response.sendRedirect("../buscar.jsp");
	} else if (nivel.equals("1")) {
		response.sendRedirect("../admin.jsp");
	} else {
		response.sendRedirect("../entrar.jsp");
	}
%>