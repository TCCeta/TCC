package br.com.jsp.bean.Locais;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
public class Conexao {
	private final String url;
    private final String user = "root";
    private final String password = "";
	
	public Conexao() {
        String s= "WhatsMissing";
        url = "jdbc:mysql://localhost:3306/" + s;
	}
	
	 public Connection obterConexao(){
	        
		 Connection conexao = null;
	        
	        try{
	            conexao = DriverManager.getConnection(url, user, password);
	            
	        }catch(Exception e){
	        	if (e.getMessage().equals("Unknown database 'whatsmissing'")){
	        		try {
						conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", user, password);						
						PreparedStatement pstmt = conexao.prepareStatement("CREATE DATABASE WhatsMissing;");
						pstmt.execute();
						conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/whatsmissing", user, password);
						criarTabela(conexao);
						criarRelacionamento(conexao);
						inserirBaseLocais(conexao);
						conexao.close();
					} catch (SQLException e1) {System.out.println("Falha na cria��o: " + e1.getMessage());}
					

	        	}
	        	else {
	        		JOptionPane.showMessageDialog(null, e.getMessage());
	        		throw new RuntimeException(e);
	        	}
	            
	        }
	        return conexao;
	        
	    }

	 private void criarTabela(Connection conexao){
		 String[] comandos = new String[11];
		 try {
			 Statement pstmt = conexao.createStatement();
			 comandos[0] = "CREATE TABLE cidades ("
					 	 + "cod_idCidade INT AUTO_INCREMENT PRIMARY KEY,"
					 	 + "dad_nomeCidade varchar(100),"
					 	 + "cod_idEstado INT);";

			 comandos[1] = "CREATE TABLE estados("
					 	 + "cod_idEstado INT AUTO_INCREMENT PRIMARY KEY,"
					 	 + "dad_nomeEstado varchar(100),"
					 	 + "dad_siglaEstado varchar(2));";
		
			 comandos[2] = "CREATE TABLE empresas ("
					 	 + "cod_idEmpresa INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
					 	 + "dad_nomeEmpresa varchar(50) ,"
					 	 + "dad_cnpjEmpresa varchar(20) ,"
					 	 + "dad_emailEmpresa varchar(100) ,"
					 	 + "dad_telefoneEmpresa varchar(15) ,"
					 	 + "cod_idConta INT ,"
					 	 + "cod_idLourado INT);";

			 comandos[3] = "CREATE TABLE `funcionarios` ("
						 + "`cod_idFuncionario` INT  AUTO_INCREMENT PRIMARY KEY,"
						 + "`cod_idEmpresa` INT ,"
						 + "`cod_idPessoa` INT ,"
						 + "`cod_idConta` INT"
						 + ");";
			 
			 comandos[4] = "CREATE TABLE itens ("
					 	 + "cod_idItem INT  AUTO_INCREMENT PRIMARY KEY ,"
					 	 + "cod_idFuncionario INT ,"
					 	 + "cod_idImagem INT ,"
					 	 + "dat_dataPerdidoItem DATE ,"
					 	 + "dat_dataDevolvidoItem DATE ,"
					 	 + "dad_devolvidoItem BOOLEAN);";

			 comandos[5] = "CREATE TABLE usuarios ("
					  	 + "cod_idUsuario INT AUTO_INCREMENT PRIMARY KEY,"
					  	 + "cod_idConta INT NOT NULL,"
					  	 + "cod_idLurado INT NOT NULL,"
					  	 + "cod_idPessoa INT NOT NULL);";
			 
			 comandos[6] = "CREATE TABLE imagens ("
					 	 + "cod_idImagem INT AUTO_INCREMENT PRIMARY KEY,"
					 	 + "img_Imagem blob);";

			 comandos[7] = "CREATE TABLE pessoas ("
			  			 + "cod_idPessoa INT AUTO_INCREMENT PRIMARY KEY,"
			  			 + "dad_nomePessoa varchar(50),"
			  			 + "dad_cpfPessoa varchar(14),"
			  			 + "dad_emailPessoa varchar(100),"
			  			 + "dad_telefonePessoa varchar(20));";

			 comandos[8] = "CREATE TABLE contas ("
					 	 + "cod_idConta INT AUTO_INCREMENT PRIMARY KEY,"
					 	 + "dad_loginConta varchar(50),"
					 	 + "dad_senhaConta varchar(50),"
					 	 + "dad_nvlAcesso INT DEFAULT '1',"
					 	 + "dad_salt VARCHAR(50));";

			 comandos[9] = "CREATE TABLE bairros ("
			  		 	 + "cod_idBairro INT AUTO_INCREMENT PRIMARY KEY ,"
			  		 	 + "dad_nomeBairro VARCHAR(100),"
			  		 	 + "cod_idCidade INT);";

			comandos[10] = "CREATE TABLE lourados ("
						 + "cod_idLourado INT AUTO_INCREMENT PRIMARY KEY ,"
						 + "dad_nomeLourado VARCHAR(100),"
						 + "cod_idBairro INT);";
			for (String comando : comandos) {
				pstmt.execute(comando);
			}
		} catch (SQLException e) {}
	 }

	 private void criarRelacionamento(Connection conexao){
		 String[] comandos = new String[13];
		 comandos[0] = "ALTER TABLE cidades ADD FOREIGN KEY (cod_idEstado) REFERENCES estados (cod_idEstado);";
		 comandos[1] = "ALTER TABLE `empresas` ADD CONSTRAINT `empresas_fk0` FOREIGN KEY (`cod_idConta`) REFERENCES `contas`(`cod_idConta`);";
		 comandos[2] = "ALTER TABLE `empresas` ADD CONSTRAINT `empresas_fk1` FOREIGN KEY (`cod_idLourado`) REFERENCES `lourados`(`cod_idLourado`);";
		 comandos[3] = "ALTER TABLE `funcionarios` ADD CONSTRAINT `funcionarios_fk0` FOREIGN KEY (`cod_idEmpresa`) REFERENCES `empresas`(`cod_idEmpresa`);";
		 comandos[4] = "ALTER TABLE `funcionarios` ADD CONSTRAINT `funcionarios_fk1` FOREIGN KEY (`cod_idPessoa`) REFERENCES `pessoas`(`cod_idPessoa`);";
		 comandos[5] = "ALTER TABLE `funcionarios` ADD CONSTRAINT `funcionarios_fk2` FOREIGN KEY (`cod_idConta`) REFERENCES `contas`(`cod_idConta`);";
		 comandos[6] = "ALTER TABLE `itens` ADD CONSTRAINT `itens_fk0` FOREIGN KEY (`cod_idFuncionario`) REFERENCES `funcionarios`(`cod_idFuncionario`);";
		 comandos[7] = "ALTER TABLE `itens` ADD CONSTRAINT `itens_fk1` FOREIGN KEY (`cod_idImagem`) REFERENCES `imagens`(`cod_idImagem`);";
		 comandos[8] = "ALTER TABLE `usuarios` ADD CONSTRAINT `usuarios_fk0` FOREIGN KEY (`cod_idConta`) REFERENCES `contas`(`cod_idConta`);";
		 comandos[9] = "ALTER TABLE `usuarios` ADD CONSTRAINT `usuarios_fk1` FOREIGN KEY (`cod_idLurado`) REFERENCES `lourados`(`cod_idLourado`);";
		 comandos[10] = "ALTER TABLE `usuarios` ADD CONSTRAINT `usuarios_fk2` FOREIGN KEY (`cod_idPessoa`) REFERENCES `pessoas`(`cod_idPessoa`);";
		 comandos[11] = "ALTER TABLE `bairros` ADD CONSTRAINT `bairros_fk0` FOREIGN KEY (`cod_idCidade`) REFERENCES `cidades`(`cod_idCidade`);";
		 comandos[12] = "ALTER TABLE `lourados` ADD CONSTRAINT `lourados_fk0` FOREIGN KEY (`cod_idBairro`) REFERENCES `bairros`(`cod_idBairro`);";
		 try{
			 Statement stmt = conexao.createStatement();
			 for (String comando : comandos) {
				stmt.execute(comando);
			 }
		 }catch (Exception e) {System.out.println("Falha ao criar rela��o no banco de dados:\n"+e.getMessage());}
	 }

	 private void inserirBaseLocais(Connection conexao){
		 String[] comandos = new String[2];
		 comandos[0] = "INSERT INTO estados VALUES\r\n" + 
					"(1, \"Acre\", \"AC\"),\r\n" + 
					"(2, \"Alagoas\", \"AL\"),\r\n" + 
					"(3, \"Amazonas\", \"AM\"),\r\n" + 
					"(4, \"Amap�\", \"AP\"),\r\n" + 
					"(5, \"Bahia\", \"BA\"),\r\n" + 
					"(6, \"Ceara\", \"CE\"),\r\n" + 
					"(7, \"Distrito Federal\", \"DF\"),\r\n" + 
					"(8, \"Espirito Santo\", \"ES\"),\r\n" + 
					"(9, \"Goi�s\", \"GO\"),\r\n" + 
					"(10, \"Maranh�o\", \"MA\"),\r\n" + 
					"(11, \"Minas Gerais\", \"MG\"),\r\n" + 
					"(12, \"Mato Grosso do Sul\", \"MS\"),\r\n" + 
					"(13, \"Mato Grosso\", \"MT\"),\r\n" + 
					"(14, \"Par�\", \"PA\"),\r\n" + 
					"(15, \"Para�ba\", \"PB\"),\r\n" + 
					"(16, \"Pernambuco\", \"PE\"),\r\n" + 
					"(17, \"Piau�\", \"PI\"),\r\n" + 
					"(18, \"Paran�\", \"PR\"),\r\n" + 
					"(19, \"Rio de Janeiro\", \"RJ\"),\r\n" + 
					"(20, \"Rio Grande do Norte\", \"RN\"),\r\n" + 
					"(21, \"Rond�nia\", \"RO\"),\r\n" + 
					"(22, \"Roraima\", \"RR\"),\r\n" + 
					"(23, \"Rio Grande do Sul\", \"RS\"),\r\n" + 
					"(24, \"Santa Catarina\", \"SC\"),\r\n" + 
					"(25, \"Sergipe\", \"SE\"),\r\n" + 
					"(26, \"S�o Paulo\", \"SP\"),\r\n" + 
					"(27, \"Tocantins\", \"TO\");";
		 
			comandos[1] = "INSERT INTO cidades VALUES\r\n" + 
					"(1, \"Abacate da Pedreira (Macap�)\", 4),\r\n" + 
					"(2, \"Abadia de Goi�s\", 9),\r\n" + 
					"(3, \"Abadia dos Dourados\", 11),\r\n" + 
					"(4, \"Abadia (Janda�ra)\", 5),\r\n" + 
					"(5, \"Abadi�nia\", 9),\r\n" + 
					"(6, \"Abaet�\", 11),\r\n" + 
					"(7, \"Abaet� dos Mendes (Rio Parana�ba)\", 11),\r\n" + 
					"(8, \"Abaetetuba\", 14),\r\n" + 
					"(9, \"Abaiara\", 6),\r\n" + 
					"(10, \"Aba�ba (Leopoldina)\", 11),\r\n" + 
					"(11, \"Aba�ra\", 5),\r\n" + 
					"(12, \"Abap� (Castro)\", 18),\r\n" + 
					"(13, \"Abar�\", 5),\r\n" + 
					"(14, \"Abarracamento (Rio das Flores)\", 19),\r\n" + 
					"(15, \"Abati�\", 18),\r\n" + 
					"(16, \"Abdon Batista\", 24),\r\n" + 
					"(17, \"Abelardo Luz\", 24),\r\n" + 
					"(18, \"Abel Figueiredo\", 14),\r\n" + 
					"(19, \"Ab�lio Martins (Ipu)\", 6),\r\n" + 
					"(20, \"Ab�bora (Juazeiro)\", 5),\r\n" + 
					"(21, \"Abrantes (Cama�ari)\", 5),\r\n" + 
					"(22, \"Abre Campo\", 11),\r\n" + 
					"(23, \"Abreu e Lima\", 16),\r\n" + 
					"(24, \"Abreul�ndia\", 27),\r\n" + 
					"(25, \"Abreus (Alto Rio Doce)\", 11),\r\n" + 
					"(26, \"Abun� (Porto Velho)\", 21),\r\n" + 
					"(27, \"Acaiaca\", 11),\r\n" + 
					"(28, \"A�ail�ndia\", 10),\r\n" + 
					"(29, \"Acajutiba\", 5),\r\n" + 
					"(30, \"Acampamento das Minas (Tel�maco Borba)\", 18),\r\n" + 
					"(31, \"Acar�\", 14),\r\n" + 
					"(32, \"Acarape\", 6),\r\n" + 
					"(33, \"Acara�\", 6),\r\n" + 
					"(34, \"Acari\", 20),\r\n" + 
					"(35, \"Acau�\", 17),\r\n" + 
					"(36, \"Acegu�\", 23),\r\n" + 
					"(37, \"Acioli (Jo�o Neiva)\", 8),\r\n" + 
					"(38, \"Acopiara\", 6),\r\n" + 
					"(39, \"Acorizal\", 13),\r\n" + 
					"(40, \"Acrel�ndia\", 1),\r\n" + 
					"(41, \"Acre�na\", 9),\r\n" + 
					"(42, \"A�u\", 20),\r\n" + 
					"(43, \"A�ucena\", 11),\r\n" + 
					"(44, \"A�u da Torre (Mata de S�o Jo�o)\", 5),\r\n" + 
					"(45, \"A�ude dos Pinheiros (Ibicuitinga)\", 6),\r\n" + 
					"(46, \"A�udina (Santa Maria da Vit�ria)\", 5),\r\n" + 
					"(47, \"A�udinho dos Costas (Momba�a)\", 6),\r\n" + 
					"(48, \"A�ungui (Rio Branco do Sul)\", 18),\r\n" + 
					"(49, \"Acupe (Santo Amaro)\", 5),\r\n" + 
					"(50, \"Acuru� (Itabirito)\", 11),\r\n" + 
					"(51, \"Adamantina\", 26),\r\n" + 
					"(52, \"Ad�o Colares (Botumirim)\", 11),\r\n" + 
					"(53, \"Adel�ndia\", 9),\r\n" + 
					"(54, \"Adhemar de Barros (Terra Rica)\", 18),\r\n" + 
					"(55, \"Adolfo\", 26),\r\n" + 
					"(56, \"Adrian�polis\", 18),\r\n" + 
					"(57, \"Adrian�polis (Granja)\", 6),\r\n" + 
					"(58, \"Adro de S�o Gon�alo (Iti�ba)\", 5),\r\n" + 
					"(59, \"Adustina\", 5),\r\n" + 
					"(60, \"Afligidos (S�o Gon�alo dos Campos)\", 5),\r\n" + 
					"(61, \"Afogados da Ingazeira\", 16),\r\n" + 
					"(62, \"Afonso Arinos (Comendador Levy Gasparian)\", 19),\r\n" + 
					"(63, \"Afonso Bezerra\", 20),\r\n" + 
					"(64, \"Afonso Cl�udio\", 8),\r\n" + 
					"(65, \"Afonso Cunha\", 10),\r\n" + 
					"(66, \"Afonso Rodrigues (S�o Luiz Gonzaga)\", 23),\r\n" + 
					"(67, \"Afr�nio\", 16),\r\n" + 
					"(68, \"Afr�nio Peixoto (Len��is)\", 5),\r\n" + 
					"(69, \"Afu�\", 14),\r\n" + 
					"(70, \"Agh� (Pi�ma)\", 8),\r\n" + 
					"(71, \"Agisse (Rancharia)\", 26),\r\n" + 
					"(72, \"Agostinho (Castro)\", 18),\r\n" + 
					"(73, \"Agrestina\", 16),\r\n" + 
					"(74, \"Agricol�ndia\", 17),\r\n" + 
					"(75, \"Agrol�ndia\", 24),\r\n" + 
					"(76, \"Agron�mica\", 24),\r\n" + 
					"(77, \"Agr�polis Bela Vista (Santar�m)\", 14),\r\n" + 
					"(78, \"Agrovila S�o Sebasti�o do Caburi (Parintins)\", 3),\r\n" + 
					"(79, \"�gua Azul do Norte\", 14),\r\n" + 
					"(80, \"�gua Azul (Lapa)\", 18),\r\n" + 
					"(81, \"�gua Boa\", 11),\r\n" + 
					"(82, \"�gua Boa\", 13),\r\n" + 
					"(83, \"�gua Boa (Pai�andu)\", 18),\r\n" + 
					"(84, \"�gua Boa (Rochedo)\", 12),\r\n" + 
					"(85, \"�gua Branca\", 15),\r\n" + 
					"(86, \"�gua Branca\", 2),\r\n" + 
					"(87, \"�gua Branca\", 17),\r\n" + 
					"(88, \"�gua Branca de Minas (Comercinho)\", 11),\r\n" + 
					"(89, \"�gua Branca (Guarapuava)\", 18),\r\n" + 
					"(90, \"�gua Clara\", 12),\r\n" + 
					"(91, \"�gua Comprida\", 11),\r\n" + 
					"(92, \"Agua�u (Cuiab�)\", 13),\r\n" + 
					"(93, \"�gua da Prata (Brasnorte)\", 13),\r\n" + 
					"(94, \"�gua Doce\", 24),\r\n" + 
					"(95, \"�gua Doce do Maranh�o\", 10),\r\n" + 
					"(96, \"�gua Doce do Norte\", 8),\r\n" + 
					"(97, \"�gua Doce (Ibicu�)\", 5),\r\n" + 
					"(98, \"�gua Fria\", 5),\r\n" + 
					"(99, \"�gua Fria (Belo Jardim)\", 16),\r\n" + 
					"(100, \"�gua Fria (Chapada dos Guimar�es)\", 13),\r\n" + 
					"(101, \"�gua Fria de Goi�s\", 9),\r\n" + 
					"(102, \"�gua Fria (Xinguara)\", 14),\r\n" + 
					"(103, \"Agua�\", 26),\r\n" + 
					"(104, \"Agua� (Itapaj�)\", 6),\r\n" + 
					"(105, \"�gua Limpa\", 9),\r\n" + 
					"(106, \"�gua Mineral (Guarapuava)\", 18),\r\n" + 
					"(107, \"Aguanil\", 11),\r\n" + 
					"(108, \"�gua Nova\", 20),\r\n" + 
					"(109, \"Aguape� (Vila Bela da Sant�ssima Trindade)\", 13),\r\n" + 
					"(110, \"Aguap�s (Os�rio)\", 23),\r\n" + 
					"(111, \"�gua Preta\", 16),\r\n" + 
					"(112, \"�gua Santa\", 23),\r\n" + 
					"(113, \"�gua Santa de Minas (Tombos)\", 11),\r\n" + 
					"(114, \"�guas Belas\", 16),\r\n" + 
					"(115, \"�guas Belas (Boa Viagem)\", 6),\r\n" + 
					"(116, \"�guas Brancas (Urubici)\", 24),\r\n" + 
					"(117, \"�guas Claras (Mafra)\", 24),\r\n" + 
					"(118, \"�guas Claras (Novo Horizonte do Norte)\", 13),\r\n" + 
					"(119, \"�guas Claras (Viam�o)\", 23),\r\n" + 
					"(120, \"�guas da Prata\", 26),\r\n" + 
					"(121, \"�guas de Chapec�\", 24),\r\n" + 
					"(122, \"�guas de Contendas (Concei��o do Rio Verde)\", 11),\r\n" + 
					"(123, \"�guas de Lind�ia\", 26),\r\n" + 
					"(124, \"�guas de Santa B�rbara\", 26),\r\n" + 
					"(125, \"�guas de S�o Pedro\", 26),\r\n" + 
					"(126, \"�guas do Paulista (Paratinga)\", 5),\r\n" + 
					"(127, \"�guas F�rreas (S�o Pedro dos Ferros)\", 11),\r\n" + 
					"(128, \"�guas Formosas\", 11),\r\n" + 
					"(129, \"�guas Frias\", 24),\r\n" + 
					"(130, \"�guas Lindas de Goi�s\", 9),\r\n" + 
					"(131, \"�guas Mornas\", 24),\r\n" + 
					"(132, \"�guas Vermelhas\", 11),\r\n" + 
					"(133, \"�gua Verde (Guai�ba)\", 6),\r\n" + 
					"(134, \"�gua Vermelha (Castro)\", 18),\r\n" + 
					"(135, \"�gua Vermelha (S�o Carlos)\", 26),\r\n" + 
					"(136, \"�gua Viva (Estrela Dalva)\", 11),\r\n" + 
					"(137, \"Agudo\", 23),\r\n" + 
					"(138, \"Agudos\", 26),\r\n" + 
					"(139, \"Agudos do Sul\", 18),\r\n" + 
					"(140, \"�guia Branca\", 8),\r\n" + 
					"(141, \"Aguiar\", 15),\r\n" + 
					"(142, \"Aguiarn�polis\", 27),\r\n" + 
					"(143, \"Agulha (Fernando Prestes)\", 26),\r\n" + 
					"(144, \"Aguti (Nova Trento)\", 24),\r\n" + 
					"(145, \"Aimor�s\", 11),\r\n" + 
					"(146, \"Ainhumas (Rondon�polis)\", 13),\r\n" + 
					"(147, \"Aiquara\", 5),\r\n" + 
					"(148, \"Airi (Floresta)\", 16),\r\n" + 
					"(149, \"Airituba (S�o Jos� do Cal�ado)\", 8),\r\n" + 
					"(150, \"Aiuaba\", 6),\r\n" + 
					"(151, \"Aiu� (Massap�)\", 6),\r\n" + 
					"(152, \"Aiur� (Gr�o Par�)\", 24),\r\n" + 
					"(153, \"Aiuruoca\", 11),\r\n" + 
					"(154, \"Ajapi (Rio Claro)\", 26),\r\n" + 
					"(155, \"Ajuricaba\", 23),\r\n" + 
					"(156, \"Alagoa\", 11),\r\n" + 
					"(157, \"Alagoa Grande\", 15),\r\n" + 
					"(158, \"Alagoa Nova\", 15),\r\n" + 
					"(159, \"Alagoinha\", 15),\r\n" + 
					"(160, \"Alagoinha\", 16),\r\n" + 
					"(161, \"Alagoinha (Araripe)\", 6),\r\n" + 
					"(162, \"Alagoinha do Piau�\", 17),\r\n" + 
					"(163, \"Alagoinha (Paraipaba)\", 6),\r\n" + 
					"(164, \"Alagoinhas\", 5),\r\n" + 
					"(165, \"Alambari\", 26),\r\n" + 
					"(166, \"Alaz�o (Arapiraca)\", 2),\r\n" + 
					"(167, \"Albard�o (Rio Pardo)\", 23),\r\n" + 
					"(168, \"Albertina\", 11),\r\n" + 
					"(169, \"Alberto Isaacson (Martinho Campos)\", 11),\r\n" + 
					"(170, \"Alberto Moreira (Barretos)\", 26),\r\n" + 
					"(171, \"Albertos (Formiga)\", 11),\r\n" + 
					"(172, \"Albuquerque (Corumb�)\", 12),\r\n" + 
					"(173, \"Albuquerque N� (Sert�nia)\", 16),\r\n" + 
					"(174, \"Alc�ntara\", 10),\r\n" + 
					"(175, \"Alc�ntaras\", 6),\r\n" + 
					"(176, \"Alcantil\", 15),\r\n" + 
					"(177, \"Alcantilado (Guiratinga)\", 13),\r\n" + 
					"(178, \"Alcin�polis\", 12),\r\n" + 
					"(179, \"Alcoba�a\", 5),\r\n" + 
					"(180, \"Aldeia (Conselheiro Pena)\", 11),\r\n" + 
					"(181, \"Aldeias Altas\", 10),\r\n" + 
					"(182, \"Alecrim\", 23),\r\n" + 
					"(183, \"Alecrim (P�o de A��car)\", 2),\r\n" + 
					"(184, \"Alegre\", 8),\r\n" + 
					"(185, \"Alegre (Conde�ba)\", 5),\r\n" + 
					"(186, \"Alegre (Coromandel)\", 11),\r\n" + 
					"(187, \"Alegrete\", 23),\r\n" + 
					"(188, \"Alegrete do Piau�\", 17),\r\n" + 
					"(189, \"Alegria\", 23),\r\n" + 
					"(190, \"Alegria (Simon�sia)\", 11),\r\n" + 
					"(191, \"Al�m Para�ba\", 11),\r\n" + 
					"(192, \"Alenquer\", 14),\r\n" + 
					"(193, \"Alexandra (Paranagu�)\", 18),\r\n" + 
					"(194, \"Alexandria\", 20),\r\n" + 
					"(195, \"Alexandrita (Iturama)\", 11),\r\n" + 
					"(196, \"Alex�nia\", 9),\r\n" + 
					"(197, \"Alfenas\", 11),\r\n" + 
					"(198, \"Alfredo Brenner (Ibirub�)\", 23),\r\n" + 
					"(199, \"Alfredo Chaves\", 8),\r\n" + 
					"(200, \"Alfredo Guedes (Len��is Paulista)\", 26),\r\n" + 
					"(201, \"Alfredo Marcondes\", 26),\r\n" + 
					"(202, \"Alfredo Vasconcelos\", 11),\r\n" + 
					"(203, \"Alfredo Wagner\", 24),\r\n" + 
					"(204, \"Algod�o de Janda�ra\", 15),\r\n" + 
					"(205, \"Algod�o (Ibirataia)\", 5),\r\n" + 
					"(206, \"Algodoal (Maracan�)\", 14),\r\n" + 
					"(207, \"Algod�es (Quijingue)\", 5),\r\n" + 
					"(208, \"Algod�es (Quiterian�polis)\", 6),\r\n" + 
					"(209, \"Algod�es (Sert�nia)\", 16),\r\n" + 
					"(210, \"Alhandra\", 15),\r\n" + 
					"(211, \"Alian�a\", 16),\r\n" + 
					"(212, \"Alian�a do Tocantins\", 27),\r\n" + 
					"(213, \"Almadina\", 5),\r\n" + 
					"(214, \"Almas\", 27),\r\n" + 
					"(215, \"Almeida (Jaboticatubas)\", 11),\r\n" + 
					"(216, \"Almeirim\", 14),\r\n" + 
					"(217, \"Almenara\", 11),\r\n" + 
					"(218, \"Almino Afonso\", 20),\r\n" + 
					"(219, \"Almirante Tamandar�\", 18),\r\n" + 
					"(220, \"Almirante Tamandar� do Sul\", 23),\r\n" + 
					"(221, \"Almo�o (Bragan�a)\", 14),\r\n" + 
					"(222, \"Almofala (Itarema)\", 6),\r\n" + 
					"(223, \"Alo�ndia\", 9),\r\n" + 
					"(224, \"Alpercata\", 11),\r\n" + 
					"(225, \"Alpestre\", 23),\r\n" + 
					"(226, \"Alpin�polis\", 11),\r\n" + 
					"(227, \"Alta Floresta\", 13),\r\n" + 
					"(228, \"Alta Floresta D'Oeste\", 21),\r\n" + 
					"(229, \"Altair\", 26),\r\n" + 
					"(230, \"Altamira\", 14),\r\n" + 
					"(231, \"Altamira do Maranh�o\", 10),\r\n" + 
					"(232, \"Altamira do Paran�\", 18),\r\n" + 
					"(233, \"Altaneira\", 6),\r\n" + 
					"(234, \"Altaneira (Maring�)\", 18),\r\n" + 
					"(235, \"Alta Par� (Santar�m)\", 14),\r\n" + 
					"(236, \"Alter do Ch�o (Santar�m)\", 14),\r\n" + 
					"(237, \"Alterosa\", 11),\r\n" + 
					"(238, \"Altinho\", 16),\r\n" + 
					"(239, \"Altin�polis\", 26),\r\n" + 
					"(240, \"Alto Alegre\", 22),\r\n" + 
					"(241, \"Alto Alegre\", 23),\r\n" + 
					"(242, \"Alto Alegre\", 26),\r\n" + 
					"(243, \"Alto Alegre (Capinzal)\", 24),\r\n" + 
					"(244, \"Alto Alegre (Cerrito)\", 23),\r\n" + 
					"(245, \"Alto Alegre (Colorado)\", 18),\r\n" + 
					"(246, \"Alto Alegre do Igua�u (Capit�o Le�nidas Marques)\", 18),\r\n" + 
					"(247, \"Alto Alegre do Maranh�o\", 10),\r\n" + 
					"(248, \"Alto Alegre do Pindar�\", 10),\r\n" + 
					"(249, \"Alto Alegre dos Parecis\", 21),\r\n" + 
					"(250, \"Alto Alegre (Pedro Os�rio)\", 23),\r\n" + 
					"(251, \"Alto Alegre (Umuarama)\", 18),\r\n" + 
					"(252, \"Alto Alvorada (Orizona)\", 9),\r\n" + 
					"(253, \"Alto Amparo (Tel�maco Borba)\", 18),\r\n" + 
					"(254, \"Alto Araguaia\", 13),\r\n" + 
					"(255, \"Alto Bela Vista\", 24),\r\n" + 
					"(256, \"Alto Belo (Bocai�va)\", 11),\r\n" + 
					"(257, \"Alto Boa Vista\", 13),\r\n" + 
					"(258, \"Alto Bonito (Mundo Novo)\", 5),\r\n" + 
					"(259, \"Alto Cal�ado (S�o Jos� do Cal�ado)\", 8),\r\n" + 
					"(260, \"Alto Caldeir�o (Santa Teresa)\", 8),\r\n" + 
					"(261, \"Alto Capara�\", 11),\r\n" + 
					"(262, \"Alto Capim (Aimor�s)\", 11),\r\n" + 
					"(263, \"Alto Castelinho (Vargem Alta)\", 8),\r\n" + 
					"(264, \"Alto Coit� (Poxor�u)\", 13),\r\n" + 
					"(265, \"Alto da Serra (Chapec�)\", 24),\r\n" + 
					"(266, \"Alto da Uni�o (Iju�)\", 23),\r\n" + 
					"(267, \"Alto de Santa Helena (Governador Valadares)\", 11),\r\n" + 
					"(268, \"Alto do Amparo (Tibagi)\", 18),\r\n" + 
					"(269, \"Alto do Rodrigues\", 20),\r\n" + 
					"(270, \"Alto Erval Novo (Tr�s Passos)\", 23),\r\n" + 
					"(271, \"Alto Feliz\", 23),\r\n" + 
					"(272, \"Alto Gar�as\", 13),\r\n" + 
					"(273, \"Alto Horizonte\", 9),\r\n" + 
					"(274, \"Alto Jequitib�\", 11),\r\n" + 
					"(275, \"Alto Juruena (Comodoro)\", 13),\r\n" + 
					"(276, \"Altol�ndia (Tapira�)\", 11),\r\n" + 
					"(277, \"Alto Lindo (Ibiapina)\", 6),\r\n" + 
					"(278, \"Alto Long�\", 17),\r\n" + 
					"(279, \"Alto Maranh�o (Congonhas)\", 11),\r\n" + 
					"(280, \"Alto Mutum Preto (Baixo Guandu)\", 8),\r\n" + 
					"(281, \"Alt�nia\", 18),\r\n" + 
					"(282, \"Alto Par� (Cascavel)\", 18),\r\n" + 
					"(283, \"Alto Paraguai\", 13),\r\n" + 
					"(284, \"Alto Para�so\", 18),\r\n" + 
					"(285, \"Alto Para�so\", 21),\r\n" + 
					"(286, \"Alto Para�so (Alta Floresta)\", 13),\r\n" + 
					"(287, \"Alto Para�so de Goi�s\", 9),\r\n" + 
					"(288, \"Alto Paran�\", 18),\r\n" + 
					"(289, \"Alto Pared�o (Santa Cruz do Sul)\", 23),\r\n" + 
					"(290, \"Alto Parna�ba\", 10),\r\n" + 
					"(291, \"Alto Piquiri\", 18),\r\n" + 
					"(292, \"Alto Por� (Ivaipor�)\", 18),\r\n" + 
					"(293, \"Alto Por� (Pedregulho)\", 26),\r\n" + 
					"(294, \"Alto Recreio (Ronda Alta)\", 23),\r\n" + 
					"(295, \"Alto Rio Doce\", 11),\r\n" + 
					"(296, \"Alto Rio Novo\", 8),\r\n" + 
					"(297, \"Altos\", 17),\r\n" + 
					"(298, \"Alto Sabi� (Guarapuava)\", 18),\r\n" + 
					"(299, \"Alto Santa F� (Nova Santa Rosa)\", 18),\r\n" + 
					"(300, \"Alto Santa Maria (Santa Teresa)\", 8),\r\n" + 
					"(301, \"Alto Santo\", 6),\r\n" + 
					"(302, \"Alto S�o Jo�o (Roncador)\", 18),\r\n" + 
					"(303, \"Alto Sucuri� (Para�so das �guas)\", 12),\r\n" + 
					"(304, \"Altos Verdes (Carira)\", 25),\r\n" + 
					"(305, \"Alto Taquari\", 13),\r\n" + 
					"(306, \"Alto Uruguai (Tiradentes do Sul)\", 23),\r\n" + 
					"(307, \"Alto Uruguai (Tr�s Passos)\", 23),\r\n" + 
					"(308, \"Alum�nio\", 26),\r\n" + 
					"(309, \"Alva��o (Cora��o de Jesus)\", 11),\r\n" + 
					"(310, \"Alvar�es\", 3),\r\n" + 
					"(311, \"Alvarenga\", 11),\r\n" + 
					"(312, \"�lvares Florence\", 26),\r\n" + 
					"(313, \"�lvares Machado\", 26),\r\n" + 
					"(314, \"�lvaro de Carvalho\", 26),\r\n" + 
					"(315, \"Alverne (Alagoinha)\", 16),\r\n" + 
					"(316, \"Alvinl�ndia\", 26),\r\n" + 
					"(317, \"Alvin�polis\", 11),\r\n" + 
					"(318, \"Alvorada\", 27),\r\n" + 
					"(319, \"Alvorada\", 23),\r\n" + 
					"(320, \"Alvorada (Carangola)\", 11),\r\n" + 
					"(321, \"Alvorada de Minas\", 11),\r\n" + 
					"(322, \"Alvorada D'Oeste\", 21),\r\n" + 
					"(323, \"Alvorada do Gurgu�ia\", 17),\r\n" + 
					"(324, \"Alvorada do Igua�u (Foz do Igua�u)\", 18),\r\n" + 
					"(325, \"Alvorada do Norte\", 9),\r\n" + 
					"(326, \"Alvorada do Sul\", 18),\r\n" + 
					"(327, \"Alvorada (Itaituba)\", 14),\r\n" + 
					"(328, \"Alvorada (Uruar�)\", 14),\r\n" + 
					"(329, \"Amado Bahia (Mata de S�o Jo�o)\", 5),\r\n" + 
					"(330, \"Amajari\", 22),\r\n" + 
					"(331, \"Amamba�\", 12),\r\n" + 
					"(332, \"Amanaiara (Reriutaba)\", 6),\r\n" + 
					"(333, \"Amanari (Maranguape)\", 6),\r\n" + 
					"(334, \"Amanda (Baldim)\", 11),\r\n" + 
					"(335, \"Amandaba (Mirand�polis)\", 26),\r\n" + 
					"(336, \"Amandina (Ivinhema)\", 12),\r\n" + 
					"(337, \"Amanhece (Araguari)\", 11),\r\n" + 
					"(338, \"Amani� (Sento S�)\", 5),\r\n" + 
					"(339, \"Amaniutuba (Lavras da Mangabeira)\", 6),\r\n" + 
					"(340, \"Amap�\", 4),\r\n" + 
					"(341, \"Amap� do Maranh�o\", 10),\r\n" + 
					"(342, \"Amapor�\", 18),\r\n" + 
					"(343, \"Amaraji\", 16),\r\n" + 
					"(344, \"Amaral Ferrador\", 23),\r\n" + 
					"(345, \"Amaralina\", 9),\r\n" + 
					"(346, \"Amarante\", 17),\r\n" + 
					"(347, \"Amarante do Maranh�o\", 10),\r\n" + 
					"(348, \"Amarantina (Ouro Preto)\", 11),\r\n" + 
					"(349, \"Amarelas (Camocim)\", 6),\r\n" + 
					"(350, \"Amargosa\", 5),\r\n" + 
					"(351, \"Amaro (Assar�)\", 6),\r\n" + 
					"(352, \"Amatari (Itacoatiara)\", 3),\r\n" + 
					"(353, \"Amatur�\", 3),\r\n" + 
					"(354, \"Amb� (Macap�)\", 4),\r\n" + 
					"(355, \"Ameixas (Cumaru)\", 16),\r\n" + 
					"(356, \"Am�lia Rodrigues\", 5),\r\n" + 
					"(357, \"Am�rica Dourada\", 5),\r\n" + 
					"(358, \"Am�rica (Ipueiras)\", 6),\r\n" + 
					"(359, \"Americana\", 26),\r\n" + 
					"(360, \"Americano do Brasil\", 9),\r\n" + 
					"(361, \"Americano (Santa Isabel do Par�)\", 14),\r\n" + 
					"(362, \"Am�rico Alves (Sento S�)\", 5),\r\n" + 
					"(363, \"Am�rico Brasiliense\", 26),\r\n" + 
					"(364, \"Am�rico de Campos\", 26),\r\n" + 
					"(365, \"Ametista do Sul\", 23),\r\n" + 
					"(366, \"Amolar (Corumb�)\", 12),\r\n" + 
					"(367, \"Amontada\", 6),\r\n" + 
					"(368, \"Amoras (Taquari)\", 23),\r\n" + 
					"(369, \"Amorinha (Ibaiti)\", 18),\r\n" + 
					"(370, \"Amorin�polis\", 9),\r\n" + 
					"(371, \"Amparo\", 15),\r\n" + 
					"(372, \"Amparo\", 26),\r\n" + 
					"(373, \"Amparo da Serra\", 11),\r\n" + 
					"(374, \"Amparo de S�o Francisco\", 25),\r\n" + 
					"(375, \"Amp�re\", 18),\r\n" + 
					"(376, \"Anadia\", 2),\r\n" + 
					"(377, \"Ana Dias (Itariri)\", 26),\r\n" + 
					"(378, \"Anag�\", 5),\r\n" + 
					"(379, \"Anahy\", 18),\r\n" + 
					"(380, \"Anajan�polis (Pedro Afonso)\", 27),\r\n" + 
					"(381, \"Anaj�s\", 14),\r\n" + 
					"(382, \"Anajatuba\", 10),\r\n" + 
					"(383, \"Anal�ndia\", 26),\r\n" + 
					"(384, \"Anal�ndia do Norte (Peixoto de Azevedo)\", 13),\r\n" + 
					"(385, \"Anam�\", 3),\r\n" + 
					"(386, \"Anan�s\", 27),\r\n" + 
					"(387, \"Ananindeua\", 14),\r\n" + 
					"(388, \"An�polis\", 9),\r\n" + 
					"(389, \"An�polis (Iacri)\", 26),\r\n" + 
					"(390, \"Anapu\", 14),\r\n" + 
					"(391, \"Anapurus\", 10),\r\n" + 
					"(392, \"Anast�cio\", 12),\r\n" + 
					"(393, \"Anau� (Mauriti)\", 6),\r\n" + 
					"(394, \"Anauril�ndia\", 12),\r\n" + 
					"(395, \"Anchieta\", 8),\r\n" + 
					"(396, \"Anchieta\", 24),\r\n" + 
					"(397, \"Andara�\", 5),\r\n" + 
					"(398, \"Andir�\", 18),\r\n" + 
					"(399, \"Andiroba (Esmeraldas)\", 11),\r\n" + 
					"(400, \"Andorinha\", 5),\r\n" + 
					"(401, \"Andorinhas (Castro)\", 18),\r\n" + 
					"(402, \"Andradas\", 11),\r\n" + 
					"(403, \"Andrade Pinto (Vassouras)\", 19),\r\n" + 
					"(404, \"Andradina\", 26),\r\n" + 
					"(405, \"Andr� da Rocha\", 23),\r\n" + 
					"(406, \"Andrel�ndia\", 11),\r\n" + 
					"(407, \"Andrequic� (Tr�s Marias)\", 11),\r\n" + 
					"(408, \"Anel (Vi�osa)\", 2),\r\n" + 
					"(409, \"Angai (Fernandes Pinheiro)\", 18),\r\n" + 
					"(410, \"Angatuba\", 26),\r\n" + 
					"(411, \"Angaturama (Recreio)\", 11),\r\n" + 
					"(412, \"Angel�ndia\", 11),\r\n" + 
					"(413, \"Ang�lica\", 12),\r\n" + 
					"(414, \"Angelim\", 16),\r\n" + 
					"(415, \"Angelina\", 24),\r\n" + 
					"(416, \"Angical\", 5),\r\n" + 
					"(417, \"Angical do Piau�\", 17),\r\n" + 
					"(418, \"Angico\", 27),\r\n" + 
					"(419, \"Angico (Mairi)\", 5),\r\n" + 
					"(420, \"Angicos\", 20),\r\n" + 
					"(421, \"Angicos de Minas (Bras�lia de Minas)\", 11),\r\n" + 
					"(422, \"Angra dos Reis\", 19),\r\n" + 
					"(423, \"Anguera\", 5),\r\n" + 
					"(424, \"Angueret� (Curvelo)\", 11),\r\n" + 
					"(425, \"�ngulo\", 18),\r\n" + 
					"(426, \"Angustura (Al�m Para�ba)\", 11),\r\n" + 
					"(427, \"Anhandui (Campo Grande)\", 12),\r\n" + 
					"(428, \"Anhang�era\", 9),\r\n" + 
					"(429, \"Anhembi\", 26),\r\n" + 
					"(430, \"Anhumas\", 26),\r\n" + 
					"(431, \"Anicuns\", 9),\r\n" + 
					"(432, \"Anil (Meruoca)\", 6),\r\n" + 
					"(433, \"Aning�s (Horizonte)\", 6),\r\n" + 
					"(434, \"An�sio de Abreu\", 17),\r\n" + 
					"(435, \"Anita Garibaldi\", 24),\r\n" + 
					"(436, \"Anit�polis\", 24),\r\n" + 
					"(437, \"Anjinhos (Santana do Cariri)\", 6),\r\n" + 
					"(438, \"Anori\", 3),\r\n" + 
					"(439, \"Anta Gorda\", 23),\r\n" + 
					"(440, \"Anta Gorda (Videira)\", 24),\r\n" + 
					"(441, \"Antar� (Barra do Mendes)\", 5),\r\n" + 
					"(442, \"Antas\", 5),\r\n" + 
					"(443, \"Anta (Sapucaia)\", 19),\r\n" + 
					"(444, \"Antas (Tel�maco Borba)\", 18),\r\n" + 
					"(445, \"Antonina\", 18),\r\n" + 
					"(446, \"Antonina do Norte\", 6),\r\n" + 
					"(447, \"Ant�nio Almeida\", 17),\r\n" + 
					"(448, \"Ant�nio Brand�o de Oliveira (Jataizinho)\", 18),\r\n" + 
					"(449, \"Ant�nio Cardoso\", 5),\r\n" + 
					"(450, \"Ant�nio Carlos\", 11),\r\n" + 
					"(451, \"Ant�nio Carlos\", 24),\r\n" + 
					"(452, \"Ant�nio Dias\", 11),\r\n" + 
					"(453, \"Ant�nio Diogo (Reden��o)\", 6),\r\n" + 
					"(454, \"Ant�nio dos Santos (Caet�)\", 11),\r\n" + 
					"(455, \"Ant�nio Ferreira (Francisc�polis)\", 11),\r\n" + 
					"(456, \"Ant�nio Gon�alves\", 5),\r\n" + 
					"(457, \"Ant�nio Jo�o\", 12),\r\n" + 
					"(458, \"Ant�nio Kerpel (Coronel Bicaco)\", 23),\r\n" + 
					"(459, \"Ant�nio Lemos (Breves)\", 14),\r\n" + 
					"(460, \"Ant�nio Marques (Maranguape)\", 6),\r\n" + 
					"(461, \"Ant�nio Martins\", 20),\r\n" + 
					"(462, \"Ant�nio Olinto\", 18),\r\n" + 
					"(463, \"Ant�nio Pereira (Ouro Preto)\", 11),\r\n" + 
					"(464, \"Ant�nio Prado\", 23),\r\n" + 
					"(465, \"Ant�nio Prado de Minas\", 11),\r\n" + 
					"(466, \"Antunes (Igaratinga)\", 11),\r\n" + 
					"(467, \"Anum Novo (Palmeira dos �ndios)\", 2),\r\n" + 
					"(468, \"Anum Velho (Palmeira dos �ndios)\", 2),\r\n" + 
					"(469, \"Anuncia��o (Santa Izabel do Oeste)\", 18),\r\n" + 
					"(470, \"Anutiba (Alegre)\", 8),\r\n" + 
					"(471, \"Aparecida\", 26),\r\n" + 
					"(472, \"Aparecida\", 15),\r\n" + 
					"(473, \"Aparecida de Goi�nia\", 9),\r\n" + 
					"(474, \"Aparecida de Goi�s (Itapaci)\", 9),\r\n" + 
					"(475, \"Aparecida de Minas (Frutal)\", 11),\r\n" + 
					"(476, \"Aparecida de Monte Alto (Monte Alto)\", 26),\r\n" + 
					"(477, \"Aparecida de S�o Manuel (S�o Manuel)\", 26),\r\n" + 
					"(478, \"Aparecida do Bonito (Santa Rita D'Oeste)\", 26),\r\n" + 
					"(479, \"Aparecida D'Oeste\", 26),\r\n" + 
					"(480, \"Aparecida do Iva� (Santa M�nica)\", 18),\r\n" + 
					"(481, \"Aparecida do Oeste (Tuneiras do Oeste)\", 18),\r\n" + 
					"(482, \"Aparecida do Rio Claro (Montes Claros de Goi�s)\", 9),\r\n" + 
					"(483, \"Aparecida do Rio Doce\", 9),\r\n" + 
					"(484, \"Aparecida do Rio Negro\", 27),\r\n" + 
					"(485, \"Aparecida do Taboado\", 12),\r\n" + 
					"(486, \"Aparecida (Sanclerl�ndia)\", 9),\r\n" + 
					"(487, \"Aperib�\", 19),\r\n" + 
					"(488, \"Ape� (Castanhal)\", 14),\r\n" + 
					"(489, \"Apiaba (Imbituva)\", 18),\r\n" + 
					"(490, \"Apiac�\", 8),\r\n" + 
					"(491, \"Apiac�s\", 13),\r\n" + 
					"(492, \"Apia�\", 26),\r\n" + 
					"(493, \"Apia�-Mirim (Cap�o Bonito)\", 26),\r\n" + 
					"(494, \"Apicum-A�u\", 10),\r\n" + 
					"(495, \"Apinag�s (S�o Jo�o do Araguaia)\", 14),\r\n" + 
					"(496, \"Apinaj� (S�o Val�rio da Natividade)\", 27),\r\n" + 
					"(497, \"Api�na\", 24),\r\n" + 
					"(498, \"Apodi\", 20),\r\n" + 
					"(499, \"Apor�\", 5),\r\n" + 
					"(500, \"Apor�\", 9),\r\n" + 
					"(501, \"Aporema (Tartarugalzinho)\", 4),\r\n" + 
					"(502, \"Apoti (Gl�ria do Goit�)\", 16),\r\n" + 
					"(503, \"Apraz�vel (Sobral)\", 6),\r\n" + 
					"(504, \"Apuarema\", 5),\r\n" + 
					"(505, \"Apucarana\", 18),\r\n" + 
					"(506, \"Apu�\", 3),\r\n" + 
					"(507, \"Apuiar�s\", 6),\r\n" + 
					"(508, \"Aquidab�\", 25),\r\n" + 
					"(509, \"Aquidaban (Marialva)\", 18),\r\n" + 
					"(510, \"Aquidauana\", 12),\r\n" + 
					"(511, \"Aquin�polis (Jaguaribe)\", 6),\r\n" + 
					"(512, \"Aquiraz\", 6),\r\n" + 
					"(513, \"Arab� (Ouroeste)\", 26),\r\n" + 
					"(514, \"Arabela (Ouro Verde)\", 26),\r\n" + 
					"(515, \"Arabut�\", 24),\r\n" + 
					"(516, \"Araca�u (Buri)\", 26),\r\n" + 
					"(517, \"Ara�agi\", 15),\r\n" + 
					"(518, \"Ara�a�\", 11),\r\n" + 
					"(519, \"Ara�a�ba (Apia�)\", 26),\r\n" + 
					"(520, \"Ara�aji de Minas (Pedra Azul)\", 11),\r\n" + 
					"(521, \"Aracaju\", 25),\r\n" + 
					"(522, \"Ara�ariguama\", 26),\r\n" + 
					"(523, \"Ara��s\", 5),\r\n" + 
					"(524, \"Ara��s (Paracuru)\", 6),\r\n" + 
					"(525, \"Aracati\", 6),\r\n" + 
					"(526, \"Aracatia�u (Sobral)\", 6),\r\n" + 
					"(527, \"Aracatiara (Amontada)\", 6),\r\n" + 
					"(528, \"Ara�atiba (Viana)\", 8),\r\n" + 
					"(529, \"Aracati de Minas (Cataguases)\", 11),\r\n" + 
					"(530, \"Aracatu\", 5),\r\n" + 
					"(531, \"Ara�atuba\", 26),\r\n" + 
					"(532, \"Arac� (Domingos Martins)\", 8),\r\n" + 
					"(533, \"Araci\", 5),\r\n" + 
					"(534, \"Aracitaba\", 11),\r\n" + 
					"(535, \"Aracoiaba\", 6),\r\n" + 
					"(536, \"Ara�oiaba\", 16),\r\n" + 
					"(537, \"Ara�oiaba da Serra\", 26),\r\n" + 
					"(538, \"Aracruz\", 8),\r\n" + 
					"(539, \"Ara�u\", 9),\r\n" + 
					"(540, \"Ara�ua�\", 11),\r\n" + 
					"(541, \"Aracui (Castelo)\", 8),\r\n" + 
					"(542, \"Araga�u� (Caseara)\", 27),\r\n" + 
					"(543, \"Aragar�as\", 9),\r\n" + 
					"(544, \"Aragoi�nia\", 9),\r\n" + 
					"(545, \"Aragominas\", 27),\r\n" + 
					"(546, \"Araguacema\", 27),\r\n" + 
					"(547, \"Aragua�u\", 27),\r\n" + 
					"(548, \"Araguaia (Marechal Floriano)\", 8),\r\n" + 
					"(549, \"Araguaiana\", 13),\r\n" + 
					"(550, \"Aragua�na\", 27),\r\n" + 
					"(551, \"Araguainha\", 13),\r\n" + 
					"(552, \"Araguan�\", 10),\r\n" + 
					"(553, \"Araguan�\", 27),\r\n" + 
					"(554, \"Araguapaz\", 9),\r\n" + 
					"(555, \"Araguari\", 11),\r\n" + 
					"(556, \"Araguatins\", 27),\r\n" + 
					"(557, \"Arai�ses\", 10),\r\n" + 
					"(558, \"Arajara (Barbalha)\", 6),\r\n" + 
					"(559, \"Aral Moreira\", 12),\r\n" + 
					"(560, \"Aramari\", 5),\r\n" + 
					"(561, \"Arambar�\", 23),\r\n" + 
					"(562, \"Arame\", 10),\r\n" + 
					"(563, \"Aramina\", 26),\r\n" + 
					"(564, \"Aramirim (A�ucena)\", 11),\r\n" + 
					"(565, \"Arana� (Acara�)\", 6),\r\n" + 
					"(566, \"Arandu\", 26),\r\n" + 
					"(567, \"Aranha (Brumadinho)\", 11),\r\n" + 
					"(568, \"Aranha (Colombo)\", 18),\r\n" + 
					"(569, \"Arantina\", 11),\r\n" + 
					"(570, \"Arantina (Acre�na)\", 9),\r\n" + 
					"(571, \"Arapari (Itapipoca)\", 6),\r\n" + 
					"(572, \"Arap� (Tiangu�)\", 6),\r\n" + 
					"(573, \"Arape�\", 26),\r\n" + 
					"(574, \"Arapiraca\", 2),\r\n" + 
					"(575, \"Arapiranga (Rio de Contas)\", 5),\r\n" + 
					"(576, \"Arapixuna (Santar�m)\", 14),\r\n" + 
					"(577, \"Arapoema\", 27),\r\n" + 
					"(578, \"Araponga\", 11),\r\n" + 
					"(579, \"Arapongas\", 18),\r\n" + 
					"(580, \"Arapor�\", 11),\r\n" + 
					"(581, \"Araporanga (Santana do Cariri)\", 6),\r\n" + 
					"(582, \"Arapoti\", 18),\r\n" + 
					"(583, \"Arapu�\", 18),\r\n" + 
					"(584, \"Arapu�\", 11),\r\n" + 
					"(585, \"Arapuan (Jani�polis)\", 18),\r\n" + 
					"(586, \"Arapu� (Tr�s Lagoas)\", 12),\r\n" + 
					"(587, \"Araputanga\", 13),\r\n" + 
					"(588, \"Araquaim (Curu��)\", 14),\r\n" + 
					"(589, \"Araquari\", 24),\r\n" + 
					"(590, \"Araqu�m (Corea�)\", 6),\r\n" + 
					"(591, \"Arara\", 15),\r\n" + 
					"(592, \"Arara� (Alegre)\", 8),\r\n" + 
					"(593, \"Ararangu�\", 24),\r\n" + 
					"(594, \"Ararapira (Guaraque�aba)\", 18),\r\n" + 
					"(595, \"Araraquara\", 26),\r\n" + 
					"(596, \"Araras\", 26),\r\n" + 
					"(597, \"Araras (Caapiranga)\", 3),\r\n" + 
					"(598, \"Ararend�\", 6),\r\n" + 
					"(599, \"Arari\", 10),\r\n" + 
					"(600, \"Araric�\", 23),\r\n" + 
					"(601, \"Araripe\", 6),\r\n" + 
					"(602, \"Araripina\", 16),\r\n" + 
					"(603, \"Arari�s (Carir�)\", 6),\r\n" + 
					"(604, \"Araruama\", 19),\r\n" + 
					"(605, \"Araruna\", 18),\r\n" + 
					"(606, \"Araruna\", 15),\r\n" + 
					"(607, \"Arataca\", 5),\r\n" + 
					"(608, \"Aratama (Assar�)\", 6),\r\n" + 
					"(609, \"Aratiba\", 23),\r\n" + 
					"(610, \"Araticum (Ubajara)\", 6),\r\n" + 
					"(611, \"Aratuba\", 6),\r\n" + 
					"(612, \"Aratu�pe\", 5),\r\n" + 
					"(613, \"Arau�\", 25),\r\n" + 
					"(614, \"Arauc�ria\", 18),\r\n" + 
					"(615, \"Ara�jos\", 11),\r\n" + 
					"(616, \"Ara�na (Guap�)\", 11),\r\n" + 
					"(617, \"Arax�\", 11),\r\n" + 
					"(618, \"Arax�s (Presidente Bernardes)\", 26),\r\n" + 
					"(620, \"Arceburgo\", 11),\r\n" + 
					"(621, \"Arco-�ris\", 26),\r\n" + 
					"(622, \"Arco-�ris (Erval Seco)\", 23),\r\n" + 
					"(623, \"Arco-�ris (Paragominas)\", 14),\r\n" + 
					"(624, \"Arcos\", 11),\r\n" + 
					"(625, \"Arcoverde\", 16),\r\n" + 
					"(626, \"Arco Verde (Carlos Barbosa)\", 23),\r\n" + 
					"(627, \"Areado\", 11),\r\n" + 
					"(628, \"Areado (S�o Gabriel do Oeste)\", 12),\r\n" + 
					"(629, \"Areal\", 19),\r\n" + 
					"(630, \"Arealva\", 26),\r\n" + 
					"(631, \"Areia\", 15),\r\n" + 
					"(632, \"Areia Branca\", 25),\r\n" + 
					"(633, \"Areia Branca\", 20),\r\n" + 
					"(635, \"Areia Branca dos Assis (Mandirituba)\", 18),\r\n" + 
					"(636, \"Areia de Bara�nas\", 15),\r\n" + 
					"(637, \"Areial\", 15),\r\n" + 
					"(638, \"Areial (Santa Quit�ria)\", 6),\r\n" + 
					"(639, \"Areias\", 26),\r\n" + 
					"(640, \"Areias (Bezerros)\", 16),\r\n" + 
					"(641, \"Areias (Cama�ari)\", 5),\r\n" + 
					"(642, \"Areias (Castro)\", 18),\r\n" + 
					"(643, \"Areias (Melga�o)\", 14),\r\n" + 
					"(644, \"Areias (Uira�na)\", 15),\r\n" + 
					"(645, \"Arei�polis\", 26),\r\n" + 
					"(646, \"Arembepe (Cama�ari)\", 5),\r\n" + 
					"(647, \"Aren�polis\", 13),\r\n" + 
					"(648, \"Aren�polis\", 9),\r\n" + 
					"(649, \"Arer� (Barreira)\", 6),\r\n" + 
					"(650, \"Arez\", 20),\r\n" + 
					"(651, \"Argenita (Ibi�)\", 11),\r\n" + 
					"(652, \"Argirita\", 11),\r\n" + 
					"(653, \"Argoim (Rafael Jambeiro)\", 5),\r\n" + 
					"(654, \"Argolo (Nova Vi�osa)\", 5),\r\n" + 
					"(655, \"Aria� (Barreirinha)\", 3),\r\n" + 
					"(656, \"Aria� (Iranduba)\", 3),\r\n" + 
					"(657, \"Aribice (Euclides da Cunha)\", 5),\r\n" + 
					"(658, \"Aricanduva\", 11),\r\n" + 
					"(659, \"Aricanduva (Arapongas)\", 18),\r\n" + 
					"(660, \"Arinos\", 11),\r\n" + 
					"(661, \"Aripibu (Ribeir�o)\", 16),\r\n" + 
					"(662, \"Aripuan�\", 13),\r\n" + 
					"(663, \"Ariquemes\", 21),\r\n" + 
					"(664, \"Ariranha\", 26),\r\n" + 
					"(665, \"Ariranha do Iva�\", 18),\r\n" + 
					"(666, \"Ariri (Canan�ia)\", 26),\r\n" + 
					"(667, \"Ariri (Macap�)\", 4),\r\n" + 
					"(668, \"Ariscos dos Marianos (Ocara)\", 6),\r\n" + 
					"(669, \"Aristides Batista (Cora��o de Jesus)\", 11),\r\n" + 
					"(670, \"Aritagu� (Ilh�us)\", 5),\r\n" + 
					"(671, \"Arizona (Afr�nio)\", 16),\r\n" + 
					"(672, \"Arlindo (Ven�ncio Aires)\", 23),\r\n" + 
					"(673, \"Arma��o dos B�zios\", 19),\r\n" + 
					"(674, \"Armaz�m\", 24),\r\n" + 
					"(675, \"Arneiroz\", 6),\r\n" + 
					"(676, \"Arn�polis (Alfredo Wagner)\", 24),\r\n" + 
					"(677, \"Aroazes\", 17),\r\n" + 
					"(678, \"Aroeira (Cascavel)\", 18),\r\n" + 
					"(679, \"Aroeiras\", 15),\r\n" + 
					"(680, \"Aroeiras (Corea�)\", 6),\r\n" + 
					"(681, \"Aroeiras do Itaim\", 17),\r\n" + 
					"(682, \"Aroeiras (Pantano Grande)\", 23),\r\n" + 
					"(683, \"Arquimedes (Cascavel)\", 18),\r\n" + 
					"(684, \"Arraial\", 17),\r\n" + 
					"(685, \"Arraial D'Ajuda (Porto Seguro)\", 5),\r\n" + 
					"(686, \"Arraial do Cabo\", 19),\r\n" + 
					"(687, \"Arraias\", 27),\r\n" + 
					"(688, \"Arroio Bonito (Mato Leit�o)\", 23),\r\n" + 
					"(689, \"Arroio Canoas (Bar�o)\", 23),\r\n" + 
					"(690, \"Arroio do Meio\", 23),\r\n" + 
					"(691, \"Arroio do Ouro (Vale Real)\", 23),\r\n" + 
					"(692, \"Arroio do Padre\", 23),\r\n" + 
					"(693, \"Arroio do Sal\", 23),\r\n" + 
					"(694, \"Arroio dos Mengues (Dom Pedro de Alc�ntara)\", 23),\r\n" + 
					"(695, \"Arroio do S� (Santa Maria)\", 23),\r\n" + 
					"(696, \"Arroio dos Ratos\", 23),\r\n" + 
					"(697, \"Arroio do Tigre\", 23),\r\n" + 
					"(698, \"Arroio Grande\", 23),\r\n" + 
					"(699, \"Arroio Grande (Santa Maria)\", 23),\r\n" + 
					"(700, \"Arroio Grande (Selbach)\", 23),\r\n" + 
					"(701, \"Arroio Teixeira (Cap�o da Canoa)\", 23),\r\n" + 
					"(702, \"Arroio Trinta\", 24),\r\n" + 
					"(703, \"Arrojado (Lavras da Mangabeira)\", 6),\r\n" + 
					"(704, \"Arrozal (Pira�)\", 19),\r\n" + 
					"(705, \"Arruda (Ros�rio Oeste)\", 13),\r\n" + 
					"(706, \"Artur Nogueira\", 26),\r\n" + 
					"(707, \"Aruan�\", 9),\r\n" + 
					"(708, \"Aruaru (Morada Nova)\", 6),\r\n" + 
					"(709, \"Aruj�\", 26),\r\n" + 
					"(710, \"Arumanduba (Almeirim)\", 14),\r\n" + 
					"(711, \"Aruri (Itaituba)\", 14),\r\n" + 
					"(712, \"Arvoredo\", 24),\r\n" + 
					"(713, \"�rvore S� (Santa Vit�ria do Palmar)\", 23),\r\n" + 
					"(714, \"Arvorezinha\", 23),\r\n" + 
					"(715, \"Ascen��o (Par� de Minas)\", 11),\r\n" + 
					"(716, \"Ascurra\", 24),\r\n" + 
					"(717, \"Asp�sia\", 26),\r\n" + 
					"(718, \"Assa�\", 18),\r\n" + 
					"(719, \"Assara� (Pocrane)\", 11),\r\n" + 
					"(720, \"Assar�\", 6),\r\n" + 
					"(721, \"Assari (Barra do Bugres)\", 13),\r\n" + 
					"(722, \"Assis\", 26),\r\n" + 
					"(723, \"Assis Brasil\", 1),\r\n" + 
					"(724, \"Assis Chateaubriand\", 18),\r\n" + 
					"(725, \"Assis (Crate�s)\", 6),\r\n" + 
					"(726, \"Assist�ncia (Rio Claro)\", 26),\r\n" + 
					"(727, \"Assun��o\", 15),\r\n" + 
					"(728, \"Assun��o do Piau�\", 17),\r\n" + 
					"(729, \"Assun��o (Itapipoca)\", 6),\r\n" + 
					"(730, \"Assun��o (Solon�pole)\", 6),\r\n" + 
					"(731, \"Astolfo Dutra\", 11),\r\n" + 
					"(732, \"Astorga\", 18),\r\n" + 
					"(733, \"Atafona (Santo �ngelo)\", 23),\r\n" + 
					"(734, \"Atalaia\", 18),\r\n" + 
					"(735, \"Atalaia\", 2),\r\n" + 
					"(736, \"Atalaia do Norte\", 3),\r\n" + 
					"(737, \"Atalanta\", 24),\r\n" + 
					"(738, \"Atal�ia\", 11),\r\n" + 
					"(739, \"Atapus (Goiana)\", 16),\r\n" + 
					"(740, \"Aterrado Torto (Pouso Redondo)\", 24),\r\n" + 
					"(741, \"Atia�u (Sarandi)\", 23),\r\n" + 
					"(742, \"Atibaia\", 26),\r\n" + 
					"(743, \"At�lio Viv�cqua\", 8),\r\n" + 
					"(744, \"Atl�ntico (Santa Vit�ria do Palmar)\", 23),\r\n" + 
					"(745, \"Atl�ntida (Fl�rida Paulista)\", 26),\r\n" + 
					"(746, \"Aturia� (Augusto Corr�a)\", 14),\r\n" + 
					"(747, \"Augustin�polis\", 27),\r\n" + 
					"(748, \"Augusto Corr�a\", 14),\r\n" + 
					"(749, \"Augusto de Lima\", 11),\r\n" + 
					"(750, \"Augusto Montenegro (Urucurituba)\", 3),\r\n" + 
					"(751, \"Augusto Pestana\", 23),\r\n" + 
					"(752, \"�urea\", 23),\r\n" + 
					"(753, \"Aurelino Leal\", 5),\r\n" + 
					"(754, \"Auriflama\", 26),\r\n" + 
					"(755, \"Auril�ndia\", 9),\r\n" + 
					"(756, \"Auriverde (Crix�s)\", 9),\r\n" + 
					"(757, \"Aurizona (Godofredo Viana)\", 10),\r\n" + 
					"(758, \"Aurora\", 24),\r\n" + 
					"(759, \"Aurora\", 6),\r\n" + 
					"(760, \"Aurora do Igua�u (S�o Miguel do Igua�u)\", 18),\r\n" + 
					"(761, \"Aurora do Par�\", 14),\r\n" + 
					"(762, \"Aurora do Tocantins\", 27),\r\n" + 
					"(763, \"Autazes\", 3),\r\n" + 
					"(764, \"Ava�\", 26),\r\n" + 
					"(765, \"Ava� do Jacinto (Jacinto)\", 11),\r\n" + 
					"(766, \"Avanhandava\", 26),\r\n" + 
					"(767, \"Avar�\", 26),\r\n" + 
					"(768, \"Aveiro\", 14),\r\n" + 
					"(769, \"Avelar (Paty do Alferes)\", 19),\r\n" + 
					"(770, \"Avelino Lopes\", 17),\r\n" + 
					"(771, \"Avelino Paranhos (Espumoso)\", 23),\r\n" + 
					"(772, \"Avelin�polis\", 9),\r\n" + 
					"(773, \"Axinim (Borba)\", 3),\r\n" + 
					"(774, \"Axix�\", 10),\r\n" + 
					"(775, \"Axix� do Tocantins\", 27),\r\n" + 
					"(776, \"Azambuja (Pedras Grandes)\", 24),\r\n" + 
					"(777, \"Azevedo Sodr� (S�o Gabriel)\", 23),\r\n" + 
					"(778, \"Azurita (Mateus Leme)\", 11),\r\n" + 
					"(779, \"Baba�ul�ndia\", 27),\r\n" + 
					"(780, \"Babil�nia (Delfin�polis)\", 11),\r\n" + 
					"(781, \"Bacabal\", 10),\r\n" + 
					"(782, \"Bacabeira\", 10),\r\n" + 
					"(783, \"Bacaetava (Iper�)\", 26),\r\n" + 
					"(784, \"Ba��o (Itabirito)\", 11),\r\n" + 
					"(785, \"Bacatuba (Buriti Bravo)\", 10),\r\n" + 
					"(786, \"Bacax� (Saquarema)\", 19),\r\n" + 
					"(787, \"Bacil�ndia (Fazenda Nova)\", 9),\r\n" + 
					"(788, \"Bacupari (Palmares do Sul)\", 23),\r\n" + 
					"(789, \"Bacuri\", 10),\r\n" + 
					"(790, \"Bacuriti (Cafel�ndia)\", 26),\r\n" + 
					"(791, \"Bacurituba\", 10),\r\n" + 
					"(792, \"Badaj�s (Codaj�s)\", 3),\r\n" + 
					"(793, \"Bady Bassitt\", 26),\r\n" + 
					"(794, \"Baependi\", 11),\r\n" + 
					"(795, \"Bag�\", 23),\r\n" + 
					"(796, \"Bagre\", 14),\r\n" + 
					"(797, \"Bagua�u (Ol�mpia)\", 26),\r\n" + 
					"(798, \"Baguari (Governador Valadares)\", 11),\r\n" + 
					"(799, \"Baia Alta (Ponte Serrada)\", 24),\r\n" + 
					"(800, \"Ba�a da Trai��o\", 15),\r\n" + 
					"(801, \"Ba�a Formosa\", 20),\r\n" + 
					"(802, \"Baian�polis\", 5),\r\n" + 
					"(803, \"Baian�polis (Corguinho)\", 12),\r\n" + 
					"(804, \"Bai�o\", 14),\r\n" + 
					"(805, \"Bailique (Macap�)\", 4),\r\n" + 
					"(806, \"Bai�es (Formiga)\", 11),\r\n" + 
					"(807, \"Bairro Alto (Natividade da Serra)\", 26),\r\n" + 
					"(808, \"Bairro Cachoeira (Guarapuava)\", 18),\r\n" + 
					"(809, \"Bairro Limoeiro (Londrina)\", 18),\r\n" + 
					"(810, \"Baixa da On�a (Arapiraca)\", 2),\r\n" + 
					"(811, \"Baixa do Capim (Arapiraca)\", 2),\r\n" + 
					"(812, \"Baixa do Palmeira (Sapea�u)\", 5),\r\n" + 
					"(813, \"Baixa Grande\", 5),\r\n" + 
					"(814, \"Baixa Grande (Arapiraca)\", 2),\r\n" + 
					"(815, \"Baixa Grande do Ribeiro\", 17),\r\n" + 
					"(816, \"Baixa Grande (Itapaj�)\", 6),\r\n" + 
					"(817, \"Baix�o (Jequi�)\", 5),\r\n" + 
					"(818, \"Baixa Quente (Minas Novas)\", 11),\r\n" + 
					"(819, \"Baixa (Uberaba)\", 11),\r\n" + 
					"(820, \"Baixinha (Uba�ra)\", 5),\r\n" + 
					"(821, \"Baixio\", 6),\r\n" + 
					"(822, \"Baixio da Donana (Juc�s)\", 6),\r\n" + 
					"(823, \"Baixio das Palmeiras (Crato)\", 6),\r\n" + 
					"(824, \"Baixio (Deputado Irapuan Pinheiro)\", 6),\r\n" + 
					"(825, \"Baixo Guandu\", 8),\r\n" + 
					"(826, \"Balan�os (Cachoeira dos �ndios)\", 15),\r\n" + 
					"(827, \"Balbina (Presidente Figueiredo)\", 3),\r\n" + 
					"(828, \"Balbin�polis (Bert�polis)\", 11),\r\n" + 
					"(829, \"Balbinos\", 26),\r\n" + 
					"(830, \"Baldim\", 11),\r\n" + 
					"(831, \"Baliza\", 9),\r\n" + 
					"(832, \"Baliza (Gaurama)\", 23),\r\n" + 
					"(833, \"Balne�rio Arroio do Silva\", 24),\r\n" + 
					"(834, \"Balne�rio Barra do Sul\", 24),\r\n" + 
					"(835, \"Balne�rio Cambori�\", 24),\r\n" + 
					"(836, \"Balne�rio Gaivota\", 24),\r\n" + 
					"(838, \"Balne�rio Pi�arras\", 24),\r\n" + 
					"(839, \"Balne�rio Pinhal\", 23),\r\n" + 
					"(840, \"Balne�rio Rinc�o\", 24),\r\n" + 
					"(841, \"B�lsamo\", 26),\r\n" + 
					"(842, \"B�lsamo (Arapiraca)\", 2),\r\n" + 
					"(843, \"B�lsamo (Ribas do Rio Pardo)\", 12),\r\n" + 
					"(844, \"Balsa Nova\", 18),\r\n" + 
					"(845, \"Balsas\", 10),\r\n" + 
					"(846, \"Baltazar (Santo Ant�nio de P�dua)\", 19),\r\n" + 
					"(847, \"Baluarte (Pil�o Arcado)\", 5),\r\n" + 
					"(848, \"Bambu�\", 11),\r\n" + 
					"(849, \"Banabui�\", 6),\r\n" + 
					"(850, \"Bananal\", 26),\r\n" + 
					"(851, \"Bananas (Nova Laranjeiras)\", 18),\r\n" + 
					"(852, \"Bananeiras\", 15),\r\n" + 
					"(853, \"Bananeiras (Arapiraca)\", 2),\r\n" + 
					"(854, \"Bandeira\", 11),\r\n" + 
					"(855, \"Bandeira do Almada (Itaju�pe)\", 5),\r\n" + 
					"(856, \"Bandeira do Col�nia (Itapetinga)\", 5),\r\n" + 
					"(857, \"Bandeira do Sul\", 11),\r\n" + 
					"(858, \"Bandeira (Itatira)\", 6),\r\n" + 
					"(859, \"Bandeirante\", 24),\r\n" + 
					"(860, \"Bandeirantes\", 12),\r\n" + 
					"(861, \"Bandeirantes\", 18),\r\n" + 
					"(862, \"Bandeirantes d'Oeste (Quarto Centen�rio)\", 18),\r\n" + 
					"(863, \"Bandeirantes D'Oeste (Sud Mennucci)\", 26),\r\n" + 
					"(864, \"Bandeirantes do Tocantins\", 27),\r\n" + 
					"(865, \"Bandeirantes (Mariana)\", 11),\r\n" + 
					"(866, \"Bandeirantes (Nova Crix�s)\", 9),\r\n" + 
					"(867, \"Bandeirinha (Camaqu�)\", 23),\r\n" + 
					"(868, \"Bandia�u (Concei��o do Coit�)\", 5),\r\n" + 
					"(869, \"Banhado do Col�gio (Camaqu�)\", 23),\r\n" + 
					"(870, \"Banhado (Guarapuava)\", 18),\r\n" + 
					"(871, \"Bannach\", 14),\r\n" + 
					"(872, \"Banquete (Bom Jardim)\", 19),\r\n" + 
					"(873, \"Banza�\", 5),\r\n" + 
					"(874, \"Baracho (Sobral)\", 6),\r\n" + 
					"(875, \"Bar�o\", 23),\r\n" + 
					"(876, \"Bar�o Ataliba Nogueira (Itapira)\", 26),\r\n" + 
					"(877, \"Bar�o de Antonina\", 26),\r\n" + 
					"(878, \"Bar�o de Aquiraz (Campos Sales)\", 6),\r\n" + 
					"(879, \"Bar�o de Cocais\", 11),\r\n" + 
					"(880, \"Bar�o de Cotegipe\", 23),\r\n" + 
					"(881, \"Bar�o de Graja�\", 10),\r\n" + 
					"(882, \"Bar�o de Juparana (Valen�a)\", 19),\r\n" + 
					"(883, \"Bar�o de Lucena (Nova Esperan�a)\", 18),\r\n" + 
					"(884, \"Bar�o de Melga�o\", 13),\r\n" + 
					"(885, \"Bar�o de Monte Alto\", 11),\r\n" + 
					"(886, \"Bar�o de Serra Branca (Santana do Matos)\", 20),\r\n" + 
					"(887, \"Bar�o de Tromai (C�ndido Mendes)\", 10),\r\n" + 
					"(888, \"Bar�o do Triunfo\", 23),\r\n" + 
					"(889, \"Bara�na\", 15),\r\n" + 
					"(890, \"Bara�na\", 20),\r\n" + 
					"(891, \"Bara�nas (Seabra)\", 5),\r\n" + 
					"(892, \"Barbacena\", 11),\r\n" + 
					"(893, \"Barbada (Chor�)\", 6),\r\n" + 
					"(894, \"Barbalha\", 6),\r\n" + 
					"(895, \"Barbosa\", 26),\r\n" + 
					"(896, \"Barbosa Ferraz\", 18),\r\n" + 
					"(897, \"Barbosil�ndia (Posse)\", 9),\r\n" + 
					"(898, \"Barcarena\", 14),\r\n" + 
					"(899, \"Barcelona\", 20),\r\n" + 
					"(900, \"Barcelos\", 3),\r\n" + 
					"(901, \"Barcelos do Sul (Camamu)\", 5),\r\n" + 
					"(902, \"Barcelos (S�o Jo�o da Barra)\", 19),\r\n" + 
					"(903, \"Bariri\", 26),\r\n" + 
					"(904, \"Barra\", 5),\r\n" + 
					"(905, \"Barra (Aiuaba)\", 6),\r\n" + 
					"(906, \"Barra Alegre (Bom Jardim)\", 19),\r\n" + 
					"(907, \"Barra Alegre (Ipatinga)\", 11),\r\n" + 
					"(908, \"Barra Avenida (Jequi�)\", 5),\r\n" + 
					"(909, \"Barra Bonita\", 26),\r\n" + 
					"(910, \"Barra Bonita\", 24),\r\n" + 
					"(911, \"Barra Bonita (Francisco Beltr�o)\", 18),\r\n" + 
					"(912, \"Barra Bonita (Mato Rico)\", 18),\r\n" + 
					"(913, \"Barrac�o\", 18),\r\n" + 
					"(914, \"Barrac�o\", 23),\r\n" + 
					"(915, \"Barracas (Capela)\", 25),\r\n" + 
					"(916, \"Barracas (Ponto Novo)\", 5),\r\n" + 
					"(917, \"Barra Clara (Angelina)\", 24),\r\n" + 
					"(918, \"Barra Curta Baixa (Constantina)\", 23),\r\n" + 
					"(919, \"Barra da Estiva\", 5),\r\n" + 
					"(920, \"Barra da Figueira (Pocrane)\", 11),\r\n" + 
					"(921, \"Barra D'Alc�ntara\", 17),\r\n" + 
					"(922, \"Barra da Prata (V�tor Meireles)\", 24),\r\n" + 
					"(923, \"Barra de Farias (Brejo da Madre de Deus)\", 16),\r\n" + 
					"(924, \"Barra de Guabiraba\", 16),\r\n" + 
					"(925, \"Barra de Santana\", 15),\r\n" + 
					"(926, \"Barra de Santa Rosa\", 15),\r\n" + 
					"(927, \"Barra de Santo Ant�nio\", 2),\r\n" + 
					"(928, \"Barra de S�o Francisco\", 8),\r\n" + 
					"(929, \"Barra de S�o Jo�o (Casimiro de Abreu)\", 19),\r\n" + 
					"(930, \"Barra de S�o Miguel\", 15),\r\n" + 
					"(931, \"Barra de S�o Miguel\", 2),\r\n" + 
					"(932, \"Barra de S�o Pedro (Ouricuri)\", 16),\r\n" + 
					"(933, \"Barra do Ariranha (Mantena)\", 11),\r\n" + 
					"(934, \"Barra do Bonif�cio (Palmeira dos �ndios)\", 2),\r\n" + 
					"(935, \"Barra do Brejo (Bom Conselho)\", 16),\r\n" + 
					"(936, \"Barra do Bugres\", 13),\r\n" + 
					"(937, \"Barra do Camaratuba (Mataraca)\", 15),\r\n" + 
					"(938, \"Barra do Chap�u\", 26),\r\n" + 
					"(939, \"Barra do Cho�a\", 5),\r\n" + 
					"(940, \"Barra do Corda\", 10),\r\n" + 
					"(941, \"Barra do Cuiet� (Conselheiro Pena)\", 11),\r\n" + 
					"(942, \"Barra do Gar�as\", 13),\r\n" + 
					"(943, \"Barra do Grota (Aragua�na)\", 27),\r\n" + 
					"(944, \"Barra do Guarita\", 23),\r\n" + 
					"(945, \"Barra do Ing� (Acopiara)\", 6),\r\n" + 
					"(946, \"Barra do Jacar�\", 18),\r\n" + 
					"(947, \"Barra do Jacu�pe (Cama�ari)\", 5),\r\n" + 
					"(948, \"Barra do Jardim (Agrestina)\", 16),\r\n" + 
					"(949, \"Barra do Mendes\", 5),\r\n" + 
					"(950, \"Barra do Ouro\", 27),\r\n" + 
					"(951, \"Barra do Ouro (Maquin�)\", 23),\r\n" + 
					"(952, \"Barra do Pira�\", 19),\r\n" + 
					"(953, \"Barra do Pojuca (Cama�ari)\", 5),\r\n" + 
					"(954, \"Barra do Quara�\", 23),\r\n" + 
					"(955, \"Barra do Riach�o (S�o Joaquim do Monte)\", 16),\r\n" + 
					"(956, \"Barra do Ribeiro\", 23),\r\n" + 
					"(957, \"Barra do Rio Azul\", 23),\r\n" + 
					"(958, \"Barra do Rocha\", 5),\r\n" + 
					"(959, \"Barra dos Coqueiros\", 25),\r\n" + 
					"(960, \"Barra do Sirinha�m (Sirinha�m)\", 16),\r\n" + 
					"(961, \"Barra do Sotero (Croat�)\", 6),\r\n" + 
					"(962, \"Barra do Tarrachil (Chorroch�)\", 5),\r\n" + 
					"(963, \"Barra do Turvo\", 26),\r\n" + 
					"(964, \"Barra Dourada (Neves Paulista)\", 26),\r\n" + 
					"(965, \"Barra Feliz (Santa B�rbara)\", 11),\r\n" + 
					"(966, \"Barra (Fortim)\", 6),\r\n" + 
					"(967, \"Barra Fria (Erval Velho)\", 24),\r\n" + 
					"(968, \"Barra Funda\", 23),\r\n" + 
					"(969, \"Barragem do Itu (Manoel Viana)\", 23),\r\n" + 
					"(970, \"Barra Grande (Faxinal dos Guedes)\", 24),\r\n" + 
					"(971, \"Barra Grande (Itapejara d'Oeste)\", 18),\r\n" + 
					"(972, \"Barra Grande (Maragogi)\", 2),\r\n" + 
					"(973, \"Barra Grande (Planalto)\", 18),\r\n" + 
					"(974, \"Barra Grande (Ponta Grossa)\", 18),\r\n" + 
					"(975, \"Barra Grande (Vera Cruz)\", 5),\r\n" + 
					"(976, \"Barra (Iguatu)\", 6),\r\n" + 
					"(977, \"Barra Longa\", 11),\r\n" + 
					"(978, \"Barra Mansa\", 19),\r\n" + 
					"(979, \"Barranco Alto (Alfenas)\", 11),\r\n" + 
					"(980, \"Barr�nia (Caconde)\", 26),\r\n" + 
					"(981, \"Barra Nova (Reden��o)\", 6),\r\n" + 
					"(982, \"Barra Nova (Tau�)\", 6),\r\n" + 
					"(983, \"Barras\", 17),\r\n" + 
					"(984, \"Barra Santa Salete (Manoel Ribas)\", 18),\r\n" + 
					"(985, \"Barra Seca (Jaguar�)\", 8),\r\n" + 
					"(986, \"Barra Velha\", 24),\r\n" + 
					"(987, \"Barreira\", 6),\r\n" + 
					"(988, \"Barreira Branca (Santa Maria das Barreiras)\", 14),\r\n" + 
					"(989, \"Barreira dos Campos (Santana do Araguaia)\", 14),\r\n" + 
					"(990, \"Barreira dos Vianas (Aracati)\", 6),\r\n" + 
					"(991, \"Barreiras\", 5),\r\n" + 
					"(992, \"Barreiras (Coruripe)\", 2),\r\n" + 
					"(993, \"Barreiras do Piau�\", 17),\r\n" + 
					"(994, \"Barreiras (Iguatu)\", 6),\r\n" + 
					"(995, \"Barreiras (Itaituba)\", 14),\r\n" + 
					"(996, \"Barreirinha\", 3),\r\n" + 
					"(997, \"Barreirinhas\", 10),\r\n" + 
					"(998, \"Barreirinho (Sarandi)\", 23),\r\n" + 
					"(999, \"Barreiro Branco (Catuti)\", 11),\r\n" + 
					"(1000, \"Barreiro (Cascavel)\", 18),\r\n" + 
					"(1001, \"Barreiro da Raiz (Jana�ba)\", 11),\r\n" + 
					"(1002, \"Barreiro do Rio Verde (Jana�ba)\", 11),\r\n" + 
					"(1003, \"Barreiro (Guarapuava)\", 18),\r\n" + 
					"(1004, \"Barreiro (Iju�)\", 23),\r\n" + 
					"(1005, \"Barreiro (Ortigueira)\", 18),\r\n" + 
					"(1006, \"Barreiros\", 16),\r\n" + 
					"(1007, \"Barreiros (Apucarana)\", 18),\r\n" + 
					"(1008, \"Barreiros (Monsenhor Tabosa)\", 6),\r\n" + 
					"(1009, \"Barreiros (Potengi)\", 6),\r\n" + 
					"(1010, \"Barreiros (Riach�o do Jacu�pe)\", 5),\r\n" + 
					"(1011, \"Barreiros (S�o Benedito)\", 6),\r\n" + 
					"(1012, \"Barrento (Itapipoca)\", 6),\r\n" + 
					"(1013, \"Barretos\", 26),\r\n" + 
					"(1014, \"Barretos de Alvin�polis (Alvin�polis)\", 11),\r\n" + 
					"(1015, \"Barrinha\", 26),\r\n" + 
					"(1016, \"Barrinha (Castro)\", 18),\r\n" + 
					"(1017, \"Barrinha (Seberi)\", 23),\r\n" + 
					"(1018, \"Barrinha (Tr�s de Maio)\", 23),\r\n" + 
					"(1019, \"Barro\", 6),\r\n" + 
					"(1020, \"Barro Alto\", 5),\r\n" + 
					"(1021, \"Barro Alto\", 9),\r\n" + 
					"(1022, \"Barro Alto (Iguatu)\", 6),\r\n" + 
					"(1023, \"Barro Branco (Lauro M�ller)\", 24),\r\n" + 
					"(1024, \"Barroc�o (Gr�o Mogol)\", 11),\r\n" + 
					"(1025, \"Barroc�o (Ribeira do Pombal)\", 5),\r\n" + 
					"(1026, \"Barrocas\", 5),\r\n" + 
					"(1027, \"Barro Duro\", 17),\r\n" + 
					"(1028, \"Barro Duro (Tut�ia)\", 10),\r\n" + 
					"(1029, \"Barrol�ndia\", 27),\r\n" + 
					"(1030, \"Barro Preto\", 5),\r\n" + 
					"(1031, \"Barro Preto (Ajuricaba)\", 23),\r\n" + 
					"(1032, \"Barro Preto (Cascavel)\", 18),\r\n" + 
					"(1033, \"Barro Preto (Londrina)\", 18),\r\n" + 
					"(1034, \"Barro Preto (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(1035, \"Barroquinha\", 6),\r\n" + 
					"(1036, \"Barros Cassal\", 23),\r\n" + 
					"(1037, \"Barroso\", 11),\r\n" + 
					"(1038, \"Barro Vermelho (Cachoeira do Sul)\", 23),\r\n" + 
					"(1039, \"Barro Vermelho (Cura��)\", 5),\r\n" + 
					"(1040, \"Barro Vermelho (Gravata�)\", 23),\r\n" + 
					"(1041, \"Barro Vermelho (Junqueiro)\", 2),\r\n" + 
					"(1042, \"Barueri\", 26),\r\n" + 
					"(1043, \"Bas�lio (Herval)\", 23),\r\n" + 
					"(1044, \"Basti�o (Boninal)\", 5),\r\n" + 
					"(1045, \"Bastos\", 26),\r\n" + 
					"(1046, \"Bataguassu\", 12),\r\n" + 
					"(1047, \"Batalha\", 17),\r\n" + 
					"(1048, \"Batalha\", 2),\r\n" + 
					"(1049, \"Batatais\", 26),\r\n" + 
					"(1050, \"Batatal (Itaocara)\", 19),\r\n" + 
					"(1051, \"Batateira (Bel�m de Maria)\", 16),\r\n" + 
					"(1052, \"Batatuba (Piracaia)\", 26),\r\n" + 
					"(1053, \"Bataypor�\", 12),\r\n" + 
					"(1054, \"Bateias (Campo Largo)\", 18),\r\n" + 
					"(1055, \"Bateias de Baixo (Campo Alegre)\", 24),\r\n" + 
					"(1056, \"Bate P� (Vit�ria da Conquista)\", 5),\r\n" + 
					"(1057, \"Batinga (Itanh�m)\", 5),\r\n" + 
					"(1058, \"Batingas (Arapiraca)\", 2),\r\n" + 
					"(1059, \"Batista Botelho (�leo)\", 26),\r\n" + 
					"(1060, \"Batovira (Progresso)\", 23),\r\n" + 
					"(1061, \"Batovi (Tesouro)\", 13),\r\n" + 
					"(1062, \"Baturit�\", 6),\r\n" + 
					"(1063, \"Baturit� (Afu�)\", 14),\r\n" + 
					"(1064, \"Ba� (Candiota)\", 23),\r\n" + 
					"(1065, \"Ba� (Estrela do Indai�)\", 11),\r\n" + 
					"(1066, \"Ba� (Guai�ba)\", 6),\r\n" + 
					"(1067, \"Ba� (Iguatu)\", 6),\r\n" + 
					"(1068, \"Baul�ndia (Renascen�a)\", 18),\r\n" + 
					"(1069, \"Bauru\", 26),\r\n" + 
					"(1070, \"Ba�s (Acorizal)\", 13),\r\n" + 
					"(1071, \"Ba�s (Costa Rica)\", 12),\r\n" + 
					"(1072, \"Bauxi (Ros�rio Oeste)\", 13),\r\n" + 
					"(1073, \"Bayeux\", 15),\r\n" + 
					"(1074, \"Bebedouro\", 26),\r\n" + 
					"(1075, \"Bebel�ndia (Santa Rita)\", 15),\r\n" + 
					"(1076, \"Beberibe\", 6),\r\n" + 
					"(1077, \"Bebida Velha (Pureza)\", 20),\r\n" + 
					"(1078, \"Beja (Abaetetuba)\", 14),\r\n" + 
					"(1079, \"Bela Alvorada (Para�so das �guas)\", 12),\r\n" + 
					"(1080, \"Bela Cruz\", 6),\r\n" + 
					"(1081, \"Bela Flor (Catu)\", 5),\r\n" + 
					"(1082, \"Bela Floresta (Pereira Barreto)\", 26),\r\n" + 
					"(1083, \"Bel�gua\", 10),\r\n" + 
					"(1084, \"Bela Vista\", 12),\r\n" + 
					"(1085, \"Bela Vista (Campos Novos)\", 24),\r\n" + 
					"(1086, \"Bela Vista (Crato)\", 6),\r\n" + 
					"(1087, \"Bela Vista da Caroba\", 18),\r\n" + 
					"(1088, \"Bela Vista de Goi�s\", 9),\r\n" + 
					"(1089, \"Bela Vista de Minas\", 11),\r\n" + 
					"(1090, \"Bela Vista do Caracol (Itaituba)\", 14),\r\n" + 
					"(1091, \"Bela Vista do F�o (Marques de Souza)\", 23),\r\n" + 
					"(1092, \"Bela Vista do Iva� (F�nix)\", 18),\r\n" + 
					"(1093, \"Bela Vista do Maranh�o\", 10),\r\n" + 
					"(1094, \"Bela Vista do Para�so\", 18),\r\n" + 
					"(1095, \"Bela Vista do Piau�\", 17),\r\n" + 
					"(1096, \"Bela Vista do Piquiri (Campina da Lagoa)\", 18),\r\n" + 
					"(1097, \"Bela Vista do Sul (Mafra)\", 24),\r\n" + 
					"(1098, \"Bela Vista do Tapiracui (Tapejara)\", 18),\r\n" + 
					"(1099, \"Bela Vista do Toldo\", 24),\r\n" + 
					"(1100, \"Bela Vista (Guarania�u)\", 18),\r\n" + 
					"(1101, \"Bela Vista (Itapipoca)\", 6),\r\n" + 
					"(1102, \"Bela Vista (Machadinho)\", 23),\r\n" + 
					"(1103, \"Bela Vista (Manoel Ribas)\", 18),\r\n" + 
					"(1104, \"Bela Vista (Passo Fundo)\", 23),\r\n" + 
					"(1105, \"Bela Vista (Segredo)\", 23),\r\n" + 
					"(1106, \"Bela Vista (Tr�s Passos)\", 23),\r\n" + 
					"(1107, \"Bela Vista (Vacaria)\", 23),\r\n" + 
					"(1108, \"Bel�m\", 14),\r\n" + 
					"(1109, \"Bel�m\", 2),\r\n" + 
					"(1110, \"Bel�m\", 15),\r\n" + 
					"(1111, \"Bel�m da Cachoeira (Cachoeira)\", 5),\r\n" + 
					"(1112, \"Bel�m de Maria\", 16),\r\n" + 
					"(1113, \"Bel�m de Solim�es (Tabatinga)\", 3),\r\n" + 
					"(1114, \"Bel�m do Brejo do Cruz\", 15),\r\n" + 
					"(1115, \"Bel�m do Piau�\", 17),\r\n" + 
					"(1116, \"Bel�m do S�o Francisco\", 16),\r\n" + 
					"(1117, \"Bel�m (Quixeramobim)\", 6),\r\n" + 
					"(1118, \"Belford Roxo\", 19),\r\n" + 
					"(1119, \"Belis�rio (Muria�)\", 11),\r\n" + 
					"(1120, \"Belmiro Braga\", 11),\r\n" + 
					"(1121, \"Belmonte\", 5),\r\n" + 
					"(1122, \"Belmonte\", 24),\r\n" + 
					"(1123, \"Belmonte (Crato)\", 6),\r\n" + 
					"(1124, \"Belo Campo\", 5),\r\n" + 
					"(1125, \"Belo Campo (Am�rica Dourada)\", 5),\r\n" + 
					"(1126, \"Belo Horizonte\", 11),\r\n" + 
					"(1127, \"Belo Horizonte (Bento Fernandes)\", 20),\r\n" + 
					"(1128, \"Belo Jardim\", 16),\r\n" + 
					"(1129, \"Belo Monte\", 2),\r\n" + 
					"(1130, \"Belo Monte (Novo Repartimento)\", 14),\r\n" + 
					"(1131, \"Belo Oriente\", 11),\r\n" + 
					"(1132, \"Belo Oriente (Te�filo Otoni)\", 11),\r\n" + 
					"(1133, \"Belo Vale\", 11),\r\n" + 
					"(1134, \"Bel Rios (Diamantino)\", 13),\r\n" + 
					"(1135, \"Belterra\", 14),\r\n" + 
					"(1136, \"Beltr�o (Corinto)\", 11),\r\n" + 
					"(1137, \"Beluno (S�o Francisco de Assis)\", 23),\r\n" + 
					"(1138, \"Bem-Bom (Casa Nova)\", 5),\r\n" + 
					"(1139, \"Bemposta (Tr�s Rios)\", 19),\r\n" + 
					"(1140, \"Bendeg� (Canudos)\", 5),\r\n" + 
					"(1141, \"Beneditinos\", 17),\r\n" + 
					"(1142, \"Benedito Leite\", 10),\r\n" + 
					"(1143, \"Benedito Novo\", 24),\r\n" + 
					"(1144, \"Benevides\", 14),\r\n" + 
					"(1145, \"Benfica (Benevides)\", 14),\r\n" + 
					"(1146, \"Bengalas (Passira)\", 16),\r\n" + 
					"(1147, \"Benjamin Constant\", 3),\r\n" + 
					"(1148, \"Benjamin Constant do Sul\", 23),\r\n" + 
					"(1149, \"Bentivi (Bonito)\", 16),\r\n" + 
					"(1150, \"Bento de Abreu\", 26),\r\n" + 
					"(1151, \"Bento Fernandes\", 20),\r\n" + 
					"(1152, \"Bento Gon�alves\", 23),\r\n" + 
					"(1153, \"Bent�polis de Minas (Uba�)\", 11),\r\n" + 
					"(1154, \"Bent�polis (Guaraci)\", 18),\r\n" + 
					"(1155, \"Bento Sim�es (Irar�)\", 5),\r\n" + 
					"(1156, \"Bequim�o\", 10),\r\n" + 
					"(1157, \"Berilo\", 11),\r\n" + 
					"(1158, \"Berizal\", 11),\r\n" + 
					"(1159, \"Bernardelli (Rondon)\", 18),\r\n" + 
					"(1160, \"Bernardino Batista\", 15),\r\n" + 
					"(1161, \"Bernardino de Campos\", 26),\r\n" + 
					"(1162, \"Bernardo do Mearim\", 10),\r\n" + 
					"(1163, \"Bernardo Say�o\", 27),\r\n" + 
					"(1164, \"Bernardo Vieira (Serra Talhada)\", 16),\r\n" + 
					"(1165, \"Bertioga\", 26),\r\n" + 
					"(1166, \"Bertol�nia\", 17),\r\n" + 
					"(1167, \"Bert�polis\", 11),\r\n" + 
					"(1168, \"Beruri\", 3),\r\n" + 
					"(1169, \"Bet�nia\", 16),\r\n" + 
					"(1170, \"Bet�nia (Croat�)\", 6),\r\n" + 
					"(1171, \"Bet�nia do Piau�\", 17),\r\n" + 
					"(1172, \"Bet�nia (Hidrol�ndia)\", 6),\r\n" + 
					"(1173, \"Bet�nia (Ibiapina)\", 6),\r\n" + 
					"(1174, \"Bet�nia (Solon�pole)\", 6),\r\n" + 
					"(1175, \"Betaras (Almirante Tamandar�)\", 18),\r\n" + 
					"(1176, \"Betim\", 11),\r\n" + 
					"(1177, \"Bexiga (Rio Pardo)\", 23),\r\n" + 
					"(1178, \"Bezerra (Formosa)\", 9),\r\n" + 
					"(1179, \"Bezerro Branco (C�ceres)\", 13),\r\n" + 
					"(1180, \"Bezerros\", 16),\r\n" + 
					"(1181, \"Bias Fortes\", 11),\r\n" + 
					"(1182, \"Bicas\", 11),\r\n" + 
					"(1183, \"Bicu�ba (Raul Soares)\", 11),\r\n" + 
					"(1184, \"Bigua�u\", 24),\r\n" + 
					"(1185, \"Bilac\", 26),\r\n" + 
					"(1186, \"Biquinhas\", 11),\r\n" + 
					"(1187, \"Birig�i\", 26),\r\n" + 
					"(1188, \"Biritiba-Mirim\", 26),\r\n" + 
					"(1189, \"Biritiba-Ussu (Mogi das Cruzes)\", 26),\r\n" + 
					"(1190, \"Biritinga\", 5),\r\n" + 
					"(1191, \"Biscaia (Ponta Grossa)\", 18),\r\n" + 
					"(1192, \"Bitupit� (Barroquinha)\", 6),\r\n" + 
					"(1193, \"Bituri (Jeceaba)\", 11),\r\n" + 
					"(1194, \"Bituruna\", 18),\r\n" + 
					"(1195, \"Bixopa (Limoeiro do Norte)\", 6),\r\n" + 
					"(1196, \"Bizarra (Bom Jardim)\", 16),\r\n" + 
					"(1197, \"Blumenau\", 24),\r\n" + 
					"(1198, \"Boa �gua (Morada Nova)\", 6),\r\n" + 
					"(1199, \"Boa�u (Jequi�)\", 5),\r\n" + 
					"(1200, \"Boa Esperan�a\", 18),\r\n" + 
					"(1201, \"Boa Esperan�a\", 11),\r\n" + 
					"(1202, \"Boa Esperan�a\", 8),\r\n" + 
					"(1203, \"Boa Esperan�a do Igua�u\", 18),\r\n" + 
					"(1204, \"Boa Esperan�a do Sul\", 26),\r\n" + 
					"(1205, \"Boa Esperan�a (Formiga)\", 11),\r\n" + 
					"(1206, \"Boa Esperan�a (Maracan�)\", 14),\r\n" + 
					"(1207, \"Boa Esperan�a (Rio Bonito)\", 19),\r\n" + 
					"(1208, \"Boa Esperan�a (Rolante)\", 23),\r\n" + 
					"(1209, \"Boa Esperan�a (Santar�m)\", 14),\r\n" + 
					"(1210, \"Boa Esperan�a (Sorriso)\", 13),\r\n" + 
					"(1211, \"Boa Esperan�a (Tamboril)\", 6),\r\n" + 
					"(1212, \"Boa Espera (Santan�polis)\", 5),\r\n" + 
					"(1213, \"Boa Fam�lia (Muria�)\", 11),\r\n" + 
					"(1214, \"Boa F� (Santar�m)\", 14),\r\n" + 
					"(1215, \"Boa Hora\", 17),\r\n" + 
					"(1216, \"Boa Nova\", 5),\r\n" + 
					"(1217, \"Boa Sa�de\", 20),\r\n" + 
					"(1218, \"Boas Novas (Bezerros)\", 16),\r\n" + 
					"(1219, \"Boa Sorte (Cantagalo)\", 19),\r\n" + 
					"(1220, \"Boa Sorte (Palmeira dos �ndios)\", 2),\r\n" + 
					"(1221, \"Boa Sorte (Reden��o)\", 14),\r\n" + 
					"(1222, \"Boa Uni�o (Alagoinhas)\", 5),\r\n" + 
					"(1223, \"Boa Uni�o de Itabirinha (Itabirinha)\", 11),\r\n" + 
					"(1224, \"Boa Uni�o (Rio Branco)\", 13),\r\n" + 
					"(1225, \"Boa Ventura\", 15),\r\n" + 
					"(1226, \"Boa Ventura de S�o Roque\", 18),\r\n" + 
					"(1227, \"Boa Ventura (Itaperuna)\", 19),\r\n" + 
					"(1228, \"Boa Viagem\", 6),\r\n" + 
					"(1229, \"Boa Vista\", 15),\r\n" + 
					"(1230, \"Boa Vista\", 22),\r\n" + 
					"(1231, \"Boa Vista (Arapiraca)\", 2),\r\n" + 
					"(1232, \"Boa Vista (Baturit�)\", 6),\r\n" + 
					"(1233, \"Boa Vista (Campo Largo)\", 18),\r\n" + 
					"(1234, \"Boa Vista da Aparecida\", 18),\r\n" + 
					"(1235, \"Boa Vista das Miss�es\", 23),\r\n" + 
					"(1236, \"Boa Vista de Minas (Nova Serrana)\", 11),\r\n" + 
					"(1237, \"Boa Vista de Pacarana (Espig�o do Oeste)\", 21),\r\n" + 
					"(1238, \"Boa Vista de Santa Maria (Una�)\", 11),\r\n" + 
					"(1239, \"Boa Vista do Buric�\", 23),\r\n" + 
					"(1240, \"Boa Vista do Cadeado\", 23),\r\n" + 
					"(1241, \"Boa Vista do Caxitor� (Irau�uba)\", 6),\r\n" + 
					"(1242, \"Boa Vista do Gurupi\", 10),\r\n" + 
					"(1243, \"Boa Vista do Incra\", 23),\r\n" + 
					"(1244, \"Boa Vista do Iririteua (Curu��)\", 14),\r\n" + 
					"(1245, \"Boa Vista do Lagamar (Ibotirama)\", 5),\r\n" + 
					"(1246, \"Boa Vista do Pindar� (Cajari)\", 10),\r\n" + 
					"(1247, \"Boa Vista do Ramos\", 3),\r\n" + 
					"(1248, \"Boa Vista dos Andradas (�lvares Florence)\", 26),\r\n" + 
					"(1249, \"Boa Vista do Sul\", 23),\r\n" + 
					"(1250, \"Boa Vista do Tupim\", 5),\r\n" + 
					"(1251, \"Boa Vista (Momba�a)\", 6),\r\n" + 
					"(1252, \"Boa Vista (Palmeira dos �ndios)\", 2),\r\n" + 
					"(1253, \"Boa Vista (Paraipaba)\", 6),\r\n" + 
					"(1254, \"Boa Vista (Pato Branco)\", 18),\r\n" + 
					"(1255, \"Boa Vista (Ponta Grossa)\", 18),\r\n" + 
					"(1256, \"Boa Vista (Rondon�polis)\", 13),\r\n" + 
					"(1257, \"Boa Vista (Salvador do Sul)\", 23),\r\n" + 
					"(1258, \"Boa Vista (Santa Cruz do Sul)\", 23),\r\n" + 
					"(1259, \"Boa Vista (S�o Francisco de Assis)\", 23),\r\n" + 
					"(1260, \"Boa Vista (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(1261, \"Boa Vista (Toledo)\", 18),\r\n" + 
					"(1262, \"Boca da Mata\", 2),\r\n" + 
					"(1263, \"Boca do Acre\", 3),\r\n" + 
					"(1264, \"Boca do C�rrego (Belmonte)\", 5),\r\n" + 
					"(1265, \"Boca do Jari (Laranjal do Jari)\", 4),\r\n" + 
					"(1266, \"Boca do Monte (Santa Maria)\", 23),\r\n" + 
					"(1267, \"Bocaina\", 17),\r\n" + 
					"(1268, \"Bocaina\", 26),\r\n" + 
					"(1269, \"Bocaina de Minas\", 11),\r\n" + 
					"(1270, \"Boca�na do Sul\", 24),\r\n" + 
					"(1271, \"Bocaina (Ponta Grossa)\", 18),\r\n" + 
					"(1272, \"Bocai�va\", 11),\r\n" + 
					"(1273, \"Bocai�va do Sul\", 18),\r\n" + 
					"(1274, \"Bocaiuval (Porto Esperidi�o)\", 13),\r\n" + 
					"(1275, \"Bocaj� (Douradina)\", 12),\r\n" + 
					"(1276, \"Bocaj� (Laguna Carap�)\", 12),\r\n" + 
					"(1277, \"Bod�\", 20),\r\n" + 
					"(1278, \"Bodoc�\", 16),\r\n" + 
					"(1279, \"Bodoquena\", 12),\r\n" + 
					"(1280, \"Bo�mios (Nova Petr�polis)\", 23),\r\n" + 
					"(1281, \"Bofete\", 26),\r\n" + 
					"(1282, \"Boim (Santar�m)\", 14),\r\n" + 
					"(1283, \"Boi Preto (Chapada)\", 23),\r\n" + 
					"(1284, \"Boiteuxburgo (Major Gercino)\", 24),\r\n" + 
					"(1285, \"Boituva\", 26),\r\n" + 
					"(1286, \"Bojuru (S�o Jos� do Norte)\", 23),\r\n" + 
					"(1287, \"Bombinhas\", 24),\r\n" + 
					"(1288, \"Bom Conselho\", 16),\r\n" + 
					"(1289, \"Bom Despacho\", 11),\r\n" + 
					"(1290, \"Bom Fim do Bom Jesus (Cabre�va)\", 26),\r\n" + 
					"(1291, \"Bom Fim (Jaraguari)\", 12),\r\n" + 
					"(1292, \"Bom Futuro (Ariquemes)\", 21),\r\n" + 
					"(1293, \"Bom Jardim\", 19),\r\n" + 
					"(1294, \"Bom Jardim\", 10),\r\n" + 
					"(1295, \"Bom Jardim\", 16),\r\n" + 
					"(1296, \"Bom Jardim (Arapiraca)\", 2),\r\n" + 
					"(1297, \"Bom Jardim (Ca�apava do Sul)\", 23),\r\n" + 
					"(1298, \"Bom Jardim da Serra\", 24),\r\n" + 
					"(1299, \"Bom Jardim de Goi�s\", 9),\r\n" + 
					"(1300, \"Bom Jardim de Minas\", 11),\r\n" + 
					"(1301, \"Bom Jardim do Sul (Iva�)\", 18),\r\n" + 
					"(1302, \"Bom Jardim (Esperan�a do Sul)\", 23),\r\n" + 
					"(1303, \"Bom Jardim (Maracan�)\", 14),\r\n" + 
					"(1304, \"Bom Jesus\", 20),\r\n" + 
					"(1305, \"Bom Jesus\", 23),\r\n" + 
					"(1306, \"Bom Jesus\", 24),\r\n" + 
					"(1307, \"Bom Jesus\", 15),\r\n" + 
					"(1308, \"Bom Jesus\", 9),\r\n" + 
					"(1309, \"Bom Jesus\", 17),\r\n" + 
					"(1310, \"Bom Jesus da Cachoeira (Muria�)\", 11),\r\n" + 
					"(1311, \"Bom Jesus da Lapa\", 5),\r\n" + 
					"(1312, \"Bom Jesus da Penha\", 11),\r\n" + 
					"(1313, \"Bom Jesus da Serra\", 5),\r\n" + 
					"(1314, \"Bom Jesus das Selvas\", 10),\r\n" + 
					"(1315, \"Bom Jesus de Angicos (Carmo do Cajuru)\", 11),\r\n" + 
					"(1316, \"Bom Jesus de Cardosos (Uruc�nia)\", 11),\r\n" + 
					"(1317, \"Bom Jesus do Amparo\", 11),\r\n" + 
					"(1318, \"Bom Jesus do Araguaia\", 13),\r\n" + 
					"(1319, \"Bom Jesus do Divino (Divino)\", 11),\r\n" + 
					"(1320, \"Bom Jesus do Galho\", 11),\r\n" + 
					"(1321, \"Bom Jesus do Itabapoana\", 19),\r\n" + 
					"(1322, \"Bom Jesus do Madeira (Fervedouro)\", 11),\r\n" + 
					"(1323, \"Bom Jesus do Norte\", 8),\r\n" + 
					"(1324, \"Bom Jesus do Oeste\", 24),\r\n" + 
					"(1325, \"Bom Jesus do Prata (Frei Inoc�ncio)\", 11),\r\n" + 
					"(1326, \"Bom Jesus do Querendo (Natividade)\", 19),\r\n" + 
					"(1327, \"Bom Jesus dos Perd�es\", 26),\r\n" + 
					"(1328, \"Bom Jesus do Sul\", 18),\r\n" + 
					"(1329, \"Bom Jesus do Tocantins\", 27),\r\n" + 
					"(1330, \"Bom Jesus do Tocantins\", 14),\r\n" + 
					"(1331, \"Bom Jesus (Jaru)\", 21),\r\n" + 
					"(1332, \"Bom Lugar\", 10),\r\n" + 
					"(1333, \"Bom Nome (S�o Jos� do Belmonte)\", 16),\r\n" + 
					"(1334, \"Bom Pastor (Resplendor)\", 11),\r\n" + 
					"(1335, \"Bom Plano (Vista Ga�cha)\", 23),\r\n" + 
					"(1336, \"Bom Princ�pio\", 23),\r\n" + 
					"(1337, \"Bom Princ�pio (Caucaia)\", 6),\r\n" + 
					"(1338, \"Bom Princ�pio do Piau�\", 17),\r\n" + 
					"(1339, \"Bom Progresso\", 23),\r\n" + 
					"(1340, \"Bom Progresso (Sab�udia)\", 18),\r\n" + 
					"(1341, \"Bom Recreio (Passo Fundo)\", 23),\r\n" + 
					"(1342, \"Bom Repouso\", 11),\r\n" + 
					"(1343, \"Bom Retiro\", 24),\r\n" + 
					"(1344, \"Bom Retiro (Cambar� do Sul)\", 23),\r\n" + 
					"(1345, \"Bom Retiro (Cascavel)\", 18),\r\n" + 
					"(1346, \"Bom Retiro da Esperan�a (Angatuba)\", 26),\r\n" + 
					"(1348, \"Bom Retiro do Sul\", 23),\r\n" + 
					"(1349, \"Bom Retiro (Guarapuava)\", 18),\r\n" + 
					"(1350, \"Bom Retiro (Muitos Cap�es)\", 23),\r\n" + 
					"(1351, \"Bom Retiro (Pato Branco)\", 18),\r\n" + 
					"(1352, \"Bom Retiro (Pinh�o)\", 18),\r\n" + 
					"(1353, \"Bom Retiro (Santa B�rbara do Tug�rio)\", 11),\r\n" + 
					"(1354, \"Bom Ser� (Cristal)\", 23),\r\n" + 
					"(1355, \"Bom Sossego (Oliveira dos Brejinhos)\", 5),\r\n" + 
					"(1356, \"Bom Sucesso\", 15),\r\n" + 
					"(1357, \"Bom Sucesso\", 18),\r\n" + 
					"(1358, \"Bom Sucesso\", 11),\r\n" + 
					"(1359, \"Bom Sucesso de Itarar�\", 26),\r\n" + 
					"(1360, \"Bom Sucesso de Patos (Patos de Minas)\", 11),\r\n" + 
					"(1361, \"Bom Sucesso do Sul\", 18),\r\n" + 
					"(1362, \"Bom Sucesso (Guarapuava)\", 18),\r\n" + 
					"(1363, \"Bom Sucesso (Soledade)\", 15),\r\n" + 
					"(1364, \"Bom Sucesso (V�rzea Grande)\", 13),\r\n" + 
					"(1365, \"Bom Sucesso (Videira)\", 24),\r\n" + 
					"(1366, \"Bonan�a (Ibiracatu)\", 11),\r\n" + 
					"(1367, \"Bonfim\", 11),\r\n" + 
					"(1368, \"Bonfim\", 22),\r\n" + 
					"(1369, \"Bonfim da Feira (Feira de Santana)\", 5),\r\n" + 
					"(1370, \"Bonfim do Arari (Arari)\", 10),\r\n" + 
					"(1371, \"Bonfim do Piau�\", 17),\r\n" + 
					"(1372, \"Bonfim Paulista (Ribeir�o Preto)\", 26),\r\n" + 
					"(1373, \"Bonfim (S�o Jos� do Egito)\", 16),\r\n" + 
					"(1374, \"Bonfim (Senador Pompeu)\", 6),\r\n" + 
					"(1375, \"Bonfim (Sobral)\", 6),\r\n" + 
					"(1376, \"Bonfin�polis\", 9),\r\n" + 
					"(1377, \"Bonfin�polis de Minas\", 11),\r\n" + 
					"(1378, \"Bonhu (Russas)\", 6),\r\n" + 
					"(1379, \"Bonif�cio (Palmeira dos �ndios)\", 2),\r\n" + 
					"(1380, \"Boninal\", 5),\r\n" + 
					"(1381, \"Bonito\", 14),\r\n" + 
					"(1382, \"Bonito\", 12),\r\n" + 
					"(1383, \"Bonito\", 5),\r\n" + 
					"(1384, \"Bonito\", 16),\r\n" + 
					"(1385, \"Bonito (Camaqu�)\", 23),\r\n" + 
					"(1386, \"Bonito (Canind�)\", 6),\r\n" + 
					"(1387, \"Bonito de Minas\", 11),\r\n" + 
					"(1388, \"Bonito de Santa F�\", 15),\r\n" + 
					"(1389, \"Bon�polis\", 9),\r\n" + 
					"(1390, \"Bonsucesso (Apiac�)\", 8),\r\n" + 
					"(1391, \"Bonsucesso (Coruripe)\", 2),\r\n" + 
					"(1392, \"Boqueir�o\", 15),\r\n" + 
					"(1393, \"Boqueir�o (Boa Viagem)\", 6),\r\n" + 
					"(1394, \"Boqueir�o do Le�o\", 23),\r\n" + 
					"(1395, \"Boqueir�o do Piau�\", 17),\r\n" + 
					"(1396, \"Boqueir�o (General C�mara)\", 23),\r\n" + 
					"(1397, \"Boqueir�o (Japaratinga)\", 2),\r\n" + 
					"(1398, \"Boqueir�o (Jardim)\", 12),\r\n" + 
					"(1399, \"Boqueir�o (Lagoa Vermelha)\", 23),\r\n" + 
					"(1400, \"Boquim\", 25),\r\n" + 
					"(1401, \"Boquira\", 5),\r\n" + 
					"(1402, \"Bor�\", 26),\r\n" + 
					"(1403, \"Borac�ia\", 26),\r\n" + 
					"(1404, \"Borba\", 3),\r\n" + 
					"(1405, \"Borba Gato (Ferros)\", 11),\r\n" + 
					"(1406, \"Borborema\", 15),\r\n" + 
					"(1407, \"Borborema\", 26),\r\n" + 
					"(1408, \"Borda da Mata\", 11),\r\n" + 
					"(1409, \"Borda do Campo (Quatro Barras)\", 18),\r\n" + 
					"(1410, \"Borebi\", 26),\r\n" + 
					"(1411, \"Borges (Jaguaruana)\", 6),\r\n" + 
					"(1412, \"Borman (Guarania�u)\", 18),\r\n" + 
					"(1413, \"Boror� (Ma�ambar�)\", 23),\r\n" + 
					"(1414, \"Borraz�polis\", 18),\r\n" + 
					"(1415, \"Borussia (Os�rio)\", 23),\r\n" + 
					"(1416, \"Bossoroca\", 23),\r\n" + 
					"(1417, \"Botafogo (Coruripe)\", 2),\r\n" + 
					"(1418, \"Bote (Herval)\", 23),\r\n" + 
					"(1419, \"Botelhos\", 11),\r\n" + 
					"(1420, \"Botelho (Santa Ad�lia)\", 26),\r\n" + 
					"(1421, \"Botucara� (Candel�ria)\", 23),\r\n" + 
					"(1422, \"Botucatu\", 26),\r\n" + 
					"(1423, \"Botumirim\", 11),\r\n" + 
					"(1424, \"Botupor�\", 5),\r\n" + 
					"(1425, \"Botuquara (Ponta Grossa)\", 18),\r\n" + 
					"(1426, \"Botuquara (Riacho de Santana)\", 5),\r\n" + 
					"(1427, \"Botuver�\", 24),\r\n" + 
					"(1428, \"Bourbonia (Barbosa Ferraz)\", 18),\r\n" + 
					"(1429, \"Bozano\", 23),\r\n" + 
					"(1430, \"Bra�o do Norte\", 24),\r\n" + 
					"(1431, \"Bra�o do Rio (Concei��o da Barra)\", 8),\r\n" + 
					"(1432, \"Bra�o do Trombudo\", 24),\r\n" + 
					"(1433, \"Bra�o (Eldorado)\", 26),\r\n" + 
					"(1434, \"Bra�o Forte (Tenente Portela)\", 23),\r\n" + 
					"(1435, \"Braga\", 23),\r\n" + 
					"(1436, \"Bragan�a\", 14),\r\n" + 
					"(1437, \"Bragan�a Paulista\", 26),\r\n" + 
					"(1438, \"Braganey\", 18),\r\n" + 
					"(1439, \"Bragantina (Assis Chateaubriand)\", 18),\r\n" + 
					"(1440, \"Branquinha\", 2),\r\n" + 
					"(1441, \"Brasil�ndia\", 12),\r\n" + 
					"(1442, \"Brasil�ndia (Aragua�na)\", 27),\r\n" + 
					"(1443, \"Brasil�ndia de Minas\", 11),\r\n" + 
					"(1444, \"Brasil�ndia do Sul\", 18),\r\n" + 
					"(1445, \"Brasil�ndia do Tocantins\", 27),\r\n" + 
					"(1446, \"Brasil�ia\", 1),\r\n" + 
					"(1447, \"Brasileira\", 17),\r\n" + 
					"(1448, \"Bras�lia\", 7),\r\n" + 
					"(1449, \"Bras�lia de Minas\", 11),\r\n" + 
					"(1450, \"Bras�lia Legal (Aveiro)\", 14),\r\n" + 
					"(1451, \"Brasil Novo\", 14),\r\n" + 
					"(1452, \"Brasit�nia (Fernand�polis)\", 26),\r\n" + 
					"(1453, \"Brasnorte\", 13),\r\n" + 
					"(1454, \"Bras�polis\", 11),\r\n" + 
					"(1455, \"Br�s Pires\", 11),\r\n" + 
					"(1456, \"Bra�na\", 26),\r\n" + 
					"(1457, \"Bra�nas\", 11),\r\n" + 
					"(1458, \"Brazabrantes\", 9),\r\n" + 
					"(1459, \"Brej�o\", 16),\r\n" + 
					"(1460, \"Breja�ba (Concei��o do Mato Dentro)\", 11),\r\n" + 
					"(1461, \"Brejaubinha (Governador Valadares)\", 11),\r\n" + 
					"(1462, \"Brejetuba\", 8),\r\n" + 
					"(1463, \"Brejinho\", 20),\r\n" + 
					"(1464, \"Brejinho\", 16),\r\n" + 
					"(1465, \"Brejinho (Araripe)\", 6),\r\n" + 
					"(1466, \"Brejinho das Ametistas (Caetit�)\", 5),\r\n" + 
					"(1467, \"Brejinho de Nazar�\", 27),\r\n" + 
					"(1468, \"Brejo\", 10),\r\n" + 
					"(1469, \"Brejo Alegre\", 26),\r\n" + 
					"(1470, \"Brejo Bonito (Cruzeiro da Fortaleza)\", 11),\r\n" + 
					"(1471, \"Brejo da Madre de Deus\", 16),\r\n" + 
					"(1472, \"Brejo da Serra (Pil�o Arcado)\", 5),\r\n" + 
					"(1473, \"Brejo de Areia\", 10),\r\n" + 
					"(1474, \"Brejo de S�o F�lix (Parnarama)\", 10),\r\n" + 
					"(1475, \"Brejo do Amparo (Janu�ria)\", 11),\r\n" + 
					"(1476, \"Brejo do Cruz\", 15),\r\n" + 
					"(1477, \"Brejo do Meio (Marab�)\", 14),\r\n" + 
					"(1478, \"Brejo do Piau�\", 17),\r\n" + 
					"(1479, \"Brejo dos Santos\", 15),\r\n" + 
					"(1480, \"Brej�es\", 5),\r\n" + 
					"(1481, \"Brejo Grande\", 25),\r\n" + 
					"(1482, \"Brejo Grande do Araguaia\", 14),\r\n" + 
					"(1483, \"Brejo Grande (Santana do Cariri)\", 6),\r\n" + 
					"(1484, \"Brejol�ndia\", 5),\r\n" + 
					"(1485, \"Brejo Lu�za de Brito (Novo Horizonte)\", 5),\r\n" + 
					"(1486, \"Brejo Novo (Boa Vista do Tupim)\", 5),\r\n" + 
					"(1487, \"Brejo Santo\", 6),\r\n" + 
					"(1488, \"Breu Branco\", 14),\r\n" + 
					"(1489, \"Breves\", 14),\r\n" + 
					"(1490, \"Brianorte (Nova Maring�)\", 13),\r\n" + 
					"(1491, \"Brit�nia\", 9),\r\n" + 
					"(1492, \"Brochier\", 23),\r\n" + 
					"(1493, \"Brodowski\", 26),\r\n" + 
					"(1494, \"Brotas\", 26),\r\n" + 
					"(1495, \"Brotas de Maca�bas\", 5),\r\n" + 
					"(1496, \"Brotas (Mira�ma)\", 6),\r\n" + 
					"(1497, \"Brugnarotto (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(1498, \"Brumadinho\", 11),\r\n" + 
					"(1499, \"Brumado\", 5),\r\n" + 
					"(1500, \"Brumal (Santa B�rbara)\", 11),\r\n" + 
					"(1501, \"Brun�polis\", 24),\r\n" + 
					"(1502, \"Brusque\", 24),\r\n" + 
					"(1503, \"Buarque de Macedo (Conselheiro Lafaiete)\", 11),\r\n" + 
					"(1504, \"Bucuituba (Boquira)\", 5),\r\n" + 
					"(1505, \"Bueno Brand�o\", 11),\r\n" + 
					"(1506, \"Bueno (Conselheiro Pena)\", 11),\r\n" + 
					"(1507, \"Bueno de Andrada (Araraquara)\", 26),\r\n" + 
					"(1508, \"Buenol�ndia (Goi�s)\", 9),\r\n" + 
					"(1509, \"Buen�polis\", 11),\r\n" + 
					"(1510, \"Buenos Aires\", 16),\r\n" + 
					"(1511, \"Buerarema\", 5),\r\n" + 
					"(1512, \"Bugre\", 11),\r\n" + 
					"(1513, \"Bugre (Balsa Nova)\", 18),\r\n" + 
					"(1514, \"Bugre (Soledade)\", 23),\r\n" + 
					"(1515, \"Bu�que\", 16),\r\n" + 
					"(1516, \"Bujari\", 1),\r\n" + 
					"(1517, \"Bujaru\", 14),\r\n" + 
					"(1518, \"Bulc�o (Castro)\", 18),\r\n" + 
					"(1519, \"Buracica (Teodoro Sampaio)\", 5),\r\n" + 
					"(1520, \"Buranh�m (Guaratinga)\", 5),\r\n" + 
					"(1521, \"Burarama (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(1522, \"Buri\", 26),\r\n" + 
					"(1523, \"Buril (Cris�polis)\", 5),\r\n" + 
					"(1524, \"Buris de Abrantes (Cama�ari)\", 5),\r\n" + 
					"(1525, \"Buritama\", 26),\r\n" + 
					"(1526, \"Buriti\", 10),\r\n" + 
					"(1527, \"Buriti Alegre\", 9),\r\n" + 
					"(1528, \"Buriti (Alto Araguaia)\", 13),\r\n" + 
					"(1529, \"Buriti Bravo\", 10),\r\n" + 
					"(1530, \"Buriticupu\", 10),\r\n" + 
					"(1531, \"Buriti de Goi�s\", 9),\r\n" + 
					"(1532, \"Buriti dos Lopes\", 17),\r\n" + 
					"(1533, \"Buriti dos Montes\", 17),\r\n" + 
					"(1534, \"Buriti do Tocantins\", 27),\r\n" + 
					"(1535, \"Buriti (Guiratinga)\", 13),\r\n" + 
					"(1536, \"Buritin�polis\", 9),\r\n" + 
					"(1537, \"Buritirama\", 5),\r\n" + 
					"(1538, \"Buritirana\", 10),\r\n" + 
					"(1539, \"Buritis\", 11),\r\n" + 
					"(1540, \"Buritis\", 21),\r\n" + 
					"(1541, \"Buriti (Santo �ngelo)\", 23),\r\n" + 
					"(1542, \"Buritizal\", 26),\r\n" + 
					"(1543, \"Buritizal (Poranga)\", 6),\r\n" + 
					"(1544, \"Buritizeiro\", 11),\r\n" + 
					"(1545, \"Buritizinho (Mauriti)\", 6),\r\n" + 
					"(1546, \"Buti�\", 23),\r\n" + 
					"(1547, \"Buti�s (Tavares)\", 23),\r\n" + 
					"(1548, \"Butiatuva (Palmares do Sul)\", 23),\r\n" + 
					"(1549, \"Caapiranga\", 3),\r\n" + 
					"(1550, \"Caapor�\", 15),\r\n" + 
					"(1551, \"Caarap�\", 12),\r\n" + 
					"(1552, \"Caatiba\", 5),\r\n" + 
					"(1553, \"Caatinga (Jo�o Pinheiro)\", 11),\r\n" + 
					"(1554, \"Cabaceiras\", 15),\r\n" + 
					"(1555, \"Cabaceiras do Paragua�u\", 5),\r\n" + 
					"(1556, \"Cabanas (Cachoeirinha)\", 16),\r\n" + 
					"(1557, \"Cabeceira do Ap� (Ponta Por�)\", 12),\r\n" + 
					"(1558, \"Cabeceira do Jib�ia (Vit�ria da Conquista)\", 5),\r\n" + 
					"(1559, \"Cabeceira Grande\", 11),\r\n" + 
					"(1560, \"Cabeceiras\", 9),\r\n" + 
					"(1561, \"Cabeceiras do Piau�\", 17),\r\n" + 
					"(1562, \"Cabedelo\", 15),\r\n" + 
					"(1563, \"Cabixi\", 21),\r\n" + 
					"(1564, \"Cabo de Santo Agostinho\", 16),\r\n" + 
					"(1565, \"Cabo Frio\", 19),\r\n" + 
					"(1566, \"Cab�to (Candeias)\", 5),\r\n" + 
					"(1567, \"Cabo Verde\", 11),\r\n" + 
					"(1568, \"Cabr�lia Paulista\", 26),\r\n" + 
					"(1569, \"Cabr�lia (Piat�)\", 5),\r\n" + 
					"(1570, \"Cabreiro (Aracati)\", 6),\r\n" + 
					"(1571, \"Cabre�va\", 26),\r\n" + 
					"(1572, \"Cabrito (Paranava�)\", 18),\r\n" + 
					"(1573, \"Cabrob�\", 16),\r\n" + 
					"(1574, \"Caburu (S�o Jo�o Del Rei)\", 11),\r\n" + 
					"(1575, \"Ca�ador\", 24),\r\n" + 
					"(1576, \"Ca�apava\", 26),\r\n" + 
					"(1577, \"Ca�apava do Sul\", 23),\r\n" + 
					"(1578, \"Ca�aratiba (Turmalina)\", 11),\r\n" + 
					"(1579, \"Ca�arema (Capit�o En�as)\", 11),\r\n" + 
					"(1580, \"Cacatu (Antonina)\", 18),\r\n" + 
					"(1581, \"Cacaul�ndia\", 21),\r\n" + 
					"(1582, \"Cacau Pir�ra (Iranduba)\", 3),\r\n" + 
					"(1583, \"Cacequi\", 23),\r\n" + 
					"(1584, \"C�ceres\", 13),\r\n" + 
					"(1585, \"Cacha Pregos (Vera Cruz)\", 5),\r\n" + 
					"(1586, \"Cachoeira\", 5),\r\n" + 
					"(1587, \"Cachoeira Alegre (Bar�o de Monte Alto)\", 11),\r\n" + 
					"(1588, \"Cachoeira Alta\", 9),\r\n" + 
					"(1589, \"Cachoeira (Cascavel)\", 18),\r\n" + 
					"(1590, \"Cachoeira da Prata\", 11),\r\n" + 
					"(1591, \"Cachoeira da Serra (Altamira)\", 14),\r\n" + 
					"(1592, \"Cachoeira de Cima (Antonina)\", 18),\r\n" + 
					"(1593, \"Cachoeira de Emas (Pirassununga)\", 26),\r\n" + 
					"(1594, \"Cachoeira de F�tima (Praia Grande)\", 24),\r\n" + 
					"(1595, \"Cachoeira de Goi�s\", 9),\r\n" + 
					"(1596, \"Cachoeira de Minas\", 11),\r\n" + 
					"(1597, \"Cachoeira de Paje�\", 11),\r\n" + 
					"(1598, \"Cachoeira de Santa Cruz (Vi�osa)\", 11),\r\n" + 
					"(1599, \"Cachoeira de S�o Jos� (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(1600, \"Cachoeira do Aran� (Frei Gaspar)\", 11),\r\n" + 
					"(1601, \"Cachoeira do Arari\", 14),\r\n" + 
					"(1602, \"Cachoeira do Brumado (Mariana)\", 11),\r\n" + 
					"(1603, \"Cachoeira do Campo (Ouro Preto)\", 11),\r\n" + 
					"(1604, \"Cachoeira do Esp�rito Santo (Ribeir�o Claro)\", 18),\r\n" + 
					"(1605, \"Cachoeira do Manteiga (Buritizeiro)\", 11),\r\n" + 
					"(1606, \"Cachoeira do Mato (Teixeira de Freitas)\", 5),\r\n" + 
					"(1607, \"Cachoeira do Pinto (Bom Conselho)\", 16),\r\n" + 
					"(1608, \"Cachoeira do Piri�\", 14),\r\n" + 
					"(1609, \"Cachoeira do Roberto (Afr�nio)\", 16),\r\n" + 
					"(1610, \"Cachoeira dos Antunes (Rio Manso)\", 11),\r\n" + 
					"(1611, \"Cachoeira dos �ndios\", 15),\r\n" + 
					"(1612, \"Cachoeira do Sul\", 23),\r\n" + 
					"(1613, \"Cachoeira Dourada\", 11),\r\n" + 
					"(1614, \"Cachoeira Dourada\", 9),\r\n" + 
					"(1615, \"Cachoeira Grande\", 10),\r\n" + 
					"(1616, \"Cachoeira Grande (Poranga)\", 6),\r\n" + 
					"(1617, \"Cachoeira (Guarabira)\", 15),\r\n" + 
					"(1618, \"Cachoeira (Itatira)\", 6),\r\n" + 
					"(1619, \"Cachoeira (Maranguape)\", 6),\r\n" + 
					"(1620, \"Cachoeira (Parana�ba)\", 12),\r\n" + 
					"(1621, \"Cachoeira Paulista\", 26),\r\n" + 
					"(1622, \"Cachoeiras de Macacu\", 19),\r\n" + 
					"(1623, \"Cachoeira Seca (Caruaru)\", 16),\r\n" + 
					"(1624, \"Cachoeirinha\", 27),\r\n" + 
					"(1625, \"Cachoeirinha\", 23),\r\n" + 
					"(1626, \"Cachoeirinha\", 16),\r\n" + 
					"(1627, \"Cachoeirinha (C�rrego Danta)\", 11),\r\n" + 
					"(1628, \"Cachoeirinha de Ita�na (Barra de S�o Francisco)\", 8),\r\n" + 
					"(1629, \"Cachoeirinha (Ibiara)\", 15),\r\n" + 
					"(1630, \"Cachoeirinha (Pato Branco)\", 18),\r\n" + 
					"(1631, \"Cachoeiro de Itapemirim\", 8),\r\n" + 
					"(1632, \"Cachoeiros (Maca�)\", 19),\r\n" + 
					"(1633, \"Cacimba de Areia\", 15),\r\n" + 
					"(1634, \"Cacimba de Dentro\", 15),\r\n" + 
					"(1635, \"Cacimbas\", 15),\r\n" + 
					"(1636, \"Cacimbas (Carir�)\", 6),\r\n" + 
					"(1637, \"Cacimbinhas\", 2),\r\n" + 
					"(1638, \"Cacique Doble\", 23),\r\n" + 
					"(1639, \"Cacoal\", 21),\r\n" + 
					"(1640, \"Caconde\", 26),\r\n" + 
					"(1641, \"Ca�u\", 9),\r\n" + 
					"(1642, \"Cacul�\", 5),\r\n" + 
					"(1643, \"Cadeadinho (Irati)\", 18),\r\n" + 
					"(1644, \"Cadorna (Nova Alvorada)\", 23),\r\n" + 
					"(1645, \"Ca�m\", 5),\r\n" + 
					"(1646, \"Caetano Lopes (Jeceaba)\", 11),\r\n" + 
					"(1647, \"Caetano Mendes (Tibagi)\", 18),\r\n" + 
					"(1648, \"Caetan�polis\", 11),\r\n" + 
					"(1649, \"Caetanos\", 5),\r\n" + 
					"(1650, \"Caet�\", 11),\r\n" + 
					"(1651, \"Caet�-A�u (Palmeiras)\", 5),\r\n" + 
					"(1652, \"Caet� (Juiz de Fora)\", 11),\r\n" + 
					"(1653, \"Caet�s\", 16),\r\n" + 
					"(1654, \"Caetit�\", 5),\r\n" + 
					"(1655, \"Cafarnaum\", 5),\r\n" + 
					"(1656, \"Caf� (Alegre)\", 8),\r\n" + 
					"(1657, \"Cafeara\", 18),\r\n" + 
					"(1658, \"Cafeeiros (Cruzeiro do Oeste)\", 18),\r\n" + 
					"(1659, \"Cafel�ndia\", 18),\r\n" + 
					"(1660, \"Cafel�ndia\", 26),\r\n" + 
					"(1661, \"Cafemirim (Tarumirim)\", 11),\r\n" + 
					"(1662, \"Cafes�polis (Cafel�ndia)\", 26),\r\n" + 
					"(1663, \"Cafezal do Sul\", 18),\r\n" + 
					"(1664, \"Cafezal (Magalh�es Barata)\", 14),\r\n" + 
					"(1665, \"Caiabu\", 26),\r\n" + 
					"(1666, \"Caiamb� (Tef�)\", 3),\r\n" + 
					"(1667, \"Caiana\", 11),\r\n" + 
					"(1668, \"Caiap�nia\", 9),\r\n" + 
					"(1669, \"Caiap� (Pirapetinga)\", 11),\r\n" + 
					"(1670, \"Caibat�\", 23),\r\n" + 
					"(1671, \"Caibi\", 24),\r\n" + 
					"(1672, \"Cai�ara\", 15),\r\n" + 
					"(1673, \"Cai�ara\", 23),\r\n" + 
					"(1674, \"Cai�ara (Canind�)\", 6),\r\n" + 
					"(1675, \"Cai�ara (Cruz)\", 6),\r\n" + 
					"(1676, \"Cai�ara do Norte\", 20),\r\n" + 
					"(1677, \"Cai�ara do Rio do Vento\", 20),\r\n" + 
					"(1678, \"Cai�ara (Faina)\", 9),\r\n" + 
					"(1679, \"Cai�ara (Vit�ria da Conquista)\", 5),\r\n" + 
					"(1680, \"Cai�arinha (Chor�)\", 6),\r\n" + 
					"(1681, \"Cai�arinha da Penha (Serra Talhada)\", 16),\r\n" + 
					"(1682, \"Caic�\", 20),\r\n" + 
					"(1683, \"Caieiras\", 26),\r\n" + 
					"(1684, \"Caimb� (Euclides da Cunha)\", 5),\r\n" + 
					"(1685, \"Caioca (Sobral)\", 6),\r\n" + 
					"(1686, \"Caio Prado (Itapi�na)\", 6),\r\n" + 
					"(1687, \"Caipu (Cari�s)\", 6),\r\n" + 
					"(1688, \"Cairari (Moju)\", 14),\r\n" + 
					"(1689, \"Cairu\", 5),\r\n" + 
					"(1690, \"Cait� (S�o Mateus do Sul)\", 18),\r\n" + 
					"(1691, \"Caite (Santo Ant�nio do Leverger)\", 13),\r\n" + 
					"(1692, \"Caiu�\", 26),\r\n" + 
					"(1693, \"Caiubi (Itapebi)\", 5),\r\n" + 
					"(1694, \"Caiva (Campo Largo)\", 18),\r\n" + 
					"(1695, \"Cajamar\", 26),\r\n" + 
					"(1696, \"Cajapi�\", 10),\r\n" + 
					"(1697, \"Cajarana (Arapiraca)\", 2),\r\n" + 
					"(1698, \"Cajari\", 10),\r\n" + 
					"(1699, \"Cajati\", 26),\r\n" + 
					"(1700, \"Cajazeiras\", 15),\r\n" + 
					"(1701, \"Cajazeiras (Bezerros)\", 16),\r\n" + 
					"(1702, \"Cajazeiras do Piau�\", 17),\r\n" + 
					"(1703, \"Cajazeirinhas\", 15),\r\n" + 
					"(1704, \"Cajobi\", 26),\r\n" + 
					"(1705, \"Cajueiro\", 2),\r\n" + 
					"(1706, \"Cajueiro (Barreira)\", 6),\r\n" + 
					"(1707, \"Cajueiro da Praia\", 17),\r\n" + 
					"(1708, \"Caju� (Sento S�)\", 5),\r\n" + 
					"(1709, \"Cajuri\", 11),\r\n" + 
					"(1710, \"Cajuru\", 26),\r\n" + 
					"(1711, \"Caju (S�o Miguel do Guam�)\", 14),\r\n" + 
					"(1712, \"Calaba�a (V�rzea Alegre)\", 6),\r\n" + 
					"(1713, \"Calama (Porto Velho)\", 21),\r\n" + 
					"(1714, \"Cal�ado\", 16),\r\n" + 
					"(1715, \"Calcil�ndia (Goi�s)\", 9),\r\n" + 
					"(1716, \"Cal�oene\", 4),\r\n" + 
					"(1717, \"Caldas\", 11),\r\n" + 
					"(1718, \"Caldas Brand�o\", 15),\r\n" + 
					"(1719, \"Caldas do Jorro (Tucano)\", 5),\r\n" + 
					"(1720, \"Caldas Novas\", 9),\r\n" + 
					"(1721, \"Caldazinha\", 9),\r\n" + 
					"(1722, \"Caldeir�o (Brejo da Madre de Deus)\", 16),\r\n" + 
					"(1723, \"Caldeir�o Grande\", 5),\r\n" + 
					"(1724, \"Caldeir�o Grande do Piau�\", 17),\r\n" + 
					"(1725, \"Caldeir�o (Salitre)\", 6),\r\n" + 
					"(1726, \"Caldeir�o (Uau�)\", 5),\r\n" + 
					"(1727, \"Caldeiras (Caetit�)\", 5),\r\n" + 
					"(1728, \"Caldeir�es (Bom Conselho)\", 16),\r\n" + 
					"(1729, \"Caldeir�es de Cima (Palmeira dos �ndios)\", 2),\r\n" + 
					"(1730, \"Calheiros (Bom Jesus do Itabapoana)\", 19),\r\n" + 
					"(1731, \"Calif�rnia\", 18),\r\n" + 
					"(1732, \"Calif�rnia da Barra (Barra do Pira�)\", 19),\r\n" + 
					"(1733, \"Calif�rnia (Quixad�)\", 6),\r\n" + 
					"(1734, \"Calixto (An�polis)\", 9),\r\n" + 
					"(1735, \"Calixto (Resplendor)\", 11),\r\n" + 
					"(1736, \"Calmon\", 24),\r\n" + 
					"(1737, \"Cal�geras (Arapoti)\", 18),\r\n" + 
					"(1738, \"Calumbi\", 16),\r\n" + 
					"(1739, \"Camacan\", 5),\r\n" + 
					"(1740, \"Cama�ari\", 5),\r\n" + 
					"(1741, \"Camacho\", 11),\r\n" + 
					"(1742, \"Camadanta (Arapiraca)\", 2),\r\n" + 
					"(1743, \"Camala�\", 15),\r\n" + 
					"(1744, \"Camamu\", 5),\r\n" + 
					"(1745, \"Camanducaia\", 11),\r\n" + 
					"(1746, \"Camapu�\", 12),\r\n" + 
					"(1747, \"Camaqu�\", 23),\r\n" + 
					"(1748, \"Camar� (Aquiraz)\", 6),\r\n" + 
					"(1749, \"C�mara do Maraj� (Cachoeira do Arari)\", 14),\r\n" + 
					"(1750, \"Camaragibe\", 16),\r\n" + 
					"(1751, \"C�mara (Muqui)\", 8),\r\n" + 
					"(1752, \"Camargo\", 23),\r\n" + 
					"(1753, \"Camargos (Mariana)\", 11),\r\n" + 
					"(1754, \"Camassandi (Jaguaripe)\", 5),\r\n" + 
					"(1755, \"Cambaquara (Ilhabela)\", 26),\r\n" + 
					"(1756, \"Cambar�\", 18),\r\n" + 
					"(1757, \"Cambar� do Sul\", 23),\r\n" + 
					"(1758, \"Cambaratiba (Ibitinga)\", 26),\r\n" + 
					"(1759, \"Camb�\", 18),\r\n" + 
					"(1760, \"Cambiasca (S�o Fid�lis)\", 19),\r\n" + 
					"(1761, \"Cambira\", 18),\r\n" + 
					"(1762, \"Camboas (Paraipaba)\", 6),\r\n" + 
					"(1763, \"Cambori�\", 24),\r\n" + 
					"(1764, \"Cambuci\", 19),\r\n" + 
					"(1765, \"Cambu�\", 11),\r\n" + 
					"(1766, \"Cambuinzal (Xanxer�)\", 24),\r\n" + 
					"(1767, \"Cambuquira\", 11),\r\n" + 
					"(1768, \"Cambuquira (Santar�m)\", 14),\r\n" + 
					"(1769, \"Camela (Ipojuca)\", 16),\r\n" + 
					"(1770, \"Camet�\", 14),\r\n" + 
					"(1771, \"Camet� (Barreirinha)\", 3),\r\n" + 
					"(1772, \"Camilos (Meruoca)\", 6),\r\n" + 
					"(1773, \"Camiranga (Viseu)\", 14),\r\n" + 
					"(1774, \"Camirim (Morro do Chap�u)\", 5),\r\n" + 
					"(1775, \"Camis�o (Aquidauana)\", 12),\r\n" + 
					"(1776, \"Camocim\", 6),\r\n" + 
					"(1777, \"Camocim de S�o F�lix\", 16),\r\n" + 
					"(1778, \"Campan�rio\", 11),\r\n" + 
					"(1779, \"Campan�rio (Uruoca)\", 6),\r\n" + 
					"(1780, \"Campanha\", 11),\r\n" + 
					"(1781, \"Campestre\", 11),\r\n" + 
					"(1782, \"Campestre\", 2),\r\n" + 
					"(1783, \"Campestre (Ant�nio Jo�o)\", 12),\r\n" + 
					"(1784, \"Campestre (Chorozinho)\", 6),\r\n" + 
					"(1785, \"Campestre da Serra\", 23),\r\n" + 
					"(1786, \"Campestre de Goi�s\", 9),\r\n" + 
					"(1787, \"Campestre do Maranh�o\", 10),\r\n" + 
					"(1788, \"Campestre (Fortim)\", 6),\r\n" + 
					"(1789, \"Campestre (Salvador do Sul)\", 23),\r\n" + 
					"(1790, \"Campestre (S�o Miguel das Miss�es)\", 23),\r\n" + 
					"(1791, \"Campestrinho (Andradas)\", 11),\r\n" + 
					"(1792, \"Campestrinho (Arauc�ria)\", 18),\r\n" + 
					"(1793, \"Campestrinho (Divinol�ndia)\", 26),\r\n" + 
					"(1794, \"Campina (Campo Largo)\", 18),\r\n" + 
					"(1795, \"Campina�u\", 9),\r\n" + 
					"(1796, \"Campina da Lagoa\", 18),\r\n" + 
					"(1797, \"Campina das Miss�es\", 23),\r\n" + 
					"(1798, \"Campina de Fora (Ribeir�o Branco)\", 26),\r\n" + 
					"(1799, \"Campina do Miranguava (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(1800, \"Campina do Monte Alegre\", 26),\r\n" + 
					"(1801, \"Campina dos Furtados (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(1802, \"Campina do Sim�o\", 18),\r\n" + 
					"(1803, \"Campina Grande\", 15),\r\n" + 
					"(1804, \"Campina Grande do Sul\", 18),\r\n" + 
					"(1805, \"Campina (Guarapuava)\", 18),\r\n" + 
					"(1806, \"Campinal (Presidente Epit�cio)\", 26),\r\n" + 
					"(1807, \"Campin�polis\", 13),\r\n" + 
					"(1808, \"Campina Redonda (Espumoso)\", 23),\r\n" + 
					"(1809, \"Campinas\", 26),\r\n" + 
					"(1810, \"Campinas (Campo Largo)\", 18),\r\n" + 
					"(1811, \"Campinas do Piau�\", 17),\r\n" + 
					"(1812, \"Campinas do Sul\", 23),\r\n" + 
					"(1813, \"Campinas (Pl�cido de Castro)\", 1),\r\n" + 
					"(1814, \"Campinas (Saldanha Marinho)\", 23),\r\n" + 
					"(1815, \"Campina Verde\", 11),\r\n" + 
					"(1816, \"Campinho (Roca Sales)\", 23),\r\n" + 
					"(1817, \"Campinhos (Santo Amaro)\", 5),\r\n" + 
					"(1818, \"Campinorte\", 9),\r\n" + 
					"(1819, \"Campo Alegre\", 2),\r\n" + 
					"(1820, \"Campo Alegre\", 24),\r\n" + 
					"(1821, \"Campo Alegre (Crato)\", 6),\r\n" + 
					"(1822, \"Campo Alegre de Goi�s\", 9),\r\n" + 
					"(1823, \"Campo Alegre de Lourdes\", 5),\r\n" + 
					"(1824, \"Campo Alegre de Minas (Resplendor)\", 11),\r\n" + 
					"(1825, \"Campo Alegre do Fidalgo\", 17),\r\n" + 
					"(1826, \"Campo Alegre (Lagoa do Ouro)\", 16),\r\n" + 
					"(1827, \"Campo Alegre (Sousa)\", 15),\r\n" + 
					"(1828, \"Campo Azul\", 11),\r\n" + 
					"(1829, \"Campo Belo\", 11),\r\n" + 
					"(1830, \"Campo Belo do Sul\", 24),\r\n" + 
					"(1831, \"Campo Bom\", 23),\r\n" + 
					"(1832, \"Campo Bonito\", 18),\r\n" + 
					"(1833, \"Campo Branco (Progresso)\", 23),\r\n" + 
					"(1834, \"Campo de Sobradinho (Passa Sete)\", 23),\r\n" + 
					"(1835, \"Campo do Brito\", 25),\r\n" + 
					"(1836, \"Campo do Bugre (Laranjeiras do Sul)\", 18),\r\n" + 
					"(1837, \"Campo do Meio\", 11),\r\n" + 
					"(1838, \"Campo do Meio (Mato Castelhano)\", 23),\r\n" + 
					"(1839, \"Campo do Meio (Ponta Grossa)\", 18),\r\n" + 
					"(1840, \"Campo do Tenente\", 18),\r\n" + 
					"(1841, \"Campo Er�\", 24),\r\n" + 
					"(1842, \"Campo Florido\", 11),\r\n" + 
					"(1843, \"Campo Formoso\", 5),\r\n" + 
					"(1844, \"Campo Formoso (Presidente Dutra)\", 5),\r\n" + 
					"(1845, \"Campo Grande\", 20),\r\n" + 
					"(1846, \"Campo Grande\", 12),\r\n" + 
					"(1847, \"Campo Grande\", 2),\r\n" + 
					"(1848, \"Campo Grande do Piau�\", 17),\r\n" + 
					"(1849, \"Campo Grande (Itabaiana)\", 15),\r\n" + 
					"(1850, \"Campol�ndia (Ivol�ndia)\", 9),\r\n" + 
					"(1851, \"Campo Largo\", 18),\r\n" + 
					"(1852, \"Campo Largo da Roseira (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(1853, \"Campo Largo do Piau�\", 17),\r\n" + 
					"(1854, \"Campolide (Ant�nio Carlos)\", 11),\r\n" + 
					"(1855, \"Campo Limpo de Goi�s\", 9),\r\n" + 
					"(1856, \"Campo Limpo (Itumbiara)\", 9),\r\n" + 
					"(1857, \"Campo Limpo Paulista\", 26),\r\n" + 
					"(1858, \"Campo Lindo (Cristalina)\", 9),\r\n" + 
					"(1859, \"Campo Magro\", 18),\r\n" + 
					"(1860, \"Campo Maior\", 17),\r\n" + 
					"(1861, \"Campo Mour�o\", 18),\r\n" + 
					"(1862, \"Campo Novo\", 23),\r\n" + 
					"(1863, \"Campo Novo de Rond�nia\", 21),\r\n" + 
					"(1864, \"Campo Novo do Parecis\", 13),\r\n" + 
					"(1865, \"Campo Novo (Fontoura Xavier)\", 23),\r\n" + 
					"(1866, \"Campo Redondo\", 20),\r\n" + 
					"(1867, \"Campo Redondo (Varzel�ndia)\", 11),\r\n" + 
					"(1868, \"Campos Altos\", 11),\r\n" + 
					"(1869, \"Campo Santo (Coronel Bicaco)\", 23),\r\n" + 
					"(1870, \"Campos Belos\", 9),\r\n" + 
					"(1871, \"Campos Belos (Caridade)\", 6),\r\n" + 
					"(1872, \"Campos Borges\", 23),\r\n" + 
					"(1873, \"Campos (Canind�)\", 6),\r\n" + 
					"(1874, \"Campos de Cunha (Cunha)\", 26),\r\n" + 
					"(1875, \"Campos de J�lio\", 13),\r\n" + 
					"(1876, \"Campos do Jord�o\", 26),\r\n" + 
					"(1877, \"Campos dos Goytacazes\", 19),\r\n" + 
					"(1878, \"Campo Seco (Ros�rio do Sul)\", 23),\r\n" + 
					"(1879, \"Campos Gerais\", 11),\r\n" + 
					"(1880, \"Campos Lindos\", 27),\r\n" + 
					"(1881, \"Campos Novos\", 24),\r\n" + 
					"(1882, \"Campos Novos Paulista\", 26),\r\n" + 
					"(1883, \"Campos Sales\", 6),\r\n" + 
					"(1884, \"Campos Verdes\", 9),\r\n" + 
					"(1885, \"Campo Verde\", 13),\r\n" + 
					"(1886, \"Campo Vicente (Nova Hartz)\", 23),\r\n" + 
					"(1887, \"Camurugi (Tapero�)\", 5),\r\n" + 
					"(1888, \"Camurupim (Rio Tinto)\", 15),\r\n" + 
					"(1889, \"Camutanga\", 16),\r\n" + 
					"(1890, \"Cana�\", 11),\r\n" + 
					"(1891, \"Cana� (Arapiraca)\", 2),\r\n" + 
					"(1892, \"Cana� dos Caraj�s\", 14),\r\n" + 
					"(1893, \"Canaan (Trairi)\", 6),\r\n" + 
					"(1894, \"Cana� (Triunfo)\", 16),\r\n" + 
					"(1895, \"Cana Brava (Arraias)\", 27),\r\n" + 
					"(1896, \"Canabrava do Norte\", 13),\r\n" + 
					"(1897, \"Canabrava (Francisco S�)\", 11),\r\n" + 
					"(1898, \"Canabrava (Jo�o Pinheiro)\", 11),\r\n" + 
					"(1899, \"Cana Brava (Malhada)\", 5),\r\n" + 
					"(1900, \"Cana Brava (Mina�u)\", 9),\r\n" + 
					"(1901, \"Canabravinha (Paramirim)\", 5),\r\n" + 
					"(1902, \"Canad� (Jussara)\", 9),\r\n" + 
					"(1903, \"Canafistula (Apuiar�s)\", 6),\r\n" + 
					"(1904, \"Canafistula (Juc�s)\", 6),\r\n" + 
					"(1905, \"Canaf�stula (Palmeira dos �ndios)\", 2),\r\n" + 
					"(1906, \"Canan�ia\", 26),\r\n" + 
					"(1907, \"Canapi\", 2),\r\n" + 
					"(1908, \"Can�polis\", 11),\r\n" + 
					"(1909, \"Can�polis\", 5),\r\n" + 
					"(1910, \"Canarana\", 5),\r\n" + 
					"(1911, \"Canarana\", 13),\r\n" + 
					"(1912, \"Canas\", 26),\r\n" + 
					"(1913, \"Canastra (Ibateguara)\", 2),\r\n" + 
					"(1914, \"Canastr�o (Tiros)\", 11),\r\n" + 
					"(1915, \"Canatiba (Maca�bas)\", 5),\r\n" + 
					"(1916, \"Cana�na (Ipaumirim)\", 6),\r\n" + 
					"(1917, \"Cana Verde\", 11),\r\n" + 
					"(1918, \"Canavieira\", 17),\r\n" + 
					"(1919, \"Canavieiras\", 5),\r\n" + 
					"(1920, \"Canch� (Jeremoabo)\", 5),\r\n" + 
					"(1921, \"Candeal\", 5),\r\n" + 
					"(1922, \"Candeia (Marip�)\", 18),\r\n" + 
					"(1923, \"Candeias\", 5),\r\n" + 
					"(1924, \"Candeias\", 11),\r\n" + 
					"(1925, \"Candeias do Jamari\", 21),\r\n" + 
					"(1926, \"Candel�ria\", 23),\r\n" + 
					"(1927, \"Candia (Pontal)\", 26),\r\n" + 
					"(1928, \"Candiba\", 5),\r\n" + 
					"(1929, \"C�ndido de Abreu\", 18),\r\n" + 
					"(1930, \"C�ndido Freire (Giru�)\", 23),\r\n" + 
					"(1931, \"C�ndido God�i\", 23),\r\n" + 
					"(1932, \"C�ndido Mendes\", 10),\r\n" + 
					"(1933, \"C�ndido Mota\", 26),\r\n" + 
					"(1934, \"C�ndido Rodrigues\", 26),\r\n" + 
					"(1935, \"C�ndido Sales\", 5),\r\n" + 
					"(1936, \"Candiota\", 23),\r\n" + 
					"(1937, \"Candiota (Pinheiro Machado)\", 23),\r\n" + 
					"(1938, \"Cand�i\", 18),\r\n" + 
					"(1939, \"Canela\", 23),\r\n" + 
					"(1940, \"Canela (Renascen�a)\", 18),\r\n" + 
					"(1941, \"Canelinha\", 24),\r\n" + 
					"(1942, \"Cangandu (Arapiraca)\", 2),\r\n" + 
					"(1943, \"Cangas (Pocon�)\", 13),\r\n" + 
					"(1944, \"Cangati (Momba�a)\", 6),\r\n" + 
					"(1945, \"Cangati (Solon�pole)\", 6),\r\n" + 
					"(1946, \"Canguaretama\", 20),\r\n" + 
					"(1947, \"Cangu�u\", 23),\r\n" + 
					"(1948, \"Canhembora (Nova Palma)\", 23),\r\n" + 
					"(1949, \"Canhoba\", 25),\r\n" + 
					"(1950, \"Canhotinho\", 16),\r\n" + 
					"(1951, \"Canind�\", 6),\r\n" + 
					"(1952, \"Canind� de S�o Francisco\", 25),\r\n" + 
					"(1953, \"Canindezinho (Ibicuitinga)\", 6),\r\n" + 
					"(1954, \"Canindezinho (Nova Russas)\", 6),\r\n" + 
					"(1955, \"Canindezinho (Potiretama)\", 6),\r\n" + 
					"(1956, \"Canindezinho (V�rzea Alegre)\", 6),\r\n" + 
					"(1957, \"Canitar\", 26),\r\n" + 
					"(1958, \"Cano�o (Ibitit�)\", 5),\r\n" + 
					"(1959, \"Canoas\", 23),\r\n" + 
					"(1960, \"Canoas (Bom Retiro)\", 24),\r\n" + 
					"(1961, \"Canoeiros (S�o Gon�alo do Abaet�)\", 11),\r\n" + 
					"(1962, \"Canoinhas\", 24),\r\n" + 
					"(1963, \"Cansan��o\", 5),\r\n" + 
					"(1964, \"Cant�\", 22),\r\n" + 
					"(1965, \"Cantagalo\", 11),\r\n" + 
					"(1966, \"Cantagalo\", 18),\r\n" + 
					"(1967, \"Cantagalo\", 19),\r\n" + 
					"(1968, \"Canta Galo (Acarape)\", 6),\r\n" + 
					"(1969, \"Cantanhede\", 10),\r\n" + 
					"(1970, \"Cantanzal (Macap�)\", 4),\r\n" + 
					"(1971, \"Canto do Buriti\", 17),\r\n" + 
					"(1972, \"Canto do Sol (Cama�ari)\", 5),\r\n" + 
					"(1973, \"Canto Krewer (Vale Real)\", 23),\r\n" + 
					"(1974, \"Canudos\", 5),\r\n" + 
					"(1975, \"Canudos do Vale\", 23),\r\n" + 
					"(1976, \"Canudos (Mulungu do Morro)\", 5),\r\n" + 
					"(1977, \"Canum� (Borba)\", 3),\r\n" + 
					"(1978, \"Canutama\", 3),\r\n" + 
					"(1979, \"Canzian�polis (Pranchita)\", 18),\r\n" + 
					"(1980, \"Capan� (Cachoeira do Sul)\", 23),\r\n" + 
					"(1981, \"Capanema\", 14),\r\n" + 
					"(1982, \"Capanema\", 18),\r\n" + 
					"(1983, \"Cap�o Alto\", 24),\r\n" + 
					"(1984, \"Cap�o Alto (Castro)\", 18),\r\n" + 
					"(1985, \"Cap�o Alto (Guarapuava)\", 18),\r\n" + 
					"(1986, \"Cap�o Alto (Santa B�rbara do Sul)\", 23),\r\n" + 
					"(1987, \"Cap�o Bonito\", 26),\r\n" + 
					"(1988, \"Cap�o Bonito do Sul\", 23),\r\n" + 
					"(1989, \"Cap�o Bonito (Guarapuava)\", 18),\r\n" + 
					"(1990, \"Cap�o Bonito (Salto do Jacu�)\", 23),\r\n" + 
					"(1991, \"Cap�o Comprido (Tavares)\", 23),\r\n" + 
					"(1992, \"Cap�o da Canoa\", 23),\r\n" + 
					"(1993, \"Cap�o da Heran�a (Vacaria)\", 23),\r\n" + 
					"(1994, \"Cap�o da Lagoa (Guarapuava)\", 18),\r\n" + 
					"(1995, \"Cap�o da Porteira (Viam�o)\", 23),\r\n" + 
					"(1996, \"Cap�o do Cedro (Lagoa Vermelha)\", 23),\r\n" + 
					"(1997, \"Cap�o do Cip�\", 23),\r\n" + 
					"(1998, \"Cap�o do Le�o\", 23),\r\n" + 
					"(1999, \"Cap�o do Tigre (Bom Jesus)\", 23),\r\n" + 
					"(2000, \"Cap�o Grande (Muitos Cap�es)\", 23),\r\n" + 
					"(2001, \"Cap�o Grande (Ponta Grossa)\", 18),\r\n" + 
					"(2002, \"Cap�o Grande (V�rzea Grande)\", 13),\r\n" + 
					"(2003, \"Cap�o (Laje)\", 5),\r\n" + 
					"(2004, \"Cap�o Novo (Cap�o da Canoa)\", 23),\r\n" + 
					"(2005, \"Cap�o Rico (Guarapuava)\", 18),\r\n" + 
					"(2006, \"Cap�o Seco (Sidrol�ndia)\", 12),\r\n" + 
					"(2007, \"Cap�o Verde (Alto Paraguai)\", 13),\r\n" + 
					"(2008, \"Capara�\", 11),\r\n" + 
					"(2009, \"Capela\", 25),\r\n" + 
					"(2010, \"Capela\", 2),\r\n" + 
					"(2011, \"Capela de Santana\", 23),\r\n" + 
					"(2012, \"Capela do Alto\", 26),\r\n" + 
					"(2013, \"Capela do Alto Alegre\", 5),\r\n" + 
					"(2014, \"Capela Nova\", 11),\r\n" + 
					"(2015, \"Capela Santo Ant�nio (Camaqu�)\", 23),\r\n" + 
					"(2016, \"Capela S�o Francisco (Bom Jesus)\", 23),\r\n" + 
					"(2017, \"Capela S�o Paulo (S�o Luiz Gonzaga)\", 23),\r\n" + 
					"(2018, \"Capela Velha (Camaqu�)\", 23),\r\n" + 
					"(2019, \"Capelinha\", 11),\r\n" + 
					"(2020, \"Capelinha (Anicuns)\", 9),\r\n" + 
					"(2021, \"Capetinga\", 11),\r\n" + 
					"(2022, \"Capim\", 15),\r\n" + 
					"(2023, \"Capim Branco\", 11),\r\n" + 
					"(2024, \"Capim de Ro�a (Pindoretama)\", 6),\r\n" + 
					"(2025, \"Capim Grosso\", 5),\r\n" + 
					"(2026, \"Capin�polis\", 11),\r\n" + 
					"(2027, \"Capinzal\", 24),\r\n" + 
					"(2028, \"Capinzal (Arauc�ria)\", 18),\r\n" + 
					"(2029, \"Capinzal (Constantina)\", 23),\r\n" + 
					"(2030, \"Capinzal do Norte\", 10),\r\n" + 
					"(2031, \"Capistrano\", 6),\r\n" + 
					"(2032, \"Capit�nia (Montalv�nia)\", 11),\r\n" + 
					"(2033, \"Capit�o\", 23),\r\n" + 
					"(2034, \"Capit�o Andrade\", 11),\r\n" + 
					"(2035, \"Capit�o de Campos\", 17),\r\n" + 
					"(2036, \"Capit�o En�as\", 11),\r\n" + 
					"(2037, \"Capit�o Gerv�sio Oliveira\", 17),\r\n" + 
					"(2038, \"Capit�o Le�nidas Marques\", 18),\r\n" + 
					"(2039, \"Capit�o Mor (Pedra Branca)\", 6),\r\n" + 
					"(2040, \"Capit�o Noronha (Encruzilhada do Sul)\", 23),\r\n" + 
					"(2041, \"Capit�o Pedro Sampaio (Canind�)\", 6),\r\n" + 
					"(2042, \"Capit�o Po�o\", 14),\r\n" + 
					"(2043, \"Capit�lio\", 11),\r\n" + 
					"(2044, \"Capivara (Almirante Tamandar�)\", 18),\r\n" + 
					"(2045, \"Capivara (Erval Seco)\", 23),\r\n" + 
					"(2046, \"Capivari\", 26),\r\n" + 
					"(2047, \"Capivari da Mata (Ituverava)\", 26),\r\n" + 
					"(2048, \"Capivari de Baixo\", 24),\r\n" + 
					"(2049, \"Capivari do Sul\", 23),\r\n" + 
					"(2050, \"Capivarita (Pantano Grande)\", 23),\r\n" + 
					"(2051, \"Capixaba\", 1),\r\n" + 
					"(2052, \"Capoeira Grande (Tenente Portela)\", 23),\r\n" + 
					"(2053, \"Capoeiras\", 16),\r\n" + 
					"(2054, \"Capoeirinha (Castro)\", 18),\r\n" + 
					"(2055, \"Capo-Er� (Erechim)\", 23),\r\n" + 
					"(2056, \"Caponga (Cascavel)\", 6),\r\n" + 
					"(2057, \"Caponga da Bernarda (Aquiraz)\", 6),\r\n" + 
					"(2058, \"Caponguinha (Pindoretama)\", 6),\r\n" + 
					"(2059, \"Caporanga (Santa Cruz do Rio Pardo)\", 26),\r\n" + 
					"(2060, \"Caputira\", 11),\r\n" + 
					"(2061, \"Cara�\", 23),\r\n" + 
					"(2062, \"Carabu�u (Bom Jesus do Itabapoana)\", 19),\r\n" + 
					"(2063, \"Caracar� do Arari (Cachoeira do Arari)\", 14),\r\n" + 
					"(2064, \"Caracara�\", 22),\r\n" + 
					"(2065, \"Car�-Car� (Ponta Grossa)\", 18),\r\n" + 
					"(2066, \"Caracar� (Sobral)\", 6),\r\n" + 
					"(2067, \"Caracol\", 12),\r\n" + 
					"(2068, \"Caracol\", 17),\r\n" + 
					"(2069, \"Caracol (Trair�o)\", 14),\r\n" + 
					"(2070, \"Caraguata� (Jussiape)\", 5),\r\n" + 
					"(2071, \"Caraguatatuba\", 26),\r\n" + 
					"(2072, \"Cara�\", 11),\r\n" + 
					"(2073, \"Cara�ba do Norte (S�o Francisco do Maranh�o)\", 10),\r\n" + 
					"(2074, \"Cara�bas\", 5),\r\n" + 
					"(2075, \"Cara�ba (Santa Maria da Boa Vista)\", 16),\r\n" + 
					"(2076, \"Cara�bas (Arcoverde)\", 16),\r\n" + 
					"(2077, \"Cara�ba (Seara)\", 24),\r\n" + 
					"(2078, \"Cara�bas (Paramirim)\", 5),\r\n" + 
					"(2079, \"Cara�ba (Vian�polis)\", 9),\r\n" + 
					"(2080, \"Caraibeiras (Tacaratu)\", 16),\r\n" + 
					"(2081, \"Caraibuna (Contendas do Sincor�)\", 5),\r\n" + 
					"(2082, \"Cara�pe (S�o Felipe)\", 5),\r\n" + 
					"(2083, \"Caraiva (Porto Seguro)\", 5),\r\n" + 
					"(2084, \"Caraj� (Jesu�tas)\", 18),\r\n" + 
					"(2085, \"Caraj� Seival (Ca�apava do Sul)\", 23),\r\n" + 
					"(2086, \"Caraj�s (Parauapebas)\", 14),\r\n" + 
					"(2087, \"Carambe�\", 18),\r\n" + 
					"(2088, \"Caramujo (C�ceres)\", 13),\r\n" + 
					"(2089, \"Caramuru (Camb�)\", 18),\r\n" + 
					"(2090, \"Caramuru (Carpina)\", 16),\r\n" + 
					"(2091, \"Caramuru (Santa Maria do Cambuc�)\", 16),\r\n" + 
					"(2092, \"Carana�ba\", 11),\r\n" + 
					"(2093, \"Caranda�\", 11),\r\n" + 
					"(2094, \"Carangola\", 11),\r\n" + 
					"(2095, \"Carapaj� (Camet�)\", 14),\r\n" + 
					"(2096, \"Caraparu (Santa Isabel do Par�)\", 14),\r\n" + 
					"(2097, \"Carapebus\", 19),\r\n" + 
					"(2098, \"Carapicu�ba\", 26),\r\n" + 
					"(2099, \"Cara Pintado (Guarapuava)\", 18),\r\n" + 
					"(2100, \"Carapotos (Caruaru)\", 16),\r\n" + 
					"(2101, \"Caratateua (Bragan�a)\", 14),\r\n" + 
					"(2102, \"Caratinga\", 11),\r\n" + 
					"(2103, \"Caratuva (Arapoti)\", 18),\r\n" + 
					"(2104, \"Carauari\", 3),\r\n" + 
					"(2105, \"Cara�bas\", 15),\r\n" + 
					"(2106, \"Cara�bas\", 20),\r\n" + 
					"(2107, \"Cara�bas do Piau�\", 17),\r\n" + 
					"(2108, \"Cara�ba Torta (Carpina)\", 16),\r\n" + 
					"(2109, \"Caravagio (Sorriso)\", 13),\r\n" + 
					"(2110, \"Caravelas\", 5),\r\n" + 
					"(2111, \"Carazinho\", 23),\r\n" + 
					"(2112, \"Carazinho (Ponta Grossa)\", 18),\r\n" + 
					"(2113, \"Carbonera (Maria Helena)\", 18),\r\n" + 
					"(2114, \"Carbonita\", 11),\r\n" + 
					"(2115, \"Cardeal da Silva\", 5),\r\n" + 
					"(2116, \"Cardeal (Elias Fausto)\", 26),\r\n" + 
					"(2117, \"Cardoso\", 26),\r\n" + 
					"(2118, \"Cardoso (Concei��o)\", 15),\r\n" + 
					"(2119, \"Cardoso Moreira\", 19),\r\n" + 
					"(2120, \"Carea�u\", 11),\r\n" + 
					"(2121, \"Careiro\", 3),\r\n" + 
					"(2122, \"Careiro da V�rzea\", 3),\r\n" + 
					"(2123, \"Careiro Parau� (Careiro)\", 3),\r\n" + 
					"(2124, \"Cariacica\", 8),\r\n" + 
					"(2125, \"Carice (Itamb�)\", 16),\r\n" + 
					"(2126, \"Caridade\", 6),\r\n" + 
					"(2127, \"Caridade do Piau�\", 17),\r\n" + 
					"(2128, \"Carima (Barreiros)\", 16),\r\n" + 
					"(2129, \"Carinhanha\", 5),\r\n" + 
					"(2130, \"Carioca (Par� de Minas)\", 11),\r\n" + 
					"(2131, \"Caripare (Riach�o das Neves)\", 5),\r\n" + 
					"(2132, \"Caripi (Igarap�-A�u)\", 14),\r\n" + 
					"(2133, \"Carira\", 25),\r\n" + 
					"(2134, \"Carir�\", 6),\r\n" + 
					"(2135, \"Cariria�u\", 6),\r\n" + 
					"(2136, \"Cariri do Tocantins\", 27),\r\n" + 
					"(2137, \"Caririmirim (Moreil�ndia)\", 16),\r\n" + 
					"(2138, \"Cari�s\", 6),\r\n" + 
					"(2139, \"Cariutaba (Farias Brito)\", 6),\r\n" + 
					"(2140, \"Carlinda\", 13),\r\n" + 
					"(2141, \"Carl�polis\", 18),\r\n" + 
					"(2142, \"Carlos Alves (S�o Jo�o Nepomuceno)\", 11),\r\n" + 
					"(2143, \"Carlos Barbosa\", 23),\r\n" + 
					"(2144, \"Carlos Chagas\", 11),\r\n" + 
					"(2145, \"Carlos Gomes\", 23),\r\n" + 
					"(2146, \"Carmel�polis (Campos Sales)\", 6),\r\n" + 
					"(2147, \"Carm�sia\", 11),\r\n" + 
					"(2148, \"Carmo\", 19),\r\n" + 
					"(2149, \"Carmo da Cachoeira\", 11),\r\n" + 
					"(2150, \"Carmo da Mata\", 11),\r\n" + 
					"(2151, \"Carmo de Minas\", 11),\r\n" + 
					"(2152, \"Carmo do Cajuru\", 11),\r\n" + 
					"(2153, \"Carmo do Parana�ba\", 11),\r\n" + 
					"(2154, \"Carmo do Rio Claro\", 11),\r\n" + 
					"(2155, \"Carmo do Rio Verde\", 9),\r\n" + 
					"(2156, \"Carmol�ndia\", 27),\r\n" + 
					"(2157, \"Carmo (Macap�)\", 4),\r\n" + 
					"(2158, \"Carm�polis\", 25),\r\n" + 
					"(2159, \"Carm�polis de Minas\", 11),\r\n" + 
					"(2160, \"Carna�ba\", 16),\r\n" + 
					"(2161, \"Carna�ba do Sert�o (Juazeiro)\", 5),\r\n" + 
					"(2162, \"Carna�ba dos Dantas\", 20),\r\n" + 
					"(2163, \"Carnaubais\", 20),\r\n" + 
					"(2164, \"Carna�ba (Jati)\", 6),\r\n" + 
					"(2165, \"Carnaubal\", 6),\r\n" + 
					"(2166, \"Carna�bas (Momba�a)\", 6),\r\n" + 
					"(2167, \"Carnaubeira da Penha\", 16),\r\n" + 
					"(2168, \"Carnaubinha (Milh�)\", 6),\r\n" + 
					"(2169, \"Carne-de-Vaca (Goiana)\", 16),\r\n" + 
					"(2170, \"Carneirinho\", 11),\r\n" + 
					"(2171, \"Carneiro (Bu�que)\", 16),\r\n" + 
					"(2172, \"Carneiros\", 2),\r\n" + 
					"(2173, \"Caroalina (Sert�nia)\", 16),\r\n" + 
					"(2174, \"Caroba (Candeias)\", 5),\r\n" + 
					"(2175, \"Caroebe\", 22),\r\n" + 
					"(2176, \"Carolina\", 10),\r\n" + 
					"(2177, \"Carovi (Santiago)\", 23),\r\n" + 
					"(2178, \"Carpina\", 16),\r\n" + 
					"(2179, \"Carqueja (Floresta)\", 16),\r\n" + 
					"(2180, \"Carquejo (Mucambo)\", 6),\r\n" + 
					"(2181, \"Carrancas\", 11),\r\n" + 
					"(2182, \"Carrapateira\", 15),\r\n" + 
					"(2183, \"Carrapateiras (Tau�)\", 6),\r\n" + 
					"(2184, \"Carrapichel (Senhor do Bonfim)\", 5),\r\n" + 
					"(2185, \"Carrasco (Arapiraca)\", 2),\r\n" + 
					"(2186, \"Carrasco Bonito\", 27),\r\n" + 
					"(2187, \"Carrazedo (Gurup�)\", 14),\r\n" + 
					"(2188, \"Cartucho (Goiatins)\", 27),\r\n" + 
					"(2189, \"Caruara (Santos)\", 26),\r\n" + 
					"(2190, \"Caruaru\", 16),\r\n" + 
					"(2191, \"Caruata� (Tiangu�)\", 6),\r\n" + 
					"(2192, \"Carumb� (Itapor�)\", 12),\r\n" + 
					"(2193, \"Carutapera\", 10),\r\n" + 
					"(2194, \"Carvalh�polis\", 11),\r\n" + 
					"(2195, \"Carvalhos\", 11),\r\n" + 
					"(2196, \"Carvalho (Tamboril)\", 6),\r\n" + 
					"(2197, \"Carvoeiro (Barcelos)\", 3),\r\n" + 
					"(2198, \"Carvoeiro (Itarema)\", 6),\r\n" + 
					"(2199, \"Casa Branca\", 26),\r\n" + 
					"(2200, \"Casa Branca (Bom Jesus)\", 23),\r\n" + 
					"(2201, \"Casa Branca (Xambr�)\", 18),\r\n" + 
					"(2202, \"Casa Grande\", 11),\r\n" + 
					"(2203, \"Casa Nova\", 5),\r\n" + 
					"(2204, \"Casa Nova (Castro)\", 18),\r\n" + 
					"(2205, \"Casca\", 23),\r\n" + 
					"(2206, \"Cascalho Rico\", 11),\r\n" + 
					"(2207, \"Cascata (Horizontina)\", 23),\r\n" + 
					"(2208, \"Cascata (Pelotas)\", 23),\r\n" + 
					"(2209, \"Cascatinha (Camb�)\", 18),\r\n" + 
					"(2210, \"Cascavel\", 18),\r\n" + 
					"(2211, \"Cascavel\", 6),\r\n" + 
					"(2212, \"Caseara\", 27),\r\n" + 
					"(2213, \"Caseiros\", 23),\r\n" + 
					"(2214, \"Casimiro de Abreu\", 19),\r\n" + 
					"(2215, \"Casinhas\", 16),\r\n" + 
					"(2216, \"Casserengue\", 15),\r\n" + 
					"(2217, \"C�ssia\", 11),\r\n" + 
					"(2218, \"C�ssia dos Coqueiros\", 26),\r\n" + 
					"(2219, \"Cassil�ndia\", 12),\r\n" + 
					"(2220, \"Cassununga (Tesouro)\", 13),\r\n" + 
					"(2221, \"Castanhal\", 14),\r\n" + 
					"(2222, \"Castanh�o (Alto Santo)\", 6),\r\n" + 
					"(2223, \"Castanheira\", 13),\r\n" + 
					"(2224, \"Castanheiras\", 21),\r\n" + 
					"(2225, \"Castel�ndia\", 9),\r\n" + 
					"(2226, \"Castelinho (Frederico Westphalen)\", 23),\r\n" + 
					"(2227, \"Castelo\", 8),\r\n" + 
					"(2228, \"Castelo do Piau�\", 17),\r\n" + 
					"(2229, \"Castelo dos Sonhos (Altamira)\", 14),\r\n" + 
					"(2230, \"Castelo Novo (Ilh�us)\", 5),\r\n" + 
					"(2231, \"Castilho\", 26),\r\n" + 
					"(2232, \"Castrin�polis (Rian�polis)\", 9),\r\n" + 
					"(2233, \"Castro\", 18),\r\n" + 
					"(2234, \"Castro Alves\", 5),\r\n" + 
					"(2235, \"Cataguarino (Cataguases)\", 11),\r\n" + 
					"(2236, \"Cataguases\", 11),\r\n" + 
					"(2237, \"Cataj�s (Santo Ant�nio do Jacinto)\", 11),\r\n" + 
					"(2238, \"Catal�o\", 9),\r\n" + 
					"(2239, \"Catanduva\", 26),\r\n" + 
					"(2240, \"Catanduva Grande (Santo Ant�nio da Patrulha)\", 23),\r\n" + 
					"(2241, \"Catanduvas\", 18),\r\n" + 
					"(2242, \"Catanduvas\", 24),\r\n" + 
					"(2243, \"Catanduvas (Castro)\", 18),\r\n" + 
					"(2244, \"Catanduvas do Sul (Contenda)\", 18),\r\n" + 
					"(2245, \"Catarina\", 6),\r\n" + 
					"(2246, \"Catarinenses (Paranava�)\", 18),\r\n" + 
					"(2247, \"Catas Altas\", 11),\r\n" + 
					"(2248, \"Catas Altas da Noruega\", 11),\r\n" + 
					"(2249, \"Catende\", 16),\r\n" + 
					"(2250, \"Catiara (Serra do Salitre)\", 11),\r\n" + 
					"(2251, \"Catigu�\", 26),\r\n" + 
					"(2252, \"Catimbau (Alegrete)\", 23),\r\n" + 
					"(2253, \"Catimba� (Bu�que)\", 16),\r\n" + 
					"(2254, \"Catinga do Moura (Jacobina)\", 5),\r\n" + 
					"(2255, \"Catingal (Manoel Vitorino)\", 5),\r\n" + 
					"(2256, \"Catingueira\", 15),\r\n" + 
					"(2257, \"Catol�ndia\", 5),\r\n" + 
					"(2258, \"Catol� (Campina Grande)\", 15),\r\n" + 
					"(2259, \"Catol� (Casinhas)\", 16),\r\n" + 
					"(2260, \"Catol� da Pista (Piquet Carneiro)\", 6),\r\n" + 
					"(2261, \"Catol� do Rocha\", 15),\r\n" + 
					"(2262, \"Catol� (Momba�a)\", 6),\r\n" + 
					"(2263, \"Catol�s (Aba�ra)\", 5),\r\n" + 
					"(2264, \"Catolezinho (Itamb�)\", 5),\r\n" + 
					"(2265, \"Catu\", 5),\r\n" + 
					"(2266, \"Catuai (S�o Jos� do Rio Claro)\", 13),\r\n" + 
					"(2267, \"Catuana (Caucaia)\", 6),\r\n" + 
					"(2268, \"Catucaba (S�o Luiz do Paraitinga)\", 26),\r\n" + 
					"(2269, \"Catu de Abrantes (Cama�ari)\", 5),\r\n" + 
					"(2270, \"Catu�pe\", 23),\r\n" + 
					"(2271, \"Catu�ra (Alfredo Wagner)\", 24),\r\n" + 
					"(2272, \"Catuji\", 11),\r\n" + 
					"(2273, \"Catunda\", 6),\r\n" + 
					"(2274, \"Catun� (Tombos)\", 11),\r\n" + 
					"(2275, \"Catuni (Francisco S�)\", 11),\r\n" + 
					"(2276, \"Catura�\", 9),\r\n" + 
					"(2277, \"Caturama\", 5),\r\n" + 
					"(2278, \"Caturit�\", 15),\r\n" + 
					"(2279, \"Catuti\", 11),\r\n" + 
					"(2280, \"Caucaia\", 6),\r\n" + 
					"(2281, \"Cavajureta (S�o Vicente do Sul)\", 23),\r\n" + 
					"(2282, \"Cavalcante\", 9),\r\n" + 
					"(2283, \"Cavalheiro (Ipameri)\", 9),\r\n" + 
					"(2284, \"Cavalo Ru�o de Deus (Brejo da Madre de Deus)\", 16),\r\n" + 
					"(2285, \"Cavunge (Ipecaet�)\", 5),\r\n" + 
					"(2286, \"Caxambu\", 11),\r\n" + 
					"(2287, \"Caxambu (Castro)\", 18),\r\n" + 
					"(2288, \"Caxambu do Sul\", 24),\r\n" + 
					"(2289, \"Caxias\", 10),\r\n" + 
					"(2290, \"Caxias do Sul\", 23),\r\n" + 
					"(2291, \"Caxing�\", 17),\r\n" + 
					"(2292, \"Caxitor� (Teju�uoca)\", 6),\r\n" + 
					"(2293, \"Caxitor� (Umirim)\", 6),\r\n" + 
					"(2294, \"Cazuza Ferreira (S�o Francisco de Paula)\", 23),\r\n" + 
					"(2295, \"Cear�-Mirim\", 20),\r\n" + 
					"(2296, \"Cebrasa (An�polis)\", 9),\r\n" + 
					"(2297, \"Cedral\", 10),\r\n" + 
					"(2298, \"Cedral\", 26),\r\n" + 
					"(2299, \"Cedro\", 16),\r\n" + 
					"(2300, \"Cedro\", 6),\r\n" + 
					"(2301, \"Cedro Alto (Rio dos Cedros)\", 24),\r\n" + 
					"(2302, \"Cedro (Chorozinho)\", 6),\r\n" + 
					"(2303, \"Cedro de S�o Jo�o\", 25),\r\n" + 
					"(2304, \"Cedro do Abaet�\", 11),\r\n" + 
					"(2305, \"Cedro Marcado (Tenente Portela)\", 23),\r\n" + 
					"(2306, \"Cedro (Perobal)\", 18),\r\n" + 
					"(2307, \"Celina (Alegre)\", 8),\r\n" + 
					"(2308, \"Celso Ramos\", 24),\r\n" + 
					"(2309, \"Cemoaba (Tururu)\", 6),\r\n" + 
					"(2310, \"Centen�rio\", 23),\r\n" + 
					"(2311, \"Centen�rio\", 27),\r\n" + 
					"(2312, \"Centen�rio do Sul\", 18),\r\n" + 
					"(2313, \"Centen�rio (Mutum)\", 11),\r\n" + 
					"(2314, \"Centen�rio (Palmeira das Miss�es)\", 23),\r\n" + 
					"(2315, \"Centen�rio (Ponta Grossa)\", 18),\r\n" + 
					"(2316, \"Central\", 5),\r\n" + 
					"(2317, \"Central de Minas\", 11),\r\n" + 
					"(2318, \"Central de Santa Helena (Divino das Laranjeiras)\", 11),\r\n" + 
					"(2319, \"Central do Maranh�o\", 10),\r\n" + 
					"(2320, \"Centralina\", 11),\r\n" + 
					"(2321, \"Centralito (Cascavel)\", 18),\r\n" + 
					"(2322, \"Central Lupion (Cascavel)\", 18),\r\n" + 
					"(2323, \"Centro do Guilherme\", 10),\r\n" + 
					"(2324, \"Centro Linha Brasil (Ven�ncio Aires)\", 23),\r\n" + 
					"(2325, \"Centro Novo do Maranh�o\", 10),\r\n" + 
					"(2326, \"Centro Novo (Planalto)\", 18),\r\n" + 
					"(2327, \"Cepilho (Areia)\", 15),\r\n" + 
					"(2328, \"Ceraima (Guanambi)\", 5),\r\n" + 
					"(2329, \"Cercadinho (Vit�ria da Conquista)\", 5),\r\n" + 
					"(2330, \"Cerejeiras\", 21),\r\n" + 
					"(2331, \"Ceres\", 9),\r\n" + 
					"(2332, \"Cerne (Campo Largo)\", 18),\r\n" + 
					"(2333, \"Cerqueira C�sar\", 26),\r\n" + 
					"(2334, \"Cerquilho\", 26),\r\n" + 
					"(2335, \"Cerrado Grande (Ponta Grossa)\", 18),\r\n" + 
					"(2336, \"Cerrito\", 23),\r\n" + 
					"(2337, \"Cerrito Alegre (Pelotas)\", 23),\r\n" + 
					"(2338, \"Cerrito do Ouro (S�o Sep�)\", 23),\r\n" + 
					"(2339, \"Cerro Alegre Baixo (Santa Cruz do Sul)\", 23),\r\n" + 
					"(2340, \"Cerro Alto (Tuparendi)\", 23),\r\n" + 
					"(2341, \"Cerro Azul\", 18),\r\n" + 
					"(2342, \"Cerro Azul (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(2343, \"Cerro Branco\", 23),\r\n" + 
					"(2344, \"Cerro Chato (Herval)\", 23),\r\n" + 
					"(2345, \"Cerro Claro (S�o Pedro do Sul)\", 23),\r\n" + 
					"(2346, \"Cerro Cor�\", 20),\r\n" + 
					"(2347, \"Cerro do Martins (Ca�apava do Sul)\", 23),\r\n" + 
					"(2348, \"Cerro do Roque (Buti�)\", 23),\r\n" + 
					"(2349, \"Cerro Grande\", 23),\r\n" + 
					"(2350, \"Cerro Grande do Sul\", 23),\r\n" + 
					"(2351, \"Cerro Largo\", 23),\r\n" + 
					"(2352, \"Cerro Negro\", 24),\r\n" + 
					"(2353, \"Cerro Partido (Encruzilhada do Sul)\", 23),\r\n" + 
					"(2354, \"Cerro Preto (Tunas)\", 23),\r\n" + 
					"(2355, \"Cerro Velho (Ponta Grossa)\", 18),\r\n" + 
					"(2356, \"Cervo (Borda da Mata)\", 11),\r\n" + 
					"(2357, \"Ces�rio Lange\", 26),\r\n" + 
					"(2358, \"C�u Azul\", 18),\r\n" + 
					"(2359, \"Cezarina\", 9),\r\n" + 
					"(2360, \"Ch�cara\", 11),\r\n" + 
					"(2361, \"Ch� de Alegria\", 16),\r\n" + 
					"(2362, \"Ch� de Cruz (Abreu e Lima)\", 16),\r\n" + 
					"(2363, \"Ch� do Rocha (Orob�)\", 16),\r\n" + 
					"(2364, \"Ch� Grande\", 16),\r\n" + 
					"(2365, \"Chal�\", 11),\r\n" + 
					"(2366, \"Chapada\", 23),\r\n" + 
					"(2367, \"Chapada da Natividade\", 27),\r\n" + 
					"(2368, \"Chapada de Areia\", 27),\r\n" + 
					"(2369, \"Chapada de Minas (Estrela do Sul)\", 11),\r\n" + 
					"(2370, \"Chapada do Norte\", 11),\r\n" + 
					"(2371, \"Chapada dos Guimar�es\", 13),\r\n" + 
					"(2372, \"Chapada Ga�cha\", 11),\r\n" + 
					"(2373, \"Chapada (Jaquirana)\", 23),\r\n" + 
					"(2374, \"Chapad�o do C�u\", 9),\r\n" + 
					"(2375, \"Chapad�o do Lageado\", 24),\r\n" + 
					"(2376, \"Chapad�o do Sul\", 12),\r\n" + 
					"(2377, \"Chapadinha\", 10),\r\n" + 
					"(2378, \"Chapec�\", 24),\r\n" + 
					"(2379, \"Chap�u D'Uvas (Juiz de Fora)\", 11),\r\n" + 
					"(2380, \"Ch� Preta\", 2),\r\n" + 
					"(2381, \"Charco (Castro)\", 18),\r\n" + 
					"(2382, \"Charqueada\", 26),\r\n" + 
					"(2383, \"Charqueadas\", 23),\r\n" + 
					"(2384, \"Charrua\", 23),\r\n" + 
					"(2385, \"Chaval\", 6),\r\n" + 
					"(2386, \"Chavantes\", 26),\r\n" + 
					"(2387, \"Chaves\", 14),\r\n" + 
					"(2388, \"Chavesl�ndia (Santa Vit�ria)\", 11),\r\n" + 
					"(2389, \"Chaves (Rio Parana�ba)\", 11),\r\n" + 
					"(2390, \"Chiador\", 11),\r\n" + 
					"(2391, \"Chiapetta\", 23),\r\n" + 
					"(2392, \"Chico-lom� (Santo Ant�nio da Patrulha)\", 23),\r\n" + 
					"(2393, \"Chile (Ibicuitinga)\", 6),\r\n" + 
					"(2394, \"Chimarr�o (Andr� da Rocha)\", 23),\r\n" + 
					"(2395, \"Chonim (Governador Valadares)\", 11),\r\n" + 
					"(2396, \"Chonin de Baixo (Governador Valadares)\", 11),\r\n" + 
					"(2397, \"Chopinzinho\", 18),\r\n" + 
					"(2398, \"Chor�o (Iju�)\", 23),\r\n" + 
					"(2399, \"Chor�\", 6),\r\n" + 
					"(2400, \"Chorozinho\", 6),\r\n" + 
					"(2401, \"Chorroch�\", 5),\r\n" + 
					"(2402, \"Choupana (Anicuns)\", 9),\r\n" + 
					"(2403, \"Chu�\", 23),\r\n" + 
					"(2404, \"Chumbo (Patos de Minas)\", 11),\r\n" + 
					"(2405, \"Chupinguaia\", 21),\r\n" + 
					"(2406, \"Chuvisca\", 23),\r\n" + 
					"(2407, \"Cianorte\", 18),\r\n" + 
					"(2408, \"Cibele (Itapuranga)\", 9),\r\n" + 
					"(2409, \"C�cero Dantas\", 5),\r\n" + 
					"(2410, \"Cidade Ga�cha\", 18),\r\n" + 
					"(2411, \"Cidade Morena (Aripuan�)\", 13),\r\n" + 
					"(2412, \"Cidade Ocidental\", 9),\r\n" + 
					"(2413, \"Cidel�ndia\", 10),\r\n" + 
					"(2414, \"Cidreira\", 23),\r\n" + 
					"(2415, \"Cimbres (Pesqueira)\", 16),\r\n" + 
					"(2416, \"Cinco da Boa Vista (Carlos Barbosa)\", 23),\r\n" + 
					"(2417, \"Cinco Rios (S�o Sebasti�o do Passe)\", 5),\r\n" + 
					"(2418, \"Cinq�enten�rio (Tuparendi)\", 23),\r\n" + 
					"(2419, \"Cintra Pimentel (Nova Londrina)\", 18),\r\n" + 
					"(2420, \"Cip�\", 5),\r\n" + 
					"(2421, \"Cip� dos Anjos (Quixad�)\", 6),\r\n" + 
					"(2422, \"Cip�-Gua�u (Embu-Gua�u)\", 26),\r\n" + 
					"(2423, \"Cipol�ndia (Aquidauana)\", 12),\r\n" + 
					"(2424, \"Cipot�nea\", 11),\r\n" + 
					"(2425, \"Cir�aco\", 23),\r\n" + 
					"(2426, \"Ciril�ndia (Santa Isabel)\", 9),\r\n" + 
					"(2427, \"Cisneiros (Palma)\", 11),\r\n" + 
					"(2428, \"Clara�ba (Nova Trento)\", 24),\r\n" + 
					"(2429, \"Clara (Mata)\", 23),\r\n" + 
					"(2430, \"Clarana (Bodoc�)\", 16),\r\n" + 
					"(2431, \"Claraval\", 11),\r\n" + 
					"(2432, \"Clarinia (Santa Cruz do Rio Pardo)\", 26),\r\n" + 
					"(2433, \"Claro de Minas (Vazante)\", 11),\r\n" + 
					"(2434, \"Claro dos Po��es\", 11),\r\n" + 
					"(2435, \"Cl�udia\", 13),\r\n" + 
					"(2436, \"Claudin�polis (Naz�rio)\", 9),\r\n" + 
					"(2437, \"Cl�udio\", 11),\r\n" + 
					"(2438, \"Cl�udio Manuel (Mariana)\", 11),\r\n" + 
					"(2439, \"Clemente Argolo (Lagoa Vermelha)\", 23),\r\n" + 
					"(2440, \"Clementina\", 26),\r\n" + 
					"(2441, \"Clevel�ndia\", 18),\r\n" + 
					"(2442, \"Clevel�ndia do Norte (Oiapoque)\", 4),\r\n" + 
					"(2443, \"Coaraci\", 5),\r\n" + 
					"(2444, \"Coari\", 3),\r\n" + 
					"(2445, \"Cocaes (Sarapu�)\", 26),\r\n" + 
					"(2446, \"Cocais (Bar�o de Cocais)\", 11),\r\n" + 
					"(2447, \"Cocal\", 17),\r\n" + 
					"(2448, \"Cocal�ndia (Aragua�na)\", 27),\r\n" + 
					"(2449, \"Cocal de Telha\", 17),\r\n" + 
					"(2450, \"Cocal dos Alves\", 17),\r\n" + 
					"(2451, \"Cocal do Sul\", 24),\r\n" + 
					"(2452, \"Cocalinho\", 13),\r\n" + 
					"(2453, \"Cocalinho (Aragua�na)\", 27),\r\n" + 
					"(2454, \"Cocalzinho de Goi�s\", 9),\r\n" + 
					"(2455, \"Cocau (Bodoc�)\", 16),\r\n" + 
					"(2456, \"Cocau (Rio Formoso)\", 16),\r\n" + 
					"(2457, \"Cococi (Parambu)\", 6),\r\n" + 
					"(2458, \"Coco (Moeda)\", 11),\r\n" + 
					"(2459, \"Cocos\", 5),\r\n" + 
					"(2460, \"Codaj�s\", 3),\r\n" + 
					"(2461, \"Codi� (Senador Pompeu)\", 6),\r\n" + 
					"(2462, \"Cod�\", 10),\r\n" + 
					"(2463, \"Codozinho (Cod�)\", 10),\r\n" + 
					"(2464, \"Coelho Neto\", 10),\r\n" + 
					"(2465, \"Coimbra\", 11),\r\n" + 
					"(2466, \"Coimbra (Corumb�)\", 12),\r\n" + 
					"(2467, \"Coimbra (S�o Miguel das Miss�es)\", 23),\r\n" + 
					"(2468, \"Coit� do N�ia\", 2),\r\n" + 
					"(2469, \"Coit� (Irau�uba)\", 6),\r\n" + 
					"(2470, \"Coit� (Mauriti)\", 6),\r\n" + 
					"(2471, \"Coitinho (Guarapuava)\", 18),\r\n" + 
					"(2472, \"Coivaras\", 17),\r\n" + 
					"(2473, \"Colares\", 14),\r\n" + 
					"(2474, \"Colatina\", 8),\r\n" + 
					"(2475, \"Col�der\", 13),\r\n" + 
					"(2476, \"Colina\", 26),\r\n" + 
					"(2477, \"Colina�u (Campinorte)\", 9),\r\n" + 
					"(2478, \"Colina (Pacoti)\", 6),\r\n" + 
					"(2479, \"Colinas\", 23),\r\n" + 
					"(2480, \"Colinas\", 10),\r\n" + 
					"(2481, \"Colinas do Sul\", 9),\r\n" + 
					"(2482, \"Colinas do Tocantins\", 27),\r\n" + 
					"(2483, \"Colm�ia\", 27),\r\n" + 
					"(2484, \"Colniza\", 13),\r\n" + 
					"(2485, \"Col�mbia\", 26),\r\n" + 
					"(2486, \"Colombo\", 18),\r\n" + 
					"(2487, \"Colombo (Guapor�)\", 23),\r\n" + 
					"(2488, \"Col�nia Acioli (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(2489, \"Col�nia Cachoeira (Guarapuava)\", 18),\r\n" + 
					"(2490, \"Col�nia Castelhanos (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(2491, \"Col�nia Castrol�nda (Castro)\", 18),\r\n" + 
					"(2492, \"Col�nia Centen�rio (Cascavel)\", 18),\r\n" + 
					"(2493, \"Col�nia Cristina (Arauc�ria)\", 18),\r\n" + 
					"(2494, \"Col�nia das Almas (Catu�pe)\", 23),\r\n" + 
					"(2495, \"Col�nia do Formoso (Coribe)\", 5),\r\n" + 
					"(2496, \"Col�nia do Gurgu�ia\", 17),\r\n" + 
					"(2497, \"Col�nia Dom Carlos (Pato Branco)\", 18),\r\n" + 
					"(2498, \"Col�nia do Piau�\", 17),\r\n" + 
					"(2499, \"Col�nia Esperan�a (Cascavel)\", 18),\r\n" + 
					"(2500, \"Col�nia (Eun�polis)\", 5),\r\n" + 
					"(2501, \"Col�nia General Carneiro (General Carneiro)\", 18),\r\n" + 
					"(2502, \"Col�nia Iap� (Castro)\", 18),\r\n" + 
					"(2503, \"Col�nia Jord�ozinho (Guarapuava)\", 18),\r\n" + 
					"(2504, \"Col�nia Leopoldina\", 2),\r\n" + 
					"(2505, \"Col�nia Malhada (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(2506, \"Col�nia Medeiros (Independ�ncia)\", 23),\r\n" + 
					"(2507, \"Col�nia Melissa (Cascavel)\", 18),\r\n" + 
					"(2508, \"Col�nia Municipal (Santo �ngelo)\", 23),\r\n" + 
					"(2509, \"Col�nia Murici (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(2510, \"Col�nia Nova (Bag�)\", 23),\r\n" + 
					"(2511, \"Col�nia Padre Paulo (Agudos do Sul)\", 18),\r\n" + 
					"(2512, \"Col�nia Pereira (Paranagu�)\", 18),\r\n" + 
					"(2513, \"Col�nia Samambaia (Guarapuava)\", 18),\r\n" + 
					"(2514, \"Col�nia Santos Andrade (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(2515, \"Col�nia (S�o Fid�lis)\", 19),\r\n" + 
					"(2516, \"Col�nia S�o Jo�o (Cruz Alta)\", 23),\r\n" + 
					"(2517, \"Col�nia S�o Jos� (Cascavel)\", 18),\r\n" + 
					"(2518, \"Col�nia Sapuca� (Cascavel)\", 18),\r\n" + 
					"(2519, \"Col�nia Sa�de (Londrina)\", 18),\r\n" + 
					"(2520, \"Col�nia Socorro (Guarapuava)\", 18),\r\n" + 
					"(2521, \"Col�nia Tapera (Ponta Grossa)\", 18),\r\n" + 
					"(2522, \"Col�nia Vit�ria (Guarapuava)\", 18),\r\n" + 
					"(2523, \"Col�nia Z-3 (Pelotas)\", 23),\r\n" + 
					"(2524, \"Coloninha (Arroio do Tigre)\", 23),\r\n" + 
					"(2525, \"Colorado\", 18),\r\n" + 
					"(2526, \"Colorado\", 23),\r\n" + 
					"(2527, \"Colorado do Norte (Nova Cana� do Norte)\", 13),\r\n" + 
					"(2528, \"Colorado do Oeste\", 21),\r\n" + 
					"(2529, \"Coluna\", 11),\r\n" + 
					"(2530, \"Comandai (Santo �ngelo)\", 23),\r\n" + 
					"(2531, \"Combinado\", 27),\r\n" + 
					"(2532, \"Comendador Gomes\", 11),\r\n" + 
					"(2533, \"Comendador Levy Gasparian\", 19),\r\n" + 
					"(2534, \"Comendador Ven�ncio (Itaperuna)\", 19),\r\n" + 
					"(2535, \"Comercinho\", 11),\r\n" + 
					"(2536, \"Com�rcio (Concei��o do Almeida)\", 5),\r\n" + 
					"(2537, \"Comodoro\", 13),\r\n" + 
					"(2538, \"Comunidade Parque Boa Vista (V�rzea Grande)\", 13),\r\n" + 
					"(2539, \"Comur (Planaltina do Paran�)\", 18),\r\n" + 
					"(2540, \"Concei��o\", 15),\r\n" + 
					"(2541, \"Concei��o (Castro)\", 18),\r\n" + 
					"(2542, \"Concei��o da Aparecida\", 11),\r\n" + 
					"(2543, \"Concei��o da Barra\", 8),\r\n" + 
					"(2544, \"Concei��o da Barra de Minas\", 11),\r\n" + 
					"(2545, \"Concei��o da Boa Vista (Recreio)\", 11),\r\n" + 
					"(2546, \"Concei��o da Breja�ba (Gonzaga)\", 11),\r\n" + 
					"(2547, \"Concei��o da Feira\", 5),\r\n" + 
					"(2548, \"Concei��o da Ibitipoca (Lima Duarte)\", 11),\r\n" + 
					"(2549, \"Concei��o das Alagoas\", 11),\r\n" + 
					"(2550, \"Concei��o das Crioulas (Salgueiro)\", 16),\r\n" + 
					"(2551, \"Concei��o das Pedras\", 11),\r\n" + 
					"(2552, \"Concei��o de Ipanema\", 11),\r\n" + 
					"(2553, \"Concei��o de Itagu� (Brumadinho)\", 11),\r\n" + 
					"(2554, \"Concei��o de Jacare� (Mangaratiba)\", 19),\r\n" + 
					"(2555, \"Concei��o de Macabu\", 19),\r\n" + 
					"(2556, \"Concei��o de Minas (Dion�sio)\", 11),\r\n" + 
					"(2557, \"Concei��o de Monte Alegre (Paragua�u Paulista)\", 26),\r\n" + 
					"(2558, \"Concei��o de Piracicaba (Rio Piracicaba)\", 11),\r\n" + 
					"(2559, \"Concei��o de Tronqueiras (Coroaci)\", 11),\r\n" + 
					"(2560, \"Concei��o do Almeida\", 5),\r\n" + 
					"(2561, \"Concei��o do Araguaia\", 14),\r\n" + 
					"(2562, \"Concei��o do Canind�\", 17),\r\n" + 
					"(2563, \"Concei��o do Capim (Aimor�s)\", 11),\r\n" + 
					"(2564, \"Concei��o do Castelo\", 8),\r\n" + 
					"(2565, \"Concei��o do Coit�\", 5),\r\n" + 
					"(2566, \"Concei��o do Formoso (Santos Dumont)\", 11),\r\n" + 
					"(2567, \"Concei��o do Jacu�pe\", 5),\r\n" + 
					"(2568, \"Concei��o do Lago-A�u\", 10),\r\n" + 
					"(2569, \"Concei��o do Mato Dentro\", 11),\r\n" + 
					"(2570, \"Concei��o do Muqui (Mimoso do Sul)\", 8),\r\n" + 
					"(2571, \"Concei��o do Par�\", 11),\r\n" + 
					"(2572, \"Concei��o do Rio Acima (Santa B�rbara)\", 11),\r\n" + 
					"(2573, \"Concei��o do Rio Verde\", 11),\r\n" + 
					"(2574, \"Concei��o dos Ouros\", 11),\r\n" + 
					"(2575, \"Concei��o do Tocantins\", 27),\r\n" + 
					"(2576, \"Concei��o (Hidrol�ndia)\", 6),\r\n" + 
					"(2577, \"Concei��o (Itapetininga)\", 26),\r\n" + 
					"(2578, \"Concei��o (Paragominas)\", 14),\r\n" + 
					"(2579, \"Concei��o (Tururu)\", 6),\r\n" + 
					"(2580, \"Conchal\", 26),\r\n" + 
					"(2581, \"Conchas\", 26),\r\n" + 
					"(2582, \"Conchas Velhas (Ponta Grossa)\", 18),\r\n" + 
					"(2583, \"Conciol�ndia (P�rola d'Oeste)\", 18),\r\n" + 
					"(2584, \"Conc�rdia\", 24),\r\n" + 
					"(2585, \"Conc�rdia de Mucuri (Ladainha)\", 11),\r\n" + 
					"(2586, \"Conc�rdia do Oeste (Toledo)\", 18),\r\n" + 
					"(2587, \"Conc�rdia do Par�\", 14),\r\n" + 
					"(2588, \"Condado\", 15),\r\n" + 
					"(2589, \"Condado\", 16),\r\n" + 
					"(2590, \"Condado do Norte (S�o Jo�o da Ponte)\", 11),\r\n" + 
					"(2591, \"Conde\", 5),\r\n" + 
					"(2592, \"Conde\", 15),\r\n" + 
					"(2593, \"Condeixa (Salvaterra)\", 14),\r\n" + 
					"(2594, \"Conde�ba\", 5),\r\n" + 
					"(2595, \"Condor\", 23),\r\n" + 
					"(2596, \"Conduru (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(2597, \"C�nego Jo�o Pio (S�o Domingos do Prata)\", 11),\r\n" + 
					"(2598, \"C�nego Marinho\", 11),\r\n" + 
					"(2599, \"Confins\", 11),\r\n" + 
					"(2600, \"Confresa\", 13),\r\n" + 
					"(2601, \"Congo\", 15),\r\n" + 
					"(2602, \"Congonhal\", 11),\r\n" + 
					"(2603, \"Congonhas\", 11),\r\n" + 
					"(2604, \"Congonhas (Bandeirantes)\", 12),\r\n" + 
					"(2605, \"Congonhas (Corn�lio Proc�pio)\", 18),\r\n" + 
					"(2606, \"Congonhas do Norte\", 11),\r\n" + 
					"(2607, \"Congonhinhas\", 18),\r\n" + 
					"(2608, \"Conquista\", 11),\r\n" + 
					"(2609, \"Conquista D'Oeste\", 13),\r\n" + 
					"(2610, \"Conrado (Miguel Pereira)\", 19),\r\n" + 
					"(2611, \"Conselheiro Lafaiete\", 11),\r\n" + 
					"(2612, \"Conselheiro Mairinck\", 18),\r\n" + 
					"(2613, \"Conselheiro Mata (Diamantina)\", 11),\r\n" + 
					"(2614, \"Conselheiro Pena\", 11),\r\n" + 
					"(2615, \"Conselheiro Zacarias (Santo Ant�nio da Platina)\", 18),\r\n" + 
					"(2616, \"Conservat�ria (Valen�a)\", 19),\r\n" + 
					"(2617, \"Consola��o\", 11),\r\n" + 
					"(2618, \"Consolata (Tr�s de Maio)\", 23),\r\n" + 
					"(2619, \"Constantina\", 23),\r\n" + 
					"(2620, \"Constantino (Progresso)\", 23),\r\n" + 
					"(2621, \"Contagem\", 11),\r\n" + 
					"(2622, \"Contenda\", 18),\r\n" + 
					"(2623, \"Contendas do Sincor�\", 5),\r\n" + 
					"(2624, \"Contrato (Itamarandiba)\", 11),\r\n" + 
					"(2625, \"Copacabana do Norte (S�o Jorge do Iva�)\", 18),\r\n" + 
					"(2626, \"Copixaba (Xique-Xique)\", 5),\r\n" + 
					"(2627, \"Coqueiral\", 11),\r\n" + 
					"(2628, \"Coqueiro Baixo\", 23),\r\n" + 
					"(2629, \"Coqueiros do Sul\", 23),\r\n" + 
					"(2630, \"Coqueiro Seco\", 2),\r\n" + 
					"(2631, \"Coqueiros (Maragogipe)\", 5),\r\n" + 
					"(2632, \"Coquinhos (Anag�)\", 5),\r\n" + 
					"(2633, \"Cora��o de Jesus\", 11),\r\n" + 
					"(2634, \"Cora��o de Maria\", 5),\r\n" + 
					"(2635, \"Corb�lia\", 18),\r\n" + 
					"(2636, \"Cordeiro\", 19),\r\n" + 
					"(2637, \"Cordeiro (Cristal)\", 23),\r\n" + 
					"(2638, \"Cordeiro de Minas (Caratinga)\", 11),\r\n" + 
					"(2639, \"Cordeir�polis\", 26),\r\n" + 
					"(2640, \"Cordeiros\", 5),\r\n" + 
					"(2641, \"Cordilheira Alta\", 24),\r\n" + 
					"(2642, \"Cordilheira (Cachoeira do Sul)\", 23),\r\n" + 
					"(2643, \"Cordisburgo\", 11),\r\n" + 
					"(2644, \"Cordisl�ndia\", 11),\r\n" + 
					"(2645, \"Corea�\", 6),\r\n" + 
					"(2646, \"Coremas\", 15),\r\n" + 
					"(2647, \"Corguinho\", 12),\r\n" + 
					"(2648, \"Coribe\", 5),\r\n" + 
					"(2649, \"Corinto\", 11),\r\n" + 
					"(2650, \"Corn�lio Proc�pio\", 18),\r\n" + 
					"(2651, \"Coroaci\", 11),\r\n" + 
					"(2652, \"Coroados\", 26),\r\n" + 
					"(2653, \"Coroados (S�o Val�rio do Sul)\", 23),\r\n" + 
					"(2654, \"Coroat�\", 10),\r\n" + 
					"(2655, \"Coromandel\", 11),\r\n" + 
					"(2656, \"Coronel Barros\", 23),\r\n" + 
					"(2657, \"Coronel Bicaco\", 23),\r\n" + 
					"(2658, \"Coronel Domingos Soares\", 18),\r\n" + 
					"(2659, \"Coronel Ezequiel\", 20),\r\n" + 
					"(2660, \"Coronel Fabriciano\", 11),\r\n" + 
					"(2661, \"Coronel Finzito (Erval Seco)\", 23),\r\n" + 
					"(2662, \"Coronel Firmino Martins (Clevel�ndia)\", 18),\r\n" + 
					"(2663, \"Coronel Freitas\", 24),\r\n" + 
					"(2664, \"Coronel Goulart (�lvares Machado)\", 26),\r\n" + 
					"(2665, \"Coronel Jo�o Pessoa\", 20),\r\n" + 
					"(2666, \"Coronel Jo�o S�\", 5),\r\n" + 
					"(2667, \"Coronel Jos� Dias\", 17),\r\n" + 
					"(2668, \"Coronel Macedo\", 26),\r\n" + 
					"(2669, \"Coronel Maia (Catol� do Rocha)\", 15),\r\n" + 
					"(2670, \"Coronel Martins\", 24),\r\n" + 
					"(2671, \"Coronel Murta\", 11),\r\n" + 
					"(2672, \"Coronel Pacheco\", 11),\r\n" + 
					"(2673, \"Coronel Pilar\", 23),\r\n" + 
					"(2674, \"Coronel Ponce (Campo Verde)\", 13),\r\n" + 
					"(2675, \"Coronel Prestes (Encruzilhada do Sul)\", 23),\r\n" + 
					"(2676, \"Coronel Sapucaia\", 12),\r\n" + 
					"(2677, \"Coronel Teixeira (Marcelino Ramos)\", 23),\r\n" + 
					"(2678, \"Coronel Vivida\", 18),\r\n" + 
					"(2679, \"Coronel Xavier Chaves\", 11),\r\n" + 
					"(2680, \"Corre �gua (Macap�)\", 4),\r\n" + 
					"(2681, \"Corredeira (Piraju�)\", 26),\r\n" + 
					"(2682, \"C�rrego (Barreira)\", 6),\r\n" + 
					"(2683, \"C�rrego Danta\", 11),\r\n" + 
					"(2684, \"C�rrego da Prata (Carmo)\", 19),\r\n" + 
					"(2685, \"C�rrego de S�o Mateus (Boa Sa�de)\", 20),\r\n" + 
					"(2686, \"C�rrego do Barro (Par� de Minas)\", 11),\r\n" + 
					"(2687, \"C�rrego do Bom Jesus\", 11),\r\n" + 
					"(2688, \"C�rrego do Ouro\", 9),\r\n" + 
					"(2689, \"C�rrego do Ouro (Campos Gerais)\", 11),\r\n" + 
					"(2690, \"C�rrego do Ouro (Maca�)\", 19),\r\n" + 
					"(2691, \"C�rrego dos Bernardes (Governador Valadares)\", 11),\r\n" + 
					"(2692, \"C�rrego dos Borges (Governador Valadares)\", 11),\r\n" + 
					"(2693, \"C�rrego dos Fernandes (Aracati)\", 6),\r\n" + 
					"(2694, \"C�rrego dos Melqu�ades (Governador Valadares)\", 11),\r\n" + 
					"(2695, \"C�rrego dos Monos (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(2696, \"C�rrego Fundo\", 11),\r\n" + 
					"(2697, \"C�rrego Moacyr �vidos (Governador Lindenberg)\", 8),\r\n" + 
					"(2698, \"C�rrego Novo\", 11),\r\n" + 
					"(2699, \"C�rrego Rico (Planaltina)\", 9),\r\n" + 
					"(2700, \"C�rregos (Concei��o do Mato Dentro)\", 11),\r\n" + 
					"(2701, \"Correia de Almeida (Barbacena)\", 11),\r\n" + 
					"(2702, \"Correia de Freitas (Apucarana)\", 18),\r\n" + 
					"(2703, \"Correia Pinto\", 24),\r\n" + 
					"(2704, \"Correinha (Aragua�na)\", 27),\r\n" + 
					"(2705, \"Corrente\", 17),\r\n" + 
					"(2706, \"Corrente (Jardim)\", 6),\r\n" + 
					"(2707, \"Correntes\", 16),\r\n" + 
					"(2708, \"Correntezas (Silva Jardim)\", 19),\r\n" + 
					"(2709, \"Correntina\", 5),\r\n" + 
					"(2710, \"Correntinho (Guanh�es)\", 11),\r\n" + 
					"(2711, \"Cortado (Novo Cabrais)\", 23),\r\n" + 
					"(2712, \"Corta M�o (Amargosa)\", 5),\r\n" + 
					"(2713, \"Cort�s\", 16),\r\n" + 
					"(2714, \"Corumb�\", 12),\r\n" + 
					"(2715, \"Corumb� de Goi�s\", 9),\r\n" + 
					"(2716, \"Corumba�ba\", 9),\r\n" + 
					"(2717, \"Corumbata�\", 26),\r\n" + 
					"(2718, \"Corumbata� do Sul\", 18),\r\n" + 
					"(2719, \"Corumbiara\", 21),\r\n" + 
					"(2720, \"Corup�\", 24),\r\n" + 
					"(2721, \"Coruripe\", 2),\r\n" + 
					"(2722, \"Coruripe da Cal (Palmeira dos �ndios)\", 2),\r\n" + 
					"(2723, \"Corvo (Guarapuava)\", 18),\r\n" + 
					"(2724, \"Cosm�polis\", 26),\r\n" + 
					"(2725, \"Cosmorama\", 26),\r\n" + 
					"(2726, \"Costa da Cadeia (Triunfo)\", 23),\r\n" + 
					"(2727, \"Costa Machado (Mirante do Paranapanema)\", 26),\r\n" + 
					"(2728, \"Costa Marques\", 21),\r\n" + 
					"(2729, \"Cost�o (Estrela)\", 23),\r\n" + 
					"(2730, \"Costa Rica\", 12),\r\n" + 
					"(2731, \"Costas da Mantiqueira (Barbacena)\", 11),\r\n" + 
					"(2732, \"Costa Sena (Concei��o do Mato Dentro)\", 11),\r\n" + 
					"(2733, \"Costas (Parais�polis)\", 11),\r\n" + 
					"(2734, \"Costeira (Arauc�ria)\", 18),\r\n" + 
					"(2735, \"Cotax� (Ecoporanga)\", 8),\r\n" + 
					"(2736, \"Cotegipe\", 5),\r\n" + 
					"(2737, \"Cotia\", 26),\r\n" + 
					"(2738, \"Cotipor�\", 23),\r\n" + 
					"(2739, \"Cotrel (Guarant� do Norte)\", 13),\r\n" + 
					"(2740, \"Cotrigua�u\", 13),\r\n" + 
					"(2741, \"Couro D'Antas (Riacho das Almas)\", 16),\r\n" + 
					"(2742, \"Couto de Magalh�es\", 27),\r\n" + 
					"(2743, \"Couto de Magalh�es de Minas\", 11),\r\n" + 
					"(2744, \"Coutos (Ilh�us)\", 5),\r\n" + 
					"(2745, \"Cov� (Mangueirinha)\", 18),\r\n" + 
					"(2746, \"Coxilha\", 23),\r\n" + 
					"(2747, \"Coxilha do Lageado (Herval)\", 23),\r\n" + 
					"(2748, \"Coxilha Grande (Vacaria)\", 23),\r\n" + 
					"(2749, \"Coxilha Rica (Itapejara d'Oeste)\", 18),\r\n" + 
					"(2750, \"Coxilha Seca (Tr�s Arroios)\", 23),\r\n" + 
					"(2751, \"Coxim\", 12),\r\n" + 
					"(2752, \"Coxip� A�u (Cuiab�)\", 13),\r\n" + 
					"(2753, \"Coxip� do Ouro (Cuiab�)\", 13),\r\n" + 
					"(2754, \"Coxixola\", 15),\r\n" + 
					"(2755, \"CR-1 (Palmares do Sul)\", 23),\r\n" + 
					"(2756, \"Cra�bas\", 2),\r\n" + 
					"(2757, \"Craol�ndia (Goiatins)\", 27),\r\n" + 
					"(2758, \"Crate�s\", 6),\r\n" + 
					"(2759, \"Crato\", 6),\r\n" + 
					"(2760, \"Cravinhos\", 26),\r\n" + 
					"(2761, \"Cravol�ndia\", 5),\r\n" + 
					"(2762, \"Crici�ma\", 24),\r\n" + 
					"(2763, \"Crioulos (Pereiro)\", 6),\r\n" + 
					"(2764, \"Cripuriz�o (Itaituba)\", 14),\r\n" + 
					"(2765, \"Cripurizinho (Itaituba)\", 14),\r\n" + 
					"(2766, \"Cris�lia (Ouro Fino)\", 11),\r\n" + 
					"(2767, \"Cris�lita\", 11),\r\n" + 
					"(2768, \"Cris�polis\", 5),\r\n" + 
					"(2769, \"Crispim Jaques (Te�filo Otoni)\", 11),\r\n" + 
					"(2770, \"Crissiumal\", 23),\r\n" + 
					"(2771, \"Cristais\", 11),\r\n" + 
					"(2772, \"Cristais (Cascavel)\", 6),\r\n" + 
					"(2773, \"Cristais Paulista\", 26),\r\n" + 
					"(2774, \"Cristal\", 23),\r\n" + 
					"(2775, \"Cristal�ndia\", 27),\r\n" + 
					"(2776, \"Cristal�ndia (Brumado)\", 5),\r\n" + 
					"(2777, \"Cristal�ndia do Piau�\", 17),\r\n" + 
					"(2778, \"Cristal do Norte (Pedro Can�rio)\", 8),\r\n" + 
					"(2779, \"Cristal do Sul\", 23),\r\n" + 
					"(2780, \"Crist�lia\", 11),\r\n" + 
					"(2781, \"Crist�lia (Petrolina)\", 16),\r\n" + 
					"(2782, \"Cristalina\", 9),\r\n" + 
					"(2783, \"Cristalina (Caarap�)\", 12),\r\n" + 
					"(2784, \"Cristiano Otoni\", 11),\r\n" + 
					"(2785, \"Cristian�polis\", 9),\r\n" + 
					"(2786, \"Cristina\", 11),\r\n" + 
					"(2787, \"Cristin�polis\", 25),\r\n" + 
					"(2788, \"Cristino Castro\", 17),\r\n" + 
					"(2789, \"Cristin�polis (Salto do C�u)\", 13),\r\n" + 
					"(2790, \"Crist�polis\", 5),\r\n" + 
					"(2791, \"Cristo Rei (Capanema)\", 18),\r\n" + 
					"(2792, \"Cri�va (Caxias do Sul)\", 23),\r\n" + 
					"(2793, \"Crix�s\", 9),\r\n" + 
					"(2794, \"Crix�s do Tocantins\", 27),\r\n" + 
					"(2795, \"Crix�s (Gurupi)\", 27),\r\n" + 
					"(2796, \"Croat�\", 6),\r\n" + 
					"(2797, \"Croat� (S�o Gon�alo do Amarante)\", 6),\r\n" + 
					"(2798, \"Croat� (Varjota)\", 6),\r\n" + 
					"(2799, \"Crom�nia\", 9),\r\n" + 
					"(2800, \"Cruanji (Timba�ba)\", 16),\r\n" + 
					"(2801, \"Crubix� (Alfredo Chaves)\", 8),\r\n" + 
					"(2802, \"Crucil�ndia\", 11),\r\n" + 
					"(2803, \"Crussa� (Castro Alves)\", 5),\r\n" + 
					"(2804, \"Cruxati (Itapipoca)\", 6),\r\n" + 
					"(2805, \"Cruz\", 6),\r\n" + 
					"(2806, \"Cruz�lia\", 26),\r\n" + 
					"(2807, \"Cruz Alta\", 23),\r\n" + 
					"(2808, \"Cruzaltense\", 23),\r\n" + 
					"(2809, \"Cruzaltina (Douradina)\", 12),\r\n" + 
					"(2810, \"Cruz das Almas\", 5),\r\n" + 
					"(2811, \"Cruz de Pedra (Iguatu)\", 6),\r\n" + 
					"(2812, \"Cruz do Esp�rito Santo\", 15),\r\n" + 
					"(2813, \"Cruzeiro\", 26),\r\n" + 
					"(2814, \"Cruzeiro da Fortaleza\", 11),\r\n" + 
					"(2815, \"Cruzeiro do Igua�u\", 18),\r\n" + 
					"(2816, \"Cruzeiro do Nordeste (Sert�nia)\", 16),\r\n" + 
					"(2817, \"Cruzeiro do Norte (Porangatu)\", 9),\r\n" + 
					"(2818, \"Cruzeiro do Norte (Ura�)\", 18),\r\n" + 
					"(2819, \"Cruzeiro do Oeste\", 18),\r\n" + 
					"(2820, \"Cruzeiro dos Peixotos (Uberl�ndia)\", 11),\r\n" + 
					"(2821, \"Cruzeiro do Sul\", 1),\r\n" + 
					"(2822, \"Cruzeiro do Sul\", 18),\r\n" + 
					"(2823, \"Cruzeiro do Sul\", 23),\r\n" + 
					"(2824, \"Cruzeiro (Planalto)\", 23),\r\n" + 
					"(2825, \"Cruzes (Panelas)\", 16),\r\n" + 
					"(2826, \"Cruzeta\", 20),\r\n" + 
					"(2827, \"Cruz�lia\", 11),\r\n" + 
					"(2828, \"Cruzinha (Minas Novas)\", 11),\r\n" + 
					"(2829, \"Cruz (Itapaj�)\", 6),\r\n" + 
					"(2830, \"Cruz Machado\", 18),\r\n" + 
					"(2831, \"Cruzmaltina\", 18),\r\n" + 
					"(2832, \"Cubas (Ferros)\", 11),\r\n" + 
					"(2833, \"Cubat�o\", 26),\r\n" + 
					"(2834, \"Cubati\", 15),\r\n" + 
					"(2835, \"Cucu� (S�o Gabriel da Cachoeira)\", 3),\r\n" + 
					"(2836, \"Cuiab�\", 13),\r\n" + 
					"(2837, \"Cuiab� Paulista (Mirante do Paranapanema)\", 26),\r\n" + 
					"(2838, \"Cuiambuca (Gameleira)\", 16),\r\n" + 
					"(2839, \"Cuieiras (Igarassu)\", 16),\r\n" + 
					"(2840, \"Cuit�\", 15),\r\n" + 
					"(2841, \"Cuit� de Mamanguape\", 15),\r\n" + 
					"(2842, \"Cuitegi\", 15),\r\n" + 
					"(2843, \"Cuit� Velho (Conselheiro Pena)\", 11),\r\n" + 
					"(2844, \"Cui�-Cui� (Itaituba)\", 14),\r\n" + 
					"(2845, \"Cujubim\", 21),\r\n" + 
					"(2846, \"Culturama (F�tima do Sul)\", 12),\r\n" + 
					"(2847, \"Cumari\", 9),\r\n" + 
					"(2848, \"Cumaru\", 16),\r\n" + 
					"(2849, \"Cumaru do Norte\", 14),\r\n" + 
					"(2850, \"Cumbe\", 25),\r\n" + 
					"(2851, \"Cumuruxatiba (Prado)\", 5),\r\n" + 
					"(2852, \"Cunani (Cal�oene)\", 4),\r\n" + 
					"(2853, \"Cuncas (Barro)\", 6),\r\n" + 
					"(2854, \"Cunha\", 26),\r\n" + 
					"(2855, \"Cunhangi (Jaguaripe)\", 5),\r\n" + 
					"(2856, \"Cunha Por�\", 24),\r\n" + 
					"(2857, \"Cunhaporanga (Castro)\", 18),\r\n" + 
					"(2858, \"Cunhata�\", 24),\r\n" + 
					"(2859, \"Cuparaque\", 11),\r\n" + 
					"(2860, \"Cupins (Aparecida do Taboado)\", 12),\r\n" + 
					"(2861, \"Cupira\", 16),\r\n" + 
					"(2862, \"Cupissura (Caapor�)\", 15),\r\n" + 
					"(2863, \"Cura��\", 5),\r\n" + 
					"(2864, \"Curatis (Tamboril)\", 6),\r\n" + 
					"(2865, \"Curia� (Macap�)\", 4),\r\n" + 
					"(2866, \"Curimat�\", 17),\r\n" + 
					"(2867, \"Curimata� (Buen�polis)\", 11),\r\n" + 
					"(2868, \"Curion�polis\", 14),\r\n" + 
					"(2869, \"Curitiba\", 18),\r\n" + 
					"(2870, \"Curitibanos\", 24),\r\n" + 
					"(2871, \"Curi�va\", 18),\r\n" + 
					"(2872, \"Currais\", 17),\r\n" + 
					"(2873, \"Currais Novos\", 20),\r\n" + 
					"(2874, \"Curral Alto (Santa Vit�ria do Palmar)\", 23),\r\n" + 
					"(2875, \"Curral de Cima\", 15),\r\n" + 
					"(2876, \"Curral de Dentro\", 11),\r\n" + 
					"(2877, \"Curral Falso (Ribeira do Pombal)\", 5),\r\n" + 
					"(2878, \"Curralinho\", 14),\r\n" + 
					"(2879, \"Curralinhos\", 17),\r\n" + 
					"(2880, \"Curral Novo do Piau�\", 17),\r\n" + 
					"(2881, \"Curral Queimado (Petrolina)\", 16),\r\n" + 
					"(2882, \"Curral Velho\", 15),\r\n" + 
					"(2883, \"Curral Velho (Crate�s)\", 6),\r\n" + 
					"(2884, \"Curu�\", 14),\r\n" + 
					"(2885, \"Curua� (Santar�m)\", 14),\r\n" + 
					"(2886, \"Curu��\", 14),\r\n" + 
					"(2887, \"Curucaca (Guarapuava)\", 18),\r\n" + 
					"(2888, \"Curu�ambaba (Camet�)\", 14),\r\n" + 
					"(2889, \"Curumim (Cap�o da Canoa)\", 23),\r\n" + 
					"(2890, \"Curumu (Breves)\", 14),\r\n" + 
					"(2891, \"Curupa (Alto Parna�ba)\", 10),\r\n" + 
					"(2892, \"Curup� (Tabatinga)\", 26),\r\n" + 
					"(2893, \"Curupira (Ocara)\", 6),\r\n" + 
					"(2894, \"Cururupu\", 10),\r\n" + 
					"(2895, \"Curva Grande (Santa Helena)\", 10),\r\n" + 
					"(2896, \"Curvel�ndia\", 13),\r\n" + 
					"(2897, \"Curvelo\", 11),\r\n" + 
					"(2898, \"Cust�dia\", 16),\r\n" + 
					"(2899, \"Cust�dio Lima (Magalh�es de Almeida)\", 10),\r\n" + 
					"(2900, \"Cust�dio (Quixad�)\", 6),\r\n" + 
					"(2901, \"Cutias\", 4),\r\n" + 
					"(2902, \"Dalas (Palmeira D'Oeste)\", 26),\r\n" + 
					"(2903, \"Dalb�rgia (Ibirama)\", 24),\r\n" + 
					"(2904, \"Dal Pai (Campos Novos)\", 24),\r\n" + 
					"(2905, \"Daltro Filho (Garibaldi)\", 23),\r\n" + 
					"(2906, \"Daltro Filho (Imigrante)\", 23),\r\n" + 
					"(2907, \"Daltro Filho (Tenente Portela)\", 23),\r\n" + 
					"(2908, \"Damian�polis\", 9),\r\n" + 
					"(2909, \"Dami�o\", 15),\r\n" + 
					"(2910, \"Dami�o Carneiro (Quixeramobim)\", 6),\r\n" + 
					"(2911, \"Damol�ndia\", 9),\r\n" + 
					"(2912, \"Daniel de Queir�s (Quixad�)\", 6),\r\n" + 
					"(2913, \"Dantel�ndia (Vit�ria da Conquista)\", 5),\r\n" + 
					"(2914, \"Darcin�polis\", 27),\r\n" + 
					"(2915, \"D�rio Lassance (Bag�)\", 23),\r\n" + 
					"(2916, \"D�rio Meira\", 5),\r\n" + 
					"(2917, \"Datas\", 11),\r\n" + 
					"(2918, \"David Canabarro\", 23),\r\n" + 
					"(2919, \"David�polis (Goi�s)\", 9),\r\n" + 
					"(2920, \"Davin�polis\", 10),\r\n" + 
					"(2921, \"Davin�polis\", 9),\r\n" + 
					"(2922, \"Debrasa (Brasil�ndia)\", 12),\r\n" + 
					"(2923, \"Delfim Moreira\", 11),\r\n" + 
					"(2924, \"Delfina (Estrela)\", 23),\r\n" + 
					"(2925, \"Delfino (Campo Formoso)\", 5),\r\n" + 
					"(2926, \"Delfin�polis\", 11),\r\n" + 
					"(2927, \"Delmiro Gouveia\", 2),\r\n" + 
					"(2928, \"Delmiro Gouveia (Pires Ferreira)\", 6),\r\n" + 
					"(2929, \"Delta\", 11),\r\n" + 
					"(2930, \"Demarca��o (Porto Velho)\", 21),\r\n" + 
					"(2931, \"Demerval Lob�o\", 17),\r\n" + 
					"(2932, \"Denise\", 13),\r\n" + 
					"(2933, \"Deod�polis\", 12),\r\n" + 
					"(2934, \"Deodoro (Ven�ncio Aires)\", 23),\r\n" + 
					"(2935, \"Dep�sito (Espumoso)\", 23),\r\n" + 
					"(2936, \"Deputado Augusto Clementino (Serro)\", 11),\r\n" + 
					"(2937, \"Deputado Irapuan Pinheiro\", 6),\r\n" + 
					"(2938, \"Deputado Jos� Afonso (Paranava�)\", 18),\r\n" + 
					"(2939, \"Derribadinha (Governador Valadares)\", 11),\r\n" + 
					"(2940, \"Derrubadas\", 23),\r\n" + 
					"(2941, \"Descalvado\", 26),\r\n" + 
					"(2942, \"Descanso\", 24),\r\n" + 
					"(2943, \"Descoberto\", 11),\r\n" + 
					"(2944, \"Descoberto (Coribe)\", 5),\r\n" + 
					"(2945, \"Desembargador Otoni (Diamantina)\", 11),\r\n" + 
					"(2946, \"Desemboque (Sacramento)\", 11),\r\n" + 
					"(2947, \"Deserto (Itapipoca)\", 6),\r\n" + 
					"(2948, \"Despique (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(2949, \"Despraiado (Tunas)\", 23),\r\n" + 
					"(2950, \"Desterro\", 15),\r\n" + 
					"(2951, \"Desterro de Entre Rios\", 11),\r\n" + 
					"(2952, \"Desterro do Melo\", 11),\r\n" + 
					"(2953, \"Dez de Maio (Toledo)\", 18),\r\n" + 
					"(2954, \"Dezesseis de Novembro\", 23),\r\n" + 
					"(2955, \"Diadema\", 26),\r\n" + 
					"(2956, \"Diamante\", 15),\r\n" + 
					"(2957, \"Diamante de Ub� (Ub�)\", 11),\r\n" + 
					"(2958, \"Diamante d'Oeste\", 18),\r\n" + 
					"(2959, \"Diamante do Norte\", 18),\r\n" + 
					"(2960, \"Diamante do Sul\", 18),\r\n" + 
					"(2961, \"Diamantina\", 11),\r\n" + 
					"(2962, \"Diamantino\", 13),\r\n" + 
					"(2963, \"Dian�polis\", 27),\r\n" + 
					"(2964, \"Dias (Bras�polis)\", 11),\r\n" + 
					"(2965, \"Dias Coelho (Morro do Chap�u)\", 5),\r\n" + 
					"(2966, \"Dias D'�vila\", 5),\r\n" + 
					"(2967, \"Dias Tavares (Juiz de Fora)\", 11),\r\n" + 
					"(2968, \"Dilermando de Aguiar\", 23),\r\n" + 
					"(2969, \"Di�genes Sampaio (Amargosa)\", 5),\r\n" + 
					"(2970, \"Diogo de Vasconcelos\", 11),\r\n" + 
					"(2971, \"Diol�ndia (Itapuranga)\", 9),\r\n" + 
					"(2972, \"Dion�sio\", 11),\r\n" + 
					"(2973, \"Dion�sio Cerqueira\", 24),\r\n" + 
					"(2974, \"Diorama\", 9),\r\n" + 
					"(2975, \"Dirce Reis\", 26),\r\n" + 
					"(2976, \"Dirceu Arcoverde\", 17),\r\n" + 
					"(2977, \"Divina Pastora\", 25),\r\n" + 
					"(2978, \"Divin�sia\", 11),\r\n" + 
					"(2979, \"Divino\", 11),\r\n" + 
					"(2980, \"Divino das Laranjeiras\", 11),\r\n" + 
					"(2981, \"Divino de S�o Louren�o\", 8),\r\n" + 
					"(2982, \"Divino de Virgol�ndia (Virgol�ndia)\", 11),\r\n" + 
					"(2983, \"Divino Esp�rito Santo (Alterosa)\", 11),\r\n" + 
					"(2984, \"Divino Esp�rito Santo (S�o Jos� do Cal�ado)\", 8),\r\n" + 
					"(2985, \"Divinol�ndia\", 26),\r\n" + 
					"(2986, \"Divinol�ndia de Minas\", 11),\r\n" + 
					"(2987, \"Divino (Planalto)\", 23),\r\n" + 
					"(2988, \"Divin�polis\", 11),\r\n" + 
					"(2989, \"Divin�polis de Goi�s\", 9),\r\n" + 
					"(2990, \"Divin�polis do Tocantins\", 27),\r\n" + 
					"(2991, \"Divisa Alegre\", 11),\r\n" + 
					"(2992, \"Divisa Nova\", 11),\r\n" + 
					"(2993, \"Divis�polis\", 11),\r\n" + 
					"(2994, \"Djalma Coutinho (Santa Leopoldina)\", 8),\r\n" + 
					"(2995, \"Dobrada\", 26),\r\n" + 
					"(2996, \"Doce Grande (Quitandinha)\", 18),\r\n" + 
					"(2997, \"Dois C�rregos\", 26),\r\n" + 
					"(2998, \"Dois de Abril (Palm�polis)\", 11),\r\n" + 
					"(2999, \"Dois Irm�os\", 23),\r\n" + 
					"(3000, \"Dois Irm�os das Miss�es\", 23),\r\n" + 
					"(3001, \"Dois Irm�os do Buriti\", 12),\r\n" + 
					"(3002, \"Dois Irm�os do Tocantins\", 27),\r\n" + 
					"(3003, \"Dois Irm�os (S�o Jo�o)\", 18),\r\n" + 
					"(3004, \"Dois Irm�os (Toledo)\", 18),\r\n" + 
					"(3005, \"Dois Lajeados\", 23),\r\n" + 
					"(3006, \"Dois Le�es (Pombos)\", 16),\r\n" + 
					"(3007, \"Dois Marcos (Toledo)\", 18),\r\n" + 
					"(3008, \"Dois Riachos\", 2),\r\n" + 
					"(3009, \"Dois Vizinhos\", 18),\r\n" + 
					"(3010, \"Dolcin�polis\", 26),\r\n" + 
					"(3011, \"Dom Aquino\", 13),\r\n" + 
					"(3012, \"Dom Bas�lio\", 5),\r\n" + 
					"(3013, \"Dom Bosco\", 11),\r\n" + 
					"(3014, \"Dom Cavati\", 11),\r\n" + 
					"(3015, \"Dom�lia (Agudos)\", 26),\r\n" + 
					"(3016, \"Dom Eliseu\", 14),\r\n" + 
					"(3017, \"Dom Expedito Lopes\", 17),\r\n" + 
					"(3018, \"Dom Feliciano\", 23),\r\n" + 
					"(3019, \"Dom Feliciano (Gravata�)\", 23),\r\n" + 
					"(3020, \"Domiciano Ribeiro (Ipameri)\", 9),\r\n" + 
					"(3021, \"Domingos da Costa (Boa Viagem)\", 6),\r\n" + 
					"(3022, \"Domingos Martins\", 8),\r\n" + 
					"(3023, \"Domingos Mour�o\", 17),\r\n" + 
					"(3024, \"Dom Inoc�ncio\", 17),\r\n" + 
					"(3025, \"Dom Joaquim\", 11),\r\n" + 
					"(3026, \"Dom Lara (Caratinga)\", 11),\r\n" + 
					"(3027, \"Dom Leme (Santana do Cariri)\", 6),\r\n" + 
					"(3028, \"Dom Macedo Costa\", 5),\r\n" + 
					"(3029, \"Dom Maur�cio (Quixad�)\", 6),\r\n" + 
					"(3030, \"Dom Modesto (Caratinga)\", 11),\r\n" + 
					"(3031, \"Dom Pedrito\", 23),\r\n" + 
					"(3032, \"Dom Pedro\", 10),\r\n" + 
					"(3033, \"Dom Pedro de Alc�ntara\", 23),\r\n" + 
					"(3034, \"Dom Quintino (Crato)\", 6),\r\n" + 
					"(3035, \"Dom Rodrigo (Campo Largo)\", 18),\r\n" + 
					"(3036, \"Dom Silv�rio\", 11),\r\n" + 
					"(3037, \"Dom Vi�oso\", 11),\r\n" + 
					"(3038, \"Dona Am�rica (Mimoso do Sul)\", 8),\r\n" + 
					"(3039, \"Dona Emma\", 24),\r\n" + 
					"(3040, \"Dona Euz�bia\", 11),\r\n" + 
					"(3041, \"Dona Francisca\", 23),\r\n" + 
					"(3042, \"Dona In�s\", 15),\r\n" + 
					"(3043, \"Dona Maria (Olindina)\", 5),\r\n" + 
					"(3044, \"Dona Ot�lia (Roque Gonzales)\", 23),\r\n" + 
					"(3045, \"Donato (Pires Ferreira)\", 6),\r\n" + 
					"(3046, \"Dor�ndia (Barra do Pira�)\", 19),\r\n" + 
					"(3047, \"Dores da Vit�ria (Mira�)\", 11),\r\n" + 
					"(3048, \"Dores de Campos\", 11),\r\n" + 
					"(3049, \"Dores de Guanh�es\", 11),\r\n" + 
					"(3050, \"Dores de Macabu (Campos dos Goytacazes)\", 19),\r\n" + 
					"(3051, \"Dores do Indai�\", 11),\r\n" + 
					"(3052, \"Dores do Paraibuna (Santos Dumont)\", 11),\r\n" + 
					"(3053, \"Dores do Rio Preto\", 8),\r\n" + 
					"(3054, \"Dores do Turvo\", 11),\r\n" + 
					"(3055, \"Dores�polis\", 11),\r\n" + 
					"(3056, \"Dorizon (Mallet)\", 18),\r\n" + 
					"(3057, \"Dormentes\", 16),\r\n" + 
					"(3058, \"Douradilho (Barra do Ribeiro)\", 23),\r\n" + 
					"(3059, \"Douradina\", 12),\r\n" + 
					"(3060, \"Douradina\", 18),\r\n" + 
					"(3061, \"Douradinho (Machado)\", 11),\r\n" + 
					"(3062, \"Dourado\", 26),\r\n" + 
					"(3063, \"Dourado (Aratiba)\", 23),\r\n" + 
					"(3064, \"Dourado (Guai�ba)\", 6),\r\n" + 
					"(3065, \"Dourado (Horizonte)\", 6),\r\n" + 
					"(3066, \"Douradoquara\", 11),\r\n" + 
					"(3067, \"Dourados\", 12),\r\n" + 
					"(3068, \"Doutor Ant�nio Paranhos (S�o Jorge D'Oeste)\", 18),\r\n" + 
					"(3069, \"Doutor Camargo\", 18),\r\n" + 
					"(3070, \"Doutor Campolina (Jequitib�)\", 11),\r\n" + 
					"(3071, \"Doutor Edgardo Pereira Velho (Mostardas)\", 23),\r\n" + 
					"(3072, \"Doutor Elias (Trajano de Moraes)\", 19),\r\n" + 
					"(3073, \"Doutor Ernesto (Toledo)\", 18),\r\n" + 
					"(3074, \"Doutor Loreti (Santa Maria Madalena)\", 19),\r\n" + 
					"(3075, \"Doutor Lund (Pedro Leopoldo)\", 11),\r\n" + 
					"(3076, \"Doutor Maur�cio Cardoso\", 23),\r\n" + 
					"(3077, \"Doutor Oliveira Castro (Gua�ra)\", 18),\r\n" + 
					"(3078, \"Doutor Pedrinho\", 24),\r\n" + 
					"(3079, \"Doutor Ricardo\", 23),\r\n" + 
					"(3080, \"Doutor Severiano\", 20),\r\n" + 
					"(3081, \"Doutor Ulysses\", 18),\r\n" + 
					"(3082, \"Doverl�ndia\", 9),\r\n" + 
					"(3083, \"Dracena\", 26),\r\n" + 
					"(3084, \"Duartina\", 26),\r\n" + 
					"(3085, \"Duas Barras\", 19),\r\n" + 
					"(3086, \"Duas Barras do Morro (Morro do Chap�u)\", 5),\r\n" + 
					"(3087, \"Duas Barras (Iconha)\", 8),\r\n" + 
					"(3088, \"Duas Estradas\", 15),\r\n" + 
					"(3089, \"Duer�\", 27),\r\n" + 
					"(3090, \"Dumont\", 26),\r\n" + 
					"(3091, \"Duplo C�u (Palestina)\", 26),\r\n" + 
					"(3092, \"Duque Bacelar\", 10),\r\n" + 
					"(3093, \"Duque de Caxias\", 19),\r\n" + 
					"(3094, \"Durand�\", 11),\r\n" + 
					"(3095, \"Durasnal (Alegrete)\", 23),\r\n" + 
					"(3096, \"Ebron (Acopiara)\", 6),\r\n" + 
					"(3097, \"Echapor�\", 26),\r\n" + 
					"(3098, \"Ecoporanga\", 8),\r\n" + 
					"(3099, \"Edealina\", 9),\r\n" + 
					"(3100, \"Ed�ia\", 9),\r\n" + 
					"(3101, \"Edgard Melo (Itanhomi)\", 11),\r\n" + 
					"(3102, \"Edil�ndia (Cocalzinho de Goi�s)\", 9),\r\n" + 
					"(3103, \"Eduardo Xavier da Silva (Jaguaria�va)\", 18),\r\n" + 
					"(3104, \"Eirunep�\", 3),\r\n" + 
					"(3105, \"Eldorado\", 12),\r\n" + 
					"(3106, \"Eldorado\", 26),\r\n" + 
					"(3107, \"Eldorado dos Caraj�s\", 14),\r\n" + 
					"(3108, \"Eldorado do Sul\", 23),\r\n" + 
					"(3109, \"Elesb�o Veloso\", 17),\r\n" + 
					"(3110, \"Eletra (S�o Francisco de Paula)\", 23),\r\n" + 
					"(3111, \"Eleut�rio (Itapira)\", 26),\r\n" + 
					"(3112, \"Elias Fausto\", 26),\r\n" + 
					"(3113, \"Eliseu Martins\", 17),\r\n" + 
					"(3114, \"Elisi�rio\", 26),\r\n" + 
					"(3115, \"El�sio Medrado\", 5),\r\n" + 
					"(3116, \"Eliza (Xambr�)\", 18),\r\n" + 
					"(3117, \"El�i Mendes\", 11),\r\n" + 
					"(3118, \"Ema (Iracema)\", 6),\r\n" + 
					"(3119, \"Ema (Pindoretama)\", 6),\r\n" + 
					"(3120, \"Emas\", 15),\r\n" + 
					"(3121, \"Ematuba (Independ�ncia)\", 6),\r\n" + 
					"(3122, \"Emba�ba\", 26),\r\n" + 
					"(3123, \"Emboabas (S�o Jo�o Del Rei)\", 11),\r\n" + 
					"(3124, \"Embogua�u (Paranagu�)\", 18),\r\n" + 
					"(3125, \"Emboque (Piraquara)\", 18),\r\n" + 
					"(3126, \"Emboque (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(3127, \"Embora� (Augusto Corr�a)\", 14),\r\n" + 
					"(3128, \"Embu das Artes\", 26),\r\n" + 
					"(3129, \"Embu-Gua�u\", 26),\r\n" + 
					"(3130, \"Emilian�polis\", 26),\r\n" + 
					"(3131, \"Encantado\", 23),\r\n" + 
					"(3132, \"Encantado d'Oeste (Assis Chateaubriand)\", 18),\r\n" + 
					"(3133, \"Encantado (Quixeramobim)\", 6),\r\n" + 
					"(3134, \"Encanto\", 20),\r\n" + 
					"(3135, \"Encruzilhada\", 5),\r\n" + 
					"(3136, \"Encruzilhada do Sul\", 23),\r\n" + 
					"(3137, \"Encruzilhada (Guarapuava)\", 18),\r\n" + 
					"(3138, \"Encruzilhada (Ma�ambar�)\", 23),\r\n" + 
					"(3139, \"Encruzilhada (Pato Branco)\", 18),\r\n" + 
					"(3140, \"Encruzilhada S�o Sebasti�o (Muitos Cap�es)\", 23),\r\n" + 
					"(3141, \"En�as Marques\", 18),\r\n" + 
					"(3142, \"Eneida (Presidente Prudente)\", 26),\r\n" + 
					"(3143, \"Engenheiro �vidos (Cajazeiras)\", 15),\r\n" + 
					"(3144, \"Engenheiro Baldu�no (Monte Apraz�vel)\", 26),\r\n" + 
					"(3145, \"Engenheiro Beltr�o\", 18),\r\n" + 
					"(3146, \"Engenheiro Caldas\", 11),\r\n" + 
					"(3147, \"Engenheiro Coelho\", 26),\r\n" + 
					"(3148, \"Engenheiro Correia (Ouro Preto)\", 11),\r\n" + 
					"(3149, \"Engenheiro Dolabela (Bocai�va)\", 11),\r\n" + 
					"(3150, \"Engenheiro Fran�a (Uba�ra)\", 5),\r\n" + 
					"(3151, \"Engenheiro Jo�o Tom� (Ipueiras)\", 6),\r\n" + 
					"(3152, \"Engenheiro Jos� Lopes (Senador Pompeu)\", 6),\r\n" + 
					"(3153, \"Engenheiro Luiz Englert (Sert�o)\", 23),\r\n" + 
					"(3154, \"Engenheiro Maia (Itaber�)\", 26),\r\n" + 
					"(3155, \"Engenheiro Navarro\", 11),\r\n" + 
					"(3156, \"Engenheiro Passos (Resende)\", 19),\r\n" + 
					"(3157, \"Engenheiro Paulo de Frontin\", 19),\r\n" + 
					"(3158, \"Engenheiro Pontes (Laje)\", 5),\r\n" + 
					"(3159, \"Engenheiro Schnoor (Ara�ua�)\", 11),\r\n" + 
					"(3160, \"Engenho (Acorizal)\", 13),\r\n" + 
					"(3161, \"Engenho do Ribeiro (Bom Despacho)\", 11),\r\n" + 
					"(3162, \"Engenho Grande (�gua Santa)\", 23),\r\n" + 
					"(3163, \"Engenho Novo (Mar de Espanha)\", 11),\r\n" + 
					"(3164, \"Engenho Velho\", 23),\r\n" + 
					"(3165, \"Engenho Velho (Barro)\", 6),\r\n" + 
					"(3166, \"Engenho Velho (Conc�rdia)\", 24),\r\n" + 
					"(3167, \"Engenho Velho (Santo Ant�nio do Leverger)\", 13),\r\n" + 
					"(3168, \"Entre Folhas\", 11),\r\n" + 
					"(3169, \"Entre-Iju�s\", 23),\r\n" + 
					"(3170, \"Entremontes (Piranhas)\", 2),\r\n" + 
					"(3171, \"Entrepelado (Taquara)\", 23),\r\n" + 
					"(3172, \"Entre Rios\", 5),\r\n" + 
					"(3173, \"Entre Rios\", 24),\r\n" + 
					"(3174, \"Entre Rios de Minas\", 11),\r\n" + 
					"(3175, \"Entre Rios (Dom Aquino)\", 13),\r\n" + 
					"(3176, \"Entre Rios do Oeste\", 18),\r\n" + 
					"(3177, \"Entre Rios do Sul\", 23),\r\n" + 
					"(3178, \"Entre Rios (Guarapuava)\", 18),\r\n" + 
					"(3179, \"Entre Rios (Nova Ubirat�)\", 13),\r\n" + 
					"(3180, \"Entroncamento (Lagoa dos Gatos)\", 16),\r\n" + 
					"(3181, \"Envira\", 3),\r\n" + 
					"(3182, \"Enxovia (Monte Alegre dos Campos)\", 23),\r\n" + 
					"(3183, \"Epaminondas Otoni (Carlos Chagas)\", 11),\r\n" + 
					"(3184, \"Epitaciol�ndia\", 1),\r\n" + 
					"(3185, \"Equador\", 20),\r\n" + 
					"(3186, \"Erebango\", 23),\r\n" + 
					"(3187, \"Erechim\", 23),\r\n" + 
					"(3188, \"Erer�\", 6),\r\n" + 
					"(3189, \"�rico Cardoso\", 5),\r\n" + 
					"(3190, \"Ermidinha (Montes Claros)\", 11),\r\n" + 
					"(3191, \"Ermo\", 24),\r\n" + 
					"(3192, \"Ernestina\", 23),\r\n" + 
					"(3193, \"Ernesto Alves (Santiago)\", 23),\r\n" + 
					"(3194, \"Erval Grande\", 23),\r\n" + 
					"(3195, \"Erv�lia\", 11),\r\n" + 
					"(3196, \"Erval Seco\", 23),\r\n" + 
					"(3197, \"Erval Velho\", 24),\r\n" + 
					"(3198, \"Escada\", 16),\r\n" + 
					"(3199, \"Escondido (Aragua�na)\", 27),\r\n" + 
					"(3200, \"Esmeralda\", 23),\r\n" + 
					"(3201, \"Esmeralda (Rubin�ia)\", 26),\r\n" + 
					"(3202, \"Esmeraldas\", 11),\r\n" + 
					"(3203, \"Esmeraldas de Ferros (Ferros)\", 11),\r\n" + 
					"(3204, \"Espacinha (Nova Russas)\", 6),\r\n" + 
					"(3205, \"Espera Feliz\", 11),\r\n" + 
					"(3206, \"Esperan�a\", 15),\r\n" + 
					"(3207, \"Esperan�a (Canind�)\", 6),\r\n" + 
					"(3208, \"Esperan�a D'Oeste (Caiabu)\", 26),\r\n" + 
					"(3209, \"Esperan�a do Norte (Alvorada do Sul)\", 18),\r\n" + 
					"(3210, \"Esperan�a do Sul\", 23),\r\n" + 
					"(3211, \"Esperan�a Nova\", 18),\r\n" + 
					"(3212, \"Esperan�a (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(3213, \"Esperantina\", 27),\r\n" + 
					"(3214, \"Esperantina\", 17),\r\n" + 
					"(3215, \"Esperantin�polis\", 10),\r\n" + 
					"(3216, \"Espig�o Alto (Barrac�o)\", 23),\r\n" + 
					"(3217, \"Espig�o Alto do Igua�u\", 18),\r\n" + 
					"(3218, \"Espig�o do Oeste\", 21),\r\n" + 
					"(3219, \"Espig�o (Regente Feij�)\", 26),\r\n" + 
					"(3220, \"Espig�o (Viam�o)\", 23),\r\n" + 
					"(3221, \"Espinilho (Campos Novos)\", 24),\r\n" + 
					"(3222, \"Espinilho Grande (Tupanciret�)\", 23),\r\n" + 
					"(3223, \"Espinosa\", 11),\r\n" + 
					"(3224, \"Esp�rito Santo\", 20),\r\n" + 
					"(3225, \"Esp�rito Santo (Alegria)\", 23),\r\n" + 
					"(3226, \"Esp�rito Santo do Dourado\", 11),\r\n" + 
					"(3227, \"Esp�rito Santo do Pinhal\", 26),\r\n" + 
					"(3228, \"Esp�rito Santo do Tau� (Santo Ant�nio do Tau�)\", 14),\r\n" + 
					"(3229, \"Esp�rito Santo do Turvo\", 26),\r\n" + 
					"(3230, \"Esp�rito Santo (Londrina)\", 18),\r\n" + 
					"(3231, \"Esp�rito Santo (S�o Bento do Una)\", 16),\r\n" + 
					"(3232, \"Esplanada\", 5),\r\n" + 
					"(3233, \"Espumoso\", 23),\r\n" + 
					"(3234, \"Esquina Ara�jo (Independ�ncia)\", 23),\r\n" + 
					"(3235, \"Esquina Bom Sucesso (Catu�pe)\", 23),\r\n" + 
					"(3236, \"Esquina Brasil Neves (Catu�pe)\", 23),\r\n" + 
					"(3237, \"Esquina Ga�cha (Crissiumal)\", 23),\r\n" + 
					"(3238, \"Esquina Ga�cha (Entre-Iju�s)\", 23),\r\n" + 
					"(3239, \"Esquina Ipiranga (Giru�)\", 23),\r\n" + 
					"(3240, \"Esquina Piratini (Bossoroca)\", 23),\r\n" + 
					"(3241, \"Esta��o\", 23),\r\n" + 
					"(3242, \"Esta��o Cocal (Morro da Fuma�a)\", 24),\r\n" + 
					"(3243, \"Esta��o General L�cio (Arauc�ria)\", 18),\r\n" + 
					"(3244, \"Esta��o Ro�a Nova (Piraquara)\", 18),\r\n" + 
					"(3245, \"Est�ncia\", 25),\r\n" + 
					"(3246, \"Est�ncia Grande (Alvorada)\", 23),\r\n" + 
					"(3247, \"Est�ncia Nova (Ven�ncio Aires)\", 23),\r\n" + 
					"(3248, \"Est�ncia Velha\", 23),\r\n" + 
					"(3249, \"Est�ncia Velha (Tramanda�)\", 23),\r\n" + 
					"(3250, \"Estandarte (C�ndido Mendes)\", 10),\r\n" + 
					"(3251, \"Esteio\", 23),\r\n" + 
					"(3252, \"Esteios (Luz)\", 11),\r\n" + 
					"(3254, \"Estev�o de Ara�jo (Araponga)\", 11),\r\n" + 
					"(3255, \"Estir�o do Equador (Atalaia do Norte)\", 3),\r\n" + 
					"(3256, \"Estiva\", 11),\r\n" + 
					"(3257, \"Estiva Gerbi\", 26),\r\n" + 
					"(3258, \"Estrada Nova (Itaocara)\", 19),\r\n" + 
					"(3259, \"Estreito\", 10),\r\n" + 
					"(3260, \"Estreito (S�o Jos� do Norte)\", 23),\r\n" + 
					"(3261, \"Estrela\", 23),\r\n" + 
					"(3262, \"Estrela da Barra (Carneirinho)\", 11),\r\n" + 
					"(3263, \"Estrela Dalva\", 11),\r\n" + 
					"(3264, \"Estrela de Alagoas\", 2),\r\n" + 
					"(3265, \"Estrela de Jord�nia (Jord�nia)\", 11),\r\n" + 
					"(3266, \"Estrela D'Oeste\", 26),\r\n" + 
					"(3267, \"Estrela do Indai�\", 11),\r\n" + 
					"(3268, \"Estrela do Leste (Guiratinga)\", 13),\r\n" + 
					"(3269, \"Estrela do Norte\", 26),\r\n" + 
					"(3270, \"Estrela do Norte\", 9),\r\n" + 
					"(3271, \"Estrela do Norte (Castelo)\", 8),\r\n" + 
					"(3272, \"Estrela do Sul\", 11),\r\n" + 
					"(3274, \"Estrela Velha\", 23),\r\n" + 
					"(3275, \"Euclidel�ndia (Cantagalo)\", 19),\r\n" + 
					"(3276, \"Euclides da Cunha\", 5),\r\n" + 
					"(3277, \"Euclides da Cunha Paulista\", 26),\r\n" + 
					"(3278, \"Eug�nio de Castro\", 23),\r\n" + 
					"(3279, \"Eugen�polis\", 11),\r\n" + 
					"(3280, \"Eun�polis\", 5),\r\n" + 
					"(3281, \"Europa (Paranagu�)\", 18),\r\n" + 
					"(3282, \"Eus�bio\", 6),\r\n" + 
					"(3283, \"Euxenita (Sabin�polis)\", 11),\r\n" + 
					"(3284, \"Euz�bio de Oliveira (Ibaiti)\", 18),\r\n" + 
					"(3285, \"Evangelista (Casca)\", 23),\r\n" + 
					"(3286, \"Evaristo (Santo Ant�nio da Patrulha)\", 23),\r\n" + 
					"(3287, \"Ewbank da C�mara\", 11),\r\n" + 
					"(3288, \"Expedicion�rio Al�cio (Aimor�s)\", 11),\r\n" + 
					"(3289, \"Extra��o (Diamantina)\", 11),\r\n" + 
					"(3290, \"Extrema\", 11),\r\n" + 
					"(3291, \"Extrema (Porto Velho)\", 21),\r\n" + 
					"(3292, \"Extremoz\", 20),\r\n" + 
					"(3293, \"Exu\", 16),\r\n" + 
					"(3294, \"Fagundes\", 15),\r\n" + 
					"(3295, \"Fagundes Varela\", 23),\r\n" + 
					"(3296, \"Faina\", 9),\r\n" + 
					"(3297, \"Fa�sca (Reden��o)\", 6),\r\n" + 
					"(3298, \"Faisqueira (Tel�maco Borba)\", 18),\r\n" + 
					"(3299, \"Fama\", 11),\r\n" + 
					"(3300, \"F�o (Lajeado)\", 23),\r\n" + 
					"(3301, \"Faria (Barbacena)\", 11),\r\n" + 
					"(3302, \"Faria Lemos\", 11),\r\n" + 
					"(3303, \"Faria Lemos (Bento Gon�alves)\", 23),\r\n" + 
					"(3304, \"Farias Brito\", 6),\r\n" + 
					"(3305, \"Farias (Guanh�es)\", 11),\r\n" + 
					"(3306, \"Farinhas (Alpestre)\", 23),\r\n" + 
					"(3307, \"Faro\", 14),\r\n" + 
					"(3308, \"Farol\", 18),\r\n" + 
					"(3309, \"Farrapos (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(3310, \"Farrinheira (Arapiraca)\", 2),\r\n" + 
					"(3311, \"Farroupilha\", 23),\r\n" + 
					"(3312, \"Fartura\", 26),\r\n" + 
					"(3313, \"Fartura do Piau�\", 17),\r\n" + 
					"(3314, \"Fartura (S�o Gabriel da Palha)\", 8),\r\n" + 
					"(3315, \"F�tima\", 5),\r\n" + 
					"(3316, \"F�tima\", 27),\r\n" + 
					"(3317, \"F�tima (Cachoeira dos �ndios)\", 15),\r\n" + 
					"(3318, \"F�tima do Sul\", 12),\r\n" + 
					"(3319, \"F�tima (Guaimb�)\", 26),\r\n" + 
					"(3320, \"F�tima (Pacoti)\", 6),\r\n" + 
					"(3321, \"F�tima Paulista (Turmalina)\", 26),\r\n" + 
					"(3322, \"Faval (Nossa Senhora do Livramento)\", 13),\r\n" + 
					"(3323, \"Faveira (Carnaubal)\", 6),\r\n" + 
					"(3324, \"Faxinal\", 18),\r\n" + 
					"(3325, \"Faxinal do C�u (Pinh�o)\", 18),\r\n" + 
					"(3326, \"Faxinal dos Elias (Guarapuava)\", 18),\r\n" + 
					"(3327, \"Faxinal dos Guedes\", 24),\r\n" + 
					"(3328, \"Faxinal do Soturno\", 23),\r\n" + 
					"(3329, \"Faxinal Preto (S�o Jos� dos Ausentes)\", 23),\r\n" + 
					"(3330, \"Faxinal Santa Cruz (Ponta Grossa)\", 18),\r\n" + 
					"(3331, \"Faxinal (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(3332, \"Faxinal (Victor Graeff)\", 23),\r\n" + 
					"(3333, \"Faxinalzinho\", 23),\r\n" + 
					"(3334, \"Faxina (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(3335, \"Fazenda Colorado (Fortaleza dos Valos)\", 23),\r\n" + 
					"(3336, \"Fazenda das Laranjeiras (Muitos Cap�es)\", 23),\r\n" + 
					"(3337, \"Fazenda de Cima (Pocon�)\", 13),\r\n" + 
					"(3338, \"Fazenda do Brigadeiro (Cascavel)\", 18),\r\n" + 
					"(3339, \"Fazenda Fialho (S�o Valentim do Sul)\", 23),\r\n" + 
					"(3340, \"Fazenda Fialho (Taquara)\", 23),\r\n" + 
					"(3341, \"Fazenda Guandu (Afonso Cl�udio)\", 8),\r\n" + 
					"(3342, \"Fazenda Jangada (Cascavel)\", 18),\r\n" + 
					"(3343, \"Fazenda Nova\", 9),\r\n" + 
					"(3344, \"Fazenda Nova (Brejo da Madre de Deus)\", 16),\r\n" + 
					"(3345, \"Fazenda Nova (Uira�na)\", 15),\r\n" + 
					"(3346, \"Fazenda Rio Grande\", 18),\r\n" + 
					"(3347, \"Fazenda Salmo 23 (Umuarama)\", 18),\r\n" + 
					"(3348, \"Fazenda Souza (Caxias do Sul)\", 23),\r\n" + 
					"(3349, \"Fazenda Velha (Ces�rio Lange)\", 26),\r\n" + 
					"(3350, \"Fazenda Vilanova\", 23),\r\n" + 
					"(3351, \"Fazenda Zandavalli (Guatamb�)\", 24),\r\n" + 
					"(3352, \"Fazendinha (Francisco Beltr�o)\", 18),\r\n" + 
					"(3353, \"Fechados (Santana de Pirapama)\", 11),\r\n" + 
					"(3354, \"Feij�\", 1),\r\n" + 
					"(3355, \"Feijoal (Benjamin Constant)\", 3),\r\n" + 
					"(3356, \"Feira da Mata\", 5),\r\n" + 
					"(3357, \"Feira de Santana\", 5),\r\n" + 
					"(3358, \"Feira Grande\", 2),\r\n" + 
					"(3359, \"Feira Nova\", 25),\r\n" + 
					"(3360, \"Feira Nova\", 16),\r\n" + 
					"(3361, \"Feira Nova do Maranh�o\", 10),\r\n" + 
					"(3362, \"Feiticeiro (Jaguaribe)\", 6),\r\n" + 
					"(3363, \"Feitoria (Bodoc�)\", 16),\r\n" + 
					"(3364, \"Feitosa (Cariria�u)\", 6),\r\n" + 
					"(3365, \"Felicina (A�ucena)\", 11),\r\n" + 
					"(3366, \"Fel�cio dos Santos\", 11),\r\n" + 
					"(3367, \"Felipe Guerra\", 20),\r\n" + 
					"(3368, \"Felipe Schmidt (Canoinhas)\", 24),\r\n" + 
					"(3369, \"Felisburgo\", 11),\r\n" + 
					"(3370, \"Felixl�ndia\", 11),\r\n" + 
					"(3371, \"F�lix Pinto (Cant�)\", 22),\r\n" + 
					"(3372, \"Feliz\", 23),\r\n" + 
					"(3373, \"Felizardo (Ipaumirim)\", 6),\r\n" + 
					"(3374, \"Feliz Deserto\", 2),\r\n" + 
					"(3375, \"Feliz Natal\", 13),\r\n" + 
					"(3376, \"Felpudo (Campo Largo)\", 18),\r\n" + 
					"(3377, \"F�nix\", 18),\r\n" + 
					"(3378, \"Fernandes Belo (Viseu)\", 14),\r\n" + 
					"(3379, \"Fernandes Pinheiro\", 18),\r\n" + 
					"(3380, \"Fernandes Tourinho\", 11),\r\n" + 
					"(3381, \"Fernando de Noronha\", 16),\r\n" + 
					"(3382, \"Fernando Falc�o\", 10),\r\n" + 
					"(3383, \"Fernando Pedroza\", 20),\r\n" + 
					"(3384, \"Fernand�polis\", 26),\r\n" + 
					"(3385, \"Fernando Prestes\", 26),\r\n" + 
					"(3386, \"Fern�o\", 26),\r\n" + 
					"(3387, \"Fern�o Dias (Bras�lia de Minas)\", 11),\r\n" + 
					"(3388, \"Fern�o Dias (Munhoz de Melo)\", 18),\r\n" + 
					"(3389, \"Ferraria (Campo Largo)\", 18),\r\n" + 
					"(3390, \"Ferraz de Vasconcelos\", 26),\r\n" + 
					"(3391, \"Ferreira (Cachoeira do Sul)\", 23),\r\n" + 
					"(3392, \"Ferreira Gomes\", 4),\r\n" + 
					"(3393, \"Ferreiras (Ponta Grossa)\", 18),\r\n" + 
					"(3394, \"Ferreiras (S�o Gon�alo do Sapuca�)\", 11),\r\n" + 
					"(3395, \"Ferreir�polis (Salinas)\", 11),\r\n" + 
					"(3396, \"Ferreiros\", 16),\r\n" + 
					"(3397, \"Ferros\", 11),\r\n" + 
					"(3398, \"Ferruginha (Conselheiro Pena)\", 11),\r\n" + 
					"(3399, \"Fervedouro\", 11),\r\n" + 
					"(3400, \"Fidalgo (Pedro Leopoldo)\", 11),\r\n" + 
					"(3401, \"Fidel�ndia (Atal�ia)\", 11),\r\n" + 
					"(3402, \"Figueira\", 18),\r\n" + 
					"(3403, \"Figueira (Chapec�)\", 24),\r\n" + 
					"(3404, \"Figueira do Oeste (Engenheiro Beltr�o)\", 18),\r\n" + 
					"(3405, \"Figueir�o\", 12),\r\n" + 
					"(3406, \"Figueir�polis\", 27),\r\n" + 
					"(3407, \"Figueir�polis D'Oeste\", 13),\r\n" + 
					"(3408, \"Filad�lfia\", 5),\r\n" + 
					"(3409, \"Filad�lfia\", 27),\r\n" + 
					"(3410, \"Filad�lfia (Ju�na)\", 13),\r\n" + 
					"(3411, \"Filan�sia (Apuarema)\", 5),\r\n" + 
					"(3412, \"Firmamento (Lajes)\", 20),\r\n" + 
					"(3413, \"Firmino Alves\", 5),\r\n" + 
					"(3414, \"Firmin�polis\", 9),\r\n" + 
					"(3415, \"Fiusas (Guarapuava)\", 18),\r\n" + 
					"(3416, \"Flamengo (Saboeiro)\", 6),\r\n" + 
					"(3417, \"Flexal (�bidos)\", 14),\r\n" + 
					"(3418, \"Flexeiras\", 2),\r\n" + 
					"(3419, \"Flexeiras (Arapiraca)\", 2),\r\n" + 
					"(3420, \"Flora�\", 18),\r\n" + 
					"(3421, \"Flor�lia (Santa B�rbara)\", 11),\r\n" + 
					"(3422, \"Flor�nia\", 20),\r\n" + 
					"(3423, \"Flora Rica\", 26),\r\n" + 
					"(3424, \"Flor da Serra do Sul\", 18),\r\n" + 
					"(3425, \"Flor da Serra (Guarant� do Norte)\", 13),\r\n" + 
					"(3426, \"Flor da Serra (Realeza)\", 18),\r\n" + 
					"(3427, \"Flor da Serra (Serran�polis do Igua�u)\", 18),\r\n" + 
					"(3428, \"Flor de Minas (Gurinhat�)\", 11),\r\n" + 
					"(3429, \"Flor do Sert�o\", 24),\r\n" + 
					"(3430, \"Floreal\", 26),\r\n" + 
					"(3431, \"Flores\", 16),\r\n" + 
					"(3432, \"Flores da Cunha\", 23),\r\n" + 
					"(3433, \"Flores de Goi�s\", 9),\r\n" + 
					"(3434, \"Flores do Piau�\", 17),\r\n" + 
					"(3435, \"Flores (Ipu)\", 6),\r\n" + 
					"(3436, \"Flores (Russas)\", 6),\r\n" + 
					"(3437, \"Floresta\", 18),\r\n" + 
					"(3438, \"Floresta\", 16),\r\n" + 
					"(3439, \"Floresta Azul\", 5),\r\n" + 
					"(3440, \"Floresta (Central de Minas)\", 11),\r\n" + 
					"(3441, \"Floresta do Araguaia\", 14),\r\n" + 
					"(3442, \"Floresta do Piau�\", 17),\r\n" + 
					"(3443, \"Floresta do Sul (Presidente Prudente)\", 26),\r\n" + 
					"(3444, \"Floresta (Itaituba)\", 14),\r\n" + 
					"(3445, \"Florestal\", 11),\r\n" + 
					"(3446, \"Florestal (Jequi�)\", 5),\r\n" + 
					"(3447, \"Floresta (Selbach)\", 23),\r\n" + 
					"(3448, \"Floresta (Tr�s Passos)\", 23),\r\n" + 
					"(3449, \"Florestina (Araguari)\", 11),\r\n" + 
					"(3450, \"Florest�polis\", 18),\r\n" + 
					"(3451, \"Floriano\", 17),\r\n" + 
					"(3452, \"Floriano Peixoto\", 23),\r\n" + 
					"(3453, \"Floriano Peixoto (Boca do Acre)\", 3),\r\n" + 
					"(3454, \"Florian�polis\", 24),\r\n" + 
					"(3455, \"Fl�rida\", 18),\r\n" + 
					"(3456, \"Fl�rida Paulista\", 26),\r\n" + 
					"(3457, \"Fl�rida (Santiago)\", 23),\r\n" + 
					"(3458, \"Flor�nia\", 26),\r\n" + 
					"(3459, \"Flor�polis (Paranacity)\", 18),\r\n" + 
					"(3460, \"Fluvi�polis (S�o Mateus do Sul)\", 18),\r\n" + 
					"(3461, \"Fonseca (Alvin�polis)\", 11),\r\n" + 
					"(3462, \"Fontanilhas (Castanheira)\", 13),\r\n" + 
					"(3463, \"Fonte Boa\", 3),\r\n" + 
					"(3464, \"Fontoura Xavier\", 23),\r\n" + 
					"(3465, \"Formiga\", 11),\r\n" + 
					"(3466, \"Formigone (Umuarama)\", 18),\r\n" + 
					"(3467, \"Formigueiro\", 23),\r\n" + 
					"(3468, \"Formigueiro (Arauc�ria)\", 18),\r\n" + 
					"(3469, \"Formosa\", 9),\r\n" + 
					"(3470, \"Formosa da Serra Negra\", 10),\r\n" + 
					"(3471, \"Formosa do Oeste\", 18),\r\n" + 
					"(3472, \"Formosa do Rio Preto\", 5),\r\n" + 
					"(3473, \"Formosa do Sul\", 24),\r\n" + 
					"(3474, \"Formoso\", 11),\r\n" + 
					"(3475, \"Formoso\", 9),\r\n" + 
					"(3476, \"Formoso do Araguaia\", 27),\r\n" + 
					"(3477, \"Forninho (Ca�apava do Sul)\", 23),\r\n" + 
					"(3478, \"Forqueta (Arroio do Meio)\", 23),\r\n" + 
					"(3479, \"Forqueta Baixa (Vale Real)\", 23),\r\n" + 
					"(3480, \"Forquetinha\", 23),\r\n" + 
					"(3481, \"Forquilha\", 6),\r\n" + 
					"(3482, \"Forquilha (Beberibe)\", 6),\r\n" + 
					"(3483, \"Forquilhinha\", 24),\r\n" + 
					"(3484, \"Fortaleza\", 6),\r\n" + 
					"(3485, \"Fortaleza de Minas\", 11),\r\n" + 
					"(3486, \"Fortaleza do Abun� (Porto Velho)\", 21),\r\n" + 
					"(3487, \"Fortaleza dos Nogueiras\", 10),\r\n" + 
					"(3488, \"Fortaleza dos Valos\", 23),\r\n" + 
					"(3489, \"Fortaleza do Taboc�o\", 27),\r\n" + 
					"(3490, \"Fortaleza (Santana)\", 4),\r\n" + 
					"(3491, \"Forte (S�o Jo�o D'Alian�a)\", 9),\r\n" + 
					"(3492, \"Forte Velho (Santa Rita)\", 15),\r\n" + 
					"(3493, \"Fortim\", 6),\r\n" + 
					"(3494, \"Fortuna\", 10),\r\n" + 
					"(3495, \"Fortuna de Minas\", 11),\r\n" + 
					"(3496, \"Foz do Igua�u\", 18),\r\n" + 
					"(3497, \"Foz do Jord�o\", 18),\r\n" + 
					"(3498, \"Frade (Maca�)\", 19),\r\n" + 
					"(3499, \"Fragosos (Campo Alegre)\", 24),\r\n" + 
					"(3500, \"Fraiburgo\", 24),\r\n" + 
					"(3501, \"Franca\", 26),\r\n" + 
					"(3502, \"Fran�a (Piritiba)\", 5),\r\n" + 
					"(3503, \"Francesa Alta (Bar�o)\", 23),\r\n" + 
					"(3504, \"Francin�polis\", 17),\r\n" + 
					"(3505, \"Francisco Alves\", 18),\r\n" + 
					"(3506, \"Francisco Ayres\", 17),\r\n" + 
					"(3507, \"Francisco Badar�\", 11),\r\n" + 
					"(3508, \"Francisco Beltr�o\", 18),\r\n" + 
					"(3509, \"Francisco Dantas\", 20),\r\n" + 
					"(3510, \"Francisco Dumont\", 11),\r\n" + 
					"(3511, \"Francisco Frederico Teixeira Guimar�es (Palmas)\", 18),\r\n" + 
					"(3512, \"Francisco Macedo\", 17),\r\n" + 
					"(3513, \"Francisco Morato\", 26),\r\n" + 
					"(3514, \"Francisc�polis\", 11),\r\n" + 
					"(3515, \"Francisco S�\", 11),\r\n" + 
					"(3516, \"Francisco Santos\", 17),\r\n" + 
					"(3517, \"Franco da Rocha\", 26),\r\n" + 
					"(3518, \"Frecheiras (Arai�ses)\", 10),\r\n" + 
					"(3519, \"Frecheirinha\", 6),\r\n" + 
					"(3520, \"Frederico Wastner (S�o Louren�o do Oeste)\", 24),\r\n" + 
					"(3521, \"Frederico Westphalen\", 23),\r\n" + 
					"(3522, \"Freguesia do Andir� (Barreirinha)\", 3),\r\n" + 
					"(3523, \"Frei Eust�quio (Coqueiral)\", 11),\r\n" + 
					"(3524, \"Frei Gaspar\", 11),\r\n" + 
					"(3525, \"Frei Inoc�ncio\", 11),\r\n" + 
					"(3526, \"Frei Lagonegro\", 11),\r\n" + 
					"(3527, \"Frei Martinho\", 15),\r\n" + 
					"(3528, \"Frei Miguelinho\", 16),\r\n" + 
					"(3529, \"Frei Orlando (Morada Nova de Minas)\", 11),\r\n" + 
					"(3530, \"Frei Paulo\", 25),\r\n" + 
					"(3531, \"Freire Cardoso (Coronel Murta)\", 11),\r\n" + 
					"(3532, \"Freire (Pedro Os�rio)\", 23),\r\n" + 
					"(3533, \"Frei Rog�rio\", 24),\r\n" + 
					"(3534, \"Frei Sebasti�o (Palmares do Sul)\", 23),\r\n" + 
					"(3535, \"Frei Serafim (Itambacuri)\", 11),\r\n" + 
					"(3536, \"Frei Tim�teo (Jataizinho)\", 18),\r\n" + 
					"(3537, \"Frexeiras (Escada)\", 16),\r\n" + 
					"(3538, \"Fronteira\", 11),\r\n" + 
					"(3539, \"Fronteira dos Vales\", 11),\r\n" + 
					"(3540, \"Fronteiras\", 17),\r\n" + 
					"(3541, \"Fruta de Leite\", 11),\r\n" + 
					"(3542, \"Frutal\", 11),\r\n" + 
					"(3543, \"Frutal do Campo (C�ndido Mota)\", 26),\r\n" + 
					"(3544, \"Frutuoso Gomes\", 20),\r\n" + 
					"(3545, \"Fueros (Guarapuava)\", 18),\r\n" + 
					"(3546, \"Fuma�a (Resende)\", 19),\r\n" + 
					"(3547, \"Funchal (S�o Gotardo)\", 11),\r\n" + 
					"(3548, \"Fund�o\", 8),\r\n" + 
					"(3549, \"Fund�o (Castro)\", 18),\r\n" + 
					"(3550, \"Funil�ndia\", 11),\r\n" + 
					"(3551, \"Funil (Cambuci)\", 19),\r\n" + 
					"(3552, \"Furquim (Mariana)\", 11),\r\n" + 
					"(3553, \"Gabiarra (Eun�polis)\", 5),\r\n" + 
					"(3554, \"Gabriel Monteiro\", 26),\r\n" + 
					"(3555, \"Gadelha (Iguatu)\", 6),\r\n" + 
					"(3556, \"Gado Bravo\", 15),\r\n" + 
					"(3557, \"Gado dos Rodrigues (Palm�cia)\", 6),\r\n" + 
					"(3558, \"Gado (Palm�cia)\", 6),\r\n" + 
					"(3559, \"Gaivota (Macap�)\", 4),\r\n" + 
					"(3560, \"Galante (Campina Grande)\", 15),\r\n" + 
					"(3561, \"Gale�o (Cairu)\", 5),\r\n" + 
					"(3562, \"Galego (Barbacena)\", 11),\r\n" + 
					"(3563, \"Galena (Presidente Oleg�rio)\", 11),\r\n" + 
					"(3564, \"G�lia\", 26),\r\n" + 
					"(3565, \"Galil�ia\", 11),\r\n" + 
					"(3566, \"Galinhos\", 20),\r\n" + 
					"(3567, \"Galv�o\", 24),\r\n" + 
					"(3568, \"Gama (A�ucena)\", 11),\r\n" + 
					"(3569, \"Gamadinho (Cascavel)\", 18),\r\n" + 
					"(3570, \"Gamb� (Goiana)\", 16),\r\n" + 
					"(3571, \"Gamboa (Cairu)\", 5),\r\n" + 
					"(3572, \"Gamela (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(3573, \"Gameleira\", 16),\r\n" + 
					"(3574, \"Gameleira da Lapa (S�tio do Mato)\", 5),\r\n" + 
					"(3575, \"Gameleira de Goi�s\", 9),\r\n" + 
					"(3576, \"Gameleira de S�o Sebasti�o (Miss�o Velha)\", 6),\r\n" + 
					"(3577, \"Gameleira do Assuru� (Gentio do Ouro)\", 5),\r\n" + 
					"(3578, \"Gameleiras\", 11),\r\n" + 
					"(3579, \"Gameleira (Taipu)\", 20),\r\n" + 
					"(3580, \"Gandu\", 5),\r\n" + 
					"(3581, \"Garanhuns\", 16),\r\n" + 
					"(3582, \"Garapuava (Una�)\", 11),\r\n" + 
					"(3583, \"Gararu\", 25),\r\n" + 
					"(3584, \"Gar�a\", 26),\r\n" + 
					"(3585, \"Gar�as (Amontada)\", 6),\r\n" + 
					"(3586, \"Garcia (Angelina)\", 24),\r\n" + 
					"(3587, \"Garcias (Tr�s Lagoas)\", 12),\r\n" + 
					"(3588, \"Gard�nia (Rancharia)\", 26),\r\n" + 
					"(3589, \"Garibaldi\", 23),\r\n" + 
					"(3590, \"Garibaldina (Garibaldi)\", 23),\r\n" + 
					"(3591, \"Garopaba\", 24),\r\n" + 
					"(3592, \"Garraf�o do Norte\", 14),\r\n" + 
					"(3593, \"Garraf�o (Santa Maria de Jetib�)\", 8),\r\n" + 
					"(3594, \"Garruchos\", 23),\r\n" + 
					"(3595, \"Garuva\", 24),\r\n" + 
					"(3596, \"Gaspar\", 24),\r\n" + 
					"(3597, \"Gaspar (Arapiraca)\", 2),\r\n" + 
					"(3598, \"Gast�o Vidigal\", 26),\r\n" + 
					"(3599, \"Ga�cha do Norte\", 13),\r\n" + 
					"(3600, \"Ga�cha (Francisco Beltr�o)\", 18),\r\n" + 
					"(3601, \"Gaurama\", 23),\r\n" + 
					"(3602, \"Gavi�o\", 5),\r\n" + 
					"(3603, \"Gavi�o (Eugen�polis)\", 11),\r\n" + 
					"(3604, \"Gavi�o (Laranjeiras do Sul)\", 18),\r\n" + 
					"(3605, \"Gavi�o (Parambu)\", 6),\r\n" + 
					"(3606, \"Gavi�o Peixoto\", 26),\r\n" + 
					"(3607, \"Gavi�o (Salto do Lontra)\", 18),\r\n" + 
					"(3608, \"Gavi�es (Silva Jardim)\", 19),\r\n" + 
					"(3609, \"G�zea (Ipueiras)\", 6),\r\n" + 
					"(3610, \"Geminiano\", 17),\r\n" + 
					"(3611, \"General C�mara\", 23),\r\n" + 
					"(3612, \"General Carneiro\", 13),\r\n" + 
					"(3613, \"General Carneiro\", 18),\r\n" + 
					"(3614, \"General Maynard\", 25),\r\n" + 
					"(3615, \"General Neto (Bar�o)\", 23),\r\n" + 
					"(3616, \"General Os�rio (Toledo)\", 18),\r\n" + 
					"(3617, \"General Salgado\", 26),\r\n" + 
					"(3618, \"General Sampaio\", 6),\r\n" + 
					"(3619, \"General Tib�rcio (Vi�osa do Cear�)\", 6),\r\n" + 
					"(3620, \"Gentil\", 23),\r\n" + 
					"(3621, \"Gentio do Ouro\", 5),\r\n" + 
					"(3622, \"Geol�ndia (Cabaceiras do Paragua�u)\", 5),\r\n" + 
					"(3623, \"Geremia Lunardelli (Nova Cantu)\", 18),\r\n" + 
					"(3624, \"Gerera� (Itaitinga)\", 6),\r\n" + 
					"(3625, \"Geria�u (Urua�u)\", 9),\r\n" + 
					"(3626, \"Getul�ndia (Rio Claro)\", 19),\r\n" + 
					"(3627, \"Getulina\", 26),\r\n" + 
					"(3628, \"Get�lio Vargas\", 23),\r\n" + 
					"(3629, \"Gilbu�s\", 17),\r\n" + 
					"(3630, \"Giqui (Jaguaruana)\", 6),\r\n" + 
					"(3631, \"Girassol (Cocalzinho de Goi�s)\", 9),\r\n" + 
					"(3632, \"Girau (Aracati)\", 6),\r\n" + 
					"(3633, \"Girau do Ponciano\", 2),\r\n" + 
					"(3634, \"Gironda (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(3635, \"Giru�\", 23),\r\n" + 
					"(3636, \"Giruazinho (Senador Salgado Filho)\", 23),\r\n" + 
					"(3637, \"Glaucil�ndia\", 11),\r\n" + 
					"(3638, \"Glaura (Ouro Preto)\", 11),\r\n" + 
					"(3639, \"Glic�rio\", 26),\r\n" + 
					"(3640, \"Glic�rio (Maca�)\", 19),\r\n" + 
					"(3641, \"Gl�ria\", 5),\r\n" + 
					"(3642, \"Gl�ria de Cataguases (Cataguases)\", 11),\r\n" + 
					"(3643, \"Gl�ria de Dourados\", 12),\r\n" + 
					"(3644, \"Gl�ria D'Oeste\", 13),\r\n" + 
					"(3645, \"Gl�ria do Goit�\", 16),\r\n" + 
					"(3646, \"Gl�ria (Estrela)\", 23),\r\n" + 
					"(3647, \"Gl�ria (Torres)\", 23),\r\n" + 
					"(3648, \"Glorinha\", 23),\r\n" + 
					"(3649, \"Gluc�nio (Santa Maria do Sua�u�)\", 11),\r\n" + 
					"(3650, \"Godofredo Viana\", 10),\r\n" + 
					"(3651, \"Godoy Moreira\", 18),\r\n" + 
					"(3652, \"Goiabal (Governador Valadares)\", 11),\r\n" + 
					"(3653, \"Goiabeira\", 11),\r\n" + 
					"(3654, \"Goial�ndia (An�polis)\", 9),\r\n" + 
					"(3655, \"Goiaminas (Formoso)\", 11),\r\n" + 
					"(3656, \"Goian�\", 11),\r\n" + 
					"(3657, \"Goiana\", 16),\r\n" + 
					"(3658, \"Goian�polis\", 9),\r\n" + 
					"(3659, \"Goianases (Capetinga)\", 11),\r\n" + 
					"(3660, \"Goiandira\", 9),\r\n" + 
					"(3661, \"Goian�sia\", 9),\r\n" + 
					"(3662, \"Goian�sia do Par�\", 14),\r\n" + 
					"(3663, \"Goi�nia\", 9),\r\n" + 
					"(3664, \"Goianinha\", 20),\r\n" + 
					"(3665, \"Goianira\", 9),\r\n" + 
					"(3666, \"Goianorte\", 27),\r\n" + 
					"(3667, \"Goi�s\", 9),\r\n" + 
					"(3668, \"Goiatins\", 27),\r\n" + 
					"(3669, \"Goiatuba\", 9),\r\n" + 
					"(3670, \"Goio-En (Chapec�)\", 24),\r\n" + 
					"(3671, \"Goio-En (Erval Grande)\", 23),\r\n" + 
					"(3672, \"Goioer�\", 18),\r\n" + 
					"(3673, \"Goioxim\", 18),\r\n" + 
					"(3674, \"G�is (Guarapuava)\", 18),\r\n" + 
					"(3675, \"Goitacazes (Campos dos Goytacazes)\", 19),\r\n" + 
					"(3676, \"Gon�alves\", 11),\r\n" + 
					"(3677, \"Gon�alves Dias\", 10),\r\n" + 
					"(3678, \"Gon�alves Ferreira (Caruaru)\", 16),\r\n" + 
					"(3679, \"Gon�alves J�nior (Irati)\", 18),\r\n" + 
					"(3680, \"Gongogi\", 5),\r\n" + 
					"(3681, \"Gonzaga\", 11),\r\n" + 
					"(3682, \"Goror�s (Dom Joaquim)\", 11),\r\n" + 
					"(3683, \"Gorutuba (Porteirinha)\", 11),\r\n" + 
					"(3684, \"Gouveia\", 11),\r\n" + 
					"(3685, \"Gouvel�ndia\", 9),\r\n" + 
					"(3686, \"Governador Archer\", 10),\r\n" + 
					"(3687, \"Governador Celso Ramos\", 24),\r\n" + 
					"(3688, \"Governador Dix-Sept Rosado\", 20),\r\n" + 
					"(3689, \"Governador Edson Lob�o\", 10),\r\n" + 
					"(3690, \"Governador Eug�nio Barros\", 10),\r\n" + 
					"(3691, \"Governador Jo�o Durval Carneiro (Feira de Santana)\", 5),\r\n" + 
					"(3692, \"Governador Jorge Teixeira\", 21),\r\n" + 
					"(3693, \"Governador Lacerda de Aguiar (�gua Doce do Norte)\", 8),\r\n" + 
					"(3694, \"Governador Lindenberg\", 8),\r\n" + 
					"(3695, \"Governador Luiz Rocha\", 10),\r\n" + 
					"(3696, \"Governador Mangabeira\", 5),\r\n" + 
					"(3697, \"Governador Newton Bello\", 10),\r\n" + 
					"(3698, \"Governador Nunes Freire\", 10),\r\n" + 
					"(3699, \"Governador Portela (Miguel Pereira)\", 19),\r\n" + 
					"(3700, \"Governador Valadares\", 11),\r\n" + 
					"(3701, \"Gra�a\", 6),\r\n" + 
					"(3702, \"Gra�a Aranha\", 10),\r\n" + 
					"(3703, \"Graccho Cardoso\", 25),\r\n" + 
					"(3704, \"Graciosa (Paranava�)\", 18),\r\n" + 
					"(3705, \"Gradaus (Ouril�ndia do Norte)\", 14),\r\n" + 
					"(3706, \"Graja�\", 10),\r\n" + 
					"(3707, \"Gramadinho (Itapetininga)\", 26),\r\n" + 
					"(3708, \"Gramado\", 23),\r\n" + 
					"(3709, \"Gramado dos Loureiros\", 23),\r\n" + 
					"(3710, \"Gramado S�o Pedro (Fontoura Xavier)\", 23),\r\n" + 
					"(3711, \"Gramado Xavier\", 23),\r\n" + 
					"(3712, \"Gram�nea (Andradas)\", 11),\r\n" + 
					"(3713, \"Granada (Abre Campo)\", 11),\r\n" + 
					"(3714, \"Grande Bas�lio (Palm�cia)\", 6),\r\n" + 
					"(3715, \"Grandes Rios\", 18),\r\n" + 
					"(3716, \"Granito\", 16),\r\n" + 
					"(3717, \"Granja\", 6),\r\n" + 
					"(3718, \"Granja Get�lio Vargas (Palmares do Sul)\", 23),\r\n" + 
					"(3719, \"Granjeiro\", 6),\r\n" + 
					"(3720, \"Gr�o Mogol\", 11),\r\n" + 
					"(3721, \"Gr�o Par�\", 24),\r\n" + 
					"(3722, \"Gr�pia (Para�so)\", 24),\r\n" + 
					"(3723, \"Gravat�\", 16),\r\n" + 
					"(3724, \"Gravat� do Ibiapina (Taquaritinga do Norte)\", 16),\r\n" + 
					"(3725, \"Gravata�\", 23),\r\n" + 
					"(3726, \"Gravatal\", 24),\r\n" + 
					"(3727, \"Groairas\", 6),\r\n" + 
					"(3728, \"Grossos\", 20),\r\n" + 
					"(3729, \"Grossos (Verdejante)\", 16),\r\n" + 
					"(3730, \"Grota (Jequeri)\", 11),\r\n" + 
					"(3731, \"Grot�o (Venturosa)\", 16),\r\n" + 
					"(3732, \"Grupiara\", 11),\r\n" + 
					"(3733, \"Gruta (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(3734, \"Guabiju\", 23),\r\n" + 
					"(3735, \"Guabiruba\", 24),\r\n" + 
					"(3736, \"Guachos (Martin�polis)\", 26),\r\n" + 
					"(3737, \"Gua�u (Dourados)\", 12),\r\n" + 
					"(3738, \"Gua�u�\", 8),\r\n" + 
					"(3739, \"Gua�ul�ndia (Gl�ria de Dourados)\", 12),\r\n" + 
					"(3740, \"Guadalupe\", 17),\r\n" + 
					"(3741, \"Guadalupe do Alto Paran� (Tr�s Lagoas)\", 12),\r\n" + 
					"(3742, \"Guagirus (Cama�ari)\", 5),\r\n" + 
					"(3743, \"Guaianas (Pederneiras)\", 26),\r\n" + 
					"(3744, \"Gua�ba\", 23),\r\n" + 
					"(3745, \"Guai�ara\", 26),\r\n" + 
					"(3746, \"Guaicui (V�rzea da Palma)\", 11),\r\n" + 
					"(3747, \"Gua� (Maragogipe)\", 5),\r\n" + 
					"(3748, \"Guaimb�\", 26),\r\n" + 
					"(3749, \"Guaipava (Paragua�u)\", 11),\r\n" + 
					"(3750, \"Guaipor� (Cafezal do Sul)\", 18),\r\n" + 
					"(3751, \"Gua�ra\", 18),\r\n" + 
					"(3752, \"Gua�ra\", 26),\r\n" + 
					"(3753, \"Guaira��\", 18),\r\n" + 
					"(3754, \"Guaira�� (Guarapuava)\", 18),\r\n" + 
					"(3755, \"Guairac� (Londrina)\", 18),\r\n" + 
					"(3756, \"Guai�ba\", 6),\r\n" + 
					"(3757, \"Guajar�\", 3),\r\n" + 
					"(3758, \"Guajar�-A�� (Bujaru)\", 14),\r\n" + 
					"(3759, \"Guajar�-Miri (Acar�)\", 14),\r\n" + 
					"(3760, \"Guajar�-Mirim\", 21),\r\n" + 
					"(3761, \"Guajeru\", 5),\r\n" + 
					"(3762, \"Guajiru (Fortim)\", 6),\r\n" + 
					"(3763, \"Guajuvira (Arauc�ria)\", 18),\r\n" + 
					"(3764, \"Guamar�\", 20),\r\n" + 
					"(3765, \"Guamiranga\", 18),\r\n" + 
					"(3766, \"Guamirim (Irati)\", 18),\r\n" + 
					"(3767, \"Guanac�s (Cascavel)\", 6),\r\n" + 
					"(3768, \"Guanambi\", 5),\r\n" + 
					"(3769, \"Guanh�es\", 11),\r\n" + 
					"(3770, \"Guanumbi (Bu�que)\", 16),\r\n" + 
					"(3771, \"Guap�\", 11),\r\n" + 
					"(3772, \"Guapia�u\", 26),\r\n" + 
					"(3773, \"Guapiara\", 26),\r\n" + 
					"(3774, \"Guapimirim\", 19),\r\n" + 
					"(3775, \"Guapirama\", 18),\r\n" + 
					"(3776, \"Guapira (Maragogipe)\", 5),\r\n" + 
					"(3777, \"Guapiranga (Lins)\", 26),\r\n" + 
					"(3778, \"Guap�\", 9),\r\n" + 
					"(3780, \"Guapor�\", 23),\r\n" + 
					"(3781, \"Guapor� (Chupinguaia)\", 21),\r\n" + 
					"(3782, \"Guapor� (Guarania�u)\", 18),\r\n" + 
					"(3783, \"Guaporema\", 18),\r\n" + 
					"(3784, \"Guar�\", 26),\r\n" + 
					"(3785, \"Guarabira\", 15),\r\n" + 
					"(3786, \"Guara�a�\", 26),\r\n" + 
					"(3787, \"Guaraci\", 18),\r\n" + 
					"(3788, \"Guaraci\", 26),\r\n" + 
					"(3789, \"Guaraciaba\", 11),\r\n" + 
					"(3790, \"Guaraciaba\", 24),\r\n" + 
					"(3791, \"Guaraciaba D'Oeste (Tupi Paulista)\", 26),\r\n" + 
					"(3792, \"Guaraciaba do Norte\", 6),\r\n" + 
					"(3793, \"Guaraciama\", 11),\r\n" + 
					"(3794, \"Guaragi (Ponta Grossa)\", 18),\r\n" + 
					"(3795, \"Guar� (Guarapuava)\", 18),\r\n" + 
					"(3796, \"Guara�\", 27),\r\n" + 
					"(3797, \"Guara� (Nova Laranjeiras)\", 18),\r\n" + 
					"(3798, \"Guara�ta\", 9),\r\n" + 
					"(3799, \"Guaraituba (Colombo)\", 18),\r\n" + 
					"(3800, \"Guaramiranga\", 6),\r\n" + 
					"(3801, \"Guaramirim\", 24),\r\n" + 
					"(3802, \"Guaran�sia\", 11),\r\n" + 
					"(3803, \"Guarani\", 11),\r\n" + 
					"(3804, \"Guarania�u\", 18),\r\n" + 
					"(3805, \"Guarani das Miss�es\", 23),\r\n" + 
					"(3806, \"Guarani de Goi�s\", 9),\r\n" + 
					"(3807, \"Guarani D'Oeste\", 26),\r\n" + 
					"(3808, \"Guaranil�ndia (Jequitinhonha)\", 11),\r\n" + 
					"(3809, \"Guarani (Nova Laranjeiras)\", 18),\r\n" + 
					"(3810, \"Guarant�\", 26),\r\n" + 
					"(3811, \"Guarant� do Norte\", 13),\r\n" + 
					"(3812, \"Guarapari\", 8),\r\n" + 
					"(3813, \"Guarapiranga (Ribeir�o Bonito)\", 26),\r\n" + 
					"(3814, \"Guarapu� (Dois C�rregos)\", 26),\r\n" + 
					"(3815, \"Guarapuava\", 18),\r\n" + 
					"(3816, \"Guarapuavinha (Francisco Beltr�o)\", 18),\r\n" + 
					"(3817, \"Guaraque�aba\", 18),\r\n" + 
					"(3818, \"Guarar�\", 11),\r\n" + 
					"(3819, \"Guararapes\", 26),\r\n" + 
					"(3820, \"Guararema\", 26),\r\n" + 
					"(3821, \"Guararema (Castro)\", 18),\r\n" + 
					"(3822, \"Guararema (Nova Ven�cia)\", 8),\r\n" + 
					"(3823, \"Guararu (Caucaia)\", 6),\r\n" + 
					"(3824, \"Guarataia (Itambacuri)\", 11),\r\n" + 
					"(3825, \"Guaratinga\", 5),\r\n" + 
					"(3826, \"Guaratinguet�\", 26),\r\n" + 
					"(3827, \"Guaratuba\", 18),\r\n" + 
					"(3828, \"Guara�na (Teixeira Soares)\", 18),\r\n" + 
					"(3829, \"Guaravera (Londrina)\", 18),\r\n" + 
					"(3830, \"Guar� (Xangri-L�)\", 23),\r\n" + 
					"(3831, \"Guarda dos Ferreiros (S�o Gotardo)\", 11),\r\n" + 
					"(3832, \"Guarda-Mor\", 11),\r\n" + 
					"(3833, \"Guardamoria (Tel�maco Borba)\", 18),\r\n" + 
					"(3834, \"Guardinha (S�o Sebasti�o do Para�so)\", 11),\r\n" + 
					"(3835, \"Guare�\", 26),\r\n" + 
					"(3836, \"Guariba\", 26),\r\n" + 
					"(3837, \"Guariba (Colniza)\", 13),\r\n" + 
					"(3838, \"Guaribas\", 17),\r\n" + 
					"(3839, \"Guarinos\", 9),\r\n" + 
					"(3840, \"Guariroba (Taquaritinga)\", 26),\r\n" + 
					"(3841, \"Guarita (Itabaiana)\", 15),\r\n" + 
					"(3842, \"Guarita (V�rzea Grande)\", 13),\r\n" + 
					"(3843, \"Guarizinho (Itapeva)\", 26),\r\n" + 
					"(3844, \"Guaruj�\", 26),\r\n" + 
					"(3845, \"Guaruj� do Sul\", 24),\r\n" + 
					"(3846, \"Guarulhos\", 26),\r\n" + 
					"(3847, \"Guassi (Reden��o)\", 6),\r\n" + 
					"(3849, \"Guassu Boi (Alegrete)\", 23),\r\n" + 
					"(3850, \"Guassupi (S�o Pedro do Sul)\", 23),\r\n" + 
					"(3851, \"Guat� (Lauro M�ller)\", 24),\r\n" + 
					"(3852, \"Guatamb�\", 24),\r\n" + 
					"(3853, \"Guatapar�\", 26),\r\n" + 
					"(3854, \"Guaxima (Conquista)\", 11),\r\n" + 
					"(3855, \"Guaxup�\", 11),\r\n" + 
					"(3856, \"Guer�m (Valen�a)\", 5),\r\n" + 
					"(3857, \"Guia (Boa Viagem)\", 6),\r\n" + 
					"(3858, \"Guia Lopes da Laguna\", 12),\r\n" + 
					"(3859, \"Guidoval\", 11),\r\n" + 
					"(3860, \"Guimar�es\", 10),\r\n" + 
					"(3861, \"Guimar�nia\", 11),\r\n" + 
					"(3862, \"Guinda (Diamantina)\", 11),\r\n" + 
					"(3863, \"Guin� (Mucug�)\", 5),\r\n" + 
					"(3864, \"Guirapa (Pinda�)\", 5),\r\n" + 
					"(3865, \"Guiratinga\", 13),\r\n" + 
					"(3866, \"Guiricema\", 11),\r\n" + 
					"(3867, \"Gurinhat�\", 11),\r\n" + 
					"(3868, \"Gurinh�m\", 15),\r\n" + 
					"(3869, \"Guri� (Camocim)\", 6),\r\n" + 
					"(3870, \"Gurj�o\", 15),\r\n" + 
					"(3871, \"Gurup�\", 14),\r\n" + 
					"(3872, \"Gurup� Mirim (Potiragu�)\", 5),\r\n" + 
					"(3873, \"Gurupi\", 27),\r\n" + 
					"(3874, \"Gurupizinho (Paragominas)\", 14),\r\n" + 
					"(3875, \"Gurupora (Macap�)\", 4),\r\n" + 
					"(3876, \"Guzol�ndia\", 26),\r\n" + 
					"(3877, \"Harmonia\", 23),\r\n" + 
					"(3878, \"Harmonia (Tel�maco Borba)\", 18),\r\n" + 
					"(3879, \"Heitora�\", 9),\r\n" + 
					"(3880, \"Heliodora\", 11),\r\n" + 
					"(3881, \"Heli�polis\", 5),\r\n" + 
					"(3882, \"Helvecia (Nova Vi�osa)\", 5),\r\n" + 
					"(3883, \"Hematita (Ant�nio Dias)\", 11),\r\n" + 
					"(3884, \"Henrique Dias (Sert�nia)\", 16),\r\n" + 
					"(3885, \"Hercili�polis (�gua Doce)\", 24),\r\n" + 
					"(3886, \"Hercul�ndia\", 26),\r\n" + 
					"(3887, \"Hercul�ndia (Ivat�)\", 18),\r\n" + 
					"(3888, \"Hermilo Alves (Caranda�)\", 11),\r\n" + 
					"(3889, \"Herval\", 23),\r\n" + 
					"(3890, \"Herval D'Oeste\", 24),\r\n" + 
					"(3891, \"Herval Grande (Laranjeiras do Sul)\", 18),\r\n" + 
					"(3892, \"Herveira (Campina da Lagoa)\", 18),\r\n" + 
					"(3893, \"Herveira (Nova Laranjeiras)\", 18),\r\n" + 
					"(3894, \"Herveiras\", 23),\r\n" + 
					"(3895, \"Hidr�ulica (Cap�o do Le�o)\", 23),\r\n" + 
					"(3896, \"Hidrel�trica Itaipu (Foz do Igua�u)\", 18),\r\n" + 
					"(3897, \"Hidrel�trica Tucuru� (Tucuru�)\", 14),\r\n" + 
					"(3898, \"Hidrol�ndia\", 6),\r\n" + 
					"(3899, \"Hidrol�ndia\", 9),\r\n" + 
					"(3900, \"Hidrol�ndia (Uiba�)\", 5),\r\n" + 
					"(3901, \"Hidrolina\", 9),\r\n" + 
					"(3902, \"Holambra\", 26),\r\n" + 
					"(3903, \"Holambra II (Paranapanema)\", 26),\r\n" + 
					"(3904, \"Holanda (Tamboril)\", 6),\r\n" + 
					"(3905, \"Hon�rio Serpa\", 18),\r\n" + 
					"(3906, \"Honor�polis (Campina Verde)\", 11),\r\n" + 
					"(3907, \"Horizonte\", 6),\r\n" + 
					"(3908, \"Horizonte do Oeste (C�ceres)\", 13),\r\n" + 
					"(3909, \"Horizontina\", 23),\r\n" + 
					"(3910, \"Hortol�ndia\", 26),\r\n" + 
					"(3911, \"Hugo Napole�o\", 17),\r\n" + 
					"(3912, \"Hulha Negra\", 23),\r\n" + 
					"(3913, \"Humait�\", 23),\r\n" + 
					"(3914, \"Humait�\", 3),\r\n" + 
					"(3915, \"Humait� (Juiz de Fora)\", 11),\r\n" + 
					"(3916, \"Humberto de Campos\", 10),\r\n" + 
					"(3917, \"Humildes (Feira de Santana)\", 5),\r\n" + 
					"(3918, \"Iacanga\", 26),\r\n" + 
					"(3919, \"Iaciara\", 9),\r\n" + 
					"(3920, \"Iacri\", 26),\r\n" + 
					"(3921, \"Ia�u\", 5),\r\n" + 
					"(3922, \"Iapi (Independ�ncia)\", 6),\r\n" + 
					"(3923, \"Iapu\", 11),\r\n" + 
					"(3924, \"Iara (Barro)\", 6),\r\n" + 
					"(3925, \"Iarama (Umuarama)\", 18),\r\n" + 
					"(3926, \"Iaras\", 26),\r\n" + 
					"(3927, \"Iata� (Altamira)\", 14),\r\n" + 
					"(3928, \"Iateca (Salo�)\", 16),\r\n" + 
					"(3929, \"Iati\", 16),\r\n" + 
					"(3930, \"Iauaret� (S�o Gabriel da Cachoeira)\", 3),\r\n" + 
					"(3931, \"Ibaiti\", 18),\r\n" + 
					"(3932, \"Ibarama\", 23),\r\n" + 
					"(3933, \"Ibar� (Lavras do Sul)\", 23),\r\n" + 
					"(3934, \"Ibaretama\", 6),\r\n" + 
					"(3935, \"Ibat�\", 26),\r\n" + 
					"(3936, \"Ibateguara\", 2),\r\n" + 
					"(3937, \"Ibatiba\", 8),\r\n" + 
					"(3938, \"Ibema\", 18),\r\n" + 
					"(3939, \"Ibertioga\", 11),\r\n" + 
					"(3940, \"Ibi�\", 11),\r\n" + 
					"(3941, \"Ibia��\", 23),\r\n" + 
					"(3942, \"Ibiaci (Primeiro de Maio)\", 18),\r\n" + 
					"(3943, \"Ibia�u (Mara�)\", 5),\r\n" + 
					"(3944, \"Ibia�\", 11),\r\n" + 
					"(3945, \"Ibiajara (Rio do Pires)\", 5),\r\n" + 
					"(3946, \"Ibiam\", 24),\r\n" + 
					"(3947, \"Ibiapaba (Crate�s)\", 6),\r\n" + 
					"(3948, \"Ibiapina\", 6),\r\n" + 
					"(3949, \"Ibiapora (Mundo Novo)\", 5),\r\n" + 
					"(3950, \"Ibiara\", 15),\r\n" + 
					"(3951, \"Ibiassuc�\", 5),\r\n" + 
					"(3952, \"Ibicaba (Afonso Cl�udio)\", 8),\r\n" + 
					"(3953, \"Ibicara�\", 5),\r\n" + 
					"(3954, \"Ibicar�\", 24),\r\n" + 
					"(3955, \"Ibicatu (V�rzea Alegre)\", 6),\r\n" + 
					"(3956, \"Ibicoara\", 5),\r\n" + 
					"(3957, \"Ibicu� (Piquet Carneiro)\", 6),\r\n" + 
					"(3958, \"Ibicu�\", 5),\r\n" + 
					"(3959, \"Ibicu� (Campos Novos)\", 24),\r\n" + 
					"(3960, \"Ibicuitaba (Icapu�)\", 6),\r\n" + 
					"(3961, \"Ibicuitinga\", 6),\r\n" + 
					"(3962, \"Ibimirim\", 16),\r\n" + 
					"(3963, \"Ibipeba\", 5),\r\n" + 
					"(3964, \"Ibipeba (S�o Sebasti�o do Alto)\", 19),\r\n" + 
					"(3965, \"Ibipetum (Ipupiara)\", 5),\r\n" + 
					"(3966, \"Ibipira (Mirador)\", 10),\r\n" + 
					"(3967, \"Ibipitanga\", 5),\r\n" + 
					"(3968, \"Ibipor�\", 18),\r\n" + 
					"(3969, \"Ibiporanga (Tanabi)\", 26),\r\n" + 
					"(3970, \"Ibiquera\", 5),\r\n" + 
					"(3971, \"Ibir�\", 26),\r\n" + 
					"(3972, \"Ibiraba (Barra)\", 5),\r\n" + 
					"(3973, \"Ibiracatu\", 11),\r\n" + 
					"(3974, \"Ibiraci\", 11),\r\n" + 
					"(3975, \"Ibira�u\", 8),\r\n" + 
					"(3976, \"Ibiraiaras\", 23),\r\n" + 
					"(3977, \"Ibiraj� (Itanh�m)\", 5),\r\n" + 
					"(3978, \"Ibirajuba\", 16),\r\n" + 
					"(3979, \"Ibirama\", 24),\r\n" + 
					"(3980, \"Ibiranga (Itamb�)\", 16),\r\n" + 
					"(3981, \"Ibiranh�m (Mucuri)\", 5),\r\n" + 
					"(3982, \"Ibirapitanga\", 5),\r\n" + 
					"(3983, \"Ibirapu�\", 5),\r\n" + 
					"(3984, \"Ibirapuit�\", 23),\r\n" + 
					"(3985, \"Ibirarema\", 26),\r\n" + 
					"(3986, \"Ibirataia\", 5),\r\n" + 
					"(3987, \"Ibiratinga (Sirinha�m)\", 16),\r\n" + 
					"(3988, \"Ibirit�\", 11),\r\n" + 
					"(3989, \"Ibirub�\", 23),\r\n" + 
					"(3990, \"Ibitiara\", 5),\r\n" + 
					"(3991, \"Ibitigua�u (Santo Ant�nio de P�dua)\", 19),\r\n" + 
					"(3992, \"Ibitiguira (Planaltino)\", 5),\r\n" + 
					"(3993, \"Ibitinga\", 26),\r\n" + 
					"(3994, \"Ibitioca (Campos dos Goytacazes)\", 19),\r\n" + 
					"(3995, \"Ibitirama\", 8),\r\n" + 
					"(3996, \"Ibitira (Martinho Campos)\", 11),\r\n" + 
					"(3997, \"Ibitiranga (Carna�ba)\", 16),\r\n" + 
					"(3998, \"Ibitira (Rio do Ant�nio)\", 5),\r\n" + 
					"(3999, \"Ibitiru� (Alfredo Chaves)\", 8),\r\n" + 
					"(4000, \"Ibitiruna (Piracicaba)\", 26),\r\n" + 
					"(4001, \"Ibitit�\", 5),\r\n" + 
					"(4002, \"Ibiti�ra de Minas\", 11),\r\n" + 
					"(4003, \"Ibiti�va (Pitangueiras)\", 26),\r\n" + 
					"(4004, \"Ibituba (Baixo Guandu)\", 8),\r\n" + 
					"(4005, \"Ibitunane (Gentio do Ouro)\", 5),\r\n" + 
					"(4006, \"Ibitupa (Ibicu�)\", 5),\r\n" + 
					"(4007, \"Ibituruna\", 11),\r\n" + 
					"(4008, \"Ibi�na\", 26),\r\n" + 
					"(4009, \"Ib� (Abar�)\", 5),\r\n" + 
					"(4010, \"Ib� (Bel�m do S�o Francisco)\", 16),\r\n" + 
					"(4011, \"Iborepi (Lavras da Mangabeira)\", 6),\r\n" + 
					"(4012, \"Ibotirama\", 5),\r\n" + 
					"(4013, \"Ibua�u (Boa Viagem)\", 6),\r\n" + 
					"(4014, \"Ibugua�u (Granja)\", 6),\r\n" + 
					"(4015, \"Icai�ara (Parnamirim)\", 16),\r\n" + 
					"(4016, \"I�an� (S�o Gabriel da Cachoeira)\", 3),\r\n" + 
					"(4017, \"Icapu�\", 6),\r\n" + 
					"(4018, \"I�ara\", 24),\r\n" + 
					"(4019, \"I�ara (Astorga)\", 18),\r\n" + 
					"(4020, \"Icara� (Amontada)\", 6),\r\n" + 
					"(4021, \"Icara� de Minas\", 11),\r\n" + 
					"(4022, \"Icara�ma\", 18),\r\n" + 
					"(4023, \"Icatu\", 10),\r\n" + 
					"(4024, \"Icatu (Quer�ncia do Norte)\", 18),\r\n" + 
					"(4025, \"Ic�m\", 26),\r\n" + 
					"(4026, \"Ichu\", 5),\r\n" + 
					"(4027, \"Ic�\", 6),\r\n" + 
					"(4028, \"Ic� (Morro do Chap�u)\", 5),\r\n" + 
					"(4029, \"Iconha\", 8),\r\n" + 
					"(4030, \"Icozinho (Ic�)\", 6),\r\n" + 
					"(4031, \"Ida Iolanda (Nhandeara)\", 26),\r\n" + 
					"(4032, \"Idamar (Dion�sio Cerqueira)\", 24),\r\n" + 
					"(4033, \"Ideal (Aracoiaba)\", 6),\r\n" + 
					"(4034, \"Ielmo Marinho\", 20),\r\n" + 
					"(4035, \"Iep�\", 26),\r\n" + 
					"(4036, \"Iga�aba (Pedregulho)\", 26),\r\n" + 
					"(4037, \"Igaci\", 2),\r\n" + 
					"(4038, \"Igap� (Lagoa do Ouro)\", 16),\r\n" + 
					"(4039, \"Igapor�\", 5),\r\n" + 
					"(4040, \"Igara�u do Tiet�\", 26),\r\n" + 
					"(4041, \"Igaracy\", 15),\r\n" + 
					"(4042, \"Igara� (Mococa)\", 26),\r\n" + 
					"(4043, \"Igarapava\", 26),\r\n" + 
					"(4044, \"Igarap�\", 11),\r\n" + 
					"(4045, \"Igarap�-A�u\", 14),\r\n" + 
					"(4046, \"Igarapeassu (Lagoa dos Gatos)\", 16),\r\n" + 
					"(4047, \"Igarapeba (S�o Benedito do Sul)\", 16),\r\n" + 
					"(4048, \"Igarap� da Lama (Santar�m)\", 14),\r\n" + 
					"(4049, \"Igarap� do Lago (Santana)\", 4),\r\n" + 
					"(4050, \"Igarap� do Meio\", 10),\r\n" + 
					"(4051, \"Igarap� Grande\", 10),\r\n" + 
					"(4052, \"Igarap�-Miri\", 14),\r\n" + 
					"(4053, \"Igar� (Senhor do Bonfim)\", 5),\r\n" + 
					"(4054, \"Igarassu\", 16),\r\n" + 
					"(4055, \"Igarat�\", 26),\r\n" + 
					"(4056, \"Igaratinga\", 11),\r\n" + 
					"(4057, \"Igarit� (Barra)\", 5),\r\n" + 
					"(4058, \"Igaroi (Or�s)\", 6),\r\n" + 
					"(4059, \"Igatu (Andara�)\", 5),\r\n" + 
					"(4060, \"Igrapi�na\", 5),\r\n" + 
					"(4061, \"Igreja Nova\", 2),\r\n" + 
					"(4062, \"Igreja Nova (Bom Conselho)\", 16),\r\n" + 
					"(4063, \"Igreja Nova (S�o Gon�alo do Amarante)\", 20),\r\n" + 
					"(4064, \"Igrejinha\", 23),\r\n" + 
					"(4065, \"Igrejinha (Carazinho)\", 23),\r\n" + 
					"(4066, \"Igrejinha (Coqueiros do Sul)\", 23),\r\n" + 
					"(4067, \"Igrejinha (Guarapuava)\", 18),\r\n" + 
					"(4068, \"Iguaba Grande\", 19),\r\n" + 
					"(4069, \"Igua�u (Canind�)\", 6),\r\n" + 
					"(4070, \"Igua�\", 5),\r\n" + 
					"(4071, \"Iguaibi (Igua�)\", 5),\r\n" + 
					"(4072, \"Iguape\", 26),\r\n" + 
					"(4073, \"Iguaraci\", 16),\r\n" + 
					"(4074, \"Iguara�u\", 18),\r\n" + 
					"(4075, \"Iguatama\", 11),\r\n" + 
					"(4076, \"Iguatemi\", 12),\r\n" + 
					"(4077, \"Iguatemi (Livramento de Nossa Senhora)\", 5),\r\n" + 
					"(4078, \"Iguatu\", 18),\r\n" + 
					"(4079, \"Iguatu\", 6),\r\n" + 
					"(4080, \"Igu� (Vit�ria da Conquista)\", 5),\r\n" + 
					"(4081, \"Iguipor� (Marechal C�ndido Rondon)\", 18),\r\n" + 
					"(4082, \"Iguira (Xique-Xique)\", 5),\r\n" + 
					"(4083, \"Iguitu (Ibipeba)\", 5),\r\n" + 
					"(4084, \"Ijaci\", 11),\r\n" + 
					"(4085, \"Ijucapirama (Jaguari)\", 23),\r\n" + 
					"(4086, \"Iju�\", 23),\r\n" + 
					"(4087, \"Ilha Barreira Branca (Aragua�na)\", 27),\r\n" + 
					"(4088, \"Ilhabela\", 26),\r\n" + 
					"(4089, \"Ilha Comprida\", 26),\r\n" + 
					"(4090, \"Ilha Comprida (Tr�s Lagoas)\", 12),\r\n" + 
					"(4091, \"Ilha das Flores\", 25),\r\n" + 
					"(4092, \"Ilha de Eufrasina (Paranagu�)\", 18),\r\n" + 
					"(4093, \"Ilha de Itamarac�\", 16),\r\n" + 
					"(4094, \"Ilha de Santana (Santana)\", 4),\r\n" + 
					"(4095, \"Ilha de S�o Miguel (Paranagu�)\", 18),\r\n" + 
					"(4096, \"Ilha Diana (Santos)\", 26),\r\n" + 
					"(4097, \"Ilha do Amparo (Paranagu�)\", 18),\r\n" + 
					"(4098, \"Ilha do Mel (Paranagu�)\", 18),\r\n" + 
					"(4099, \"Ilha do Rio Doce (Caratinga)\", 11),\r\n" + 
					"(4100, \"Ilha dos Marinheiros (Rio Grande)\", 23),\r\n" + 
					"(4101, \"Ilha dos Valadares (Paranagu�)\", 18),\r\n" + 
					"(4102, \"Ilha do Teixeira (Paranagu�)\", 18),\r\n" + 
					"(4103, \"Ilha Encantada (Paranagu�)\", 18),\r\n" + 
					"(4104, \"Ilha Grande\", 17),\r\n" + 
					"(4105, \"Ilha Grande (Angra dos Reis)\", 19),\r\n" + 
					"(4106, \"Ilha Grande (Aparecida do Taboado)\", 12),\r\n" + 
					"(4107, \"Ilha Solteira\", 26),\r\n" + 
					"(4108, \"Ilh�us\", 5),\r\n" + 
					"(4109, \"Ilh�us do Prata (S�o Domingos do Prata)\", 11),\r\n" + 
					"(4110, \"Ilhota\", 24),\r\n" + 
					"(4111, \"Ilic�nea\", 11),\r\n" + 
					"(4112, \"Il�polis\", 23),\r\n" + 
					"(4113, \"Imaculada\", 15),\r\n" + 
					"(4114, \"Imaru�\", 24),\r\n" + 
					"(4115, \"Imba�\", 18),\r\n" + 
					"(4116, \"Imbauzinho (Tel�maco Borba)\", 18),\r\n" + 
					"(4117, \"Imb�\", 23),\r\n" + 
					"(4118, \"Imb� de Minas\", 11),\r\n" + 
					"(4119, \"Imbituba\", 24),\r\n" + 
					"(4120, \"Imbituva\", 18),\r\n" + 
					"(4121, \"Imbuia\", 24),\r\n" + 
					"(4122, \"Imburana (Ecoporanga)\", 8),\r\n" + 
					"(4123, \"Imigrante\", 23),\r\n" + 
					"(4124, \"Imperatriz\", 10),\r\n" + 
					"(4125, \"Inaciol�ndia\", 9),\r\n" + 
					"(4126, \"In�cio Martins\", 18),\r\n" + 
					"(4127, \"Inaj�\", 18),\r\n" + 
					"(4128, \"Inaj�\", 16),\r\n" + 
					"(4129, \"Inaj� (Macap�)\", 4),\r\n" + 
					"(4130, \"Inanu (Santar�m)\", 14),\r\n" + 
					"(4131, \"Inconfid�ncia (Para�ba do Sul)\", 19),\r\n" + 
					"(4132, \"Inconfidentes\", 11),\r\n" + 
					"(4133, \"Indaiabira\", 11),\r\n" + 
					"(4134, \"Indai� do Aguape� (Fl�rida Paulista)\", 26),\r\n" + 
					"(4135, \"Indai� do Sul (Cassil�ndia)\", 12),\r\n" + 
					"(4136, \"Indai� Grande (Parana�ba)\", 12),\r\n" + 
					"(4137, \"Indaial\", 24),\r\n" + 
					"(4138, \"Indaiatuba\", 26),\r\n" + 
					"(4139, \"Inda� (Mundo Novo)\", 5),\r\n" + 
					"(4140, \"Ind�polis (Dourados)\", 12),\r\n" + 
					"(4141, \"Independ�ncia\", 23),\r\n" + 
					"(4142, \"Independ�ncia\", 6),\r\n" + 
					"(4143, \"Independ�ncia (Pato Branco)\", 18),\r\n" + 
					"(4144, \"Independ�ncia (Resplendor)\", 11),\r\n" + 
					"(4145, \"Indiana\", 26),\r\n" + 
					"(4146, \"Indian�polis (Barra do Gar�as)\", 13),\r\n" + 
					"(4147, \"Indian�polis\", 11),\r\n" + 
					"(4148, \"Indian�polis\", 18),\r\n" + 
					"(4149, \"Indiapor�\", 26),\r\n" + 
					"(4150, \"Indiara\", 9),\r\n" + 
					"(4151, \"Indiaroba\", 25),\r\n" + 
					"(4152, \"Indiava�\", 13),\r\n" + 
					"(4153, \"�ndios (Lages)\", 24),\r\n" + 
					"(4154, \"Industrial (Porto Velho)\", 21),\r\n" + 
					"(4155, \"Inema (Ilh�us)\", 5),\r\n" + 
					"(4156, \"Ing�\", 15),\r\n" + 
					"(4157, \"Inga�\", 11),\r\n" + 
					"(4158, \"Ing� (Junqueiro)\", 2),\r\n" + 
					"(4159, \"Ing�s (Nova Granada)\", 26),\r\n" + 
					"(4160, \"Ingazeira\", 16),\r\n" + 
					"(4161, \"Ingazeiras (Aurora)\", 6),\r\n" + 
					"(4162, \"Inhacor�\", 23),\r\n" + 
					"(4163, \"Inha� (Diamantina)\", 11),\r\n" + 
					"(4164, \"Inhambupe\", 5),\r\n" + 
					"(4165, \"Inhamuns (Tau�)\", 6),\r\n" + 
					"(4166, \"Inhandu� (Alegrete)\", 23),\r\n" + 
					"(4167, \"Inhangapi\", 14),\r\n" + 
					"(4168, \"Inhapi\", 2),\r\n" + 
					"(4169, \"Inhapim\", 11),\r\n" + 
					"(4170, \"Inhata (Am�lia Rodrigues)\", 5),\r\n" + 
					"(4171, \"Inha�ma\", 11),\r\n" + 
					"(4172, \"Inha�mas (Santa Maria da Vit�ria)\", 5),\r\n" + 
					"(4173, \"Inhobim (Vit�ria da Conquista)\", 5),\r\n" + 
					"(4174, \"Inhu�u (S�o Benedito)\", 6),\r\n" + 
					"(4175, \"Inhuma\", 17),\r\n" + 
					"(4176, \"Inhumas\", 9),\r\n" + 
					"(4177, \"Inimutaba\", 11),\r\n" + 
					"(4178, \"Inoc�ncia\", 12),\r\n" + 
					"(4179, \"Inspetor Carvalho (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(4180, \"Interl�ndia (An�polis)\", 9),\r\n" + 
					"(4181, \"In�bia Paulista\", 26),\r\n" + 
					"(4182, \"In�bia (Piat�)\", 5),\r\n" + 
					"(4183, \"Invernada (Castro)\", 18),\r\n" + 
					"(4184, \"Invernada (Gr�o Par�)\", 24),\r\n" + 
					"(4185, \"Invernadinha (Guarapuava)\", 18),\r\n" + 
					"(4186, \"Iol�polis (S�o Jorge D'Oeste)\", 18),\r\n" + 
					"(4187, \"Iomer�\", 24),\r\n" + 
					"(4188, \"Ipaba\", 11),\r\n" + 
					"(4189, \"Ipagua�� (Massap�)\", 6),\r\n" + 
					"(4190, \"Ipameri\", 9),\r\n" + 
					"(4191, \"Ipanema\", 11),\r\n" + 
					"(4192, \"Ipanema (Pesqueira)\", 16),\r\n" + 
					"(4193, \"Ipangua�u\", 20),\r\n" + 
					"(4194, \"Ipaporanga\", 6),\r\n" + 
					"(4195, \"Ipatinga\", 11),\r\n" + 
					"(4196, \"Ipaumirim\", 6),\r\n" + 
					"(4197, \"Ipaussu\", 26),\r\n" + 
					"(4198, \"Ip�\", 23),\r\n" + 
					"(4199, \"Ipecaet�\", 5),\r\n" + 
					"(4200, \"Iper�\", 26),\r\n" + 
					"(4201, \"Ipe�na\", 26),\r\n" + 
					"(4202, \"Ipezal (Ang�lica)\", 12),\r\n" + 
					"(4203, \"Ipiabas (Barra do Pira�)\", 19),\r\n" + 
					"(4204, \"Ipia�u\", 11),\r\n" + 
					"(4205, \"Ipia�\", 5),\r\n" + 
					"(4206, \"Ipigu�\", 26),\r\n" + 
					"(4207, \"Ipir�\", 5),\r\n" + 
					"(4208, \"Ipira\", 24),\r\n" + 
					"(4209, \"Ipiranga\", 18),\r\n" + 
					"(4210, \"Ipiranga (Arauc�ria)\", 18),\r\n" + 
					"(4211, \"Ipiranga (Boa Viagem)\", 6),\r\n" + 
					"(4212, \"Ipiranga de Goi�s\", 9),\r\n" + 
					"(4213, \"Ipiranga do Norte\", 13),\r\n" + 
					"(4214, \"Ipiranga do Piau�\", 17),\r\n" + 
					"(4215, \"Ipiranga do Sul\", 23),\r\n" + 
					"(4216, \"Ipiranga (Gravata�)\", 23),\r\n" + 
					"(4217, \"Ipiranga (S�o Jos� do Campestre)\", 20),\r\n" + 
					"(4218, \"Ipituna (S�o Sebasti�o do Alto)\", 19),\r\n" + 
					"(4219, \"Ipi�na (Jaguaquara)\", 5),\r\n" + 
					"(4220, \"Ipixuna\", 3),\r\n" + 
					"(4221, \"Ipixuna do Par�\", 14),\r\n" + 
					"(4222, \"Ipoema (Itabira)\", 11),\r\n" + 
					"(4223, \"Ipojuca\", 16),\r\n" + 
					"(4224, \"Ipom�ia (Rio das Antas)\", 24),\r\n" + 
					"(4225, \"Ipor�\", 18),\r\n" + 
					"(4226, \"Ipor�\", 9),\r\n" + 
					"(4227, \"Ipor� do Oeste\", 24),\r\n" + 
					"(4228, \"Iporanga\", 26),\r\n" + 
					"(4229, \"Ipu\", 6),\r\n" + 
					"(4230, \"Ipu�\", 26),\r\n" + 
					"(4231, \"Ipua�u\", 24),\r\n" + 
					"(4232, \"Ipua�u (Cai�ara)\", 23),\r\n" + 
					"(4233, \"Ipubi\", 16),\r\n" + 
					"(4234, \"Ipucaba (Oliveira dos Brejinhos)\", 5),\r\n" + 
					"(4235, \"Ipuca (S�o Fid�lis)\", 19),\r\n" + 
					"(4236, \"Ipueira\", 20),\r\n" + 
					"(4237, \"Ipueiras\", 27),\r\n" + 
					"(4238, \"Ipueiras\", 6),\r\n" + 
					"(4239, \"Ipueiras dos Gomes (Canind�)\", 6),\r\n" + 
					"(4240, \"Ipuera (Serrita)\", 16),\r\n" + 
					"(4241, \"Ipui�na\", 11),\r\n" + 
					"(4242, \"Ipumirim\", 24),\r\n" + 
					"(4243, \"Ipupiara\", 5),\r\n" + 
					"(4244, \"Iracema\", 22),\r\n" + 
					"(4245, \"Iracema\", 6),\r\n" + 
					"(4246, \"Iracema do Oeste\", 18),\r\n" + 
					"(4247, \"Iracem�polis\", 26),\r\n" + 
					"(4248, \"Iraceminha\", 24),\r\n" + 
					"(4249, \"Iragua�u (Triunfo)\", 16),\r\n" + 
					"(4250, \"Ira�\", 23),\r\n" + 
					"(4251, \"Ira� de Minas\", 11),\r\n" + 
					"(4252, \"Iraj� (Hidrol�ndia)\", 6),\r\n" + 
					"(4253, \"Iraja� (Iguaraci)\", 16),\r\n" + 
					"(4254, \"Irajuba\", 5),\r\n" + 
					"(4255, \"Irakitan (Tangar�)\", 24),\r\n" + 
					"(4256, \"Iramaia\", 5),\r\n" + 
					"(4257, \"Iranduba\", 3),\r\n" + 
					"(4258, \"Irani\", 24),\r\n" + 
					"(4259, \"Irap� (Chavantes)\", 26),\r\n" + 
					"(4260, \"Iraporanga (Iraquara)\", 5),\r\n" + 
					"(4261, \"Irapu�\", 26),\r\n" + 
					"(4262, \"Irapu� (Crate�s)\", 6),\r\n" + 
					"(4263, \"Irapuan (Quinta do Sol)\", 18),\r\n" + 
					"(4264, \"Irapuru\", 26),\r\n" + 
					"(4265, \"Iraput� (Itai�polis)\", 24),\r\n" + 
					"(4266, \"Iraquara\", 5),\r\n" + 
					"(4267, \"Irar�\", 5),\r\n" + 
					"(4268, \"Iratama (Garanhuns)\", 16),\r\n" + 
					"(4269, \"Irati\", 18),\r\n" + 
					"(4270, \"Irati\", 24),\r\n" + 
					"(4271, \"Iratinga (Itapaj�)\", 6),\r\n" + 
					"(4272, \"Irau�uba\", 6),\r\n" + 
					"(4273, \"Irec�\", 5),\r\n" + 
					"(4274, \"Iren�polis (Juscimeira)\", 13),\r\n" + 
					"(4275, \"Irer� (Londrina)\", 18),\r\n" + 
					"(4276, \"Iretama\", 18),\r\n" + 
					"(4277, \"Irine�polis\", 24),\r\n" + 
					"(4278, \"Iriritiba (Anchieta)\", 8),\r\n" + 
					"(4279, \"Irituia\", 14),\r\n" + 
					"(4280, \"Irundiara (Jacaraci)\", 5),\r\n" + 
					"(4281, \"Irundi (Fund�o)\", 8),\r\n" + 
					"(4282, \"Irupi\", 8),\r\n" + 
					"(4283, \"Isabel (Domingos Martins)\", 8),\r\n" + 
					"(4284, \"Isa�as Coelho\", 17),\r\n" + 
					"(4285, \"Isidoro (Acopiara)\", 6),\r\n" + 
					"(4286, \"Ismael (Esperan�a do Sul)\", 23),\r\n" + 
					"(4287, \"Israel�ndia\", 9),\r\n" + 
					"(4288, \"It�\", 24),\r\n" + 
					"(4289, \"Itaara\", 23),\r\n" + 
					"(4290, \"Ita-Azul (Itamaraju)\", 5),\r\n" + 
					"(4291, \"Itabaiana\", 25),\r\n" + 
					"(4292, \"Itabaiana\", 15),\r\n" + 
					"(4293, \"Itabaiana (Mucurici)\", 8),\r\n" + 
					"(4294, \"Itabaianinha\", 25),\r\n" + 
					"(4295, \"Itabapoana (S�o Francisco de Itabapoana)\", 19),\r\n" + 
					"(4296, \"Itabatan (Mucuri)\", 5),\r\n" + 
					"(4297, \"Itabela\", 5),\r\n" + 
					"(4298, \"Itaber�\", 26),\r\n" + 
					"(4299, \"Itaberaba\", 5),\r\n" + 
					"(4300, \"Itabera�\", 9),\r\n" + 
					"(4301, \"Itabi\", 25),\r\n" + 
					"(4302, \"Itabira\", 11),\r\n" + 
					"(4303, \"Itabirinha\", 11),\r\n" + 
					"(4304, \"Itabirito\", 11),\r\n" + 
					"(4305, \"Itaboa (Ribeir�o Branco)\", 26),\r\n" + 
					"(4306, \"Itaboca (Santa Rita de Jacutinga)\", 11),\r\n" + 
					"(4307, \"Itabora�\", 19),\r\n" + 
					"(4308, \"Itabuna\", 5),\r\n" + 
					"(4309, \"Itacaj�\", 27),\r\n" + 
					"(4310, \"Itacambira\", 11),\r\n" + 
					"(4311, \"Itacarambi\", 11),\r\n" + 
					"(4312, \"Itacar�\", 5),\r\n" + 
					"(4313, \"Itacava (Cora��o de Maria)\", 5),\r\n" + 
					"(4314, \"Itachama (Amargosa)\", 5),\r\n" + 
					"(4315, \"Itaci (Carmo do Rio Claro)\", 11),\r\n" + 
					"(4316, \"Itacima (Guai�ba)\", 6),\r\n" + 
					"(4317, \"Itacimirim (Cama�ari)\", 5),\r\n" + 
					"(4318, \"Itacoatiara\", 3),\r\n" + 
					"(4319, \"Itacolomi (Concei��o do Mato Dentro)\", 11),\r\n" + 
					"(4320, \"Itacolomi (Gravata�)\", 23),\r\n" + 
					"(4321, \"Ita�u (Itagua�u)\", 8),\r\n" + 
					"(4322, \"Itacuruba\", 16),\r\n" + 
					"(4323, \"Itacurubi\", 23),\r\n" + 
					"(4324, \"Itacurubi (Santiago)\", 23),\r\n" + 
					"(4325, \"Itacuruss� (Mangaratiba)\", 19),\r\n" + 
					"(4326, \"Itaet�\", 5),\r\n" + 
					"(4327, \"Itagi\", 5),\r\n" + 
					"(4328, \"Itagib�\", 5),\r\n" + 
					"(4329, \"Itagimirim\", 5),\r\n" + 
					"(4330, \"Itagu� (Campos Sales)\", 6),\r\n" + 
					"(4331, \"Itagua�u\", 8),\r\n" + 
					"(4332, \"Itagua�u da Bahia\", 5),\r\n" + 
					"(4333, \"Itagua�u (S�o Sim�o)\", 9),\r\n" + 
					"(4334, \"Itagua�\", 19),\r\n" + 
					"(4335, \"Itaguaj�\", 18),\r\n" + 
					"(4336, \"Itaguara\", 11),\r\n" + 
					"(4337, \"Itaguari\", 9),\r\n" + 
					"(4338, \"Itaguaru\", 9),\r\n" + 
					"(4339, \"Itaguatins\", 27),\r\n" + 
					"(4340, \"Itahum (Dourados)\", 12),\r\n" + 
					"(4341, \"Ita�\", 26),\r\n" + 
					"(4342, \"Itaiacoca (Ponta Grossa)\", 18),\r\n" + 
					"(4343, \"Itaia (Firmino Alves)\", 5),\r\n" + 
					"(4344, \"Ita�ba\", 16),\r\n" + 
					"(4345, \"Ita�ba (Santa B�rbara do Sul)\", 23),\r\n" + 
					"(4346, \"Itaib� (Jequi�)\", 5),\r\n" + 
					"(4347, \"Itai�aba\", 6),\r\n" + 
					"(4348, \"Itaic� (Muniz Freire)\", 8),\r\n" + 
					"(4349, \"Itaimb� (Itagua�u)\", 8),\r\n" + 
					"(4350, \"Itaimbezinho (Bom Jesus)\", 23),\r\n" + 
					"(4351, \"Itaim (Cachoeira de Minas)\", 11),\r\n" + 
					"(4352, \"Itain�polis\", 17),\r\n" + 
					"(4353, \"Itai� (Itai�polis)\", 24),\r\n" + 
					"(4354, \"Itai�polis\", 24),\r\n" + 
					"(4355, \"Itaipaba (Pacajus)\", 6),\r\n" + 
					"(4356, \"Itaipava do Graja�\", 10),\r\n" + 
					"(4357, \"Itaipava (Itapemirim)\", 8),\r\n" + 
					"(4358, \"Itaip�\", 11),\r\n" + 
					"(4359, \"Itaipul�ndia\", 18),\r\n" + 
					"(4360, \"Itaipu (Vit�ria da Conquista)\", 5),\r\n" + 
					"(4361, \"Itaitinga\", 6),\r\n" + 
					"(4362, \"Itaituba\", 14),\r\n" + 
					"(4363, \"Ita�tu (Jacobina)\", 5),\r\n" + 
					"(4364, \"Itai�ba (Monte Apraz�vel)\", 26),\r\n" + 
					"(4365, \"Itaj�\", 20),\r\n" + 
					"(4366, \"Itaj�\", 9),\r\n" + 
					"(4367, \"Itaja�\", 24),\r\n" + 
					"(4368, \"Itaja� (Nova Cana�)\", 5),\r\n" + 
					"(4369, \"Itajara (Itaperuna)\", 19),\r\n" + 
					"(4370, \"Itajobi\", 26),\r\n" + 
					"(4371, \"Itaju\", 26),\r\n" + 
					"(4372, \"Itajub�\", 11),\r\n" + 
					"(4373, \"Itajub� (Descanso)\", 24),\r\n" + 
					"(4374, \"Itajubaquara (Gentio do Ouro)\", 5),\r\n" + 
					"(4375, \"Itajubatiba (Catingueira)\", 15),\r\n" + 
					"(4376, \"Itaju do Col�nia\", 5),\r\n" + 
					"(4377, \"Itaju�pe\", 5),\r\n" + 
					"(4378, \"Itajuru (Jequi�)\", 5),\r\n" + 
					"(4379, \"Itajutiba (Inhapim)\", 11),\r\n" + 
					"(4380, \"Italva\", 19),\r\n" + 
					"(4381, \"Itamaraju\", 5),\r\n" + 
					"(4382, \"Itamarandiba\", 11),\r\n" + 
					"(4383, \"Itamarati\", 3),\r\n" + 
					"(4384, \"Itamarati (�guas Vermelhas)\", 11),\r\n" + 
					"(4385, \"Itamarati de Minas\", 11),\r\n" + 
					"(4386, \"Itamarati Norte (Tangar� da Serra)\", 13),\r\n" + 
					"(4387, \"Itamari\", 5),\r\n" + 
					"(4388, \"Itamatar� (Carutapera)\", 10),\r\n" + 
					"(4389, \"Itambacuri\", 11),\r\n" + 
					"(4390, \"Itambarac�\", 18),\r\n" + 
					"(4391, \"Itamb�\", 5),\r\n" + 
					"(4392, \"Itamb�\", 18),\r\n" + 
					"(4393, \"Itamb�\", 16),\r\n" + 
					"(4394, \"Itamb� (Campo Largo)\", 18),\r\n" + 
					"(4395, \"Itamb� do Mato Dentro\", 11),\r\n" + 
					"(4396, \"Itambezinho (Campo Largo)\", 18),\r\n" + 
					"(4397, \"Itamira (Apor�)\", 5),\r\n" + 
					"(4398, \"Itamira (Ponto Belo)\", 8),\r\n" + 
					"(4399, \"Itamirim (Espinosa)\", 11),\r\n" + 
					"(4400, \"Itamogi\", 11),\r\n" + 
					"(4401, \"Itamonte\", 11),\r\n" + 
					"(4402, \"Itamotinga (Juazeiro)\", 5),\r\n" + 
					"(4403, \"Itamuri (Muria�)\", 11),\r\n" + 
					"(4404, \"Itanag� (Livramento de Nossa Senhora)\", 5),\r\n" + 
					"(4405, \"Itanagra\", 5),\r\n" + 
					"(4406, \"Itanha�m\", 26),\r\n" + 
					"(4407, \"Itanhandu\", 11),\r\n" + 
					"(4408, \"Itanhang�\", 13),\r\n" + 
					"(4409, \"Itanh�m\", 5),\r\n" + 
					"(4410, \"Itanhi (Janda�ra)\", 5),\r\n" + 
					"(4411, \"Itanhomi\", 11),\r\n" + 
					"(4412, \"Itans (Itapi�na)\", 6),\r\n" + 
					"(4413, \"Itaobim\", 11),\r\n" + 
					"(4414, \"Ita�ca\", 26),\r\n" + 
					"(4415, \"Ita�ca (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(4416, \"Itaocara\", 19),\r\n" + 
					"(4417, \"It�o (Itaqui)\", 23),\r\n" + 
					"(4418, \"Itapaci\", 9),\r\n" + 
					"(4419, \"Itapagipe\", 11),\r\n" + 
					"(4420, \"Itapaj�\", 6),\r\n" + 
					"(4421, \"Itapanhacanga (Castro)\", 18),\r\n" + 
					"(4422, \"Itapanhoacanga (Alvorada de Minas)\", 11),\r\n" + 
					"(4423, \"Itapara (Irati)\", 18),\r\n" + 
					"(4424, \"Itaparica\", 5),\r\n" + 
					"(4425, \"Itap�\", 5),\r\n" + 
					"(4426, \"Itapea�u (Urucurituba)\", 3),\r\n" + 
					"(4427, \"Itapebi\", 5),\r\n" + 
					"(4428, \"Itapebussu (Maranguape)\", 6),\r\n" + 
					"(4429, \"Itapecerica\", 11),\r\n" + 
					"(4430, \"Itapecerica da Serra\", 26),\r\n" + 
					"(4431, \"Itapeco� (Itapemirim)\", 8),\r\n" + 
					"(4432, \"Itapecuru Mirim\", 10),\r\n" + 
					"(4433, \"Itapeim (Beberibe)\", 6),\r\n" + 
					"(4434, \"Itapeipu (Jacobina)\", 5),\r\n" + 
					"(4435, \"Itapejara d'Oeste\", 18),\r\n" + 
					"(4436, \"Itapema\", 24),\r\n" + 
					"(4437, \"Itapemirim\", 8),\r\n" + 
					"(4438, \"Itapera (Icatu)\", 10),\r\n" + 
					"(4439, \"Itaperu�u\", 18),\r\n" + 
					"(4440, \"Itaperuna\", 19),\r\n" + 
					"(4441, \"Itaperuna (Barra de S�o Francisco)\", 8),\r\n" + 
					"(4442, \"Itapetim\", 16),\r\n" + 
					"(4443, \"Itapetinga\", 5),\r\n" + 
					"(4444, \"Itapetininga\", 26),\r\n" + 
					"(4445, \"Itapeuna (Eldorado)\", 26),\r\n" + 
					"(4446, \"Itapeva\", 11),\r\n" + 
					"(4447, \"Itapeva\", 26),\r\n" + 
					"(4448, \"Itapevi\", 26),\r\n" + 
					"(4449, \"Itapicuru\", 5),\r\n" + 
					"(4450, \"Itapicuru (Irec�)\", 5),\r\n" + 
					"(4451, \"Itapipoca\", 6),\r\n" + 
					"(4452, \"Itapira\", 26),\r\n" + 
					"(4453, \"Itapiranga\", 24),\r\n" + 
					"(4454, \"Itapiranga\", 3),\r\n" + 
					"(4455, \"Itapirapu�\", 9),\r\n" + 
					"(4456, \"Itapirapu� Paulista\", 26),\r\n" + 
					"(4457, \"Itapiratins\", 27),\r\n" + 
					"(4458, \"Itapirema (Vit�ria da Conquista)\", 5),\r\n" + 
					"(4459, \"Itapirucu (Palma)\", 11),\r\n" + 
					"(4460, \"Itapiru (Rubim)\", 11),\r\n" + 
					"(4461, \"Itapissuma\", 16),\r\n" + 
					"(4462, \"Itapitanga\", 5),\r\n" + 
					"(4463, \"Itapi�na\", 6),\r\n" + 
					"(4464, \"Itapixuna (Augusto Corr�a)\", 14),\r\n" + 
					"(4465, \"Itapo�\", 24),\r\n" + 
					"(4466, \"Itapocu (Araquari)\", 24),\r\n" + 
					"(4467, \"It�polis\", 26),\r\n" + 
					"(4468, \"Itap� (Pacatuba)\", 6),\r\n" + 
					"(4469, \"Itapor�\", 12),\r\n" + 
					"(4470, \"Itapor� do Tocantins\", 27),\r\n" + 
					"(4471, \"Itapora (Muritiba)\", 5),\r\n" + 
					"(4472, \"Itaporanga\", 26),\r\n" + 
					"(4473, \"Itaporanga\", 15),\r\n" + 
					"(4474, \"Itaporanga D'Ajuda\", 25),\r\n" + 
					"(4475, \"Itaporor� (Alegrete)\", 23),\r\n" + 
					"(4476, \"Itapororoca\", 15),\r\n" + 
					"(4477, \"Itapu� do Oeste\", 21),\r\n" + 
					"(4478, \"Itapu� (Viam�o)\", 23),\r\n" + 
					"(4479, \"Itapuca\", 23),\r\n" + 
					"(4480, \"Itapuc� (Anta Gorda)\", 23),\r\n" + 
					"(4481, \"Itapu�\", 26),\r\n" + 
					"(4482, \"Itapura\", 26),\r\n" + 
					"(4483, \"Itapura (Miguel Calmon)\", 5),\r\n" + 
					"(4484, \"Itapuranga\", 9),\r\n" + 
					"(4485, \"Itaquaquecetuba\", 26),\r\n" + 
					"(4486, \"Itaquara\", 5),\r\n" + 
					"(4487, \"Itaquara� (Brumado)\", 5),\r\n" + 
					"(4488, \"Itaqueri da Serra (Itirapina)\", 26),\r\n" + 
					"(4489, \"Itaqui\", 23),\r\n" + 
					"(4490, \"Itaqui (Campo Largo)\", 18),\r\n" + 
					"(4491, \"Itaquira�\", 12),\r\n" + 
					"(4492, \"Itaquitinga\", 16),\r\n" + 
					"(4493, \"Itarana\", 8),\r\n" + 
					"(4494, \"Itarantim\", 5),\r\n" + 
					"(4495, \"Itarar�\", 26),\r\n" + 
					"(4496, \"Itarema\", 6),\r\n" + 
					"(4497, \"Itariri\", 26),\r\n" + 
					"(4498, \"Itarum�\", 9),\r\n" + 
					"(4499, \"Itati\", 23),\r\n" + 
					"(4500, \"Itatiaia\", 19),\r\n" + 
					"(4501, \"Itatiaiu�u\", 11),\r\n" + 
					"(4502, \"Itatiba\", 26),\r\n" + 
					"(4503, \"Itatiba do Sul\", 23),\r\n" + 
					"(4504, \"Itati (Itoror�)\", 5),\r\n" + 
					"(4505, \"Itatim\", 5),\r\n" + 
					"(4506, \"Itatinga\", 26),\r\n" + 
					"(4507, \"Itatingui (Arataca)\", 5),\r\n" + 
					"(4508, \"Itatira\", 6),\r\n" + 
					"(4509, \"Itatuba\", 15),\r\n" + 
					"(4510, \"Itatup� (Gurup�)\", 14),\r\n" + 
					"(4511, \"Ita�\", 20),\r\n" + 
					"(4512, \"Ita�ba\", 13),\r\n" + 
					"(4513, \"Ita�ba (Arroio do Tigre)\", 23),\r\n" + 
					"(4514, \"Ita�ba (Estrela Velha)\", 23),\r\n" + 
					"(4515, \"Itaubal\", 4),\r\n" + 
					"(4516, \"Itau�u\", 9),\r\n" + 
					"(4517, \"Ita� de Minas\", 11),\r\n" + 
					"(4518, \"Itaueira\", 17),\r\n" + 
					"(4519, \"Ita�na\", 11),\r\n" + 
					"(4520, \"Ita�na (Caruaru)\", 16),\r\n" + 
					"(4521, \"Ita�na do Sul\", 18),\r\n" + 
					"(4522, \"Ita�nas (Concei��o da Barra)\", 8),\r\n" + 
					"(4523, \"Itauninha (Santa Maria de Itabira)\", 11),\r\n" + 
					"(4524, \"Itaverava\", 11),\r\n" + 
					"(4525, \"Itinga\", 11),\r\n" + 
					"(4526, \"Itinga do Maranh�o\", 10),\r\n" + 
					"(4527, \"Itinga (Paranagu�)\", 18),\r\n" + 
					"(4528, \"Itiquira\", 13),\r\n" + 
					"(4529, \"Itira (Ara�ua�)\", 11),\r\n" + 
					"(4530, \"Itirapina\", 26),\r\n" + 
					"(4531, \"Itirapu�\", 26),\r\n" + 
					"(4532, \"Itiru�u\", 5),\r\n" + 
					"(4533, \"Iti�ba\", 5),\r\n" + 
					"(4534, \"Itobi\", 26),\r\n" + 
					"(4535, \"Itoror�\", 5),\r\n" + 
					"(4536, \"Itoror� do Paranapanema (Pirapozinho)\", 26),\r\n" + 
					"(4537, \"Itu\", 26),\r\n" + 
					"(4538, \"Itua�u\", 5),\r\n" + 
					"(4539, \"Ituber�\", 5),\r\n" + 
					"(4540, \"Itueta\", 11),\r\n" + 
					"(4541, \"Itugua�u (Altinho)\", 16),\r\n" + 
					"(4543, \"Itu� (S�o Jo�o Nepomuceno)\", 11),\r\n" + 
					"(4544, \"Ituiutaba\", 11),\r\n" + 
					"(4545, \"Itumbiara\", 9),\r\n" + 
					"(4546, \"Itumirim\", 11),\r\n" + 
					"(4547, \"Itupeva\", 26),\r\n" + 
					"(4548, \"Itupeva (Medeiros Neto)\", 5),\r\n" + 
					"(4549, \"Itupiranga\", 14),\r\n" + 
					"(4550, \"Ituporanga\", 24),\r\n" + 
					"(4551, \"Iturama\", 11),\r\n" + 
					"(4552, \"Itutinga\", 11),\r\n" + 
					"(4553, \"Ituverava\", 26),\r\n" + 
					"(4554, \"Iubatinga (Caiabu)\", 26),\r\n" + 
					"(4555, \"Iuitepor� (Bonito)\", 16),\r\n" + 
					"(4556, \"Iuiu\", 5),\r\n" + 
					"(4557, \"I�na\", 8),\r\n" + 
					"(4558, \"Iva�\", 18),\r\n" + 
					"(4559, \"Iva� (Bossoroca)\", 23),\r\n" + 
					"(4560, \"Ivail�ndia (Engenheiro Beltr�o)\", 18),\r\n" + 
					"(4561, \"Ivaipor�\", 18),\r\n" + 
					"(4562, \"Ivaitinga (Nova Esperan�a)\", 18),\r\n" + 
					"(4563, \"Ivat�\", 18),\r\n" + 
					"(4564, \"Ivatuba\", 18),\r\n" + 
					"(4565, \"Ivinhema\", 12),\r\n" + 
					"(4566, \"Ivol�ndia\", 9),\r\n" + 
					"(4567, \"Ivor�\", 23),\r\n" + 
					"(4568, \"Ivoti\", 23),\r\n" + 
					"(4569, \"Jabaquara (Anchieta)\", 8),\r\n" + 
					"(4570, \"Jabitaca (Iguaraci)\", 16),\r\n" + 
					"(4571, \"Jaboat�o dos Guararapes\", 16),\r\n" + 
					"(4572, \"Jabor�\", 24),\r\n" + 
					"(4573, \"Jaborandi\", 5),\r\n" + 
					"(4574, \"Jaborandi\", 26),\r\n" + 
					"(4575, \"Jaborandi (Umuarama)\", 18),\r\n" + 
					"(4576, \"Jaboti\", 18),\r\n" + 
					"(4577, \"Jaboticaba\", 23),\r\n" + 
					"(4578, \"Jaboticabal\", 26),\r\n" + 
					"(4579, \"Jaboticabal (Ponta Grossa)\", 18),\r\n" + 
					"(4580, \"Jaboticatubas\", 11),\r\n" + 
					"(4581, \"Jaburuna (Ubajara)\", 6),\r\n" + 
					"(4582, \"Jaburu (Ponta Grossa)\", 18),\r\n" + 
					"(4583, \"Jabuti (Bonito)\", 12),\r\n" + 
					"(4584, \"Jacampari (Boa Viagem)\", 6),\r\n" + 
					"(4585, \"Ja�an�\", 20),\r\n" + 
					"(4586, \"Jacaraci\", 5),\r\n" + 
					"(4587, \"Jacarandira (Resende Costa)\", 11),\r\n" + 
					"(4588, \"Jacara�\", 15),\r\n" + 
					"(4589, \"Jacareacanga\", 14),\r\n" + 
					"(4590, \"Jacar� (Cabre�va)\", 26),\r\n" + 
					"(4591, \"Jacarecoara (Cascavel)\", 6),\r\n" + 
					"(4592, \"Jacar� dos Homens\", 2),\r\n" + 
					"(4593, \"Jacar� (Francisco Beltr�o)\", 18),\r\n" + 
					"(4594, \"Jacar� Grande (Caruaru)\", 16),\r\n" + 
					"(4595, \"Jacare�\", 26),\r\n" + 
					"(4596, \"Jacare� (Japor�)\", 12),\r\n" + 
					"(4597, \"Jacar� (Itinga)\", 11),\r\n" + 
					"(4598, \"Jacarezinho\", 18),\r\n" + 
					"(4599, \"Jacarezinho (Caruaru)\", 16),\r\n" + 
					"(4600, \"Jaca�na (Aquiraz)\", 6),\r\n" + 
					"(4601, \"Jaci\", 26),\r\n" + 
					"(4602, \"Jaciaba (Prudent�polis)\", 18),\r\n" + 
					"(4603, \"Jaciara\", 13),\r\n" + 
					"(4604, \"Jacigu� (Vargem Alta)\", 8),\r\n" + 
					"(4605, \"Jacil�ndia (Itapirapu�)\", 9),\r\n" + 
					"(4606, \"Jacinto\", 11),\r\n" + 
					"(4607, \"Jacinto Machado\", 24),\r\n" + 
					"(4608, \"Jaci Paran� (Porto Velho)\", 21),\r\n" + 
					"(4609, \"Jacipor� (Dracena)\", 26),\r\n" + 
					"(4610, \"Jacobina\", 5),\r\n" + 
					"(4611, \"Jacobina do Piau�\", 17),\r\n" + 
					"(4612, \"Jacuba (Arealva)\", 26),\r\n" + 
					"(4613, \"Jacu das Piranhas (Gameleiras)\", 11),\r\n" + 
					"(4614, \"Jacu�\", 11),\r\n" + 
					"(4615, \"Jacu�pe\", 2),\r\n" + 
					"(4616, \"Jacu�pe (S�o Sebasti�o do Passe)\", 5),\r\n" + 
					"(4617, \"Jacuizinho\", 23),\r\n" + 
					"(4618, \"Jacund�\", 14),\r\n" + 
					"(4619, \"Jacupiranga\", 26),\r\n" + 
					"(4620, \"Jacuruna (Jaguaripe)\", 5),\r\n" + 
					"(4621, \"Jacu (Terra Nova)\", 5),\r\n" + 
					"(4622, \"Jacutinga\", 11),\r\n" + 
					"(4623, \"Jacutinga\", 23),\r\n" + 
					"(4624, \"Jacutinga (Francisco Beltr�o)\", 18),\r\n" + 
					"(4625, \"Jacutinga (Goioxim)\", 18),\r\n" + 
					"(4626, \"Jacutinga (Ivaipor�)\", 18),\r\n" + 
					"(4627, \"Jacutinga (Santa Izabel do Oeste)\", 18),\r\n" + 
					"(4628, \"Jafa (Gar�a)\", 26),\r\n" + 
					"(4629, \"Jaguapit�\", 18),\r\n" + 
					"(4630, \"Jaguaquara\", 5),\r\n" + 
					"(4631, \"Jaguara�u\", 11),\r\n" + 
					"(4632, \"Jaguara (Feira de Santana)\", 5),\r\n" + 
					"(4633, \"Jaguar�o\", 23),\r\n" + 
					"(4634, \"Jaguar�o (Aracoiaba)\", 6),\r\n" + 
					"(4635, \"Jaguar�o Chico (Herval)\", 23),\r\n" + 
					"(4636, \"Jaguar�o Grande (Candiota)\", 23),\r\n" + 
					"(4637, \"Jaguar�o (Jacinto)\", 11),\r\n" + 
					"(4638, \"Jaguarari\", 5),\r\n" + 
					"(4639, \"Jaguarari (Acar�)\", 14),\r\n" + 
					"(4640, \"Jaguar�\", 8),\r\n" + 
					"(4641, \"Jaguaremb� (Itaocara)\", 19),\r\n" + 
					"(4642, \"Jaguaretama\", 6),\r\n" + 
					"(4643, \"Jaguarete (Erechim)\", 23),\r\n" + 
					"(4644, \"Jaguari\", 23),\r\n" + 
					"(4645, \"Jaguaria�va\", 18),\r\n" + 
					"(4646, \"Jaguaribara\", 6),\r\n" + 
					"(4647, \"Jaguaribe\", 6),\r\n" + 
					"(4648, \"Jaguaripe\", 5),\r\n" + 
					"(4649, \"Jaguaritira (Malacacheta)\", 11),\r\n" + 
					"(4650, \"Jaguari�na\", 26),\r\n" + 
					"(4651, \"Jaguaruana\", 6),\r\n" + 
					"(4652, \"Jaguaruna\", 24),\r\n" + 
					"(4653, \"Ja�ba\", 11),\r\n" + 
					"(4654, \"Ja�ba (Feira de Santana)\", 5),\r\n" + 
					"(4655, \"Jaibaras (Sobral)\", 6),\r\n" + 
					"(4656, \"Jaic�s\", 17),\r\n" + 
					"(4657, \"Jales\", 26),\r\n" + 
					"(4658, \"Jamacaru (Miss�o Velha)\", 6),\r\n" + 
					"(4659, \"Jamaica (Dracena)\", 26),\r\n" + 
					"(4660, \"Jamanxinzinho (Itaituba)\", 14),\r\n" + 
					"(4661, \"Jamapar� (Sapucaia)\", 19),\r\n" + 
					"(4662, \"Jambeiro\", 26),\r\n" + 
					"(4663, \"Jambua�u (S�o Francisco do Par�)\", 14),\r\n" + 
					"(4664, \"Jampruca\", 11),\r\n" + 
					"(4665, \"Jana�ba\", 11),\r\n" + 
					"(4666, \"Jandaia\", 9),\r\n" + 
					"(4667, \"Jandaia do Sul\", 18),\r\n" + 
					"(4668, \"Janda�ra\", 5),\r\n" + 
					"(4669, \"Janda�ra\", 20),\r\n" + 
					"(4670, \"Jandin�polis (Le�polis)\", 18),\r\n" + 
					"(4671, \"Jandira\", 26),\r\n" + 
					"(4672, \"Jandrangoeira (Independ�ncia)\", 6),\r\n" + 
					"(4673, \"Jandu�s\", 20),\r\n" + 
					"(4674, \"Jangada\", 13),\r\n" + 
					"(4675, \"Jangada (Cafezal do Sul)\", 18),\r\n" + 
					"(4676, \"Jangada do Sul (General Carneiro)\", 18),\r\n" + 
					"(4677, \"Jani�polis\", 18),\r\n" + 
					"(4678, \"Jansen (Farroupilha)\", 23),\r\n" + 
					"(4679, \"Janu�ria\", 11),\r\n" + 
					"(4680, \"Japara�ba\", 11),\r\n" + 
					"(4681, \"Japaratinga\", 2),\r\n" + 
					"(4682, \"Japaratuba\", 25),\r\n" + 
					"(4683, \"Japecanga (Pedra)\", 16),\r\n" + 
					"(4684, \"Japeri\", 19),\r\n" + 
					"(4685, \"Japerica (Primavera)\", 14),\r\n" + 
					"(4686, \"Japi\", 20),\r\n" + 
					"(4687, \"Japira\", 18),\r\n" + 
					"(4688, \"Japoat�\", 25),\r\n" + 
					"(4689, \"Japomirim (Itagib�)\", 5),\r\n" + 
					"(4690, \"Japonvar\", 11),\r\n" + 
					"(4691, \"Japor�\", 12),\r\n" + 
					"(4692, \"Japu�ba (Cachoeiras de Macacu)\", 19),\r\n" + 
					"(4693, \"Japu (Ilh�us)\", 5),\r\n" + 
					"(4694, \"Japur�\", 18),\r\n" + 
					"(4695, \"Japur�\", 3),\r\n" + 
					"(4696, \"Jaqueira\", 16),\r\n" + 
					"(4697, \"Jaquirana\", 23),\r\n" + 
					"(4698, \"Jaracati� (Goioer�)\", 18),\r\n" + 
					"(4699, \"Jaragu�\", 9),\r\n" + 
					"(4700, \"Jaragu� do Sul\", 24),\r\n" + 
					"(4701, \"Jaraguari\", 12),\r\n" + 
					"(4702, \"Jaramataia\", 2),\r\n" + 
					"(4703, \"Jardim\", 12),\r\n" + 
					"(4704, \"Jardim\", 6),\r\n" + 
					"(4705, \"Jardim ABC de Goi�s (Cidade Ocidental)\", 9),\r\n" + 
					"(4706, \"Jardim Alegre\", 18),\r\n" + 
					"(4707, \"Jardim Alegre (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(4708, \"Jardim de Angicos\", 20),\r\n" + 
					"(4709, \"Jardim de Piranhas\", 20),\r\n" + 
					"(4710, \"Jardim do Mulato\", 17),\r\n" + 
					"(4711, \"Jardim do Serid�\", 20),\r\n" + 
					"(4712, \"Jardimirim (Jardim)\", 6),\r\n" + 
					"(4713, \"Jardim (Londrina)\", 18),\r\n" + 
					"(4714, \"Jardim Olinda\", 18),\r\n" + 
					"(4715, \"Jardim (Paracuru)\", 6),\r\n" + 
					"(4716, \"Jardim Pared�o (Alt�nia)\", 18),\r\n" + 
					"(4717, \"Jardin�sia (Prata)\", 11),\r\n" + 
					"(4718, \"Jardin�polis\", 26),\r\n" + 
					"(4719, \"Jardin�polis\", 24),\r\n" + 
					"(4720, \"Jari\", 23),\r\n" + 
					"(4721, \"Jarinu\", 26),\r\n" + 
					"(4722, \"Jaru\", 21),\r\n" + 
					"(4723, \"Jarudore (Poxor�u)\", 13),\r\n" + 
					"(4724, \"Jata�\", 9),\r\n" + 
					"(4725, \"Jataizinho\", 18),\r\n" + 
					"(4726, \"Jata�ba\", 16),\r\n" + 
					"(4727, \"Jate�\", 12),\r\n" + 
					"(4728, \"Jati\", 6),\r\n" + 
					"(4729, \"Jati�ca (Triunfo)\", 16),\r\n" + 
					"(4730, \"Jatob�\", 10),\r\n" + 
					"(4731, \"Jatob�\", 16),\r\n" + 
					"(4732, \"Jatob� (Alto Alegre)\", 26),\r\n" + 
					"(4733, \"Jatob� do Piau�\", 17),\r\n" + 
					"(4734, \"Jatob� (Jaciara)\", 13),\r\n" + 
					"(4735, \"Ja�\", 26),\r\n" + 
					"(4736, \"Jau� (Cama�ari)\", 5),\r\n" + 
					"(4737, \"Ja� do Tocantins\", 27),\r\n" + 
					"(4738, \"Jaupaci\", 9),\r\n" + 
					"(4739, \"Jauru\", 13),\r\n" + 
					"(4740, \"Jauru (Coxim)\", 12),\r\n" + 
					"(4741, \"Javaca� (Campo Largo)\", 18),\r\n" + 
					"(4742, \"Jazidas (S�o Sep�)\", 23),\r\n" + 
					"(4743, \"Jeceaba\", 11),\r\n" + 
					"(4744, \"Jenipapeiro (Aracoiaba)\", 6),\r\n" + 
					"(4745, \"Jenipapo (Arapiraca)\", 2),\r\n" + 
					"(4746, \"Jenipapo de Minas\", 11),\r\n" + 
					"(4747, \"Jenipapo dos Vieiras\", 10),\r\n" + 
					"(4748, \"Jenipapo (Sanhar�)\", 16),\r\n" + 
					"(4749, \"Jequeri\", 11),\r\n" + 
					"(4750, \"Jequi� da Praia\", 2),\r\n" + 
					"(4751, \"Jequi�\", 5),\r\n" + 
					"(4752, \"Jequiri��\", 5),\r\n" + 
					"(4753, \"Jequita�\", 11),\r\n" + 
					"(4754, \"Jequitib�\", 11),\r\n" + 
					"(4755, \"Jequitinhonha\", 11),\r\n" + 
					"(4756, \"Jeremoabo\", 5),\r\n" + 
					"(4757, \"Jeric�\", 15),\r\n" + 
					"(4758, \"Jeriquara\", 26),\r\n" + 
					"(4759, \"Jeroaquara (Faina)\", 9),\r\n" + 
					"(4760, \"Jer�nimo Monteiro\", 8),\r\n" + 
					"(4761, \"Jerumenha\", 17),\r\n" + 
					"(4762, \"Jesu�nia\", 11),\r\n" + 
					"(4763, \"Jesu�tas\", 18),\r\n" + 
					"(4764, \"Jes�polis\", 9),\r\n" + 
					"(4765, \"Jijoca de Jericoacoara\", 6),\r\n" + 
					"(4766, \"Ji-Paran�\", 21),\r\n" + 
					"(4767, \"Jirau (Ita�ba)\", 16),\r\n" + 
					"(4768, \"Jiribatuba (Vera Cruz)\", 5),\r\n" + 
					"(4769, \"Jita�na\", 5),\r\n" + 
					"(4770, \"JK (Formosa)\", 9),\r\n" + 
					"(4771, \"Joa�aba\", 24),\r\n" + 
					"(4772, \"Joa�uba (Ecoporanga)\", 8),\r\n" + 
					"(4773, \"Joa�ma\", 11),\r\n" + 
					"(4774, \"Jo� (Joaquim T�vora)\", 18),\r\n" + 
					"(4775, \"Joana Coeli (Camet�)\", 14),\r\n" + 
					"(4776, \"Joana Peres (Bai�o)\", 14),\r\n" + 
					"(4777, \"Joan�polis (An�polis)\", 9),\r\n" + 
					"(4778, \"Joan�sia\", 11),\r\n" + 
					"(4779, \"Joanes (Salvaterra)\", 14),\r\n" + 
					"(4780, \"Joan�polis\", 26),\r\n" + 
					"(4781, \"Jo�o Alfredo\", 16),\r\n" + 
					"(4782, \"Jo�o Amaro (Ia�u)\", 5),\r\n" + 
					"(4783, \"Jo�o Arregui (Uruguaiana)\", 23),\r\n" + 
					"(4784, \"Jo�o C�mara\", 20),\r\n" + 
					"(4785, \"Jo�o Cordeiro (Santana do Acara�)\", 6),\r\n" + 
					"(4786, \"Jo�o Correia (Mucug�)\", 5),\r\n" + 
					"(4787, \"Jo�o Costa\", 17),\r\n" + 
					"(4788, \"Jo�o Dias\", 20),\r\n" + 
					"(4789, \"Jo�o Dourado\", 5),\r\n" + 
					"(4790, \"Jo�o Lisboa\", 10),\r\n" + 
					"(4791, \"Jo�o Monlevade\", 11),\r\n" + 
					"(4792, \"Jo�o Neiva\", 8),\r\n" + 
					"(4793, \"Jo�o Pessoa\", 15),\r\n" + 
					"(4794, \"Jo�o Pinheiro\", 11),\r\n" + 
					"(4795, \"Jo�o Ramalho\", 26),\r\n" + 
					"(4796, \"Jo�o Rodrigues (Rio Pardo)\", 23),\r\n" + 
					"(4797, \"Joaquim Fel�cio\", 11),\r\n" + 
					"(4798, \"Joaquim Gomes\", 2),\r\n" + 
					"(4799, \"Joaquim Nabuco\", 16),\r\n" + 
					"(4800, \"Joaquim Pires\", 17),\r\n" + 
					"(4801, \"Joaquim T�vora\", 18),\r\n" + 
					"(4802, \"Joatuba (Laranja da Terra)\", 8),\r\n" + 
					"(4803, \"Joca Claudino\", 15),\r\n" + 
					"(4804, \"Joca Marques\", 17),\r\n" + 
					"(4805, \"Jo�a Tavares (Bag�)\", 23),\r\n" + 
					"(4806, \"J�ia\", 23),\r\n" + 
					"(4807, \"Joinville\", 24),\r\n" + 
					"(4808, \"Jord�nia\", 11),\r\n" + 
					"(4809, \"Jord�o\", 1),\r\n" + 
					"(4810, \"Jord�o (Guarapuava)\", 18),\r\n" + 
					"(4811, \"Jord�o (Sobral)\", 6),\r\n" + 
					"(4812, \"Jorge Lacerda (Dion�sio Cerqueira)\", 24),\r\n" + 
					"(4813, \"Jos� Boiteux\", 24),\r\n" + 
					"(4814, \"Jos� Bonif�cio\", 26),\r\n" + 
					"(4815, \"Jos� Carlos (Apiac�)\", 8),\r\n" + 
					"(4816, \"Jos� da Costa (Gameleira)\", 16),\r\n" + 
					"(4817, \"Jos� da Penha\", 20),\r\n" + 
					"(4818, \"Jos� de Alencar (Iguatu)\", 6),\r\n" + 
					"(4819, \"Jos� de Freitas\", 17),\r\n" + 
					"(4820, \"Jos� Gon�alves de Minas\", 11),\r\n" + 
					"(4821, \"Jos� Gon�alves (Vit�ria da Conquista)\", 5),\r\n" + 
					"(4822, \"Jos� Lacerda (Reserva)\", 18),\r\n" + 
					"(4823, \"Josel�ndia\", 10),\r\n" + 
					"(4824, \"Josel�ndia (Bar�o de Melga�o)\", 13),\r\n" + 
					"(4825, \"Josel�ndia (Santana dos Montes)\", 11),\r\n" + 
					"(4826, \"Jos� Mariano (Ribeir�o)\", 16),\r\n" + 
					"(4827, \"Josen�polis\", 11),\r\n" + 
					"(4828, \"Jos� Ot�vio (Bag�)\", 23),\r\n" + 
					"(4829, \"Jos� Raydan\", 11),\r\n" + 
					"(4830, \"Jovi�nia\", 9),\r\n" + 
					"(4831, \"Juab� (Camet�)\", 14),\r\n" + 
					"(4832, \"Ju� (Caruaru)\", 16),\r\n" + 
					"(4833, \"Juacema (Jaguarari)\", 5),\r\n" + 
					"(4834, \"Ju� (Irau�uba)\", 6),\r\n" + 
					"(4835, \"Ju� (Penaforte)\", 6),\r\n" + 
					"(4836, \"Ju� (Quixad�)\", 6),\r\n" + 
					"(4837, \"Juara\", 13),\r\n" + 
					"(4838, \"Juarez T�vora\", 15),\r\n" + 
					"(4839, \"Juarina\", 27),\r\n" + 
					"(4840, \"Ju� (S�o Francisco de Paula)\", 23),\r\n" + 
					"(4841, \"Juatama (Quixad�)\", 6),\r\n" + 
					"(4842, \"Juatuba\", 11),\r\n" + 
					"(4843, \"Juazeirinho\", 15),\r\n" + 
					"(4844, \"Juazeiro\", 5),\r\n" + 
					"(4845, \"Juazeiro de Baixo (Morada Nova)\", 6),\r\n" + 
					"(4846, \"Juazeiro do Norte\", 6),\r\n" + 
					"(4847, \"Juazeiro do Piau�\", 17),\r\n" + 
					"(4848, \"Jubaia (Maranguape)\", 6),\r\n" + 
					"(4849, \"Juba� (Conquista)\", 11),\r\n" + 
					"(4850, \"Jubim (Salvaterra)\", 14),\r\n" + 
					"(4851, \"Juc� (Carir�)\", 6),\r\n" + 
					"(4852, \"Juca Ferrado (Surubim)\", 16),\r\n" + 
					"(4853, \"Ju�aral (Cabo de Santo Agostinho)\", 16),\r\n" + 
					"(4854, \"Juc�s\", 6),\r\n" + 
					"(4855, \"Jucati\", 16),\r\n" + 
					"(4856, \"Juciara (Kalor�)\", 18),\r\n" + 
					"(4857, \"Jucuru�u\", 5),\r\n" + 
					"(4858, \"Jucurutu\", 20),\r\n" + 
					"(4859, \"Juerana (Caravelas)\", 5),\r\n" + 
					"(4860, \"Ju�na\", 13),\r\n" + 
					"(4861, \"Juira�u (S�o Domingos do Prata)\", 11),\r\n" + 
					"(4862, \"Juiz de Fora\", 11),\r\n" + 
					"(4863, \"Juli�nia (Hercul�ndia)\", 26),\r\n" + 
					"(4864, \"J�lio Borges\", 17),\r\n" + 
					"(4865, \"J�lio Borges (Salto do Jacu�)\", 23),\r\n" + 
					"(4866, \"J�lio de Castilhos\", 23),\r\n" + 
					"(4867, \"J�lio Mesquita\", 26),\r\n" + 
					"(4868, \"Jumirim\", 26),\r\n" + 
					"(4869, \"Junco (Casinhas)\", 16),\r\n" + 
					"(4870, \"Junco de Minas (Malacacheta)\", 11),\r\n" + 
					"(4871, \"Junco do Maranh�o\", 10),\r\n" + 
					"(4872, \"Junco do Serid�\", 15),\r\n" + 
					"(4873, \"Junco (Juazeiro)\", 5),\r\n" + 
					"(4874, \"Jundi�\", 2),\r\n" + 
					"(4875, \"Jundi�\", 20),\r\n" + 
					"(4876, \"Jundia�\", 26),\r\n" + 
					"(4877, \"Jundia� do Sul\", 18),\r\n" + 
					"(4878, \"Junqueira (Monte Apraz�vel)\", 26),\r\n" + 
					"(4879, \"Junqueiro\", 2),\r\n" + 
					"(4880, \"Junqueir�polis\", 26),\r\n" + 
					"(4881, \"Jupagu� (Cotegipe)\", 5),\r\n" + 
					"(4882, \"Jupi\", 16),\r\n" + 
					"(4883, \"Jupi�\", 24),\r\n" + 
					"(4884, \"Juqui�\", 26),\r\n" + 
					"(4885, \"Juquiratiba (Conchas)\", 26),\r\n" + 
					"(4886, \"Juquitiba\", 26),\r\n" + 
					"(4887, \"Juraci (Marcion�lio Souza)\", 5),\r\n" + 
					"(4888, \"Jurama (Vila Val�rio)\", 8),\r\n" + 
					"(4889, \"Juramento\", 11),\r\n" + 
					"(4890, \"Juranda\", 18),\r\n" + 
					"(4891, \"Jur�ia (Monte Belo)\", 11),\r\n" + 
					"(4892, \"Jurema\", 16),\r\n" + 
					"(4893, \"Jurema\", 17),\r\n" + 
					"(4894, \"Juremal (Juazeiro)\", 5),\r\n" + 
					"(4895, \"Juripiranga\", 15),\r\n" + 
					"(4896, \"Juritianha (Acara�)\", 6),\r\n" + 
					"(4897, \"Juritis (Glic�rio)\", 26),\r\n" + 
					"(4898, \"Juru\", 15),\r\n" + 
					"(4899, \"Juru�\", 3),\r\n" + 
					"(4900, \"Juruaia\", 11),\r\n" + 
					"(4901, \"Juruc� (Jardin�polis)\", 26),\r\n" + 
					"(4902, \"Juruena\", 13),\r\n" + 
					"(4903, \"Jurumirim (Rio Casca)\", 11),\r\n" + 
					"(4904, \"Jurupeba (Palestina)\", 26),\r\n" + 
					"(4905, \"Jurupema (Taquaritinga)\", 26),\r\n" + 
					"(4906, \"Juruti\", 14),\r\n" + 
					"(4907, \"Juscel�ndia (Goian�sia)\", 9),\r\n" + 
					"(4908, \"Juscel�ndia (Rio Verde de Mato Grosso)\", 12),\r\n" + 
					"(4909, \"Juscelino Kubitschek (Formoso)\", 9),\r\n" + 
					"(4910, \"Juscimeira\", 13),\r\n" + 
					"(4911, \"Jussara\", 18),\r\n" + 
					"(4912, \"Jussara\", 5),\r\n" + 
					"(4913, \"Jussara\", 9),\r\n" + 
					"(4914, \"Jussara (Aragua�na)\", 27),\r\n" + 
					"(4915, \"Jussari\", 5),\r\n" + 
					"(4916, \"Jussiape\", 5),\r\n" + 
					"(4917, \"Justiniano Serpa (Aquiraz)\", 6),\r\n" + 
					"(4918, \"Juta�\", 3),\r\n" + 
					"(4919, \"Juta� (Santa Maria da Boa Vista)\", 16),\r\n" + 
					"(4920, \"Jut�\", 12),\r\n" + 
					"(4921, \"Juven�lia\", 11),\r\n" + 
					"(4922, \"Juvin�polis (Cascavel)\", 18),\r\n" + 
					"(4923, \"Kalor�\", 18),\r\n" + 
					"(4924, \"Km 19 (Maracan�)\", 14),\r\n" + 
					"(4925, \"Km 25 (Petrolina)\", 16),\r\n" + 
					"(4926, \"Km 26 (Maracan�)\", 14),\r\n" + 
					"(4927, \"km 30 (Tel�maco Borba)\", 18),\r\n" + 
					"(4928, \"Km Sete (Feira de Santana)\", 5),\r\n" + 
					"(4929, \"L�brea\", 3),\r\n" + 
					"(4930, \"Lacerda (Quixeramobim)\", 6),\r\n" + 
					"(4931, \"Lacerdina (Carangola)\", 11),\r\n" + 
					"(4932, \"Lacerd�polis\", 24),\r\n" + 
					"(4933, \"Ladainha\", 11),\r\n" + 
					"(4934, \"Lad�rio\", 12),\r\n" + 
					"(4935, \"Ladeira Grande (Maranguape)\", 6),\r\n" + 
					"(4936, \"Lafaiete Coutinho\", 5),\r\n" + 
					"(4937, \"Lagamar\", 11),\r\n" + 
					"(4938, \"Lagarto\", 25),\r\n" + 
					"(4939, \"Lageado de Ara�a�ba (Apia�)\", 26),\r\n" + 
					"(4940, \"Lages\", 24),\r\n" + 
					"(4941, \"Lages (Maranguape)\", 6),\r\n" + 
					"(4942, \"Lagoa\", 15),\r\n" + 
					"(4943, \"Lagoa Alegre\", 17),\r\n" + 
					"(4944, \"Lagoa Azul (Osvaldo Cruz)\", 26),\r\n" + 
					"(4945, \"Lagoa Bonita (Castro)\", 18),\r\n" + 
					"(4946, \"Lagoa Bonita (Cordisburgo)\", 11),\r\n" + 
					"(4947, \"Lagoa Bonita (Deod�polis)\", 12),\r\n" + 
					"(4948, \"Lagoa Bonita do Sul\", 23),\r\n" + 
					"(4949, \"Lagoa Branca (Casa Branca)\", 26),\r\n" + 
					"(4950, \"Lagoa (Castro)\", 18),\r\n" + 
					"(4951, \"Lagoa Clara (Maca�bas)\", 5),\r\n" + 
					"(4952, \"Lagoa da Areia (Palmeira dos �ndios)\", 2),\r\n" + 
					"(4953, \"Lagoa da Canoa\", 2),\r\n" + 
					"(4954, \"Lagoa da Confus�o\", 27),\r\n" + 
					"(4955, \"Lagoa da Cruz (Croat�)\", 6),\r\n" + 
					"(4956, \"Lagoa da Estiva (Anita Garibaldi)\", 24),\r\n" + 
					"(4957, \"Lagoa D'Anta\", 20),\r\n" + 
					"(4958, \"Lagoa Dantas (Palmeira dos �ndios)\", 2),\r\n" + 
					"(4959, \"Lagoa da Pedra (Arapiraca)\", 2),\r\n" + 
					"(4960, \"Lagoa da Pedra (Casinhas)\", 16),\r\n" + 
					"(4961, \"Lagoa da Prata\", 11),\r\n" + 
					"(4962, \"Lagoa das Pedras (Crate�s)\", 6),\r\n" + 
					"(4963, \"Lagoa da Vaca (Surubim)\", 16),\r\n" + 
					"(4964, \"Lagoa de Dentro\", 15),\r\n" + 
					"(4965, \"Lagoa de Dentro (Campina Grande)\", 15),\r\n" + 
					"(4966, \"Lagoa de Melqu�ades (Vit�ria da Conquista)\", 5),\r\n" + 
					"(4967, \"Lagoa de Pedras\", 20),\r\n" + 
					"(4968, \"Lagoa de S�o Francisco\", 17),\r\n" + 
					"(4969, \"Lagoa de S�o Jo�o (Aracoiaba)\", 6),\r\n" + 
					"(4970, \"Lagoa de S�o Jos� (Bom Conselho)\", 16),\r\n" + 
					"(4971, \"Lagoa de Velhos\", 20),\r\n" + 
					"(4972, \"Lagoa do Barro (Araripina)\", 16),\r\n" + 
					"(4973, \"Lagoa do Barro (Barreira)\", 6),\r\n" + 
					"(4974, \"Lagoa do Barro do Piau�\", 17),\r\n" + 
					"(4975, \"Lagoa do Bauzinho (Rio Verde)\", 9),\r\n" + 
					"(4976, \"Lagoa do Boi (Barro Alto)\", 5),\r\n" + 
					"(4977, \"Lagoa do Caldeir�o (Palmeira dos �ndios)\", 2),\r\n" + 
					"(4978, \"Lagoa do Canto (Palmeira dos �ndios)\", 2),\r\n" + 
					"(4979, \"Lagoa do Carneiro (Acara�)\", 6),\r\n" + 
					"(4980, \"Lagoa do Carro\", 16),\r\n" + 
					"(4981, \"Lagoa do Ex� (Palmeira dos �ndios)\", 2),\r\n" + 
					"(4982, \"Lagoa do Itaenga\", 16),\r\n" + 
					"(4983, \"Lagoa do Juvenal (Maranguape)\", 6),\r\n" + 
					"(4984, \"Lagoa do Mato\", 10),\r\n" + 
					"(4985, \"Lagoa do Mato (Itatira)\", 6),\r\n" + 
					"(4986, \"Lagoa do Ouro\", 16),\r\n" + 
					"(4987, \"Lagoa do Piau�\", 17),\r\n" + 
					"(4988, \"Lagoa do Rancho (Arapiraca)\", 2),\r\n" + 
					"(4989, \"Lagoa do Rancho (Palmeira dos �ndios)\", 2),\r\n" + 
					"(4990, \"Lagoa dos Crioulos (Salitre)\", 6),\r\n" + 
					"(4991, \"Lagoa dos Gatos\", 16),\r\n" + 
					"(4992, \"Lagoa do S�tio\", 17),\r\n" + 
					"(4993, \"Lagoa do Souza (Lagoa dos Gatos)\", 16),\r\n" + 
					"(4994, \"Lagoa dos Patos\", 11),\r\n" + 
					"(4995, \"Lagoa dos Patos (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(4996, \"Lagoa dos Ribas (Castro)\", 18),\r\n" + 
					"(4997, \"Lagoa dos Tr�s Cantos\", 23),\r\n" + 
					"(4998, \"Lagoa do Tocantins\", 27),\r\n" + 
					"(4999, \"Lagoa Dourada\", 11),\r\n" + 
					"(5000, \"Lagoa Dourada (Ponta Grossa)\", 18),\r\n" + 
					"(5001, \"Lagoa Formosa\", 11),\r\n" + 
					"(5002, \"Lagoa Funda (Gararu)\", 25),\r\n" + 
					"(5003, \"Lagoa Grande\", 16),\r\n" + 
					"(5004, \"Lagoa Grande\", 11),\r\n" + 
					"(5005, \"Lagoa Grande (Amontada)\", 6),\r\n" + 
					"(5006, \"Lagoa Grande (Barreira)\", 6),\r\n" + 
					"(5007, \"Lagoa Grande (C�ndido Sales)\", 5),\r\n" + 
					"(5008, \"Lagoa Grande de Minas Novas (Minas Novas)\", 11),\r\n" + 
					"(5009, \"Lagoa Grande do Maranh�o\", 10),\r\n" + 
					"(5010, \"Lagoa Grande (Ibipeba)\", 5),\r\n" + 
					"(5011, \"Lagoa Grande (Morada Nova)\", 6),\r\n" + 
					"(5012, \"Lagoa Grande (Russas)\", 6),\r\n" + 
					"(5013, \"Lagoa Grande (Taquarana)\", 2),\r\n" + 
					"(5014, \"Lagoa Jos� Lu�s (Vit�ria da Conquista)\", 5),\r\n" + 
					"(5015, \"Lagoa Nova\", 20),\r\n" + 
					"(5016, \"Lago�o\", 23),\r\n" + 
					"(5017, \"Lagoa (Petrolina)\", 16),\r\n" + 
					"(5018, \"Lagoa Preta (Tremedal)\", 5),\r\n" + 
					"(5019, \"Lagoa Real\", 5),\r\n" + 
					"(5020, \"Lagoa Salgada\", 20),\r\n" + 
					"(5021, \"Lagoa Santa\", 11),\r\n" + 
					"(5022, \"Lagoa Santa\", 9),\r\n" + 
					"(5023, \"Lagoa Seca\", 15),\r\n" + 
					"(5024, \"Lagoa Seca (Guarapuava)\", 18),\r\n" + 
					"(5025, \"Lagoa Seca (Varj�o de Minas)\", 11),\r\n" + 
					"(5026, \"Lagoa (Tel�maco Borba)\", 18),\r\n" + 
					"(5027, \"Lagoa Verde (Quitandinha)\", 18),\r\n" + 
					"(5028, \"Lagoa Vermelha\", 23),\r\n" + 
					"(5029, \"Lago da Pedra\", 10),\r\n" + 
					"(5030, \"Lago do Junco\", 10),\r\n" + 
					"(5031, \"Lago do Lim�o (Iranduba)\", 3),\r\n" + 
					"(5032, \"Lago dos Rodrigues\", 10),\r\n" + 
					"(5033, \"Lagoinha\", 26),\r\n" + 
					"(5034, \"Lagoinha do Piau�\", 17),\r\n" + 
					"(5035, \"Lagoinha (Quixer�)\", 6),\r\n" + 
					"(5036, \"Lagoinha (Tel�maco Borba)\", 18),\r\n" + 
					"(5037, \"Lagol�ndia (Piren�polis)\", 9),\r\n" + 
					"(5038, \"Lago Preto (Boa Vista do Ramos)\", 3),\r\n" + 
					"(5039, \"Lago Verde\", 10),\r\n" + 
					"(5040, \"Laguna\", 24),\r\n" + 
					"(5041, \"Laguna Carap�\", 12),\r\n" + 
					"(5042, \"Laje\", 5),\r\n" + 
					"(5043, \"Lajeado\", 27),\r\n" + 
					"(5044, \"Lajeado\", 23),\r\n" + 
					"(5045, \"Lajeado Bonito (Cotipor�)\", 23),\r\n" + 
					"(5046, \"Lajeado Bonito (Ortigueira)\", 18),\r\n" + 
					"(5047, \"Lajeado Bonito (Seberi)\", 23),\r\n" + 
					"(5048, \"Lajeado Bonito (Tiradentes do Sul)\", 23),\r\n" + 
					"(5049, \"Lajeado Cerne (Santo �ngelo)\", 23),\r\n" + 
					"(5050, \"Lajeado do Bugre\", 23),\r\n" + 
					"(5051, \"Lajeado Grande\", 24),\r\n" + 
					"(5052, \"Lajeado Grande (Guarapuava)\", 18),\r\n" + 
					"(5053, \"Lajeado Grande (Jaquirana)\", 23),\r\n" + 
					"(5054, \"Lajeado Grande (S�o Francisco de Paula)\", 23),\r\n" + 
					"(5055, \"Lajeado Novo\", 10),\r\n" + 
					"(5056, \"Lajeado (Ponta Grossa)\", 18),\r\n" + 
					"(5057, \"Lajeado (S�o Mateus do Sul)\", 18),\r\n" + 
					"(5058, \"Laje (Caruaru)\", 16),\r\n" + 
					"(5059, \"Lajed�o\", 5),\r\n" + 
					"(5060, \"Laje de S�o Jos� (Cupira)\", 16),\r\n" + 
					"(5061, \"Lajedinho\", 5),\r\n" + 
					"(5062, \"Lajedo\", 16),\r\n" + 
					"(5063, \"Lajedo Alto (Ia�u)\", 5),\r\n" + 
					"(5064, \"Laje do Banco (Aurelino Leal)\", 5),\r\n" + 
					"(5065, \"Lajedo do Cedro (Caruaru)\", 16),\r\n" + 
					"(5066, \"Lajedo do Tabocal\", 5),\r\n" + 
					"(5067, \"Laje do Muria�\", 19),\r\n" + 
					"(5068, \"Laje Grande (Catende)\", 16),\r\n" + 
					"(5069, \"Lajes\", 20),\r\n" + 
					"(5070, \"Lajes do Caldeir�o (Palmeira dos �ndios)\", 2),\r\n" + 
					"(5071, \"Lajes Pintadas\", 20),\r\n" + 
					"(5072, \"Lajinha\", 11),\r\n" + 
					"(5073, \"Lajinha (Pancas)\", 8),\r\n" + 
					"(5074, \"Lamar�o\", 5),\r\n" + 
					"(5075, \"Lamar�o do Passe (S�o Sebasti�o do Passe)\", 5),\r\n" + 
					"(5076, \"Lambari\", 11),\r\n" + 
					"(5077, \"Lambari D'Oeste\", 13),\r\n" + 
					"(5078, \"Lambari (Sapopema)\", 18),\r\n" + 
					"(5079, \"Lambedouro (Vi�osa do Cear�)\", 6),\r\n" + 
					"(5080, \"Lamim\", 11),\r\n" + 
					"(5081, \"Lamounier (Itapecerica)\", 11),\r\n" + 
					"(5082, \"Landri Sales\", 17),\r\n" + 
					"(5083, \"Lapa\", 18),\r\n" + 
					"(5084, \"Lapa (Gra�a)\", 6),\r\n" + 
					"(5085, \"Lap�o\", 5),\r\n" + 
					"(5086, \"Lapela (Vit�ria do Mearim)\", 10),\r\n" + 
					"(5087, \"Lapinha (Lagoa Santa)\", 11),\r\n" + 
					"(5088, \"Lara (Esperan�a do Sul)\", 23),\r\n" + 
					"(5089, \"Laranja Azeda (Tel�maco Borba)\", 18),\r\n" + 
					"(5090, \"Laranja da Terra\", 8),\r\n" + 
					"(5091, \"Laranjais (Itaocara)\", 19),\r\n" + 
					"(5092, \"Laranjal\", 18),\r\n" + 
					"(5093, \"Laranjal\", 11),\r\n" + 
					"(5094, \"Laranjal (Arapiraca)\", 2),\r\n" + 
					"(5095, \"Laranjal do Jari\", 4),\r\n" + 
					"(5096, \"Laranjal Paulista\", 26),\r\n" + 
					"(5097, \"Laranjeira (Marau)\", 23),\r\n" + 
					"(5098, \"Laranjeiras\", 25),\r\n" + 
					"(5099, \"Laranjeiras (Banabui�)\", 6),\r\n" + 
					"(5100, \"Laranjeiras de Caldas (Caldas)\", 11),\r\n" + 
					"(5101, \"Laranjeiras do Sul\", 18),\r\n" + 
					"(5102, \"Laranjeira (Vicente Dutra)\", 23),\r\n" + 
					"(5103, \"Laras (Laranjal Paulista)\", 26),\r\n" + 
					"(5104, \"Lara (Tr�s Passos)\", 23),\r\n" + 
					"(5105, \"Largo (Piritiba)\", 5),\r\n" + 
					"(5106, \"Lassance\", 11),\r\n" + 
					"(5107, \"Lastro\", 15),\r\n" + 
					"(5108, \"Laurentino\", 24),\r\n" + 
					"(5109, \"Lauro de Freitas\", 5),\r\n" + 
					"(5110, \"Lauro M�ller\", 24),\r\n" + 
					"(5111, \"Lauro Penteado (Clementina)\", 26),\r\n" + 
					"(5112, \"Lauro Sodr� (Curu��)\", 14),\r\n" + 
					"(5113, \"Lavandeira\", 27),\r\n" + 
					"(5114, \"Lava-P�s (Santiago)\", 23),\r\n" + 
					"(5115, \"Lav�nia\", 26),\r\n" + 
					"(5116, \"Lavouras (Alto Paraguai)\", 13),\r\n" + 
					"(5117, \"Lavra (Campo Largo)\", 18),\r\n" + 
					"(5118, \"Lavras\", 11),\r\n" + 
					"(5119, \"Lavras da Mangabeira\", 6),\r\n" + 
					"(5120, \"Lavras do Sul\", 23),\r\n" + 
					"(5121, \"Lavras Novas (Ouro Preto)\", 11),\r\n" + 
					"(5122, \"Lavrinha (Guarapuava)\", 18),\r\n" + 
					"(5123, \"Lavrinha (Pinhal�o)\", 18),\r\n" + 
					"(5124, \"Lavrinhas\", 26),\r\n" + 
					"(5125, \"Leandro (Barra do Corda)\", 10),\r\n" + 
					"(5126, \"Leandro Ferreira\", 11),\r\n" + 
					"(5127, \"Le�o (Campos Novos)\", 24),\r\n" + 
					"(5128, \"Lebon R�gis\", 24),\r\n" + 
					"(5129, \"Lejeado Micuim (Santo �ngelo)\", 23),\r\n" + 
					"(5130, \"Leme\", 26),\r\n" + 
					"(5131, \"Leme do Prado\", 11),\r\n" + 
					"(5132, \"Len��is\", 5),\r\n" + 
					"(5133, \"Len��is Paulista\", 26),\r\n" + 
					"(5134, \"Leoberto Leal\", 24),\r\n" + 
					"(5135, \"Leonel Rocha (Sagrada Fam�lia)\", 23),\r\n" + 
					"(5136, \"Leopoldina\", 11),\r\n" + 
					"(5137, \"Leopoldo de Bulh�es\", 9),\r\n" + 
					"(5138, \"Le�polis\", 18),\r\n" + 
					"(5139, \"L�rio de Cima (Surubim)\", 16),\r\n" + 
					"(5140, \"Lerol�ndia (Santa Rita)\", 15),\r\n" + 
					"(5141, \"Lerroville (Londrina)\", 18),\r\n" + 
					"(5142, \"Levin�polis (Janu�ria)\", 11),\r\n" + 
					"(5143, \"Liberato Salzano\", 23),\r\n" + 
					"(5144, \"Liberdade\", 11),\r\n" + 
					"(5145, \"Lic�nio de Almeida\", 5),\r\n" + 
					"(5146, \"Lidian�polis\", 18),\r\n" + 
					"(5147, \"L�dice (Rio Claro)\", 19),\r\n" + 
					"(5148, \"Liga��o do Par� (Paragominas)\", 14),\r\n" + 
					"(5149, \"Lima Campos\", 10),\r\n" + 
					"(5150, \"Lima Campos (Ic�)\", 6),\r\n" + 
					"(5151, \"Lima Duarte\", 11),\r\n" + 
					"(5152, \"Limeira\", 26),\r\n" + 
					"(5153, \"Limeira de Mantena (Mantena)\", 11),\r\n" + 
					"(5154, \"Limeira do Oeste\", 11),\r\n" + 
					"(5155, \"Limoeiro\", 16),\r\n" + 
					"(5156, \"Limoeiro (Castelo)\", 8),\r\n" + 
					"(5157, \"Limoeiro de Anadia\", 2),\r\n" + 
					"(5158, \"Limoeiro do Ajuru\", 14),\r\n" + 
					"(5159, \"Limoeiro do Bom Viver (Feira de Santana)\", 5),\r\n" + 
					"(5160, \"Limoeiro do Norte\", 6),\r\n" + 
					"(5161, \"Limoeiro (Japur�)\", 3),\r\n" + 
					"(5162, \"Lindoeste\", 18),\r\n" + 
					"(5163, \"Lind�ia\", 26),\r\n" + 
					"(5164, \"Lind�ia do Sul\", 24),\r\n" + 
					"(5165, \"Lindolfo Collor\", 23),\r\n" + 
					"(5166, \"Linha Bonita (Alto Alegre)\", 23),\r\n" + 
					"(5167, \"Linha Bonita (Toropi)\", 23),\r\n" + 
					"(5168, \"Linha Comprida (Salvador do Sul)\", 23),\r\n" + 
					"(5169, \"Linha das Palmeiras (Xavantina)\", 24),\r\n" + 
					"(5170, \"Linha Francesa Alta (Bar�o)\", 23),\r\n" + 
					"(5171, \"Linha Giacomini (Toledo)\", 18),\r\n" + 
					"(5172, \"Linha Gl�ria (Lagoa dos Tr�s Cantos)\", 23),\r\n" + 
					"(5173, \"Linha Harmonia (Guarani das Miss�es)\", 23),\r\n" + 
					"(5174, \"Linha Nova\", 23),\r\n" + 
					"(5175, \"Linha Ocidental (Arroio do Tigre)\", 23),\r\n" + 
					"(5176, \"Linha Porto Alegre (Guarani das Miss�es)\", 23),\r\n" + 
					"(5177, \"Linhares\", 8),\r\n" + 
					"(5178, \"Linha S�o Jo�o (Salvador do Sul)\", 23),\r\n" + 
					"(5179, \"Linha Silveira (Fontoura Xavier)\", 23),\r\n" + 
					"(5180, \"Linha Vit�ria (Almirante Tamandar� do Sul)\", 23),\r\n" + 
					"(5181, \"Lins\", 26),\r\n" + 
					"(5182, \"Lisieux (Santa Quit�ria)\", 6),\r\n" + 
					"(5183, \"Livramento\", 15),\r\n" + 
					"(5184, \"Livramento de Nossa Senhora\", 5),\r\n" + 
					"(5185, \"Livramento do Pacu� (Macap�)\", 4),\r\n" + 
					"(5186, \"Livramento do Ti�ma (Timba�ba)\", 16),\r\n" + 
					"(5187, \"Livramento (Ipueiras)\", 6),\r\n" + 
					"(5188, \"Livramento (Vertentes)\", 16),\r\n" + 
					"(5189, \"Lizarda\", 27),\r\n" + 
					"(5190, \"Loanda\", 18),\r\n" + 
					"(5191, \"Lobato\", 18),\r\n" + 
					"(5192, \"Lobo (Itatinga)\", 26),\r\n" + 
					"(5193, \"Lobo Leite (Congonhas)\", 11),\r\n" + 
					"(5194, \"Logradouro\", 15),\r\n" + 
					"(5195, \"Logradouro dos Le�es (Bom Conselho)\", 16),\r\n" + 
					"(5196, \"Logradouro (Santa Quit�ria)\", 6),\r\n" + 
					"(5197, \"Londrina\", 18),\r\n" + 
					"(5198, \"Lontra\", 11),\r\n" + 
					"(5199, \"Lontras\", 24),\r\n" + 
					"(5200, \"Lopei (Toledo)\", 18),\r\n" + 
					"(5201, \"Lorena\", 26),\r\n" + 
					"(5202, \"Loreto\", 10),\r\n" + 
					"(5203, \"Loreto (S�o Vicente do Sul)\", 23),\r\n" + 
					"(5204, \"Lourdes\", 26),\r\n" + 
					"(5205, \"Lourdes (Videira)\", 24),\r\n" + 
					"(5206, \"Louren�o (Cal�oene)\", 4),\r\n" + 
					"(5207, \"Louveira\", 26),\r\n" + 
					"(5208, \"Lovat (Umuarama)\", 18),\r\n" + 
					"(5209, \"Luanda (Serra Talhada)\", 16),\r\n" + 
					"(5210, \"Luar (S�o Jo�o do Iva�)\", 18),\r\n" + 
					"(5211, \"Lucaia (Planalto)\", 5),\r\n" + 
					"(5212, \"Lucas do Rio Verde\", 13),\r\n" + 
					"(5213, \"Luc�lia\", 26),\r\n" + 
					"(5214, \"Lucena\", 15),\r\n" + 
					"(5215, \"Lucialva (Jauru)\", 13),\r\n" + 
					"(5216, \"Lucian�polis\", 26),\r\n" + 
					"(5217, \"Luciara\", 13),\r\n" + 
					"(5218, \"Lucil�ndia (Montes Claros de Goi�s)\", 9),\r\n" + 
					"(5219, \"Lucr�cia\", 20),\r\n" + 
					"(5220, \"Lufa (Novo Cruzeiro)\", 11),\r\n" + 
					"(5221, \"Lu�s Ant�nio\", 26),\r\n" + 
					"(5222, \"Luisburgo\", 11),\r\n" + 
					"(5223, \"Lu�s Correia\", 17),\r\n" + 
					"(5224, \"Lu�s Domingues\", 10),\r\n" + 
					"(5225, \"Lu�s Eduardo Magalh�es\", 5),\r\n" + 
					"(5226, \"Lu�s Gomes\", 20),\r\n" + 
					"(5227, \"Luisl�ndia\", 11),\r\n" + 
					"(5228, \"Lu�s Viana (Casa Nova)\", 5),\r\n" + 
					"(5229, \"Luiz Alves\", 24),\r\n" + 
					"(5230, \"Luiz Alves (S�o Miguel do Araguaia)\", 9),\r\n" + 
					"(5231, \"Luiziana\", 18),\r\n" + 
					"(5232, \"Luizi�nia\", 26),\r\n" + 
					"(5233, \"Luizl�ndia do Oeste (Jo�o Pinheiro)\", 11),\r\n" + 
					"(5234, \"Luiz Pires de Minas (Cora��o de Jesus)\", 11),\r\n" + 
					"(5235, \"Lumin�rias\", 11),\r\n" + 
					"(5236, \"Luminosa (Bras�polis)\", 11),\r\n" + 
					"(5237, \"Lunardelli\", 18),\r\n" + 
					"(5238, \"Lup�rcio\", 26),\r\n" + 
					"(5239, \"Lupion�polis\", 18),\r\n" + 
					"(5240, \"Lustosa (Teodoro Sampaio)\", 5),\r\n" + 
					"(5241, \"Lut�cia\", 26),\r\n" + 
					"(5242, \"Luz\", 11),\r\n" + 
					"(5243, \"Luzerna\", 24),\r\n" + 
					"(5244, \"Luzi�nia\", 9),\r\n" + 
					"(5245, \"Luzi�polis (Campo Alegre)\", 2),\r\n" + 
					"(5246, \"Luzil�ndia\", 17),\r\n" + 
					"(5247, \"Luzin�polis\", 27),\r\n" + 
					"(5248, \"Macabuzinho (Concei��o de Macabu)\", 19),\r\n" + 
					"(5249, \"Macaco (Castro)\", 18),\r\n" + 
					"(5250, \"Maca�\", 19),\r\n" + 
					"(5251, \"Macaia (Bom Sucesso)\", 11),\r\n" + 
					"(5252, \"Maca�ba\", 20),\r\n" + 
					"(5253, \"Macajuba\", 5),\r\n" + 
					"(5254, \"Ma�ambar�\", 23),\r\n" + 
					"(5255, \"Macambira\", 25),\r\n" + 
					"(5256, \"Macambira (Poranga)\", 6),\r\n" + 
					"(5257, \"Maca�ca (Madalena)\", 6),\r\n" + 
					"(5258, \"Macap�\", 4),\r\n" + 
					"(5259, \"Macaparana\", 16),\r\n" + 
					"(5260, \"Macarani\", 5),\r\n" + 
					"(5261, \"Macara� (Santa Quit�ria)\", 6),\r\n" + 
					"(5262, \"Macatuba\", 26),\r\n" + 
					"(5263, \"Macau\", 20),\r\n" + 
					"(5264, \"Macaubal\", 26),\r\n" + 
					"(5265, \"Maca�bas\", 5),\r\n" + 
					"(5266, \"Maced�nia\", 26),\r\n" + 
					"(5267, \"Macei�\", 2),\r\n" + 
					"(5268, \"Macei� (Fortim)\", 6),\r\n" + 
					"(5269, \"Machacalis\", 11),\r\n" + 
					"(5270, \"Machadinho\", 23),\r\n" + 
					"(5271, \"Machadinho D'Oeste\", 21),\r\n" + 
					"(5272, \"Machado\", 11),\r\n" + 
					"(5273, \"Machado (Cuiab�)\", 13),\r\n" + 
					"(5274, \"Machados\", 16),\r\n" + 
					"(5275, \"Machados (Navegantes)\", 24),\r\n" + 
					"(5276, \"Macieira\", 24),\r\n" + 
					"(5277, \"Macuco\", 19),\r\n" + 
					"(5278, \"Macuco de Minas (Itumirim)\", 11),\r\n" + 
					"(5279, \"Macuco (Muria�)\", 11),\r\n" + 
					"(5280, \"Macucos (Getulina)\", 26),\r\n" + 
					"(5281, \"Macucos (Guarapuava)\", 18),\r\n" + 
					"(5282, \"Macuj� (Alian�a)\", 16),\r\n" + 
					"(5283, \"Macurur�\", 5),\r\n" + 
					"(5284, \"Madalena\", 6),\r\n" + 
					"(5285, \"Madeira (Candeias)\", 5),\r\n" + 
					"(5286, \"Madeiro\", 17),\r\n" + 
					"(5287, \"Madre de Deus\", 5),\r\n" + 
					"(5288, \"Madre de Deus de Minas\", 11),\r\n" + 
					"(5289, \"M�e D'�gua\", 15),\r\n" + 
					"(5290, \"M�e do Rio\", 14),\r\n" + 
					"(5291, \"Maetinga\", 5),\r\n" + 
					"(5292, \"Mafra\", 24),\r\n" + 
					"(5293, \"Magalh�es Barata\", 14),\r\n" + 
					"(5294, \"Magalh�es de Almeida\", 10),\r\n" + 
					"(5295, \"M�gda\", 26),\r\n" + 
					"(5296, \"Mag�\", 19),\r\n" + 
					"(5297, \"Magist�rio (Cidreira)\", 23),\r\n" + 
					"(5298, \"Maia (Bananeiras)\", 15),\r\n" + 
					"(5299, \"Maiauata (Igarap�-Miri)\", 14),\r\n" + 
					"(5300, \"Maioba (Pa�o do Lumiar)\", 10),\r\n" + 
					"(5301, \"Mair� (Lupion�polis)\", 18),\r\n" + 
					"(5302, \"Mairi\", 5),\r\n" + 
					"(5303, \"Mairinque\", 26),\r\n" + 
					"(5304, \"Mairipor�\", 26),\r\n" + 
					"(5305, \"Mairipotaba\", 9),\r\n" + 
					"(5306, \"Mait� (Guarapuava)\", 18),\r\n" + 
					"(5307, \"Major Ezequiel (Alvin�polis)\", 11),\r\n" + 
					"(5308, \"Major Felipe (Jos� da Penha)\", 20),\r\n" + 
					"(5309, \"Major Gercino\", 24),\r\n" + 
					"(5310, \"Major Isidoro\", 2),\r\n" + 
					"(5311, \"Majorl�ndia (Aracati)\", 6),\r\n" + 
					"(5312, \"Major Porto (Patos de Minas)\", 11),\r\n" + 
					"(5313, \"Major Prado (Santo Ant�nio do Aracangu�)\", 26),\r\n" + 
					"(5314, \"Major Sales\", 20),\r\n" + 
					"(5315, \"Major Simpl�cio (Nova Russas)\", 6),\r\n" + 
					"(5316, \"Major Vieira\", 24),\r\n" + 
					"(5317, \"Malacacheta\", 11),\r\n" + 
					"(5318, \"Malhada\", 5),\r\n" + 
					"(5319, \"Malhada Barreiras Queimadas (Caruaru)\", 16),\r\n" + 
					"(5320, \"Malhada de Pedra (Caruaru)\", 16),\r\n" + 
					"(5321, \"Malhada de Pedras\", 5),\r\n" + 
					"(5322, \"Malhada dos Bois\", 25),\r\n" + 
					"(5323, \"Malhada Grande (Santa Quit�ria)\", 6),\r\n" + 
					"(5324, \"Malhador\", 25),\r\n" + 
					"(5325, \"Mallet\", 18),\r\n" + 
					"(5326, \"Malta\", 15),\r\n" + 
					"(5327, \"Malu (Terra Boa)\", 18),\r\n" + 
					"(5328, \"Mamanguape\", 15),\r\n" + 
					"(5329, \"Mamba�\", 9),\r\n" + 
					"(5330, \"Mambor�\", 18),\r\n" + 
					"(5331, \"Mamonas\", 11),\r\n" + 
					"(5332, \"Mampituba\", 23),\r\n" + 
					"(5333, \"Manacapuru\", 3),\r\n" + 
					"(5334, \"Mana�ra\", 15),\r\n" + 
					"(5335, \"Manaquiri\", 3),\r\n" + 
					"(5336, \"Manari\", 16),\r\n" + 
					"(5337, \"Manaus\", 3),\r\n" + 
					"(5338, \"Manchinha (Tr�s de Maio)\", 23),\r\n" + 
					"(5339, \"M�ncio Lima\", 1),\r\n" + 
					"(5340, \"Mandacaia (Brejo da Madre de Deus)\", 16),\r\n" + 
					"(5341, \"Manda�aia (Guarapuava)\", 18),\r\n" + 
					"(5342, \"Mandacaru (Gravat�)\", 16),\r\n" + 
					"(5343, \"Mandagua�u\", 18),\r\n" + 
					"(5344, \"Mandaguari\", 18),\r\n" + 
					"(5345, \"Mandiocaba (Paranava�)\", 18),\r\n" + 
					"(5346, \"Mandirituba\", 18),\r\n" + 
					"(5347, \"Mandiroba (Sebasti�o Laranjeiras)\", 5),\r\n" + 
					"(5348, \"Manduri\", 26),\r\n" + 
					"(5349, \"Manfrin�polis\", 18),\r\n" + 
					"(5350, \"Manga\", 11),\r\n" + 
					"(5351, \"Mangabeira (Lavras da Mangabeira)\", 6),\r\n" + 
					"(5352, \"Mangabeiras (Arapiraca)\", 2),\r\n" + 
					"(5353, \"Mangara� (Santa Leopoldina)\", 8),\r\n" + 
					"(5354, \"Mangaratiba\", 19),\r\n" + 
					"(5355, \"Mangarat� (Nova Granada)\", 26),\r\n" + 
					"(5356, \"Mangueiras (Ros�rio do Sul)\", 23),\r\n" + 
					"(5357, \"Mangueirinha\", 18),\r\n" + 
					"(5358, \"Mangue Seco (Janda�ra)\", 5),\r\n" + 
					"(5359, \"Manhua�u\", 11),\r\n" + 
					"(5360, \"Manhumirim\", 11),\r\n" + 
					"(5361, \"Mania�u (Caetit�)\", 5),\r\n" + 
					"(5362, \"Manibu (Icapu�)\", 6),\r\n" + 
					"(5363, \"Mani�oba (S�o Caitano)\", 16),\r\n" + 
					"(5364, \"Manicor�\", 3),\r\n" + 
					"(5365, \"Manituba (Quixeramobim)\", 6),\r\n" + 
					"(5366, \"Maniva (S�o Francisco de Itabapoana)\", 19),\r\n" + 
					"(5367, \"Manjeiro (Mocajuba)\", 14),\r\n" + 
					"(5368, \"Manoel Em�dio\", 17),\r\n" + 
					"(5369, \"Manoel Guedes (Maranguape)\", 6),\r\n" + 
					"(5370, \"Manoel Ribas\", 18),\r\n" + 
					"(5371, \"Manoel Urbano\", 1),\r\n" + 
					"(5372, \"Manoel Viana\", 23),\r\n" + 
					"(5373, \"Manoel Vitorino\", 5),\r\n" + 
					"(5374, \"Mansid�o\", 5),\r\n" + 
					"(5375, \"Mantena\", 11),\r\n" + 
					"(5376, \"Manten�polis\", 8),\r\n" + 
					"(5377, \"Mantiba (Feira de Santana)\", 5),\r\n" + 
					"(5378, \"Mantiqueira do Palmital (Barbacena)\", 11),\r\n" + 
					"(5379, \"Mantiqueira (Santos Dumont)\", 11),\r\n" + 
					"(5380, \"Manuel Duarte (Rio das Flores)\", 19),\r\n" + 
					"(5381, \"Mapu� (Jaguaribe)\", 6),\r\n" + 
					"(5382, \"Maquin�\", 23),\r\n" + 
					"(5383, \"Mara�\", 3),\r\n" + 
					"(5384, \"Marab�\", 14),\r\n" + 
					"(5385, \"Marab� Paulista\", 26),\r\n" + 
					"(5386, \"Marab� (Tuneiras do Oeste)\", 18),\r\n" + 
					"(5387, \"Maraca�um�\", 10),\r\n" + 
					"(5388, \"Maraca�\", 26),\r\n" + 
					"(5389, \"Maracaj�\", 24),\r\n" + 
					"(5390, \"Maracaju\", 12),\r\n" + 
					"(5391, \"Maracan�\", 14),\r\n" + 
					"(5392, \"Maracan� (Castro)\", 18),\r\n" + 
					"(5393, \"Maracana�\", 6),\r\n" + 
					"(5394, \"Marac�s\", 5),\r\n" + 
					"(5395, \"Maragogi\", 2),\r\n" + 
					"(5396, \"Maragogipe\", 5),\r\n" + 
					"(5397, \"Maragogipinho (Aratu�pe)\", 5),\r\n" + 
					"(5398, \"Maraial\", 16),\r\n" + 
					"(5399, \"Maraj� do Sena\", 10),\r\n" + 
					"(5400, \"Marajoara (Reden��o)\", 14),\r\n" + 
					"(5401, \"Maraj� (Nova Aurora)\", 18),\r\n" + 
					"(5402, \"Marambainha (Cara�)\", 11),\r\n" + 
					"(5403, \"Marangatu (Santo Ant�nio de P�dua)\", 19),\r\n" + 
					"(5404, \"Maranguape\", 6),\r\n" + 
					"(5405, \"Maranh�ozinho\", 10),\r\n" + 
					"(5406, \"Marapanim\", 14),\r\n" + 
					"(5407, \"Marapoama\", 26),\r\n" + 
					"(5408, \"Marari (Tangar�)\", 24),\r\n" + 
					"(5409, \"Mara Rosa\", 9),\r\n" + 
					"(5410, \"Mararup� (Mauriti)\", 6),\r\n" + 
					"(5411, \"Marat�\", 23),\r\n" + 
					"(5412, \"Marata�zes\", 8),\r\n" + 
					"(5413, \"Marat� (S�o Domingos)\", 24),\r\n" + 
					"(5414, \"Marau\", 23),\r\n" + 
					"(5415, \"Mara�\", 5),\r\n" + 
					"(5416, \"Maravilha\", 2),\r\n" + 
					"(5417, \"Maravilha\", 24),\r\n" + 
					"(5418, \"Maravilha (Chor�)\", 6),\r\n" + 
					"(5419, \"Maravilha (Cust�dia)\", 16),\r\n" + 
					"(5420, \"Maravilha (Londrina)\", 18),\r\n" + 
					"(5421, \"Maravilha (Realeza)\", 18),\r\n" + 
					"(5422, \"Maravilhas\", 11),\r\n" + 
					"(5423, \"Marca��o\", 15),\r\n" + 
					"(5424, \"Marcel�ndia\", 13),\r\n" + 
					"(5425, \"Marcelino Ramos\", 23),\r\n" + 
					"(5426, \"Marcelino (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(5427, \"Marcelino Vieira\", 20),\r\n" + 
					"(5428, \"Marcian�polis (Goiatuba)\", 9),\r\n" + 
					"(5429, \"Marcian�polis (Santo Ant�nio do Sudoeste)\", 18),\r\n" + 
					"(5430, \"Marc�lio Dias (Canoinhas)\", 24),\r\n" + 
					"(5431, \"Marcion�lio Souza\", 5),\r\n" + 
					"(5432, \"Marco\", 6),\r\n" + 
					"(5433, \"Marcol�ndia\", 17),\r\n" + 
					"(5434, \"Marcolino Moura (Rio de Contas)\", 5),\r\n" + 
					"(5435, \"Marcond�sia (Monte Azul Paulista)\", 26),\r\n" + 
					"(5436, \"Marcorama (Garibaldi)\", 23),\r\n" + 
					"(5437, \"Marco Rondon (Pimenta Bueno)\", 21),\r\n" + 
					"(5438, \"Marcos Parente\", 17),\r\n" + 
					"(5439, \"Mar de Espanha\", 11),\r\n" + 
					"(5440, \"Marechal C�ndido Rondon\", 18),\r\n" + 
					"(5441, \"Marechal Deodoro\", 2),\r\n" + 
					"(5442, \"Marechal Floriano\", 8),\r\n" + 
					"(5443, \"Marechal Rondon (Campo Novo do Parecis)\", 13),\r\n" + 
					"(5444, \"Marechal Thaumaturgo\", 1),\r\n" + 
					"(5445, \"Marema\", 24),\r\n" + 
					"(5446, \"Maresias (S�o Sebasti�o)\", 26),\r\n" + 
					"(5447, \"Margarida (Marechal C�ndido Rondon)\", 18),\r\n" + 
					"(5448, \"Mar Grande (Vera Cruz)\", 5),\r\n" + 
					"(5449, \"Mari\", 15),\r\n" + 
					"(5450, \"Maria da F�\", 11),\r\n" + 
					"(5451, \"Maria Helena\", 18),\r\n" + 
					"(5452, \"Maria Luiza (Paranagu�)\", 18),\r\n" + 
					"(5453, \"Marialva\", 18),\r\n" + 
					"(5454, \"Mariana\", 11),\r\n" + 
					"(5455, \"Mariana Pimentel\", 23),\r\n" + 
					"(5456, \"Mariano Moro\", 23),\r\n" + 
					"(5457, \"Marian�polis do Tocantins\", 27),\r\n" + 
					"(5458, \"Marian�polis (Pedreiras)\", 10),\r\n" + 
					"(5459, \"Mariante (Ven�ncio Aires)\", 23),\r\n" + 
					"(5460, \"Mari�polis\", 26),\r\n" + 
					"(5461, \"Mari�polis (Os�rio)\", 23),\r\n" + 
					"(5462, \"Maria Quit�ria (Feira de Santana)\", 5),\r\n" + 
					"(5463, \"Maria Santa (Encruzilhada do Sul)\", 23),\r\n" + 
					"(5464, \"Maribondo\", 2),\r\n" + 
					"(5465, \"Maric�\", 19),\r\n" + 
					"(5466, \"Maricoabo (Valen�a)\", 5),\r\n" + 
					"(5467, \"Mariental (Lapa)\", 18),\r\n" + 
					"(5468, \"Mariflor (S�o Jos� do Cedro)\", 24),\r\n" + 
					"(5469, \"Marilac\", 11),\r\n" + 
					"(5470, \"Maril�ndia\", 8),\r\n" + 
					"(5471, \"Maril�ndia do Sul\", 18),\r\n" + 
					"(5472, \"Maril�ndia (Itapecerica)\", 11),\r\n" + 
					"(5473, \"Marilena\", 18),\r\n" + 
					"(5474, \"Mar�lia\", 26),\r\n" + 
					"(5475, \"Marilu (Iretama)\", 18),\r\n" + 
					"(5476, \"Mariluz\", 18),\r\n" + 
					"(5477, \"Marimbondo (Siqueira Campos)\", 18),\r\n" + 
					"(5478, \"Maring�\", 18),\r\n" + 
					"(5479, \"Marinheiros (Itapipoca)\", 6),\r\n" + 
					"(5480, \"Marin�polis\", 26),\r\n" + 
					"(5481, \"M�rio Campos\", 11),\r\n" + 
					"(5482, \"Mari�polis\", 18),\r\n" + 
					"(5483, \"Marip�\", 18),\r\n" + 
					"(5484, \"Marip� de Minas\", 11),\r\n" + 
					"(5485, \"Mariquita (Tabocas do Brejo Velho)\", 5),\r\n" + 
					"(5486, \"Maristela (Alto Paran�)\", 18),\r\n" + 
					"(5487, \"Maristela (Laranjal Paulista)\", 26),\r\n" + 
					"(5488, \"Marituba\", 14),\r\n" + 
					"(5489, \"Mariza (S�o Pedro do Iva�)\", 18),\r\n" + 
					"(5490, \"Mariz�polis\", 15),\r\n" + 
					"(5491, \"Marli�ria\", 11),\r\n" + 
					"(5492, \"Marmel�ndia (Realeza)\", 18),\r\n" + 
					"(5493, \"Marmeleiro\", 18),\r\n" + 
					"(5494, \"Marmeleiro (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(5495, \"Marmel�polis\", 11),\r\n" + 
					"(5496, \"Marombas Bossardi (Curitibanos)\", 24),\r\n" + 
					"(5497, \"Marombas (Brun�polis)\", 24),\r\n" + 
					"(5498, \"Marqu�s de Abrantes (Tunas do Paran�)\", 18),\r\n" + 
					"(5499, \"Marques de Souza\", 23),\r\n" + 
					"(5500, \"Marqu�s dos Reis (Jacarezinho)\", 18),\r\n" + 
					"(5501, \"Marquinho\", 18),\r\n" + 
					"(5502, \"Marrecas (Guarapuava)\", 18),\r\n" + 
					"(5503, \"Marrecas (Tau�)\", 6),\r\n" + 
					"(5504, \"Marrocos (Juazeiro do Norte)\", 6),\r\n" + 
					"(5505, \"Marru�s (Tau�)\", 6),\r\n" + 
					"(5506, \"Martin�sia (Uberl�ndia)\", 11),\r\n" + 
					"(5507, \"Martinho Campos\", 11),\r\n" + 
					"(5508, \"Martin�pole\", 6),\r\n" + 
					"(5509, \"Martin�polis\", 26),\r\n" + 
					"(5510, \"Martin�polis (Farol)\", 18),\r\n" + 
					"(5511, \"Martins\", 20),\r\n" + 
					"(5512, \"Martins (Arauc�ria)\", 18),\r\n" + 
					"(5513, \"Martins Guimar�es (Lagoa da Prata)\", 11),\r\n" + 
					"(5514, \"Martinsl�ndia (Guaraciaba do Norte)\", 6),\r\n" + 
					"(5515, \"Martins Soares\", 11),\r\n" + 
					"(5516, \"Marud� (Marapanim)\", 14),\r\n" + 
					"(5517, \"Maruim\", 25),\r\n" + 
					"(5518, \"Marumbi\", 18),\r\n" + 
					"(5519, \"Mar Vermelho\", 2),\r\n" + 
					"(5520, \"Marzag�o\", 9),\r\n" + 
					"(5521, \"Marzag�o (Ros�rio Oeste)\", 13),\r\n" + 
					"(5522, \"Mascote\", 5),\r\n" + 
					"(5523, \"Massacara (Euclides da Cunha)\", 5),\r\n" + 
					"(5524, \"Massangano (Petrolina)\", 16),\r\n" + 
					"(5525, \"Massap�\", 6),\r\n" + 
					"(5526, \"Massap� do Piau�\", 17),\r\n" + 
					"(5527, \"Massaranduba\", 15),\r\n" + 
					"(5528, \"Massaranduba\", 24),\r\n" + 
					"(5529, \"Massaranduba (Arapiraca)\", 2),\r\n" + 
					"(5530, \"Massaroca (Juazeiro)\", 5),\r\n" + 
					"(5531, \"Massauari (Boa Vista do Ramos)\", 3),\r\n" + 
					"(5532, \"Mata\", 23),\r\n" + 
					"(5533, \"Mata da Alian�a (Am�lia Rodrigues)\", 5),\r\n" + 
					"(5534, \"Mata Dentro (Cuiab�)\", 13),\r\n" + 
					"(5535, \"Mata de S�o Braz (Tenente Ananias)\", 20),\r\n" + 
					"(5536, \"Mata de S�o Jo�o\", 5),\r\n" + 
					"(5537, \"Mata Fresca (Aracati)\", 6),\r\n" + 
					"(5538, \"Mata Geral (Reden��o)\", 14),\r\n" + 
					"(5539, \"Mata Grande\", 2),\r\n" + 
					"(5540, \"Mata Limpa (Arapiraca)\", 2),\r\n" + 
					"(5541, \"Mata Limpa (Areia)\", 15),\r\n" + 
					"(5542, \"Mat�o\", 26),\r\n" + 
					"(5543, \"Matapiquara (Marapanim)\", 14),\r\n" + 
					"(5544, \"Mataraca\", 15),\r\n" + 
					"(5545, \"Matarazzo (Pedro Os�rio)\", 23),\r\n" + 
					"(5546, \"Mataripe (S�o Francisco do Conde)\", 5),\r\n" + 
					"(5547, \"Mata Roma\", 10),\r\n" + 
					"(5548, \"Mata (S�o Jos� de Ribamar)\", 10),\r\n" + 
					"(5549, \"Mata Verde\", 11),\r\n" + 
					"(5550, \"Mata Virgem (Umbuzeiro)\", 15),\r\n" + 
					"(5551, \"Mateiros\", 27),\r\n" + 
					"(5552, \"Matel�ndia\", 18),\r\n" + 
					"(5553, \"Materl�ndia\", 11),\r\n" + 
					"(5554, \"Mateus Leme\", 11),\r\n" + 
					"(5555, \"Mathias Lobato\", 11),\r\n" + 
					"(5556, \"Matias Barbosa\", 11),\r\n" + 
					"(5557, \"Matias Cardoso\", 11),\r\n" + 
					"(5558, \"Matias Ol�mpio\", 17),\r\n" + 
					"(5559, \"Matias (Pentecoste)\", 6),\r\n" + 
					"(5560, \"Matilde (Alfredo Chaves)\", 8),\r\n" + 
					"(5561, \"Matina\", 5),\r\n" + 
					"(5562, \"Matinada (Orob�)\", 16),\r\n" + 
					"(5563, \"Matinha\", 10),\r\n" + 
					"(5564, \"Matinha (Feira de Santana)\", 5),\r\n" + 
					"(5565, \"Matinhas\", 15),\r\n" + 
					"(5566, \"Matinha (Vit�ria da Conquista)\", 5),\r\n" + 
					"(5567, \"Matinhos\", 18),\r\n" + 
					"(5568, \"Matinhos (Guarapuava)\", 18),\r\n" + 
					"(5569, \"Matip�\", 11),\r\n" + 
					"(5570, \"Mato Castelhano\", 23),\r\n" + 
					"(5571, \"Mat�es\", 10),\r\n" + 
					"(5572, \"Mat�es do Norte\", 10),\r\n" + 
					"(5573, \"Mato Grande (Giru�)\", 23),\r\n" + 
					"(5574, \"Mato Grande (S�o Miguel das Miss�es)\", 23),\r\n" + 
					"(5575, \"Mato Grosso\", 15),\r\n" + 
					"(5576, \"Mato Leit�o\", 23),\r\n" + 
					"(5577, \"Mato Perso (Flores da Cunha)\", 23),\r\n" + 
					"(5578, \"Mato Queimado\", 23),\r\n" + 
					"(5579, \"Mato Queimado (Ponta Grossa)\", 18),\r\n" + 
					"(5580, \"Mato Rico\", 18),\r\n" + 
					"(5581, \"Matos Costa\", 24),\r\n" + 
					"(5582, \"Mato Verde\", 11),\r\n" + 
					"(5583, \"Matozinhos\", 11),\r\n" + 
					"(5584, \"Matrinch�\", 9),\r\n" + 
					"(5585, \"Matriz de Camaragibe\", 2),\r\n" + 
					"(5586, \"Matriz (Ipueiras)\", 6),\r\n" + 
					"(5587, \"Matup�\", 13),\r\n" + 
					"(5588, \"Matur�ia\", 15),\r\n" + 
					"(5589, \"Matutina\", 11),\r\n" + 
					"(5590, \"Mau�\", 26),\r\n" + 
					"(5591, \"Mau� (Arroio Grande)\", 23),\r\n" + 
					"(5592, \"Mau� da Serra\", 18),\r\n" + 
					"(5593, \"Mau� (Iju�)\", 23),\r\n" + 
					"(5594, \"Mau�s\", 3),\r\n" + 
					"(5595, \"Mauril�ndia\", 9),\r\n" + 
					"(5596, \"Mauril�ndia do Tocantins\", 27),\r\n" + 
					"(5597, \"Mauriti\", 6),\r\n" + 
					"(5598, \"Maxaranguape\", 20),\r\n" + 
					"(5599, \"Maximiliano de Almeida\", 23),\r\n" + 
					"(5600, \"Mazag�o\", 4),\r\n" + 
					"(5601, \"Mazag�o Velho (Mazag�o)\", 4),\r\n" + 
					"(5602, \"Medeiros\", 11),\r\n" + 
					"(5603, \"Medeiros Neto\", 5),\r\n" + 
					"(5604, \"Medianeira\", 18),\r\n" + 
					"(5605, \"Medianeira (Ajuricaba)\", 23),\r\n" + 
					"(5606, \"Medicil�ndia\", 14),\r\n" + 
					"(5607, \"Medina\", 11),\r\n" + 
					"(5608, \"Meia-Lua (Almirante Tamandar�)\", 18),\r\n" + 
					"(5609, \"Meia Ponte (Itumbiara)\", 9),\r\n" + 
					"(5610, \"Meleiro\", 24),\r\n" + 
					"(5611, \"Melga�o\", 14),\r\n" + 
					"(5612, \"Melga�o (Domingos Martins)\", 8),\r\n" + 
					"(5613, \"Mel (Juc�s)\", 6),\r\n" + 
					"(5614, \"Melo (Cuit�)\", 15),\r\n" + 
					"(5615, \"Melo Viana (Esmeraldas)\", 11),\r\n" + 
					"(5616, \"Mem�ria (Toledo)\", 18),\r\n" + 
					"(5617, \"Mendanha (Diamantina)\", 11),\r\n" + 
					"(5618, \"Mendes\", 19),\r\n" + 
					"(5619, \"Mendesl�ndia (Nossa Senhora das Gra�as)\", 18),\r\n" + 
					"(5620, \"Mendes Pimentel\", 11),\r\n" + 
					"(5621, \"Mendon�a\", 26),\r\n" + 
					"(5622, \"Mendon�a (Veredinha)\", 11),\r\n" + 
					"(5623, \"Menino Deus do Anapu (Igarap�-Miri)\", 14),\r\n" + 
					"(5624, \"Menino Jesus (Candeias)\", 5),\r\n" + 
					"(5625, \"Menino Jesus (Muniz Freire)\", 8),\r\n" + 
					"(5626, \"Mercedes\", 18),\r\n" + 
					"(5627, \"Merc�s\", 11),\r\n" + 
					"(5628, \"Merc�s de �gua Limpa (S�o Tiago)\", 11),\r\n" + 
					"(5629, \"Meridiano\", 26),\r\n" + 
					"(5630, \"Meruoca\", 6),\r\n" + 
					"(5631, \"Meru� (Igarap�-Miri)\", 14),\r\n" + 
					"(5632, \"Mes�polis\", 26),\r\n" + 
					"(5633, \"Mesquita\", 11),\r\n" + 
					"(5634, \"Mesquita\", 19),\r\n" + 
					"(5635, \"Messian�polis (Moipor�)\", 9),\r\n" + 
					"(5636, \"Messias\", 2),\r\n" + 
					"(5637, \"Messias Targino\", 20),\r\n" + 
					"(5638, \"Mestre Caetano (Sabar�)\", 11),\r\n" + 
					"(5639, \"Miguel Alves\", 17),\r\n" + 
					"(5640, \"Miguel Burnier (Ouro Preto)\", 11),\r\n" + 
					"(5641, \"Miguel Calmon\", 5),\r\n" + 
					"(5642, \"Miguel Le�o\", 17),\r\n" + 
					"(5643, \"Miguel�polis\", 26),\r\n" + 
					"(5644, \"Miguel Pereira\", 19),\r\n" + 
					"(5645, \"Miguel Xavier (Cariria�u)\", 6),\r\n" + 
					"(5646, \"Milagre (Monte Santo de Minas)\", 11),\r\n" + 
					"(5647, \"Milagres\", 5),\r\n" + 
					"(5648, \"Milagres\", 6),\r\n" + 
					"(5649, \"Milagres do Maranh�o\", 10),\r\n" + 
					"(5650, \"Milh�\", 6),\r\n" + 
					"(5651, \"Milho Verde (Serro)\", 11),\r\n" + 
					"(5652, \"Milton Belo (Aracoiaba)\", 6),\r\n" + 
					"(5653, \"Milton Brand�o\", 17),\r\n" + 
					"(5654, \"Mimoso de Goi�s\", 9),\r\n" + 
					"(5655, \"Mimoso do Sul\", 8),\r\n" + 
					"(5656, \"Mimoso (Pesqueira)\", 16),\r\n" + 
					"(5657, \"Mimoso (Santo Ant�nio do Leverger)\", 13),\r\n" + 
					"(5658, \"Mimoso (Surubim)\", 16),\r\n" + 
					"(5659, \"Mina�u\", 9),\r\n" + 
					"(5660, \"Minador do Negr�o\", 2),\r\n" + 
					"(5661, \"Minas do Esp�rito Santo (Barra do Mendes)\", 5),\r\n" + 
					"(5662, \"Minas do Le�o\", 23),\r\n" + 
					"(5663, \"Minas do Mimoso (Sento S�)\", 5),\r\n" + 
					"(5664, \"Minas Novas\", 11),\r\n" + 
					"(5665, \"Minduri\", 11),\r\n" + 
					"(5666, \"Mineirol�ndia (Pedra Branca)\", 6),\r\n" + 
					"(5667, \"Mineiros\", 9),\r\n" + 
					"(5668, \"Mineiros do Tiet�\", 26),\r\n" + 
					"(5669, \"Mingote (Herval)\", 23),\r\n" + 
					"(5670, \"Ministro Andreazza\", 21),\r\n" + 
					"(5671, \"Mirabela\", 11),\r\n" + 
					"(5672, \"Miracatu\", 26),\r\n" + 
					"(5673, \"Miracema\", 19),\r\n" + 
					"(5674, \"Miracema do Tocantins\", 27),\r\n" + 
					"(5675, \"Miracica (Garanhuns)\", 16),\r\n" + 
					"(5676, \"Mirador\", 10),\r\n" + 
					"(5677, \"Mirador\", 18),\r\n" + 
					"(5678, \"Mirador (Presidente Get�lio)\", 24),\r\n" + 
					"(5679, \"Miradouro\", 11),\r\n" + 
					"(5680, \"Mira Estrela\", 26),\r\n" + 
					"(5681, \"Miragaia (Ub�)\", 11),\r\n" + 
					"(5682, \"Miragem (Cariria�u)\", 6),\r\n" + 
					"(5683, \"Miragua�\", 23),\r\n" + 
					"(5684, \"Miraguaia (Santo Ant�nio da Patrulha)\", 23),\r\n" + 
					"(5685, \"Mira�\", 11),\r\n" + 
					"(5686, \"Mira�ma\", 6),\r\n" + 
					"(5687, \"Miralta (Montes Claros)\", 11),\r\n" + 
					"(5688, \"Miraluz (Neves Paulista)\", 26),\r\n" + 
					"(5689, \"Miramb� (Caucaia)\", 6),\r\n" + 
					"(5690, \"Miranda\", 12),\r\n" + 
					"(5691, \"Miranda (Capela)\", 25),\r\n" + 
					"(5692, \"Miranda do Norte\", 10),\r\n" + 
					"(5693, \"Miranda (Parambu)\", 6),\r\n" + 
					"(5694, \"Miranda (Tel�maco Borba)\", 18),\r\n" + 
					"(5695, \"Mirandela (Banza�)\", 5),\r\n" + 
					"(5696, \"Mirandiba\", 16),\r\n" + 
					"(5697, \"Mirand�polis\", 26),\r\n" + 
					"(5698, \"Mirand�polis (Guara�)\", 27),\r\n" + 
					"(5699, \"Mirangaba\", 5),\r\n" + 
					"(5700, \"Miranga (Pojuca)\", 5),\r\n" + 
					"(5701, \"Miranorte\", 27),\r\n" + 
					"(5702, \"Mirant�o (Bocaina de Minas)\", 11),\r\n" + 
					"(5703, \"Mirante\", 5),\r\n" + 
					"(5704, \"Mirante da Serra\", 21),\r\n" + 
					"(5705, \"Mirante do Paranapanema\", 26),\r\n" + 
					"(5706, \"Mirante do Piquiri (Alto Piquiri)\", 18),\r\n" + 
					"(5707, \"Miraporanga (Uberl�ndia)\", 11),\r\n" + 
					"(5708, \"Miraselva\", 18),\r\n" + 
					"(5709, \"Mirasselvas (Capanema)\", 14),\r\n" + 
					"(5710, \"Mirassol\", 26),\r\n" + 
					"(5711, \"Mirassol�ndia\", 26),\r\n" + 
					"(5712, \"Mirassol D'Oeste\", 13),\r\n" + 
					"(5713, \"Mirav�nia\", 11),\r\n" + 
					"(5714, \"Mirim Doce\", 24),\r\n" + 
					"(5715, \"Mirim (Imbituba)\", 24),\r\n" + 
					"(5716, \"Mirim (Santa Vit�ria do Palmar)\", 23),\r\n" + 
					"(5717, \"Mirim (Severiano de Almeida)\", 23),\r\n" + 
					"(5718, \"Mirinzal\", 10),\r\n" + 
					"(5719, \"Miritituba (Itaituba)\", 14),\r\n" + 
					"(5720, \"Missal\", 18),\r\n" + 
					"(5721, \"Miss�o Nova (Miss�o Velha)\", 6),\r\n" + 
					"(5722, \"Miss�o Velha\", 6),\r\n" + 
					"(5723, \"Missi (Irau�uba)\", 6),\r\n" + 
					"(5724, \"Mission�rio (Alto Rio Doce)\", 11),\r\n" + 
					"(5725, \"Mocajuba\", 14),\r\n" + 
					"(5726, \"Mocambeiro (Matozinhos)\", 11),\r\n" + 
					"(5727, \"Mocambinho (Porteirinha)\", 11),\r\n" + 
					"(5728, \"Mocambo (Guaraciaba do Norte)\", 6),\r\n" + 
					"(5729, \"Mocambo (Ibitiara)\", 5),\r\n" + 
					"(5730, \"Mocambo (Marco)\", 6),\r\n" + 
					"(5731, \"Mocambo (Parintins)\", 3),\r\n" + 
					"(5732, \"Mococa\", 26),\r\n" + 
					"(5733, \"Modelo\", 24),\r\n" + 
					"(5734, \"Moderna (Sert�nia)\", 16),\r\n" + 
					"(5735, \"Moeda\", 11),\r\n" + 
					"(5736, \"Moema\", 11),\r\n" + 
					"(5737, \"Mogeiro\", 15),\r\n" + 
					"(5738, \"Mogi das Cruzes\", 26),\r\n" + 
					"(5739, \"Mogi Gua�u\", 26),\r\n" + 
					"(5740, \"Mogi Mirim\", 26),\r\n" + 
					"(5741, \"Mogiqui�aba (Belmonte)\", 5),\r\n" + 
					"(5742, \"Moipor�\", 9),\r\n" + 
					"(5743, \"Moiraba (Camet�)\", 14),\r\n" + 
					"(5744, \"Moita Bonita\", 25),\r\n" + 
					"(5745, \"Moitas (Amontada)\", 6),\r\n" + 
					"(5746, \"Moju\", 14),\r\n" + 
					"(5747, \"Moju� dos Campos\", 14),\r\n" + 
					"(5748, \"Momba�a\", 6),\r\n" + 
					"(5749, \"Mombuca\", 26),\r\n" + 
					"(5750, \"Mon��o\", 10),\r\n" + 
					"(5751, \"Mon��es\", 26),\r\n" + 
					"(5752, \"Monda�\", 24),\r\n" + 
					"(5753, \"Mongagu�\", 26),\r\n" + 
					"(5754, \"Monguba (Pacatuba)\", 6),\r\n" + 
					"(5755, \"Monjolinho de Minas (Lagoa Formosa)\", 11),\r\n" + 
					"(5756, \"Monjolinho (Ortigueira)\", 18),\r\n" + 
					"(5757, \"Monjolos\", 11),\r\n" + 
					"(5758, \"Monnerat (Duas Barras)\", 19),\r\n" + 
					"(5759, \"Monsaras (Salvaterra)\", 14),\r\n" + 
					"(5760, \"Monsenhor Gil\", 17),\r\n" + 
					"(5761, \"Monsenhor Hip�lito\", 17),\r\n" + 
					"(5762, \"Monsenhor Horta (Mariana)\", 11),\r\n" + 
					"(5763, \"Monsenhor Isidro (Itaverava)\", 11),\r\n" + 
					"(5764, \"Monsenhor Jo�o Alexandre (Cl�udio)\", 11),\r\n" + 
					"(5765, \"Monsenhor Paulo\", 11),\r\n" + 
					"(5766, \"Monsenhor Tabosa\", 6),\r\n" + 
					"(5767, \"Montadas\", 15),\r\n" + 
					"(5768, \"Montalv�nia\", 11),\r\n" + 
					"(5769, \"Montanha\", 8),\r\n" + 
					"(5770, \"Montanhas\", 20),\r\n" + 
					"(5771, \"Montauri\", 23),\r\n" + 
					"(5772, \"Monte Alegre\", 14),\r\n" + 
					"(5773, \"Monte Alegre\", 20),\r\n" + 
					"(5774, \"Monte Alegre (Barro)\", 6),\r\n" + 
					"(5775, \"Monte Alegre (Cambori�)\", 24),\r\n" + 
					"(5776, \"Monte Alegre (Canind�)\", 6),\r\n" + 
					"(5777, \"Monte Alegre de Goi�s\", 9),\r\n" + 
					"(5778, \"Monte Alegre de Minas\", 11),\r\n" + 
					"(5779, \"Monte Alegre de Sergipe\", 25),\r\n" + 
					"(5780, \"Monte Alegre do Mau (Marapanim)\", 14),\r\n" + 
					"(5781, \"Monte Alegre do Piau�\", 17),\r\n" + 
					"(5782, \"Monte Alegre dos Campos\", 23),\r\n" + 
					"(5783, \"Monte Alegre do Sul\", 26),\r\n" + 
					"(5784, \"Monte Alegre (General C�mara)\", 23),\r\n" + 
					"(5785, \"Monte Alegre (Santiago)\", 23),\r\n" + 
					"(5786, \"Monte Alegre (Santo Ant�nio de P�dua)\", 19),\r\n" + 
					"(5787, \"Monte Alegre (S�o Joaquim do Monte)\", 16),\r\n" + 
					"(5788, \"Monte Alto\", 26),\r\n" + 
					"(5789, \"Monte Alverne (Crato)\", 6),\r\n" + 
					"(5790, \"Monte Alverne (Santa Cruz do Sul)\", 23),\r\n" + 
					"(5791, \"Monte Apraz�vel\", 26),\r\n" + 
					"(5792, \"Monte Azul\", 11),\r\n" + 
					"(5793, \"Monte Azul Paulista\", 26),\r\n" + 
					"(5794, \"Monte Belo\", 11),\r\n" + 
					"(5795, \"Monte Belo do Sul\", 23),\r\n" + 
					"(5796, \"Monte Belo (Sede Nova)\", 23),\r\n" + 
					"(5797, \"Monte Bonito (Pelotas)\", 23),\r\n" + 
					"(5798, \"Monte Branco (Jequi�)\", 5),\r\n" + 
					"(5799, \"Monte Cabr�o (Santos)\", 26),\r\n" + 
					"(5800, \"Monte Carlo\", 24),\r\n" + 
					"(5801, \"Monte Carmelo\", 11),\r\n" + 
					"(5802, \"Monte Carmelo do Rio Novo (Alto Rio Novo)\", 8),\r\n" + 
					"(5803, \"Monte Castelo\", 24),\r\n" + 
					"(5804, \"Monte Castelo\", 26),\r\n" + 
					"(5805, \"Monte Castelo (Campos Sales)\", 6),\r\n" + 
					"(5806, \"Monte Castelo (Chor�)\", 6),\r\n" + 
					"(5807, \"Monte Castelo (Pantano Grande)\", 23),\r\n" + 
					"(5808, \"Monte Celeste (S�o Geraldo)\", 11),\r\n" + 
					"(5809, \"Monte Cruzeiro (El�sio Medrado)\", 5),\r\n" + 
					"(5810, \"Monte das Gameleiras\", 20),\r\n" + 
					"(5811, \"Monte do Carmo\", 27),\r\n" + 
					"(5812, \"Monte Dourado (Almeirim)\", 14),\r\n" + 
					"(5813, \"Monte Formoso\", 11),\r\n" + 
					"(5814, \"Monte Gordo (Cama�ari)\", 5),\r\n" + 
					"(5815, \"Monte Grave (Milh�)\", 6),\r\n" + 
					"(5816, \"Monte Horebe\", 15),\r\n" + 
					"(5817, \"Monteiro\", 15),\r\n" + 
					"(5818, \"Monteiro Lobato\", 26),\r\n" + 
					"(5819, \"Monteir�polis\", 2),\r\n" + 
					"(5820, \"Monte Lindo (Goiatins)\", 27),\r\n" + 
					"(5821, \"Monte Mor\", 26),\r\n" + 
					"(5822, \"Montenebo (Crate�s)\", 6),\r\n" + 
					"(5823, \"Monte Negro\", 21),\r\n" + 
					"(5824, \"Montenegro\", 23),\r\n" + 
					"(5825, \"Monte Pio (Castelo)\", 8),\r\n" + 
					"(5826, \"Monte Real (Santo Ant�nio da Platina)\", 18),\r\n" + 
					"(5827, \"Monte Rec�ncavo (S�o Francisco do Conde)\", 5),\r\n" + 
					"(5828, \"Monte Rei (Juven�lia)\", 11),\r\n" + 
					"(5829, \"Montes Altos\", 10),\r\n" + 
					"(5830, \"Monte Santo\", 5),\r\n" + 
					"(5831, \"Monte Santo de Minas\", 11),\r\n" + 
					"(5832, \"Monte Santo do Tocantins\", 27),\r\n" + 
					"(5833, \"Montes Claros\", 11),\r\n" + 
					"(5834, \"Montes Claros de Goi�s\", 9),\r\n" + 
					"(5835, \"Montese (Itapor�)\", 12),\r\n" + 
					"(5836, \"Monte Si�o\", 11),\r\n" + 
					"(5837, \"Monte Sinai (Barra de S�o Francisco)\", 8),\r\n" + 
					"(5838, \"Monte Sion (Parambu)\", 6),\r\n" + 
					"(5839, \"Monte Verde (Camanducaia)\", 11),\r\n" + 
					"(5840, \"Monte Verde (Cambuci)\", 19),\r\n" + 
					"(5841, \"Monte Verde (Juiz de Fora)\", 11),\r\n" + 
					"(5842, \"Monte Verde Paulista (Cajobi)\", 26),\r\n" + 
					"(5843, \"Montevid�u (Concei��o)\", 15),\r\n" + 
					"(5844, \"Montezuma\", 11),\r\n" + 
					"(5845, \"Montividiu\", 9),\r\n" + 
					"(5846, \"Montividiu do Norte\", 9),\r\n" + 
					"(5847, \"Monumento (Pira�)\", 19),\r\n" + 
					"(5848, \"Morada Nova\", 6),\r\n" + 
					"(5849, \"Morada Nova de Minas\", 11),\r\n" + 
					"(5850, \"Morada Nova (Marab�)\", 14),\r\n" + 
					"(5851, \"Moraes Almeida (Itaituba)\", 14),\r\n" + 
					"(5852, \"Morais (Araripina)\", 16),\r\n" + 
					"(5853, \"Morangaba (Campos dos Goytacazes)\", 19),\r\n" + 
					"(5854, \"Morangas (Inoc�ncia)\", 12),\r\n" + 
					"(5855, \"Mora�jo\", 6),\r\n" + 
					"(5856, \"Moreil�ndia\", 16),\r\n" + 
					"(5857, \"Moreira Sales\", 18),\r\n" + 
					"(5858, \"Morello (Governador Lindenberg)\", 8),\r\n" + 
					"(5859, \"Moreno\", 16),\r\n" + 
					"(5860, \"Morma�o\", 23),\r\n" + 
					"(5861, \"Morpar�\", 5),\r\n" + 
					"(5862, \"Morraria do Sul (Bodoquena)\", 12),\r\n" + 
					"(5863, \"Morretes\", 18),\r\n" + 
					"(5864, \"Morrinhos\", 6),\r\n" + 
					"(5865, \"Morrinhos\", 9),\r\n" + 
					"(5866, \"Morrinhos do Sul\", 23),\r\n" + 
					"(5867, \"Morrinhos (Feira de Santana)\", 5),\r\n" + 
					"(5868, \"Morrinhos Novos (Guaraciaba do Norte)\", 6),\r\n" + 
					"(5869, \"Morrinhos (S�o Jer�nimo)\", 23),\r\n" + 
					"(5870, \"Morro Agudo\", 26),\r\n" + 
					"(5871, \"Morro Agudo de Goi�s\", 9),\r\n" + 
					"(5872, \"Morro Alto (Maquin�)\", 23),\r\n" + 
					"(5873, \"Morro Alto (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(5874, \"Morro Azul (Tr�s Cachoeiras)\", 23),\r\n" + 
					"(5875, \"Morro Branco (Itatira)\", 6),\r\n" + 
					"(5876, \"Morro Cabe�a no Tempo\", 17),\r\n" + 
					"(5877, \"Morro Chato (Turvo)\", 24),\r\n" + 
					"(5878, \"Morro da Fuma�a\", 24),\r\n" + 
					"(5879, \"Morro da Gar�a\", 11),\r\n" + 
					"(5880, \"Morro das Flores (Ruy Barbosa)\", 5),\r\n" + 
					"(5881, \"Morro de S�o Paulo (Cairu)\", 5),\r\n" + 
					"(5882, \"Morro do Alto (Itapetininga)\", 26),\r\n" + 
					"(5883, \"Morro do Chap�u\", 5),\r\n" + 
					"(5884, \"Morro do Chap�u do Piau�\", 17),\r\n" + 
					"(5885, \"Morro do C�co (Campos dos Goytacazes)\", 19),\r\n" + 
					"(5886, \"Morro do Ferro (Oliveira)\", 11),\r\n" + 
					"(5887, \"Morro do Pilar\", 11),\r\n" + 
					"(5888, \"Morro dos Leffas (Dom Pedro de Alc�ntara)\", 23),\r\n" + 
					"(5889, \"Morro Ga�cho (Vale Real)\", 23),\r\n" + 
					"(5890, \"Morro Grande\", 24),\r\n" + 
					"(5891, \"Morro Grande (Araruama)\", 19),\r\n" + 
					"(5892, \"Morro Ingl�s (Paranagu�)\", 18),\r\n" + 
					"(5893, \"Morro Redondo\", 23),\r\n" + 
					"(5894, \"Morro Reuter\", 23),\r\n" + 
					"(5895, \"Morros\", 10),\r\n" + 
					"(5896, \"Morro (S�o Francisco)\", 11),\r\n" + 
					"(5897, \"Morro Vermelho (Caet�)\", 11),\r\n" + 
					"(5898, \"Mortugaba\", 5),\r\n" + 
					"(5899, \"Morumbi (Eldorado)\", 12),\r\n" + 
					"(5900, \"Morungaba\", 26),\r\n" + 
					"(5901, \"Morungava (Gravata�)\", 23),\r\n" + 
					"(5903, \"Mosquito (Amontada)\", 6),\r\n" + 
					"(5904, \"Moss�medes\", 9),\r\n" + 
					"(5905, \"Mossor�\", 20),\r\n" + 
					"(5906, \"Mostardas\", 23),\r\n" + 
					"(5907, \"Mostardas (Monte Alegre do Sul)\", 26),\r\n" + 
					"(5908, \"Motuca\", 26),\r\n" + 
					"(5909, \"Moura (Barcelos)\", 3),\r\n" + 
					"(5910, \"Mour�o (Mari�polis)\", 26),\r\n" + 
					"(5911, \"Moxot� (Ibimirim)\", 16),\r\n" + 
					"(5912, \"Mozarl�ndia\", 9),\r\n" + 
					"(5913, \"Muan�\", 14),\r\n" + 
					"(5914, \"Mucaja�\", 22),\r\n" + 
					"(5915, \"Mucambo\", 6),\r\n" + 
					"(5916, \"Mucug�\", 5),\r\n" + 
					"(5917, \"Mu�um\", 23),\r\n" + 
					"(5918, \"Mucuri\", 5),\r\n" + 
					"(5919, \"Mucurici\", 8),\r\n" + 
					"(5920, \"Mucuri (Te�filo Otoni)\", 11),\r\n" + 
					"(5921, \"Muitos Cap�es\", 23),\r\n" + 
					"(5922, \"Muliterno\", 23),\r\n" + 
					"(5923, \"Mulungu\", 15),\r\n" + 
					"(5924, \"Mulungu\", 6),\r\n" + 
					"(5925, \"Mulungu do Morro\", 5),\r\n" + 
					"(5926, \"Mulungu (Piquet Carneiro)\", 6),\r\n" + 
					"(5927, \"Mulungu (Sanhar�)\", 16),\r\n" + 
					"(5928, \"Mumbaba (Massap�)\", 6),\r\n" + 
					"(5929, \"Mundau (Trairi)\", 6),\r\n" + 
					"(5930, \"Mundo Novo\", 12),\r\n" + 
					"(5931, \"Mundo Novo\", 5),\r\n" + 
					"(5932, \"Mundo Novo\", 9),\r\n" + 
					"(5933, \"Mundo Novo (Campos Borges)\", 23),\r\n" + 
					"(5934, \"Mundo Novo de Minas (Aimor�s)\", 11),\r\n" + 
					"(5935, \"Mundo Novo (Dores do Rio Preto)\", 8),\r\n" + 
					"(5936, \"Munguba (Santana do Munda�)\", 2),\r\n" + 
					"(5937, \"Munhoz\", 11),\r\n" + 
					"(5938, \"Munhoz de Melo\", 18),\r\n" + 
					"(5939, \"Muniz Ferreira\", 5),\r\n" + 
					"(5940, \"Muniz Freire\", 8),\r\n" + 
					"(5941, \"Muqu�m (Areia)\", 15),\r\n" + 
					"(5942, \"Muqu�m de S�o Francisco\", 5),\r\n" + 
					"(5943, \"Muqu�m (Mirabela)\", 11),\r\n" + 
					"(5944, \"Muqui\", 8),\r\n" + 
					"(5945, \"Muraj� (Curu��)\", 14),\r\n" + 
					"(5946, \"Muria�\", 11),\r\n" + 
					"(5947, \"Muribeca\", 25),\r\n" + 
					"(5948, \"Muribeca (Santa Quit�ria)\", 6),\r\n" + 
					"(5949, \"Murici\", 2),\r\n" + 
					"(5950, \"Murici (Caruaru)\", 16),\r\n" + 
					"(5951, \"Murici dos Portelas\", 17),\r\n" + 
					"(5952, \"Muricil�ndia\", 27),\r\n" + 
					"(5953, \"Muritiba\", 5),\r\n" + 
					"(5954, \"Murituba (Codaj�s)\", 3),\r\n" + 
					"(5955, \"Murta (Passa Sete)\", 23),\r\n" + 
					"(5956, \"Murucupi (Barcarena)\", 14),\r\n" + 
					"(5957, \"Murumuru (Marab�)\", 14),\r\n" + 
					"(5958, \"Murup� (Vic�ncia)\", 16),\r\n" + 
					"(5959, \"Murutinga (Autazes)\", 3),\r\n" + 
					"(5960, \"Murutinga do Sul\", 26),\r\n" + 
					"(5961, \"Mussurepe (Campos dos Goytacazes)\", 19),\r\n" + 
					"(5962, \"Mutambeiras (Santana do Acara�)\", 6),\r\n" + 
					"(5963, \"Muta (Ponta de Pedras)\", 14),\r\n" + 
					"(5964, \"Mutas (Guanambi)\", 5),\r\n" + 
					"(5965, \"Mutucal (Curu��)\", 14),\r\n" + 
					"(5966, \"Mutuca (Pesqueira)\", 16),\r\n" + 
					"(5967, \"Mutu�pe\", 5),\r\n" + 
					"(5968, \"Mutum\", 11),\r\n" + 
					"(5969, \"Mutum Paran� (Porto Velho)\", 21),\r\n" + 
					"(5970, \"Mutun�polis\", 9),\r\n" + 
					"(5971, \"Muzambinho\", 11),\r\n" + 
					"(5972, \"Nacip Raydan\", 11),\r\n" + 
					"(5973, \"Nag� (Maragogipe)\", 5),\r\n" + 
					"(5974, \"Nantes\", 26),\r\n" + 
					"(5975, \"Nanuque\", 11),\r\n" + 
					"(5976, \"N�o-Me-Toque\", 23),\r\n" + 
					"(5977, \"Naque\", 11),\r\n" + 
					"(5978, \"Naque-Nanuque (A�ucena)\", 11),\r\n" + 
					"(5979, \"Narandiba\", 26),\r\n" + 
					"(5980, \"Narandiba (Alagoinhas)\", 5),\r\n" + 
					"(5981, \"Naraniu (V�rzea Alegre)\", 6),\r\n" + 
					"(5982, \"Nascente (Amontada)\", 6),\r\n" + 
					"(5983, \"Nascente (Araripina)\", 16),\r\n" + 
					"(5984, \"Natal\", 20),\r\n" + 
					"(5985, \"Natal�ndia\", 11),\r\n" + 
					"(5986, \"Natal (Araguatins)\", 27),\r\n" + 
					"(5987, \"Nat�rcia\", 11),\r\n" + 
					"(5988, \"Natingui (Ortigueira)\", 18),\r\n" + 
					"(5989, \"Natin�polis (Goian�sia)\", 9),\r\n" + 
					"(5990, \"Natividade\", 19),\r\n" + 
					"(5991, \"Natividade\", 27),\r\n" + 
					"(5992, \"Natividade da Serra\", 26),\r\n" + 
					"(5993, \"Natuba\", 15),\r\n" + 
					"(5994, \"Navegantes\", 24),\r\n" + 
					"(5995, \"Navira�\", 12),\r\n" + 
					"(5996, \"Nazar�\", 5),\r\n" + 
					"(5997, \"Nazar�\", 27),\r\n" + 
					"(5998, \"Nazar� da Mata\", 16),\r\n" + 
					"(5999, \"Nazar� de Minas (Nepomuceno)\", 11),\r\n" + 
					"(6000, \"Nazar� de Mocajuba (Curu��)\", 14),\r\n" + 
					"(6001, \"Nazar� do Piau�\", 17),\r\n" + 
					"(6002, \"Nazar� dos Patos (Tucuru�)\", 14),\r\n" + 
					"(6003, \"Nazareno\", 11),\r\n" + 
					"(6004, \"Nazar� Paulista\", 26),\r\n" + 
					"(6005, \"Nazar� (Pocinhos)\", 15),\r\n" + 
					"(6006, \"Nazar� (Porto Velho)\", 21),\r\n" + 
					"(6007, \"Nazar� (Santiago)\", 23),\r\n" + 
					"(6008, \"Nazarezinho\", 15),\r\n" + 
					"(6009, \"Naz�ria\", 17),\r\n" + 
					"(6010, \"Naz�rio\", 9),\r\n" + 
					"(6011, \"Negras (Ita�ba)\", 16),\r\n" + 
					"(6012, \"Nelson de Sena (S�o Jo�o Evangelista)\", 11),\r\n" + 
					"(6013, \"Nenenl�ndia (Quixeramobim)\", 6),\r\n" + 
					"(6014, \"Neol�ndia (Itapecerica)\", 11),\r\n" + 
					"(6015, \"Ne�polis\", 25),\r\n" + 
					"(6016, \"Nepomuceno\", 11),\r\n" + 
					"(6017, \"Ner�polis\", 9),\r\n" + 
					"(6018, \"Neves (Jucati)\", 16),\r\n" + 
					"(6019, \"Neves Paulista\", 26),\r\n" + 
					"(6020, \"Nhamund�\", 3),\r\n" + 
					"(6021, \"Nhandeara\", 26),\r\n" + 
					"(6022, \"Nhandutiba (Manga)\", 11),\r\n" + 
					"(6023, \"Nhecol�ndia (Corumb�)\", 12),\r\n" + 
					"(6024, \"Nhungua�u (Teres�polis)\", 19),\r\n" + 
					"(6025, \"Nicol�ndia (Resplendor)\", 11),\r\n" + 
					"(6026, \"Nicolau Vergueiro\", 23),\r\n" + 
					"(6027, \"Nilo Pe�anha\", 5),\r\n" + 
					"(6028, \"Nil�polis\", 19),\r\n" + 
					"(6029, \"Nilza (Ipor�)\", 18),\r\n" + 
					"(6030, \"Nina Rodrigues\", 10),\r\n" + 
					"(6031, \"Ninheira\", 11),\r\n" + 
					"(6032, \"Nioaque\", 12),\r\n" + 
					"(6033, \"Nipo�\", 26),\r\n" + 
					"(6034, \"Niquel�ndia\", 9),\r\n" + 
					"(6035, \"N�sia Floresta\", 20),\r\n" + 
					"(6036, \"Niter�i\", 19),\r\n" + 
					"(6037, \"Nobres\", 13),\r\n" + 
					"(6038, \"Nogueira (Ava�)\", 26),\r\n" + 
					"(6039, \"Nonoai\", 23),\r\n" + 
					"(6040, \"Nonoai do Norte (Col�der)\", 13),\r\n" + 
					"(6041, \"Nordestina\", 5),\r\n" + 
					"(6042, \"Nordestina (Amapor�)\", 18),\r\n" + 
					"(6043, \"Normandia\", 22),\r\n" + 
					"(6044, \"Nortel�ndia\", 13),\r\n" + 
					"(6045, \"Nossa Senhora Aparecida\", 25),\r\n" + 
					"(6046, \"Nossa Senhora Aparecida (Belo Jardim)\", 16),\r\n" + 
					"(6047, \"Nossa Senhora Aparecida (Andir�)\", 18),\r\n" + 
					"(6048, \"Nossa Senhora Aparecida (Saldanha Marinho)\", 23),\r\n" + 
					"(6049, \"Nossa Senhora da Aparecida (Rol�ndia)\", 18),\r\n" + 
					"(6050, \"Nossa Senhora da Aparecida (Sapucaia)\", 19),\r\n" + 
					"(6051, \"Nossa Senhora da Candel�ria (Bandeirantes)\", 18),\r\n" + 
					"(6052, \"Nossa Senhora da Concei��o (S�o Sebasti�o do Ca�)\", 23),\r\n" + 
					"(6053, \"Nossa Senhora da Gl�ria\", 25),\r\n" + 
					"(6054, \"Nossa Senhora da Guia (Cuiab�)\", 13),\r\n" + 
					"(6055, \"Nossa Senhora da Luz (S�o Louren�o da Mata)\", 16),\r\n" + 
					"(6056, \"Nossa Senhora da Penha (Itaperuna)\", 19),\r\n" + 
					"(6057, \"Nossa Senhora da Sa�de (Ibiraiaras)\", 23),\r\n" + 
					"(6058, \"Nossa Senhora das Dores\", 25),\r\n" + 
					"(6059, \"Nossa Senhora das Gra�as\", 18),\r\n" + 
					"(6060, \"Nossa Senhora das Gra�as (I�na)\", 8),\r\n" + 
					"(6061, \"Nossa Senhora de Caravaggio (Nova Veneza)\", 24),\r\n" + 
					"(6062, \"Nossa Senhora de F�tima (Bela Vista)\", 12),\r\n" + 
					"(6063, \"Nossa Senhora de F�tima (Lagoa Vermelha)\", 23),\r\n" + 
					"(6064, \"Nossa Senhora de F�tima (Jaguar�)\", 8),\r\n" + 
					"(6065, \"Nossa Senhora de Lourdes (Cascavel)\", 18),\r\n" + 
					"(6066, \"Nossa Senhora de Lourdes\", 25),\r\n" + 
					"(6067, \"Nossa Senhora de Nazar�\", 17),\r\n" + 
					"(6068, \"Nossa Senhora do Carmo (Pombos)\", 16),\r\n" + 
					"(6069, \"Nossa Senhora do Carmo (Pato Branco)\", 18),\r\n" + 
					"(6070, \"Nossa Senhora do Livramento\", 13),\r\n" + 
					"(6071, \"Nossa Senhora do Livramento (Santa Rita)\", 15),\r\n" + 
					"(6072, \"Nossa Senhora do Livramento (Monsenhor Tabosa)\", 6),\r\n" + 
					"(6073, \"Nossa Senhora do � (Ipojuca)\", 16),\r\n" + 
					"(6074, \"Nossa Senhora do Rem�dio (Sales�polis)\", 26),\r\n" + 
					"(6075, \"Nossa Senhora do Socorro\", 25),\r\n" + 
					"(6076, \"Nossa Senhora dos Rem�dios\", 17),\r\n" + 
					"(6077, \"Nova Alegria (Itamaraju)\", 5),\r\n" + 
					"(6078, \"Nova Alexandria (C�ndido Mota)\", 26),\r\n" + 
					"(6079, \"Nova Alian�a\", 26),\r\n" + 
					"(6080, \"Nova Alian�a do Iva�\", 18),\r\n" + 
					"(6081, \"Nova Altamira (Faxinal)\", 18),\r\n" + 
					"(6082, \"Nova Alvorada\", 23),\r\n" + 
					"(6083, \"Nova Alvorada (Comodoro)\", 13),\r\n" + 
					"(6084, \"Nova Alvorada do Sul\", 12),\r\n" + 
					"(6085, \"Nova Am�rica\", 9),\r\n" + 
					"(6086, \"Nova Am�rica (Caarap�)\", 12),\r\n" + 
					"(6087, \"Nova Am�rica da Colina\", 18),\r\n" + 
					"(6088, \"Nova Am�rica (It�polis)\", 26),\r\n" + 
					"(6089, \"Nova Amoreira (Maril�ndia do Sul)\", 18),\r\n" + 
					"(6090, \"Nova Andradina\", 12),\r\n" + 
					"(6091, \"Nova Ara��\", 23),\r\n" + 
					"(6092, \"Nova Aurora\", 18),\r\n" + 
					"(6093, \"Nova Aurora\", 9),\r\n" + 
					"(6094, \"Nova Bandeirantes\", 13),\r\n" + 
					"(6095, \"Nova Bassano\", 23),\r\n" + 
					"(6096, \"Nova Bel�m\", 11),\r\n" + 
					"(6097, \"Nova Bet�nia (Farias Brito)\", 6),\r\n" + 
					"(6098, \"Nova Bet�nia (Nova Russas)\", 6),\r\n" + 
					"(6099, \"Nova Bilac (Flora�)\", 18),\r\n" + 
					"(6100, \"Nova Boa Vista\", 23),\r\n" + 
					"(6101, \"Nova Brasil�ndia\", 13),\r\n" + 
					"(6102, \"Nova Brasil�ndia D'Oeste\", 21),\r\n" + 
					"(6103, \"Nova Bras�lia (Araruna)\", 18),\r\n" + 
					"(6104, \"Nova Bras�lia do Itarar� (Carl�polis)\", 18),\r\n" + 
					"(6105, \"Nova Bras�lia (Nova Xavantina)\", 13),\r\n" + 
					"(6106, \"Nova Bras�lia (Ribeir�o do Largo)\", 5),\r\n" + 
					"(6107, \"Nova Bras�lia (Toledo)\", 18),\r\n" + 
					"(6108, \"Nova Br�scia\", 23),\r\n" + 
					"(6109, \"Nova C�ceres (C�ceres)\", 13),\r\n" + 
					"(6110, \"Nova Calif�rnia (Porto Velho)\", 21),\r\n" + 
					"(6111, \"Nova Campina\", 26),\r\n" + 
					"(6112, \"Nova Cana�\", 5),\r\n" + 
					"(6113, \"Nova Cana� do Norte\", 13),\r\n" + 
					"(6114, \"Nova Cana� Paulista\", 26),\r\n" + 
					"(6115, \"Nova Candel�ria\", 23),\r\n" + 
					"(6116, \"Nova Cantu\", 18),\r\n" + 
					"(6117, \"Nova Cardoso (Itajobi)\", 26),\r\n" + 
					"(6118, \"Nova Casa Verde (Nova Andradina)\", 12),\r\n" + 
					"(6119, \"Nova Castilho\", 26),\r\n" + 
					"(6120, \"Nova Catanduva (Rondon�polis)\", 13),\r\n" + 
					"(6121, \"Nova Colina (Ji-Paran�)\", 21),\r\n" + 
					"(6122, \"Nova Colinas\", 10),\r\n" + 
					"(6123, \"Nova Conc�rdia (Francisco Beltr�o)\", 18),\r\n" + 
					"(6124, \"Nova Conquista (Vilhena)\", 21),\r\n" + 
					"(6125, \"Nova Crix�s\", 9),\r\n" + 
					"(6126, \"Nova Cruz\", 20),\r\n" + 
					"(6127, \"Nova Cruz (Igarassu)\", 16),\r\n" + 
					"(6128, \"Nova Cultura (Papanduva)\", 24),\r\n" + 
					"(6129, \"Nova Descoberta (Petrolina)\", 16),\r\n" + 
					"(6130, \"Nova Dimens�o (Nova Mamor�)\", 21),\r\n" + 
					"(6131, \"Nova Era\", 11),\r\n" + 
					"(6132, \"Nova Erechim\", 24),\r\n" + 
					"(6133, \"Nova Esperan�a\", 18),\r\n" + 
					"(6134, \"Nova Esperan�a do Piri�\", 14),\r\n" + 
					"(6135, \"Nova Esperan�a do Sudoeste\", 18),\r\n" + 
					"(6136, \"Nova Esperan�a do Sul\", 23),\r\n" + 
					"(6137, \"Nova Esperan�a (Espig�o do Oeste)\", 21),\r\n" + 
					"(6138, \"Nova Esperan�a (Jate�)\", 12),\r\n" + 
					"(6139, \"Nova Esperan�a (Montes Claros)\", 11),\r\n" + 
					"(6140, \"Nova Esperan�a (Rio Negro)\", 12),\r\n" + 
					"(6141, \"Nova Europa\", 26),\r\n" + 
					"(6142, \"Nova F�tima\", 18),\r\n" + 
					"(6143, \"Nova F�tima\", 5),\r\n" + 
					"(6144, \"Nova F�tima (Hidrol�ndia)\", 9),\r\n" + 
					"(6145, \"Nova F�tima (Ipueiras)\", 6),\r\n" + 
					"(6146, \"Nova Floresta\", 15),\r\n" + 
					"(6147, \"Nova Floresta (Jaguaribe)\", 6),\r\n" + 
					"(6148, \"Nova Friburgo\", 19),\r\n" + 
					"(6149, \"Nova Galileia (Rondon�polis)\", 13),\r\n" + 
					"(6150, \"Nova Gl�ria\", 9),\r\n" + 
					"(6151, \"Nova Granada\", 26),\r\n" + 
					"(6152, \"Nova Guarita\", 13),\r\n" + 
					"(6153, \"Nova Guarita (Sombrio)\", 24),\r\n" + 
					"(6154, \"Nova Guataporanga\", 26),\r\n" + 
					"(6155, \"Nova Hartz\", 23),\r\n" + 
					"(6156, \"Nova Ibi�\", 5),\r\n" + 
					"(6157, \"Nova Igua�u\", 19),\r\n" + 
					"(6158, \"Nova Igua�u de Goi�s\", 9),\r\n" + 
					"(6159, \"Nova Independ�ncia\", 26),\r\n" + 
					"(6160, \"Nova Iorque\", 10),\r\n" + 
					"(6161, \"Nova Ipixuna\", 14),\r\n" + 
					"(6162, \"Novais\", 26),\r\n" + 
					"(6163, \"Nova Itaberaba\", 24),\r\n" + 
					"(6164, \"Nova Itaip� (Planaltino)\", 5),\r\n" + 
					"(6165, \"Nova Itapirema (Nova Alian�a)\", 26),\r\n" + 
					"(6166, \"Nova Itarana\", 5),\r\n" + 
					"(6167, \"Nova Jales (Parana�ba)\", 12),\r\n" + 
					"(6168, \"Nova Lacerda\", 13),\r\n" + 
					"(6169, \"Nova Laranjeiras\", 18),\r\n" + 
					"(6170, \"Nova L�dice (Medeiros Neto)\", 5),\r\n" + 
					"(6171, \"Nova Lima\", 11),\r\n" + 
					"(6172, \"Nova Londrina\", 18),\r\n" + 
					"(6173, \"Nova Londrina (Ji-Paran�)\", 21),\r\n" + 
					"(6174, \"Nova Lourdes (S�o Jo�o)\", 18),\r\n" + 
					"(6175, \"Nova Luzit�nia\", 26),\r\n" + 
					"(6176, \"Nova Mamor�\", 21),\r\n" + 
					"(6177, \"Nova Maril�ndia\", 13),\r\n" + 
					"(6178, \"Nova Maring�\", 13),\r\n" + 
					"(6179, \"Nova Milano (Farroupilha)\", 23),\r\n" + 
					"(6180, \"Nova Minda (Japonvar)\", 11),\r\n" + 
					"(6181, \"Nova Mocajuba (Bragan�a)\", 14),\r\n" + 
					"(6182, \"Nova M�dica\", 11),\r\n" + 
					"(6183, \"Nova Monte Verde\", 13),\r\n" + 
					"(6184, \"Nova Mutum\", 13),\r\n" + 
					"(6185, \"Nova Nazar�\", 13),\r\n" + 
					"(6186, \"Nova Odessa\", 26),\r\n" + 
					"(6187, \"Nova Ol�mpia\", 13),\r\n" + 
					"(6188, \"Nova Ol�mpia\", 18),\r\n" + 
					"(6189, \"Nova Olinda\", 15),\r\n" + 
					"(6190, \"Nova Olinda\", 27),\r\n" + 
					"(6191, \"Nova Olinda\", 6),\r\n" + 
					"(6192, \"Nova Olinda do Maranh�o\", 10),\r\n" + 
					"(6193, \"Nova Olinda do Norte\", 3),\r\n" + 
					"(6194, \"Nova P�dua\", 23),\r\n" + 
					"(6195, \"Nova Palma\", 23),\r\n" + 
					"(6196, \"Nova Palmeira\", 15),\r\n" + 
					"(6197, \"Nova P�tria (Presidente Bernardes)\", 26),\r\n" + 
					"(6198, \"Nova Petr�polis\", 23),\r\n" + 
					"(6199, \"Nova Petr�polis (Joa�aba)\", 24),\r\n" + 
					"(6200, \"Nova Ponte\", 11),\r\n" + 
					"(6201, \"Nova Porteirinha\", 11),\r\n" + 
					"(6202, \"Nova Prata\", 23),\r\n" + 
					"(6203, \"Nova Prata do Igua�u\", 18),\r\n" + 
					"(6204, \"Nova Primavera (Canabrava do Norte)\", 13),\r\n" + 
					"(6205, \"Nova Ramada\", 23),\r\n" + 
					"(6206, \"Nova Reden��o\", 5),\r\n" + 
					"(6207, \"Nova Resende\", 11),\r\n" + 
					"(6208, \"Nova Riqueza (Pinhal de S�o Bento)\", 18),\r\n" + 
					"(6209, \"Nova Roma\", 9),\r\n" + 
					"(6210, \"Nova Roma do Sul\", 23),\r\n" + 
					"(6211, \"Nova Rosal�ndia\", 27),\r\n" + 
					"(6212, \"Nova Russas\", 6),\r\n" + 
					"(6213, \"Nova Santa B�rbara\", 18),\r\n" + 
					"(6214, \"Nova Santa Cruz (Mauriti)\", 6),\r\n" + 
					"(6215, \"Nova Santa Helena\", 13),\r\n" + 
					"(6216, \"Nova Santa Helena (Ipor�)\", 18),\r\n" + 
					"(6217, \"Nova Santa Luzia (Cris�lita)\", 11),\r\n" + 
					"(6218, \"Nova Santa Rita\", 17),\r\n" + 
					"(6219, \"Nova Santa Rita\", 23),\r\n" + 
					"(6220, \"Nova Santa Rosa\", 18),\r\n" + 
					"(6221, \"Nova Sardenha (Farroupilha)\", 23),\r\n" + 
					"(6222, \"Nova Serrana\", 11),\r\n" + 
					"(6223, \"Nova Soure\", 5),\r\n" + 
					"(6224, \"Nova Tebas\", 18),\r\n" + 
					"(6225, \"Nova Teut�nia (Seara)\", 24),\r\n" + 
					"(6226, \"Nova Timboteua\", 14),\r\n" + 
					"(6227, \"Nova Tirol (Piraquara)\", 18),\r\n" + 
					"(6228, \"Nova Tramanda� (Tramanda�)\", 23),\r\n" + 
					"(6229, \"Nova Trento\", 24),\r\n" + 
					"(6230, \"Nova Ubirat�\", 13),\r\n" + 
					"(6231, \"Nova Uni�o\", 11),\r\n" + 
					"(6232, \"Nova Uni�o\", 21),\r\n" + 
					"(6233, \"Nova Ven�cia\", 8),\r\n" + 
					"(6234, \"Nova Veneza\", 9),\r\n" + 
					"(6235, \"Nova Veneza\", 24),\r\n" + 
					"(6236, \"Nova Veneza (Ubajara)\", 6),\r\n" + 
					"(6237, \"Nova Vi�osa\", 5),\r\n" + 
					"(6238, \"Nova Vida (Ariquemes)\", 21),\r\n" + 
					"(6239, \"Nova Vida (Ibaretama)\", 6),\r\n" + 
					"(6240, \"Nova Videira (Toledo)\", 18),\r\n" + 
					"(6241, \"Nova Xavantina\", 13),\r\n" + 
					"(6242, \"Novilhona (Novo Cruzeiro)\", 11),\r\n" + 
					"(6243, \"Novo Acordo\", 27),\r\n" + 
					"(6244, \"Novo Acre (Iramaia)\", 5),\r\n" + 
					"(6245, \"Novo Air�o\", 3),\r\n" + 
					"(6246, \"Novo Alegre\", 27),\r\n" + 
					"(6247, \"Novo Aripuan�\", 3),\r\n" + 
					"(6248, \"Novo Assis (Parambu)\", 6),\r\n" + 
					"(6249, \"Novo Barreiro\", 23),\r\n" + 
					"(6250, \"Novo Brasil\", 9),\r\n" + 
					"(6251, \"Novo Brasil (Governador Lindenberg)\", 8),\r\n" + 
					"(6252, \"Novo Cabrais\", 23),\r\n" + 
					"(6253, \"Novo C�u (Autazes)\", 3),\r\n" + 
					"(6254, \"Novo Cravinhos (Pomp�ia)\", 26),\r\n" + 
					"(6255, \"Novo Cruzeiro\", 11),\r\n" + 
					"(6256, \"Novo Diamantino (Diamantino)\", 13),\r\n" + 
					"(6257, \"Novo Eldorado (Tapurah)\", 13),\r\n" + 
					"(6258, \"Novo Gama\", 9),\r\n" + 
					"(6259, \"Novo Goi�s (Novo Brasil)\", 9),\r\n" + 
					"(6260, \"Novo Hamburgo\", 23),\r\n" + 
					"(6261, \"Novo Horizonte\", 26),\r\n" + 
					"(6262, \"Novo Horizonte\", 24),\r\n" + 
					"(6263, \"Novo Horizonte\", 5),\r\n" + 
					"(6264, \"Novo Horizonte (Aragua�na)\", 27),\r\n" + 
					"(6265, \"Novo Horizonte (Atal�ia)\", 11),\r\n" + 
					"(6266, \"Novo Horizonte (Cascavel)\", 18),\r\n" + 
					"(6267, \"Novo Horizonte do Norte\", 13),\r\n" + 
					"(6268, \"Novo Horizonte do Oeste\", 21),\r\n" + 
					"(6269, \"Novo Horizonte do Sul\", 12),\r\n" + 
					"(6270, \"Novo Horizonte (Ecoporanga)\", 8),\r\n" + 
					"(6271, \"Novo Horizonte (Marechal C�ndido Rondon)\", 18),\r\n" + 
					"(6272, \"Novo Horizonte (Salto do Jacu�)\", 23),\r\n" + 
					"(6273, \"Novo Itacolomi\", 18),\r\n" + 
					"(6274, \"Novo Jardim\", 27),\r\n" + 
					"(6275, \"Novo Jardim (Japira)\", 18),\r\n" + 
					"(6276, \"Novo Lino\", 2),\r\n" + 
					"(6277, \"Novo Machado\", 23),\r\n" + 
					"(6278, \"Novo Mato Grosso (Nova Ubirat�)\", 13),\r\n" + 
					"(6279, \"Novo Mundo\", 13),\r\n" + 
					"(6280, \"Novo Nilo (Uni�o)\", 17),\r\n" + 
					"(6281, \"Novo Oriente\", 6),\r\n" + 
					"(6282, \"Novo Oriente de Minas\", 11),\r\n" + 
					"(6283, \"Novo Oriente do Piau�\", 17),\r\n" + 
					"(6284, \"Novo Oriente (Ocara)\", 6),\r\n" + 
					"(6285, \"Novo Para�so (Caracara�)\", 22),\r\n" + 
					"(6286, \"Novo Para�so (Estrela)\", 23),\r\n" + 
					"(6287, \"Novo Paran� (Porto dos Ga�chos)\", 13),\r\n" + 
					"(6288, \"Novo Planalto\", 9),\r\n" + 
					"(6289, \"Novo Planalto (Reden��o)\", 14),\r\n" + 
					"(6290, \"Novo Planalto (Tiradentes do Sul)\", 23),\r\n" + 
					"(6291, \"Novo Progresso\", 14),\r\n" + 
					"(6292, \"Novo Remanso (Itacoatiara)\", 3),\r\n" + 
					"(6293, \"Novo Repartimento\", 14),\r\n" + 
					"(6294, \"Novorizonte\", 11),\r\n" + 
					"(6295, \"Novo Santo Ant�nio\", 13),\r\n" + 
					"(6296, \"Novo Santo Ant�nio\", 17),\r\n" + 
					"(6297, \"Novo S�o Joaquim\", 13),\r\n" + 
					"(6298, \"Novo Sarandi (Toledo)\", 18),\r\n" + 
					"(6299, \"Novo Sobradinho (Toledo)\", 18),\r\n" + 
					"(6300, \"Novo Tiradentes\", 23),\r\n" + 
					"(6301, \"Novo Tr�s Passos (Marechal C�ndido Rondon)\", 18),\r\n" + 
					"(6302, \"Novo Triunfo\", 5),\r\n" + 
					"(6303, \"Novo Xingu\", 23),\r\n" + 
					"(6304, \"Ns2 N�cleo de Servi�os (Petrolina)\", 16),\r\n" + 
					"(6305, \"N�cleo Colonial Pio XII (Guai�ba)\", 6),\r\n" + 
					"(6306, \"N�cleo N 2 (Sousa)\", 15),\r\n" + 
					"(6307, \"N�cleo N 3 (Sousa)\", 15),\r\n" + 
					"(6308, \"N�cleo Residencial Pilar (Jaguarari)\", 5),\r\n" + 
					"(6309, \"N�cleo Urbano Quil�metro 30 (Itaituba)\", 14),\r\n" + 
					"(6310, \"Nugua�u (Mirangaba)\", 5),\r\n" + 
					"(6311, \"Nuporanga\", 26),\r\n" + 
					"(6312, \"O�sis (Tupi Paulista)\", 26),\r\n" + 
					"(6313, \"�bidos\", 14),\r\n" + 
					"(6314, \"Ocara\", 6),\r\n" + 
					"(6315, \"Ocau�u\", 26),\r\n" + 
					"(6316, \"Ocidente (Mutum)\", 11),\r\n" + 
					"(6317, \"Odil�ndia (Santa Rita)\", 15),\r\n" + 
					"(6318, \"Oeiras\", 17),\r\n" + 
					"(6319, \"Oeiras do Par�\", 14),\r\n" + 
					"(6320, \"Oiapoque\", 4),\r\n" + 
					"(6321, \"Oiticica (Crate�s)\", 6),\r\n" + 
					"(6322, \"Oiticica (Ibaretama)\", 6),\r\n" + 
					"(6323, \"Oiticica (Parambu)\", 6),\r\n" + 
					"(6324, \"Olaria\", 11),\r\n" + 
					"(6325, \"Olaria (Cascavel)\", 18),\r\n" + 
					"(6326, \"Olaria do Angico (Itarum�)\", 9),\r\n" + 
					"(6327, \"Olaria (Tel�maco Borba)\", 18),\r\n" + 
					"(6328, \"Oleg�rio Maciel (Piranguinho)\", 11),\r\n" + 
					"(6329, \"�leo\", 26),\r\n" + 
					"(6330, \"Olho Agudo (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(6331, \"Olho D'�gua\", 15),\r\n" + 
					"(6332, \"Olho d'�gua (Castro)\", 18),\r\n" + 
					"(6333, \"Olho-D'�gua da Bica (Tabuleiro do Norte)\", 6),\r\n" + 
					"(6334, \"Olho D'�gua das Cunh�s\", 10),\r\n" + 
					"(6335, \"Olho D'�gua das Flores\", 2),\r\n" + 
					"(6336, \"Olho D'�gua de Dentro (Canhotinho)\", 16),\r\n" + 
					"(6337, \"Olho D'�gua do Bezerril (Boa Viagem)\", 6),\r\n" + 
					"(6338, \"Olho-D'�gua do Borges\", 20),\r\n" + 
					"(6339, \"Olho D'�gua do Casado\", 2),\r\n" + 
					"(6340, \"Olho D'�gua do Piau�\", 17),\r\n" + 
					"(6341, \"Olho D'�gua dos Dandanhas (Arapiraca)\", 2),\r\n" + 
					"(6342, \"Olho D'�gua Grande\", 2),\r\n" + 
					"(6343, \"Olho D'�gua (Mauriti)\", 6),\r\n" + 
					"(6345, \"Olhos D'�gua\", 11),\r\n" + 
					"(6346, \"Olhos D'�gua (Alex�nia)\", 9),\r\n" + 
					"(6347, \"Olhos D'�gua (Catal�o)\", 9),\r\n" + 
					"(6348, \"Olhos D'�gua do Oeste (Jo�o Pinheiro)\", 11),\r\n" + 
					"(6349, \"Olhos D'�gua do Seco (Ibitiara)\", 5),\r\n" + 
					"(6350, \"Olhos D'�gua do Serafim (Novo Horizonte)\", 5),\r\n" + 
					"(6351, \"Ol�mpia\", 26),\r\n" + 
					"(6352, \"Ol�mpio Campos (S�o Jo�o da Ponte)\", 11),\r\n" + 
					"(6353, \"Ol�mpio Noronha\", 11),\r\n" + 
					"(6354, \"Olinda\", 16),\r\n" + 
					"(6355, \"Olinda Nova do Maranh�o\", 10),\r\n" + 
					"(6356, \"Olindina\", 5),\r\n" + 
					"(6357, \"Olivedos\", 15),\r\n" + 
					"(6358, \"Oliveira\", 11),\r\n" + 
					"(6359, \"Oliveira Barros (Miracatu)\", 26),\r\n" + 
					"(6360, \"Oliveira de F�tima\", 27),\r\n" + 
					"(6361, \"Oliveira dos Brejinhos\", 5),\r\n" + 
					"(6362, \"Oliveira Fortes\", 11),\r\n" + 
					"(6363, \"Oliveiras (Tamboril)\", 6),\r\n" + 
					"(6364, \"Oliven�a\", 2),\r\n" + 
					"(6365, \"On�a de Pitangui\", 11),\r\n" + 
					"(6366, \"Onda Branca (Nova Granada)\", 26),\r\n" + 
					"(6367, \"Onda Verde\", 26),\r\n" + 
					"(6368, \"Onha (Muniz Ferreira)\", 5),\r\n" + 
					"(6369, \"Oralina (Salto do Jacu�)\", 23),\r\n" + 
					"(6370, \"Orat�rio (Casinhas)\", 16),\r\n" + 
					"(6371, \"Orat�rios\", 11),\r\n" + 
					"(6372, \"Oriente\", 26),\r\n" + 
					"(6373, \"Oriente (Aparecida do Taboado)\", 12),\r\n" + 
					"(6374, \"Oriente Novo (Jequi�)\", 5),\r\n" + 
					"(6375, \"Orindi�va\", 26),\r\n" + 
					"(6376, \"Ori (Serrita)\", 16),\r\n" + 
					"(6377, \"Oriximin�\", 14),\r\n" + 
					"(6378, \"Oriz�nia\", 11),\r\n" + 
					"(6379, \"Orizona\", 9),\r\n" + 
					"(6380, \"Orl�ndia\", 26),\r\n" + 
					"(6381, \"Orleans\", 24),\r\n" + 
					"(6382, \"Orob�\", 16),\r\n" + 
					"(6383, \"Oroc�\", 16),\r\n" + 
					"(6384, \"Or�s\", 6),\r\n" + 
					"(6385, \"Ortigueira\", 18),\r\n" + 
					"(6386, \"Osasco\", 26),\r\n" + 
					"(6387, \"Oscar Bressane\", 26),\r\n" + 
					"(6388, \"Os�rio\", 23),\r\n" + 
					"(6389, \"Os�rio da Fonseca (Mau�s)\", 3),\r\n" + 
					"(6390, \"Osvaldil�ndia (Reden��o)\", 14),\r\n" + 
					"(6391, \"Osvaldo Cruz\", 26),\r\n" + 
					"(6392, \"Osvaldo Cruz (Frederico Westphalen)\", 23),\r\n" + 
					"(6393, \"Osvaldo Kroeff (Cambar� do Sul)\", 23),\r\n" + 
					"(6394, \"Otac�lio Costa\", 24),\r\n" + 
					"(6395, \"Ot�vio Rocha (Flores da Cunha)\", 23),\r\n" + 
					"(6396, \"Our�nia (Natividade)\", 19),\r\n" + 
					"(6397, \"Our�m\", 14),\r\n" + 
					"(6398, \"Ouricana (Canavieiras)\", 5),\r\n" + 
					"(6399, \"Ouri�angas\", 5),\r\n" + 
					"(6400, \"Ouricuri\", 16),\r\n" + 
					"(6401, \"Ouricuri do Ouro (Brotas de Maca�bas)\", 5),\r\n" + 
					"(6402, \"Ouril�ndia (Barbosa Ferraz)\", 18),\r\n" + 
					"(6403, \"Ouril�ndia do Norte\", 14),\r\n" + 
					"(6404, \"Ourinhos\", 26),\r\n" + 
					"(6405, \"Ourizona\", 18),\r\n" + 
					"(6406, \"Ouro\", 24),\r\n" + 
					"(6407, \"Ouroana (Rio Verde)\", 9),\r\n" + 
					"(6408, \"Ouro Branco\", 2),\r\n" + 
					"(6409, \"Ouro Branco\", 20),\r\n" + 
					"(6410, \"Ouro Branco\", 11),\r\n" + 
					"(6411, \"Ouroeste\", 26),\r\n" + 
					"(6412, \"Ouro Fino\", 11),\r\n" + 
					"(6413, \"Ourol�ndia\", 5),\r\n" + 
					"(6414, \"Ouro Preto\", 11),\r\n" + 
					"(6415, \"Ouro Preto do Oeste\", 21),\r\n" + 
					"(6416, \"Ouro Preto (Toledo)\", 18),\r\n" + 
					"(6417, \"Ouro Velho\", 15),\r\n" + 
					"(6418, \"Ouro Verde\", 24),\r\n" + 
					"(6419, \"Ouro Verde\", 26),\r\n" + 
					"(6420, \"Ouro Verde de Goi�s\", 9),\r\n" + 
					"(6421, \"Ouro Verde de Minas\", 11),\r\n" + 
					"(6422, \"Ouro Verde do Oeste\", 18),\r\n" + 
					"(6423, \"Ouro Verde do Piquiri (Corb�lia)\", 18),\r\n" + 
					"(6424, \"Ouro Verde (Guaraciaba)\", 24),\r\n" + 
					"(6425, \"Ouro Verde (Seng�s)\", 18),\r\n" + 
					"(6426, \"Outeiro Redondo (S�o F�lix)\", 5),\r\n" + 
					"(6427, \"Ouvidor\", 9),\r\n" + 
					"(6428, \"Pacaembu\", 26),\r\n" + 
					"(6429, \"Pacaj�\", 14),\r\n" + 
					"(6430, \"Pacajus\", 6),\r\n" + 
					"(6431, \"Pacaraima\", 22),\r\n" + 
					"(6432, \"Pacatuba\", 25),\r\n" + 
					"(6433, \"Pacatuba\", 6),\r\n" + 
					"(6434, \"Pacheca (Camaqu�)\", 23),\r\n" + 
					"(6435, \"Paci�ncia (Porteirinha)\", 11),\r\n" + 
					"(6436, \"Pa�o do Lumiar\", 10),\r\n" + 
					"(6437, \"Pacoti\", 6),\r\n" + 
					"(6438, \"Pacotuba (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(6439, \"Pacoval (Prainha)\", 14),\r\n" + 
					"(6440, \"Pacuj�\", 6),\r\n" + 
					"(6441, \"Padilha (Taquara)\", 23),\r\n" + 
					"(6442, \"Padre Bernardo\", 9),\r\n" + 
					"(6443, \"Padre Brito (Barbacena)\", 11),\r\n" + 
					"(6444, \"Padre Carvalho\", 11),\r\n" + 
					"(6445, \"Padre C�cero (Juazeiro do Norte)\", 6),\r\n" + 
					"(6446, \"Padre Felisberto (Amparo da Serra)\", 11),\r\n" + 
					"(6447, \"Padre Fialho (Matip�)\", 11),\r\n" + 
					"(6448, \"Padre Gonzales (Tr�s Passos)\", 23),\r\n" + 
					"(6449, \"Padre Jo�o Afonso (Itamarandiba)\", 11),\r\n" + 
					"(6450, \"Padre J�lio Maria (Alto Jequitib�)\", 11),\r\n" + 
					"(6451, \"Padre Linhares (Massap�)\", 6),\r\n" + 
					"(6452, \"Padre Marcos\", 17),\r\n" + 
					"(6453, \"Padre Para�so\", 11),\r\n" + 
					"(6454, \"Padre Pinto (Rio Piracicaba)\", 11),\r\n" + 
					"(6455, \"Padre Ponciano (Coronel Domingos Soares)\", 18),\r\n" + 
					"(6456, \"Padre Viegas (Mariana)\", 11),\r\n" + 
					"(6457, \"Padre Vieira (Vi�osa do Cear�)\", 6),\r\n" + 
					"(6458, \"Padronal (Comodoro)\", 13),\r\n" + 
					"(6459, \"Paes Landim\", 17),\r\n" + 
					"(6460, \"Paiagu�s (Corumb�)\", 12),\r\n" + 
					"(6461, \"Paial\", 24),\r\n" + 
					"(6462, \"Pai Andr� (V�rzea Grande)\", 13),\r\n" + 
					"(6463, \"Pai�andu\", 18),\r\n" + 
					"(6464, \"Pai Jo�o (Aratuba)\", 6),\r\n" + 
					"(6465, \"Paim Filho\", 23),\r\n" + 
					"(6466, \"Paineiras\", 11),\r\n" + 
					"(6467, \"Paineiras (Itapemirim)\", 8),\r\n" + 
					"(6468, \"Painel\", 24),\r\n" + 
					"(6469, \"Pains\", 11),\r\n" + 
					"(6470, \"Pains (Santa Maria)\", 23),\r\n" + 
					"(6471, \"Paiol de Baixo (Campina Grande do Sul)\", 18),\r\n" + 
					"(6472, \"Paiolinho (Po�o Fundo)\", 11),\r\n" + 
					"(6473, \"Paiol (Jacaraci)\", 5),\r\n" + 
					"(6474, \"Paiol Queimado (Castro)\", 18),\r\n" + 
					"(6475, \"Pai Pedro\", 11),\r\n" + 
					"(6476, \"Paiquer� (Londrina)\", 18),\r\n" + 
					"(6477, \"Paiva\", 11),\r\n" + 
					"(6478, \"Paje� (Araripe)\", 6),\r\n" + 
					"(6479, \"Paje� do Piau�\", 17),\r\n" + 
					"(6480, \"Paje� do Vento (Caetit�)\", 5),\r\n" + 
					"(6481, \"Paje� (Serra Talhada)\", 16),\r\n" + 
					"(6482, \"Palame (Esplanada)\", 5),\r\n" + 
					"(6483, \"Palanque (Ven�ncio Aires)\", 23),\r\n" + 
					"(6484, \"Palestina\", 2),\r\n" + 
					"(6485, \"Palestina\", 26),\r\n" + 
					"(6486, \"Palestina de Goi�s\", 9),\r\n" + 
					"(6487, \"Palestina do Norte (Meruoca)\", 6),\r\n" + 
					"(6488, \"Palestina do Par�\", 14),\r\n" + 
					"(6489, \"Palestina (Mauriti)\", 6),\r\n" + 
					"(6490, \"Palestina (Novo Oriente)\", 6),\r\n" + 
					"(6491, \"Palestina (Or�s)\", 6),\r\n" + 
					"(6492, \"Palhano\", 6),\r\n" + 
					"(6493, \"Palho�a\", 24),\r\n" + 
					"(6494, \"Palma\", 11),\r\n" + 
					"(6495, \"Palm�cia\", 6),\r\n" + 
					"(6496, \"Palmares\", 16),\r\n" + 
					"(6497, \"Palmares do Sul\", 23),\r\n" + 
					"(6498, \"Palmares (Itapetinga)\", 5),\r\n" + 
					"(6499, \"Palmares Paulista\", 26),\r\n" + 
					"(6500, \"Palmares (Riach�o do Dantas)\", 25),\r\n" + 
					"(6501, \"Palmares (Tail�ndia)\", 14),\r\n" + 
					"(6502, \"Palmas\", 27),\r\n" + 
					"(6503, \"Palmas\", 18),\r\n" + 
					"(6504, \"Palma (Santa Maria)\", 23),\r\n" + 
					"(6505, \"Palmas (Arroio do Meio)\", 23),\r\n" + 
					"(6506, \"Palmas (Bag�)\", 23),\r\n" + 
					"(6507, \"Palmas de Monte Alto\", 5),\r\n" + 
					"(6508, \"Palma Sola\", 24),\r\n" + 
					"(6509, \"Palmat�ria (Caruaru)\", 16),\r\n" + 
					"(6510, \"Palmat�ria (Itapi�na)\", 6),\r\n" + 
					"(6511, \"Palmeira\", 18),\r\n" + 
					"(6512, \"Palmeira\", 24),\r\n" + 
					"(6513, \"Palmeira das Miss�es\", 23),\r\n" + 
					"(6514, \"Palmeira de Fora (Palmeira dos �ndios)\", 2),\r\n" + 
					"(6515, \"Palmeira D'Oeste\", 26),\r\n" + 
					"(6516, \"Palmeira do Piau�\", 17),\r\n" + 
					"(6517, \"Palmeira dos �ndios\", 2),\r\n" + 
					"(6518, \"Palmeirais\", 17),\r\n" + 
					"(6519, \"Palmeira (Itagua�u)\", 8),\r\n" + 
					"(6520, \"Palmeiral (Botelhos)\", 11),\r\n" + 
					"(6521, \"Palmeir�ndia\", 10),\r\n" + 
					"(6522, \"Palmeirante\", 27),\r\n" + 
					"(6523, \"Palmeiras\", 5),\r\n" + 
					"(6524, \"Palmeiras de Goi�s\", 9),\r\n" + 
					"(6525, \"Palmeiras (Dois Irm�os do Buriti)\", 12),\r\n" + 
					"(6526, \"Palmeiras do Tocantins\", 27),\r\n" + 
					"(6527, \"Palmeiras (Nova Mamor�)\", 21),\r\n" + 
					"(6528, \"Palmeiras (Tabatinga)\", 3),\r\n" + 
					"(6529, \"Palmeirina\", 16),\r\n" + 
					"(6530, \"Palmeirinha (Castro)\", 18),\r\n" + 
					"(6531, \"Palmeirinha (Guarapuava)\", 18),\r\n" + 
					"(6532, \"Palmeirinha (Una�)\", 11),\r\n" + 
					"(6533, \"Palmeir�polis\", 27),\r\n" + 
					"(6534, \"Palmelo\", 9),\r\n" + 
					"(6535, \"Palmerino (Alto Rio Novo)\", 8),\r\n" + 
					"(6536, \"Palmin�polis\", 9),\r\n" + 
					"(6537, \"Palmira (S�o Jo�o do Triunfo)\", 18),\r\n" + 
					"(6538, \"Palmital\", 18),\r\n" + 
					"(6539, \"Palmital\", 26),\r\n" + 
					"(6540, \"Palmital (Castro)\", 18),\r\n" + 
					"(6541, \"Palmital de Minas (Cabeceira Grande)\", 11),\r\n" + 
					"(6542, \"Palmital de S�o Silvestre (Campo Largo)\", 18),\r\n" + 
					"(6543, \"Palmital dos Carvalhos (Senhora dos Rem�dios)\", 11),\r\n" + 
					"(6544, \"Palmitinho\", 23),\r\n" + 
					"(6545, \"Palmit�polis (Nova Aurora)\", 18),\r\n" + 
					"(6546, \"Palmitos\", 24),\r\n" + 
					"(6547, \"Palm�polis\", 11),\r\n" + 
					"(6548, \"Palotina\", 18),\r\n" + 
					"(6549, \"Pampeiro (Santana do Livramento)\", 23),\r\n" + 
					"(6550, \"Panacu� (Marco)\", 6),\r\n" + 
					"(6551, \"Panam�\", 9),\r\n" + 
					"(6552, \"Panambi\", 23),\r\n" + 
					"(6553, \"Panambi (Dourados)\", 12),\r\n" + 
					"(6554, \"Pana (Nova Alvorada do Sul)\", 12),\r\n" + 
					"(6555, \"Pancas\", 8),\r\n" + 
					"(6556, \"Panelas\", 16),\r\n" + 
					"(6557, \"Panema (Santa Mariana)\", 18),\r\n" + 
					"(6558, \"Pangar� (Quitandinha)\", 18),\r\n" + 
					"(6559, \"Panorama\", 26),\r\n" + 
					"(6560, \"P�ntano (Estiva)\", 11),\r\n" + 
					"(6561, \"Pantano Grande\", 23),\r\n" + 
					"(6562, \"P�o de A��car\", 2),\r\n" + 
					"(6563, \"P�o de A��car do Po��o (Po��o)\", 16),\r\n" + 
					"(6564, \"P�o de A��car (Taquaritinga do Norte)\", 16),\r\n" + 
					"(6565, \"Papagaio (Barra do Corda)\", 10),\r\n" + 
					"(6566, \"Papagaio (Pesqueira)\", 16),\r\n" + 
					"(6567, \"Papagaios\", 11),\r\n" + 
					"(6568, \"Papagaios Novos (Palmeira)\", 18),\r\n" + 
					"(6569, \"Papanduva\", 24),\r\n" + 
					"(6570, \"Papara (Maranguape)\", 6),\r\n" + 
					"(6571, \"Papucaia (Cachoeiras de Macacu)\", 19),\r\n" + 
					"(6572, \"Paquequer Pequeno (Teres�polis)\", 19),\r\n" + 
					"(6573, \"Paquet�\", 17),\r\n" + 
					"(6574, \"Paquevira (Canhotinho)\", 16),\r\n" + 
					"(6575, \"Paracambi\", 19),\r\n" + 
					"(6576, \"Paracatu\", 11),\r\n" + 
					"(6577, \"Paracu� (Uruoca)\", 6),\r\n" + 
					"(6578, \"Paracuru\", 6),\r\n" + 
					"(6579, \"Par� de Minas\", 11),\r\n" + 
					"(6580, \"Parafuso (Cama�ari)\", 5),\r\n" + 
					"(6581, \"Paragominas\", 14),\r\n" + 
					"(6582, \"Paragua�u\", 11),\r\n" + 
					"(6583, \"Paragua�u Paulista\", 26),\r\n" + 
					"(6584, \"Paraguai (Cajuri)\", 11),\r\n" + 
					"(6585, \"Para�\", 23),\r\n" + 
					"(6586, \"Para�ba do Sul\", 19),\r\n" + 
					"(6587, \"Paraibano\", 10),\r\n" + 
					"(6588, \"Paraibuna\", 26),\r\n" + 
					"(6589, \"Paraipaba\", 6),\r\n" + 
					"(6590, \"Para�so\", 26),\r\n" + 
					"(6591, \"Para�so\", 24),\r\n" + 
					"(6592, \"Paraiso (Catunda)\", 6),\r\n" + 
					"(6593, \"Para�so das �guas\", 12),\r\n" + 
					"(6594, \"Para�so do Leste (Poxor�u)\", 13),\r\n" + 
					"(6595, \"Para�so do Norte\", 18),\r\n" + 
					"(6596, \"Para�so do Sul\", 23),\r\n" + 
					"(6597, \"Para�so do Tobias (Miracema)\", 19),\r\n" + 
					"(6598, \"Para�so do Tocantins\", 27),\r\n" + 
					"(6599, \"Para�so Garcia (Santa Rita do Ibitipoca)\", 11),\r\n" + 
					"(6600, \"Paraisol�ndia (Charqueada)\", 26),\r\n" + 
					"(6601, \"Parais�polis\", 11),\r\n" + 
					"(6602, \"Paraju (Domingos Martins)\", 8),\r\n" + 
					"(6603, \"Parajuru (Beberibe)\", 6),\r\n" + 
					"(6604, \"Parambu\", 6),\r\n" + 
					"(6605, \"Paramirim\", 5),\r\n" + 
					"(6606, \"Paramoti\", 6),\r\n" + 
					"(6607, \"Paran�\", 20),\r\n" + 
					"(6608, \"Paran�\", 27),\r\n" + 
					"(6609, \"Paranabi (Ilhabela)\", 26),\r\n" + 
					"(6610, \"Paranacity\", 18),\r\n" + 
					"(6611, \"Paran� d'Oeste (Moreira Sales)\", 18),\r\n" + 
					"(6612, \"Paranagi (Sertaneja)\", 18),\r\n" + 
					"(6613, \"Paranagu�\", 18),\r\n" + 
					"(6614, \"Parana�ba\", 12),\r\n" + 
					"(6615, \"Paranaiguara\", 9),\r\n" + 
					"(6616, \"Paranait�\", 13),\r\n" + 
					"(6617, \"Paranapanema\", 26),\r\n" + 
					"(6618, \"Paranapoema\", 18),\r\n" + 
					"(6619, \"Paranapu�\", 26),\r\n" + 
					"(6620, \"Paranatama\", 16),\r\n" + 
					"(6621, \"Paranatinga\", 13),\r\n" + 
					"(6622, \"Paranava�\", 18),\r\n" + 
					"(6623, \"Paranhos\", 12),\r\n" + 
					"(6624, \"Paraopeba\", 11),\r\n" + 
					"(6625, \"Paraoquena (Santo Ant�nio de P�dua)\", 19),\r\n" + 
					"(6626, \"Parape�na (Valen�a)\", 19),\r\n" + 
					"(6627, \"Parapu�\", 26),\r\n" + 
					"(6628, \"Parapu� (Santana do Acara�)\", 6),\r\n" + 
					"(6629, \"Parari\", 15),\r\n" + 
					"(6630, \"Par� (Santa Cruz do Capibaribe)\", 16),\r\n" + 
					"(6631, \"Parateca (Malhada)\", 5),\r\n" + 
					"(6632, \"Parati\", 19),\r\n" + 
					"(6633, \"Parati Mirim (Parati)\", 19),\r\n" + 
					"(6634, \"Paratinga\", 5),\r\n" + 
					"(6635, \"Paratins (Marab�)\", 14),\r\n" + 
					"(6636, \"Para�\", 20),\r\n" + 
					"(6637, \"Parauapebas\", 14),\r\n" + 
					"(6638, \"Para�na\", 9),\r\n" + 
					"(6639, \"Parazinho\", 20),\r\n" + 
					"(6640, \"Parazinho (Granja)\", 6),\r\n" + 
					"(6641, \"Pardinho\", 26),\r\n" + 
					"(6642, \"Pareci Novo\", 23),\r\n" + 
					"(6643, \"Parecis\", 21),\r\n" + 
					"(6644, \"Pared�o de Minas (Buritizeiro)\", 11),\r\n" + 
					"(6645, \"Pared�o (Ferreira Gomes)\", 4),\r\n" + 
					"(6646, \"Parelhas\", 20),\r\n" + 
					"(6647, \"Paricatuba (Iranduba)\", 3),\r\n" + 
					"(6648, \"Pariconha\", 2),\r\n" + 
					"(6649, \"Parintins\", 3),\r\n" + 
					"(6650, \"Paripiranga\", 5),\r\n" + 
					"(6651, \"Paripueira\", 2),\r\n" + 
					"(6652, \"Paripueira (Beberibe)\", 6),\r\n" + 
					"(6653, \"Pariquera-A�u\", 26),\r\n" + 
					"(6654, \"Parisi\", 26),\r\n" + 
					"(6655, \"Parnagu�\", 17),\r\n" + 
					"(6656, \"Parna�ba\", 17),\r\n" + 
					"(6657, \"Parnamirim\", 20),\r\n" + 
					"(6658, \"Parnamirim\", 16),\r\n" + 
					"(6659, \"Parnarama\", 10),\r\n" + 
					"(6660, \"Parob�\", 23),\r\n" + 
					"(6661, \"Parque �gua Limpa (Nova Ubirat�)\", 13),\r\n" + 
					"(6662, \"Parque Durval de Barros (Ibirit�)\", 11),\r\n" + 
					"(6663, \"Paruru (Ibi�na)\", 26),\r\n" + 
					"(6664, \"Passab�m\", 11),\r\n" + 
					"(6665, \"Passa Dez (Bom Jesus do Galho)\", 11),\r\n" + 
					"(6666, \"Passa e Fica\", 20),\r\n" + 
					"(6667, \"Passagem\", 15),\r\n" + 
					"(6668, \"Passagem\", 20),\r\n" + 
					"(6669, \"Passagem (Chaval)\", 6),\r\n" + 
					"(6670, \"Passagem da Concei��o (V�rzea Grande)\", 13),\r\n" + 
					"(6671, \"Passagem de Mariana (Mariana)\", 11),\r\n" + 
					"(6672, \"Passagem dos Teixeiras (Candeias)\", 5),\r\n" + 
					"(6673, \"Passagem do T� (Jata�ba)\", 16),\r\n" + 
					"(6674, \"Passagem Franca\", 10),\r\n" + 
					"(6675, \"Passagem Franca do Piau�\", 17),\r\n" + 
					"(6676, \"Passagem Funda (Aracoiaba)\", 6),\r\n" + 
					"(6677, \"Passagem (Quixeramobim)\", 6),\r\n" + 
					"(6678, \"Passa Quatro\", 11),\r\n" + 
					"(6679, \"Passa Sete\", 23),\r\n" + 
					"(6680, \"Passa Tempo\", 11),\r\n" + 
					"(6681, \"Passa Tr�s (Rio Claro)\", 19),\r\n" + 
					"(6682, \"Passa Una (Arauc�ria)\", 18),\r\n" + 
					"(6683, \"Passa Vinte\", 11),\r\n" + 
					"(6684, \"Pass� (Candeias)\", 5),\r\n" + 
					"(6685, \"Passinhos (Os�rio)\", 23),\r\n" + 
					"(6686, \"Passira\", 16),\r\n" + 
					"(6687, \"Passo Amarelo (Fazenda Rio Grande)\", 18),\r\n" + 
					"(6688, \"Passo Burmann (Catu�pe)\", 23),\r\n" + 
					"(6689, \"Passo da Areia (Viam�o)\", 23),\r\n" + 
					"(6690, \"Passo da Caveira (Gravata�)\", 23),\r\n" + 
					"(6691, \"Passo da Ilha (Pato Branco)\", 18),\r\n" + 
					"(6692, \"Passo das Pedras (Cap�o do Le�o)\", 23),\r\n" + 
					"(6693, \"Passo de Camaragibe\", 2),\r\n" + 
					"(6694, \"Passo de Torres\", 24),\r\n" + 
					"(6695, \"Passo do Ad�o (Rio Pardo)\", 23),\r\n" + 
					"(6696, \"Passo do Carro (Monte Alegre dos Campos)\", 23),\r\n" + 
					"(6697, \"Passo do Goulart (Ma�ambar�)\", 23),\r\n" + 
					"(6698, \"Passo do Mendon�a (Cristal)\", 23),\r\n" + 
					"(6699, \"Passo do Sab�o (Viam�o)\", 23),\r\n" + 
					"(6700, \"Passo do Sobrado\", 23),\r\n" + 
					"(6701, \"Passo dos Pupos (Ponta Grossa)\", 18),\r\n" + 
					"(6702, \"Passo do Verde (Santa Maria)\", 23),\r\n" + 
					"(6703, \"Passo Fundo\", 23),\r\n" + 
					"(6704, \"Passo Fundo (Campo Largo)\", 18),\r\n" + 
					"(6705, \"Passo Manso (Tai�)\", 24),\r\n" + 
					"(6706, \"Passo Novo (Alegrete)\", 23),\r\n" + 
					"(6707, \"Passo Raso (Triunfo)\", 23),\r\n" + 
					"(6708, \"Passo Real de Candiota (Candiota)\", 23),\r\n" + 
					"(6709, \"Passo Real (Salto do Jacu�)\", 23),\r\n" + 
					"(6710, \"Passos\", 11),\r\n" + 
					"(6711, \"Passos Maia\", 24),\r\n" + 
					"(6712, \"Pasta (Solon�pole)\", 6),\r\n" + 
					"(6713, \"Pastos Bons\", 10),\r\n" + 
					"(6714, \"Patacas (Aquiraz)\", 6),\r\n" + 
					"(6715, \"Pata�ba (�gua Fria)\", 5),\r\n" + 
					"(6716, \"Patamut� (Cura��)\", 5),\r\n" + 
					"(6717, \"Patis\", 11),\r\n" + 
					"(6718, \"Pato Bragado\", 18),\r\n" + 
					"(6719, \"Pato Branco\", 18),\r\n" + 
					"(6720, \"Patos\", 15),\r\n" + 
					"(6721, \"Patos (Caruaru)\", 16),\r\n" + 
					"(6722, \"Patos de Minas\", 11),\r\n" + 
					"(6723, \"Patos do Piau�\", 17),\r\n" + 
					"(6724, \"Patos dos Liberatos (Chorozinho)\", 6),\r\n" + 
					"(6725, \"Patos Velhos (Prudent�polis)\", 18),\r\n" + 
					"(6726, \"Patriarca (Sobral)\", 6),\r\n" + 
					"(6727, \"Patrim�nio (Prata)\", 11),\r\n" + 
					"(6728, \"Patroc�nio\", 11),\r\n" + 
					"(6729, \"Patroc�nio (Caratinga)\", 11),\r\n" + 
					"(6730, \"Patroc�nio do Muria�\", 11),\r\n" + 
					"(6731, \"Patroc�nio Paulista\", 26),\r\n" + 
					"(6732, \"Patu\", 20),\r\n" + 
					"(6733, \"Paty do Alferes\", 19),\r\n" + 
					"(6734, \"Pau Amarelo (Correntes)\", 16),\r\n" + 
					"(6735, \"Pau a Pique (Casa Nova)\", 5),\r\n" + 
					"(6736, \"Pau Brasil\", 5),\r\n" + 
					"(6737, \"Paudalho\", 16),\r\n" + 
					"(6738, \"Pau d'Alho do Sul (Assa�)\", 18),\r\n" + 
					"(6739, \"Pau D'Arco\", 14),\r\n" + 
					"(6740, \"Pau D'Arco\", 27),\r\n" + 
					"(6741, \"Pau D'Arco (Arapiraca)\", 2),\r\n" + 
					"(6742, \"Pau D'Arco do Piau�\", 17),\r\n" + 
					"(6743, \"Pau dos Ferros\", 20),\r\n" + 
					"(6744, \"Pau Ferro (Arapiraca)\", 2),\r\n" + 
					"(6745, \"Pau Ferro (Quipap�)\", 16),\r\n" + 
					"(6746, \"Pau Ferro (Salgueiro)\", 16),\r\n" + 
					"(6747, \"Pauini\", 3),\r\n" + 
					"(6748, \"Paula C�ndido\", 11),\r\n" + 
					"(6749, \"Paula Freitas\", 18),\r\n" + 
					"(6750, \"Paula Lima (Juiz de Fora)\", 11),\r\n" + 
					"(6751, \"Paula Pereira (Canoinhas)\", 24),\r\n" + 
					"(6752, \"Paulic�ia\", 26),\r\n" + 
					"(6753, \"Paul�nia\", 26),\r\n" + 
					"(6754, \"Paulino Neves\", 10),\r\n" + 
					"(6755, \"Paulista\", 15),\r\n" + 
					"(6756, \"Paulista\", 16),\r\n" + 
					"(6757, \"Paulista (Barra de S�o Francisco)\", 8),\r\n" + 
					"(6758, \"Paulistana\", 17),\r\n" + 
					"(6759, \"Paulist�nia\", 26),\r\n" + 
					"(6760, \"Paulist�nia (Alto Piquiri)\", 18),\r\n" + 
					"(6761, \"Paulistas\", 11),\r\n" + 
					"(6762, \"Paulo Afonso\", 5),\r\n" + 
					"(6763, \"Paulo Bento\", 23),\r\n" + 
					"(6764, \"Paulo de Faria\", 26),\r\n" + 
					"(6765, \"Paulo Frontin\", 18),\r\n" + 
					"(6766, \"Paulo Jacinto\", 2),\r\n" + 
					"(6767, \"Paulo Lopes\", 24),\r\n" + 
					"(6768, \"Paul�polis (Pomp�ia)\", 26),\r\n" + 
					"(6769, \"Paulo Ramos\", 10),\r\n" + 
					"(6770, \"Pau-Santo (Caruaru)\", 16),\r\n" + 
					"(6771, \"Paus Brancos (Quixeramobim)\", 6),\r\n" + 
					"(6772, \"Pav�o\", 11),\r\n" + 
					"(6773, \"Pav�o (Cap�o do Le�o)\", 23),\r\n" + 
					"(6774, \"Paverama\", 23),\r\n" + 
					"(6775, \"Pavuna (Pacatuba)\", 6),\r\n" + 
					"(6776, \"Pavussu\", 17),\r\n" + 
					"(6777, \"Peabiru\", 18),\r\n" + 
					"(6778, \"Pe�anha\", 11),\r\n" + 
					"(6779, \"Pec�m (S�o Gon�alo do Amarante)\", 6),\r\n" + 
					"(6780, \"P� da Serra (Aragua�na)\", 27),\r\n" + 
					"(6781, \"Pederneiras\", 26),\r\n" + 
					"(6782, \"P� de Serra\", 5),\r\n" + 
					"(6783, \"P�-de-Serra dos Mendes (Agrestina)\", 16),\r\n" + 
					"(6784, \"P� do Morro (Passa Quatro)\", 11),\r\n" + 
					"(6785, \"Pedra\", 16),\r\n" + 
					"(6786, \"Pedra Alta (Araci)\", 5),\r\n" + 
					"(6787, \"Pedra Azul\", 11),\r\n" + 
					"(6788, \"Pedra Bela\", 26),\r\n" + 
					"(6789, \"Pedra Bonita\", 11),\r\n" + 
					"(6790, \"Pedra Branca\", 15),\r\n" + 
					"(6791, \"Pedra Branca\", 6),\r\n" + 
					"(6792, \"Pedra Branca (Aracoiaba)\", 6),\r\n" + 
					"(6793, \"Pedra Branca (Catal�o)\", 9),\r\n" + 
					"(6794, \"Pedra Branca de Itarar� (Itarar�)\", 26),\r\n" + 
					"(6795, \"Pedra Branca do Amapar�\", 4),\r\n" + 
					"(6796, \"Pedra Branca do Araraquara (Guaratuba)\", 18),\r\n" + 
					"(6797, \"Pedra Corrida (A�ucena)\", 11),\r\n" + 
					"(6798, \"Pedra de Fogo (Sobral)\", 6),\r\n" + 
					"(6799, \"Pedra do Anta\", 11),\r\n" + 
					"(6800, \"Pedra do Indai�\", 11),\r\n" + 
					"(6801, \"Pedra do Sino (Caranda�)\", 11),\r\n" + 
					"(6802, \"Pedra Dourada\", 11),\r\n" + 
					"(6803, \"Pedra Grande\", 20),\r\n" + 
					"(6804, \"Pedra Grande (Almenara)\", 11),\r\n" + 
					"(6805, \"Pedra Lavrada\", 15),\r\n" + 
					"(6806, \"Pedralva\", 11),\r\n" + 
					"(6807, \"Pedra Menina (Rio Vermelho)\", 11),\r\n" + 
					"(6808, \"Pedra Mole\", 25),\r\n" + 
					"(6809, \"Pedran�polis\", 26),\r\n" + 
					"(6810, \"Pedr�o\", 5),\r\n" + 
					"(6811, \"Pedra Preta\", 13),\r\n" + 
					"(6812, \"Pedra Preta\", 20),\r\n" + 
					"(6813, \"Pedras Altas\", 23),\r\n" + 
					"(6814, \"Pedras Altas do Mirim (Capim Grosso)\", 5),\r\n" + 
					"(6815, \"Pedras (Barreirinha)\", 3),\r\n" + 
					"(6816, \"Pedras Brancas (Banabui�)\", 6),\r\n" + 
					"(6817, \"Pedras Brancas (S�o Marcos)\", 23),\r\n" + 
					"(6818, \"Pedras (Capela)\", 25),\r\n" + 
					"(6819, \"Pedras (Castro)\", 18),\r\n" + 
					"(6820, \"Pedras de Fogo\", 15),\r\n" + 
					"(6821, \"Pedras de Maria da Cruz\", 11),\r\n" + 
					"(6822, \"Pedras de Maril�ndia (Una�)\", 11),\r\n" + 
					"(6823, \"Pedra Selada (Resende)\", 19),\r\n" + 
					"(6824, \"Pedras Grandes\", 24),\r\n" + 
					"(6825, \"Pedras (Morada Nova)\", 6),\r\n" + 
					"(6826, \"Pedras Negras (S�o Francisco do Guapor�)\", 21),\r\n" + 
					"(6827, \"Pedregal (Tunas)\", 23),\r\n" + 
					"(6828, \"Pedregulho\", 26),\r\n" + 
					"(6829, \"Pedreira\", 26),\r\n" + 
					"(6830, \"Pedreira (Bagre)\", 14),\r\n" + 
					"(6831, \"Pedreiras\", 10),\r\n" + 
					"(6832, \"Pedreiras (Arroio Grande)\", 23),\r\n" + 
					"(6833, \"Pedrinhas\", 25),\r\n" + 
					"(6834, \"Pedrinhas (Ic�)\", 6),\r\n" + 
					"(6835, \"Pedrinhas Paulista\", 26),\r\n" + 
					"(6836, \"Pedrinhas (Petrolina)\", 16),\r\n" + 
					"(6837, \"Pedrin�polis\", 11),\r\n" + 
					"(6838, \"Pedro Afonso\", 27),\r\n" + 
					"(6839, \"Pedro Alexandre\", 5),\r\n" + 
					"(6840, \"Pedro Avelino\", 20),\r\n" + 
					"(6841, \"Pedro Barros (Miracatu)\", 26),\r\n" + 
					"(6842, \"Pedro Can�rio\", 8),\r\n" + 
					"(6843, \"Pedro de Toledo\", 26),\r\n" + 
					"(6844, \"Pedro do Ros�rio\", 10),\r\n" + 
					"(6845, \"Pedro Garcia (Braga)\", 23),\r\n" + 
					"(6846, \"Pedro Gomes\", 12),\r\n" + 
					"(6847, \"Pedro II\", 17),\r\n" + 
					"(6848, \"Pedro Laurentino\", 17),\r\n" + 
					"(6849, \"Pedro Leopoldo\", 11),\r\n" + 
					"(6850, \"Pedro Lessa (Serro)\", 11),\r\n" + 
					"(6851, \"Pedro Ludovico (Aragua�na)\", 27),\r\n" + 
					"(6852, \"Pedro Lustosa (Reserva do Igua�u)\", 18),\r\n" + 
					"(6853, \"Pedro Neca (Porto Esperidi�o)\", 13),\r\n" + 
					"(6854, \"Pedro Os�rio\", 23),\r\n" + 
					"(6855, \"Pedro Paiva (Santo Augusto)\", 23),\r\n" + 
					"(6856, \"Pedro R�gis\", 15),\r\n" + 
					"(6857, \"Pedro Teixeira\", 11),\r\n" + 
					"(6858, \"Pedro Velho\", 20),\r\n" + 
					"(6859, \"Pedro Versiani (Te�filo Otoni)\", 11),\r\n" + 
					"(6860, \"Pega Fogo (Taquara)\", 23),\r\n" + 
					"(6861, \"Peixe\", 27),\r\n" + 
					"(6862, \"Peixe (Aragua�na)\", 27),\r\n" + 
					"(6863, \"Peixe-Boi\", 14),\r\n" + 
					"(6864, \"Peixe (Campo Alegre de Lourdes)\", 5),\r\n" + 
					"(6865, \"Peixe Gordo (Tabuleiro do Norte)\", 6),\r\n" + 
					"(6866, \"Peixe (Russas)\", 6),\r\n" + 
					"(6867, \"Peixoto de Azevedo\", 13),\r\n" + 
					"(6868, \"Peju�ara\", 23),\r\n" + 
					"(6869, \"Pelada (Caruaru)\", 16),\r\n" + 
					"(6870, \"Pelado (Guarapuava)\", 18),\r\n" + 
					"(6871, \"Pelo Sinal (Mana�ra)\", 15),\r\n" + 
					"(6872, \"Pelotas\", 23),\r\n" + 
					"(6873, \"Penaforte\", 6),\r\n" + 
					"(6874, \"Penalva\", 10),\r\n" + 
					"(6875, \"Pen�polis\", 26),\r\n" + 
					"(6876, \"Pendanga (Ibira�u)\", 8),\r\n" + 
					"(6877, \"Pend�ncias\", 20),\r\n" + 
					"(6878, \"Pen�dia (Caet�)\", 11),\r\n" + 
					"(6879, \"Penedo\", 2),\r\n" + 
					"(6880, \"Penedo (Itatiaia)\", 19),\r\n" + 
					"(6881, \"Penedo (Maranguape)\", 6),\r\n" + 
					"(6882, \"Penha\", 24),\r\n" + 
					"(6883, \"Penha de Fran�a (Itamarandiba)\", 11),\r\n" + 
					"(6884, \"Penha do Capim (Aimor�s)\", 11),\r\n" + 
					"(6885, \"Penha do Cassiano (Governador Valadares)\", 11),\r\n" + 
					"(6886, \"Penha do Norte (Conselheiro Pena)\", 11),\r\n" + 
					"(6887, \"Penha Longa (Chiador)\", 11),\r\n" + 
					"(6888, \"Penhalonga (Vigia)\", 14),\r\n" + 
					"(6889, \"Penido (Juiz de Fora)\", 11),\r\n" + 
					"(6890, \"Pentagna (Valen�a)\", 19),\r\n" + 
					"(6891, \"Pentecoste\", 6),\r\n" + 
					"(6892, \"Pequeri\", 11),\r\n" + 
					"(6893, \"Pequi\", 11),\r\n" + 
					"(6894, \"Pequi� (I�na)\", 8),\r\n" + 
					"(6895, \"Pequizeiro\", 27),\r\n" + 
					"(6896, \"Perdig�o\", 11),\r\n" + 
					"(6897, \"Perdil�ndia (Santa Vit�ria)\", 11),\r\n" + 
					"(6898, \"Perdizes\", 11),\r\n" + 
					"(6899, \"Perd�es\", 11),\r\n" + 
					"(6900, \"Pereira Barreto\", 26),\r\n" + 
					"(6901, \"Pereiras\", 26),\r\n" + 
					"(6902, \"Pereirinhas (Desterro de Entre Rios)\", 11),\r\n" + 
					"(6903, \"Pereiro\", 6),\r\n" + 
					"(6904, \"Pereiros (Sousa)\", 15),\r\n" + 
					"(6905, \"Peric� (S�o Joaquim)\", 24),\r\n" + 
					"(6906, \"Peri Mirim\", 10),\r\n" + 
					"(6907, \"Periquito\", 11),\r\n" + 
					"(6908, \"Peritiba\", 24),\r\n" + 
					"(6909, \"Peritor�\", 10),\r\n" + 
					"(6910, \"Pernambuquinho (Guaramiranga)\", 6),\r\n" + 
					"(6911, \"Pernambuquinho (Sert�nia)\", 16),\r\n" + 
					"(6912, \"Perobal\", 18),\r\n" + 
					"(6913, \"Peroba (Maragogi)\", 2),\r\n" + 
					"(6914, \"P�rola\", 18),\r\n" + 
					"(6915, \"P�rola d'Oeste\", 18),\r\n" + 
					"(6916, \"P�rola Independente (Marip�)\", 18),\r\n" + 
					"(6917, \"Perol�ndia\", 9),\r\n" + 
					"(6918, \"Perp�tuo Socorro (Alagoinha)\", 16),\r\n" + 
					"(6919, \"Perp�tuo Socorro (Belo Oriente)\", 11),\r\n" + 
					"(6920, \"Perseveran�a (S�o Caetano de Odivelas)\", 14),\r\n" + 
					"(6921, \"Peru�be\", 26),\r\n" + 
					"(6922, \"Pescador\", 11),\r\n" + 
					"(6923, \"Pescaria Brava\", 24),\r\n" + 
					"(6924, \"Pesqueira\", 16),\r\n" + 
					"(6925, \"Pesqueiro (Soure)\", 14),\r\n" + 
					"(6926, \"Pessoa Anta (Granja)\", 6),\r\n" + 
					"(6927, \"Petim (Castro Alves)\", 5),\r\n" + 
					"(6928, \"Petrol�ndia\", 24),\r\n" + 
					"(6929, \"Petrol�ndia\", 16),\r\n" + 
					"(6930, \"Petrolina\", 16),\r\n" + 
					"(6931, \"Petrolina de Goi�s\", 9),\r\n" + 
					"(6932, \"Petr�polis\", 19),\r\n" + 
					"(6933, \"Pet�nia (Nova Resende)\", 11),\r\n" + 
					"(6934, \"Piabanha (Mara�)\", 5),\r\n" + 
					"(6935, \"Piabas (Bragan�a)\", 14),\r\n" + 
					"(6936, \"Pia�abu�u\", 2),\r\n" + 
					"(6937, \"Piacatu\", 26),\r\n" + 
					"(6938, \"Piacatuba (Leopoldina)\", 11),\r\n" + 
					"(6939, \"Pia�u (Muniz Freire)\", 8),\r\n" + 
					"(6940, \"Pianc�\", 15),\r\n" + 
					"(6941, \"Pi�o (Santa Rita de Caldas)\", 11),\r\n" + 
					"(6942, \"Pi�o (Sapucaia)\", 19),\r\n" + 
					"(6943, \"Piassuguera (Paranagu�)\", 18),\r\n" + 
					"(6944, \"Piat�\", 5),\r\n" + 
					"(6945, \"Piau\", 11),\r\n" + 
					"(6946, \"Picada Caf�\", 23),\r\n" + 
					"(6947, \"Picadinha (Dourados)\", 12),\r\n" + 
					"(6948, \"Pi�arra\", 14),\r\n" + 
					"(6949, \"Pi�arr�o (Sento S�)\", 5),\r\n" + 
					"(6950, \"Pi�arra (Xinguara)\", 14),\r\n" + 
					"(6951, \"Picinguaba (Ubatuba)\", 26),\r\n" + 
					"(6952, \"Picos\", 17),\r\n" + 
					"(6953, \"Pic Sagarana (Arinos)\", 11),\r\n" + 
					"(6954, \"Picu�\", 15),\r\n" + 
					"(6955, \"Piedade\", 26),\r\n" + 
					"(6956, \"Piedade de Caratinga\", 11),\r\n" + 
					"(6957, \"Piedade de Ponte Nova\", 11),\r\n" + 
					"(6958, \"Piedade do Paraopeba (Brumadinho)\", 11),\r\n" + 
					"(6959, \"Piedade do Rio Grande\", 11),\r\n" + 
					"(6960, \"Piedade dos Gerais\", 11),\r\n" + 
					"(6961, \"Pi�n\", 18),\r\n" + 
					"(6962, \"Pil�o Arcado\", 5),\r\n" + 
					"(6963, \"Pilar\", 15),\r\n" + 
					"(6964, \"Pilar\", 2),\r\n" + 
					"(6965, \"Pilar de Goi�s\", 9),\r\n" + 
					"(6966, \"Pilar do Sul\", 26),\r\n" + 
					"(6967, \"Pilar (Patos de Minas)\", 11),\r\n" + 
					"(6968, \"Pilo�ndia (Israel�ndia)\", 9),\r\n" + 
					"(6969, \"Pil�es\", 20),\r\n" + 
					"(6970, \"Pil�es\", 15),\r\n" + 
					"(6971, \"Pil�es (Aragua�na)\", 27),\r\n" + 
					"(6972, \"Pil�es (Arapiraca)\", 2),\r\n" + 
					"(6973, \"Pil�es (Candiba)\", 5),\r\n" + 
					"(6974, \"Pil�ezinhos\", 15),\r\n" + 
					"(6975, \"Pimenta\", 11),\r\n" + 
					"(6976, \"Pimenta Bueno\", 21),\r\n" + 
					"(6977, \"Pimenteira (Ilh�us)\", 5),\r\n" + 
					"(6978, \"Pimenteiras\", 17),\r\n" + 
					"(6979, \"Pimenteiras do Oeste\", 21),\r\n" + 
					"(6980, \"Pimentel (Pindar� Mirim)\", 10),\r\n" + 
					"(6981, \"Pinar� (Cruz Machado)\", 18),\r\n" + 
					"(6982, \"Pinda�\", 5),\r\n" + 
					"(6983, \"Pinda�bas (Patos de Minas)\", 11),\r\n" + 
					"(6984, \"Pindamonhangaba\", 26),\r\n" + 
					"(6985, \"Pindar� Mirim\", 10),\r\n" + 
					"(6986, \"Pindoba\", 2),\r\n" + 
					"(6987, \"Pindoba�u\", 5),\r\n" + 
					"(6988, \"Pindoguaba (Tiangu�)\", 6),\r\n" + 
					"(6989, \"Pindorama\", 26),\r\n" + 
					"(6990, \"Pindorama do Tocantins\", 27),\r\n" + 
					"(6991, \"Pindoretama\", 6),\r\n" + 
					"(6992, \"Pindotiba (Orleans)\", 24),\r\n" + 
					"(6993, \"Pindur�o (Camala�)\", 15),\r\n" + 
					"(6994, \"Pingo-D'�gua\", 11),\r\n" + 
					"(6995, \"Pinhais\", 18),\r\n" + 
					"(6996, \"Pinhal\", 23),\r\n" + 
					"(6997, \"Pinhal Alto (Nova Petr�polis)\", 23),\r\n" + 
					"(6998, \"Pinhal�o\", 18),\r\n" + 
					"(6999, \"Pinhal (Aveiro)\", 14),\r\n" + 
					"(7000, \"Pinhal (Bom Retiro do Sul)\", 23),\r\n" + 
					"(7001, \"Pinhal da Serra\", 23),\r\n" + 
					"(7002, \"Pinhal de S�o Bento\", 18),\r\n" + 
					"(7003, \"Pinhal Grande\", 23),\r\n" + 
					"(7004, \"Pinhal Queimado (Arvorezinha)\", 23),\r\n" + 
					"(7005, \"Pinhal (Sinimbu)\", 23),\r\n" + 
					"(7006, \"Pinhal (Soledade)\", 23),\r\n" + 
					"(7007, \"Pinhalzinho\", 26),\r\n" + 
					"(7008, \"Pinhalzinho\", 24),\r\n" + 
					"(7009, \"Pinhalzinho (Cascavel)\", 18),\r\n" + 
					"(7010, \"Pinhalzinho (En�as Marques)\", 18),\r\n" + 
					"(7011, \"Pinhalzinho (Erval Grande)\", 23),\r\n" + 
					"(7012, \"Pinhalzinho (Goioxim)\", 18),\r\n" + 
					"(7013, \"Pinhalzinho (Liberato Salzano)\", 23),\r\n" + 
					"(7014, \"Pinhalzinho (Pinh�o)\", 18),\r\n" + 
					"(7015, \"Pinh�o\", 25),\r\n" + 
					"(7016, \"Pinh�o\", 18),\r\n" + 
					"(7017, \"Pinheiral\", 19),\r\n" + 
					"(7018, \"Pinheiral (Major Gercino)\", 24),\r\n" + 
					"(7019, \"Pinheirinho do Vale\", 23),\r\n" + 
					"(7020, \"Pinheirinhos (Passa Quatro)\", 11),\r\n" + 
					"(7021, \"Pinheirinhos (Santo Ant�nio da Patrulha)\", 23),\r\n" + 
					"(7022, \"Pinheiro\", 10),\r\n" + 
					"(7023, \"Pinheiro (Capanema)\", 18),\r\n" + 
					"(7024, \"Pinheiro de Minas (Martins Soares)\", 11),\r\n" + 
					"(7025, \"Pinheiro Grosso (Barbacena)\", 11),\r\n" + 
					"(7026, \"Pinheiro Machado\", 23),\r\n" + 
					"(7027, \"Pinheiro Machado (S�o Paulo das Miss�es)\", 23),\r\n" + 
					"(7028, \"Pinheiro Marcado (Carazinho)\", 23),\r\n" + 
					"(7029, \"Pinheiro Preto\", 24),\r\n" + 
					"(7030, \"Pinheiros\", 8),\r\n" + 
					"(7031, \"Pinheiros Altos (Piranga)\", 11),\r\n" + 
					"(7032, \"Pinheiros (Canoinhas)\", 24),\r\n" + 
					"(7033, \"Pinheiros (Lavrinhas)\", 26),\r\n" + 
					"(7034, \"Pinh�es (Juazeiro)\", 5),\r\n" + 
					"(7035, \"Pinhotiba (Eugen�polis)\", 11),\r\n" + 
					"(7036, \"Pintadas\", 5),\r\n" + 
					"(7037, \"Pinto Bandeira\", 23),\r\n" + 
					"(7038, \"Pint�polis\", 11),\r\n" + 
					"(7039, \"Pintos Negreiros (Maria da F�)\", 11),\r\n" + 
					"(7040, \"Pio IX\", 17),\r\n" + 
					"(7041, \"Pioneiros (Guar�)\", 26),\r\n" + 
					"(7042, \"Pio XII\", 10),\r\n" + 
					"(7043, \"Pio X (Sum�)\", 15),\r\n" + 
					"(7044, \"Pio X (Umari)\", 6),\r\n" + 
					"(7045, \"Pipa (Tibau do Sul)\", 20),\r\n" + 
					"(7046, \"Pipeiras (S�o Jo�o da Barra)\", 19),\r\n" + 
					"(7047, \"Piquerobi\", 26),\r\n" + 
					"(7048, \"Piquet Carneiro\", 6),\r\n" + 
					"(7049, \"Piquete\", 26),\r\n" + 
					"(7050, \"Piquiri (Cachoeira do Sul)\", 23),\r\n" + 
					"(7051, \"Piquiriva� (Campo Mour�o)\", 18),\r\n" + 
					"(7052, \"Piracaia\", 26),\r\n" + 
					"(7053, \"Piracaiba (Araguari)\", 11),\r\n" + 
					"(7054, \"Piracanjuba\", 9),\r\n" + 
					"(7055, \"Piracema\", 11),\r\n" + 
					"(7056, \"Piracema (Afonso Cl�udio)\", 8),\r\n" + 
					"(7057, \"Piracema (Paranava�)\", 18),\r\n" + 
					"(7058, \"Piracicaba\", 26),\r\n" + 
					"(7059, \"Piracuruca\", 17),\r\n" + 
					"(7060, \"Piragi (Itamaraju)\", 5),\r\n" + 
					"(7061, \"Pira�\", 19),\r\n" + 
					"(7062, \"Pira� (Bag�)\", 23),\r\n" + 
					"(7063, \"Pira� do Norte\", 5),\r\n" + 
					"(7064, \"Pira� do Sul\", 18),\r\n" + 
					"(7065, \"Piraj� (Itamaraju)\", 5),\r\n" + 
					"(7066, \"Piraju\", 26),\r\n" + 
					"(7067, \"Pirajuba\", 11),\r\n" + 
					"(7068, \"Piraju�\", 26),\r\n" + 
					"(7069, \"Pirajuia (Jaguaripe)\", 5),\r\n" + 
					"(7070, \"Piraju (Manoel Viana)\", 23),\r\n" + 
					"(7071, \"Piramb�ia (Anhembi)\", 26),\r\n" + 
					"(7072, \"Pirambu\", 25),\r\n" + 
					"(7073, \"Piranga\", 11),\r\n" + 
					"(7074, \"Piranga� (Resende)\", 19),\r\n" + 
					"(7075, \"Pirangi\", 26),\r\n" + 
					"(7076, \"Pirangi (Ibaretama)\", 6),\r\n" + 
					"(7077, \"Pirangu�u\", 11),\r\n" + 
					"(7078, \"Piranguinho\", 11),\r\n" + 
					"(7079, \"Piranguita (Rio Espera)\", 11),\r\n" + 
					"(7080, \"Piranhas\", 9),\r\n" + 
					"(7081, \"Piranhas\", 2),\r\n" + 
					"(7082, \"Pirapanema (Muria�)\", 11),\r\n" + 
					"(7083, \"Pirapemas\", 10),\r\n" + 
					"(7084, \"Pirapetinga\", 11),\r\n" + 
					"(7085, \"Pirapetinga de Bom Jesus (Bom Jesus do Itabapoana)\", 19),\r\n" + 
					"(7086, \"Pirapitinga (Juiz de Fora)\", 11),\r\n" + 
					"(7087, \"Pirap�\", 23),\r\n" + 
					"(7088, \"Pirap� (Apucarana)\", 18),\r\n" + 
					"(7089, \"Pirapora\", 11),\r\n" + 
					"(7090, \"Pirapora do Bom Jesus\", 26),\r\n" + 
					"(7091, \"Pirapora (Itapor�)\", 12),\r\n" + 
					"(7092, \"Pirapozinho\", 26),\r\n" + 
					"(7093, \"Piraputanga (Aquidauana)\", 12),\r\n" + 
					"(7094, \"Piraquara\", 18),\r\n" + 
					"(7095, \"Piraquara (Santar�m)\", 14),\r\n" + 
					"(7096, \"Piraqu�\", 27),\r\n" + 
					"(7097, \"Pirassununga\", 26),\r\n" + 
					"(7098, \"Piratini\", 23),\r\n" + 
					"(7099, \"Piratininga\", 26),\r\n" + 
					"(7100, \"Piratininga (Nova Ubirat�)\", 13),\r\n" + 
					"(7101, \"Piratuba\", 24),\r\n" + 
					"(7102, \"Pirau� (Natuba)\", 15),\r\n" + 
					"(7103, \"Pira�ba\", 11),\r\n" + 
					"(7104, \"Piren�polis\", 9),\r\n" + 
					"(7105, \"Pires Belo (Catal�o)\", 9),\r\n" + 
					"(7106, \"Pires do Rio\", 9),\r\n" + 
					"(7107, \"Pires Ferreira\", 6),\r\n" + 
					"(7108, \"Piri� (Curralinho)\", 14),\r\n" + 
					"(7109, \"Pirip�\", 5),\r\n" + 
					"(7110, \"Piripiri\", 17),\r\n" + 
					"(7111, \"Piri (Sento S�)\", 5),\r\n" + 
					"(7112, \"Piritiba\", 5),\r\n" + 
					"(7113, \"Pirituba (Vit�ria de Santo Ant�o)\", 16),\r\n" + 
					"(7114, \"Pirizal (Nossa Senhora do Livramento)\", 13),\r\n" + 
					"(7115, \"Pirpirituba\", 15),\r\n" + 
					"(7116, \"Piscamba (Jequeri)\", 11),\r\n" + 
					"(7117, \"Pitanga\", 18),\r\n" + 
					"(7118, \"Pitanga de Estrada (Mamanguape)\", 15),\r\n" + 
					"(7119, \"Pitanga (Doutor Maur�cio Cardoso)\", 23),\r\n" + 
					"(7120, \"Pitangueiras\", 18),\r\n" + 
					"(7121, \"Pitangueiras\", 26),\r\n" + 
					"(7122, \"Pitangui\", 11),\r\n" + 
					"(7123, \"Pitangui (Ponta Grossa)\", 18),\r\n" + 
					"(7124, \"Pitarana (Montalv�nia)\", 11),\r\n" + 
					"(7125, \"Pitimbu\", 15),\r\n" + 
					"(7126, \"Pitingal (Passa Sete)\", 23),\r\n" + 
					"(7127, \"Pitombeira (Itapaj�)\", 6),\r\n" + 
					"(7128, \"Pitombeiras (Cascavel)\", 6),\r\n" + 
					"(7129, \"Pituba (Gentio do Ouro)\", 5),\r\n" + 
					"(7130, \"Pium\", 27),\r\n" + 
					"(7131, \"Pi�ma\", 8),\r\n" + 
					"(7132, \"Piumhi\", 11),\r\n" + 
					"(7133, \"Placa de Santo Ant�nio (Juscimeira)\", 13),\r\n" + 
					"(7134, \"Placas\", 14),\r\n" + 
					"(7135, \"Pl�cido de Castro\", 1),\r\n" + 
					"(7136, \"Pl�cido Martins (Aracoiaba)\", 6),\r\n" + 
					"(7137, \"Planalmira (Abadi�nia)\", 9),\r\n" + 
					"(7138, \"Planaltina\", 9),\r\n" + 
					"(7139, \"Planaltina do Paran�\", 18),\r\n" + 
					"(7140, \"Planaltino\", 5),\r\n" + 
					"(7141, \"Planalto\", 18),\r\n" + 
					"(7142, \"Planalto\", 26),\r\n" + 
					"(7143, \"Planalto\", 23),\r\n" + 
					"(7144, \"Planalto\", 5),\r\n" + 
					"(7145, \"Planalto Alegre\", 24),\r\n" + 
					"(7146, \"Planalto (Andradina)\", 26),\r\n" + 
					"(7147, \"Planalto (Conc�rdia)\", 24),\r\n" + 
					"(7148, \"Planalto (Crissiumal)\", 23),\r\n" + 
					"(7149, \"Planalto da Serra\", 13),\r\n" + 
					"(7150, \"Planalto de Minas (Diamantina)\", 11),\r\n" + 
					"(7151, \"Planalto do Sul (Teodoro Sampaio)\", 26),\r\n" + 
					"(7152, \"Plano Alto (Uruguaiana)\", 23),\r\n" + 
					"(7153, \"Planura\", 11),\r\n" + 
					"(7154, \"Platina\", 26),\r\n" + 
					"(7155, \"Plat� do Piqui� (Boca do Acre)\", 3),\r\n" + 
					"(7156, \"Plautino Soares (Sobr�lia)\", 11),\r\n" + 
					"(7157, \"Po�\", 26),\r\n" + 
					"(7158, \"Poaia (Santa Maria do Sua�u�)\", 11),\r\n" + 
					"(7159, \"Po��o\", 16),\r\n" + 
					"(7160, \"Po��o (Arapiraca)\", 2),\r\n" + 
					"(7161, \"Po��o de Afr�nio (Afr�nio)\", 16),\r\n" + 
					"(7162, \"Po��o de Pedras\", 10),\r\n" + 
					"(7163, \"Pocinho (Barbosa Ferraz)\", 18),\r\n" + 
					"(7164, \"Pocinho (Ponta Grossa)\", 18),\r\n" + 
					"(7165, \"Pocinhos\", 15),\r\n" + 
					"(7166, \"Po�o (Arapiraca)\", 2),\r\n" + 
					"(7167, \"Po�o Branco\", 20),\r\n" + 
					"(7168, \"Po�o (Brejo Santo)\", 6),\r\n" + 
					"(7169, \"Po�o Central (Aurelino Leal)\", 5),\r\n" + 
					"(7170, \"Po�o Comprido (Amontada)\", 6),\r\n" + 
					"(7171, \"Po�o Comprido (Correntes)\", 16),\r\n" + 
					"(7172, \"Po�o Comprido (Jaguaribara)\", 6),\r\n" + 
					"(7173, \"Po�o Dantas\", 15),\r\n" + 
					"(7174, \"Po�o da On�a (Mira�ma)\", 6),\r\n" + 
					"(7175, \"Po�o da Pedra (Arapiraca)\", 2),\r\n" + 
					"(7176, \"Po�o da Pedra de Cima (Arapiraca)\", 2),\r\n" + 
					"(7177, \"Po�o das Antas\", 23),\r\n" + 
					"(7178, \"Po�o das Trincheiras\", 2),\r\n" + 
					"(7179, \"Po�o de Baixo (Arapiraca)\", 2),\r\n" + 
					"(7180, \"Po�o de Fora (Cura��)\", 5),\r\n" + 
					"(7181, \"Po�o de Jos� de Moura\", 15),\r\n" + 
					"(7182, \"Po�o de Santana (Arapiraca)\", 2),\r\n" + 
					"(7183, \"Po��es\", 5),\r\n" + 
					"(7184, \"Po��es de Paineiras (Paineiras)\", 11),\r\n" + 
					"(7185, \"Po�o Fundo\", 11),\r\n" + 
					"(7186, \"Po�o Fundo (Parob�)\", 23),\r\n" + 
					"(7187, \"Po�o Fundo (Santa Cruz do Capibaribe)\", 16),\r\n" + 
					"(7188, \"Po�o Grande (Juc�s)\", 6),\r\n" + 
					"(7189, \"Pocon�\", 13),\r\n" + 
					"(7190, \"Po�o Preto (Irine�polis)\", 24),\r\n" + 
					"(7191, \"Po�o Redondo\", 25),\r\n" + 
					"(7192, \"Po�os de Caldas\", 11),\r\n" + 
					"(7193, \"Po�os (Remanso)\", 5),\r\n" + 
					"(7194, \"Po�o Verde\", 25),\r\n" + 
					"(7195, \"Po�o Verde (Mucambo)\", 6),\r\n" + 
					"(7196, \"Pocrane\", 11),\r\n" + 
					"(7197, \"Poema (Nova Tebas)\", 18),\r\n" + 
					"(7198, \"Pojuca\", 5),\r\n" + 
					"(7199, \"Pol�gono do Erval (Victor Graeff)\", 23),\r\n" + 
					"(7200, \"Poloni\", 26),\r\n" + 
					"(7201, \"P�lo Petroqu�mico de Triunfo (Triunfo)\", 23),\r\n" + 
					"(7202, \"Pombal\", 15),\r\n" + 
					"(7203, \"Pombas (Dom Aquino)\", 13),\r\n" + 
					"(7204, \"Pombos\", 16),\r\n" + 
					"(7205, \"Pomerode\", 24),\r\n" + 
					"(7206, \"Pomp�ia\", 26),\r\n" + 
					"(7207, \"Pomp�u\", 11),\r\n" + 
					"(7208, \"Pompeu Machado (Encruzilhada do Sul)\", 23),\r\n" + 
					"(7209, \"Poncianos (Concei��o das Alagoas)\", 11),\r\n" + 
					"(7210, \"Ponga�\", 26),\r\n" + 
					"(7211, \"Ponta da Areia (Caravelas)\", 5),\r\n" + 
					"(7212, \"Ponta da Serra (Crato)\", 6),\r\n" + 
					"(7213, \"Ponta de Pedras\", 14),\r\n" + 
					"(7214, \"Ponta de Ramos (Curu��)\", 14),\r\n" + 
					"(7215, \"Ponta do Pasto (Paranagu�)\", 18),\r\n" + 
					"(7216, \"Ponta Grossa\", 18),\r\n" + 
					"(7217, \"Pontal\", 26),\r\n" + 
					"(7218, \"Pontal de Coruripe (Coruripe)\", 2),\r\n" + 
					"(7219, \"Pontal do Araguaia\", 13),\r\n" + 
					"(7220, \"Pontal do Paran�\", 18),\r\n" + 
					"(7221, \"Pontalete (Tr�s Pontas)\", 11),\r\n" + 
					"(7222, \"Pontalina\", 9),\r\n" + 
					"(7223, \"Pontalinda\", 26),\r\n" + 
					"(7224, \"Pont�o\", 23),\r\n" + 
					"(7225, \"Pont�o Santo Ant�nio (Catu�pe)\", 23),\r\n" + 
					"(7226, \"Ponta Por�\", 12),\r\n" + 
					"(7227, \"Pontas de Pedra (Goiana)\", 16),\r\n" + 
					"(7228, \"Ponte Alta\", 24),\r\n" + 
					"(7229, \"Ponte Alta de Minas (Carangola)\", 11),\r\n" + 
					"(7230, \"Ponte Alta do Bom Jesus\", 27),\r\n" + 
					"(7231, \"Ponte Alta do Norte\", 24),\r\n" + 
					"(7232, \"Ponte Alta do Tocantins\", 27),\r\n" + 
					"(7233, \"Ponte Alta (Uberaba)\", 11),\r\n" + 
					"(7234, \"Ponte Branca\", 13),\r\n" + 
					"(7235, \"Ponte de Itabapoana (Mimoso do Sul)\", 8),\r\n" + 
					"(7236, \"Ponte de Pedra (Rondon�polis)\", 13),\r\n" + 
					"(7237, \"Ponte do Cosme (Barbacena)\", 11),\r\n" + 
					"(7238, \"Ponte dos Ciganos (Cora��o de Jesus)\", 11),\r\n" + 
					"(7239, \"Ponte Firme (Presidente Oleg�rio)\", 11),\r\n" + 
					"(7240, \"Ponte Nova\", 11),\r\n" + 
					"(7241, \"Ponte Preta\", 23),\r\n" + 
					"(7242, \"Pontes (Aragua�na)\", 27),\r\n" + 
					"(7243, \"Ponte Segura (Senador Amaral)\", 11),\r\n" + 
					"(7244, \"Pontes e Lacerda\", 13),\r\n" + 
					"(7245, \"Ponte Serrada\", 24),\r\n" + 
					"(7246, \"Pontes Filho (Teut�nia)\", 23),\r\n" + 
					"(7247, \"Pontes Gestal\", 26),\r\n" + 
					"(7248, \"Ponte Vermelha (S�o Gabriel do Oeste)\", 12),\r\n" + 
					"(7249, \"Pontevila (Formiga)\", 11),\r\n" + 
					"(7250, \"Pontinha do Cocho (Camapu�)\", 12),\r\n" + 
					"(7251, \"Pontin�polis (S�o F�lix do Araguaia)\", 13),\r\n" + 
					"(7252, \"Ponto Belo\", 8),\r\n" + 
					"(7253, \"Ponto Chique\", 11),\r\n" + 
					"(7254, \"Ponto Chique do Martelo (Barbacena)\", 11),\r\n" + 
					"(7255, \"Ponto do Marambaia (Cara�)\", 11),\r\n" + 
					"(7256, \"Ponto dos Volantes\", 11),\r\n" + 
					"(7257, \"Pont�es (Afonso Cl�udio)\", 8),\r\n" + 
					"(7258, \"Ponto Novo\", 5),\r\n" + 
					"(7259, \"Populina\", 26),\r\n" + 
					"(7260, \"Poranga\", 6),\r\n" + 
					"(7261, \"Porangaba\", 26),\r\n" + 
					"(7262, \"Porangatu\", 9),\r\n" + 
					"(7263, \"Por�ozinho (Aragua�na)\", 27),\r\n" + 
					"(7264, \"Porci�ncula\", 19),\r\n" + 
					"(7265, \"Porecatu\", 18),\r\n" + 
					"(7266, \"Porfirio Sampaio (Pentecoste)\", 6),\r\n" + 
					"(7267, \"Portalegre\", 20),\r\n" + 
					"(7268, \"Port�o\", 23),\r\n" + 
					"(7269, \"Port�o (Cascavel)\", 18),\r\n" + 
					"(7270, \"Porteir�o\", 9),\r\n" + 
					"(7271, \"Porteira Preta (F�nix)\", 18),\r\n" + 
					"(7272, \"Porteiras\", 6),\r\n" + 
					"(7273, \"Porteirinha\", 11),\r\n" + 
					"(7274, \"Porteirinha de Pedra (Campina Grande)\", 15),\r\n" + 
					"(7275, \"Portel\", 14),\r\n" + 
					"(7276, \"Portela (Itaocara)\", 19),\r\n" + 
					"(7277, \"Portel�ndia\", 9),\r\n" + 
					"(7278, \"Porto\", 17),\r\n" + 
					"(7279, \"Porto Acre\", 1),\r\n" + 
					"(7280, \"Porto Agr�rio (Juven�lia)\", 11),\r\n" + 
					"(7281, \"Porto Alegre\", 23),\r\n" + 
					"(7282, \"Porto Alegre do Norte\", 13),\r\n" + 
					"(7283, \"Porto Alegre do Piau�\", 17),\r\n" + 
					"(7284, \"Porto Alegre do Tocantins\", 27),\r\n" + 
					"(7285, \"Porto Amazonas\", 18),\r\n" + 
					"(7286, \"Porto Barreiro\", 18),\r\n" + 
					"(7287, \"Porto Batista (Triunfo)\", 23),\r\n" + 
					"(7288, \"Porto Belo\", 24),\r\n" + 
					"(7289, \"Porto Bras�lio (Quer�ncia do Norte)\", 18),\r\n" + 
					"(7290, \"Porto Calvo\", 2),\r\n" + 
					"(7291, \"Porto Camargo (Icara�ma)\", 18),\r\n" + 
					"(7292, \"Porto Col�nia (Dom Pedro de Alc�ntara)\", 23),\r\n" + 
					"(7293, \"Porto da Folha\", 25),\r\n" + 
					"(7294, \"Porto das Flores (Belmiro Braga)\", 11),\r\n" + 
					"(7295, \"Porto das Gabarras (Anajatuba)\", 10),\r\n" + 
					"(7296, \"Porto de Cima (Morretes)\", 18),\r\n" + 
					"(7297, \"Porto de Moz\", 14),\r\n" + 
					"(7298, \"Porto de Pedras\", 2),\r\n" + 
					"(7299, \"Porto do Mangue\", 20),\r\n" + 
					"(7300, \"Porto dos Ga�chos\", 13),\r\n" + 
					"(7301, \"Porto dos Mendes (Campo Belo)\", 11),\r\n" + 
					"(7302, \"Porto Esperan�a (Corumb�)\", 12),\r\n" + 
					"(7303, \"Porto Esperidi�o\", 13),\r\n" + 
					"(7304, \"Porto Estrela\", 13),\r\n" + 
					"(7305, \"Porto Feliz\", 26),\r\n" + 
					"(7306, \"Porto Ferreira\", 26),\r\n" + 
					"(7307, \"Porto Firme\", 11),\r\n" + 
					"(7308, \"Porto Franco\", 10),\r\n" + 
					"(7309, \"Porto Grande\", 4),\r\n" + 
					"(7310, \"Porto Lemos (Aragua�na)\", 27),\r\n" + 
					"(7311, \"Porto Lucena\", 23),\r\n" + 
					"(7312, \"Porto Mau�\", 23),\r\n" + 
					"(7313, \"Porto Meira (Foz do Igua�u)\", 18),\r\n" + 
					"(7314, \"Porto Mendes (Marechal C�ndido Rondon)\", 18),\r\n" + 
					"(7315, \"Porto Murtinho\", 12),\r\n" + 
					"(7316, \"Porto Nacional\", 27),\r\n" + 
					"(7317, \"Porto Novo (Santana)\", 5),\r\n" + 
					"(7318, \"Porto Real\", 19),\r\n" + 
					"(7319, \"Porto Real do Col�gio\", 2),\r\n" + 
					"(7320, \"Porto Rico\", 18),\r\n" + 
					"(7321, \"Porto Rico do Maranh�o\", 10),\r\n" + 
					"(7322, \"Porto Salvo (Vigia)\", 14),\r\n" + 
					"(7323, \"Porto San Juan (Foz do Igua�u)\", 18),\r\n" + 
					"(7324, \"Porto S�o Carlos (S�o Carlos do Iva�)\", 18),\r\n" + 
					"(7325, \"Porto S�o Jos� (S�o Pedro do Paran�)\", 18),\r\n" + 
					"(7326, \"Porto Sau�pe (Entre Rios)\", 5),\r\n" + 
					"(7327, \"Porto Seguro\", 5),\r\n" + 
					"(7328, \"Porto Soberbo (Tiradentes do Sul)\", 23),\r\n" + 
					"(7329, \"Porto Trombetas (Oriximin�)\", 14),\r\n" + 
					"(7330, \"Porto Uni�o\", 24),\r\n" + 
					"(7331, \"Porto Velho\", 21),\r\n" + 
					"(7332, \"Porto Velho do Cunha (Carmo)\", 19),\r\n" + 
					"(7333, \"Porto Vera Cruz\", 23),\r\n" + 
					"(7334, \"Porto Vilma (Deod�polis)\", 12),\r\n" + 
					"(7335, \"Porto Vit�ria\", 18),\r\n" + 
					"(7336, \"Porto Walter\", 1),\r\n" + 
					"(7337, \"Porto Xavier\", 23),\r\n" + 
					"(7338, \"Porto XV de Novembro (Bataguassu)\", 12),\r\n" + 
					"(7339, \"Posse\", 9),\r\n" + 
					"(7340, \"Posse D'Abadia (Abadi�nia)\", 9),\r\n" + 
					"(7341, \"Posse dos Linhares (Rio dos �ndios)\", 23),\r\n" + 
					"(7342, \"Possel�ndia (Guap�)\", 9),\r\n" + 
					"(7343, \"Posto da Mata (Nova Vi�osa)\", 5),\r\n" + 
					"(7344, \"Pot�\", 11),\r\n" + 
					"(7345, \"Potengi\", 6),\r\n" + 
					"(7346, \"Poti (Crate�s)\", 6),\r\n" + 
					"(7347, \"Potim\", 26),\r\n" + 
					"(7348, \"Potiragu�\", 5),\r\n" + 
					"(7349, \"Potirendaba\", 26),\r\n" + 
					"(7350, \"Potiretama\", 6),\r\n" + 
					"(7351, \"Pouso Alegre\", 11),\r\n" + 
					"(7352, \"Pouso Alegre (Formiga)\", 11),\r\n" + 
					"(7353, \"Pouso Alto\", 11),\r\n" + 
					"(7354, \"Pouso Novo\", 23),\r\n" + 
					"(7355, \"Pouso Redondo\", 24),\r\n" + 
					"(7356, \"Povoado Tozzo (Itatiba do Sul)\", 23),\r\n" + 
					"(7358, \"Poxim (Coruripe)\", 2),\r\n" + 
					"(7359, \"Poxim do Sul (Canavieiras)\", 5),\r\n" + 
					"(7360, \"Poxor�u\", 13),\r\n" + 
					"(7361, \"Pracinha\", 26),\r\n" + 
					"(7362, \"Pracu�ba\", 4),\r\n" + 
					"(7363, \"Prad�nia (Piraju�)\", 26),\r\n" + 
					"(7364, \"Prado\", 5),\r\n" + 
					"(7365, \"Prado Ferreira\", 18),\r\n" + 
					"(7366, \"Prado Novo (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(7367, \"Prad�polis\", 26),\r\n" + 
					"(7368, \"Prados\", 11),\r\n" + 
					"(7369, \"Pradoso (Vit�ria da Conquista)\", 5),\r\n" + 
					"(7370, \"Praia Grande\", 24),\r\n" + 
					"(7371, \"Praia Grande\", 26),\r\n" + 
					"(7372, \"Praia Grande (Fund�o)\", 8),\r\n" + 
					"(7373, \"Praia Norte\", 27),\r\n" + 
					"(7374, \"Praia Rica (Chapada dos Guimar�es)\", 13),\r\n" + 
					"(7375, \"Prainha\", 14),\r\n" + 
					"(7376, \"Pranchada (Doutor Maur�cio Cardoso)\", 23),\r\n" + 
					"(7377, \"Pranchita\", 18),\r\n" + 
					"(7378, \"Prata\", 15),\r\n" + 
					"(7379, \"Prata\", 11),\r\n" + 
					"(7380, \"Prata (Bela Cruz)\", 6),\r\n" + 
					"(7381, \"Prata (Camb�)\", 18),\r\n" + 
					"(7382, \"Prata do Piau�\", 17),\r\n" + 
					"(7383, \"Prata (Monte Alegre de Goi�s)\", 9),\r\n" + 
					"(7384, \"Prat�nia\", 26),\r\n" + 
					"(7385, \"Prat�polis\", 11),\r\n" + 
					"(7386, \"Prata (S�o Miguel do Oeste)\", 24),\r\n" + 
					"(7387, \"Prata Um (Tel�maco Borba)\", 18),\r\n" + 
					"(7388, \"Pratinha\", 11),\r\n" + 
					"(7389, \"Prati�s (Pindoretama)\", 6),\r\n" + 
					"(7390, \"Pratos (Tucunduva)\", 23),\r\n" + 
					"(7391, \"Prefeita Suely Pinheiro (Solon�pole)\", 6),\r\n" + 
					"(7392, \"Presidente Alves\", 26),\r\n" + 
					"(7393, \"Presidente Bernardes\", 11),\r\n" + 
					"(7394, \"Presidente Bernardes\", 26),\r\n" + 
					"(7395, \"Presidente Castello Branco\", 24),\r\n" + 
					"(7396, \"Presidente Castelo Branco\", 18),\r\n" + 
					"(7397, \"Presidente Castelo (Deod�polis)\", 12),\r\n" + 
					"(7398, \"Presidente Dutra\", 5),\r\n" + 
					"(7399, \"Presidente Dutra\", 10),\r\n" + 
					"(7400, \"Presidente Epit�cio\", 26),\r\n" + 
					"(7401, \"Presidente Figueiredo\", 3),\r\n" + 
					"(7402, \"Presidente Get�lio\", 24),\r\n" + 
					"(7403, \"Presidente J�nio Quadros\", 5),\r\n" + 
					"(7404, \"Presidente Juscelino\", 10),\r\n" + 
					"(7405, \"Presidente Juscelino\", 11),\r\n" + 
					"(7406, \"Presidente Juscelino (S�o Louren�o do Oeste)\", 24),\r\n" + 
					"(7407, \"Presidente Kennedy\", 27),\r\n" + 
					"(7408, \"Presidente Kennedy\", 8),\r\n" + 
					"(7409, \"Presidente Kennedy (Conc�rdia)\", 24),\r\n" + 
					"(7410, \"Presidente Kennedy (Ver�)\", 18),\r\n" + 
					"(7411, \"Presidente Kubitschek\", 11),\r\n" + 
					"(7412, \"Presidente Lucena\", 23),\r\n" + 
					"(7413, \"Presidente M�dici\", 10),\r\n" + 
					"(7414, \"Presidente M�dici\", 21),\r\n" + 
					"(7415, \"Presidente Nereu\", 24),\r\n" + 
					"(7416, \"Presidente Oleg�rio\", 11),\r\n" + 
					"(7417, \"Presidente Pena (Carlos Chagas)\", 11),\r\n" + 
					"(7418, \"Presidente Prudente\", 26),\r\n" + 
					"(7419, \"Presidente Sarney\", 10),\r\n" + 
					"(7420, \"Presidente Tancredo Neves\", 5),\r\n" + 
					"(7421, \"Presidente Vargas\", 10),\r\n" + 
					"(7422, \"Presidente Venceslau\", 26),\r\n" + 
					"(7423, \"Prevenido (Am�rica Dourada)\", 5),\r\n" + 
					"(7424, \"Primavera\", 14),\r\n" + 
					"(7425, \"Primavera\", 16),\r\n" + 
					"(7426, \"Primavera de Rond�nia\", 21),\r\n" + 
					"(7427, \"Primavera do Leste\", 13),\r\n" + 
					"(7428, \"Primavera (Rosana)\", 26),\r\n" + 
					"(7429, \"Primavera (Sorriso)\", 13),\r\n" + 
					"(7430, \"Primeira Cruz\", 10),\r\n" + 
					"(7431, \"Primeiro de Maio\", 18),\r\n" + 
					"(7432, \"Princesa\", 24),\r\n" + 
					"(7433, \"Princesa Isabel\", 15),\r\n" + 
					"(7434, \"Princesa (Rio Novo do Sul)\", 8),\r\n" + 
					"(7435, \"Pr�ncipe da Beira (Costa Marques)\", 21),\r\n" + 
					"(7436, \"Pr�ncipe (Natividade)\", 27),\r\n" + 
					"(7437, \"Professor Jamil\", 9),\r\n" + 
					"(7438, \"Professor Sperber (Chal�)\", 11),\r\n" + 
					"(7439, \"Progresso\", 23),\r\n" + 
					"(7440, \"Progresso (Arroio do Tigre)\", 23),\r\n" + 
					"(7441, \"Progresso (Francisco Beltr�o)\", 18),\r\n" + 
					"(7442, \"Progresso (Tangar� da Serra)\", 13),\r\n" + 
					"(7443, \"Progresso (Tr�s de Maio)\", 23),\r\n" + 
					"(7444, \"Progresso (Tr�s Palmeiras)\", 23),\r\n" + 
					"(7445, \"Promiss�o\", 26),\r\n" + 
					"(7446, \"Propri�\", 25),\r\n" + 
					"(7447, \"Prosperidade (Vargem Alta)\", 8),\r\n" + 
					"(7448, \"Prot�sio Alves\", 23),\r\n" + 
					"(7449, \"Provid�ncia (Leopoldina)\", 11),\r\n" + 
					"(7450, \"Prud�ncio de Morais (General Salgado)\", 26),\r\n" + 
					"(7451, \"Prud�ncio Thomaz (Rio Brilhante)\", 12),\r\n" + 
					"(7452, \"Prudente de Morais\", 11),\r\n" + 
					"(7453, \"Prudente de Morais (Quixeramobim)\", 6),\r\n" + 
					"(7454, \"Prudent�polis\", 18),\r\n" + 
					"(7455, \"Pugmil\", 27),\r\n" + 
					"(7456, \"Pulador (Passo Fundo)\", 23),\r\n" + 
					"(7457, \"Pulador (Uni�o da Serra)\", 23),\r\n" + 
					"(7458, \"Pulin�polis (Mandagua�u)\", 18),\r\n" + 
					"(7459, \"Pureza\", 20),\r\n" + 
					"(7460, \"Pureza (S�o Fid�lis)\", 19),\r\n" + 
					"(7461, \"Puril�ndia (Porci�ncula)\", 19),\r\n" + 
					"(7462, \"Purupuru (Careiro)\", 3),\r\n" + 
					"(7463, \"Putinga\", 23),\r\n" + 
					"(7464, \"Puxinan�\", 15),\r\n" + 
					"(7465, \"Quadra\", 26),\r\n" + 
					"(7466, \"Quara�u (C�ndido Sales)\", 5),\r\n" + 
					"(7467, \"Quara�\", 23),\r\n" + 
					"(7468, \"Quaraim (Tr�s de Maio)\", 23),\r\n" + 
					"(7469, \"Quart�is (Silva Jardim)\", 19),\r\n" + 
					"(7470, \"Quartel de S�o Jo�o (Quartel Geral)\", 11),\r\n" + 
					"(7471, \"Quartel do Sacramento (Bom Jesus do Galho)\", 11),\r\n" + 
					"(7472, \"Quartel Geral\", 11),\r\n" + 
					"(7473, \"Quarto Centen�rio\", 18),\r\n" + 
					"(7474, \"Quat�\", 26),\r\n" + 
					"(7475, \"Quati (Arapiraca)\", 2),\r\n" + 
					"(7476, \"Quatigu�\", 18),\r\n" + 
					"(7477, \"Quatiguaba (Vi�osa do Cear�)\", 6),\r\n" + 
					"(7478, \"Quatipuru\", 14),\r\n" + 
					"(7479, \"Quatis\", 19),\r\n" + 
					"(7480, \"Quatituba (Itueta)\", 11),\r\n" + 
					"(7481, \"Quatro Barras\", 18),\r\n" + 
					"(7482, \"Quatro Bocas (Tom�-A��)\", 14),\r\n" + 
					"(7483, \"Quatro Irm�os\", 23),\r\n" + 
					"(7484, \"Quatro Pontes\", 18),\r\n" + 
					"(7485, \"Quebracho (Anauril�ndia)\", 12),\r\n" + 
					"(7486, \"Quebra C�co (Sidrol�ndia)\", 12),\r\n" + 
					"(7487, \"Quebra Freio (Pato Branco)\", 18),\r\n" + 
					"(7488, \"Quebrangulo\", 2),\r\n" + 
					"(7489, \"Quedas do Igua�u\", 18),\r\n" + 
					"(7490, \"Queimada Nova\", 17),\r\n" + 
					"(7491, \"Queimadas\", 5),\r\n" + 
					"(7492, \"Queimadas\", 15),\r\n" + 
					"(7493, \"Queimados\", 19),\r\n" + 
					"(7494, \"Queimados (Guarapuava)\", 18),\r\n" + 
					"(7495, \"Queimados (Horizonte)\", 6),\r\n" + 
					"(7496, \"Queiroz\", 26),\r\n" + 
					"(7497, \"Queixada (Novo Cruzeiro)\", 11),\r\n" + 
					"(7498, \"Queluz\", 26),\r\n" + 
					"(7499, \"Queluzito\", 11),\r\n" + 
					"(7500, \"Quem-Quem (Jana�ba)\", 11),\r\n" + 
					"(7501, \"Quer�ncia\", 13),\r\n" + 
					"(7502, \"Quer�ncia do Norte\", 18),\r\n" + 
					"(7503, \"Quevedos\", 23),\r\n" + 
					"(7504, \"Quijingue\", 5),\r\n" + 
					"(7505, \"Quilombo\", 24),\r\n" + 
					"(7506, \"Quilombo Nossa Senhora do Ros�rio (Tr�s Pontas)\", 11),\r\n" + 
					"(7507, \"Quilombo (Pelotas)\", 23),\r\n" + 
					"(7508, \"Quilombo (Sabin�polis)\", 11),\r\n" + 
					"(7509, \"Quil�metro 14 do Mutum (Baixo Guandu)\", 8),\r\n" + 
					"(7510, \"Quimami (Miss�o Velha)\", 6),\r\n" + 
					"(7511, \"Quinco� (Acopiara)\", 6),\r\n" + 
					"(7512, \"Quincunc� (Farias Brito)\", 6),\r\n" + 
					"(7513, \"Quinta do Sol\", 18),\r\n" + 
					"(7514, \"Quintana\", 26),\r\n" + 
					"(7515, \"Quint�o (Palmares do Sul)\", 23),\r\n" + 
					"(7517, \"Quintinos (Carmo do Parana�ba)\", 11),\r\n" + 
					"(7518, \"Quinze de Novembro\", 23),\r\n" + 
					"(7519, \"Quinze de Novembro (Giru�)\", 23),\r\n" + 
					"(7520, \"Quinz�polis (Santa Mariana)\", 18),\r\n" + 
					"(7521, \"Quipap�\", 16),\r\n" + 
					"(7522, \"Quirin�polis\", 9),\r\n" + 
					"(7523, \"Quissam�\", 19),\r\n" + 
					"(7524, \"Quitai�s (Lavras da Mangabeira)\", 6),\r\n" + 
					"(7525, \"Quitandinha\", 18),\r\n" + 
					"(7526, \"Quiterian�polis\", 6),\r\n" + 
					"(7527, \"Quit�ria (S�o Jer�nimo)\", 23),\r\n" + 
					"(7528, \"Quitimbu (Cust�dia)\", 16),\r\n" + 
					"(7529, \"Quixab�\", 15),\r\n" + 
					"(7530, \"Quixab�\", 16),\r\n" + 
					"(7531, \"Quixab� (Morpar�)\", 5),\r\n" + 
					"(7532, \"Quixabeira\", 5),\r\n" + 
					"(7533, \"Quixad�\", 6),\r\n" + 
					"(7534, \"Quixad� (Uira�na)\", 15),\r\n" + 
					"(7535, \"Quixari� (Campos Sales)\", 6),\r\n" + 
					"(7536, \"Quixel�\", 6),\r\n" + 
					"(7537, \"Quixeramobim\", 6),\r\n" + 
					"(7538, \"Quixer�\", 6),\r\n" + 
					"(7539, \"Rafael (Caruaru)\", 16),\r\n" + 
					"(7540, \"Rafael Fernandes\", 20),\r\n" + 
					"(7541, \"Rafael Godeiro\", 20),\r\n" + 
					"(7542, \"Rafael Jambeiro\", 5),\r\n" + 
					"(7543, \"Rafard\", 26),\r\n" + 
					"(7544, \"Raimundo Martins (Santa Quit�ria)\", 6),\r\n" + 
					"(7545, \"Rainha do Mar (Xangri-L�)\", 23),\r\n" + 
					"(7546, \"Rainha Isabel (Bom Conselho)\", 16),\r\n" + 
					"(7547, \"Raiz (Belo Jardim)\", 16),\r\n" + 
					"(7548, \"Rajada (Petrolina)\", 16),\r\n" + 
					"(7549, \"Ramadinha (Ararend�)\", 6),\r\n" + 
					"(7550, \"Ramil�ndia\", 18),\r\n" + 
					"(7551, \"Rancharia\", 26),\r\n" + 
					"(7552, \"Rancharia (Araripina)\", 16),\r\n" + 
					"(7553, \"Rancharia (Nova Brasil�ndia)\", 13),\r\n" + 
					"(7554, \"Ranchinho (Monte Alegre dos Campos)\", 23),\r\n" + 
					"(7555, \"Rancho Alegre\", 18),\r\n" + 
					"(7556, \"Rancho Alegre d'Oeste\", 18),\r\n" + 
					"(7557, \"Rancho Queimado\", 24),\r\n" + 
					"(7558, \"Rancho Velho (Capivari do Sul)\", 23),\r\n" + 
					"(7559, \"Rangel (Riacho das Almas)\", 16),\r\n" + 
					"(7560, \"Raposa\", 10),\r\n" + 
					"(7561, \"Raposo (Itaperuna)\", 19),\r\n" + 
					"(7562, \"Raposos\", 11),\r\n" + 
					"(7563, \"Raspador (Ribeira do Amparo)\", 5),\r\n" + 
					"(7564, \"Raul Soares\", 11),\r\n" + 
					"(7565, \"Ravena (Sabar�)\", 11),\r\n" + 
					"(7566, \"Realejo (Crate�s)\", 6),\r\n" + 
					"(7567, \"Realeza\", 18),\r\n" + 
					"(7568, \"Realeza (Manhua�u)\", 11),\r\n" + 
					"(7569, \"Rebou�as\", 18),\r\n" + 
					"(7570, \"Rechan (Itapetininga)\", 26),\r\n" + 
					"(7571, \"Recife\", 16),\r\n" + 
					"(7572, \"Recife (Jussara)\", 5),\r\n" + 
					"(7573, \"Recreio\", 11),\r\n" + 
					"(7574, \"Recursol�ndia\", 27),\r\n" + 
					"(7575, \"Reden��o\", 6),\r\n" + 
					"(7576, \"Reden��o\", 14),\r\n" + 
					"(7577, \"Reden��o da Serra\", 26),\r\n" + 
					"(7578, \"Reden��o do Gurgu�ia\", 17),\r\n" + 
					"(7579, \"Redentora\", 23),\r\n" + 
					"(7580, \"Reduto\", 11),\r\n" + 
					"(7581, \"Refugiado (Vacaria)\", 23),\r\n" + 
					"(7582, \"Regenera��o\", 17),\r\n" + 
					"(7583, \"Regente Feij�\", 26),\r\n" + 
					"(7584, \"Regi�o dos Valos (Tel�maco Borba)\", 18),\r\n" + 
					"(7585, \"Regin�polis\", 26),\r\n" + 
					"(7586, \"Registro\", 26),\r\n" + 
					"(7587, \"Registro do Araguaia (Montes Claros de Goi�s)\", 9),\r\n" + 
					"(7588, \"Reian�polis (Seng�s)\", 18),\r\n" + 
					"(7589, \"Relvado\", 23),\r\n" + 
					"(7590, \"Remans�o (Tucuru�)\", 14),\r\n" + 
					"(7591, \"Remanso\", 5),\r\n" + 
					"(7592, \"Rem�dios (Novo Horizonte)\", 5),\r\n" + 
					"(7593, \"Rem�gio\", 15),\r\n" + 
					"(7594, \"Renascen�a\", 18),\r\n" + 
					"(7595, \"Renascen�a (Santa Maria Madalena)\", 19),\r\n" + 
					"(7596, \"Repartimento de Tuiu� (Manacapuru)\", 3),\r\n" + 
					"(7597, \"Repartimento (Mau�s)\", 3),\r\n" + 
					"(7598, \"Repartimento (Tucuru�)\", 14),\r\n" + 
					"(7599, \"Reriutaba\", 6),\r\n" + 
					"(7600, \"Resende\", 19),\r\n" + 
					"(7601, \"Resende Costa\", 11),\r\n" + 
					"(7602, \"Reserva\", 18),\r\n" + 
					"(7603, \"Reserva do Caba�al\", 13),\r\n" + 
					"(7604, \"Reserva do Igua�u\", 18),\r\n" + 
					"(7605, \"Resid�ncia Fuck (Monte Castelo)\", 24),\r\n" + 
					"(7606, \"Resplandes (Barra do Corda)\", 10),\r\n" + 
					"(7607, \"Resplendor\", 11),\r\n" + 
					"(7608, \"Ressaca do Buriti (Santo �ngelo)\", 23),\r\n" + 
					"(7609, \"Ressaquinha\", 11),\r\n" + 
					"(7610, \"Restinga\", 26),\r\n" + 
					"(7611, \"Restinga Seca\", 23),\r\n" + 
					"(7612, \"Restinga Seca (Santo �ngelo)\", 23),\r\n" + 
					"(7613, \"Retiro (Arapiraca)\", 2),\r\n" + 
					"(7614, \"Retiro (Castro)\", 18),\r\n" + 
					"(7615, \"Retiro do Muria� (Itaperuna)\", 19),\r\n" + 
					"(7616, \"Retiro Grande (Campo Largo)\", 18),\r\n" + 
					"(7617, \"Retiro (Junqueiro)\", 2),\r\n" + 
					"(7618, \"Retirol�ndia\", 5),\r\n" + 
					"(7619, \"Retiro Velho (Chapada Ga�cha)\", 11),\r\n" + 
					"(7620, \"Riach�o\", 10),\r\n" + 
					"(7621, \"Riach�o\", 15),\r\n" + 
					"(7622, \"Riach�o das Neves\", 5),\r\n" + 
					"(7623, \"Riach�o do Bacamarte\", 15),\r\n" + 
					"(7624, \"Riach�o do Dantas\", 25),\r\n" + 
					"(7625, \"Riach�o do Jacu�pe\", 5),\r\n" + 
					"(7626, \"Riach�o do Po�o\", 15),\r\n" + 
					"(7627, \"Riach�o do Utinga (Utinga)\", 5),\r\n" + 
					"(7628, \"Riach�o (Junqueiro)\", 2),\r\n" + 
					"(7629, \"Riachinho\", 11),\r\n" + 
					"(7630, \"Riachinho\", 27),\r\n" + 
					"(7631, \"Riacho da Cruz\", 20),\r\n" + 
					"(7632, \"Riacho da Cruz (Janu�ria)\", 11),\r\n" + 
					"(7633, \"Riacho da Guia (Alagoinhas)\", 5),\r\n" + 
					"(7634, \"Riacho das Almas\", 16),\r\n" + 
					"(7635, \"Riacho de Santana\", 5),\r\n" + 
					"(7636, \"Riacho de Santana\", 20),\r\n" + 
					"(7637, \"Riacho de Santo Ant�nio\", 15),\r\n" + 
					"(7638, \"Riacho do Meio (Arcoverde)\", 16),\r\n" + 
					"(7639, \"Riacho do Meio (S�o Jos� do Egito)\", 16),\r\n" + 
					"(7640, \"Riacho dos Cavalos\", 15),\r\n" + 
					"(7641, \"Riacho do Sert�o (Major Isidoro)\", 2),\r\n" + 
					"(7642, \"Riacho dos Machados\", 11),\r\n" + 
					"(7643, \"Riacho Fechado (Tacaimb�)\", 16),\r\n" + 
					"(7644, \"Riacho Frio\", 17),\r\n" + 
					"(7645, \"Riacho Fundo de Cima (Palmeira dos �ndios)\", 2),\r\n" + 
					"(7646, \"Riacho Grande (Araripe)\", 6),\r\n" + 
					"(7647, \"Riacho Pequeno (Bel�m do S�o Francisco)\", 16),\r\n" + 
					"(7648, \"Riacho Seco (Cura��)\", 5),\r\n" + 
					"(7649, \"Riacho Verde (Quixad�)\", 6),\r\n" + 
					"(7650, \"Riacho Verde (V�rzea Alegre)\", 6),\r\n" + 
					"(7651, \"Riacho Vermelho (Iguatu)\", 6),\r\n" + 
					"(7652, \"Riachuelo\", 25),\r\n" + 
					"(7653, \"Riachuelo\", 20),\r\n" + 
					"(7654, \"Rialma\", 9),\r\n" + 
					"(7655, \"Rian�polis\", 9),\r\n" + 
					"(7656, \"Ribamar Fiquene\", 10),\r\n" + 
					"(7657, \"Ribas do Rio Pardo\", 12),\r\n" + 
					"(7658, \"Ribeira\", 26),\r\n" + 
					"(7659, \"Ribeira do Amparo\", 5),\r\n" + 
					"(7660, \"Ribeira do Piau�\", 17),\r\n" + 
					"(7661, \"Ribeira do Pombal\", 5),\r\n" + 
					"(7662, \"Ribeir�o\", 16),\r\n" + 
					"(7663, \"Ribeir�o Azul (S�o Francisco do Maranh�o)\", 10),\r\n" + 
					"(7664, \"Ribeir�o Bonito\", 26),\r\n" + 
					"(7665, \"Ribeir�o Bonito (Grandes Rios)\", 18),\r\n" + 
					"(7666, \"Ribeir�o Branco\", 26),\r\n" + 
					"(7667, \"Ribeir�o Cascalheira\", 13),\r\n" + 
					"(7668, \"Ribeir�o Claro\", 18),\r\n" + 
					"(7669, \"Ribeir�o Corrente\", 26),\r\n" + 
					"(7670, \"Ribeir�o da Folha (Minas Novas)\", 11),\r\n" + 
					"(7671, \"Ribeir�o das Neves\", 11),\r\n" + 
					"(7672, \"Ribeir�o de S�o Domingos (Santa Margarida)\", 11),\r\n" + 
					"(7673, \"Ribeir�o do Cristo (Alfredo Chaves)\", 8),\r\n" + 
					"(7674, \"Ribeir�o do Largo\", 5),\r\n" + 
					"(7675, \"Ribeir�o do Pinhal\", 18),\r\n" + 
					"(7676, \"Ribeir�o do Pinheiro (Castro)\", 18),\r\n" + 
					"(7677, \"Ribeir�o do Salto (Itarantim)\", 5),\r\n" + 
					"(7678, \"Ribeir�o dos Cocais (Nossa Senhora do Livramento)\", 13),\r\n" + 
					"(7679, \"Ribeir�o dos �ndios\", 26),\r\n" + 
					"(7680, \"Ribeir�o do Sul\", 26),\r\n" + 
					"(7681, \"Ribeir�o Grande\", 26),\r\n" + 
					"(7682, \"Ribeir�o Pequeno (Laguna)\", 24),\r\n" + 
					"(7683, \"Ribeir�o Pires\", 26),\r\n" + 
					"(7684, \"Ribeir�o Preto\", 26),\r\n" + 
					"(7685, \"Ribeir�o Vermelho\", 11),\r\n" + 
					"(7686, \"Ribeir�ozinho\", 13),\r\n" + 
					"(7687, \"Ribeira (Santa Rita)\", 15),\r\n" + 
					"(7688, \"Ribeiro dos Santos (Ol�mpia)\", 26),\r\n" + 
					"(7689, \"Ribeiro do Vale (Guararapes)\", 26),\r\n" + 
					"(7690, \"Ribeiro Gon�alves\", 17),\r\n" + 
					"(7691, \"Ribeiro Junqueira (Leopoldina)\", 11),\r\n" + 
					"(7692, \"Ribeir�polis\", 25),\r\n" + 
					"(7693, \"Ribeiros (S�o Gon�alo do Sapuca�)\", 11),\r\n" + 
					"(7694, \"Rifaina\", 26),\r\n" + 
					"(7695, \"Rinar� (Banabui�)\", 6),\r\n" + 
					"(7696, \"Rinc�o\", 26),\r\n" + 
					"(7697, \"Rinc�o Comprido (Porto Xavier)\", 23),\r\n" + 
					"(7698, \"Rinc�o Comprido (Tunas)\", 23),\r\n" + 
					"(7699, \"Rinc�o da Cruz (Pelotas)\", 23),\r\n" + 
					"(7700, \"Rinc�o da Estrela (Estrela Velha)\", 23),\r\n" + 
					"(7701, \"Rinc�o Del Rei (Rio Pardo)\", 23),\r\n" + 
					"(7702, \"Rinc�o de S�o Miguel (Alegrete)\", 23),\r\n" + 
					"(7703, \"Rinc�o de S�o Pedro (S�o Luiz Gonzaga)\", 23),\r\n" + 
					"(7704, \"Rinc�o do Appel (Pinhal Grande)\", 23),\r\n" + 
					"(7705, \"Rinc�o Doce (Santo Ant�nio do Planalto)\", 23),\r\n" + 
					"(7706, \"Rinc�o do Crist�v�o Pereira (Mostardas)\", 23),\r\n" + 
					"(7707, \"Rinc�o do Iva� (Salto do Jacu�)\", 23),\r\n" + 
					"(7708, \"Rinc�o do Meio (S�o Borja)\", 23),\r\n" + 
					"(7709, \"Rinc�o do Segredo (Almirante Tamandar� do Sul)\", 23),\r\n" + 
					"(7710, \"Rinc�o dos Kroeff (S�o Francisco de Paula)\", 23),\r\n" + 
					"(7711, \"Rinc�o dos Mellos (Giru�)\", 23),\r\n" + 
					"(7712, \"Rinc�o dos Mendes (Santo �ngelo)\", 23),\r\n" + 
					"(7713, \"Rinc�o dos Meoti (Santo �ngelo)\", 23),\r\n" + 
					"(7714, \"Rinc�o dos Moraes (S�o Miguel das Miss�es)\", 23),\r\n" + 
					"(7715, \"Rinc�o dos Paivas (Santo Augusto)\", 23),\r\n" + 
					"(7716, \"Rincao dos Pintos (S�o Luiz Gonzaga)\", 23),\r\n" + 
					"(7717, \"Rinc�o dos Roratos (Santo �ngelo)\", 23),\r\n" + 
					"(7718, \"Rinc�o dos Toledos (Campos Borges)\", 23),\r\n" + 
					"(7719, \"Rinc�o Maciel (Giru�)\", 23),\r\n" + 
					"(7720, \"Rinc�o Vermelho (Roque Gonzales)\", 23),\r\n" + 
					"(7721, \"Rin�polis\", 26),\r\n" + 
					"(7722, \"Rio Abaixo (Castro)\", 18),\r\n" + 
					"(7723, \"Rio Acima\", 11),\r\n" + 
					"(7724, \"Rio Antinha (Petrol�ndia)\", 24),\r\n" + 
					"(7725, \"Rio Azul\", 18),\r\n" + 
					"(7726, \"Rio Azul (Aratiba)\", 23),\r\n" + 
					"(7727, \"Rio Bananal\", 8),\r\n" + 
					"(7728, \"Rio Bom\", 18),\r\n" + 
					"(7729, \"Rio Bonito\", 19),\r\n" + 
					"(7730, \"Rio Bonito (Coqueiros do Sul)\", 23),\r\n" + 
					"(7731, \"Rio Bonito do Igua�u\", 18),\r\n" + 
					"(7732, \"Rio Bonito (Francisco Alves)\", 18),\r\n" + 
					"(7733, \"Rio Bonito (Ituporanga)\", 24),\r\n" + 
					"(7734, \"Rio Branco\", 13),\r\n" + 
					"(7735, \"Rio Branco\", 1),\r\n" + 
					"(7736, \"Rio Branco do Iva�\", 18),\r\n" + 
					"(7737, \"Rio Branco do Sul\", 18),\r\n" + 
					"(7738, \"Rio Branco (Nova Prata)\", 23),\r\n" + 
					"(7739, \"Rio Brilhante\", 12),\r\n" + 
					"(7740, \"Rio Casca\", 11),\r\n" + 
					"(7741, \"Rio Claro\", 26),\r\n" + 
					"(7742, \"Rio Claro\", 19),\r\n" + 
					"(7743, \"Rio Claro do Sul (Mallet)\", 18),\r\n" + 
					"(7744, \"Rio Crespo\", 21),\r\n" + 
					"(7745, \"Rio da Anta (Itai�polis)\", 24),\r\n" + 
					"(7746, \"Rio da Barra (Sert�nia)\", 16),\r\n" + 
					"(7747, \"Rio da Concei��o\", 27),\r\n" + 
					"(7748, \"Rio da Dona (Concei��o do Almeida)\", 5),\r\n" + 
					"(7749, \"Rio da Ilha (Taquara)\", 23),\r\n" + 
					"(7750, \"Rio da Prata (Nova Laranjeiras)\", 18),\r\n" + 
					"(7751, \"Rio da Prata (Santa Izabel do Oeste)\", 18),\r\n" + 
					"(7752, \"Rio das Antas\", 24),\r\n" + 
					"(7753, \"Rio das Antas (Cascavel)\", 18),\r\n" + 
					"(7754, \"Rio das Flores\", 19),\r\n" + 
					"(7755, \"Rio das Furnas (Orleans)\", 24),\r\n" + 
					"(7756, \"Rio das Mortes (Guarapuava)\", 18),\r\n" + 
					"(7757, \"Rio das Mortes (S�o Jo�o Del Rei)\", 11),\r\n" + 
					"(7758, \"Rio das Ostras\", 19),\r\n" + 
					"(7759, \"Rio das Pedras\", 26),\r\n" + 
					"(7760, \"Rio das Pedras (Guarapuava)\", 18),\r\n" + 
					"(7761, \"Rio das Pedras (Paranagu�)\", 18),\r\n" + 
					"(7762, \"Rio das Pombas (Paranagu�)\", 18),\r\n" + 
					"(7763, \"Rio de Contas\", 5),\r\n" + 
					"(7764, \"Rio de Janeiro\", 19),\r\n" + 
					"(7765, \"Rio do Ant�nio\", 5),\r\n" + 
					"(7766, \"Rio do Bra�o (Ilh�us)\", 5),\r\n" + 
					"(7767, \"Rio do Campo\", 24),\r\n" + 
					"(7768, \"Rio Doce\", 11),\r\n" + 
					"(7769, \"Rio do Fogo\", 20),\r\n" + 
					"(7770, \"Rio do Mato (Francisco Beltr�o)\", 18),\r\n" + 
					"(7771, \"Rio do Meio (Itoror�)\", 5),\r\n" + 
					"(7772, \"Rio do Oeste\", 24),\r\n" + 
					"(7773, \"Rio do Pires\", 5),\r\n" + 
					"(7774, \"Rio do Prado\", 11),\r\n" + 
					"(7775, \"Rio do Salto (Cascavel)\", 18),\r\n" + 
					"(7776, \"Rio dos Bichos (Arapiraca)\", 2),\r\n" + 
					"(7777, \"Rio dos Bois\", 27),\r\n" + 
					"(7778, \"Rio dos Bugres (Ituporanga)\", 24),\r\n" + 
					"(7779, \"Rio dos Cedros\", 24),\r\n" + 
					"(7780, \"Rio dos �ndios\", 23),\r\n" + 
					"(7781, \"Rio do Sul\", 24),\r\n" + 
					"(7782, \"Rio do Terra (Tr�s Cachoeiras)\", 23),\r\n" + 
					"(7783, \"Rio D'Una (Imaru�)\", 24),\r\n" + 
					"(7784, \"Rio Espera\", 11),\r\n" + 
					"(7785, \"Rio Formoso\", 16),\r\n" + 
					"(7786, \"Rio Fortuna\", 24),\r\n" + 
					"(7787, \"Rio Fundo (Terra Nova)\", 5),\r\n" + 
					"(7788, \"Rio Grande\", 23),\r\n" + 
					"(7789, \"Rio Grande da Serra\", 26),\r\n" + 
					"(7790, \"Rio Grande do Piau�\", 17),\r\n" + 
					"(7791, \"Riol�ndia\", 26),\r\n" + 
					"(7792, \"Riol�ndia (Nova Brasil�ndia)\", 13),\r\n" + 
					"(7793, \"Rio Largo\", 2),\r\n" + 
					"(7794, \"Rio Manso\", 11),\r\n" + 
					"(7795, \"Rio Manso (Nova Brasil�ndia)\", 13),\r\n" + 
					"(7796, \"Rio Maria\", 14),\r\n" + 
					"(7797, \"Rio Melo (Rio Espera)\", 11),\r\n" + 
					"(7798, \"Rio Muqui (Itapemirim)\", 8),\r\n" + 
					"(7799, \"Rio Negrinho\", 24),\r\n" + 
					"(7800, \"Rio Negro\", 12),\r\n" + 
					"(7801, \"Rio Negro\", 18),\r\n" + 
					"(7802, \"Rio Novo\", 11),\r\n" + 
					"(7803, \"Rio Novo (Cascavel)\", 18),\r\n" + 
					"(7804, \"Rio Novo do Sul\", 8),\r\n" + 
					"(7805, \"Rio Novo (Reserva)\", 18),\r\n" + 
					"(7806, \"Rio Parana�ba\", 11),\r\n" + 
					"(7807, \"Rio Pardinho (Santa Cruz do Sul)\", 23),\r\n" + 
					"(7808, \"Rio Pardo\", 23),\r\n" + 
					"(7809, \"Rio Pardo de Minas\", 11),\r\n" + 
					"(7810, \"Rio Pinheiro (Mari�polis)\", 18),\r\n" + 
					"(7811, \"Rio Piracicaba\", 11),\r\n" + 
					"(7812, \"Rio Pomba\", 11),\r\n" + 
					"(7813, \"Rio Pretinho (Te�filo Otoni)\", 11),\r\n" + 
					"(7814, \"Rio Preto\", 11),\r\n" + 
					"(7815, \"Rio Preto da Eva\", 3),\r\n" + 
					"(7816, \"Rio Preto do Sul (Mafra)\", 24),\r\n" + 
					"(7817, \"Rio Preto (Nova Ven�cia)\", 8),\r\n" + 
					"(7818, \"Rio Quatorze (Francisco Beltr�o)\", 18),\r\n" + 
					"(7819, \"Rio Quente\", 9),\r\n" + 
					"(7820, \"Rio Real\", 5),\r\n" + 
					"(7821, \"Rio Rufino\", 24),\r\n" + 
					"(7822, \"Rio Saltinho (Cascavel)\", 18),\r\n" + 
					"(7823, \"Rio Saudade (Francisco Beltr�o)\", 18),\r\n" + 
					"(7824, \"Rio Sono\", 27),\r\n" + 
					"(7825, \"Rio Telha (Ibia��)\", 23),\r\n" + 
					"(7826, \"Rio Tigre (Sananduva)\", 23),\r\n" + 
					"(7827, \"Rio Tinto\", 15),\r\n" + 
					"(7828, \"Rio Toldo (Get�lio Vargas)\", 23),\r\n" + 
					"(7829, \"Rio Verde\", 9),\r\n" + 
					"(7830, \"Rio Verde de Mato Grosso\", 12),\r\n" + 
					"(7831, \"Rio Verde (Juranda)\", 18),\r\n" + 
					"(7832, \"Rio Vermelho\", 11),\r\n" + 
					"(7833, \"Rio Vermelho (Xinguara)\", 14),\r\n" + 
					"(7834, \"Rio XV de Baixo (Pitanga)\", 18),\r\n" + 
					"(7835, \"Riozinho\", 23),\r\n" + 
					"(7836, \"Riozinho (Cacoal)\", 21),\r\n" + 
					"(7837, \"Riozinho (Itaituba)\", 14),\r\n" + 
					"(7838, \"Riqueza\", 24),\r\n" + 
					"(7839, \"Rit�polis\", 11),\r\n" + 
					"(7840, \"Rive (Alegre)\", 8),\r\n" + 
					"(7841, \"Riverl�ndia (Rio Verde)\", 9),\r\n" + 
					"(7842, \"Riversul\", 26),\r\n" + 
					"(7843, \"Roberto (Pindorama)\", 26),\r\n" + 
					"(7844, \"Roberto Silveira (Umuarama)\", 18),\r\n" + 
					"(7845, \"Rocado (Pastos Bons)\", 10),\r\n" + 
					"(7846, \"Ro�a Grande (S�o Jo�o Nepomuceno)\", 11),\r\n" + 
					"(7847, \"Roca Sales\", 23),\r\n" + 
					"(7848, \"Ro�as Novas (Caet�)\", 11),\r\n" + 
					"(7849, \"Ro�a Velha (Ponta Grossa)\", 18),\r\n" + 
					"(7850, \"Ro�a Velha (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(7851, \"Rocha Cavalcante (Uni�o dos Palmares)\", 2),\r\n" + 
					"(7852, \"Rochedinho (Campo Grande)\", 12),\r\n" + 
					"(7853, \"Rochedo\", 12),\r\n" + 
					"(7854, \"Rochedo de Minas\", 11),\r\n" + 
					"(7855, \"Roda Velha (S�o Desid�rio)\", 5),\r\n" + 
					"(7856, \"Rodeador (Monjolos)\", 11),\r\n" + 
					"(7857, \"Rodeio\", 24),\r\n" + 
					"(7858, \"Rodeio Bonito\", 23),\r\n" + 
					"(7859, \"Rodeiro\", 11),\r\n" + 
					"(7860, \"Rodelas\", 5),\r\n" + 
					"(7861, \"Rodolfo Fernandes\", 20),\r\n" + 
					"(7862, \"Rodrigo Silva (Ouro Preto)\", 11),\r\n" + 
					"(7863, \"Rodrigues Alves\", 1),\r\n" + 
					"(7864, \"Rolador\", 23),\r\n" + 
					"(7865, \"Rol�ndia\", 18),\r\n" + 
					"(7866, \"Rolante\", 23),\r\n" + 
					"(7867, \"Rolantinho da Figueira (Rolante)\", 23),\r\n" + 
					"(7868, \"Rold�o (Morada Nova)\", 6),\r\n" + 
					"(7869, \"Rolim de Moura\", 21),\r\n" + 
					"(7870, \"Romaria\", 11),\r\n" + 
					"(7871, \"Romel�ndia\", 24),\r\n" + 
					"(7872, \"Rome�polis (Arapu�)\", 18),\r\n" + 
					"(7873, \"Roncador\", 18),\r\n" + 
					"(7874, \"Ronda Alta\", 23),\r\n" + 
					"(7875, \"Rondinha\", 23),\r\n" + 
					"(7876, \"Rondinha (Guarapuava)\", 18),\r\n" + 
					"(7877, \"Rondinha (Paula Freitas)\", 18),\r\n" + 
					"(7878, \"Rondol�ndia\", 13),\r\n" + 
					"(7879, \"Rondon\", 18),\r\n" + 
					"(7880, \"Rondon do Par�\", 14),\r\n" + 
					"(7881, \"Rondon�polis\", 13),\r\n" + 
					"(7882, \"Roque Gonzales\", 23),\r\n" + 
					"(7883, \"Roque (Pinheiro)\", 10),\r\n" + 
					"(7884, \"Rorain�polis\", 22),\r\n" + 
					"(7885, \"Rosal�ndia (S�o Lu�s de Montes Belos)\", 9),\r\n" + 
					"(7886, \"Rosal (Bom Jesus do Itabapoana)\", 19),\r\n" + 
					"(7887, \"Rosana\", 26),\r\n" + 
					"(7888, \"Ros�rio\", 10),\r\n" + 
					"(7889, \"Ros�rio (Augusto Pestana)\", 23),\r\n" + 
					"(7890, \"Ros�rio da Limeira\", 11),\r\n" + 
					"(7891, \"Ros�rio de Minas (Juiz de Fora)\", 11),\r\n" + 
					"(7892, \"Ros�rio do Catete\", 25),\r\n" + 
					"(7893, \"Ros�rio do Iva�\", 18),\r\n" + 
					"(7894, \"Ros�rio do Pontal (Ponte Nova)\", 11),\r\n" + 
					"(7895, \"Ros�rio do Sul\", 23),\r\n" + 
					"(7896, \"Ros�rio (Jo�o Dias)\", 20),\r\n" + 
					"(7897, \"Ros�rio (Milagres)\", 6),\r\n" + 
					"(7898, \"Ros�rio Oeste\", 13),\r\n" + 
					"(7899, \"Roseira\", 26),\r\n" + 
					"(7900, \"Roseiral (Mutum)\", 11),\r\n" + 
					"(7901, \"Roteiro\", 2),\r\n" + 
					"(7902, \"Rua Nova (Bel�m)\", 15),\r\n" + 
					"(7903, \"Rubelita\", 11),\r\n" + 
					"(7904, \"Rubi�cea\", 26),\r\n" + 
					"(7905, \"Rubiataba\", 9),\r\n" + 
					"(7906, \"Rubim\", 11),\r\n" + 
					"(7907, \"Rubin�ia\", 26),\r\n" + 
					"(7908, \"Ruil�ndia (Mirassol)\", 26),\r\n" + 
					"(7909, \"Ruralminas (Una�)\", 11),\r\n" + 
					"(7910, \"Rur�polis\", 14),\r\n" + 
					"(7911, \"Russas\", 6),\r\n" + 
					"(7912, \"Ruy Barbosa\", 5),\r\n" + 
					"(7913, \"Ruy Barbosa\", 20),\r\n" + 
					"(7914, \"Sabar�\", 11),\r\n" + 
					"(7915, \"Sab�udia\", 18),\r\n" + 
					"(7916, \"Sabiaguaba (Amontada)\", 6),\r\n" + 
					"(7917, \"Sabino\", 26),\r\n" + 
					"(7918, \"Sabin�polis\", 11),\r\n" + 
					"(7919, \"Saboeiro\", 6),\r\n" + 
					"(7920, \"Sacra Fam�lia do Tingu� (Engenheiro Paulo de Frontin)\", 19),\r\n" + 
					"(7921, \"Sacramento\", 11),\r\n" + 
					"(7922, \"Sacramento (Ipaporanga)\", 6),\r\n" + 
					"(7923, \"Sagrada Fam�lia\", 23),\r\n" + 
					"(7924, \"Sagrada Fam�lia (Alfredo Chaves)\", 8),\r\n" + 
					"(7925, \"Sagrada Fam�lia (Planalto)\", 18),\r\n" + 
					"(7926, \"Sagres\", 26),\r\n" + 
					"(7927, \"Saic� (Cacequi)\", 23),\r\n" + 
					"(7928, \"Sair�\", 16),\r\n" + 
					"(7929, \"Sai (S�o Francisco do Sul)\", 24),\r\n" + 
					"(7930, \"Sal�o (Senador S�)\", 6),\r\n" + 
					"(7931, \"Saldanha Marinho\", 23),\r\n" + 
					"(7932, \"Saldanha (Pil�o Arcado)\", 5),\r\n" + 
					"(7933, \"Salema (Rio Tinto)\", 15),\r\n" + 
					"(7934, \"Sales\", 26),\r\n" + 
					"(7935, \"Sales Oliveira\", 26),\r\n" + 
					"(7936, \"Sales�polis\", 26),\r\n" + 
					"(7937, \"Salete\", 24),\r\n" + 
					"(7938, \"Salgad�lia (Concei��o do Coit�)\", 5),\r\n" + 
					"(7939, \"Salgadinho\", 16),\r\n" + 
					"(7940, \"Salgadinho\", 15),\r\n" + 
					"(7941, \"Salgado\", 25),\r\n" + 
					"(7942, \"Salgado de S�o F�lix\", 15),\r\n" + 
					"(7943, \"Salgado dos Machados (Sobral)\", 6),\r\n" + 
					"(7944, \"Salgado Filho\", 18),\r\n" + 
					"(7945, \"Salgueiro\", 16),\r\n" + 
					"(7946, \"Salinas\", 11),\r\n" + 
					"(7947, \"Salinas da Margarida\", 5),\r\n" + 
					"(7948, \"Salin�polis\", 14),\r\n" + 
					"(7949, \"Salitre\", 6),\r\n" + 
					"(7950, \"Salitre de Minas (Patroc�nio)\", 11),\r\n" + 
					"(7951, \"Salles de Oliveira (Campina da Lagoa)\", 18),\r\n" + 
					"(7952, \"Salmour�o\", 26),\r\n" + 
					"(7953, \"Salo�\", 16),\r\n" + 
					"(7954, \"Salobro (Canarana)\", 5),\r\n" + 
					"(7955, \"Salobro (Pesqueira)\", 16),\r\n" + 
					"(7956, \"Saltinho\", 26),\r\n" + 
					"(7957, \"Saltinho\", 24),\r\n" + 
					"(7958, \"Saltinho do Oeste (Alto Piquiri)\", 18),\r\n" + 
					"(7959, \"Saltinho (Itatiba do Sul)\", 23),\r\n" + 
					"(7960, \"Saltinho (Realeza)\", 18),\r\n" + 
					"(7961, \"Saltinho (Rodeio Bonito)\", 23),\r\n" + 
					"(7962, \"Salto\", 26),\r\n" + 
					"(7963, \"Salto da Divisa\", 11),\r\n" + 
					"(7964, \"Salto de Pirapora\", 26),\r\n" + 
					"(7965, \"Salto do Avanhandava (Jos� Bonif�cio)\", 26),\r\n" + 
					"(7966, \"Salto do C�u\", 13),\r\n" + 
					"(7967, \"Salto do Itarar�\", 18),\r\n" + 
					"(7968, \"Salto do Jacu�\", 23),\r\n" + 
					"(7969, \"Salto do Lontra\", 18),\r\n" + 
					"(7970, \"Salto Grande\", 26),\r\n" + 
					"(7971, \"Salto (Iju�)\", 23),\r\n" + 
					"(7972, \"Salto Port�o (Cascavel)\", 18),\r\n" + 
					"(7973, \"Salto Veloso\", 24),\r\n" + 
					"(7974, \"Salutaris (Para�ba do Sul)\", 19),\r\n" + 
					"(7975, \"Salvador\", 5),\r\n" + 
					"(7976, \"Salvador das Miss�es\", 23),\r\n" + 
					"(7977, \"Salvador do Sul\", 23),\r\n" + 
					"(7978, \"Salvaterra\", 14),\r\n" + 
					"(7979, \"Salva Vida (Martins)\", 20),\r\n" + 
					"(7980, \"Samambaia (Tobias Barreto)\", 25),\r\n" + 
					"(7981, \"Samba�ba\", 10),\r\n" + 
					"(7982, \"Samba�ba (Granja)\", 6),\r\n" + 
					"(7983, \"Samba�ba (Itapicuru)\", 5),\r\n" + 
					"(7984, \"Sampaio\", 27),\r\n" + 
					"(7985, \"Sampaio Correia (Saquarema)\", 19),\r\n" + 
					"(7986, \"Sampaio (Mato Leit�o)\", 23),\r\n" + 
					"(7987, \"Sana (Maca�)\", 19),\r\n" + 
					"(7988, \"Sananduva\", 23),\r\n" + 
					"(7989, \"Sanat�rio Santa F� (Tr�s Cora��es)\", 11),\r\n" + 
					"(7990, \"Sanclerl�ndia\", 9),\r\n" + 
					"(7991, \"Sandol�ndia\", 27),\r\n" + 
					"(7992, \"Sandovalina\", 26),\r\n" + 
					"(7993, \"Sanga Funda (Terra de Areia)\", 23),\r\n" + 
					"(7994, \"Sang�o\", 24),\r\n" + 
					"(7995, \"Sanga Puit� (Ponta Por�)\", 12),\r\n" + 
					"(7996, \"Sangradouro (Barra do Gar�as)\", 13),\r\n" + 
					"(7997, \"Sanhar�\", 16),\r\n" + 
					"(7998, \"Santa Ad�lia\", 26),\r\n" + 
					"(7999, \"Santa Albertina\", 26),\r\n" + 
					"(8000, \"Santa Am�lia\", 18),\r\n" + 
					"(8001, \"Santa Am�rica (Getulina)\", 26),\r\n" + 
					"(8002, \"Santa Ana (Pacoti)\", 6),\r\n" + 
					"(8003, \"Santa Ang�lica (Alegre)\", 8),\r\n" + 
					"(8004, \"Santa B�rbara\", 5),\r\n" + 
					"(8005, \"Santa B�rbara\", 11),\r\n" + 
					"(8006, \"Santa B�rbara (Ca�apava do Sul)\", 23),\r\n" + 
					"(8007, \"Santa B�rbara de Goi�s\", 9),\r\n" + 
					"(8008, \"Santa B�rbara D'Oeste\", 26),\r\n" + 
					"(8009, \"Santa B�rbara do Leste\", 11),\r\n" + 
					"(8010, \"Santa B�rbara do Monte Verde\", 11),\r\n" + 
					"(8011, \"Santa B�rbara do Par�\", 14),\r\n" + 
					"(8012, \"Santa B�rbara do Sul\", 23),\r\n" + 
					"(8013, \"Santa B�rbara do Tug�rio\", 11),\r\n" + 
					"(8014, \"Santa B�rbara (Encruzilhada do Sul)\", 23),\r\n" + 
					"(8015, \"Santa B�rbara (Ivaipor�)\", 18),\r\n" + 
					"(8016, \"Santa B�rbara (S�o Valentim do Sul)\", 23),\r\n" + 
					"(8017, \"Santa Branca\", 26),\r\n" + 
					"(8018, \"Santa Br�gida\", 5),\r\n" + 
					"(8019, \"Santa Carmem\", 13),\r\n" + 
					"(8020, \"Santa Catarina (Salvador das Miss�es)\", 23),\r\n" + 
					"(8021, \"Santa Cec�lia\", 24),\r\n" + 
					"(8022, \"Santa Cec�lia\", 15),\r\n" + 
					"(8023, \"Santa Cec�lia (Cant�)\", 22),\r\n" + 
					"(8024, \"Santa Cec�lia do Pav�o\", 18),\r\n" + 
					"(8025, \"Santa Cec�lia do Sul\", 23),\r\n" + 
					"(8026, \"Santa Clara (Castro)\", 18),\r\n" + 
					"(8027, \"Santa Clara D'Oeste\", 26),\r\n" + 
					"(8028, \"Santa Clara do Inga� (Quinze de Novembro)\", 23),\r\n" + 
					"(8029, \"Santa Clara do Monte Cristo (Vila Bela da Sant�ssima Trindade)\", 13),\r\n" + 
					"(8030, \"Santa Clara do Sul\", 23),\r\n" + 
					"(8031, \"Santa Clara (Porci�ncula)\", 19),\r\n" + 
					"(8032, \"Santa Cristina do Pinhal (Parob�)\", 23),\r\n" + 
					"(8033, \"Santa Cruz\", 15),\r\n" + 
					"(8034, \"Santa Cruz\", 20),\r\n" + 
					"(8035, \"Santa Cruz\", 16),\r\n" + 
					"(8036, \"Santa Cruz Cabr�lia\", 5),\r\n" + 
					"(8037, \"Santa Cruz (Campo Largo)\", 18),\r\n" + 
					"(8038, \"Santa Cruz da Aparecida (Monte Belo)\", 11),\r\n" + 
					"(8039, \"Santa Cruz da Baixa Verde\", 16),\r\n" + 
					"(8040, \"Santa Cruz da Concei��o\", 26),\r\n" + 
					"(8041, \"Santa Cruz da Conc�rdia (Taquara)\", 23),\r\n" + 
					"(8042, \"Santa Cruz da Esperan�a\", 26),\r\n" + 
					"(8043, \"Santa Cruz da Estrela (Santa Rita do Passa Quatro)\", 26),\r\n" + 
					"(8044, \"Santa Cruz das Lajes (Santo Ant�nio da Barra)\", 9),\r\n" + 
					"(8045, \"Santa Cruz das Palmeiras\", 26),\r\n" + 
					"(8046, \"Santa Cruz da Vit�ria\", 5),\r\n" + 
					"(8047, \"Santa Cruz de Botumirim (Botumirim)\", 11),\r\n" + 
					"(8048, \"Santa Cruz de Goi�s\", 9),\r\n" + 
					"(8049, \"Santa Cruz de Irupi (Irupi)\", 8),\r\n" + 
					"(8050, \"Santa Cruz de Minas\", 11),\r\n" + 
					"(8051, \"Santa Cruz de Monte Castelo\", 18),\r\n" + 
					"(8052, \"Santa Cruz de Salinas\", 11),\r\n" + 
					"(8053, \"Santa Cruz do Arari\", 14),\r\n" + 
					"(8054, \"Santa Cruz do Banabui� (Pedra Branca)\", 6),\r\n" + 
					"(8055, \"Santa Cruz do Capibaribe\", 16),\r\n" + 
					"(8056, \"Santa Cruz do Escalvado\", 11),\r\n" + 
					"(8057, \"Santa Cruz do Piau�\", 17),\r\n" + 
					"(8058, \"Santa Cruz do Prata (Guaran�sia)\", 11),\r\n" + 
					"(8059, \"Santa Cruz do Rio Pardo\", 26),\r\n" + 
					"(8060, \"Santa Cruz dos Lopes (Itarar�)\", 26),\r\n" + 
					"(8061, \"Santa Cruz dos Milagres\", 17),\r\n" + 
					"(8062, \"Santa Cruz do Sul\", 23),\r\n" + 
					"(8063, \"Santa Cruz do Timb� (Porto Uni�o)\", 24),\r\n" + 
					"(8064, \"Santa Cruz do Xingu\", 13),\r\n" + 
					"(8065, \"Santa Cruz (Jaru)\", 21),\r\n" + 
					"(8066, \"Santa Cruz (Planalto)\", 23),\r\n" + 
					"(8067, \"Santa Cruz (Santo Ant�nio de P�dua)\", 19),\r\n" + 
					"(8068, \"Santa da Pedra (S�o Jo�o do Pacu�)\", 11),\r\n" + 
					"(8069, \"Santa Efig�nia (Capela)\", 2),\r\n" + 
					"(8070, \"Santa Efig�nia (Caratinga)\", 11),\r\n" + 
					"(8071, \"Santa Efig�nia de Minas\", 11),\r\n" + 
					"(8072, \"Santa Eliza (Umuarama)\", 18),\r\n" + 
					"(8073, \"Santa Elvira (Juscimeira)\", 13),\r\n" + 
					"(8074, \"Santa Em�lia (Ven�ncio Aires)\", 23),\r\n" + 
					"(8075, \"Santa Ernestina\", 26),\r\n" + 
					"(8076, \"Santa Esmeralda (Santa Cruz de Monte Castelo)\", 18),\r\n" + 
					"(8077, \"Santa Eud�xia (S�o Carlos)\", 26),\r\n" + 
					"(8078, \"Santa F�\", 18),\r\n" + 
					"(8079, \"Santa F� (Acara�)\", 6),\r\n" + 
					"(8080, \"Santa F� (Crato)\", 6),\r\n" + 
					"(8081, \"Santa F� de Goi�s\", 9),\r\n" + 
					"(8082, \"Santa F� de Minas\", 11),\r\n" + 
					"(8083, \"Santa F� do Araguaia\", 27),\r\n" + 
					"(8084, \"Santa F� do Pirap� (Marialva)\", 18),\r\n" + 
					"(8085, \"Santa F� do Sul\", 26),\r\n" + 
					"(8086, \"Santa F� (Jundi�)\", 20),\r\n" + 
					"(8087, \"Santa Fel�cia (Acopiara)\", 6),\r\n" + 
					"(8088, \"Santa F� (S�o Jos� dos Quatro Marcos)\", 13),\r\n" + 
					"(8089, \"Santa Filomena\", 16),\r\n" + 
					"(8090, \"Santa Filomena\", 17),\r\n" + 
					"(8091, \"Santa Filomena do Maranh�o\", 10),\r\n" + 
					"(8092, \"Santa Filomena (Santana do Manhua�u)\", 11),\r\n" + 
					"(8093, \"Santa Flora (Santa Maria)\", 23),\r\n" + 
					"(8094, \"Santa Gema (S�o Domingos do Sul)\", 23),\r\n" + 
					"(8095, \"Santa Gertrudes\", 26),\r\n" + 
					"(8096, \"Santa Gertrudes (Patos)\", 15),\r\n" + 
					"(8097, \"Santa Helena\", 10),\r\n" + 
					"(8098, \"Santa Helena\", 24),\r\n" + 
					"(8099, \"Santa Helena\", 18),\r\n" + 
					"(8100, \"Santa Helena\", 15),\r\n" + 
					"(8101, \"Santa Helena de Goi�s\", 9),\r\n" + 
					"(8102, \"Santa Helena de Minas\", 11),\r\n" + 
					"(8103, \"Santa Helena (Joa�aba)\", 24),\r\n" + 
					"(8104, \"Santa In�s\", 5),\r\n" + 
					"(8105, \"Santa In�s\", 15),\r\n" + 
					"(8106, \"Santa In�s\", 18),\r\n" + 
					"(8107, \"Santa In�s\", 10),\r\n" + 
					"(8108, \"Santa In�s (S�o Luiz Gonzaga)\", 23),\r\n" + 
					"(8109, \"Santa Isabel\", 9),\r\n" + 
					"(8110, \"Santa Isabel\", 26),\r\n" + 
					"(8111, \"Santa Isabel do Iva�\", 18),\r\n" + 
					"(8112, \"Santa Isabel do Marinheiro (Pedran�polis)\", 26),\r\n" + 
					"(8113, \"Santa Isabel do Par�\", 14),\r\n" + 
					"(8114, \"Santa Isabel do Rio Negro\", 3),\r\n" + 
					"(8115, \"Santa Isabel do Rio Preto (Valen�a)\", 19),\r\n" + 
					"(8116, \"Santa Izabel do Oeste\", 18),\r\n" + 
					"(8117, \"Santa Izabel do Sul (Arroio Grande)\", 23),\r\n" + 
					"(8118, \"Santa Izabel (S�o Francisco)\", 11),\r\n" + 
					"(8119, \"Santa Izabel (S�o Joaquim)\", 24),\r\n" + 
					"(8120, \"Santa Juliana\", 11),\r\n" + 
					"(8121, \"Santa J�lia (S�o Roque do Cana�)\", 8),\r\n" + 
					"(8122, \"Santa Leopoldina\", 8),\r\n" + 
					"(8123, \"Santa L�cia\", 18),\r\n" + 
					"(8124, \"Santa L�cia\", 26),\r\n" + 
					"(8125, \"Santa L�cia (Alto Alegre)\", 23),\r\n" + 
					"(8126, \"Santa L�cia do Pia� (Caxias do Sul)\", 23),\r\n" + 
					"(8127, \"Santa L�cia (Iju�)\", 23),\r\n" + 
					"(8128, \"Santa L�cia (Ouro)\", 24),\r\n" + 
					"(8129, \"Santa L�cia (Palmitos)\", 24),\r\n" + 
					"(8130, \"Santa Lu�za (Carlos Barbosa)\", 23),\r\n" + 
					"(8131, \"Santa Lurdes (Camb�)\", 18),\r\n" + 
					"(8132, \"Santaluz\", 5),\r\n" + 
					"(8133, \"Santa Luz\", 17),\r\n" + 
					"(8134, \"Santa Luzia\", 15),\r\n" + 
					"(8135, \"Santa Luzia\", 11),\r\n" + 
					"(8136, \"Santa Luzia\", 10),\r\n" + 
					"(8137, \"Santa Luzia\", 5),\r\n" + 
					"(8138, \"Santa Luzia (Belo Jardim)\", 16),\r\n" + 
					"(8139, \"Santa Luzia (Caratinga)\", 11),\r\n" + 
					"(8140, \"Santa Luzia da Alvorada (S�o Jo�o do Iva�)\", 18),\r\n" + 
					"(8141, \"Santa Luzia da Serra (Jo�o Pinheiro)\", 11),\r\n" + 
					"(8142, \"Santa Luzia de Manten�polis (Manten�polis)\", 8),\r\n" + 
					"(8143, \"Santa Luzia do Azul (�gua Doce do Norte)\", 8),\r\n" + 
					"(8144, \"Santa Luzia do Cariri (Serra Branca)\", 15),\r\n" + 
					"(8145, \"Santa Luzia D'Oeste\", 21),\r\n" + 
					"(8146, \"Santa Luzia do Itanhy\", 25),\r\n" + 
					"(8147, \"Santa Luzia do Norte\", 2),\r\n" + 
					"(8148, \"Santa Luzia do Norte (Ecoporanga)\", 8),\r\n" + 
					"(8149, \"Santa Luzia do Pacu� (Macap�)\", 4),\r\n" + 
					"(8150, \"Santa Luzia do Par�\", 14),\r\n" + 
					"(8151, \"Santa Luzia do Paru�\", 10),\r\n" + 
					"(8152, \"Santa Luzia (Jaguaruana)\", 6),\r\n" + 
					"(8153, \"Santa Luzia (Lagoa Vermelha)\", 23),\r\n" + 
					"(8154, \"Santa Luzia (Os�rio)\", 23),\r\n" + 
					"(8155, \"Santa Luzia (Uruburetama)\", 6),\r\n" + 
					"(8156, \"Santa Margarida\", 11),\r\n" + 
					"(8157, \"Santa Margarida (Bela Vista do Para�so)\", 18),\r\n" + 
					"(8158, \"Santa Margarida do Sul\", 23),\r\n" + 
					"(8159, \"Santa Margarida (Londrina)\", 18),\r\n" + 
					"(8160, \"Santa Maria\", 23),\r\n" + 
					"(8161, \"Santa Maria\", 20),\r\n" + 
					"(8162, \"Santa Maria (Alto Paran�)\", 18),\r\n" + 
					"(8163, \"Santa Maria (Benedito Novo)\", 24),\r\n" + 
					"(8164, \"Santa Maria (Campos dos Goytacazes)\", 19),\r\n" + 
					"(8165, \"Santa Maria da Boa Vista\", 16),\r\n" + 
					"(8166, \"Santa Maria das Barreiras\", 14),\r\n" + 
					"(8167, \"Santa Maria da Serra\", 26),\r\n" + 
					"(8168, \"Santa Maria da Vit�ria\", 5),\r\n" + 
					"(8169, \"Santa Maria de Itabira\", 11),\r\n" + 
					"(8170, \"Santa Maria de Jetib�\", 8),\r\n" + 
					"(8171, \"Santa Maria de Marechal (Marechal Floriano)\", 8),\r\n" + 
					"(8172, \"Santa Maria do Cambuc�\", 16),\r\n" + 
					"(8173, \"Santa Maria do Gurup� (Promiss�o)\", 26),\r\n" + 
					"(8174, \"Santa Maria do Herval\", 23),\r\n" + 
					"(8175, \"Santa Maria do Oeste\", 18),\r\n" + 
					"(8176, \"Santa Maria do Par�\", 14),\r\n" + 
					"(8177, \"Santa Maria do Rio do Peixe (Congonhinhas)\", 18),\r\n" + 
					"(8178, \"Santa Maria do Salto\", 11),\r\n" + 
					"(8179, \"Santa Maria do Sua�u�\", 11),\r\n" + 
					"(8180, \"Santa Maria do Tocantins\", 27),\r\n" + 
					"(8181, \"Santa Maria (Macap�)\", 4),\r\n" + 
					"(8182, \"Santa Maria Madalena\", 19),\r\n" + 
					"(8183, \"Santa Maria (Maracan�)\", 14),\r\n" + 
					"(8184, \"Santa Mariana\", 18),\r\n" + 
					"(8185, \"Santa Maria (S�o Jo�o do Tigre)\", 15),\r\n" + 
					"(8186, \"Santa Marta (Ibitirama)\", 8),\r\n" + 
					"(8187, \"Santa Mercedes\", 26),\r\n" + 
					"(8188, \"Santa M�nica\", 18),\r\n" + 
					"(8189, \"Santana\", 4),\r\n" + 
					"(8190, \"Santana\", 5),\r\n" + 
					"(8191, \"Santana (Chapada)\", 23),\r\n" + 
					"(8192, \"Santana (Crate�s)\", 6),\r\n" + 
					"(8193, \"Santana (Cruz Machado)\", 18),\r\n" + 
					"(8194, \"Santana da Boa Vista\", 23),\r\n" + 
					"(8195, \"Santana da Ponte Pensa\", 26),\r\n" + 
					"(8196, \"Santana da Vargem\", 11),\r\n" + 
					"(8197, \"Santana de Caldas (Caldas)\", 11),\r\n" + 
					"(8198, \"Santana de Cataguases\", 11),\r\n" + 
					"(8199, \"Santana de Mangueira\", 15),\r\n" + 
					"(8200, \"Santana de Parna�ba\", 26),\r\n" + 
					"(8201, \"Santana de Patos (Patos de Minas)\", 11),\r\n" + 
					"(8202, \"Santana de Pirapama\", 11),\r\n" + 
					"(8203, \"Santana de S�o Joaquim (S�o Joaquim do Monte)\", 16),\r\n" + 
					"(8204, \"Santana do Acara�\", 6),\r\n" + 
					"(8205, \"Santana do Alfi� (S�o Domingos do Prata)\", 11),\r\n" + 
					"(8206, \"Santana do Ara�ua� (Ponto dos Volantes)\", 11),\r\n" + 
					"(8207, \"Santana do Araguaia\", 14),\r\n" + 
					"(8208, \"Santana do Campestre (Astolfo Dutra)\", 11),\r\n" + 
					"(8209, \"Santana do Capivari (Pouso Alto)\", 11),\r\n" + 
					"(8210, \"Santana do Cariri\", 6),\r\n" + 
					"(8211, \"Santana do Deserto\", 11),\r\n" + 
					"(8212, \"Santana do Garamb�u\", 11),\r\n" + 
					"(8213, \"Santana do Ipanema\", 2),\r\n" + 
					"(8214, \"Santana do Itarar�\", 18),\r\n" + 
					"(8215, \"Santana do Jacar�\", 11),\r\n" + 
					"(8216, \"Santana do Livramento\", 23),\r\n" + 
					"(8217, \"Santana do Manhua�u\", 11),\r\n" + 
					"(8218, \"Santana do Maranh�o\", 10),\r\n" + 
					"(8219, \"Santana do Matos\", 20),\r\n" + 
					"(8220, \"Santana do Munda�\", 2),\r\n" + 
					"(8221, \"Santana do Para�so\", 11),\r\n" + 
					"(8222, \"Santana do Paraopeba (Belo Vale)\", 11),\r\n" + 
					"(8223, \"Santana do Piau�\", 17),\r\n" + 
					"(8224, \"Santana do Riacho\", 11),\r\n" + 
					"(8225, \"Santana do S�o Francisco\", 25),\r\n" + 
					"(8226, \"Santana do Serid�\", 20),\r\n" + 
					"(8227, \"Santana dos Garrotes\", 15),\r\n" + 
					"(8228, \"Santana dos Montes\", 11),\r\n" + 
					"(8229, \"Santana do Sobrado (Casa Nova)\", 5),\r\n" + 
					"(8230, \"Santana do Tabuleiro (Raul Soares)\", 11),\r\n" + 
					"(8231, \"Santana (Erval Grande)\", 23),\r\n" + 
					"(8232, \"Santana (Iju�)\", 23),\r\n" + 
					"(8233, \"Santana (S�o Sebasti�o do Uatum�)\", 3),\r\n" + 
					"(8234, \"Santan�sia (Pira�)\", 19),\r\n" + 
					"(8235, \"Santaninha (Vila Rica)\", 13),\r\n" + 
					"(8236, \"Santan�polis\", 5),\r\n" + 
					"(8237, \"Santa Quit�ria\", 6),\r\n" + 
					"(8238, \"Santa Quit�ria (Campo Largo)\", 18),\r\n" + 
					"(8239, \"Santa Quit�ria (Castro)\", 18),\r\n" + 
					"(8240, \"Santa Quit�ria do Maranh�o\", 10),\r\n" + 
					"(8241, \"Santar�m\", 14),\r\n" + 
					"(8242, \"Santar�m Novo\", 14),\r\n" + 
					"(8243, \"Santar�m (Or�s)\", 6),\r\n" + 
					"(8244, \"Santa Rita\", 10),\r\n" + 
					"(8245, \"Santa Rita\", 15),\r\n" + 
					"(8246, \"Santa Rita (Castro)\", 18),\r\n" + 
					"(8247, \"Santa Rita (Chor�)\", 6),\r\n" + 
					"(8248, \"Santa Rita da Estrela (Estrela do Sul)\", 11),\r\n" + 
					"(8249, \"Santa Rita da Floresta (Cantagalo)\", 19),\r\n" + 
					"(8250, \"Santa Rita de Caldas\", 11),\r\n" + 
					"(8251, \"Santa Rita de C�ssia\", 5),\r\n" + 
					"(8252, \"Santa Rita de Jacutinga\", 11),\r\n" + 
					"(8253, \"Santa Rita de Minas\", 11),\r\n" + 
					"(8254, \"Santa Rita de Ouro Preto (Ouro Preto)\", 11),\r\n" + 
					"(8255, \"Santa Rita do Araguaia\", 9),\r\n" + 
					"(8256, \"Santa Rita do Cedro (Curvelo)\", 11),\r\n" + 
					"(8257, \"Santa Rita D'Oeste\", 26),\r\n" + 
					"(8258, \"Santa Rita do Ibitipoca\", 11),\r\n" + 
					"(8259, \"Santa Rita do Itueto\", 11),\r\n" + 
					"(8260, \"Santa Rita do Novo Destino\", 9),\r\n" + 
					"(8261, \"Santa Rita do Oeste (Terra Roxa)\", 18),\r\n" + 
					"(8262, \"Santa Rita do Pardo\", 12),\r\n" + 
					"(8263, \"Santa Rita do Passa Quatro\", 26),\r\n" + 
					"(8264, \"Santa Rita do Ribeira (Miracatu)\", 26),\r\n" + 
					"(8265, \"Santa Rita do Rio do Peixe (Ferros)\", 11),\r\n" + 
					"(8266, \"Santa Rita do Sapuca�\", 11),\r\n" + 
					"(8267, \"Santa Rita do Sul (Arambar�)\", 23),\r\n" + 
					"(8268, \"Santa Rita do Sul (Camaqu�)\", 23),\r\n" + 
					"(8269, \"Santa Rita do Tocantins\", 27),\r\n" + 
					"(8270, \"Santa Rita do Trivelato\", 13),\r\n" + 
					"(8271, \"Santa Rita Dur�o (Mariana)\", 11),\r\n" + 
					"(8272, \"Santa Rita (Muitos Cap�es)\", 23),\r\n" + 
					"(8273, \"Santa Rita (Nobres)\", 13),\r\n" + 
					"(8274, \"Santa Rita (Ouricuri)\", 16),\r\n" + 
					"(8275, \"Santa Rita (Pato Branco)\", 18),\r\n" + 
					"(8276, \"Santa Rita (Pe�anha)\", 11),\r\n" + 
					"(8277, \"Santa Rita (S�o Paulo de Oliven�a)\", 3),\r\n" + 
					"(8278, \"Santa Rita (Tuparetama)\", 16),\r\n" + 
					"(8279, \"Santa Rita (Uira�na)\", 15),\r\n" + 
					"(8280, \"Santa Rosa\", 23),\r\n" + 
					"(8281, \"Santa Rosa (Capivari do Sul)\", 23),\r\n" + 
					"(8282, \"Santa Rosa da Serra\", 11),\r\n" + 
					"(8283, \"Santa Rosa da Vigia (Vigia)\", 14),\r\n" + 
					"(8284, \"Santa Rosa de Goi�s\", 9),\r\n" + 
					"(8285, \"Santa Rosa de Lima\", 25),\r\n" + 
					"(8286, \"Santa Rosa de Lima\", 24),\r\n" + 
					"(8287, \"Santa Rosa de Lima (Montes Claros)\", 11),\r\n" + 
					"(8288, \"Santa Rosa de Viterbo\", 26),\r\n" + 
					"(8289, \"Santa Rosa do Piau�\", 17),\r\n" + 
					"(8290, \"Santa Rosa do Purus\", 1),\r\n" + 
					"(8291, \"Santa Rosa dos Dourados (Coromandel)\", 11),\r\n" + 
					"(8292, \"Santa Rosa do Sul\", 24),\r\n" + 
					"(8293, \"Santa Rosa do Tocantins\", 27),\r\n" + 
					"(8294, \"Santa Rosa (Formosa)\", 9),\r\n" + 
					"(8295, \"Santa Rosa (Francisco Beltr�o)\", 18),\r\n" + 
					"(8296, \"Santa Rosa (Iati)\", 16),\r\n" + 
					"(8297, \"Santa Rosa (Palmeira das Miss�es)\", 23),\r\n" + 
					"(8298, \"Santa Rosa (Serrita)\", 16),\r\n" + 
					"(8299, \"Santa Salete\", 26),\r\n" + 
					"(8300, \"Santa Silvana (Pelotas)\", 23),\r\n" + 
					"(8301, \"Santa Teresa\", 8),\r\n" + 
					"(8302, \"Santa Teresa do Bonito (Pe�anha)\", 11),\r\n" + 
					"(8303, \"Santa Teresa (Santana do Matos)\", 20),\r\n" + 
					"(8304, \"Santa Teresinha\", 15),\r\n" + 
					"(8305, \"Santa Tereza\", 23),\r\n" + 
					"(8306, \"Santa Tereza (Aracati)\", 6),\r\n" + 
					"(8307, \"Santa Tereza (Catu�pe)\", 23),\r\n" + 
					"(8308, \"Santa Tereza (Croat�)\", 6),\r\n" + 
					"(8309, \"Santa Tereza de Goi�s\", 9),\r\n" + 
					"(8310, \"Santa Tereza do Oeste\", 18),\r\n" + 
					"(8311, \"Santa Tereza do Tocantins\", 27),\r\n" + 
					"(8312, \"Santa Tereza (Tau�)\", 6),\r\n" + 
					"(8313, \"Santa Terezinha\", 13),\r\n" + 
					"(8314, \"Santa Terezinha\", 16),\r\n" + 
					"(8315, \"Santa Terezinha\", 24),\r\n" + 
					"(8316, \"Santa Terezinha\", 5),\r\n" + 
					"(8317, \"Santa Terezinha (�gua Preta)\", 16),\r\n" + 
					"(8318, \"Santa Terezinha (Campina Grande)\", 15),\r\n" + 
					"(8319, \"Santa Terezinha (Castanhal)\", 14),\r\n" + 
					"(8320, \"Santa Terezinha de Goi�s\", 9),\r\n" + 
					"(8321, \"Santa Terezinha de Itaipu\", 18),\r\n" + 
					"(8322, \"Santa Terezinha de Minas (Itatiaiu�u)\", 11),\r\n" + 
					"(8323, \"Santa Terezinha do Progresso\", 24),\r\n" + 
					"(8324, \"Santa Terezinha do Rio Ferro (Nova Ubirat�)\", 13),\r\n" + 
					"(8325, \"Santa Terezinha do Salto (Lages)\", 24),\r\n" + 
					"(8326, \"Santa Terezinha do Tocantins\", 27),\r\n" + 
					"(8327, \"Santa Terezinha (Ecoporanga)\", 8),\r\n" + 
					"(8328, \"Santa Terezinha (Itapor�)\", 12),\r\n" + 
					"(8329, \"Santa Terezinha (Palmeira das Miss�es)\", 23),\r\n" + 
					"(8330, \"Santa Terezinha (Soledade)\", 23),\r\n" + 
					"(8331, \"Sant'auta (Camaqu�)\", 23),\r\n" + 
					"(8332, \"Santa Vit�ria\", 11),\r\n" + 
					"(8333, \"Santa Vit�ria do Palmar\", 23),\r\n" + 
					"(8334, \"Santa Z�lia (Astorga)\", 18),\r\n" + 
					"(8335, \"Santelmo (Pederneiras)\", 26),\r\n" + 
					"(8336, \"Santiago\", 23),\r\n" + 
					"(8337, \"Santiago do Iguap� (Cachoeira)\", 5),\r\n" + 
					"(8338, \"Santiago do Sul\", 24),\r\n" + 
					"(8339, \"Sant�ssima Trindade (I�na)\", 8),\r\n" + 
					"(8340, \"Santo Afonso\", 13),\r\n" + 
					"(8341, \"Santo Agostinho (�gua Doce do Norte)\", 8),\r\n" + 
					"(8342, \"Santo Agostinho (Cabo de Santo Agostinho)\", 16),\r\n" + 
					"(8343, \"Santo Amaro\", 10),\r\n" + 
					"(8344, \"Santo Amaro\", 5),\r\n" + 
					"(8345, \"Santo Amaro da Imperatriz\", 24),\r\n" + 
					"(8346, \"Santo Amaro das Brotas\", 25),\r\n" + 
					"(8347, \"Santo Amaro de Campos (Campos dos Goytacazes)\", 19),\r\n" + 
					"(8348, \"Santo Amaro do Sul (General C�mara)\", 23),\r\n" + 
					"(8349, \"Santo Amaro (Sirinha�m)\", 16),\r\n" + 
					"(8350, \"Santo Anast�cio\", 26),\r\n" + 
					"(8351, \"Santo Andr�\", 15),\r\n" + 
					"(8352, \"Santo Andr�\", 26),\r\n" + 
					"(8353, \"Santo Andr� (Penaforte)\", 6),\r\n" + 
					"(8354, \"Santo �ngelo\", 23),\r\n" + 
					"(8355, \"Santo Ant�o (Santa Maria)\", 23),\r\n" + 
					"(8356, \"Santo Ant�nio\", 20),\r\n" + 
					"(8357, \"Santo Ant�nio (Acopiara)\", 6),\r\n" + 
					"(8358, \"Santo Ant�nio (Ararend�)\", 6),\r\n" + 
					"(8359, \"Santo Ant�nio (Barra de S�o Francisco)\", 8),\r\n" + 
					"(8360, \"Santo Ant�nio (Barro)\", 6),\r\n" + 
					"(8361, \"Santo Ant�nio (Castro)\", 18),\r\n" + 
					"(8362, \"Santo Ant�nio (Cerro Largo)\", 23),\r\n" + 
					"(8363, \"Santo Ant�nio (Crate�s)\", 6),\r\n" + 
					"(8364, \"Santo Ant�nio da Alegria\", 26),\r\n" + 
					"(8365, \"Santo Ant�nio da Barra\", 9),\r\n" + 
					"(8366, \"Santo Ant�nio da Boa Vista (S�o Jo�o da Ponte)\", 11),\r\n" + 
					"(8367, \"Santo Ant�nio da Esperan�a (Santa Cruz de Goi�s)\", 9),\r\n" + 
					"(8368, \"Santo Ant�nio da Fortaleza (Ferros)\", 11),\r\n" + 
					"(8369, \"Santo Ant�nio da Patrulha\", 23),\r\n" + 
					"(8370, \"Santo Ant�nio da Pindoba (Ibiapina)\", 6),\r\n" + 
					"(8371, \"Santo Ant�nio da Platina\", 18),\r\n" + 
					"(8372, \"Santo Ant�nio das Miss�es\", 23),\r\n" + 
					"(8373, \"Santo Ant�nio das Queimadas (Jurema)\", 16),\r\n" + 
					"(8374, \"Santo Ant�nio da Vargem Alegre (Bonfim)\", 11),\r\n" + 
					"(8375, \"Santo Ant�nio de Barcelona (Caravelas)\", 5),\r\n" + 
					"(8376, \"Santo Ant�nio de Castro (Carlos Barbosa)\", 23),\r\n" + 
					"(8377, \"Santo Ant�nio de Goi�s\", 9),\r\n" + 
					"(8378, \"Santo Ant�nio de Jesus\", 5),\r\n" + 
					"(8379, \"Santo Ant�nio de Lisboa\", 17),\r\n" + 
					"(8380, \"Santo Ant�nio de P�dua\", 19),\r\n" + 
					"(8381, \"Santo Ant�nio de Posse\", 26),\r\n" + 
					"(8382, \"Santo Ant�nio do Amparo\", 11),\r\n" + 
					"(8383, \"Santo Ant�nio do Aracangu�\", 26),\r\n" + 
					"(8384, \"Santo Ant�nio do Aventureiro\", 11),\r\n" + 
					"(8385, \"Santo Ant�nio do Bom Retiro (Ibirub�)\", 23),\r\n" + 
					"(8386, \"Santo Ant�nio do Boqueir�o (Una�)\", 11),\r\n" + 
					"(8387, \"Santo Ant�nio do Caiu�\", 18),\r\n" + 
					"(8388, \"Santo Ant�nio do Cana� (Santa Teresa)\", 8),\r\n" + 
					"(8389, \"Santo Ant�nio do Cruzeiro (Nepomuceno)\", 11),\r\n" + 
					"(8390, \"Santo Ant�nio do Descoberto\", 9),\r\n" + 
					"(8391, \"Santo Ant�nio do Gl�ria (Vieiras)\", 11),\r\n" + 
					"(8392, \"Santo Ant�nio do Grama\", 11),\r\n" + 
					"(8393, \"Santo Ant�nio do I��\", 3),\r\n" + 
					"(8394, \"Santo Ant�nio do Imb� (Santa Maria Madalena)\", 19),\r\n" + 
					"(8395, \"Santo Ant�nio do Iratim (Bituruna)\", 18),\r\n" + 
					"(8396, \"Santo Ant�nio do Itamb�\", 11),\r\n" + 
					"(8397, \"Santo Ant�nio do Jacinto\", 11),\r\n" + 
					"(8398, \"Santo Ant�nio do Jardim\", 26),\r\n" + 
					"(8399, \"Santo Ant�nio do Leite (Ouro Preto)\", 11),\r\n" + 
					"(8400, \"Santo Ant�nio do Leste\", 13),\r\n" + 
					"(8401, \"Santo Ant�nio do Leverger\", 13),\r\n" + 
					"(8402, \"Santo Ant�nio do Manhua�u (Caratinga)\", 11),\r\n" + 
					"(8403, \"Santo Ant�nio do Matupi (Manicor�)\", 3),\r\n" + 
					"(8404, \"Santo Ant�nio do Monte\", 11),\r\n" + 
					"(8405, \"Santo Ant�nio do Mucuri (Malacacheta)\", 11),\r\n" + 
					"(8406, \"Santo Ant�nio do Muqui (Mimoso do Sul)\", 8),\r\n" + 
					"(8407, \"Santo Ant�nio do Norte (Concei��o do Mato Dentro)\", 11),\r\n" + 
					"(8408, \"Santo Ant�nio do Palma\", 23),\r\n" + 
					"(8409, \"Santo Ant�nio do Palmital (Rio Bom)\", 18),\r\n" + 
					"(8410, \"Santo Ant�nio do Para�so\", 18),\r\n" + 
					"(8411, \"Santo Ant�nio do Paranapanema (C�ndido Mota)\", 26),\r\n" + 
					"(8412, \"Santo Ant�nio do Pinhal\", 26),\r\n" + 
					"(8413, \"Santo Ant�nio do Pirapetinga (Piranga)\", 11),\r\n" + 
					"(8414, \"Santo Ant�nio do Planalto\", 23),\r\n" + 
					"(8415, \"Santo Ant�nio do Pontal (Governador Valadares)\", 11),\r\n" + 
					"(8416, \"Santo Ant�nio do Porto (Governador Valadares)\", 11),\r\n" + 
					"(8417, \"Santo Ant�nio do Potengi (S�o Gon�alo do Amarante)\", 20),\r\n" + 
					"(8418, \"Santo Ant�nio do Pousalegre (Boa Esperan�a)\", 8),\r\n" + 
					"(8419, \"Santo Ant�nio do Quinze (Nova Ven�cia)\", 8),\r\n" + 
					"(8420, \"Santo Ant�nio do Retiro\", 11),\r\n" + 
					"(8421, \"Santo Ant�nio do Rio Abaixo\", 11),\r\n" + 
					"(8422, \"Santo Antonio do Rio Bonito (Nova Ubirat�)\", 13),\r\n" + 
					"(8423, \"Santo Ant�nio do Rio Verde (Catal�o)\", 9),\r\n" + 
					"(8424, \"Santo Ant�nio do Salto (Ouro Preto)\", 11),\r\n" + 
					"(8425, \"Santo Ant�nio dos Ara�jos (S�o Sebasti�o do Maranh�o)\", 11),\r\n" + 
					"(8426, \"Santo Ant�nio dos Campos (Divin�polis)\", 11),\r\n" + 
					"(8427, \"Santo Ant�nio dos Fernandes (Meruoca)\", 6),\r\n" + 
					"(8428, \"Santo Ant�nio dos Lopes\", 10),\r\n" + 
					"(8429, \"Santo Ant�nio dos Milagres\", 17),\r\n" + 
					"(8430, \"Santo Ant�nio dos Palmares (Palmares)\", 16),\r\n" + 
					"(8431, \"Santo Antonio dos Pinheirinhos (�gua Santa)\", 23),\r\n" + 
					"(8432, \"Santo Ant�nio do Sudoeste\", 18),\r\n" + 
					"(8433, \"Santo Ant�nio do Tau�\", 14),\r\n" + 
					"(8434, \"Santo Ant�nio (Guapor�)\", 23),\r\n" + 
					"(8435, \"Santo Ant�nio (Iju�)\", 23),\r\n" + 
					"(8436, \"Santo Ant�nio (Laranjeiras do Sul)\", 18),\r\n" + 
					"(8437, \"Santo Ant�nio (Mato Leit�o)\", 23),\r\n" + 
					"(8438, \"Santo Ant�nio (Palmeira dos �ndios)\", 2),\r\n" + 
					"(8439, \"Santo Ant�nio (Reden��o)\", 14),\r\n" + 
					"(8440, \"Santo Ant�nio (Santo Augusto)\", 23),\r\n" + 
					"(8441, \"Santo Ant�nio (Teixeira de Freitas)\", 5),\r\n" + 
					"(8442, \"Santo Ant�nio (Tr�s Passos)\", 23),\r\n" + 
					"(8443, \"Santo Augusto\", 23),\r\n" + 
					"(8444, \"Santo Cristo\", 23),\r\n" + 
					"(8445, \"Santo Eduardo (Campos dos Goytacazes)\", 19),\r\n" + 
					"(8446, \"Santo Estev�o\", 5),\r\n" + 
					"(8447, \"Santo Expedito\", 26),\r\n" + 
					"(8448, \"Santo Expedito do Sul\", 23),\r\n" + 
					"(8449, \"Santo Hil�rio (Pimenta)\", 11),\r\n" + 
					"(8450, \"Santo Hip�lito\", 11),\r\n" + 
					"(8451, \"Santo In�cio\", 18),\r\n" + 
					"(8452, \"Santo In�cio (Bom Jesus)\", 23),\r\n" + 
					"(8453, \"Santo In�cio do Piau�\", 17),\r\n" + 
					"(8454, \"Santo In�cio (Gentio do Ouro)\", 5),\r\n" + 
					"(8455, \"Santo Izidoro (Tr�s Barras do Paran�)\", 18),\r\n" + 
					"(8456, \"Sant�polis do Aguape�\", 26),\r\n" + 
					"(8457, \"Santos\", 26),\r\n" + 
					"(8458, \"Santos Dumont\", 11),\r\n" + 
					"(8459, \"S�o Bartolomeu (Cari�s)\", 6),\r\n" + 
					"(8460, \"S�o Bartolomeu (Ouro Preto)\", 11),\r\n" + 
					"(8461, \"S�o Benedito\", 6),\r\n" + 
					"(8462, \"S�o Benedito da Cachoeirinha (Ituverava)\", 26),\r\n" + 
					"(8463, \"S�o Benedito das Areias (Mococa)\", 26),\r\n" + 
					"(8464, \"S�o Benedito do Rio Preto\", 10),\r\n" + 
					"(8465, \"S�o Benedito do Sul\", 16),\r\n" + 
					"(8466, \"S�o Bentinho\", 15),\r\n" + 
					"(8467, \"S�o Bento\", 10),\r\n" + 
					"(8468, \"S�o Bento\", 15),\r\n" + 
					"(8469, \"S�o Bento Abade\", 11),\r\n" + 
					"(8470, \"S�o Bento Baixo (Nova Veneza)\", 24),\r\n" + 
					"(8471, \"S�o Bento (Carazinho)\", 23),\r\n" + 
					"(8472, \"S�o Bento de Caldas (Santa Rita de Caldas)\", 11),\r\n" + 
					"(8473, \"S�o Bento do Norte\", 20),\r\n" + 
					"(8474, \"S�o Bento do Sapuca�\", 26),\r\n" + 
					"(8475, \"S�o Bento do Sul\", 24),\r\n" + 
					"(8476, \"S�o Bento do Tocantins\", 27),\r\n" + 
					"(8477, \"S�o Bento do Trairi\", 20),\r\n" + 
					"(8478, \"S�o Bento do Una\", 16),\r\n" + 
					"(8479, \"S�o Bento (Palmeira das Miss�es)\", 23),\r\n" + 
					"(8480, \"S�o Bernardino\", 24),\r\n" + 
					"(8481, \"S�o Bernardo\", 10),\r\n" + 
					"(8482, \"S�o Bernardo do Campo\", 26),\r\n" + 
					"(8483, \"S�o Bernardo (Lu�s Gomes)\", 20),\r\n" + 
					"(8484, \"S�o Bernardo (Quixad�)\", 6),\r\n" + 
					"(8485, \"S�o Berto (Manduri)\", 26),\r\n" + 
					"(8486, \"S�o Bom Jesus (Erval Seco)\", 23),\r\n" + 
					"(8487, \"S�o Bonif�cio\", 24),\r\n" + 
					"(8488, \"S�o Borja\", 23),\r\n" + 
					"(8489, \"S�o Br�s\", 2),\r\n" + 
					"(8490, \"S�o Br�s do Sua�u�\", 11),\r\n" + 
					"(8491, \"S�o Braz (Cascavel)\", 18),\r\n" + 
					"(8492, \"S�o Braz do Piau�\", 17),\r\n" + 
					"(8493, \"S�o Braz (Lagamar)\", 11),\r\n" + 
					"(8494, \"S�o Braz (Pato Branco)\", 18),\r\n" + 
					"(8495, \"S�o Caetano (�gua Santa)\", 23),\r\n" + 
					"(8496, \"S�o Caetano de Odivelas\", 14),\r\n" + 
					"(8497, \"S�o Caetano do Navio (Bet�nia)\", 16),\r\n" + 
					"(8498, \"S�o Caetano do Sul\", 26),\r\n" + 
					"(8499, \"S�o Caitano\", 16),\r\n" + 
					"(8500, \"S�o Camilo (Palotina)\", 18),\r\n" + 
					"(8501, \"S�o C�ndido (Caratinga)\", 11),\r\n" + 
					"(8502, \"S�o Carlos\", 24),\r\n" + 
					"(8503, \"S�o Carlos\", 26),\r\n" + 
					"(8504, \"S�o Carlos do Iva�\", 18),\r\n" + 
					"(8505, \"S�o Carlos (Porto Velho)\", 21),\r\n" + 
					"(8506, \"S�o Carlos (Ros�rio do Sul)\", 23),\r\n" + 
					"(8507, \"S�o Cirilo (Castro)\", 18),\r\n" + 
					"(8508, \"S�o Clemente (Santa Helena)\", 18),\r\n" + 
					"(8509, \"S�o Crist�v�o\", 25),\r\n" + 
					"(8510, \"S�o Crist�v�o do Sul\", 24),\r\n" + 
					"(8511, \"S�o Cristov�o (Paranava�)\", 18),\r\n" + 
					"(8512, \"S�o Crist�v�o (Tr�s Barras)\", 24),\r\n" + 
					"(8513, \"S�o Cristov�o (Uni�o da Vit�ria)\", 18),\r\n" + 
					"(8514, \"S�o Desid�rio\", 5),\r\n" + 
					"(8515, \"S�o Domingos\", 9),\r\n" + 
					"(8516, \"S�o Domingos\", 24),\r\n" + 
					"(8517, \"S�o Domingos\", 25),\r\n" + 
					"(8518, \"S�o Domingos\", 5),\r\n" + 
					"(8519, \"S�o Domingos (Apucarana)\", 18),\r\n" + 
					"(8520, \"S�o Domingos (Brejo da Madre de Deus)\", 16),\r\n" + 
					"(8521, \"S�o Domingos (Caridade)\", 6),\r\n" + 
					"(8522, \"S�o Domingos (Cruzmaltina)\", 18),\r\n" + 
					"(8523, \"S�o Domingos da Bocaina (Lima Duarte)\", 11),\r\n" + 
					"(8524, \"S�o Domingos das Dores\", 11),\r\n" + 
					"(8526, \"S�o Domingos do Araguaia\", 14),\r\n" + 
					"(8527, \"S�o Domingos do Azeit�o\", 10),\r\n" + 
					"(8528, \"S�o Domingos do Capim\", 14),\r\n" + 
					"(8529, \"S�o Domingos do Cariri\", 15),\r\n" + 
					"(8530, \"S�o Domingos do Maranh�o\", 10),\r\n" + 
					"(8531, \"S�o Domingos do Norte\", 8),\r\n" + 
					"(8532, \"S�o Domingos do Prata\", 11),\r\n" + 
					"(8533, \"S�o Domingos do Sul\", 23),\r\n" + 
					"(8534, \"S�o Domingos (Santo Ant�nio do Aventureiro)\", 11),\r\n" + 
					"(8535, \"S�o Domingos (Uni�o da Vit�ria)\", 18),\r\n" + 
					"(8536, \"S�o Felipe\", 5),\r\n" + 
					"(8537, \"S�o Felipe (Brejo Santo)\", 6),\r\n" + 
					"(8538, \"S�o Felipe D'Oeste\", 21),\r\n" + 
					"(8539, \"S�o Felipe (S�o Gabriel da Cachoeira)\", 3),\r\n" + 
					"(8540, \"S�o F�lix\", 5),\r\n" + 
					"(8541, \"S�o F�lix de Balsas\", 10),\r\n" + 
					"(8542, \"S�o F�lix de Minas\", 11),\r\n" + 
					"(8543, \"S�o F�lix do Araguaia\", 13),\r\n" + 
					"(8544, \"S�o F�lix do Coribe\", 5),\r\n" + 
					"(8545, \"S�o F�lix do Piau�\", 17),\r\n" + 
					"(8546, \"S�o F�lix do Tocantins\", 27),\r\n" + 
					"(8547, \"S�o F�lix do Xingu\", 14),\r\n" + 
					"(8548, \"S�o Fernando\", 20),\r\n" + 
					"(8549, \"S�o Fid�lis\", 19),\r\n" + 
					"(8550, \"S�o Francisco\", 11),\r\n" + 
					"(8551, \"S�o Francisco\", 25),\r\n" + 
					"(8552, \"S�o Francisco\", 26),\r\n" + 
					"(8553, \"S�o Francisco\", 15),\r\n" + 
					"(8554, \"S�o Francisco (Cerro Largo)\", 23),\r\n" + 
					"(8555, \"S�o Francisco (Chopinzinho)\", 18),\r\n" + 
					"(8556, \"S�o Francisco da Jararaca (Muan�)\", 14),\r\n" + 
					"(8557, \"S�o Francisco da Praia (S�o Sebasti�o)\", 26),\r\n" + 
					"(8558, \"S�o Francisco de Assis do Piau�\", 17),\r\n" + 
					"(8559, \"S�o Francisco de Assis\", 23),\r\n" + 
					"(8560, \"S�o Francisco de Goi�s\", 9),\r\n" + 
					"(8561, \"S�o Francisco de Imba� (Congonhinhas)\", 18),\r\n" + 
					"(8562, \"S�o Francisco de Itabapoana\", 19),\r\n" + 
					"(8563, \"S�o Francisco de Paula\", 11),\r\n" + 
					"(8564, \"S�o Francisco de Paula\", 23),\r\n" + 
					"(8565, \"S�o Francisco de Sales\", 11),\r\n" + 
					"(8566, \"S�o Francisco de Sales (Clevel�ndia)\", 18),\r\n" + 
					"(8567, \"S�o Francisco do Brej�o\", 10),\r\n" + 
					"(8568, \"S�o Francisco do Br�gida (Serrita)\", 16),\r\n" + 
					"(8569, \"S�o Francisco do Conde\", 5),\r\n" + 
					"(8570, \"S�o Francisco do Gl�ria\", 11),\r\n" + 
					"(8571, \"S�o Francisco do Guapor�\", 21),\r\n" + 
					"(8572, \"S�o Francisco do Humait� (Mutum)\", 11),\r\n" + 
					"(8573, \"S�o Francisco do Maranh�o\", 10),\r\n" + 
					"(8574, \"S�o Francisco do Oeste\", 20),\r\n" + 
					"(8575, \"S�o Francisco do Par�\", 14),\r\n" + 
					"(8576, \"S�o Francisco do Piau�\", 17),\r\n" + 
					"(8577, \"S�o Francisco do Sul\", 24),\r\n" + 
					"(8578, \"S�o Francisco (Guarapuava)\", 18),\r\n" + 
					"(8579, \"S�o Francisco (Meruoca)\", 6),\r\n" + 
					"(8580, \"S�o Francisco (Quiterian�polis)\", 6),\r\n" + 
					"(8581, \"S�o Francisco (S�o Domingos do Norte)\", 8),\r\n" + 
					"(8582, \"S�o Francisco (Umuarama)\", 18),\r\n" + 
					"(8583, \"S�o Francisco Xavier do Guandu (Afonso Cl�udio)\", 8),\r\n" + 
					"(8584, \"S�o Francisco Xavier (S�o Jos� dos Campos)\", 26),\r\n" + 
					"(8585, \"S�o Francisco (Xinguara)\", 14),\r\n" + 
					"(8586, \"S�o Gabriel\", 23),\r\n" + 
					"(8587, \"S�o Gabriel\", 5),\r\n" + 
					"(8588, \"S�o Gabriel (Camb�)\", 18),\r\n" + 
					"(8589, \"S�o Gabriel (Colombo)\", 18),\r\n" + 
					"(8590, \"S�o Gabriel da Cachoeira\", 3),\r\n" + 
					"(8591, \"S�o Gabriel da Palha\", 8),\r\n" + 
					"(8592, \"S�o Gabriel de Goi�s (Planaltina)\", 9),\r\n" + 
					"(8593, \"S�o Gabriel do Oeste\", 12),\r\n" + 
					"(8594, \"S�o Gabriel (Treze de Maio)\", 24),\r\n" + 
					"(8595, \"S�o Geraldo\", 11),\r\n" + 
					"(8596, \"S�o Geraldo (Cara�bas)\", 20),\r\n" + 
					"(8597, \"S�o Geraldo (Cora��o de Jesus)\", 11),\r\n" + 
					"(8598, \"S�o Geraldo da Piedade\", 11),\r\n" + 
					"(8599, \"S�o Geraldo de Tumiritinga (Tumiritinga)\", 11),\r\n" + 
					"(8600, \"S�o Geraldo do Araguaia\", 14),\r\n" + 
					"(8601, \"S�o Geraldo do Baguari (S�o Jo�o Evangelista)\", 11),\r\n" + 
					"(8602, \"S�o Geraldo do Baixio\", 11),\r\n" + 
					"(8603, \"S�o Geraldo (Manten�polis)\", 8),\r\n" + 
					"(8604, \"S�o Gon�alo\", 19),\r\n" + 
					"(8605, \"S�o Gon�alo de Botelhos (Botelhos)\", 11),\r\n" + 
					"(8606, \"S�o Gon�alo do Abaet�\", 11),\r\n" + 
					"(8607, \"S�o Gon�alo do Amarante\", 6),\r\n" + 
					"(8608, \"S�o Gon�alo do Amarante\", 20),\r\n" + 
					"(8609, \"S�o Gon�alo do Gurgu�ia\", 17),\r\n" + 
					"(8610, \"S�o Gon�alo do Monte (Itabirito)\", 11),\r\n" + 
					"(8611, \"S�o Gon�alo do Par�\", 11),\r\n" + 
					"(8612, \"S�o Gon�alo do Piau�\", 17),\r\n" + 
					"(8613, \"S�o Gon�alo do Rio Abaixo\", 11),\r\n" + 
					"(8614, \"S�o Gon�alo do Rio das Pedras (Serro)\", 11),\r\n" + 
					"(8615, \"S�o Gon�alo do Rio Preto\", 11),\r\n" + 
					"(8616, \"S�o Gon�alo do Sapuca�\", 11),\r\n" + 
					"(8617, \"S�o Gon�alo dos Campos\", 5),\r\n" + 
					"(8618, \"S�o Gon�alo do Umari (Momba�a)\", 6),\r\n" + 
					"(8619, \"S�o Gon�alo (S�o Jos� dos Ausentes)\", 23),\r\n" + 
					"(8620, \"S�o Gon�alo (Sousa)\", 15),\r\n" + 
					"(8621, \"S�o Gotardo\", 11),\r\n" + 
					"(8622, \"S�o Gotardo (Pato Branco)\", 18),\r\n" + 
					"(8623, \"S�o Jacinto (S�o Roque do Cana�)\", 8),\r\n" + 
					"(8624, \"S�o Jer�nimo\", 23),\r\n" + 
					"(8625, \"S�o Jer�nimo da Serra\", 18),\r\n" + 
					"(8626, \"S�o Jer�nimo dos Po��es (Campos Altos)\", 11),\r\n" + 
					"(8627, \"S�o Jo�o\", 16),\r\n" + 
					"(8628, \"S�o Jo�o\", 18),\r\n" + 
					"(8629, \"S�o Jo�o (Alt�nia)\", 18),\r\n" + 
					"(8630, \"S�o Jo�o Batista\", 24),\r\n" + 
					"(8631, \"S�o Jo�o Batista\", 10),\r\n" + 
					"(8632, \"S�o Jo�o Batista do Gl�ria\", 11),\r\n" + 
					"(8633, \"S�o Jo�o Batista (Vit�ria das Miss�es)\", 23),\r\n" + 
					"(8634, \"S�o Jo�o (Boa Vista das Miss�es)\", 23),\r\n" + 
					"(8635, \"S�o Jo�o Bosco (Cir�aco)\", 23),\r\n" + 
					"(8636, \"S�o Jo�o Bosco (Uira�na)\", 15),\r\n" + 
					"(8637, \"S�o Jo�o da Baliza\", 22),\r\n" + 
					"(8638, \"S�o Jo�o da Barra\", 19),\r\n" + 
					"(8639, \"S�o Jo�o da Boa Vista\", 26),\r\n" + 
					"(8640, \"S�o Jo�o da Canabrava\", 17),\r\n" + 
					"(8641, \"S�o Jo�o da Chapada (Diamantina)\", 11),\r\n" + 
					"(8642, \"S�o Jo�o da Fortaleza (C�cero Dantas)\", 5),\r\n" + 
					"(8643, \"S�o Jo�o da Fronteira\", 17),\r\n" + 
					"(8644, \"S�o Jo�o da Lagoa\", 11),\r\n" + 
					"(8645, \"S�o Jo�o D'Alian�a\", 9),\r\n" + 
					"(8646, \"S�o Jo�o da Mata\", 11),\r\n" + 
					"(8647, \"S�o Jo�o da Para�na\", 9),\r\n" + 
					"(8648, \"S�o Jo�o da Ponta\", 14),\r\n" + 
					"(8649, \"S�o Jo�o da Ponte\", 11),\r\n" + 
					"(8650, \"S�o Jo�o da Sapucaia (Laranjal)\", 11),\r\n" + 
					"(8651, \"S�o Jo�o das Duas Pontes\", 26),\r\n" + 
					"(8652, \"S�o Jo�o da Serra\", 17),\r\n" + 
					"(8653, \"S�o Jo�o da Serra Negra (Patroc�nio)\", 11),\r\n" + 
					"(8654, \"S�o Jo�o da Serra (Santos Dumont)\", 11),\r\n" + 
					"(8655, \"S�o Jo�o das Miss�es\", 11),\r\n" + 
					"(8656, \"S�o Jo�o das Miss�es (S�o Miguel das Miss�es)\", 23),\r\n" + 
					"(8657, \"S�o Jo�o da Urtiga\", 23),\r\n" + 
					"(8658, \"S�o Jo�o da Varjota\", 17),\r\n" + 
					"(8659, \"S�o Jo�o da Vereda (Montes Claros)\", 11),\r\n" + 
					"(8660, \"S�o Jo�o da Vit�ria (Vit�ria da Conquista)\", 5),\r\n" + 
					"(8661, \"S�o Jo�o de Cortes (Alc�ntara)\", 10),\r\n" + 
					"(8662, \"S�o Jo�o de Deus (Russas)\", 6),\r\n" + 
					"(8663, \"S�o Jo�o de Iracema\", 26),\r\n" + 
					"(8664, \"S�o Jo�o de Itagua�u (Urup�s)\", 26),\r\n" + 
					"(8665, \"S�o Jo�o Del Rei\", 11),\r\n" + 
					"(8666, \"S�o Jo�o de Meriti\", 19),\r\n" + 
					"(8667, \"S�o Jo�o de Petr�polis (Santa Teresa)\", 8),\r\n" + 
					"(8668, \"S�o Jo�o de Pirabas\", 14),\r\n" + 
					"(8669, \"S�o Jo�o de Vi�osa (Venda Nova do Imigrante)\", 8),\r\n" + 
					"(8670, \"S�o Jo�o do Acangata (Portel)\", 14),\r\n" + 
					"(8671, \"S�o Jo�o do Amanari (Maranguape)\", 6),\r\n" + 
					"(8672, \"S�o Jo�o do Apore (Parana�ba)\", 12),\r\n" + 
					"(8673, \"S�o Jo�o do Araguaia\", 14),\r\n" + 
					"(8674, \"S�o Jo�o do Arraial\", 17),\r\n" + 
					"(8675, \"S�o Jo�o do Bonito (Mato Verde)\", 11),\r\n" + 
					"(8676, \"S�o Jo�o do Caiu�\", 18),\r\n" + 
					"(8677, \"S�o Jo�o do Cariri\", 15),\r\n" + 
					"(8678, \"S�o Jo�o do Car�\", 10),\r\n" + 
					"(8679, \"S�o Jo�o d'Oeste (Cascavel)\", 18),\r\n" + 
					"(8680, \"S�o Jo�o do Ferraz (Vertentes)\", 16),\r\n" + 
					"(8681, \"S�o Jo�o do Itaperi�\", 24),\r\n" + 
					"(8682, \"S�o Jo�o do Iva�\", 18),\r\n" + 
					"(8683, \"S�o Jo�o do Jacutinga (Caratinga)\", 11),\r\n" + 
					"(8684, \"S�o Jo�o do Jaguaribe\", 6),\r\n" + 
					"(8685, \"S�o Jo�o do Manhua�u\", 11),\r\n" + 
					"(8686, \"S�o Jo�o do Manteninha\", 11),\r\n" + 
					"(8687, \"S�o Jo�o do Marinheiro (Cardoso)\", 26),\r\n" + 
					"(8688, \"S�o Jo�o do Norte (Alegre)\", 8),\r\n" + 
					"(8689, \"S�o Jo�o do Oeste\", 24),\r\n" + 
					"(8690, \"S�o Jo�o do Oriente\", 11),\r\n" + 
					"(8691, \"S�o Jo�o do Pacu�\", 11),\r\n" + 
					"(8692, \"S�o Jo�o do Para�so\", 11),\r\n" + 
					"(8693, \"S�o Jo�o do Para�so\", 10),\r\n" + 
					"(8694, \"S�o Jo�o do Para�so (Cambuci)\", 19),\r\n" + 
					"(8695, \"S�o Jo�o do Pau d'Alho\", 26),\r\n" + 
					"(8696, \"S�o Jo�o do Piau�\", 17),\r\n" + 
					"(8697, \"S�o Jo�o do Pinhal (S�o Jer�nimo da Serra)\", 18),\r\n" + 
					"(8698, \"S�o Jo�o do Piri� (Paragominas)\", 14),\r\n" + 
					"(8699, \"S�o Jo�o do Pol�sine\", 23),\r\n" + 
					"(8700, \"S�o Jo�o do Pr�ncipe (I�na)\", 8),\r\n" + 
					"(8701, \"S�o Jo�o do Rio do Peixe\", 15),\r\n" + 
					"(8702, \"S�o Jo�o do Sabugi\", 20),\r\n" + 
					"(8703, \"S�o Jo�o dos Mellos (J�lio de Castilhos)\", 23),\r\n" + 
					"(8704, \"S�o Jo�o do Sobrado (Pinheiros)\", 8),\r\n" + 
					"(8705, \"S�o Jo�o do S�ter\", 10),\r\n" + 
					"(8706, \"S�o Jo�o dos Patos\", 10),\r\n" + 
					"(8707, \"S�o Jo�o dos Queiroz (Quixad�)\", 6),\r\n" + 
					"(8708, \"S�o Jo�o dos Ramos (S�o Caetano de Odivelas)\", 14),\r\n" + 
					"(8709, \"S�o Jo�o do Sul\", 24),\r\n" + 
					"(8710, \"S�o Jo�o do Tigre\", 15),\r\n" + 
					"(8711, \"S�o Jo�o do Triunfo\", 18),\r\n" + 
					"(8712, \"S�o Jo�o (Erer�)\", 6),\r\n" + 
					"(8713, \"S�o Jo�o Evangelista\", 11),\r\n" + 
					"(8714, \"S�o Jo�o (Goi�s)\", 9),\r\n" + 
					"(8715, \"S�o Jo�o (Laranjeiras do Sul)\", 18),\r\n" + 
					"(8716, \"S�o Jo�o Marcos (Rio Claro)\", 19),\r\n" + 
					"(8717, \"S�o Jo�o Nepomuceno\", 11),\r\n" + 
					"(8718, \"S�o Jo�o (Redentora)\", 23),\r\n" + 
					"(8719, \"S�o Joaquim\", 24),\r\n" + 
					"(8720, \"S�o Joaquim (Arapongas)\", 18),\r\n" + 
					"(8721, \"S�o Joaquim (Cardoso Moreira)\", 19),\r\n" + 
					"(8722, \"S�o Joaquim (Cora��o de Jesus)\", 11),\r\n" + 
					"(8723, \"S�o Joaquim da Barra\", 26),\r\n" + 
					"(8724, \"S�o Joaquim de Bicas\", 11),\r\n" + 
					"(8725, \"S�o Joaquim do Monte\", 16),\r\n" + 
					"(8726, \"S�o Joaquim do Pacu� (Macap�)\", 4),\r\n" + 
					"(8727, \"S�o Joaquim do Pontal (Itambarac�)\", 18),\r\n" + 
					"(8728, \"S�o Joaquim do Salgado (Senador Pompeu)\", 6),\r\n" + 
					"(8729, \"S�o Joaquim dos Melos (Tuntum)\", 10),\r\n" + 
					"(8730, \"S�o Joaquim do Tapar� (Santar�m)\", 14),\r\n" + 
					"(8731, \"S�o Joaquim (Guarapuava)\", 18),\r\n" + 
					"(8732, \"S�o Joaquim (Janu�ria)\", 11),\r\n" + 
					"(8733, \"S�o Joaquim (Tangar� da Serra)\", 13),\r\n" + 
					"(8734, \"S�o Joaquim (Umirim)\", 6),\r\n" + 
					"(8735, \"S�o Jorge\", 23),\r\n" + 
					"(8736, \"S�o Jorge (Alto Para�so de Goi�s)\", 9),\r\n" + 
					"(8737, \"S�o Jorge (Comodoro)\", 13),\r\n" + 
					"(8738, \"S�o Jorge da Barra Seca (Vila Val�rio)\", 8),\r\n" + 
					"(8739, \"S�o Jorge D'Oeste\", 18),\r\n" + 
					"(8740, \"S�o Jorge do Iva�\", 18),\r\n" + 
					"(8741, \"S�o Jorge do Oliveira (Brejetuba)\", 8),\r\n" + 
					"(8742, \"S�o Jorge do Patroc�nio\", 18),\r\n" + 
					"(8743, \"S�o Jorge do Tiradentes (Rio Bananal)\", 8),\r\n" + 
					"(8744, \"S�o Jorge (Santar�m)\", 14),\r\n" + 
					"(8745, \"S�o Jos�\", 24),\r\n" + 
					"(8746, \"S�o Jos� (Abaiara)\", 6),\r\n" + 
					"(8747, \"S�o Jos� (Alto Alegre)\", 23),\r\n" + 
					"(8749, \"S�o Jos� (Comodoro)\", 13),\r\n" + 
					"(8750, \"S�o Jos� da Barra\", 11),\r\n" + 
					"(8751, \"S�o Jos� da Bela Vista (S�o Gotardo)\", 11),\r\n" + 
					"(8752, \"S�o Jos� da Bela Vista\", 26),\r\n" + 
					"(8753, \"S�o Jos� da Boa Vista\", 18),\r\n" + 
					"(8754, \"S�o Jos� da Coroa Grande\", 16),\r\n" + 
					"(8755, \"S�o Jos� da Gl�ria (Victor Graeff)\", 23),\r\n" + 
					"(8756, \"S�o Jos� da Lagoa Tapada\", 15),\r\n" + 
					"(8757, \"S�o Jos� da Laje\", 2),\r\n" + 
					"(8758, \"S�o Jos� da Lapa\", 11),\r\n" + 
					"(8759, \"S�o Jos� da Mata (Campina Grande)\", 15),\r\n" + 
					"(8760, \"S�o Jos� da Passagem (Santana do Matos)\", 20),\r\n" + 
					"(8761, \"S�o Jos� da Pedra Menina (Espera Feliz)\", 11),\r\n" + 
					"(8762, \"S�o Jos� da Reserva (Santa Cruz do Sul)\", 23),\r\n" + 
					"(8763, \"S�o Jos� da Safira\", 11),\r\n" + 
					"(8764, \"S�o Jos� das Laranjeiras (Maraca�)\", 26),\r\n" + 
					"(8765, \"S�o Jos� das Lontras (Ipueiras)\", 6),\r\n" + 
					"(8766, \"S�o Jos� das Miss�es\", 23),\r\n" + 
					"(8767, \"S�o Jos� das Palmeiras\", 18),\r\n" + 
					"(8768, \"S�o Jos� das Torres (Mimoso do Sul)\", 8),\r\n" + 
					"(8769, \"S�o Jos� da Tapera\", 2),\r\n" + 
					"(8770, \"S�o Jos� da Varginha\", 11),\r\n" + 
					"(8771, \"S�o Jos� da Vit�ria\", 5),\r\n" + 
					"(8772, \"S�o Jos� de Caiana\", 15),\r\n" + 
					"(8773, \"S�o Jos� de Castro (Garibaldi)\", 23),\r\n" + 
					"(8774, \"S�o Jos� de Espinharas\", 15),\r\n" + 
					"(8775, \"S�o Jos� de Fruteiras (Vargem Alta)\", 8),\r\n" + 
					"(8776, \"S�o Jos� de Manten�polis (Manten�polis)\", 8),\r\n" + 
					"(8777, \"S�o Jos� de Marimbas (Cachoeira dos �ndios)\", 15),\r\n" + 
					"(8778, \"S�o Jos� de Mipibu\", 20),\r\n" + 
					"(8779, \"S�o Jos� de Piranhas\", 15),\r\n" + 
					"(8780, \"S�o Jos� de Princesa\", 15),\r\n" + 
					"(8781, \"S�o Jos� de Ribamar\", 10),\r\n" + 
					"(8782, \"S�o Jos� de Solon�pole (Solon�pole)\", 6),\r\n" + 
					"(8783, \"S�o Jos� de Ub�\", 19),\r\n" + 
					"(8784, \"S�o Jos� do Ac�cio (Engenheiro Caldas)\", 11),\r\n" + 
					"(8785, \"S�o Jos� do Alegre\", 11),\r\n" + 
					"(8786, \"S�o Jos� do Apu� (Alta Floresta)\", 13),\r\n" + 
					"(8787, \"S�o Jos� do Barreiro\", 26),\r\n" + 
					"(8788, \"S�o Jos� do Barreiro (S�o Roque de Minas)\", 11),\r\n" + 
					"(8789, \"S�o Jos� do Batatal (Ubaporanga)\", 11),\r\n" + 
					"(8790, \"S�o Jos� do Belmonte\", 16),\r\n" + 
					"(8791, \"S�o Jos� do Bonfim\", 15),\r\n" + 
					"(8792, \"S�o Jos� do Brejo do Cruz\", 15),\r\n" + 
					"(8793, \"S�o Jos� do Buriti (Felixl�ndia)\", 11),\r\n" + 
					"(8794, \"S�o Jos� do Cal�ado\", 8),\r\n" + 
					"(8795, \"S�o Jos� do Campestre\", 20),\r\n" + 
					"(8796, \"S�o Jos� do Cedro\", 24),\r\n" + 
					"(8797, \"S�o Jos� do Centro (N�o-Me-Toque)\", 23),\r\n" + 
					"(8798, \"S�o Jos� do Cerrito\", 24),\r\n" + 
					"(8799, \"S�o Jos� do Col�nia (Itamb�)\", 5),\r\n" + 
					"(8800, \"S�o Jos� do Divino\", 11),\r\n" + 
					"(8801, \"S�o Jos� do Divino\", 17),\r\n" + 
					"(8802, \"S�o Jos� do Egito\", 16),\r\n" + 
					"(8803, \"S�o Jos� do Goiabal\", 11),\r\n" + 
					"(8804, \"S�o Jos� do Gurupi (Viseu)\", 14),\r\n" + 
					"(8805, \"S�o Jos� do Herval\", 23),\r\n" + 
					"(8806, \"S�o Jos� do Hort�ncio\", 23),\r\n" + 
					"(8807, \"S�o Jos� do Igua�u (S�o Miguel do Igua�u)\", 18),\r\n" + 
					"(8808, \"S�o Jos� do Inhacor�\", 23),\r\n" + 
					"(8809, \"S�o Jos� do Itavo (Itaipul�ndia)\", 18),\r\n" + 
					"(8810, \"S�o Jos� do Itueto (Santa Rita do Itueto)\", 11),\r\n" + 
					"(8811, \"S�o Jos� do Iva� (Santa Isabel do Iva�)\", 18),\r\n" + 
					"(8812, \"S�o Jos� do Jacu�pe\", 5),\r\n" + 
					"(8813, \"S�o Jos� do Jacuri\", 11),\r\n" + 
					"(8814, \"S�o Jos� do Lagamar (Jaguaruana)\", 6),\r\n" + 
					"(8815, \"S�o Jos� do Laranjal (Iraceminha)\", 24),\r\n" + 
					"(8816, \"S�o Jos� do Mantimento\", 11),\r\n" + 
					"(8817, \"S�o Jos� do Mato Dentro (Ouro Fino)\", 11),\r\n" + 
					"(8818, \"S�o Jos� do Norte\", 23),\r\n" + 
					"(8819, \"S�o Jos� do Ouro\", 23),\r\n" + 
					"(8820, \"S�o Jos� do P�ntano (Pouso Alegre)\", 11),\r\n" + 
					"(8821, \"S�o Jos� do Paraopeba (Brumadinho)\", 11),\r\n" + 
					"(8822, \"S�o Jos� do Peixe\", 17),\r\n" + 
					"(8823, \"S�o Jos� do Piau�\", 17),\r\n" + 
					"(8824, \"S�o Jos� do Pingador (Lambari D'Oeste)\", 13),\r\n" + 
					"(8825, \"S�o Jos� do Piri� (Viseu)\", 14),\r\n" + 
					"(8826, \"S�o Jos� do Planalto (Pedra Preta)\", 13),\r\n" + 
					"(8827, \"S�o Jos� do Povo\", 13),\r\n" + 
					"(8828, \"S�o Jos� do Prado (Vereda)\", 5),\r\n" + 
					"(8829, \"S�o Jos� do Ribeir�o (Bom Jardim)\", 19),\r\n" + 
					"(8830, \"S�o Jos� do Rio Claro\", 13),\r\n" + 
					"(8831, \"S�o Jos� do Rio Grande (Riach�o das Neves)\", 5),\r\n" + 
					"(8832, \"S�o Jos� do Rio Manso (Itajub�)\", 11),\r\n" + 
					"(8833, \"S�o Jos� do Rio Pardo\", 26),\r\n" + 
					"(8834, \"S�o Jos� do Rio Preto\", 26),\r\n" + 
					"(8835, \"S�o Jos� do Sabugi\", 15),\r\n" + 
					"(8836, \"S�o Jos� dos Ausentes\", 23),\r\n" + 
					"(8837, \"S�o Jos� dos Bas�lios\", 10),\r\n" + 
					"(8838, \"S�o Jos� dos Campos\", 26),\r\n" + 
					"(8839, \"S�o Jos� dos Campos Borges (Campos Borges)\", 23),\r\n" + 
					"(8840, \"S�o Jos� dos Cordeiros\", 15),\r\n" + 
					"(8841, \"S�o Jos� do Serid�\", 20),\r\n" + 
					"(8842, \"S�o Jos� dos Lopes (Lima Duarte)\", 11),\r\n" + 
					"(8843, \"S�o Jos� do Sobradinho (Boa Esperan�a)\", 8),\r\n" + 
					"(8844, \"S�o Jos� dos Pinhais\", 18),\r\n" + 
					"(8845, \"S�o Jos� dos Quatro Marcos\", 13),\r\n" + 
					"(8846, \"S�o Jos� dos Ramos\", 15),\r\n" + 
					"(8847, \"S�o Jos� dos Salgados (Carmo do Cajuru)\", 11),\r\n" + 
					"(8848, \"S�o Jos� do Sul\", 23),\r\n" + 
					"(8849, \"S�o Jos� do Torto (Sobral)\", 6),\r\n" + 
					"(8850, \"S�o Jos� do Turvo (Barra do Pira�)\", 19),\r\n" + 
					"(8851, \"S�o Jos� do Vale do Rio Preto\", 19),\r\n" + 
					"(8852, \"S�o Jos� do Xingu\", 13),\r\n" + 
					"(8853, \"S�o Jos� (Iracema)\", 6),\r\n" + 
					"(8854, \"S�o Jos� (Jandaia do Sul)\", 18),\r\n" + 
					"(8855, \"S�o Jos� (Palhano)\", 6),\r\n" + 
					"(8856, \"S�o Jos� (Planalto)\", 23),\r\n" + 
					"(8857, \"S�o Jos� (Santa Maria do Oeste)\", 18),\r\n" + 
					"(8858, \"S�o Jos� (Santo Ant�nio das Miss�es)\", 23),\r\n" + 
					"(8859, \"S�o Jos� (S�o Miguel das Miss�es)\", 23),\r\n" + 
					"(8860, \"S�o Jos� (Vicentina)\", 12),\r\n" + 
					"(8861, \"S�o Judas Tadeu de Minas (Luisl�ndia)\", 11),\r\n" + 
					"(8862, \"S�o Judas Tadeu (Santo Ant�nio do Para�so)\", 18),\r\n" + 
					"(8863, \"S�o Juli�o\", 17),\r\n" + 
					"(8864, \"S�o L�zaro (Panelas)\", 16),\r\n" + 
					"(8865, \"S�o Leonardo (Alfredo Wagner)\", 24),\r\n" + 
					"(8866, \"S�o Leonardo (Londrina)\", 18),\r\n" + 
					"(8867, \"S�o Leopoldo\", 23),\r\n" + 
					"(8868, \"S�o Loren�o (Goiana)\", 16),\r\n" + 
					"(8869, \"S�o Louren�o\", 11),\r\n" + 
					"(8870, \"S�o Louren�o (Arapiraca)\", 2),\r\n" + 
					"(8871, \"S�o Louren�o (Castro)\", 18),\r\n" + 
					"(8872, \"S�o Louren�o (Cianorte)\", 18),\r\n" + 
					"(8873, \"S�o Louren�o da Mata\", 16),\r\n" + 
					"(8874, \"S�o Louren�o da Serra\", 26),\r\n" + 
					"(8875, \"S�o Louren�o das Miss�es (S�o Luiz Gonzaga)\", 23),\r\n" + 
					"(8876, \"S�o Louren�o de F�tima (Juscimeira)\", 13),\r\n" + 
					"(8877, \"S�o Louren�o do Oeste\", 24),\r\n" + 
					"(8878, \"S�o Louren�o do Piau�\", 17),\r\n" + 
					"(8879, \"S�o Louren�o do Sul\", 23),\r\n" + 
					"(8880, \"S�o Ludgero\", 24),\r\n" + 
					"(8881, \"S�o Lu�s\", 10),\r\n" + 
					"(8882, \"S�o Lu�s de Montes Belos\", 9),\r\n" + 
					"(8883, \"S�o Lu�s do Curu\", 6),\r\n" + 
					"(8884, \"S�o Lu�s do Piau�\", 17),\r\n" + 
					"(8885, \"S�o Lu�s do Quitunde\", 2),\r\n" + 
					"(8886, \"S�o Lu�s Gonzaga do Maranh�o\", 10),\r\n" + 
					"(8887, \"S�o Lu�s Rei (Cacique Doble)\", 23),\r\n" + 
					"(8888, \"S�o Luiz\", 22),\r\n" + 
					"(8889, \"S�o Luiz (Capanema)\", 18),\r\n" + 
					"(8890, \"S�o Luiz (Chopinzinho)\", 18),\r\n" + 
					"(8891, \"S�o Luiz (Clevel�ndia)\", 18),\r\n" + 
					"(8892, \"S�o Luiz do Guaricanga (Presidente Alves)\", 26),\r\n" + 
					"(8893, \"S�o Luiz do Norte\", 9),\r\n" + 
					"(8894, \"S�o Luiz do Oeste (Toledo)\", 18),\r\n" + 
					"(8895, \"S�o Luiz do Paraitinga\", 26),\r\n" + 
					"(8896, \"S�o Luiz do Purun� (Balsa Nova)\", 18),\r\n" + 
					"(8897, \"S�o Luiz do Tapaj�s (Itaituba)\", 14),\r\n" + 
					"(8898, \"S�o Luiz do Tocantins (Niquel�ndia)\", 9),\r\n" + 
					"(8899, \"S�o Luiz (Estrela Velha)\", 23),\r\n" + 
					"(8900, \"S�o Luiz Gonzaga\", 23),\r\n" + 
					"(8901, \"S�o Luiz (Ibiraiaras)\", 23),\r\n" + 
					"(8902, \"S�o Luiz (Londrina)\", 18),\r\n" + 
					"(8903, \"S�o Luiz (Marialva)\", 18),\r\n" + 
					"(8904, \"S�o Luiz (Mariluz)\", 18),\r\n" + 
					"(8905, \"S�o Luiz (Paranagu�)\", 18),\r\n" + 
					"(8906, \"S�o Luiz (Planalto)\", 23),\r\n" + 
					"(8907, \"S�o Mamede\", 15),\r\n" + 
					"(8908, \"S�o Manoel do Boi (Carangola)\", 11),\r\n" + 
					"(8909, \"S�o Manoel do Guaia�u (Dona Euz�bia)\", 11),\r\n" + 
					"(8910, \"S�o Manoel do Paran�\", 18),\r\n" + 
					"(8911, \"S�o Manuel\", 26),\r\n" + 
					"(8913, \"S�o Marcos\", 23),\r\n" + 
					"(8914, \"S�o Marcos Baixo (Constantina)\", 23),\r\n" + 
					"(8915, \"S�o Marcos (Londrina)\", 18),\r\n" + 
					"(8916, \"S�o Marcos (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(8917, \"S�o Marcos (Uruguaiana)\", 23),\r\n" + 
					"(8918, \"S�o Martinho\", 24),\r\n" + 
					"(8919, \"S�o Martinho\", 23),\r\n" + 
					"(8920, \"S�o Martinho da Serra\", 23),\r\n" + 
					"(8921, \"S�o Martinho D'Oeste (Alto Alegre)\", 26),\r\n" + 
					"(8922, \"S�o Martinho (Rol�ndia)\", 18),\r\n" + 
					"(8923, \"S�o Martinho (Santa Cruz do Sul)\", 23),\r\n" + 
					"(8924, \"S�o Mateus\", 8),\r\n" + 
					"(8925, \"S�o Mateus da Palestina (Gararu)\", 25),\r\n" + 
					"(8926, \"S�o Mateus de Minas (Camanducaia)\", 11),\r\n" + 
					"(8927, \"S�o Mateus do Maranh�o\", 10),\r\n" + 
					"(8928, \"S�o Mateus do Sul\", 18),\r\n" + 
					"(8929, \"S�o Miguel\", 20),\r\n" + 
					"(8930, \"S�o Miguel (Arauc�ria)\", 18),\r\n" + 
					"(8931, \"S�o Miguel Arcanjo\", 26),\r\n" + 
					"(8932, \"S�o Miguel (Chapada)\", 23),\r\n" + 
					"(8933, \"S�o Miguel da Baixa Grande\", 17),\r\n" + 
					"(8934, \"S�o Miguel da Boa Vista\", 24),\r\n" + 
					"(8935, \"S�o Miguel da Serra (Porto Uni�o)\", 24),\r\n" + 
					"(8936, \"S�o Miguel das Matas\", 5),\r\n" + 
					"(8937, \"S�o Miguel das Miss�es\", 23),\r\n" + 
					"(8938, \"S�o Miguel de Pracuuba (Muan�)\", 14),\r\n" + 
					"(8939, \"S�o Miguel de Taipu\", 15),\r\n" + 
					"(8940, \"S�o Miguel do Aleixo\", 25),\r\n" + 
					"(8941, \"S�o Miguel do Anta\", 11),\r\n" + 
					"(8942, \"S�o Miguel do Araguaia\", 9),\r\n" + 
					"(8943, \"S�o Miguel do Cambui (Marialva)\", 18),\r\n" + 
					"(8944, \"S�o Miguel do Fidalgo\", 17),\r\n" + 
					"(8945, \"S�o Miguel do Gostoso\", 20),\r\n" + 
					"(8946, \"S�o Miguel do Guam�\", 14),\r\n" + 
					"(8947, \"S�o Miguel do Guapor�\", 21),\r\n" + 
					"(8948, \"S�o Miguel do Igua�u\", 18),\r\n" + 
					"(8949, \"S�o Miguel do Oeste\", 24),\r\n" + 
					"(8950, \"S�o Miguel do Passa Quatro\", 9),\r\n" + 
					"(8951, \"S�o Miguel dos Campos\", 2),\r\n" + 
					"(8952, \"S�o Miguel dos Macacos (Breves)\", 14),\r\n" + 
					"(8953, \"S�o Miguel dos Milagres\", 2),\r\n" + 
					"(8954, \"S�o Miguel do Tapuio\", 17),\r\n" + 
					"(8955, \"S�o Miguel do Tocantins\", 27),\r\n" + 
					"(8956, \"S�o Miguel (Francisco Beltr�o)\", 18),\r\n" + 
					"(8957, \"S�o Miguel (Marau)\", 23),\r\n" + 
					"(8958, \"S�o Miguel (Mauriti)\", 6),\r\n" + 
					"(8959, \"S�o Miguel (Quixeramobim)\", 6),\r\n" + 
					"(8960, \"S�o Miguel (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(8961, \"S�o Miguel (Toledo)\", 18),\r\n" + 
					"(8962, \"S�o Nicolau\", 23),\r\n" + 
					"(8963, \"S�o Patr�cio\", 9),\r\n" + 
					"(8964, \"S�o Paulinho (Acopiara)\", 6),\r\n" + 
					"(8965, \"S�o Paulinho (Itamaraju)\", 5),\r\n" + 
					"(8966, \"S�o Paulo\", 26),\r\n" + 
					"(8967, \"S�o Paulo das Miss�es\", 23),\r\n" + 
					"(8968, \"S�o Paulo das Tunas (Giru�)\", 23),\r\n" + 
					"(8969, \"S�o Paulo de Oliven�a\", 3),\r\n" + 
					"(8970, \"S�o Paulo do Potengi\", 20),\r\n" + 
					"(8971, \"S�o Paulo (Ip�)\", 23),\r\n" + 
					"(8972, \"S�o Paulo (Toledo)\", 18),\r\n" + 
					"(8973, \"S�o Pedro\", 20),\r\n" + 
					"(8974, \"S�o Pedro\", 26),\r\n" + 
					"(8975, \"S�o Pedro (Amp�re)\", 18),\r\n" + 
					"(8976, \"S�o Pedro (Apucarana)\", 18),\r\n" + 
					"(8977, \"S�o Pedro (Bento Gon�alves)\", 23),\r\n" + 
					"(8978, \"S�o Pedro (Campo Largo)\", 18),\r\n" + 
					"(8979, \"S�o Pedro da �gua Branca\", 10),\r\n" + 
					"(8980, \"S�o Pedro da Aldeia\", 19),\r\n" + 
					"(8981, \"S�o Pedro da Cipa\", 13),\r\n" + 
					"(8982, \"S�o Pedro da Gar�a (Montes Claros)\", 11),\r\n" + 
					"(8983, \"S�o Pedro da Serra\", 23),\r\n" + 
					"(8984, \"S�o Pedro das Miss�es\", 23),\r\n" + 
					"(8985, \"S�o Pedro das Tabocas (Pedras de Maria da Cruz)\", 11),\r\n" + 
					"(8986, \"S�o Pedro da Uni�o\", 11),\r\n" + 
					"(8987, \"S�o Pedro de Alc�ntara\", 24),\r\n" + 
					"(8988, \"S�o Pedro de Caldas (Caldas)\", 11),\r\n" + 
					"(8989, \"S�o Pedro de Itabapoana (Mimoso do Sul)\", 8),\r\n" + 
					"(8990, \"S�o Pedro de Rates (Gua�u�)\", 8),\r\n" + 
					"(8991, \"S�o Pedro de Viseu (Mocajuba)\", 14),\r\n" + 
					"(8992, \"S�o Pedro do Ava� (Manhua�u)\", 11),\r\n" + 
					"(8993, \"S�o Pedro do Buti�\", 23),\r\n" + 
					"(8994, \"S�o Pedro do Capim (S�o Domingos do Capim)\", 14),\r\n" + 
					"(8995, \"S�o Pedro do Florido (Santo Ant�nio do Sudoeste)\", 18),\r\n" + 
					"(8996, \"S�o Pedro do Gavi�o (Tururu)\", 6),\r\n" + 
					"(8997, \"S�o Pedro do Gl�ria (Fervedouro)\", 11),\r\n" + 
					"(8998, \"S�o Pedro do Igua�u\", 18),\r\n" + 
					"(8999, \"S�o Pedro do Iraxim (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(9000, \"S�o Pedro do Iva�\", 18),\r\n" + 
					"(9001, \"S�o Pedro do Jequitinhonha (Jequitinhonha)\", 11),\r\n" + 
					"(9002, \"S�o Pedro do Norte (Juc�s)\", 6),\r\n" + 
					"(9003, \"S�o Pedro do Norte (Nova Russas)\", 6),\r\n" + 
					"(9004, \"S�o Pedro do Paran�\", 18),\r\n" + 
					"(9005, \"S�o Pedro do Passa Tr�s (Buritis)\", 11),\r\n" + 
					"(9006, \"S�o Pedro do Piau�\", 17),\r\n" + 
					"(9007, \"S�o Pedro dos Crentes\", 10),\r\n" + 
					"(9008, \"S�o Pedro dos Ferros\", 11),\r\n" + 
					"(9009, \"S�o Pedro do Sua�u�\", 11),\r\n" + 
					"(9010, \"S�o Pedro do Sul\", 23),\r\n" + 
					"(9011, \"S�o Pedro do Turvo\", 26),\r\n" + 
					"(9012, \"S�o Pedro (Dourados)\", 12),\r\n" + 
					"(9013, \"S�o Pedro (Garanhuns)\", 16),\r\n" + 
					"(9014, \"S�o Pedro (Guarapuava)\", 18),\r\n" + 
					"(9015, \"S�o Pedro (Inoc�ncia)\", 12),\r\n" + 
					"(9016, \"S�o Pedro Lopei (Cascavel)\", 18),\r\n" + 
					"(9017, \"S�o Pedro (Muniz Freire)\", 8),\r\n" + 
					"(9018, \"S�o Pedro (Paranava�)\", 18),\r\n" + 
					"(9019, \"S�o Pedro (Santa Cruz)\", 15),\r\n" + 
					"(9020, \"S�o Pedro (Tapera)\", 23),\r\n" + 
					"(9021, \"S�o Pedro (Tenente Portela)\", 23),\r\n" + 
					"(9022, \"S�o Pedro Tobias (Dion�sio Cerqueira)\", 24),\r\n" + 
					"(9023, \"S�o Pio X (Francisco Beltr�o)\", 18),\r\n" + 
					"(9024, \"S�o Rafael\", 20),\r\n" + 
					"(9025, \"S�o Raimundo da Pedra Menina (Dores do Rio Preto)\", 8),\r\n" + 
					"(9026, \"S�o Raimundo das Mangabeiras\", 10),\r\n" + 
					"(9027, \"S�o Raimundo de Borralhos (Santo Ant�nio do Tau�)\", 14),\r\n" + 
					"(9028, \"S�o Raimundo de Cod� (Cod�)\", 10),\r\n" + 
					"(9029, \"S�o Raimundo do Araguaia (Brejo Grande do Araguaia)\", 14),\r\n" + 
					"(9030, \"S�o Raimundo do Doca Bezerra\", 10),\r\n" + 
					"(9031, \"S�o Raimundo dos Furtados (Camet�)\", 14),\r\n" + 
					"(9032, \"S�o Raimundo Nonato\", 17),\r\n" + 
					"(9033, \"S�o Raimundo (Novo Oriente)\", 6),\r\n" + 
					"(9034, \"S�o Roberto\", 10),\r\n" + 
					"(9035, \"S�o Roberto (Maracan�)\", 14),\r\n" + 
					"(9036, \"S�o Roberto (S�o Jo�o da Lagoa)\", 11),\r\n" + 
					"(9037, \"S�o Rom�o\", 11),\r\n" + 
					"(9038, \"S�o Rom�o (Altaneira)\", 6),\r\n" + 
					"(9039, \"S�o Rom�o (Coxim)\", 12),\r\n" + 
					"(9040, \"S�o Roque\", 26),\r\n" + 
					"(9041, \"S�o Roque (Croat�)\", 6),\r\n" + 
					"(9042, \"S�o Roque da Fartura (�guas da Prata)\", 26),\r\n" + 
					"(9043, \"S�o Roque de Minas\", 11),\r\n" + 
					"(9044, \"S�o Roque do Cana�\", 8),\r\n" + 
					"(9045, \"S�o Roque do Chopim (Pato Branco)\", 18),\r\n" + 
					"(9046, \"S�o Roque (Dois Vizinhos)\", 18),\r\n" + 
					"(9047, \"S�o Roque do Paragua�u (Maragogipe)\", 5),\r\n" + 
					"(9048, \"S�o Roque do Pinhal (Joaquim T�vora)\", 18),\r\n" + 
					"(9049, \"S�o Roque (Marechal C�ndido Rondon)\", 18),\r\n" + 
					"(9050, \"S�o Roque (Passo Fundo)\", 23),\r\n" + 
					"(9051, \"S�o Roque (Realeza)\", 18),\r\n" + 
					"(9052, \"S�o Roque (S�o Louren�o do Oeste)\", 24),\r\n" + 
					"(9053, \"S�o Salvador (Amp�re)\", 18),\r\n" + 
					"(9054, \"S�o Salvador do Tocantins\", 27),\r\n" + 
					"(9055, \"S�o Sebasti�o\", 26),\r\n" + 
					"(9056, \"S�o Sebasti�o\", 2),\r\n" + 
					"(9057, \"S�o Sebasti�o (Baturit�)\", 6),\r\n" + 
					"(9058, \"S�o Sebasti�o (Cari�s)\", 6),\r\n" + 
					"(9059, \"S�o Sebasti�o (Castro)\", 18),\r\n" + 
					"(9060, \"S�o Sebasti�o (Cerro Azul)\", 18),\r\n" + 
					"(9061, \"S�o Sebasti�o da Amoreira\", 18),\r\n" + 
					"(9062, \"S�o Sebasti�o da Barra (Iapu)\", 11),\r\n" + 
					"(9063, \"S�o Sebasti�o da Bela Vista\", 11),\r\n" + 
					"(9064, \"S�o Sebasti�o da Boa Vista\", 14),\r\n" + 
					"(9065, \"S�o Sebasti�o da Grama\", 26),\r\n" + 
					"(9066, \"S�o Sebasti�o da Serra (Brotas)\", 26),\r\n" + 
					"(9067, \"S�o Sebasti�o da Vala (Aimor�s)\", 11),\r\n" + 
					"(9068, \"S�o Sebasti�o da Vargem Alegre\", 11),\r\n" + 
					"(9069, \"S�o Sebasti�o da Vit�ria (S�o Jo�o Del Rei)\", 11),\r\n" + 
					"(9070, \"S�o Sebasti�o de Bra�nas (Belo Oriente)\", 11),\r\n" + 
					"(9071, \"S�o Sebasti�o de Campos (Campos dos Goytacazes)\", 19),\r\n" + 
					"(9072, \"S�o Sebasti�o de Lagoa de Ro�a\", 15),\r\n" + 
					"(9073, \"S�o Sebasti�o de Vi�osa (Chaves)\", 14),\r\n" + 
					"(9074, \"S�o Sebasti�o do Alto\", 19),\r\n" + 
					"(9075, \"S�o Sebasti�o do Anta\", 11),\r\n" + 
					"(9076, \"S�o Sebasti�o do Arvoredo (S�o Joaquim)\", 24),\r\n" + 
					"(9077, \"S�o Sebasti�o do Baixio (A�ucena)\", 11),\r\n" + 
					"(9078, \"S�o Sebasti�o do Barreado (Rio Preto)\", 11),\r\n" + 
					"(9079, \"S�o Sebasti�o do Barreiro (Jampruca)\", 11),\r\n" + 
					"(9080, \"S�o Sebasti�o do Bonsucesso (Concei��o do Mato Dentro)\", 11),\r\n" + 
					"(9081, \"S�o Sebasti�o do Bugre (Coroaci)\", 11),\r\n" + 
					"(9082, \"S�o Sebasti�o do Ca�\", 23),\r\n" + 
					"(9083, \"S�o Sebasti�o do Gil (Desterro de Entre Rios)\", 11),\r\n" + 
					"(9084, \"S�o Sebasti�o do Livramento (Macap�)\", 4),\r\n" + 
					"(9085, \"S�o Sebasti�o do Maranh�o\", 11),\r\n" + 
					"(9086, \"S�o Sebasti�o do �culo (Raul Soares)\", 11),\r\n" + 
					"(9087, \"S�o Sebasti�o do Oeste\", 11),\r\n" + 
					"(9088, \"S�o Sebasti�o do Para�ba (Cantagalo)\", 19),\r\n" + 
					"(9089, \"S�o Sebasti�o do Para�so\", 11),\r\n" + 
					"(9090, \"S�o Sebasti�o do Passe\", 5),\r\n" + 
					"(9091, \"S�o Sebasti�o do Pontal (Carneirinho)\", 11),\r\n" + 
					"(9092, \"S�o Sebasti�o do Rio Preto\", 11),\r\n" + 
					"(9093, \"S�o Sebasti�o do Rio Verde\", 11),\r\n" + 
					"(9094, \"S�o Sebasti�o do Sacramento (Manhua�u)\", 11),\r\n" + 
					"(9095, \"S�o Sebasti�o dos Ferreiros (Vassouras)\", 19),\r\n" + 
					"(9096, \"S�o Sebasti�o do Soberbo (Santa Cruz do Escalvado)\", 11),\r\n" + 
					"(9097, \"S�o Sebasti�o dos Po��es (Montalv�nia)\", 11),\r\n" + 
					"(9098, \"S�o Sebasti�o dos Robertos (Jacutinga)\", 11),\r\n" + 
					"(9099, \"S�o Sebasti�o dos Torres (Barbacena)\", 11),\r\n" + 
					"(9100, \"S�o Sebasti�o do Sul (Lebon R�gis)\", 24),\r\n" + 
					"(9101, \"S�o Sebasti�o do Tocantins\", 27),\r\n" + 
					"(9102, \"S�o Sebasti�o do Uatum�\", 3),\r\n" + 
					"(9103, \"S�o Sebasti�o do Umbuzeiro\", 15),\r\n" + 
					"(9104, \"S�o Sebasti�o (Esmeralda)\", 23),\r\n" + 
					"(9105, \"S�o Sebasti�o (Guarapuava)\", 18),\r\n" + 
					"(9106, \"S�o Sebasti�o (Ibiraiaras)\", 23),\r\n" + 
					"(9107, \"S�o Sebasti�o (Jo�o Pinheiro)\", 11),\r\n" + 
					"(9108, \"S�o Sebasti�o (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(9109, \"S�o Sebasti�o (Tel�maco Borba)\", 18),\r\n" + 
					"(9110, \"S�o Sebasti�o (Vit�ria da Conquista)\", 5),\r\n" + 
					"(9111, \"S�o Sep�\", 23),\r\n" + 
					"(9112, \"S�o Severino (Gravat�)\", 16),\r\n" + 
					"(9113, \"S�o Silvestre (Cruzeiro do Oeste)\", 18),\r\n" + 
					"(9114, \"S�o Sim�o\", 26),\r\n" + 
					"(9115, \"S�o Sim�o\", 9),\r\n" + 
					"(9116, \"S�o Sim�o (Mostardas)\", 23),\r\n" + 
					"(9117, \"S�o Thom� das Letras\", 11),\r\n" + 
					"(9118, \"S�o Tiago\", 11),\r\n" + 
					"(9119, \"S�o Tiago (Gua�u�)\", 8),\r\n" + 
					"(9120, \"S�o Tim�teo (Livramento de Nossa Senhora)\", 5),\r\n" + 
					"(9121, \"S�o Tom�s de Aquino\", 11),\r\n" + 
					"(9122, \"S�o Tom�\", 18),\r\n" + 
					"(9123, \"S�o Tom�\", 20),\r\n" + 
					"(9124, \"S�o Tom� (Itapaj�)\", 6),\r\n" + 
					"(9125, \"S�o Tom� (Macap�)\", 4),\r\n" + 
					"(9126, \"S�o Valentim\", 23),\r\n" + 
					"(9127, \"S�o Valentim (Dois Vizinhos)\", 18),\r\n" + 
					"(9128, \"S�o Valentim do Sul\", 23),\r\n" + 
					"(9129, \"S�o Valentim (Independ�ncia)\", 23),\r\n" + 
					"(9130, \"S�o Valentim (Santa Maria)\", 23),\r\n" + 
					"(9131, \"S�o Valentin da Gruta (Ametista do Sul)\", 23),\r\n" + 
					"(9132, \"S�o Val�rio da Natividade\", 27),\r\n" + 
					"(9133, \"S�o Val�rio do Sul\", 23),\r\n" + 
					"(9134, \"S�o Vendelino\", 23),\r\n" + 
					"(9135, \"S�o Vicente\", 20),\r\n" + 
					"(9136, \"S�o Vicente\", 26),\r\n" + 
					"(9137, \"S�o Vicente (An�polis)\", 9),\r\n" + 
					"(9138, \"S�o Vicente (Araruna)\", 18),\r\n" + 
					"(9139, \"S�o Vicente (Baldim)\", 11),\r\n" + 
					"(9140, \"S�o Vicente (Cuiab�)\", 13),\r\n" + 
					"(9141, \"S�o Vicente da Estrela (Raul Soares)\", 11),\r\n" + 
					"(9142, \"S�o Vicente de Minas\", 11),\r\n" + 
					"(9143, \"S�o Vicente de Paula (Araruama)\", 19),\r\n" + 
					"(9144, \"S�o Vicente do Grama (Jequeri)\", 11),\r\n" + 
					"(9145, \"S�o Vicente do Rio Doce (Tarumirim)\", 11),\r\n" + 
					"(9146, \"S�o Vicente do Serid�\", 15),\r\n" + 
					"(9147, \"S�o Vicente do Sul\", 23),\r\n" + 
					"(9148, \"S�o Vicente Ferrer\", 10),\r\n" + 
					"(9149, \"S�o Vicente Ferrer\", 16),\r\n" + 
					"(9150, \"S�o Vicente (Ic�)\", 6),\r\n" + 
					"(9151, \"S�o Vicente (Itapetim)\", 16),\r\n" + 
					"(9152, \"S�o Vicente (Momba�a)\", 6),\r\n" + 
					"(9153, \"S�o V�tor (Governador Valadares)\", 11),\r\n" + 
					"(9154, \"Sap�\", 15),\r\n" + 
					"(9155, \"Sapea�u\", 5),\r\n" + 
					"(9156, \"Sap� (Tomazina)\", 18),\r\n" + 
					"(9157, \"Sapezal\", 13),\r\n" + 
					"(9158, \"Sapezal (Paragua�u Paulista)\", 26),\r\n" + 
					"(9159, \"Sapiranga\", 23),\r\n" + 
					"(9160, \"Sapiranga (Meleiro)\", 24),\r\n" + 
					"(9161, \"Sapopema\", 18),\r\n" + 
					"(9162, \"Sapo (Santana do Acara�)\", 6),\r\n" + 
					"(9163, \"Sapucaia\", 14),\r\n" + 
					"(9164, \"Sapucaia\", 19),\r\n" + 
					"(9165, \"Sapucaia (Arapiraca)\", 2),\r\n" + 
					"(9166, \"Sapucaia (Atalaia)\", 2),\r\n" + 
					"(9167, \"Sapucaia (Caratinga)\", 11),\r\n" + 
					"(9168, \"Sapucaia de Guanh�es (Guanh�es)\", 11),\r\n" + 
					"(9169, \"Sapucaia do Norte (Galil�ia)\", 11),\r\n" + 
					"(9170, \"Sapucaia do Sul\", 23),\r\n" + 
					"(9171, \"Sapucaia (Maril�ndia)\", 8),\r\n" + 
					"(9172, \"Sapuca� (Jacutinga)\", 11),\r\n" + 
					"(9173, \"Sapuca�-Mirim\", 11),\r\n" + 
					"(9174, \"Sapucarana (Bezerros)\", 16),\r\n" + 
					"(9175, \"Sapupara (Maranguape)\", 6),\r\n" + 
					"(9176, \"Saquarema\", 19),\r\n" + 
					"(9177, \"Saraiva (Santa Cruz do Sul)\", 23),\r\n" + 
					"(9178, \"Sarandi\", 18),\r\n" + 
					"(9179, \"Sarandi\", 23),\r\n" + 
					"(9180, \"Sarandi (Itumbiara)\", 9),\r\n" + 
					"(9181, \"Sarandira (Juiz de Fora)\", 11),\r\n" + 
					"(9182, \"Sarandi (Santa Izabel do Oeste)\", 18),\r\n" + 
					"(9183, \"Sarapu�\", 26),\r\n" + 
					"(9184, \"Sardo�\", 11),\r\n" + 
					"(9185, \"Sarutai�\", 26),\r\n" + 
					"(9186, \"Sarzedo\", 11),\r\n" + 
					"(9187, \"S�tiro Dias\", 5),\r\n" + 
					"(9188, \"Satuba\", 2),\r\n" + 
					"(9189, \"Satubinha\", 10),\r\n" + 
					"(9190, \"Saubara\", 5),\r\n" + 
					"(9191, \"Saudade do Igua�u\", 18),\r\n" + 
					"(9192, \"Saudade (Mar de Espanha)\", 11),\r\n" + 
					"(9193, \"Saudades\", 24),\r\n" + 
					"(9194, \"Saud�vel (Brotas de Maca�bas)\", 5),\r\n" + 
					"(9195, \"Sa�de\", 5),\r\n" + 
					"(9196, \"Sau� (Rio Formoso)\", 16),\r\n" + 
					"(9197, \"Schroeder\", 24),\r\n" + 
					"(9198, \"Seabra\", 5),\r\n" + 
					"(9199, \"Seara\", 24),\r\n" + 
					"(9200, \"Sebastian�polis do Sul\", 26),\r\n" + 
					"(9201, \"Sebasti�o Barros\", 17),\r\n" + 
					"(9202, \"Sebasti�o de Abreu (Pentecoste)\", 6),\r\n" + 
					"(9203, \"Sebasti�o de Lacerda (Vassouras)\", 19),\r\n" + 
					"(9204, \"Sebasti�o Laranjeiras\", 5),\r\n" + 
					"(9205, \"Sebasti�o Leal\", 17),\r\n" + 
					"(9206, \"Seberi\", 23),\r\n" + 
					"(9207, \"Sede Alvorada (Cascavel)\", 18),\r\n" + 
					"(9208, \"Sede Aurora (Quinze de Novembro)\", 23),\r\n" + 
					"(9209, \"Sede Chaparral (Toledo)\", 18),\r\n" + 
					"(9210, \"Sede Independ�ncia (Passo Fundo)\", 23),\r\n" + 
					"(9211, \"Sede Nova\", 23),\r\n" + 
					"(9212, \"Sede Nova Sant'Ana (S�o Jorge D'Oeste)\", 18),\r\n" + 
					"(9213, \"Sede Oldemburg (Palmitos)\", 24),\r\n" + 
					"(9214, \"Sede Progresso (Francisco Beltr�o)\", 18),\r\n" + 
					"(9215, \"Sede Progresso (Ver�)\", 18),\r\n" + 
					"(9216, \"Segredo\", 23),\r\n" + 
					"(9217, \"Segredo (Ip�)\", 23),\r\n" + 
					"(9218, \"Seival (Bag�)\", 23),\r\n" + 
					"(9219, \"Seival (Candiota)\", 23),\r\n" + 
					"(9220, \"Selbach\", 23),\r\n" + 
					"(9221, \"Selma (Jaciara)\", 13),\r\n" + 
					"(9222, \"Selva (Londrina)\", 18),\r\n" + 
					"(9223, \"Selv�ria\", 12),\r\n" + 
					"(9224, \"Sem Peixe\", 11),\r\n" + 
					"(9225, \"Senador Alexandre Costa\", 10),\r\n" + 
					"(9226, \"Senador Amaral\", 11),\r\n" + 
					"(9227, \"Senador Canedo\", 9),\r\n" + 
					"(9228, \"Senador Carlos Jereissati (Pacatuba)\", 6),\r\n" + 
					"(9229, \"Senador Cortes\", 11),\r\n" + 
					"(9230, \"Senador El�i de Souza\", 20),\r\n" + 
					"(9231, \"Senador Firmino\", 11),\r\n" + 
					"(9232, \"Senador Georgino Avelino\", 20),\r\n" + 
					"(9233, \"Senador Guiomard\", 1),\r\n" + 
					"(9234, \"Senador Jos� Bento\", 11),\r\n" + 
					"(9235, \"Senador Jos� Porf�rio\", 14),\r\n" + 
					"(9236, \"Senador La Roque\", 10),\r\n" + 
					"(9237, \"Senador Modestino Gon�alves\", 11),\r\n" + 
					"(9238, \"Senador Mour�o (Diamantina)\", 11),\r\n" + 
					"(9239, \"Senador Pompeu\", 6),\r\n" + 
					"(9240, \"Senador Rui Palmeira\", 2),\r\n" + 
					"(9241, \"Senador S�\", 6),\r\n" + 
					"(9242, \"Senador Salgado Filho\", 23),\r\n" + 
					"(9243, \"Sena Madureira\", 1),\r\n" + 
					"(9244, \"Seng�s\", 18),\r\n" + 
					"(9245, \"Senhora da Gl�ria (Santo Hip�lito)\", 11),\r\n" + 
					"(9246, \"Senhora da Penha (Fernandes Tourinho)\", 11),\r\n" + 
					"(9247, \"Senhora das Dores (Barbacena)\", 11),\r\n" + 
					"(9248, \"Senhora de Oliveira\", 11),\r\n" + 
					"(9249, \"Senhora do Carmo (Itabira)\", 11),\r\n" + 
					"(9250, \"Senhora do Porto\", 11),\r\n" + 
					"(9251, \"Senhora dos Rem�dios\", 11),\r\n" + 
					"(9252, \"Senhor Bom Jesus dos Gramados (Mari�polis)\", 18),\r\n" + 
					"(9253, \"Senhor do Bonfim\", 5),\r\n" + 
					"(9254, \"Sentinela do Sul\", 23),\r\n" + 
					"(9255, \"Sento S�\", 5),\r\n" + 
					"(9256, \"Serafim Schmidt (Sinimbu)\", 23),\r\n" + 
					"(9257, \"Serafina Corr�a\", 23),\r\n" + 
					"(9258, \"Sereno (Cataguases)\", 11),\r\n" + 
					"(9259, \"Sereno de Cima (Ocara)\", 6),\r\n" + 
					"(9260, \"Sergi (S�o Gon�alo dos Campos)\", 5),\r\n" + 
					"(9261, \"Sericita\", 11),\r\n" + 
					"(9262, \"Serid� (S�o Vicente do Serid�)\", 15),\r\n" + 
					"(9263, \"Seringueiras\", 21),\r\n" + 
					"(9264, \"S�rio\", 23),\r\n" + 
					"(9265, \"Seritinga\", 11),\r\n" + 
					"(9266, \"Serop�dica\", 19),\r\n" + 
					"(9267, \"Serra\", 8),\r\n" + 
					"(9268, \"Serra Alta\", 24),\r\n" + 
					"(9269, \"Serra Azul\", 26),\r\n" + 
					"(9270, \"Serra Azul de Minas\", 11),\r\n" + 
					"(9271, \"Serra Azul (Mateus Leme)\", 11),\r\n" + 
					"(9272, \"Serra Bonita (Buritis)\", 11),\r\n" + 
					"(9273, \"Serra Branca\", 15),\r\n" + 
					"(9274, \"Serra Branca (Ipubi)\", 16),\r\n" + 
					"(9275, \"Serra Caiada\", 20),\r\n" + 
					"(9276, \"Serra da Cachoeira (Vertentes)\", 16),\r\n" + 
					"(9277, \"Serra da Canabrava (Uau�)\", 5),\r\n" + 
					"(9278, \"Serra da Canastra (S�o Roque de Minas)\", 11),\r\n" + 
					"(9279, \"Serra da Mandioca (Palmeira dos �ndios)\", 2),\r\n" + 
					"(9280, \"Serra da Massa (Arapiraca)\", 2),\r\n" + 
					"(9281, \"Serra da Raiz\", 15),\r\n" + 
					"(9282, \"Serra da Saudade\", 11),\r\n" + 
					"(9283, \"Serra da Tapuia (S�tio Novo)\", 20),\r\n" + 
					"(9284, \"Serra de S�o Bento\", 20),\r\n" + 
					"(9285, \"Serra do Camapu� (Entre Rios de Minas)\", 11),\r\n" + 
					"(9286, \"Serra do Cip� (Santana do Riacho)\", 11),\r\n" + 
					"(9287, \"Serra do F�lix (Beberibe)\", 6),\r\n" + 
					"(9288, \"Serra do Mel\", 20),\r\n" + 
					"(9289, \"Serra do Navio\", 4),\r\n" + 
					"(9290, \"Serra do Pont�o (Coqueiros do Sul)\", 23),\r\n" + 
					"(9291, \"Serra do Ramalho\", 5),\r\n" + 
					"(9292, \"Serra dos Aimor�s\", 11),\r\n" + 
					"(9293, \"Serra do Salitre\", 11),\r\n" + 
					"(9294, \"Serra do S�o Francisco (Carinhanha)\", 5),\r\n" + 
					"(9295, \"Serra do S�o Jos� (Palmeira dos �ndios)\", 2),\r\n" + 
					"(9296, \"Serra dos Dourados (Umuarama)\", 18),\r\n" + 
					"(9297, \"Serra dos Greg�rios (Esmeralda)\", 23),\r\n" + 
					"(9298, \"Serra dos Lemes (Cabo Verde)\", 11),\r\n" + 
					"(9299, \"Serra Dourada\", 5),\r\n" + 
					"(9300, \"Serra Dourada (Fazenda Nova)\", 9),\r\n" + 
					"(9301, \"Serra do Vento (Belo Jardim)\", 16),\r\n" + 
					"(9302, \"Serragem (Ocara)\", 6),\r\n" + 
					"(9303, \"Serra Grande\", 15),\r\n" + 
					"(9304, \"Serra Grande (C�cero Dantas)\", 5),\r\n" + 
					"(9305, \"Serra Grande (Valen�a)\", 5),\r\n" + 
					"(9306, \"Serrana\", 26),\r\n" + 
					"(9307, \"Serra Negra\", 26),\r\n" + 
					"(9308, \"Serra Negra (Bezerros)\", 16),\r\n" + 
					"(9309, \"Serra Negra do Norte\", 20),\r\n" + 
					"(9310, \"Serra Negra (Guaraque�aba)\", 18),\r\n" + 
					"(9311, \"Serrania\", 11),\r\n" + 
					"(9312, \"Serrano do Maranh�o\", 10),\r\n" + 
					"(9313, \"Serran�polis\", 9),\r\n" + 
					"(9314, \"Serran�polis de Minas\", 11),\r\n" + 
					"(9315, \"Serran�polis do Igua�u\", 18),\r\n" + 
					"(9316, \"Serranos\", 11),\r\n" + 
					"(9317, \"Serra Nova Dourada\", 13),\r\n" + 
					"(9318, \"Serra Nova (Rio Pardo de Minas)\", 11),\r\n" + 
					"(9319, \"Serra Pelada (Afonso Cl�udio)\", 8),\r\n" + 
					"(9320, \"Serra Pelada (Marab�)\", 14),\r\n" + 
					"(9321, \"Serra Preta\", 5),\r\n" + 
					"(9322, \"Serra Redonda\", 15),\r\n" + 
					"(9323, \"Serraria\", 15),\r\n" + 
					"(9324, \"Serraria Klas (Ponta Grossa)\", 18),\r\n" + 
					"(9325, \"Serra Talhada\", 16),\r\n" + 
					"(9326, \"Serrinha\", 5),\r\n" + 
					"(9327, \"Serrinha\", 20),\r\n" + 
					"(9328, \"Serrinha (Camb�)\", 18),\r\n" + 
					"(9329, \"Serrinha (Campos dos Goytacazes)\", 19),\r\n" + 
					"(9330, \"Serrinha (Castro)\", 18),\r\n" + 
					"(9331, \"Serrinha da Prata (Salo�)\", 16),\r\n" + 
					"(9332, \"Serrinha dos Pintos\", 20),\r\n" + 
					"(9333, \"Serrinha (S�o Luiz Gonzaga)\", 23),\r\n" + 
					"(9334, \"Serrinha Velha (Segredo)\", 23),\r\n" + 
					"(9335, \"Serrita\", 16),\r\n" + 
					"(9336, \"Serro\", 11),\r\n" + 
					"(9337, \"Serrol�ndia\", 5),\r\n" + 
					"(9338, \"Serrol�ndia (Ipubi)\", 16),\r\n" + 
					"(9339, \"Serrota (Barro)\", 6),\r\n" + 
					"(9340, \"Serrota (Senador S�)\", 6),\r\n" + 
					"(9341, \"Serrote do Meio (Itapaj�)\", 6),\r\n" + 
					"(9342, \"Serrote dos Bois de Cima (Caruaru)\", 16),\r\n" + 
					"(9343, \"Serrote do Urubu (Petrolina)\", 16),\r\n" + 
					"(9344, \"Serrote (Iguatu)\", 6),\r\n" + 
					"(9345, \"Serrote (S�o Gon�alo do Amarante)\", 6),\r\n" + 
					"(9346, \"Sertaneja\", 18),\r\n" + 
					"(9347, \"Sert�nia\", 16),\r\n" + 
					"(9348, \"Sertan�polis\", 18),\r\n" + 
					"(9349, \"Sert�o\", 23),\r\n" + 
					"(9350, \"Sert�o Santana\", 23),\r\n" + 
					"(9351, \"Sert�ozinho\", 26),\r\n" + 
					"(9352, \"Sert�ozinho\", 15),\r\n" + 
					"(9353, \"Sert�ozinho (Alpestre)\", 23),\r\n" + 
					"(9354, \"Sert�ozinho (Borda da Mata)\", 11),\r\n" + 
					"(9355, \"Sert�ozinho de Baixo (Maraial)\", 16),\r\n" + 
					"(9356, \"Sert�ozinho (Engenheiro Beltr�o)\", 18),\r\n" + 
					"(9357, \"Sete Barras\", 26),\r\n" + 
					"(9358, \"Sete Cachoeiras (Ferros)\", 11),\r\n" + 
					"(9359, \"Sete de Setembro\", 23),\r\n" + 
					"(9360, \"Sete de Setembro (Erval Grande)\", 23),\r\n" + 
					"(9361, \"Sete de Setembro (Santa Rosa)\", 23),\r\n" + 
					"(9362, \"Sete Lagoas\", 11),\r\n" + 
					"(9363, \"Sete Lagoas (Itatiba do Sul)\", 23),\r\n" + 
					"(9364, \"Sete Quedas\", 12),\r\n" + 
					"(9365, \"Setubinha\", 11),\r\n" + 
					"(9366, \"Severiano de Almeida\", 23),\r\n" + 
					"(9367, \"Severiano Melo\", 20),\r\n" + 
					"(9368, \"Sever�nia\", 26),\r\n" + 
					"(9369, \"Sider�polis\", 24),\r\n" + 
					"(9370, \"Sidrol�ndia\", 12),\r\n" + 
					"(9371, \"Sigefredo Pacheco\", 17),\r\n" + 
					"(9372, \"Silva Campos (Pomp�u)\", 11),\r\n" + 
					"(9373, \"Silva Jardim\", 19),\r\n" + 
					"(9374, \"Silva Jardim (Serafina Corr�a)\", 23),\r\n" + 
					"(9375, \"Silv�nia\", 9),\r\n" + 
					"(9376, \"Silvano (Patroc�nio)\", 11),\r\n" + 
					"(9377, \"Silvan�polis\", 27),\r\n" + 
					"(9378, \"Silva Xavier (Sete Lagoas)\", 11),\r\n" + 
					"(9379, \"Silveira Carvalho (Bar�o de Monte Alto)\", 11),\r\n" + 
					"(9380, \"Silveira Martins\", 23),\r\n" + 
					"(9381, \"Silveir�nia\", 11),\r\n" + 
					"(9382, \"Silveiras\", 26),\r\n" + 
					"(9383, \"Silveira (S�o Jos� dos Ausentes)\", 23),\r\n" + 
					"(9384, \"Silves\", 3),\r\n" + 
					"(9385, \"Silvestre (Vi�osa)\", 11),\r\n" + 
					"(9386, \"Silvian�polis\", 11),\r\n" + 
					"(9387, \"Sim�o Campos (S�o Jo�o da Ponte)\", 11),\r\n" + 
					"(9388, \"Sim�o Dias\", 25),\r\n" + 
					"(9389, \"Sim�o Pereira\", 11),\r\n" + 
					"(9390, \"Sim�o (Porteiras)\", 6),\r\n" + 
					"(9391, \"Sim�es\", 17),\r\n" + 
					"(9392, \"Sim�es (Cafel�ndia)\", 26),\r\n" + 
					"(9393, \"Sim�es Filho\", 5),\r\n" + 
					"(9394, \"Simol�ndia\", 9),\r\n" + 
					"(9395, \"Simon�sia\", 11),\r\n" + 
					"(9396, \"Simpl�cio Mendes\", 17),\r\n" + 
					"(9397, \"Sinimbu\", 23),\r\n" + 
					"(9398, \"Sinop\", 13),\r\n" + 
					"(9399, \"Siqueira Belo (Barrac�o)\", 18),\r\n" + 
					"(9400, \"Siqueira Campos\", 18),\r\n" + 
					"(9401, \"Siriji (S�o Vicente Ferrer)\", 16),\r\n" + 
					"(9402, \"Sirinha�m\", 16),\r\n" + 
					"(9403, \"S�rio (Santo Cristo)\", 23),\r\n" + 
					"(9404, \"Siriri\", 25),\r\n" + 
					"(9405, \"Siti� (Banabui�)\", 6),\r\n" + 
					"(9406, \"S�tio Alto (Arroio do Tigre)\", 23),\r\n" + 
					"(9407, \"S�tio Cassemiro (Redentora)\", 23),\r\n" + 
					"(9408, \"S�tio D'Abadia\", 9),\r\n" + 
					"(9409, \"S�tio da Bara�na (Andorinha)\", 5),\r\n" + 
					"(9410, \"S�tio do Herval (Lagoa Vermelha)\", 23),\r\n" + 
					"(9411, \"S�tio do Mato\", 5),\r\n" + 
					"(9412, \"S�tio do Meio (Castro Alves)\", 5),\r\n" + 
					"(9413, \"S�tio do Quinto\", 5),\r\n" + 
					"(9414, \"S�tio dos Nunes (Flores)\", 16),\r\n" + 
					"(9415, \"S�tio dos Rem�dios (Bezerros)\", 16),\r\n" + 
					"(9416, \"S�tio Gabriel (Miragua�)\", 23),\r\n" + 
					"(9417, \"S�tio Grande (S�o Desid�rio)\", 5),\r\n" + 
					"(9418, \"S�tio Novo\", 10),\r\n" + 
					"(9419, \"S�tio Novo\", 20),\r\n" + 
					"(9420, \"S�tio Novo (Catu)\", 5),\r\n" + 
					"(9421, \"S�tio Novo do Tocantins\", 27),\r\n" + 
					"(9422, \"S�tios Novos (Caucaia)\", 6),\r\n" + 
					"(9423, \"Siup� (S�o Gon�alo do Amarante)\", 6),\r\n" + 
					"(9424, \"Soares (Am�rica Dourada)\", 5),\r\n" + 
					"(9425, \"Sobradinho\", 23),\r\n" + 
					"(9426, \"Sobradinho\", 5),\r\n" + 
					"(9427, \"Sobrado\", 15),\r\n" + 
					"(9428, \"Sobral\", 6),\r\n" + 
					"(9429, \"Sobr�lia\", 11),\r\n" + 
					"(9430, \"Sobral Pinto (Astolfo Dutra)\", 11),\r\n" + 
					"(9431, \"Sobreiro (Laranja da Terra)\", 8),\r\n" + 
					"(9432, \"Socav�o (Castro)\", 18),\r\n" + 
					"(9433, \"Socorro\", 26),\r\n" + 
					"(9434, \"Socorro do Piau�\", 17),\r\n" + 
					"(9435, \"Sodrel�ndia (Trajano de Moraes)\", 19),\r\n" + 
					"(9436, \"Sodr�lia (Santa Cruz do Rio Pardo)\", 26),\r\n" + 
					"(9437, \"Sol�nea\", 15),\r\n" + 
					"(9438, \"Soledade\", 23),\r\n" + 
					"(9439, \"Soledade\", 15),\r\n" + 
					"(9440, \"Soledade de Minas\", 11),\r\n" + 
					"(9441, \"Soledade (Itapaj�)\", 6),\r\n" + 
					"(9442, \"Solid�o\", 16),\r\n" + 
					"(9443, \"Solon�pole\", 6),\r\n" + 
					"(9444, \"Sombrio\", 24),\r\n" + 
					"(9445, \"Sonho Azul (Mirassol D'Oeste)\", 13),\r\n" + 
					"(9446, \"Sonora\", 12),\r\n" + 
					"(9447, \"Sooretama\", 8),\r\n" + 
					"(9448, \"Sopa (Diamantina)\", 11),\r\n" + 
					"(9449, \"Sorocaba\", 26),\r\n" + 
					"(9450, \"Sorocaba do Sul (Bigua�u)\", 24),\r\n" + 
					"(9451, \"Sorriso\", 13),\r\n" + 
					"(9452, \"Sossego\", 15),\r\n" + 
					"(9453, \"Sossego (Santa Maria Madalena)\", 19),\r\n" + 
					"(9454, \"Sossego (Santo �ngelo)\", 23),\r\n" + 
					"(9455, \"Soure\", 14),\r\n" + 
					"(9456, \"Sousa\", 15),\r\n" + 
					"(9457, \"Sous�nia (An�polis)\", 9),\r\n" + 
					"(9458, \"Souto Neto (Ponte Preta)\", 23),\r\n" + 
					"(9459, \"Souto Soares\", 5),\r\n" + 
					"(9460, \"Souzal�ndia (Barro Alto)\", 9),\r\n" + 
					"(9461, \"Souza Ramos (Get�lio Vargas)\", 23),\r\n" + 
					"(9462, \"Suassurana (Iguatu)\", 6),\r\n" + 
					"(9463, \"Subaio (Cachoeiras de Macacu)\", 19),\r\n" + 
					"(9464, \"Suba�ma (Entre Rios)\", 5),\r\n" + 
					"(9465, \"Sucanga (Pot�)\", 11),\r\n" + 
					"(9466, \"Sucatinga (Beberibe)\", 6),\r\n" + 
					"(9467, \"Sucesso (Tamboril)\", 6),\r\n" + 
					"(9468, \"Sucupira\", 27),\r\n" + 
					"(9469, \"Sucupira do Norte\", 10),\r\n" + 
					"(9470, \"Sucupira do Riach�o\", 10),\r\n" + 
					"(9471, \"Sucuriju (Amap�)\", 4),\r\n" + 
					"(9472, \"Sucuru (Serra Branca)\", 15),\r\n" + 
					"(9473, \"Sud Mennucci\", 26),\r\n" + 
					"(9474, \"Suinana (Altair)\", 26),\r\n" + 
					"(9475, \"Sul Brasil\", 24),\r\n" + 
					"(9476, \"Sulina\", 18),\r\n" + 
					"(9477, \"Sumar�\", 26),\r\n" + 
					"(9478, \"Sum�\", 15),\r\n" + 
					"(9479, \"Sumidouro\", 19),\r\n" + 
					"(9480, \"Sumidouro (Diamantino)\", 13),\r\n" + 
					"(9481, \"Surubim\", 16),\r\n" + 
					"(9482, \"Suspiro (S�o Gabriel)\", 23),\r\n" + 
					"(9483, \"Sussuanha (Guaraciaba do Norte)\", 6),\r\n" + 
					"(9484, \"Sussuapara\", 17),\r\n" + 
					"(9485, \"Sussuarana (Tanha�u)\", 5),\r\n" + 
					"(9486, \"Sussui (Engenheiro Beltr�o)\", 18),\r\n" + 
					"(9487, \"Sussui (Palmital)\", 26),\r\n" + 
					"(9488, \"Sutis (Londrina)\", 18),\r\n" + 
					"(9489, \"Suzan�polis\", 26),\r\n" + 
					"(9490, \"Suzano\", 26),\r\n" + 
					"(9491, \"Taba�\", 23),\r\n" + 
					"(9492, \"Tabainha (Tiangu�)\", 6),\r\n" + 
					"(9493, \"Tabajara (Inhapim)\", 11),\r\n" + 
					"(9494, \"Tabajara (Lav�nia)\", 26),\r\n" + 
					"(9495, \"Tabajara (Machadinho D'Oeste)\", 21),\r\n" + 
					"(9496, \"Tabajara (Salto do Jacu�)\", 23),\r\n" + 
					"(9497, \"Tabapor�\", 13),\r\n" + 
					"(9498, \"Tabapu�\", 26),\r\n" + 
					"(9499, \"Tabatinga\", 26),\r\n" + 
					"(9500, \"Tabatinga\", 3),\r\n" + 
					"(9501, \"Taba�na (Aimor�s)\", 11),\r\n" + 
					"(9502, \"Tabira\", 16),\r\n" + 
					"(9503, \"Tabo�o da Serra\", 26),\r\n" + 
					"(9504, \"Taboas (Rio das Flores)\", 19),\r\n" + 
					"(9505, \"Taboca (Cant�)\", 22),\r\n" + 
					"(9506, \"Tabocas (Arapiraca)\", 2),\r\n" + 
					"(9507, \"Tabocas do Brejo Velho\", 5),\r\n" + 
					"(9508, \"Tabocas (Exu)\", 16),\r\n" + 
					"(9509, \"Taboleiro (Antonina do Norte)\", 6),\r\n" + 
					"(9510, \"Taboleiro do Castro (Varzedo)\", 5),\r\n" + 
					"(9511, \"Taboleiro do Pinto (Rio Largo)\", 2),\r\n" + 
					"(9512, \"Taboleiro Grande\", 20),\r\n" + 
					"(9513, \"Taboquinha (Arapiraca)\", 2),\r\n" + 
					"(9514, \"Taboquinha (Belo Jardim)\", 16),\r\n" + 
					"(9515, \"Taboquinhas (Itacar�)\", 5),\r\n" + 
					"(9516, \"Tabu�o (Bom Jardim de Minas)\", 11),\r\n" + 
					"(9517, \"Tabuleiro\", 11),\r\n" + 
					"(9518, \"Tabuleiro do Norte\", 6),\r\n" + 
					"(9519, \"Tacaimb�\", 16),\r\n" + 
					"(9520, \"Tacaratu\", 16),\r\n" + 
					"(9521, \"Taciba\", 26),\r\n" + 
					"(9522, \"Tacima\", 15),\r\n" + 
					"(9523, \"Tacuru\", 12),\r\n" + 
					"(9524, \"Tagu� (Cotegipe)\", 5),\r\n" + 
					"(9525, \"Tagua�\", 26),\r\n" + 
					"(9526, \"Taguatinga\", 27),\r\n" + 
					"(9527, \"Taia�u\", 26),\r\n" + 
					"(9528, \"Ta�ba (S�o Gon�alo do Amarante)\", 6),\r\n" + 
					"(9529, \"Tail�ndia\", 14),\r\n" + 
					"(9530, \"Taim (Rio Grande)\", 23),\r\n" + 
					"(9531, \"Taim (Santa Vit�ria do Palmar)\", 23),\r\n" + 
					"(9532, \"Tainhas (S�o Francisco de Paula)\", 23),\r\n" + 
					"(9533, \"Tai�\", 24),\r\n" + 
					"(9534, \"Taiobeiras\", 11),\r\n" + 
					"(9535, \"Taipa (Guarapuava)\", 18),\r\n" + 
					"(9536, \"Taipas do Tocantins\", 27),\r\n" + 
					"(9537, \"Taipu\", 20),\r\n" + 
					"(9538, \"Tai�va\", 26),\r\n" + 
					"(9539, \"Talism�\", 27),\r\n" + 
					"(9540, \"Tamandar�\", 16),\r\n" + 
					"(9541, \"Tamandar� (Parana�ba)\", 12),\r\n" + 
					"(9542, \"Tamandu� (Marques de Souza)\", 23),\r\n" + 
					"(9543, \"Tamandu� (Segredo)\", 23),\r\n" + 
					"(9544, \"Tamarana\", 18),\r\n" + 
					"(9545, \"Tambarutaca (Paranagu�)\", 18),\r\n" + 
					"(9546, \"Tamba�\", 26),\r\n" + 
					"(9547, \"Tambauzinho (Santa Rita)\", 15),\r\n" + 
					"(9548, \"Tamboara\", 18),\r\n" + 
					"(9549, \"Tamboat� (Bom Jardim)\", 16),\r\n" + 
					"(9550, \"Tamboril\", 6),\r\n" + 
					"(9551, \"Tamboril do Piau�\", 17),\r\n" + 
					"(9552, \"Tamboril (Nazar�)\", 27),\r\n" + 
					"(9553, \"Tamburil (Morro do Chap�u)\", 5),\r\n" + 
					"(9555, \"Tanabi\", 26),\r\n" + 
					"(9556, \"Tangar�\", 24),\r\n" + 
					"(9557, \"Tangar�\", 20),\r\n" + 
					"(9558, \"Tangar� da Serra\", 13),\r\n" + 
					"(9559, \"Tangu�\", 19),\r\n" + 
					"(9560, \"Tanha�u\", 5),\r\n" + 
					"(9561, \"Tanque D'Arca\", 2),\r\n" + 
					"(9562, \"Tanque do Piau�\", 17),\r\n" + 
					"(9563, \"Tanque Grande (Castro)\", 18),\r\n" + 
					"(9564, \"Tanque Novo\", 5),\r\n" + 
					"(9565, \"Tanque (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(9566, \"Tanques (Maranguape)\", 6),\r\n" + 
					"(9567, \"Tanques (Po�o Dantas)\", 15),\r\n" + 
					"(9568, \"Tanquinho\", 5),\r\n" + 
					"(9569, \"Tanquinho do Po�o (Andorinha)\", 5),\r\n" + 
					"(9570, \"Taparuba\", 11),\r\n" + 
					"(9571, \"Tapau�\", 3),\r\n" + 
					"(9572, \"Tapejara\", 18),\r\n" + 
					"(9573, \"Tapejara\", 23),\r\n" + 
					"(9574, \"Tapera\", 23),\r\n" + 
					"(9575, \"Tapera (Aquiraz)\", 6),\r\n" + 
					"(9576, \"Tapera (Tavares)\", 23),\r\n" + 
					"(9577, \"Tapera (Trajano de Moraes)\", 19),\r\n" + 
					"(9578, \"Tapero�\", 15),\r\n" + 
					"(9579, \"Tapero�\", 5),\r\n" + 
					"(9580, \"Taperuaba (Sobral)\", 6),\r\n" + 
					"(9581, \"Tapes\", 23),\r\n" + 
					"(9582, \"Tapinas (It�polis)\", 26),\r\n" + 
					"(9583, \"Tapira\", 11),\r\n" + 
					"(9584, \"Tapira\", 18),\r\n" + 
					"(9585, \"Tapira�\", 11),\r\n" + 
					"(9586, \"Tapira�\", 26),\r\n" + 
					"(9587, \"Tapiraim (S�o Caitano)\", 16),\r\n" + 
					"(9588, \"Tapira�pe (Ruy Barbosa)\", 5),\r\n" + 
					"(9589, \"Tapirama (Gongogi)\", 5),\r\n" + 
					"(9590, \"Tapiramut�\", 5),\r\n" + 
					"(9591, \"Tapiranga (Miguel Calmon)\", 5),\r\n" + 
					"(9592, \"Tapirapua (Barra do Bugres)\", 13),\r\n" + 
					"(9593, \"Tapiratiba\", 26),\r\n" + 
					"(9594, \"Tap�ia (Camamu)\", 5),\r\n" + 
					"(9595, \"Tapuiara (Quixad�)\", 6),\r\n" + 
					"(9596, \"Tapuio (Carir�)\", 6),\r\n" + 
					"(9597, \"Tapuirama (Uberl�ndia)\", 11),\r\n" + 
					"(9598, \"Tapui (Toledo)\", 18),\r\n" + 
					"(9599, \"Tapurah\", 13),\r\n" + 
					"(9600, \"Taquara\", 23),\r\n" + 
					"(9601, \"Taquara (Arapiraca)\", 2),\r\n" + 
					"(9602, \"Taquara (Castro)\", 18),\r\n" + 
					"(9603, \"Taquara�u de Minas\", 11),\r\n" + 
					"(9604, \"Taquaral\", 26),\r\n" + 
					"(9605, \"Taquaral (Arroio do Tigre)\", 23),\r\n" + 
					"(9606, \"Taquaral de Goi�s\", 9),\r\n" + 
					"(9607, \"Taquaral de Guanh�es (Guanh�es)\", 11),\r\n" + 
					"(9608, \"Taquaral (Rinc�o)\", 26),\r\n" + 
					"(9609, \"Taquaral (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(9611, \"Taquarana\", 2),\r\n" + 
					"(9612, \"Taquaras (Rancho Queimado)\", 24),\r\n" + 
					"(9613, \"Taquara Verde (Ca�ador)\", 24),\r\n" + 
					"(9614, \"Taquarendi (Mirangaba)\", 5),\r\n" + 
					"(9615, \"Taquari\", 23),\r\n" + 
					"(9616, \"Taquarichim (Jaguari)\", 23),\r\n" + 
					"(9617, \"Taquari (Coxim)\", 12),\r\n" + 
					"(9618, \"Taquari dos Polacos (Ponta Grossa)\", 18),\r\n" + 
					"(9619, \"Taquari dos Russos (Ponta Grossa)\", 18),\r\n" + 
					"(9620, \"Taquarinha (Mucuri)\", 5),\r\n" + 
					"(9621, \"Taquaritinga\", 26),\r\n" + 
					"(9622, \"Taquaritinga do Norte\", 16),\r\n" + 
					"(9623, \"Taquarituba\", 26),\r\n" + 
					"(9624, \"Taquariva�\", 26),\r\n" + 
					"(9625, \"Taquaru�u do Sul\", 23),\r\n" + 
					"(9626, \"Taquaruna (Londrina)\", 18),\r\n" + 
					"(9627, \"Taquarussu\", 12),\r\n" + 
					"(9628, \"Tarabai\", 26),\r\n" + 
					"(9629, \"Tara (Pedra)\", 16),\r\n" + 
					"(9630, \"Tarauac�\", 1),\r\n" + 
					"(9631, \"Targinos (Canind�)\", 6),\r\n" + 
					"(9632, \"Taril�ndia (Jaru)\", 21),\r\n" + 
					"(9633, \"Tarituba (Parati)\", 19),\r\n" + 
					"(9634, \"Tarrafas\", 6),\r\n" + 
					"(9635, \"Tartarugalzinho\", 4),\r\n" + 
					"(9636, \"Tartaruga (Milagres)\", 5),\r\n" + 
					"(9637, \"Tarua�u (S�o Jo�o Nepomuceno)\", 11),\r\n" + 
					"(9638, \"Tarum�\", 26),\r\n" + 
					"(9639, \"Tarumirim\", 11),\r\n" + 
					"(9640, \"Tasso Fragoso\", 10),\r\n" + 
					"(9641, \"Tatuamunha (Porto de Pedras)\", 2),\r\n" + 
					"(9642, \"Tatu�\", 26),\r\n" + 
					"(9643, \"Tatuteua (Maracan�)\", 14),\r\n" + 
					"(9644, \"Tau�\", 6),\r\n" + 
					"(9645, \"Tauap� (Lic�nio de Almeida)\", 5),\r\n" + 
					"(9646, \"Tauapiranga (Serra Talhada)\", 16),\r\n" + 
					"(9647, \"Tauari (Capanema)\", 14),\r\n" + 
					"(9648, \"Tauarizinho (Peixe-Boi)\", 14),\r\n" + 
					"(9649, \"Taubat�\", 26),\r\n" + 
					"(9650, \"Taunay (Aquidauana)\", 12),\r\n" + 
					"(9651, \"Tavares\", 23),\r\n" + 
					"(9652, \"Tavares\", 15),\r\n" + 
					"(9653, \"Taveira (Niquel�ndia)\", 9),\r\n" + 
					"(9654, \"Tebas (Leopoldina)\", 11),\r\n" + 
					"(9655, \"Tecainda (Martin�polis)\", 26),\r\n" + 
					"(9656, \"Tef�\", 3),\r\n" + 
					"(9657, \"Teixeira\", 15),\r\n" + 
					"(9658, \"Teixeira de Freitas\", 5),\r\n" + 
					"(9659, \"Teixeiras\", 11),\r\n" + 
					"(9660, \"Teixeira Soares\", 18),\r\n" + 
					"(9661, \"Teixeir�polis\", 21),\r\n" + 
					"(9662, \"Tejuco (Janu�ria)\", 11),\r\n" + 
					"(9663, \"Teju�uoca\", 6),\r\n" + 
					"(9664, \"Tejucupapo (Goiana)\", 16),\r\n" + 
					"(9665, \"Tejup�\", 26),\r\n" + 
					"(9666, \"Tel�maco Borba\", 18),\r\n" + 
					"(9667, \"Telha\", 25),\r\n" + 
					"(9668, \"Tenebre (Pedra)\", 16),\r\n" + 
					"(9669, \"Tenente Ananias\", 20),\r\n" + 
					"(9670, \"Tenente Laurentino Cruz\", 20),\r\n" + 
					"(9671, \"Tenente Portela\", 23),\r\n" + 
					"(9672, \"Ten�rio\", 15),\r\n" + 
					"(9673, \"Tentugal (Our�m)\", 14),\r\n" + 
					"(9674, \"Teodoro Sampaio\", 26),\r\n" + 
					"(9675, \"Teodoro Sampaio\", 5),\r\n" + 
					"(9676, \"Teofil�ndia\", 5),\r\n" + 
					"(9677, \"Te�filo Otoni\", 11),\r\n" + 
					"(9678, \"Teol�ndia\", 5),\r\n" + 
					"(9679, \"Teol�ndia (Pato Branco)\", 18),\r\n" + 
					"(9680, \"Teot�nio Vilela\", 2),\r\n" + 
					"(9681, \"Terenos\", 12),\r\n" + 
					"(9682, \"Teresina\", 17),\r\n" + 
					"(9683, \"Teresina de Goi�s\", 9),\r\n" + 
					"(9684, \"Teres�polis\", 19),\r\n" + 
					"(9685, \"Tereza Breda (Barbosa Ferraz)\", 18),\r\n" + 
					"(9686, \"Tereza Cristina (C�ndido de Abreu)\", 18),\r\n" + 
					"(9687, \"Terezinha\", 16),\r\n" + 
					"(9688, \"Terez�polis de Goi�s\", 9),\r\n" + 
					"(9689, \"Termas de Ibir� (Ibir�)\", 26),\r\n" + 
					"(9690, \"Terra Alta\", 14),\r\n" + 
					"(9691, \"Terra Boa\", 18),\r\n" + 
					"(9692, \"Terra Branca (Bocai�va)\", 11),\r\n" + 
					"(9693, \"Terra de Areia\", 23),\r\n" + 
					"(9694, \"Terra Nova\", 5),\r\n" + 
					"(9695, \"Terra Nova\", 16),\r\n" + 
					"(9696, \"Terra Nova (Careiro)\", 3),\r\n" + 
					"(9697, \"Terra Nova D'Oeste (Santa Mercedes)\", 26),\r\n" + 
					"(9698, \"Terra Nova do Norte\", 13),\r\n" + 
					"(9699, \"Terra Nova (S�o Jer�nimo da Serra)\", 18),\r\n" + 
					"(9700, \"Terra Rica\", 18),\r\n" + 
					"(9701, \"Terra Roxa\", 26),\r\n" + 
					"(9702, \"Terra Roxa\", 18),\r\n" + 
					"(9703, \"Terra Roxa (Ju�na)\", 13),\r\n" + 
					"(9704, \"Terra Santa\", 14),\r\n" + 
					"(9705, \"Terra Vermelha (Caruaru)\", 16),\r\n" + 
					"(9706, \"Tesouras (Chapada)\", 23),\r\n" + 
					"(9707, \"Tesouro\", 13),\r\n" + 
					"(9708, \"Teut�nia\", 23),\r\n" + 
					"(9709, \"Theobroma\", 21),\r\n" + 
					"(9710, \"Tiangu�\", 6),\r\n" + 
					"(9711, \"Tiaraju (S�o Gabriel)\", 23),\r\n" + 
					"(9712, \"Tibagi\", 18),\r\n" + 
					"(9713, \"Tibau\", 20),\r\n" + 
					"(9714, \"Tibau do Sul\", 20),\r\n" + 
					"(9715, \"Tibiri�� (Bauru)\", 26),\r\n" + 
					"(9716, \"Tibiri�� do Paranapanema (Piraju)\", 26),\r\n" + 
					"(9717, \"Tiet�\", 26),\r\n" + 
					"(9718, \"Tigipi� (S�o Jo�o Batista)\", 24),\r\n" + 
					"(9719, \"Tigrinhos\", 24),\r\n" + 
					"(9720, \"Tijoca (Bragan�a)\", 14),\r\n" + 
					"(9721, \"Tijua�� (Senhor do Bonfim)\", 5),\r\n" + 
					"(9722, \"Tijucas\", 24),\r\n" + 
					"(9723, \"Tijucas do Sul\", 18),\r\n" + 
					"(9724, \"Tijuco Preto (Almirante Tamandar�)\", 18),\r\n" + 
					"(9725, \"Tijuco Preto (Castro)\", 18),\r\n" + 
					"(9726, \"Timba�ba\", 16),\r\n" + 
					"(9727, \"Timba�ba dos Batistas\", 20),\r\n" + 
					"(9728, \"Timba�ba dos Marinheiros (Chorozinho)\", 6),\r\n" + 
					"(9729, \"Timba�va (Bossoroca)\", 23),\r\n" + 
					"(9730, \"Timb� do Sul\", 24),\r\n" + 
					"(9731, \"Timbiras\", 10),\r\n" + 
					"(9732, \"Timb�\", 24),\r\n" + 
					"(9733, \"Timb� Grande\", 24),\r\n" + 
					"(9734, \"Timboteua (Nova Timboteua)\", 14),\r\n" + 
					"(9735, \"Timbu� (Fund�o)\", 8),\r\n" + 
					"(9736, \"Timburi\", 26),\r\n" + 
					"(9737, \"Timbu Velho (Colombo)\", 18),\r\n" + 
					"(9738, \"Timon\", 10),\r\n" + 
					"(9739, \"Timonha (Granja)\", 6),\r\n" + 
					"(9740, \"Timorante (Exu)\", 16),\r\n" + 
					"(9741, \"Tim�teo\", 11),\r\n" + 
					"(9742, \"Tindiquera (Arauc�ria)\", 18),\r\n" + 
					"(9743, \"Tio Hugo\", 23),\r\n" + 
					"(9744, \"Tipi (Aurora)\", 6),\r\n" + 
					"(9745, \"Tiquaru�u (Feira de Santana)\", 5),\r\n" + 
					"(9746, \"Tiradentes\", 11),\r\n" + 
					"(9747, \"Tiradentes do Sul\", 23),\r\n" + 
					"(9748, \"Tiradentes (Pato Branco)\", 18),\r\n" + 
					"(9749, \"Tiradentes (Salgado Filho)\", 18),\r\n" + 
					"(9750, \"Tiros\", 11),\r\n" + 
					"(9751, \"Tobati (Ibi�)\", 11),\r\n" + 
					"(9752, \"Tobias Barreto\", 25),\r\n" + 
					"(9753, \"Tocandira (Porteirinha)\", 11),\r\n" + 
					"(9754, \"Tocant�nia\", 27),\r\n" + 
					"(9755, \"Tocantin�polis\", 27),\r\n" + 
					"(9756, \"Tocantins\", 11),\r\n" + 
					"(9757, \"Tocos (Campos dos Goytacazes)\", 19),\r\n" + 
					"(9758, \"Tocos do Moji\", 11),\r\n" + 
					"(9759, \"Todos os Santos (Guarapari)\", 8),\r\n" + 
					"(9760, \"Toledo\", 11),\r\n" + 
					"(9761, \"Toledo\", 18),\r\n" + 
					"(9762, \"Toledos (Juiz de Fora)\", 11),\r\n" + 
					"(9763, \"Toledo (Tup�)\", 26),\r\n" + 
					"(9764, \"Tomar do Geru\", 25),\r\n" + 
					"(9765, \"Tom�s Gonzaga (Curvelo)\", 11),\r\n" + 
					"(9766, \"Tomaz Coelho (Arauc�ria)\", 18),\r\n" + 
					"(9767, \"Tomazina\", 18),\r\n" + 
					"(9768, \"Tombos\", 11),\r\n" + 
					"(9769, \"Tom�-A��\", 14),\r\n" + 
					"(9770, \"Tom� (Quixer�)\", 6),\r\n" + 
					"(9771, \"Tonantins\", 3),\r\n" + 
					"(9772, \"Top�zio (Te�filo Otoni)\", 11),\r\n" + 
					"(9773, \"Toricueyje (Barra do Gar�as)\", 13),\r\n" + 
					"(9774, \"Toritama\", 16),\r\n" + 
					"(9775, \"Torixor�u\", 13),\r\n" + 
					"(9776, \"Torneiros (Par� de Minas)\", 11),\r\n" + 
					"(9777, \"Toropi\", 23),\r\n" + 
					"(9778, \"Toroqu� (S�o Francisco de Assis)\", 23),\r\n" + 
					"(9779, \"Torquato Severo (Dom Pedrito)\", 23),\r\n" + 
					"(9780, \"Torre de Pedra\", 26),\r\n" + 
					"(9781, \"Torre�es (Juiz de Fora)\", 11),\r\n" + 
					"(9782, \"Torres\", 23),\r\n" + 
					"(9783, \"Torrinha\", 26),\r\n" + 
					"(9784, \"Torrinhas (Pinheiro Machado)\", 23),\r\n" + 
					"(9785, \"Touro Passo (Ros�rio do Sul)\", 23),\r\n" + 
					"(9786, \"Touros\", 20),\r\n" + 
					"(9787, \"Trabiju\", 26),\r\n" + 
					"(9788, \"Tracuateua\", 14),\r\n" + 
					"(9789, \"Tracunha�m\", 16),\r\n" + 
					"(9790, \"Tracupa (Tucano)\", 5),\r\n" + 
					"(9791, \"Traipu\", 2),\r\n" + 
					"(9792, \"Trair�o\", 14),\r\n" + 
					"(9793, \"Trairi\", 6),\r\n" + 
					"(9794, \"Trairi (Tangar�)\", 20),\r\n" + 
					"(9796, \"Tramanda�\", 23),\r\n" + 
					"(9797, \"Trancoso (Porto Seguro)\", 5),\r\n" + 
					"(9798, \"Trapi� (Riacho das Almas)\", 16),\r\n" + 
					"(9799, \"Trapi� (Santa Quit�ria)\", 6),\r\n" + 
					"(9800, \"Trapi� (Sobral)\", 6),\r\n" + 
					"(9801, \"Travess�o (Campos dos Goytacazes)\", 19),\r\n" + 
					"(9802, \"Travesseiro\", 23),\r\n" + 
					"(9803, \"Tremedal\", 5),\r\n" + 
					"(9804, \"Trememb�\", 26),\r\n" + 
					"(9805, \"Trentin (Jaboticaba)\", 23),\r\n" + 
					"(9806, \"Tr�s Alian�as (Mirand�polis)\", 26),\r\n" + 
					"(9807, \"Tr�s Arroios\", 23),\r\n" + 
					"(9808, \"Tr�s Barras\", 24),\r\n" + 
					"(9809, \"Tr�s Barras (Aratiba)\", 23),\r\n" + 
					"(9810, \"Tr�s Barras da Estrada Real (Serro)\", 11),\r\n" + 
					"(9811, \"Tr�s Barras do Paran�\", 18),\r\n" + 
					"(9812, \"Tr�s Bicos (C�ndido de Abreu)\", 18),\r\n" + 
					"(9813, \"Tr�s Bocas (Toledo)\", 18),\r\n" + 
					"(9814, \"Tr�s Cachoeiras\", 23),\r\n" + 
					"(9815, \"Tr�s Cap�es (Guarapuava)\", 18),\r\n" + 
					"(9816, \"Tr�s Cora��es\", 11),\r\n" + 
					"(9817, \"Tr�s Coroas\", 23),\r\n" + 
					"(9818, \"Tr�s C�rregos (Campo Largo)\", 18),\r\n" + 
					"(9819, \"Tr�s de Maio\", 23),\r\n" + 
					"(9820, \"Tr�s Forquilhas\", 23),\r\n" + 
					"(9821, \"Tr�s Fronteiras\", 26),\r\n" + 
					"(9822, \"Tr�s Ilhas (Belmiro Braga)\", 11),\r\n" + 
					"(9823, \"Tr�s Irm�os (Cambuci)\", 19),\r\n" + 
					"(9824, \"Tr�s Ladeiras (Igarassu)\", 16),\r\n" + 
					"(9825, \"Tr�s Lagoas\", 12),\r\n" + 
					"(9826, \"Tr�s Lagoas (Foz do Igua�u)\", 18),\r\n" + 
					"(9827, \"Tr�s Marias\", 11),\r\n" + 
					"(9828, \"Tr�s Palmeiras\", 23),\r\n" + 
					"(9829, \"Tr�s Passos\", 23),\r\n" + 
					"(9830, \"Tr�s Pinheiros (Fontoura Xavier)\", 23),\r\n" + 
					"(9831, \"Tr�s Pinheiros (Guarapuava)\", 18),\r\n" + 
					"(9832, \"Tr�s Placas (Umuarama)\", 18),\r\n" + 
					"(9833, \"Tr�s Pontas\", 11),\r\n" + 
					"(9834, \"Tr�s Pontes (Rondon�polis)\", 13),\r\n" + 
					"(9835, \"Tr�s Ranchos\", 9),\r\n" + 
					"(9836, \"Tr�s Rios\", 19),\r\n" + 
					"(9837, \"Tr�s Vendas (Cachoeira do Sul)\", 23),\r\n" + 
					"(9838, \"Treviso\", 24),\r\n" + 
					"(9839, \"Trevo do Jos� Ros�rio (Leopoldo de Bulh�es)\", 9),\r\n" + 
					"(9840, \"Treze de Maio\", 24),\r\n" + 
					"(9841, \"Treze T�lias\", 24),\r\n" + 
					"(9842, \"Tri�ngulo (Chorozinho)\", 6),\r\n" + 
					"(9843, \"Tri�ngulo (Engenheiro Beltr�o)\", 18),\r\n" + 
					"(9844, \"Trici (Tau�)\", 6),\r\n" + 
					"(9845, \"Trimonte (Volta Grande)\", 11),\r\n" + 
					"(9846, \"Trindade\", 16),\r\n" + 
					"(9847, \"Trindade\", 9),\r\n" + 
					"(9848, \"Trindade do Sul\", 23),\r\n" + 
					"(9849, \"Trindade (Ponta Grossa)\", 18),\r\n" + 
					"(9850, \"Triol�ndia (Ribeir�o do Pinhal)\", 18),\r\n" + 
					"(9851, \"Triunfo\", 15),\r\n" + 
					"(9852, \"Triunfo\", 23),\r\n" + 
					"(9853, \"Triunfo\", 16),\r\n" + 
					"(9854, \"Triunfo (Candeias do Jamari)\", 21),\r\n" + 
					"(9855, \"Triunfo do Sincor� (Barra da Estiva)\", 5),\r\n" + 
					"(9856, \"Triunfo (Nova Olinda)\", 6),\r\n" + 
					"(9857, \"Triunfo (Pelotas)\", 23),\r\n" + 
					"(9858, \"Triunfo Potiguar\", 20),\r\n" + 
					"(9859, \"Triunfo (Santa Maria Madalena)\", 19),\r\n" + 
					"(9860, \"Trizidela do Vale\", 10),\r\n" + 
					"(9861, \"Tr�ia (Pedra Branca)\", 6),\r\n" + 
					"(9862, \"Trombas\", 9),\r\n" + 
					"(9863, \"Trombudo Central\", 24),\r\n" + 
					"(9864, \"Tronco (Castro)\", 18),\r\n" + 
					"(9865, \"Tronqueiras (Miragua�)\", 23),\r\n" + 
					"(9866, \"Trussu (Acopiara)\", 6),\r\n" + 
					"(9867, \"Tubar�o\", 24),\r\n" + 
					"(9868, \"Tucano\", 5),\r\n" + 
					"(9869, \"Tucum�\", 14),\r\n" + 
					"(9870, \"Tucunduba (Caucaia)\", 6),\r\n" + 
					"(9871, \"Tucunduva\", 23),\r\n" + 
					"(9872, \"Tucuns (Crate�s)\", 6),\r\n" + 
					"(9873, \"Tucuru�\", 14),\r\n" + 
					"(9874, \"Tufil�ndia\", 10),\r\n" + 
					"(9875, \"Tu�na (Massap�)\", 6),\r\n" + 
					"(9876, \"Tuiuti\", 26),\r\n" + 
					"(9877, \"Tuiuti (Bento Gon�alves)\", 23),\r\n" + 
					"(9878, \"Tuiutinga (Guiricema)\", 11),\r\n" + 
					"(9879, \"Tujuguaba (Conchal)\", 26),\r\n" + 
					"(9880, \"Tumiritinga\", 11),\r\n" + 
					"(9881, \"Tun�polis\", 24),\r\n" + 
					"(9882, \"Tunas\", 23),\r\n" + 
					"(9883, \"Tunas do Paran�\", 18),\r\n" + 
					"(9884, \"Tuneiras do Oeste\", 18),\r\n" + 
					"(9885, \"T�nel Verde (Cidreira)\", 23),\r\n" + 
					"(9886, \"Tuntum\", 10),\r\n" + 
					"(9887, \"Tup�\", 26),\r\n" + 
					"(9888, \"Tupaciguara\", 11),\r\n" + 
					"(9889, \"Tupanaci (Mirandiba)\", 16),\r\n" + 
					"(9890, \"Tupanatinga\", 16),\r\n" + 
					"(9891, \"Tupanci do Sul\", 23),\r\n" + 
					"(9892, \"Tupanciret�\", 23),\r\n" + 
					"(9893, \"Tupanci (S�o Sep�)\", 23),\r\n" + 
					"(9894, \"Tupandi\", 23),\r\n" + 
					"(9895, \"Tupantuba (Santiago)\", 23),\r\n" + 
					"(9896, \"Tupa�ca (Alian�a)\", 16),\r\n" + 
					"(9897, \"Tuparec� (Medina)\", 11),\r\n" + 
					"(9898, \"Tuparendi\", 23),\r\n" + 
					"(9899, \"Tuparetama\", 16),\r\n" + 
					"(9900, \"Tup�ssi\", 18),\r\n" + 
					"(9901, \"Tupinamb� (Astorga)\", 18),\r\n" + 
					"(9902, \"Tupinamb� (Lagoa Vermelha)\", 23),\r\n" + 
					"(9903, \"Tupi Paulista\", 26),\r\n" + 
					"(9904, \"Tupiracaba (Niquel�ndia)\", 9),\r\n" + 
					"(9905, \"Tupirama\", 27),\r\n" + 
					"(9906, \"Tupirat� (Presidente Kennedy)\", 27),\r\n" + 
					"(9907, \"Tupiratins\", 27),\r\n" + 
					"(9908, \"Tupi Silveira (Hulha Negra)\", 23),\r\n" + 
					"(9909, \"Tupitinga (Campos Novos)\", 24),\r\n" + 
					"(9910, \"Turia�u\", 10),\r\n" + 
					"(9911, \"Turiba do Sul (Itaber�)\", 26),\r\n" + 
					"(9912, \"Turil�ndia\", 10),\r\n" + 
					"(9913, \"Turi�ba\", 26),\r\n" + 
					"(9914, \"Turmalina\", 11),\r\n" + 
					"(9915, \"Turmalina\", 26),\r\n" + 
					"(9916, \"Turu�u\", 23),\r\n" + 
					"(9917, \"Tururu\", 6),\r\n" + 
					"(9918, \"Turv�nia\", 9),\r\n" + 
					"(9919, \"Turvel�ndia\", 9),\r\n" + 
					"(9920, \"Turvinho (Coronel Bicaco)\", 23),\r\n" + 
					"(9921, \"Turvo\", 18),\r\n" + 
					"(9922, \"Turvo\", 24),\r\n" + 
					"(9923, \"Turvol�ndia\", 11),\r\n" + 
					"(9924, \"Tut�ia\", 10),\r\n" + 
					"(9925, \"Uarini\", 3),\r\n" + 
					"(9926, \"Uau�\", 5),\r\n" + 
					"(9927, \"Ub�\", 11),\r\n" + 
					"(9928, \"Uba�\", 11),\r\n" + 
					"(9929, \"Uba�ra\", 5),\r\n" + 
					"(9930, \"Ubaitaba\", 5),\r\n" + 
					"(9931, \"Ubajara\", 6),\r\n" + 
					"(9932, \"Ubaldino Taques (Coronel Domingos Soares)\", 18),\r\n" + 
					"(9933, \"Ubaporanga\", 11),\r\n" + 
					"(9934, \"Ubarana\", 26),\r\n" + 
					"(9935, \"Ubari (Ub�)\", 11),\r\n" + 
					"(9936, \"Ubat�\", 5),\r\n" + 
					"(9937, \"Ubatuba\", 26),\r\n" + 
					"(9938, \"Uba�na (Corea�)\", 6),\r\n" + 
					"(9939, \"Ubauna (S�o Jo�o do Iva�)\", 18),\r\n" + 
					"(9940, \"Uberaba\", 11),\r\n" + 
					"(9941, \"Uberl�ndia\", 11),\r\n" + 
					"(9942, \"Ubiracaba (Brumado)\", 5),\r\n" + 
					"(9943, \"Ubira�u (Canind�)\", 6),\r\n" + 
					"(9944, \"Ubirait� (Andara�)\", 5),\r\n" + 
					"(9945, \"Ubirajara\", 26),\r\n" + 
					"(9946, \"Ubirat�\", 18),\r\n" + 
					"(9947, \"Ubiretama\", 23),\r\n" + 
					"(9948, \"Uchoa\", 26),\r\n" + 
					"(9949, \"Uiba�\", 5),\r\n" + 
					"(9950, \"Uiramut�\", 22),\r\n" + 
					"(9951, \"Uiraponga (Morada Nova)\", 6),\r\n" + 
					"(9952, \"Uirapuru\", 9),\r\n" + 
					"(9953, \"Uira�na\", 15),\r\n" + 
					"(9954, \"Ulian�polis\", 14),\r\n" + 
					"(9955, \"Umari\", 6),\r\n" + 
					"(9956, \"Umari (S�o Jo�o do Rio do Peixe)\", 15),\r\n" + 
					"(9957, \"Umarituba (S�o Gon�alo do Amarante)\", 6),\r\n" + 
					"(9958, \"Umarizal\", 20),\r\n" + 
					"(9959, \"Umarizeiras (Maranguape)\", 6),\r\n" + 
					"(9960, \"Um�s (Salgueiro)\", 16),\r\n" + 
					"(9961, \"Umba�ba\", 25),\r\n" + 
					"(9962, \"Umbu (Cacequi)\", 23),\r\n" + 
					"(9963, \"Umburanas\", 5),\r\n" + 
					"(9964, \"Umburanas (Mauriti)\", 6),\r\n" + 
					"(9965, \"Umburanas (Sert�nia)\", 16),\r\n" + 
					"(9966, \"Umburatiba\", 11),\r\n" + 
					"(9967, \"Umburetama (Orob�)\", 16),\r\n" + 
					"(9968, \"Umbuzeiro\", 15),\r\n" + 
					"(9969, \"Umbuzeiro (Lontra)\", 11),\r\n" + 
					"(9970, \"Umbuzeiro (Mundo Novo)\", 5),\r\n" + 
					"(9971, \"Umbuzeiro (Olindina)\", 5),\r\n" + 
					"(9972, \"Umirim\", 6),\r\n" + 
					"(9973, \"Umuarama\", 18),\r\n" + 
					"(9974, \"Una\", 5),\r\n" + 
					"(9975, \"Una�\", 11),\r\n" + 
					"(9976, \"Uni�o\", 17),\r\n" + 
					"(9977, \"Uni�o Bandeirante (Porto Velho)\", 21),\r\n" + 
					"(9978, \"Uni�o da Floresta (Medicil�ndia)\", 14),\r\n" + 
					"(9979, \"Uni�o da Serra\", 23),\r\n" + 
					"(9980, \"Uni�o da Vit�ria\", 18),\r\n" + 
					"(9981, \"Uni�o de Minas\", 11),\r\n" + 
					"(9982, \"Uni�o do Oeste\", 24),\r\n" + 
					"(9983, \"Uni�o do Oeste (Santa Izabel do Oeste)\", 18),\r\n" + 
					"(9984, \"Uni�o dos Palmares\", 2),\r\n" + 
					"(9985, \"Uni�o do Sul\", 13),\r\n" + 
					"(9986, \"Uni�o (Laranjeiras do Sul)\", 18),\r\n" + 
					"(9987, \"Uni�o Paulista\", 26),\r\n" + 
					"(9988, \"Uni�o (Santo �ngelo)\", 23),\r\n" + 
					"(9989, \"Uniflor\", 18),\r\n" + 
					"(9990, \"Unistalda\", 23),\r\n" + 
					"(9991, \"Upanema\", 20),\r\n" + 
					"(9992, \"Upatininga (Alian�a)\", 16),\r\n" + 
					"(9993, \"Ura�\", 18),\r\n" + 
					"(9994, \"Urandi\", 5),\r\n" + 
					"(9995, \"Ur�nia\", 26),\r\n" + 
					"(9996, \"Ur�nia (Alfredo Chaves)\", 8),\r\n" + 
					"(9997, \"Urbano Santos\", 10),\r\n" + 
					"(9998, \"Urimama (Santa Maria da Boa Vista)\", 16),\r\n" + 
					"(9999, \"Uru\", 26),\r\n" + 
					"(10000, \"Urua�u\", 9),\r\n" + 
					"(10001, \"Uruana\", 9),\r\n" + 
					"(10002, \"Uruana de Minas\", 11),\r\n" + 
					"(10003, \"Uruar�\", 14),\r\n" + 
					"(10004, \"Uru�s (Petrolina)\", 16),\r\n" + 
					"(10005, \"Urubici\", 24),\r\n" + 
					"(10006, \"Uruburetama\", 6),\r\n" + 
					"(10007, \"Uruc�nia\", 11),\r\n" + 
					"(10008, \"Urucar�\", 3),\r\n" + 
					"(10009, \"Urucub� (Limoeiro)\", 16),\r\n" + 
					"(10010, \"Uru�uca\", 5),\r\n" + 
					"(10011, \"Uru�u�\", 17),\r\n" + 
					"(10012, \"Urucuia\", 11),\r\n" + 
					"(10013, \"Uru�u-Mirim (Gravat�)\", 16),\r\n" + 
					"(10014, \"Urucuri (S�o Miguel do Guam�)\", 14),\r\n" + 
					"(10015, \"Urucuriteua (S�o Miguel do Guam�)\", 14),\r\n" + 
					"(10016, \"Urucurituba\", 3),\r\n" + 
					"(10017, \"Uruguaiana\", 23),\r\n" + 
					"(10018, \"Uruguai (Piratuba)\", 24),\r\n" + 
					"(10019, \"Uruita (Uruana)\", 9),\r\n" + 
					"(10020, \"Uruoca\", 6),\r\n" + 
					"(10021, \"Urup�\", 21),\r\n" + 
					"(10022, \"Urupema\", 24),\r\n" + 
					"(10023, \"Urup�s\", 26),\r\n" + 
					"(10024, \"Uruqu� (Quixeramobim)\", 6),\r\n" + 
					"(10025, \"Ururai (Santa Ad�lia)\", 26),\r\n" + 
					"(10026, \"Urussanga\", 24),\r\n" + 
					"(10027, \"Uruta�\", 9),\r\n" + 
					"(10028, \"Usina Cama�ari (Coruripe)\", 2),\r\n" + 
					"(10029, \"Usina Monte Alegre (Monte Belo)\", 11),\r\n" + 
					"(10030, \"Usina Ro�adinho (Catende)\", 16),\r\n" + 
					"(10031, \"Utinga\", 5),\r\n" + 
					"(10032, \"Uv� (Goi�s)\", 9),\r\n" + 
					"(10033, \"Uvaia (Ponta Grossa)\", 18),\r\n" + 
					"(10034, \"Vacacai (S�o Gabriel)\", 23),\r\n" + 
					"(10035, \"Vacaria\", 23),\r\n" + 
					"(10036, \"Vai-Volta (Tarumirim)\", 11),\r\n" + 
					"(10037, \"Valadares (Juiz de Fora)\", 11),\r\n" + 
					"(10038, \"Val�o do Barro (S�o Sebasti�o do Alto)\", 19),\r\n" + 
					"(10039, \"Val�o (Pot�)\", 11),\r\n" + 
					"(10040, \"Vald�stico (Encantado)\", 23),\r\n" + 
					"(10041, \"Valdel�ndia (Rubiataba)\", 9),\r\n" + 
					"(10042, \"Valdemar Siqueira (Sert�nia)\", 16),\r\n" + 
					"(10043, \"Vale de S�o Domingos\", 13),\r\n" + 
					"(10044, \"Vale do Anari\", 21),\r\n" + 
					"(10045, \"Vale do Arroio Grande (Arroio do Meio)\", 23),\r\n" + 
					"(10046, \"Vale do Para�so\", 21),\r\n" + 
					"(10047, \"Vale do Rio Cai (Nova Petr�polis)\", 23),\r\n" + 
					"(10048, \"Vale do Sampaio (Ven�ncio Aires)\", 23),\r\n" + 
					"(10049, \"Vale do Sol\", 23),\r\n" + 
					"(10050, \"Vale dos Sonhos (Barra do Gar�as)\", 13),\r\n" + 
					"(10051, \"Vale dos Vinhedos (Bento Gon�alves)\", 23),\r\n" + 
					"(10052, \"Vale Formoso (Novo Horizonte)\", 26),\r\n" + 
					"(10053, \"Valen�a\", 5),\r\n" + 
					"(10054, \"Valen�a\", 19),\r\n" + 
					"(10055, \"Valen�a do Piau�\", 17),\r\n" + 
					"(10056, \"Valente\", 5),\r\n" + 
					"(10057, \"Valentim Gentil\", 26),\r\n" + 
					"(10058, \"Valentins (Ponta Grossa)\", 18),\r\n" + 
					"(10059, \"Vale Real\", 23),\r\n" + 
					"(10060, \"Vale Rico (Guiratinga)\", 13),\r\n" + 
					"(10061, \"Val�rio (Planalto)\", 18),\r\n" + 
					"(10062, \"Vale Veneto (S�o Jo�o do Pol�sine)\", 23),\r\n" + 
					"(10063, \"Vale Verde\", 23),\r\n" + 
					"(10064, \"Vale Verde de Minas (Ipaba)\", 11),\r\n" + 
					"(10065, \"Vale Verde (Porto Seguro)\", 5),\r\n" + 
					"(10066, \"Valinhos\", 26),\r\n" + 
					"(10067, \"Valo Fundo (Santo Hip�lito)\", 11),\r\n" + 
					"(10068, \"Valpara�so\", 26),\r\n" + 
					"(10069, \"Valpara�so de Goi�s\", 9),\r\n" + 
					"(10070, \"Vangl�ria (Pederneiras)\", 26),\r\n" + 
					"(10071, \"Vanini\", 23),\r\n" + 
					"(10072, \"Varame (Campos Borges)\", 23),\r\n" + 
					"(10073, \"Vargeado (Castro)\", 18),\r\n" + 
					"(10074, \"Varge�o\", 24),\r\n" + 
					"(10075, \"Vargem\", 26),\r\n" + 
					"(10076, \"Vargem\", 24),\r\n" + 
					"(10077, \"Vargem Alegre\", 11),\r\n" + 
					"(10078, \"Vargem Alegre (Barra do Pira�)\", 19),\r\n" + 
					"(10079, \"Vargem Alta\", 8),\r\n" + 
					"(10080, \"Vargem Bonita\", 11),\r\n" + 
					"(10081, \"Vargem Bonita\", 24),\r\n" + 
					"(10082, \"Vargem do Cedro (S�o Martinho)\", 24),\r\n" + 
					"(10083, \"Vargem Grande\", 10),\r\n" + 
					"(10084, \"Vargem Grande do Rio Pardo\", 11),\r\n" + 
					"(10085, \"Vargem Grande do Soturno (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(10086, \"Vargem Grande do Sul\", 26),\r\n" + 
					"(10087, \"Vargem Grande Paulista\", 26),\r\n" + 
					"(10088, \"Vargem Linda (S�o Domingos do Prata)\", 11),\r\n" + 
					"(10089, \"Varginha\", 11),\r\n" + 
					"(10090, \"Varginha (Arapiraca)\", 2),\r\n" + 
					"(10091, \"Varginha (Santo Ant�nio do Leverger)\", 13),\r\n" + 
					"(10092, \"Varj�o\", 9),\r\n" + 
					"(10093, \"Varj�o de Minas\", 11),\r\n" + 
					"(10094, \"Varjota\", 6),\r\n" + 
					"(10095, \"Varre-Sai\", 19),\r\n" + 
					"(10096, \"V�rzea\", 15),\r\n" + 
					"(10097, \"V�rzea\", 20),\r\n" + 
					"(10098, \"V�rzea Alegre\", 6),\r\n" + 
					"(10099, \"V�rzea Branca\", 17),\r\n" + 
					"(10100, \"V�rzea (Cedro)\", 6),\r\n" + 
					"(10101, \"V�rzea Comprida (Pombal)\", 15),\r\n" + 
					"(10102, \"V�rzea da On�a (Quixad�)\", 6),\r\n" + 
					"(10103, \"V�rzea da Palma\", 11),\r\n" + 
					"(10104, \"V�rzea da Ro�a\", 5),\r\n" + 
					"(10105, \"V�rzea da Volta (Mora�jo)\", 6),\r\n" + 
					"(10106, \"V�rzea do Caldas (Seabra)\", 5),\r\n" + 
					"(10107, \"V�rzea do Cerco (Mulungu do Morro)\", 5),\r\n" + 
					"(10108, \"V�rzea do Gilo (Ipu)\", 6),\r\n" + 
					"(10109, \"V�rzea do Po�o\", 5),\r\n" + 
					"(10110, \"V�rzea dos Antunes (Muitos Cap�es)\", 23),\r\n" + 
					"(10111, \"V�rzea dos Espinhos (Guaraciaba do Norte)\", 6),\r\n" + 
					"(10112, \"V�rzea Grande\", 13),\r\n" + 
					"(10113, \"V�rzea Grande\", 17),\r\n" + 
					"(10114, \"V�rzea Nova\", 5),\r\n" + 
					"(10115, \"V�rzea Nova (Santa Rita)\", 15),\r\n" + 
					"(10116, \"V�rzea (Pantano Grande)\", 23),\r\n" + 
					"(10117, \"V�rzea Paulista\", 26),\r\n" + 
					"(10118, \"V�rzea (S�o Jos� dos Ausentes)\", 23),\r\n" + 
					"(10119, \"V�rzeas (Baian�polis)\", 5),\r\n" + 
					"(10120, \"Varzedo\", 5),\r\n" + 
					"(10121, \"Varzel�ndia\", 11),\r\n" + 
					"(10122, \"Vasco Alves (Alegrete)\", 23),\r\n" + 
					"(10123, \"Vasques (Salgueiro)\", 16),\r\n" + 
					"(10124, \"Vassoural (Ibaiti)\", 18),\r\n" + 
					"(10125, \"Vassouras\", 19),\r\n" + 
					"(10126, \"Vau-A�u (Ponte Nova)\", 11),\r\n" + 
					"(10127, \"Vazante\", 11),\r\n" + 
					"(10128, \"Vazante (Diamante)\", 15),\r\n" + 
					"(10129, \"Vazante (Divin�polis de Goi�s)\", 9),\r\n" + 
					"(10130, \"Vazantes (Aracoiaba)\", 6),\r\n" + 
					"(10131, \"Vazente do Cur� (Canind�)\", 6),\r\n" + 
					"(10132, \"Veado Pardo (Marau)\", 23),\r\n" + 
					"(10133, \"Veiros (Porto de Moz)\", 14),\r\n" + 
					"(10134, \"Velha Boipeba (Cairu)\", 5),\r\n" + 
					"(10135, \"Velhacaria (Parana�ba)\", 12),\r\n" + 
					"(10136, \"Ven�ncio Aires\", 23),\r\n" + 
					"(10137, \"Venda Branca (Casa Branca)\", 26),\r\n" + 
					"(10138, \"Venda das Flores (Miracema)\", 19),\r\n" + 
					"(10139, \"Venda Nova do Imigrante\", 8),\r\n" + 
					"(10140, \"Veneza (Parnamirim)\", 16),\r\n" + 
					"(10141, \"Venha-Ver\", 20),\r\n" + 
					"(10142, \"Ventania\", 18),\r\n" + 
					"(10143, \"Ventura (Alc�ntaras)\", 6),\r\n" + 
					"(10144, \"Ventura (Morro do Chap�u)\", 5),\r\n" + 
					"(10145, \"Venturosa\", 16),\r\n" + 
					"(10146, \"V�nus (Aragua�na)\", 27),\r\n" + 
					"(10147, \"Vera\", 13),\r\n" + 
					"(10148, \"Vera Cruz\", 5),\r\n" + 
					"(10149, \"Vera Cruz\", 26),\r\n" + 
					"(10150, \"Vera Cruz\", 23),\r\n" + 
					"(10151, \"Vera Cruz\", 20),\r\n" + 
					"(10152, \"Vera Cruz de Minas (Pedro Leopoldo)\", 11),\r\n" + 
					"(10153, \"Vera Cruz do Oeste\", 18),\r\n" + 
					"(10154, \"Vera Guarani (Paulo Frontin)\", 18),\r\n" + 
					"(10155, \"Vera Mendes\", 17),\r\n" + 
					"(10156, \"Veran�polis\", 23),\r\n" + 
					"(10157, \"Veran�polis (Confresa)\", 13),\r\n" + 
					"(10158, \"Verdejante\", 16),\r\n" + 
					"(10159, \"Verdel�ndia\", 11),\r\n" + 
					"(10160, \"Ver�\", 18),\r\n" + 
					"(10161, \"Vereda\", 5),\r\n" + 
					"(10162, \"Vereda do Para�so (S�o Jo�o do Para�so)\", 11),\r\n" + 
					"(10163, \"Veredas (Jo�o Pinheiro)\", 11),\r\n" + 
					"(10164, \"Veredinha\", 11),\r\n" + 
					"(10165, \"Veredinha (Vit�ria da Conquista)\", 5),\r\n" + 
					"(10166, \"Ver�ssimo\", 11),\r\n" + 
					"(10167, \"Vermelho (Muria�)\", 11),\r\n" + 
					"(10168, \"Vermelho Novo\", 11),\r\n" + 
					"(10169, \"Vermelhos (Lagoa Grande)\", 16),\r\n" + 
					"(10170, \"Vermelho Velho (Raul Soares)\", 11),\r\n" + 
					"(10171, \"Vertente do L�rio\", 16),\r\n" + 
					"(10172, \"Vertentes\", 16),\r\n" + 
					"(10173, \"Vertentes do Lagedo (Maranguape)\", 6),\r\n" + 
					"(10174, \"Vertentes (Uruguaiana)\", 23),\r\n" + 
					"(10175, \"Vespasiano\", 11),\r\n" + 
					"(10176, \"Vespasiano Correa\", 23),\r\n" + 
					"(10177, \"Viadutos\", 23),\r\n" + 
					"(10178, \"Viam�o\", 23),\r\n" + 
					"(10179, \"Viana\", 8),\r\n" + 
					"(10180, \"Viana\", 10),\r\n" + 
					"(10181, \"Vian�polis\", 9),\r\n" + 
					"(10182, \"Vic�ncia\", 16),\r\n" + 
					"(10183, \"Vicente Dutra\", 23),\r\n" + 
					"(10184, \"Vicentina\", 12),\r\n" + 
					"(10185, \"Vicentin�polis\", 9),\r\n" + 
					"(10186, \"Vicentin�polis (Santo Ant�nio do Aracangu�)\", 26),\r\n" + 
					"(10187, \"Vi�osa\", 20),\r\n" + 
					"(10188, \"Vi�osa\", 11),\r\n" + 
					"(10189, \"Vi�osa\", 2),\r\n" + 
					"(10190, \"Vi�osa do Cear�\", 6),\r\n" + 
					"(10191, \"Vi�osa (Fortim)\", 6),\r\n" + 
					"(10192, \"Vi�osa (Ibicuitinga)\", 6),\r\n" + 
					"(10193, \"Victor Graeff\", 23),\r\n" + 
					"(10194, \"Vidal Ramos\", 24),\r\n" + 
					"(10195, \"Vida Nova (Sapopema)\", 18),\r\n" + 
					"(10196, \"Videira\", 24),\r\n" + 
					"(10197, \"Video (Catunda)\", 6),\r\n" + 
					"(10198, \"Vidigal (Cianorte)\", 18),\r\n" + 
					"(10199, \"Vieira Machado (Muniz Freire)\", 8),\r\n" + 
					"(10200, \"Vieiras\", 11),\r\n" + 
					"(10201, \"Vieir�polis\", 15),\r\n" + 
					"(10202, \"Vigia\", 14),\r\n" + 
					"(10203, \"Vila Acari (Pint�polis)\", 11),\r\n" + 
					"(10204, \"Vila Amaz�nia (Parintins)\", 3),\r\n" + 
					"(10205, \"Vila Aparecida (Arapiraca)\", 2),\r\n" + 
					"(10206, \"Vila Atl�ntica (Marcel�ndia)\", 13),\r\n" + 
					"(10207, \"Vila Bela da Sant�ssima Trindade\", 13),\r\n" + 
					"(10208, \"Vila Bem-Querer (Riacho dos Machados)\", 11),\r\n" + 
					"(10209, \"Vila Bender (Crissiumal)\", 23),\r\n" + 
					"(10210, \"Vila Bittencourt (Japur�)\", 3),\r\n" + 
					"(10211, \"Vila Block (S�o Sep�)\", 23),\r\n" + 
					"(10212, \"Vila Boa\", 9),\r\n" + 
					"(10213, \"Vila Bom Jesus (Arinos)\", 11),\r\n" + 
					"(10214, \"Vila Boqueir�o (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(10215, \"Vila Bueno (Rondon�polis)\", 13),\r\n" + 
					"(10216, \"Vila Campos (Tapejara)\", 23),\r\n" + 
					"(10217, \"Vila Cardoso (Porto Esperidi�o)\", 13),\r\n" + 
					"(10218, \"Vila Celeste (Santa Helena)\", 18),\r\n" + 
					"(10219, \"Vila Concei��o (S�o Jo�o do Sul)\", 24),\r\n" + 
					"(10220, \"Vila Costina (Pains)\", 11),\r\n" + 
					"(10221, \"Vila Cristina (Caxias do Sul)\", 23),\r\n" + 
					"(10222, \"Vila Cruz (Nova Palma)\", 23),\r\n" + 
					"(10223, \"Vila da Grama (Trajano de Moraes)\", 19),\r\n" + 
					"(10224, \"Vila de Campinas (Manacapuru)\", 3),\r\n" + 
					"(10225, \"Vila de Sacambu (Manacapuru)\", 3),\r\n" + 
					"(10226, \"Vila de Volta Grande (Rio Negrinho)\", 24),\r\n" + 
					"(10227, \"Vila Diniz (Cruzmaltina)\", 18),\r\n" + 
					"(10228, \"Vila do Caf� (Encruzilhada)\", 5),\r\n" + 
					"(10229, \"Vila do Carmo do Tocantins (Camet�)\", 14),\r\n" + 
					"(10230, \"Vila do Lago do Jacar� (Manacapuru)\", 3),\r\n" + 
					"(10231, \"Vila dos Cabanos (Barcarena)\", 14),\r\n" + 
					"(10232, \"Vila dos Roldos (Pato Branco)\", 18),\r\n" + 
					"(10233, \"Vila Fernando Ferrari (Tr�s Cachoeiras)\", 23),\r\n" + 
					"(10234, \"Vila Flor\", 20),\r\n" + 
					"(10235, \"Vila Flores\", 23),\r\n" + 
					"(10236, \"Vila Fl�rida (Toledo)\", 18),\r\n" + 
					"(10237, \"Vila Formosa (Dourados)\", 12),\r\n" + 
					"(10238, \"Vila Fran�a (Santar�m)\", 14),\r\n" + 
					"(10239, \"Vila Freire (Cerrito)\", 23),\r\n" + 
					"(10240, \"Vila Gandhi (Primeiro de Maio)\", 18),\r\n" + 
					"(10241, \"Vila Goreth (Santar�m)\", 14),\r\n" + 
					"(10242, \"Vila Guay (Ibaiti)\", 18),\r\n" + 
					"(10243, \"Vila Ipiranga (Toledo)\", 18),\r\n" + 
					"(10244, \"Vila Isol (Itaituba)\", 14),\r\n" + 
					"(10245, \"Vila Ituim (Muitos Cap�es)\", 23),\r\n" + 
					"(10246, \"Vila L�ngaro\", 23),\r\n" + 
					"(10247, \"Vila Laranjeira (Santo Cristo)\", 23),\r\n" + 
					"(10248, \"Vila Lind�ia (Itacoatiara)\", 3),\r\n" + 
					"(10249, \"Vila Mandi (Santana do Araguaia)\", 14),\r\n" + 
					"(10250, \"Vila Maria\", 23),\r\n" + 
					"(10251, \"Vila Marques (Aral Moreira)\", 12),\r\n" + 
					"(10252, \"Vila Milani (S�o Domingos)\", 24),\r\n" + 
					"(10253, \"Vila Muriqui (Mangaratiba)\", 19),\r\n" + 
					"(10254, \"Vila Mutum (Alta Floresta)\", 13),\r\n" + 
					"(10255, \"Vila Nelita (�gua Doce do Norte)\", 8),\r\n" + 
					"(10256, \"Vila Nossa Senhora Aparecida (Petrolina)\", 16),\r\n" + 
					"(10257, \"Vila Nova (Belo Jardim)\", 16),\r\n" + 
					"(10258, \"Vila Nova (Casinhas)\", 16),\r\n" + 
					"(10259, \"Vila Nova de Bananal (Baixo Guandu)\", 8),\r\n" + 
					"(10260, \"Vila Nova de Campos (Campos dos Goytacazes)\", 19),\r\n" + 
					"(10261, \"Vila Nova de Floren�a (S�o Jer�nimo da Serra)\", 18),\r\n" + 
					"(10262, \"Vila Nova de Minas (Montes Claros)\", 11),\r\n" + 
					"(10263, \"Vila Nova do Piau�\", 17),\r\n" + 
					"(10264, \"Vila Nova dos Mart�rios\", 10),\r\n" + 
					"(10265, \"Vila Nova dos Po��es (Jana�ba)\", 11),\r\n" + 
					"(10266, \"Vila Nova do Sul\", 23),\r\n" + 
					"(10267, \"Vila Nova Floresta (Governador Valadares)\", 11),\r\n" + 
					"(10268, \"Vila Nova (Imbituba)\", 24),\r\n" + 
					"(10269, \"Vila Nova - N5 (Petrolina)\", 16),\r\n" + 
					"(10270, \"Vila Nova (Pitanga)\", 18),\r\n" + 
					"(10271, \"Vila Nova (S�o Caetano de Odivelas)\", 14),\r\n" + 
					"(10272, \"Vila Nova (Toledo)\", 18),\r\n" + 
					"(10273, \"Vila Oliva (Caxias do Sul)\", 23),\r\n" + 
					"(10274, \"Vila Oper�ria (Rondon�polis)\", 13),\r\n" + 
					"(10275, \"Vila Para�so (S�o Jo�o)\", 18),\r\n" + 
					"(10276, \"Vila Paulista (Rondon�polis)\", 13),\r\n" + 
					"(10277, \"Vila Pav�o\", 8),\r\n" + 
					"(10278, \"Vila Paz (Tapera)\", 23),\r\n" + 
					"(10279, \"Vila Pereira (Nanuque)\", 11),\r\n" + 
					"(10280, \"Vila Pitinga (Presidente Figueiredo)\", 3),\r\n" + 
					"(10281, \"Vila Planalto (Itaituba)\", 14),\r\n" + 
					"(10282, \"Vila Progresso (Salto do C�u)\", 13),\r\n" + 
					"(10283, \"Vila Prop�cio\", 9),\r\n" + 
					"(10284, \"Vila Reis (Apucarana)\", 18),\r\n" + 
					"(10285, \"Vila Rica\", 13),\r\n" + 
					"(10286, \"Vila Rica (Chapada)\", 23),\r\n" + 
					"(10287, \"Vila Rica de Caviana (Manacapuru)\", 3),\r\n" + 
					"(10288, \"Vila Rica do Iva� (Icara�ma)\", 18),\r\n" + 
					"(10289, \"Vila Rica (Vicentina)\", 12),\r\n" + 
					"(10290, \"Vilarinho do Monte (Porto de Moz)\", 14),\r\n" + 
					"(10291, \"Vila Santa F� (Marab�)\", 14),\r\n" + 
					"(10292, \"Vila S�o Francisco (Arapiraca)\", 2),\r\n" + 
					"(10293, \"Vilas Boas (Guiricema)\", 11),\r\n" + 
					"(10294, \"Vila Seca (Caxias do Sul)\", 23),\r\n" + 
					"(10295, \"Vila Seca (Santo �ngelo)\", 23),\r\n" + 
					"(10296, \"Vila Sertaneja (Uirapuru)\", 9),\r\n" + 
					"(10297, \"Vila Silva Jardim (Paranacity)\", 18),\r\n" + 
					"(10298, \"Vila Soares (Apuiar�s)\", 6),\r\n" + 
					"(10299, \"Vila Socorro (Santar�m)\", 14),\r\n" + 
					"(10300, \"Vila Souza (S�o Jos� do Ouro)\", 23),\r\n" + 
					"(10301, \"Vila Turvo (Campo Novo)\", 23),\r\n" + 
					"(10302, \"Vila Uni�o (Deod�polis)\", 12),\r\n" + 
					"(10303, \"Vila Val�rio\", 8),\r\n" + 
					"(10304, \"Vila Vargas (Dourados)\", 12),\r\n" + 
					"(10305, \"Vila Velha\", 8),\r\n" + 
					"(10306, \"Vila Velha (Oiapoque)\", 4),\r\n" + 
					"(10307, \"Vila Velha (Ponta Grossa)\", 18),\r\n" + 
					"(10308, \"Vila Verde (Pancas)\", 8),\r\n" + 
					"(10309, \"Vila Z� A�u (Parintins)\", 3),\r\n" + 
					"(10310, \"Vilhena\", 21),\r\n" + 
					"(10311, \"Vinh�tico (Montanha)\", 8),\r\n" + 
					"(10312, \"Vinhedo\", 26),\r\n" + 
					"(10313, \"Vinte e Cinco de Julho (Santa Teresa)\", 8),\r\n" + 
					"(10314, \"Vira��o (Exu)\", 16),\r\n" + 
					"(10315, \"Viradouro\", 26),\r\n" + 
					"(10316, \"Virgem da Lapa\", 11),\r\n" + 
					"(10317, \"Virg�nia\", 11),\r\n" + 
					"(10318, \"Virgin�polis\", 11),\r\n" + 
					"(10319, \"Virgol�ndia\", 11),\r\n" + 
					"(10320, \"Virmond\", 18),\r\n" + 
					"(10321, \"Visconde de Imb� (Trajano de Moraes)\", 19),\r\n" + 
					"(10322, \"Visconde de Mau� (Resende)\", 19),\r\n" + 
					"(10323, \"Visconde do Rio Branco\", 11),\r\n" + 
					"(10324, \"Viseu\", 14),\r\n" + 
					"(10325, \"Vista Alegre\", 23),\r\n" + 
					"(10326, \"Vista Alegre (Cataguases)\", 11),\r\n" + 
					"(10327, \"Vista Alegre (Claro dos Po��es)\", 11),\r\n" + 
					"(10328, \"Vista Alegre (Colorado)\", 23),\r\n" + 
					"(10329, \"Vista Alegre (Coronel Vivida)\", 18),\r\n" + 
					"(10330, \"Vista Alegre (Curu��)\", 14),\r\n" + 
					"(10331, \"Vista Alegre do Abun� (Porto Velho)\", 21),\r\n" + 
					"(10332, \"Vista Alegre do Alto\", 26),\r\n" + 
					"(10333, \"Vista Alegre do Par� (Marapanim)\", 14),\r\n" + 
					"(10334, \"Vista Alegre do Prata\", 23),\r\n" + 
					"(10335, \"Vista Alegre (En�as Marques)\", 18),\r\n" + 
					"(10336, \"Vista Alegre (Maracaju)\", 12),\r\n" + 
					"(10337, \"Vista Bonita (Campo Largo)\", 18),\r\n" + 
					"(10338, \"Vista Ga�cha\", 23),\r\n" + 
					"(10339, \"Vista Ga�cha (Erval Seco)\", 23),\r\n" + 
					"(10340, \"Vista Nova (Crissiumal)\", 23),\r\n" + 
					"(10341, \"Vista Serrana\", 15),\r\n" + 
					"(10342, \"Vit�ria\", 8),\r\n" + 
					"(10343, \"Vit�ria Brasil\", 26),\r\n" + 
					"(10344, \"Vit�ria da Conquista\", 5),\r\n" + 
					"(10345, \"Vit�ria das Miss�es\", 23),\r\n" + 
					"(10346, \"Vit�ria de Santo Ant�o\", 16),\r\n" + 
					"(10347, \"Vit�ria do Jari\", 4),\r\n" + 
					"(10348, \"Vit�ria do Mearim\", 10),\r\n" + 
					"(10349, \"Vit�ria do Xingu\", 14),\r\n" + 
					"(10350, \"Vit�ria (Ibia��)\", 23),\r\n" + 
					"(10351, \"Vitorino\", 18),\r\n" + 
					"(10352, \"Vitorino Freire\", 10),\r\n" + 
					"(10353, \"Vitorinos (Alto Rio Doce)\", 11),\r\n" + 
					"(10354, \"V�tor Meireles\", 24),\r\n" + 
					"(10355, \"Volta Alegre (Espumoso)\", 23),\r\n" + 
					"(10356, \"Volta da Serra (Paratinga)\", 5),\r\n" + 
					"(10357, \"Volta do Moxot� (Jatob�)\", 16),\r\n" + 
					"(10358, \"Volta Fechada (Aratiba)\", 23),\r\n" + 
					"(10359, \"Volta Grande\", 11),\r\n" + 
					"(10360, \"Volta Grande (Alpestre)\", 23),\r\n" + 
					"(10361, \"Volta Grande (Tapiramut�)\", 5),\r\n" + 
					"(10362, \"Volta Redonda\", 19),\r\n" + 
					"(10363, \"Volta Vit�ria (Campos Borges)\", 23),\r\n" + 
					"(10364, \"Votorantim\", 26),\r\n" + 
					"(10365, \"Votuporanga\", 26),\r\n" + 
					"(10366, \"Wagner\", 5),\r\n" + 
					"(10367, \"Wall Ferraz\", 17),\r\n" + 
					"(10368, \"Wanderl�ndia\", 27),\r\n" + 
					"(10369, \"Wanderley\", 5),\r\n" + 
					"(10370, \"Warta (Londrina)\", 18),\r\n" + 
					"(10371, \"Wenceslau Braz\", 18),\r\n" + 
					"(10372, \"Wenceslau Braz\", 11),\r\n" + 
					"(10373, \"Wenceslau Guimar�es\", 5),\r\n" + 
					"(10374, \"Werneck (Para�ba do Sul)\", 19),\r\n" + 
					"(10375, \"Westfalia\", 23),\r\n" + 
					"(10376, \"Witmarsum\", 24),\r\n" + 
					"(10377, \"Xadrez (Coqueiros do Sul)\", 23),\r\n" + 
					"(10378, \"Xambio�\", 27),\r\n" + 
					"(10379, \"Xambr�\", 18),\r\n" + 
					"(10380, \"Xangri-L�\", 23),\r\n" + 
					"(10381, \"Xanxer�\", 24),\r\n" + 
					"(10382, \"Xapuri\", 1),\r\n" + 
					"(10383, \"Xarqueada (Putinga)\", 23),\r\n" + 
					"(10384, \"Xavantina\", 24),\r\n" + 
					"(10385, \"Xaxim\", 24),\r\n" + 
					"(10386, \"Xaxim (Progresso)\", 23),\r\n" + 
					"(10387, \"Xaxim (Toledo)\", 18),\r\n" + 
					"(10388, \"Xex�u\", 16),\r\n" + 
					"(10389, \"Xicuru (Caruaru)\", 16),\r\n" + 
					"(10390, \"Xinguara\", 14),\r\n" + 
					"(10391, \"Xinguarinha (Xinguara)\", 14),\r\n" + 
					"(10392, \"Xiniqu� (S�o Pedro do Sul)\", 23),\r\n" + 
					"(10393, \"Xique-Xique\", 5),\r\n" + 
					"(10394, \"Xique-Xique (Caruaru)\", 16),\r\n" + 
					"(10395, \"Xucuru (Belo Jardim)\", 16),\r\n" + 
					"(10396, \"Yolanda (Ubirat�)\", 18),\r\n" + 
					"(10397, \"Zabel�\", 15),\r\n" + 
					"(10398, \"Zacarias\", 26),\r\n" + 
					"(10399, \"Z� Doca\", 10),\r\n" + 
					"(10400, \"Z� Gomes (Exu)\", 16),\r\n" + 
					"(10401, \"Zel�ndia (Santa Juliana)\", 11),\r\n" + 
					"(10402, \"Zito Soares (Santa Cruz do Escalvado)\", 11),\r\n" + 
					"(10403, \"Zort�a\", 24),\r\n" + 
					"(10404, \"Caibura (Santos)\", 26),\r\n" + 
					"(10405, \"Bandeirantes D'Oeste (Sud Mennucci)\", 26),\r\n" + 
					"(10406, \"Santo Ant�nio da Estiva (Piraju�)\", 26),\r\n" + 
					"(10407, \"Frutal do Campo (C�ndido Mota)\", 26),\r\n" + 
					"(10408, \"Barra Seca (S�o Francisco de Itabapoana)\", 19),\r\n" + 
					"(10409, \"Barra Alegre (Bom Jardim)\", 19),\r\n" + 
					"(10410, \"Isabel (Domingos Martins)\", 8),\r\n" + 
					"(10411, \"Ita�ca (Cachoeiro de Itapemirim)\", 8),\r\n" + 
					"(10412, \"S�o Francisco (S�o Domingos do Norte)\", 8),\r\n" + 
					"(10413, \"Era Nova (Alpercata)\", 11),\r\n" + 
					"(10414, \"Santa Vit�ria de Cocais dos Arrudas (Coronel Fabriciano)\", 11),\r\n" + 
					"(10415, \"Floresta (Central de Minas)\", 11),\r\n" + 
					"(10416, \"S�o Bartolomeu de Sem Peixe (Sem Peixe)\", 11),\r\n" + 
					"(10417, \"Santana do Paraopeba (Belo Vale)\", 11),\r\n" + 
					"(10418, \"Bom Jardim das Pedras (Carm�polis de Minas)\", 11),\r\n" + 
					"(10419, \"Silva Xavier (Sete Lagoas)\", 11),\r\n" + 
					"(10420, \"Curral Novo de Minas (Ant�nio Carlos)\", 11),\r\n" + 
					"(10421, \"Santa Quit�ria (Congonhas)\", 11),\r\n" + 
					"(10422, \"Santo Ant�nio do Pirapetinga (Piranga)\", 11),\r\n" + 
					"(10423, \"Vinte Alqueires (Porto Firme)\", 11),\r\n" + 
					"(10424, \"Angustura (Al�m Para�ba)\", 11),\r\n" + 
					"(10425, \"Bom Jesus da Cachoeira (Muria�)\", 11),\r\n" + 
					"(10426, \"Santa Quit�ria (Santana do Manhua�u)\", 11),\r\n" + 
					"(10427, \"Frei Eust�quio (Coqueiral)\", 11),\r\n" + 
					"(10428, \"Botic�o (Aguanil)\", 11),\r\n" + 
					"(10429, \"Iterer� (Wenceslau Braz)\", 11),\r\n" + 
					"(10430, \"Contria (Corinto)\", 11),\r\n" + 
					"(10431, \"Olhos D�gua (C�nego Marinho)\", 11),\r\n" + 
					"(10432, \"Freire Cardoso (Coronel Murta)\", 11),\r\n" + 
					"(10433, \"Posses (Leme do Prado)\", 11),\r\n" + 
					"(10434, \"S�o Jos� do Jacar� (Senhora do Porto)\", 11),\r\n" + 
					"(10435, \"M�e dos Homens (S�o Sebasti�o do Maranh�o)\", 11),\r\n" + 
					"(10436, \"Guarajuba (Cama�ari)\", 5),\r\n" + 
					"(10437, \"Itacava (Cora��o de Maria)\", 5),\r\n" + 
					"(10438, \"S�tio do Meio (Castro Alves)\", 5),\r\n" + 
					"(10439, \"Dias Coelho (Morro do Chap�u)\", 5),\r\n" + 
					"(10440, \"Abelhas (Vit�ria da Conquista)\", 5),\r\n" + 
					"(10441, \"Areia Branca (Jussari)\", 5),\r\n" + 
					"(10442, \"Banco Central (Ilh�us)\", 5),\r\n" + 
					"(10443, \"Maiquinique\", 5),\r\n" + 
					"(10444, \"Piabanha (Aracatu)\", 5),\r\n" + 
					"(10445, \"Ibatui (Entre Rios)\", 5),\r\n" + 
					"(10446, \"Porto Sau�pe (Entre Rios)\", 5),\r\n" + 
					"(10448, \"Po�o Fundo (Santa Cruz do Capibaribe)\", 16),\r\n" + 
					"(10449, \"Barra do Chata (Agrestina)\", 16),\r\n" + 
					"(10450, \"Izacolandia (Petrolina)\", 16),\r\n" + 
					"(10451, \"Cana Brava (S�o Sebasti�o)\", 2),\r\n" + 
					"(10452, \"Olho D'�gua de Cima (Arapiraca)\", 2),\r\n" + 
					"(10453, \"Moreira (Palmeira dos �ndios)\", 2),\r\n" + 
					"(10454, \"Casinha do Homem (Santa Cruz)\", 15),\r\n" + 
					"(10455, \"Bilheira (Sobral)\", 6),\r\n" + 
					"(10456, \"Patos (Sobral)\", 6),\r\n" + 
					"(10457, \"Tangente (Massap�)\", 6),\r\n" + 
					"(10458, \"S�o Jos� (Ipueiras)\", 6),\r\n" + 
					"(10459, \"Balseiros (Ipueiras)\", 6),\r\n" + 
					"(10460, \"Alazans (Ipueiras)\", 6),\r\n" + 
					"(10461, \"V�rzea da Volta (Mora�jo)\", 6),\r\n" + 
					"(10462, \"Riach�o (Mira�ma)\", 6),\r\n" + 
					"(10463, \"Po�o Doce (Paracuru)\", 6),\r\n" + 
					"(10464, \"S�o Pedro (Paracuru)\", 6),\r\n" + 
					"(10465, \"Munguba (Trairi)\", 6),\r\n" + 
					"(10466, \"Pascoal (Pacajus)\", 6),\r\n" + 
					"(10467, \"Cruzeirinho (Ic�)\", 6),\r\n" + 
					"(10468, \"Solid�o (Acopiara)\", 6),\r\n" + 
					"(10469, \"Aurora (Deputado Irapuan Pinheiro)\", 6),\r\n" + 
					"(10470, \"Marato� (Deputado Irapuan Pinheiro)\", 6),\r\n" + 
					"(10471, \"Velame (Deputado Irapuan Pinheiro)\", 6),\r\n" + 
					"(10472, \"Cachoeira de Fora (Arneiroz)\", 6),\r\n" + 
					"(10473, \"Planalto (Arneiroz)\", 6),\r\n" + 
					"(10474, \"Buriti Cortado (Timon)\", 10),\r\n" + 
					"(10475, \"Nova Mocajuba (Bragan�a)\", 14),\r\n" + 
					"(10476, \"Jandia� (Inhangapi)\", 14),\r\n" + 
					"(10477, \"S�o Sebasti�o do Rio Claro (Jussara)\", 9),\r\n" + 
					"(10478, \"Taquarussu do Tocantins (Palmas)\", 27),\r\n" + 
					"(10479, \"Nova Esperan�a (Santo Ant�nio do Leverger)\", 13),\r\n" + 
					"(10480, \"Vila Progresso (Salto do C�u)\", 13),\r\n" + 
					"(10481, \"Monte Castelo do Oeste (Gl�ria D'Oeste)\", 13),\r\n" + 
					"(10482, \"Del Rios (Diamantino)\", 13),\r\n" + 
					"(10483, \"Campos Novos (Alto Paraguai)\", 13),\r\n" + 
					"(10484, \"Catuai (S�o Jos� do Rio Claro)\", 13),\r\n" + 
					"(10485, \"Indian�polis (Barra do Gar�as)\", 13),\r\n" + 
					"(10486, \"Santa Elvira (Juscimeira)\", 13),\r\n" + 
					"(10487, \"Cap�o Seco (Sidrol�ndia)\", 12),\r\n" + 
					"(10488, \"Nossa Senhora de F�tima (Bela Vista)\", 12),\r\n" + 
					"(10489, \"Pontinha do Cocho (Camapu�)\", 12),\r\n" + 
					"(10490, \"�rvore Grande (Parana�ba)\", 12),\r\n" + 
					"(10491, \"S�o Jos� do Sucuri� (Inoc�ncia)\", 12),\r\n" + 
					"(10492, \"Presidente Castelo (Deod�polis)\", 12),\r\n" + 
					"(10493, \"Campina (S�o Jos� dos Pinhais)\", 18),\r\n" + 
					"(10494, \"A�ungui (Rio Branco do Sul)\", 18),\r\n" + 
					"(10495, \"Piriquitos (Ponta Grossa)\", 18),\r\n" + 
					"(10496, \"Sete Saltos de Cima (Ponta Grossa)\", 18),\r\n" + 
					"(10497, \"Cambiju (Ponta Grossa)\", 18),\r\n" + 
					"(10498, \"Lajeado (Castro)\", 18),\r\n" + 
					"(10499, \"Alecrim (Curi�va)\", 18),\r\n" + 
					"(10500, \"S�o Domingos (Uni�o da Vit�ria)\", 18),\r\n" + 
					"(10501, \"Palmeirinha (Guarapuava)\", 18),\r\n" + 
					"(10502, \"Fazenda dos Barbosas (Guarapuava)\", 18),\r\n" + 
					"(10503, \"S�o Roque (Pinh�o)\", 18),\r\n" + 
					"(10504, \"Antas (Laranjeiras do Sul)\", 18),\r\n" + 
					"(10505, \"Passo Liso (Laranjeiras do Sul)\", 18),\r\n" + 
					"(10506, \"Porto Santana (Porto Barreiro)\", 18),\r\n" + 
					"(10507, \"Porto Belo (Foz do Igua�u)\", 18),\r\n" + 
					"(10508, \"Vila Celeste (Santa Helena)\", 18),\r\n" + 
					"(10509, \"Iguipor� (Marechal C�ndido Rondon)\", 18),\r\n" + 
					"(10510, \"Vila Nova de Floren�a (S�o Jer�nimo da Serra)\", 18),\r\n" + 
					"(10511, \"Pinhalzinho (Apucarana)\", 18),\r\n" + 
					"(10512, \"Santa B�rbara (Ivaipor�)\", 18),\r\n" + 
					"(10514, \"Santo Rei (Nova Cantu)\", 18),\r\n" + 
					"(10515, \"Cristo Rei (Paranava�)\", 18),\r\n" + 
					"(10516, \"Santa Maria (Alto Paran�)\", 18),\r\n" + 
					"(10517, \"Santa Cruz do Pery (Curitibanos)\", 24),\r\n" + 
					"(10518, \"Bom Retiro (Eldorado do Sul)\", 23),\r\n" + 
					"(10519, \"Parque Eldorado (Eldorado do Sul)\", 23),\r\n" + 
					"(10520, \"Morro Grande (Muitos Cap�es)\", 23),\r\n" + 
					"(10521, \"Capela da Luz (Monte Alegre dos Campos)\", 23),\r\n" + 
					"(10522, \"Casa Velha (Palmares do Sul)\", 23),\r\n" + 
					"(10523, \"Harmonia (S�o Louren�o do Sul)\", 23),\r\n" + 
					"(10524, \"Iru� (Rio Pardo)\", 23),\r\n" + 
					"(10525, \"Rinc�o Del Rei (Rio Pardo)\", 23),\r\n" + 
					"(10526, \"Tabo�ozinho (Arroio do Tigre)\", 23),\r\n" + 
					"(10527, \"Caver� (Ros�rio do Sul)\", 23),\r\n" + 
					"(10528, \"Tr�s M�rtires (J�lio de Castilhos)\", 23),\r\n" + 
					"(10529, \"A�oita Cavalo (Esperan�a do Sul)\", 23),\r\n" + 
					"(10530, \"Floresta (Iju�)\", 23),\r\n" + 
					"(10531, \"Ita� (Iju�)\", 23),\r\n" + 
					"(10532, \"Pratos (Novo Machado)\", 23),\r\n" + 
					"(10533, \"Campo do Meio (Gentil)\", 23),\r\n" + 
					"(10534, \"Treze de Maio (Alto Alegre)\", 23),\r\n" + 
					"(10535, \"Linha Ferrari (Campos Borges)\", 23),\r\n" + 
					"(10536, \"Coronel Teixeira (Marcelino Ramos)\", 23),\r\n" + 
					"(10537, \"Cruzaltinha (Cir�aco)\", 23),\r\n" + 
					"(10538, \"Serrinha (Bom Jesus do Itabapoana)\", 19),\r\n" + 
					"(10539, \"Trajano de Moraes\", 19),\r\n" + 
					"(10540, \"Porang� (Barra de S�o Francisco)\", 8),\r\n" + 
					"(10541, \"Santa Maria do Baixio (S�o Jo�o do Oriente)\", 11),\r\n" + 
					"(10542, \"Vargem Grande da Serra (Baldim)\", 11),\r\n" + 
					"(10543, \"S�o Miguel do Cajuru (S�o Jo�o Del Rei)\", 11),\r\n" + 
					"(10544, \"Vardiero (Bar�o de Monte Alto)\", 11),\r\n" + 
					"(10545, \"Vila Bom Jesus de Minas (Chapada Ga�cha)\", 11),\r\n" + 
					"(10546, \"Santana da Serra (Capit�o En�as)\", 11),\r\n" + 
					"(10547, \"Campo Alegre de Minas (Ibiracatu)\", 11),\r\n" + 
					"(10548, \"Varzea Grande (Pindoba�u)\", 5),\r\n" + 
					"(10549, \"Bananeiras (Pindoba�u)\", 5),\r\n" + 
					"(10550, \"Espraiado (Palmas de Monte Alto)\", 5),\r\n" + 
					"(10551, \"Rancho das M�es (Palmas de Monte Alto)\", 5),\r\n" + 
					"(10552, \"Pinga Fogo (Palmas de Monte Alto)\", 5),\r\n" + 
					"(10553, \"Ros�rio (Correntina)\", 5),\r\n" + 
					"(10554, \"Mimoso (S�tiro Dias)\", 5),\r\n" + 
					"(10555, \"Bela Vista (S�tiro Dias)\", 5),\r\n" + 
					"(10556, \"S�o Jos� (Panelas)\", 16),\r\n" + 
					"(10557, \"S�o Domingos\", 15),\r\n" + 
					"(10558, \"Rafael Arruda (Sobral)\", 6),\r\n" + 
					"(10559, \"Ingazeiras (Ipu)\", 6),\r\n" + 
					"(10560, \"Vista Alegre (Croat�)\", 6),\r\n" + 
					"(10561, \"Reparti��o (Croat�)\", 6),\r\n" + 
					"(10562, \"Baleia (Itapipoca)\", 6),\r\n" + 
					"(10563, \"Saquinho (Jaguaruana)\", 6),\r\n" + 
					"(10564, \"Guassuss� (Or�s)\", 6),\r\n" + 
					"(10565, \"A�udinho (Tamboril)\", 6),\r\n" + 
					"(10566, \"Paus Branco (Madalena)\", 6),\r\n" + 
					"(10567, \"Cajazeiras (Madalena)\", 6),\r\n" + 
					"(10568, \"Cacimba Nova (Madalena)\", 6),\r\n" + 
					"(10569, \"Uni�o (Madalena)\", 6),\r\n" + 
					"(10570, \"Cidade Ecl�tica (Santo Ant�nio do Descoberto)\", 9),\r\n" + 
					"(10571, \"Luzimangues (Porto Nacional)\", 27),\r\n" + 
					"(10572, \"Fazenda da Estrela (Vacaria)\", 23),\r\n" + 
					"(10573, \"Nossa Senhora de F�tima (Santo Augusto)\", 23),\r\n" + 
					"(10574, \"Nossa Senhora Aparecida (Tunas)\", 23),\r\n" + 
					"(10575, \"Boa Esperan�a (Mora�jo)\", 6),\r\n" + 
					"(10576, \"�gua Fria (Quixer�)\", 6),\r\n" + 
					"(10577, \"Basti�es (Iracema)\", 6),\r\n" + 
					"(10578, \"Caldas (Barbalha)\", 6),\r\n" + 
					"(10579, \"Manoel Correia (Momba�a)\", 6),\r\n" + 
					"(10580, \"Grosl�ndia (Lucas do Rio Verde)\", 13),\r\n" + 
					"(10581, \"Uni�o do Norte (Peixoto de Azevedo)\", 13);\r\n";
			try{
				Statement stmt = conexao.createStatement();
				stmt.execute(comandos[0]);
				stmt.execute(comandos[1]);
			}catch (Exception e) {System.out.println("Falha ao realizar insert:\n" + e.getMessage());}
	 }
}
