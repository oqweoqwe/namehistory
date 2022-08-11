package me.oqwe.namehistory;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.oqwe.namehistory.command.MainCommand;

public class Main extends JavaPlugin implements CommandExecutor {

	private static Main instance;
	
	private static Map<CommandSender, Long> timestamps = new HashMap<>();

	public void onEnable() {
		
		instance = this;
		
		getCommand("namehistory").setExecutor(new MainCommand());
		saveDefaultConfig();

	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public static Map<CommandSender, Long> getTimestamps() {
		return timestamps;
	}

}
