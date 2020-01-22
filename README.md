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
| Boston |   RandomForest   |  XGBoost  |                |          | 3.5 |26.9 |
| Boston |       SVR        |  XGBoost  |                |          |     |     |
| Boston |       GTB        |  XGBoost  |                |          |     |     |
| Boston |       SGD        |  XGBoost  |                |          |     |     |
| Boston |       DT         |  XGBoost  |                |          |     |     |
| Boston |   RandomForest   |     R     |                |          |     |     |
| Boston |       SVR        |     R     |                |          |     |     |
| Boston |       GTB        |     R     |                |          |     |     |
| Boston |       SGD        |     R     |                |          |     |     |
| Boston |       DT         |     R     |                |          |     |     |

The "?" algorithm tends to be faster but it's the "?" algorithm that is the most accurate
The framework ? seems to be faster in general
...
The ? dataset gives better results.
..

Given the previous results, I would recommend using ?


<h2>Implementation limits</h2>
//What can't you do? why wasn't it implmented?
