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

	Resposta<ArrayList<Item>> resposta = ItemDao.selectWhere("nome", Where.LIKE, nome);

	String estrutura = "";

	ArrayList<Item> lista = resposta.getObjeto();
	estrutura += "<div class =\"margin\">";

	for (Item item : lista) {
		
		/*<div class="buscar">			
					<h4>BATATA</h4>
					<img src="imagens/180.png">
		</div>*/
		estrutura += "<div class=\"buscar\"";
		//estrutura += "<h4>"+item.getNome()+"</h4>";
		estrutura += "<h4>BATATA</h4>";
		estrutura += "<img src=\"imagens/180.png\">";

	}
	estrutura += "</div>";

	out.print(estrutura);
%>