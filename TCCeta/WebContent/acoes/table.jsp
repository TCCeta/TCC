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
<link href="../css/bootstrap.min.css" rel="stylesheet">

<%
    
	Resposta<ArrayList<Funcionario>> resposta = FuncionarioDao.selectWhere("idEmpresa", Where.IGUAL, session.getAttribute("idEmpresa"));

	String estrutura;

	if (resposta.getFuncionou()) {

		estrutura = "<table class='table table-striped tabela'>";
		estrutura += "<tr>";
		estrutura += "<td><b>Login</b></td>";
		estrutura += "<td><b>IdEmpresa</b></td>";
		estrutura += "<td><b>Editar</b></td>";
		estrutura += "</tr>";

		ArrayList<Funcionario> lista = resposta.getObjeto();

		for (Funcionario funcionario : lista) {

			String ovo = "";
			ovo += session.getAttribute("nivel");
			int numero = Integer.parseInt(ovo);

			if (funcionario.getIdEmpresa() == numero) {

			
				
				estrutura += "<tr>";
				estrutura += "<td>" + funcionario.getLogin() + "</td>";
				estrutura += "<td>" + funcionario.getId()+ "</td>";
				estrutura += "<td><a href='edicao.jsp?cod_idFuncionario=" + funcionario.getId()
						+ "'><span class='glyphicon glyphicon-pencil'></span></a></td>";
				estrutura += "</tr>";

			}

		}
	} else {
		estrutura = "<h1>" + resposta.getMensagem()+"</h1>";
	}

	out.print(estrutura);

	
%>
