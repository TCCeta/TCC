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
	String ruaInformada = request.getParameter("rua");
	String bairroInformado = request.getParameter("bairro");
	String cidadeInformada = request.getParameter("cidade");
	String estadoInformado = request.getParameter("estado");
	String cepInformado = request.getParameter("cep");
	String nomeInformado = request.getParameter("cad_nome");
	String cpfInformado = request.getParameter("dat_cpfUsuario");
	String emailInformado = request.getParameter("dad_email");
	String telefoneInformado = request.getParameter("telefone");
	String usuarioInformado = request.getParameter("cad_user");
	String senhaInformado = request.getParameter("cad_senha");

	
	//usuarioInformado, senhaInformado, nomeInformado, cpfInformado, emailInformado, telefoneInformado
	
	Usuario u = new Usuario(usuarioInformado, senhaInformado, nomeInformado, cpfInformado, emailInformado, telefoneInformado, new Local(ruaInformada, bairroInformado, cidadeInformada, estadoInformado, cepInformado));
 			

	Usuario.cadastrar(u);
	response.sendRedirect("../cadastro.jsp?msg=cadastroUsuarioOk");
// 	boolean funcionou = Usuario.cadastrar(u);

// 	if (funcionou) {

// 		response.sendRedirect("../cadastro.jsp?msg=cadastroUsuarioOk");

// 	} else {

// 		response.sendRedirect("../cadastro.jsp?msg=cadastroUsuarioFalha");

// 	}
%>