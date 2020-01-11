package org.xtext.example.mydsl.tests.classes;

import org.xtext.example.mydsl.mml.MMLModel;

/**
 * Interface of the Design Pattern Strategy
 * @author hugues
 *
 */
public interface ModelStrategy {
	
	/**
	 * Method to configure the strategy with necessary values
	 * @param model
	 * @param fileLocation
	 */
	public void configure(MMLModel model, String fileLocation);

	/**
	 * Execute the strategy
	 * @return
	 */
	public String execute();
}
