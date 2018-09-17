/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.dao.ImagemDao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Types;

import javax.imageio.ImageIO;

/**
 *
 * @author 103782
 */
@Tabela(nome = "imagens")
public class Imagem {

	@Deprecated
	public Imagem() {}
	
	@Coluna(nome = "cod_idImagem", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "img_caminhoImagem", tipo = Types.VARCHAR)
    private String caminho;
    
    @Coluna(nome = "img_imagem", tipo = Types.BLOB)
    private Blob imagem;
	
    public static void cadastrar(Imagem imagem) {
    	ImagemDao.insert(imagem);
    }
    
    public static void atualizar(Imagem imagem) {
    	ImagemDao.update(imagem);
    }
    
    //SETTERS --------------------------------------
    
    /**
     * @param caminho the caminho to set
     */
    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
    
    //GETTERS --------------------------------------
   
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the caminho
     */
    public String getCaminho() {
        return caminho;
    }
    
    public byte[] getByteArray()
    {
    	try {
			return imagem.getBytes(1, (int) imagem.length());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
    	return null;
    }
    
    
    public BufferedImage getImagem() {
    	
    	byte[] array = null;
    	
    	BufferedImage retorno = null;
    	
    	try {
    		array = imagem.getBytes(1, (int) imagem.length());
    		
    		ByteArrayInputStream bis =new ByteArrayInputStream(array);
    		
    		retorno = ImageIO.read(bis);
    		
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
    	
    	return null;
    	
    }
    
}
