/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.dao.PessoaDao;

import java.sql.Types;

/**
 *
 * @author 103782
 */
@Tabela(nome = "pessoas")
public class Pessoa {

	@Deprecated
	public Pessoa() {}
	
	public Pessoa(String nome, String cpf, String email, String telefone) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.telefone = telefone;
	}
	
	@Coluna(nome = "cod_idPessoa", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "dad_nomePessoa", tipo = Types.VARCHAR)
    private String nome;
    
    @Coluna(nome = "dad_cpfPessoa", tipo = Types.VARCHAR)
    private String cpf;
    
    @Coluna(nome = "dad_emailPessoa", tipo = Types.VARCHAR)
    private String email;
    
    @Coluna(nome = "dad_telefonePessoa", tipo = Types.VARCHAR)
    private String telefone;
    
    
    public static void cadastrar(Pessoa pessoa) {
    	PessoaDao.insert(pessoa);
    }
    
    public static void atualizar(Pessoa pessoa) {
    	PessoaDao.update(pessoa);
    }
    
    
    //SETTERS --------------------------------------
    
    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    //GETTERS --------------------------------------
   
    
	
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    
    

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

}


