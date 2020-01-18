#!/usr/bin/env python2
from sklearn.metrics import median_absolute_error
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import cross_val_predict
from sklearn.model_selection import cross_validate
from sklearn.ensemble import GradientBoostingRegressor
import pandas as pd
import warnings
df = pd.read_csv('/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/x264-netflix.csv', sep=',')
warnings.filterwarnings("ignore")

X = df.drop(['usertime'], axis=1)
y = df['usertime']

clf = GradientBoostingRegressor()

accuracy = cross_validate(clf, X, y, cv=5)
y_pred = cross_val_predict(clf, X, y, cv=5)
y_test = y
print(mean_squared_error(y_test, y_pred))
print(mean_absolute_error(y_test, y_pred))
print(median_absolute_error(y_test, y_pred))

