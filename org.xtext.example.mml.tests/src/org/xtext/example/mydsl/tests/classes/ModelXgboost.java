package org.xtext.example.mydsl.tests.classes;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;

public class ModelXgboost extends TemplateModel{

	private String algoname;
	
	
	public ModelXgboost(MLAlgorithm algo) {
		super(algo);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeFileLocation(String filelocation) {
		// TODO Auto-generated method stub
		return "df = pd.read_csv('"+filelocation+"')";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeImport() {
		// TODO Auto-generated method stub
		
		StratificationMethod strat = model.getValidation().getStratification();
		//MLAlgorithm algo = model.getAlgorithm().getAlgorithm();
		EList<ValidationMetric> eList = model.getValidation().getMetric();
		
		String ret = "import pandas as pd \n" +
				"from sklearn.model_selection import train_test_split \n"+
				"import numpy as nps\n" + 
				"import xgboost as xgb\n";
		
		if (strat instanceof CrossValidation) {
			ret += "from sklearn.model_selection import cross_val_predict \n";
		}
		//if ret
		if(algo instanceof DT) {
			this.algoname = "DT";
			ret += "from sklearn import tree \n";
		}
		if(algo instanceof SVR) {
			this.algoname = "SVR";
			ret += "from sklearn import svm \n";
		}
		if(algo instanceof RandomForest) {
			this.algoname = "RF";
			ret += "from sklearn.ensemble import RandomForestRegressor \n";
		}
		if(algo instanceof SGD) {
			this.algoname = "SGD";
			ret += "from sklearn import linear_model \n";
		}
		if(algo instanceof GTB) {
			this.algoname = "GTB";
			ret += "from sklearn.ensemble import GradientBoostingRegressor \n";
		}
		
		for(ValidationMetric metric : eList) {
			if(metric.getLiteral().equals("mean_squared_error")) {
				ret += "from sklearn.metrics import mean_squared_error \n";
			}

			if(metric.getLiteral().contains("mean_absolute_error")) {
				ret += "from sklearn.metrics import mean_absolute_error \n";
			}
			
			if(metric.getLiteral().contains("mean_absolute_percentage_error")) {
				ret += "from sklearn.metrics import median_absolute_error";
			}
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeVarCible() {
		
		String varCibleStr = "len(df)";
		String declY = "y = df[df.columns[len(df.columns)-1]]";
		String declX = "X = df.drop(columns=[len(df.columns)-1])";
		int varCibleInt = -1;
		
		// Gestion formula
		if(model.getFormula() != null) {
			//Gestion predictive
			if( model.getFormula().getPredictive() != null) {
				varCibleStr = model.getFormula().getPredictive().getColName();
				varCibleInt = model.getFormula().getPredictive().getColumn();
				
				declY = "y = df["+(varCibleStr != null? "'" + varCibleStr + "'" : "df.columns["+varCibleInt+"]")+"]";
				declX = "X = df.drop(columns=["+(varCibleStr != null? "'"+varCibleStr+"'" : varCibleInt)+"])";
			}
			
			//Gestion Predictors
			if(model.getFormula().getPredictors() != null) {
				if(model.getFormula().getPredictors() instanceof PredictorVariables) {
					PredictorVariables pred = (PredictorVariables)model.getFormula().getPredictors();
					declX = "X = df[[";
					Iterator<FormulaItem> it = pred.getVars().iterator();
					while(it.hasNext()) {
						FormulaItem i = it.next();
						declX += (i.getColName() == null? "df.columns["+i.getColumn()+"]" : "'"+i.getColName()+"'");
						if(it.hasNext()) declX += ",";
					}
					declX += "]]";
				}
			}
		}
		
		String ret = declX + "\n" + declY + "\n";
		
		return ret;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeStratificationMethod() {
		String res = "";
		if(model.getValidation().getStratification() instanceof CrossValidation) {
			res = "y_test = cross_val_predict(clf, X, y, cv="+model.getValidation().getStratification().getNumber()+")";
		}
		if(model.getValidation().getStratification() instanceof TrainingTest) {
			float num = model.getValidation().getStratification().getNumber();
			res = "test_size = " + num/100 + "\n";
			res += "X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)";
		}
	return res;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeAlgorithm() {
		String res = "";
		//LAlgorithm algo = model.getAlgorithm().getAlgorithm();
		
		if(algo instanceof SVR) {
			SVR svr = (SVR) algo; //(SVR) model.getAlgorithm().getAlgorithm();
			String kernel = "";
			String c = "";
			res = "clf = svm.SVR(";
			if(svr.getKernel() != null) {
				kernel = "kernel='"+ svr.getKernel() + "'";
			}
			if(svr.getC() != null) {
				c = "C="+ svr.getC();
				if (!kernel.equals("") ) kernel += ", ";
			}
			res += kernel + c;
			res += ")";
		}
		
		if(algo instanceof DT) {
			DT dt = (DT) algo; //model.getAlgorithm().getAlgorithm();
			String md = "";
			if(dt.getMax_depth() > 0) {
				md = "max_depth=" + dt.getMax_depth();
			}
			res += "clf = tree.DecisionTreeRegressor("+ md +")";
		}
		
		if(algo instanceof RandomForest) {
			res += "clf = RandomForestRegressor()";
		}
		
		if(algo instanceof SGD) {
			res += "clf = linear_model.SGDRegressor()";
		}
		if(algo instanceof GTB) {
			res += "clf = GradientBoostingRegressor()";
		}
		
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writeModel() {
		// TODO Auto-generated method stub
		if(model.getValidation().getStratification() instanceof TrainingTest) {
			return "dtrain = xgb.DMatrix(X_train, label= y_train)\n" + 
					"dtest = xgb.DMatrix(X_test, label = y_test)\n" + 
					"\n" + 
					"param = {\n" + 
					"	'max_depth' : 3, \n" + 
					"	'eta' : 0.3}\n" + 
					"num_round = 20\n" + 
					"bst = xgb.train(param, dtrain, num_round)\n" + 
					"preds = bst.predict(dtest)";
		}
		else if(model.getValidation().getStratification() instanceof CrossValidation) {
			//return	"y_pred = cross_val_predict(clf, X, y, cv=" + model.getValidation().getStratification().getNumber() + ")\n" + 
			return "preds = y\n"; //"X_test = y";
		}
		else return "[MODEL] not implemented yet";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String writePrint() {
		String res = "\n";		
		for(ValidationMetric item : model.getValidation().getMetric()) {
			if(item.getLiteral().equals("mean_absolute_percentage_error")) {
				res += "print '"+item.getLiteral()+": {}'.format(median_absolute_error(preds, y_test))" + "\n";
			}
			else {
				res += "print '"+item.getLiteral()+": {}'.format("+item.getLiteral()+"(preds, y_test))" + "\n";
			}
		}
		return res;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Float> getScore(String line, Map<String, Float> map) {
		String[] values = line.split(":");
		map.put("XgBoost_"+this.algoname+"_"+values[0], Float.valueOf(values[1]));
		return map;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "XGBoost";
	}
}
