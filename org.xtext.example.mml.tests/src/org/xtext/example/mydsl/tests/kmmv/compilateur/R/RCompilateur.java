package org.xtext.example.mydsl.tests.kmmv.compilateur.R;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.xtext.example.mydsl.mml.*;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Compilateur;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Pair;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Utils;

public class RCompilateur implements Compilateur {

	@Override
	public String compile(DataInput input, MLChoiceAlgorithm algorithm, RFormula formula, Validation validation, int uniqueId) {
		Pair<List<String>, List<String>> inputC = compileDataInput(input);
		Pair<List<String>, List<String>> formulaC = compileRFormula(formula);
		Pair<List<String>, List<String>> validationC = ValidationCompiler.compile(validation);
		Pair<List<String>, List<String>> algorithmC = MLAlgorithmCompiler.compile(algorithm.getAlgorithm(), validation);
		Pair<List<String>, List<String>> validationMetricsC = ValidationCompiler.compileMetrics(validation);
		
		List<String> buffer = new LinkedList<>(), bufferImports = new LinkedList<>();
		
		bufferImports.addAll(inputC.getFirst());
		bufferImports.addAll(formulaC.getFirst());
		bufferImports.addAll(validationC.getFirst());
		bufferImports.addAll(algorithmC.getFirst());
		bufferImports.addAll(validationMetricsC.getFirst());
		
		buffer.addAll(filterImport(bufferImports));
		buffer.add("");
		buffer.addAll(inputC.getSecond());
		buffer.addAll(formulaC.getSecond());
		buffer.addAll(validationC.getSecond());
		buffer.addAll(algorithmC.getSecond());
		buffer.addAll(validationMetricsC.getSecond());
		
		return String.join("\n", buffer);
	}

	@Override
	public String fileName(DataInput input, MLChoiceAlgorithm algorithm, Validation validation, int uniqueId) {
		return String.format("%s_%s_%s_%s.R", input.getFilelocation().replace('.', '_'), Utils.algorithmName(algorithm.getAlgorithm()), Utils.stratificationToString(validation.getStratification()), uniqueId);
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
		
		CSVParsingConfiguration parsingInstruction = input.getParsingInstruction();
		String separateur = parsingInstruction != null ? parsingInstruction.getSep().toString() : ",";
		
		code_.add(String.format("mml_data <- read.csv(file='%s' header=TRUE,sep='%s')", input.getFilelocation(), separateur));
		
		return new Pair<>(import_, code_);
	}
	
	private Pair<List<String>, List<String>> compileRFormula(RFormula formula) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
				
		if(formula != null && formula.getPredictive() != null) {
			if(formula.getPredictive().getColName() != null)
				code_.add(String.format("Y_name <- '%s'", formula.getPredictive().getColName()));
			else
				code_.add(String.format("Y_name <- names(mml_data)[%d]", formula.getPredictive().getColumn()));
		} else {
			code_.add("Y_name <- names(mml_data)[ncol(mml_data)]");
		}
		code_.add("Y <- mml_data[Y_name][[1]]");
		
		if(formula != null && formula.getPredictors() != null) {
			if(formula.getPredictors() instanceof AllVariables) {
				code_.add("X <- mml_data[, !names(mml_data) %in% Y_name, drop = F]");
			} else {
				List<String> predictorsName = ((PredictorVariables) formula.getPredictors())
						.getVars()
						.stream()
						.filter(variable -> variable.getColName() != null && !variable.getColName().isBlank())
						.map(variable -> String.format("'%s'", variable.getColName()))
						.collect(Collectors.toList());
				List<Integer> predictorsIndex = ((PredictorVariables) formula.getPredictors())
						.getVars()
						.stream()
						.filter(variable -> variable.getColName() == null || variable.getColName().isBlank())
						.map(variable -> variable.getColumn())
						.collect(Collectors.toList());
				
				code_.add("predictorsSet <- c()");
				for(String predN : predictorsName)
					code_.add(String.format("predictorsSet <- c(predictorsSet, %s)", predN));
				for(Integer predI : predictorsIndex)
					code_.add(String.format("predictorsSet <- c(predictorsSet, names(mml_data)[%d])", predI));
				code_.add("predictorsSet <- unique(predictorsSet)");
				code_.add("X <- mml_data[ , predictorsSet]");
			}
				
		} else {
			code_.add("X <- mml_data[, !names(mml_data) %in% Y_name, drop = F]");
		}
		
		code_.add("X_names <- c(names(X))");
		code_.add("formula <- as.formula(paste(Y_name, paste(X_names, collapse=' + '), sep' ~ '))");
		
		return new Pair<>(import_, code_);
	}
		
	@Override
	public String commandLine(String file) {
		return String.format("R -q --vanilla < %s", file);
	}

}
