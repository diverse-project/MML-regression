package org.xtext.example.mydsl.tests.classes;

import org.xtext.example.mydsl.mml.MMLModel;

public interface ModelStrategy {
	public void configure(TemplateModel template, MMLModel model, String fileLocation, String type);
	public void execute();
}
