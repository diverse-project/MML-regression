import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn import *
from sklearn.metrics import mean_absolute_error
mml_data = pd.read_csv("boston.csv")
column = mml_data.columns[-1] 
X = mml_data.drop(columns=[column]) 

y = mml_data[column] 

test_size = 0.30000000000000004 

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size) 

clf = tree.DecisionTreeRegressor() 

clf.fit(X_train, y_train) 

accuracy = mean_absolute_error(y_test, clf.predict(X_test)) 

print(accuracy) 
