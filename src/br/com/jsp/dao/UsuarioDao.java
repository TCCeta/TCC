/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Usuario;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
public class UsuarioDao {
    
    public static Resposta<ArrayList<Usuario>> selectAll(){
        
        return new GenericDao<Usuario>(Usuario.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Usuario>> selectAll(String campo, Order order){
        
        return new GenericDao<Usuario>(Usuario.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Usuario>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Usuario>(Usuario.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static void insert(Usuario obj){
        
        new GenericDao<Usuario>(Usuario.class).insert(obj);
        
    }
    
    public static void insert(ArrayList<Usuario> lista){
        
        new GenericDao<Usuario>(Usuario.class).insert(lista);
        
    }
    
    public static void update(Usuario obj){
        
        new GenericDao<Usuario>(Usuario.class).update(obj);
        
    }
    
    public static void update(ArrayList<Usuario> lista){
        
        new GenericDao<Usuario>(Usuario.class).update(lista);
        
    }
    
}
