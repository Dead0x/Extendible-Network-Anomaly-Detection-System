package substructures;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Utils;

public class URL extends Entity
{	
	public static HashMap<String, URL> list = new HashMap<String, URL>();
	public ArrayList<String> subURLs = new ArrayList<String>();
	
	public URL(String host, String fullURL) 
	{
		super(ENTITY.URL, host);
				
		if (list.keySet().contains(host))
		{
			list.get(host).subURLs.add(fullURL);
		}
		else
		{
			this.subURLs.add(fullURL);
			list.put(host, this);
		}
	}
	
	public static void extractUrls(String text)
	{
	    String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
	    Matcher urlMatcher = pattern.matcher(text);

	    while (urlMatcher.find())
	    {
	        String url = text.substring(urlMatcher.start(0), urlMatcher.end(0));
	        
	        try 
	        {
				java.net.URL urlParser = new java.net.URL(url);
				new URL(urlParser.getHost(), url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public static void print()
	{
		Utils.printDivider("URL List");
		
		for (String s : list.keySet())
		{
			System.out.println(list.get(s).name + ": " + list.get(s).subURLs.size());
		}
	}	

}
