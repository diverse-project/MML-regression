package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.tests.algoList.PythonCode;
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
			default:
				System.err.println(String.format("\"%s\" is not implemented yet.", filename));
				filename += String.format("_%s_%s.unimplementedformat", stratificationName,
						getName(algo.getAlgorithm()));
				;
				break;
			}

			StringBuilder program = gen.generate(csv_separator, algo, result);

			files_code.putIfAbsent(filename, program);
			long finish = System.nanoTime();
			long timeElapsed = finish - start;

			System.out.println(String.format("Time elapsed for %s : %s ns", filename, timeElapsed));
		}

		EList<ValidationMetric> metrics = result.getValidation().getMetric();
		Map<String, Map<String, Double>> scoreResult = new HashMap<>();

		for (Map.Entry<String, StringBuilder> entry : files_code.entrySet()) {
			Map<String, Double> metricsScore = new HashMap<>();

			File file = new File(entry.getKey());
			Files.write(entry.getValue().toString().getBytes(), file);
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);

			/*
			 * Calling generated Python script (basic solution through systems call) we
			 * assume that "python" is in the path
			 */
			long start = System.currentTimeMillis();

			Process p = Runtime.getRuntime().exec("python " + entry.getKey());
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			System.out.println(entry.getKey() + " : ");
			int i = 0;
			while ((line = in.readLine()) != null) {
				metricsScore.put(metrics.get(i).getName(), Double.valueOf(line));
				System.out.println(metrics.get(i).getName() + " : " + Double.valueOf(line));
				i++;
			}
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			metricsScore.put("Execution_Time", (double) timeElapsed);
			scoreResult.put(entry.getKey(), metricsScore);
			System.out.println(String.format("Time elapsed for %s : %s ms", entry.getKey(), timeElapsed));
			System.out.println("");
		}

		System.out.println("Score comparison...");

		for (ValidationMetric metric : metrics) {
			Map<String, Double> metricComparison = new HashMap<>();

			for (Entry<String, Map<String, Double>> entry : scoreResult.entrySet()) {
				metricComparison.put(entry.getKey(), entry.getValue().get(metric.getName()));
			}

			metricComparison = metricComparison.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(
					Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			printTop3(metric.getName(), metricComparison, "score");
		}

		Map<String, Double> executionTimeComparison = new HashMap<>();

		for (Entry<String, Map<String, Double>> entry : scoreResult.entrySet()) {
			executionTimeComparison.put(entry.getKey(), entry.getValue().get("Execution_Time"));
		}

		executionTimeComparison = executionTimeComparison.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		printTop3("Execution_Time", executionTimeComparison, "ms");

	}

	private void printTop3(String metricName, Map<String, Double> metricComparison, String unit) {
		int i = 1;
		System.out.println(metricName + " rating : ");
		for (Entry<String, Double> entry : metricComparison.entrySet()) {
			System.out.println(String.format("n°%d : %s with %s %s", i, entry.getKey(), entry.getValue(), unit));
			i++;
			if (i == 4)
				break;
		}
		System.out.println("");
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