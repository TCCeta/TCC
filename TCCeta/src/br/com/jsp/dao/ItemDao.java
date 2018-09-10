/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao;

import br.com.jsp.bean.Empresa;
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

		Resposta<ArrayList<Empresa>> respEmpresa = new GenericDao<Empresa>(Empresa.class).selectAll();

		if (!respEmpresa.getFuncionou()) {
			return new Resposta<>("Erro : " + respEmpresa.getMensagem());
		}

		Resposta<ArrayList<Imagem>> respImg = new GenericDao<Imagem>(Imagem.class).selectAll();

		if (!respImg.getFuncionou()) {
			return new Resposta<>("Erro : " + respImg.getMensagem());
		}

		for (Item item : respItem.getObjeto()) {
			for (Empresa empresa : respEmpresa.getObjeto()) {
				if(empresa.getId() == item.getIdEmpresa()) {
					item.setEmpresa(empresa);
					break;
				}
			}
			
			for (Imagem imagem : respImg.getObjeto()) {
				if(imagem.getId() == item.getIdEmpresa()) {
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

		Resposta<ArrayList<Empresa>> respEmpresa = new GenericDao<Empresa>(Empresa.class).selectAll();

		if (!respEmpresa.getFuncionou()) {
			return new Resposta<>("Erro : " + respEmpresa.getMensagem());
		}

		Resposta<ArrayList<Imagem>> respImg = new GenericDao<Imagem>(Imagem.class).selectAll();

		if (!respImg.getFuncionou()) {
			return new Resposta<>("Erro : " + respImg.getMensagem());
		}

		for (Item item : respItem.getObjeto()) {
			for (Empresa empresa : respEmpresa.getObjeto()) {
				if(empresa.getId() == item.getIdEmpresa()) {
					item.setEmpresa(empresa);
					break;
				}
			}
			
			for (Imagem imagem : respImg.getObjeto()) {
				if(imagem.getId() == item.getIdEmpresa()) {
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

		Resposta<ArrayList<Empresa>> respEmpresa = new GenericDao<Empresa>(Empresa.class).selectAll();

		if (!respEmpresa.getFuncionou()) {
			return new Resposta<>("Erro : " + respEmpresa.getMensagem());
		}

		Resposta<ArrayList<Imagem>> respImg = new GenericDao<Imagem>(Imagem.class).selectAll();

		if (!respImg.getFuncionou()) {
			return new Resposta<>("Erro : " + respImg.getMensagem());
		}

		for (Item item : respItem.getObjeto()) {
			for (Empresa empresa : respEmpresa.getObjeto()) {
				if(empresa.getId() == item.getIdEmpresa()) {
					item.setEmpresa(empresa);
					break;
				}
			}
			
			for (Imagem imagem : respImg.getObjeto()) {
				if(imagem.getId() == item.getIdEmpresa()) {
					item.setImagem(imagem);
					break;
				}
			}
		}
		
		return respItem;
		
	}

	public static void insert(Item obj) {

		new GenericDao<Item>(Item.class).insert(obj);

	}

	public static void insert(ArrayList<Item> lista) {

		new GenericDao<Item>(Item.class).insert(lista);

	}

	public static void update(Item obj) {

		new GenericDao<Item>(Item.class).update(obj);

	}

	public static void update(ArrayList<Item> lista) {

		new GenericDao<Item>(Item.class).update(lista);

	}

}
