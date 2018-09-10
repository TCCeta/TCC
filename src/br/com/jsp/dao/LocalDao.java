/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

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
public class LocalDao {
 
    public static Resposta<ArrayList<Local>> selectAll(){
        
        return new GenericDao<Local>(Local.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Local>> selectAll(String campo, Order order){
        
        return new GenericDao<Local>(Local.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Local>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Local>(Local.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static void insert(Local obj){
        
        new GenericDao<Local>(Local.class).insert(obj);
        
    }
    
    public static void insert(ArrayList<Local> lista){
        
        new GenericDao<Local>(Local.class).insert(lista);
        
    }
    
    public static void update(Local obj){
        
        new GenericDao<Local>(Local.class).update(obj);
        
    }
    
    public static void update(ArrayList<Local> lista){
        
        new GenericDao<Local>(Local.class).update(lista);
        
    }
    
}
