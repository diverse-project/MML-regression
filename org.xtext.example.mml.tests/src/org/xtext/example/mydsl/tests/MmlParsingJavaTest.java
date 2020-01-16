package org.xtext.example.mydsl.tests;

import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.classes.Generator;
import org.xtext.example.mydsl.tests.classes.ModelFactory;

import com.google.inject.Inject;


@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
@TestInstance(Lifecycle.PER_CLASS)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	//Path to the mml to compile
	String path = "/home/hugues/Documents/eclipse/workspace/MML-regression/org.xtext.example.mml.tests/programmeMML/SVRLinear_CrossValid.mml";
	String csvPath = "";
	File program;	
	String lang;
	String filename = "test1";
	MMLModel model;
	
	@BeforeEach
	public void init() throws Exception {
		program = new File(path);
		Assertions.assertNotNull(program, "mml file not found");
		String contents = new Scanner(program).useDelimiter("\\Z").next();
		model = parseHelper.parse(contents);
		csvPath = model.getInput().getFilelocation();
	}
	
	@Test
	public void loadModel() throws Exception {
		
		String pathFile = "/home/hugues/Documents/cours/DMI/";
		
		//Instantiate a Generator
		Generator generator = new Generator();
		Map<String, Float> scores = new HashMap<>();
		Map<String, Double> execTime = new HashMap<>();
		
		for (MLChoiceAlgorithm algo : model.getAlgorithms()) {
			
			//Set the strategy (Scikit, XGboost, R) with a factory
			generator.setStrategy(ModelFactory.getModel(algo));
			
			//Configure the generator
			generator.configure(model, algo.getFramework().getLiteral());
			
			//Generate and compile program
			generator.generate();
			
			//Get the scores of the program
			scores = Stream.of(scores, generator.getScore())
					.flatMap(map -> map.entrySet().stream())
					.collect(Collectors.toMap(
					    Map.Entry::getKey,
					    Map.Entry::getValue,
					    Float::min));
			
			//Get the execution time of the program
			execTime = Stream.of(execTime, generator.getExecTime())
					.flatMap(map -> map.entrySet().stream())
					.collect(Collectors.toMap(
					    Map.Entry::getKey,
					    Map.Entry::getValue,
					    Double::min));
		}
		
		//Output the scores for each program (sorted)
		System.out.println("\nBest scores : ");
		scores.entrySet().stream()
			.sorted(Map.Entry.comparingByValue())
			.collect(
					Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
							(oldValue, newValue) -> oldValue, LinkedHashMap::new))
			.forEach((k,v) -> System.out.println(k+":"+v));
		
		//Output the execution time of each program (sorted)
		System.out.println("\nExecution Time : ");
		execTime.entrySet().stream()
			.sorted(Map.Entry.comparingByValue())
			.collect(
					Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
							(oldValue, newValue) -> oldValue, LinkedHashMap::new))
			.forEach((k,v) -> System.out.println(k+":"+v+"s"));
	}

}