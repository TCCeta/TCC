/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.dao.ImagemDao;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Types;

import javax.imageio.ImageIO;

import com.sun.javafx.tk.Toolkit;

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
    
    /*public static void main(String[] args) {
		
    	Image image = ImageIO.read(new File(""));
    	BufferedImage bi = (BufferedImage) image;
    	
    	ByteArrayOutputStream baos = null;
    	try {
    	    baos = new ByteArrayOutputStream();
    	    ImageIO.write(bi, "png", baos);
    	} finally {
    	    try {
    	        baos.close();
    	    } catch (Exception e) {
    	    }
    	}
    	ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    	
    	System.out.println();
    	
    	Imagem img = new Imagem();
    	Image imagem = new
    	
	}*/
    
}
