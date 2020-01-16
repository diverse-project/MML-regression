package org.xtext.example.mydsl.tests.kmmv;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingKMMVTest {
	
	@Inject
	ParseHelper<MMLModel> parseHelper;
	
	@Test
	public void loadModel() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "mean_absolute_error\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("foo.csv", result.getInput().getFilelocation());
	}
	
	@Test
	public void loadThreeAlgos() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"boston.csv\"\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm RF\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm GradientBoostingRegressor\n"
				+ "CrossValidation { numRepetitionCross 5 }\n"
				+ "mean_absolute_percentage_error\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("boston.csv", result.getInput().getFilelocation());			
	}		
	
	@Test
	public void loadMultiThreeAlgos() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"boston.csv\"\n"
				+ "mlframework R\n"
				+ "algorithm DT\n"
				+ "mlframework R\n"
				+ "algorithm RF\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm GradientBoostingRegressor\n"
				+ "CrossValidation { numRepetitionCross 5 }\n"
				+ "mean_absolute_percentage_error\n"
				+ "");
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");			
		Assertions.assertEquals("boston.csv", result.getInput().getFilelocation());			
	}
	
	@Test
	public void compileTests() throws Exception {
		File folder = new File("");
		for(int i = 1; i <= 12; ++i) {
			try {
				String data = new String(Files.readAllBytes(Paths.get(folder.getAbsoluteFile()+"/prg"+Integer.toString(i)+".mml")));
				MMLModel model = parseHelper.parse(data);
				
				List<String> commandLines = MmlCompiler.compile(model);
				
				for(String command : commandLines) {
					System.out.println(command);
					String[] exec = {"bash", "-c", command};
					Process p = Runtime.getRuntime().exec(exec);
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = in.readLine()) != null) {
						System.out.println(line);
				    }
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.err.println(e.getCause());
			}
			System.out.println("");
		}
	}
}
