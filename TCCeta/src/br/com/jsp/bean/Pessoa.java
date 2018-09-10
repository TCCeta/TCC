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
import br.com.jsp.dao.EmpresaDao;
import br.com.jsp.dao.FuncionarioDao;
import br.com.jsp.dao.LocalDao;
import br.com.jsp.dao.PessoaDao;
import br.com.jsp.dao.UsuarioDao;

import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
@Tabela(nome = "pessoas")
public class Pessoa {

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

    
    
    
public static void main(String[] args) {
	
	ArrayList<Conta> contas = ContaDao.selectAll().getObjeto();
	ArrayList<Empresa> empresas = EmpresaDao.selectAll().getObjeto();
	ArrayList<Funcionario> funcs = FuncionarioDao.selectAll().getObjeto();
	ArrayList<Local> locais = LocalDao.selectAll().getObjeto();
	ArrayList<Pessoa> pessoas = PessoaDao.selectAll().getObjeto();
	ArrayList<Usuario> usuarios = UsuarioDao.selectAll().getObjeto();
	
	
	Conta c = new Conta("login1", "123", NivelDeAcesso.Funcionario);
	
	ContaDao.insert(c);
	
	Funcionario f = new Funcionario(empresas.get(0), "cpff2", c);
	
	FuncionarioDao.insert(f);
	
	/*Conta c = new Conta("empresa", "123", NivelDeAcesso.Empresa);
	
	ContaDao.insert(c);
	
	Local l = new Local();
	l.setBairro("bairro");
	l.setCep("cep");
	l.setCidade("cidade");
	l.setEstado("estado");
	l.setRua("rua");
	
	LocalDao.insert(l);
	
	
	Empresa e = new Empresa(c, "Nome empresa", "cnpj da empresa", "empresa@email.empresa.com", "11111111", l);
	
	EmpresaDao.insert(e);
	
	
	Conta c2 = new Conta("loginFuncionario", "123", NivelDeAcesso.Funcionario);
	
	ContaDao.insert(c2);
	
	Funcionario f = new Funcionario(e, "cpfFunc", c2);
	
	FuncionarioDao.insert(f);*/
	
}    
    
}


