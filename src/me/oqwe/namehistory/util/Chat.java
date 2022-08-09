package me.oqwe.namehistory.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import me.oqwe.namehistory.Main;

public class Chat {

	private static FileConfiguration config = Main.getInstance().getConfig();

	public static String cc(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static void sendFormattedMessage(CommandSender sender, Set<Entry<String, Long>> names, String currentName) {

		// if there is only one name in the map, player has never changed their name
		if (names.size() == 1) {
			StringBuilder sb = new StringBuilder();
			names.forEach(entry -> sb.append(entry.getKey()));
			sender.sendMessage(cc(config.getString("never-changed-name").replace("<player>", sb.toString())));
			return;
		}

		sender.sendMessage(cc(config.getString("history-is").replace("<player>", currentName)));
		
		for (var entry : names) {

			// the first name is formatted differently, as there is no change date
			if (entry.getValue() == 0L) {
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

			sender.sendMessage(
					cc(config.getString("name").replace("<month>", month).replace("<year>", Integer.toString(year))
							.replace("<day>", Integer.toString(day)).replace("<name>", entry.getKey())));

		}

	}

}
