package org.xtext.example.mydsl.tests

import com.google.inject.Inject
import org.eclipse.xtext.testing.util.ParseHelper
import org.xtext.example.mydsl.mml.*
import com.google.common.io.Files
import java.io.File

class MmlCompilateurR {
	
	@Inject
	static ParseHelper<MMLModel> parseHelper
	
	def static void main(String[] args) {
		
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
		var String rasCode = "read.csv(\""+fileLocation+ ",head = TRUE, sep=\""+csv_separator+"\"\")->df"+ "\n" +
		importDPLYR + "\n";
		val MLChoiceAlgorithm algorithm = result.algorithm
		
		val RFormula formula = result.formula;
		if(formula === null) {
			rasCode = "df %>% select(-c())->X "+ "\n";
		} else {
			val String predictiveColName = formula.predictive.colName;
			val int predictiveColumn = formula.predictive.column;
			
			if (predictiveColName !== null) {
				rasCode = "df %>% select(-c("+predictiveColName+"))->X "+ "\n";
			} else {
				rasCode = "df %>% select(-c("+predictiveColumn+"))->X "+ "\n";
			}
		}
		
		
		/*
		read.csv("https://raw.githubusercontent.com/acherm/teaching-MDE1920/master/boston/boston.csv")->df
		View(df)
		library(dplyr)
		df %>% select(-c(medv))->X
		View(X)
		df %>% select(c(medv))->Y
		View(Y)
		#Spliting Data
		library(caTools)
		sample.split(df$medv,SplitRatio=0.7)->split_index
		train<-subset(df,split_index==T)
		test<-subset(df,split_index==F)
		test %>% select(-c(medv))->testX
		test %>% select(c(medv))->testY
		View(train)
		View(test)
		nrow(train)
		nrow(test)
		#training
		library(rpart)
		library(rpart.plot)
		fit <- rpart(medv~., data = train, method = 'class')
		rpart.plot(fit)
		summary(fit)
		result<-predict(fit, test, type = 'class')
		#table_mat <- table(testY, result)
		#table_mat
		library(Metrics)
		View(testY)
		testY2 <- testY[1:length(testY)]
		View(testY2)
		View(result)
		#mse(testY, result)
		*/
		Files.write(rasCode.getBytes(), new File("mml.py"));
	}
}