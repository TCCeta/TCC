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


<div class="jumbotron tabela">
	<h1 class="titulosTabs">
		<strong> Administradores</strong>
	</h1>
	
	<%@ include file="acoes/table.jsp"%>
		<button onclick="location.href = 'cadastroAdm.jsp';"
			class="btn btn-default float-left submit-button">Cadastrar administrador</button>
	
</div>



</main>
<%@ include file="include/rodape.jsp"%>
</body>

</html>