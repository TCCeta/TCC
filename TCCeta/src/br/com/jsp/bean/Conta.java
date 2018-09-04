/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.dao.ContaDao;
import br.com.jsp.encripcao.PasswordUtils;
import java.sql.Types;

/**
 *
 * @author 103782
 */
@Tabela(nome = "contas")
public class Conta {
    
    @Coluna(nome = "cod_idConta", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "dad_loginConta", tipo = Types.VARCHAR)
    private String login;
    
    @Coluna(nome = "dad_senhaConta", tipo = Types.VARCHAR)
    private String senha;
    
    @Coluna(nome = "dad_nvlAcesso", tipo = Types.INTEGER)
    private int nivelDeAcesso;
    
    @Coluna(nome = "dad_salt", tipo = Types.VARCHAR)
    private String salt;
    
    public void setSenha(String senha){
    	
        this.salt = PasswordUtils.getSalt(10);
        String senhaSegura = PasswordUtils.generateSecurePassword(senha, salt);
        this.senha = senhaSegura;
        
    }
    
    public void setID(int id) {
    	
    	this.id = id;
    	
    }
    
    public boolean senhaEstaCorreta(String senhaDigitada){
        return PasswordUtils.verifyUserPassword(senhaDigitada, this.getSenha(), this.salt);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @return the nivelDeAcesso
     */
    public int getNivelDeAcesso() {
        return nivelDeAcesso;
    }

    /**
     * @param nivelDeAcesso the nivelDeAcesso to set
     */
    public void setNivelDeAcesso(int nivelDeAcesso) {
        this.nivelDeAcesso = nivelDeAcesso;
    }
    
    
    /*
    public static void main(String[] args) {
		
    	Conta c = new Conta();
    	c.login = "JAS";
    	c.setNivelDeAcesso(2);
    	c.setSenha("123");
    	
    	ContaDao.insert(c);
    	
	}
    */
    
    
}
