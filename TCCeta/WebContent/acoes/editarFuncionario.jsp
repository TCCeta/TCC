<%@page import="br.com.jsp.bean.Empresa"%>
<%@page import="br.com.jsp.dao.EmpresaDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="br.com.jsp.bean.response.Resposta"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.Where"%>
<%@page import="br.com.jsp.dao.CriadorDeComandosSQL.GenericDao"%>
<%@page import="br.com.jsp.dao.FuncionarioDao"%>
<%@page import="br.com.jsp.bean.Funcionario"%>



<%



Funcionario funcionario = (Funcionario) session.getAttribute("funcionarioAlterado");

String newLogin = request.getParameter("newlogin");
String newSenha = request.getParameter("newSenha");

funcionario.getConta().setLogin(newLogin);
funcionario.getConta().setSenha(newSenha);

Funcionario.atualizar(funcionario);

response.sendRedirect("../empresa.jsp?msg=edicaoOk");

%>
