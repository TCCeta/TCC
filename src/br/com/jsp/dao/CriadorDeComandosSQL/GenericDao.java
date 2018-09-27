/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.jsp.dao.CriadorDeComandosSQL;

import br.com.jsp.bean.Annotations.Tabela;
import br.com.jsp.bean.Annotations.Coluna;
import br.com.jsp.bean.response.Resposta;
import br.com.jsp.connector.ConnectionFactory;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

/**
 * Classe gen√©rica para cria√ß√£o de comandos SQL
 *
 * @author patrick
 * @param <T> Tipo de dado a ser Usado nos comandos
 */
@SuppressWarnings("unused")
public class GenericDao<T> {

	final String nomeBanco = "newtccnew";

	private final Connection conexao;

	final Class<T> typeClass;

	/**
	 * Construtor
	 * 
	 * @param clazz Classe.class a ser usada nos comandos
	 */
	public GenericDao(Class<T> clazz) {
		typeClass = clazz;
		conexao = new ConnectionFactory(nomeBanco).obterConexao();
	}

	/**
	 * Gerador de comandos INSERT para um s√≥ Objeto
	 *
	 * @param obj Objeto a ser inserido
	 */
	public Resposta<Integer> insert(T obj) {

		// Prepara uma ArrayList
		ArrayList<Field> fieldsUsados = new ArrayList<>();

		if (typeClass.isAnnotationPresent(Tabela.class)) {

			Tabela tab = (Tabela) typeClass.getAnnotation(Tabela.class);

			String sql = "";

			sql += "\nINSERT INTO " + tab.nome() + " (";

			for (Field field : typeClass.getDeclaredFields()) {

				if (field.isAnnotationPresent(Coluna.class)) {

					fieldsUsados.add(field);
					Coluna col = (Coluna) field.getAnnotation(Coluna.class);

					sql += col.nome() + ",";

				}

			}

			sql = sql.substring(0, sql.length() - 1) + ") \nVALUES\n    (";

			for (Field fieldsUsado : fieldsUsados) {

				sql += "?,";

			}

			sql = sql.substring(0, sql.length() - 1) + ")";

			try {

				PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				for (int i = 0; i < fieldsUsados.size(); i++) {

					Field field = fieldsUsados.get(i);

					Coluna col = field.getAnnotation(Coluna.class);

					boolean wasAccessible = field.isAccessible();
					if (!wasAccessible) {
						field.setAccessible(true);
					}

					if (col.autoGerado()) {

						pstmt.setNull(i + 1, col.tipo());

					} else if (col.tipo() == Types.BLOB) {

						pstmt.setBlob(i + 1, (Blob) field.get(obj));

					} else {

						pstmt.setObject(i + 1, field.get(obj), col.tipo());

					}

					if (!wasAccessible) {
						field.setAccessible(false);
					}

				}

				pstmt.execute();

				ResultSet rs = pstmt.getGeneratedKeys();

				rs.next();

				int generatedKey = rs.getInt(1);

				pstmt.close();

				return new Resposta<Integer>("OperaÁ„o efetuada com sucesso", generatedKey);

			} catch (Exception e) {

				return new Resposta<>("Erro : " + e.getMessage());

			}

		} else {
			return new Resposta<>(typeClass.getSimpleName() + " n„o contÈm @Tabela");
		}
	}

	

	/**
	 * Gerador de comandos UPDATE para um √∫nico objeto, ir√° mudar no banco o item
	 * que tiver o ID do objeto
	 *
	 * @param obj Objeto a receber Update
	 */
	public Resposta<Boolean> update(T obj) {

		String sql = "";

		if (typeClass.isAnnotationPresent(Tabela.class)) {

			Tabela tab = (Tabela) typeClass.getAnnotation(Tabela.class);

			sql = "UPDATE " + tab.nome() + " SET ";

			ArrayList<Field> fieldsUsados = new ArrayList<>();

			Field primaryField = null;

			for (Field field : typeClass.getDeclaredFields()) {

				if (field.isAnnotationPresent(Coluna.class)) {

					Coluna col = field.getAnnotation(Coluna.class);

					if (!col.autoGerado()) {

						fieldsUsados.add(field);
						sql += col.nome() + " = ?,";

					} else if (col.primaryKey()) {

						primaryField = field;

					}

				}

			}

			sql = sql.substring(0, sql.length() - 1) + " WHERE " + primaryField.getAnnotation(Coluna.class).nome()
					+ " = ?";


			try {

				PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				for (int i = 0; i < fieldsUsados.size(); i++) {

					Field field = fieldsUsados.get(i);

					Coluna col = field.getAnnotation(Coluna.class);

					int index = i + 1;

					Boolean wasActive = field.isAccessible();
					if (!wasActive) {
						field.setAccessible(true);
					}

					if (col.primaryKey()) {
						index = fieldsUsados.size() + 1;

						pstmt.setNull(index, col.tipo());

					} else if (col.tipo() == Types.BLOB) {

						pstmt.setBlob(index, (Blob) field.get(obj));

					} else {
						pstmt.setObject(index, field.get(obj), col.tipo());
					}

					if (!wasActive) {
						field.setAccessible(false);
					}

				}

				boolean wasAccessible = primaryField.isAccessible();
				if (!wasAccessible) {
					primaryField.setAccessible(true);
				}

				pstmt.setObject(fieldsUsados.size() + 1, primaryField.get(obj),
						primaryField.getAnnotation(Coluna.class).tipo());

				if (!wasAccessible) {
					primaryField.setAccessible(false);
				}

				pstmt.execute();
				pstmt.close();

				return new Resposta<Boolean>("Funcionou", true);

			} catch (Exception e) {

				return new Resposta<>("Erro : " + e.getMessage());

			}

		} else {
			return new Resposta<>(typeClass.getSimpleName() + " n„o contÈm @Tabela");
		}

	}

	
	/**
	 * Gerador de comando SELECT * [...]
	 *
	 * @return lista dos objetos obtidos a partir do SELECT
	 */
	public Resposta<ArrayList<T>> selectAll() {

		String sql = "";

		if (typeClass.isAnnotationPresent(Tabela.class)) {

			Tabela tab = typeClass.getAnnotation(Tabela.class);

			sql = "SELECT * FROM " + tab.nome();

			ArrayList<T> list = new ArrayList<>();

			try {

				Statement pstmt = conexao.createStatement();

				ResultSet rs = pstmt.executeQuery(sql);

				while (rs.next()) {

					T obj = typeClass.newInstance();

					for (Field field : typeClass.getDeclaredFields()) {

						if (field.isAnnotationPresent(Coluna.class)) {

							boolean wasAccessible = field.isAccessible();

							if (!wasAccessible) {
								field.setAccessible(true);
							}

							if (field.getAnnotation(Coluna.class).tipo() == Types.BLOB) {
								field.set(obj, rs.getBlob(field.getAnnotation(Coluna.class).nome()));
							} else {
								field.set(obj, rs.getObject(field.getAnnotation(Coluna.class).nome()));
							}

							if (!wasAccessible) {

								field.setAccessible(false);

							}

						}

					}

					list.add(obj);

				}

				pstmt.close();

				return new Resposta<ArrayList<T>>("OperaÁ„o feita com sucesso", list);

			} catch (Exception e) {

				return new Resposta<ArrayList<T>>("Erro : " + e.getMessage());

			}

		} else {
			return new Resposta<ArrayList<T>>(typeClass.getSimpleName() + " n„o contÈm Annotation @Tabela");
		}

	}

	/**
	 * Gerador de comando SELECT * [...] ORDER BY campo
	 * 
	 * @param campo nome do campo pelo qual o resultado deve ser ordenado
	 * @param ordem Ascendente ou Descendente
	 * @return lista dos objetos obtidos a partir do SELECT
	 *
	 */
	public Resposta<ArrayList<T>> selectAll(String campo, Order ordem) {

		String sql = null;

		Field f = null;
		try {
			f = typeClass.getDeclaredField(campo);
		} catch (Exception e) {
			return new Resposta<>("Campo " + campo + " n√£o existe em " + typeClass.getSimpleName());
		}
		if (!f.isAnnotationPresent(Coluna.class)) {
			return new Resposta<ArrayList<T>>("Field apresentado n√£o cont√©m @Coluna", null, false);
		}

		if (typeClass.isAnnotationPresent(Tabela.class)) {

			Tabela tab = typeClass.getAnnotation(Tabela.class);

			if (null == ordem) {
				return new Resposta<ArrayList<T>>("Ordem n√£o foi informada", null, false);
			} else {
				switch (ordem) {
				case ASC:
					sql = "SELECT * FROM " + tab.nome() + " ORDER BY " + f.getAnnotation(Coluna.class).nome() + " ASC";
					break;
				case DESC:
					sql = "SELECT * FROM " + tab.nome() + " ORDER BY " + f.getAnnotation(Coluna.class).nome() + " DESC";
					break;
				}
			}

			ArrayList<T> list = new ArrayList<>();

			try {

				Statement pstmt = conexao.createStatement();

				System.out.println(pstmt);

				ResultSet rs = pstmt.executeQuery(sql);

				while (rs.next()) {

					T obj = typeClass.newInstance();

					for (Field field : typeClass.getDeclaredFields()) {

						if (field.isAnnotationPresent(Coluna.class)) {

							boolean wasAccessible = field.isAccessible();

							if (!wasAccessible) {
								field.setAccessible(true);
							}

							field.set(obj, rs.getObject(field.getAnnotation(Coluna.class).nome()));

							if (!wasAccessible) {
								field.setAccessible(false);
							}

						}

					}

					list.add(obj);

				}

				pstmt.close();

				return new Resposta<ArrayList<T>>("OperaÁ„o efetuada com sucess", list);

			} catch (Exception e) {

				return new Resposta<ArrayList<T>>("Erro : " + e.getMessage());

			}

		}

		return new Resposta<ArrayList<T>>("Classe n√£o tem @Tabela");

	}

	/**
	 * Criador de comandos SELECT com WHERE
	 * 
	 * @param campo      campo a ser usado no WHERE
	 * @param comparacao tipo de verifica√ß√£o do WHERE, como Where.IGUAL,
	 *                   Where.MAIOR, etc...
	 * @param valor      valor a ser usado para verifica√ß√£o do Where
	 * @return ArrayList com os objetos retornados pelo SELECT
	 */
	public Resposta<ArrayList<T>> selectWhere(String campo, Where comparacao, Object valor) {

		String sql = "";

		if (typeClass.isAnnotationPresent(Tabela.class)) {

			Tabela tab = typeClass.getAnnotation(Tabela.class);

			Field fieldEscolhido;

			sql = "SELECT * FROM " + tab.nome() + " WHERE ";

			try {
				fieldEscolhido = typeClass.getDeclaredField(campo);
			} catch (Exception e) {
				return new Resposta<>("Campo " + campo + " n√£o existe na classe " + typeClass.getSimpleName());
			}

			if (!fieldEscolhido.isAnnotationPresent(Coluna.class)) {
				return new Resposta<>("Campo " + campo + " n√£o tem @Coluna");
			}

			if (comparacao == Where.IGUAL) {
				sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " = ?";
			} else if (comparacao == Where.DIFERENTE) {
				sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " != ?";
			} else if (comparacao == Where.MAIOR) {
				sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " > ?";
			} else if (comparacao == Where.MENOR) {
				sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " < ?";
			} else if (comparacao == Where.MAIOR_IGUAL) {
				sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " >= ?";
			} else if (comparacao == Where.MENOR_IGUAL) {
				sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " <= ?";
			} else if (comparacao == Where.BETWEEN) {
				if (valor.getClass().isArray()) {
					try {
						Object[] inf = (Object[]) valor;
						sql = "SELECT * FROM " + tab.nome() + " WHERE " + fieldEscolhido.getAnnotation(Coluna.class).nome() + " BETWEEN ? AND ?";
					} catch (Exception e) {
						return new Resposta<>("3" + e.getMessage());
					}
				} else {
					return new Resposta<>("valor informado deve ser Array quando comparacao √© BETWEEN");
				}
			} else if (comparacao == Where.NOT_BETWEEN) {
				if (valor.getClass().isArray()) {
					try {
						Object[] inf = (Object[]) valor;
						sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " NOT BETWEEN ? AND ?";
					} catch (Exception e) {
						return new Resposta<>("2" + e.getMessage());
					}
				} else {
					return new Resposta<>("valor informado deve ser Array quando comparacao √© NOT_BETWEEN");
				}
			} else if (comparacao == Where.LIKE) {

				if (valor instanceof String) {
					sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " LIKE ?";
				} else {
					return new Resposta<>("valor informado deve ser String quando comparacao √© LIKE");
				}

			} else if (comparacao == Where.NOT_LIKE) {

				if (valor instanceof String) {
					sql += fieldEscolhido.getAnnotation(Coluna.class).nome() + " NOT LIKE ?";
				} else {
					return new Resposta<>("valor informado deve ser String quando comparacao √© NOT LIKE");
				}

			} else {
				return new Resposta<>("comparacao invalida");
			}

			ArrayList<T> list = new ArrayList<>();

			try {

				PreparedStatement pstmt = conexao.prepareStatement(sql);

				if (comparacao == Where.IGUAL || comparacao == Where.DIFERENTE || comparacao == Where.MAIOR
						|| comparacao == Where.MENOR || comparacao == Where.MAIOR_IGUAL
						|| comparacao == Where.MENOR_IGUAL || comparacao == Where.LIKE
						|| comparacao == Where.NOT_LIKE) {
					pstmt.setObject(1, valor, fieldEscolhido.getAnnotation(Coluna.class).tipo());
				} else if (comparacao == Where.BETWEEN || comparacao == Where.NOT_BETWEEN) {

					try {
						Object[] inf = (Object[]) valor;
						
						pstmt.setObject(1, inf[0], fieldEscolhido.getAnnotation(Coluna.class).tipo());
						pstmt.setObject(2, inf[1], fieldEscolhido.getAnnotation(Coluna.class).tipo());
						
					} catch (Exception e) {
						return new Resposta<>("3" + e.getMessage());
					}

				}

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {

					T obj = typeClass.newInstance();

					for (Field field : typeClass.getDeclaredFields()) {

						if (field.isAnnotationPresent(Coluna.class)) {

							boolean wasAccessible = field.isAccessible();

							if (!wasAccessible) {
								field.setAccessible(true);
							}

							field.set(obj, rs.getObject(field.getAnnotation(Coluna.class).nome()));

							if (!wasAccessible) {
								field.setAccessible(false);
							}

						}

					}

					list.add(obj);

				}

				pstmt.close();

				return new Resposta<ArrayList<T>>("OperaÁ„o efetuada com sucesso", list);

			} catch (Exception e) {

				return new Resposta<ArrayList<T>>(e.getMessage());

			}

		}

		return null;

	}

}
