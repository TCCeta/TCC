/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.bean.Enums.NivelDeAcesso;
import br.com.jsp.dao.EmpresaDao;

import java.sql.Types;

/**
 *
 * @author 103782
 */
@Tabela(nome = "empresas")
public class Empresa{
	
	@Deprecated
	public Empresa() {}
	
	public Empresa(String login, String senha, String nome, String cnpj, String email, String telefone, Local local) {
		this(new Conta(login, senha, NivelDeAcesso.Empresa), nome, cnpj, email, telefone, local);
	}
	
	public Empresa(Conta conta, String nome, String cnpj, String email, String telefone, Local local) {
		this.conta = conta;
		this.idConta = conta.getId();
		this.nome = nome;
		this.cnpj = cnpj;
		this.email = email;
		this.telefone = telefone;
		this.local = local;
		this.idLocal = local.getId();
	}
	
	

	@Coluna(nome = "cod_idEmpresa", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "dad_nomeEmpresa", tipo = Types.VARCHAR)
    private String nome;
    
    @Coluna(nome = "dad_cnpjEmpresa", tipo = Types.VARCHAR)
    private String cnpj;
    
    @Coluna(nome = "dad_emailEmpresa", tipo = Types.VARCHAR)
    private String email;
    
    @Coluna(nome = "dad_telefoneEmpresa", tipo = Types.VARCHAR)
    private String telefone;
    
    @Coluna(nome = "cod_idLocal", tipo = Types.INTEGER)
    private int idLocal;
    private Local local;
    
    @Coluna(nome = "cod_idConta", tipo = Types.INTEGER)
    private int idConta;
    private Conta conta;

    
    
    public static void Cadastrar(Empresa empresa) {
		Conta.Cadastrar(empresa.conta);
		empresa.idConta = empresa.conta.getId();
		
		Local.cadastrar(empresa.local);
		empresa.idLocal = empresa.local.getId();
		
		EmpresaDao.insert(empresa);
	}
    
    public static void atualizar(Empresa empresa) {
    	
    	Conta.Atualizar(empresa.conta);
    	Local.atualizar(empresa.local);
    	EmpresaDao.update(empresa);
    }
     
    
    
    //SETTERS
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    //TODO procurar se pode mudar de CNPJ
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public void setLocal(Local local) {
        this.local = local;
        this.idLocal = local.getId();
    }
    
    /**
     * @param conta
     */
    public void setConta(Conta conta) {
    	if(this.conta == null) {
    		this.conta = conta;
    		this.idConta = conta.getId();
    	}
    }
    
    //GETTERS
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Local getLocal() {
        return local;
    }
    
    public int getIdLocal() {
    	return idLocal;
    }

    public Conta getConta() {
        return conta;
    }
    
    public int getIdConta() {
    	return idConta;
    }
    
}
