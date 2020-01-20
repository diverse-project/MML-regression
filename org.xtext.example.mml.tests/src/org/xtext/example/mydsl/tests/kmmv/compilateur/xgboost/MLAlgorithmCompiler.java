package org.xtext.example.mydsl.tests.kmmv.compilateur.xgboost;

import java.util.LinkedList;
import java.util.List;

import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Pair;

/**
 * -> clf : algorithm
 */
public class MLAlgorithmCompiler {
	public static Pair<List<String>, List<String>> compile(MLAlgorithm algorithm) {
		if(algorithm instanceof DT)
			return compile((DT)algorithm);
		if(algorithm instanceof GTB)
			return compile((GTB)algorithm);
		if(algorithm instanceof RandomForest)
			return compile((RandomForest)algorithm);
		if(algorithm instanceof SGD)
			return compile((SGD)algorithm);
		if(algorithm instanceof SVR)
			return compile((SVR)algorithm);
		return new Pair<>(List.of(), List.of());			
	}
	
	private static Pair<List<String>, List<String>> compile(DT algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("from xgboost import XGBRegressor");
		
		if(algorithm.getMax_depth() != 0)
			code_.add(String.format("clf = XGBRegressor(max_depth=%d)",algorithm.getMax_depth()));
		else
			code_.add("clf = XGBRegressor()");	
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(GTB algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("from xgboost import XGBRegressor");
		code_.add("clf = XGBRegressor()");
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(RandomForest algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("from xgboost import XGBRFRegressor");
		
		code_.add("clf = XGBRFRegressor()");
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(SGD algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("from xgboost import XGBRegressor");
		code_.add("clf = XGBRegressor()");
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(SVR algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("from xgboost import XGBRegressor");
		code_.add("clf = XGBRegressor()");
		
		
		return new Pair<>(import_, code_);
	}
}
