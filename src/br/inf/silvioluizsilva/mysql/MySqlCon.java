/**
 * Classe de conexão com o banco de dados MySql
 * 
 * @author SilvioLuizSilva
 * @version 1.1
 * 
 * Dependências: Main(), YmlConfig, DbConfig()
 * 
 * Changelog:
 * 25/01/2018 - Versão inicial
 * 26/01/2018 - 1. Removidos os métodos static
 * 26/01/2018 - 1. Movido do package mysql para connect, possibilitando adicionar novas plataformas de banco de dados
 */

package br.inf.silvioluizsilva.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import br.inf.silvioluizsilva.config.DbConfig;
import br.inf.silvioluizsilva.essentials.Main;

public class MySqlCon {

	Main plugin;
	DbConfig cfg = new DbConfig();

	public String pass, user, host, port, name, driver;
	private Connection connection;

	/**
	 * Método para abrir uma conexão. Os dados são carregados do arquivo "database.yml" que podem ser ajustados por código ou manualmente.
	 */
	private void abrirConexao() {

		pass = cfg.getConfig().getString("Database.mysql.pass");
		user = cfg.getConfig().getString("Database.mysql.user");
		host = cfg.getConfig().getString("Database.mysql.host");
		port = cfg.getConfig().getString("Database.mysql.port");
		name = cfg.getConfig().getString("Database.mysql.name");
		driver = cfg.getConfig().getString("Database.mysql.driver");
		try {
			synchronized (this) {
				if (connection != null && connection.isClosed()) {

					return;
				}
				Class.forName("com.mysql.jdbc.Driver");
				setConnection(DriverManager.getConnection(this.driver + this.host + ":" + this.port + "/" + this.name + "?verifyServerCertificate=false&useSSL=false", this.user, this.pass));
			}
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Conectando com o host de dados...");
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel estabelecer conexao com o host de dados!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel estabelecer conexao com o host de dados!");
			e.printStackTrace();
		}
	}

	/**
	 * Método para buscar a conexão atual
	 * 
	 * @param connection
	 *            - A própria conexão atual
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Método para setar a nova conexão
	 * 
	 * @param connection
	 *            - A nova conexão
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Método para criar uma tabela de dados.
	 * 
	 * @param table
	 *            - Nome da tabela que deverá ser criada
	 * @param key
	 *            - Campos da tabela com seus respectivos padrões
	 */
	public void criarTabela(String table, String columns) {
		try {
			String cmd = ("CREATE TABLE IF NOT EXISTS " + table + "(" + columns + ") ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;");
			abrirConexao();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Tentando criar a tabela <" + table + ">...");
			// Verifica se a tabela já existe
			DatabaseMetaData metadados = getConnection().getMetaData();
			ResultSet rs = metadados.getTables(null, null, table, null);
			if (rs.next()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "A tabela <" + table + "> ja existe!");
			} else {
				PreparedStatement st = getConnection().prepareStatement(cmd);
				st.executeUpdate();
				getConnection().close();
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.GREEN + "A tabela <" + table + "> foi criada com sucesso!");
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel criar a tabela <" + table + ">!");
		}
	}

	/**
	 * Método para verificar se uma tabela existe ou não
	 * 
	 * @param table
	 *            - Nome da tabela a ser verificada
	 * @return Boolean - Se é verdadeiro a tabela existe
	 * @throws SQLException
	 */
	public boolean verificaTabela(String table) {
		try {
			abrirConexao();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Tentando verificar a tabela <" + table + ">...");
			DatabaseMetaData metadados = getConnection().getMetaData();
			ResultSet rs = metadados.getTables(null, null, table, null);
			if (!rs.next()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel encontrar a tabela <" + table + ">!");
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.GREEN + "Tabela encontrada: <" + table + ">!");
				getConnection().close();
				return true;
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel verificar a tabela <" + table + ">!");
		}
		return false;
	}

	/**
	 * Apaga uma tabela existente
	 * 
	 * @param table
	 *            - Tabela a ser apagada
	 */
	public void apagarTabela(String table) {
		try {
			String cmd = ("DROP TABLE IF EXISTS " + table + ";");
			abrirConexao();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Tentando apagar a tabela <" + table + ">...");
			// Verifica se a tabela já existe
			DatabaseMetaData metadados = getConnection().getMetaData();
			ResultSet rs = metadados.getTables(null, null, table, null);
			if (rs.next()) {
				PreparedStatement st = getConnection().prepareStatement(cmd);
				st.executeUpdate();
				getConnection().close();
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.GREEN + "A tabela " + table + " foi apagada com sucesso!");
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel apagar a tabela <" + table + ">!");
		}
	}

}
