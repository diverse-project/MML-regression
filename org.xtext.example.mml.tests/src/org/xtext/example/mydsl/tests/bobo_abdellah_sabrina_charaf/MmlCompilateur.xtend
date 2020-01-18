package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import java.util.ArrayList
import java.util.List
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.UniqueEList
import org.xtext.example.mydsl.mml.MLAlgorithm
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm
import org.xtext.example.mydsl.mml.MMLModel
import org.xtext.example.mydsl.mml.FrameworkLang

class MmlCompilateur {
	var MMLModel mmlModel;
	val String name = "MmlCompilateur";
	
	private new(){
		
	}
	new(MMLModel mmlModel) {
		if (mmlModel === null ){
			throw new IllegalArgumentException("you should initialize "+this.name+"with non null value");
		}
		this.mmlModel = mmlModel;
	}
	
	def EList<MLChoiceAlgorithm> removeDuplicate(EList<MLChoiceAlgorithm> MLCAList){
		
		val EList<MLChoiceAlgorithm> result = new UniqueEList<MLChoiceAlgorithm>();
		val List<String> list = new ArrayList<String>();
		
		for (MLChoiceAlgorithm item: MLCAList){
			val MLAlgorithm MLA = item.algorithm;
			val FrameworkLang framework = item.framework;
			if(!list.contains(MLA.class.simpleName+"_"+framework.class.simpleName)){
				list.add(MLA.class.simpleName+"_"+framework.class.simpleName);
				result.add(item);
			}
		}
		return result;
	}
	
	def String render(){
		var String result = "";
		val EList<MLChoiceAlgorithm> MLCAList = removeDuplicate(mmlModel.algorithms)
		val EList<Output> outputList = new UniqueEList<Output>();
		for(MLChoiceAlgorithm item : MLCAList){
			val FrameworkLang framework = item.framework;
			switch framework {
				case FrameworkLang.SCIKIT: {
					val MmlCompilateurScikitLearn mmCompilateurScikitLearn = new MmlCompilateurScikitLearn(mmlModel,item.algorithm);
					outputList.add(mmCompilateurScikitLearn.compile);
					
				}
				case FrameworkLang.R: {
					val MmlCompilateurR mmlCompilateurR = new MmlCompilateurR(mmlModel,item.algorithm);
					outputList.add(mmlCompilateurR.compile()); 
				}
				default: {
				}
			}
		}
		for(Output output:outputList){
			println(output.toString());
		}
		return result;
	}
}