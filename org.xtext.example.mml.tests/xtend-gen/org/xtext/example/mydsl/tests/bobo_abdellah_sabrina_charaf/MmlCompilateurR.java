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
  
  private MmlCompilateurR() {
  }
  
  public MmlCompilateurR(final MMLModel mmlModel, final MLAlgorithm MLA) {
    if ((mmlModel == null)) {
      throw new IllegalArgumentException((("you should initialize " + this.name) + "with non null value"));
    }
    this.mmlModel = mmlModel;
    this.MLA = MLA;
  }
  
  public String metricCode(final EList<ValidationMetric> VMList) {
    String result = "";
    for (final ValidationMetric item : VMList) {
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
  
  public void algorithmCode(final String predictiveColName, final String predictors) {
    final MLAlgorithm _switchValue = this.MLA;
    boolean _matched = false;
    if (_switchValue instanceof DT) {
      _matched=true;
      String _imports = this.imports;
      this.imports = (_imports + "library(rpart)\n");
      final DTImpl dtImpl = ((DTImpl) this.MLA);
      String _rasCode = this.rasCode;
      this.rasCode = (_rasCode + (((("fit <- rpart(" + predictiveColName) + "~") + predictors) + 
        ", data = train, method = \'class\', control = rpart.control(cp = 0"));
      int _max_depth = dtImpl.getMax_depth();
      boolean _tripleNotEquals = (_max_depth != 0);
      if (_tripleNotEquals) {
        String _rasCode_1 = this.rasCode;
        int _max_depth_1 = dtImpl.getMax_depth();
        String _plus = (",maxdepth = " + Integer.valueOf(_max_depth_1));
        this.rasCode = (_rasCode_1 + _plus);
      }
      String _rasCode_2 = this.rasCode;
      this.rasCode = (_rasCode_2 + ("))" + "\n"));
      String _rasCode_3 = this.rasCode;
      this.rasCode = (_rasCode_3 + ("result1<-predict(fit, test, type = \'class\')" + "\n"));
      String _rasCode_4 = this.rasCode;
      this.rasCode = (_rasCode_4 + ("result <- as.numeric(levels(result1))[result1]" + "\n"));
    }
    if (!_matched) {
      if (_switchValue instanceof SVR) {
        _matched=true;
        InputOutput.<String>println("SVR");
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
        String _rasCode = this.rasCode;
        this.rasCode = (_rasCode + 
          ((((("fit <- randomForest(" + predictiveColName) + "~") + predictors) + 
            ", data = train, method = \'class\')") + "\n"));
        String _rasCode_1 = this.rasCode;
        this.rasCode = (_rasCode_1 + ("result<-predict(fit, test, type = \'class\')" + "\n"));
      }
    }
    if (!_matched) {
      if (_switchValue instanceof SGD) {
        _matched=true;
        InputOutput.<String>println("SGD");
      }
    }
    if (!_matched) {
      InputOutput.<String>println("default");
    }
  }
  
  public String render() {
    final DataInput dataInput = this.mmlModel.getInput();
    final RFormula formula = this.mmlModel.getFormula();
    final Validation validation = this.mmlModel.getValidation();
    final StratificationMethod stratificationMethod = validation.getStratification();
    final EList<ValidationMetric> VMList = validation.getMetric();
    this.fileLocation = dataInput.getFilelocation();
    double split_ratio = 0.7;
    boolean _matched = false;
    if (stratificationMethod instanceof CrossValidation) {
      _matched=true;
    }
    if (!_matched) {
      if (stratificationMethod instanceof TrainingTest) {
        _matched=true;
        final TrainingTest trainingTest = ((TrainingTest) stratificationMethod);
        split_ratio = trainingTest.getNumber();
      }
    }
    String _imports = this.imports;
    this.imports = (_imports + ("library(dplyr)" + "\n"));
    String _imports_1 = this.imports;
    this.imports = (_imports_1 + ("library(caTools)" + "\n"));
    String _imports_2 = this.imports;
    this.imports = (_imports_2 + ("library(Metrics)" + "\n"));
    String predictiveColName = "colnames(df)[ncol(df)-1]";
    int predictiveColumn = 0;
    String predictors = ".";
    final String DEFAULT_COLUMN_SEPARATOR = ",";
    final String csv_separator = DEFAULT_COLUMN_SEPARATOR;
    String _rasCode = this.rasCode;
    this.rasCode = (_rasCode + ((((("read.csv(\"" + this.fileLocation) + "\",head = TRUE, sep=\"") + csv_separator) + "\")->df") + "\n"));
    String selectX = ("df %>% select(-c())->X" + "\n");
    String selectY = ("df %>% select(c())->Y" + "\n");
    if ((formula != null)) {
      FormulaItem _predictive = formula.getPredictive();
      boolean _tripleNotEquals = (_predictive != null);
      if (_tripleNotEquals) {
        predictiveColName = formula.getPredictive().getColName();
        predictiveColumn = formula.getPredictive().getColumn();
        if ((predictiveColName != null)) {
          selectX = ((("df %>% select(-c(" + predictiveColName) + "))->X") + "\n");
        } else {
          selectY = ((("df %>% select(-c(colnames(df)[" + Integer.valueOf(predictiveColumn)) + "]))->X") + "\n");
        }
      }
      XFormula _predictors = formula.getPredictors();
      boolean _matched_1 = false;
      if (_predictors instanceof AllVariables) {
        _matched_1=true;
        predictors = ".";
      }
      if (!_matched_1) {
        if (_predictors instanceof PredictorVariables) {
          _matched_1=true;
          XFormula _predictors_1 = formula.getPredictors();
          final PredictorVariables predictorVariables = ((PredictorVariables) _predictors_1);
          EList<FormulaItem> _vars = predictorVariables.getVars();
          for (final FormulaItem formulaItem : _vars) {
            String _colName = formulaItem.getColName();
            boolean _tripleNotEquals_1 = (_colName != null);
            if (_tripleNotEquals_1) {
              String _predictors_2 = predictors;
              String _colName_1 = formulaItem.getColName();
              String _plus = (_colName_1 + " + ");
              predictors = (_predictors_2 + _plus);
            } else {
              String _predictors_3 = predictors;
              int _column = formulaItem.getColumn();
              String _plus_1 = ("colnames(df)[" + Integer.valueOf(_column));
              String _plus_2 = (_plus_1 + "] + ");
              predictors = (_predictors_3 + _plus_2);
            }
          }
          int _length = predictors.length();
          int _minus = (_length - 4);
          predictors = predictors.substring(0, _minus);
        }
      }
    }
    String _rasCode_1 = this.rasCode;
    this.rasCode = (_rasCode_1 + (selectX + selectY));
    String _rasCode_2 = this.rasCode;
    this.rasCode = (_rasCode_2 + ((((("sample.split(df$" + predictiveColName) + ",SplitRatio=") + Double.valueOf(split_ratio)) + ")->split_index") + "\n"));
    String _rasCode_3 = this.rasCode;
    this.rasCode = (_rasCode_3 + ("train<-subset(df,split_index==T)" + "\n"));
    String _rasCode_4 = this.rasCode;
    this.rasCode = (_rasCode_4 + ("test<-subset(df,split_index==F)" + "\n"));
    this.algorithmCode(predictiveColName, predictors);
    this.rasCode = (this.imports + this.rasCode);
    String _rasCode_5 = this.rasCode;
    this.rasCode = (_rasCode_5 + ((("test %>% select(c(" + predictiveColName) + "))->testY") + "\n"));
    String _rasCode_6 = this.rasCode;
    this.rasCode = (_rasCode_6 + ("testY2 <- testY[,1:length(testY)]" + "\n"));
    String _rasCode_7 = this.rasCode;
    String _metricCode = this.metricCode(VMList);
    this.rasCode = (_rasCode_7 + _metricCode);
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
      final double startTime = ((double) _currentTimeMillis);
      final Process p = Runtime.getRuntime().exec(("Rscript " + filePath));
      long _currentTimeMillis_1 = System.currentTimeMillis();
      final double endTime = ((double) _currentTimeMillis_1);
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
