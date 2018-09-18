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


<main>

<div>

	<!-- Nav tabs -->
	<ul class="nav nav-tabs" role="tablist">
		<li role="presentation" class="active"><a href="#cadastroDeItem"
			aria-controls="cadastroDeItem" role="tab" data-toggle="tab">Administradores</a></li>
		<li role="presentation"><a href="#itemsPerdidos"
			aria-controls="itemsPerdidos" role="tab" data-toggle="tab">Cadastrar
				Administradores</a></li>

	</ul>

	<!-- Tab panes -->
	<div class="tab-content">

		<!--Aba dos itens-->
		<div role="tabpanel" class="tab-pane active" id="cadastroDeItem">
			<h1 class="titulosTabs">
				<strong> Administradores </strong>
			</h1>
	<%@ include file="acoes/table.jsp" %>


		</div>




		<!--Aba de Busca-->

		<div role="tabpanel" class="tab-pane" id="itemsPerdidos">

			<h1 class="titulosTabs">
				<strong> Cadastrar Administradores </strong>
			</h1>

				<form action="acoes/cadastrarAdm.jsp">
				
				</form>

		</div>

	</div>
</div>

</main>
<%@ include file="include/rodape.jsp"%>
</body>

</html>