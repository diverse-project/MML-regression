# MML-regression
multi machine learning language (for regression)

<h1>Comparative report</h1>

<h2>Comparative table of frameworks and algorithms</h2>

The following results are gathered using cross-validation with a file split in 5.

|  Data  | Algorithm        | Framework | Execution Time | MAPE (%) | MAE | MSE |
|:------:|------------------|-----------|----------------|----------|-----|-----|
| Boston |   RandomForest   |  sklearn  |                |          | 3.2 |23.6 |
| Boston |       SVR        |  sklearn  |                |          | 3.8 |33.8 |
| Boston |       GTB        |  sklearn  |                |          | 2.9 |18.4 |
| Boston |       SGD        |  sklearn  |                |          |     |     |
| Boston |       DT         |  sklearn  |                |          | 5.4 |83.7 |
| Boston |   RandomForest   |    Weka   |                |          |     |     |
| Boston |       SVR        |    Weka   |                |          |     |     |
| Boston |       GTB        |    Weka   |                |          |     |     |
| Boston |       SGD        |    Weka   |                |          |     |     |
| Boston |       DT         |    Weka   |                |          |     |     |
| Boston |   RandomForest   |  XGBoost  |                |          |     |     |
| Boston |       SVR        |  XGBoost  |                |          |     |     |
| Boston |       GTB        |  XGBoost  |                |          |     |     |
| Boston |       SGD        |  XGBoost  |                |          |     |     |
| Boston |       DT         |  XGBoost  |                |          |     |     |
| Boston |   RandomForest   |     R     |                |          |     |     |
| Boston |       SVR        |     R     |                |          |     |     |
| Boston |       GTB        |     R     |                |          |     |     |
| Boston |       SGD        |     R     |                |          |     |     |
| Boston |       DT         |     R     |                |          |     |     |
|  x264  |   RandomForest   |  sklearn  |                |          |     |     |
|  x264  |       SVR        |  sklearn  |                |          |     |     |
|  x264  |       GTB        |  sklearn  |                |          |     |     |
|  x264  |       SGD        |  sklearn  |                |          |     |     |
|  x264  |       DT         |  sklearn  |                |          |     |     |
|  x264  |   RandomForest   |    Weka   |                |          |     |     |
|  x264  |       SVR        |    Weka   |                |          |     |     |
|  x264  |       GTB        |    Weka   |                |          |     |     |
|  x264  |       SGD        |    Weka   |                |          |     |     |
|  x264  |       DT         |    Weka   |                |          |     |     |
|  x264  |   RandomForest   |  XGBoost  |                |          |     |     |
|  x264  |       SVR        |  XGBoost  |                |          |     |     |
|  x264  |       GTB        |  XGBoost  |                |          |     |     |
|  x264  |       SGD        |  XGBoost  |                |          |     |     |
|  x264  |       DT         |  XGBoost  |                |          |     |     |
|  x264  |   RandomForest   |     R     |                |          |     |     |
|  x264  |       SVR        |     R     |                |          |     |     |
|  x264  |       GTB        |     R     |                |          |     |     |
|  x264  |       SGD        |     R     |                |          |     |     |
|  x264  |       DT         |     R     |                |          |     |     |


The "?" algorithm tends to be faster but it's the "?" algorithm that is the most accurate
The framework ? seems to be faster in general
...
The ? dataset gives better results.
..

Given the previous results, I would recommend using ?


<h2>Implementation limits</h2>
//What can't you do? why wasn't it implmented?
