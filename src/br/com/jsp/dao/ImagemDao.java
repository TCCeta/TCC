/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Imagem;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
public class ImagemDao {
    
    public static Resposta<ArrayList<Imagem>> selectAll(){
        
        return new GenericDao<Imagem>(Imagem.class).selectAll();
        
    }
    
    public static Resposta<ArrayList<Imagem>> selectAll(String campo, Order order){
        
        return new GenericDao<Imagem>(Imagem.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Imagem>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Imagem>(Imagem.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static Resposta<Integer> insert(Imagem obj){
        
    	Resposta<Integer> resp = new GenericDao<Imagem>(Imagem.class).insert(obj);
    	//if(resp.getFuncionou()) {obj.setId(resp.getObjeto());}
    	return resp;
        
    }
    
    /*public static void insert(ArrayList<Imagem> lista){
        
        new GenericDao<Imagem>(Imagem.class).insert(lista);
        
    }*/
    
    public static Resposta<Boolean> update(Imagem obj){
        
        return new GenericDao<Imagem>(Imagem.class).update(obj);
        
    }
    
//    public static void update(ArrayList<Imagem> lista){
//        
//        new GenericDao<Imagem>(Imagem.class).update(lista);
//        
//    }
    
}
