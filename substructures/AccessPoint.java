package substructures;

import java.util.HashMap;

import utils.Utils;

public class AccessPoint extends Entity
{
	public static HashMap<String, AccessPoint> list = new HashMap<String, AccessPoint>();
	
	public String bssid = "";
	public String ssid = "";

	public AccessPoint(String bssid, String ssid)  
	{
		super(ENTITY.WIRELESSAP, bssid);
		this.setName(ssid);
		
		this.bssid = bssid;
		this.ssid = ssid; 
		
		if (list.keySet().contains(ssid))
		{
			list.get(ssid).packetCount++;
		}
		else
		{
			list.put(ssid, this);
		}
	}
	
	public static void print()
	{
		Utils.printDivider("Access Point List");
		for (String s : AccessPoint.list.keySet())
		{
			System.out.println(AccessPoint.list.get(s).ssid + ": " + AccessPoint.list.get(s).packetCount);
		}
	}

}
