package me.oqwe.namehistory.util;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Json {

	private static Gson gson = new Gson();
	
	public static Set<Entry<String, Long>> parseJson(String json) {
		
		Set<Entry<String, Long>> names = new LinkedHashSet<>();
		
		for (var element : gson.fromJson(json, JsonArray.class)) {
			if (element.isJsonObject()) {
				
				JsonObject object = (JsonObject) element;
				if (object.has("name")) {
					
					String name = object.get("name").getAsString().toString();
					name = name.substring(0, name.length());
					if (object.has("changedToAt"))
						names.add(Map.entry(name, object.get("changedToAt").getAsLong()));
					else
						names.add(Map.entry(name,0L));
					
				}
					
				
			}
		}
		
		return names;
		
	}
	
}
