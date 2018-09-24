package br.com.jsp.bean.Locais;
public class CidadeBean {
	private int idCidade;
	private String nomeCidade;
	private int idEstado;
	//Getters
	public int getIdCidade() {return idCidade;}
	public String getNomeCidade() {return nomeCidade;}
	public int getIdEstado() {return idEstado;}
	//Setters
	public void setIdCidade(int idCidade) {this.idCidade = idCidade;}
	public void setNomeCidade(String nomeCidade) {this.nomeCidade = nomeCidade;}
	public void setIdEstado(int idEstado) {this.idEstado = idEstado;}
	//Construtor
	public CidadeBean(int idCidade, String cidade, int idEstado){
		this.idCidade = idCidade;
		this.nomeCidade = cidade;
		this.idEstado = idEstado;
	}
}
