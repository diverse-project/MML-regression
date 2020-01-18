package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import com.google.common.io.Files
import com.google.inject.Inject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.xtext.example.mydsl.mml.MMLModel
import org.xtext.example.mydsl.tests.MmlInjectorProvider

@ExtendWith(InjectionExtension)
@InjectWith(MmlInjectorProvider)
class MmlCompilateurRTest {

	@Inject
	ParseHelper<MMLModel> parseHelper

	@Test
	def void mmlcomp() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework scikit-learn
				algorithm DT
				formula "medv" ~ .
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error
		''')

		val MmlCompilateurR mmlCompilateurR = new MmlCompilateurR(result);
		val String rasCode = mmlCompilateurR.render(); 
		Files.write(rasCode.getBytes(), new File("mml.R"));
		
//		val MmlCompilateurScikitLearn mmlCompilateurScikitLearn = new MmlCompilateurScikitLearn(result);
//		val String rasCode1 = mmlCompilateurScikitLearn.compileDataInput();
//		Files.write(rasCode1.getBytes(), new File("mml.py"));
		
		
	}

}
