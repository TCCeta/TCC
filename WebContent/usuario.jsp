<%@page import="br.com.jsp.dao.EmpresaDao"%>
<%@page import="br.com.jsp.bean.Empresa"%>
<%@page import="br.com.jsp.bean.Local"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.Where"%>
<%@page import="br.com.jsp.dao.FuncionarioDao"%>
<%@page import="br.com.jsp.bean.Conta"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="br.com.jsp.bean.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.bean.Funcionario"%>
<%@page import="br.com.jsp.connector.ConnectionFactory"%>
<%@page import="br.com.jsp.bean.response.Resposta"%>
<link href="css/bootstrap.min.css" rel="stylesheet">

<script>
	$(document).ready(function() {

		$('.data').mask('00/00/0000', {
			placeholder : "__/__/____"
		});

	});
</script>


<!-- Máscara da Data -->
<script src="js/jquery.mask.js"></script>

<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/mascaras.js"></script>

<%@ include file="include/topo_novo_com_sessao.jsp"%>
<%@ include file="include/rodape.jsp"%>

<%
	/*
		//Valida o nível
		if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) != 2) {
			response.sendRedirect("index.jsp");
		}
	*/
%>

<main>

<div>

	<!-- Nav tabs -->
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="tab-pane active"><a
			href="#itemsPerdidos" aria-controls="itemsPerdidos" role="tab"
			data-toggle="tab">Items Perdidos</a></li>
		<li role="presentation" class="tab-pane"><a
			href="#cadastroDeItem" aria-controls="cadastroDeItem" role="tab"
			data-toggle="tab">Perdi Algo</a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">

		<!--Aba de Busca-->

		<div role="tabpanel" class="tab-pane active" id="itemsPerdidos">

			<h1>
				<strong> Itens Perdidos </strong>
			</h1>

			<div>
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Local"
							id="botaoRedondo"> <input type="text"
							class="form-control" placeholder="Objeto" id="botaoRedondo">
						<input type="data" class="form-control data"
							name="dat_perdidoItem">

					</div>
					<button type="submit" class="btn btn-default">Buscar</button>
				</form>
			</div>
		</div>

		<!--Aba dos TICKETS*****************************************************************************************************-->
		<div role="tabpanel" class="tab-pane" id="cadastroDeItem">
			<h1>
				<strong> Não encontrei meu item </strong>
				<h5>Perdeu alguma coisa mas não consegue encontrar no site?</h5>

			</h1>
			<form class="formularioCadastro" action="cadastrarItemUsuario.jsp">
				<input type="text" placeholder="Nome" class="form-control"
					name="dad_nomeItem"><br> <input type="data"
					class="form-control data" name="dat_perdidoItem" />

				<%@ include file="acoes/listaEmpresas.jsp"%>


				<%
					/*
					
								<!-- DropBox do Local -->
								<div class="dropdown">
									<button class="btn btn-secondary dropdown-toggle form-control"
										type="button" id="dropdownMenuButton" data-toggle="dropdown"
										aria-haspopup="true" aria-expanded="false" name="local">
										Local</button>
									<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
										<a class="dropdown-item" href="#">Tem que colocar aqui a
											geração automatica de box para cada local cadastrados</a> <a
											class="dropdown-item" href="#">Another action</a> <a
											class="dropdown-item" href="#">Something else here</a>
									</div>
								</div>
					*/
				%>

				<textarea class="form-control" placeholder="Descrição" rows="3"
					name="inf_descItem"></textarea>
			</form>

			<!--Imagem-->
			<input type="file" onchange="readURL(this);"
				accept="image/png, image/jpg, image/jpeg" />

			<!--Função do preview da imagem-->
			<h5>Tem alguma foto do item?</h5>
			<script>
				function readURL(input) {

					if (input.files && input.files[0]) {
						var reader = new FileReader();

						reader.onload = function(e) {
							$('#imagemItem').attr('src', e.target.result);
						};

						reader.readAsDataURL(input.files[0]);

					}
				};
			</script>
			<img id="imagemItem" src="imagens/180.png" alt="your image"
				class="campoImagem" />
			<p>

				<center>
				<input type="submit" class="btn btn-default" value="Cadastrar">
			</center>
			</p>

		</div>

	</div>

</div>
</div>

</main>
<%@ include file="include/rodape.jsp"%></body>

				</html>