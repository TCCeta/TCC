/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Conta;
import br.com.jsp.bean.Empresa;
import br.com.jsp.bean.Local;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
public class EmpresaDao {
    
    public static Resposta<ArrayList<Empresa>> selectAll(){
        
    	Resposta<ArrayList<Empresa>> respEmpresa = new GenericDao<>(Empresa.class).selectAll();
    	
    	if(!respEmpresa.getFuncionou()) {
    		return new Resposta<>(respEmpresa.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>(respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Local>> respLocal = LocalDao.selectAll();
    	
    	if(!respLocal.getFuncionou()) {
    		return new Resposta<>(respLocal.getMensagem());
    	}
    	
    	for (Empresa empresa : respEmpresa.getObjeto()) {
			
    		for (Conta conta : respConta.getObjeto()) {
				
    			if(conta.getId() == empresa.getIdConta()) {
    				empresa.setConta(conta);
    				break;
    			}
    			
			}
    		
    		for (Local local : respLocal.getObjeto()) {
				
    			if(local.getId() == empresa.getIdLocal()) {
    				empresa.setLocal(local);
    				break;
    			}
    			
			}
    		
		}
    	
        return respEmpresa;
        
    }
    
    public static Resposta<ArrayList<Empresa>> selectAll(String campo, Order order){
        
        Resposta<ArrayList<Empresa>> respEmpresa = new GenericDao<>(Empresa.class).selectAll(campo, order);
    	
    	if(!respEmpresa.getFuncionou()) {
    		return new Resposta<>(respEmpresa.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>(respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Local>> respLocal = LocalDao.selectAll();
    	
    	if(!respLocal.getFuncionou()) {
    		return new Resposta<>(respLocal.getMensagem());
    	}
    	
    	for (Empresa empresa : respEmpresa.getObjeto()) {
			
    		for (Conta conta : respConta.getObjeto()) {
				
    			if(conta.getId() == empresa.getIdConta()) {
    				empresa.setConta(conta);
    				break;
    			}
    			
			}
    		
    		for (Local local : respLocal.getObjeto()) {
				
    			if(local.getId() == empresa.getIdLocal()) {
    				empresa.setLocal(local);
    				break;
    			}
    			
			}
    		
		}
    	
        return respEmpresa;
        
    }
    
    public static Resposta<ArrayList<Empresa>> selectWhere(String campo, Where comparacao, Object valor){
        
        Resposta<ArrayList<Empresa>> respEmpresa = new GenericDao<>(Empresa.class).selectWhere(campo, comparacao, valor);
    	
    	if(!respEmpresa.getFuncionou()) {
    		return new Resposta<>(respEmpresa.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>(respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Local>> respLocal = LocalDao.selectAll();
    	
    	if(!respLocal.getFuncionou()) {
    		return new Resposta<>(respLocal.getMensagem());
    	}
    	
    	for (Empresa empresa : respEmpresa.getObjeto()) {
			
    		for (Conta conta : respConta.getObjeto()) {
				
    			if(conta.getId() == empresa.getIdConta()) {
    				empresa.setConta(conta);
    				break;
    			}
    			
			}
    		
    		for (Local local : respLocal.getObjeto()) {
				
    			if(local.getId() == empresa.getIdLocal()) {
    				empresa.setLocal(local);
    				break;
    			}
    			
			}
    		
		}
    	
        return respEmpresa;
        
    }
    
    public static void insert(Empresa obj){
        
    	System.out.println(new GenericDao<Empresa>(Empresa.class).insert(obj).getMensagem());
    	
        obj.setId(new GenericDao<Empresa>(Empresa.class).insert(obj).getObjeto());
        
    }
    
    public static void insert(ArrayList<Empresa> lista){
        
        new GenericDao<Empresa>(Empresa.class).insert(lista);
        
    }
    
    public static void update(Empresa obj){
        
        new GenericDao<Empresa>(Empresa.class).update(obj);
        
    }
    
    public static void update(ArrayList<Empresa> lista){
        
        new GenericDao<Empresa>(Empresa.class).update(lista);
        
    }
    
    
}
