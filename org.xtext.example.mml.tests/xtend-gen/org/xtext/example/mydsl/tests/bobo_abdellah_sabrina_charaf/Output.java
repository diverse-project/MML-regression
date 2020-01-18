package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import java.util.Map;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.ValidationMetric;

@SuppressWarnings("all")
public class Output {
  private MLAlgorithm mlAlgorithm;
  
  private FrameworkLang frameworkLang;
  
  private String fileLocation;
  
  private double timestamp;
  
  private Map<ValidationMetric, Double> validationMetric_result;
}
