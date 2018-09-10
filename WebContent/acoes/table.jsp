<%@page import="javax.swing.JOptionPane"%>
<%@page import="br.com.jsp.bean.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.bean.Funcionario"%>
<%@page import="br.com.jsp.connector.ConnectionFactory"%>
<%@page import="br.com.jsp.bean.response.Resposta"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link href="../css/bootstrap.min.css" rel="stylesheet">

<%
    /*
	Resposta<ArrayList<Funcionario>> resposta = new GenericDao<Funcionario>(Funcionario.class)
			.selectAll();

	String estrutura;

	if (resposta.getFuncionou()) {

		estrutura = "<table class='table table-striped tabela'>";
		estrutura += "<tr>";
		estrutura += "<td><b>Login</b></td>";
		estrutura += "<td><b>Nome</b></td>";
		estrutura += "<td><b>IdEmpresa</b></td>";
		estrutura += "<td><b>Editar</b></td>";
		estrutura += "</tr>";

		ArrayList<Funcionario> lista = resposta.getObjeto();

		for (Funcionario funcionario : lista) {

			String ovo = "";
			ovo += session.getAttribute("id");
			int numero = Integer.parseInt(ovo);

			if (funcionario.getIdCliente() == numero) {

				estrutura += "<tr>";
				estrutura += "<td>" + funcionario.getLogin() + "</td>";
				estrutura += "<td>" + funcionario.getNome() + "</td>";
				estrutura += "<td>" + funcionario.getIdCliente() + "</td>";
				estrutura += "<td><a href='edicao.jsp?cod_idFuncionario=" + funcionario.getId()
						+ "'><span class='glyphicon glyphicon-pencil'></span></a></td>";
				estrutura += "</tr>";

			}

		}
	} else {
		estrutura = "<h1>Skidaddle skadoodle your dick is now a noodle</h1>";
	}

	out.print(estrutura);
*/
	
%>
