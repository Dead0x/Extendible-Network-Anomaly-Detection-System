package frames;

import engine.Frame;
import substructures.Entity;
import substructures.Entity.ENTITY;
import utils.Packet;
import utils.Field.DTYPE;

public class ARP extends Packet
{
	public static final String HWTYPE_ARP = "0806";
	public static final String OP_REQUEST = "0001";
	public static final String OP_REPLY = "0002";
	
	public ARP(Frame frame) 
	{
		super(frame, PROTOCOL.ARP, ENDIANNESS.LITTLE);
		
		parseBytes("hardwareType", 2, DTYPE.NUM);
		parseBytes("protocolType", 2, DTYPE.NUM);
		
		parseBytes("hardwareAddressLen", 1, DTYPE.NUM);
		parseBytes("protocolAddressLen", 1, DTYPE.NUM);
		
		parseBytes("operation", 2, DTYPE.NUM);
		
		parseBytes("senderMAC", field("hardwareAddressLen").num(), DTYPE.MAC);
		parseBytes("senderProtoAddress", field("protocolAddressLen").num(), DTYPE.IP);
		
		parseBytes("targetMAC", field("hardwareAddressLen").num(), DTYPE.MAC);
		parseBytes("targetProtoAddress", field("protocolAddressLen").num(), DTYPE.IP);
		
		new Entity(ENTITY.HOSTDEVICE, field("senderMAC").mac());
		new Entity(ENTITY.HOSTDEVICE, field("targetMAC").mac());
		new Entity(ENTITY.HOSTDEVICE, field("senderProtoAddress").ip());
		new Entity(ENTITY.HOSTDEVICE, field("targetProtoAddress").ip());
		
		printHeader();
	}
}
