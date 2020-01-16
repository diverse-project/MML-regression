#!/usr/bin/env python2
from sklearn.metrics import median_absolute_error
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import cross_val_predict
from sklearn.model_selection import cross_validate
import pandas as pd
import xgboost as xgb
import warnings
df = pd.read_csv('/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/BostonHousing.csv', sep=',')
warnings.filterwarnings("ignore")

coltokeep = [df.columns[0], df.columns[1], df.columns[2], df.columns[3], 'nox', df.columns[5], 'tax']
X = df[coltokeep]
y = df[df.columns[4]]

xgb_model = xgb.XGBRegressor()

accuracy = cross_validate(xgb_model, X, y, cv=5)
y_pred = cross_val_predict(xgb_model, X, y, cv=5)
y_test = y
print(mean_squared_error(y_test, y_pred))
print(mean_absolute_error(y_test, y_pred))
print(median_absolute_error(y_test, y_pred))

