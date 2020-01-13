package org.xtext.example.mydsl.mml.impl;

import org.xtext.example.mydsl.mml.AlgorithmVisitor;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;

public class AlgorithmVisitorImpl implements AlgorithmVisitor<String> {

	@Override
	public String visit(DT dt) {
		return "decision_tree";
	}

	@Override
	public String visit(SVR svr) {
		return "SVR";
	}

	@Override
	public String visit(RandomForest randomForest) {
		return "Random Forest";
	}

	@Override
	public String visit(SGD sgd) {
		return "SGD";
	}

	@Override
	public String visit(GTB gtb) {
		return "GTB";
	}

}
