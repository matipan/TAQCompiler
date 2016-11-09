import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Compiler {

	public static void main(String[] args) {
		Scanner inp = new Scanner(System.in);
		System.out.print("Ingrese el nombre del archivo a compilar (sin extension): ");
		String fileName = inp.next();
		Scanner scn = IOFileManager.IO(fileName, fileName + ".taq");
		HashMap<String, String> codeOps = HashMapBuilder.build("LOAD", "00000010", "ADD", "00000011", "STORE", "00000001", "JUMP", "00000000",
				"SUB", "00000110", "AND", "00000100", "JZ", "00000101", "NOP", "00000111", "HALT", "00001000");
		if(scn == null) {
			return;
		}
		String[] program = new String[256];
		ArrayList<String> labels = new ArrayList<String>();
		for (int i = 0; i < program.length; i++) {
			program[i] = "0000000000000000";
		}

		// Variables loading
		boolean stopError=false;
		boolean stop = false;
		boolean varRead=false;
		while(scn.hasNext() && !stop && !stopError){
			String instruction = scn.nextLine();
			if(instruction.equalsIgnoreCase("var:") && !varRead){
				varRead=true;
			}else if(!varRead) {
				stopError=true;
			} else if(instruction.equalsIgnoreCase("endvar")){
				stop=true;
			} else if(!instruction.trim().equalsIgnoreCase("")){
				instruction = instruction.replaceAll(" ", ""); // cleanup so that it's like this: 'label:value'
				String label = instruction.split(":")[0];
				// TODO: validate_uniqueness_of :label
				int value = Integer.parseInt(instruction.split(":")[1]);
				labels.add(label);
				program[256 - labels.size()] = String.format("%16s", Integer.toString(value, 2)).replace(" ", "0");
			}
		}
		if(stopError) {
			// SE PRENDIO FUEGO EL PROCESADOR Y DESAPROBASTE TALLER
			System.err.println("Error de compilacion");
			return;
		}
		
		
		// Program compilation
		boolean end = false;
		boolean programRead = false;
		int lastIndex = 0;
		while(scn.hasNext() && !end && !stopError){
			String instruction = scn.nextLine();
			if(instruction.equalsIgnoreCase("programa:") && !programRead){
				programRead=true;
			}else if(!programRead) {
				stopError=true;
			} else if(instruction.equalsIgnoreCase("end")){
				end=true;
			} else if(!instruction.trim().equalsIgnoreCase("")){
				String[] decodedInst = instruction.split(" ");
				if(decodedInst.length > 2){
					// Es una inst con label
					codeOps.put(decodedInst[0].replaceAll(":", ""), String.format("%8s", Integer.toString(lastIndex, 2)).replaceAll(" ", "0"));
					
				} else if(decodedInst.length == 1){
					// Es halt
					program[lastIndex++] = codeOps.get(decodedInst[0]) + "00000000";
					continue;
				}
				String codeOp = decodedInst[decodedInst.length-2];
				String op;
				if(codeOp.equalsIgnoreCase("jump") || codeOp.equalsIgnoreCase("jz")){
					// se obvia para analizarlo despues
					lastIndex++;
				}else {
					op = decodedInst[decodedInst.length-1];
					program[lastIndex++] = codeOps.get(codeOp) + String.format("%8s", Integer.toString(255 - labels.indexOf(op), 2)).replaceAll(" ", "0");
				}
			}
		}
		end = false;
		lastIndex = 0;
		scn=IOFileManager.IO(fileName, fileName+".taq");
		while(!scn.nextLine().equalsIgnoreCase("programa:") && !end && !stopError){
		}
		
		while(scn.hasNext() && !end && !stopError){
			String instruction = scn.nextLine();
			if(instruction.equalsIgnoreCase("end")){
				end=true;
			} else if(!instruction.trim().equalsIgnoreCase("")){
				String[] decodedInst = instruction.split(" ");
				if(decodedInst.length>1){
					String codeOp = decodedInst[decodedInst.length-2];
					if(codeOp.equalsIgnoreCase("jump") || codeOp.equalsIgnoreCase("jz")){
						String op = decodedInst[decodedInst.length-1];
						program[lastIndex] = codeOps.get(codeOp) + codeOps.get(op);
					}
				}else{
				}
				lastIndex++;
			}
		}
		for (int i = 0; i < program.length; i++) {
			if(program[i].equalsIgnoreCase(codeOps.get("JUMP")) || program[i].equalsIgnoreCase(codeOps.get("JZ"))){
				program[i] = program[i].substring(0, -2) + codeOps.get(program[i].substring(-2, -1));
			}
		}
		if(stopError) {
			// SE PRENDIO FUEGO EL PROCESADOR Y DESAPROBASTE TALLER
			System.err.println("Error de compilacion");
			return;
		}
		for (int i = 0; i < program.length; i++) {
			System.out.println(program[i]);
		}
		inp.close();
	}

}
