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
import br.com.jsp.dao.FuncionarioDao;

import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 
 * @author 103782
 */
@Tabela(nome = "funcionarios")
public class Funcionario{

	@Deprecated
	public Funcionario() {}
	
	public Funcionario(Empresa empresa, String cpf, String login, String senha) {
		this(empresa,cpf, new Conta(login, senha, NivelDeAcesso.Funcionario));
	}
	
	public Funcionario(Empresa empresa, String cpf, Conta conta) {
		this.empresa = empresa;
		this.idEmpresa = empresa.getId();
		this.cpf = cpf;
		this.conta = conta;
	}
	
    @Coluna(nome = "cod_idFuncionario", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "cod_idEmpresa", tipo = Types.INTEGER)
    private int idEmpresa;
    private Empresa empresa;
    
    @Coluna(nome = "dad_CPF", tipo = Types.VARCHAR)
    private String cpf;
    
    @Coluna(nome = "cod_idConta", tipo = Types.INTEGER)
    private int idConta;
    private Conta conta;
    
    private static void cadastrar(Funcionario funcionario) {
    	
    	Conta.cadastrar(funcionario.conta);
    	funcionario.idConta = funcionario.conta.getId();
    	
    	FuncionarioDao.insert(funcionario);
    	
    }
    
    public void cadastrar() {
    	cadastrar(this);
    }
    
    public static void atualizar(Funcionario funcionario) {
    	
    	Conta.Atualizar(funcionario.conta);
    	
    	FuncionarioDao.update(funcionario);
    	
    }

    
    
    public Resposta<Boolean> cadastrarItem(String dataStr, Imagem imagem, String nome, String descricao) {
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	
    	LocalDate data;
    	
    	try {
    		data = LocalDate.parse(dataStr, formatter);
		} catch (Exception e) {
			return new Resposta<>("Data Inv�lida");
		}
    	
    	
    	Item item = new Item(data, this, nome, descricao);
    	
    	if(item.cadastrar().getFuncionou()) {
    		return new Resposta<Boolean>("funcionou", true);
    	}else {
    		return new Resposta<>("Erro ao cadastrar item");
    	}
    	
    }
    
    
    
    
    //SETTERS --------------------------------------
    
    public void setId(int id) {
    	this.id = id;
    }
    
    
    /**
     * @param empresa
     */
    public void setEmpresa(Empresa empresa) {
    	if(this.empresa == null) {
    		this.empresa = empresa;
    	}
    }
    
    public void setConta(Conta conta) {
    	if(this.conta == null) {
    		this.conta = conta;
    	}
    }
    
    //GETTERS --------------------------------------
   
    public int getId() {
    	return id;
    }
    
    public Empresa getEmpresa(){
    	return empresa;
    }
    
    public int getIdEmpresa() {
    	return idEmpresa;
    }
    
    public String getCPF() {
    	return cpf;
    }
    
    public Conta getConta() {
    	return conta;
    }
    
    public int getIdConta() {
    	return idConta;
    }
    
}
