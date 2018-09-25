/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Conta;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
public class ContaDao {
    
    public static Resposta<ArrayList<Conta>> selectAll(){
        
        return new GenericDao<Conta>(Conta.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Conta>> selectAll(String campo, Order order){
        
        return new GenericDao<Conta>(Conta.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Conta>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Conta>(Conta.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static Resposta<Integer> insert(Conta obj){
        
    	Resposta<Integer> resp = new GenericDao<Conta>(Conta.class).insert(obj);
    	if(resp.getFuncionou()) {obj.setId(resp.getObjeto());}
        return resp;
        
    }
    
    public static Resposta<Boolean> update(Conta obj){
        
        return new GenericDao<Conta>(Conta.class).update(obj);
        
    }
    
}
