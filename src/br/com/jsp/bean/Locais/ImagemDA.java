package br.com.jsp.bean.Locais;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.bind.DatatypeConverter;
public class ImagemDA {
	public static Blob imagemToBlob(String caminho){
		try{ 
			File file = new File(caminho);
			if(file.exists()){
				BufferedImage img = ImageIO.read( file );
				ByteArrayOutputStream b = new ByteArrayOutputStream();          
				ImageIO.write( img, "jpg", b );
				byte[] imgArray = b.toByteArray();
				return new SerialBlob(imgArray);
			}
		}
		catch (Exception e) {System.out.println("Falha ao fazer upload: " + e);
		}
		return null;
	}
	
	
	public String blobPara64(Blob imagem) {
		
		byte[] imgArray = null;
		try {
			imgArray = imagem.getBytes(1, (int)imagem.length());
			imagem.free();
		} catch (SQLException e) {}
        String photo64 = DatatypeConverter.printBase64Binary(imgArray);
        
        return photo64;
	}
}