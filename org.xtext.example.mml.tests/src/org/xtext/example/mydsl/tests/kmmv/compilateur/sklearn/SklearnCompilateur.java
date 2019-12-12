package org.xtext.example.mydsl.tests.kmmv.compilateur.sklearn;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.xtext.example.mydsl.mml.*;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Compilateur;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Pair;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Utils;

/**
 * List of key word accross the compilateur :
 * -> clf : algorithm
 * -> X : predictive variable
 * -> Y : variable cible
 * -> mml_data : data
 */

public class SklearnCompilateur implements Compilateur {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String compile(DataInput input, MLChoiceAlgorithm algorithm, RFormula formula, Validation validation, int uniqueId) {
		Pair<List<String>, List<String>> inputC = compileDataInput(input);
		Pair<List<String>, List<String>> formulaC = compileRFormula(formula);
		Pair<List<String>, List<String>> algorithmC = MLAlgorithmCompiler.compile(algorithm.getAlgorithm());
		Pair<List<String>, List<String>> validationC = ValidationCompiler.compile(validation);
		
		List<String> buffer = new LinkedList<>(), bufferImports = new LinkedList<>();
		
		bufferImports.addAll(inputC.getFirst());
		bufferImports.addAll(formulaC.getFirst());
		bufferImports.addAll(algorithmC.getFirst());
		bufferImports.addAll(validationC.getFirst());
		
		buffer.addAll(filterImport(bufferImports));
		buffer.add("");
		buffer.addAll(inputC.getSecond());
		buffer.addAll(formulaC.getSecond());
		buffer.addAll(algorithmC.getSecond());
		buffer.addAll(validationC.getSecond());
		
		return String.join("\n", buffer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fileName(DataInput input, MLChoiceAlgorithm algorithm, Validation validation, int uniqueId) {
		return String.format("%s_%s_%s_%s.py", input.getFilelocation().replace('.', '_'), Utils.algorithmName(algorithm.getAlgorithm()), Utils.stratificationToString(validation.getStratification()), uniqueId);
	}
	
	private List<String> filterImport(List<String> imports) {
		List<String> result = new LinkedList<>();
		
		for(String imp : imports) {
			if(!result.contains(imp))
				result.add(imp);
		}
		
		return result;
	}
	
	private Pair<List<String>, List<String>> compileDataInput(DataInput input) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("from pandas import read_csv");
		
		CSVParsingConfiguration parsingInstruction = input.getParsingInstruction();
		String separateur = parsingInstruction != null ? parsingInstruction.getSep().toString() : ",";
		
		code_.add(String.format("mml_data = read_csv('%s',sep='%s')", input.getFilelocation(), separateur));
		
		return new Pair<>(import_, code_);
	}
	
	private Pair<List<String>, List<String>> compileRFormula(RFormula formula) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		code_.add("column = list(mml_data)");
				
		if(formula != null && formula.getPredictive() != null) {
			if(formula.getPredictive().getColName() != null)
				code_.add(String.format("Y_name = '%s'", formula.getPredictive().getColName()));
			else
				code_.add(String.format("Y_name = column[%d]", formula.getPredictive().getColumn()));
		} else {
			code_.add("Y_name = column[-1]");
		}
		
		if(formula != null && formula.getPredictors() != null) {
			if(formula.getPredictors() instanceof AllVariables) {
				code_.add("X = mml_data.drop(columns=Y_name)");
			} else {
				List<String> predictors = ((PredictorVariables) formula.getPredictors())
					.getVars()
					.stream()
					.map(variable -> String.format("\"%s\"", variable))
					.collect(Collectors.toList());
				code_.add(String.format("X = mml_data[[%s]]", String.join(",", predictors)));
			}
				
		} else {
			code_.add("X = mml_data.drop(columns=Y_name)");
		}
		
		code_.add("Y = mml_data[Y_name]");
		
		return new Pair<>(import_, code_);
	}

	@Override
	public String commandLine(String file) {
		return String.format("python3 %s", file);
	}
}
