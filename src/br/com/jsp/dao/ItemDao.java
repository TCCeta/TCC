/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Item;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
public class ItemDao {
    
    public static Resposta<ArrayList<Item>> selectAll(){
        
        return new GenericDao<Item>(Item.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Item>> selectAll(String campo, Order order){
        
        return new GenericDao<Item>(Item.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Item>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Item>(Item.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static void insert(Item obj){
        
        new GenericDao<Item>(Item.class).insert(obj);
        
    }
    
    public static void insert(ArrayList<Item> lista){
        
        new GenericDao<Item>(Item.class).insert(lista);
        
    }
    
    public static void update(Item obj){
        
        new GenericDao<Item>(Item.class).update(obj);
        
    }
    
    public static void update(ArrayList<Item> lista){
        
        new GenericDao<Item>(Item.class).update(lista);
        
    }
    
}
