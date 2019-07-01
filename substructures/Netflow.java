package substructures;

import java.util.ArrayList;

import utils.Field;
import utils.Utils;

public class Netflow extends Entity
{
	public int numPackets = 1;
	public boolean newFlow = true;
	
	public String source = null;
	public String destination = null;
	public int protocol = -1;
	public int sourcePort = -1;
	public int destinationPort = -1;
	public Field data = null;
	
	public static ArrayList<Netflow> list = new ArrayList<Netflow>();
	
	public Netflow(String srcIP, String dstIP, int proto, int srcPort, int dstPort, String data) 
	{
		super(ENTITY.NETFLOW, srcIP);
		setName(String.valueOf(srcPort));
		
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).source.equals(srcIP) && list.get(i).destination.equals(dstIP) && list.get(i).protocol == proto && list.get(i).sourcePort == srcPort && list.get(i).destinationPort == dstPort)
			{
				newFlow = false;
				list.get(i).data.appendHex(data);
				list.get(i).numPackets++;
			}
		}
		
		if (this.newFlow == true)
		{
			this.source = srcIP;
			this.destination = dstIP;
			this.protocol = proto;
			this.sourcePort = srcPort;
			this.destinationPort = dstPort;
			this.data = new Field(data, data.length() * 4);
			
			list.add(this);
		}
	}
	
	public static void print()
	{
		Utils.printDivider("Netflow List");
		
		for (Netflow nf : list)
		{
			System.out.println(nf.source + " (" + nf.sourcePort + ") > " + nf.destination + " (" + nf.destinationPort + ") [" + nf.protocol + "]: " + nf.numPackets);
		}
	}	
}
