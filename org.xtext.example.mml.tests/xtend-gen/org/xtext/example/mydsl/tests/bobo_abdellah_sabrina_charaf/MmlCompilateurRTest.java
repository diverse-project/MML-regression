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
      _builder.append("datainput \"boston.csv\" separator ,");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("mlframework R");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("algorithm DT ");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("mlframework R");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("algorithm RandomForest");
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
      _builder.append("mean_absolute_error mean_squared_error");
      _builder.newLine();
      final MMLModel result = this.parseHelper.parse(_builder);
      final MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
      mmlcompilateur.render();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void mmlcomp1() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("datainput \"boston.csv\" separator ,");
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
      final MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
      mmlcompilateur.render();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
