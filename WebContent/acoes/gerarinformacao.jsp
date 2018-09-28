<%@page import="br.com.jsp.dao.ItemDao"%>
<%@page import="br.com.jsp.bean.Item"%>
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
<%@ include file="../include/topo_novo_com_sessao.jsp"%>

<link href="css/estilos.css" rel="stylesheet">
<link href="css/bootstrap.min.css" rel="stylesheet">
<script src="js/preview.js"></script>

<!-- Máscara da Data -->
<script src="js/jquery.mask.js"></script>

<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.mask.js"></script>
<script type="text/javascript" src="js/mascaras.js"></script>



<%
	try {
		int nome = Integer.parseInt(request.getParameter("item"));

		Resposta<ArrayList<Item>> resposta2 = ItemDao.selectWhere("id", Where.IGUAL, nome);

		Item item = null;

		if (resposta2.getFuncionou()) {
			if (resposta2.getObjeto().isEmpty()) {
				System.out.print("Item nao encontrado");
			} else {

				item = resposta2.getObjeto().get(0);

			}

		} else {
			//erro
		}

		System.out.print(resposta2.getMensagem());
		System.out.print(item.getNome());

		//ArrayList<Item> lista2 = resposta2.getObjeto();

		String estrutura = "<div class='informacao'>";

		estrutura += "<form class='infoForm'>";
		estrutura += "<h4>" + item.getNome().toUpperCase() + "</h4>";
		estrutura += "<center><img src=\"imagens/180.png\"></center>";
		estrutura += "<h5><b>Quando esse item foi perdido: </b>" + item.getDataPerdido() + "</h5>";
		estrutura += "<b>Descrição: </b>" + item.dad_descricaoItem();
		estrutura += "</form>";
		estrutura += "</div>";
		
		estrutura += "<br><center><button onclick=\"location.href = 'buscar.jsp?msg=contatoOk';\" class=\"btn btn-default float-left submit-button\">Este item é meu</button></center>";
		estrutura += "<br><center><button onclick=\"location.href = 'buscar.jsp';\" class=\"btn btn-default float-left submit-button\">Voltar</button></center>";

		out.print(estrutura);
	} catch (Exception e) {

	}
%>