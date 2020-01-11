package org.xtext.example.mydsl.tests.classes;

import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;


/**
 * Factory which produces Model (e.g. Strategy) depending on the given algorithm
 * @author hugues
 *
 */
public class ModelFactory {

	/**
	 * Return the strategy depending on the algorithm 
	 * @param algo the algorithm used to know the type of the Strategy
	 * @return the Strategy
	 */
	public static TemplateModel getModel(MLChoiceAlgorithm algo) {
		if(algo.getFramework().getLiteral().equalsIgnoreCase(FrameworkLang.SCIKIT.getLiteral())) 
		{
			return new ModelScikit(algo.getAlgorithm());
		}
		else if(algo.getFramework().getLiteral().equalsIgnoreCase(FrameworkLang.XG_BOOST.getLiteral()))
		{
			return new ModelXgboost(algo.getAlgorithm());
		}
		else if(algo.getFramework().getLiteral().equalsIgnoreCase(FrameworkLang.R.getLiteral()))
		{
			return new ModelR(algo.getAlgorithm());
		}
		return null;
	}
	
}
