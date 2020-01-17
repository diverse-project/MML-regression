package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import com.google.common.io.Files;
import com.google.inject.Inject;
import java.io.File;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
@SuppressWarnings("all")
public class MmlCompilateurR {
  @Inject
  private static ParseHelper<MMLModel> parseHelper;
  
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
      final MMLModel result = MmlCompilateurR.parseHelper.parse(_builder);
      final DataInput dataInput = result.getInput();
      final String fileLocation = dataInput.getFilelocation();
      final String importDPLYR = "library(dplyr)";
      final String DEFAULT_COLUMN_SEPARATOR = ",";
      final String csv_separator = DEFAULT_COLUMN_SEPARATOR;
      String rasCode = ((((((("read.csv(\"" + fileLocation) + ",head = TRUE, sep=\"") + csv_separator) + "\"\")->df") + 
        "\n") + importDPLYR) + "\n");
      final MLChoiceAlgorithm MLCAlgorithm = result.getAlgorithm();
      final RFormula formula = result.getFormula();
      if ((formula == null)) {
        String _rasCode = rasCode;
        rasCode = (_rasCode + ("df %>% select(-c())->X " + "\n"));
      } else {
        final String predictiveColName = formula.getPredictive().getColName();
        final int predictiveColumn = formula.getPredictive().getColumn();
        if ((predictiveColName != null)) {
          String _rasCode_1 = rasCode;
          rasCode = (_rasCode_1 + ((("df %>% select(-c(" + predictiveColName) + "))->X ") + "\n"));
        } else {
          String _rasCode_2 = rasCode;
          rasCode = (_rasCode_2 + ((("df %>% select(-c(" + Integer.valueOf(predictiveColumn)) + "))->X ") + "\n"));
        }
      }
      final MLAlgorithm mlA = MLCAlgorithm.getAlgorithm();
      byte[] _bytes = rasCode.getBytes();
      File _file = new File("mml.R");
      Files.write(_bytes, _file);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
