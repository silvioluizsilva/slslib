/**
 * Classe de conexão com o banco de dados MySql
 * 
 * @author SilvioLuizSilva
 * @version 1.0
 * 
 * Dependências: Main(), YmlConfig, DbConfig()
 * 
 * Changelog:
 * 25/01/2018 - Versão inicial 
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
	static DbConfig cfg = DbConfig.getConfig();

	/**
	 * Método para abrir uma conexão. Os dados são carregados do arquivo "database.yml" que podem ser ajustados por código ou manualmente.
	 */
	@SuppressWarnings("static-access")
	private static Connection abrirConexao() {
		try {

			String pass = cfg.getConfig().getString("Database.mysql.pass");
			String user = cfg.getConfig().getString("Database.mysql.user");
			String host = cfg.getConfig().getString("Database.mysql.host");
			String port = cfg.getConfig().getString("Database.mysql.port");
			String name = cfg.getConfig().getString("Database.mysql.name");
			String driver = cfg.getConfig().getString("Database.mysql.driver");
			String url = driver + host + ":" + port + "/" + name;
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Conectando com o host de dados...");
			return DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "§cNao foi possivel estabelecer conexao com o host de dados!");
		}
		return null;
	}

	/**
	 * Método para criar uma tabela de dados.
	 * 
	 * @param tabela
	 *            - Nome da tabela que deverá ser criada
	 * @param key
	 *            - Campos da tabela com seus respectivos padrões
	 */
	public static void criarTabela(String tabela, String key) {
		try {
			String cmd = ("CREATE TABLE IF NOT EXISTS " + tabela + "(" + key + ") ENGINE = InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci;");
			Connection con = abrirConexao();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Tentando criar a tabela <" + tabela + ">...");
			// Verifica se a tabela já existe
			DatabaseMetaData metadados = con.getMetaData();
			ResultSet rs = metadados.getTables(null, null, tabela, null);
			if (rs.next()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "A tabela <" + tabela + "> ja existe!");
			} else {
				PreparedStatement st = con.prepareStatement(cmd);
				st.executeUpdate();
				con.close();
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.GREEN + "A tabela <" + tabela + "> foi criada com sucesso!");
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel criar a tabela <" + tabela + ">!");
		}
	}

	/**
	 * Método para verificar se uma tabela existe ou não
	 * 
	 * @param tabela
	 *            - Nome da tabela a ser verificada
	 * @return Boolean - Se é verdadeiro a tabela existe
	 * @throws SQLException
	 */
	public static boolean verificaTabela(String tabela) {
		try {
			Connection con = abrirConexao();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Tentando verificar a tabela <" + tabela + ">...");
			DatabaseMetaData metadados = con.getMetaData();
			ResultSet rs = metadados.getTables(null, null, tabela, null);
			if (!rs.next()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel encontrar a tabela <" + tabela + ">!");
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.GREEN + "Tabela encontrada: <" + tabela + ">!");
				return true;
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel verificar a tabela <" + tabela + ">!");
		}
		return false;
	}

	/**
	 * Apaga uma tabela existente
	 * 
	 * @param tabela
	 *            - Tabela a ser apagada
	 */
	public static void apagarTabela(String tabela) {
		try {
			String cmd = ("DROP TABLE IF EXISTS " + tabela + ";");
			Connection con = abrirConexao();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Tentando apagar a tabela <" + tabela + ">...");
			// Verifica se a tabela já existe
			DatabaseMetaData metadados = con.getMetaData();
			ResultSet rs = metadados.getTables(null, null, tabela, null);
			if (rs.next()) {
				PreparedStatement st = con.prepareStatement(cmd);
				st.executeUpdate();
				con.close();
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.GREEN + "A tabela " + tabela + " foi apagada com sucesso!");
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel apagar a tabela <" + tabela + ">!");
		}
	}

}
