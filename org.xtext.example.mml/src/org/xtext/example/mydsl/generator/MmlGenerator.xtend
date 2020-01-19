/*
 * generated by Xtext 2.19.0
 */
package org.xtext.example.mydsl.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import org.xtext.example.mydsl.mml.FrameworkLang
import org.xtext.example.mydsl.mml.*

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
class MmlGenerator extends AbstractGenerator {

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {
		val dataInputIterator = resource.allContents.filter(DataInput)
		val dataInput = dataInputIterator.next
		val fileLocation = dataInput.filelocation
		val mlAlgoIterator = resource.allContents.filter(MLChoiceAlgorithm)
		val mlchoicealgo = mlAlgoIterator.next
		val validationIterator = resource.allContents.filter(Validation)
		val validation = validationIterator.next
		val formuleIterator = resource.allContents.filter(RFormula)
		val formule = if(formuleIterator.hasNext)formuleIterator.next
		
		
		//csv
		val DEFAULT_COLUMN_SEPARATOR = ","; // by default
		var csv_separator = DEFAULT_COLUMN_SEPARATOR;
		val parsingInstruction = dataInput.getParsingInstruction();
		if (parsingInstruction !== null) {			
			//System.err.println("parsing instruction..." + parsingInstruction);
			csv_separator = parsingInstruction.getSep().toString();
		}
		
		val framework = mlchoicealgo.getFramework
		var code=""
		var type=""
		
		switch(framework.getValue()) {
			case FrameworkLang.JAVA_WEKA_VALUE:
				code =compileWeka(formule, fileLocation, mlchoicealgo, validation, csv_separator)
			case FrameworkLang.SCIKIT_VALUE:
				code=compileScikit(formule, fileLocation, mlchoicealgo, validation, csv_separator)
			case FrameworkLang.R_VALUE:
				code=compileR(formule, fileLocation, mlchoicealgo, validation, csv_separator)
			case FrameworkLang.XG_BOOST_VALUE:
				code=compileXG(formule, fileLocation, mlchoicealgo, validation, csv_separator)
			default:code=""
		}
		switch(framework.getValue()) {
			case FrameworkLang.JAVA_WEKA_VALUE:type="java"
			case FrameworkLang.SCIKIT_VALUE:type="py"
			case FrameworkLang.R_VALUE:type="r"
			case FrameworkLang.XG_BOOST_VALUE:type="py"
			default:type=""
		}
        fsa.generateFile(
           resource.URI.lastSegment.replace("mml",type), code
        )
        
	}
	
	def String compileWeka(RFormula formule, String fileLocation, MLChoiceAlgorithm mlchoicealgo, Validation validation, String csv_separator) {
		
	}
	
	def String compileXG(RFormula formule, String fileLocation, MLChoiceAlgorithm mlchoicealgo, Validation validation, String csv_separator) {
		
	}
	
	def String compileR(RFormula formule, String fileLocation, MLChoiceAlgorithm mlchoicealgo, Validation validation, String csv_separator) {
		//TODO adapt for R using RStudio
		//TODO R import needed?
		var rImport= "library(data.table)\n";
		//formule : recuperation des champs du csv à garder
		var csvSplit="";
		if(formule!==null) {
			var xformule = formule.getPredictors();
			var formuleItem = formule.getPredictive();
			var items="";
			if(xformule instanceof PredictorVariables) {
				var predictorItems = xformule.getVars();
				var sb = new StringBuilder();
				if(predictorItems!==null && !predictorItems.isEmpty()) {
					if(predictorItems.get(0).getColName() !== null) {
						for(FormulaItem item : predictorItems) {
							if(predictorItems.get(0)!=item)sb.append(',')
							sb.append(mkValueInDoubleQuote(item.getColName()))
						}
					}else {
						for(FormulaItem item : predictorItems) {
							if(predictorItems.get(0)!=item)sb.append(',');
							sb.append(item.getColumn());
						}
					}
						
				}
				items = sb.toString(); 						
				
				csvSplit+="X <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+", select=c("+items+") ,header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\n";
				csvSplit+="y <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+", select=c("+formuleItem.getColumn()+") ,header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\\n";
			}else if(xformule instanceof AllVariables) {
				csvSplit+="myFile <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+",header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\n";
				csvSplit+="h<-head(myFile)\nlastcol <- tail(h, n=1)\n";
				csvSplit+="X <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+", drop=c(lastcol) ,header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\n";
				csvSplit+="y <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+", select=c(lastcol) ,header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\n";
			}
		}else {
			//if formule is null, all the fields are used to predict the last one
			csvSplit+="myFile <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+",header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\n";
			csvSplit+="h<-head(myFile)\nlastcol <- tail(h, n=1)\n";
			csvSplit+="X <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+", drop=c(lastcol) ,header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\n";
			csvSplit+="y <- read.csv(file="+mkValueInDoubleQuote(fileLocation)+", select=c(lastcol) ,header=TRUE, sep="+mkValueInDoubleQuote(csv_separator)+")\n";
		}
		
		//algo
		val algo = mlchoicealgo.getAlgorithm();
		var algoDeclaration="";
		if(algo instanceof SVR) {
			//TODO complete with same template as DT below
		}else if(algo instanceof DT) { //DecisionTree
			rImport+="library(rpart)\n";
			algoDeclaration = "fit <- rpart(survived~., data= data_train, method='class')\n";
		}//TODO other algos
		
		//validation
		val stratMethod = validation.getStratification();
		val validMetrics = validation.getMetric();
		val number = stratMethod.getNumber();
		var validationPrint="";
		validationPrint += "test_size = "+number+"\n";
		validationPrint += "n_row = nrow(X)\n";
		validationPrint += "total_row = test_size * n_row\n";
		if(stratMethod instanceof CrossValidation){
			//TODO equivalent à ci-dessous
		}else if(stratMethod instanceof TrainingTest){
			validationPrint+="X_train <- 1:total_row\n"; 
			validationPrint+="X_train, X_test, y_train, y_test = train_test_split(X, y, test_size="+number+")"; 
		}
		validationPrint+="\n";
		
		//ValidationMetric
		var metrics="";
		var metricsResult="";
		for(var i=0;i<validMetrics.size();i++) {
			var metric = "";
			/*switch(validMetrics.get(i).name()) {
				case "MSE":
					rImport+="from sklearn.metrics import mean_squared_error\n"
					metric+="accuracy"+i+"=mean_squared_error(y_test, clf.predict(X_test))"
				case "MAE":
					rImport+="from sklearn.metrics import mean_absolute_error\n";
					metric+="accuracy"+i+"=mean_absolute_error(y_test, clf.predict(X_test))"
				case "MAPE":
					rImport+="from sklearn.utils import check_arrays\n";
					metric+="y_test, y_pred = check_arrays(y_test, clf.predict(X_test))";
					metric+="accuracy"+i+"=np.mean(np.abs((y_test - y_pred) / y_test)) * 100";
				default:
			}*/
			metricsResult+="print(accuracy"+i+")\n";
			metrics+=metric+"\n";
		}
		
		val pandasCode = rImport + csvSplit + algoDeclaration+ validationPrint+ metrics+metricsResult;
		
		return pandasCode;
	}
	
	def String compileScikit(RFormula formule, String fileLocation, MLChoiceAlgorithm mlchoicealgo, Validation validation, String csv_separator) {
		
		var pythonImport = "import pandas as pd\n"
		var csvReading = "df = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator) + ")\n";						
		
		//formule : recuperation des champs du csv à garder
		var csvSplit="";
		if(formule!==null) {
			var xformule = formule.getPredictors();
			var formuleItem = formule.getPredictive();
			var items="";
			if(xformule instanceof PredictorVariables) {
				var predictorItems = xformule.getVars();
				var sb = new StringBuilder();
				if(predictorItems!==null && !predictorItems.isEmpty()) {
					if(predictorItems.get(0).getColName() !== null) {
						for(FormulaItem item : predictorItems) {
							if(predictorItems.get(0)!=item)sb.append(',');
							sb.append("'"+item.getColName()+"'");
						}
					}else {
						for(FormulaItem item : predictorItems) {
							if(predictorItems.get(0)!=item)sb.append(',');
							sb.append(item.getColumn());
						}
					}
						
				}
				items = sb.toString();
				csvSplit+="X = df[df.columns.difference(["+items+"])]\n";
				csvSplit+="y = df[columns = [df.columns["+formuleItem.getColumn()+"]]]\n";
			}else if(xformule instanceof AllVariables) {
				csvSplit+="X = df.drop(columns = [df.columns[-1]])\n";
				csvSplit+="y = df[[df.columns[-1]]]\n";
			}
		}else {
			//if formule is null, all the fields are used to predict the last one
			csvSplit+="X = df.drop(columns = [df.columns[-1]])\n";
			csvSplit+="y = df[[df.columns[-1]]]\n";
		}
		
		//algo
		var algo = mlchoicealgo.getAlgorithm();
		var algoDeclaration="";
		if(algo instanceof SVR) {
			pythonImport+="from sklearn.svm import SVR\n";
			algoDeclaration = "clf = SVR("+ (algo.c!==null?"C="+algo.c+",":"") + (algo.kernel!==null?"kernel='"+algo.kernel+"'":"") +")\n";
		}else if(algo instanceof DT) { //DecisionTree
			pythonImport+="from sklearn import tree\n";
			algoDeclaration = "clf = tree.DecisionTreeRegressor(max_depth="+algo.max_depth+")\n";
		}else if(algo instanceof SGD){
			pythonImport+="from sklearn.linear_model import SGDClassifier\n";
			algoDeclaration = "clf = tree.SGDClassifier()\n";
		}else if(algo instanceof GTB){
			pythonImport+="from sklearn import ensemble\n";
			algoDeclaration = "clf = ensemble.GradientBoostingRegressor()\n";
		}else if(algo instanceof RandomForest){
			pythonImport+="from sklearn import ensemble\n";
			algoDeclaration = "clf = ensemble.RandomForest"+(algo.type==TYPE.CLASSIFIER?"Classifier":"Regressor")
						+ "(max_depth="+ algo.max_depth 
						+ ", n_estimators=" + algo.n_estimators
						+ ")\n";
		}
		
		//ValidationMetric
		var metrics=""
		var metricsResult=""
		
		//validation
		var stratMethod = validation.getStratification()
		var validMetrics = validation.getMetric()
		var number = stratMethod.getNumber()
		var validationPrint=""
		validationPrint += "split_size = "+number+"\n"
		
		if(stratMethod instanceof CrossValidation){
			pythonImport+="from sklearn.model_selection import cross_validate\n"
			var metricList=""
			for(var i=0;i<validMetrics.size();i++) {
				val metricName = validMetrics.get(i).name()
				if(metricName=="MSE"){
					metricList+="'neg_mean_squared_error',"
				}else if(metricName=="MAE"){
					metricList+="'neg_mean_absolute_error',"
				}else if(metricName=="MAPE"){
					//metricList+="'neg_mean_absolute_error',"
				}
			}
			metricList = metricList.substring(0, metricList.length() - 1)
			validationPrint+="scores = cross_validate(clf, X, y, scoring=("+metricList+") ,cv="+ number +")\n"
			for(var i=0;i<validMetrics.size();i++) {
				val metricName = validMetrics.get(i).name()
				if(metricName=="MSE"){
					metrics+="accuracyMSE"+i+"=scores['test_neg_mean_squared_error']\n"
					metricsResult+="print(sum(accuracy"+i+")/split_size)\n"
				}else if(metricName=="MAE"){
					metrics+="accuracyMAE"+i+"=scores['test_neg_mean_absolute_error']\n"
					metricsResult+="print(sum(accuracy"+i+")/split_size)\n"
				}else if(metricName=="MAPE"){
					//TODO
					//metrics+="accuracyMAPE"+i+"=scores['test_neg_mean_squared_error']\n"
					//metricsResult+="print(sum(accuracy"+i+")/split_size)\n"
					metrics+="#Not developed"
				}
			}
			if(validMetrics.size()==1){
				metrics = metrics.replaceAll("'[a-z_]*'","'test_score'");
			}
		}
		
		else if(stratMethod instanceof TrainingTest){
			pythonImport+="from sklearn.model_selection import train_test_split\n"
			validationPrint+="X_train, X_test, y_train, y_test = train_test_split(X, y, test_size="+number+")\n"
			//Fit
			validationPrint+="clf.fit(X_train, y_train)\n"
			//metrics
			for(var i=0;i<validMetrics.size();i++) {
				var metric = "";
				val metricName = validMetrics.get(i).name()
				if(metricName=="MSE"){
					pythonImport+="from sklearn.metrics import mean_squared_error\n"
					metric+="accuracyMSE"+i+"=mean_squared_error(y_test, clf.predict(X_test))"
				}else if(metricName=="MAE"){
					pythonImport+="from sklearn.metrics import mean_absolute_error\n"
					metric+="accuracyMAE"+i+"=mean_absolute_error(y_test, clf.predict(X_test))"
				}else if(metricName=="MAPE"){
					pythonImport+="import numpy as np\n"
					metric+="y_test, y_pred = np.array(y_true), np.array(clf.predict(X_test))\n"
					metric+="accuracyMAPE"+i+"=np.mean(np.abs((y_test - y_pred) / y_test)) * 100"
				}
				metricsResult+="print(accuracy"+i+")\n"
				metrics+=metric+"\n"
			}
		}
		
		validationPrint+="\n"

		var pandasCode = pythonImport + csvReading + csvSplit + algoDeclaration+ validationPrint +metrics+metricsResult
		return pandasCode
	}
	
	def String mkValueInSingleQuote(String value) {
		return "'" + value + "'";
	}
	
	def String mkValueInDoubleQuote(String value) {
		return "\"" + value + "\"";
	}
}
