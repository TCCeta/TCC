<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Máscara da Data -->
<script src="js/jquery.mask.js"></script>

<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/mascaras.js"></script>

<%@ include file="include/topo_novo_com_sessao.jsp"%>
<%@ include file="include/rodape.jsp"%>

<%/*
	//Valida o nível
	if (Integer.parseInt(String.valueOf(session.getAttribute("nivel"))) != 2) {
		response.sendRedirect("index.jsp");
	}
*/
%>


<!-- Campos de busca -->
<main>
<div>
	<busca> <!--                    *******************************************************************-->
	<form2 class="navbar-form navbar-left" role="search">
	<div class="form-group">
		<input type="text" placeholder="Local" id="botaoRedondo"> <input
			type="text" placeholder="Objeto" id="botaoRedondo"> <input
			type="data" class="data" name="dat_perdidoItem">
	</div>
	<button type="submit" class="btn btn-default">Buscar</button>
	</form2> </busca>
</div>

<!-- Ordem dos Itens Encontrados --> </main>


</body>

</html>