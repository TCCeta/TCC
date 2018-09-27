<%@page import="br.com.jsp.dao.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.jsp.bean.Funcionario"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.bean.response.Resposta"%>

<%@page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="include/topo_novo.jsp"%>

<link href="css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap.js"></script>

<link href="css/estilos.css" rel="stylesheet">
<script src="js/preview.js"></script>

<!-- Máscara da Data -->
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/jquery.mask.js"></script>
<script src="js/upload.js"></script>


<script type="text/javascript" src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/mascaras.js"></script>

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
			<div role="tabpanel" class="jumbotron tab-pane active"
				id="cadastroDeItem">
				<h1 class="titulosTabs">
					<strong> Cadastro de Items </strong>
				</h1>
				<form class="formularioCadastro divLeft"
					action="acoes/cadastrarItem.jsp">
					<input type="text" placeholder="Nome" class="form-control"
						name="dad_nomeItem"> <br>
					<input type="date" class="data form-control" name="dat_perdidoItem"><br>
					<textarea class="form-control" placeholder="Descrição" rows="3"
						name="inf_descItem"></textarea>

					<!--Imagem-->
					<input type="file" onchange="readURL(this);"
						accept="image/png, image/jpg, image/jpeg" name="imagemPicked2" />
					<img id="imagemItem" src="imagens/180.png" alt="your image"
						name="imagemPicked" class="campoImagem" />



					<center>
						<input type="submit" class="btn btn-default" value="Cadastrar">
					</center>


				</form>

				//////// usar o class.getpath...


			</div>



		<!--Aba de Busca-->

		<!-- 		<div role="tabpanel" class="tab-pane" id="itemsPerdidos"> -->

		<!-- 			<h1 class="titulosTabs"> -->
		<!-- 				<strong> Items Perdidos </strong> -->
		<!-- 			</h1> -->

		<!-- 			<div> -->
		<!-- 				<form class="navbar-form navbar-left" role="search"> -->
		<!-- 					<div class="form-group"> -->
		<!-- 						<input type="text" class="form-control" placeholder="Local" -->
		<!-- 							id="botaoRedondo"> <input type="text" -->
		<!-- 							class="form-control" placeholder="Objeto" id="botaoRedondo"> -->
		<!-- 						<input type="data" class="form-control data" -->
		<!-- 							name="dat_perdidoItem" /> -->
		<!-- 					</div> -->
		<!-- 					<button type="submit" class="btn btn-default">Buscar</button> -->
		<!-- 				</form> -->
		<!-- 			</div> -->
		<!-- 		</div> -->

	</div>

</div>
</div>



</main>
<%@ include file="include/rodape.jsp"%>
</body>

</html>