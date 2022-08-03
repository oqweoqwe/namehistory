package me.oqwe.namehistory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

	// NOTE all this was written at like 3 in the morning and i was too tired to fix
	// the gson exceptions i kept getting so i made it with no json parser
	// which means its not very pretty. it does work tho, so just dont think too
	// much about the quality of the code
	// i might make this work in an intelligent and elegant way some day if im bored
	// but not today

	public void onEnable() {

		getCommand("namehistory").setExecutor(this);
		saveDefaultConfig();

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("namehistory.use")) {
			sender.sendMessage(cc(getConfig().getString("nopermission")));
			return true;
		}			
		
		if (args.length >= 1) {

			if (args[0].equalsIgnoreCase("reload")) {
				reloadConfig();
				sender.sendMessage(cc(getConfig().getString("reload")));
				return true;
			}
			
			String id = null;

			sender.sendMessage(cc(getConfig().getString("fetch").replace("<player>", args[0])));

			// get uuid from username
			try {
				id = resolveUsername(args[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// uuid is good, get name history
			if (id != null) {

				String history = null;

				try {
					history = getNameHistory(id);
				} catch (Exception e) {
					e.printStackTrace();
				}

				sendPrettyMessage(sender, getAMapFromTheGoddamnList(parseJsonArray(history)), args[0]);

			} else
				sender.sendMessage(cc(getConfig().getString("noplayer")));

		} else
			sender.sendMessage(cc(getConfig().getString("wronguse")));

		return true;
	}

	private void sendPrettyMessage(CommandSender sender, Map<String, Long> data, String currentname) {

		if (data.entrySet().size() == 1) {
			data.entrySet().forEach(
					entry -> sender.sendMessage(cc("&e" + entry.getKey() + " &7has never changed their name")));
			return;
		}

		sender.sendMessage(cc("&eThe name history for &6" + currentname + "&e is"));

		for (var entry : data.entrySet()) {
			if (entry.getValue() == 0) {
				sender.sendMessage(cc("&7- &e" + entry.getKey() + " &7was this players first name"));
				continue;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(entry.getValue()));
			sender.sendMessage(cc("&7- &e" + entry.getKey() + " &7(changed in &6" + calendar.get(Calendar.YEAR)
					+ "&7 on &6" + Integer.toString(calendar.get(Calendar.MONTH))
					.replace("10", "November")
					.replace("11", "December")
					.replace("0", "January")
					.replace("1", "February")
					.replace("2", "March")
					.replace("3", "April")
					.replace("4", "May")
					.replace("5", "June")
					.replace("6", "July")
					.replace("7", "August")
					.replace("8", "September")
					.replace("9", "October")
					+ " "
					+ calendar.get(Calendar.DAY_OF_MONTH)+"&7)"));
		}

	}

	// puts firstname with value null and the rest with unix epoch timestamps
	private Map<String, Long> getAMapFromTheGoddamnList(List<String> data) {

		Map<String, Long> map = new LinkedHashMap<String, Long>();
		// iterate over all strings in list
		for (String s : data) {

			if (!s.contains(",")) {
				// first name
				StringBuilder sb = new StringBuilder();
				for (int i = 9; i < s.length(); i++) {
					if (s.charAt(i) == '"')
						break;
					sb.append(s.charAt(i));
				}
				map.put(sb.toString(), 0L);
			} else {

				// not first name
				StringBuilder sb = new StringBuilder();
				for (int i = 9; i < s.length(); i++) {
					if (s.charAt(i) == '"')
						break;
					sb.append(s.charAt(i));
				}
				String name = sb.toString();
				sb = new StringBuilder();
				for (int i = 25 + name.length(); i < s.length(); i++) {
					if (s.charAt(i) == '}')
						break;
					sb.append(s.charAt(i));
				}

				long timestamp = Long.parseLong(sb.toString());

				map.put(name, timestamp);
			}

		}

		// order map
		Map<String, Long> orderedmap = new LinkedHashMap<String, Long>();

		map.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.forEach(entry -> orderedmap.put(entry.getKey(), entry.getValue()));

		return orderedmap;
	}

	// i got annoyed at gson so i made this shitty method that works only for my
	// purpose and is ugly af but i was tired
	// might make this not disgusting later
	private List<String> parseJsonArray(String json) {

		if (json.length() == 0)
			return null;

		// trim so that no []
		json = json.substring(1, json.length() - 1);
		StringBuilder sb = new StringBuilder();
		// loop through string and replace commas between json objects with weird
		// character
		for (int i = 0; i < json.length(); i++) {

			if (json.charAt(i) == ',' && json.charAt(i - 1) == '}') {
				sb.append("ø");
			} else
				sb.append(json.charAt(i));

		}
		// make a list of the json objects separated by the weird character
		return Arrays.asList(sb.toString().split("ø"));
	}

	private String getNameHistory(String id) throws Exception {

		URL url = new URL("https://api.mojang.com/user/profile/" + id + "/names");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// if http ok
		if (con.getResponseCode() == 200) {

			// could probably be done in a nicer way but this is copied from stack overflow
			// dont sue me im tired
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			return content.toString();

		}
		con.disconnect();
		return null;
	}

	private String resolveUsername(String name) throws Exception {

		URL url = new URL("https://api.mojang.com/user/profile/agent/minecraft/name/" + name);

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// if http ok
		if (con.getResponseCode() == 200) {

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			if (content.length() >= 2) {
				con.disconnect();
				return content.substring(17 + name.length(), content.length() - 2);
			}

		}
		con.disconnect();
		return null;
	}

	private String cc(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
