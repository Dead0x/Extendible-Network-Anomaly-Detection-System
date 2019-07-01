package utils;

import java.util.zip.CRC32;

public class Parser 
{
	public String hex = null;
	public String binary = null;
	
	public boolean parseErr = false;
	private boolean frameBuilder = false;
	
	public int offset = 0;
	public int hexLen = 0;
	public int binLen = 0;
	private int crcIndex = 0;
	
	public Parser(String hex) 
	{
		// For Building frames manually
		if (hex == null)
		{
			frameBuilder = true;
		}
		else
		{
			this.hex = hex;
			this.binary = Types.hexToBin(hex);
			
			hexLen = hex.length();
			binLen = binary.length();
		}
	}
	
	public void printHex()
	{
		System.out.println("Raw: " + hex);
		System.out.println(Utils.repeat((offset / 4) + 5, " ") + "^");
	}
	
	public void printBin()
	{
		System.out.println(binary);
	}
	
	public void initialiseCRC()
	{
		crcIndex = offset;
	}
	
	/** Performs the Cyclic redundancy check.
	 *  Prerequisite: must call crcInit() at the start of the frame
	 *  Input: The CRC from the frame
	 *  Process: Substrings the binary string using the CRC index - the 32 bit CRC value
	 *  Returns: True if matches, else false   */
	public boolean checkCRC32(String crc)
	{
		CRC32 crc32 = new CRC32();
		String substr = "";
		
		try {
			substr = binary.substring(crcIndex, binLen - 32);
		} catch (StringIndexOutOfBoundsException e) {
			this.parseErr = true;
			System.out.println(e);
			return false;
		}
		
		crc32.update(Types.hexToBytes(Types.binToHex(substr)));
		String calculatedCRC = String.format("%X", crc32.getValue());
				
		if (crc.equals(calculatedCRC))
		{
			return true;
		}
		else
		{
			this.parseErr = true;
			return false;
		}
	}
	
	public String lastBits(int n)
	{
		String substr = "";
			
		try {
			substr = binary.substring((binLen - n), binLen);
		} catch (StringIndexOutOfBoundsException e) {
			this.parseErr = true;
			System.out.println(e);
			return "ERROR";
		}
		
		return Types.binToHex(substr);
	}
	
	public String lastBytes(int n)
	{
		return lastBits(n * 8);
	}
	
	/** Returns the next # of bytes as hex */
	public Field nextBits(int n)
	{
		int numBits = n;
		int newOffset = offset + numBits;
		String substr = "";
		
		try {
			substr = binary.substring(offset, newOffset);
		} catch (StringIndexOutOfBoundsException e) {
			this.parseErr = true;
			substr = "0000";
		}
		
		offset = newOffset;
		
		return new Field(Types.binToHex(substr), n);
	}
	
	public String range(int start, int end)
	{
		String substr = "";
			
		try {
			substr = binary.substring(start, end);
		} catch (StringIndexOutOfBoundsException e) 
		{
			System.out.println(e);
			System.exit(0);
		}
				
		return substr;
	}
	
	/** Returns the next 8 bits as hex */
	public Field nextBytes(int n)
	{
		return nextBits(n * 8);
	}
}
