package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.MLAlgorithm;

@SuppressWarnings("all")
public class Output {
  public MLAlgorithm mlAlgorithm;
  
  public FrameworkLang frameworkLang;
  
  public String fileLocation;
  
  public long timestamp;
  
  public Map<String, Double> validationMetric_result = new HashMap<String, Double>();
  
  @Override
  public String toString() {
    String result = "";
    String _result = result;
    String _simpleName = (this.mlAlgorithm.getClass().getInterfaces()[0]).getSimpleName();
    String _plus = ((("[ Data: " + this.fileLocation) + ", Algorithm : ") + _simpleName);
    String _plus_1 = (_plus + 
      ", Framework: ");
    String _plus_2 = (_plus_1 + this.frameworkLang);
    String _plus_3 = (_plus_2 + ", ExectutionTime: ");
    String _plus_4 = (_plus_3 + Long.valueOf(this.timestamp));
    String _plus_5 = (_plus_4 + "]");
    result = (_result + _plus_5);
    Set<Map.Entry<String, Double>> _entrySet = this.validationMetric_result.entrySet();
    for (final Map.Entry e : _entrySet) {
      {
        final String key = e.getKey().toString();
        final double value = (Double.valueOf(e.getValue().toString())).doubleValue();
        String _result_1 = result;
        result = (_result_1 + (((", Metric : " + key) + ", value: ") + Double.valueOf(value)));
      }
    }
    return result;
  }
}
