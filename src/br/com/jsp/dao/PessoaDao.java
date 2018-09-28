/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Pessoa;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
public class PessoaDao {
    
    public static Resposta<ArrayList<Pessoa>> selectAll(){
        
        return new GenericDao<Pessoa>(Pessoa.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Pessoa>> selectAll(String campo, Order order){
        
        return new GenericDao<Pessoa>(Pessoa.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Pessoa>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Pessoa>(Pessoa.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static Resposta<Integer> insert(Pessoa obj){
    	
        Resposta<Integer> resp = new GenericDao<Pessoa>(Pessoa.class).insert(obj);
        if(resp.getFuncionou()) {obj.setId(resp.getObjeto());}
        return resp;
        
    }
    
//    public static void insert(ArrayList<Pessoa> lista){
//        
//        new GenericDao<Pessoa>(Pessoa.class).insert(lista);
//        
//    }
    
    public static Resposta<Boolean> update(Pessoa obj){
        
        return new GenericDao<Pessoa>(Pessoa.class).update(obj);
        
    }
    
//    public static void update(ArrayList<Pessoa> lista){
//        
//        new GenericDao<Pessoa>(Pessoa.class).update(lista);
//        
//    }
//    
}
