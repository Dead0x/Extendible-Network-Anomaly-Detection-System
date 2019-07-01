package utils;

public class Field 
{
	private String name = "";
	private String hex = "";
	private String bin = "";
	private DTYPE type = null;
	public int sizeBits = -1;
	
	public Field(String hex, int size) 
	{
		this.hex = hex;
		this.sizeBits = size;		
		
		String binary = Types.hexToBin(hex);
		this.bin = binary.substring(binary.length() - size);
	}
	
	public void setFieldName(String s)
	{
		this.name = s;
	}
	
	public void setDataType(DTYPE d)
	{
		this.type = d;
	}
	
	public enum DTYPE
	{
		HEX, NUM, IP, MAC, ASCII, HIDDEN
	}
	
	public int num()
	{
		return Types.hexToNum(hex);
	}
	
	public String bin()
	{
		return bin;
	}
	
	public String ip()
	{
		return Types.hexToIP(hex);
	}
	
	public String mac()
	{
		return Types.hexToMac(hex);
	}
	
	public DTYPE type()
	{
		return type;
	}
	
	public String hex()
	{
		return hex;
	}
	
	public String ascii()
	{
		return Types.hexToAscii(hex);
	}
	
	public void flip()
	{
		hex = Utils.flipBytes(hex);
	}
	
	public String castedString()
	{
		if (type == DTYPE.IP)
			return ip();
		else if (type == DTYPE.MAC)
			return mac();
		else if (type == DTYPE.NUM)
			return String.valueOf(num());
		else if (type == DTYPE.ASCII)
			return ascii();
		else if (type == DTYPE.HIDDEN)
			return "###HIDDEN###";
		else
			return hex();
		
	}
	
	public void appendHex(String hex)
	{
		this.hex += hex;
	}

}
