package com.sulvic.sqfixer.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sulvic.sqfixer.SpiderQueenFixer;

import radixcore.util.RadixExcept;
import sq.core.SpiderCore;

public class PlayerNamesHelper{

	private static final String PROTOCOL = "http://";
	private static final String SECURE_PROTOCOL = "https://";

	private static boolean hasProtocol(String url){ return url.contains(PROTOCOL) || hasSecureProtocol(url); }

	private static boolean hasSecureProtocol(String url){ return url.contains(SECURE_PROTOCOL); }

	private static String sanitizeLink(String url){
		if(url == null) return null;
		return url.replaceAll("[^\\x20-\\x7E]", ".");
	}

	private static String secureLink(String url){ return sanitizeLink(hasProtocol(url)? hasSecureProtocol(url)? url: url.replace(PROTOCOL, SECURE_PROTOCOL): SECURE_PROTOCOL + url); }

	private static HttpURLConnection findContent(String link, String cookies) throws IOException{
		URL url = new URL(secureLink(link));
		InetAddress netAddr = InetAddress.getByName(url.getHost());
		if(netAddr.isAnyLocalAddress() || netAddr.isLoopbackAddress() || netAddr.isLinkLocalAddress())
			throw new IOException("The given link is either a local address or a loopback address");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		if(cookies != null && !cookies.equals("")) conn.addRequestProperty("Cookie", cookies);
		conn.connect();
		int status = conn.getResponseCode();
		boolean redir = false;
		if(status != HttpURLConnection.HTTP_OK)
			redir = status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER;
		if(redir){
			String newLink = conn.getHeaderField("Location"), cookies1 = conn.getHeaderField("Set-Cookie");
			conn.disconnect();
			conn = findContent(newLink, cookies1);
		}
		return conn;
	}

	private static void readSkinsFromURL(String stringUrl, List<String> returnList) throws IOException{
		HttpURLConnection conn = findContent(stringUrl, null);
		if(conn != null){
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = reader.readLine()) != null) returnList.add(line);
			reader.close();
			conn.disconnect();
		}
		SpiderQueenFixer.getLogger().info("Found data with link \"{}\"", conn.getURL());
	}

	public static List<String> downloadFakePlayerNames(){
		SpiderQueenFixer.getLogger().info("Downloading contributor/volunteered player names with redirections...");
		List<String> returnList = new ArrayList<String>();
		try{
			readSkinsFromURL(SpiderCore.PERM_SKINS_URL, returnList);
			readSkinsFromURL(SpiderCore.SKINS_URL, returnList);
			SpiderQueenFixer.getLogger().info("Contributor/volunteer player names downloaded successfully!");
		}
		catch(Exception ex){
			RadixExcept.logErrorCatch(ex, "Unable to download player names.");
		}
		return returnList;
	}

}
