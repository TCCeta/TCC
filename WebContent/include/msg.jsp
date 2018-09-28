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

		case "saidaInvalida":
			out.print(
					"<div class='alert alert-danger msg'>Voc� n�o est� logado!   <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "edicaoOk":
			out.print(
					"<div class='alert alert-success msg'>Usu�rio editado com sucesso.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "edicaoFalha":
			out.print(
					"<div class='alert alert-success msg'>Falha na edi��o do usu�rio.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "cadastroUsuarioOk":
			out.print(
					"<div class='alert alert-success msg'>Usu�rio cadastrado com sucesso.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;
		case "cadastroUsuarioFalhaCampo":
			out.print(
					"<div class='alert alert-danger msg'>Preencha todos os campos.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;

		case "cadastroItemOk":
			out.print(
					"<div class='alert alert-success msg'>Item cadastrado com sucesso.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>�</span></button></div>");
			break;
		case "cadastroItemFalhaCampo":
			out.print(
					"<div class='alert alert-danger msg'>Preencha todos os campos.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>�</span></button></div>");
			break;
			
		case "contatoOk":
			out.print(
					"<div class='alert alert-success msg'>Um ticket foi registrado,\n venha ao estabelecimento para confirmar a propriedade do item.  <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>�</span></button></div>");
			break;

		case "cadastroUsuarioFalha":
			out.print(
					"<div class='alert alert-danger msg'>Falha no cadastro de usu�rio.   <button type='button' class='close' data-dismiss='alert' aria-label='Close'> <span aria-hidden='true'>&times;</span></button></div>");
			break;
		}

	}
%>