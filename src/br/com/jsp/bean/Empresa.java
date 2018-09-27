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
import br.com.jsp.dao.EmpresaDao;
import br.com.jsp.dao.FuncionarioDao;
import br.com.jsp.dao.ItemDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;

import java.lang.reflect.Array;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
@Tabela(nome = "empresas")
public class Empresa{
	
	@Deprecated
	public Empresa() {}
	
	public Empresa(String login, String senha, String nome, String cnpj, String email, String telefone, Local local) {
		this(new Conta(login, 
				senha, 
				NivelDeAcesso.Empresa), 
				nome, 
				cnpj, 
				email, 
				telefone, 
				local);
	}
	
	public boolean equals(Empresa i) {
		
		if(this.id == i.id) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		String s = "";
		
		s += "\nid = " + id;
		s += "\nnome = " + nome;
	    s += "\ncnpj = " + cnpj;
	    s += "\nemail = " + email;
	    s += "\ntelefone = " + telefone;
	    s += "\nidLocal = " + idLocal;
	    s += "\nidConta = " +  idConta;
	    
	    
		return s;
	}
	
	public Empresa(Conta conta, String nome, String cnpj, String email, String telefone, Local local) {
		this.conta = conta;
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

    public void contratarFuncionario(String cpf, Conta conta) {
    	
    	Funcionario funcionario = new Funcionario(this, cpf, conta);
    	
    	funcionario.cadastrar();
    	
    }
    
    private void demitirFuncionario() {
	
    	//TODO
    	try {
			throw new Exception("não implementado");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
    
    public Resposta<ArrayList<Funcionario>> meusFuncionarios(){
    	Resposta<ArrayList<Funcionario>> resp = FuncionarioDao.selectWhere("idEmpresa", Where.IGUAL, this.id);
    	return resp;
    }
    
    public Resposta<ArrayList<Item>> itensPerdidosAqui(){
    	
    	ArrayList<Item> resposta = new ArrayList<>();
    	
    	Resposta<ArrayList<Item>> resp = ItemDao.selectAll();
    	
    	if(resp.getFuncionou()) {
    		
    		if(resp.getObjeto().isEmpty()) {
    			return new Resposta<>("Erro");
    		}
    		
    		for (Item item : resp.getObjeto()) {
				
    			if(item.getEmpresa().equals(this)) {
    				resposta.add(item);
    			}
    			
			}
    		
    		if(resposta.isEmpty()) {
    			return new Resposta<>("Nenhum item encontrado");
    		}
    		
    		return new Resposta<ArrayList<Item>>("Operação efetuada com sucesso", resposta);
    		
    	}else {
    		return new Resposta<>("Erro");
    	}
    	
    	
    }
    
    
    public static Resposta<Integer> Cadastrar(Empresa empresa) {
		Resposta<Integer> respConta = Conta.cadastrar(empresa.conta);
		Resposta<Integer> respLocal = Local.cadastrar(empresa.local);
		if(respConta.getFuncionou()) {
			
			if(respLocal.getFuncionou()) {
				empresa.idConta = empresa.conta.getId();
				empresa.idLocal = empresa.local.getId();
				
				Resposta<Integer> resp = EmpresaDao.insert(empresa);
				
				return resp;
				
			}else {
				return respLocal;
			}
			
		} else {
			return respConta;
		}
		
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
