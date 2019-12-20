package org.xtext.example.mydsl.tests.classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;

public class Generator implements ModelStrategy{
	private TemplateModel strategy;
	private String content;
	private String pathfile;
	private String fileType;
	
	public Generator(String pathfile) {
		this.pathfile = pathfile;
	}
	
	@Override
	public void configure(TemplateModel strategy, MMLModel model, String fileLocation, String type) {
		this.strategy = strategy;
		this.strategy.setModel(model);
		this.strategy.setFileLocation(fileLocation);
		this.fileType = type;
	}

	@Override
	public void execute() {
		this.content = this.strategy.generate();
		System.out.println(this.content);
		String filename = this.pathfile + generateFilename();
		try {
			File file = new File(filename);
			if(file.createNewFile()) {
				FileOutputStream write = new FileOutputStream(filename);
				write.write(this.content.getBytes());
				write.flush();
				write.close();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	
	}
	
	private String generateFilename() {
		Random rand = new Random();
		return fileType + "_" + getAlgoName(this.strategy.getAlgo()) + "_" + (rand.nextInt(999 - 0 + 1) + 0) + getExtension();
	}
	
	private String getExtension() {
		if(fileType.equalsIgnoreCase(FrameworkLang.SCIKIT.getLiteral())) {
			return ".py";
		}
		else return ".txt";
	}
	
	private String getAlgoName(MLAlgorithm MLalgo) {
		String name = "";
		if (MLalgo instanceof SVR) {
			name="SVR";
		} else if (MLalgo instanceof RandomForest) {
			name="RF";
		} else if (MLalgo instanceof DT) {
			name="DT";
		} else if (MLalgo instanceof SGD) {
			name="SGD";
		} else if (MLalgo instanceof GTB) {
			name="GTB";
		}
		return name;
	}
	
}
	