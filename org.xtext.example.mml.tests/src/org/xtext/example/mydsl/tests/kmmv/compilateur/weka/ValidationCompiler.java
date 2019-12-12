package org.xtext.example.mydsl.tests.kmmv.compilateur.weka;

import java.util.LinkedList;
import java.util.List;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Pair;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Utils;

public class ValidationCompiler {
	public static Pair<List<String>, List<String>> compile(Validation validation) {
		if(validation.getStratification() instanceof CrossValidation)
			return compile((CrossValidation)validation.getStratification(), validation.getMetric());
		if(validation.getStratification() instanceof TrainingTest)
			return compile((TrainingTest)validation.getStratification(), validation.getMetric());
		return new Pair<>(List.of(), List.of());
	}
	
	private static Pair<List<String>, List<String>> compile(CrossValidation stratificationMethod, List<ValidationMetric> metrics) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		if(metrics != null) {
			int fold = 5, metrics_ind = 0;
			
			if(stratificationMethod.getNumber() != 0)
				fold = stratificationMethod.getNumber();
			
			import_.add("import weka.classifiers.Evaluation;");
			
			for(ValidationMetric metric : metrics) {
				String evalName = String.format("eval_%d", metrics_ind++);
				code_.add(String.format("Evaluation %s = new Evaluation(data);", evalName));
				code_.add(String.format("%s.crossValidateModel(clf, data, %d);", evalName, fold));
				switch(metric) {
					case MAE:
						code_.add(String.format("System.out.println(%s.meanAbsoluteError());", evalName));
						break;
					case MSE:
						code_.add(String.format("System.out.println(%s.rootMeanSquaredError());", evalName));
						break;
					case MAPE:
						String bufferName = String.format("mape_%d", metrics_ind++);
						String predictionName = String.format("prediction_%d", metrics_ind++);
						import_.add("import weka.classifiers.evaluation.NominalPrediction;");
						code_.add(String.format("double %s = 0.0;",bufferName));
						code_.add(String.format("FastVector %s = %s.predictions();",predictionName, evalName));
						code_.add(String.format("for(int i=0; i<%s.size(); ++i) {",predictionName));
						code_.add(String.format("%sNominalPrediction np = (NominalPrediction) %s.elementAt(i);",Utils.tab(), predictionName));
						code_.add(String.format("%s%s += Math.abs(np.actual() - np.predicted());", Utils.tab(), bufferName));
						code_.add("}");
						code_.add(String.format("System.out.println(%s*100/%s.size());", bufferName, predictionName));
						break;
				}
			}
		}
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(TrainingTest stratificationMethod, List<ValidationMetric> metrics) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		if(stratificationMethod.getNumber() > 0 && stratificationMethod.getNumber() < 100)
			code_.add(String.format("double train_percent = %f;", stratificationMethod.getNumber()/100.0));
		else
			code_.add("double train_percent = 0.7;");
		code_.add("int trainSize = (int) Math.round(data.numInstances() * train_percent);");
		code_.add("int testSize = data.numInstances() - trainSize;");
		code_.add("Instances traindata = new Instances(data, 0, trainSize);");
		code_.add("Instances testdata = new Instances(data, trainSize, testSize);");
		code_.add("clf.buildClassifier(traindata);");
		
		if(metrics != null) {
			int metrics_ind = 0;
			
			for(ValidationMetric metric : metrics) {
				String evalName = String.format("eval_%d", metrics_ind++);
				code_.add(String.format("Evaluation %s = new Evaluation(traindata);", evalName));
				code_.add(String.format("%s.evaluateModel(clf, testdata);", evalName));
				switch(metric) {
					case MAE:
						code_.add(String.format("System.out.println(%s.meanAbsoluteError());", evalName));
						break;
					case MSE:
						code_.add(String.format("System.out.println(%s.rootMeanSquaredError());", evalName));
						break;
					case MAPE:
						String bufferName = String.format("mape_%d", metrics_ind++);
						String predictionName = String.format("prediction_%d", metrics_ind++);
						import_.add("import weka.classifiers.evaluation.NominalPrediction;");
						code_.add(String.format("double %s = 0.0;",bufferName));
						code_.add(String.format("FastVector %s = %s.predictions();",predictionName, evalName));
						code_.add(String.format("for(int i=0; i<%s.size(); ++i) {",predictionName));
						code_.add(String.format("%sNominalPrediction np = (NominalPrediction) %s.elementAt(i);",Utils.tab(), predictionName));
						code_.add(String.format("%s%s += Math.abs(np.actual() - np.predicted());", Utils.tab(), bufferName));
						code_.add("}");
						code_.add(String.format("System.out.println(%s*100/%s.size());", bufferName, predictionName));
						break;
				}
			}
		}
		
		return new Pair<>(import_, code_);
	}
}
