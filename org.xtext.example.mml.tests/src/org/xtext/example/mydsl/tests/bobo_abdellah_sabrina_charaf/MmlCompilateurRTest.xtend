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

	// test1 R
	@Test
	def void mmlcompR() {

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

	// test1 Python
	@Test
	def void mmlcompP() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm DT
				mlframework Scikit-Learn
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

	// test2 R
	@Test
	def void mmlcompR2() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ,
				mlframework R
				algorithm DT
				mlframework R
				algorithm RandomForest
				formula "medv" ~ .
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error mean_squared_error
		''')

		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}

	// test 2 Python
	@Test
	def void mmlcompP2() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm DT
				mlframework Scikit-Learn
				algorithm RandomForest
				formula "medv" ~ .
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error mean_squared_error
		''')

		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}

// test 3 R
	@Test
	def void mmlcompR3() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ,
				mlframework R
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

	// test 3 Python
	@Test
	def void mmlcompP3() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm DT
				mlframework Scikit-Learn
				algorithm SVR
				CrossValidation { 
					numRepetitionCross 8
				}
				mean_absolute_error mean_squared_error
		''')

		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}

	//test 4 R
	@Test
	def void mmlcompR4() {

		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ,
				mlframework R
				algorithm DT
				mlframework R
				algorithm SVR
				formula "medv" ~ .
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error
		''')

		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}

	//test 5 R
	@Test
	def void mmlcompR5() {

		val MMLModel result = parseHelper.parse('''
<<<<<<< HEAD
			datainput "boston.csv" separator ;
				mlframework Scikit-Learn
				algorithm RandomForest
				mlframework Scikit-Learn
				algorithm SVR
				mlframework Scikit-Learn
				algorithm DT
=======
			datainput "boston.csv" separator ,
				mlframework R
				algorithm SVR
				mlframework R
				algorithm DT
				mlframework R
				algorithm RandomForest
				formula "medv" ~ .
>>>>>>> b3e6f391ea5254fa776a9b37aafe2822cac7ee56
				CrossValidation { 
					numRepetitionCross 8
				}
				mean_absolute_error mean_squared_error
		''')

		val MmlCompilateur mmlcompilateur = new MmlCompilateur(result);
		mmlcompilateur.render();
	}
}
