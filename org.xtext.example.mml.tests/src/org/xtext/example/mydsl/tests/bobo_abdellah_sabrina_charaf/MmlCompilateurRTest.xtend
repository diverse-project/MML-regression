package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf

import com.google.inject.Inject
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
			datainput "boston.csv" separator ,
				mlframework R
				algorithm DT
				mlframework R
				algorithm RandomForest
				formula "medv" ~ .
				CrossValidation { 
					numRepetitionCross 8
				}
				mean_absolute_error mean_squared_error
		''')
		
		
		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}

	@Test
	def void mmlcomp2() {
	
		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm DT
				mlframework Scikit-Learn
				algorithm RandomForest
				formula .
				CrossValidation { 
					numRepetitionCross 8
				}
				mean_absolute_error mean_squared_error
		''')
		
		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}

@Test
	def void mmlcomp3() {
	
		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm DT
				mlframework R
				algorithm SVR
				CrossValidation { 
					numRepetitionCross 8
				}
				mean_absolute_error mean_squared_error
		''')
		
		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}
	@Test
	def void mmlcomp4() {
	
		
		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm DT
				mlframework Scikit-Learn
				algorithm SVR
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error
		''')
		
		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}
	@Test
	def void mmlcomp5() {
	
		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm DT
				mlframework R
				algorithm DT
				mlframework Scikit-Learn
				algorithm SVR
				mlframework Scikit-Learn
				algorithm RandomForest
				CrossValidation { 
					numRepetitionCross 8
				}
				mean_absolute_error mean_squared_error
		''')
		
		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}
}
