package me.oqwe.namehistory.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.oqwe.namehistory.Main;
import me.oqwe.namehistory.command.sub.Help;
import me.oqwe.namehistory.command.sub.History;
import me.oqwe.namehistory.command.sub.Reload;
import me.oqwe.namehistory.util.Chat;

public class MainCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("namehistory.use")) {
			sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("no-permission")));
			return true;
		}
		
		if (args.length < 1) {
			sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("wrong-use")));
		}
		
		if (args[0].charAt(0) == '!') {
			try {
				History.run(sender, args[0].substring(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
		
		switch (args[0]) {
		
		case "reload":
			Reload.run(sender); 
			return true;
			
		/*case "skin":
			Skin.run(sender, args);
			return true;*/
			
		case "help":
			Help.run(sender);
			return true;
			
		default:
			try {
				History.run(sender, args[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
			
		}

	}

}
