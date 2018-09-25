<%@page import="br.com.jsp.bean.Enums.NivelDeAcesso"%>
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
	String userAdm = request.getParameter("userAdm");
	String pswdAdm = request.getParameter("pswdAdm");
	String cpfAdm = request.getParameter("cpfAdm");

	//usuarioInformado, senhaInformado, nomeInformado, cpfInformado, emailInformado, telefoneInformado

	Resposta<ArrayList<Empresa>> resp = EmpresaDao.selectWhere("id", Where.IGUAL,
			session.getAttribute("idEmpresa"));

	Empresa e = null;

	if ((userAdm.equals("")) || (userAdm.equals(" ")) || (pswdAdm.equals("")) || (pswdAdm.equals(" "))) {
		response.sendRedirect("../cadastroAdm.jsp?msg=cadastroUsuarioFalhaCampo");
	} else {
		if (resp.getFuncionou()) {
			if (resp.getObjeto().isEmpty()) {
				//erro
				response.sendRedirect("../cadastroAdm.jsp?msg=cadastroUsuarioFalha");
			} else {
				e = resp.getObjeto().get(0);
				Conta c = new Conta(userAdm, pswdAdm, NivelDeAcesso.Funcionario);
				e.contratarFuncionario(cpfAdm, c);
				response.sendRedirect("../cadastroAdm.jsp?msg=cadastroUsuarioOk");
			}
		} else {
			//erro
			response.sendRedirect("../cadastroAdm.jsp?msg=cadastroUsuarioFalha1");
		}
	}

	// 	boolean funcionou = Usuario.cadastrar(u);

	// 	if (funcionou) {

	// 		response.sendRedirect("../cadastro.jsp?msg=cadastroUsuarioOk");

	// 	} else {

	// 		response.sendRedirect("../cadastro.jsp?msg=cadastroUsuarioFalha");

	// 	}
%>