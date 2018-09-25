package br.com.jsp.bean.Locais;
public class BairroBean {
	// Atributos
	private int idBairro;
	private String nomeBairro;
	private int idCidade;
	//Getters
	public int getIdBairro() {return idBairro;}
	public String getBairro() {return nomeBairro;}
	public int getIdCidade() {return idCidade;}
	//Setters
	public void setIdBairro(int idBairro) {this.idBairro = idBairro;}
	public void setBairro(String bairro) {nomeBairro = bairro;}
	public void setIdCidade(int idCidade) {this.idCidade = idCidade;}
	//Construtor
	public BairroBean(int idBairro, String Bairro, int idCidade){
		this.idBairro = idBairro;
		this.nomeBairro = Bairro;
		this.idCidade = idCidade;
	}
}
