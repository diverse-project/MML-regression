package org.xtext.example.mydsl.tests.classes;

import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;

public class ModelFactory {

	public static TemplateModel getModel(MLChoiceAlgorithm algo) {
		System.out.println(algo.getFramework().getLiteral());
		if(algo.getFramework().getLiteral().equalsIgnoreCase(FrameworkLang.SCIKIT.getLiteral())) 
		{
			return new ModelScikit(algo.getAlgorithm());
		}
		else if(algo.getFramework().getLiteral().equalsIgnoreCase(FrameworkLang.XG_BOOST.getLiteral()))
		{
			return new ModelXgboost(algo.getAlgorithm());
		}
		return null;
		
	}
	
}
