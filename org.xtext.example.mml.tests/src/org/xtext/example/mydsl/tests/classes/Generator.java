package org.xtext.example.mydsl.tests.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;

import com.google.common.io.Files;


/**
 * Class Generator
 * 
 * Generate a program by using a Strategy
 * Write the program in a new file, 
 * compile it and get the result
 * 
 * @author hugues
 *
 */
public class Generator {
	
	private TemplateModel strategy;
	private String fileType;
	private Map<String, Float> scores;
	private String dirName; 
	private Map<String, Double> execTime;
	
	public Generator() {
		scores = new HashMap<>();
		execTime = new HashMap<>();
		
		this.dirName = "prog-gen/"+java.time.LocalDateTime.now();
	}

	/**
	 * Set the strategy to use
	 * @param strategy the new strategy
	 */
	public void setStrategy(TemplateModel strategy) {
		this.strategy = strategy;
	}

	/**
	 * Configure the strategy with information the it needs.
	 * @param model the model of the wished program
	 * @param type type of the file to produce
	 */
	public void configure(MMLModel model, String type) {
		this.strategy.configure(model, model.getInput().getFilelocation());
		this.fileType = type;
	}

	/**
	 * Function that generate and execute the program
	 * @throws IOException
	 */
	public void generate() throws IOException {
		String content = this.strategy.execute();
		String filename = generateFilename();
		
		
		Runtime.getRuntime().exec("mkdir " + dirName);
		
		Files.write(content.getBytes(), new File(dirName+"/"+filename));
		Timestamp start = new Timestamp(System.currentTimeMillis());
		Process p = Runtime.getRuntime().exec(getCommande() + " "+ dirName + "/" + filename);
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
			scores = strategy.getScore(line, scores);
		}
		Timestamp end = new Timestamp(System.currentTimeMillis());
		double time = (double)(end.getTime() - start.getTime()) / 1000;
		
		execTime.put(strategy.getName(), time);
	}
	
	/**
	 * Generate a file name for the program with the type and the algo used
	 * @return the name of the file
	 */
	private String generateFilename() {
		Random rand = new Random();
		return fileType + "_" + getAlgoName(this.strategy.getAlgo())+ getExtension();
	}
	
	/**
	 * determine the extension of the new file
	 * @return the extension of the file
	 */
	private String getExtension() {
		if(fileType.equalsIgnoreCase(FrameworkLang.SCIKIT.getLiteral())) {
			return ".py";
		}
		if(fileType.equalsIgnoreCase(FrameworkLang.XG_BOOST.getLiteral())) {
			return ".py";
		}
		if(fileType.equalsIgnoreCase(FrameworkLang.R.getLiteral())) {
			return ".R";
		}
		else return ".txt";
	}
	
	/**
	 * Get the command to execute the file
	 * @return the command to execute
	 */
	private String getCommande() {
		if(fileType.equalsIgnoreCase(FrameworkLang.SCIKIT.getLiteral())) {
			return "python";
		}
		if(fileType.equalsIgnoreCase(FrameworkLang.XG_BOOST.getLiteral())) {
			return "python";
		}
		if(fileType.equalsIgnoreCase(FrameworkLang.R.getLiteral())) {
			return "Rscript";
		}
		else return "python";
	}
	
	/**
	 * Get the name of the algo on parameter
	 * @param MLalgo the algo to test
	 * @return the name of the algo
	 */
	private String getAlgoName(MLAlgorithm MLalgo) {
		String name = "";
		if (MLalgo instanceof SVR) {
			name="SVR";
		} else if (MLalgo instanceof RandomForest) {
			name="RF";
		} else if (MLalgo instanceof DT) {
			name="DT";
		} else if (MLalgo instanceof SGD) {
			name="SGD";
		} else if (MLalgo instanceof GTB) {
			name="GTB";
		}
		return name;
	}
	
	/**
	 * Return the scores of the executed program
	 * @return
	 */
	public Map<String, Float> getScore(){
		return this.scores;
	}
	
	/**
	 * Return the execution time of the program
	 * @return
	 */
	public Map<String, Double> getExecTime(){
		return this.execTime;
	}
	
}
	