/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Empresa;
import br.com.jsp.bean.Funcionario;
import br.com.jsp.bean.Imagem;
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

	public static Resposta<ArrayList<Item>> selectAll() {

		Resposta<ArrayList<Item>> respItem = new GenericDao<Item>(Item.class).selectAll();

		if (!respItem.getFuncionou()) {
			return new Resposta<>("Erro : " + respItem.getMensagem());
		}

		Resposta<ArrayList<Funcionario>> respFuncionario = new GenericDao<Funcionario>(Funcionario.class).selectAll();

		if (!respFuncionario.getFuncionou()) {
			return new Resposta<>("Erro : " + respFuncionario.getMensagem());
		}

		Resposta<ArrayList<Imagem>> respImg = new GenericDao<Imagem>(Imagem.class).selectAll();

		if (!respImg.getFuncionou()) {
			return new Resposta<>("Erro : " + respImg.getMensagem());
		}

		for (Item item : respItem.getObjeto()) {
			for (Funcionario funcionario : respFuncionario.getObjeto()) {
				if(funcionario.getId() == item.getIdFuncionario()) {
					item.setFuncionario(funcionario);
					break;
				}
			}
			
			for (Imagem imagem : respImg.getObjeto()) {
				if(imagem.getId() == item.getIdImagem()) {
					item.setImagem(imagem);
					break;
				}
			}
		}
		
		return respItem;

	}

	public static Resposta<ArrayList<Item>> selectAll(String campo, Order order) {

		Resposta<ArrayList<Item>> respItem = new GenericDao<Item>(Item.class).selectAll(campo, order);

		if (!respItem.getFuncionou()) {
			return new Resposta<>("Erro : " + respItem.getMensagem());
		}

		Resposta<ArrayList<Funcionario>> respFuncionario = new GenericDao<Funcionario>(Funcionario.class).selectAll();

		if (!respFuncionario.getFuncionou()) {
			return new Resposta<>("Erro : " + respFuncionario.getMensagem());
		}

		Resposta<ArrayList<Imagem>> respImg = new GenericDao<Imagem>(Imagem.class).selectAll();

		if (!respImg.getFuncionou()) {
			return new Resposta<>("Erro : " + respImg.getMensagem());
		}

		for (Item item : respItem.getObjeto()) {
			for (Funcionario funcionario : respFuncionario.getObjeto()) {
				if(funcionario.getId() == item.getIdFuncionario()) {
					item.setFuncionario(funcionario);
					break;
				}
			}
			
			for (Imagem imagem : respImg.getObjeto()) {
				if(imagem.getId() == item.getIdImagem()) {
					item.setImagem(imagem);
					break;
				}
			}
		}
		
		return respItem;

	}

	public static Resposta<ArrayList<Item>> selectWhere(String campo, Where comparacao, Object valor) {

		Resposta<ArrayList<Item>> respItem = new GenericDao<Item>(Item.class).selectWhere(campo, comparacao, valor);

		if (!respItem.getFuncionou()) {
			return new Resposta<>("Erro : " + respItem.getMensagem());
		}

		//Resposta<ArrayList<Funcionario>> respFuncionario = new GenericDao<Funcionario>(Funcionario.class).selectAll();

		Resposta<ArrayList<Funcionario>> respFuncionario = FuncionarioDao.selectAll();
		
		if (!respFuncionario.getFuncionou()) {
			return new Resposta<>("Erro : " + respFuncionario.getMensagem());
		}

		Resposta<ArrayList<Imagem>> respImg = new GenericDao<Imagem>(Imagem.class).selectAll();

		if (!respImg.getFuncionou()) {
			return new Resposta<>("Erro : " + respImg.getMensagem());
		}

		for (Item item : respItem.getObjeto()) {
			for (Funcionario funcionario : respFuncionario.getObjeto()) {
				if(funcionario.getId() == item.getIdFuncionario()) {
					item.setFuncionario(funcionario);
					break;
				}
			}
			
			for (Imagem imagem : respImg.getObjeto()) {
				if(imagem.getId() == item.getIdImagem()) {
					item.setImagem(imagem);
					break;
				}
			}
		}
		
		return respItem;
		
	}

	public static Resposta<Integer> insert(Item obj) {

		return new GenericDao<Item>(Item.class).insert(obj);

	}

	public static Resposta<Boolean> update(Item obj) {

		return new GenericDao<Item>(Item.class).update(obj);

	}

}
