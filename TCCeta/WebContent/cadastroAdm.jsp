<%@ include file="include/topo_novo_com_sessao.jsp"%>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/mascaras.js"></script>

<main>

<div>
	<form class="jumbotron formularioCadastroUsuario"
		action="acoes/cadastrarAdm.jsp">
		<h1 class="titulosTabs">
			<center>Cadastro</center>
		</h1>

		<input type="text" placeholder="Usuário" class="form-control"
			id="inputs" name="userAdm"> <input type="text"
			class="cpf form-control" id="inputs" name="cpfAdm"> <input
			type="password" placeholder="Senha" class="form-control" id="inputs"
			name="pswdAdm"> <input type="submit" class="btn btn-default"
			id="inputs" value="Cadastrar">

	</form>
	<center>
		<button onclick="location.href = 'empresa.jsp';"
			class="btn btn-default">Cancelar</button>
	</center>
</div>

</main>

<%@ include file="include/rodape.jsp"%>


</body>

</html>