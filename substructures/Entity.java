package substructures;

import java.util.HashMap;

import utils.Utils;

public class Entity 
{
	public ENTITY type = null;
	public String source = null;
	public String name = null;
	
	public int packetCount = 0;
	
	public static HashMap<String, Entity> list = new HashMap<String, Entity>();
	
	public Entity(ENTITY type, String source) 
	{
		this.type = type;
		this.source = source;
		
		if (list.keySet().contains(source))
		{
			list.get(source).packetCount++;
		}
		else
		{
			list.put(source, this);
		}
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public enum ENTITY
	{
		WIRELESSAP,
		HOSTIP,
		URL,
		NETFLOW,
		HOSTDEVICE
	}
	
	public static void print()
	{
		Utils.printDivider("Entities List");
		for (String s : list.keySet())
		{
			System.out.println("[" + list.get(s).type + "] " + list.get(s).source + " (" + list.get(s).name + "): " + list.get(s).packetCount);
		}
	}

}
