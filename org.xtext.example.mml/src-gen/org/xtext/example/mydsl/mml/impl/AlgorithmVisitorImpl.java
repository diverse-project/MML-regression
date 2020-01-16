package org.xtext.example.mydsl.mml.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xtext.example.mydsl.mml.AlgorithmVisitor;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;

public class AlgorithmVisitorImpl implements AlgorithmVisitor<Map<String,List<String>>> {

	private String frameworkLang;
	
	public AlgorithmVisitorImpl(String frameworkLang) {
		this.frameworkLang = frameworkLang;
	}
	@Override
	public Map<String,List<String>> visit(DT dt) {
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		switch(this.frameworkLang) {
			case "R":
				break;
			case "scikit-learn":
				break;
			case "Weka":
				break;
			case "xgboost":
				break;
			default:
				break;
		}
		result.put("inputs", Arrays.asList("from sklearn import tree"));
		List<String> containsBody = new ArrayList<String>();
		containsBody.add("clf = tree.DecisionTreeRegressor()");
		containsBody.add("clf.fit(X_train, y_train)");
		containsBody.add("y_pred =  clf.predict(X_test)");
		result.put("body", containsBody);
		return result;
	}

	@Override
	public Map<String,List<String>> visit(SVR svr) {
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		return result;
	}

	@Override
	public Map<String,List<String>> visit(RandomForest randomForest) {
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		List<String> containsInputs = new ArrayList<String>();
		containsInputs.add("import numpy as np");
		containsInputs.add("from sklearn.ensemble import RandomForestRegressor");
		result.put("inputs", containsInputs);
		List<String> containsBody = new ArrayList<String>();
		containsBody.add("regressor = RandomForestRegressor()");
		containsBody.add("regressor.fit(X_train, y_train)");
		containsBody.add("y_pred = regressor.predict(X_test)");
		result.put("body", containsBody);
		return result;
	}

	@Override
	public Map<String,List<String>> visit(SGD sgd) {
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		return result;
	}

	@Override
	public Map<String,List<String>> visit(GTB gtb) {
		Map<String,List<String>> result = new HashMap<String, List<String>>();
		return result;
	}

}
