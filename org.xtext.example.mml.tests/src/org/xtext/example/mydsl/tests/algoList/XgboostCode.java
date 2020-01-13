package org.xtext.example.mydsl.tests.algoList;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.tests.templateMethod.CodeGenerator;

public class XgboostCode extends CodeGenerator {

	@Override
	protected void generateImports(String csv_separator, MLChoiceAlgorithm algo) {
		imports.append("import pandas as pd\n");
		imports.append("import xgboost as xgb\n");
		imports.append("import warnings\n");
		imports.append("df = pd.read_csv(" + simpleQuote(result.getInput().getFilelocation()) + ", sep="
				+ simpleQuote(csv_separator) + ")\n");
		imports.append("warnings.filterwarnings(\"ignore\")\n");
		imports.append("\n");
	}

	@Override
	protected void generateAlgorithm(MLChoiceAlgorithm algo) {
		MLAlgorithm MLalgo = algo.getAlgorithm();
		if (MLalgo instanceof SVR) {
			imports.insert(0, "from sklearn.svm import SVR\n");
			SVR svr = (SVR) algo.getAlgorithm();
			String valC = (svr.getC() != null) ? "C=" + svr.getC() : "";
			String kernel = (svr.getKernel() != null) ? "kernel=" + simpleQuote(svr.getKernel().getName()) : "";

			if (valC.isEmpty()) {
				if (!kernel.isEmpty()) {
					algorithm.append("xgb_model = SVR(" + kernel + ",epsilon=0.2)\n");
				} else {
					algorithm.append("xgb_model = SVR(epsilon=0.2)\n");
				}
			} else {
				if (!kernel.isEmpty()) {
					algorithm.append("xgb_model = SVR(" + valC + "," + kernel + ",epsilon=0.2)\n");

				} else {
					algorithm.append("xgb_model = SVR(" + valC + ",epsilon=0.2)\n");
				}
			}
			// algorithm.append("xgb_model.fit(X, y)\n");
		} else if (MLalgo instanceof RandomForest) {
			algorithm.append("xgb_model = xgb.XGBRegressor()\n");
		} else if (MLalgo instanceof DT) {
			DT dt = (DT) algo.getAlgorithm();
			String max_depth = (dt.getMax_depth() == 0) ? "" : "max_depth=" + dt.getMax_depth();

			algorithm.append("xgb_model = xgb.XGBRegressor(" + max_depth + ")\n");
		} else if (MLalgo instanceof SGD) {
			algorithm.append("xgb_model = xgb.XGBRegressor()\n");
		} else if (MLalgo instanceof GTB) {
			algorithm.append("xgb_model = xgb.XGBRegressor()\n");
		}
		algorithm.append("\n");
	}

	@Override
	protected void generatePredictive(MLChoiceAlgorithm algo) {
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
		predictive.append("\n");
	}

	@Override
	protected void generateValidation(MLChoiceAlgorithm algo) {
		StratificationMethod stratification = result.getValidation().getStratification();

		if (stratification instanceof CrossValidation) {
			CrossValidation crossVal = (CrossValidation) stratification;
			imports.insert(0, "from sklearn.model_selection import cross_validate\n");
			imports.insert(0, "from sklearn.model_selection import cross_val_predict\n");
			validation.append("accuracy = cross_validate(xgb_model, X, y, cv=" + crossVal.getNumber() + ")\n");
			validation.append("y_pred = cross_val_predict(xgb_model, X, y, cv=" + crossVal.getNumber() + ")\n");
			validation.append("y_test = y\n");
			validation.append("print(\"accuracy : \" + str(accuracy))\n");
		} else {
			TrainingTest training = (TrainingTest) stratification;
			imports.insert(0, "from sklearn.model_selection import train_test_split\n");
			validation.append("X_train, X_test, y_train, y_test = train_test_split(X, y, test_size="
					+ ((float) training.getNumber() / 100.0) + ")\n");
			validation.append("xgb_model.fit(X_train,y_train)\n");
			validation.append("y_pred = xgb_model.predict(X_test)\n");
		}
		for (ValidationMetric metric : result.getValidation().getMetric()) {
			imports.insert(0, "from sklearn.metrics import " + metric.getLiteral() + "\n");
			validation.append("print(\'" + metric.getLiteral() + " : \' + str(" + metric.getLiteral() + "(y_test, y_pred)))\n");
		}
		validation.append("\n");
	}

	@Override
	protected void addLastOperations() {
		imports.insert(0, "#!/usr/bin/env python2\n");
	}

	private String simpleQuote(String val) {
		return "'" + val + "'";
	}
}
