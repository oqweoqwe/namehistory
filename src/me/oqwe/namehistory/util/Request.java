package me.oqwe.namehistory.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.command.CommandSender;

import me.oqwe.namehistory.Main;

public class Request {

	public static String sendGetIdRequest(String name, CommandSender sender) throws Exception {

		URL url = new URL("https://api.mojang.com/user/profile/agent/minecraft/name/" + name);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		// if http ok
		if (connection.getResponseCode() == 200) {

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			if (content.length() >= 2) {
				connection.disconnect();
				return content.substring(17 + name.length(), content.length() - 2);

			}

		}

		// if too many requests
		if (connection.getResponseCode() == 429) {
			connection.disconnect();
			sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("too-many-requests")));
			return null;
		}
		connection.disconnect();
		return null;
	}
	
	public static String sendGetIdRequest(String name) throws Exception {

		URL url = new URL("https://api.mojang.com/user/profile/agent/minecraft/name/" + name);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		// if http ok
		if (connection.getResponseCode() == 200) {

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			if (content.length() >= 2) {
				connection.disconnect();
				return content.substring(17 + name.length(), content.length() - 2);

			}

		}
		connection.disconnect();
		return null;
	}

	public static String sendGetHistoryRequest(String id, CommandSender sender) throws Exception {

		URL url = new URL("https://api.mojang.com/user/profile/" + id + "/names");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		// if http ok
		if (connection.getResponseCode() == 200) {

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			connection.disconnect();
			sender.sendMessage("dwa222");
			return content.toString();

		}

		// if too many requests
		if (connection.getResponseCode() == 429) {
			connection.disconnect();
			sender.sendMessage(Chat.cc(Main.getInstance().getConfig().getString("too-many-requests")));
			return null;
		}
		connection.disconnect();
		return null;
	}

}
