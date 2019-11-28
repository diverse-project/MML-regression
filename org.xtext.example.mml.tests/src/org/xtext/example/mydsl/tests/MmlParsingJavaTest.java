package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.tests.classes.Model;
import org.xtext.example.mydsl.tests.classes.ModelPython;

import com.google.common.io.Files;
import com.google.inject.Inject;


@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
@TestInstance(Lifecycle.PER_CLASS)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	String path = "/home/hugues/Documents/eclipse/runtime-EclipseXtext/testmml/src/myprogram1.mml";
	String csvPath = "/home/hugues/Documents/eclipse/runtime-EclipseXtext/testmml/src/Boston.csv";
	File program;	
	String lang;
	String filename = "test1";
	MMLModel model;
	
	@BeforeEach
	public void init() throws Exception {
		program = new File(path);
		Assertions.assertNotNull(program, "mml file not found");
		String contents = new Scanner(program).useDelimiter("\\Z").next();
		//System.out.println(contents);
		model = parseHelper.parse(contents);
		//lang = model.getAlgorithms().getFramework().getLiteral();
		
	}
	
	@Test
	public void loadModel() throws Exception {
		
		String result = "";
		String pathFile = "/home/hugues/Documents/cours/DMI/";
		String fileName = "";
		Random rand = new Random();
		//add factory
		
		for (MLChoiceAlgorithm algo : model.getAlgorithms()) {
			if(algo.getFramework().getLiteral().equals("scikit-learn")) {
				Model py = new ModelPython(model, algo.getAlgorithm(), csvPath);
				fileName = algo.getFramework().getLiteral() + "_" +  getAlgoName(algo.getAlgorithm()) + "_" + (rand.nextInt(999 - 0 + 1) + 0) + ".py";
				String res = py.generate();
				System.out.println(res);
				File file = new File(pathFile + fileName);
				if(file.createNewFile()) {
					FileOutputStream write = new FileOutputStream(pathFile + fileName);
					write.write(res.getBytes());
					write.flush();
					write.close();
				}
			}
		
		}
	}		
	
	public String getAlgoName(MLAlgorithm MLalgo) {
		String name = "";
		if (MLalgo instanceof SVR) {
			name="SVR";
		} else if (MLalgo instanceof RandomForest) {
			name="RandomForest";
		} else if (MLalgo instanceof DT) {
			name="DT";
		} else if (MLalgo instanceof SGD) {
			name="SGD";
		} else if (MLalgo instanceof GTB) {
			name="GTB";
		}
		return name;
	}

	private String mkValueInSingleQuote(String val) {
		return "'" + val + "'";
	}


}