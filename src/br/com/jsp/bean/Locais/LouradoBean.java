package br.com.jsp.bean.Locais;
public class LouradoBean {
	//Atributos
	private int cod_idLourado;
	private String dad_nomeLourado;
	private int cod_idBairro;
	// Getters
	public int getCod_idLourado() {return cod_idLourado;}
	public String getDad_nomeLourado() {return dad_nomeLourado;}
	public void setCod_idLourado(int cod_idLourado) {this.cod_idLourado = cod_idLourado;}
	//Setters
	public int getCod_idBairro() {return cod_idBairro;}
	public void setDad_nomeLourado(String dad_nomeLourado) {this.dad_nomeLourado = dad_nomeLourado;}
	public void setCod_idBairro(int cod_idBairro) {this.cod_idBairro = cod_idBairro;}
	//Construtor
	public LouradoBean(int idLourado, String lourado, int idBairro){
		this.cod_idLourado = idLourado;
		this.dad_nomeLourado = lourado;
		this.cod_idBairro = idBairro;
	}
}
