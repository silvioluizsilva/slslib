/**
 * Classe de conexão com o banco de dados SqLite
 * 
 * @author SilvioLuizSilva
 * @version 1.0
 * 
 * Dependências: Main()
 * 
 * Changelog:
 * 26/01/2018 - Versão inicial
 */

package br.inf.silvioluizsilva.connect;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import br.inf.silvioluizsilva.essentials.Main;

public class SqLiteCon {

	private Main plugin = Main.getPlugin(Main.class);
	private File file;
	private Connection connection;

	/**
	 * Método para abrir uma conexão. Os dados são carregados localmente do arquivo "SqLite.db".
	 */
	private void abrirConexao() {

		file = new File(plugin.getDataFolder(), "SqLite.db");
		try {
			if (!file.exists()) {
				file.createNewFile();
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Arquivo <SqLite.db> nao encontrado. Criando...");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel criar o arquivo <SqLite.db>!");
			return;
		}
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + file);
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Buscando arquivo de dados <SqLite.db>...");
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel estabelecer conexao com o arquivo de dados <SqLite.db>!");
			e.printStackTrace();
		}
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
	 * Método para buscar a conexão atual
	 * 
	 * @param connection
	 *            - A própria conexão atual
	 */
	public Connection getConnection() {
		return connection;
	}

	public void criarTabela(String table, String columns) {
		String cmd = ("CREATE TABLE IF NOT EXISTS " + table + "(" + columns + ");");
		try {
			abrirConexao();
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Tentando criar a tabela <" + table + ">...");
			DatabaseMetaData metadados = getConnection().getMetaData();
			ResultSet rs = metadados.getTables(null, null, table, null);
			if (rs.next()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "A tabela <" + table + "> ja existe!");
				getConnection().close();
				setConnection(null);
			} else {
				PreparedStatement st = getConnection().prepareStatement(cmd);
				st.executeUpdate();
				getConnection().close();
				setConnection(null);
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.GREEN + "A tabela <" + table + "> foi criada com sucesso!");
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.RED + "Nao foi possivel criar a tabela <" + table + ">!");
			e.printStackTrace();
		}
	}

}