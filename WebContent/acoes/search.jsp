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

<script>
	var imgCarregada = document.createElement('image')
	var blobissimo = imagemdocumento.value
	imgCarregada.src = 'data:image/bmp;base64,' + Base64.encode(blobissimo);
	document.querySelector("#imagem").src = imgCarregada
</script>

<%
		String nome = request.getParameter("item");
		String nomeEmpresa = request.getParameter("nomeEmpresa");
		String data = request.getParameter("dat_perdidoItem");

		Resposta<ArrayList<Item>> nomeItem = ItemDao.selectWhere("nome", Where.LIKE, "%"+nome+"%");

	String estrutura2 = "";

	if(nomeItem.getFuncionou()){
		System.out.println("funcionou");
	}else{
		System.out.println(nomeItem.getMensagem());
	}
	
	ArrayList<Item> lista2 = nomeItem.getObjeto();
	
	//System.out.println();
	//JOptionPane.showMessageDialog(null, resposta2.getMensagem());	
	estrutura2 += "<div class =\"margin\">";

	for (Item item : lista2) {

		estrutura2 += "<form class=\"buscar\" action=\"informacoes.jsp\">";
		estrutura2 += "<input type='text' name='item' value='"+item.getId()+"'>";
		estrutura2 += "<div class=\"form\">";
		estrutura2 += "<h4>"+item.getNome().toUpperCase()+"</h4>";
		estrutura2 += "<img src=\"imagens/180.png\" width='180' height='180'>";
		estrutura2 += "</div>";
		estrutura2 += "<button type=\"submit\">Informações</button>";
		estrutura2 += "</form>";

	}
	
	estrutura2 += "</div>";

	out.print(estrutura2);
%>