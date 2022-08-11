package me.oqwe.namehistory.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import me.oqwe.namehistory.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Chat {

	private static FileConfiguration config = Main.getInstance().getConfig();

	public static String cc(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static void sendFormattedMessage(CommandSender sender, Set<Entry<String, Long>> names, String currentName)
			throws Exception {

		// if there is only one name in the map, player has never changed their name
		if (names.size() == 1) {
			StringBuilder sb = new StringBuilder();
			names.forEach(entry -> sb.append(entry.getKey()));
			if (Main.getInstance().getConfig().getBoolean("show-link")) {
				sender.spigot().sendMessage(getTextWithLink(
						config.getString("never-changed-name").replace("<player>", sb.toString()), currentName));
			} else
				sender.sendMessage(cc(config.getString("never-changed-name").replace("<player>", sb.toString())));
			return;
		}

		sender.sendMessage(cc(config.getString("history-is").replace("<player>", currentName)));

		for (var entry : names) {

			// the first name is formatted differently, as there is no change date
			if (entry.getValue() == 0L) {

				if (Main.getInstance().getConfig().getBoolean("show-link")) {
					sender.spigot().sendMessage(getTextWithLink(
							config.getString("first-name").replace("<name>", entry.getKey()), currentName));
				} else
					sender.sendMessage(cc(config.getString("first-name").replace("<name>", entry.getKey())));
				continue;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(entry.getValue()));

			int year = calendar.get(Calendar.YEAR);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			String month = Integer.toString(calendar.get(Calendar.MONTH)).replace("10", "November")
					.replace("11", "December").replace("0", "January").replace("1", "February").replace("2", "March")
					.replace("3", "April").replace("4", "May").replace("5", "June").replace("6", "July")
					.replace("7", "August").replace("8", "September").replace("9", "October");

			if (Main.getInstance().getConfig().getBoolean("show-link")) {
				sender.spigot().sendMessage(getTextWithLink(
						config.getString("name").replace("<month>", month).replace("<year>", Integer.toString(year))
								.replace("<day>", Integer.toString(day)).replace("<name>", entry.getKey()),
						currentName));
			} else
				sender.sendMessage(
						cc(config.getString("name").replace("<month>", month).replace("<year>", Integer.toString(year))
								.replace("<day>", Integer.toString(day)).replace("<name>", entry.getKey())));

		}

		if (Main.getInstance().getConfig().getBoolean("uuid")) {

			String id = Request.sendGetIdRequest(currentName);

			TextComponent text = new TextComponent(cc(Main.getInstance().getConfig().getString("uuid-msg")
					.replace("<player>", currentName).replace("<uuid>", id)));

			text.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, id));
			text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(cc("&eClick to copy the UUID"))));

			sender.spigot().sendMessage(text);

		}

	}

	public static TextComponent getTextWithLink(String msg, String name) {

		TextComponent text = new TextComponent(cc(msg));

		text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://namemc.com/search?q=" + name));
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new Text(cc("&eClick to visit this players name mc site"))));

		return text;
	}

}
