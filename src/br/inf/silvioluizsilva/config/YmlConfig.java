/**
 * Classe de configuração do arquivo config.yml
 * 
 * @author SilvioLuizSilva
 * @version 1.0
 * 
 * Dependências: Main(), config.yml
 * 
 * Changelog:
 * 25/01/2018 - Versão inicial
 */

package br.inf.silvioluizsilva.config;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import br.inf.silvioluizsilva.essentials.Main;

public class YmlConfig {

	Main plugin;

	/**
	 * Método que testa se existe, e caso não exista, cria o arquivo config.yml
	 * 
	 * @param plugin
	 *            - estende o JavaPlugin da classe Main().
	 */
	public void createConfig(Main plugin) {
		try {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdirs();
			}
			File file = new File(plugin.getDataFolder(), "config.yml");
			if (!file.exists()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Arquivo <config.yml> nao encontrado. Criando estrutura default.");
				plugin.saveDefaultConfig();
			} else {
				if (plugin.getConfig().getString("Database.enable") == "false") {
					Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[SlsLib] " + ChatColor.YELLOW + "Arquivo <config.yml> encontrado! Carregando estrutura default.");
				}
				if (plugin.getConfig().getString("Database.enable") == "true") {
					new DbConfig();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
