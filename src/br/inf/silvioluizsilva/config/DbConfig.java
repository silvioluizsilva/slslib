/**
 * Classe de configuração do arquivo database.yml
 * 
 * @author SilvioLuizSilva
 * @version 1.0
 * 
 * Dependências: Main(), database.yml
 * 
 * Changelog:
 * 25/01/2018 - Versão inicial
 * 26/01/2018 - Removidos os métodos static
 */

package br.inf.silvioluizsilva.config;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import br.inf.silvioluizsilva.essentials.Main;

public class DbConfig extends YamlConfiguration {

	private DbConfig config;
	private Main plugin;
	private File configFile;

	/**
	 * Função que verifica se existe uma configuração salva, caso contrário, cria uma
	 */
	public DbConfig getConfig() {
		if (config == null) {
			config = new DbConfig();
		}
		return config;
	}

	/**
	 * Função que cria o arquivo database.yml padrão
	 */
	public DbConfig() {
		plugin = Main.getPlugin(Main.class);
		configFile = new File(plugin.getDataFolder(), "database.yml");
		if (!configFile.exists()) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Arquivo <database.yml> nao encontrado. Criando estrutura default.");
			saveDefault();
		} else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Arquivo <database.yml> encontrado! Carregando estrutura default.");
		}
		reload();
	}

	/**
	 * Função alternativa de reload, para evitar que seja necessário colocar try..catch no código
	 */
	public void reload() {
		try {
			super.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função alternativa de save, para evitar que seja necessário colocar try..catch no código
	 */
	public void save() {
		try {
			super.save(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função alternativa, que salva o padrão do arquivo database.yml
	 */
	public void saveDefault() {
		plugin.saveResource("database.yml", false);
	}

}