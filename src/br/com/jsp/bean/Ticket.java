 package br.com.jsp.bean;

import java.sql.Date;
import java.sql.Types;
import java.util.ArrayList;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.TicketDao;

@Tabela(nome = "ticket")
public class Ticket {

	public int getIdTicket() {
		return idTicket;
	}

	public void setIdTicket(int idTicket) {
		this.idTicket = idTicket;
	}

	public int getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(int idPessoa) {
		this.idPessoa = idPessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataPerdido() {
		return dataPerdido;
	}

	public void setDataPerdido(Date dataPerdido) {
		this.dataPerdido = dataPerdido;
	}

	public int getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getIdImagem() {
		return idImagem;
	}

	public void setIdImagem(int idImagem) {
		this.idImagem = idImagem;
	}

	@Coluna(nome = "cod_idTicket", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
	private int idTicket;

	@Coluna(nome = "cod_idPessoa", tipo = Types.INTEGER)
	private int idPessoa;

	@Coluna(nome = "dad_nomeItem", tipo = Types.VARCHAR)
	private String nome;

	@Coluna(nome = "dat_dataPerdidoItem", tipo = Types.DATE)
	private Date dataPerdido;

	@Coluna(nome = "cod_idEmpresa", tipo = Types.INTEGER)
	private int idEmpresa;

	@Coluna(nome = "dad_descricao", tipo = Types.VARCHAR)
	private String descricao;

	@Coluna(nome = "cod_idImagem", tipo = Types.INTEGER)
	private int idImagem;

	public static void main(String[] args) {
		Resposta<ArrayList<Ticket>> lista = TicketDao.selectAll();

		for (Ticket t : lista.getObjeto()) {

			System.out.println(" idTicket = " + t.getIdTicket());
			System.out.println(" idPessoa = " + t.getIdPessoa());
			System.out.println(" nomeItem = " + t.getNome());
			System.out.println(" dataPerdido = " + t.getDataPerdido());
			System.out.println(" idEmpresa = " + t.getIdEmpresa());
			System.out.println(" idImagem = " + t.getIdImagem() + "\n");

		}

	}

}
