package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import com.google.inject.Inject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.MmlInjectorProvider;
import org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf.MmlCompilateur;

@ExtendWith(InjectionExtension.class)
@InjectWith(MmlInjectorProvider.class)
@SuppressWarnings("all")
public class MmlCompilateurRTest {
  @Inject
  private ParseHelper<MMLModel> parseHelper;
  
  @Test
  public void mmlcomp() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("datainput \"boston.csv\" separator ;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("mlframework Scikit-Learn");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("algorithm DT");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("mlframework Scikit-Learn");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("algorithm RandomForest");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("formula \"medv\" ~ .");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("CrossValidation { ");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("numRepetitionCross 8");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("mean_absolute_error mean_squared_error");
      _builder.newLine();
      final MMLModel result = this.parseHelper.parse(_builder);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("datainput \"boston.csv\" separator ;");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("mlframework Scikit-Learn");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("algorithm DT");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("mlframework Scikit-Learn");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("algorithm RandomForest");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("formula .");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("CrossValidation { ");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("numRepetitionCross 8");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("mean_absolute_error mean_squared_error");
      _builder_1.newLine();
      final MMLModel result1 = this.parseHelper.parse(_builder_1);
      StringConcatenation _builder_2 = new StringConcatenation();
      _builder_2.append("datainput \"boston.csv\" separator ;");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("mlframework Scikit-Learn");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("algorithm DT");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("mlframework R");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("algorithm SVR");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("CrossValidation { ");
      _builder_2.newLine();
      _builder_2.append("\t\t");
      _builder_2.append("numRepetitionCross 8");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("}");
      _builder_2.newLine();
      _builder_2.append("\t");
      _builder_2.append("mean_absolute_error mean_squared_error");
      _builder_2.newLine();
      final MMLModel result2 = this.parseHelper.parse(_builder_2);
      StringConcatenation _builder_3 = new StringConcatenation();
      _builder_3.append("datainput \"boston.csv\" separator ;");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("mlframework Scikit-Learn");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("algorithm DT");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("mlframework Scikit-Learn");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("algorithm SVR");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("TrainingTest { ");
      _builder_3.newLine();
      _builder_3.append("\t\t");
      _builder_3.append("percentageTraining 70");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("}");
      _builder_3.newLine();
      _builder_3.append("\t");
      _builder_3.append("mean_absolute_error");
      _builder_3.newLine();
      final MMLModel result3 = this.parseHelper.parse(_builder_3);
      StringConcatenation _builder_4 = new StringConcatenation();
      _builder_4.append("datainput \"boston.csv\" separator ;");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("mlframework Scikit-Learn");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("algorithm DT");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("mlframework R");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("algorithm DT");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("mlframework Scikit-Learn");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("algorithm SVR");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("mlframework Scikit-Learn");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("algorithm RandomForest");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("CrossValidation { ");
      _builder_4.newLine();
      _builder_4.append("\t\t");
      _builder_4.append("numRepetitionCross 8");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("}");
      _builder_4.newLine();
      _builder_4.append("\t");
      _builder_4.append("mean_absolute_error mean_squared_error");
      _builder_4.newLine();
      final MMLModel result4 = this.parseHelper.parse(_builder_4);
      final MmlCompilateur mmlcompilateur = new MmlCompilateur(result3);
      mmlcompilateur.render();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
