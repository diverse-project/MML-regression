package org.xtext.example.mydsl.tests.kmmv.compilateur.R;

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
		
		import_.add("library(caret)");
		
		if(metrics != null) {
			int cv = 5;
			if(stratificationMethod.getNumber() != 0)
				cv = stratificationMethod.getNumber();
			
			if(metrics.contains(ValidationMetric.MAPE)) {
				code_.add("mapeSummary <- function (data, lev = NULL, model = NULL) {");
				code_.add(String.format("%smape <- function(a, b) mean(abs((a - b)/a))*100", Utils.tab()));
				code_.add(String.format("%sout <- mape(data$obs, data$pred)", Utils.tab()));
				code_.add(String.format("%snames(out) <- 'MAPE'", Utils.tab()));
				code_.add(String.format("%sout", Utils.tab()));
				code_.add("}");
				
				code_.add(String.format("train_control_mape <- trainControl(method='cv', number=%d, summaryFunction = mapeSummary)", cv));
				
				if(metrics.size()>1)
					code_.add(String.format("train_control <- trainControl(method='cv', number=%d)", cv));
			} else {
				code_.add(String.format("train_control <- trainControl(method='cv', number=%d)", cv));
			}
		}
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(TrainingTest stratificationMethod, List<ValidationMetric> metrics) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		if(stratificationMethod.getNumber() > 0 && stratificationMethod.getNumber() < 100)
			code_.add(String.format("training_size = %f",stratificationMethod.getNumber()/100.0));
		else
			code_.add("training_size = 0.7");
		code_.add("index <- createDataPartition(y = Y, p = training_size, list = F)");
		code_.add("training <- mml_data[index, ]");
		code_.add("testing <- mml_data[-index, ]");
		
		return new Pair<>(import_, code_);
	}
	
	public static Pair<List<String>, List<String>> compileMetrics(Validation validation) {
		if(validation.getStratification() instanceof CrossValidation)
			return compileMetrics((CrossValidation)validation.getStratification(), validation.getMetric());
		if(validation.getStratification() instanceof TrainingTest)
			return compileMetrics((TrainingTest)validation.getStratification(), validation.getMetric());
		return new Pair<>(List.of(), List.of());
	}
	
	private static Pair<List<String>, List<String>> compileMetrics(CrossValidation stratificationMethod, List<ValidationMetric> metrics){
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		if(metrics != null) {
			for(ValidationMetric metric : metrics) {
				switch(metric) {
				case MAE:
					code_.add("mean(model$resample$MAE)");
					break;
				case MSE:
					code_.add("mean(model$resample$RMSE^2)");
					break;
				case MAPE:
					code_.add("mean(model_mape$resample$MAPE)");
					break;
				}
			}
		}
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compileMetrics(TrainingTest stratificationMethod, List<ValidationMetric> metrics) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		code_.add("Y_test <- testing[Y_name][[1]]");
		
		if(metrics != null) {
			for(ValidationMetric metric : metrics) {
				switch(metric) {
				case MAE:
					code_.add("mean(abs(preds - Y_test))");
					break;
				case MSE:
					code_.add("mean((preds - Y_test)^2)");
					break;
				case MAPE:
					code_.add("mean(abs((Y_test-preds)/Y_test) * 100)");
					break;
				}
			}
		}
		
		return new Pair<>(import_, code_);
	}
	
}
