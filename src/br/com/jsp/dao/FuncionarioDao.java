/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

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
        
        return new GenericDao<Funcionario>(Funcionario.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Funcionario>> selectAll(String campo, Order order){
        
        return new GenericDao<Funcionario>(Funcionario.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Funcionario>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Funcionario>(Funcionario.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static void insert(Funcionario obj){
        
        new GenericDao<Funcionario>(Funcionario.class).insert(obj);
        
    }
    
    public static void insert(ArrayList<Funcionario> lista){
        
        new GenericDao<Funcionario>(Funcionario.class).insert(lista);
        
    }
    
    public static void update(Funcionario obj){
        
        new GenericDao<Funcionario>(Funcionario.class).update(obj);
        
    }
    
    public static void update(ArrayList<Funcionario> lista){
        
        new GenericDao<Funcionario>(Funcionario.class).update(lista);
        
    }
    
}
