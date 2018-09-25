package br.com.jsp.bean.Locais;
public class EstadosBean {
	//Atributos
	private int idEstado;
	private String nomeEstado;
	private String siglaEstado;
	//Getters
	public int getIdEstado() {return idEstado;}
	public String getNomeEstado() {return nomeEstado;}
	public String getSiglaEstado() {return siglaEstado;}
	//Setters
	public void setIdEstado(int idEstado) {this.idEstado = idEstado;}
	public void setNomeEstado(String nomeEstado) {this.nomeEstado = nomeEstado;}
	public void setSiglaEstado(String siglaEstado) {this.siglaEstado = siglaEstado;}
	//Construtor
	public EstadosBean(int idEstado, String nomeEstado, String siglaEstado){
		this.idEstado = idEstado;
		this.nomeEstado = nomeEstado;
		this.siglaEstado = siglaEstado;
	}
}
