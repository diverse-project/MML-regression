package org.xtext.example.mydsl.mml;

import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;

public interface AlgorithmVisitor<V> {

	V visit(DT dt);

	V visit(SVR svr);

	V visit(RandomForest randomForest);

	V visit(SGD sgd);

	V visit(GTB gtb);

}