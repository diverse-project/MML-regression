package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import com.google.common.io.Files
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.UniqueEList
import org.xtext.example.mydsl.mml.AllVariables
import org.xtext.example.mydsl.mml.CrossValidation
import org.xtext.example.mydsl.mml.DT
import org.xtext.example.mydsl.mml.DataInput
import org.xtext.example.mydsl.mml.FormulaItem
import org.xtext.example.mydsl.mml.FrameworkLang
import org.xtext.example.mydsl.mml.GTB
import org.xtext.example.mydsl.mml.MLAlgorithm
import org.xtext.example.mydsl.mml.MMLModel
import org.xtext.example.mydsl.mml.PredictorVariables
import org.xtext.example.mydsl.mml.RFormula
import org.xtext.example.mydsl.mml.RandomForest
import org.xtext.example.mydsl.mml.SGD
import org.xtext.example.mydsl.mml.SVR
import org.xtext.example.mydsl.mml.StratificationMethod
import org.xtext.example.mydsl.mml.TrainingTest
import org.xtext.example.mydsl.mml.Validation
import org.xtext.example.mydsl.mml.ValidationMetric
import org.xtext.example.mydsl.mml.impl.DTImpl
import org.xtext.example.mydsl.mml.CSVParsingConfiguration
import org.xtext.example.mydsl.mml.CSVSeparator

class MmlCompilateurR {

	var MMLModel mmlModel;
	var MLAlgorithm MLA;
	val String name = "MmlCompilateurR";

	// default value
	var String imports = "";
	var String rasCode = "";
	val EList<String> metricList = new UniqueEList<String>();
	var String fileLocation = "";
	var int numRepetitionCross = 0;
	var String predictiveColName = "colnames(df)[ncol(df)-1]";
	var int numberOfMetric = 0;
	EList<ValidationMetric> VMList = new UniqueEList<ValidationMetric>();
	var double split_ratio = 0.7;
	var String predictors = ".";
	var String csv_separator;

	private new() {
	}

	new(MMLModel mmlModel, MLAlgorithm MLA) {
		if (mmlModel === null) {
			throw new IllegalArgumentException("you should initialize " + this.name + "with non null value");
		}
		this.mmlModel = mmlModel;
		this.MLA = MLA;
	}

	def String metricCode() {
		var String result = "";
		for (ValidationMetric item : VMList) {
			switch item {
				case ValidationMetric.MSE: {
					result += "mse(testY2, result)" + "\n";
					metricList.add("mse");
				}
				case ValidationMetric.MAE: {
					result += "mae(testY2, result)" + "\n";
					metricList.add("mae");
				}
				case ValidationMetric.MAPE: {
					result += "mape(testY2, result)" + "\n";
					metricList.add("mape");
				}
				default: {
				}
			}
		}
		return result;
	}

	def String metricCodeCrossV() {
		var String result = "";
		numberOfMetric = VMList.size();
		
		for (var i = 0; i < VMList.size(); i++) {
			switch VMList.get(i) {
				case ValidationMetric.MSE: {
					result += "my_array[k," + (i + 1) + "] <- mse(df$" + predictiveColName + "[bloc==k],result)" + "\n";
					metricList.add("mse");
				}
				case ValidationMetric.MAE: {
					result += "my_array[k," + (i + 1) + "] <- mae(df$" + predictiveColName + "[bloc==k],result)" + "\n";
					metricList.add("mae");
				}
				case ValidationMetric.MAPE: {
					result +=
						"my_array[k," + (i + 1) + "] <- mape(df$" + predictiveColName + "[bloc==k],result)" + "\n";
					metricList.add("mape");
				}
				default: {
				}
			}
		}
		return result;
	}

	def void algorithmCodeCrossValid(String predictors) {
		val String metricCodeCrossV = metricCodeCrossV();
		rasCode += "n <- nrow(df)" + "\n";
		rasCode += "K <- " + numRepetitionCross+"\n";
		rasCode += "taille <- n%/%K" + "\n";
		rasCode += "set.seed(5)" + "\n";
		rasCode += "alea <- runif(n)" + "\n";
		rasCode += "rang <- rank(alea)" + "\n";
		rasCode += "bloc <- (rang-1)%/%taille + 1" + "\n";
		rasCode += "bloc <- as.factor(bloc)" + "\n";
		rasCode += "numberOfMetric <-"+numberOfMetric+ "\n";
		rasCode += "my_array <- array(0,dim=c(K," + numberOfMetric + ")) " + "\n";
		rasCode += "for (k in 1:K) {" + "\n";

		switch this.MLA {
			DT: {
				imports += "library(rpart)\n";
				val DTImpl dtImpl = MLA as DTImpl;
				rasCode += "fit <- rpart(" + predictiveColName + "~" + predictors +
					", data = df[bloc!=k,], method = 'class', control = rpart.control(cp = 0";
				if (dtImpl.max_depth !== 0) {
					rasCode += ",maxdepth = " + dtImpl.max_depth;
				}
				rasCode += "))" + "\n";
				rasCode += "result <-predict(fit, df[bloc == k,], type = 'class')" + "\n";
				rasCode += "result <- as.numeric(levels(result))[result]" + "\n";

			}
			SVR: {
				imports += "library(e1071)\n";
				rasCode +=
					"fit <- svm(" + predictiveColName + "~" + predictors + ", data = df[bloc!=k,], method = 'class')" + "\n";
				rasCode += "result<-predict(fit, df[bloc == k,], type = 'class')" + "\n";
			}
			GTB: {
				println("GTB")
			}
			RandomForest: {
				imports += "library(randomForest)\n";
				rasCode +=
					"fit <- randomForest(" + predictiveColName + "~" + predictors +
						", data = df[bloc!=k,], method = 'class')" + "\n";
				rasCode += "result<-predict(fit, df[bloc == k,], type = 'class')" + "\n";
			}
			SGD: {
				imports += "library(sgd)\n";
				rasCode +=
					"fit <- sgd(" + predictiveColName + "~" + predictors + ", data = df[bloc!=k,], method = 'class')" + "\n";
				rasCode += "result<-predict(fit, df[bloc == k,], type = 'class')" + "\n";
			}
			default: {
				println("default")
			}
		}
		rasCode += metricCodeCrossV;
		rasCode += "}" + "\n";

		rasCode += "for (i in 1:numberOfMetric) {" + "\n";
		rasCode += "value <- mean(my_array[,i])" + "\n";
		rasCode += "print(value)" + "\n";
		rasCode += "}" + "\n";
	}

	def void algorithmCode(String predictors) {

		rasCode += "df %>% select(-c("+predictiveColName+"))->X" + "\n";
		rasCode += "df %>% select(c(" + predictiveColName + "))->Y" + "\n";
		rasCode += "sample.split(df$" + predictiveColName + ",SplitRatio=" + split_ratio + ")->split_index" + "\n";
		rasCode += "train<-subset(df,split_index==T)" + "\n";
		rasCode += "test<-subset(df,split_index==F)" + "\n";
		switch this.MLA {
			DT: {
				imports += "library(rpart)\n";
				val DTImpl dtImpl = MLA as DTImpl;
				rasCode += "fit <- rpart(" + predictiveColName + "~" + predictors +
					", data = train, method = 'class', control = rpart.control(cp = 0";
				if (dtImpl.max_depth !== 0) {
					rasCode += ",maxdepth = " + dtImpl.max_depth;
				}
				rasCode += "))" + "\n";
				rasCode += "result<-predict(fit, test, type = 'class')" + "\n";
				rasCode += "result <- as.numeric(levels(result))[result]" + "\n";

			}
			SVR: {
				imports += "library(e1071)\n";
				rasCode +=
					"fit <- svm(" + predictiveColName + "~" + predictors + ", data = train, method = 'class')" + "\n";
				rasCode += "result<-predict(fit, test, type = 'class')" + "\n";
			}
			GTB: {
				println("GTB")
			}
			RandomForest: {
				imports += "library(randomForest)\n";
				rasCode +=
					"fit <- randomForest(" + predictiveColName + "~" + predictors +
						", data = train, method = 'class')" + "\n";
				rasCode += "result<-predict(fit, test, type = 'class')" + "\n";
			}
			SGD: {
				imports += "library(sgd)\n";
				rasCode +=
					"fit <- sgd(" + predictiveColName + "~" + predictors + ", data = train, method = 'class')" + "\n";
				rasCode += "result<-predict(fit, test, type = 'class')" + "\n";
			}
			default: {
				println("default")
			}
		}

		rasCode += "test %>% select(c(" + predictiveColName + "))->testY" + "\n";
		rasCode += "testY2 <- testY[,1:length(testY)]" + "\n";
		rasCode += metricCode();
	}

	def String requiredImports() {
		var String result = "";
		result += "library(dplyr)" + "\n";
		result += "library(caTools)" + "\n";
		result += "library(Metrics)" + "\n";
		return result;
	}

	def String getCsvSeparator(CSVParsingConfiguration csv_separator) {
		if (csv_separator !== null) {
			if (csv_separator.sep === CSVSeparator.SEMI_COLON) {
				return ";";
			}
		}
		return ",";
	}

	def void setPredictiveAndPredictors(RFormula formula) {
		var int predictiveColumn;
		if (formula !== null) {

			if (formula.predictive !== null) {
				predictiveColName = formula.predictive.colName;
				predictiveColumn = formula.predictive.column;

				if (predictiveColName === null) {
					predictiveColName = "colnames(df)[" + predictiveColumn + "]";
				}

			}
			switch formula.predictors {
				AllVariables: {
					predictors = ".";
				}
				PredictorVariables: {
					val PredictorVariables predictorVariables = formula.predictors as PredictorVariables;
					for (FormulaItem formulaItem : predictorVariables.vars) {
						if (formulaItem.colName !== null) {
							predictors += formulaItem.colName + " + ";
						} else {
							predictors += "colnames(df)[" + formulaItem.column + "] + ";
						}
					}
					predictors = predictors.substring(0, predictors.length - 4);
				}
			}
		}
	}

	def String render() {
		// initialise value
		val DataInput dataInput = mmlModel.input;
		val RFormula formula = mmlModel.formula;
		val Validation validation = mmlModel.validation;
		val StratificationMethod stratificationMethod = validation.stratification;
		VMList = validation.metric;
		fileLocation = dataInput.filelocation;

		switch stratificationMethod {
			CrossValidation: {
				val CrossValidation crossValidation = stratificationMethod as CrossValidation;
				numRepetitionCross = crossValidation.number;
			}
			TrainingTest: {
				val TrainingTest trainingTest = stratificationMethod as TrainingTest;
				split_ratio =  trainingTest.number as double / 100.0;
			}
		}

		imports += requiredImports();
		csv_separator = getCsvSeparator(dataInput.parsingInstruction);
		rasCode += "read.csv(\"" + fileLocation + "\",head = TRUE, sep=\"" + csv_separator + "\")->df" + "\n";
		setPredictiveAndPredictors(formula);

		if (numRepetitionCross > 0) {
			algorithmCodeCrossValid(predictors);
		} else {
			algorithmCode(predictors);
		}

		rasCode = imports + rasCode;
		return rasCode;
	}

	def Output compile() {
		val Output result = new Output();
		result.frameworkLang = FrameworkLang.R;
		result.mlAlgorithm = this.MLA;
		val String render = render();
		result.fileLocation = fileLocation;
		val String filePath = "mml.R";
		Files.write(render.getBytes(), new File(filePath));

		val double startTime = System.currentTimeMillis() as double;
		val Process p = Runtime.getRuntime().exec("Rscript " + filePath);
		val double endTime = System.currentTimeMillis() as double;
		val BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		var String line;

		val EList<Double> metricValueList = new UniqueEList<Double>();
		while (( line = in.readLine()) !== null) {
			val String[] compileResult = line.split(" ");
			metricValueList.add(Double.valueOf(compileResult.get(1)));
		}

		for (var i = 0; i < metricValueList.size; i++) {
			result.validationMetric_result.put(metricList.get(i), metricValueList.get(i));
		}
		result.timestamp = endTime - startTime;
		return result;
	}
}
