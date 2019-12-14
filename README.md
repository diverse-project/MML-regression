# MML-regression

Authors : Picard Michaël

## Code

Disclaimer : we are novice in machine learning field, so we couldn't grasp the whole understanding needed to look back and judge our work. So, our implementation and interpretation can be mistaken.

### Compilators
The **Scikit-Learn** compilator is complete and running.

The **Weka** compilator is complete and running, but lack the one feature and is could be unstable. `SVR` kernel option can't be set to `Linear`, since it's not implemented in **Weka**, so if `Linear` is selected, an error message will tell the user about it and select `Poly` instead.

The **R** compilator is TODO

The **XGBoost** compilator (with **Python**) is TODO

### Testing and experiment

We have implemented 10 program in mml that test an algorithm and a training method in each software, in order to ensure that our compiler will work flawlessly.
Each program is compiled, and run, regardless of the result or the encountered error, and the result (or the abscence of result) are displayed.

For the experiment, we have create a collection which can collect result about each run, such as the result and the time spent. This result are then displayed conveniently for us to work with.

## Experiment
### How
Just run `MmlEvaluator` with the mml's files as parameters.

It will automatically try to build and run all the mml program, displaying the result of the analysis after a while. You could retrieve the result as **Markdown** table, as we have done in this report.

### Result

TODO

### Interpretation

TODO

-> SGD is really bad, inadapted to our data or our implementation is really bad?