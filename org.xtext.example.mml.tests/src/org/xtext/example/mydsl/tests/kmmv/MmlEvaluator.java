package org.xtext.example.mydsl.tests.kmmv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.xtext.testing.util.ParseHelper;
import org.xtext.example.mydsl.MmlStandaloneSetup;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Utils;

import com.google.inject.Injector;

public class MmlEvaluator {
	
	ParseHelper<MMLModel> parseHelper;
	
	public MmlEvaluator() {
		Injector injector = new MmlStandaloneSetup().createInjectorAndDoEMFRegistration();
		this.parseHelper = injector.getInstance(ParseHelper.class);
	}

	public MmlResult compile_and_run(String file) throws Exception {
		MmlResult result = new MmlResult();
		String data = new String(Files.readAllBytes(Paths.get(file)));
		MMLModel model = parseHelper.parse(data);
		
		String fileLocation = model.getInput().getFilelocation();
		
		List<String> commandLines = MmlCompiler.compile(model);
		for(int i=0; i < commandLines.size(); ++i) {
			String[] exec = {"bash", "-c", commandLines.get(i)};
			String line, lastline = "";
			
			Long startTime = System.nanoTime();
			Process p = Runtime.getRuntime().exec(exec);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = in.readLine()) != null) {
				lastline = line;
		    }
			Long elapsedTime = System.nanoTime() - startTime;
			
			try {
				if(lastline.isBlank())
					throw new Exception();
				Double d = Double.parseDouble(lastline);
				result.add(
						fileLocation,
						model.getAlgorithms().get(i).getFramework(),
						model.getAlgorithms().get(i).getAlgorithm(),
						d,
						elapsedTime/1000000000.0);
			} catch (Exception e) {
				System.err.println(String.format(
						"Error while running or compiling %s %s with %s .",
						model.getAlgorithms().get(i).getFramework().toString(),
						Utils.algorithmName(model.getAlgorithms().get(i).getAlgorithm()),
						fileLocation));
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		MmlEvaluator evaluator = new MmlEvaluator();
		MmlResult result = new MmlResult();
		
		for(String file : args) {
			System.out.println("Compiling and running " + file);
			try {
				result.addAll(evaluator.compile_and_run(file));
				System.out.println("Done");
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
				System.err.println(ex.getCause());
				System.err.println("\n");
			}
		}
		
		result.sort();
		System.out.println("Result : ");
		System.out.println(result.toMarkdown());
		System.out.println("");
		result.sortByResult();
		System.out.println("Top 3 by result : ");
		List<MmlResult.Element> resultTop = result.getResults();
		for(int i = 0; i < 3 && i < resultTop.size(); ++i)
			System.out.println(Utils.tab() + resultTop.get(i));
		System.out.println("");
		System.out.println("Result order by efficiency : ");
		System.out.println(result.toMarkdown());
		System.out.println("");
		result.sortByTime();
		System.out.println("Top 3 by time : ");
		List<MmlResult.Element> resultTime = result.getResults();
		for(int i = 0; i < 3 && i < resultTime.size(); ++i)
			System.out.println(Utils.tab() + resultTime.get(i));
		System.out.println("");
		System.out.println("Result order by time : ");
		System.out.println(result.toMarkdown());
	}

}
