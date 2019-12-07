package org.xtext.example.mydsl.tests.classes;

import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;

public class ModelFactory {

	public Model getModel(String model_txt, MMLModel model, MLAlgorithm algo, String fileLocation) {
		if(model_txt.equalsIgnoreCase(FrameworkLang.SCIKIT.getLiteral())) 
		{
			return new ModelScikit(model, algo, fileLocation);
		}
		else if(model_txt.equalsIgnoreCase(FrameworkLang.XG_BOOST.getLiteral()))
		{
			return new ModelXgboost(model, algo, fileLocation);
		}
		return null;
		
	}
	
}
