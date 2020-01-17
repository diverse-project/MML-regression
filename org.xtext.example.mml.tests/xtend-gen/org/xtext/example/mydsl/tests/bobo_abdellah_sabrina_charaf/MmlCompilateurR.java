package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import com.google.common.io.Files;
import com.google.inject.Inject;
import java.io.File;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.AllVariables;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
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
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
@SuppressWarnings("all")
public class MmlCompilateurR {
  @Inject
  private ParseHelper<MMLModel> parseHelper;
  
  @Test
  public void mmlcomp() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("datainput \"boston.csv\" separator ;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("mlframework scikit-learn");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("algorithm DT");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("formula \"medv\" ~ .");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("TrainingTest { ");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("percentageTraining 70");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("mean_absolute_error");
      _builder.newLine();
      final MMLModel result = this.parseHelper.parse(_builder);
      final DataInput dataInput = result.getInput();
      final MLChoiceAlgorithm MLCAlgorithm = result.getAlgorithm();
      final RFormula formula = result.getFormula();
      final Validation validation = result.getValidation();
      final StratificationMethod stratificationMethod = validation.getStratification();
      final EList<ValidationMetric> eList = validation.getMetric();
      final String fileLocation = dataInput.getFilelocation();
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
      String imports = ("library(dplyr)" + "\n");
      String _imports = imports;
      imports = (_imports + ("library(caTools)" + "\n"));
      String predictiveColName = "colnames(df)[ncol(df)-1]";
      int predictiveColumn = 0;
      String predictors = ".";
      final String DEFAULT_COLUMN_SEPARATOR = ",";
      final String csv_separator = DEFAULT_COLUMN_SEPARATOR;
      String rasCode = ((((("read.csv(\"" + fileLocation) + "\",head = TRUE, sep=\"") + csv_separator) + "\")->df") + "\n");
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
      String _rasCode = rasCode;
      rasCode = (_rasCode + (selectX + selectY));
      String _rasCode_1 = rasCode;
      rasCode = (_rasCode_1 + ((((("sample.split(df$" + predictiveColName) + ",SplitRatio=") + Double.valueOf(split_ratio)) + ")->split_index") + "\n"));
      String _rasCode_2 = rasCode;
      rasCode = (_rasCode_2 + ("train<-subset(df,split_index==T)" + "\n"));
      String _rasCode_3 = rasCode;
      rasCode = (_rasCode_3 + ("test<-subset(df,split_index==F)" + "\n"));
      final MLAlgorithm mlA = MLCAlgorithm.getAlgorithm();
      boolean _matched_2 = false;
      if (mlA instanceof DT) {
        _matched_2=true;
        String _imports_1 = imports;
        imports = (_imports_1 + "library(rpart)\n");
        final DTImpl dtImpl = ((DTImpl) mlA);
        if ((predictiveColName != null)) {
          String _rasCode_4 = rasCode;
          rasCode = (_rasCode_4 + (((("fit <- rpart(" + predictiveColName) + "~") + predictors) + 
            ", data = train, method = \'class\', control = rpart.control(cp = 0"));
          int _max_depth = dtImpl.getMax_depth();
          boolean _tripleNotEquals_1 = (_max_depth != 0);
          if (_tripleNotEquals_1) {
            String _rasCode_5 = rasCode;
            int _max_depth_1 = dtImpl.getMax_depth();
            String _plus = (",maxdepth = " + Integer.valueOf(_max_depth_1));
            rasCode = (_rasCode_5 + _plus);
          }
          String _rasCode_6 = rasCode;
          rasCode = (_rasCode_6 + ("))" + "\n"));
        }
      }
      if (!_matched_2) {
        if (mlA instanceof SVR) {
          _matched_2=true;
          InputOutput.<String>println("SVR");
        }
      }
      if (!_matched_2) {
        if (mlA instanceof GTB) {
          _matched_2=true;
          InputOutput.<String>println("GTB");
        }
      }
      if (!_matched_2) {
        if (mlA instanceof RandomForest) {
          _matched_2=true;
          InputOutput.<String>println("RandomForest");
        }
      }
      if (!_matched_2) {
        if (mlA instanceof SGD) {
          _matched_2=true;
          InputOutput.<String>println("SGD");
        }
      }
      if (!_matched_2) {
        InputOutput.<String>println("default");
      }
      rasCode = (imports + rasCode);
      String _rasCode_4 = rasCode;
      rasCode = (_rasCode_4 + ((("test %>% select(c(" + predictiveColName) + "))->testY") + "\n"));
      byte[] _bytes = rasCode.getBytes();
      File _file = new File("mml.R");
      Files.write(_bytes, _file);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
