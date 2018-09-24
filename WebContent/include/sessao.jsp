<link href="../css/bootstrap.min.css" rel="stylesheet">

<%
	//Verificar se há sessão usuário
	if (session.getAttribute("sessaoUsuario") == null) {
		response.sendRedirect("index.jsp");
	}

	

%>