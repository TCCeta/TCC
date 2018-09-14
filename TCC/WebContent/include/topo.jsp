<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>

<!-- Incluir a verificação de sessão -->
<%@ include file="sessao.jsp"%>
<!DOCTYPE>
<html>
<head>

<!-- Bootstrap -->
<link href="../css/bootstrap.min.css" rel="stylesheet">

<!-- CSS -->
<link href="css/estilos.css" rel="stylesheet">

<meta charse="UTF-8">
<title>What is missing</title>
</head>
<body>


	<%
		if (session.getAttribute("sessaoUsuario") == null) {
	%>

	<header class="header">
		<h1>What is missing</h1>
		<nav id="menu">
			<a href="index.jsp">Ínicio</a> <a href="entrar.jsp">Login/Registre-se</a>
			<a href="sair.jsp">Sair</a>

		</nav>

	</header>
	<%@ include file="msg.jsp"%>


	<%
		} else {
			if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) == 1) {
	%>

	<header class="header">
		<h1>What is missing</h1>
		<nav id="menu">
			<a href="index.jsp">Ínicio</a> <a href="admin.jsp">Administrador</a>
			<a href="sair.jsp">Sair</a>

		</nav>

	</header>
	<%@ include file="msg.jsp"%>


	<%
		} else if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) == 2) {
	%>

	<header class="header">
		<h1>What is missing</h1>
		<nav id="menu">
			<a href="index.jsp">Ínicio</a> <a href="buscar.jsp">Buscar</a> <a
				href="sair.jsp">Sair</a>

		</nav>

	</header>
	<%@ include file="msg.jsp"%>
	<%
		}
		}
	%>