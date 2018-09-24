package br.com.jsp.dao;

import br.com.jsp.bean.Ticket;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.CriadorDeComandosSQL.GenericDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Order;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import java.util.ArrayList;

public class TicketDao {

	public static Resposta<ArrayList<Ticket>> selectAll() {
		return new GenericDao<Ticket>(Ticket.class).selectAll();
	}
	
    public static Resposta<ArrayList<Ticket>> selectAll(String campo, Order order){
        
        return new GenericDao<Ticket>(Ticket.class).selectAll(campo, order);
        
    }
    
    public static Resposta<ArrayList<Ticket>> selectWhere(String campo, Where comparacao, Object valor){
        
        return new GenericDao<Ticket>(Ticket.class).selectWhere(campo, comparacao, valor);
        
    }
    
    public static void insert(Ticket obj){
        
        new GenericDao<Ticket>(Ticket.class).insert(obj);
        
    }
    
    public static void insert(ArrayList<Ticket> lista){
        
        new GenericDao<Ticket>(Ticket.class).insert(lista);
        
    }
    
    public static void update(Ticket obj){
        
        new GenericDao<Ticket>(Ticket.class).update(obj);
        
    }
    
    public static void update(ArrayList<Ticket> lista){
        
        new GenericDao<Ticket>(Ticket.class).update(lista);
        
    }
    
}
