/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean.response;

/**
 *
 * @author 103782
 */
public class Resposta<T> {

    private T objeto;

    private Boolean funcionou;

    private String mensagem;

    public Resposta(String mensagem, T objeto, boolean funcionou) {

        this.objeto = objeto;
        this.funcionou = funcionou;
        this.mensagem = mensagem;

    }
    
    public Resposta(String mensagem){
        this(mensagem, null, false);
    }

    public Resposta() {
    }

    /**
     * @return the objeto
     */
    public T getObjeto() {
        return objeto;
    }

    /**
     * @return the funcionou
     */
    public boolean getFuncionou() {
        return funcionou;
    }

    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * @param objeto the objeto to set
     */
    public void setObjeto(T objeto) {

        this.objeto = objeto;

    }

    /**
     * @param funcionou the funcionou to set
     */
    public void setFuncionou(Boolean funcionou) {

        this.funcionou = funcionou;

    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(String mensagem) {

        this.mensagem = mensagem;

    }

}
