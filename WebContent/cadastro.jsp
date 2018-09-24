<%@ include file="include/topo_novo.jsp"%>
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/jquery.mask.js"></script>
<script>
	$(document).ready(function() {

		$('.cpf').mask('000.000.000-00', {
			placeholder : "___.___.___-__"
		});

	});
</script>
<main>

<div class="jumbotron formularioCadastroUsuario">
	<form action="acoes/cadastrarUser.jsp">
		<h1>
			<center>Cadastro</center>
		</h1>

		<p>
			<input type="text" placeholder="Rua" class="form-control" id="inputs"
				name="rua"> 
				<input type="text" placeholder="Bairro"
				class="form-control" id="inputs" name="bairro">
		</p>
		<p>
			<input type="text" placeholder="Cidade" class="form-control"
				id="inputs" name="cidade">
				<input type="text"
				placeholder="Estado" class="form-control" id="inputs" name="estado">
		</p>
		<p>
			<input type="text" placeholder="CEP" class="form-control" id="inputs"
				name="cep">
				<input type="text" placeholder="Nome"
				class="form-control" id="inputs" name="cad_nome">
		</p>

		<p>
			<input type="text" class="form-control cpf" id="inputs"
				name="dat_cpfUsuario">
				<input type="email"
				placeholder="E-mail" class="form-control" id="inputs"
				name="dad_email">
		</p>

		<p>
			<input type="text" placeholder="Telefone/Celular"
				class="form-control" id="inputs" name="telefone">
				<input type="text"
				placeholder="Usuário" class="form-control" id="inputs" name="cad_user">
		</p>
		<p>
			<input type="password" placeholder="Senha" class="form-control"
				id="inputs" name="cad_senha">
		</p>

		<p>
			<input type="submit" value="Cadastrar">
		</p>
	</form>
</div>

</main>

<%@ include file="include/rodape.jsp"%>


</body>

</html>
