/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.bean.Enums.NivelDeAcesso;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.ContaDao;
import br.com.jsp.dao.LocalDao;
import br.com.jsp.dao.UsuarioDao;

import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
@Tabela(nome = "usuarios")
public class Usuario {

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the idConta
     */
    public int getIdConta() {
        return idConta;
    }

    /**
     * @param idConta the idConta to set
     */
    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    /**
     * @return the idLocal
     */
    public int getIdLocal() {
        return idLocal;
    }

    /**
     * @param idLocal the idLocal to set
     */
    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    /**
     * @return the idPessoa
     */
    public int getIdPessoa() {
        return idPessoa;
    }

    /**
     * @param idPessoa the idPessoa to set
     */
    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }
    
    @Coluna(nome = "cod_idUsuario", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "cod_idConta", tipo = Types.INTEGER)
    private int idConta;
    
    @Coluna(nome = "cod_idLocal", tipo = Types.INTEGER)
    private int idLocal;
    
    @Coluna(nome = "cod_idPessoa", tipo = Types.INTEGER)
    private int idPessoa;
    
    
    public static void main(String[] args) {
    	
    	/*Conta c = new Conta();
    	
    	c.setLogin("Usuario");
    	c.setNivelDeAcesso(NivelDeAcesso.Usuario.ordinal());
    	c.setSenha("12345678");
    	
		ContaDao.insert(c);*/
    	
    	/*Local l = new Local();
    	l.setBairro("bairro");
    	l.setCep("cep");
    	l.setCidade("cidade");
    	l.setEstado("estado");
    	l.setRua("rua");
    	LocalDao.insert(l);*/
    	
    	/*Usuario u = new Usuario();
    	
    	u.idConta = 5;
    	u.idLocal = 1;
    	u.idPessoa = 1;
    	
    	UsuarioDao.insert(u);*/
    
    	
    	
    	Resposta<ArrayList<Usuario>> lista = UsuarioDao.selectAll();
    	
    	for (Usuario c : lista.getObjeto()) {
			
    		System.out.println(" id = " + c.getId());
    		System.out.println(" login = " + c.getIdConta());
    		System.out.println(" nivel = " + c.getIdLocal());
    		System.out.println(" senha = " + c.getIdPessoa() + "\n");
    		
		}
    	
    	
    	
    	
    }
    
    
}
