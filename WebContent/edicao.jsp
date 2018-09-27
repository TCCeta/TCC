<%@page import="java.util.ArrayList"%>
<%@page import="br.com.jsp.bean.response.Resposta"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.Where"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.dao.FuncionarioDao"%>
<%@page import="br.com.jsp.bean.Funcionario"%>
<%@ include file="include/topo_novo_com_sessao.jsp"%>
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

<div>
	<form class="jumbotron formularioCadastroUsuario"
		action="acoes/editarFuncionario.jsp" method="post">
		<h1>
			<center>Editar Funcionário</center>
		</h1>
		<%
			//Obter o usuário
			int idUsuario = Integer.parseInt(request.getParameter("idFuncionario"));

			//Instanciar o objeto usuário
			Funcionario funcionario = new Funcionario();

			Resposta<ArrayList<Funcionario>> resp = FuncionarioDao.selectWhere("id", Where.IGUAL, idUsuario);

			if (resp.getFuncionou()) {
				funcionario = resp.getObjeto().get(0);
			} else {
				//erro funcionario nao encontrado
			}
			session.setAttribute("funcionarioAlterado", funcionario);
		%>

		<p>

			<input type="text"
				placeholder=<%out.print(funcionario.getConta().getLogin());%>
				class="form-control inputs" name="newlogin"> <input
				type="text" placeholder=<%out.print(funcionario.getCPF());%>
				class="form-control inputs" id="inputs" name="dat_cpfUsuario"
				disabled> <input type="password" placeholder="Nova Senha"
				class="form-control inputs" name="newSenha"> <input
				type="submit" class="btn btn-default" value="Cadastrar">
		</p>



	</form>
</div>

</main>

<%@ include file="include/rodape.jsp"%>


</body>

</html>
