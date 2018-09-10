/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe para criar conex�es
 * @author patrick
 */
public class ConnectionFactory {
    
    private final String url;
    private final String user = "root";//"tcceta";
    private final String password = "";//"tcc!@12";
    
    /**
     * Contrutor para conex�o com o banco digitado
     * @param s nome do banco escolhido
     */
    public ConnectionFactory(String s){
        
        //
        
        url = "jdbc:mysql://localhost:3306/" + s;
        
        //url = "jdbc:mysql://bd-tcc-wim.mysql.uhserser.com/" + s;
        //url = "jdbc:mysql://bd_tcc_wim:3306/" + s;

        //url = "jdbc:mysql://tcc-entra21.mysql.uhserver.com:3306/" + s;
        
    }
    
    /**
     * Construtor para conex�o com o banco mysql para quando nao se tem bancos criados ainda
     */
    public ConnectionFactory(){
        
        //
        //url = "jdbc:mysql://localhost:3306/mysql";
        
        url = "jdbc:mysql://bd-tcc-wim.mysql.uhserser.com:3306/mysql";
        
        //url = "jdbc:mysql://tcc-entra21.mysql.uhserver.com:3306/mysql";
        
    }
    
    /**
     * Cria uma conex�o com o banco de dados
     * @return a conex�o criada
     */
    public Connection obterConexao(){
        
        Connection conexao = null;
        
        try{
            
            Class.forName("com.mysql.jdbc.Driver");

            conexao = DriverManager.getConnection(url, user, password);
            
        }catch(Exception e){
            
            JOptionPane.showMessageDialog(null, e.getMessage());
            throw new RuntimeException(e);
            
        }
        return conexao;
        
    }
    
}
