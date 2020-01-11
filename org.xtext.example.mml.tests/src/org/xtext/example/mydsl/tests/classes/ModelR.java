package org.xtext.example.mydsl.tests.classes;

import java.util.Iterator;
import java.util.Map;

import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVMKernel;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;

public class ModelR extends TemplateModel{

	private String algoname;
	private String printType;
	
	public ModelR(MLAlgorithm algo) {
		super(algo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeFileLocation(String filelocation) {
		return "data <- read.csv('" + filelocation + "') \n";//+ 
				//"head(data)\n";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeImport() {
		return "library(caret)\n";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeVarCible() {
		String res = "";
		if(model.getValidation().getStratification() instanceof CrossValidation) {
			res = "param_train <- trainControl(method = 'cv', number = "+ model.getValidation().getStratification().getNumber() +")\n" + 
				  "training <- data \n";
		}
		if(model.getValidation().getStratification() instanceof TrainingTest) {
			float num = model.getValidation().getStratification().getNumber();
			String var = "";
			if(model.getFormula() != null) {
				//Gestion predictive
				if( model.getFormula().getPredictive() != null) {
					String name = model.getFormula().getPredictive().getColName();
					if(name == null) {
						var = "[,"+ model.getFormula().getPredictive().getColumn() + "]";
					}
					else {
						var = "$"+name;
					}
				}
			}
			res = "param_train <- trainControl(method='none')\n" + 
					"trainset <- createDataPartition(data"+var+", p="+ num/100 +", list = FALSE) \n" + 
					"training <- data[trainset,]\n" + 
					"testing <- data[-trainset,]\n";
		}
		return res;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeStratificationMethod() {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeAlgorithm() {
		String alg = "";
		String predictive = "";
		String predictors = ".";
		if(algo instanceof SVR) {
			algoname = "SVR";
			SVR svr = (SVR) algo;
			if(svr.getKernel() == null) {
				alg = "svmLinear";
			}
			else {
				if(svr.getKernel().getLiteral().equals(SVMKernel.LINEAR.getLiteral())) {
					alg = "svmLinear";
				}
				if(svr.getKernel().getLiteral().equals(SVMKernel.POLY.getLiteral())) {
					alg = "svmPoly";
				}
				if(svr.getKernel().getLiteral().equals(SVMKernel.RBF.getLiteral())) {
					alg = "svmRadial";
				}
			}
		}
		
		if(algo instanceof DT) {
			algoname = "DT";
			alg = "bstTree";
		}
		
		if(algo instanceof RandomForest) {
			algoname = "RF";
			alg = "rf";
		}
		
		if(algo instanceof SGD) {
			algoname = "SGD";
			alg += "mlpSGD";
		}
		if(algo instanceof GTB) {
			algoname = "GTB";
			alg += "gbm_h2o";
		}
		if(model.getFormula() != null) {
			//Gestion predictive
			if( model.getFormula().getPredictive() != null) {
				String name = model.getFormula().getPredictive().getColName();
				if(name == null) {
					predictive = Integer.toString(model.getFormula().getPredictive().getColumn());
				}
				else {
					predictive = name;
				}
				predictive += " ~ ";
			}
			if(model.getFormula().getPredictors() != null) {
				if(model.getFormula().getPredictors() instanceof PredictorVariables) {
					predictors = "";
					PredictorVariables pred = (PredictorVariables)model.getFormula().getPredictors();
					Iterator<FormulaItem> it = pred.getVars().iterator();
					while(it.hasNext()) {
						FormulaItem i = it.next();
						predictors += (i.getColName() == null? i.getColumn() : i.getColName());
						if(it.hasNext()) predictors += " + ";
					}
				}
			}
		}
		return "fit <- train("+ predictive + " "+ predictors +", data = training, method='"+ alg +"', trControl=param_train) \n";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeModel() {
		return "pred <- predict(fit)\n";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writePrint() {
		String res = "\n";		
		String name = model.getFormula().getPredictive().getColName();
		int num = 1;
		if(name == null) {
			name = "[,"+ model.getFormula().getPredictive().getColumn() + "]";
		}
		else name = "$"+name;
		for(ValidationMetric item : model.getValidation().getMetric()) {
			if(item.getLiteral().equals(ValidationMetric.MAPE.getLiteral())) num = 1;
			if(item.getLiteral().equals(ValidationMetric.MSE.getLiteral())) num = 2;
			if(item.getLiteral().equals(ValidationMetric.MAE.getLiteral())) num = 3;
			res += "postResample(pred = pred, obs = training"+name+")[" + num + "]\n";
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Float> getScore(String line, Map<String, Float> map) {
		if(line.contains("RMSE")) this.printType = ValidationMetric.MAPE.getLiteral();
		else if(line.contains("Rsquared")) this.printType = ValidationMetric.MSE.getLiteral();
		else if(line.contains("MAE")) this.printType = ValidationMetric.MAE.getLiteral();
		else {
			map.put("R_"+this.algoname+"_"+this.printType, Float.valueOf(line));
		}
		return map;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "R";
	}

}
