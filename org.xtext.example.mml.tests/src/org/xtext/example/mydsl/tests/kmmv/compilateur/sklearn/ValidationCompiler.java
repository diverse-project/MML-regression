package org.xtext.example.mydsl.tests.kmmv.compilateur.sklearn;

import java.util.LinkedList;
import java.util.List;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Pair;

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
		
		// TODO
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(TrainingTest stratificationMethod, List<ValidationMetric> metrics) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("from sklearn.model_selection import train_test_split");
		
		if(stratificationMethod.getNumber() > 0 && stratificationMethod.getNumber() < 100)
			code_.add(String.format("test_size = %f",stratificationMethod.getNumber()/100.0));
		else
			code_.add("test_size = 0.3");
		code_.add("X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=test_size)");
		
		if(metrics != null) {
			if(metrics.contains(ValidationMetric.MAE))
				import_.add("from sklearn.metrics import mean_absolute_error");
			if(metrics.contains(ValidationMetric.MSE))
				import_.add("from sklearn.metrics import mean_squared_error");
			if(metrics.contains(ValidationMetric.MAPE)) {
				import_.add("from numpy import mean");
				import_.add("from numpy import abs");
				import_.add("from sklearn.utils import check_arrays");
			}
			
			for(ValidationMetric metric : metrics) {
				switch(metric) {
					case MAE:
						code_.add("print(mean_absolute_error(Y_test, clf.predict(X_test)))");
						break;
					case MSE:
						code_.add("print(mean_squared_error(Y_test, clf.predict(X_test)))");
						break;
					case MAPE:
						code_.add("y_true, y_pred = check_arrays(Y_test, clf.predict(X_test))");
						code_.add("print(mean(abs((y_true - y_pred) / y_true)) * 100)");
						break;
				}
			}
		}
		
		return new Pair<>(import_, code_);
	}
}
