/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.bean.response.Resposta;
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
	
	public Item(LocalDate dataPerdido, Funcionario funcionario, Imagem imagem, String nome, String descricao) {
		
		this.setDataPerdido(dataPerdido);
		this.funcionario = funcionario;
		this.idFuncionario = funcionario.getId();
		this.nome = nome;
		this.descricao = descricao;
		
		this.imagem = imagem;
		
	}
	
	@Coluna(nome = "cod_idItem", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
	private int id;

	
	@Coluna(nome = "TODO BOTAR NOME", tipo = Types.VARCHAR)
	private String nome;
	
	
	@Coluna(nome = "TODO BOTAR NOME", tipo = Types.VARCHAR)
	private String descricao;
	
	@Coluna(nome = "dat_dataPerdidoItem", tipo = Types.DATE)
	private Date dataPerdido;

	@Coluna(nome = "dat_dataDevolvidoItem", tipo = Types.DATE)
	private Date dataDevolvido;

	@Coluna(nome = "dad_devolvidoItem", tipo = Types.BOOLEAN)
	private boolean devolvido;

	@Coluna(nome = "cod_idFuncionario", tipo = Types.INTEGER)
	private int idFuncionario;
	private Funcionario funcionario;

	
	@Coluna(nome = "cod_idImagem", tipo = Types.INTEGER)
	private int idImagem;
	private Imagem imagem;

	
	public static Resposta<Integer> cadastrar(Item item) {
		
		//Imagem.cadastrar(item.imagem);
		//item.idImagem = item.imagem.getId();
		
		item.idImagem = 1;
		
		return ItemDao.insert(item);
		
	}
	
	public Resposta<Integer> cadastrar() {
		return cadastrar(this);
	}
	
	public static void atualizar(Item item) {
		
		Imagem.atualizar(item.imagem);
		
		ItemDao.update(item);
		
	}
	
	
	
	
	
    //SETTERS --------------------------------------
    
	/**
	 * @param devolvido the devolvido to set
	 */
	public void setDevolvido(boolean devolvido) {
		this.devolvido = devolvido;
	}
	
	public void setImagem(Imagem imagem) {
		this.imagem = imagem;
	}
	
	public void setFuncionario(Funcionario funcionario) {
		
		this.funcionario = funcionario;
		this.idFuncionario = funcionario.getId();
		
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

	public Funcionario getFuncionario() {
		
		return this.funcionario;
		
	}
	
	public int getIdFuncionario() {
		return this.idFuncionario;
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
