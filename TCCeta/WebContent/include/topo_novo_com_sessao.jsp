<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>

<!-- Incluir a verificação de sessão -->
<%@ include file="sessao.jsp"%>

<!DOCTYPE>
<html>
<head>

<!-- Bootstrap -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../css/bootstrap.min.css" rel="stylesheet">

<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/istique.js"></script>



<meta charse="UTF-8">
<title>What is missing</title>
</head>
<body>


	<%
		if (session.getAttribute("sessaoUsuario") == null) {
	%>

	<div class=bs-example data-example-id=inverted-navbar>
		<nav class="navbar navbar-inverse navbar-fixed-top">
			<div class=container-fluid>
				<div class=navbar-header>
					<button type=button class="collapsed navbar-toggle"
						data-toggle=collapse data-target=#bs-example-navbar-collapse-9
						aria-expanded=false>
						<span class=sr-only>Toggle navigation</span> <span class=icon-bar></span>
						<span class=icon-bar></span> <span class=icon-bar></span>
					</button>
					<a href="index.jsp" class=navbar-brand>What is Missing</a>
				</div>
				<div class="collapse navbar-collapse"
					id=bs-example-navbar-collapse-9>
					<ul class="nav navbar-nav">
						<li class=active><a href="index.jsp">Início</a></li>
						<li><a href="entrar.jsp">Login/Registre-se</a></li>
						<li><a href="contato.jsp">Contato</a></li>

						<li><a href="sair.jsp">Sair</a></li>
					</ul>
				</div>
			</div>
		</nav>
	</div>

	<%
		} else {
			if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) == 0) {
	%>

	<div class=bs-example data-example-id=inverted-navbar>
		<nav class="navbar navbar-inverse navbar-fixed-top">
			<div class=container-fluid>
				<div class=navbar-header>
					<button type=button class="collapsed navbar-toggle"
						data-toggle=collapse data-target=#bs-example-navbar-collapse-9
						aria-expanded=false>
						<span class=sr-only>Toggle navigation</span> <span class=icon-bar></span>
						<span class=icon-bar></span> <span class=icon-bar></span>
					</button>
					<a href="index.jsp" class=navbar-brand>What is Missing</a>
				</div>
				<div class="collapse navbar-collapse"
					id=bs-example-navbar-collapse-9>
					<ul class="nav navbar-nav">
						<li class=active><a href="index.jsp">Início</a></li>
						<li><a href="admin.jsp">Administrador</a></li>
						<li><a href="contato.jsp">Contato</a></li>

						<li><a href="sair.jsp">Sair</a></li>
					</ul>
				</div>
			</div>
		</nav>
	</div>




	<%
		} else {
				if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) == 2) {
	%>

	<div class=bs-example data-example-id=inverted-navbar>
		<nav class="navbar navbar-inverse navbar-fixed-top">
			<div class=container-fluid>
				<div class=navbar-header>
					<button type=button class="collapsed navbar-toggle"
						data-toggle=collapse data-target=#bs-example-navbar-collapse-9
						aria-expanded=false>
						<span class=sr-only>Toggle navigation</span> <span class=icon-bar></span>
						<span class=icon-bar></span> <span class=icon-bar></span>
					</button>
					<a href="index.jsp" class=navbar-brand>What is Missing</a>
				</div>
				<div class="collapse navbar-collapse"
					id=bs-example-navbar-collapse-9>
					<ul class="nav navbar-nav">
						<li class=active><a href="index.jsp">Início</a></li>
						<li><a href="buscar.jsp">Usuário</a></li>
						<li><a href="contato.jsp">Contato</a></li>

						<li><a href="sair.jsp">Sair</a></li>
					</ul>
				</div>
			</div>
		</nav>
	</div>


	<%
		} else {
					if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) == 1) {
	%>

	<div class=bs-example data-example-id=inverted-navbar>
		<nav class="navbar navbar-inverse navbar-fixed-top">
			<div class=container-fluid>
				<div class=navbar-header>
					<button type=button class="collapsed navbar-toggle"
						data-toggle=collapse data-target=#bs-example-navbar-collapse-9
						aria-expanded=false>
						<span class=sr-only>Toggle navigation</span> <span class=icon-bar></span>
						<span class=icon-bar></span> <span class=icon-bar></span>
					</button>
					<a href="index.jsp" class=navbar-brand>What is Missing</a>
				</div>
				<div class="collapse navbar-collapse"
					id=bs-example-navbar-collapse-9>
					<ul class="nav navbar-nav">
						<li class=active><a href="index.jsp">Início</a></li>
						<li><a href="empresa.jsp">Empresa</a></li>
						<li><a href="contato.jsp">Contato</a></li>

						<li><a href="sair.jsp">Sair</a></li>
					</ul>
				</div>
			</div>
		</nav>
	</div>


	<%
		}
				}
			}
		}
	%>
	<%@ include file="msg.jsp"%>