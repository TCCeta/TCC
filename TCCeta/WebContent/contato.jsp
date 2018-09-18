<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.dao.FuncionarioDao"%>
<%@page import="br.com.jsp.bean.Funcionario"%>
<%@ include file="include/topo_novo.jsp"%>
<link href="css/estilos.css" rel="stylesheet">
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

<div class="jumbotron telaContato">
	<form>
		<h1>Contato</h1>
		<p>
			<strong>Email:</strong> tcc@gmail.com.br
		</p>
		<p>
			<strong>Telefone:</strong> (47) 3322-3344
		</p>
		<p>
			<strong>Whatsapp:</strong> (47) 99876-5432
		</p>
		<p>
			<strong>Endereço:</strong> Shopping Neumarkt (piso principal) Rua 7
			de Setembro 1213 Loja 129 - Centro 89.010-911 - Blumenau / SC
		<p>
	</form>
</div>

<div>
	<section>
		<iframe
			src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d14229.939545929574!2d-49.0689354!3d-26.9198385!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xa5ec9dd8406a6a7!2sProWay+IT+Training!5e0!3m2!1spt-BR!2sbr!4v1535975557455"
			class="mapaContato"></iframe>
	</section>
</div>
</main>

<%@ include file="include/rodape.jsp"%>


</body>

</html>
