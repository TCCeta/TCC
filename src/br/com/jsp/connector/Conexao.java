package br.com.jsp.connector;
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
        String s= "whatsmissing";
        url = "jdbc:mysql://localhost:3306/" + s;
        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public Connection obterConexao(){
	        
		 Connection conexao = null;
	        
	        try{
	            conexao = DriverManager.getConnection(url, user, password);
	            
	        }catch(Exception e){
	        	if (e.getMessage().equals("Unknown database 'whatsmissing'")){
	        		try {
						conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql", user, password);						
						PreparedStatement pstmt = conexao.prepareStatement("CREATE DATABASE whatsmissing;");
						pstmt.execute();
						conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/whatsmissing", user, password);
						criarTabela(conexao);
						criarRelacionamento(conexao);
						conexao.close();
					} catch (SQLException e1) {System.out.println("Falha na criação: " + e1.getMessage());}
					

	        	}
	        	else {
	        		JOptionPane.showMessageDialog(null, e.getMessage());
	        		throw new RuntimeException(e);
	        	}
	            
	        }
	        
	        return conexao;
	        
	    }

	 private void criarTabela(Connection conexao){
		 String[] comandos = new String[8];
		 try {
			 Statement pstmt = conexao.createStatement();
			 
			 comandos[0] = "CREATE TABLE empresas ("
				 	 + "cod_idEmpresa INT NOT NULL AUTO_INCREMENT	PRIMARY KEY,"
				 	 + "dad_nomeEmpresa varchar(50) ,"
				 	 + "dad_cnpjEmpresa varchar(20) ,"
				 	 + "dad_emailEmpresa varchar(100) ,"
				 	 + "dad_telefoneEmpresa varchar(15) ,"
				 	 + "cod_idConta INT ,"
				 	 + "cod_idLocal INT);";

			 comandos[1] = "CREATE TABLE `funcionarios` ("
					 + "`cod_idFuncionario` INT  AUTO_INCREMENT PRIMARY KEY,"
					 + "`cod_idEmpresa` INT ,"
					 + "`dad_CPF` VARCHAR(20) ,"
					 + "`cod_idConta` INT"
					 + ");";
		 
			 comandos[2] = "CREATE TABLE itens ("
				 	 + "cod_idItem INT  AUTO_INCREMENT PRIMARY KEY ,"
				 	 + "dad_nomeItem VARCHAR(50) ,"
				 	 + "dad_descricaoItem VARCHAR(1000) ,"
				 	 + "cod_idFuncionario INT ,"
				 	 + "cod_idImagem INT ,"
				 	 + "dat_dataPerdidoItem DATE ,"
				 	 + "dat_dataDevolvidoItem DATE ,"
				 	 + "dad_devolvidoItem BOOLEAN);";

			 comandos[3] = "CREATE TABLE usuarios ("
				  	 + "cod_idUsuario INT AUTO_INCREMENT PRIMARY KEY,"
				  	 + "cod_idConta INT NOT NULL,"
				  	 + "cod_idLocal INT NOT NULL,"
				  	 + "cod_idPessoa INT NOT NULL);";
		 
			 comandos[4] = "CREATE TABLE imagens ("
				 	 + "cod_idImagem INT AUTO_INCREMENT PRIMARY KEY,"
				 	 + "img_Imagem blob);";

			 comandos[5] = "CREATE TABLE pessoas ("
		  			 + "cod_idPessoa INT AUTO_INCREMENT PRIMARY KEY,"
		  			 + "dad_nomePessoa varchar(50),"
		  			 + "dad_cpfPessoa varchar(14),"
		  			 + "dad_emailPessoa varchar(100),"
		  			 + "dad_telefonePessoa varchar(20));";

			 comandos[6] = "CREATE TABLE contas ("
				 	 + "cod_idConta INT AUTO_INCREMENT PRIMARY KEY,"
				 	 + "dad_loginConta varchar(50),"
				 	 + "dad_senhaConta varchar(50),"
				 	 + "dad_nvlAcesso INT DEFAULT '1',"
				 	 + "dad_salt VARCHAR(50));";

			 comandos[7] = "CREATE TABLE locais ("
					+ "cod_idLocal INT AUTO_INCREMENT PRIMARY KEY,"
					+ "dad_rua VARCHAR(150),"
					+ "dad_bairro VARCHAR(150),"
					+ "dad_cidade VARCHAR(150),"
					+ "dad_estado VARCHAR(150),"
					+ "dad_cep VARCHAR(150));";

			 for (String comando : comandos) {
				pstmt.execute(comando);
			}
		} catch (SQLException e) {}
	 }

	 private void criarRelacionamento(Connection conexao){
		 String[] comandos = new String[8];
		 comandos[0] = "ALTER TABLE `empresas` ADD CONSTRAINT `empresas_fk0` FOREIGN KEY (`cod_idConta`) REFERENCES `contas`(`cod_idConta`);";
		 comandos[1] = "ALTER TABLE empresas ADD CONSTRAINT `empresas_fk1` FOREIGN KEY (`cod_idLocal`) REFERENCES `locais`(`cod_idLocal`);";
		 comandos[2] = "ALTER TABLE `funcionarios` ADD CONSTRAINT `funcionarios_fk0` FOREIGN KEY (`cod_idEmpresa`) REFERENCES `empresas`(`cod_idEmpresa`);";
		 comandos[3] = "ALTER TABLE `funcionarios` ADD CONSTRAINT `funcionarios_fk2` FOREIGN KEY (`cod_idConta`) REFERENCES `contas`(`cod_idConta`);";
		 comandos[4] = "ALTER TABLE `itens` ADD CONSTRAINT `itens_fk0` FOREIGN KEY (`cod_idFuncionario`) REFERENCES `funcionarios`(`cod_idFuncionario`);";
		 comandos[5] = "ALTER TABLE `usuarios` ADD CONSTRAINT `usuarios_fk0` FOREIGN KEY (`cod_idConta`) REFERENCES `contas`(`cod_idConta`);";
		 comandos[6] = "ALTER TABLE `usuarios` ADD CONSTRAINT `usuarios_fk1` FOREIGN KEY (`cod_idLocal`) REFERENCES `locais`(`cod_idLocal`);";
		 comandos[7] = "ALTER TABLE `usuarios` ADD CONSTRAINT `usuarios_fk2` FOREIGN KEY (`cod_idPessoa`) REFERENCES `pessoas`(`cod_idPessoa`);";
				 		 
		 try{
			 Statement stmt = conexao.createStatement();
			 for (String comando : comandos) {
				stmt.execute(comando);
			 }
		 }catch (Exception e) {System.out.println("Falha ao criar relação no banco de dados:\n"+e.getMessage());}
	 
	 }

}

