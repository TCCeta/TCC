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

<%
//asdasdasdasd
	//UBAUBA
	Resposta<ArrayList<Empresa>> resposta = EmpresaDao.selectAll();

	String estrutura = "";

	ArrayList<Empresa> lista = resposta.getObjeto();
	estrutura += "<select class='form-control'>";

	for (Empresa empresa : lista) {

		estrutura += "<option>" + empresa.getNome() + "</option>";

	}
	estrutura += "</select>";

	out.print(estrutura);
%>

