package frames;

import java.util.zip.CRC32;

import frames.Specification.ENDIANNESS;
import frames.Specification.NAME;
import substructures.AccessPoint;
import utils.Parser;
import utils.Types;

public class IEEE_802_11_mgmt_beacon 
{
	public static Specification spec = new Specification();
	
	public String frameControl = "";
	
	public String version = "";
	public String packType = "";
	public String subType = "";
	public String toDS = "";
	public String fromDS = "";
	public String moreFrag = "";
	public String retry = "";
	public String powerManagement = ""; 
	public String moreData = "";
	public String protectedFrame = "";
	public String order = "";
	
	public String duration = "";
	public String seqCtrl = "";
	
	public String destMac = "";
	public String sourceMac = "";
	public String bssid = "";
	public String transmitterMac = "";
	public String receiverMac = "";
	
	public String timestamp = "";
	public String beaconInterval = "";
	public String capabilityInformation = "";
	
	public String ssid = "";
	
	public String fcs = "";
	
	public IEEE_802_11_mgmt_beacon(Frame frame) 
	{	
		int frameOff = frame.parse.offset;
		
		/* Frame Control field (2 bytes) */
		frameControl = frame.parse.nextBytes(2);
		Parser frameControlParser = new Parser(frameControl);
		
		// B0 - B1 : REVERSED ENDIANNESS
		subType = frameControlParser.nextBits(4);
		packType = frameControlParser.nextBits(2);
		version = frameControlParser.nextBits(2);
		
		// B1 - B2 : REVERSED ENDIANNESS
		order = frameControlParser.nextBits(1);
		protectedFrame = frameControlParser.nextBits(1);
		moreData = frameControlParser.nextBits(1);
		powerManagement = frameControlParser.nextBits(1);
		retry = frameControlParser.nextBits(1);
		moreFrag = frameControlParser.nextBits(1);
		fromDS = frameControlParser.nextBits(1);
		toDS = frameControlParser.nextBits(1);
		
		// B2 - B26
		duration = frame.parse.nextBytes(2);
		String addr1 = Types.formatMAC(frame.parse.nextBytes(6));
		String addr2 = Types.formatMAC(frame.parse.nextBytes(6));
		String addr3 = Types.formatMAC(frame.parse.nextBytes(6));
		String addr4 = "";
		seqCtrl = frame.parse.nextBytes(2);
		
		if (toDS.equals("1") && fromDS.equals("1"))
			addr4 = Types.formatMAC(frame.parse.nextBytes(6));
		
		sortMacAddresses(addr1, addr2, addr3, addr4);
				
		timestamp = frame.parse.nextBytes(8);
		beaconInterval = frame.parse.nextBytes(2);
		capabilityInformation = frame.parse.nextBytes(2);
		
		// Tags in frame body section
		String ssidTag = frame.parse.nextBytes(1);
		int ssidLen = Types.hexToInt(frame.parse.nextBytes(1));
		ssid = Types.hexToAscii(frame.parse.nextBytes(ssidLen));
		
		fcs = frame.parse.lastBytes(4);
		
		if (ssid.length() > 32)
			frame.parse.parseErr = true;
		
		if (frame.parse.parseErr == false)
			new AccessPoint(bssid, ssid);
		
		CRC32 crc32 = new CRC32();
		crc32.update(Types.hexToBytes(frame.parse.lastBytes((frame.parse.binLen - frameOff))));
		System.out.printf("%X\n", crc32.getValue());
		
		print();
	}
	
	public void print()
	{
		if (Frame.print)
		{
			System.out.printf("MGMT Beacon \n");
			System.out.printf("    | [Version: %s][Type: %s][Subtype: %s]\n", version, packType, subType);
			System.out.printf("    | [Duration: %s][DST MAC: %s][SRC MAC: %s][BSSID: %s][Seq CTRL: %s]\n", duration, destMac, sourceMac, bssid, seqCtrl);
			System.out.printf("    | [SSID: %s][To DS: %s][From DS: %s][FCS: %S]\n", ssid, toDS, fromDS, fcs);
		}
	}
	
	public static void spec()
	{
		spec.setEndianess(ENDIANNESS.mixed_byte_boundaries);
		spec.setName(NAME.ieee_802_11_mgmt_beacon);
		spec.setInfo("While the bit ordering within each individual data field is big-endian, the fields themselves are transmitted in reverse order, within the byte-boundaries.");
		spec.setLinks(1, "https://www.edn.com/Home/PrintView?contentItemId=4375340");
		spec.setLinks(2, "https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=7786995");
		spec.setLinks(3, "http://www.studioreti.it/slide/802-11-Frame_E_C.pdf");
		spec.setLinks(4, "http://80211notes.blogspot.com/2013/09/understanding-address-fields-in-80211.html");
	}
	
	/* Sort out Mac addresses into their correct order */
	public void sortMacAddresses(String addr1, String addr2, String addr3, String addr4)
	{
		if (toDS.equals("0") && fromDS.equals("0"))
		{
			this.destMac = addr1;
			this.sourceMac = addr2;
			this.bssid = addr3;
		}
		else if (toDS.equals("1") && fromDS.equals("0"))
		{
			this.bssid = addr1;
			this.sourceMac = addr2;
			this.destMac = addr3;
		}
		else if (toDS.equals("0") && fromDS.equals("1"))
		{
			this.destMac = addr1;
			this.bssid = addr2;
			this.sourceMac = addr3;
		}
		else if (toDS.equals("1") && fromDS.equals("1"))
		{
			this.receiverMac = addr1;
			this.transmitterMac = addr2;
			this.destMac = addr3;
			this.sourceMac = addr4;
		}
	}

}
