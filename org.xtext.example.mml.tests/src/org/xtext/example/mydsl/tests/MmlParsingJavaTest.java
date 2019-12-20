package org.xtext.example.mydsl.tests;

import java.io.File;
import java.util.Scanner;

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
	
	String path = "/home/hugues/Documents/eclipse/runtime-EclipseXtext/testmml/src/myprogram1.mml";
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
		Generator generator = new Generator(pathFile);
		
		for (MLChoiceAlgorithm algo : model.getAlgorithms()) {
			generator.configure(ModelFactory.getModel(algo), model, pathFile, algo.getFramework().getLiteral());
			generator.execute();		
		}
	}

	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}


}