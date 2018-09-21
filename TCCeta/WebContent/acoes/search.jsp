<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="br.com.jsp.dao.ItemDao"%>
<%@page import="br.com.jsp.bean.Item"%>
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
<link href="../css/estilos.css" rel="stylesheet">
<%@ include file="../buscar.jsp" %>

<script>
	var imgCarregada = document.createElement('image')
	var blobissimo = imagemdocumento.value
	imgCarregada.src = 'data:image/bmp;base64,' + Base64.encode(blobissimo);
	document.querySelector("#imagem").src = imgCarregada
</script>

<%
	String nome = request.getParameter("item");

	String nomeEmpresa = request.getParameter("nomeEsmpresa");
	
	String data = request.getParameter("dat_perdidoItem");

		Resposta<ArrayList<Item>> resposta2 = ItemDao.selectWhere("nome", Where.LIKE, "%"+nome+"%");

		
		
		Resposta<ArrayList<Item>> respostaNome = ItemDao.selectWhere("nome", Where.LIKE, "%"+nome+"%");
		
		Resposta<ArrayList<Item>> respostaData =  ItemDao.selectWhere("dataPerdido", Where.IGUAL, data);
		
		Resposta<ArrayList<Empresa>> respEmpresa = EmpresaDao.selectWhere("nome", Where.LIKE, "%"+nomeEmpresa+"%");
		
		if(!respostaNome.getFuncionou()){
			//erro no select do nome
			String estrutura999 = "<h2>Este item não foi encontrado</h2>";
			out.print(estrutura999);
		}else if(!respostaData.getFuncionou()){
			//erro no select da data
			String estrutura999 = "<h2>Item não encontrado nessa data</h2>";
			out.print(estrutura999);
		}else if(!respEmpresa.getFuncionou()){
			String estrutura999 = "<h2>Item não encontrado nessa empresa</h2>";
			out.print(estrutura999);
		}else{
			
			if(respostaNome.getObjeto().isEmpty() || respostaData.getObjeto().isEmpty() || respEmpresa.getObjeto().isEmpty() ){
				//nenhum item foi encontrado
			}else{
				
				ArrayList<Item> lista = new ArrayList<>(); 
				
				for(Item item : respostaNome.getObjeto()){
				
					boolean encontrou = false;
					
					for(Item item2 : respostaData.getObjeto()){
						
						
						for(Empresa empresa : respEmpresa.getObjeto()){
							
							if(item.equals(item2) && item.getEmpresa().equals(empresa)){
								
								lista.add(item);
								encontrou = true;
								break;
							}
							
						}
						
						if(encontrou){
							break;
						}
						
					}
					
				}
				
			}
			
		}
		
		
		
		
		
		
		
		
	String estrutura2 = "";

	if(resposta2.getFuncionou()){
		System.out.println("funcionou");
	}else{
		System.out.println(resposta2.getMensagem());
	}
	
	ArrayList<Item> lista2 = resposta2.getObjeto();
	
	System.out.println();
	estrutura2 += "<div class =\"margin\">";

	for (Item item : lista2) {
		

		estrutura2 += "<form class=\"buscar\" action=\"informacao.jsp\">";
		estrutura2 += "<input type='text' name='item' value='"+item.getId()+"'>";
		estrutura2 += "<div class=\"form\">";
		estrutura2 += "<h4>"+item.getNome().toUpperCase()+"</h4>";
		estrutura2 += "<img src=\"imagens/180.png\">";
		estrutura2 += "</div>";
		estrutura2 += "<button type=\"submit\">Informações</button>";
		estrutura2 += "</form>";

	}
	
	estrutura2 += "</div>";

	out.print(estrutura2);
%>