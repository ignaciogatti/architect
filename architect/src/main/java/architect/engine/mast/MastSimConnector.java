package architect.engine.mast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import architect.engine.mast.parser.MastParser;
import architect.model.Scenario;

public class MastSimConnector {
	
	public static String MAST_RESULTS_FILE_BASE = "architecture_#ID_result.txt";
	public static String MAST_OUTPUT_FILE_BASE = "architecture_#ID_output.txt";
	
	public static String MAST_SIM_DIR = getMastDir();
	public static String MAST_SIM_ARCHITECT_TEMP = MAST_SIM_DIR +"tmp\\";
	
	public static String MAST_COMMAND = "mast_analysis default";
	public static String MAST_COMMAND_OPTIONS = "-v -c";
	
	private static String getMastDir() {
		File dir = new File(".\\mast\\");
		if (!dir.exists()) {
			dir = new File("C:\\mast\\");
			if(dir.exists())
				return "C:\\mast\\";
		}
		return ".\\mast\\";
	}
	
	public MastSimConnector(){
	}
	
	public Double execute(Scenario scenario) {
		try {		    
			MastInputFile inputFile = new MastInputFile(scenario);
			inputFile.generateAndSave();
			Double performance = MastSimConnector.runCommand(scenario,inputFile.getFileName());
			return roundTwoDecimals(performance);
		} catch (IOException e) {
			return 0d;
		}
	}


	// Modificaciones: Se parseo el archivo output del Mast para buscar el Worst_Global_Response_Times  
	private synchronized static Double runCommand(Scenario scenario, String inputFile) throws IOException {
		try {
			String resultsFile = MAST_RESULTS_FILE_BASE.replace("#ID", scenario.getArchitecture().getId().toString());
			String fullCommand = MAST_SIM_DIR + MAST_COMMAND + " " + MAST_COMMAND_OPTIONS + " " + inputFile + " " + MAST_SIM_ARCHITECT_TEMP + resultsFile;
			//log.info("Ejecutando MAST... " + scenario.getName());
			Process process = Runtime.getRuntime().exec(fullCommand);

			// Código necesario para que el Process termine (TODO evaluar otra alternativa)
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String outputline = null;
			while ( (outputline = br.readLine()) != null) ;
			//System.out.println(outputline);
			
			process.waitFor();
			
			Double cpuUtilization = 0D;
			// BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream())); Versión Original procesaba la consola
			BufferedReader output = new BufferedReader(new InputStreamReader(new FileInputStream(MAST_SIM_ARCHITECT_TEMP + resultsFile)));
			String line = output.readLine();

			// Parsea las transacciones
			List<String> transactions = new ArrayList<String>();
			while (line != null) {
				StringBuffer transaction = new StringBuffer();
				if (line.startsWith("Transaction")) {
					transaction.append(line);
					char c = (char)output.read();
					//log.info(c);
					while (c != ';') {
						if (c != ' ' || c != '\n') {
							//log.info(c);
							transaction.append(c);
						}
						c = (char)output.read();
					}
					transaction.append(c); //Agrega el ;
					transactions.add(transaction.toString());
				}
				//line = output.readLine();


				if (line.contains("Processing_Resource")) {
					while (!line.contains("Total")){
						line = output.readLine();
					}
					//+ scenario.getId() + " :")) {
					//System.out.println("++ MAST: " + line);
					String cpuUtilizationStr = line.substring(line.indexOf(">")+2, line.indexOf("%"));
					//System.out.println("  ++ " + cpuUtilizationStr);
					cpuUtilization = Double.parseDouble(cpuUtilizationStr);	
				}
				line = output.readLine();
			}
			// Procesamiento de las transacciones, se queda con el peor Worst_Global_Response_Times TODO Sacar afuera a una política
			if (!transactions.isEmpty()) { 
				MastParser parser = new MastParser();
				double worstTime;
				double maxWorstTime = -1;
				for(String transaction: transactions) {
					// Obtiene el primer Worst Global Response Time del primer result
					Transaction t = parser.parseTransaction(transaction);
					if (t.getName().contains("_"+scenario.getName().toLowerCase()+"_")){
						worstTime = parser.parseTransaction(transaction).getResults().get(0).getWorst_global_rt().get(0).getTime_value();
						if (worstTime > maxWorstTime)
							maxWorstTime = worstTime;
					}
				}

				process.destroy();

				return maxWorstTime + (cpuUtilization*scenario.getMeasure()/100);
			}
			else
				return Double.MAX_VALUE; // Escenario con una única responsabilidad
		}
		catch (Exception e) {
			e.printStackTrace();
			return Double.MAX_VALUE;
		}
	}

	private static double roundTwoDecimals(Double d) {
		BigDecimal bd = new BigDecimal(d.doubleValue());
		BigDecimal rounded = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
		return rounded.doubleValue();
	}

}
