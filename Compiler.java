import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Compiler {
	/*
	 * Sintaxis:
	 * 1 instruccion por linea
	 * Debe haber espacios entre las palabras clave
	 * Luego del ":" de una etiqueta debe haber un espacio 
	 * Comentarios #<comentarios> 
	 */

	//TODO chequear uniqueness


	//Definicion tabla de codOps
	//La misma contendrá tambien las referecias de etiquetas
	static HashMap<String, String> codeOps = HashMapBuilder.build("LOAD", "00000010", "ADD", "00000011", "STORE", "00000001", "JUMP", "00000000",
			"SUB", "00000110", "AND", "00000100", "JZ", "00000101", "NOP", "00000111", "HALT", "00001000");

	//Memoria de 256 celdas
	static String[] programa = new String[256];

	//En labels se almacenarán las variables
	static ArrayList<String> variables = new ArrayList<String>();

	//Codigo pasado en limpio
	static ArrayList<String> codigoFuente= new ArrayList<String>();

	//Mensajes con errores de compilacion
	static ArrayList<String> erroresCompilacion =new ArrayList<String>();

	
	public static void main(String[] args) {		
		//Lectura del nombre del archivo
		Scanner inp = new Scanner(System.in);
		System.out.print("Ingrese el nombre del archivo a compilar: ");
		String fileName = inp.next();

		//Configuracion de lecutura y escritura de archivos
		Scanner scn = IOFileManager.IO(fileName, fileName.split("\\.")[0] + ".taq"); 
		if(scn == null) {
			System.err.println("Error en la carga del archivo");
			inp.close();
			return;
		}

		//Inicializacion de la tabla de memoria de 256 celdas		
		for (int i = 0; i < programa.length; i++) {
			programa[i] = "0000000000000000";
		}


		//Compilacion
		cargaCodigoFuente(scn);
		chequeoDeclaracionCuerpoVariablesPrograma();
		if(erroresCompilacion.isEmpty()){
			cargaVariables();
			cargaPrograma();
		}	

		//Reporte
		if(erroresCompilacion.isEmpty()){
			System.err.println("Compilacion exitosa");
			//Se escribe el archivo .faq
			for (int i = 0; i < programa.length; i++) {
				System.out.println(programa[i]);
			}			
		}else{
			System.err.println(erroresCompilacion.size()+" errores de compilacion.\n");
			for(String s:erroresCompilacion){
				System.err.println(s);
			}
		}
		inp.close();
	}

	private static void cargaVariables(){
		//En este punto se supone que codigoFuente[0]="var:" por lo tanto se ignora
		int i=1;
		while(!codigoFuente.get(i).equalsIgnoreCase("endvar")){
			String var=codigoFuente.get(i).replaceAll(" ","").split(":")[0];
			if(variables.contains(var)){
				erroresCompilacion.add("No se pueden definiar variables con el mismo nombre");
			}else{
				try{
					Integer num=Integer.parseInt(codigoFuente.get(i).replaceAll(" ","").split(":")[1]);
					variables.add(var);
					programa[256-variables.size()]=String.format("%16s", Integer.toString(num, 2)).replace(" ", "0");
				}catch(NumberFormatException e){
					erroresCompilacion.add("Las variables deben inicializarse con un valor decimal");
				}
			}
			i++;
		}		
	}

	private static void cargaPrograma(){
		ArrayList<Integer> posicionSaltos=new ArrayList<Integer>();
		
		int inicio=codigoFuente.indexOf("programa:")+1;
		int i=inicio;
		//se saltea la linea "programa:"
		
		while(!codigoFuente.get(i).equalsIgnoreCase("end")){
			String[] instruction=codigoFuente.get(i).split(" ");
			if(instruction.length>2){
				//Es una inst con label. Guarda la posicion de memoria a la que corresponde
				if(!codeOps.containsKey(instruction[0].replaceAll(":", ""))){
					codeOps.put(instruction[0].replaceAll(":", ""), String.format("%8s", Integer.toString(i-inicio, 2)).replaceAll(" ", "0"));
				}else{
					erroresCompilacion.add("Las etiquetas no se pueden repetir ni coincidir con el nombre de otras instrucciones");
				}
			}else if (instruction.length==1){
				//HALT
				programa[i-inicio] = codeOps.get(instruction[0]) + "00000000";
				i++;
				continue;
			}
			//Ya sea para instruccion con etiqueta o no
			String codeOp = instruction[instruction.length-2];
			String op;
			if(codeOp.equalsIgnoreCase("jump") || codeOp.equalsIgnoreCase("jz")){
				posicionSaltos.add(i);//se procesaran mas abajo
			}else {
				op = instruction[instruction.length-1];
				programa[i-inicio] = codeOps.get(codeOp) + String.format("%8s", Integer.toString(255 - variables.indexOf(op), 2)).replaceAll(" ", "0");
			}			
			i++;
		}
		//se procesan los saltos
		for(Integer j:posicionSaltos){
			String[] instruction=codigoFuente.get(j).split(" ");
			programa[j-inicio]=codeOps.get(instruction[instruction.length-2]) + codeOps.get(instruction[instruction.length-1]);
		}
	}

	private static void cargaCodigoFuente(Scanner scn){
		//se limpiarán lineas vacias,tabs,espacios de sobra y comentarios
		while(scn.hasNext()){
			String instruction = scn.nextLine().trim().replace("\t","").split("#")[0];
			if(!instruction.equalsIgnoreCase("")){
				codigoFuente.add(instruction);
			}			
		}		
	}
	
	private static void chequeoDeclaracionCuerpoVariablesPrograma(){
		if(!codigoFuente.contains("var:")){
			erroresCompilacion.add("Falta la declaracion 'var:'");
		}else{
			if(codigoFuente.indexOf("var:")!=0){
				erroresCompilacion.add("El codigo debe empezar con 'var:'");
			}
			if(!codigoFuente.contains("endvar")){
				erroresCompilacion.add("Falta la declaracion 'endvar'");
			}else{
				//tiene ambas
				if(codigoFuente.indexOf("var:")>codigoFuente.indexOf("endvar")){
					erroresCompilacion.add("'endvar' debe declararse luego de 'var:'");
				}	
			}
		}

		if(!codigoFuente.contains("programa:")){
			erroresCompilacion.add("Falta la declaracion 'programa:'");
		}else{
			if(!codigoFuente.contains("end")){
				erroresCompilacion.add("Falta la declaracion 'end'");
			}else{
				//tiene ambas
				if(codigoFuente.indexOf("programa:")>codigoFuente.indexOf("end")){
					erroresCompilacion.add("'end' debe declararse luego de 'programa:'");
				}	
			}
		}

		if(codigoFuente.indexOf("var:")>codigoFuente.indexOf("programa:") && codigoFuente.contains("programa:")){
			erroresCompilacion.add("Se deben declarar las variables antes del programa principal");
		}
	}

}
