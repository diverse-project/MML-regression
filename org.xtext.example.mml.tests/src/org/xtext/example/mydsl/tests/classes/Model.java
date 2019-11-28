package org.xtext.example.mydsl.tests.classes;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.ValidationMetric;

public interface Model {
	public String writeFileLocation(String filelocation);
	public String writeImport();
	public String writeReadCSV(String filelocation);
	public String writeVarCible();
	public String writeStratificationMethod();
	public String writeAlgorithm();
	public String writeModel();
	public String writeValidationMetric();
	public String writePrint();
	public String generate();
}
