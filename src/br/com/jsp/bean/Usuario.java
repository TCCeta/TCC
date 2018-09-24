/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.bean.Enums.NivelDeAcesso;
import br.com.jsp.dao.UsuarioDao;

import java.sql.Types;

/**
 *
 * @author 103782
 */
@Tabela(nome = "usuarios")
public class Usuario{

	@Deprecated
	public Usuario() {}
	
    public Usuario(String login, String senha,String nome, String cpf, String email, String telefone, Local local) {
		this(new Conta(login, senha, NivelDeAcesso.Usuario), local, new Pessoa(nome, cpf, email, telefone));
	}
    
    public Usuario(Conta conta, Local local, Pessoa pessoa) {
    	this.conta = conta;
    	this.local = local;
    	this.pessoa = pessoa;
    }
    
    public static void cadastrar(Usuario usuario) {
    	Conta.Cadastrar(usuario.conta);
    	usuario.idConta = usuario.conta.getId();
    	
    	Local.cadastrar(usuario.local);
    	usuario.idLocal = usuario.local.getId();
    	
    	Pessoa.cadastrar(usuario.pessoa);
    	usuario.idPessoa =usuario.pessoa.getId();
    	
    	UsuarioDao.insert(usuario);
    }
    
    public void cadastrar() {
    	Conta.Cadastrar(this.conta);
    	this.idConta = this.conta.getId();
    	
    	Local.cadastrar(this.local);
    	this.idLocal = this.local.getId();
    	
    	Pessoa.cadastrar(this.pessoa);
    	this.idPessoa = this.pessoa.getId();
    	
    	UsuarioDao.insert(this);
    }
    
    public static void atualizar(Usuario usuario) {
    	Conta.Atualizar(usuario.conta);
    	Local.atualizar(usuario.local);
    	Pessoa.atualizar(usuario.pessoa);
    	UsuarioDao.update(usuario);
    }
    
    
    @Coluna(nome = "cod_idUsuario", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "cod_idConta", tipo = Types.INTEGER)
    private int idConta;
    private Conta conta;
    
    @Coluna(nome = "cod_idLocal", tipo = Types.INTEGER)
    private int idLocal;
    private Local local;
    
    @Coluna(nome = "cod_idPessoa", tipo = Types.INTEGER)
    private int idPessoa;
    private Pessoa pessoa;
	

    //SETTERS --------------------------------------
    
    public void setConta(Conta conta) {
    	if(this.conta == null) {
    		this.conta = conta;
    		this.idConta = conta.getId();
    	}
    }
    
    public void setPessoa(Pessoa pessoa) {
    	if(this.pessoa == null) {
    		this.pessoa = pessoa;
    		this.idPessoa = pessoa.getId();
    	}
    }
    
    public void setLocal(Local local) {
    	this.local = local;
    	this.idLocal = local.getId();
    }
    
    //GETTERS --------------------------------------
   
    public int getId() {
		return id;
	}

	public int getIdConta() {
		return idConta;
	}

	public Conta getConta() {
		return conta;
	}

	public int getIdLocal() {
		return idLocal;
	}

	public Local getLocal() {
		return local;
	}

	public int getIdPessoa() {
		return idPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
    
    
    
    
}
