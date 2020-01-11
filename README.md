# MML Project

### Structure of the project 

To generate the different kind of program and to be able to add new languages easily, 
the architecture of our project uses several design patterns :

* Strategy : to change the language to generate
* Template Method : to generate every language in the same way. 
Define method to implement for the content of the program. only define method for write line of code
* Factory : to get the right strategy

UML diagram showing the interactions between each design pattern : 
 ![alt text](images/umlMML.png "uml")


### Features


In this project the real difficulty was to create functional programs which uses those framework. 
Indeed, the lack of knowledge in machine learning or in these languages/framework was a brake for the development of this project.

We failed to create a program in Weka, but we were able to generate programs in this 3 frameworks :
* Scikit 
* XGBoost
* R
 
Here, XGBoost uses python to work, the structure of the program is the same as Scikit but some functions changes.
R also need many libraries to work, for each algorithm needed (Decision Tree, Random Forest...).


To use this program we must create .mml file 
and change the variable ``path`` with the path to our .mml in the file MmlParsgingJavaTest (package org.xtext.example.mml.tests).
Then the program will parse the .mml and generate all desired programs. 
Every results are then print in the console. 
The files containing these programs are put in the directory prog-gen (at the root of org.xtext.example.mml.tests) in a directory named with the current date.

Some program used for our tests are in a file named programmeMML.

### Results

We realize tests on Boston.csv to determine which is the best framework to use. 
Here are the results with the function Random Forest : 

 ![alt text](images/results.png "results")
 
 As we can see, R have the best results, but needs a bigger execution time. 
 XGBoost and Scikit have approximately the same execution time but XGBoost has better results.
 
 So, we suggest to use R if we need the best result no matter the execution time, but if we want to be quick Xgboost is the best choice. 
 
 Moreover, XGboost's environment is much easier to set up. It can be use with Python with is pretty easy install.
 R took much more time, to install the environment, the libraries an it is harder to handle.