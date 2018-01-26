/**
 * Classe Main() somente para testes
 * 
 * @author SilvioLuizSilva
 * @version 1.0
 * 
 * ############################################################################################################
 * ATENÇÃO: NÃO ALTERE NENHUMA DAS CLASSES AQUI. PARA REALIZAR ALTERAÇÕES COPIE PARA A ÁREA DE DESENVOLVIMENTO!
 * ############################################################################################################
 * 
 * Fileslog:
 * 25/01/2018 - DbConfig  1.0
 * 25/01/2018 - YmlConfig 1.0
 * 25/01/2018 - MySqlCon  1.0
 */
package br.inf.silvioluizsilva.essentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import br.inf.silvioluizsilva.config.YmlConfig;

public class Main extends JavaPlugin implements Listener {

	public static Main m;
	public PluginManager pm = Bukkit.getPluginManager();
	public BukkitScheduler sh = Bukkit.getScheduler();
	private YmlConfig config;

	public YmlConfig getConfigs() {
		return this.config;
	}

	@Override
	public void onLoad() {
		m = this;

	}

	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[SlsLib] " + ChatColor.GREEN + "Library Carregada");
		pm.registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[SlsLib] " + ChatColor.RED + "Library Descarregada");
		HandlerList.unregisterAll();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Verifica se foi um player que solicitou
		// if (!(sender instanceof Player)) {
		// sender.sendMessage("§cComando apenas para Players!");
		// return true;
		// }
		// Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("teste")) {

			// Teste do YmlConfig()
			// YmlConfig cfg = new YmlConfig();
			// cfg.createConfig(m);

			// Teste de criar as tabelas
			// MySqlApi.criarTabela("testesql", "SAPO VARCHAR(10), APELIDO VARCHAR(10)");

			// Para verificar as tabelas
			// try {
			// if (MySqlApi.verificaTabela("teste")) {
			// Bukkit.getConsoleSender().sendMessage("§a[Essentials.MySql]Teste");
			// } else {
			// Bukkit.getConsoleSender().sendMessage("§c[Essentials.MySql]No Teste");
			// }
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

			// try {
			// MySqlApi.verificaTabela("teste");
			// Bukkit.getConsoleSender().sendMessage("§c[Essentials.MySql]Tabela nao encontrada!");
			// } catch (SQLException e) {
			// e.printStackTrace();
			// }

			// Teste de comandos;
			// Comandos key = new Comandos();
			// // key.restoreFood(sender);

		}
		return false;
	}

}