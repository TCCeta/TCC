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
<%@ include file="../buscar.jsp"%>



<script>
	var imgCarregada = document.createElement('image')
	var blobissimo = imagemdocumento.value
	imgCarregada.src = 'data:image/bmp;base64,' + Base64.encode(blobissimo);
	document.querySelector("#imagem").src = imgCarregada
</script>

<%
	String nome = request.getParameter("item");

	Resposta<ArrayList<Item>> resposta = ItemDao.selectWhere("nome", Where.LIKE, "%"+nome+"%");

	String estrutura = "";

	ArrayList<Item> lista = resposta.getObjeto();
	JOptionPane.showMessageDialog(null, resposta.getMensagem());	
	estrutura += "<div>";

	for (Item item : lista) {
		
		estrutura += "<h4>BATATA</h4>";

	}
	
	estrutura += "</div>";

	out.print(estrutura);
%>