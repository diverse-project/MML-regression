package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.tests.algoList.PythonCode;
import org.xtext.example.mydsl.tests.algoList.RCode;
import org.xtext.example.mydsl.tests.algoList.XgboostCode;
import org.xtext.example.mydsl.tests.templateMethod.CodeGenerator;

import com.google.common.io.Files;
import com.google.inject.Inject;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
public class MmlParsingJavaTest {

	@Inject
	ParseHelper<MMLModel> parseHelper;
	MMLModel result;

	private Map<String, StringBuilder> files_code = new HashMap<String, StringBuilder>();

	@BeforeEach
	public void init() throws Exception {
		File initialFile = new File(
				"/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/boston.mml");
		InputStream inputStream = new FileInputStream(initialFile);

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();
		byte[] byteArray = buffer.toByteArray();

		String text = new String(byteArray, StandardCharsets.UTF_8);
		System.out.println(text);
		result = parseHelper.parse(text);
		inputStream.close();
	}

	@Test
	public void loadModel() throws Exception {
		Assertions.assertNotNull(result);
		EList<Resource.Diagnostic> errors = result.eResource().getErrors();
		Assertions.assertTrue(errors.isEmpty(), "Unexpected errors");
		Assertions.assertEquals(
				"/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/BostonHousing.csv",
				result.getInput().getFilelocation());

	}

	@Test
	public void compileDataInput() throws Exception {
		DataInput dataInput = result.getInput();
		String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		String csv_separator = DEFAULT_COLUMN_SEPARATOR;

		CSVParsingConfiguration parsingInstruction = dataInput.getParsingInstruction();
		if (parsingInstruction != null) {
			System.err.println("parsing instruction : " + parsingInstruction.getSep());
			csv_separator = parsingInstruction.getSep().toString();
		}

		String filename;

		CodeGenerator gen = null;

		String stratificationName = result.getValidation().getStratification().eClass().getName();
		for (MLChoiceAlgorithm algo : result.getAlgorithms()) {
			long start = System.nanoTime();

			filename = algo.getFramework().getName();

			switch (filename) {
			case "SCIKIT":
				gen = new PythonCode();
				filename += String.format("_%s_%s.py", stratificationName, getName(algo.getAlgorithm()));
				break;
			case "XGBoost":
				gen = new XgboostCode();
				filename += String.format("_%s_%s.py", stratificationName, getName(algo.getAlgorithm()));
				break;
			case "R":
				gen = new RCode();
				filename += String.format("_%s_%s.R", stratificationName, getName(algo.getAlgorithm()));
				break;
			default:
				System.err.println(String.format("\"%s\" is not implemented yet.", filename));
				filename += String.format("_%s_%s.unimplementedformat", stratificationName, getName(algo.getAlgorithm()));;
				break;
			}
			
			StringBuilder program = gen.generate(csv_separator, algo, result);

			files_code.putIfAbsent(filename, program);
			long finish = System.nanoTime();
			long timeElapsed = finish - start;

			System.out.println(String.format("Time elapsed for %s : %s ns", filename, timeElapsed));
		}

		for (Map.Entry<String, StringBuilder> entry : files_code.entrySet()) {
			File file = new File(entry.getKey());
			Files.write(entry.getValue().toString().getBytes(), file);
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);
		}
		// end of Python generation

		/*
		 * Calling generated Python script (basic solution through systems call) we
		 * assume that "python" is in the path
		 */
		Process p = Runtime.getRuntime().exec("python mml.py");
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}
	}

	public String getName(MLAlgorithm MLalgo) {
		String name = "";
		if (MLalgo instanceof SVR) {
			name = "SVR";
		} else if (MLalgo instanceof RandomForest) {
			name = "RandomForest";
		} else if (MLalgo instanceof DT) {
			name = "DT";
		} else if (MLalgo instanceof SGD) {
			name = "SGD";
		} else if (MLalgo instanceof GTB) {
			name = "GTB";
		}
		return name;
	}
}