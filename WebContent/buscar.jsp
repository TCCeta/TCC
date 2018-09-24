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
<script src="js/preview.js"></script>

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
<div class="topo">
	<h1>
		<strong> Itens Perdidos </strong>
	</h1>

	<div>
		<form class="navbar-form navbar-left" role="search" action="buscar.jsp">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Local" id="botaoRedondo" name="nomeEmpresa"> 
				<input type="text" name="item" class="form-control" placeholder="Objeto" id="botaoRedondo">
				<input type="date" class="form-control data" name="dat_perdidoItem">
			</div>
			<button type="submit" class="btn btn-default">Buscar</button>
		</form>
	</div>
</div>

<%@ include file="acoes/search.jsp"%>

</main>
<%@ include file="include/rodape.jsp"%></body>

</html>