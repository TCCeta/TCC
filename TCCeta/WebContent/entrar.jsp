<%@ include file="include/topo_novo.jsp"%>
<link href="css/bootstrap.min.css" rel="stylesheet">
<%
	if (session.getAttribute("sessaoUsuario") == null) {
%>
<main>

<div>
	<form class="formularioLogin" action="acoes/logar.jsp">
		<h1>
			<center>Login</center>
		</h1>
		<p>
		<center>
			<input type="text" placeholder="Usu�rio" class="form-control" id="inputs" name="email">
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
			N�o tem login?<br>Fa�a seu cadastro <a href="cadastro.jsp">aqui</a>
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
			<center>Voc� j� fez login</center>
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