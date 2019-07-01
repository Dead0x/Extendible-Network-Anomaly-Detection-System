package config;

import engine.NetHandler;
import engine.NetHandler.STATUS;
import utils.OS;
import utils.Types;
import utils.Utils;

public class Start 
{
	/*
	 * Package Structure: 
	 * 		config : Startup and settings
	 * 		data : External resources
	 * 		engine : JNI interface for working with libcap in C
	 * 		frames : specification for each protocol
	 * 		natives : the C library for working with libpcap
	 * 		utils : various functions for doing stuff
	 * 
	 *  Frame Structurre:
	 *  	Frame is the top level class for every captured frame.
	 *  	For each sub-protocol a class is initialised and frame passed to it.
	 *  	Frame is put inside of a packet class from which the protocol class extends.
	 * */
	
	public static void main(String[] args) throws InterruptedException 
	{
		new Types(); // Load the types util class
		new OS();
				
		System.out.println(Utils.repeat(400, "-"));
		System.out.println("Operating System: " + OS.osVersion);
		System.out.println("Working Directory: " + OS.workingDirectory);
		System.out.println("User is Root: " + OS.isRoot);
		System.out.println("Endianness: " + OS.getEndianness());
		System.out.println(Utils.repeat(400, "-"));
		
		//Attacks.spoofARP();
		
		new NetHandler("en0", STATUS.ETHER_PROMISC_ON, 100).start();
		
//		Net net = new Net("en0");
//		net.hopper.addChannelSet(CHANNELS.LOW_CHANNELS);
//		net.hopper.addChannelSet(CHANNELS.GHZ_24_CHANNELS);
//		net.hopper.addChannelSet(CHANNELS.GHZ_5_CHANNELS);
//		net.hopper.addChannelSet(CHANNELS.UNII1_CHANNELS);
//		net.hopper.start();
	}

}
