import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

//TEST:
//R1 = 8, R2 = 3, R3 = 1 
//ldi r1 5
//ldi r2 3
//ldi r3 1
//add r1 r2 #8
//sub r1 r3 #7
//sb r1 0
//lw r5 0

public class CA {
	DataMemory dm;
	InstructionsMemory im;
	RegisterFile rf;

	public CA() {
		this.dm = new DataMemory();
		this.im = new InstructionsMemory();
		this.rf = new RegisterFile();

	}

	public static void main(String[] args) throws IOException {
		CA ca = new CA();
		short oldPC = 0;
		short newPC;
		short diffPC;


		// Comment and Uncomment to test programs :)
		
//		int[] neededClk = ca.createProgram("program3.txt");
//		int[] neededClk = ca.createProgram("program2.txt");
//		int[] neededClk = ca.createProgram("beqzProgram.txt");
//		int[] created= ca.createProgram("jumpProgram.txt");
//		int[] created= ca.createProgram("jumpBack.txt");
//		int[] created= ca.createProgram("negativeNumber.txt");
//		int[] created= ca.createProgram("beqzFails.txt");
//		int[] created= ca.createProgram("lastBEQZ.txt");
		int[] created= ca.createProgram("7agaKbeera.txt");
		
		
		int total= created[1];
		int neededClk=created[0];
		int clk = 0;
		String fetched = "";
		Vector decoded = null;

		// check for control hazards
		for (int i = 0; i < neededClk; i++) {

			clk++;
			
			System.out.println("Current Cycle is: " + (clk));
			
			if (i == 0) {
				fetched = ca.fetch();
			}
			
			// START
			else if (i == 1) {
				decoded = ca.decode(fetched);
				fetched = ca.fetch();

			} else if (i == neededClk - 2) { // before last instr

				oldPC = (short) ca.rf.get("PC");
				ca.execute(decoded);
				
				newPC = (short) ca.rf.get("PC");
				
				if ((decoded.get(1).equals("BEQZ") || decoded.get(1).equals("JR")) && oldPC != newPC) {

					
					diffPC = (short) (newPC - oldPC - 1); // number of skipped instructions
					

					neededClk = 3 + ((total-newPC) -1);
					
					i=-1;
				}
			else {

				decoded = ca.decode(fetched);

				
			}
				
				


			}
			
			else if (i == neededClk - 1) { // last instr

				oldPC = (short) ca.rf.get("PC");
				ca.execute(decoded);
				newPC = (short) ca.rf.get("PC");
				
				if ((decoded.get(1).equals("BEQZ") || decoded.get(1).equals("JR")) && oldPC != newPC) {

					neededClk = 3 + ((total-newPC) -1);

					i=-1;
				}
				
			}
			
			else { //instr fel nos

				oldPC = (short) ca.rf.get("PC");
				ca.execute(decoded);
				newPC = (short) ca.rf.get("PC");
				
				if ((decoded.get(1).equals("BEQZ") || decoded.get(1).equals("JR")) && oldPC != newPC) {

					neededClk = 3 + ((total-newPC) -1);

					i=-1;
				}
			else {

				decoded = ca.decode(fetched);
				fetched = ca.fetch();
				
			}

			}
			System.out.println("-----------------");
		}
		
		
		System.out.println("Done!");

	}

	private int[] createProgram(String string) throws IOException {
		BufferedReader Reader = new BufferedReader(new FileReader(string));
		String row = "";
		int count=0;
		Vector<String> codeLines = new Vector();
		while ((row = Reader.readLine()) != null ) {
			count++;
			String[] data = row.split(" ");
			String tmp = "";
			String type = "";
			switch (data[0].toLowerCase()) {
			case ("add"):
				tmp+= "0000";
			type="r";
				break;
			case ("sub"):
				tmp+= "0001";
			type="r";
				break;
			case ("mul"):
				tmp+= "0010";type="r";
				break;
			case ("ldi"):
				tmp+= "0011";type="i";
				break;

			case ("beqz"):
				tmp+= "0100";type="i";
				break;
			case ("and"):
				tmp+= "0101";type="r";
				break;
			case ("or"):
				tmp+= "0110";type="r";
				break;
			case ("jr"):
				tmp+= "0111";type="r";
				break;

			case ("slc"):
				tmp+= "1000";type="i";
				break;
			case ("src"):
				tmp+= "1001";type="i";
				break;
			case ("lb"):
				tmp+= "1010";type="i";
				break;
			case ("sb"):
				tmp+= "1011";type="i";
				break;
			}
			
			String r1 = data[1].substring(1);
			System.out.println(data[1].substring(1)+"----------------->");
			String r1Bin = Integer.toBinaryString(Integer.parseInt(r1));
			System.out.println(r1Bin+"------->");
			r1Bin = "000000" + r1Bin;
	    	r1Bin = r1Bin.substring(r1Bin.length()-6);
	    	System.out.println(r1Bin+"helloooooo");
	    	
	    	String r2Bin ="";
	    	
	    	
	    	if (type.equals("i")) {
	    		
	    		r2Bin = Integer.toBinaryString(Integer.parseInt(data[2]));
	    		r2Bin = "000000" + r2Bin;
		    	r2Bin = r2Bin.substring(r2Bin.length()-6);
	    	}
	    	else {
	    		String r2 = data[2].substring(1);
				r2Bin = Integer.toBinaryString(Integer.parseInt(r2));
				r2Bin = "000000" + r2Bin;
		    	r2Bin = r2Bin.substring(r2Bin.length()-6);
	    	}
			System.out.println(tmp + " temmmmmmmmmmmmpBEFORE");
			System.out.println(r1Bin);
			System.out.println(r2Bin);
			tmp+= r1Bin+r2Bin;
			System.out.println(tmp + " temmmmmmmmmmmmp");
			codeLines.add(tmp);
		}
		int neededClk = 3 + ((codeLines.size() - 1) * 1);
		for (int i = 0; i < codeLines.size(); i++) {
			//System.out.println(codeLines.get(i));
			//System.out.println("i: "(short) Integer.parseInt(codeLines.get(i),2));
		//	System.out.println((short) Integer.parseInt(codeLines.get(i),2));
			this.im.addInstructionMemory(codeLines.get(i)); // check issue with

			if (i == 0) {
				this.rf.put("PC", (short) this.im.lastEdited); // assuming w create the process and we run it directly
			}
		}
		int [] res = new int[2];
		res[0]=neededClk;
		res[1]=count;
		return res;
	}

	static int unsignedBinaryToDecimal(String n) {
		String num = n;
		int dec_value = 0;

		// Initializing base value to 1,
		// i.e 2^0
		int base = 1;

		int len = num.length();
		for (int i = len - 1; i >= 0; i--) {
			if (num.charAt(i) == '1')
				dec_value += base;
			base = base * 2;
		}

		return dec_value;
	}
	
//	static byte signedBinaryToDecimal(String n) {
//		if (n.charAt(0)=='1') {
//			
//		}
//	}

	
	
	
	
	private int createProgramMachineLanguage(String string) throws IOException {
		BufferedReader Reader = new BufferedReader(new FileReader(string));
		String row = "";
		Vector<String> codeLines = new Vector();
		while ((row = Reader.readLine()) != null) {
			codeLines.add(row);
		}
		int neededClk = 3 + ((codeLines.size() - 1) * 1);
		for (int i = 0; i < codeLines.size(); i++) {
			//System.out.println(codeLines.get(i));
			//System.out.println("i: "(short) Integer.parseInt(codeLines.get(i),2));
		//	System.out.println((short) Integer.parseInt(codeLines.get(i),2));
			this.im.addInstructionMemory(codeLines.get(i)); // check issue with

			if (i == 0) {
				this.rf.put("PC", (short) this.im.lastEdited); // assuming w create the process and we run it directly
			}
		}
		return neededClk;
	}

	public void execute(Vector decoded) {
		String op = (String) decoded.get(1);
		switch (op) { // 2= r1 address, 3= r1 val, 4 = r2 val
		case ("ADD"):
			this.add((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("SUB"):
			this.sub((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("MUL"):
			this.mul((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("LDI"):
			this.ldi((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;

		case ("BEQZ"):
			short pc = (short) decoded.get(5);
			this.beqz((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4),pc);
			break;
		case ("AND"):
			this.and((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("OR"):
			this.or((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("JR"):
			this.jr((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;

		case ("SLC"):
			this.slc((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("SRC"):
			this.src((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("LB"):
			this.lb((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		case ("SB"):
			this.sb((int) decoded.get(2), (byte) decoded.get(3), (byte) decoded.get(4));
			break;
		}
		String SREG = (String) this.rf.get("SREG");
		System.out.println("The value of the SREG Register is: " +this.rf.get("SREG"));
		
		System.out.println("The carry flag is: " + SREG.charAt(3));
		System.out.println("The overflow flag is: " + SREG.charAt(4));
		System.out.println("The negative flag is: " + SREG.charAt(5));
		System.out.println("The sign flag is: " + SREG.charAt(6));
		System.out.println("The zero flag is: " + SREG.charAt(7));
		
		
		

	}

	private void sb(int i, byte s, byte t) {
//		byte data = this.dm.getData(t);
//		this.rf.put("R"+i, data);
		System.out.println("SB operation");
	
//		byte data = (byte) this.rf.get("R" + i);
		this.dm.addDataMemory(t, s);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", "00000000");
		System.out.println("New memory value in address: " +t +" is " +this.dm.getData(t));
		
	}

	private void lb(int i, byte s, byte t) {
		
		System.out.println("LB operation");
		
		
		this.rf.put("R" + i, this.dm.getData(t));
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", "00000000");
		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;

	}

	private void src(int i, byte s, byte t) {
		System.out.println("SRC operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		s = (byte) (s >>> t | s << 8 - t);
		byte actualVal = s;
//		char c =  ((res>Byte.MAX_VALUE)? '1':'0');
		char c = '0';
		/*
		 * If 2 numbers are added, and they both have the same sign (both positive or
		 * both negative), then overflow occurs (V = 1) if and only if the result has
		 * the opposite sign. Overflow never occurs when adding operands with different
		 * signs.
		 */
		char v = '0';

//		if(s<0 && t>=0 ) {
//			if (res>=0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}
//		else if (s>=0 && t<0) {
//			if (res<0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}

		char n = ((actualVal < 0) ? '1' : '0');
//		char sign =((actualVal<0)? '1':'0');
		char sign = '0';

		char z = ((actualVal == 0) ? '1' : '0');

		String SREG = "000" + c + v + n + sign + z;

		this.rf.put("R" + i, actualVal);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", SREG);

		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;
	}

	private void slc(int i, byte s, byte t) {
		
		System.out.println("SLC operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		s = (byte) (s << t | s >>> 8 - t);
		byte actualVal = s;
//		char c =  ((res>Byte.MAX_VALUE)? '1':'0');
		char c = '0';
		/*
		 * If 2 numbers are added, and they both have the same sign (both positive or
		 * both negative), then overflow occurs (V = 1) if and only if the result has
		 * the opposite sign. Overflow never occurs when adding operands with different
		 * signs.
		 */
		char v = '0';

//		if(s<0 && t>=0 ) {
//			if (res>=0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}
//		else if (s>=0 && t<0) {
//			if (res<0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}

		char n = ((actualVal < 0) ? '1' : '0');
//		char sign =((actualVal<0)? '1':'0');
		char sign = '0';

		char z = ((actualVal == 0) ? '1' : '0');

		String SREG = "000" + c + v + n + sign + z;

		this.rf.put("R" + i, actualVal);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", SREG);
		
		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;
	}

	private void jr(int i, byte s, byte t) {
		
		System.out.println("JR operation");
		
//		int res =  s & t;
		short sh = (short) s;
		sh <<= 8;
		short actualVal = (short) (sh | t);

		String SREG = "00000000";

		this.rf.put("PC", actualVal);
		this.rf.put("SREG", SREG);

	}

	private void or(int i, byte s, byte t) {
//		int res =  s & t;
		
		
		System.out.println("OR operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		byte actualVal = (byte) (s | t);
//		char c =  ((res>Byte.MAX_VALUE)? '1':'0');
		char c = '0';
		/*
		 * If 2 numbers are added, and they both have the same sign (both positive or
		 * both negative), then overflow occurs (V = 1) if and only if the result has
		 * the opposite sign. Overflow never occurs when adding operands with different
		 * signs.
		 */
		char v = '0';

//		if(s<0 && t>=0 ) {
//			if (res>=0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}
//		else if (s>=0 && t<0) {
//			if (res<0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}

		char n = ((actualVal < 0) ? '1' : '0');
//		char sign =((actualVal<0)? '1':'0');
		char sign = '0';

		char z = ((actualVal == 0) ? '1' : '0');

		String SREG = "000" + c + v + n + sign + z;

		this.rf.put("R" + i, actualVal);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", SREG);

		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;
	}

	private void and(int i, byte s, byte t) {
//		int res =  s & t;
		
		System.out.println("And operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		byte actualVal = (byte) (s & t);
//		char c =  ((res>Byte.MAX_VALUE)? '1':'0');
		char c = '0';

		char v = '0';

//		if(s<0 && t>=0 ) {
//			if (res>=0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}
//		else if (s>=0 && t<0) {
//			if (res<0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}

		char n = ((actualVal < 0) ? '1' : '0');
//		char sign =((actualVal<0)? '1':'0');
		char sign = '0';

		char z = ((actualVal == 0) ? '1' : '0');

		String SREG = "000" + c + v + n + sign + z;

		this.rf.put("R" + i, actualVal);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", SREG);
		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;
	}

	private void beqz(int i, byte s, byte t, short pc) {
		System.out.println("BEQZ operation");
	
		this.rf.put("SREG", "00000000");
		if (s == 0) {
//			short pc = (short) this.rf.get("PC");
			pc = (short) (pc + 1 + t);
			this.rf.put("PC", pc);
		} 
//			else {
//			short pc = (short) this.rf.get("PC");
//			pc = (short) (pc + 1);
//			this.rf.put("PC", pc);
//		}

	}

	private void ldi(int i, byte s, byte t) {
		System.out.println("LDI operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		this.rf.put("R" + i, t);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", "00000000");
		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;

	}

	private void mul(int i, byte s, byte t) {
		System.out.println("MUL operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		int res = s * t;
		byte actualVal = (byte) (s * t);
		char c = ((res > Byte.MAX_VALUE) ? '1' : '0');
		/*
		 * If 2 numbers are added, and they both have the same sign (both positive or
		 * both negative), then overflow occurs (V = 1) if and only if the result has
		 * the opposite sign. Overflow never occurs when adding operands with different
		 * signs.
		 */
		char v = '0';

//		if(s<0 && t>=0 ) {
//			if (res>=0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}
//		else if (s>=0 && t<0) {
//			if (res<0) {
//				v='1';
//			}
//			else {
//				v='0';
//			}
//		}

		char n = ((actualVal < 0) ? '1' : '0');
//		char sign =((actualVal<0)? '1':'0');
		char sign = '0';

		char z = ((actualVal == 0) ? '1' : '0');

		String SREG = "000" + c + v + n + sign + z;

		this.rf.put("R" + i, actualVal);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", SREG);
		
		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;
	}

	private void sub(int i, byte s, byte t) {
		
		System.out.println("SUB operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		int res = s - t;
		byte actualVal = (byte) (s - t);
		char c = ((res > Byte.MAX_VALUE) ? '1' : '0');
		/*
		 * If 2 numbers are added, and they both have the same sign (both positive or
		 * both negative), then overflow occurs (V = 1) if and only if the result has
		 * the opposite sign. Overflow never occurs when adding operands with different
		 * signs.
		 */
		char v = '0';

		if (s < 0 && t >= 0) {
			if (actualVal >= 0) {
				v = '1';
			} else {
				v = '0';
			}
		} else if (s >= 0 && t < 0) {
			if (actualVal < 0) {
				v = '1';
			} else {
				v = '0';
			}
		}

		char n = ((actualVal < 0) ? '1' : '0');

		String signCalc = "" + (Integer.parseInt(n + "") ^ Integer.parseInt(v + ""));
		char sign = signCalc.charAt(0);
		
		char z = ((actualVal == 0) ? '1' : '0');

		String SREG = "000" + c + v + n + sign + z;

		this.rf.put("R" + i, actualVal);
		
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", SREG);
		
		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;
	}

	private void add(int i, byte s, byte t) {
		
		
		System.out.println("Add operation");
		System.out.println("Old " + "R"+i+" value is: " + s);
		
		int res = s + t;
		byte actualVal = (byte) (s + t);

		char c = ((res > Byte.MAX_VALUE) ? '1' : '0');
		char v = '0';

		if (s < 0 && t < 0) {
			if (actualVal >= 0) {
				v = '1';
			} else {
				v = '0';
			}
		} else if (s >= 0 && t >= 0) {
			if (actualVal < 0) {
				v = '1';
			} else {
				v = '0';
			}
		}

		char n = ((actualVal < 0) ? '1' : '0');

		String signCalc = "" + (Integer.parseInt(n + "") ^ Integer.parseInt(v + ""));
		char sign = signCalc.charAt(0);

		char z = ((actualVal == 0) ? '1' : '0');

		String SREG = "000" + c + v + n + sign + z;

		this.rf.put("R" + i, actualVal);
//		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
		this.rf.put("SREG", SREG);
		
		System.out.println("New value in register R" +i+": "+  this.rf.get("R"+i)) ;

	}

	public String fetch() {
		System.out.println();
		System.out.println("Fetching...");
		short pc = (short) this.rf.get("PC");
		String instruction = this.im.getInstruction(pc);
		this.rf.put("PC", (short) ((short) this.rf.get("PC") + 1));
//		return instruction;
		return instruction + "," + pc;
	}

	public Vector decode(String fetched) {
		System.out.println();
		System.out.println("Decoding: "+ fetched);
		String opcode = fetched.substring(0, 4);

		String type = "";

		if (opcode.equals("0000") || opcode.equals("0001") || opcode.equals("0010") || opcode.equals("0101")
				|| opcode.equals("0110") || opcode.equals("0111")) {
			type = "R";
		} else if (opcode.equals("0011") || opcode.equals("0100") || opcode.equals("1000") || opcode.equals("1001")
				|| opcode.equals("1010") || opcode.equals("1011")) {
			type = "I";
		}

		String op = "";
		switch (opcode) {
		case ("0000"):
			op = "ADD";
			break;
		case ("0001"):
			op = "SUB";
			break;
		case ("0010"):
			op = "MUL";
			break;
		case ("0011"):
			op = "LDI";
			break;

		case ("0100"):
			op = "BEQZ";
			break;
		case ("0101"):
			op = "AND";
			break;
		case ("0110"):
			op = "OR";
			break;
		case ("0111"):
			op = "JR";
			break;

		case ("1000"):
			op = "SLC";
			break;
		case ("1001"):
			op = "SRC";
			break;
		case ("1010"):
			op = "LB";
			break;
		case ("1011"):
			op = "SB";
			break;
		}

		String R1 = fetched.substring(4, 10);
		String R2 = fetched.substring(10, 16);
		
		System.out.println(R1 + " ana hena");
		System.out.println(R2 + " ana hena2");
		int R1Address = Integer.parseInt(R1, 2);
		System.out.println(R1Address + " ana hena3");
		byte R1val = (byte) this.rf.get("R" + R1Address);
		System.out.println(R1val + " ana hena4");
		byte R2val = 0;
		
		
		if (type.equals("R")) {
			int decimal = Integer.parseInt(R2, 2);
			R2val = (byte) this.rf.get("R" + decimal);
			System.out.println(R2val + " ana hena fel R");
		} else if (type.equals("I")) {
			
			if (R2.charAt(0)=='1') {
				R2 = "111111111111111111111111" + R2;
			}
			
			R2val = (byte) Integer.parseInt(R2, 2);
			System.out.println(R2val + " ana hena fel I");
		}
		
		String[] tmpS = fetched.split(",");
		System.out.println(Arrays.toString(tmpS) + " eh el kalam da");
		short pc = Short.parseShort(tmpS[1]);
		
		
		Vector res = new Vector();
		res.add(type);
		res.add(op);
		res.add(R1Address);
		res.add(R1val);
		res.add(R2val);
		res.add(pc);
		return res;
		// TODO Auto-generated method stub

	}

}
