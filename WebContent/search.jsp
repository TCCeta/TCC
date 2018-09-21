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
<%@ include file="buscar.jsp" %>

<script>
	var imgCarregada = document.createElement('image')
	var blobissimo = imagemdocumento.value
	imgCarregada.src = 'data:image/bmp;base64,' + Base64.encode(blobissimo);
	document.querySelector("#imagem").src = imgCarregada
</script>

<%
	String nome = request.getParameter("item");

		Resposta<ArrayList<Item>> resposta2 = ItemDao.selectWhere("nome", Where.LIKE, "%"+nome+"%");

	String estrutura2 = "";

	if(resposta2.getFuncionou()){
		System.out.println("funcionou");
	}else{
		System.out.println(resposta2.getMensagem());
	}
	
	ArrayList<Item> lista2 = resposta2.getObjeto();
	
	System.out.println();
	//JOptionPane.showMessageDialog(null, resposta2.getMensagem());	
	estrutura2 += "<div class =\"margin\">";

	for (Item item : lista2) {
		
		/*<div class="buscar">			
					<h4>BATATA</h4>
					<img src="imagens/180.png">
		</div>*/
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