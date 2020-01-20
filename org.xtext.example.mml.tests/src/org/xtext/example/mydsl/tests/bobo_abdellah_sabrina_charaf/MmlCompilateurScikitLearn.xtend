package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import com.google.common.io.Files
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.List
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
import org.xtext.example.mydsl.mml.XFormula

class MmlCompilateurScikitLearn {
	
	var MMLModel mmlModel;
	var MLAlgorithm mlAlgorithm;
	val String name = "MmlCompilateurScikitLearn";
	
	private new(){
		
	}
	new(MMLModel mmlModel, MLAlgorithm mlAlgorithm) {
		if (mmlModel === null ){
			throw new IllegalArgumentException("you should initialize "+this.name+"with non null value");
		}
		this.mmlModel = mmlModel;
		this.mlAlgorithm = mlAlgorithm;
	}	
	
	def String compileDataInput() {
		
		val DataInput dataInput = mmlModel.input;
		val String fileLocation = dataInput.filelocation;
		var int crossValidationNumber = 0
		
		//Algorithm
		var String algorithmImport="";
		var String algorithmBody="";
		
		val Validation validation = mmlModel.validation;
		val StratificationMethod stratification = validation.stratification;
		//Metric
		val List<ValidationMetric> metrics = validation.metric;
		var double percentageTest = 0.3
		
		
		switch stratification {
			CrossValidation: {
				val CrossValidation crossValidation = stratification as CrossValidation;
				crossValidationNumber = crossValidation.number ;
				algorithmImport += "\nfrom sklearn.model_selection import cross_val_score";
				
			}
			TrainingTest: {
				val TrainingTest trainingTest = stratification as TrainingTest;
				val double percentageTraining = trainingTest.number as double / 100.0;
				percentageTest = 1.0 - percentageTraining;
			}
		}
		
		switch mlAlgorithm {
			DT: {
				algorithmImport += "\nfrom sklearn import tree";
				algorithmBody += "\nclf = tree.DecisionTreeRegressor()";
				algorithmBody += "\nclf.fit(X_train, y_train)";
				if(crossValidationNumber === 0){
					algorithmBody += "\ny_pred =  clf.predict(X_test)"
				}
			}
			RandomForest: {
				algorithmImport += "\nimport numpy as np";
				algorithmImport += "\nfrom sklearn.ensemble import RandomForestRegressor";
				algorithmBody += "\nclf = RandomForestRegressor()";
				algorithmBody += "\nclf.fit(X_train, y_train)";
				if(crossValidationNumber === 0){
					algorithmBody += "\ny_pred = clf.predict(X_test)"
				}
			}
			SVR: {
				algorithmImport += "\nimport numpy as np";
				algorithmImport += "\nfrom sklearn.svm import SVR";
				algorithmBody += "\nclf = SVR()";
				algorithmBody += "\nclf.fit(X_train, y_train)";
				if(crossValidationNumber === 0){
					algorithmBody += "\ny_pred = clf.predict(X_test)"
				}
			}
			SGD: {
				println("SGD")
			}
			GTB: {
				println("GTB")
			}
			default : print("default")
		
		}
		val String trainning = "train_test_split";
		
		// start of Python generation
		//Imports
		var String pythonImport = "import pandas as pd\n";
		pythonImport += algorithmImport+"\n";
		for(ValidationMetric validationMetric: metrics){
			pythonImport += "from sklearn.metrics import "+validationMetric.literal.toString()+"\n";
		}
		pythonImport += "from sklearn.model_selection import "+trainning+"\n";
		//Code
		val String	csvReading = "\nmml_data = pd.read_csv(\""+ fileLocation + "\")";
		var String	pandasCode = pythonImport + csvReading;
		
		//Formula
		val RFormula formula = mmlModel.formula;
		if (formula === null) {
			var String column= "\ncolumn = mml_data.columns[-1]";
			pandasCode += "\n"+column+" \nX = mml_data.drop(columns=[column]) ";
			pandasCode += "\ny = mml_data[column] ";
		} else {
			
			var int predictiveColumn
			var String predictiveColName
			if (formula.predictive !== null) {
				val FormulaItem predictive = formula.predictive;
				if(predictive.column !== 0 ){
					predictiveColumn = predictive.column;
					pandasCode += "y = mml_data.iloc[:, "+predictiveColumn+"].values";
				}
				else if(predictive.colName !== null){
					predictiveColName = formula.predictive.colName;
					pandasCode += "\ny = mml_data['"+predictiveColName+"'] ";
				}
			}
			else{
				var String column= "\ncolumn = mml_data.columns[-1]";
				pandasCode += "\n"+column;
				pandasCode += "\ny = mml_data[column] ";
			}
			val XFormula xformula = formula.predictors;
			switch xformula{
				AllVariables:{
					if (predictiveColumn !== 0){
						pandasCode += "X = mml_data.iloc[:, 0:"+predictiveColumn+"].values";
					}else if(predictiveColName !== null){
						pandasCode += "\ncolumn = \""+predictiveColName+"\"";
						pandasCode += "\nX = mml_data.drop(columns=[column]) ";
					}
					else{
						var String column= "\ncolumn = mml_data.columns[-1]";
						pandasCode += "\n"+column+" \nX = mml_data.drop(columns=[column]) ";
					}
				}
				PredictorVariables:{
					var PredictorVariables	predictorsVariables =  formula.predictors as PredictorVariables;
					val List<FormulaItem> predictorsList = predictorsVariables.vars
					var String column= "\ncolumn = mml_data.columns[-1]";
					pandasCode += "\n"+column+" \nX = mml_data.drop(columns=[column]) ";
					//TODO
				}	
				default : print("default")	
			}
		}
		pandasCode += "\ntest_size = "+ percentageTest ;
		pandasCode += "\nX_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size) ";
		pandasCode += "\n"+algorithmBody;
		
		for(ValidationMetric validationMetric: metrics){
			if(crossValidationNumber === 0){
				pandasCode += "\naccuracy = "+validationMetric.literal.toString()+"(y_test, y_pred)";
				pandasCode += "\nprint('"+validationMetric.literal.toString()+"', accuracy)";
			}
			else{
				pandasCode += "\nscores = cross_val_score(estimator=clf,  X=X, y=y, cv="+crossValidationNumber+",scoring=('neg_"
				+validationMetric.literal.toString()+"'))";
				pandasCode += "\nprint('"+validationMetric.literal.toString()+"', abs(scores.mean()))";
			}
			
		}
				
		Files.write(pandasCode.getBytes(), new File("mml.py"));
		// end of Python generation
		
		return pandasCode;
	}
	
	def Output compile(){
		val Output result = new Output();
		result.frameworkLang = FrameworkLang.SCIKIT;
		result.mlAlgorithm = this.mlAlgorithm;
		val DataInput dataInput = mmlModel.input;
		result.fileLocation = dataInput.filelocation;
		val String render = compileDataInput();
		
		Files.write(render.getBytes(), new File("mml.py"));
		
		val long startTime = System.currentTimeMillis() as long;
		val Process p = Runtime.getRuntime().exec("python mml.py");
		val long endTime = System.currentTimeMillis() as long;
		val BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		var String line;
		while ((line = in.readLine()) !== null) {
			var String[] output = line.split(',');
			val String metricName = output.get(0).replace("(","").replace("'","");
			val String value = output.get(1).replace(")","");
			result.validationMetric_result.put(metricName,Double.valueOf(value));
	    }
		result.timestamp = endTime - startTime;
		return result;
	}
}