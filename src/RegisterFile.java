import java.util.HashMap;

public class RegisterFile extends HashMap{

	

	public RegisterFile() {
		
		short tmp = 0; //16
		byte tmp2 = 0; //8
		
		for(int i =0; i<64; i++) {
			this.put("R" + i, tmp2);
		}
		this.put("SREG", "00000000");
		this.put("PC", tmp); //PC
	}

}
