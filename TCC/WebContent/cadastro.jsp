<%@ include file="include/topo_novo.jsp"%>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/jquery.mask.js"></script>
<script>
	$(document).ready(function() {

		$('.cpf').mask('000.000.000-00', {placeholder : "___.___.___-__"});

	});
</script>
<main>

<div class="jumbotron formularioCadastroUsuario">
	<form action="acoes/cadastrarUser.jsp">
		<h1>
			<center>Cadastro</center>
		</h1>
		<p>
		<center>
			<input type="text" placeholder="Nome" class="form-control" id="inputs">
		</p>
		<p>
			<input type="text" placeholder="Usuário" class="form-control" id="inputs">
		</p>
		<p>
			<input type="text" class="form-control cpf" id="inputs" name="dat_cpfUsuario">
		</p>
		<p>
			<input type="password" placeholder="Senha" class="form-control" id="inputs">
		</p>
		<p>
			<input type="submit" value="Cadastrar">
		</center>
		</p>
	</form>
</div>

</main>

<%@ include file="include/rodape.jsp"%>


</body>

</html>
