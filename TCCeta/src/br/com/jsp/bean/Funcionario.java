/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import java.sql.Types;

/**
 *
 * @author 103782
 */
@Tabela(nome = "funcionarios")
public class Funcionario {

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the idEmpresa
     */
    public int getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * @return the idPessoa
     */
   

    /**
     * @return the idConta
     */
    public int getIdConta() {
        return idConta;
    }
    
    private String getCpf() {
		return cpf;
	}

	private void setCFP(String cpf) {
		this.cpf = cpf;
	}

	@Coluna(nome = "cod_idFuncionario", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
    private int id;
    
    @Coluna(nome = "cod_idEmpresa", tipo = Types.INTEGER)
    private int idEmpresa;
    
    @Coluna(nome = "dad_CPF", tipo = Types.VARCHAR)
    private String cpf;
    
    @Coluna(nome = "cod_idConta", tipo = Types.INTEGER)
    private int idConta;
    
}
