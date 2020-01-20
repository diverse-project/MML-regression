package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.xtext.example.mydsl.mml.AllVariables;
import org.xtext.example.mydsl.mml.CSVParsingConfiguration;
import org.xtext.example.mydsl.mml.CSVSeparator;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.XFormula;
import org.xtext.example.mydsl.mml.impl.DTImpl;
import org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf.Output;

@SuppressWarnings("all")
public class MmlCompilateurR {
  private MMLModel mmlModel;
  
  private MLAlgorithm MLA;
  
  private final String name = "MmlCompilateurR";
  
  private String imports = "";
  
  private String rasCode = "";
  
  private final EList<String> metricList = new UniqueEList<String>();
  
  private String fileLocation = "";
  
  private int numRepetitionCross = 0;
  
  private String predictiveColName = "colnames(df)[ncol(df)-1]";
  
  private int numberOfMetric = 0;
  
  private EList<ValidationMetric> VMList = new UniqueEList<ValidationMetric>();
  
  private double split_ratio = 0.7;
  
  private String predictors = ".";
  
  private String csv_separator;
  
  private MmlCompilateurR() {
  }
  
  public MmlCompilateurR(final MMLModel mmlModel, final MLAlgorithm MLA) {
    if ((mmlModel == null)) {
      throw new IllegalArgumentException((("you should initialize " + this.name) + "with non null value"));
    }
    this.mmlModel = mmlModel;
    this.MLA = MLA;
  }
  
  public String metricCode() {
    String result = "";
    for (final ValidationMetric item : this.VMList) {
      if (item != null) {
        switch (item) {
          case MSE:
            String _result = result;
            result = (_result + ("mse(testY2, result)" + "\n"));
            this.metricList.add("mse");
            break;
          case MAE:
            String _result_1 = result;
            result = (_result_1 + ("mae(testY2, result)" + "\n"));
            this.metricList.add("mae");
            break;
          case MAPE:
            String _result_2 = result;
            result = (_result_2 + ("mape(testY2, result)" + "\n"));
            this.metricList.add("mape");
            break;
          default:
            break;
        }
      } else {
      }
    }
    return result;
  }
  
  public String metricCodeCrossV() {
    String result = "";
    this.numberOfMetric = this.VMList.size();
    for (int i = 0; (i < this.VMList.size()); i++) {
      ValidationMetric _get = this.VMList.get(i);
      if (_get != null) {
        switch (_get) {
          case MSE:
            String _result = result;
            result = (_result + ((((("my_array[k," + Integer.valueOf((i + 1))) + "] <- mse(df$") + this.predictiveColName) + "[bloc==k],result)") + "\n"));
            this.metricList.add("mse");
            break;
          case MAE:
            String _result_1 = result;
            result = (_result_1 + ((((("my_array[k," + Integer.valueOf((i + 1))) + "] <- mae(df$") + this.predictiveColName) + "[bloc==k],result)") + "\n"));
            this.metricList.add("mae");
            break;
          case MAPE:
            String _result_2 = result;
            result = (_result_2 + 
              ((((("my_array[k," + Integer.valueOf((i + 1))) + "] <- mape(df$") + this.predictiveColName) + "[bloc==k],result)") + "\n"));
            this.metricList.add("mape");
            break;
          default:
            break;
        }
      } else {
      }
    }
    return result;
  }
  
  public void algorithmCodeCrossValid(final String predictors) {
    final String metricCodeCrossV = this.metricCodeCrossV();
    String _rasCode = this.rasCode;
    this.rasCode = (_rasCode + ("n <- nrow(df)" + "\n"));
    String _rasCode_1 = this.rasCode;
    this.rasCode = (_rasCode_1 + (("K <- " + Integer.valueOf(this.numRepetitionCross)) + "\n"));
    String _rasCode_2 = this.rasCode;
    this.rasCode = (_rasCode_2 + ("taille <- n%/%K" + "\n"));
    String _rasCode_3 = this.rasCode;
    this.rasCode = (_rasCode_3 + ("set.seed(5)" + "\n"));
    String _rasCode_4 = this.rasCode;
    this.rasCode = (_rasCode_4 + ("alea <- runif(n)" + "\n"));
    String _rasCode_5 = this.rasCode;
    this.rasCode = (_rasCode_5 + ("rang <- rank(alea)" + "\n"));
    String _rasCode_6 = this.rasCode;
    this.rasCode = (_rasCode_6 + ("bloc <- (rang-1)%/%taille + 1" + "\n"));
    String _rasCode_7 = this.rasCode;
    this.rasCode = (_rasCode_7 + ("bloc <- as.factor(bloc)" + "\n"));
    String _rasCode_8 = this.rasCode;
    this.rasCode = (_rasCode_8 + (("numberOfMetric <-" + Integer.valueOf(this.numberOfMetric)) + "\n"));
    String _rasCode_9 = this.rasCode;
    this.rasCode = (_rasCode_9 + ((("my_array <- array(0,dim=c(K," + Integer.valueOf(this.numberOfMetric)) + ")) ") + "\n"));
    String _rasCode_10 = this.rasCode;
    this.rasCode = (_rasCode_10 + ("for (k in 1:K) {" + "\n"));
    final MLAlgorithm _switchValue = this.MLA;
    boolean _matched = false;
    if (_switchValue instanceof DT) {
      _matched=true;
      String _imports = this.imports;
      this.imports = (_imports + "library(rpart)\n");
      final DTImpl dtImpl = ((DTImpl) this.MLA);
      String _rasCode_11 = this.rasCode;
      this.rasCode = (_rasCode_11 + (((("fit <- rpart(" + this.predictiveColName) + "~") + predictors) + 
        ", data = df[bloc!=k,], method = \'class\', control = rpart.control(cp = 0"));
      int _max_depth = dtImpl.getMax_depth();
      boolean _tripleNotEquals = (_max_depth != 0);
      if (_tripleNotEquals) {
        String _rasCode_12 = this.rasCode;
        int _max_depth_1 = dtImpl.getMax_depth();
        String _plus = (",maxdepth = " + Integer.valueOf(_max_depth_1));
        this.rasCode = (_rasCode_12 + _plus);
      }
      String _rasCode_13 = this.rasCode;
      this.rasCode = (_rasCode_13 + ("))" + "\n"));
      String _rasCode_14 = this.rasCode;
      this.rasCode = (_rasCode_14 + ("result <-predict(fit, df[bloc == k,], type = \'class\')" + "\n"));
      String _rasCode_15 = this.rasCode;
      this.rasCode = (_rasCode_15 + ("result <- as.numeric(levels(result))[result]" + "\n"));
    }
    if (!_matched) {
      if (_switchValue instanceof SVR) {
        _matched=true;
        String _imports = this.imports;
        this.imports = (_imports + "library(e1071)\n");
        String _rasCode_11 = this.rasCode;
        this.rasCode = (_rasCode_11 + 
          ((((("fit <- svm(" + this.predictiveColName) + "~") + predictors) + ", data = df[bloc!=k,], method = \'class\')") + "\n"));
        String _rasCode_12 = this.rasCode;
        this.rasCode = (_rasCode_12 + ("result<-predict(fit, df[bloc == k,], type = \'class\')" + "\n"));
      }
    }
    if (!_matched) {
      if (_switchValue instanceof GTB) {
        _matched=true;
        InputOutput.<String>println("GTB");
      }
    }
    if (!_matched) {
      if (_switchValue instanceof RandomForest) {
        _matched=true;
        String _imports = this.imports;
        this.imports = (_imports + "library(randomForest)\n");
        String _rasCode_11 = this.rasCode;
        this.rasCode = (_rasCode_11 + 
          ((((("fit <- randomForest(" + this.predictiveColName) + "~") + predictors) + 
            ", data = df[bloc!=k,], method = \'class\')") + "\n"));
        String _rasCode_12 = this.rasCode;
        this.rasCode = (_rasCode_12 + ("result<-predict(fit, df[bloc == k,], type = \'class\')" + "\n"));
      }
    }
    if (!_matched) {
      if (_switchValue instanceof SGD) {
        _matched=true;
        String _imports = this.imports;
        this.imports = (_imports + "library(sgd)\n");
        String _rasCode_11 = this.rasCode;
        this.rasCode = (_rasCode_11 + 
          ((((("fit <- sgd(" + this.predictiveColName) + "~") + predictors) + ", data = df[bloc!=k,], method = \'class\')") + "\n"));
        String _rasCode_12 = this.rasCode;
        this.rasCode = (_rasCode_12 + ("result<-predict(fit, df[bloc == k,], type = \'class\')" + "\n"));
      }
    }
    if (!_matched) {
      InputOutput.<String>println("default");
    }
    String _rasCode_11 = this.rasCode;
    this.rasCode = (_rasCode_11 + metricCodeCrossV);
    String _rasCode_12 = this.rasCode;
    this.rasCode = (_rasCode_12 + ("}" + "\n"));
    String _rasCode_13 = this.rasCode;
    this.rasCode = (_rasCode_13 + ("for (i in 1:numberOfMetric) {" + "\n"));
    String _rasCode_14 = this.rasCode;
    this.rasCode = (_rasCode_14 + ("value <- mean(my_array[,i])" + "\n"));
    String _rasCode_15 = this.rasCode;
    this.rasCode = (_rasCode_15 + ("print(value)" + "\n"));
    String _rasCode_16 = this.rasCode;
    this.rasCode = (_rasCode_16 + ("}" + "\n"));
  }
  
  public void algorithmCode(final String predictors) {
    String _rasCode = this.rasCode;
    this.rasCode = (_rasCode + ((("df %>% select(-c(" + this.predictiveColName) + "))->X") + "\n"));
    String _rasCode_1 = this.rasCode;
    this.rasCode = (_rasCode_1 + ((("df %>% select(c(" + this.predictiveColName) + "))->Y") + "\n"));
    String _rasCode_2 = this.rasCode;
    this.rasCode = (_rasCode_2 + ((((("sample.split(df$" + this.predictiveColName) + ",SplitRatio=") + Double.valueOf(this.split_ratio)) + ")->split_index") + "\n"));
    String _rasCode_3 = this.rasCode;
    this.rasCode = (_rasCode_3 + ("train<-subset(df,split_index==T)" + "\n"));
    String _rasCode_4 = this.rasCode;
    this.rasCode = (_rasCode_4 + ("test<-subset(df,split_index==F)" + "\n"));
    final MLAlgorithm _switchValue = this.MLA;
    boolean _matched = false;
    if (_switchValue instanceof DT) {
      _matched=true;
      String _imports = this.imports;
      this.imports = (_imports + "library(rpart)\n");
      final DTImpl dtImpl = ((DTImpl) this.MLA);
      String _rasCode_5 = this.rasCode;
      this.rasCode = (_rasCode_5 + (((("fit <- rpart(" + this.predictiveColName) + "~") + predictors) + 
        ", data = train, method = \'class\', control = rpart.control(cp = 0"));
      int _max_depth = dtImpl.getMax_depth();
      boolean _tripleNotEquals = (_max_depth != 0);
      if (_tripleNotEquals) {
        String _rasCode_6 = this.rasCode;
        int _max_depth_1 = dtImpl.getMax_depth();
        String _plus = (",maxdepth = " + Integer.valueOf(_max_depth_1));
        this.rasCode = (_rasCode_6 + _plus);
      }
      String _rasCode_7 = this.rasCode;
      this.rasCode = (_rasCode_7 + ("))" + "\n"));
      String _rasCode_8 = this.rasCode;
      this.rasCode = (_rasCode_8 + ("result<-predict(fit, test, type = \'class\')" + "\n"));
      String _rasCode_9 = this.rasCode;
      this.rasCode = (_rasCode_9 + ("result <- as.numeric(levels(result))[result]" + "\n"));
    }
    if (!_matched) {
      if (_switchValue instanceof SVR) {
        _matched=true;
        String _imports = this.imports;
        this.imports = (_imports + "library(e1071)\n");
        String _rasCode_5 = this.rasCode;
        this.rasCode = (_rasCode_5 + 
          ((((("fit <- svm(" + this.predictiveColName) + "~") + predictors) + ", data = train, method = \'class\')") + "\n"));
        String _rasCode_6 = this.rasCode;
        this.rasCode = (_rasCode_6 + ("result<-predict(fit, test, type = \'class\')" + "\n"));
      }
    }
    if (!_matched) {
      if (_switchValue instanceof GTB) {
        _matched=true;
        InputOutput.<String>println("GTB");
      }
    }
    if (!_matched) {
      if (_switchValue instanceof RandomForest) {
        _matched=true;
        String _imports = this.imports;
        this.imports = (_imports + "library(randomForest)\n");
        String _rasCode_5 = this.rasCode;
        this.rasCode = (_rasCode_5 + 
          ((((("fit <- randomForest(" + this.predictiveColName) + "~") + predictors) + 
            ", data = train, method = \'class\')") + "\n"));
        String _rasCode_6 = this.rasCode;
        this.rasCode = (_rasCode_6 + ("result<-predict(fit, test, type = \'class\')" + "\n"));
      }
    }
    if (!_matched) {
      if (_switchValue instanceof SGD) {
        _matched=true;
        String _imports = this.imports;
        this.imports = (_imports + "library(sgd)\n");
        String _rasCode_5 = this.rasCode;
        this.rasCode = (_rasCode_5 + 
          ((((("fit <- sgd(" + this.predictiveColName) + "~") + predictors) + ", data = train, method = \'class\')") + "\n"));
        String _rasCode_6 = this.rasCode;
        this.rasCode = (_rasCode_6 + ("result<-predict(fit, test, type = \'class\')" + "\n"));
      }
    }
    if (!_matched) {
      InputOutput.<String>println("default");
    }
    String _rasCode_5 = this.rasCode;
    this.rasCode = (_rasCode_5 + ((("test %>% select(c(" + this.predictiveColName) + "))->testY") + "\n"));
    String _rasCode_6 = this.rasCode;
    this.rasCode = (_rasCode_6 + ("testY2 <- testY[,1:length(testY)]" + "\n"));
    String _rasCode_7 = this.rasCode;
    String _metricCode = this.metricCode();
    this.rasCode = (_rasCode_7 + _metricCode);
  }
  
  public String requiredImports() {
    String result = "";
    String _result = result;
    result = (_result + ("library(dplyr)" + "\n"));
    String _result_1 = result;
    result = (_result_1 + ("library(caTools)" + "\n"));
    String _result_2 = result;
    result = (_result_2 + ("library(Metrics)" + "\n"));
    return result;
  }
  
  public String getCsvSeparator(final CSVParsingConfiguration csv_separator) {
    if ((csv_separator != null)) {
      CSVSeparator _sep = csv_separator.getSep();
      boolean _tripleEquals = (_sep == CSVSeparator.SEMI_COLON);
      if (_tripleEquals) {
        return ";";
      }
    }
    return ",";
  }
  
  public void setPredictiveAndPredictors(final RFormula formula) {
    int predictiveColumn = 0;
    if ((formula != null)) {
      FormulaItem _predictive = formula.getPredictive();
      boolean _tripleNotEquals = (_predictive != null);
      if (_tripleNotEquals) {
        this.predictiveColName = formula.getPredictive().getColName();
        predictiveColumn = formula.getPredictive().getColumn();
        if ((this.predictiveColName == null)) {
          this.predictiveColName = (("colnames(df)[" + Integer.valueOf(predictiveColumn)) + "]");
        }
      }
      XFormula _predictors = formula.getPredictors();
      boolean _matched = false;
      if (_predictors instanceof AllVariables) {
        _matched=true;
        this.predictors = ".";
      }
      if (!_matched) {
        if (_predictors instanceof PredictorVariables) {
          _matched=true;
          XFormula _predictors_1 = formula.getPredictors();
          final PredictorVariables predictorVariables = ((PredictorVariables) _predictors_1);
          EList<FormulaItem> _vars = predictorVariables.getVars();
          for (final FormulaItem formulaItem : _vars) {
            String _colName = formulaItem.getColName();
            boolean _tripleNotEquals_1 = (_colName != null);
            if (_tripleNotEquals_1) {
              String _predictors_2 = this.predictors;
              String _colName_1 = formulaItem.getColName();
              String _plus = (_colName_1 + " + ");
              this.predictors = (_predictors_2 + _plus);
            } else {
              String _predictors_3 = this.predictors;
              int _column = formulaItem.getColumn();
              String _plus_1 = ("colnames(df)[" + Integer.valueOf(_column));
              String _plus_2 = (_plus_1 + "] + ");
              this.predictors = (_predictors_3 + _plus_2);
            }
          }
          int _length = this.predictors.length();
          int _minus = (_length - 4);
          this.predictors = this.predictors.substring(0, _minus);
        }
      }
    }
  }
  
  public String render() {
    final DataInput dataInput = this.mmlModel.getInput();
    final RFormula formula = this.mmlModel.getFormula();
    final Validation validation = this.mmlModel.getValidation();
    final StratificationMethod stratificationMethod = validation.getStratification();
    this.VMList = validation.getMetric();
    this.fileLocation = dataInput.getFilelocation();
    boolean _matched = false;
    if (stratificationMethod instanceof CrossValidation) {
      _matched=true;
      final CrossValidation crossValidation = ((CrossValidation) stratificationMethod);
      this.numRepetitionCross = crossValidation.getNumber();
    }
    if (!_matched) {
      if (stratificationMethod instanceof TrainingTest) {
        _matched=true;
        final TrainingTest trainingTest = ((TrainingTest) stratificationMethod);
        int _number = trainingTest.getNumber();
        double _divide = (((double) _number) / 100.0);
        this.split_ratio = _divide;
      }
    }
    String _imports = this.imports;
    String _requiredImports = this.requiredImports();
    this.imports = (_imports + _requiredImports);
    this.csv_separator = this.getCsvSeparator(dataInput.getParsingInstruction());
    String _rasCode = this.rasCode;
    this.rasCode = (_rasCode + ((((("read.csv(\"" + this.fileLocation) + "\",head = TRUE, sep=\"") + this.csv_separator) + "\")->df") + "\n"));
    this.setPredictiveAndPredictors(formula);
    if ((this.numRepetitionCross > 0)) {
      this.algorithmCodeCrossValid(this.predictors);
    } else {
      this.algorithmCode(this.predictors);
    }
    this.rasCode = (this.imports + this.rasCode);
    return this.rasCode;
  }
  
  public Output compile() {
    try {
      final Output result = new Output();
      result.frameworkLang = FrameworkLang.R;
      result.mlAlgorithm = this.MLA;
      final String render = this.render();
      result.fileLocation = this.fileLocation;
      final String filePath = "mml.R";
      byte[] _bytes = render.getBytes();
      File _file = new File(filePath);
      Files.write(_bytes, _file);
      long _currentTimeMillis = System.currentTimeMillis();
      final long startTime = ((long) _currentTimeMillis);
      final Process p = Runtime.getRuntime().exec(("Rscript " + filePath));
      long _currentTimeMillis_1 = System.currentTimeMillis();
      final long endTime = ((long) _currentTimeMillis_1);
      InputStream _inputStream = p.getInputStream();
      InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
      final BufferedReader in = new BufferedReader(_inputStreamReader);
      String line = null;
      final EList<Double> metricValueList = new UniqueEList<Double>();
      while (((line = in.readLine()) != null)) {
        {
          final String[] compileResult = line.split(" ");
          metricValueList.add(Double.valueOf(compileResult[1]));
        }
      }
      for (int i = 0; (i < metricValueList.size()); i++) {
        result.validationMetric_result.put(this.metricList.get(i), metricValueList.get(i));
      }
      result.timestamp = (endTime - startTime);
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
