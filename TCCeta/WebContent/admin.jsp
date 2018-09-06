<%@page import="br.com.jsp.dao.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.jsp.bean.Funcionario"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.bean.response.Resposta"%>

<%@page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link href="css/estilos.css" rel="stylesheet">
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap.js"></script>

<!-- Máscara da Data -->
<script src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/mascaras.js"></script>

<script type="text/javascript" src="js/jquery.mask.js"></script>

<%@ include file="include/topo_novo_com_sessao.jsp"%>
<%
/*	//Valida o nÃ­vel
	if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) != 1) {
		response.sendRedirect("index.jsp");
	}*/
%>

<main>

<div>

	<!-- Nav tabs -->
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#cadastroDeItem"
			aria-controls="cadastroDeItem" role="tab" data-toggle="tab">Cadastro
				de Items</a></li>
		<li role="presentation"><a href="#itemsPerdidos"
			aria-controls="itemsPerdidos" role="tab" data-toggle="tab">Items
				Perdidos</a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">

		<!--Aba dos itens-->
		<div role="tabpanel" class="tab-pane active" id="cadastroDeItem">
			<h1 class="titulosTabs">
				<strong> Cadastro de Items </strong>
			</h1>
			<form class="formularioCadastro" action="cadastrarItem.jsp">
				<input type="text" readonly="readonly" placeholder="Codigo"
					class="form-control" name="cod_idItem"><br> <input
					type="text" placeholder="Nome" class="form-control"
					name="dad_nomeItem"><br> <input type="data"
					class="form-control data" name="dat_perdidoItem"><br>
				<input type="text" placeholder="IdCliente" class="form-control"
					name="cod_idCliente"><br>
				<textarea class="form-control" placeholder="Descrição" rows="3"
					name="inf_descItem"></textarea>
			</form>

			<!--Imagem-->
			<input type="file" onchange="readURL(this);"
				accept="image/png, image/jpg, image/jpeg" />

			<!--Função do preview da imagem-->
			<script>
				function readURL(input) {

					if (input.files && input.files[0]) {
						var reader = new FileReader();

						reader.onload = function(e) {
							$('#imagemItem').attr('src', e.target.result);
						};

						//                                var nome = input.value.toString();
						//                                alert(nome);

						//                                if (nome.indexOf(".jpg") == -1) {
						//
						//                                } else if (nome.indexOf(".jpeg") == -1) {
						//
						//                                } else if (nome.indexOf(".png") == -1) {
						//
						//                                } else {
						//                                    reader.readAsDataURL(input.files[0]);
						//                                }
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




		<!--Aba de Busca-->

		<div role="tabpanel" class="tab-pane" id="itemsPerdidos">

			<h1 class="titulosTabs">
				<strong> Items Perdidos </strong>
			</h1>

			<div>
				<form class="navbar-form navbar-left" role="search">
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Local"
							id="botaoRedondo"> <input type="text"
							class="form-control" placeholder="Objeto" id="botaoRedondo">
						<input type="data" class="form-control data"
							name="dat_perdidoItem" />
					</div>
					<button type="submit" class="btn btn-default">Buscar</button>
				</form>
			</div>
		</div>

		
	</div>

</div>
</div>

</main>
<%@ include file="include/rodape.jsp"%>
</body>

</html>