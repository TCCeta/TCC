/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Conta;
import br.com.jsp.bean.Empresa;
import br.com.jsp.bean.Funcionario;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
public class FuncionarioDao {
    
    public static Resposta<ArrayList<Funcionario>> selectAll(){
        
    	Resposta<ArrayList<Funcionario>> respFuncionario = new GenericDao<Funcionario>(Funcionario.class).selectAll();
    	
    	if(!respFuncionario.getFuncionou()) {
    		return new Resposta<>(respFuncionario.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>(respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Empresa>> respEmpresa = EmpresaDao.selectAll();
    	
    	if(!respEmpresa.getFuncionou()) {
    		return new Resposta<>(respEmpresa.getMensagem());
    	}
    	
    	for (Funcionario funcionario : respFuncionario.getObjeto()) {
    		for (Conta conta : respConta.getObjeto()) {
    			if(funcionario.getIdConta() == conta.getId()) {
    				funcionario.setConta(conta);
    			}
			}
    		for (Empresa empresa : respEmpresa.getObjeto()) {
				
    			if(funcionario.getIdEmpresa() == empresa.getId()) {
    				funcionario.setEmpresa(empresa);
    			}
			}
		}
    	
    	return respFuncionario;
        
    }
    
    public static Resposta<ArrayList<Funcionario>> selectAll(String campo, Order order){
        
        Resposta<ArrayList<Funcionario>> respFuncionario = new GenericDao<Funcionario>(Funcionario.class).selectAll(campo, order);
    	
    	if(!respFuncionario.getFuncionou()) {
    		return new Resposta<>(respFuncionario.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>(respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Empresa>> respEmpresa = EmpresaDao.selectAll();
    	
    	if(!respEmpresa.getFuncionou()) {
    		return new Resposta<>(respEmpresa.getMensagem());
    	}
    	
    	for (Funcionario funcionario : respFuncionario.getObjeto()) {
    		for (Conta conta : respConta.getObjeto()) {
    			if(funcionario.getIdConta() == conta.getId()) {
    				funcionario.setConta(conta);
    			}
			}
    		for (Empresa empresa : respEmpresa.getObjeto()) {
				
    			if(funcionario.getIdEmpresa() == empresa.getId()) {
    				funcionario.setEmpresa(empresa);
    			}
			}
		}
    	
    	return respFuncionario;
        
    }
    
    public static Resposta<ArrayList<Funcionario>> selectWhere(String campo, Where comparacao, Object valor){
        
        Resposta<ArrayList<Funcionario>> respFuncionario = new GenericDao<Funcionario>(Funcionario.class).selectWhere(campo, comparacao, valor);
    	
    	if(!respFuncionario.getFuncionou()) {
    		return new Resposta<>(respFuncionario.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>(respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Empresa>> respEmpresa = EmpresaDao.selectAll();
    	
    	if(!respEmpresa.getFuncionou()) {
    		return new Resposta<>(respEmpresa.getMensagem());
    	}
    	
    	for (Funcionario funcionario : respFuncionario.getObjeto()) {
    		for (Conta conta : respConta.getObjeto()) {
    			if(funcionario.getIdConta() == conta.getId()) {
    				funcionario.setConta(conta);
    			}
			}
    		for (Empresa empresa : respEmpresa.getObjeto()) {
    			if(funcionario.getIdEmpresa() == empresa.getId()) {
    				funcionario.setEmpresa(empresa);
    			}
			}
		}
    	
    	return respFuncionario;
        
    }
    
    public static void insert(Funcionario obj){
        
        obj.setId(new GenericDao<Funcionario>(Funcionario.class).insert(obj).getObjeto());
        
    }
    
    public static void update(Funcionario obj){
        
        new GenericDao<Funcionario>(Funcionario.class).update(obj);
        
    }
}
