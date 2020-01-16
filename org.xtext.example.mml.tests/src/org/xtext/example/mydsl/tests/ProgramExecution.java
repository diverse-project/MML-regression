package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.mml.ValidationMetric;

public class ProgramExecution implements Callable<Map<String, Map<String, Double>>> {

	private Set<String> fileNames;
	private EList<ValidationMetric> metrics;

	ProgramExecution(Set<String> fileNames, EList<ValidationMetric> metrics) {
		this.fileNames = fileNames;
		this.metrics = metrics;
	}

	@Override
	public Map<String, Map<String, Double>> call() {
		Map<String, Map<String, Double>> scoreResult = new HashMap<>();
		for (String fileName : fileNames) {
			Map<String, Double> metricsScore = new HashMap<>();

			/*
			 * Calling generated Python script (basic solution through systems call) we
			 * assume that "python" is in the path
			 */
			long start = System.currentTimeMillis();

			Process p;
			try {
				p = Runtime.getRuntime().exec("python " + fileName);

				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				int i = 0;
				while ((line = in.readLine()) != null) {
					metricsScore.put(metrics.get(i).getName(), Double.valueOf(line));
					i++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			metricsScore.put("Execution_Time", (double) timeElapsed);
			scoreResult.put(fileName, metricsScore);
		}
		return scoreResult;
	}
}
