<%@ include file="include/topo_novo.jsp"%>
<link href="css/bootstrap.min.css" rel="stylesheet">
<%
	if (session.getAttribute("sessaoUsuario") == null) {
%>
<main>

<div class="jumbotron formularioLogin">
	<form	 action="acoes/logar.jsp">
		<h1>
			<center>Login</center>
		</h1>
		<p>
		<center>
			<input type="text" placeholder="Usuário" class="form-control" id="inputs" name="email">
		</center>
		</p>
		<p>
		<center>
			<input type="password" placeholder="Senha" class="form-control" id="inputs"
				name="senha">
		</center>
		</p>
		<p>
		<center>
			<input type="submit" value="Entrar">
		</center>
		
		</p>
		<p>
		<center>
			Não tem login?<br>Faça seu cadastro <a href="cadastro.jsp">aqui</a>
		</center>
		</p>
	</form>
</div>

</main>

<%@ include file="include/rodape.jsp"%>


</body>

</html>
<%
	} else {
%>

<main>

<div>
	<form class="formularioLogin" action="acoes
	/logar.jsp">
		<h1>
			<center>Você já fez login</center>
		</h1>
		<p>
		<center>
			Voltar para a <a href="index.jsp">home</a>
			<center></center>
			</p>
	</form>
</div>

</main>

<%@ include file="include/rodape.jsp"%>


</body>

</html>

<%
	}
%>