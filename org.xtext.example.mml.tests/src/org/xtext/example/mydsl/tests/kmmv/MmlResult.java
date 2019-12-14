package org.xtext.example.mydsl.tests.kmmv;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.tests.kmmv.compilateur.Utils;

public class MmlResult {
	public class Element {
		// We assume that we use MAPE all the time
		String fileLocation;
		FrameworkLang framework;
		MLAlgorithm algorithm;
		Double result;
		Double time;
		
		public Element(String fileLocation, FrameworkLang framework, MLAlgorithm algorithm, Double result, Double time) {
			this.fileLocation = fileLocation;
			this.framework = framework;
			this.algorithm = algorithm;
			this.result = result;
			this.time = time;
		}
		
		public int compare(Element other) {
			int result = this.fileLocation.compareTo(other.fileLocation);
			if(result == 0)
				result = Utils.algorithmName(this.algorithm).compareTo(Utils.algorithmName(other.algorithm));
			if(result == 0)
				result = this.framework.compareTo(other.framework);
			if(result == 0)
				result = compareByResult(other);
			if(result == 0)
				result = compareByTime(other);
			
			return result;
		}
		
		public int compareByResult(Element other) {
			return Double.compare(this.result, other.result);
		}
		
		public int compareByTime(Element other) {
			return Double.compare(this.time, other.time);
		}
		
		public String toMarkdown() {
			List<String> markdown = new LinkedList<>();
			markdown.add("|");
			markdown.add(this.fileLocation);
			markdown.add("|");
			markdown.add(Utils.algorithmName(this.algorithm));
			markdown.add("|");
			markdown.add(this.framework.toString());
			markdown.add("|");
			markdown.add(Double.toString(this.time));
			markdown.add("|");
			markdown.add(Double.toString(this.result));
			markdown.add("|");
			return String.join(" ", markdown);
		}
		
		public String toString() {
			return String.format("%s %s with %s elapsed %s with %s MAPE", this.framework, Utils.algorithmName(this.algorithm), this.fileLocation, Double.toString(this.time), Double.toString(this.result));
		}
	}
	
	private List<Element> results = new LinkedList<>();
	
	public MmlResult() {
		
	}
	
	public void add(Element element) {
		this.results.add(element);
	}
	
	public void add(String fileLocation, FrameworkLang framework, MLAlgorithm algorithm, Double result, Double time) {
		this.results.add(new Element(fileLocation, framework, algorithm, result, time));
	}
	
	public void addAll(MmlResult other) {
		this.results.addAll(other.getResults());
	}
	
	public void sort() {
		Comparator<Element> comparator = (Element o1, Element o2) -> o1.compare(o2);
		Collections.sort(this.results, comparator);
	}
	
	public void sortByResult() {
		Comparator<Element> comparator = (Element o1, Element o2) -> o1.compareByResult(o2);
		Collections.sort(this.results, comparator);
	}
	
	public void sortByTime() {
		Comparator<Element> comparator = (Element o1, Element o2) -> o1.compareByTime(o2);
		Collections.sort(this.results, comparator);
	}
	
	public String toMarkdown() {
		List<String> markdown = new LinkedList<>();
		markdown.add("| Data | Algorithm | Framework | Execution Time (sec) | MAPE (%) |");
		markdown.add("|---|---|---|---|---|");
		for(Element e : this.results) {
			markdown.add(e.toMarkdown());
		}
		return String.join("\n", markdown);
	}
	
	public List<Element> getResults() {
		return Collections.unmodifiableList(this.results);
	}
}
