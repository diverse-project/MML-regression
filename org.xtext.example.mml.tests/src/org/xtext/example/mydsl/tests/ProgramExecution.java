package org.xtext.example.mydsl.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.mml.ValidationMetric;

public class ProgramExecution implements Callable<Map<String, Double>> {

	private String fileName;
	private EList<ValidationMetric> metrics;
	private int timeout;

	ProgramExecution(int timeout, String fileName, EList<ValidationMetric> metrics) {
		this.timeout = timeout;
		this.fileName = fileName;
		this.metrics = metrics;
	}

	@Override
	public Map<String, Double> call() {
		Map<String, Double> metricsScore = new HashMap<>();

		/*
		 * Calling generated Python script (basic solution through systems call) we
		 * assume that "python" is in the path
		 */

		try {
			long start = System.currentTimeMillis();

			Process p = Runtime.getRuntime().exec("timeout " + timeout + "  python " + fileName);

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			int i = 0;
			while ((line = in.readLine()) != null) {
				metricsScore.put(metrics.get(i).getName(), Double.valueOf(line));
				i++;
			}
			long finish = System.currentTimeMillis();
			long timeElapsed = finish - start;
			if (metricsScore.size() > 0) {
				metricsScore.put("Execution_Time", (double) timeElapsed);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (metricsScore.size() > 0) ? metricsScore : null;
	}
}
