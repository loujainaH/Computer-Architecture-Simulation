import java.util.Vector;

public class InstructionsMemory{

String[] instructionMemory;
static short lastEdited=-1;
	public InstructionsMemory() {
		this.instructionMemory = new String[1024];
	}

	public String getInstruction(int address) {
		return (String) instructionMemory[address];
	}

	public void addInstructionMemory(String instruction) {
		
		if (instruction.length()>16) {
			System.out.println("Invalid Instruction");
		}
		
		if(lastEdited<1024) {
			lastEdited++;
			instructionMemory[lastEdited]=instruction;
			
		}
		else {
			System.out.println("Instructions Memory Full!");
		}
	}
	
	public void setInstructionMemory(int address, String instruction) {
		instructionMemory[address]=instruction;
	}
}
