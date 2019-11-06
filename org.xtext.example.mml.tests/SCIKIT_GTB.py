#!/usr/bin/env python2
from sklearn.metrics import median_absolute_error
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split
from sklearn.ensemble import GradientBoostingRegressor
import pandas as pd
df = pd.read_csv('/home/nico/IdeaProjects/idm_project/MML-regression/runtimeXText/boston/BostonHousing.csv', sep=',')

coltokeep = [df.columns[0], df.columns[1], df.columns[2], df.columns[3], 'nox', df.columns[9], 'tax']
X = df[coltokeep]
y = df[df.columns[4]]

clf = GradientBoostingRegressor()

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.4)
clf.fit(X_train,y_train)
y_pred = clf.predict(X_test)
print(mean_squared_error(y_test, y_pred))
print(mean_absolute_error(y_test, y_pred))
print(median_absolute_error(y_test, y_pred))

