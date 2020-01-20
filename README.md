# Rapport MML_Regression
Master 2 Ingénierie Logicielle en alternance

Nicolas CROQ<br/>
Nicolas GIRAULT<br/>
Emerick MOREL<br/>
Axel SAMMANI

## Implementation choices :
In order to generate different machine learning program we decided to implement the template method design pattern.<br/>
It allows us to preserve the overall structure and sequence of the algorithm, the skeleton of operations, in a specific class.<br/>
Using this design pattern makes easier for developers to add new machine learning frameworks such as R, weka etc.<br/>
Each dedicated machine learning framework java classes will have 5 common methods to generate the program structure : 
* generateImports
* generateAlgorithm
* generatePredictive
* generateValidation
* addLastOperations


## Questions :
We manually wrote codes for weka and R but despite the use of different datasets, the lack of explicit documentation and our limited machine learning skills led us to put them aside because they would have taken us longer to understand them better.<br/><br/>
We had difficulties to create working programs using different framework but we finally managed to generate programs in xgboost and scikit-learn using DT, GTB, SGD, RF and SVR algorithms. 


### Question 1 :
>Ecrire un programme qui, étant donné un programme MML, retourne le classement des frameworks+algorithmes de machine learning.<br/>
Le classement s’effectuera en fonction de la métrique utilisée, on aura ainsi un “podium” (top 3).<br/>
Pour cette question, et pour le reste du projet, on utilisera la cross-validation avec CV=5 pour calculer les métriques (et donc le classement).<br/>

In order to display the ranking of frameworks + machine learning algorithms, we have written a program that will first execute X times (X correspond to the number of time you want the program to execute each file) the generated programs, using the principle of multithreading (it’s configuration is based on number of core of the machine), finally it will gather the results obtained (results of the metrics, as well as the execution time for question 3) to make the classification.

Console result:<br/>
![alt text](images/Q1.png?raw=true "answer Q1")

### Question 2 :
>Utiliser d’autres jeux de données et d’autres frameworks/algorithmes pour tester votre solution

Use of the program with the dataset runtimeXText/boston/house-prices-uk.csv :<br/> 

MML file : <br/>

    datainput "/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/house-prices-uk.csv" separator ,
    mlframework scikit-learn
    algorithm SVR kernel=linear
    mlframework scikit-learn
    algorithm DT
    mlframework scikit-learn
    algorithm RandomForest
    mlframework scikit-learn
    algorithm SGD
    mlframework scikit-learn
    algorithm GTB
    formula "PriceN"~"PriceA"+"ChangeA"+"ChangeN"+"PriceM"+"ChangeM"+"PriceO"+"ChangeO"
    CrossValidation {
        numRepetitionCross 5
    }
    mean_squared_error mean_absolute_error median_absolute_error

Console result (iterations = 4, timeout = 30s):<br/>
![alt text](images/Q2_1.png?raw=true "answer Q2")

Use of the program with the dataset `runtimeXText/boston/x264-netflix.csv` :<br/> 

MML file : <br/>
    
    datainput "/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/x264-netflix.csv" separator ,
    mlframework scikit-learn
    algorithm SVR kernel=linear
    mlframework scikit-learn
    algorithm DT
    mlframework scikit-learn
    algorithm RandomForest
    mlframework scikit-learn
    algorithm SGD
    mlframework scikit-learn
    algorithm GTB
    formula "usertime"~.
    CrossValidation {
        numRepetitionCross 5
    }
    mean_squared_error mean_absolute_error median_absolute_error

Console result (itérations = 4, timeout = 30s):<br/>
![alt text](images/Q2_2.png?raw=true "answer Q2 bis")

### Question 3 :
>Adapter/paramétrer votre programme de la question Q1 pour pouvoir établir un classement en fonction du temps d’exécution

Result obtained in console after adding the execution time option :<br/>
![alt text](images/Q3.png?raw=true "answer Q3")

### Question 4 :
>Conduire une évaluation rigoureuse des solutions de machine learning grâce à votre infrastructure MML.<br/>
Concrètement, et en vous appuyant sur vos expérimentations/données, vous rédigerez un rapport en anglais, au format Markdown, pour répondre aux questions suivantes:<br/>
>- Sur vos jeux de données, quel framework+algorithme est le mieux classé (en comparaison d’autres frameworks) en termes de temps d’exécution? 
>- de précision?

We based the results on an average of 20 executions per framework + algorithm in CrossValidation 5-fold with the following mml file :<br/>

    datainput "/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/BostonHousing.csv" separator ,
    mlframework scikit-learn
    algorithm SVR kernel=linear
    mlframework scikit-learn
    algorithm DT
    mlframework scikit-learn
    algorithm RandomForest
    mlframework scikit-learn
    algorithm SGD
    mlframework scikit-learn
    algorithm GTB
    mlframework xgboost
    algorithm SVR kernel=linear
    mlframework xgboost
    algorithm DT
    mlframework xgboost
    algorithm RandomForest
    mlframework xgboost
    algorithm SGD
    mlframework xgboost
    algorithm GTB
    formula "medv"~.
    CrossValidation {
        numRepetitionCross 5
    }
    mean_squared_error mean_absolute_error median_absolute_error

Results obtained :<br/>

![alt text](images/TABLE1.png?raw=true "TABLE 1")

>Parmi les frameworks et algorithmes de machine learning, y a-t-il des implémentations significativement plus lentes/précises que d’autres?

Looking at the results we can see that Scikit-Learn and xgboost with the SVR algorithm are taking way more time than the other algorithm (50 seconds for 1.5 seconds on average) which was also the case with other datasets. Scikit learn with SGD gives particularly high results compared to the other.

>Etant donné un algorithme de machine learning (e.g., decision tree), est-ce qu’on observe des différences (temps d’exécution/précision) entre les frameworks ?

![alt text](images/TABLE2.png?raw=true "TABLE 2")

The mean squared error tells you how close a regression line is to a set of points. It does this by taking the distances from the points to the regression line (these distances are the “errors”) and squaring them. By knowing that, It’s allows us to see that xgboost gives us more precises results than scikit-learn with decision tree, but with a higher execution time.

>Y a-t-il des jeux de données plus difficiles à traiter (en termes de précision/temps d’exécution) ?  

The smaller the dataset the lower the accuracy, but the run time is fast. Conversely, the larger the data set, the higher the running time, the higher the accuracy.

>Au vu des résultats, quel framework de machine learning recommanderiez-vous ?

It’s depends on the type of algorithm we want to proceed. For example, we saw that xgboost was more efficient than sklearn with decision tree algorithms. But if we take Stochastic Gradient Descent (SGD) algorithms, we noted that sklearn was faster and more accurate than Xgboost. 



