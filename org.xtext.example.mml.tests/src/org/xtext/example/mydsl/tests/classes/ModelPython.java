package org.xtext.example.mydsl.tests.classes;

	import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.junit.platform.commons.util.StringUtils;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.ValidationMetric;

	public class ModelPython implements Model{

		MMLModel model;
		
		@Override
		public String writeFileLocation(String filelocation) {
			// TODO Auto-generated method stub
			return "df = pd.read_csv('"+filelocation+"')";
		}

		@Override
		public String writeImport() {
			// TODO Auto-generated method stub
			
			StratificationMethod strat = model.getValidation().getStratification();
			MLAlgorithm algo = model.getAlgorithm().getAlgorithm();
			EList<ValidationMetric> eList = model.getValidation().getMetric();
			
			String ret = "import pandas as pd \n" +
					"from sklearn.model_selection import train_test_split \n";
			
			if (strat instanceof CrossValidation) {
				ret += "from sklearn.model_selection import cross_val_score \n";
			}
			//if ret
			if(algo instanceof DT) {
				ret += "from sklearn import tree \n";
			}
			if(algo instanceof SVR) {
				ret += "from sklearn import svm \n";
			}
			
			for(ValidationMetric metric : eList) {
				if(metric.getLiteral().equals("mean_squared_error")) {
					ret += "from sklearn.metrics import mean_squared_error \n";
				}

				if(metric.getLiteral().contains("mean_absolute_error")) {
					ret += "from sklearn.metrics import mean_absolute_error \n";
				}
				/*
				if(metric.equals("mean_absolute_percentage_error")) {
					ret.concat("");
				}*/
			}
			return ret;
		}

		@Override
		public String writeReadCSV(String filelocation) {
			// TODO Auto-generated method stub
			return "[ReadCSV] not implemented yet";
		}

		@Override
		public String writeVarCible() {
			
			String varCibleStr = "len(df)";
			String declY = "y = df[df.columns[len(df.columns)-1]]";
			String declX = "X = df.drop(columns=[len(df.columns)-1])";
			int varCibleInt = -1;
			
			//Gestion predictive
			if(model.getFormula() != null) {
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
						declX = "X = X[[";
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

		@Override
		public String writeStratificationMethod() {
			String res = "";
			if(model.getValidation().getStratification() instanceof CrossValidation) {
				res = "scores = cross_val_score(clf, X, y cv="+model.getValidation().getStratification().getNumber()+")";
			}
			if(model.getValidation().getStratification() instanceof TrainingTest) {
				float num = model.getValidation().getStratification().getNumber();
				res = "test_size = " + num/100 + "\n";
				res += "X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)";
			}
		return res;	
		}

		@Override
		public String writeAlgorithm() {
			String res = "";
			
			if(model.getAlgorithm().getAlgorithm() instanceof SVR) {
				SVR svr = (SVR) model.getAlgorithm().getAlgorithm();
				String kernel = "";
				String c = "";
				res = "clf = svm.SVR(";
				if(svr.getKernel() != null) {
					kernel = "kernel='"+ svr.getKernel() + "'";
				}
				if(svr.getC() != null) {
					c = "C='"+ svr.getC() + "'";
					if (!kernel.equals("") ) kernel += ", ";
				}
				res += kernel + c;
				res += ")";
			}
			
			return res;
		}

		@Override
		public String writeModel() {
			// TODO Auto-generated method stub
			return "[Model] not implemented yet";
		}

		@Override
		public String writeValidationMetric() {
			// TODO Auto-generated method stub
			return "[ValidationMetric] not implemented yet";
		}

		@Override
		public String writePrint() {
			// TODO Auto-generated method stub
			return "[Print] not implemented yet";
		}

		@Override
		public String generate(MMLModel model, String filelocation) {
			// TODO Auto-generated method stub
			String ret = "";
			this.model = model;
			ret = this.writeImport() + "\n";
			ret += this.writeFileLocation(filelocation)  + "\n";
			ret += this.writeVarCible() + "\n";
			ret += this.writeAlgorithm() + "\n";
			ret += this.writeStratificationMethod();
			return ret;
		}
		
	}
