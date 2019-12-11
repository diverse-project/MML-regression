package org.xtext.example.mydsl.tests.kmmv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
	public void compileDataInput() throws Exception {
		MMLModel result = parseHelper.parse("datainput \"foo2.csv\" separator ;\n"
				+ "mlframework scikit-learn\n"
				+ "algorithm DT\n"
				+ "TrainingTest { percentageTraining 70 }\n"
				+ "mean_absolute_error\n"
				+ "");
		List<String> commandLines = MmlCompiler.compile(result);
		
		for(String command : commandLines) {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line; 
			while ((line = in.readLine()) != null) {
				System.out.println(line);
		    }
		}
		
	}
}
