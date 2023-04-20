package com.sulvic.sqfixer.handler;

public class LinkHandler{

	private static boolean asciiPrintable(char ch){ return ch >= 0x20 && ch < 0x7F; }

	private static String sanitize(String header){
		if(header == null) return null;
		String result = "";
		for(char ch: header.toCharArray()) result += asciiPrintable(ch)? ch: (char)0x2E;
		return result;
	}

	public static String makeSecure(String url){ return sanitize(url.replace("http://", "https://")); }

}
