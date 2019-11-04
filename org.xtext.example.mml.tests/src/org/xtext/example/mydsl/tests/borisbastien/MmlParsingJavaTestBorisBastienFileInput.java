package org.xtext.example.mydsl.tests.borisbastien;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.AllVariables;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.XFormula;

import com.google.common.io.Files;
import com.google.inject.Inject;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingJavaTestBorisBastienFileInput {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	@Test
	public void loadModel() throws Exception {
		
		MMLModel result = parseHelper.parse(new String(java.nio.file.Files.readAllBytes(Paths.get("BostonTest.Mml"))));
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());			
		
	}		
	
	@Test
	public void compileDataInput() throws Exception {
		MMLModel result = parseHelper.parse(new String(java.nio.file.Files.readAllBytes(Paths.get("BostonTest2.Mml"))));
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		MLChoiceAlgorithm mlchoicealgo = result.getAlgorithm();
		Validation validation = result.getValidation();
		RFormula formule = result.getFormula();
		
		String pythonImport = "import pandas as pd\n"; 
		
		//csv
		String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
		if (parsingInstruction != null) {			
			System.err.println("parsing instruction..." + parsingInstruction);
			csv_separator = parsingInstruction.getSep().toString();
		}
		String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator) + ")\n";						
		
		//formule : recuperation des champs du csv à garder
		String csvSplit="";
		if(formule!=null) {
			XFormula xformule = formule.getPredictors();
			FormulaItem formuleItem = formule.getPredictive();
			String items="";
			if(xformule instanceof PredictorVariables) {
				List<FormulaItem> predictorItems = ((PredictorVariables) xformule).getVars();
				StringBuilder sb = new StringBuilder();
				if(predictorItems!=null && !predictorItems.isEmpty()) {
					if(predictorItems.get(0).getColName() != null) {
						for(FormulaItem item : predictorItems) {
							if(predictorItems.get(0)!=item)sb.append(',');
							sb.append(item.getColName());
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
				csvSplit+="y = df[columns = [df.columns[-1]]]\n";
			}
		}else {
			//if formule is null, all the fields are used to predict the last one
			csvSplit+="X = df.drop(columns = [df.columns[-1]])\n";
			csvSplit+="y = df[columns = [df.columns[-1]]]\n";
		}
		
		//algo
		FrameworkLang framework = mlchoicealgo.getFramework();
		MLAlgorithm algo = mlchoicealgo.getAlgorithm();
		String algoDeclaration="";
		if(algo instanceof SVR) {
			//TODO complete with same template as DT below
		}else if(algo instanceof DT) { //DecisionTree
			pythonImport+="from sklearn import tree\n";
			algoDeclaration = "clf = tree.DecisionTreeRegressor()\n";
		}//TODO other algos
		
		//validation
		StratificationMethod stratMethod = validation.getStratification();
		EList<ValidationMetric> validMetrics = validation.getMetric();
		int number = stratMethod.getNumber();
		String validationPrint="";
		validationPrint += "test_size = "+number+"\n";
		if(stratMethod instanceof CrossValidation){
			//TODO equivalent à ci-dessous
		}else if(stratMethod instanceof TrainingTest){
			pythonImport+="from sklearn.model_selection import train_test_split\n";
			validationPrint+="X_train, X_test, y_train, y_test = train_test_split(X, y, test_size="+number+")"; 
		}
		validationPrint+="\n";
		
		//ValidationMetric
		String metrics="";
		String metricsResult="";
		for(int i=0;i<validMetrics.size();i++) {
			String metric = "accuracy"+i+"=";
			switch(validMetrics.get(i).name()) {
				case "MSE":
					pythonImport+="from sklearn.metrics import mean_squared_error\n";
					metric+="mean_squared_error(y_test, clf.predict(X_test))";
					break;
				//TODO other types
				default:
					break;
			}
			metricsResult+="print(accuracy"+i+")\n";
			metrics+=metric+"\n";
		}
		
		String pandasCode = pythonImport + csvReading + csvSplit + algoDeclaration+ validationPrint+ metrics+metricsResult;
		
		pandasCode += "\nprint (mml_data)\n";
		
		Files.write(pandasCode.getBytes(), new File("mml.py"));
		// end of Python generation
		
		
		/*
		 * Calling generated Python script (basic solution through systems call)
		 * we assume that "python" is in the path
		 */
		Process p = Runtime.getRuntime().exec("python mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line; 
		while ((line = in.readLine()) != null) {
			System.out.println(line);
	    }

		
		
	}

	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}


}
