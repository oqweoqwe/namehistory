package me.oqwe.namehistory.command.sub;

import org.bukkit.command.CommandSender;

import me.oqwe.namehistory.Main;
import me.oqwe.namehistory.util.Chat;
import me.oqwe.namehistory.util.Json;
import me.oqwe.namehistory.util.Request;

public class History {

	public static void run(CommandSender sender, String name) throws Exception {

		if (Main.getTimestamps().containsKey(sender)) {

			if (!(System.currentTimeMillis()
					- Main.getTimestamps().get(sender) > Main.getInstance().getConfig().getLong("cooldown") * 1000)) {
				// on cooldown
				sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("cooldown-msg")));
				return;

			} else
				Main.getTimestamps().replace(sender, System.currentTimeMillis());
		}
		
		Main.getTimestamps().put(sender, System.currentTimeMillis());
		
		sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("fetch").replace("<player>", name)));

		// get uuid from username
		String id = Request.sendGetIdRequest(name, sender);

		if (id != null) {
			// id is good, get history

			String history = Request.sendGetHistoryRequest(id, sender);

			Chat.sendFormattedMessage(sender, Json.parseJson(history), name);

		} else
			sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("no-player").replace("<name>", name)));

	}

}
