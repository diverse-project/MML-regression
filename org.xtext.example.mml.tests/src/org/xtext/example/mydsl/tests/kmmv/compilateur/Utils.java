package org.xtext.example.mydsl.tests.kmmv.compilateur;

import java.util.List;
import java.util.stream.Collectors;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;

public class Utils {
	public static String algorithmName(MLAlgorithm algorithm) {
		if(algorithm instanceof DT)
			return "DT";
		if(algorithm instanceof GTB)
			return "GTB";
		if(algorithm instanceof RandomForest)
			return "RF";
		if(algorithm instanceof SVR)
			return "SVR";
		if(algorithm instanceof SGD)
			return "SGD";
		return "";
	}
	
	public static String tab() {
		return "    ";
	}
	
	private static List<String> insertTab(List<String> data) {
		return data.stream().map(el -> tab() + el).collect(Collectors.toList());
	}
	
	public static List<String> insertTab(List<String> data, int iteration) {
		List<String> result = data;
		for(int i=0; i < iteration; ++i) {
			result = insertTab(result);
		}
		return result;
	}
	
	public static String stratificationToString(StratificationMethod stratification) {
		if(stratification instanceof CrossValidation) {
			int fold = 5;
			if(stratification.getNumber() != 0)
				fold = stratification.getNumber();
			return String.format("CrossValidation_%d", fold);
		}
		if(stratification instanceof TrainingTest) {
			double train = 0.7;
			if(stratification.getNumber() > 0 && stratification.getNumber() < 100)
				train = stratification.getNumber()/100.0;
			return String.format("TrainingTest_%s", Double.toString(train)).replace('.', '_');
		}
		return "";
	}
}
