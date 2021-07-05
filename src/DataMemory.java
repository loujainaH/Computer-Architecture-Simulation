import java.util.Vector;

public class DataMemory{

byte[] dataMemory;

	public DataMemory() {
		this.dataMemory = new byte[2048];
	}

	public byte getData(int address) {
		return (byte) dataMemory[address];
	}
	
	

	public void addDataMemory(int address,byte data) {
			dataMemory[address]=data;		
		
	}
}
