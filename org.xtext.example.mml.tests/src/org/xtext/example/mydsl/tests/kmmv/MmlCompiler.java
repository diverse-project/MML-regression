package org.xtext.example.mydsl.tests.kmmv;

import java.io.File;
import java.io.IOException;

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
	public static void compile(MMLModel model) throws Exception {
		Compilateur scikit = new SklearnCompilateur();
		Compilateur r = new RCompilateur();
		Compilateur weka = new WekaCompilateur();
		Compilateur xgboost = new XGBoostCompilateur();
		int compteur = 0;
		
		DataInput input = model.getInput();
		RFormula formula = model.getFormula();
		Validation validation = model.getValidation();
		
		for(MLChoiceAlgorithm algorithm : model.getAlgorithms()) {
			switch(algorithm.getFramework()) {
				case SCIKIT:
					Files.write(
							scikit.compile(input, algorithm, formula, validation).getBytes(),
							new File(scikit.fileName(input, algorithm, compteur++))
					);
					break;
				case R:
					// TODO
					break;
				case JAVA_WEKA:
					// TODO
					break;
				case XG_BOOST:
					// TODO
					break;
				default:
					break;
			}
		}
	}
}
