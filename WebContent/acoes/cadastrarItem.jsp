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
<%@page import="br.com.jsp.bean.Usuario"%>
<%
	String nome = request.getParameter("dad_nomeItem");
	String data = request.getParameter("dat_perdidoItem");
	String desc = request.getParameter("inf_descItem");
	String img = request.getParameter("imagemPicked2");

	if ((nome.equals("")) || (data.equals(" ")) || (desc.equals("")) || (img.equals(" "))) {
		response.sendRedirect("../admin.jsp?msg=cadastroItemFalhaCampo");
	} else {
		response.sendRedirect("../admin.jsp?msg=cadastroItemOk");

	}
%>