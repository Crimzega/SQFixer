package com.sulvic.sqfixer.client;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sulvic.sqfixer.SpiderQueenFixer;

import sq.core.SpiderCore;

public class PlayerInfoStorage{
	
	private static final Map<String, UUID> NAME_TO_UUID = Maps.newHashMap();
	private static final Map<UUID, GameProfile> CURRENT_PROFILE = Maps.newHashMap();
	
	private static boolean hasId(String name){ return getIdFromOldName(name) != null; }
	
	private static UUID getIdFromOldName(String name){ return NAME_TO_UUID.get(getStoredName(name)); }
	
	private static String getStoredName(String name){ return name.equals("LuvTrumpetStyle")? "SheWolfDeadly": name; }
	
	public static void init(){
		// PERM_SKINS_UUID
		NAME_TO_UUID.put("djoslin", (UUID)null);
		NAME_TO_UUID.put("Lemonszz", UUID.fromString("0b33b89f-042b-4255-9322-131c660a568a"));
		NAME_TO_UUID.put("WildBamaBoy", UUID.fromString("a690e673-7acf-48d5-b7df-4bb3f86d6b1c"));
		NAME_TO_UUID.put("SheWolfDeadly", UUID.fromString("4384cd57-9235-4f36-814d-635d45f28297"));
		NAME_TO_UUID.put("Pullahoko", UUID.fromString("ca689a6d-58dc-4770-a46b-fca2a772055f"));
		NAME_TO_UUID.put("mezzodrinker", UUID.fromString("60fcf0a7-978d-4da2-a1fb-4db0ee9e18cc"));
		NAME_TO_UUID.put("Shadow_Darkrai", UUID.fromString("39b3e7b5-9906-4b70-8a1c-8982b8d583c1"));
		NAME_TO_UUID.put("LexiLouxoxo", UUID.fromString("a3fbf455-e16e-40bb-b1ee-96318a01e13d"));
		NAME_TO_UUID.put("mario384", UUID.fromString("03e568d5-7ab1-4405-ae82-0ffc48d337fd"));
		NAME_TO_UUID.put("TBone105", UUID.fromString("e93c0370-c378-41bb-a726-80066ab23b87"));
		NAME_TO_UUID.put("scottnov", UUID.fromString("1bf55188-e635-4c82-a3d8-335f5fb0f078"));
		NAME_TO_UUID.put("AllenWL", (UUID)null);
		NAME_TO_UUID.put("Xiari", UUID.fromString("fcd8f5fd-4986-411f-a14c-7acba169e781"));
		// SKINS_UUID
		NAME_TO_UUID.put("CakeKittyCat", UUID.fromString("cfca7cfd-8383-4472-a0e4-afe749a72658"));
		NAME_TO_UUID.put("metagross534", UUID.fromString("74122baa-acb0-4d28-b7d4-5bf9f45b8f2c"));
		NAME_TO_UUID.put("thehexbugofwater", UUID.fromString("605eb1aa-2520-4dbe-a830-ac41b81689db"));
		NAME_TO_UUID.put("eggsforeva381", UUID.fromString("207642d9-6db9-4709-a5e8-7b520168189c"));
		NAME_TO_UUID.put("unknow11", UUID.fromString("4ac264b0-561b-4fee-9876-46f7024e3d1d"));
		NAME_TO_UUID.put("Yelf42", UUID.fromString("102ac60f-bebb-41dc-8a68-9fd9695fd6f1"));
		NAME_TO_UUID.put("InfamousArgyle", UUID.fromString("008d77ad-cbd8-4741-8981-bf1a0c6801ed"));
		NAME_TO_UUID.put("Cheetahsgrowl123", UUID.fromString("6cd4ce2f-5ac8-404e-b26c-63f201a3117f"));
		NAME_TO_UUID.put("Ethan_Unken", UUID.fromString("5794d4db-a50e-4bc9-9e8d-606ee093ef26"));
		NAME_TO_UUID.put("Epar86", UUID.fromString("cec6e849-8460-43e1-894e-89605e1fc379"));
		NAME_TO_UUID.put("TheMelonSquid", UUID.fromString("1bb224c3-bafd-43f8-a3e1-fc0a2c116c0f"));
		NAME_TO_UUID.put("DaSkowt", UUID.fromString("63c30483-3b97-4f0e-9e66-f6c88eb8fff7"));
		NAME_TO_UUID.put("veganinja", UUID.fromString("f223d87c-0273-4555-bda5-84327d35842f"));
		NAME_TO_UUID.put("RyanTheCactus", UUID.fromString("9a807e83-15db-4267-9940-cdda7cb696fd"));
		NAME_TO_UUID.put("creepermanager", UUID.fromString("34b5fd2b-f1f5-4cde-9f80-7915ec12b2a7"));
		NAME_TO_UUID.put("Ittonator97", UUID.fromString("ff9575f4-2fc9-437d-b426-5c32a9d2e3b3"));
		NAME_TO_UUID.put("goanna678", UUID.fromString("e04105e6-3e69-43c7-987c-638995876894"));
		NAME_TO_UUID.put("skymaner377", UUID.fromString("6ba009c9-2ad0-4b57-9946-e8d1265d04eb"));
		NAME_TO_UUID.put("Jefferytolmie14", UUID.fromString("62a9b8f2-852d-4022-a42f-9a4a8a466988"));
		NAME_TO_UUID.put("Jaacfredie", UUID.fromString("e4cc1c1b-9079-4338-8f41-45d69c42ac27"));
		NAME_TO_UUID.put("Theamericanpilot", UUID.fromString("a579c9eb-78e6-4964-9271-5c58d8b8fca7"));
		NAME_TO_UUID.put("Khaamat", UUID.fromString("9223a4cf-4ace-44f9-9bca-b16d5d70d8d0"));
		NAME_TO_UUID.put("Danielblom", UUID.fromString("1023f4a8-2b43-41b9-86cd-c3ca9a3c83d6"));
	}
	
	public static void populate(){
		for(String origName: NAME_TO_UUID.keySet()){
			Gson gson = new GsonBuilder().registerTypeAdapter(GameProfile.class, new ProfileDeserializer()).create();
			URL url;
			try{
				UUID id = NAME_TO_UUID.get(origName);
				if(id != null){
					url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString());
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					GameProfile profile = gson.fromJson(new InputStreamReader(conn.getInputStream()), GameProfile.class);
					CURRENT_PROFILE.put(id, profile);
				}
			}
			catch(IOException ex){
				SpiderQueenFixer.getLogger().catching(ex);
			}
		}
	}
	
	public static void applyConfigData(){
		if(SpiderQueenFixer.getConfig().addCreatorToPlayerList()){
				String name = "Crimzega";
				SpiderCore.fakePlayerNames.add(name);
				UUID id = UUID.fromString("7db16c19-6e67-4931-afb8-78db3f640d3c");
				NAME_TO_UUID.put(name, UUID.fromString("7db16c19-6e67-4931-afb8-78db3f640d3c"));
				try{Gson gson = new GsonBuilder().registerTypeAdapter(GameProfile.class, new ProfileDeserializer()).create();
				URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString());
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				GameProfile profile = gson.fromJson(new InputStreamReader(conn.getInputStream()), GameProfile.class);
				CURRENT_PROFILE.put(id, profile);
			}
			catch(IOException ex){
				SpiderQueenFixer.getLogger().catching(ex);
			}
		}
		else{
			String name = "Crimzega";
			SpiderCore.fakePlayerNames.remove("Crimzega");
			UUID id = NAME_TO_UUID.get(name);
			CURRENT_PROFILE.remove(id);
			NAME_TO_UUID.remove(name);
		}
	}
	
	public static GameProfile getProfileFromOldName(String name){ return hasId(name)? CURRENT_PROFILE.get(getIdFromOldName(name)): (GameProfile)null; }
	
	private static class ProfileDeserializer implements JsonDeserializer<GameProfile>{
		
		public GameProfile deserialize(JsonElement jsonElem, Type type, JsonDeserializationContext context) throws JsonParseException{
			JsonObject jsonObj = jsonElem.getAsJsonObject();
			String id = jsonObj.get("id").getAsString().replaceAll("([\\da-f]{8})([\\da-f]{4})([\\da-f]{4})([\\da-f]{4})([\\da-f]{12})", "$1-$2-$3-$4-$5");
			GameProfile profile = new GameProfile(UUID.fromString(id), jsonObj.get("name").getAsString());
			JsonArray jsonArr = jsonObj.get("properties").getAsJsonArray();
			for(int i = 0; i < jsonArr.size(); i++){
				JsonObject jsonObj1 = jsonArr.get(i).getAsJsonObject();
				String name = jsonObj1.get("name").getAsString();
				Property property = new Property(name, jsonObj1.get("value").getAsString());
				profile.getProperties().put(name, property);
			}
			return profile;
		}
		
	}
	
}
