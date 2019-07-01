package utils;

import java.math.BigInteger;

public class Utils 
{
	public static final boolean PRINT = true;
	
	public static String repeat(int i, String s) 
	{
	    StringBuilder sb = new StringBuilder();
	    
	    for (int j = 0; j < i; j++)
    	{ 
    		sb.append(s);
    	}
	    
	    return sb.toString();
	}

	public static String leftPad4(String s, int amt) 
	{
		BigInteger num = new BigInteger(s);
		return String.format("%0" + amt + "d", num);
	}

	public static String removeAlphabetic(String s)
	{
		if (s != null)
			return s.replaceAll("[^\\d]", "" );
		else
			return "null";
	}
	
	public static void exit(String file, String err)
	{
		System.out.printf("[ERR in %s] $s", file, err);
		System.exit(0);
	}
	
	public static String flipBytes(String hex)
	{
		String reorderedBits = "";
		
		for (int i = hex.length(); i > 0; i -= 2)
		{
			reorderedBits += hex.substring(i - 2, i);
		}
		
		return reorderedBits;
	}
	
	public static void printDivider(String s)
	{
		System.out.print("-" + s);
		System.out.println(Utils.repeat(400, "-"));
	}
}
