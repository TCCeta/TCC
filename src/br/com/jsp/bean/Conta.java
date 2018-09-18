/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.bean;

import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.bean.Enums.NivelDeAcesso;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.dao.ContaDao;
import br.com.jsp.dao.CriadorDeComandosSQL.Where;
import br.com.jsp.encripcao.PasswordUtils;
import java.sql.Types;
import java.util.ArrayList;

/**
 *
 * @author 103782
 */
@Tabela(nome = "contas")
public class Conta {

	@Deprecated
	public Conta() {
	}

	public Conta(String login, String senha, NivelDeAcesso nivel) {
		
		this.login = login;
		setSenha(senha);
		nivelDeAcesso = nivel.ordinal();

	}

	
	@Coluna(nome = "cod_idConta", tipo = Types.INTEGER, autoGerado = true, primaryKey = true)
	private Integer id;

	@Coluna(nome = "dad_loginConta", tipo = Types.VARCHAR)
	private String login;

	@Coluna(nome = "dad_senhaConta", tipo = Types.VARCHAR)
	private String senha;

	@Coluna(nome = "dad_salt", tipo = Types.VARCHAR)
	private String salt;

	@Coluna(nome = "dad_nvlAcesso", tipo = Types.INTEGER)
	private int nivelDeAcesso;

	public static void Cadastrar(Conta c) {
		ContaDao.insert(c);
	}
	
	public static void Atualizar(Conta c) {
		ContaDao.update(c);
	}
	
	
	/**
	 * Tenta fazer o login da Conta
	 * 
	 * @param loginDigitado O login informado
	 * @param senhaDigitada A senha informada
	 * @return A Resposta<Conta> com ou a conta logada caso o login funcione, ou
	 *         null e uma mensagem de erro
	 */
	public static Resposta<Conta> logar(String loginDigitado, String senhaDigitada) {

		Resposta<ArrayList<Conta>> resp = ContaDao.selectWhere("login", Where.IGUAL, loginDigitado);

		System.out.println(resp.getMensagem());

		System.out.println(resp.getFuncionou());

		if (resp.getFuncionou()) {

			if (resp.getObjeto().isEmpty()) {
				return new Resposta<>("Conta com login " + loginDigitado + " não encontrado");
			}

			if (resp.getObjeto().size() > 1) {
				return new Resposta<>("Erro");
			}

			// Pega a conta da resposta do SELECT
			Conta resultado = resp.getObjeto().get(0);

			if (resultado.senhaEstaCorreta(senhaDigitada)) {
				return new Resposta<Conta>("Login efetuado com sucesso", resultado);
			}

			return new Resposta<>("Senha incorreta");

		} else {

			return new Resposta<>(resp.getMensagem());

		}
	}

	private boolean senhaEstaCorreta(String senhaDigitada) {
		return PasswordUtils.verifyUserPassword(senhaDigitada, this.getSenha(), this.salt);
	}

	// SETTERS

	// feito deste jeito para simular campo como "final"
	public void setId(Integer id) {

		this.id = id;

	}

	public void setSenha(String senha) {
		this.salt = PasswordUtils.getSalt(10);
		String senhaSegura = PasswordUtils.generateSecurePassword(senha, salt);
		this.senha = senhaSegura;
	}

	// GETTERS

	public int getId() {
		return id;
	}

	public String getLogin() {
		return login;
	}

	public String getSenha() {
		return senha;
	}

	public NivelDeAcesso getNivelDeAcesso() {
		return NivelDeAcesso.fromInt(nivelDeAcesso);
	}

}
