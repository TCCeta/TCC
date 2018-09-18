<link href="../css/bootstrap.min.css" rel="stylesheet">

<%
	//Verificar se h� vari�vel msg na URL
	if (request.getParameter("msg") != null) {

		//Obter a mensagem
		String msg = request.getParameter("msg");

		//Exibir a mensagem
		switch (msg) {
		case "falhaLogin":
			out.print(
					"<div class='alert alert-danger msg'>Usu�rio ou senha incorretos.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "sessaoExpirada":
			out.print(
					"<div class='alert alert-danger msg'>Sess�o expirada, fa�a o login e tente novamente.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "saida":
			out.print(
					"<div class='alert alert-success msg'>Voc� saiu da �rea restrita com sucesso.   <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "cadastroUsuarioOk":
			out.print(
					"<div class='alert alert-success msg'>Usu�rio cadastrado com sucesso.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "cadastroUsuarioFalha":
			out.print(
					"<div class='alert alert-danger msg'>Falha no cadastro de usu�rio.   <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
		}

	}
%>