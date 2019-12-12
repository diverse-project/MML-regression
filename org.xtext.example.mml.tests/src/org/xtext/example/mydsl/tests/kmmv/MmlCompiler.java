package org.xtext.example.mydsl.tests.kmmv;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Compilateur;
import org.xtext.example.mydsl.tests.kmmv.compilateur.R.RCompilateur;
import org.xtext.example.mydsl.tests.kmmv.compilateur.sklearn.SklearnCompilateur;
import org.xtext.example.mydsl.tests.kmmv.compilateur.weka.WekaCompilateur;
import org.xtext.example.mydsl.tests.kmmv.compilateur.xgboost.XGBoostCompilateur;

import com.google.common.io.Files;

public class MmlCompiler {
	/**
	 * Compile the model and return all the command line to execute
	 * @param model the model
	 * @return the command line
	 * @throws Exception
	 */
	public static List<String> compile(MMLModel model) throws Exception {
		Compilateur scikit = new SklearnCompilateur();
		Compilateur r = new RCompilateur();
		Compilateur weka = new WekaCompilateur();
		Compilateur xgboost = new XGBoostCompilateur();
		int compteur = 0, tmp;
		
		DataInput input = model.getInput();
		RFormula formula = model.getFormula();
		Validation validation = model.getValidation();
		
		List<String> commandLines = new LinkedList<>();
		String filename;
		
		for(MLChoiceAlgorithm algorithm : model.getAlgorithms()) {
			tmp = compteur;
			switch(algorithm.getFramework()) {
				case SCIKIT:
					filename = scikit.fileName(input, algorithm, compteur++);
					Files.write(
							scikit.compile(input, algorithm, formula, validation, tmp).getBytes(),
							new File(filename)
					);
					commandLines.add(scikit.commandLine(filename));
					break;
				case R:
					filename = r.fileName(input, algorithm, compteur++);
//					Files.write(
//							r.compile(input, algorithm, formula, validation, tmp).getBytes(),
//							new File(filename)
//					);
//					commandLines.add(r.commandLine(filename));
					break;
				case JAVA_WEKA:
					filename = weka.fileName(input, algorithm, compteur++);
					Files.write(
							weka.compile(input, algorithm, formula, validation, tmp).getBytes(),
							new File(filename)
					);
					commandLines.add(weka.commandLine(filename));
					break;
				case XG_BOOST:
					filename = xgboost.fileName(input, algorithm, compteur++);
//					Files.write(
//							xgboost.compile(input, algorithm, formula, validation, tmp).getBytes(),
//							new File(filename)
//					);
//					commandLines.add(xgboost.commandLine(filename));
					break;
				default:
					break;
			}
		}
		return commandLines;
	}
}
