package engine;

import java.util.ArrayList;

import frames.ARP;
import frames.DNS;
import frames.ICMP;
import frames.ICMPv6;
import frames.IEEE_802_11_mgmt_beacon;
import frames.IEEE_802_11_radiotap_header;
import frames.IEEE_802_3_ethernet_header;
import frames.IPv4;
import frames.IPv6;
import frames.TCP;
import frames.UDP;
import substructures.AccessPoint;
import substructures.Entity;
import substructures.Netflow;
import utils.Packet;
import utils.Parser;
import utils.Utils;

/* 1: https://github.com/the-tcpdump-group/libpcap/blob/master/pcap/dlt.h */
public class Frame
{
	public static int frameCount = 0;
	public static int errParsing = 0;
	public static int errCRC = 0;
	public static boolean print = true;
	
	public int dlt = -1;
	public Parser parse = null;
	
	public ArrayList<Packet> frameList = new ArrayList<Packet>();
		
	public Frame(String dlt, String hex) 
	{			
		Frame.frameCount++;
		parse = new Parser(hex);
				
		if (Frame.print)
		{
			Utils.printDivider(String.valueOf(Frame.frameCount));
			parse.printHex();
		}
	
		dltSwitch(Integer.valueOf(dlt));
		
		if (parse.parseErr == true)
		{
			errParsing++;
		}
	}
	
	// See [1] for DLT numbers
	public void dltSwitch(int dlt)
	{
		if (Frame.print)
			System.out.println("[+] DLT: " + dlt);
		
		switch(dlt)
		{
			case 1:
				frameList.add(new IEEE_802_3_ethernet_header(this));
				break;
			case 127:
				frameList.add(new IEEE_802_11_radiotap_header(this));
				frameList.add(new IEEE_802_11_mgmt_beacon(this));
				break;
		}
	}
	
	public void etherTypeSwitch(int etherType)
	{		
		switch (etherType)
		{
			case 2048:
				frameList.add(new IPv4(this));
				break;
			case 2054:
				frameList.add(new ARP(this));
				break;
			case 34525:
				frameList.add(new IPv6(this));
				break;
		}
	}
	
	public void ipProtoSwitch(int proto)
	{		
		switch (proto)
		{
			case 1:
				frameList.add(new ICMP(this));
				break;
			case 6:
				frameList.add(new TCP(this));
				break;
			case 17:
				frameList.add(new UDP(this));
				break;
			case 58:
				frameList.add(new ICMPv6(this));
				break;
		}
	}
	
	public void portSwitch(int port)
	{
		switch (port)
		{
			case 53:
				frameList.add(new DNS(this));
				break;
		}
	}
	
	public static void printCaptureSummary()
	{
		Entity.print();
		AccessPoint.print();
		Netflow.print();
		
		Utils.printDivider("Capture Summary");
		System.out.println("    | Parsing Errors: " + Frame.errParsing);
		System.out.println("    | CRC Errors: " + Frame.errCRC);
		System.out.println("    | Packet Count: " + Frame.frameCount);
		System.out.println("    | Entities: " + Entity.list.keySet().size());
		System.out.println("    | Access Points: " + AccessPoint.list.keySet().size());
		System.out.println("    | Netflows: " + Netflow.list.size());
	}
}
