<link href="css/bootstrap.min.css" rel="stylesheet">

<%
	if (session.getAttribute("sessaoUsuario") == null) {

	}
	//Limpar a sess�o
	session.setAttribute("sessaoUsuario", null);
	session.setAttribute("nivel", null);

	//Redirecionamento
	response.sendRedirect("index.jsp?msg=saida");
%>