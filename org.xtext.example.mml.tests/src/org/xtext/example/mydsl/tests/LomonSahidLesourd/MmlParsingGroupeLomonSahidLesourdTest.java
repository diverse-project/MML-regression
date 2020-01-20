package org.xtext.example.mydsl.tests.LomonSahidLesourd;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.impl.DTImpl;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.common.io.Files;
import com.google.inject.Inject;


@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingGroupeLomonSahidLesourdTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "mean_absolute_error\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());			
		
	}		
	
	@Test
	public void compileDataInput() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo2.csv\" separator ;\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "mean_absolute_error\n"
				+ "");
		
		// on place chaque éléments du MMLModel dans des variables qui sont passées en param de pythonGen
		// autre possibilité, passer uniquement le MMLModel en parametre
		DataInput dataInput = result.getInput();
		Validation validation = result.getValidation();
		EList<MLChoiceAlgorithm> algorithm = result.getAlgorithms();
		RFormula formula = result.getFormula();
		// le nom de l'agorithme de regression choisi est defini par la classe de de "algorithm"
		String algo = "";
		if(algorithm.get(0).getAlgorithm() instanceof DTImpl)
			algo = "DecisionTreeRegressor";
		String fileLocation = dataInput.getFilelocation();
		pythonGen(dataInput, fileLocation, validation, algorithm, algo, formula);
	
		
		
		
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
	
	private void pythonGen (DataInput dataInput, String fileLocation, Validation validation, EList<MLChoiceAlgorithm> algorithm, String algo, RFormula formula) throws IOException {
		// on import toutes les bibliothèques python qui peuvent être utilisées
		// amélioration possible : importer uniquement les bibliothèques nécessaires (voir l.97)
		String pythonImport = "#Importing python modules\n" + 
				"import pandas as pd\n" + 
				"from sklearn.model_selection import train_test_split, cross_validate\n" + 
				"from sklearn.tree import DecisionTreeRegressor, DecisionTreeClassifier, ExtraTreeClassifier, ExtraTreeRegressor\n" + 
				"from sklearn.metrics import mean_absolute_error, mean_squared_error\n" + 
				"from sklearn.metrics import " + validation.getMetric().get(0).getLiteral() + "\n"; 
		String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
		if (parsingInstruction != null) {			
			System.err.println("parsing instruction..." + parsingInstruction);
			csv_separator = parsingInstruction.getSep().toString();
		}
		// on commence par récupérer les valeurs dans le csv
		String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" + mkValueInSingleQuote(csv_separator) + ")\n";						
		// on regroupe les imports et la lecture de csv dans un string
		String pandasCode = pythonImport + csvReading;
		
		// on determine quelle colonne contient les valuers à prédire
		// les autres sont alors les données d'entrée
		String resultCol = "medv";
		if(formula != null) {
			if(formula.getPredictive() != null) {
				if(formula.getPredictive().getColName() != null) {
					resultCol = formula.getPredictive().getColName();
				}
			}
		}
		double trainSize = validation.getStratification().getNumber();
		double testSize = 1-(trainSize/100);
		// on sépare ensuite les valeurs qui vont servir à l'entrainement
		// et celles qui vont servir au test
		String splitingCode = "X = mml_data.drop(columns=[\""+ resultCol +"\"])\n";
		splitingCode += "y = mml_data[\""+ resultCol +"\"]\n";
		splitingCode += "test_size = " + testSize +"\n";
		splitingCode += "X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)\n";
		
		// on mesure ensuite la précision du resultat obtenu
		String accuracyMethod = validation.getMetric().get(0).getLiteral();
		String mlCode = "clf = " + algo + "()\n";
		mlCode += "clf.fit(X_train, y_train)\n";
		mlCode += "accuracy = " + accuracyMethod + "(y_test, clf.predict(X_test))\n";
		mlCode += "print(\"result : \"+str(accuracy))\n";
		
		// finalement on regroupe les string pour générer le script python complet
		String code = pandasCode + splitingCode + mlCode;
		Files.write(code.getBytes(), new File("mml.py"));
		// end of Python generation
	}
	
	@Test
	public void loadThreeAlgos() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"boston.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm RF\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm GradientBoostingRegressor\n"
				+ "CrossValidation { numRepetitionCross 5 }\n"
				+ "mean_absolute_percentage_error\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("boston.csv", result.getInput().getFilelocation());			
		
	}		
	
	@Test
	public void loadMultiThreeAlgos() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"boston.csv\"\n"
				+ "mlframework R\n"
				+ "algorithm DT\n"
				+ "mlframework R\n"
				+ "algorithm RF\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm GradientBoostingRegressor\n"
				+ "CrossValidation { numRepetitionCross 5 }\n"
				+ "mean_absolute_percentage_error\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("boston.csv", result.getInput().getFilelocation());			
		
	}		

	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}


}

