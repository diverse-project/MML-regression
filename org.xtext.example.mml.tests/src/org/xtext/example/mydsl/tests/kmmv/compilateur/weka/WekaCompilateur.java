package org.xtext.example.mydsl.tests.kmmv.compilateur.weka;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.xtext.example.mydsl.mml.AllVariables;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Compilateur;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Pair;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Utils;

/**
 * List of key word accross the compilateur :
 * -> clf : algorithm
 * -> data : data
 */
public class WekaCompilateur implements Compilateur {

	@Override
	public String compile(DataInput input, MLChoiceAlgorithm algorithm, RFormula formula, Validation validation) {
		Pair<List<String>, List<String>> inputC = compileDataInput(input);
		Pair<List<String>, List<String>> formulaC = compileRFormula(formula);
//		Pair<List<String>, List<String>> algorithmC = MLAlgorithmCompiler.compile(algorithm.getAlgorithm());
//		Pair<List<String>, List<String>> validationC = ValidationCompiler.compile(validation);
		
		List<String> buffer = new LinkedList<>(), bufferImports = new LinkedList<>();
		
		bufferImports.addAll(inputC.getFirst());
		bufferImports.addAll(formulaC.getFirst());
//		bufferImports.addAll(algorithmC.getFirst());
//		bufferImports.addAll(validationC.getFirst());
		
		buffer.addAll(filterImport(bufferImports));
		buffer.add("");
		buffer.addAll(inputC.getSecond());
		buffer.addAll(formulaC.getSecond());
//		buffer.addAll(algorithmC.getSecond());
//		buffer.addAll(validationC.getSecond());
		
		return String.join("\n", buffer);
	}

	@Override
	public String fileName(DataInput input, MLChoiceAlgorithm algorithm, int uniqueId) {
		return String.format("%s_%s_%s.java", input.getFilelocation().replace('.', '_'), Utils.algorithmName(algorithm.getAlgorithm()), uniqueId);
	}

	private Pair<List<String>, List<String>> compileDataInput(DataInput input) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("import weka.core.converters.ConverterUtils.DataSource;");
		import_.add("import weka.core.Instances;");
		
		code_.add(String.format("Instances data = DataSource.read(\"%s\");", input.getFilelocation()));
		
		return new Pair<>(import_, code_);
	}
	
	private Pair<List<String>, List<String>> compileRFormula(RFormula formula){
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("import weka.core.Attribute;");
		import_.add("import java.util.List;");
		import_.add("import java.util.LinkedList;");
		import_.add("import weka.core.Instances;");
		
		code_.add("List<String> column = new LinkedList<>();");
		code_.add("String className = \"\";");
		code_.add("for(Attribute atr : data.enumerateAttributes())");
		code_.add(String.format("%scolumn.add(atr.name());", Utils.tab()));
		
		if(formula != null && formula.getPredictive() != null) {
			if(formula.getPredictive().getColName() != null)
				code_.add(String.format("className = \"%s\";", formula.getPredictive().getColName()));
			else
				code_.add(String.format("className = column.get(%d);", formula.getPredictive().getColumn()));
		} else {
			code_.add("className = column.get(column.size()-1);");
		}
		
		code_.add("data.setClassIndex(column.indexOf(className));");
		
		if(formula != null && formula.getPredictors() != null && !(formula.getPredictors() instanceof AllVariables)) {
//				List<String> predictors = ((PredictorVariables) formula.getPredictors())
//					.getVars()
//					.stream()
//					.map(variable -> String.format("\"%s\"", variable))
//					.collect(Collectors.toList());
//				code_.add(String.format("X = mml_data[[%s]]", String.join(",", predictors)));
			// TODO : remove all the useless attributes
		}
		
		return new Pair<>(import_, code_);
	}
	
	private List<String> filterImport(List<String> imports) {
		List<String> result = new LinkedList<>();
		
		for(String imp : imports) {
			if(!result.contains(imp))
				result.add(imp);
		}
		
		return result;
	}
}
