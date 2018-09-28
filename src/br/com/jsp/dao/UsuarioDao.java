/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Conta;
import br.com.jsp.bean.Local;
import br.com.jsp.bean.Pessoa;
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
        
    	Resposta<ArrayList<Usuario>> respUsuario = new GenericDao<Usuario>(Usuario.class).selectAll();
    	if(!respUsuario.getFuncionou()) {
    		return new Resposta<>("Erro : " + respUsuario.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>("Erro : " + respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Local>> respLocal = LocalDao.selectAll();
    	if(!respLocal.getFuncionou()) {
    		return new Resposta<>("Erro : " + respLocal.getMensagem());
    	}
    	
    	Resposta<ArrayList<Pessoa>> respPessoa = PessoaDao.selectAll();
    	if(!respPessoa.getFuncionou()) {
    		return new Resposta<>("Erro : " + respPessoa.getMensagem());
    	}
    	
    	for (Usuario usuario : respUsuario.getObjeto()) {
    		for (Conta conta : respConta.getObjeto()) {
    			if(usuario.getIdConta() == conta.getId()) {
    				usuario.setConta(conta);
    				break;
    			}
			}
    		for (Local local : respLocal.getObjeto()) {
    			if(usuario.getIdLocal() == local.getId()) {
    				usuario.setLocal(local);
    				break;
    			}
			}
    		for (Pessoa pessoa : respPessoa.getObjeto()) {
    			if(usuario.getIdPessoa() == pessoa.getId()) {
    				usuario.setPessoa(pessoa);
    				break;
    			}
			}
		}
        return respUsuario;
    }
    
    public static Resposta<ArrayList<Usuario>> selectAll(String campo, Order order){
        
    	Resposta<ArrayList<Usuario>> respUsuario = new GenericDao<Usuario>(Usuario.class).selectAll(campo, order);
    	if(!respUsuario.getFuncionou()) {
    		return new Resposta<>("Erro : " + respUsuario.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>("Erro : " + respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Local>> respLocal = LocalDao.selectAll();
    	if(!respLocal.getFuncionou()) {
    		return new Resposta<>("Erro : " + respLocal.getMensagem());
    	}
    	
    	Resposta<ArrayList<Pessoa>> respPessoa = PessoaDao.selectAll();
    	if(!respPessoa.getFuncionou()) {
    		return new Resposta<>("Erro : " + respPessoa.getMensagem());
    	}
    	
    	for (Usuario usuario : respUsuario.getObjeto()) {
    		for (Conta conta : respConta.getObjeto()) {
    			if(usuario.getIdConta() == conta.getId()) {
    				usuario.setConta(conta);
    				break;
    			}
			}
    		for (Local local : respLocal.getObjeto()) {
    			if(usuario.getIdLocal() == local.getId()) {
    				usuario.setLocal(local);
    				break;
    			}
			}
    		for (Pessoa pessoa : respPessoa.getObjeto()) {
    			if(usuario.getIdPessoa() == pessoa.getId()) {
    				usuario.setPessoa(pessoa);
    				break;
    			}
			}
		}
        return respUsuario;
        
    }
    
    public static Resposta<ArrayList<Usuario>> selectWhere(String campo, Where comparacao, Object valor){
        
    	Resposta<ArrayList<Usuario>> respUsuario = new GenericDao<Usuario>(Usuario.class).selectWhere(campo, comparacao, valor);
    	if(!respUsuario.getFuncionou()) {
    		return new Resposta<>("Erro : " + respUsuario.getMensagem());
    	}
    	
    	Resposta<ArrayList<Conta>> respConta = ContaDao.selectAll();
    	if(!respConta.getFuncionou()) {
    		return new Resposta<>("Erro : " + respConta.getMensagem());
    	}
    	
    	Resposta<ArrayList<Local>> respLocal = LocalDao.selectAll();
    	if(!respLocal.getFuncionou()) {
    		return new Resposta<>("Erro : " + respLocal.getMensagem());
    	}
    	
    	Resposta<ArrayList<Pessoa>> respPessoa = PessoaDao.selectAll();
    	if(!respPessoa.getFuncionou()) {
    		return new Resposta<>("Erro : " + respPessoa.getMensagem());
    	}
    	
    	for (Usuario usuario : respUsuario.getObjeto()) {
    		for (Conta conta : respConta.getObjeto()) {
    			if(usuario.getIdConta() == conta.getId()) {
    				usuario.setConta(conta);
    				break;
    			}
			}
    		for (Local local : respLocal.getObjeto()) {
    			if(usuario.getIdLocal() == local.getId()) {
    				usuario.setLocal(local);
    				break;
    			}
			}
    		for (Pessoa pessoa : respPessoa.getObjeto()) {
    			if(usuario.getIdPessoa() == pessoa.getId()) {
    				usuario.setPessoa(pessoa);
    				break;
    			}
			}
		}
        return respUsuario;
        
    }
    
    public static Resposta<Integer> insert(Usuario obj){
        
    	Resposta<Integer> resp = new GenericDao<Usuario>(Usuario.class).insert(obj);
    	if(resp.getFuncionou()) {obj.setId(resp.getObjeto());}
    	return resp;
    	
        
    }
    
//    public static void insert(ArrayList<Usuario> lista){
//        
//        new GenericDao<Usuario>(Usuario.class).insert(lista);
//        
//    }
    
    public static Resposta<Boolean> update(Usuario obj){
        
        return new GenericDao<Usuario>(Usuario.class).update(obj);
        
    }
    
//    public static void update(ArrayList<Usuario> lista){
//        
//        new GenericDao<Usuario>(Usuario.class).update(lista);
//        
//    }
    
}
