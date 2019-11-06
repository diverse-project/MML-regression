package org.xtext.example.mydsl.tests.kmmv.compilateur;

import org.xtext.example.mydsl.mml.*;

public interface Compilateur {
	/**
	 * Compile the given algorithm.
	 * @param input 
	 * @param algorithm 
	 * @param formula
	 * @param validation
	 * @return
	 */
	String compile(DataInput input, MLChoiceAlgorithm algorithm, RFormula formula, Validation validation);
	
	/**
	 * Generate an unique filename.
	 * @param input
	 * @param algorithm
	 * @param uniqueId
	 * @return
	 */
	String fileName(DataInput input, MLChoiceAlgorithm algorithm, int uniqueId);
}
