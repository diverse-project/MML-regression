package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import com.google.common.io.Files
import com.google.inject.Inject
import java.io.File
import org.eclipse.emf.common.util.EList
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.xtext.example.mydsl.mml.AllVariables
import org.xtext.example.mydsl.mml.DT
import org.xtext.example.mydsl.mml.DataInput
import org.xtext.example.mydsl.mml.FormulaItem
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
import org.xtext.example.mydsl.mml.Validation
import org.xtext.example.mydsl.mml.ValidationMetric
import org.xtext.example.mydsl.mml.impl.DTImpl
import org.xtext.example.mydsl.tests.MmlInjectorProvider
import org.xtext.example.mydsl.mml.CrossValidation
import org.xtext.example.mydsl.mml.TrainingTest

@ExtendWith(InjectionExtension)
@InjectWith(MmlInjectorProvider)
class MmlCompilateurR {

	@Inject
	ParseHelper<MMLModel> parseHelper

	@Test
	def void mmlcomp() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework scikit-learn
				algorithm DT
				formula "medv" ~ .
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error
		''')

		val DataInput dataInput = result.input;
		val EList<MLChoiceAlgorithm> mlA = result.algorithms
		val RFormula formula = result.formula;
		val Validation validation = result.validation;
		
		
		val StratificationMethod stratificationMethod = validation.stratification;
		val EList<ValidationMetric> eList = validation.metric;
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
		rasCode += "sample.split(df$" + predictiveColName + ",SplitRatio=" + split_ratio + ")->split_index"+"\n";
		rasCode += "train<-subset(df,split_index==T)" + "\n";
		rasCode += "test<-subset(df,split_index==F)" + "\n";

		switch mlA {
			DT: {
				imports += "library(rpart)\n";
				val DTImpl dtImpl = mlA as DTImpl;
				if (predictiveColName !== null) {
					rasCode += "fit <- rpart(" + predictiveColName + "~" + predictors +
						", data = train, method = 'class', control = rpart.control(cp = 0";
					if (dtImpl.max_depth !== 0) {
						rasCode += ",maxdepth = " + dtImpl.max_depth;
					}
					rasCode += "))" + "\n";
				}
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
		// visitor.visit(mlA);		
		/*
		 * read.csv("https://raw.githubusercontent.com/acherm/teaching-MDE1920/master/boston/boston.csv")->df
		 * View(df)
		 * library(dplyr)
		 * df %>% select(-c(medv))->X
		 * View(X)
		 * df %>% select(c(medv))->Y
		 * View(Y)
		 * #Spliting Data
		 * library(caTools)
		 * sample.split(df$medv,SplitRatio=0.7)->split_index
		 * train<-subset(df,split_index==T)
		 * test<-subset(df,split_index==F)
		 * test %>% select(-c(medv))->testX
		 * test %>% select(c(medv))->testY
		 * View(train)
		 * View(test)
		 * nrow(train)
		 * nrow(test)
		 * #training
		 * library(rpart)
		 * library(rpart.plot)
		 * fit <- rpart(medv~., data = train, method = 'class')
		 * rpart.plot(fit)
		 * summary(fit)
		 * result<-predict(fit, test, type = 'class')
		 * #table_mat <- table(testY, result)
		 * #table_mat
		 * library(Metrics)
		 * View(testY)
		 * testY2 <- testY[1:length(testY)]
		 * View(testY2)
		 * View(result)
		 * #mse(testY, result)
		 */
		rasCode = imports + rasCode;
		rasCode += "test %>% select(c("+predictiveColName+"))->testY"+"\n";
		Files.write(rasCode.getBytes(), new File("mml.R"));
	}

}
