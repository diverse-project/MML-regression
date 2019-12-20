package org.xtext.example.mydsl.tests.classes;

import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;

public abstract class TemplateModel {
	private TemplateModel strategy;
	protected MMLModel model;
	protected String content;
	protected String filelocation;
	protected MLAlgorithm algo;
	
	public TemplateModel(MLAlgorithm algo) {
		this.algo = algo;
	}
	
	public void setModel(MMLModel model) {
		this.model = model;
	}
	
	public void setFileLocation(String filelocation) {
		this.filelocation = filelocation;
	}
	
	public MLAlgorithm getAlgo() {
		return this.algo;
	}
	
	public abstract String writeFileLocation(String filelocation);
	public abstract String writeImport();
	public abstract String writeReadCSV(String filelocation);
	public abstract String writeVarCible();
	public abstract String writeStratificationMethod();
	public abstract String writeAlgorithm();
	public abstract String writeModel();
	public abstract String writeValidationMetric();
	public abstract String writePrint();
	
	public String generate() {
		String ret = "";
		ret += this.writeImport() + "\n";
		ret += this.writeFileLocation(filelocation)  + "\n";
		ret += this.writeVarCible() + "\n";
		ret += this.writeAlgorithm() + "\n";
		ret += this.writeStratificationMethod() + "\n";
		ret += this.writeModel() + "\n";
		ret += this.writePrint();
		return ret;
	}
	
	
}
