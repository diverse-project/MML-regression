package org.xtext.example.mydsl.tests.templateMethod;

import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;

public abstract class CodeGenerator {

	// Blocks of code
	protected StringBuilder imports = new StringBuilder();
	protected StringBuilder algorithm = new StringBuilder();
	protected StringBuilder predictive = new StringBuilder();
	protected StringBuilder validation = new StringBuilder();

	protected abstract void generateImports(String separator, MLChoiceAlgorithm algo);

	protected abstract void generateAlgorithm(MLChoiceAlgorithm algo);

	protected abstract void generatePredictive(MLChoiceAlgorithm algo);

	protected abstract void generateValidation(MLChoiceAlgorithm algo);

	protected abstract void addLastOperations();

	protected MMLModel result;

	// template method
	public final StringBuilder generate(String separator, MLChoiceAlgorithm algo, MMLModel result) {
		this.result = result;
		
		StringBuilder program = new StringBuilder();

		imports = new StringBuilder();
		algorithm = new StringBuilder();
		predictive = new StringBuilder();
		validation = new StringBuilder();

		// imports related code generation
		generateImports(separator, algo);

		// algorithm related code generation
		generateAlgorithm(algo);

		// predictive variables related code generation
		generatePredictive(algo);

		// validation related code generation
		generateValidation(algo); 
		
		// append last operations if necessary
		addLastOperations(); 
	
		program.append(imports.toString());
		program.append(predictive.toString());
		program.append(algorithm.toString());
		program.append(validation.toString());

		return program;
	}
}
