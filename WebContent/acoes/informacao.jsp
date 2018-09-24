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

	<%
	
		int nome = Integer.parseInt(request.getParameter("item"));
	
		Resposta<ArrayList<Item>> resposta2 = ItemDao.selectWhere("id", Where.IGUAL, nome);
	
		Item item = null;
		
		if(resposta2.getFuncionou()){
			if(resposta2.getObjeto().isEmpty()){
				System.out.print("Item nao encontrado");
			}else{
				
				item = resposta2.getObjeto().get(0);
				
			}
			
		}else{
			//erro
		}
		
		//System.out.print(resposta2.getMensagem());
		//System.out.print(item.getNome());
	
		String estrutura = "<div class='informacao'>";
		
		estrutura += "<form class='infoForm'>";
		estrutura += "<h4>"+item.getNome().toUpperCase()+"</h4>";
		estrutura += "<img src=\"imagens/180.png\" width='180' height='180'>";
		estrutura += "<h5>Data Perdido: "+item.getDataPerdido()+"</h5>";
		estrutura += "<h6>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</h6>";
		estrutura += "<button type=\"submit\">Este item é meu</button>";		
		estrutura += "</form>";
		estrutura += "<form class='infoForm2' action='buscar.jsp'>";
		estrutura += "<button type=\"submit\">Voltar</button>";
		estrutura += "</form>";
		
		estrutura += "</div>";
		
		out.print(estrutura);
	
	%>