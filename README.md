# MML-regression

Authors : Picard Michaël, Pavy Myriam, Petit Valentin, Chertier Kevin

## Code

Disclaimer : we are new to machine learning, so we couldn't grasp the whole understanding needed to look back and judge our work. So, our implementation and interpretation can be inaccurate.

### Architecture

The main package is composed of :
- `MmlCompiler` : API to compile an MML program.
- `MmlEvaluator` : An entry point to compile, run and compare the result of an MML program.
- `MmlParsingKMMVTest` : The test case.
- `MmlResult` : A collection class, used by MmlEvaluator.
- `Compilateur` package :
  - `Compilateur` : An interface, describing all the methods a compilator has to provide.
  - `Pair` : A class to manage Pairs.
  - `Utils` : An utility class.
  - `R`, `sklearn`, `weka` and `xgboost` packages : contain the implementations of each framework.

### Compilers
The **Scikit-Learn** compiler is complete and running. You need to have **python3** and two python's packages, `pandas` and `sklearn` to use it.

The **Weka** compiler is complete and running, but lacks one feature. `SVR` kernel option can't be set to `Linear` since it's not implemented in **Weka**, so if `Linear` is selected, an error message will inform the user and the program will stop. You need to have at least **Java 8** to use it.

The **R** compiler is complete and running, but lacks the cross validation on SGD, since we couldn't find a satisfying implementation. You need to install the system package `r-base` and `r-base-dev` (for ubuntu), along with the following R libraries : `caret`, `rpart`, `gbm`, `randomForest`, `e1071`, `kernlab` and `sgd`.

The **XGBoost** compiler (with **Python**) is particulary difficult to work with. Indeed, XGBoost selects automatically the best algorithm depending on your dataset. It was then not really possible to adapt our language to XGBoost. Thus, the calls to XGBoost are made almost regardless of the algorithm chosen in mml. You need the same dependencies as **Scikit-Learn** compiler with also `xgboost` python package.

### Testing and experiment

We have implemented 12 programs in mml that test an algorithm and a training method using the 3 frameworks, in order to ensure that our compilers work flawlessly.
Each program is compiled, and run, regardless of the result or the encountered errors, and the result (or the absence of result) is displayed.

For the experiment, we created a collection which collects the result and the execution time for each run. This result is then displayed conveniently for us to work with.

## Experiment
### How
Just run `MmlEvaluator` with the mml's files as parameters.

It will automatically try to build and run all the mml programs, displaying the result of the analysis after a while. You could retrieve the result as a **Markdown** table, as we did in this report.

We used boston.mml, x264.mml and x264_netflix.mml, replacing all the `True` and `False` values by the full uppercase equivalents (`TRUE` and `FALSE`) in the x264*.csv files, in order to prevent R from failing for the SGD algorithm.

### Results

| Data | Algorithm | Framework | Execution Time (sec) | MAPE (%) |
|---|---|---|---|---|
| boston.csv | DT | xgboost | 1.133573377 | 2.8937462278339057 |
| boston.csv | DT | Weka | 1.585691822 | 3.1943202904338226 |
| boston.csv | DT | R | 2.799517952 | 3.442562 |
| boston.csv | DT | scikit-learn | 0.76479899 | 4.028899242865462 |
| boston.csv | GTB | R | 4.14968708 | 2.280601 |
| boston.csv | GTB | xgboost | 0.965572886 | 2.8937462278339057 |
| boston.csv | GTB | scikit-learn | 1.187764589 | 2.9903503654331347 |
| boston.csv | GTB | Weka | 1.502886134 | 3.4707646784300668 |
| boston.csv | RF | Weka | 1.979110393 | 2.097034044978956 |
| boston.csv | RF | R | 16.864751681 | 2.205648 |
| boston.csv | RF | scikit-learn | 2.122135898 | 3.017133779848573 |
| boston.csv | RF | xgboost | 1.21093358 | 3.4595606715210296 |
| boston.csv | SGD | xgboost | 1.011155293 | 2.8937462278339057 |
| boston.csv | SGD | Weka | 1.443167963 | 22.532806324110673 |
| boston.csv | SGD | scikit-learn | 0.721721155 | 1.1622960588844716E14 |
| boston.csv | SVR | R | 10.625018648 | 2.562566 |
| boston.csv | SVR | xgboost | 1.386332443 | 2.8937462278339057 |
| boston.csv | SVR | Weka | 1.698826271 | 3.2247242713060507 |
| boston.csv | SVR | scikit-learn | 0.762683275 | 6.142426082759478 |
| x264.csv | DT | scikit-learn | 0.857569609 | 0.06290884622623753 |
| x264.csv | DT | xgboost | 0.976240807 | 0.06476712297101042 |
| x264.csv | DT | Weka | 2.020502001 | 0.08278945861012546 |
| x264.csv | DT | R | 3.38543663 | 0.2045158 |
| x264.csv | GTB | scikit-learn | 1.781868578 | 0.06342732192445807 |
| x264.csv | GTB | xgboost | 0.993572265 | 0.06476712297101042 |
| x264.csv | GTB | R | 3.941740846 | 0.07622274 |
| x264.csv | GTB | Weka | 1.620880438 | 0.196061288668904 |
| x264.csv | RF | R | 33.22749568 | 0.047949 |
| x264.csv | RF | scikit-learn | 2.701399093 | 0.053032076525503555 |
| x264.csv | RF | Weka | 3.569256242 | 0.05739139542533088 |
| x264.csv | RF | xgboost | 0.92070304 | 0.18041077462449623 |
| x264.csv | SGD | xgboost | 0.973295962 | 0.06476712297101042 |
| x264.csv | SGD | Weka | 1.573210797 | 4.009559895833336 |
| x264.csv | SGD | scikit-learn | 1.033800596 | 3.1103549859305635E21 |
| x264.csv | SVR | xgboost | 0.987910961 | 0.06476712297101042 |
| x264.csv | SVR | Weka | 14.552418624 | 0.09792003232978479 |
| x264.csv | SVR | scikit-learn | 1.201010778 | 0.7337023781419834 |
| x264.csv | SVR | R | 15.475981132 | 5.065024E19 |
| x264_netflix.csv | DT | xgboost | 0.971985282 | 0.2688682050385952 |
| x264_netflix.csv | DT | scikit-learn | 0.736688122 | 0.275387261434218 |
| x264_netflix.csv | DT | Weka | 1.516941556 | 0.36785891253579134 |
| x264_netflix.csv | DT | R | 2.736554799 | 1.258885 |
| x264_netflix.csv | GTB | scikit-learn | 1.361222191 | 0.26161797710902523 |
| x264_netflix.csv | GTB | xgboost | 1.001088717 | 0.2688682050385952 |
| x264_netflix.csv | GTB | R | 4.222282445 | 0.3134925 |
| x264_netflix.csv | GTB | Weka | 1.543983694 | 1.306714792828421 |
| x264_netflix.csv | RF | R | 31.316768079 | 0.2098105 |
| x264_netflix.csv | RF | scikit-learn | 2.433944023 | 0.21176141223414296 |
| x264_netflix.csv | RF | Weka | 2.390037003 | 0.3053649253934903 |
| x264_netflix.csv | RF | xgboost | 0.941979368 | 1.2029915055314417 |
| x264_netflix.csv | SGD | xgboost | 0.963284639 | 0.2688682050385952 |
| x264_netflix.csv | SGD | Weka | 1.564196897 | 29.202870659722194 |
| x264_netflix.csv | SGD | scikit-learn | 0.741845317 | 2.520699417380451E23 |
| x264_netflix.csv | SVR | xgboost | 0.968631968 | 0.2688682050385952 |
| x264_netflix.csv | SVR | Weka | 24.806184722 | 0.3497032613613583 |
| x264_netflix.csv | SVR | scikit-learn | 0.951010997 | 7.052318289762591 |
| x264_netflix.csv | SVR | R | 14.937443756 | 1.180777E23 |

Top 3 by results : 
- R RF with x264.csv took 33.22749568 seconds with 0.047949 MAPE
- scikit-learn RF with x264.csv took 2.701399093 seconds with 0.053032076525503555 MAPE
- Weka RF with x264.csv took 3.569256242 seconds with 0.05739139542533088 MAPE

Results ordered by efficiency : 

| Data | Algorithm | Framework | Execution Time (sec) | MAPE (%) |
|---|---|---|---|---|
| x264.csv | RF | R | 33.22749568 | 0.047949 |
| x264.csv | RF | scikit-learn | 2.701399093 | 0.053032076525503555 |
| x264.csv | RF | Weka | 3.569256242 | 0.05739139542533088 |
| x264.csv | DT | scikit-learn | 0.857569609 | 0.06290884622623753 |
| x264.csv | GTB | scikit-learn | 1.781868578 | 0.06342732192445807 |
| x264.csv | DT | xgboost | 0.976240807 | 0.06476712297101042 |
| x264.csv | GTB | xgboost | 0.993572265 | 0.06476712297101042 |
| x264.csv | SGD | xgboost | 0.973295962 | 0.06476712297101042 |
| x264.csv | SVR | xgboost | 0.987910961 | 0.06476712297101042 |
| x264.csv | GTB | R | 3.941740846 | 0.07622274 |
| x264.csv | DT | Weka | 2.020502001 | 0.08278945861012546 |
| x264.csv | SVR | Weka | 14.552418624 | 0.09792003232978479 |
| x264.csv | RF | xgboost | 0.92070304 | 0.18041077462449623 |
| x264.csv | GTB | Weka | 1.620880438 | 0.196061288668904 |
| x264.csv | DT | R | 3.38543663 | 0.2045158 |
| x264_netflix.csv | RF | R | 31.316768079 | 0.2098105 |
| x264_netflix.csv | RF | scikit-learn | 2.433944023 | 0.21176141223414296 |
| x264_netflix.csv | GTB | scikit-learn | 1.361222191 | 0.26161797710902523 |
| x264_netflix.csv | DT | xgboost | 0.971985282 | 0.2688682050385952 |
| x264_netflix.csv | GTB | xgboost | 1.001088717 | 0.2688682050385952 |
| x264_netflix.csv | SGD | xgboost | 0.963284639 | 0.2688682050385952 |
| x264_netflix.csv | SVR | xgboost | 0.968631968 | 0.2688682050385952 |
| x264_netflix.csv | DT | scikit-learn | 0.736688122 | 0.275387261434218 |
| x264_netflix.csv | RF | Weka | 2.390037003 | 0.3053649253934903 |
| x264_netflix.csv | GTB | R | 4.222282445 | 0.3134925 |
| x264_netflix.csv | SVR | Weka | 24.806184722 | 0.3497032613613583 |
| x264_netflix.csv | DT | Weka | 1.516941556 | 0.36785891253579134 |
| x264.csv | SVR | scikit-learn | 1.201010778 | 0.7337023781419834 |
| x264_netflix.csv | RF | xgboost | 0.941979368 | 1.2029915055314417 |
| x264_netflix.csv | DT | R | 2.736554799 | 1.258885 |
| x264_netflix.csv | GTB | Weka | 1.543983694 | 1.306714792828421 |
| boston.csv | RF | Weka | 1.979110393 | 2.097034044978956 |
| boston.csv | RF | R | 16.864751681 | 2.205648 |
| boston.csv | GTB | R | 4.14968708 | 2.280601 |
| boston.csv | SVR | R | 10.625018648 | 2.562566 |
| boston.csv | DT | xgboost | 1.133573377 | 2.8937462278339057 |
| boston.csv | GTB | xgboost | 0.965572886 | 2.8937462278339057 |
| boston.csv | SGD | xgboost | 1.011155293 | 2.8937462278339057 |
| boston.csv | SVR | xgboost | 1.386332443 | 2.8937462278339057 |
| boston.csv | GTB | scikit-learn | 1.187764589 | 2.9903503654331347 |
| boston.csv | RF | scikit-learn | 2.122135898 | 3.017133779848573 |
| boston.csv | DT | Weka | 1.585691822 | 3.1943202904338226 |
| boston.csv | SVR | Weka | 1.698826271 | 3.2247242713060507 |
| boston.csv | DT | R | 2.799517952 | 3.442562 |
| boston.csv | RF | xgboost | 1.21093358 | 3.4595606715210296 |
| boston.csv | GTB | Weka | 1.502886134 | 3.4707646784300668 |
| x264.csv | SGD | Weka | 1.573210797 | 4.009559895833336 |
| boston.csv | DT | scikit-learn | 0.76479899 | 4.028899242865462 |
| boston.csv | SVR | scikit-learn | 0.762683275 | 6.142426082759478 |
| x264_netflix.csv | SVR | scikit-learn | 0.951010997 | 7.052318289762591 |
| boston.csv | SGD | Weka | 1.443167963 | 22.532806324110673 |
| x264_netflix.csv | SGD | Weka | 1.564196897 | 29.202870659722194 |
| boston.csv | SGD | scikit-learn | 0.721721155 | 1.1622960588844716E14 |
| x264.csv | SVR | R | 15.475981132 | 5.065024E19 |
| x264.csv | SGD | scikit-learn | 1.033800596 | 3.1103549859305635E21 |
| x264_netflix.csv | SVR | R | 14.937443756 | 1.180777E23 |
| x264_netflix.csv | SGD | scikit-learn | 0.741845317 | 2.520699417380451E23 |

Top 3 by execution time : 
- scikit-learn SGD with boston.csv took 0.721721155 seconds with 1.1622960588844716E14 MAPE
- scikit-learn DT with x264_netflix.csv took 0.736688122 seconds with 0.275387261434218 MAPE
- scikit-learn SGD with x264_netflix.csv took 0.741845317 seconds with 2.520699417380451E23 MAPE

Results ordered by execution time : 

| Data | Algorithm | Framework | Execution Time (sec) | MAPE (%) |
|---|---|---|---|---|
| boston.csv | SGD | scikit-learn | 0.721721155 | 1.1622960588844716E14 |
| x264_netflix.csv | DT | scikit-learn | 0.736688122 | 0.275387261434218 |
| x264_netflix.csv | SGD | scikit-learn | 0.741845317 | 2.520699417380451E23 |
| boston.csv | SVR | scikit-learn | 0.762683275 | 6.142426082759478 |
| boston.csv | DT | scikit-learn | 0.76479899 | 4.028899242865462 |
| x264.csv | DT | scikit-learn | 0.857569609 | 0.06290884622623753 |
| x264.csv | RF | xgboost | 0.92070304 | 0.18041077462449623 |
| x264_netflix.csv | RF | xgboost | 0.941979368 | 1.2029915055314417 |
| x264_netflix.csv | SVR | scikit-learn | 0.951010997 | 7.052318289762591 |
| x264_netflix.csv | SGD | xgboost | 0.963284639 | 0.2688682050385952 |
| boston.csv | GTB | xgboost | 0.965572886 | 2.8937462278339057 |
| x264_netflix.csv | SVR | xgboost | 0.968631968 | 0.2688682050385952 |
| x264_netflix.csv | DT | xgboost | 0.971985282 | 0.2688682050385952 |
| x264.csv | SGD | xgboost | 0.973295962 | 0.06476712297101042 |
| x264.csv | DT | xgboost | 0.976240807 | 0.06476712297101042 |
| x264.csv | SVR | xgboost | 0.987910961 | 0.06476712297101042 |
| x264.csv | GTB | xgboost | 0.993572265 | 0.06476712297101042 |
| x264_netflix.csv | GTB | xgboost | 1.001088717 | 0.2688682050385952 |
| boston.csv | SGD | xgboost | 1.011155293 | 2.8937462278339057 |
| x264.csv | SGD | scikit-learn | 1.033800596 | 3.1103549859305635E21 |
| boston.csv | DT | xgboost | 1.133573377 | 2.8937462278339057 |
| boston.csv | GTB | scikit-learn | 1.187764589 | 2.9903503654331347 |
| x264.csv | SVR | scikit-learn | 1.201010778 | 0.7337023781419834 |
| boston.csv | RF | xgboost | 1.21093358 | 3.4595606715210296 |
| x264_netflix.csv | GTB | scikit-learn | 1.361222191 | 0.26161797710902523 |
| boston.csv | SVR | xgboost | 1.386332443 | 2.8937462278339057 |
| boston.csv | SGD | Weka | 1.443167963 | 22.532806324110673 |
| boston.csv | GTB | Weka | 1.502886134 | 3.4707646784300668 |
| x264_netflix.csv | DT | Weka | 1.516941556 | 0.36785891253579134 |
| x264_netflix.csv | GTB | Weka | 1.543983694 | 1.306714792828421 |
| x264_netflix.csv | SGD | Weka | 1.564196897 | 29.202870659722194 |
| x264.csv | SGD | Weka | 1.573210797 | 4.009559895833336 |
| boston.csv | DT | Weka | 1.585691822 | 3.1943202904338226 |
| x264.csv | GTB | Weka | 1.620880438 | 0.196061288668904 |
| boston.csv | SVR | Weka | 1.698826271 | 3.2247242713060507 |
| x264.csv | GTB | scikit-learn | 1.781868578 | 0.06342732192445807 |
| boston.csv | RF | Weka | 1.979110393 | 2.097034044978956 |
| x264.csv | DT | Weka | 2.020502001 | 0.08278945861012546 |
| boston.csv | RF | scikit-learn | 2.122135898 | 3.017133779848573 |
| x264_netflix.csv | RF | Weka | 2.390037003 | 0.3053649253934903 |
| x264_netflix.csv | RF | scikit-learn | 2.433944023 | 0.21176141223414296 |
| x264.csv | RF | scikit-learn | 2.701399093 | 0.053032076525503555 |
| x264_netflix.csv | DT | R | 2.736554799 | 1.258885 |
| boston.csv | DT | R | 2.799517952 | 3.442562 |
| x264.csv | DT | R | 3.38543663 | 0.2045158 |
| x264.csv | RF | Weka | 3.569256242 | 0.05739139542533088 |
| x264.csv | GTB | R | 3.941740846 | 0.07622274 |
| boston.csv | GTB | R | 4.14968708 | 2.280601 |
| x264_netflix.csv | GTB | R | 4.222282445 | 0.3134925 |
| boston.csv | SVR | R | 10.625018648 | 2.562566 |
| x264.csv | SVR | Weka | 14.552418624 | 0.09792003232978479 |
| x264_netflix.csv | SVR | R | 14.937443756 | 1.180777E23 |
| x264.csv | SVR | R | 15.475981132 | 5.065024E19 |
| boston.csv | RF | R | 16.864751681 | 2.205648 |
| x264_netflix.csv | SVR | Weka | 24.806184722 | 0.3497032613613583 |
| x264_netflix.csv | RF | R | 31.316768079 | 0.2098105 |
| x264.csv | RF | R | 33.22749568 | 0.047949 |

### Interpretation

We made the following observations :
- scikit-learn is the fastest framework on DT, regardless of the data, and also on SGD with boston.csv and x264_netflix.csv, but XGBoost outperforms it in all other cases. So Python's framework is globally faster.
- On each csv file, the RandomForest algorithm has performed the best, with R being the best framework in term of overall results.
- SGD and SVR efficiency is really poor in some cases.
- All files are equivalent in term of evaluation time, but in term of efficiency, x264 yields the best efficiency and boston the worst.
- Each framework is pretty equivalent to others in terms of efficiency, except in some weird cases.

Overall, we advise to use either Python's frameworks or R's, because of how natural and easy to use these languages are. But if you have to choose between all, it will be better to use sklearn as a beginner, since it provides a lot of useful documentation, when others can be quite obscure to understand.

## Feedback

Although the project can be quite interesting, the lack of context or knowledge of machine learning held us back from making a really accurate implementation and from optimizing it.
The poor efficiency of SVR and SGD is maybe due to the fact that we didn't choose the correct implementation or didn't tune it the good way? We don't have the elements to answer that question.
Also, it was quite a huge project to work on, and the workload on other projects prevented us from improving our work the way we wanted.