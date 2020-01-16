package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import com.google.common.io.Files
import com.google.inject.Inject
import java.io.File
import java.util.List
import java.util.Map
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.xtext.example.mydsl.mml.DataInput
import org.xtext.example.mydsl.mml.MLAlgorithm
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm
import org.xtext.example.mydsl.mml.MMLModel
import org.xtext.example.mydsl.mml.RFormula
import org.xtext.example.mydsl.mml.impl.AlgorithmVisitorImpl
import org.xtext.example.mydsl.tests.MmlInjectorProvider

@ExtendWith(InjectionExtension)
@InjectWith(MmlInjectorProvider)
class MmlCompilateurR {

	@Inject
	static ParseHelper<MMLModel> parseHelper

	@Test
	def void mmlcomp() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework scikit-learn
				algorithm DT
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error
		''')
		val DataInput dataInput = result.input;
		val String fileLocation = dataInput.filelocation;

		val String importDPLYR = "library(dplyr)";
		val String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		val String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		var String rasCode = "read.csv(\"" + fileLocation + ",head = TRUE, sep=\"" + csv_separator + "\"\")->df" +
			"\n" + importDPLYR + "\n";
		val MLChoiceAlgorithm MLCAlgorithm = result.algorithm

		val RFormula formula = result.formula;
		if (formula === null) {
			rasCode += "df %>% select(-c())->X " + "\n";
		} else {
			val String predictiveColName = formula.predictive.colName;
			val int predictiveColumn = formula.predictive.column;

			if (predictiveColName !== null) {
				rasCode += "df %>% select(-c(" + predictiveColName + "))->X " + "\n";
			} else {
				rasCode += "df %>% select(-c(" + predictiveColumn + "))->X " + "\n";
			}
		}

		val MLAlgorithm mlA = MLCAlgorithm.algorithm;
		val Map<String,List<String>> map = mlA.accept(new AlgorithmVisitorImpl("R"));
		//visitor.visit(mlA);		

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
		Files.write(rasCode.getBytes(), new File("mml.R"));
	}
	
}
