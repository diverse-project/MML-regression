package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
		// Assertions.assertEquals("/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/*.csv",
		// result.getInput().getFilelocation());

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

		for (Map.Entry<String, StringBuilder> entry : files_code.entrySet()) {
			File file = new File(entry.getKey());
			Files.write(entry.getValue().toString().getBytes(), file);
			file.setExecutable(true, false);
			file.setReadable(true, false);
			file.setWritable(true, false);
		}

		int nbIte = 4;
		int cpu_cores = Runtime.getRuntime().availableProcessors();
		System.out.println("CPU cores available : " + cpu_cores);
		System.out.println("Number of iteration : " + nbIte);

		long start = System.currentTimeMillis();
		System.out.println("Score comparison...");
		EList<ValidationMetric> metrics = result.getValidation().getMetric();

		List<Map<String, Future<Map<String, Double>>>> futureList = new ArrayList<>();
		List<Map<String, Map<String, Double>>> threadResults = new ArrayList<>();
		int totalExecutions = 0;
		int timeout = 30;

		ExecutorService executor = Executors.newFixedThreadPool(cpu_cores);
		for (int i = 0; i < nbIte; i++) {
			Map<String, Future<Map<String, Double>>> futureIteration = new LinkedHashMap<>();
			for (String fileName : files_code.keySet()) {
				totalExecutions++;
				futureIteration.put(fileName, executor.submit(new ProgramExecution(timeout, fileName, metrics)));
			}
			futureList.add(futureIteration);
		}
		int tNb = 0;
		Map<String, Integer> successfulFilename = new HashMap<>();
		for (Map<String, Future<Map<String, Double>>> futureItem : futureList) {
			for (Entry<String, Future<Map<String, Double>>> threadResult : futureItem.entrySet()) {
				printProgress(0, totalExecutions, tNb);
				Map<String, Double> futureResult = threadResult.getValue().get();
				if (futureResult != null) {
					successfulFilename.put(threadResult.getKey(), 0);
					threadResults.add(Collections.singletonMap(threadResult.getKey(), futureResult));
				} else {
					System.err.print("\n"+threadResult.getKey() + " timeout");
				}
				tNb++;
			}
		}
		printProgress(0, totalExecutions, totalExecutions);
		System.out.println();

		Map<String, Map<String, Double>> finalScoreResult = initScoreResultMap(metrics, successfulFilename);

		Set<String> statsValName = finalScoreResult.entrySet().iterator().next().getValue().keySet();
		System.out.println("Gathering results...");

		for (Map<String, Map<String, Double>> scoreResult : threadResults) {

			for (Entry<String, Map<String, Double>> entry : scoreResult.entrySet()) {
				Map<String, Double> metricComparison = new HashMap<>(entry.getValue());
				Map<String, Double> oldMetricValue = new HashMap<>(finalScoreResult.get(entry.getKey()));
				String fileName = entry.getKey();
				for (String metric : statsValName) {
					Double oldValue = oldMetricValue.get(metric);
					Double newValue = metricComparison.get(metric);
					metricComparison.put(metric, (newValue + oldValue));
					finalScoreResult.put(entry.getKey(), metricComparison);
				}
				oldMetricValue = new HashMap<>(finalScoreResult.get(fileName));
			}
		}
		System.out.println("Done !");
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println("Took : " + getDurationBreakdown(timeElapsed) + "\n");
		finalScoreResult = meanMetrics(finalScoreResult, nbIte);

		printTop3(finalScoreResult);
		executor.shutdown();
	}

	private Map<String, Map<String, Double>> meanMetrics(Map<String, Map<String, Double>> finalScoreResult, int nbIte) {
		Map<String, Map<String, Double>> meanMap = new HashMap<>();
		for (Entry<String, Map<String, Double>> entry : finalScoreResult.entrySet()) {
			Map<String, Double> newMetricComparison = entry.getValue();
			newMetricComparison.replaceAll((metricName, metricSum) -> metricSum /= nbIte);
			meanMap.put(entry.getKey(), newMetricComparison);
		}
		return meanMap;
	}

	private Map<String, Map<String, Double>> initScoreResultMap(EList<ValidationMetric> metrics,
			Map<String, Integer> successfulFilename) {
		Map<String, Map<String, Double>> initMap = new HashMap<>();
		Map<String, Double> emptyMap = new HashMap<>();
		for (ValidationMetric metric : metrics)
			emptyMap.put(metric.getName(), 0.0);

		emptyMap.put("Execution_Time", 0.0);

		for (String entry : successfulFilename.keySet())
			initMap.put(entry, emptyMap);

		return initMap;
	}

	private void printTop3(Map<String, Map<String, Double>> finalScoreResult) {
		System.out.print("Results : ");
		Set<String> statsValName = finalScoreResult.entrySet().iterator().next().getValue().keySet();
		for (String statName : statsValName) {
			int i = 1;
			System.out.println("\n" + statName + " rating : ");
			Map<String, Double> fileValueMap = finalScoreResult.entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(statName))).entrySet()
					.stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey,
							Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

			for (Entry<String, Double> entry : fileValueMap.entrySet()) {
				System.out.println(String.format("n°%d : %s with %s %s", i, entry.getKey(), entry.getValue(),
						(statName.equals("Execution_Time")) ? "ms" : "score"));
				i++;

				if (i == 4)
					break;

			}
		}
	}

	public static String getDurationBreakdown(long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		sb.append(days);
		sb.append(" Days ");
		sb.append(hours);
		sb.append(" Hours ");
		sb.append(minutes);
		sb.append(" Minutes ");
		sb.append(seconds);
		sb.append(" Seconds");

		return (sb.toString());
	}

	private static void printProgress(long startTime, long total, long current) {
		StringBuilder string = new StringBuilder(140);
		int percent = (int) (current * 100 / total);
		string.append('\r')
				.append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
				.append(String.format(" %d%% [", percent)).append(String.join("", Collections.nCopies(percent, "=")))
				.append('>').append(String.join("", Collections.nCopies(100 - percent, " "))).append(']')
				.append(String.join("",
						Collections.nCopies(current == 0 ? (int) (Math.log10(total))
								: (int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
				.append(String.format(" %d/%d", current, total));

		System.out.print(string);
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