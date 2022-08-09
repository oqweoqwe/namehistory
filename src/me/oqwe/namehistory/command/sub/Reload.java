package me.oqwe.namehistory.command.sub;

import org.bukkit.command.CommandSender;

import me.oqwe.namehistory.Main;
import me.oqwe.namehistory.util.Chat;

public class Reload {

	public static void run(CommandSender sender) {
		Main.getInstance().reloadConfig();
		sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("reload")
				.replace("<version>", Main.getInstance().getDescription().getVersion())
				.replace("<pluginname>", Main.getInstance().getDescription().getName())));
	}

}
