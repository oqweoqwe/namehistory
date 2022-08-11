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

		if (args.length < 1) {
			sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("wrong-use")));
		}

		if (args[0].charAt(0) == '!') {

			if (!sender.hasPermission("namehistory.history")) {
				sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("no-permission")));
				return true;
			}

			try {
				History.run(sender, args[0].substring(1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		switch (args[0]) {

		case "reload":
			if (!sender.hasPermission("namehistory.reload")) {
				sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			Reload.run(sender);
			return true;

		case "help":
			if (!sender.hasPermission("namehistory.help")) {
				sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			Help.run(sender);
			return true;

		default:
			if (!sender.hasPermission("namehistory.history")) {
				sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("no-permission")));
				return true;
			}
			try {
				History.run(sender, args[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;

		}

	}

}
