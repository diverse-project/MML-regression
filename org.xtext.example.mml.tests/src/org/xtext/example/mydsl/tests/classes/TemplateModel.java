package org.xtext.example.mydsl.tests.classes;

import java.util.Map;

import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;

/**
 * Template Model for each concrete strategy
 * Abstract class that provide methods for the generation of programs
 * Implements ModelStrategy and define config() and execute()
 * @author hugues
 *
 */
public abstract class TemplateModel implements ModelStrategy {
	
	private TemplateModel strategy;
	protected MMLModel model;
	protected String content;
	protected String filelocation;
	protected MLAlgorithm algo;
	
	/**
	 * Constructor
	 * @param algo that will be used bu the generate program
	 */
	public TemplateModel(MLAlgorithm algo) {
		this.algo = algo;
	}
	
	/**
	 * Set the model
	 * @param model
	 */
	public void setModel(MMLModel model) {
		this.model = model;
	}
	
	/**
	 * Set the filelocation
	 * @param filelocation
	 */
	public void setFileLocation(String filelocation) {
		this.filelocation = filelocation;
	}
	
	/**
	 * Return the current algo used
	 * @return
	 */
	public MLAlgorithm getAlgo() {
		return this.algo;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(MMLModel model, String fileLocation) {
		this.model = model;
		this.filelocation = fileLocation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String execute() {
		String ret = "";
		ret += this.writeImport() + "\n";
		ret += this.writeFileLocation(filelocation) + "\n";
		ret += this.writeVarCible() + "\n";
		ret += this.writeAlgorithm() + "\n";
		ret += this.writeStratificationMethod() + "\n";
		ret += this.writeModel() + "\n";
		ret += this.writePrint();
		return ret;
	}

	/**
	 * Write code for read the .csv
	 * @param filelocation
	 * @return
	 */
	public abstract String writeFileLocation(String filelocation);
	
	/**
	 * Write code for necessaries imports
	 * @return
	 */
	public abstract String writeImport();
	
	/**
	 * Write code for handle predictives and predictors variable
	 * @return
	 */
	public abstract String writeVarCible();
	
	/**
	 * Write code for the stratifications
	 * @return
	 */
	public abstract String writeStratificationMethod();
	
	/**
	 * Write the algorithm
	 * @return
	 */
	public abstract String writeAlgorithm();
	
	/**
	 * Write the model 
	 * @return
	 */
	public abstract String writeModel();
	
	/**
	 * Write prints
	 * @return
	 */
	public abstract String writePrint();
	
	/**
	 * Return the score of a given line
	 * @param line line to parse
	 * @param map with all the scores
	 * @return the new map
	 */
	public abstract Map<String, Float> getScore(String line, Map<String, Float> map);
	
	/**
	 * Get the name of the langage produce (Python, R..)
	 * @return
	 */
	public abstract String getName();
	
}
