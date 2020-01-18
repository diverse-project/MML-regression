import pandas as pd

import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error
from sklearn.metrics import mean_absolute_error
from sklearn.model_selection import train_test_split

mml_data = pd.read_csv("boston.csv")

column = mml_data.columns[-1]
y = mml_data[column] 

column = mml_data.columns[-1] 
X = mml_data.drop(columns=[column]) 
test_size = 0.30000000000000004
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size) 

regressor = RandomForestRegressor()
regressor.fit(X_train, y_train)
y_pred = regressor.predict(X_test)
accuracy = mean_squared_error(y_test, y_pred)
print('mean_squared_error', accuracy)
accuracy = mean_absolute_error(y_test, y_pred)
print('mean_absolute_error', accuracy)