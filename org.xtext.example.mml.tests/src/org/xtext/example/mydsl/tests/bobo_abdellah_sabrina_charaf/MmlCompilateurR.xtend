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

class MmlCompilateurR {

	var MMLModel mmlModel;
	var MLAlgorithm MLA;
	val String name = "MmlCompilateurR";
	var String imports = "";
	var String rasCode = "";
	val EList<String> metricList = new UniqueEList<String>();
	var String fileLocation = "";

	private new() {
	}

	new(MMLModel mmlModel, MLAlgorithm MLA) {
		if (mmlModel === null) {
			throw new IllegalArgumentException("you should initialize " + this.name + "with non null value");
		}
		this.mmlModel = mmlModel;
		this.MLA = MLA;
	}

	def String metricCode(EList<ValidationMetric> VMList) {
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

	def void algorithmCode(String predictiveColName, String predictors) {
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
				rasCode += "result1<-predict(fit, test, type = 'class')" + "\n";
				rasCode += "result <- as.numeric(levels(result1))[result1]" + "\n";

			}
			SVR: {
				println("SVR")
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
				println("SGD")
			}
			default: {
				println("default")
			}
		}
	}

	def String render() {
		val DataInput dataInput = mmlModel.input;
		val RFormula formula = mmlModel.formula;
		val Validation validation = mmlModel.validation;

		val StratificationMethod stratificationMethod = validation.stratification;
		val EList<ValidationMetric> VMList = validation.metric;
		fileLocation = dataInput.filelocation;
		var double split_ratio = 0.7;

		switch stratificationMethod {
			CrossValidation: {
			}
			TrainingTest: {
				val TrainingTest trainingTest = stratificationMethod as TrainingTest;
				split_ratio = trainingTest.number;
			}
		}
		
		imports += "library(dplyr)" + "\n";
		imports += "library(caTools)" + "\n";
		imports += "library(Metrics)" + "\n";
		var String predictiveColName = "colnames(df)[ncol(df)-1]";
		var int predictiveColumn;
		var String predictors = "."; // by default
		val String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		val String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		rasCode += "read.csv(\"" + fileLocation + "\",head = TRUE, sep=\"" + csv_separator + "\")->df" + "\n";

		var String selectX = "df %>% select(-c())->X" + "\n";
		var String selectY = "df %>% select(c())->Y" + "\n";
		if (formula !== null) {
			if (formula.predictive !== null) {
				predictiveColName = formula.predictive.colName;
				predictiveColumn = formula.predictive.column;

				if (predictiveColName !== null) {
					selectX = "df %>% select(-c(" + predictiveColName + "))->X" + "\n";
				} else {
					selectY = "df %>% select(-c(colnames(df)[" + predictiveColumn + "]))->X" + "\n";
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

		rasCode += selectX + selectY;
		rasCode += "sample.split(df$" + predictiveColName + ",SplitRatio=" + split_ratio + ")->split_index" + "\n";
		rasCode += "train<-subset(df,split_index==T)" + "\n";
		rasCode += "test<-subset(df,split_index==F)" + "\n";

		algorithmCode(predictiveColName, predictors);

		rasCode = imports + rasCode;
		rasCode += "test %>% select(c(" + predictiveColName + "))->testY" + "\n";
		rasCode += "testY2 <- testY[,1:length(testY)]" + "\n";

		rasCode += metricCode(VMList);
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
		
		for (var i = 0; i< metricValueList.size; i++){
			result.validationMetric_result.put(metricList.get(i),metricValueList.get(i));
		}
		result.timestamp = endTime - startTime;
		return result;
	}
}
