package org.xtext.example.mydsl.tests.kmmv.compilateur;

import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;

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
}
