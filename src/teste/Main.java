/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import br.com.jsp.bean.Conta;
import br.com.jsp.bean.Enums.NivelDeAcesso;
import br.com.jsp.dao.ContaDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import br.com.jsp.dao.FuncionarioDao;

/**
 *
 * @author 104948
 */
public class Main {
    
    public static void main(String[] args) {
        
        Conta c = new Conta();
        c.setLogin("hahaYes");
        c.setNivelDeAcesso(NivelDeAcesso.Funcionario.ordinal());
        c.setSenha("123");
        
        ContaDao.insert(c);
        
    }
    
}
