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
  
  public double timestamp;
  
  public Map<String, Double> validationMetric_result = new HashMap<String, Double>();
  
  @Override
  public String toString() {
    String result = "";
    String _result = result;
    result = (_result + ((((((("[ Data: " + this.fileLocation) + " Algorithm : ") + this.mlAlgorithm) + 
      " Framework: ") + this.frameworkLang) + " ExectutionTime: ") + Double.valueOf(this.timestamp)));
    Set<Map.Entry<String, Double>> _entrySet = this.validationMetric_result.entrySet();
    for (final Map.Entry e : _entrySet) {
      {
        final String key = e.getKey().toString();
        final double value = (Double.valueOf(e.getValue().toString())).doubleValue();
        String _result_1 = result;
        result = (_result_1 + (((" Metric : " + key) + " value: ") + Double.valueOf(value)));
      }
    }
    return result;
  }
}
