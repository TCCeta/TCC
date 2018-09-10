/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;

/**
 *
 * @author 103782
 */
@Tabela(nome = "itens")
public class Item {

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param dataPerdido the dataPerdido to set
     */
    public void setDataPerdido(Date dataPerdido) {
        this.dataPerdido = dataPerdido;
    }

    /**
     * @return the dataDevolvido
     */
    public Date getDataDevolvido() {
        return dataDevolvido;
    }

    /**
     * @param dataDevolvido the dataDevolvido to set
     */
    public void setDataDevolvido(Date dataDevolvido) {
        this.dataDevolvido = dataDevolvido;
    }

    /**
     * @return the devolvido
     */
    public boolean isDevolvido() {
        return devolvido;
    }

    /**
     * @param devolvido the devolvido to set
     */
    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    /**
     * @return the idEmpresa
     */
    public int getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * @param idEmpresa the idEmpresa to set
     */
    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    /**
     * @return the idImagem
     */
    public int getIdImagem() {
        return idImagem;
    }

    /**
     * @param idImagem the idImagem to set
     */
    public void setIdImagem(int idImagem) {
        this.idImagem = idImagem;
    }
    
    @Coluna(nome = "cod_idItem", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "dat_dataPerdidoItem", tipo = Types.DATE)
    private Date dataPerdido;
    
    @Coluna(nome = "dat_dataDevolvidoItem", tipo = Types.DATE)
    private Date dataDevolvido;
    
    @Coluna(nome = "dad_devolvidoItem", tipo = Types.BOOLEAN)
    private boolean devolvido;
    
    @Coluna(nome = "cod_idEmpresa", tipo = Types.INTEGER)
    private int idEmpresa;
    
    @Coluna(nome = "cod_idImagem", tipo = Types.INTEGER)
    private int idImagem;
    
    
    
    
    
    
    public void setDataPerdido(LocalDate data){
        setDataPerdido(Date.valueOf(data));
    }
    
    public LocalDate getDataPerdido(){
        return dataPerdido.toLocalDate();
    }
    
}
