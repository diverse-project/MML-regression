package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import com.google.common.io.Files
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.List
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
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm
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

	private new() {
	}

	new(MMLModel mmlModel, MLAlgorithm MLA) {
		if (mmlModel === null) {
			throw new IllegalArgumentException("you should initialize " + this.name + "with non null value");
		}
		this.mmlModel = mmlModel;
		this.MLA = MLA;
	}

	def EList<MLAlgorithm> removeDuplicate(EList<MLChoiceAlgorithm> input) {
		val EList<MLAlgorithm> result = new UniqueEList<MLAlgorithm>();
		val List<String> list = new ArrayList<String>();

		for (MLChoiceAlgorithm item : input) {
			val MLAlgorithm MLA = item.algorithm;
			if (!list.contains(MLA.class.simpleName)) {
				list.add(MLA.class.simpleName);
				result.add(MLA);
			}
		}
		return result;
	}

	def String render() {
		val DataInput dataInput = mmlModel.input;
		val EList<MLChoiceAlgorithm> MLCAList = mmlModel.algorithms
		val RFormula formula = mmlModel.formula;
		val Validation validation = mmlModel.validation;

		val StratificationMethod stratificationMethod = validation.stratification;
		val EList<ValidationMetric> VMList = validation.metric;
		val String fileLocation = dataInput.filelocation;
		var double split_ratio = 0.7;

		switch stratificationMethod {
			CrossValidation: {
			}
			TrainingTest: {
				val TrainingTest trainingTest = stratificationMethod as TrainingTest;
				split_ratio = trainingTest.number;
			}
		}

		var String imports = "library(dplyr)" + "\n";
		imports += "library(caTools)" + "\n";
		imports += "library(Metrics)" + "\n";
		var String predictiveColName = "colnames(df)[ncol(df)-1]";
		var int predictiveColumn;
		var String predictors = "."; // by default
		val String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		val String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		var String rasCode = "read.csv(\"" + fileLocation + "\",head = TRUE, sep=\"" + csv_separator + "\")->df" + "\n";

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

		val EList<MLAlgorithm> MLAList = removeDuplicate(MLCAList);
		for (MLAlgorithm MLA : MLAList) {
			switch MLA {
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
					println("RandomForest")
				}
				SGD: {
					println("SGD")
				}
				default: {
					println("default")
				}
			}
		}

		rasCode = imports + rasCode;
		rasCode += "test %>% select(c(" + predictiveColName + "))->testY" + "\n";
		rasCode += "testY2 <- testY[,1:length(testY)]" + "\n";

		for (ValidationMetric item : VMList) {
			switch item {
				case ValidationMetric.MSE: {
					rasCode += "mse(testY2, result)";
				}
				case ValidationMetric.MAE: {
					rasCode += "mae(testY2, result)";
				}
				case ValidationMetric.MAPE: {
					rasCode += "mape(testY2, result)";
				}
				default: {
				}
			}
		}
		return rasCode;
	}

	def Output compile() {
		val Output result = new Output();
		result.frameworkLang = FrameworkLang.R;
		result.mlAlgorithm = this.MLA;
		val String render = render();
		val String filePath = "./src/org/xtext/example/mydsl/tests/bobo_abdellah_sabrina_charaf/mml.R";
		Files.write(render.getBytes(), new File(filePath));
		val Process p = Runtime.getRuntime().exec("Rscript " + filePath);

		val BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		var String line;

		while (( line = in.readLine()) !== null) {
			System.out.println(line);
		}
		return result;
	}
}
