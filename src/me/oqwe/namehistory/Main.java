package me.oqwe.namehistory;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import me.oqwe.namehistory.command.MainCommand;

public class Main extends JavaPlugin implements CommandExecutor {

	private static Main instance;
	
	// TODO add /nh skin command
	// TODO show uuid option in config

	public void onEnable() {
		
		instance = this;
		
		getCommand("namehistory").setExecutor(new MainCommand());
		saveDefaultConfig();

	}
	
	public static Main getInstance() {
		return instance;
	}

}
