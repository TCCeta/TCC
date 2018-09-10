/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Empresa;
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
        
        return new GenericDao<Empresa>(Empresa.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Empresa>> selectAll(String campo, Order order){
        
        return new GenericDao<Empresa>(Empresa.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Empresa>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Empresa>(Empresa.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static void insert(Empresa obj){
        
        new GenericDao<Empresa>(Empresa.class).insert(obj);
        
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
