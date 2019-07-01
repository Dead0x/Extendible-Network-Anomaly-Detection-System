package utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

public class Types 
{
	/* Converts from hex to binary, don't use BigInteger because it drops leading zeros */
	private static Map<String, String> binMap = new HashMap<>();
	private static Map<String, String> hexMap = new HashMap<>();
	
	public Types()
	{
		binMap.put("0", "0000");	binMap.put("1", "0001");	binMap.put("2", "0010");
		binMap.put("3", "0011");	binMap.put("4", "0100");	binMap.put("5", "0101");
		binMap.put("6", "0110");	binMap.put("7", "0111");	binMap.put("8", "1000");
		binMap.put("9", "1001");	binMap.put("A", "1010");	binMap.put("B", "1011");
		binMap.put("C", "1100");	binMap.put("D", "1101");	binMap.put("E", "1110");
		binMap.put("F", "1111");
		
		for (String k : binMap.keySet())
		{
			hexMap.put(binMap.get(k), k);
		}
	}
	
	// Convert a hex string to binary string
	public static String hexToBin(String s) 
	{	
		char[] hex = s.toCharArray(); 
	    String binaryString = "";
	    
	    for(char h : hex)
	    {
	        binaryString += binMap.get(String.valueOf(h));
	    }
	    	    
	    return binaryString;
	}
	
	// Convert a hex string to ASCII string
	public static String hexToAscii(String hex)
	{
		StringBuilder output = new StringBuilder();
		
	    for (int i = 0; i < hex.length(); i += 2) 
	    {
	        String str = hex.substring(i, i + 2);
	        output.append((char)Integer.parseInt(str, 16));
	    }
	    
	    return output.toString();
	}
	
	public static char[] hexToCharArr(String hex)
	{
		char[] output = new char[hex.length() / 2];
		int counter = 0;
		
	    for (int i = 0; i < hex.length(); i += 2) 
	    {
	        String str = hex.substring(i, i + 2);
	        output[counter] = (char)Integer.parseInt(str, 16);
	        counter++;
	    }
	    
	    return output;
	}
	
	public static String binToHex(String s)
	{
		if (s.length() % 4 != 0)
		{
			s = Utils.leftPad4(s, (int)(4 * Math.ceil((double) s.length() / 4.0))); // Pad out any single bit values
		}
		
		char[] bin = s.toCharArray();
	    String hexString = "";
	    	    
	    for (int i = 0; i < bin.length; i += 4)
	    {
	    	hexString += hexMap.get("" + bin[i] + bin[i+1] + bin[i+2] + bin[i+3]);
	    }
	    
	    return hexString;
	}
	
	public static String hexToIP(String hex)
	{
		try 
		{
			if (hex.length() == 8)
				return Inet4Address.getByAddress(DatatypeConverter.parseHexBinary(hex)).getHostAddress();
			else if (hex.length() == 32)
				return Inet6Address.getByAddress(DatatypeConverter.parseHexBinary(hex)).getHostAddress();
			else
				Utils.exit("Types.java", "IP length invalid in hexToIP");
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static int hexToNum(String hex)
	{
		return Integer.parseInt(hex, 16); 
	}
	
	// Convert a hex string to byte array
	public static byte[] hexToBytes(String s) 
	{
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    
	    for (int i = 0; i < len; i += 2) 
	    {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
	    }
	    
	    return data;
	}
	
	// Convert Hex string to MAC address format
	public static String hexToMac(String address)
	{
		StringBuilder sb = new StringBuilder(address);
				
		for(int i = 2; i < address.length( ) + (i / 3); i += 3)
		{
			sb.insert(i, ':');
		}
		
		return sb.toString();
	}
	
	public static String bytesToHex(byte[] bytes) 
	{
		char[] hexArray = "0123456789ABCDEF".toCharArray();
	    char[] hexChars = new char[bytes.length * 2];
	    
	    for ( int j = 0; j < bytes.length; j++ ) 
	    {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    
	    return new String(hexChars);
	}
	
	public static String numToHex(int num)
	{
		return Integer.toHexString(num);
	}
	
	public static String ipToHex(String ip)
	{
		String hex = "";
		String[] part = ip.split("[\\.,]");
		
		if (part.length < 4) 
		{
			return "00000000";
		}
		
		for (int i = 0; i < 4; i++) 
		{
			int decimal = Integer.parseInt(part[i]);
			if (decimal < 16) // Append a 0 to maintian 2 digits for every
			{
				hex += "0" + String.format("%01X", decimal);
			} else {
				hex += String.format("%01X", decimal);
			}
		}
		
		return hex;
	}
}
