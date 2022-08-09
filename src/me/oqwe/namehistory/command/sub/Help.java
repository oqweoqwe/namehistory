package me.oqwe.namehistory.command.sub;

import org.bukkit.command.CommandSender;

import me.oqwe.namehistory.Main;
import me.oqwe.namehistory.util.Chat;

public class Help {

	public static void run(CommandSender sender) {

		Main.getInstance().getConfig().getStringList("help")
				.forEach(msg -> sender.sendMessage(Chat.cc(msg.replace("<pluginname>", Main.getInstance().getDescription().getName())
						.replace("<version>", Main.getInstance().getDescription().getVersion()))));

	}

}
