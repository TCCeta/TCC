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
    /**
     * Contrutor para quanto a resposta deve ser verdadeira
     * @param mensagem A mensagem que pode ser acessada e mostrada se necessário
     * @param objeto O Objeto que pode ser acessado depois
     */
    public Resposta(String mensagem,T objeto) {
    	this(mensagem, objeto, true);
    }
    
    /**
     * Contrutor para quando a resposta deve ser falsa
     * @param mensagem A mensagem de erro que pode ser acessada e mostrada se necessário
     */
    public Resposta(String mensagem){
        this(mensagem, null, false);
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

}
