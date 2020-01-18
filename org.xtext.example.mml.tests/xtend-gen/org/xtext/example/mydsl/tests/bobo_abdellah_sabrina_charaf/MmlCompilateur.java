package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf.MmlCompilateurR;

@SuppressWarnings("all")
public class MmlCompilateur {
  private MMLModel mmlModel;
  
  private final String name = "MmlCompilateur";
  
  private MmlCompilateur() {
  }
  
  public MmlCompilateur(final MMLModel mmlModel) {
    if ((mmlModel == null)) {
      throw new IllegalArgumentException((("you should initialize " + this.name) + "with non null value"));
    }
    this.mmlModel = mmlModel;
  }
  
  public EList<MLChoiceAlgorithm> removeDuplicate(final EList<MLChoiceAlgorithm> MLCAList) {
    final EList<MLChoiceAlgorithm> result = new UniqueEList<MLChoiceAlgorithm>();
    final List<String> list = new ArrayList<String>();
    for (final MLChoiceAlgorithm item : MLCAList) {
      {
        final MLAlgorithm MLA = item.getAlgorithm();
        final FrameworkLang framework = item.getFramework();
        String _simpleName = MLA.getClass().getSimpleName();
        String _plus = (_simpleName + "_");
        String _simpleName_1 = framework.getClass().getSimpleName();
        String _plus_1 = (_plus + _simpleName_1);
        boolean _contains = list.contains(_plus_1);
        boolean _not = (!_contains);
        if (_not) {
          String _simpleName_2 = MLA.getClass().getSimpleName();
          String _plus_2 = (_simpleName_2 + "_");
          String _simpleName_3 = framework.getClass().getSimpleName();
          String _plus_3 = (_plus_2 + _simpleName_3);
          list.add(_plus_3);
          result.add(item);
        }
      }
    }
    return result;
  }
  
  public String render() {
    String result = "";
    final EList<MLChoiceAlgorithm> MLCAList = this.removeDuplicate(this.mmlModel.getAlgorithms());
    for (final MLChoiceAlgorithm item : MLCAList) {
      {
        final FrameworkLang framework = item.getFramework();
        if (framework != null) {
          switch (framework) {
            case SCIKIT:
              break;
            case R:
              final MmlCompilateurR mmlCompilateurR = new MmlCompilateurR(this.mmlModel);
              result = mmlCompilateurR.render();
              break;
            default:
              break;
          }
        } else {
        }
      }
    }
    return result;
  }
}
