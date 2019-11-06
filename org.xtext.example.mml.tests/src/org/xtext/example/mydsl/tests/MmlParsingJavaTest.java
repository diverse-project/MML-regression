package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVMKernel;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;

import com.google.common.io.Files;
import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;

	MMLModel result;
	
	private Map<String,StringBuilder> files_code = new HashMap<String,StringBuilder>();
 
	// Blocks of code
	private StringBuilder imports = new StringBuilder();
	private StringBuilder algorithm = new StringBuilder();
	private StringBuilder predictive = new StringBuilder();
	private StringBuilder validation = new StringBuilder();

	@BeforeEach
	public void init() throws Exception {
		File initialFile = new File("/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/boston.mml");
		InputStream inputStream = new FileInputStream(initialFile);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();
		byte[] byteArray = buffer.toByteArray();

		String text = new String(byteArray, StandardCharsets.UTF_8);
		System.out.println(text);
		result = parseHelper.parse(text);
		inputStream.close();
	}

	@Test
	public void loadModel() throws Exception {
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");
		Assertions.assertEquals("/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/BostonHousing.csv",
				result.getInput().getFilelocation());

	}

	@Test
	public void compileDataInput() throws Exception {
		DataInput dataInput = result.getInput();
		String fileLocation = dataInput.getFilelocation();
		String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		String csv_separator = DEFAULT_COLUMN_SEPARATOR;

		CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
		if (parsingInstruction != null) {
			System.err.println("parsing instruction..." + parsingInstruction);
			csv_separator = parsingInstruction.getSep().toString();
		}
		
		StringBuilder program;
		String filename;
		
		for (MLChoiceAlgorithm algo : result.getAlgorithms()) {
			imports = new StringBuilder();
			algorithm = new StringBuilder();
			predictive = new StringBuilder();
			validation = new StringBuilder();
			generateImports(csv_separator, algo);
			generateAlgorithm(algo);
			generatePredictive(algo);
			generateValidation(algo);
			// Append at the end
			if (algo.getFramework().getName().equals("SCIKIT")) {
				imports.insert(0, "#!/usr/bin/env python2\n");
			}
			program = new StringBuilder();
			program.append(imports.toString());
			program.append(predictive.toString());
			program.append(algorithm.toString());
			program.append(validation.toString());
			filename = algo.getFramework().getName()+"_"+ getName(algo.getAlgorithm()) + 
					(algo.getFramework().getName().equals("SCIKIT") ? ".py" : ".autre");
			files_code.putIfAbsent(filename, program);
		}

		for (Map.Entry<String,StringBuilder> entry : files_code.entrySet()) {
			Files.write(entry.getValue().toString().getBytes(), new File(entry.getKey()));
		}
		// end of Python generation

		/*
		 * Calling generated Python script (basic solution through systems call) we
		 * assume that "python" is in the path
		
		Process p = Runtime.getRuntime().exec("python mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
		 */
	}

	private void generateImports(String csv_separator,MLChoiceAlgorithm algo) {
		if (algo.getFramework().getName().equals("SCIKIT")) {
			pythonImport(csv_separator);
		} else {
			// TODO java R xgboost
		}
		imports.append("\n");
	}

	private void pythonImport(String csv_separator) {
		imports.append("import pandas as pd\n");
		imports.append("df = pd.read_csv(" + simpleQuote(result.getInput().getFilelocation()) + ", sep="
				+ simpleQuote(csv_separator) + ")\n");
	}

	private String simpleQuote(String val) {
		return "'" + val + "'";
	}

	private void generateAlgorithm(MLChoiceAlgorithm algo) {

		if (algo.getFramework().getName().equals("SCIKIT")) {
			pythonAlgorithm(algo);
		} else {
			// TODO java R xgboost
		}
		algorithm.append("\n");
	}

	private void pythonAlgorithm(MLChoiceAlgorithm algo) {
		MLAlgorithm MLalgo = algo.getAlgorithm();
		if (MLalgo instanceof SVR) {
			imports.insert(0, "from sklearn.svm import SVR\n");
			SVR svr = (SVR) algo.getAlgorithm();
			String valC = (svr.getC() != null) ? "C=" + svr.getC() : "";
			String kernel = (svr.getKernel() != null) ? "kernel=" + simpleQuote(svr.getKernel().getName()) : "";

			if (valC.isEmpty()) {
				if (!kernel.isEmpty()) {
					algorithm.append("clf = SVR(" + kernel + ",epsilon=0.2)\n");
				} else {
					algorithm.append("clf = SVR(epsilon=0.2)\n");
				}
			} else {
				if (!kernel.isEmpty()) {
					algorithm.append("clf = SVR(" + valC + "," + kernel + ",epsilon=0.2)\n");

				} else {
					algorithm.append("clf = SVR(" + valC + ",epsilon=0.2)\n");
				}
			}
			// algorithm.append("clf.fit(X, y)\n");
		} else if (MLalgo instanceof RandomForest) {
			imports.insert(0, "from sklearn.ensemble import RandomForestRegressor\n");
			algorithm.append("clf = RandomForestRegressor()\n");
		} else if (MLalgo instanceof DT) {
			DT dt = (DT) algo.getAlgorithm();
			imports.insert(0, "from sklearn.tree import DecisionTreeRegressor\n");
			String max_depth = (dt.getMax_depth()==0) ? "" : "max_depth=" + dt.getMax_depth();

			algorithm.append("clf = DecisionTreeRegressor(" + max_depth + ")\n");
		} else if (MLalgo instanceof SGD) {
			imports.insert(0, "from sklearn.linear_model import SGDRegressor\n");
			algorithm.append("clf = SGDRegressor()\n");
		} else if (MLalgo instanceof GTB) {
			imports.insert(0, "from sklearn.ensemble import GradientBoostingRegressor\n");
			algorithm.append("clf = GradientBoostingRegressor()\n");
		}
	}

	private void generatePredictive(MLChoiceAlgorithm algo) {
		if (algo.getFramework().getName().equals("SCIKIT")) {
			pythonPredictive();
		} else {
			// TODO java R xgboost
		}
		predictive.append("\n");
	}

	private void pythonPredictive() {
		FormulaItem myItem = result.getFormula().getPredictive();

		if (myItem == null) {
			predictive.insert(0, "col_index = df.shape[1] - 1\n");
		}

		if (result.getFormula().getPredictors() instanceof PredictorVariables) {
			EList<FormulaItem> predictorVars = ((PredictorVariables) result.getFormula().getPredictors()).getVars();

			predictive.append("coltokeep = [");
			FormulaItem item;
			for (int i = 0; i < predictorVars.size(); i++) {
				item = predictorVars.get(i);
				if (item.getColName() != null) {
					predictive.append(simpleQuote(item.getColName()));
				} else {
					predictive.append("df.columns[" + item.getColumn() + "]");
				}

				if (i != predictorVars.size() - 1) {
					predictive.append(", ");
				}
			}
			predictive.append("]\n");
			predictive.append("X = df[coltokeep]\n");
		} else {
			if (myItem == null) {
				predictive.append("X = df.drop([col_index], axis=1)\n");
			} else if (myItem.getColName() != null) {
				predictive.append("X = df.drop([" + simpleQuote(myItem.getColName()) + "], axis=1)\n");
			} else {
				predictive.append("X = df.drop(df.columns[" + myItem.getColumn() + "], axis=1)\n");
			}
		}

		if (myItem == null) {
			predictive.append("y = df[col_index]\n");
		} else if (myItem.getColName() != null) {
			predictive.append("y = df[" + simpleQuote(myItem.getColName()) + "]\n");
		} else {
			predictive.append("y = df[df.columns[" + myItem.getColumn() + "]]\n");
		}
	}

	private void generateValidation(MLChoiceAlgorithm algo) {
		if (algo.getFramework().getName().equals("SCIKIT")) {
			pythonValidation();
		} else {
			// TODO java R xgboost
		}
		validation.append("\n");
	}

	private void pythonValidation() {
		StratificationMethod stratification = result.getValidation().getStratification();

		if (stratification instanceof CrossValidation) {
			CrossValidation crossVal = (CrossValidation) stratification;
			imports.insert(0, "from sklearn.model_selection import cross_validate\n");
			imports.insert(0, "from sklearn.model_selection import cross_val_predict\n");
			validation.append("accuracy = cross_validate(clf, X, y, cv=" + crossVal.getNumber() + ")\n");
			validation.append("y_pred = cross_val_predict(clf, X, y, cv=" + crossVal.getNumber() + ")\n");
			validation.append("y_test = y\n");
			validation.append("print(accuracy)\n");
		} else {
			TrainingTest training = (TrainingTest) stratification;
			imports.insert(0, "from sklearn.model_selection import train_test_split\n");
			validation.append("X_train, X_test, y_train, y_test = train_test_split(X, y, test_size="
					+ ((float) training.getNumber() / 100.0) + ")\n");
			validation.append("clf.fit(X_train,y_train)\n");
			validation.append("y_pred = clf.predict(X_test)\n");
		}
		for (ValidationMetric metric : result.getValidation().getMetric()) {
			imports.insert(0, "from sklearn.metrics import " + metric.getLiteral() + "\n");
			validation.append("print(" + metric.getLiteral() + "(y_test, y_pred))\n");
		}
	}
	
	public String getName(MLAlgorithm MLalgo) {
		String name = "";
		if (MLalgo instanceof SVR) {
			name="SVR";
		} else if (MLalgo instanceof RandomForest) {
			name="RandomForest";
		} else if (MLalgo instanceof DT) {
			name="DT";
		} else if (MLalgo instanceof SGD) {
			name="SGD";
		} else if (MLalgo instanceof GTB) {
			name="GTB";
		}
		return name;
	}
}