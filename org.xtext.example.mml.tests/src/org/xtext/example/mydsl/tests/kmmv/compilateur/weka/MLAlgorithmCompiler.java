package org.xtext.example.mydsl.tests.kmmv.compilateur.weka;

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
		
		import_.add("import weka.classifiers.trees.REPTree;");
		
		code_.add("REPTree clf = new REPTree();");
		
		if(algorithm.getMax_depth() != 0)
			code_.add(String.format("clf.setMaxDepth(%d);",algorithm.getMax_depth()));
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(GTB algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();

		import_.add("import weka.classifiers.Classifier;");
		import_.add("import weka.classifiers.meta.AdditiveRegression;");
		
		code_.add("Classifier clf = new AdditiveRegression();");
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(RandomForest algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
	
		import_.add("import weka.classifiers.Classifier;");
		import_.add("import weka.classifiers.trees.RandomForest;");
		
		code_.add("Classifier clf = new RandomForest();");

		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(SGD algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();

		import_.add("import weka.classifiers.Classifier;");
		import_.add("import weka.classifiers.functions.SGD;");
		
		code_.add("Classifier clf = new SGD();");
		
		return new Pair<>(import_, code_);
	}
	
	private static Pair<List<String>, List<String>> compile(SVR algorithm) {
		List<String> import_ = new LinkedList<>();
		List<String> code_ = new LinkedList<>();
		
		import_.add("import weka.classifiers.functions.SMOreg;");
		
		code_.add("SMOreg clf = new SMOreg();");
		
		if(algorithm.getC() != null)
			code_.add(String.format("clf.setC(%f);", algorithm.getC()));
		if(algorithm.getKernel() != null) {
			if(algorithm.getKernel().toString().equals("poly")) {
				import_.add("import weka.classifiers.functions.supportVector.PolyKernel;");
				code_.add("clf.setKernel(new PolyKernel());");
			} else if(algorithm.getKernel().toString().equals("linear")) {
				System.err.println("WEKA SVR linear kernel not supported");
			} else if(algorithm.getKernel().toString().equals("rbf")) {
				import_.add("import weka.classifiers.functions.supportVector.RBFKernel;");
				code_.add("clf.setKernel(new RBFKernel());");
			}
		}
		
		return new Pair<>(import_, code_);
	}
}
