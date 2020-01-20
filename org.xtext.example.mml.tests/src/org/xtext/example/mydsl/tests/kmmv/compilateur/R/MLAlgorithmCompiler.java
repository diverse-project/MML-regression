package org.xtext.example.mydsl.tests.kmmv.compilateur.R;

import java.util.LinkedList;
import java.util.List;

import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Pair;

/**
 * -> clf : algorithm
 */
public class MLAlgorithmCompiler {
	public static Pair<List<String>, List<String>> compile(MLAlgorithm algorithm, Validation validation) {
		if(algorithm instanceof DT)
			return compile((DT)algorithm, validation);
		if(algorithm instanceof GTB)
			return compile((GTB)algorithm, validation);
		if(algorithm instanceof RandomForest)
			return compile((RandomForest)algorithm, validation);
		if(algorithm instanceof SGD)
			return compile((SGD)algorithm, validation);
		if(algorithm instanceof SVR)
			return compile((SVR)algorithm, validation);
		return new Pair<>(List.of(), List.of());			
	}
	
	private static Pair<List<String>, List<String>> compile(DT algorithm, Validation validation) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		String depth = "";
		if(validation.getStratification() instanceof CrossValidation) {
			if(algorithm.getMax_depth() != 0) {
				depth = ", tuneGrid=depth";
				code_.add(String.format("depth <- expand.grid(maxdepth = %d)", algorithm.getMax_depth()));
			}
			List<ValidationMetric> metrics = validation.getMetric();
			if(metrics != null) {
				if(metrics.contains(ValidationMetric.MAPE)) {
					code_.add(String.format("model_mape <- train(formula, data=mml_data, method='rpart2'%s, trControl=train_control_mape)", depth));
					
					if(metrics.size()>1)
						code_.add(String.format("model <- train(formula, data=mml_data, method='rpart2'%s, trControl=train_control)", depth));
				} else {
					code_.add(String.format("model <- train(formula, data=mml_data, method='rpart2'%s, trControl=train_control)", depth));
				}
			}
		}
		
		if(validation.getStratification() instanceof TrainingTest) {
			import_.add("library(rpart)");
			
			if(algorithm.getMax_depth() != 0)
				depth = String.format(", maxdepth=%d", algorithm.getMax_depth());
			code_.add(String.format("model <- rpart(formula, data=training, method='anova', control= rpart.control(xval= 0%s))", depth));
			code_.add("preds <- predict(model, newdata=testing, type = 'vector')");
		}
	
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(GTB algorithm, Validation validation) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		if(validation.getStratification() instanceof CrossValidation) {
			List<ValidationMetric> metrics = validation.getMetric();
			if(metrics != null) {
				if(metrics.contains(ValidationMetric.MAPE)) {
					code_.add("model_mape <- train(formula, data=mml_data, method='gbm', verbose=FALSE, trControl=train_control_mape)");
					
					if(metrics.size()>1)
						code_.add("model <- train(formula, data=mml_data, method='gbm', verbose=FALSE, trControl=train_control)");
				} else {
					code_.add("model <- train(formula, data=mml_data, method='gbm', verbose=FALSE, trControl=train_control)");
				}
			}
		}
		
		if(validation.getStratification() instanceof TrainingTest) {
			import_.add("library(gbm)");
			code_.add("model <- gbm(formula, data=training, cv.folds=0, verbose = FALSE)");
			code_.add("preds <- predict(model, newdata=testing, n.trees = 100)");
		}
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(RandomForest algorithm, Validation validation) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		if(validation.getStratification() instanceof CrossValidation) {
			List<ValidationMetric> metrics = validation.getMetric();
			if(metrics != null) {
				if(metrics.contains(ValidationMetric.MAPE)) {
					code_.add("model_mape <- train(formula, data=mml_data, method='rf', trControl=train_control_mape)");
					
					if(metrics.size()>1)
						code_.add("model <- train(formula, data=mml_data, method='rf', trControl=train_control)");
				} else {
					code_.add("model <- train(formula, data=mml_data, method='rf', trControl=train_control)");
				}
			}
		}
		
		if(validation.getStratification() instanceof TrainingTest) {
			import_.add("library(randomForest)");
			code_.add("model <- randomForest(formula, data=training)");
			code_.add("preds <- predict(model, newdata=testing, n.trees = 100)");
		}
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(SGD algorithm, Validation validation) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		

		if(validation.getStratification() instanceof CrossValidation) {
			code_.add("stop('Sorry but crossValidation is not available for SGD, please try again selecting TrainingTest')");
		}
		
		if(validation.getStratification() instanceof TrainingTest) {
			import_.add("library(sgd)");
			code_.add("model <- sgd(formula, data=training, model = 'lm')");
			code_.add("preds <- predict(model, newdata=data.matrix(testing))");
		}
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(SVR algorithm, Validation validation) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		if(validation.getStratification() instanceof CrossValidation) {
			String cost = "1";
			if(algorithm.getC() != null)  {
				cost = algorithm.getC(); // getC() is a string ?
			}
			
			if(algorithm.getKernel() != null) {			
				switch(algorithm.getKernel()) {
					case LINEAR:
						code_.add(String.format("params <- expand.grid(C = %s)", cost));
						code_.add("clf <- 'svmLinear'");
						break;
					case POLY:
						code_.add(String.format("params <- expand.grid(C = %s, degree = 2, scale = 1)", cost)); // LENT
						code_.add("clf <- 'svmPoly'");
						break;
					case RBF:
						code_.add(String.format("params <- expand.grid(C = %s, sigma = 0.01)", cost));
						code_.add("clf <- 'svmRadial'");
						break;
				}
			} else {
				code_.add(String.format("params <- expand.grid(C = %s, sigma = 0.01)", cost));
				code_.add("clf <- 'svmRadial'");
			}
			
			List<ValidationMetric> metrics = validation.getMetric();
			if(metrics != null) {
				if(metrics.contains(ValidationMetric.MAPE)) {
					code_.add("model_mape <- train(formula, data=mml_data, method=clf, tuneGrid=params, trControl=train_control_mape)");
					
					if(metrics.size()>1)
						code_.add("model <- train(formula, data=mml_data, method=clf, tuneGrid=params, trControl=train_control)");
				} else {
					code_.add("model <- train(formula, data=mml_data, method=clf, tuneGrid=params, trControl=train_control)");
				}
			}
		}
		
		if(validation.getStratification() instanceof TrainingTest) {
			import_.add("library(e1071)");
			
			String cost = "";
			if(algorithm.getC() != null) 
				cost = String.format(", cost=%s",algorithm.getC());
			
			if(algorithm.getKernel() != null) {			
				switch(algorithm.getKernel()) {
					case LINEAR:
						code_.add(String.format("model <- svm(formula, data=training, type = 'eps-regression', kernel='linear'%s)", cost));
						break;
					case POLY:
						code_.add(String.format("model <- svm(formula, data=training, type = 'eps-regression', kernel='polynomial'%s)", cost));
						break;
					case RBF:
						code_.add(String.format("model <- svm(formula, data=training, type = 'eps-regression', kernel='radial'%s)", cost));
						break;
				}
			} else {
				code_.add(String.format("model <- svm(formula, data=training, type = 'eps-regression', kernel='radial'%s)", cost));
			}
						
			code_.add("preds <- predict(model, newdata=testing)");
		}

		return new Pair<>(import_, code_);
	}
}
