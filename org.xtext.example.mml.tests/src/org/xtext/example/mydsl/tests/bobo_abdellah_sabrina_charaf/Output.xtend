package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import org.xtext.example.mydsl.mml.FrameworkLang
import org.xtext.example.mydsl.mml.MLAlgorithm
import org.xtext.example.mydsl.mml.ValidationMetric
import java.util.Map

class Output {
	
	MLAlgorithm mlAlgorithm;
	FrameworkLang frameworkLang;
	String fileLocation;
	double timestamp;
	Map<ValidationMetric, Double> validationMetric_result;
	

	
}
