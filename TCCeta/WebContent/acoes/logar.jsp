<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.Where"%>
<%@page import="br.com.jsp.bean.Conta"%>
<%@page import="br.com.jsp.dao.ContaDao"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="br.com.jsp.bean.Usuario"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.jsp.bean.Enums.NivelDeAcesso"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.bean.Funcionario"%>
<%@page import="br.com.jsp.connector.ConnectionFactory"%>
<%@page import="br.com.jsp.bean.response.Resposta;"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="../css/bootstrap.min.css" rel="stylesheet">

<%
	//Obter o email e a senha
	String loginInformado = request.getParameter("email");
	String senhaInformada = request.getParameter("senha");

	//Instanciar um objeto bean
	Funcionario ub = new Funcionario();

	Resposta<ArrayList<Funcionario>> respostaF = new GenericDao<Funcionario>(Funcionario.class)
			.selectAll();

	Resposta<ArrayList<Usuario>> respostaU = new GenericDao<Usuario>(Usuario.class).selectAll();
	
	
	
	
	
	
	
	Resposta<ArrayList<Conta>> respostaContas = ContaDao.selectWhere("login", Where.IGUAL, loginInformado);
	
	
	
	if(respostaContas.getObjeto().get(0).senhaEstaCorreta(senhaInformada)){
		
		int nivel = respostaContas.getObjeto().get(0).getNivelDeAcesso();
		
		if(nivel == NivelDeAcesso.Empresa.ordinal()){
			
			//login com empresa
			
		}else if(nivel == NivelDeAcesso.Funcionario.ordinal()){
			
			//login com funcionario
			//response.sendRedirect("../admin.jsp");
			
		}else if(nivel == NivelDeAcesso.Usuario.ordinal()){
			
			//login com usuario
			//response.sendRedirect("../buscar.jsp");

		}
		
	}
	
	
	
	/*
	

	if (!respostaF.getFuncionou() || !respostaU.getFuncionou()) {

		response.sendRedirect("../entrar.jsp?msg=falhaLogin");

	} else {

		ArrayList<Funcionario> listaFuncionarios = new GenericDao<Funcionario>(Funcionario.class)
				.selectAll().getObjeto();

		ArrayList<Usuario> listaUsuarios = new GenericDao<Usuario>(Usuario.class).selectAll()
				.getObjeto();

		boolean encontrou = false;

		for (Funcionario funcionario : listaFuncionarios) {
			if ((funcionario.getLogin().equals(email)) && (funcionario.getSenha().equals(senha))) {

				//fazer login de funcionario
				//Criar a sessão			
				session.setAttribute("sessaoUsuario", ub);
				session.setAttribute("nivel", 1);
				session.setAttribute("id", funcionario.getIdCliente());
				response.sendRedirect("../admin.jsp");
				encontrou = true;
			}

		}

		for (Usuario usuario : listaUsuarios) {

			if ((usuario.getLogin().equals(email)) && (usuario.getSenha().equals(senha))) {
				//fazer login de usuario
				//vai pra proxima pagina de usuario
				session.setAttribute("sessaoUsuario", ub);
				session.setAttribute("nivel", 2);
				response.sendRedirect("../buscar.jsp");
				encontrou = true;
			}
		}

		out.print("haha yes");

		if (!encontrou) {
			response.sendRedirect("../login.jsp?msg=falhaLogin");
		}

	}
	*/
%>