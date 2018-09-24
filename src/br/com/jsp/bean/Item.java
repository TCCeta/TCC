/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.dao.ItemDao;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;

/**
 *
 * @author 103782
 */
@Tabela(nome = "itens")
public class Item {

	@Deprecated
	public Item() {}
	
	public Item(LocalDate dataPerdido, Empresa empresa, Imagem imagem) {
		
		this.setDataPerdido(dataPerdido);
		
	}
	
	public boolean equals(Item item) {
		
		if(id == item.id && item.devolvido == devolvido  && item.dataPerdido.equals(dataPerdido)
				&& item.idEmpresa == idEmpresa && item.idImagem == idImagem && item.nome.equals(nome)) {
			return true;
		}else {
			return false;
		}
	}
	
	public String pString(){
		
		String s = "";
		
		s += "\nid = " + id;
		s += "\nnome = " + nome;
		s += "\ndataPerdido = " + dataPerdido;
		s += "\ndataDevolvido = " + dataDevolvido;
		s += "\ndataDevolvido = " + dataDevolvido;
		s += "\nidEmpresa = " + idEmpresa;
		s += "\nidImagem = " + idImagem;
		
		return s;
		
	}
	
	@Coluna(nome = "cod_idItem", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
	private int id;
	
	@Coluna(nome = "dad_nomeItem", tipo = Types.VARCHAR)
	private String nome;

	@Coluna(nome = "dat_dataPerdidoItem", tipo = Types.DATE)
	private Date dataPerdido;

	@Coluna(nome = "dat_dataDevolvidoItem", tipo = Types.DATE)
	private Date dataDevolvido;

	@Coluna(nome = "dad_devolvidoItem", tipo = Types.BOOLEAN)
	private boolean devolvido;

	@Coluna(nome = "cod_idEmpresa", tipo = Types.INTEGER)
	private int idEmpresa;
	private Empresa empresa;

	@Coluna(nome = "cod_idImagem", tipo = Types.INTEGER)
	private int idImagem;
	private Imagem imagem;

	
	public static void cadastrar(Item item) {
		
		Imagem.cadastrar(item.imagem);
		item.idImagem = item.imagem.getId();
		
		ItemDao.insert(item);
		
	}
	
	public static void atualizar(Item item) {
		
		Imagem.atualizar(item.imagem);
		
		ItemDao.update(item);
		
	}
	
	
	
	
	
    //SETTERS --------------------------------------
    
	/**
	 * @param devolvido the devolvido to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setDevolvido(boolean devolvido) {
		this.devolvido = devolvido;
	}
	
	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public void setDataPerdido(LocalDate data) {
		this.dataPerdido = Date.valueOf(data);
	}
    
    //GETTERS --------------------------------------
   
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return the dataDevolvido
	 */
	public Date getDataDevolvido() {
		return dataDevolvido;
	}

	/**
	 * @return the devolvido
	 */
	public boolean getDevolvido() {
		return devolvido;
	}

	public Empresa getEmpresa() {
		return empresa;
	}
	
	public int getIdEmpresa() {
		return idEmpresa;
	}
	
	public Imagem getImagem() {
		return imagem;
	}
	
	public int getIdImagem() {
		return idImagem;
	}
	
	public LocalDate getDataPerdido() {
		return dataPerdido.toLocalDate();
	}

}
